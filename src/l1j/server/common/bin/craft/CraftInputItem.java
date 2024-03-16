package l1j.server.common.bin.craft;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class CraftInputItem implements ProtoMessage {
	public static CraftInputItem newInstance() {
		return new CraftInputItem();
	}

	private int _name_id;
	private int _count;// 수량
	private int _slot;// 슬롯 번호
	private int _enchant;
	private int _bless;
	private String _desc;
	private int _iconId;
	private int _elemental_enchant_type;
	private int _elemental_enchant_value;
	private int _increase_prob;// 확률 증가
	private boolean _all_enchants_allowed;// 모든 인챈트 수치 허용
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CraftInputItem() {
	}

	public int get_name_id() {
		return _name_id;
	}

	public void set_name_id(int val) {
		_bit |= 0x1;
		_name_id = val;
	}

	public boolean has_name_id() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_count() {
		return _count;
	}

	public void set_count(int val) {
		_bit |= 0x2;
		_count = val;
	}

	public boolean has_count() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_slot() {
		return _slot;
	}

	public void set_slot(int val) {
		_bit |= 0x4;
		_slot = val;
	}

	public boolean has_slot() {
		return (_bit & 0x4) == 0x4;
	}

	public int get_enchant() {
		return _enchant;
	}

	public void set_enchant(int val) {
		_bit |= 0x8;
		_enchant = val;
	}

	public boolean has_enchant() {
		return (_bit & 0x8) == 0x8;
	}

	public int get_bless() {
		return _bless;
	}

	public void set_bless(int val) {
		_bit |= 0x10;
		_bless = val;
	}

	public boolean has_bless() {
		return (_bit & 0x10) == 0x10;
	}

	public String get_desc() {
		return _desc;
	}

	public void set_desc(String val) {
		_bit |= 0x20;
		_desc = val;
	}

	public boolean has_desc() {
		return (_bit & 0x20) == 0x20;
	}

	public int get_iconId() {
		return _iconId;
	}

	public void set_iconId(int val) {
		_bit |= 0x40;
		_iconId = val;
	}

	public boolean has_iconId() {
		return (_bit & 0x40) == 0x40;
	}

	public int get_elemental_enchant_type() {
		return _elemental_enchant_type;
	}

	public void set_elemental_enchant_type(int val) {
		_bit |= 0x80;
		_elemental_enchant_type = val;
	}

	public boolean has_elemental_enchant_type() {
		return (_bit & 0x80) == 0x80;
	}

	public int get_elemental_enchant_value() {
		return _elemental_enchant_value;
	}

	public void set_elemental_enchant_value(int val) {
		_bit |= 0x100;
		_elemental_enchant_value = val;
	}

	public boolean has_elemental_enchant_value() {
		return (_bit & 0x100) == 0x100;
	}

	public int get_increase_prob() {
		return _increase_prob;
	}

	public void set_increase_prob(int val) {
		_bit |= 0x200;
		_increase_prob = val;
	}

	public boolean has_increase_prob() {
		return (_bit & 0x200) == 0x200;
	}

	public boolean get_all_enchants_allowed() {
		return _all_enchants_allowed;
	}

	public void set_all_enchants_allowed(boolean val) {
		_bit |= 0x400;
		_all_enchants_allowed = val;
	}

	public boolean has_all_enchants_allowed() {
		return (_bit & 0x400) == 0x400;
	}

	@Override
	public long getInitializeBit() {
		return (long) _bit;
	}
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;
	}
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_name_id()){
			size += ProtoOutputStream.computeInt32Size(1, _name_id);
		}
		if (has_count()){
			size += ProtoOutputStream.computeInt32Size(2, _count);
		}
		if (has_slot()){
			size += ProtoOutputStream.computeInt32Size(3, _slot);
		}
		if (has_enchant()){
			size += ProtoOutputStream.computeInt32Size(4, _enchant);
		}
		if (has_bless()){
			size += ProtoOutputStream.computeInt32Size(5, _bless);
		}
		if (has_desc()){
			size += ProtoOutputStream.computeStringSize(6, _desc);
		}
		if (has_iconId()){
			size += ProtoOutputStream.computeInt32Size(7, _iconId);
		}
		if (has_elemental_enchant_type()){
			size += ProtoOutputStream.computeInt32Size(8, _elemental_enchant_type);
		}
		if (has_elemental_enchant_value()){
			size += ProtoOutputStream.computeInt32Size(9, _elemental_enchant_value);
		}
		if (has_increase_prob()){
			size += ProtoOutputStream.computeInt32Size(10, _increase_prob);
		}
		if (has_all_enchants_allowed()){
			size += ProtoOutputStream.computeBoolSize(11, _all_enchants_allowed);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_name_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_count()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_slot()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_enchant()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_bless()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_desc()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_iconId()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_name_id()){
			output.wirteInt32(1, _name_id);
		}
		if (has_count()){
			output.wirteInt32(2, _count);
		}
		if (has_slot()){
			output.wirteInt32(3, _slot);
		}
		if (has_enchant()){
			output.wirteInt32(4, _enchant);
		}
		if (has_bless()){
			output.wirteInt32(5, _bless);
		}
		if (has_desc()){
			output.writeString(6, _desc);
		}
		if (has_iconId()){
			output.wirteInt32(7, _iconId);
		}
		if (has_elemental_enchant_type()){
			output.wirteInt32(8, _elemental_enchant_type);
		}
		if (has_elemental_enchant_value()){
			output.wirteInt32(9, _elemental_enchant_value);
		}
		if (has_increase_prob()){
			output.wirteInt32(10, _increase_prob);
		}
		if (has_all_enchants_allowed()){
			output.writeBool(11, _all_enchants_allowed);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
			case 0x00000008: {
				set_name_id(input.readInt32());
				break;
			}
			case 0x00000010: {
				set_count(input.readInt32());
				break;
			}
			case 0x00000018: {
				set_slot(input.readInt32());
				break;
			}
			case 0x00000020: {
				set_enchant(input.readInt32());
				break;
			}
			case 0x00000028: {
				set_bless(input.readInt32());
				break;
			}
			case 0x00000032: {
				set_desc(input.readString());
				break;
			}
			case 0x00000038: {
				set_iconId(input.readInt32());
				break;
			}
			case 0x00000040: {
				set_elemental_enchant_type(input.readInt32());
				break;
			}
			case 0x00000048: {
				set_elemental_enchant_value(input.readInt32());
				break;
			}
			case 0x00000050: {
				set_increase_prob(input.readInt32());
				break;
			}
			case 0x00000058: {
				set_all_enchants_allowed(input.readBool());
				break;
			}
			default: {
				System.out.println(String.format("[CraftInputItem] NEW_TAG : TAG(%d)", tag));
				return this;
			}
			}
		}
		return this;
	}

	@Override
	public void dispose() {
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}

