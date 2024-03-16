package l1j.server.server.utils;

import java.util.HashMap;

import l1j.server.Config;
import l1j.server.server.templates.L1Item;

/**
 * 아이템 프로토 패킷 Stream
 * @author LinOffice
 */
public class BinaryOutputStreamItemInfo extends BinaryOutputStream {
	public BinaryOutputStreamItemInfo() {
		super();
	}
	
	public void write_object_id(int object_id) {
		writeC(0x08);// object_id
		writeBit(object_id);
	}
	
	public void write_name_id(int name_id) {
		writeC(0x10);// name_id
		writeBit(name_id == 0 ? -1L : name_id);
	}
	
	public void write_db_id(int db_id) {
		writeC(0x18);// db_id
		writeBit(db_id);
	}
	
	public void write_count(int count) {
		writeC(0x20);// count
		writeBit(count);
	}
	
	public void write_interact_type(int interact_type) {
		writeC(0x28);// interact_type
		writeBit(interact_type);
	}
	
	public void write_number_of_use(int number_of_use) {
		writeC(0x30);// number_of_use
		writeBit(number_of_use);
	}
	
	public void write_icon_id(int icon_id) {
		writeC(0x38);// icon_id
		writeBit(icon_id);
	}
	
	public void write_bless_code_for_display(int bless_code_for_display) {
		writeC(0x40);// bless_code_for_display
		writeBit(bless_code_for_display);
	}
	
	public void write_attribute_bit_set(int attribute_bit_set) {
		writeC(0x48);// attribute_bit_set
		writeBit(attribute_bit_set);
	}
	
	public void write_attribute_bit_set_ex(int attribute_bit_set_ex) {
		writeC(0x50);// attribute_bit_set_ex
		writeBit(attribute_bit_set_ex);
	}
	
	public void write_is_timeout(boolean is_timeout) {
		writeC(0x58);// is_timeout
		writeB(is_timeout);
	}
	
	public void write_category(int category) {
		writeC(0x60);// category
		writeBit(category);
	}
	
	public void write_enchant(int enchant) {
		writeC(0x68);// enchant
		writeBit(enchant);
	}
	
	public void write_deposit(int deposit) {
		writeC(0x70);// deposit
		writeBit(deposit);
	}
	
	public void write_overlay_surf_id(int overlay_surf_id) {
		writeC(0x78);// overlay_surf_id
		writeBit(overlay_surf_id);
	}
	
	public void write_elemental_enchant_type(int elemental_enchant_type) {
		writeH(0x0180);// elemental_enchant_type
		writeC(elemental_enchant_type);
	}
	
	public void write_elemental_enchant_value(int elemental_enchant_value) {
		writeH(0x0188);// elemental_enchant_value
		writeC(elemental_enchant_value);
	}
	
	public void write_description(String description) {
		writeH(0x0192);// description
		writeS2(description);
	}
	
	public void write_extra_description(byte[] extra_description) {
		writeH(0x019a);// extra_description
		writeBytesWithLength(extra_description);
	}
	
	public void write_left_time_for_pre_notify(int left_time_for_pre_notify) {
		writeH(0x01a0);// left_time_for_pre_notify
		writeBit(left_time_for_pre_notify);
	}
	
	public void write_companion_card(byte[] companion_card) {
		writeH(0x01aa);// companion_card
        writeBytesWithLength(companion_card);
	}
	
	public void write_bless_code(int bless_code) {
		writeH(0x01b0);// bless_code
		writeBit(bless_code);
	}
	
	public void write_real_enchant(int real_enchant) {
		writeH(0x01b8);// real_enchant
		writeBit(real_enchant);
	}
	
	public void write_is_merging(boolean is_merging) {
		writeH(0x01c0);// is_merging
        writeB(is_merging);
	}
	
	public void write_weight(int weight) {
		writeH(0x01c8);// weight
        writeBit(weight);
	}
	
	public void write_is_identified(boolean is_identified) {
		writeH(0x01d0);// is_identified
        writeB(is_identified);
	}
	
	public void write_potential_grade(int potential_grade) {
		writeH(0x01d8);// potential_grade
    	writeC(potential_grade);
	}
	
	public void write_potential_bonus_id(int potential_bonus_id) {
		writeH(0x01e0);// potential_bonus_id
    	writeC(potential_bonus_id);
	}
	
	public void write_slot_count(int slot_count) {
		writeH(0x01e8);// slot_count
    	writeC(slot_count);
	}
	
	public void write_slot_info(HashMap<Integer, L1Item> slots) {
		L1Item slot;
		int slot_scroll_nameId;
		for (int slot_no=0; slot_no<Config.SMELTING.SMELTING_LIMIT_SLOT_VALUE; slot_no++) {
    		slot				= slots.get(slot_no);
    		slot_scroll_nameId	= slot == null ? 0 : slot.getItemNameId();
    		writeH(0x01f2);// slot_info
    		writeC(3 + getBitSize(slot_scroll_nameId));
        	writeC(0x08);// slot_no
        	writeC(slot_no);
        	writeC(0x10);// slot_scroll_nameId
        	writeBit(slot_scroll_nameId);
    	}
	}
	
	public void write_usable_time(int usable_time) {
		writeH(0x01f8);// usable_time
		writeBit(usable_time);
	}
	
}

