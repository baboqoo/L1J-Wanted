package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.utils.DelayClose;

public class C_Quit extends ClientBasePacket{
	public C_Quit(byte abyte0[], GameClient client) {
		super(abyte0);
		try {
			GeneralThreadPool.getInstance().schedule(new DelayClose(client), 500L);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			clear();
		}
	}
}

