package l1j.server.server.model.npc.action.id;

import l1j.server.Config;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class ExpgiveAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new ExpgiveAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private ExpgiveAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return expNpc(pc, s);
	}
	
	private String expNpc(L1PcInstance pc, String s){
		if (s.equalsIgnoreCase("0")) {
			if (pc.getLevel() < 51) {
				pc.addExp((ExpTable.getExpByLevel(51) - 1) - pc.getExp() + ((ExpTable.getExpByLevel(51) - 1) / 100));
			} else if (pc.getLevel() >= 51 && pc.getLevel() < Config.ALT.EXPERIENCE_RETURN_MAX_LEVEL) {
				pc.addExp((ExpTable.getExpByLevel(pc.getLevel() + 1) - 1) - pc.getExp() + 100);
				pc.setCurrentHp(pc.getMaxHp());
				pc.setCurrentMp(pc.getMaxMp());
			}
			if (ExpTable.getLevelByExp(pc.getExp()) >= Config.ALT.EXPERIENCE_RETURN_MAX_LEVEL) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("\\aA알림: 경험치 지급은 \\aG" + Config.ALT.EXPERIENCE_RETURN_MAX_LEVEL + "\\aA 까지 가능합니다"), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1131) + Config.ALT.EXPERIENCE_RETURN_MAX_LEVEL  + S_SystemMessage.getRefText(1132), true), true);
				return "expgive3";
			}
			return "expgive";
		}
		return null;
	}
}


