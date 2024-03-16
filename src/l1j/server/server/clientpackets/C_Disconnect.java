package l1j.server.server.clientpackets;

import java.util.logging.Logger;

import l1j.server.server.GameClient;
import l1j.server.server.model.Instance.L1PcInstance;

public class C_Disconnect extends ClientBasePacket {
	private static final String C_DISCONNECT = "[C] C_Disconnect";
	private static Logger _log = Logger.getLogger(C_Disconnect.class.getName());

	public C_Disconnect(byte[] decrypt, GameClient client) {
		super(decrypt);
		L1PcInstance pc = client.getActiveChar();
		if (pc != null) {
			_log.fine("Disconnect from: " + pc.getName());
			if (client.isInterServer()) {
				client.releaseInter();
			}
			pc.logout();
			client.setActiveChar(null);
		} else {
			_log.fine("Disconnect Request from Account : " + client.getAccountName());
		}
	}

	@Override
	public String getType() {
		return C_DISCONNECT;
	}
}

