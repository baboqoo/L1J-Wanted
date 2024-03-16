package l1j.server.server;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.clientpackets.proto.A_ClientOptionChangeNoti;
import l1j.server.server.clientpackets.proto.ProtoPacketExecutor;
import l1j.server.server.construct.L1InterServer;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.controller.LoginController;
import l1j.server.server.datatables.IpTable;
import l1j.server.server.datatables.IpTable.BanIpReason;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.shop.L1PersonalShop;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.types.UChar8;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.FormatterUtil;
import l1j.server.server.utils.HexHelper;
import l1j.server.server.utils.StringUtil;
import l1j.server.server.utils.SystemUtil;
import l1j.server.web.dispatcher.response.account.LoginFactory;
import l1j.server.web.http.connector.HttpAccountManager;
import l1j.server.web.http.connector.HttpLoginSession;
import xnetwork.Connection;
import xnetwork.ConnectionHandler;
import xnetwork.SelectorThread;

public class GameClient implements ConnectionHandler, PacketOutput {
	private static Logger _log = Logger.getLogger(GameClient.class.getName());
	
	private PacketExecutor _executor;
	private ProtoPacketExecutor _proto;
	private Account _account;
	private L1PcInstance _activeChar;
	private String _ip;
	private int _ipBigEndian;
	private String _hostname;
	private Connection _connection;
	private ClientThreadObserver _observer;
	private DollObserver _dollobserver;
	private int _loginStatus;
	public byte[] loginInfoToken;
	private long _version;
	
	protected GameClient() {}
	
	public GameClient(SocketChannel socketChannel, SelectorThread selector) throws IOException {
		Socket socket = socketChannel.socket();
		if (socket.getPort() != 0) {
			_ip			= socket.getInetAddress().getHostAddress();
			setIpBigEndian();
			_hostname	= Config.SERVER.HOSTNAME_LOOKUPS ? socket.getInetAddress().getHostName() : _ip;
			_executor	= new PacketExecutor();// PacketHandler 생성
			start(socketChannel, selector);
		}
	}
	
	protected void start(SocketChannel socketChannel, SelectorThread selector) throws IOException {
		System.out.println(String.format("[Establishing Connection] [%s] IP : %s Memory : %d MB", FormatterUtil.get_formatter_time(), _ip, SystemUtil.getUsedMemoryMB()));
		_connection		= new Connection(socketChannel, selector, this);
		if (Config.SERVER.AUTOMATIC_KICK > 0) {
			_observer	= new ClientThreadObserver(Config.SERVER.AUTOMATIC_KICK * 60000);// 자동절단까지의시간(단위:ms)
			_observer.start();// 클라이언트 thread의 감시
		}
		_dollobserver	= new DollObserver(15000);// 마법인형 15초 액션
		_dollobserver.start();
		
		_connection.send(Config.VERSION.FIRST_KEY);
		_cryption.initKeys(Config.VERSION.FIRST_KEY_SEED);
		_connection.resumeRecv();
	}
	
	public ProtoPacketExecutor getProto(){
		return _proto;
	}
	
	public void setIp(String ip) {
		_ip = ip;
	}
	public String getIp() {
		return _ip;
	}

	public String getHostname() {
		return _hostname;
	}
	
	public int getIpBigEndian() {
		return _ipBigEndian;
	}
	
	private void setIpBigEndian() {
		if(StringUtil.isNullOrEmpty(_ip)) {
			return;
		}
		int bigendian = 0;
		StringTokenizer tok = new StringTokenizer(getIp());
		for (int i=3; i>=0; --i) {
			int bit = i << 3;
			bigendian |= (Integer.parseInt(tok.nextToken(".")) << bit) &  (0xff << bit);
		}
		_ipBigEndian = bigendian;
	}
	
	// ClientThread에 의한 일정 간격 자동 세이브를 제한하기 때문에(위해)의 플래그(true:제한 false:제한 없음)
	// 현재는 C_LoginToServer가 실행되었을 때에 false가 되어,
	// C_NewCharSelect가 실행되었을 때에 true가 된다
	private boolean _charRestart = true;
	public boolean isCharRestart() {
		return _charRestart;
	}
	public void setCharReStart(boolean flag) {
		_charRestart = flag;
	}
	
	private l1j.server.server.encryptions.lin380.LineageEncryption _cryption = new l1j.server.server.encryptions.lin380.LineageEncryption();
	
	private int _kick = 0;
	public void kick() {
		if (_kick == 0) {
			sendPacket(S_Disconnect.DISCONNECT);
			_kick = 1;
			_connection.close();
		}
	}
	
	class PacketExecutor implements Runnable {
		private final Queue<byte[]> _queue;
		private AtomicInteger _jobCount;
		private PacketHandler _handler;

