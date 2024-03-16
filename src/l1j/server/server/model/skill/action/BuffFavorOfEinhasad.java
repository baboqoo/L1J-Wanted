package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;

public class BuffFavorOfEinhasad extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		if (cha instanceof L1PcInstance == false) {
			return;
		}
		L1PcInstance pc = (L1PcInstance) cha;
		
		L1ItemInstance scheduled_item = null;
		for (L1ItemInstance item : pc.getInventory().getItems()) {
			if (item == null || !item.isScheduled() || item.getIconId() != 9659) {
				continue;
			}
			scheduled_item = item;
			break;
		}

		if (scheduled_item == null) {
			return;
		}
		scheduled_item.clickItem(pc, null);// 예약된 가호 사용
	}
	
	@Override
	public void icon(L1PcInstance pc, int time) {
	}
	
	@Override
	public void wrap(L1PcInstance pc, boolean flag) {
		super.wrap(pc, flag);
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new BuffFavorOfEinhasad().setValue(_skillId, _skill);
	}

}

