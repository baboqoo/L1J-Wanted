package l1j.server.server.clientpackets.proto;

import java.sql.Timestamp;

import javolution.util.FastMap;
import l1j.server.GameSystem.dungeontimer.bean.L1DungeonTimerUser;
import l1j.server.GameSystem.dungeontimer.bean.TimerResetType;
import l1j.server.common.bin.ChargedTimeMapCommonBinLoader;
import l1j.server.common.bin.chargedtimemap.ChargedTimeMapDataT;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.utils.CommonUtil;

public class A_MapTimeChargeRequest extends ProtoHandler {
	protected A_MapTimeChargeRequest(){}
	private A_MapTimeChargeRequest(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);// 0x08
		int used_item_id = readBit();
		readP(1);// 0x10;
		int group = readC();
		readP(1);// 0x18;
		int charge_count = readC();
		
		ChargedTimeMapDataT.GroupT groupT = ChargedTimeMapCommonBinLoader.get_data().get_group(group);
		if (groupT == null) {
			System.out.println(String.format(
					"[A_MapTimeChargeRequest] GROUP_T_NOT_FOUND : GROUP(%d), CHARNAME(%s)", 
					group, _pc.getName()));
			return;
		}
		int cost = groupT.get_cost_per_time() * charge_count;
		
		L1PcInventory inv = _pc.getInventory();
		L1ItemInstance cost_item = inv.findItem(used_item_id, cost);
		if (cost_item == null) {
			System.out.println(String.format(
					"[A_MapTimeChargeRequest] COST_ITEM_NOT_FOUND : USE_NAME_ID(%d), COUNT(%d) CHARNAME(%s)", 
					used_item_id, cost, _pc.getName()));
			return;
		}
		
		FastMap<Integer, L1DungeonTimerUser> map = _pc.getDungoenTimer().getTimers();
		L1DungeonTimerUser timer = null;
		for (L1DungeonTimerUser val : map.values()) {
			if (val.getInfo().getGroup().toInt() == group) {
				timer = val;
				break;
			}
		}
		if (timer == null) {
			System.out.println(String.format(
					"[A_MapTimeChargeRequest] USER_TIMER_NOT_FOUND : GROUP(%d), CHARNAME(%s)", 
					group, _pc.getName()));
			return;
		}
		
		if (timer.getChargeCount() >= timer.getInfo().getMaxChargeCount()) {
			_pc.sendPackets(L1ServerMessage.sm7577);// 충전불가: 충전 제한 횟수 소진
			return;
		}
		if (timer.getResetTime() == null 
				|| ((timer.getInfo().getResetType() == TimerResetType.WEEK && CommonUtil.isWeekResetCheck(timer.getResetTime()))
				|| (timer.getInfo().getResetType() == TimerResetType.DAY && CommonUtil.isDayResetTimeCheck(timer.getResetTime())))) {
			if (timer.getResetTime() != null) {
				timer.getResetTime().setTime(System.currentTimeMillis());
			} else {
				timer.setResetTime(new Timestamp(System.currentTimeMillis()));
			}
		}
		timer.setChargeCount(timer.getChargeCount() + charge_count);
		_pc.getInventory().removeItem(cost_item, cost);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_MapTimeChargeRequest(data, client);
	}

}

