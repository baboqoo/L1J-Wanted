package l1j.server.server.model.skill.action;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.action.S_UseAttackSkill;

public class BallogFire extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (attacker instanceof L1NpcInstance) {
			L1NpcInstance npc = (L1NpcInstance)attacker;
			npc.broadcastPacket(new S_PacketBox(S_PacketBox.EFFECT_DURATOR, npc.getId(), 12750, true), true);
			GeneralThreadPool.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					try {
						if (npc == null || npc._destroyed || npc.isDead()) {
							return;
						}
						npc.broadcastPacket(new S_PacketBox(S_PacketBox.EFFECT_DURATOR, npc.getId(), 12750, false), true);
						for (L1PcInstance _pc : L1World.getInstance().getVisiblePlayer(npc, 10)) {
							if (isUseCounterMagic(_pc) || _pc.isGhost() || _pc.isDead() || _pc.isAbsol() || _pc.isBind()) {
								npc.broadcastPacket(new S_UseAttackSkill(npc, _pc.getId(), 12751, _pc.getX(), _pc.getY(), 18, 0), true);
								continue;
							}
							_pc.receiveDamage(npc, 400, 2);
							npc.broadcastPacket(new S_UseAttackSkill(npc, _pc.getId(), 12751, _pc.getX(), _pc.getY(), 18), true);
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}, 5000);
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
		// TODO Auto-generated method stub
	}
	
	private boolean isUseCounterMagic(L1Character cha) {
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
		return new BallogFire().setValue(_skillId, _skill);
	}

}

