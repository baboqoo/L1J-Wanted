package l1j.server.server.storage;

import java.util.ArrayList;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.storage.mysql.MySqlCharactersItemStorage;

public abstract class CharactersItemStorage {
	public abstract ArrayList<L1ItemInstance> loadItems(int objId) throws Exception;
	public abstract void storeItem(int objId, L1ItemInstance item) throws Exception;
	public abstract void deleteItem(L1ItemInstance item) throws Exception;
	public abstract void updateItemListAll(ArrayList<L1ItemInstance> itemList) throws Exception;
	public abstract void updateItemAll(L1ItemInstance item) throws Exception;
	public abstract void updateItemId(L1ItemInstance item) throws Exception;
	public abstract void updateItemCount(L1ItemInstance item) throws Exception;
	public abstract void updateItemIdentified(L1ItemInstance item) throws Exception;
	public abstract void updateItemEquipped(L1ItemInstance item) throws Exception;
	public abstract void updateItemEnchantLevel(L1ItemInstance item) throws Exception;
	public abstract void updateItemDurability(L1ItemInstance item) throws Exception;
	public abstract void updateItemChargeCount(L1ItemInstance item) throws Exception;
	public abstract void updateItemRemainingTime(L1ItemInstance item) throws Exception;
	public abstract void updateItemDelayEffect(L1ItemInstance item) throws Exception;
	public abstract void updateItemBless(L1ItemInstance item) throws Exception;
	public abstract void updateItemAttrEnchantLevel(L1ItemInstance item) throws Exception;
	public abstract void updateItemSpecialEnchant(L1ItemInstance item) throws Exception;
	public abstract void updateItemPotential(L1ItemInstance item) throws Exception;
	public abstract void updateItemSlotAll(L1ItemInstance item) throws Exception;
	public abstract void updateItemSlot(L1ItemInstance item, int slot_no, int stone_name_id) throws Exception;
	public abstract void updateItemEndTime(L1ItemInstance item) throws Exception;
	public abstract void updateItemScheduled(L1ItemInstance item) throws Exception;
	public abstract int getItemCount(int objId) throws Exception;

	public static CharactersItemStorage create() {
		if (_instance == null) {
			_instance = new MySqlCharactersItemStorage();
		}
		return _instance;
	}
	private static CharactersItemStorage _instance;
}

