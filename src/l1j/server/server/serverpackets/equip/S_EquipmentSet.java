package l1j.server.server.serverpackets.equip;

import java.util.ArrayList;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1EquipmentSet;
import l1j.server.server.model.L1EquipmentSet.EquipSet;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.BinaryOutputStream;

public class S_EquipmentSet extends ServerBasePacket {
	private static final String S_EQUIPMENT_SET = "[S] S_EquipmentSet";
	private byte[] _byte = null;
	private static final int EQUIP = 0x0320;

	public S_EquipmentSet(int current_set) {
		write_init();
		write_current_set(current_set);
		writeH(0x00);
	}

	public S_EquipmentSet(L1EquipmentSet set) {
		write_init();
		write_current_set(set.getCurrentSet());
		for (EquipSet equip : set.getEquipSets().values()) {
			write_equip_sets(equip.getSlotItems(), equip.getEquipSet(), equip.getSlotName(), equip.getSlotColor());
		}
		write_request_limit_sec(2);
		write_limit_level(70);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(EQUIP);
	}
	
	void write_current_set(int current_set) {
		writeRaw(0x08);// current_set
		writeRaw(current_set);
	}
	
	void write_equip_sets(ArrayList<L1ItemInstance> item_list, int equip_set, String slot_name, int slot_color) {
		S_EquipmentSet.Sets os = null;
		try {
			os = new S_EquipmentSet.Sets(item_list, equip_set, slot_name, slot_color);
			writeRaw(0x12);// set
			writeBytesWithLength(os.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
					os = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	void write_request_limit_sec(int request_limit_sec) {
		writeRaw(0x18);// request_limit_sec
		writeRaw(request_limit_sec);
	}
	
	void write_limit_level(int limit_level) {
		writeRaw(0x20);// limit_level
		writeRaw(limit_level);
	}
	
	public static class Sets extends BinaryOutputStream {
		Sets(ArrayList<L1ItemInstance> item_list, int equip_set, String slot_name, int slot_color) {
			super();
			write_equip_set(equip_set);
			if (item_list != null && !item_list.isEmpty()) {
				for (L1ItemInstance item : item_list) {
					write_object_id(item.getId());
				}
			}
			write_slot_name(slot_name);
			write_slot_color(slot_color);
		}
		
		void write_equip_set(int equip_set) {
			writeC(0x08);// equip_set
			writeC(equip_set);
		}
		
		void write_object_id(int object_id) {
			writeC(0x10);// object_id
			writeBit(object_id);
		}
		
		void write_slot_name(String slot_name) {
			writeC(0x1a);// slot_name
			writeStringWithLength(slot_name);
		}
		
		void write_slot_color(int slot_color) {
			writeC(0x20);// slot_color
			writeC(slot_color);
		}
		
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_EQUIPMENT_SET;
	}
}

