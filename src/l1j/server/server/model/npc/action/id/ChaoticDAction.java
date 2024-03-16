package l1j.server.server.model.npc.action.id;

import l1j.server.server.GameServerSetting;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.utils.StringUtil;

public class ChaoticDAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new ChaoticDAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private ChaoticDAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return blackDragonDungeonEnter(pc, s);
	}
	
	private String blackDragonDungeonEnter(L1PcInstance pc, String s) {
		try {
			int x = 32740, y = 32804, mapid = 0, adena = 0;
			if (s.equalsIgnoreCase("chaoticD")) {
				if (pc.getLevel() >= 80 && GameServerSetting.BLACK_DRAGON && pc.getInventory().checkItem(L1ItemId.ADENA, 30000)) {
					mapid = 1318;
					adena = 30000;
				}
			}
			if (mapid != 0) {
				if (adena > 0) {
					pc.getInventory().consumeItem(L1ItemId.ADENA, adena);
				}
				L1Location loc = new L1Location(x, y, mapid).randomLocation(5, true);
				pc.getTeleport().start(loc, 5, true);
				loc = null;
				pc.getSkill().setSkillEffect(L1SkillId.ABSOLUTE_BARRIER, 3000);
			} else {
				pc.getConfig().finishPlaySupport();
				return "chaoticDf";
			}
		} catch (Exception e) {
		}
		return StringUtil.EmptyString;
	}
}

