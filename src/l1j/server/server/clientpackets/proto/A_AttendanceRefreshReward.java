package l1j.server.server.clientpackets.proto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import l1j.server.GameSystem.attendance.AttendanceGroupType;
import l1j.server.GameSystem.attendance.AttendanceTable;
import l1j.server.GameSystem.attendance.bean.AttendanceAccount;
import l1j.server.GameSystem.attendance.bean.AttendanceRandomItem;
import l1j.server.server.GameClient;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.attendance.S_AttenDanceRefreshReward;

public class A_AttendanceRefreshReward extends ProtoHandler {
	protected A_AttendanceRefreshReward(){}
	private A_AttendanceRefreshReward(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private static final int MAX_CHANGE_COUNT = 5;

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		AttendanceAccount attendAccount = _pc.getAccount().getAttendance();
		if (attendAccount == null) {
			return;
		}
		readP(1);// 0x08
		int index = readC();
		readP(1);// 0x10
		AttendanceGroupType attendType = AttendanceGroupType.fromInt(readC());
		if (attendType == null || !AttendanceGroupType.isUse(attendType)) {
			return;
		}
		readP(1);// 0x18
		int seasonId = readC();
		if (seasonId != attendType.getSeasonId()) {
			return;
		}
		
		Map<Integer, HashMap<Integer, ArrayList<AttendanceRandomItem>>> items = attendAccount.getRandomItems(attendType);
		if (items == null) {
			return;
		}
		HashMap<Integer, ArrayList<AttendanceRandomItem>> radomItemInfo = items.get(index);
		if (radomItemInfo == null || radomItemInfo.isEmpty()) {
			return;
		}
		int changeCount = radomItemInfo.keySet().iterator().next();// 교체 횟수
		if (changeCount >= MAX_CHANGE_COUNT) {
			return;// 최대 교체 횟수
		}
		int consumeCount = 0;
		switch(changeCount){
		case 0:consumeCount = 10000;break;
		case 1:consumeCount = 20000;break;
		case 2:consumeCount = 30000;break;
		case 3:consumeCount = 50000;break;
		case 4:consumeCount = 100000;break;
		default:return;
		}
		if (!_pc.getInventory().consumeItem(L1ItemId.ADENA, consumeCount)) {
			return;
		}
		radomItemInfo.get(changeCount).clear();// 메모리 제거
		radomItemInfo.remove(changeCount);// 메모리 제거
		radomItemInfo.put(changeCount + 1, AttendanceTable.getRandomItemListSelector(attendType));// 5개의 랜덤한 아이템 새로 갱신
		_pc.sendPackets(new S_AttenDanceRefreshReward(index, attendType, radomItemInfo), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_AttendanceRefreshReward(data, client);
	}

}

