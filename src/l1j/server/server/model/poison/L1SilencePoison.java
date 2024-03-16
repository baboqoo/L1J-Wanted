package l1j.server.server.model.poison;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_PacketBox;

public class L1SilencePoison extends L1Poison {
	private final L1Character _target;

	public static boolean doInfection(L1Character cha) {
		return doInfection(cha, 16);
	}
	
	public static boolean doInfection(L1Character cha, int time) {
		if (!L1Poison.isValidTarget(cha))
			return false;
		cha.setPoison(new L1SilencePoison(cha, time));
		return true;
	}

	private L1SilencePoison(L1Character cha, int time) {
		_target = cha;
		doInfection(time);
	}

	private void doInfection(int time) {
		_target.setPoisonEffect(1);
		sendMessageIfPlayer(_target, 310);
		if (_target instanceof L1PcInstance) {
			((L1PcInstance) _target).sendPackets(new S_PacketBox(S_PacketBox.POSION_ICON, 6, time == 0 ? -1 : time), true);
		}
		_target.getSkill().setSkillEffect(L1SkillId.STATUS_POISON_SILENCE, time * 1000);
	}

	@Override
	public int getEffectId() {
		return 1;
	}

	@Override
	public void cure() {
		_target.setPoisonEffect(0);
		sendMessageIfPlayer(_target, 311);
		if (_target instanceof L1PcInstance) {
			((L1PcInstance) _target).sendPackets(S_PacketBox.POISON_ICON_OFF);
		}
		_target.getSkill().killSkillEffectTimer(L1SkillId.STATUS_POISON_SILENCE);
		_target.setPoison(null);
	}
}

