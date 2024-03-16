package l1j.server.server.model.skill.action;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1TowerInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.message.S_MessageYN;

public class Resurrection extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (attacker == null) {
			return 0;
		}
		if (!cha.isDead()) {
			((L1PcInstance)attacker).sendPackets(L1ServerMessage.sm592);
			return 0;
		}
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (attacker.getId() != pc.getId()) {
				if (L1World.getInstance().getVisiblePlayer(pc, 0).size() > 0) {
					for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(pc, 0)) {
						if (!visiblePc.isDead()) {
							((L1PcInstance)attacker).sendPackets(L1ServerMessage.sm592);
							return 0;
						}
					}
				}
				if (L1CastleLocation.getCastleIdByArea(pc) != 0) {// 공성장에서는 부활 불가능
					pc.sendPackets(L1ServerMessage.sm563);
					return 0;
				}
				if (pc.getCurrentHp() == 0 && pc.isDead() && pc.getMap().isUseResurrection()) {
					if (_skillId == RESURRECTION) {
						pc.setGres(false);
					} else if (_skillId == GREATER_RESURRECTION) {
						pc.setGres(true);
					}
					pc.setTempID(attacker.getId());
					pc.sendPackets(S_MessageYN.RESURRECT_BLESS_YN);
				}
			}
		}
		if (cha instanceof L1NpcInstance && !(cha instanceof L1TowerInstance)) {
			L1NpcInstance npc = (L1NpcInstance) cha;
			if (npc.getNpcTemplate().isCantResurrect() && !(npc instanceof L1PetInstance)) {
				return 0;
			}
			if (npc instanceof L1PetInstance && L1World.getInstance().getVisiblePlayer(npc, 0).size() > 0) {
				for(L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(npc, 0)){
					if (!visiblePc.isDead()) {
						((L1PcInstance)attacker).sendPackets(L1ServerMessage.sm592);
						return 0;
					}
				}
			}
			if (npc.getCurrentHp() == 0 && npc.isDead()) {
				npc.resurrect(npc.getMaxHp() >> 2);
				npc.setResurrect(true);
			}
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
		pc.sendPackets(new S_OwnCharStatus(pc), true);
		super.wrap(pc, flag);
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new Resurrection().setValue(_skillId, _skill);
	}

}

