package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.S_FightBoard;
import l1j.server.server.serverpackets.S_RaceBoard;

public class MaenoAction implements L1NpcIdAction {// 버경 승률처리 start
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new MaenoAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private MaenoAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		if (s.equals("status")) {
			pc.sendPackets(npcId == 170041 ? new S_FightBoard(1) : new S_RaceBoard(1), true);
		}
		return null;
	}
}

