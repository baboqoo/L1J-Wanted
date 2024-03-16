package l1j.server.server.model.skill.action;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.L1PinkName;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;

public class Prime extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (attacker instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) attacker;
			boolean pink = false;
			int primeDmg = 3, primeSp = 2, primeHp = 0;
			if (pc.warZone) {
				primeDmg	= 9;
				primeSp		= 6;
				primeHp		= 500;
			}
			for (L1PcInstance member : L1World.getInstance().getVisiblePlayer(pc, 18)) {
				if (member.getClanid() == pc.getClanid()) {
					member.setPrimeDmgState(primeDmg);
					member.setPrimeSpState(primeSp);
					member.setPrimeHpState(primeHp);
					member.getAbility().addShortDmgup(member.getPrimeDmgState());
					member.getAbility().addShortHitup(member.getPrimeDmgState());
					member.getAbility().addLongDmgup(member.getPrimeDmgState());
					member.getAbility().addLongHitup(member.getPrimeDmgState());
					member.getAbility().addSp(member.getPrimeSpState());
					member.getAbility().addMagicHitup(member.getPrimeSpState());
					member.addMaxHp(member.getPrimeHpState());
					if (member.warZone) {
						if (member.getLevel() >= 95) {
							member.setPrimePvPDmgReducState(15);
						} else if (member.getLevel() >= 90) {
							member.setPrimePvPDmgReducState(10);
						} else if (member.getLevel() >= 85) {
							member.setPrimePvPDmgReducState(5);
						}
						member.getAbility().addPVPDamageReduction(member.getPrimePvPDmgReducState());
					}
					
					member.getSkill().setSkillEffect(PRIME, time * 1000);
					member.sendPackets(new S_SpellBuffNoti(member, _skillId, true, time), true);
					member.send_effect(member.warZone ? 18582 : 18406);
					member.sendPackets(L1ServerMessage.sm7054);// 군주의 외침이 믿을 수 없는 용기를 부여합니다.
					
					if (member.isPinkName()) {
						pink = true;
					}
				}
			}
			if (pc.getId() == attacker.getId()) {
				primeDmg	= 9;
				primeSp		= 6;
				primeHp		= 500;
				pc.setPrimePrinceState(15);
				pc.getResistance().addHitupSkill(pc.getPrimePrinceState());
			}
			pc.setPrimeDmgState(primeDmg);
			pc.setPrimeSpState(primeSp);
			pc.setPrimeHpState(primeHp);
			pc.getAbility().addShortDmgup(pc.getPrimeDmgState());
			pc.getAbility().addShortHitup(pc.getPrimeDmgState());
			pc.getAbility().addLongDmgup(pc.getPrimeDmgState());
			pc.getAbility().addLongHitup(pc.getPrimeDmgState());
			pc.getAbility().addSp(pc.getPrimeSpState());
			pc.getAbility().addMagicHitup(pc.getPrimeSpState());
			pc.addMaxHp(pc.getPrimeHpState());
			if (pc.getLevel() >= 95) {
				pc.setPrimePvPDmgReducState(15);
			} else if (pc.getLevel() >= 90) {
				pc.setPrimePvPDmgReducState(10);
			} else if (pc.getLevel() >= 85) {
				pc.setPrimePvPDmgReducState(5);
			}
			pc.getAbility().addPVPDamageReduction(pc.getPrimePvPDmgReducState());
			
			pc.sendPackets(new S_SpellBuffNoti(pc, _skillId, true, time), true);
			pc.send_effect(18582);
			pc.sendPackets(L1ServerMessage.sm7054);// 군주의 외침이 믿을 수 없는 용기를 부여합니다.
			
			if (pink) {
				L1PinkName.onAction(pc);
			}
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.getPrimePrinceState() > 0) {
				pc.getResistance().addHitupSkill(-pc.getPrimePrinceState());
				pc.setPrimePrinceState(0);
			}
			pc.getAbility().addShortDmgup(-pc.getPrimeDmgState());
			pc.getAbility().addShortHitup(-pc.getPrimeDmgState());
			pc.getAbility().addLongDmgup(-pc.getPrimeDmgState());
			pc.getAbility().addLongHitup(-pc.getPrimeDmgState());
			pc.getAbility().addSp(-pc.getPrimeSpState());
			pc.getAbility().addMagicHitup(-pc.getPrimeSpState());
			pc.addMaxHp(-pc.getPrimeHpState());
			if (pc.getPrimePvPDmgReducState() > 0) {
				pc.getAbility().addPVPDamageReduction(-pc.getPrimePvPDmgReducState());
				pc.setPrimePvPDmgReducState(0);
			}
			pc.setPrimeDmgState(0);
			pc.setPrimeSpState(0);
			pc.setPrimeHpState(0);
			pc.sendPackets(new S_ServerMessage(7055), true); // 용솟음 친 힘이 가라 앉습니다.
		}
	}
	
	@Override
	public void icon(L1PcInstance pc, int time) {
		pc.sendPackets(new S_SpellBuffNoti(pc, _skillId, true, time), true);
	}
	
	@Override
	public void wrap(L1PcInstance pc, boolean flag) {
		pc.sendPackets(new S_OwnCharStatus(pc), true);
		pc.sendPackets(new S_SPMR(pc), true);
		super.wrap(pc, flag);
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new Prime().setValue(_skillId, _skill);
	}

}

