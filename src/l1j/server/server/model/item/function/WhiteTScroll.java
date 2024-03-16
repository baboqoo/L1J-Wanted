package l1j.server.server.model.item.function;

import java.sql.Timestamp;

import l1j.server.Config;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.templates.L1Item;

public class WhiteTScroll extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public WhiteTScroll(L1Item item) {
		super(item);
	}
	
	private L1ItemInstance inputItem;

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			int inputId = packet.readD();
			inputItem = pc.getInventory().getItem(inputId);
			if (inputItem == null) {
				return;
			}
			int inputItemId = inputItem.getItemId();
			if (this.getItemId() == 30116) {
				if (inputItemId >= 22340 && inputItemId <= 22348) { // 순백의 요정족 티
					changeT(pc, 20084, 1);
					pc.getInventory().DeleteEnchant(inputItemId, inputItem.getEnchantLevel());
					pc.getInventory().removeItem(this, 1);
				} else if (inputItemId >= 22349 && inputItemId <= 22357) { // 순백의 티
					changeT(pc, 20085, 1);
					pc.getInventory().DeleteEnchant(inputItemId, inputItem.getEnchantLevel());
					pc.getInventory().removeItem(this, 1);
				} else {
					pc.sendPackets(L1ServerMessage.sm79);
				}
			} else {
				if (inputItemId == 20084) {// 요정족 티셔츠
					for (int i = 0; i < L1ItemId.WHITE_T_SCROLL_ITEMS.length; i++) {
						if (this.getItemId() == L1ItemId.WHITE_T_SCROLL_ITEMS[i]) {
							changeT(pc, L1ItemId.WHITE_ELF_T_ITEMS[i], 1);
							pc.getInventory().DeleteEnchant(inputItemId, inputItem.getEnchantLevel());
							pc.getInventory().removeItem(this, 1);
							break;
						}
					}
				} else if (inputItemId == 20085) {// 티셔츠
					for (int i = 0; i < L1ItemId.WHITE_T_SCROLL_ITEMS.length; i++) {
						if (this.getItemId() == L1ItemId.WHITE_T_SCROLL_ITEMS[i]) {
							changeT(pc, L1ItemId.WHITE_T_ITEMS[i], 1);
							pc.getInventory().DeleteEnchant(inputItemId, inputItem.getEnchantLevel());
							pc.getInventory().removeItem(this, 1);
							break;
						}
					}
				} else {
					pc.sendPackets(L1ServerMessage.sm79);
				}
			}
		}
	}
	
	boolean changeT(L1PcInstance pc, int ouputItemId, int outputCount) {
		L1ItemInstance item = ItemTable.getInstance().createItem(ouputItemId);
		if (item != null) {
			item.setCount(outputCount);
			item.setEnchantLevel(inputItem.getEnchantLevel());
			item.setIdentified(true);
			if (pc.getInventory().checkAddItem(item, outputCount) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
			} else {
				pc.sendPackets(L1ServerMessage.sm82);// 무게 게이지가 부족하거나 인벤토리가 꽉차서 더 들 수 없습니다.
				return false;
			}
			pc.sendPackets(new S_ServerMessage(403, item.getLogNameRef()), true);// %0를 손에넣었습니다.
			if (Config.ETC.T_WHITE_DAY_LIMIT) {
				Timestamp deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * 4320));// 1일
				item.setEndTime(deleteTime);
				pc.getInventory().updateItem(item, L1PcInventory.COL_REMAINING_TIME);
				pc.getInventory().saveItem(item, L1PcInventory.COL_REMAINING_TIME);
			}			
			pc.saveInventory();
			return true;
		}
		return false;
	}
}

