package l1j.server.server.clientpackets.proto;

import java.util.logging.Logger;

import l1j.server.Base64;
import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.clientpackets.Authorization;
import l1j.server.server.datatables.BanAccountTable;
import l1j.server.server.datatables.BanAccountTable.BanAccount;
import l1j.server.server.serverpackets.message.S_CommonNews;
import l1j.server.server.utils.CharsetUtil;
import l1j.server.server.utils.DelayClose;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.http.connector.HttpAccountManager;
import l1j.server.web.http.connector.HttpLoginSession;

public class A_NpLogin extends ProtoHandler {
	private static Logger _log = Logger.getLogger(A_NpLogin.class.getName());
	protected A_NpLogin(){}
	private A_NpLogin(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		readP(1);			// 0x08
		readBit();			// IP
		
		readP(1);			// 0x10
		readBit();			// OTP
		
		readP(1);			// 0x1a
		int sublength		= readC();
		readP(sublength);	// AUTH NP
		
		readP(1);			// 0x22
		int auth_length		= readBit();// account length
		byte[] auth_bytes	= readByte(auth_length);
		
		readP(1);			// 0x2a
		int maclength		= readC();
		readP(maclength);	// MAC_HASH
		
		String authToken;
		try {
			authToken	= Base64.decryptToKey(new String(auth_bytes, CharsetUtil.UTF_8), Config.LAUNCHER.CONNECTOR_ENCRYPT_KEY);
		} catch(Exception e) {
			//System.out.println(String.format("[A_NpLogin] 승인토큰 복호화 실패 : IP(%s)\r\nINFO =>\r\n%s", _client.getIp(), toString()));
			System.out.println(String.format("[A_NpLogin] Authorization token decryption failed: IP(%s)\r\nINFO =>\r\n%s", _client.getIp(), toString()));
			_client.sendPacket(S_CommonNews.OTHER_CONNECTOR);
			GeneralThreadPool.getInstance().schedule(new DelayClose(_client), 1500L);
			return;
		}
		
		// 커넥터 로그인 세션 취득
		HttpLoginSession session = HttpAccountManager.get(authToken);
		if (session == null) {
			_client.sendPacket(S_CommonNews.SESSOION_EMPTY);
			GeneralThreadPool.getInstance().schedule(new DelayClose(_client), 1500L);
			return;
		}
		
		BanAccount ban = BanAccountTable.getBan(session.getAccount());
		if (ban != null) {
			S_CommonNews message = new S_CommonNews(String.format(
					//"\r\n\r\n※※※ 서비스 이용 제한 안내 ※※※\r\n\r\n안녕하세요 '%s' 입니다.\r\n\r\n해당 고객님께선 이하\r\n'%s' 사유로\r\n%s까지 서비스 이용이 제한되었습니다.\r\n\r\n올바른 서버이용을 부탁드립니다.\r\n감사합니다.",
					"\r\n\r\n※※※ Service Restriction Notice ※※※\r\n\r\nHello, '%s'.\r\n\r\nWe regret to inform you that your service access has been restricted until %s due to the following reasons:\r\n'%s'.\r\n\r\nWe kindly request your cooperation for proper server usage.\r\nThank you.",
					Config.WEB.WEB_SERVER_ENABLE, ban.getReason().getReason(), StringUtil.getFormatDate(ban.getLimitTime())
					));
			_client.sendPacket(message);
			message.clear();
			message = null;
			GeneralThreadPool.getInstance().schedule(new DelayClose(_client), 1500L);
			return;
		}
		
		_log.finest(String.format("Request NP Login from user : %s", session.getAccount()));
		_client.setLoginSession(session);
		Authorization.getInstance().auth(_client, session.getAccount(), session.getPassword(), _client.getIp(), _client.getHostname());
		if (_client.getAccount() != null) {
			_client.getAccount().setLoginSession(session);
		}
		_client.loginInfoToken = auth_bytes;
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_NpLogin(data, client);
	}

}

