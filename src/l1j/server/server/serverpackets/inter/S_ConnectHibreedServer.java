package l1j.server.server.serverpackets.inter;

import java.util.StringTokenizer;

import l1j.server.Config;
import l1j.server.GameSystem.inter.L1InterServerModel;
import l1j.server.server.Opcodes;
import l1j.server.server.construct.L1InterServer;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.BinaryOutputStream;
import l1j.server.server.utils.StringUtil;

public class S_ConnectHibreedServer extends ServerBasePacket {
	private static final String S_CONNECT_HIBREED_SERVER = "[S] S_ConnectHibreedServer";
	private byte[] _byte = null;
	public static final int CONNECT	= 0x0071;
	
	private static final byte[] HEAD_BYTES			= getInitBytes(true);
	private static final byte[] TAIL_BYTES			= getInitBytes(false);
	private static final byte[] LEAVE_HEAD_BYTES	= getLeaveBytes(true);
	private static final byte[] LEAVE_TAIL_BYTES	= getLeaveBytes(false);
	
	public S_ConnectHibreedServer(L1InterServerModel model) {
		boolean isLeave = model.getInter() == L1InterServer.LEAVE;
		write_init();
		writeByte(isLeave ? LEAVE_HEAD_BYTES : HEAD_BYTES);
		write_reservednumber(model.getInter().getReserverId());
		write_onetimetoken(model.getCharName().getBytes());
		write_interkind(model.getInter().getKind());
	  	writeByte(isLeave ? LEAVE_TAIL_BYTES : TAIL_BYTES);
	  	writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(CONNECT);
	}
	
	void write_reservednumber(int reservednumber) {
		writeRaw(0x18);// reservednumber
	  	writeBit(reservednumber);
	}
	
	void write_onetimetoken(byte[] onetimetoken) {
		writeRaw(0x22);// onetimetoken
	  	writeBytesWithLength(onetimetoken);
	}
	
	void write_interkind(int interkind) {
		writeRaw(0x28);// interkind
	  	writeBit(interkind);
	}
	
	static byte[] getInitBytes(boolean first){
		BinaryOutputStream os = null;
		try {
			os = new BinaryOutputStream();
			if (first) {
				int destIP = 0; 
				StringTokenizer tok = new StringTokenizer(Config.INTER.INTER_SERVER_INNER_SETTING ? Config.SERVER.LOGIN_SERVER_ADDRESS : Config.INTER.INTER_SERVER_OUTER_IP);
				for (int i = 3; i >= 0; --i) {
					int bit = i << 3;
					destIP |= (Integer.parseInt(tok.nextToken(StringUtil.PeriodString)) << bit) & (0xFF << bit);
				}
				tok = null;
				
				os.writeC(0x08);// destIP
				os.writeBit(destIP);
		  		
				os.writeC(0x10);// destPort
				os.writeBit(Config.INTER.INTER_SERVER_INNER_SETTING ?  Config.SERVER.LOGIN_SERVER_PORT : Config.INTER.INTER_SERVER_OUTER_PORT);
			} else {
				os.writeC(0x32);// domainname
				os.writeBytesWithLength(Config.INTER.INTER_SERVER_INNER_SETTING ? Config.SERVER.LOGIN_SERVER_ADDRESS.getBytes() : Config.INTER.INTER_SERVER_OUTER_IP.getBytes());
			
				// os.writeC(0x38); worldmove
				// os.writeB(false);
			}
			return os.getBytes();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
					os = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	static byte[] getLeaveBytes(boolean first){
		BinaryOutputStream os = null;
		try {
			os = new BinaryOutputStream();
			if (first) {
				int destIP = 0; 
				StringTokenizer tok = new StringTokenizer(Config.SERVER.LOGIN_SERVER_ADDRESS);
				for (int i = 3; i >= 0; --i) {
					int bit = i << 3;
					destIP |= (Integer.parseInt(tok.nextToken(StringUtil.PeriodString)) << bit) & (0xFF << bit);
				}
				tok = null;
				
				os.writeC(0x08);// destIP
				os.writeBit(destIP);
		  		
				os.writeC(0x10);// destPort
				os.writeBit(Config.SERVER.LOGIN_SERVER_PORT);
			} else {
				os.writeC(0x32);// domainname
				os.writeBytesWithLength(Config.SERVER.LOGIN_SERVER_ADDRESS.getBytes());
			
				// os.writeC(0x38); worldmove
				// os.writeB(false);
			}
			return os.getBytes();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
					os = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public static void init(){}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_CONNECT_HIBREED_SERVER;
	}
}
