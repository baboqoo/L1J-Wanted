package l1j.server.server.clientpackets.proto;

import java.util.logging.Logger;

import l1j.server.server.GameClient;
import l1j.server.server.clientpackets.Authorization;
import l1j.server.server.controller.LoginController;
import l1j.server.server.serverpackets.S_MoveServerAuthError;
import l1j.server.web.http.connector.HttpLoginSession;

public class A_EntranceEnterRequest extends ProtoHandler {
	private static Logger _log = Logger.getLogger(A_EntranceEnterRequest.class.getName());
	protected A_EntranceEnterRequest(){}
	private A_EntranceEnterRequest(byte[] data, GameClient client) {
		super(data, client);
	}
	
	@Override
	protected void doWork() throws Exception {
		if (_client == null) {
			return;
		}
		LoginController.getInstance().logout(_client);
		HttpLoginSession session	= _client.getLoginSession();// 보관중인 로그인 세션 정보
		if (session == null) {
			_client.sendPacket(S_MoveServerAuthError.INVALID_SERVER);
			_client.kick();
			return;
		}
		
		String accountName		= session.getAccount();
		String password			= session.getPassword();
		_log.finest("Request entrance enter from user : " + accountName);
		Authorization.getInstance().auth(_client, accountName, password, _client.getIp(), _client.getHostname());
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_EntranceEnterRequest(data, client);
	}
}