		PacketExecutor() {
			_queue				= new ConcurrentLinkedQueue<byte[]>();
			_handler			= new PacketHandler(GameClient.this);
			_jobCount			= new AtomicInteger(0);
			_proto				= new ProtoPacketExecutor(GameClient.this);
		}

		/**
		 * C_Packet in
		 * @param data
		 * @param opcode
		 */
		private void requestWork(byte data[], int opcode) {
			_queue.offer(data);
			if (_jobCount.getAndIncrement() == 0) {
				GeneralThreadPool.getInstance().execute(this);
			}
		}

		@Override
		public void run(){
			while(_connection.isOpen()){
				boolean needToBreak = false;
				try {
					synchronized (this) {
						byte[] data = _queue.peek();
						if (data == null) {
							break;
						}
						int opcode = data[0] & 0xFF;
						if (Config.SERVER.OPCODES_PRINT_C) {
							if (Config.SERVER.OPCODES_ONLY_HEADER) { 
								if (opcode == 157 || opcode == 19)
									System.out.println(String.format("[C_OPCODE] = %d [LENGTH] = %d [NAME] = %s\n%s", opcode, data.length, Opcodes.get_c_opcode_name(opcode), HexHelper.DataToPacket(data, data.length)));
								else
									System.out.println(String.format("[C_OPCODE] = %d [LENGTH] = %d [NAME] = %s", opcode, data.length, Opcodes.get_c_opcode_name(opcode)));
							} else
								System.out.println(String.format("[C_OPCODE] = %d [LENGTH] = %d [NAME] = %s\n%s", opcode, data.length, Opcodes.get_c_opcode_name(opcode), HexHelper.DataToPacket(data, data.length)));
						}
						needToBreak = false;
						_queue.remove();
						if (_jobCount.decrementAndGet() == 0) {
							needToBreak = true;
						}
						_handler.handlePacket(data, opcode);// clientpacket send
						if (needToBreak) {
							break;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					if (needToBreak) {
						break;
					}
				}
			}
		}
	}
	
	// 클라이언트 thread의 감시 타이머
	class ClientThreadObserver extends RepeatTask {
		private int _checkct = 1;

		ClientThreadObserver(int interval) {
			super(interval);
		}

		public void start() {
			GeneralThreadPool.getInstance().schedule(ClientThreadObserver.this, getInterval());
		}

		@Override
		public void execute() {
			try {
				if (!_connection.isOpen()) {
					cancel();
					return;
				}
				if (_version != Config.VERSION.CLIENT_VERSION && _activeChar != null) {
					notEqualsVersion();
				}
				if (_checkct > 0) {
					_checkct = 0;
					return;
				}
				if (_activeChar == null) {// 캐릭터 선택전
					kick();
					//_log.warning(String.format("일정시간 응답을 얻을 수 없었기 때문에(%s)과(와)의 접속을 강제 절단 했습니다.", _ip));
					_log.warning(String.format("The connection with (%s) was forcibly disconnected because a response could not be obtained for a certain period of time.", _ip));
					cancel();
					return;
				}
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				cancel();
			}
		}
		
		public void packetReceived() {
			_checkct++;
		}
		
		/**
		 * 린빈 버전 체크
		 */
		private void notEqualsVersion(){
			_activeChar.sendPackets(L1ServerMessage.sm3362);
			_activeChar.sendPackets(L1SystemMessage.NOT_EQUALS_VERSION);
			if (_activeChar.isGm()) {
				//_activeChar.sendPackets(new S_SystemMessage(String.format( "클라이언트 버전: %d, 접속 린빈 버전: %d", _version, Config.VERSION.CLIENT_VERSION )), true);
				_activeChar.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(11), String.valueOf(_version), String.valueOf(Config.VERSION.CLIENT_VERSION)), true);
			}
		}
	}
	
	// 마법인형 15초 액션
	class DollObserver extends RepeatTask {
		private int _checkct = 1;

		DollObserver(int interval) {
			super(interval);
		}

		public void start() {
			GeneralThreadPool.getInstance().schedule(DollObserver.this, 0);
		}

		@Override
		public void execute() {
			try {
				if (!_connection.isOpen()) {
					cancel();
					return;
				}
				if (_checkct > 0) {
					_checkct = 0;
					return;
				}
				
				if (_activeChar == null || _activeChar.getDoll() == null) {
					return;
				}
				_activeChar.getDoll().broadcastPacket(new S_DoActionGFX(_activeChar.getDoll().getId(), 66 + CommonUtil.random(2)), true);
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				cancel();
			}
		}
		
		public void packetReceived() {
			_checkct++;
		}
	}
	
	private Object sync = new Object();
	
