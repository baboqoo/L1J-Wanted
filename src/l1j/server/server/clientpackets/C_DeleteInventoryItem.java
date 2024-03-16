package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.monitor.Logger.ItemActionType;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;

public class C_DeleteInventoryItem extends ClientBasePacket {
	private static final String C_DELETE_INVENTORY_ITEM = "[C] C_DeleteInventoryItem";

	public C_DeleteInventoryItem(byte[] decrypt, GameClient client) {
		super(decrypt);
		L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		int size = readD();
		int itemObjectId;
		int itemCount;
		L1ItemInstance item = null;
		if (size < 1) {
			return;
		}
		if (size > 20) {
			size = 20;
		}
		for (int i = 0; i < size; i++) {
			itemObjectId = readD();
			itemCount = readD();
			item = pc.getInventory().getItem(itemObjectId);
			// 삭제하려고 한 아이템이 서버상에 없는 경우
			if (item == null) {
				return;
			}
			if (!pc.isGm() && item.getItem().isCantDelete()) {
				pc.sendPackets(L1ServerMessage.sm125);
				continue;
			}

			if (!pc.isGm() && (item.getBless() >= 128 || item.isSlot())) {
				pc.sendPackets(new S_ServerMessage(210, item.getItem().getDesc()), true);
				continue;
			}
			
			if (pc.getPet() != null && pc.getPet().getAmuletId() == item.getId()) {
				pc.sendPackets(new S_ServerMessage(210, item.getItem().getDesc()), true);// \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
				return;
			}

			if (pc.getDoll() != null && item.getId() == pc.getDoll().getItemObjId()) {
				pc.sendPackets(new S_ServerMessage(210, item.getItem().getDesc()), true);// \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
				continue;
			}

			if (item.isEquipped()) {
				pc.sendPackets(L1ServerMessage.sm125);// \f1삭제할 수 없는 아이템이나 장비 하고 있는 아이템은 버릴 수 없습니다.
				continue;
			}
			if (itemCount > item.getCount() || itemCount == 0) {
				itemCount = item.getCount();
			}
			pc.getInventory().removeItem(item, itemCount);
			pc.getLight().turnOnOffLight();
			/** 파일로그저장 **/
			LoggerInstance.getInstance().addItemAction(ItemActionType.Delete, pc, item, itemCount);
		}
	}

	@Override
	public String getType() {
		return C_DELETE_INVENTORY_ITEM;
	}
}

