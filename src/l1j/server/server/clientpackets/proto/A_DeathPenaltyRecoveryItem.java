package l1j.server.server.clientpackets.proto;

import l1j.server.GameSystem.deathpenalty.DeathPenaltyTable;
import l1j.server.GameSystem.deathpenalty.bean.DeathPenaltyItemObject;
import l1j.server.GameSystem.deathpenalty.bean.DeathPenaltyObject;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.monitor.Logger.DeathPenaltyType;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.inventory.S_DeathPenaltyRecoveryItemListNoti;
import l1j.server.server.serverpackets.message.S_ServerMessage;

public class A_DeathPenaltyRecoveryItem extends ProtoHandler {
	protected A_DeathPenaltyRecoveryItem(){}
	private A_DeathPenaltyRecoveryItem(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int _db_id;
	
	void parse() {
		readP(1);// 0x08
		_db_id = readBit();
	}
	
	boolean isValidation() {
		return _db_id > 0;
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost() || _pc.isStop() || _pc.isDead()) {
			return;
		}
		L1PcInventory inv = _pc.getInventory();
		if (inv.getSize() >= L1PcInventory.MAX_SIZE || inv.getWeightPercent() > 98) {
			_pc.sendPackets(L1ServerMessage.sm82);// 무게 게이지가 부족하거나 인벤토리가 꽉차서 더 들 수 없습니다.
			return;
		}
		parse();
		if (!isValidation()) {
			return;
		}

		java.util.LinkedList<DeathPenaltyObject> list = _pc.get_penalty_item().get_list();
		if (list == null || list.isEmpty()) {
			return;
		}
		DeathPenaltyItemObject obj = null;
		for (DeathPenaltyObject val : list) {
			DeathPenaltyItemObject check = (DeathPenaltyItemObject) val;
			if (check.get_recovery_item().getId() == _db_id) {
				obj = check;
				break;
			}
		}
		if (obj == null) {
			System.out.println(String.format("[A_DeathPenaltyRecoveryItem] ITEM_OBJECT_NOT_FOUND : DB_ID(%d), CHAR(%s)", _db_id, _pc.getName()));
			return;
		}
		if (obj.get_delete_time().getTime() <= System.currentTimeMillis()) {
			return;
		}
		if (!inv.checkItem(L1ItemId.ADENA, obj.get_recovery_cost())) {
			_pc.sendPackets(L1ServerMessage.sm189);// \f1%0이 부족합니다.
			return;
		}
		if (!DeathPenaltyTable.getInstance().deleteItem(obj)) {
			return;
		}
		inv.consumeItem(L1ItemId.ADENA, obj.get_recovery_cost());
		L1ItemInstance recovery_item = inv.storeItem(obj.get_recovery_item());
		_pc.sendPackets(new S_ServerMessage(403, recovery_item.getLogNameRef()), true);// %0를 손에 넣었습니다.
		list.remove(obj);
		_pc.sendPackets(new S_DeathPenaltyRecoveryItemListNoti(_pc, list), true);
		LoggerInstance.getInstance().addDeathPenaltyItem(DeathPenaltyType.RECOVERY, _pc, obj);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_DeathPenaltyRecoveryItem(data, client);
	}

}

