package l1j.server.common.bin.craft;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class CraftOutputItem implements ProtoMessage {
	public static CraftOutputItem newInstance() {
		return new CraftOutputItem();
	}

	private int _name_id;
	private int _count;
	private int _slot;
	private int _enchant;
	private int _bless;
	private int _elemental_type;
	private int _elemental_level;
	private String _desc;
	private int _system_desc;
	private int _broadcast_desc;
	private int _iconId;
	private String _url;
	private byte[] _extra_desc;
	private int _inherit_enchant_from;
	private int _inherit_elemental_enchant_from;
	private int _event_id;
	private int _inherit_bless_from;
	private int _attribute_bit_set;
	private long _attribute_bit_set_ex;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CraftOutputItem() {
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

	public int get_elemental_type() {
		return _elemental_type;
	}
	public void set_elemental_type(int val) {
		_bit |= 0x20;
		_elemental_type = val;
	}
	public boolean has_elemental_type() {
		return (_bit & 0x20) == 0x20;
	}

	public int get_elemental_level() {
		return _elemental_level;
	}
	public void set_elemental_level(int val) {
		_bit |= 0x40;
		_elemental_level = val;
	}
	public boolean has_elemental_level() {
		return (_bit & 0x40) == 0x40;
	}

	public String get_desc() {
		return _desc;
	}
	public void set_desc(String val) {
		_bit |= 0x80;
		_desc = val;
	}
	public boolean has_desc() {
		return (_bit & 0x80) == 0x80;
	}

	public int get_system_desc() {
		return _system_desc;
	}
	public void set_system_desc(int val) {
		_bit |= 0x100;
		_system_desc = val;
	}
	public boolean has_system_desc() {
		return (_bit & 0x100) == 0x100;
	}

	public int get_broadcast_desc() {
		return _broadcast_desc;
	}
	public void set_broadcast_desc(int val) {
		_bit |= 0x200;
		_broadcast_desc = val;
	}
	public boolean has_broadcast_desc() {
		return (_bit & 0x200) == 0x200;
	}

	public int get_iconId() {
		return _iconId;
	}
	public void set_iconId(int val) {
		_bit |= 0x400;
		_iconId = val;
	}
	public boolean has_iconId() {
		return (_bit & 0x400) == 0x400;
	}

	public String get_url() {
		return _url;
	}
	public void set_url(String val) {
		_bit |= 0x800;
		_url = val;
	}
	public boolean has_url() {
		return (_bit & 0x800) == 0x800;
	}

	public byte[] get_extra_desc() {
		return _extra_desc;
	}
	public void set_extra_desc(byte[] val) {
		_bit |= 0x1000;
		_extra_desc = val;
	}
	public boolean has_extra_desc() {
		return (_bit & 0x1000) == 0x1000;
	}

	public int get_inherit_enchant_from() {
		return _inherit_enchant_from;
	}
	public void set_inherit_enchant_from(int val) {
		_bit |= 0x2000;
		_inherit_enchant_from = val;
	}
	public boolean has_inherit_enchant_from() {
		return (_bit & 0x2000) == 0x2000;
	}

	public int get_inherit_elemental_enchant_from() {
		return _inherit_elemental_enchant_from;
	}
	public void set_inherit_elemental_enchant_from(int val) {
		_bit |= 0x4000;
		_inherit_elemental_enchant_from = val;
	}
	public boolean has_inherit_elemental_enchant_from() {
		return (_bit & 0x4000) == 0x4000;
	}

	public int get_event_id() {
		return _event_id;
	}
	public void set_event_id(int val) {
		_bit |= 0x8000;
		_event_id = val;
	}
	public boolean has_event_id() {
		return (_bit & 0x8000) == 0x8000;
	}

	public int get_inherit_bless_from() {
		return _inherit_bless_from;
	}
	public void set_inherit_bless_from(int val) {
		_bit |= 0x10000;
		_inherit_bless_from = val;
	}
	public boolean has_inherit_bless_from() {
		return (_bit & 0x10000) == 0x10000;
	}
	
	public int get_attribute_bit_set(){
		return _attribute_bit_set;
	}
	public void set_attribute_bit_set(int val){
		_bit |= 0x20000;
		_attribute_bit_set = val;
	}
	public boolean has_attribute_bit_set(){
		return (_bit & 0x20000) == 0x20000;
	}
	public long get_attribute_bit_set_ex(){
		return _attribute_bit_set_ex;
	}
	public void set_attribute_bit_set_ex(long val){
		_bit |= 0x40000;
		_attribute_bit_set_ex = val;
	}
	public boolean has_attribute_bit_set_ex(){
		return (_bit & 0x40000) == 0x40000;
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
		if (has_elemental_type()){
			size += ProtoOutputStream.computeInt32Size(6, _elemental_type);
		}
		if (has_elemental_level()){
			size += ProtoOutputStream.computeInt32Size(7, _elemental_level);
		}
		if (has_desc()){
			size += ProtoOutputStream.computeStringSize(8, _desc);
		}
		if (has_system_desc()){
			size += ProtoOutputStream.computeInt32Size(9, _system_desc);
		}
		if (has_broadcast_desc()){
			size += ProtoOutputStream.computeInt32Size(10, _broadcast_desc);
		}
		if (has_iconId()){
			size += ProtoOutputStream.computeInt32Size(11, _iconId);
		}
		if (has_url()){
			size += ProtoOutputStream.computeStringSize(12, _url);
		}
		if (has_extra_desc()){
			size += ProtoOutputStream.computeBytesSize(13, _extra_desc);
		}
		if (has_inherit_enchant_from()){
			size += ProtoOutputStream.computeInt32Size(14, _inherit_enchant_from);
		}
		if (has_inherit_elemental_enchant_from()){
			size += ProtoOutputStream.computeInt32Size(15, _inherit_elemental_enchant_from);
		}
		if (has_event_id()){
			size += ProtoOutputStream.computeInt32Size(16, _event_id);
		}
		if (has_inherit_bless_from()){
			size += ProtoOutputStream.computeInt32Size(17, _inherit_bless_from);
		}
		if (has_attribute_bit_set()){
			size += ProtoOutputStream.computeInt32Size(18, _attribute_bit_set);
		}
		if (has_attribute_bit_set_ex()){
			size += ProtoOutputStream.computeUInt64Size(19, _attribute_bit_set_ex);
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
		if (!has_elemental_type()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_elemental_level()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_desc()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_broadcast_desc()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_iconId()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_url()) {
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
		if (has_elemental_type()){
			output.wirteInt32(6, _elemental_type);
		}
		if (has_elemental_level()){
			output.wirteInt32(7, _elemental_level);
		}
		if (has_desc()){
			output.writeString(8, _desc);
		}
		if (has_system_desc()){
			output.wirteInt32(9, _system_desc);
		}
		if (has_broadcast_desc()){
			output.wirteInt32(10, _broadcast_desc);
		}
		if (has_iconId()){
			output.wirteInt32(11, _iconId);
		}
		if (has_url()){
			output.writeString(12, _url);
		}
		if (has_extra_desc()){
			output.writeBytes(13, _extra_desc);
		}
		if (has_inherit_enchant_from()){
			output.wirteInt32(14, _inherit_enchant_from);
		}
		if (has_inherit_elemental_enchant_from()){
			output.wirteInt32(15, _inherit_elemental_enchant_from);
		}
		if (has_event_id()){
			output.wirteInt32(16, _event_id);
		}
		if (has_inherit_bless_from()){
			output.wirteInt32(17, _inherit_bless_from);
		}
		if (has_attribute_bit_set()){
			output.wirteInt32(18, _attribute_bit_set);
		}
		if (has_attribute_bit_set_ex()){
			output.wirteUInt64(19, _attribute_bit_set_ex);
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
			case 0x00000030: {
				set_elemental_type(input.readInt32());
				break;
			}
			case 0x00000038: {
				set_elemental_level(input.readInt32());
				break;
			}
			case 0x00000042: {
				set_desc(input.readString());
				break;
			}
			case 0x00000048: {
				set_system_desc(input.readInt32());
				break;
			}
			case 0x00000050: {
				set_broadcast_desc(input.readInt32());
				break;
			}
			case 0x00000058: {
				set_iconId(input.readInt32());
				break;
			}
			case 0x00000062: {
				set_url(input.readString());
				break;
			}
			case 0x0000006A: {
				set_extra_desc(input.readBytes());
				break;
			}
			case 0x00000070: {
				set_inherit_enchant_from(input.readInt32());
				break;
			}
			case 0x00000078: {
				set_inherit_elemental_enchant_from(input.readInt32());
				break;
			}
			case 0x00000080: {
				set_event_id(input.readInt32());
				break;
			}
			case 0x00000088: {
				set_inherit_bless_from(input.readInt32());
				break;
			}
			case 0x00000090:{
				set_attribute_bit_set(input.readInt32());
				break;
			}
			case 0x00000098:{
				set_attribute_bit_set_ex(input.readUInt64());
				break;
			}
			default: {
				System.out.println(String.format("[CraftOutputItem] NEW_TAG : TAG(%d)", tag));
				return this;
			}
			}
		}
		return this;
	}
	
	public static final int aa = 0x00000098;

	@Override
	public void dispose() {
		_bit = 0;
		_memorizedIsInitialized = -1;
	}

}

