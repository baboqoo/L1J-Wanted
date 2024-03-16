package l1j.server.common.bin.craft;

import java.util.HashMap;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class CraftCommonBin implements ProtoMessage {
	public static CraftCommonBin newInstance() {
		return new CraftCommonBin();
	}

	private HashMap<Integer, CraftInfo> _craft_list;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CraftCommonBin() {
	}

	public HashMap<Integer, CraftInfo> get_craft_list() {
		return _craft_list;
	}
	
	public CraftInfo get_craft(int craftId) {
		return _craft_list.get(craftId);
	}

	public void add_craft(CraftInfo val) {
		if (!has_craft()) {
			_bit |= 0x1;
			_craft_list = new HashMap<Integer, CraftInfo>();
		}
		_craft_list.put(val.get_craft_id(), val);
	}

	public boolean has_craft() {
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
		if (has_craft()){
			for (CraftInfo val : _craft_list.values()) {
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
		
		if (has_craft()) {
			for (CraftInfo val : _craft_list.values()) {
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
		if (has_craft()){
			for (CraftInfo val : _craft_list.values()) {
				output.writeMessage(1, val);
			}
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
			case 0x00000012:
				add_craft((CraftInfo) input.readMessage(CraftInfo.newInstance()));
				break;
			default:
				System.out.println(String.format("[CraftCommonBin] NEW_TAG : TAG(%d)", tag));
				return this;
			}
		}
		return this;
	}

	@Override
	public void dispose() {
		if (has_craft()) {
			for (CraftInfo val : _craft_list.values()) {
				val.dispose();
			}
			_craft_list.clear();
			_craft_list = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	
}

