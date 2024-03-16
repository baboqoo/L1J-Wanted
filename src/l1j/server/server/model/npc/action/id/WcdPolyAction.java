package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.object.S_PCObject;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.utils.StringUtil;

public class WcdPolyAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new WcdPolyAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private WcdPolyAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return minJ(pc, s);
	}
	
	private String minJ(L1PcInstance pc, String s) {
		if (s.equalsIgnoreCase("x")){
			if (!pc.getInventory().checkItem(L1ItemId.ADENA, 50000)) {
				return "wcd_poly3";
			}
			pc.getInventory().consumeItem(L1ItemId.ADENA, 50000);
			pc.getSkill().setSkillEffect(L1SkillId.ANONYMITY_POLY, 1800000);
			pc.getResistance().addHitupAll(5);
			pc.getAbility().addPVPDamageReduction(5);
			pc.sendPackets(new S_SpellBuffNoti(pc, L1SkillId.ANONYMITY_POLY, true, 1800), true);
			pc.broadcastPacket(new S_PCObject(pc), true);
			return "wcd_poly2";
		}
		return StringUtil.EmptyString;
	}
}

