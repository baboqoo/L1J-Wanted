package l1j.server.QuestSystem.Compensator.Element;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;

public class ItemCompensator  implements CompensatorElement{
	private L1Item 	_item;
	private int 	_count;
	private int		_compLevel;
	
	public ItemCompensator(int itemId, int count, int compLevel){
		this(ItemTable.getInstance().getTemplate(itemId), count, compLevel);
	}
	
	public ItemCompensator(L1Item item, int count, int compLevel){
		_item 		= item;
		_count 		= count;
		_compLevel 	= compLevel;
	}
	
	@Override
	public void compensate(L1PcInstance pc) {
		if (_item == null || _count <= 0)
			return;
		
		if (pc.getInventory().checkAddItem(_item, _count) != L1Inventory.OK) return;
		pc.getInventory().storeItem(_item.getItemId(), _count, _compLevel);
	}
	
	public int getNameId(){
		return _item.getItemNameId();
	}
	
	public int getCount(){
		return _count;
	}
}

