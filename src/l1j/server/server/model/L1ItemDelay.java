package l1j.server.server.model;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.templates.L1EtcItem;

public class L1ItemDelay {
	private L1ItemDelay() {}

	static class ItemDelayTimer implements Runnable {
		private int _delayId;
		private L1Character _cha;

		public ItemDelayTimer(L1Character cha, int id) {
			_cha		= cha;
			_delayId	= id;
		}

		@Override
		public void run() {
			stopDelayTimer(_delayId);
		}

		public void stopDelayTimer(int delayId) {
			_cha.removeItemDelay(delayId);
		}
	}

	public static void onItemUse(L1PcInstance pc, L1ItemInstance item) {
		if (pc == null) {
			return;
		}
		int delayId = 0, delayTime = 0;
		if (item.getItem().getItemType() == L1ItemType.NORMAL) {
			delayId		= ((L1EtcItem) item.getItem()).getDelayId();
			delayTime	= ((L1EtcItem) item.getItem()).getDelayTime();
			if (item.getItem().getType() == 6) {// 회복 물약류
				int decreaseDelay = pc.getAbility().getPotionDelay() + pc.getAbility().getHpPotionDelayDecrease();
				if (decreaseDelay > 0) {
					delayTime -= decreaseDelay;
				}
			}
		} else if (item.getItem().getItemType() == L1ItemType.WEAPON) {
			return;
		} else if (item.getItem().getItemType() == L1ItemType.ARMOR) {
			if (L1ItemId.isInvisItem(item.getItem().getItemId())) {
				if (item.isEquipped() && !pc.isInvisble()) {
					pc.beginInvisTimer();
				}
			} else {
				return;
			}
		}
		ItemDelayTimer timer = new ItemDelayTimer(pc, delayId);
		pc.addItemDelay(delayId, timer);
		GeneralThreadPool.getInstance().schedule(timer, delayTime);
	}

}

