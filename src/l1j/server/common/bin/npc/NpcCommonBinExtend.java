package l1j.server.common.bin.npc;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class NpcCommonBinExtend implements ProtoMessage {
	public static NpcCommonBinExtend newInstance() {
		return new NpcCommonBinExtend();
	}
	
	private int _class_id;
	private CommonNPCInfo _npc;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private NpcCommonBinExtend() {
	}

	public int get_class_id() {
		return _class_id;
	}
	public void set_class_id(int id) {
		_class_id = id;
		_bit |= 0x1;
	}
	public boolean has_class_id() {
		return (_bit & 0x1) == 0x1;
	}
	
	public CommonNPCInfo get_npc() {
		return _npc;
	}

	public void set_npc(CommonNPCInfo val) {
		_bit |= 0x2;
		_npc = val;
	}

	public boolean has_npc() {
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
		if (has_class_id()){
			size += ProtoOutputStream.computeUInt32Size(1, _class_id);
		}
		if (has_npc()) {
			size += ProtoOutputStream.computeMessageSize(2, _npc);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_class_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_npc()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_class_id()){
			output.writeUInt32(1, _class_id);
		}
		if (has_npc()){
			output.writeMessage(2, _npc);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
			case 0x00000008:{
				set_class_id(input.readUInt32());
				break;
			}
			case 0x00000012: {
				set_npc((CommonNPCInfo) input.readMessage(CommonNPCInfo.newInstance()));
				break;
			}
			default: {
				System.out.println(String.format("[NpcCommonBinExtend] NEW_TAG : TAG(%d)", tag));
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

