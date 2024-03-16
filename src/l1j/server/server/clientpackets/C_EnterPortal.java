package l1j.server.server.clientpackets;

import l1j.server.GameSystem.inn.InnTeleport;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.Dungeon;
import l1j.server.server.model.Instance.L1PcInstance;

public class C_EnterPortal extends ClientBasePacket {
	private static final String C_ENTER_PORTAL = "[C] C_EnterPortal";
	public C_EnterPortal(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);
		L1PcInstance pc = client.getActiveChar();
	    if (pc == null || pc.getTeleport().isTeleport()) {
	    	return;
	    }
		try {
		    int locx = readH();
		    int locy = readH();

		    // 던전 이동 텔레포트
		    String key = new StringBuilder().append(pc.getMap().getId()).append(locx).append(locy).toString();
		    if (Dungeon.dg(key, pc)) {
		    	key = null;
				return;
		    }
		    key = null;
	    	int xdis = Math.abs(pc.getX() - locx);
			int ydis = Math.abs(pc.getY() - locy);
			if (ydis > 3 || xdis > 3) {
				pc.sendPackets(L1ServerMessage.sm626);// 포탈 이동을 하려면 좀 더 가까워야 합니다.
				return;
			}
			if (InnTeleport.checkInn(pc, locx, locy)) {// 여관이동
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getType() {
		return C_ENTER_PORTAL;
	}
}

