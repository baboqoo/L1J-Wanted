package l1j.server.server.model.skill.action;

import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1EffectSpawn;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PeopleInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.utils.CommonUtil;

public class Desperado extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		boolean desperadoAbsolute = attacker.isPassiveStatus(L1PassiveId.DESPERADO_ABSOLUTE);
		int changeBuffDuration = desperadoAbsolute ? CommonUtil.randomIntChoice(DESPERADO_ABSOLUTE_ARRAY) : CommonUtil.randomIntChoice(DESPERADO_ARRAY);
		if (attacker.getAbility().getStrangeTimeIncrease() > 0) {
			changeBuffDuration += attacker.getAbility().getStrangeTimeIncrease();
		}
		if (cha.getAbility().getStrangeTimeDecrease() > 0) {
			changeBuffDuration -= cha.getAbility().getStrangeTimeDecrease();
		}
		if (changeBuffDuration <= 0) {
			return 0;
		}
		L1EffectSpawn.getInstance().spawnEffect(desperadoAbsolute ? 19416 : 9416, changeBuffDuration, cha.getX(), cha.getY(), cha.getMapId());
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.setDesperadoAttackerLevel(attacker.getLevel());
			pc.sendPackets(S_Paralysis.DESPERADO_ON);
			pc.sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, 571, true), true);
			if (desperadoAbsolute) {
				pc._isDesperadoAbsolute = true;
			}
		} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance || cha instanceof L1PeopleInstance) {
			L1NpcInstance npc = (L1NpcInstance) cha;
			npc.setHold(true);
		}
		return changeBuffDuration;
	}

	@Override
	public void stop(L1Character cha) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.setDesperadoAttackerLevel(0);
			pc.sendPackets(S_Paralysis.DESPERADO_OFF);
			pc.sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, 571, false), true);
			//pc.sendPackets(new S_ServerMessage(4120), true);
			if (pc._isDesperadoAbsolute) {
				pc._isDesperadoAbsolute = false;
			}
		} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
			L1NpcInstance npc = (L1NpcInstance) cha;
			npc.setHold(false);
		}
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
		return new Desperado().setValue(_skillId, _skill);
	}

}

