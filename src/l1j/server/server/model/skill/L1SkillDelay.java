package l1j.server.server.model.skill;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Character;

/**
 * 스펠 재사용 딜레이 타이머
 * @author LinOffice
 */
public class L1SkillDelay {
	private L1SkillDelay() {}
	
	private static class SkillDelayTimer implements Runnable {
		L1Character _cha;
		int _groupId;
		
		SkillDelayTimer(L1Character cha, int groupId) {
			_cha		= cha;
			_groupId	= groupId;
			_cha.getSkill().setSkillDelay(_groupId, true);
		}

		@Override
		public void run() {
			if (_cha == null) {
				return;
			}
			_cha.getSkill().setSkillDelay(_groupId, false);
		}
	}
	
	public static void onSkillUse(L1Character cha, int delay, int groupId) {
		GeneralThreadPool.getInstance().schedule(new SkillDelayTimer(cha, groupId), delay);
	}
}

