package l1j.server.server.model.skill.action;

import l1j.server.server.ActionCodes;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.L1PinkName;
import l1j.server.server.model.L1SkillStatus;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.action.S_DoActionShop;
import l1j.server.server.serverpackets.polymorph.S_Polymorph;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;

public class Cancellation extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		try {
			if (cha instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.getMoveState().setMoveSpeed(0);
				npc.getMoveState().setBraveSpeed(0);
				npc.broadcastPacket(new S_SkillHaste(cha.getId(), 0, 0), true);
				npc.broadcastPacket(new S_SkillBrave(cha.getId(), 0, 0), true);
				npc.setWeaponBreaked(false);
				npc.setParalyzed(false);
			} else if (cha instanceof L1PcInstance) {
				L1SkillUse.detection((L1PcInstance) cha, false);
			}

			L1SkillStatus status = cha.getSkill();
			for (int skillNum : REMOVE_SKILLS) {
				if (NOT_CANCELABLE_SKILL_LIST.contains(skillNum) && !cha.isDead()) {
					continue;
				}
				if (!status.hasSkillEffect(skillNum)) {
					continue;
				}
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					if (skillNum == SHAPE_CHANGE && (pc.isRankingPoly(pc.getSpriteId()) || pc._isArmorSetPoly)) {
						continue;
					}
					if (skillNum == SHAPE_CHANGE_DOMINATION || skillNum == SHAPE_CHANGE_100LEVEL) {
						pc.send_effect(15846);
						continue;
					}
				}
				
				status.removeSkillEffect(skillNum);
				if (cha instanceof L1PcInstance && PROTO_ICON_SKILL_LIST.contains(skillNum)) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_SpellBuffNoti(pc, skillNum, false, -1), true);
				}
			}

			cha.curePoison();
			cha.cureParalaysis();
			status.removeSkillEffect(STATUS_FREEZE);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.broadcastPacketWithMe(new S_CharVisualUpdate(pc), true);
				if (pc.isPrivateShop()) {
					pc.broadcastPacketWithMe(new S_DoActionShop(pc.getId(), ActionCodes.ACTION_Shop, pc.getShopChat()), true);
				}
				if (attacker instanceof L1PcInstance) {
					L1PinkName.onAction(pc, attacker);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void icon(L1PcInstance pc, int time) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void wrap(L1PcInstance pc, boolean flag) {
		super.wrap(pc, flag);
	}
	
	void unicornTrans(L1NpcInstance npc){
		if (npc.getSpriteId() == 2332) {
			npc.setCurrentHp(npc.getMaxHp());
			npc.setSpriteId(2755);
			npc.broadcastPacket(new S_Polymorph(npc.getId(), 2755, 0, "$2488", npc.getNpcTemplate().getClassId()), true);
			npc.setName("$2488");
			npc.setDesc(npc.getName());
		} else {
			npc.setCurrentHp(npc.getMaxHp());
			npc.setSpriteId(2332);
			npc.broadcastPacket(new S_Polymorph(npc.getId(), 2332, 0, "$2103", npc.getNpcTemplate().getClassId()), true);
			npc.setName("$2103");
			npc.setDesc(npc.getName());
		}
	}
	
	static final java.util.LinkedList<Integer> REMOVE_SKILLS;
	static {
		REMOVE_SKILLS = new java.util.LinkedList<Integer>();
		for (int skillNum = SKILLS_BEGIN; skillNum <= SKILLS_END; skillNum++) {
			REMOVE_SKILLS.add(skillNum);
		}
		for (int skillNum : SKILLS_EXCEPTION_IDS) {
			REMOVE_SKILLS.add(skillNum);
		}
		for (int skillNum = STATUS_BEGIN; skillNum <= STATUS_CANCLE_END; skillNum++) {
			if (skillNum == STATUS_CHAT_PROHIBITED) {
				continue;
			}
			REMOVE_SKILLS.add(skillNum);
		}
		for (int skillNum = COOKING_BEGIN; skillNum <= COOKING_END; skillNum++) {
			REMOVE_SKILLS.add(skillNum);
		}
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new Cancellation().setValue(_skillId, _skill);
	}

}

