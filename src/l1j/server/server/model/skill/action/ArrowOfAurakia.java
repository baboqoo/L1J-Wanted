package l1j.server.server.model.skill.action;

import l1j.server.IndunSystem.indun.IndunCreator;
import l1j.server.IndunSystem.indun.IndunHandler;
import l1j.server.IndunSystem.indun.action.Aurakia;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1EffectSpawn;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.action.S_ChangeHeading;
import l1j.server.server.serverpackets.spell.S_AvailableSpellNoti;

public class ArrowOfAurakia extends L1SkillActionHandler {
	private static IndunCreator indun = IndunCreator.getInstance();
	
	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (cha instanceof L1MonsterInstance) {
			L1MonsterInstance mon = (L1MonsterInstance) cha;
			if (mon.getNpcId() != 7800300) {
				return 0;
			}
			Aurakia aurakia			= null;
			IndunHandler handler	= indun.getIndun(attacker.getMapId());
			if (handler != null && handler instanceof Aurakia) {
				aurakia = (Aurakia)handler;
			}
			if (aurakia == null) {
				((L1PcInstance) attacker).sendPackets(S_AvailableSpellNoti.ARROW_OF_AURAKIA_OFF);
				return 0;
			}
			attack((L1PcInstance) attacker, mon, aurakia);
		}
		return 0;
	}
	
	private void attack(L1PcInstance attacker, L1MonsterInstance target, Aurakia aurakia){
		long currentTime = System.currentTimeMillis();
		if (attacker.lastArrowOfAurakiaTime + 1500 < currentTime) {
			attacker.lastArrowOfAurakiaTime = currentTime;
			attacker.successArrowOfAurakia = true;
			GeneralThreadPool.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					if (attacker == null || target == null || !attacker.successArrowOfAurakia) {
						return;
					}
					L1EffectSpawn.getInstance().spawnEffect(7800314, 2000, attacker.getX(), attacker.getY(), attacker.getMapId());// 녹색 법진
					int newheading = attacker.targetDirection(target.getX(), target.getY());
					if (newheading != attacker.getMoveState().getHeading()) {
						attacker.getMoveState().setHeading(newheading);
						attacker.broadcastPacketWithMe(new S_ChangeHeading(attacker), true);
					}
					attacker.send_effect(20336);// 공격 이팩트
					target.broadcastPacket(new S_Effect(target.getId(), 20338), true);// 타격 이팩트
					aurakia.increaseArrestedAttackCount();
					attacker.successArrowOfAurakia = false;
				}
			}, 1000L);
		} else {
			attacker.successArrowOfAurakia = false;
		}
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
		// TODO Auto-generated method stub
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new ArrowOfAurakia().setValue(_skillId, _skill);
	}

}

