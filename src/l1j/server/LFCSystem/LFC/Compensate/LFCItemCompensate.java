package l1j.server.LFCSystem.LFC.Compensate;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class LFCItemCompensate implements LFCCompensate{
	private int _partition;
	private int _itemId;
	private int _itemQuantity;
	private int _level;
	@Override
	public void setPartition(int i){
		_partition = i;
	}
	
	@Override
	public int getPartition(){
		return _partition;
	}
	
	@Override
	public void setIdentity(int i){
		_itemId = i;
	}
	
	@Override
	public void setQuantity(int i){
		_itemQuantity = i;
	}
	
	@Override
	public void setLevel(int i){
		_level = i;
	}
	
	@Override
	public void compensate(L1PcInstance pc){
		//pc.getInventory().storeItem(_itemId, _itemQuantity, _level);
		if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(_itemId), _itemQuantity) != L1Inventory.OK) return;
		L1ItemInstance item = pc.getInventory().storeItem(_itemId, _itemQuantity, _level);
		if (item == null) {
			return;
		}
		if (item != null) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("\\aD LFC 승리보상 : " + item.getItem().getDesc() + " (" + _itemQuantity  + ")개 지급완료")); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(56) + item.getItem().getDesc()  + " (" + _itemQuantity   + S_SystemMessage.getRefText(57), true));
		}
	}
}


