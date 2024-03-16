package l1j.server.server.clientpackets.proto;

import java.util.logging.Logger;

import l1j.server.server.GameClient;
import l1j.server.server.clientpackets.Authorization;
import l1j.server.server.controller.LoginController;
import l1j.server.server.serverpackets.S_MoveServerAuthError;
import l1j.server.web.http.connector.HttpLoginSession;

public class A_MoveServerAuth extends ProtoHandler {
	private static Logger _log = Logger.getLogger(A_MoveServerAuth.class.getName());
	protected A_MoveServerAuth(){}
	private A_MoveServerAuth(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int dest_serverno;
	public int get_dest_serverno() {
		return dest_serverno;
	}

	@Override
	protected void doWork() throws Exception {
		readP(1);// 0x08
		dest_serverno = readC();
		
		if (_client.getAccount() != null) {
			_client.accountDisconnect();
		}
		LoginController.getInstance().logout(_client);
		
		HttpLoginSession session	= _client.getLoginSession();// 보관중인 로그인 세션
		if (session == null) {
			_client.sendPacket(S_MoveServerAuthError.INVALID_SERVER);
			_client.close();
			return;
		}
		
		String accountName		= session.getAccount();
		String password			= session.getPassword();
		_log.finest(String.format("Request A_MoveServerAuth from user : %s", accountName));
		Authorization.getInstance().auth(_client, accountName, password, _client.getIp(), _client.getHostname());
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_MoveServerAuth(data, client);
	}

}

