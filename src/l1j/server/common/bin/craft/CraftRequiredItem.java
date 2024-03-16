package l1j.server.common.bin.craft;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class CraftRequiredItem implements ProtoMessage {
	public static CraftRequiredItem newInstance() {
		return new CraftRequiredItem();
	}

	private int _name_id;
	private int _count;
	private boolean _is_nagative;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CraftRequiredItem() {
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

	public boolean get_is_nagative() {
		return _is_nagative;
	}

	public void set_is_nagative(boolean val) {
		_bit |= 0x4;
		_is_nagative = val;
	}

	public boolean has_is_nagative() {
		return (_bit & 0x4) == 0x4;
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
		if (has_is_nagative()){
			size += ProtoOutputStream.computeBoolSize(3, _is_nagative);
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
		if (!has_is_nagative()) {
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
		if (has_is_nagative()){
			output.writeBool(3, _is_nagative);
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
				set_is_nagative(input.readBool());
				break;
			}
			default: {
				System.out.println(String.format("[CraftRequiredItem] NEW_TAG : TAG(%d)", tag));
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

