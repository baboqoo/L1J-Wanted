package l1j.server.common.bin.craft;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;
import l1j.server.server.model.Instance.L1PcInstance;

public class CraftAttr implements ProtoMessage {
	public static CraftAttr newInstance() {
		return new CraftAttr();
	}
	
	public boolean isValidation(L1PcInstance _pc, int create_count) {
		int level = _pc.getLevel();
		if ((_min_level != 0 && level < _min_level) 
				|| (_max_level != 0 && level > _max_level)) {
			return false;
		}
		int align = _pc.getAlignment();
		if ((_min_align != 0 && align < _min_align) 
				|| (_max_align != 0 && align > _max_align)) {
			return false;
		}
		int karma = _pc.getKarma();
		if ((_min_karma != 0 && karma < _min_karma) 
				|| (_max_karma != 0 && karma > _max_karma)) {
			return false;
		}
		if (_required_gender < 2 && _pc.getGender().toInt() != _required_gender) {
			return false;
		}
		if (_pccafe_only && !_pc.isPCCafe()) {
			return false;
		}
		if (_max_count < create_count) {
			return false;
		}
		return true;
	}

	private int _desc;
	private int _min_level;
	private int _max_level;
	private int _required_gender;
	private int _min_align;
	private int _max_align;
	private int _min_karma;
	private int _max_karma;
	private int _max_count;
	private boolean _show;
	private boolean _pccafe_only;
	private boolean _bm_prop_open;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CraftAttr() {
		set_show(false);
		set_bmProbOpen(false);
	}

	public int get_desc() {
		return _desc;
	}

	public void set_desc(int val) {
		_bit |= 0x1;
		_desc = val;
	}

	public boolean has_desc() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_min_level() {
		return _min_level;
	}

	public void set_min_level(int val) {
		_bit |= 0x2;
		_min_level = val;
	}

	public boolean has_min_level() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_max_level() {
		return _max_level;
	}

	public void set_max_level(int val) {
		_bit |= 0x4;
		_max_level = val;
	}

	public boolean has_max_level() {
		return (_bit & 0x4) == 0x4;
	}

	public int get_required_gender() {
		return _required_gender;
	}

	public void set_required_gender(int val) {
		_bit |= 0x8;
		_required_gender = val;
	}

	public boolean has_required_gender() {
		return (_bit & 0x8) == 0x8;
	}

	public int get_min_align() {
		return _min_align;
	}

	public void set_min_align(int val) {
		_bit |= 0x10;
		_min_align = val;
	}

	public boolean has_min_align() {
		return (_bit & 0x10) == 0x10;
	}

	public int get_max_align() {
		return _max_align;
	}

	public void set_max_align(int val) {
		_bit |= 0x20;
		_max_align = val;
	}

	public boolean has_max_align() {
		return (_bit & 0x20) == 0x20;
	}

	public int get_min_karma() {
		return _min_karma;
	}

	public void set_min_karma(int val) {
		_bit |= 0x40;
		_min_karma = val;
	}

	public boolean has_min_karma() {
		return (_bit & 0x40) == 0x40;
	}

	public int get_max_karma() {
		return _max_karma;
	}

	public void set_max_karma(int val) {
		_bit |= 0x80;
		_max_karma = val;
	}

	public boolean has_max_karma() {
		return (_bit & 0x80) == 0x80;
	}

	public int get_max_count() {
		return _max_count;
	}

	public void set_max_count(int val) {
		_bit |= 0x100;
		_max_count = val;
	}

	public boolean has_max_count() {
		return (_bit & 0x100) == 0x100;
	}

	public boolean get_show() {
		return _show;
	}

	public void set_show(boolean val) {
		_bit |= 0x200;
		_show = val;
	}

	public boolean has_show() {
		return (_bit & 0x200) == 0x200;
	}
	
	public boolean get_PCCafeOnly(){
		return _pccafe_only;
	}
	public void set_PCCafeOnly(boolean val){
		_bit |= 0x400;
		_pccafe_only = val;
	}
	public boolean has_PCCafeOnly(){
		return (_bit & 0x400) == 0x400;
	}
	
