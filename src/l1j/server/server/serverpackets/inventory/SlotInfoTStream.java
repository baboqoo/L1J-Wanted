package l1j.server.server.serverpackets.inventory;

import l1j.server.common.bin.favorbook.AUBIBookInfoForClient;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookObject;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookUserObject;
import l1j.server.server.utils.BinaryOutputStream;

public class SlotInfoTStream extends BinaryOutputStream {
	
	protected SlotInfoTStream(L1PcInstance pc, L1FavorBookObject obj, L1FavorBookUserObject user, L1ItemInstance item) {
		super();
		int name_id		= item == null ? 0 : item.getItem().getItemNameId();
		int craft_id	= 0;
		int awakening	= 0;
		if (user == null) {
			AUBIBookInfoForClient.BookT.CategoryT.SlotT.StateT stateT = obj.getSlotT().get_state_infos().getFirst();
			craft_id	= stateT.get_craft_id();
			awakening	= stateT.get_awakening();
		} else {
			craft_id	= user.getCraftId();
			awakening	= user.getAwakening();
		}
		
		write_category(obj.getCategory().getCategory());
		write_slot_id(obj.getSlotId());
		write_item_id(name_id);
		write_craft_id(craft_id);
		if (item != null) {
			write_awakening(awakening);
			int red_dot_notice = obj.getSlotT().get_red_dot_notice();
			if (red_dot_notice > 0) {
				write_red_dot_notice(red_dot_notice);
			}
			write_item_info(item.getItemInfo(pc));
		}
	}
	
	void write_category(int category) {
		writeC(0x08);// category
		writeC(category);
	}
	
	void write_slot_id(int slot_id) {
		writeC(0x10);// slot_id
		writeC(slot_id);
	}
	
	void write_item_id(int item_id) {
		writeC(0x18);// item_id
		writeBit(item_id);
	}
	
	void write_craft_id(int craft_id) {
		writeC(0x20);// craft_id
		writeBit(craft_id);
	}
	
	void write_awakening(int awakening) {
		writeC(0x28);// awakening
		writeC(awakening);
	}
	
	void write_red_dot_notice(int red_dot_notice) {
		writeC(0x30);// red_dot_notice
		writeC(red_dot_notice);
	}
	
	void write_item_info(byte[] item_info) {
		writeC(0x3a);// item_info
		writeBytesWithLength(item_info);
	}
	
}

