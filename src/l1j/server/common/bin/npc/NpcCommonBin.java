package l1j.server.common.bin.npc;

import java.util.HashMap;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class NpcCommonBin implements ProtoMessage {
	public static NpcCommonBin newInstance() {
		return new NpcCommonBin();
	}
	
	private NpcCommonBin() {
	}
	
	private HashMap<Integer, NpcCommonBinExtend> _npc_list;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	
	public HashMap<Integer, NpcCommonBinExtend> get_npc_list() {
		return _npc_list;
	}

	public NpcCommonBinExtend getNpc(int class_id) {
		return _npc_list.get(class_id);
	}

	public void add_npc(NpcCommonBinExtend val) {
		if (!has_npc_list()) {
			_npc_list = new HashMap<Integer, NpcCommonBinExtend>();
			_bit |= 0x1;
		}
		_npc_list.put(val.get_class_id(), val);
	}

	public boolean has_npc_list() {
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
		if (has_npc_list()){
			for (NpcCommonBinExtend val : _npc_list.values()) {
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
		if (has_npc_list()) {
			for (NpcCommonBinExtend val : _npc_list.values()) {
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
		if (has_npc_list()){
			for (NpcCommonBinExtend val : _npc_list.values()) {
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
				add_npc((NpcCommonBinExtend) input.readMessage(NpcCommonBinExtend.newInstance()));
				break;
			}
			default: {
				System.out.println(String.format("[NpcCommonBin] NEW_TAG : TAG(%d)", tag));
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

