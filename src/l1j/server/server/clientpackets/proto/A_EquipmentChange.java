package l1j.server.server.clientpackets.proto;

import java.util.ArrayList;

import l1j.server.server.GameClient;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.datatables.EquipSetTable;
import l1j.server.server.model.L1EquipmentSet;
import l1j.server.server.model.L1EquipmentSet.EquipSet;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.equip.S_EquipmentSet;
import l1j.server.server.utils.StringUtil;

public class A_EquipmentChange extends ProtoHandler {
	protected A_EquipmentChange(){}
	private A_EquipmentChange(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private L1PcInventory inv;
	private L1EquipmentSet set;

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		inv = _pc.getInventory();
		set	= _pc.getEquipmentSet();
    	switch (readC()) {
    	case 0x08:// 저장
    		save(readC());
    		break;
    	case 0x10:// 변경
    		equip(readC());
    		break;
    	default:
    		break;
    	}
	}
	
	private void save(int equip_set) {
		EquipSet equip		= set.getEquipSet(equip_set);
		readP(1);// 0x18
		boolean is_desc_save = readC() == 1;
		if (!is_desc_save) {
			// 아이템 저장
			saveEquipItem(equip.getSlotItems());
			EquipSetTable.getInstance().upsertItems(_pc.getId(), equip_set, equip);
		} else {
			// 편집
			readP(1);// 0x22
			int slotNameLength = readC();
			String slotName = StringUtil.EmptyString;// 슬롯이름
			if (slotNameLength > 0) {
				slotName = readS(slotNameLength);
			}
			readP(1);// 0x28
			int slotColor = readC();// 슬롯색깔
			EquipSetTable.getInstance().upsertDesc(_pc.getId(), equip_set, equip, slotName, slotColor);
		}
	}
	
	private void equip(int request_set) {
		if (_pc.isDesperado() || _pc.isOsiris()) {
			return;
		}
		ArrayList<L1ItemInstance> current_items	= set.getEquipSet(set.getCurrentSet()).getSlotItems();
		ArrayList<L1ItemInstance> request_items	= set.getEquipSet(request_set).getSlotItems();
		itemUnEquip(request_items);
		itemSameEquip(current_items, request_items);
		itemEquip(request_items);
		set.setCurrentSet(request_set);
		_pc.sendPackets(new S_EquipmentSet(request_set), true);
	}
	
	// 착용중인 아이템을 세트에 저장
	private void saveEquipItem(ArrayList<L1ItemInstance> saveList){
		saveList.clear();
		for (L1ItemInstance item : inv.getItems()) {
			if (item != null && item.isEquipped() && !saveList.contains(item)) {
				saveList.add(item);
			}
		}
	}
	
	// 현제 착용중인 아이템이 변경할 아이템에 없을경우 착용해제
	private void itemUnEquip(ArrayList<L1ItemInstance> changeList){
		if (changeList == null || changeList.isEmpty()) {
			return;
		}
		for (L1ItemInstance item : inv.getItems()) {
			if (item != null && item.isEquipped() && !changeList.contains(item)) {
				if (item.getItem().getItemType() == L1ItemType.WEAPON) {
					inv.setEquipped(item, false, false, false);
				} else {
					inv.setEquipped(item, false);
					if (L1ItemId.isInvisItem(item.getItem().getItemId())) {
						_pc.beginInvisTimer();
					}
				}
			}
		}
	}
	
	// 현재 슬롯의 아이템리스트가 변경할 아이템 리스트에 존재하지 않을경우 착용해제
	private void itemSameEquip(ArrayList<L1ItemInstance> oldList, ArrayList<L1ItemInstance> newList){
		if (oldList == null || oldList.isEmpty()) {
			return;
		}
		for (L1ItemInstance item : oldList) {
			if (item == null || !inv.checkEQ(item.getId())) {
				continue;
			}
			if (!newList.contains(item)) { //착용하려는 목록에 없는경우 해제
				if (item.getItem().getItemType() == L1ItemType.WEAPON) {
					inv.setEquipped(item, false, false, false);
				} else {
					inv.setEquipped(item, false);
					if (L1ItemId.isInvisItem(item.getItem().getItemId())) {
						_pc.beginInvisTimer();
					}
				}
			}
		}
	}
	
	// 변경할 슬롯의 아이템들 착용
	private void itemEquip(ArrayList<L1ItemInstance> equipList){
		if (equipList == null || equipList.isEmpty()) {
			return;
		}
		for (L1ItemInstance item : equipList) {
			if (item == null || !inv.checkEQ(item.getId())) {
				continue;
			}
			if (!item.isEquipped()) {
				item.clickItem(_pc, null);
			}
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_EquipmentChange(data, client);
	}

}

