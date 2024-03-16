package l1j.server.common.bin.item;

import java.util.HashMap;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class ItemCommonBin implements ProtoMessage {
	public static ItemCommonBin newInstance() {
		return new ItemCommonBin();
	}
	
	private HashMap<Integer, ItemCommonBinExtend> _item_list;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private ItemCommonBin() {
	}

	public HashMap<Integer, ItemCommonBinExtend> get_item_list() {
		return _item_list;
	}

	public ItemCommonBinExtend getItem(int name_id) {
		return _item_list.get(name_id);
	}

	public void add_item(ItemCommonBinExtend val) {
		if (!has_item_list()) {
			_item_list = new HashMap<Integer, ItemCommonBinExtend>();
			_bit |= 0x1;
		}
		_item_list.put(val.get_name_id(), val);
	}

	public boolean has_item_list() {
		return (_bit & 0x1) == 0x1;
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
		if (has_item_list()){
			for (ItemCommonBinExtend val : _item_list.values()) {
				size += ProtoOutputStream.computeMessageSize(1, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (has_item_list()) {
			for (ItemCommonBinExtend val : _item_list.values()) {
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
		if (has_item_list()){
			for (ItemCommonBinExtend val : _item_list.values()) {
				output.writeMessage(1, val);
			}
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException {
		
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
			case 0x00000012: {
				add_item((ItemCommonBinExtend) input.readMessage(ItemCommonBinExtend.newInstance()));
				break;
			}
			default: {
				System.out.println(String.format("[ItemCommonBin] NEW_TAG : TAG(%d)", tag));
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

