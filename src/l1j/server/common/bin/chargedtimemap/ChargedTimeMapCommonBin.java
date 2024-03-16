package l1j.server.common.bin.chargedtimemap;

import java.util.HashMap;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class ChargedTimeMapCommonBin implements ProtoMessage {
	public static ChargedTimeMapCommonBin newInstance(){
		return new ChargedTimeMapCommonBin();
	}
	private HashMap<Integer, ChargedTimeMapCommonBinExtend> _extend;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private ChargedTimeMapCommonBin(){
	}
	public HashMap<Integer, ChargedTimeMapCommonBinExtend> get_extend(){
		return _extend;
	}
	public void add_extend(ChargedTimeMapCommonBinExtend val){
		if (!has_extend()) {
			_extend = new HashMap<Integer, ChargedTimeMapCommonBin.ChargedTimeMapCommonBinExtend>();
			_bit |= 0x1;
		}
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
		if (has_extend()) {
			for (ChargedTimeMapCommonBinExtend val : _extend.values()) {
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
			for (ChargedTimeMapCommonBinExtend val : _extend.values()) {
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
					add_extend((ChargedTimeMapCommonBinExtend) input.readMessage(ChargedTimeMapCommonBinExtend.newInstance()));
					break;
				default:
					System.out.println(String.format("[ChargedTimeMapCommonBin] NEW_TAG : TAG(%d)", tag));
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
	
	public static class ChargedTimeMapCommonBinExtend implements ProtoMessage{
		public static ChargedTimeMapCommonBinExtend newInstance(){
			return new ChargedTimeMapCommonBinExtend();
		}
		private int _id;
		private ChargedTimeMapDataT _data;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private ChargedTimeMapCommonBinExtend(){
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
		public ChargedTimeMapDataT get_data(){
			return _data;
		}
		public void set_data(ChargedTimeMapDataT val){
			_bit |= 0x2;
			_data = val;
		}
		public boolean has_data(){
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
			if (has_data()) {
				size += ProtoOutputStream.computeMessageSize(2, _data);
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
			if (!has_data()){
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
			if (has_data()){
				output.writeMessage(2, _data);
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
						set_data((ChargedTimeMapDataT) input.readMessage(ChargedTimeMapDataT.newInstance()));
						break;
					default:
						System.out.println(String.format("[ChargedTimeMapCommonBinExtend] NEW_TAG : TAG(%d)", tag));
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