	@Override
	public void sendPacket(ServerBasePacket packet) {
		synchronized (sync) {
			try {
				byte[] abyte0 = packet.getContent();
				if (abyte0 == null || abyte0.length < 1) {
					return;
				}
				//TODO 서버패킷 출력
				if (Config.SERVER.OPCODES_PRINT_S) {
					int opcode = abyte0[0] & 0xFF;
					if (Config.SERVER.OPCODES_ONLY_HEADER) {
  						if (opcode == 157 || opcode == 19)
							System.out.println(String.format("[S_OPCODE] = %d [LENGTH] = %d [NAME] = %s\n%s", opcode, abyte0.length, Opcodes.get_s_opcode_name(opcode), HexHelper.DataToPacket(abyte0, abyte0.length)));
						else
							System.out.println(String.format("[S_OPCODE] = %d [LENGTH] = %d [NAME] = %s", opcode, abyte0.length, Opcodes.get_s_opcode_name(opcode)));
					} else
						System.out.println(String.format("[S_OPCODE] = %d [LENGTH] = %d [NAME] = %s\n%s", opcode, abyte0.length, Opcodes.get_s_opcode_name(opcode), HexHelper.DataToPacket(abyte0, abyte0.length)));
				}
				char[] data1	= _cryption.fromArray(abyte0);
				data1			= _cryption.encrypt_S(data1);
				abyte0			= _cryption.fromArray(data1);
				int j			= abyte0.length + 2;
				byte[] buffer	= new byte[j];
				buffer[0]		= (byte) (j & 0xff);
				buffer[1]		= (byte) (j >> 8 & 0xff);
				System.arraycopy(abyte0, 0, buffer, 2, abyte0.length);
				_connection.send(buffer);
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
	}
	
	public void packetwaitgo(byte[] bb) {
		if (bb == null) {
			return;
		}
		try {
			onPacket(bb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		_connection.close();// onDisconnect 호출됨
	}
	public boolean isConnected() {
		return _connection.isOpen();
	}

	public void setActiveChar(L1PcInstance pc) {
		_activeChar = pc;
	}
	public L1PcInstance getActiveChar() {
		return _activeChar;
	}

	public void setAccount(Account account) {
		_account = account;
	}
	public Account getAccount() {
		return _account;
	}

	public String getAccountName() {
		return _account == null ? null : _account.getName();
	}
	
	/**
	 * 활성화 계정 종료
	 */
	public void accountDisconnect() {
		synchronized (_account) {
			_account.updateQuit();
		}
	}
	
	/**
	 * 활성화 캐릭터 종료
	 */
	public void characterDisconnect() {
		synchronized (_activeChar) {
			if (_activeChar.isPrivateShop()) {
				L1PersonalShop.delete(_activeChar);// 클라이언트 종료시 상점아이템 삭제
			}
			_activeChar.logout();
		}
	}
	
	@Override
	public void onDisconnect(Connection connection) {// 소켓 종료
		System.out.println(String.format("[Connection Closed] [%s] IP : %s", FormatterUtil.get_formatter_time(), _ip));
		try {
			if (_account != null) {
				accountDisconnect();
			}
			if (_activeChar != null) {
				characterDisconnect();
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			LoginController.getInstance().logout(this);
			if (_loginSession != null) {
				LoginFactory.remove_ingame(_loginSession);
			}
			setActiveChar(null);
			_proto.dispose();
			HttpAccountManager.put(_loginSession);
			setLoginSession(null);
		}
	}
	
	@Override
	public void onRecv(Connection connection, ByteBuffer buffer) {
		//TODO 클라이언트 패킷 수신
		try {
			while(true){
				buffer.mark();
				if (buffer.remaining() < 2) { // size 2byte
					buffer.reset();
					break;
				}

				int first		= UChar8.fromUByte8(buffer.get());// first size;
				int second		= UChar8.fromUByte8(buffer.get());// second size;
				int dataLength	= ((second << 0x00000008) + first) + ~0x00000001;
				if (isBadPacket(dataLength)) {
					kickBadPacket(dataLength);
					return;
				}
				overPending = 0;
				if (buffer.remaining() < dataLength) {
					buffer.reset();
					break;
				}
				byte data[] = new byte[dataLength];
				buffer.get(data, 0, dataLength);
				data = _cryption.decrypt_S(data);// 복호화
				onPacket(data);
			}
		} catch (Exception e) {
			connection.close();
		}
	}
	
	private int overPending;
	private boolean isBadPacket(int length){
		return length > Config.SERVER.RECEIVE_PACKET_LIMIT_SIZE && ++overPending >= Config.SERVER.RECEIVE_PACKET_OVER_COUNT;
	}
	private void kickBadPacket(int length){
		//String message = String.format("[ClientPacket] 패킷공격(의심)을 차단하였습니다.\r\nIP(%s), SIZE(%d)", _ip, length);
		String message = String.format("[ClientPacket] Packet attack (suspicious) has been blocked.\r\nIP(%s), SIZE(%d)", _ip, length);
		System.out.println(message);
		for (L1PcInstance gm : L1World.getInstance().getAllGms()) {
			gm.sendPackets(new S_SystemMessage(message), true);
		}
		IpTable.getInstance().insert(_ip, BanIpReason.PACKET_ATTACK);
		this.close();
	}
	
	@Override
	public void onSend(Connection connection) {
		// do nothing
	}
	
	public void setLoginAvailable() {
		_loginStatus = 1;
	}

	private void onPacket(byte[] data) {
		int opcode = data[0] & 0xFF;
		// 오픈 대기 검증 차단 패킷
		if (Config.SERVER.STANDBY_SERVER && Opcodes.isStanbyBlockCode(opcode)) {
			if (_activeChar != null && _activeChar.getNetConnection() != null) {
				_activeChar.sendPackets(L1SystemMessage.STANBY_FAIL_MSG);
			}
			return;
		}
		// 인터서버 접속 중 검증 차단 패킷
		if (isInterServerLock && Opcodes.isInterConnectBlockCode(opcode)) {
			return;
		}
		switch (opcode) {
		case Opcodes.C_MOVE:
			if (_activeChar == null || _activeChar.isNotEnableMove()) {
				return;
			}
			_dollobserver.packetReceived();// 마법인형 15초 액션
			break;
		case Opcodes.C_USE_SPELL:
			if (_activeChar == null || _activeChar.isNotEnableUseSpell()) {
				return;
			}
			break;
		case Opcodes.C_ATTACK:
		case Opcodes.C_ATTACK_CONTINUE:
			if (_activeChar == null || _activeChar.isNotEnableAttack()) {
				return;
			}
			break;
		case Opcodes.C_ENTER_WORLD:
			if (_loginStatus != 1){
				return;
			}
			if (data.length > 16 || data.length < 4) {
				connectBug();
				return;
			}
			break;
		case Opcodes.C_READ_NEWS:
		case Opcodes.C_RESTART:
			_loginStatus = 1;
			break;
		case Opcodes.C_ONOFF:
			_loginStatus = 0;
			break;
		}
		
		if (opcode != Opcodes.C_ALIVE) {// C_KEEPALIVE 이외의 뭔가의 패킷을 받으면(자) Observer에 통지
			_observer.packetReceived();
		}
		_executor.requestWork(data, opcode);
	}
	
	private void connectBug(){
		//System.out.println(String.format("케릭터 접속버그를 유도하고 있습니다!\n버그IP(%s)의 접속을 끊습니다!", getIp()));
		System.out.println(String.format("You are causing a character connection bug!\nDisconnecting the bug IP (%s)!", getIp()));
		kick();
	}
	
	/** 접속정보 **/
	private HttpLoginSession _loginSession;
	public HttpLoginSession getLoginSession(){
		return _loginSession;
	}
	public void setLoginSession(HttpLoginSession session){
		_loginSession = session;
	}
	
	private byte loginCount = 0;
	public void addLoginCount(){
		loginCount++;
		if (loginCount >= 3) {
			close();
		}
	}
	
	public long getVersion(){
		return _version;
	}
	public void setVersion(long version){
		_version = version;
	}
	
	private boolean isInterServerLock;// 인터서버 입장 중
	public void onInterServerLock(){
		isInterServerLock = true;
	}
	
	private boolean interServer;
	public boolean isInterServer() {
		return interServer;
	}
	public void setInterServer(boolean Inter) {
		interServer = Inter;
	}
	
	private L1InterServer inter;
	public L1InterServer getInter(){
		return inter;
	}
	public void setInter(L1InterServer Inter){
		inter = Inter;
	}

	private boolean isEnterReady;
	public void setEnterReady(boolean enterReady) {
		isEnterReady = enterReady;
	}
	public boolean isEnterReady() {
		return isEnterReady;
	}
	
	public Runnable accessStanbyCallBack;
	public int accessStanbyCount;
	public void accessStanbyRelease(){
		accessStanbyCount		= 0;
		accessStanbyCallBack	= null;
	}
	
	public void releaseInter(){
		interServer		= false;
		inter			= null;
	}
	
	private long restartMillis;
	public long getRestartMillis() {
		return restartMillis;
	}
	public void setRestartMillis(long restartMillis) {
		this.restartMillis = restartMillis;
	}
	
	private A_ClientOptionChangeNoti.SoundOption sound_option;
	public A_ClientOptionChangeNoti.SoundOption get_sound_option() {
		return sound_option;
	}
	public void set_sound_option(A_ClientOptionChangeNoti.SoundOption val) {
		sound_option = val;
	}
	
}

