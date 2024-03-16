package l1j.server.server.model.npc.action.id;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.utils.StringUtil;

public class RuunCastleAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new RuunCastleAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private RuunCastleAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return ruunDungeon(pc, s);
	}
	
	private String ruunDungeon(L1PcInstance pc, String s) {
		try {
			int consumeItemId = L1ItemId.ADENA, consumeCount = 200000;
			if (s.equalsIgnoreCase("b")) {// 입장권
				consumeItemId = 94000;
				consumeCount = 1;
			}
			if (pc._isRuunPaper) {
				consumeItemId = consumeCount = 0;// 초대권
			}
			if (consumeItemId > 0 && !pc.getInventory().consumeItem(consumeItemId, consumeCount)) {
				pc.sendPackets((consumeItemId == L1ItemId.ADENA) ? L1ServerMessage.sm189 : L1ServerMessage.sm337_RUUN); // \f1%0이 부족합니다.
				return StringUtil.EmptyString;
			}
			L1Location loc = new L1Location(32732, 32764, 4000).randomLocation(3, true);
			pc.getTeleport().start(loc, 5, true);
			pc.getSkill().setSkillEffect(L1SkillId.ABSOLUTE_BARRIER, 3000);
			loc = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return StringUtil.EmptyString;
	}
}

