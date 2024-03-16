package l1j.server.common.bin.craft;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class CraftInfo implements ProtoMessage {
	public static CraftInfo newInstance() {
		return new CraftInfo();
	}

	private int _craf_id;
	private Craft _craft;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CraftInfo() {
	}

	public int get_craft_id() {
		return _craf_id;
	}

	public void set_craft_id(int val) {
		_bit |= 0x1;
		_craf_id = val;
	}

	public boolean has_craft_id() {
		return (_bit & 0x1) == 0x1;
	}

	public Craft get_craft() {
		return _craft;
	}

	public void set_craft(Craft val) {
		_bit |= 0x2;
		_craft = val;
	}

	public boolean has_craft() {
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
		if (has_craft_id()){
			size += ProtoOutputStream.computeUInt32Size(1, _craf_id);
		}
		if (has_craft()) {
			size += ProtoOutputStream.computeMessageSize(2, _craft);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_craft_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_craft()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_craft_id()){
			output.writeUInt32(1, _craf_id);
		}
		if (has_craft()){
			output.writeMessage(2, _craft);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
			case 0x00000008: {
				set_craft_id(input.readInt32());
				break;
			}
			case 0x00000012: {
				set_craft((Craft) input.readMessage(Craft.newInstance()));
				break;
			}
			default: {
				System.out.println(String.format("[CraftInfo] NEW_TAG : TAG(%d)", tag));
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

