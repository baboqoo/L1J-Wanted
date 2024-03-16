package l1j.server.server.model.skill.action;

import l1j.server.Config;
import l1j.server.server.ActionCodes;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PeopleInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.action.S_AttackPacket;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.utils.CommonUtil;

public class Eternity extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (cha instanceof L1PcInstance) {
			((L1PcInstance) cha).send_effect(18418);
		} else if (cha instanceof L1NpcInstance) {
			cha.broadcastPacket(new S_Effect(cha.getId(), 18418), true);
		}
		if (magic.calcProbabilityMagic(_skillId)) {
			int eterTime = CommonUtil.randomIntChoice(ETERNITY_ARRAY);
			if (attacker.getAbility().getStrangeTimeIncrease() > 0) {
				eterTime += attacker.getAbility().getStrangeTimeIncrease();
			}
			if (cha.getAbility().getStrangeTimeDecrease() > 0) {
				eterTime -= cha.getAbility().getStrangeTimeDecrease();
			}
			if (eterTime <= 0) {
				return 0;
			}
			cha.getSkill().setSkillEffect(STATUS_ETERNITY, eterTime);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(S_Paralysis.RESTRICT_ON);
				pc.sendPackets(new S_SpellBuffNoti(pc, STATUS_ETERNITY, true, eterTime / 1000), true);
				pc.setEternityAttacker((L1PcInstance)attacker);
				pc.setEternityDmg(magic.calcMagicDamage(_skillId));
				pc.broadcastPacketWithMe(new S_PacketBox(S_PacketBox.EFFECT_DURATOR, pc.getId(), 18562, true), true);
				pc.sendPackets(L1ServerMessage.sm7052); // 창에 꿰뚫려 행동에 제약을 받습니다.
			} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof L1PeopleInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.setEternityAttacker((L1PcInstance)attacker);
				npc.setEternityDmg(magic.calcMagicDamage(_skillId));
				npc.setHold(true);
				npc.broadcastPacket(new S_PacketBox(S_PacketBox.EFFECT_DURATOR, npc.getId(), 18562, true), true);
			}
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (!isUseCounterMagic(pc) && !pc.isGhost() && !pc.isDead()
					&& !pc.getSkill().hasSkillEffect(ABSOLUTE_BARRIER) && !pc.isBind()) {
				L1Magic _magic = new L1Magic(pc.getEternityAttacker(), pc);
				double eternity = pc.getEternityDmg();
				if (Config.SPELL.DIS_LOCK_DMG > 0 && eternity > Config.SPELL.DIS_LOCK_DMG) {
					eternity = Config.SPELL.DIS_LOCK_DMG;
				}
				eternity *= 0.8;
				_magic.commit((int) eternity, 0);
				pc.broadcastPacketExceptTargetSight(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Damage), pc.getEternityAttacker(), true);
			}
			Broadcaster.broadcastPacket(pc.getEternityAttacker(), new S_AttackPacket(pc.getEternityAttacker(), pc.getId(), 77, 0), true);
			pc.setEternityAttacker(null);
			pc.setEternityDmg(0);
			pc.sendPackets(new S_SpellBuffNoti(pc, _skillId, false, 0), true);
			if (!pc._isBurningShot) {
				pc.sendPackets(S_Paralysis.RESTRICT_OFF);
			}
			pc.sendPackets(new S_ServerMessage(7053), true); // 자신을 속박하던 창이 사라집니다.
			pc.broadcastPacketWithMe(new S_PacketBox(S_PacketBox.EFFECT_DURATOR, pc.getId(), 18562, false), true);
		} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
			L1NpcInstance npc = (L1NpcInstance) cha;
			if (!npc.isDead() && !npc._destroyed
					&& !npc.getSkill().hasSkillEffect(ABSOLUTE_BARRIER) && !npc.isBind()) {
				L1Magic _magic = new L1Magic(npc.getEternityAttacker(), npc);
				double eternity = npc.getEternityDmg();
				if (Config.SPELL.DIS_LOCK_DMG > 0 && eternity > Config.SPELL.DIS_LOCK_DMG) {
					eternity = Config.SPELL.DIS_LOCK_DMG;
				}
				eternity *= 0.8;
				_magic.commit((int) eternity, 0);
				npc.broadcastPacketExceptTargetSight(new S_DoActionGFX(npc.getId(), ActionCodes.ACTION_Damage), npc.getEternityAttacker(), true);
			}
			Broadcaster.broadcastPacket(npc.getEternityAttacker(), new S_AttackPacket(npc.getEternityAttacker(), npc.getId(), 77, 0), true);
			npc.setEternityAttacker(null);
			npc.setEternityDmg(0);
			npc.setHold(false);
			Broadcaster.broadcastPacket(npc, new S_PacketBox(S_PacketBox.EFFECT_DURATOR, npc.getId(), 18562, false), true);
		}
	}
	
	@Override
	public void icon(L1PcInstance pc, int time) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void wrap(L1PcInstance pc, boolean flag) {
		pc.sendPackets(new S_OwnCharStatus(pc), true);
		super.wrap(pc, flag);
	}
	
	private static boolean isUseCounterMagic(L1Character cha) {
		if (cha.getSkill().hasSkillEffect(COUNTER_MAGIC)) {
			cha.getSkill().removeSkillEffect(COUNTER_MAGIC);
			cha.broadcastPacket(new S_Effect(cha.getId(), 10702), true);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Effect(pc.getId(), 10702), true);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new Eternity().setValue(_skillId, _skill);
	}

}

