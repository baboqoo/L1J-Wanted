package l1j.server.server.clientpackets;

import java.util.logging.Logger;

import l1j.server.server.GameClient;
import l1j.server.server.controller.LoginController;

public class C_ReturnToLogin extends ClientBasePacket {

	private static final String C_RETURN_TO_LOGIN = "[C] C_ReturnToLogin";
	private static Logger _log = Logger.getLogger(C_ReturnToLogin.class.getName());

	public C_ReturnToLogin(byte decrypt[], GameClient client) throws Exception {
		super(decrypt);
		try {
			String account = client.getAccountName();
			StringBuilder sb = new StringBuilder();
			_log.finest(sb.append("account : ").append(account).toString());
			sb = null;
			if (client.getActiveChar() != null) {
				client.kick();
				client.close();
				System.out.println("─────────────────────────────────");
				//System.out.println("버그의심됨 -- 계정명 " + client.getAccountName());
				System.out.println("Suspected bug -- Account name " + client.getAccountName());
				System.out.println("─────────────────────────────────");
				return;
			}
			LoginController.getInstance().logout(client);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			clear();
		}
	}

	@Override
	public String getType() {
		return C_RETURN_TO_LOGIN;
	}

}

