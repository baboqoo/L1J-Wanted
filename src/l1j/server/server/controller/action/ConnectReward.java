package l1j.server.server.controller.action;

import java.util.LinkedList;

import l1j.server.Config;
import l1j.server.server.datatables.ConnectRewardTable;
import l1j.server.server.datatables.ConnectRewardTable.REWARD_TYPE;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

/**
 * 접속 보상 컨트롤러
 * @author LinOffice
 */
public class ConnectReward implements ControllerInterface {
	private static class newInstance {
		public static final ConnectReward INSTANCE = new ConnectReward();
	}
	public static ConnectReward getInstance(){
		return newInstance.INSTANCE;
	}
	private ConnectReward(){}

	@Override
	public void execute() {
		try {
			LinkedList<ConnectRewardTable.RewardInfo> list = ConnectRewardTable.get_reward_list();
			if (list == null || list.isEmpty()) {
				return;
			}
			long currentTime = System.currentTimeMillis();
			for (ConnectRewardTable.RewardInfo reward : list) {
				// 오픈 대기 보상 검증
				if (reward.get_reward_type() == REWARD_TYPE.STANBY_SERVER && !Config.SERVER.STANDBY_SERVER) {
					continue;
				}
				// 시간 설정 시 검증
				long reward_start_date	= reward.get_reward_start_date();
				if (reward_start_date > 0L && reward_start_date > currentTime) {
					continue;
				}
				long reward_finish_date	= reward.get_reward_finish_date();
				if (reward_finish_date > 0L && reward_finish_date <= currentTime) {
					continue;
				}
				// 보상 시간 검증
				if (reward.increase_counter_and_get() < reward.get_reward_interval_minute()) {
					continue;
				}
				reward.reset_counter();
				do_reward(reward);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void execute(L1PcInstance pc) {
	}
	
	/**
	 * 보상 지급
	 * @param reward
	 */
	void do_reward(ConnectRewardTable.RewardInfo reward) {
		L1PcInventory inv = null;
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc == null || pc.getNetConnection() == null) {
				continue;
			}
			inv = pc.getInventory();
			if (inv.getSize() >= L1PcInventory.MAX_SIZE || inv.getWeightPercent() >= 100) {
				continue;
			}
			inv.storeItem(pc, reward.get_reward_item(), reward.get_reward_item_count(), 0);
			pc.sendPackets(reward.get_reward_message_pck());
		}
	}

}