	public boolean get_bmProbOpen() {
		return _bm_prop_open;
	}

	public void set_bmProbOpen(boolean val) {
		_bit |= 0x800;
		_bm_prop_open = val;
	}

	public boolean has_bmProbOpen() {
		return (_bit & 0x800) == 0x800;
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
		if (has_desc()){
			size += ProtoOutputStream.computeInt32Size(1, _desc);
		}
		if (has_min_level()){
			size += ProtoOutputStream.computeInt32Size(2, _min_level);
		}
		if (has_max_level()){
			size += ProtoOutputStream.computeInt32Size(3, _max_level);
		}
		if (has_required_gender()){
			size += ProtoOutputStream.computeInt32Size(4, _required_gender);
		}
		if (has_min_align()){
			size += ProtoOutputStream.computeInt32Size(5, _min_align);
		}
		if (has_max_align()){
			size += ProtoOutputStream.computeInt32Size(6, _max_align);
		}
		if (has_min_karma()){
			size += ProtoOutputStream.computeInt32Size(7, _min_karma);
		}
		if (has_max_karma()){
			size += ProtoOutputStream.computeInt32Size(8, _max_karma);
		}
		if (has_max_count()){
			size += ProtoOutputStream.computeInt32Size(9, _max_count);
		}
		if (has_show()){
			size += ProtoOutputStream.computeBoolSize(10, _show);
		}
		if (has_PCCafeOnly()){
			size += ProtoOutputStream.computeBoolSize(11, _pccafe_only);
		}
		if (has_bmProbOpen()){
			size += ProtoOutputStream.computeBoolSize(12, _bm_prop_open);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_desc()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_min_level()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_max_level()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_required_gender()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_min_align()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_max_align()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_min_karma()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_max_karma()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_max_count()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_desc()){
			output.wirteInt32(1, _desc);
		}
		if (has_min_level()){
			output.wirteInt32(2, _min_level);
		}
		if (has_max_level()){
			output.wirteInt32(3, _max_level);
		}
		if (has_required_gender()){
			output.wirteInt32(4, _required_gender);
		}
		if (has_min_align()){
			output.wirteInt32(5, _min_align);
		}
		if (has_max_align()){
			output.wirteInt32(6, _max_align);
		}
		if (has_min_karma()){
			output.wirteInt32(7, _min_karma);
		}
		if (has_max_karma()){
			output.wirteInt32(8, _max_karma);
		}
		if (has_max_count()){
			output.wirteInt32(9, _max_count);
		}
		if (has_show()){
			output.writeBool(10, _show);
		}
		if (has_PCCafeOnly()){
			output.writeBool(11, _pccafe_only);
		}
		if (has_bmProbOpen()){
			output.writeBool(12, _bm_prop_open);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
			case 0x00000008: {
				set_desc(input.readInt32());
				break;
			}
			case 0x00000010: {
				set_min_level(input.readInt32());
				break;
			}
			case 0x00000018: {
				set_max_level(input.readInt32());
				break;
			}
			case 0x00000020: {
				set_required_gender(input.readInt32());
				break;
			}
			case 0x00000028: {
				set_min_align(input.readInt32());
				break;
			}
			case 0x00000030: {
				set_max_align(input.readInt32());
				break;
			}
			case 0x00000038: {
				set_min_karma(input.readInt32());
				break;
			}
			case 0x00000040: {
				set_max_karma(input.readInt32());
				break;
			}
			case 0x00000048: {
				set_max_count(input.readInt32());
				break;
			}
			case 0x00000050: {
				set_show(input.readBool());
				break;
			}
			case 0x00000058: {
				set_PCCafeOnly(input.readBool());
				break;
			}
			case 0x00000060: {
				set_bmProbOpen(input.readBool());
				break;
			}
			default: {
				System.out.println(String.format("[CraftAttr] NEW_TAG : TAG(%d)", tag));
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

