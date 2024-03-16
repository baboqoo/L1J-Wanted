package l1j.server.server.clientpackets.proto;

import java.util.Calendar;

import l1j.server.server.GameClient;
import l1j.server.server.datatables.HouseTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.system.S_ShelterOwnerChange;
import l1j.server.server.templates.L1House;

public class A_ShelterOwnerChange extends ProtoHandler {
	protected A_ShelterOwnerChange(){}
	private A_ShelterOwnerChange(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private String _target_name;
	private int _target_uid;

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);// 0x0a
		int nameLength = readC();
		_target_name = readS(nameLength);
		readP(1);// 0x10
		_target_uid = readBit();
		
		int houseId = 2237;
		L1PcInstance target = L1World.getInstance().getPlayer(_target_name);
		if (target == null || _target_uid == 0) {
			_pc.sendPackets(new S_ShelterOwnerChange(S_ShelterOwnerChange.eRES.eNotTarget, 0), true);// 대상을 찾을 수 없습니다.
			return;
		}
		if (target.getLevel() < 100) {
			_pc.sendPackets(new S_ShelterOwnerChange(S_ShelterOwnerChange.eRES.eTargetNeedLevel, 0), true);// 대상의 레벨이 부족합니다.
			return;
		}
		if (_pc.getMapId() != houseId || target.getMapId() != houseId) {
			_pc.sendPackets(new S_ShelterOwnerChange(S_ShelterOwnerChange.eRES.eNotShelterMap, 0), true);// 본인과 대상이 아지트 맵에 있어야 합니다.
			return;
		}
		L1ItemInstance key = _pc.getInventory().findItemId(31235);
		if (key == null) {
			_pc.sendPackets(new S_ShelterOwnerChange(S_ShelterOwnerChange.eRES.eNotOwner, 0), true);// 아지트를 소유하고 있지 않아 아이템을 사용할 수 없습니다.
			return;
		}
		if (target.getInventory().checkItem(31235)) {
			_pc.sendPackets(new S_ShelterOwnerChange(S_ShelterOwnerChange.eRES.eTargetOwner, 0), true);// 대상이 이미 아지트를 보유하고 있습니다.
			return;
		}
		
		HouseTable ht = HouseTable.getInstance();
		L1House house = ht.getHouseTable(houseId);
		if (house == null) {
			_pc.sendPackets(new S_ShelterOwnerChange(S_ShelterOwnerChange.eRES.eNotOwnerShelterKey, 0), true);// 알 수 없는 에러가 발생하였습니다.
			System.out.println(String.format("[A_ShelterOwnerChange] HOUSE_EMPTY : HOUSE_ID(%d)", houseId));
			return;
		}
		Calendar cal = house.getTaxDeadline();
		if (cal == null) {
			System.out.println("[A_ShelterOwnerChange] HOUSE_DATE_NOT_FOUND");
			return;
		}
		
		long currentTime = System.currentTimeMillis();
		if (cal.getTimeInMillis() + 604800000L >= currentTime) {// 7일 제한
			_pc.sendPackets(new S_ShelterOwnerChange(S_ShelterOwnerChange.eRES.eNotChangeTime, 0), true);// 소유하고 일정 시간이 지난 후 양도가 가능합니다.
			return;
		}
		
		house.getTaxDeadline().setTimeInMillis(currentTime);
		ht.updateHouse(house);
		_pc.getInventory().tradeItem(key, 1, target.getInventory());// 이전
		_pc.sendPackets(new S_ShelterOwnerChange(S_ShelterOwnerChange.eRES.eOK, 1000), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_ShelterOwnerChange(data, client);
	}

}

