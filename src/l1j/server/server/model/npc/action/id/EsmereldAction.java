package l1j.server.server.model.npc.action.id;

import l1j.server.server.construct.L1TeleportInfo;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;

public class EsmereldAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new EsmereldAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private EsmereldAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		esmerald(pc, s);
		return null;
	}
	
	private void esmerald(L1PcInstance pc, String s) {
		if (s.equalsIgnoreCase("journey")) {// 미래를 본다
			ghostTeleport(pc, L1TeleportInfo.GHOST_LOC[0][pc._ghostCount], L1TeleportInfo.GHOST_LOC[1][pc._ghostCount], (short)L1TeleportInfo.GHOST_LOC[2][pc._ghostCount], 10);
		}
	}
	
	private void ghostTeleport(L1PcInstance pc, int x, int y, short m, int second) {
		try {
			pc.save();
			pc.beginGhost(x, y, (short) m, false, second); //30초
		} catch(Exception e) {}
	}
}

