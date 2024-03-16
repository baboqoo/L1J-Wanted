package l1j.server.common.bin.treasureisland;

import java.util.HashMap;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class TreasureIslandBoxCommonBin implements ProtoMessage {
	public static TreasureIslandBoxCommonBin newInstance(){
		return new TreasureIslandBoxCommonBin();
	}
	private HashMap<Integer, TreasureIslandBoxCommonBinExtend> _extend;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private TreasureIslandBoxCommonBin(){
	}
	public HashMap<Integer, TreasureIslandBoxCommonBinExtend> get_extend(){
		return _extend;
	}
	public void add_extend(TreasureIslandBoxCommonBinExtend val){
		if (!has_extend()) {
			_extend = new HashMap<>();
			_bit |= 0x1;
		}
		_bit |= 0x1;
		_extend.put(val.get_id(), val);
	}
	public boolean has_extend(){
		return (_bit & 0x1) == 0x1;
	}
	@Override
	public long getInitializeBit(){
		return (long)_bit;
	}
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;
	}
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_extend()){
			for (TreasureIslandBoxCommonBinExtend val : _extend.values()) {
				size += ProtoOutputStream.computeMessageSize(1, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_extend()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_extend()){
			for (TreasureIslandBoxCommonBinExtend val : _extend.values()) {
				output.writeMessage(1, val);
			}
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000012:
					add_extend((TreasureIslandBoxCommonBinExtend) input.readMessage(TreasureIslandBoxCommonBinExtend.newInstance()));
					break;
				default:
					System.out.println(String.format("[TreasureIslandBoxCommonBin] NEW_TAG : TAG(%d)", tag));
					return this;
			}
		}
		return this;
	}

	@Override
	public void dispose(){
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	
	public static class TreasureIslandBoxCommonBinExtend implements ProtoMessage{
		public static TreasureIslandBoxCommonBinExtend newInstance(){
			return new TreasureIslandBoxCommonBinExtend();
		}
		private int _id;
		private TreasureIslandBox.TreasureBoxInfoListT.TreasureBoxInfoT.BoxT _box;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private TreasureIslandBoxCommonBinExtend(){
		}

		public int get_id() {
			return _id;
		}
		public void set_id(int val) {
			_bit |= 0x1;
			_id = val;
		}
		public boolean has_id() {
			return (_bit & 0x1) == 0x1;
		}
		public TreasureIslandBox.TreasureBoxInfoListT.TreasureBoxInfoT.BoxT get_box(){
			return _box;
		}
		public void set_box(TreasureIslandBox.TreasureBoxInfoListT.TreasureBoxInfoT.BoxT val){
			_bit |= 0x2;
			_box = val;
		}
		public boolean has_box(){
			return (_bit & 0x2) == 0x2;
		}
		@Override
		public long getInitializeBit(){
			return (long)_bit;
		}
		@Override
		public int getMemorizedSerializeSizedSize(){
			return _memorizedSerializedSize;
		}
		@Override
		public int getSerializedSize(){
			int size = 0;
			if (has_id()){
				size += ProtoOutputStream.computeUInt32Size(1, _id);
			}
			if (has_box()) {
				size += ProtoOutputStream.computeMessageSize(2, _box);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_id()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_box()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_id()){
				output.writeUInt32(1, _id);
			}
			if (has_box()){
				output.writeMessage(2, _box);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:
						set_id(input.readInt32());
						break;
					case 0x00000012:
						set_box((TreasureIslandBox.TreasureBoxInfoListT.TreasureBoxInfoT.BoxT) input.readMessage(TreasureIslandBox.TreasureBoxInfoListT.TreasureBoxInfoT.BoxT.newInstance()));
						break;
					default:
						System.out.println(String.format("[TreasureIslandBoxCommonBinExtend] NEW_TAG : TAG(%d)", tag));
						return this;
				}
			}
			return this;
		}

		@Override
		public void dispose(){
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
	}
}

