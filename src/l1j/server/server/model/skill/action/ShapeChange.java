package l1j.server.server.model.skill.action;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.skill.L1SkillInfo;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.message.S_MessageYN;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.utils.CommonUtil;

public class ShapeChange extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (attacker == null) {
			return 0;
		}
		if (attacker instanceof L1PcInstance && attacker.isInvisble()) {
			((L1PcInstance)attacker).delInvis();
		}
		int polyId = CommonUtil.randomIntChoice(L1SkillInfo.SHAPE_CHANGE_POLY_ARRAY);
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc._isArmorSetPoly) {
				return 0;
			}
			if (attacker.getId() != pc.getId() && (pc.getSkill().hasSkillEffect(SHAPE_CHANGE_DOMINATION) || pc.getSkill().hasSkillEffect(SHAPE_CHANGE_100LEVEL))) {
				pc.send_effect(15846);
				return 0;
			}
			if (pc.getInventory().checkEquipped(20281)) {// 변신 조종 반지
				pc.sendPackets(S_MessageYN.POLY_YN);
				return 0;
			} else {
				L1PolyMorph.doPoly(pc, polyId, _skill.getBuffDuration(), L1PolyMorph.MORPH_BY_ITEMMAGIC);
				if (attacker.getId() != pc.getId()) {
					pc.sendPackets(new S_ServerMessage(241, attacker.getName()), true);
				}
			}
		} else {
			if (attacker instanceof L1PcInstance) {
				((L1PcInstance)attacker).sendPackets(L1ServerMessage.sm280);
			}
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		L1PolyMorph.undoPoly(cha);
	}
	
	@Override
	public void icon(L1PcInstance pc, int time) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void wrap(L1PcInstance pc, boolean flag) {
		super.wrap(pc, flag);
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new ShapeChange().setValue(_skillId, _skill);
	}

}

