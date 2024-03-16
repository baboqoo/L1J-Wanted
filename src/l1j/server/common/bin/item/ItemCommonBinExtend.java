package l1j.server.common.bin.item;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class ItemCommonBinExtend implements ProtoMessage {
	public static ItemCommonBinExtend newInstance() {
		return new ItemCommonBinExtend();
	}
	
	private int _name_id;
	private CommonItemInfo _item;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private ItemCommonBinExtend() {
	}

	public int get_name_id(){
		return _name_id;
	}
	public void set_name_id(int name_id) {
		_name_id = name_id;
		_bit |= 0x1;
	}
	public boolean has_name_id(){
		return (_bit & 0x1) == 0x1;
	}
	
	public CommonItemInfo get_item() {
		return _item;
	}

	public void set_item(CommonItemInfo val) {
		_bit |= 0x2;
		_item = val;
	}

	public boolean has_item() {
		return (_bit & 0x2) == 0x2;
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
			size += ProtoOutputStream.computeUInt32Size(1, _name_id);
		}
		if (has_item()) {
			size += ProtoOutputStream.computeMessageSize(2, _item);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_item()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_name_id()){
			output.writeUInt32(1, _name_id);
		}
		if (has_item()){
			output.writeMessage(2, _item);
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
			case 0x00000012: {
				set_item((CommonItemInfo) input.readMessage(CommonItemInfo.newInstance()));
				break;
			}
			default: {
				System.out.println(String.format("[ItemCommonBinExtend] NEW_TAG : TAG(%d)", tag));
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

