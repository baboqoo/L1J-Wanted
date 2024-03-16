package l1j.server.server.model.poison;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.message.S_ServerMessage;

public abstract class L1Poison {
	protected static boolean isValidTarget(L1Character cha) {
		if (cha == null || cha.getPoison() != null) {
			return false;
		}
		if (cha instanceof L1PcInstance == false) {
			return true;
		}
		if (cha.isBind() 
				|| cha.getAbility().get_poison_regist() >= 2 
				|| cha.getSkill().hasSkillEffect(L1SkillId.VENOM_RESIST)) {
			return false;
		}
		return true;
	}

	protected static void sendMessageIfPlayer(L1Character cha, int msgId) {
		if (cha instanceof L1PcInstance == false) {
			return;
		}
		((L1PcInstance) cha).sendPackets(new S_ServerMessage(msgId), true);
	}

	public abstract int getEffectId();

	public abstract void cure();
}

