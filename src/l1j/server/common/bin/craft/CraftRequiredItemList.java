package l1j.server.common.bin.craft;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class CraftRequiredItemList implements ProtoMessage {
	public static CraftRequiredItemList newInstance() {
		return new CraftRequiredItemList();
	}

	private boolean _and_or;
	private int _count;
	private java.util.LinkedList<CraftRequiredItem> _items;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CraftRequiredItemList() {
	}

	public boolean get_and_or() {
		return _and_or;
	}

	public void set_and_or(boolean val) {
		_bit |= 0x1;
		_and_or = val;
	}

	public boolean has_and_or() {
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

	public java.util.LinkedList<CraftRequiredItem> get_items() {
		return _items;
	}

	public void add_items(CraftRequiredItem val) {
		if (!has_items()) {
			_items = new java.util.LinkedList<CraftRequiredItem>();
			_bit |= 0x4;
		}
		_items.add(val);
	}

	public boolean has_items() {
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
		if (has_and_or()){
			size += ProtoOutputStream.computeBoolSize(1, _and_or);
		}
		if (has_count()){
			size += ProtoOutputStream.computeInt32Size(2, _count);
		}
		if (has_items()){
			for(CraftRequiredItem val : _items){
				size += ProtoOutputStream.computeMessageSize(3, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_and_or()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_count()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_items()) {
			for (CraftRequiredItem val : _items) {
				if (!val.isInitialized()) {
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_and_or()){
			output.writeBool(1, _and_or);
		}
		if (has_count()){
			output.wirteInt32(2, _count);
		}
		if (has_items()){
			for (CraftRequiredItem val : _items){
				output.writeMessage(3, val);
			}
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
			case 0x00000008: {
				set_and_or(input.readBool());
				break;
			}
			case 0x00000010: {
				set_count(input.readInt32());
				break;
			}
			case 0x0000001A: {
				add_items((CraftRequiredItem) input.readMessage(CraftRequiredItem.newInstance()));
				break;
			}
			default: {
				System.out.println(String.format("[CraftRequiredItemList] NEW_TAG : TAG(%d)", tag));
				return this;
			}
			}
		}
		return this;
	}

	@Override
	public void dispose() {
		if (has_items()) {
			for (CraftRequiredItem val : _items)
				val.dispose();
			_items.clear();
			_items = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}

}

