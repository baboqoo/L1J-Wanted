package l1j.server.server.model.warehouse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public abstract class Warehouse extends L1Object {
	private static final long serialVersionUID = 1L;
	protected List<L1ItemInstance> _items = new CopyOnWriteArrayList<L1ItemInstance>();
	private final String name;

	public Warehouse(String n) {
		super();
		name = n;
	}
	
	public String getName() {
		return name;
	}

	public abstract void loadItems(); 
	//public abstract boolean checkitem(int id, int count);
	public abstract void deleteItem(L1ItemInstance item);
	public abstract void insertItem(L1ItemInstance item);
	public abstract void updateItem(L1ItemInstance findItem);
	protected abstract int getMax();
	
	public L1ItemInstance findItemId(int id) {
		for (L1ItemInstance item : _items) {
			if (item.getItem().getItemId() == id) {
				return item;
			}
		}
		return null;
	}

	public synchronized L1ItemInstance storeTradeItem(L1ItemInstance item) {
		if (item.isMerge()) {
			L1ItemInstance findItem = findItemId(item.getItemId());
			if (findItem != null) {
				findItem.setCount(findItem.getCount() + item.getCount());
				updateItem(findItem);
				return findItem;
			}
		}
		item.setX(getX());
		item.setY(getY());
		item.setMap(getMapId());
		_items.add(item);
		insertItem(item);
		return item;
	}

	public synchronized L1ItemInstance tradeItem(L1Object obj, L1ItemInstance item, int count, L1Inventory inventory) {
		if (item == null)
			return null;
		if (item.getCount() <= 0 || count <= 0)
			return null;
		if (item.isEquipped())
			return null;
		if (!checkItem(item.getItem().getItemId(), count))
			return null;
		
		L1ItemInstance carryItem;
		//엔진관련 버그 방지 추가
		if (item.getCount() <= count || count < 0) {
			deleteItem(item);
			carryItem = item;
		} else {
			item.setCount(item.getCount() - count);
			updateItem(item);
			carryItem = ItemTable.getInstance().createItem(item.getItem().getItemId());
			carryItem.setCount(count);
			carryItem.setEnchantLevel(item.getEnchantLevel());
			carryItem.setIdentified(item.isIdentified());
			carryItem.setDurability(item.getDurability());
			carryItem.setChargeCount(item.getChargeCount());
			carryItem.setRemainingTime(item.getRemainingTime());
			carryItem.setLastUsed(item.getLastUsed());
			carryItem.setBless(item.getItem().getBless());
			carryItem.setAttrEnchantLevel(item.getAttrEnchantLevel());
			carryItem.setSpecialEnchant(item.getSpecialEnchant());
			carryItem.setPotential(item.getPotential());
			/** 패키지상점 **/
			carryItem.setPackage(item.isPackage());
		}
		return inventory.storeTradeItem(carryItem);
	}

	public L1ItemInstance getItem(int objectId) {
		L1ItemInstance item = null;
		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			if (item.getId() == objectId) {
				return item;
			}
		}
		return null;
	}
	
	public L1ItemInstance getItemIndex(int index) {
		return _items.get(index);
	}
	
	public synchronized void removeItem(L1ItemInstance item) {
		if (_items.contains(item)) {
			_items.remove(item);
		}
		deleteItem(item);
	}

	public List<L1ItemInstance> getItems() {
		return _items;
	}

	public void clearItems() {
		L1ItemInstance item = null;
		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			L1World.getInstance().removeObject(item);
		}
		_items.clear();
	}

	public L1ItemInstance[] findItemsId(int id) {
		ArrayList<L1ItemInstance> itemList = new ArrayList<L1ItemInstance>();
		for (L1ItemInstance item : _items) {
			if (item.getItemId() == id) {
				itemList.add(item);
			}
		}
		return itemList.toArray(new L1ItemInstance[] {});
	}

	public boolean checkItem(int id, int count) {
		if (count == 0) {
			return true;
		}
		if (ItemTable.getInstance().getTemplate(id).isMerge()) {
			L1ItemInstance item = findItemId(id);
			if (item != null && item.getCount() >= count) {
				return true;
			}
		} else {
			Object[] itemList = findItemsId(id);
			if (itemList.length >= count) {
				return true;
			}
		}
		return false;
	}

	public int getSize() {
		return _items.size();
	}

	public int checkAddItemToWarehouse(L1ItemInstance item, int count) {
		if (item == null)
			return -1;
		if (item.getCount() <= 0 || count <= 0)
			return -1;
		
		final int OK = 0, SIZE_OVER = 1;
		final int maxSize = getMax(), SIZE = getSize();
		if ( SIZE > maxSize || (SIZE == maxSize && (!item.isMerge() || !checkItem(item.getItem().getItemId(), 1))))
			return SIZE_OVER;
		return OK;
	}
	
	public int checkAddItemToSpecialWarehouse(L1PcInstance pc, L1ItemInstance item, int count){
		if (item == null)
			return -1;
		if (item.getCount() <= 0 || count <= 0)
			return -1;
		
		final int OK = 0, SIZE_OVER = 1;
		final int maxSize = pc.getSpecialWareHouseSize(), SIZE = getSize();
		if ( SIZE > maxSize || (SIZE == maxSize && (!item.isMerge() || !checkItem(item.getItem().getItemId(), 1))))
			return SIZE_OVER;
		return OK;
	}
}
