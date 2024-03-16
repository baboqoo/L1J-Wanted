package l1j.server.server.model.npc.action.id;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.utils.StringUtil;

public class RiftWizardAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new RiftWizardAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private RiftWizardAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return riftWizard(pc, s);
	}
	
	private String riftWizard(L1PcInstance pc, String s) {
		L1Location loc = null;
		if (s.matches("21|22|23|24|25|26|27|28|29|30")) {
			loc = L1Location.randomLocation(pc.getMap());
			tel(pc, loc);
		} else if (s.equalsIgnoreCase("14")) {
			if (pc.getInventory().consumeItem(L1ItemId.ADENA, 20000)) {
				loc = new L1Location(32637, 32812, 12862).randomLocation(5, true);
				tel(pc, loc);
			} else {
				pc.sendPackets(L1ServerMessage.sm189); // \f1%0이 부족합니다.
			}
		} else if (s.equalsIgnoreCase("15")) {
			if (pc.getInventory().consumeItem(L1ItemId.ADENA, 50000)) {
				loc = new L1Location(32798, 32964, 12862).randomLocation(5, true);
				tel(pc, loc);
			} else {
				pc.sendPackets(L1ServerMessage.sm189); // \f1%0이 부족합니다.
			}
		} else if (s.equalsIgnoreCase("a")) {// 시작지점
			if (pc.getInventory().consumeItem(L1ItemId.ADENA, 20000)) {
				loc = new L1Location(32627, 32926, 1209).randomLocation(5, true);
				tel(pc, loc);
			} else {
				pc.sendPackets(L1ServerMessage.sm189); // \f1%0이 부족합니다.
			}
		} else if (s.equalsIgnoreCase("b")) {// 중간지점
			if (pc.getInventory().consumeItem(L1ItemId.ADENA, 50000)) {
				loc = new L1Location(32665, 32935, 1209).randomLocation(5, true);
				tel(pc, loc);
			} else {
				pc.sendPackets(L1ServerMessage.sm189); // \f1%0이 부족합니다.
			}
		}
		loc = null;
		return StringUtil.EmptyString;
	}
	
	void tel(L1PcInstance pc, L1Location loc) {
		pc.getSkill().setSkillEffect(L1SkillId.ABSOLUTE_BARRIER, 3000);
		pc.getTeleport().start(loc, pc.getMoveState().getHeading(), true);
	}
	
}

