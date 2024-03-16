package l1j.server.common.bin.catalyst;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class CatalystTableCommonBin implements ProtoMessage{
	public static CatalystTableCommonBin newInstance(){
		return new CatalystTableCommonBin();
	}
	private CatalystTableCommonBinExtend _catalyst;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CatalystTableCommonBin(){
	}
	public CatalystTableCommonBinExtend get_catalyst(){
		return _catalyst;
	}
	public void set_catalyst(CatalystTableCommonBinExtend val){
		_bit |= 0x1;
		_catalyst = val;
	}
	public boolean has_catalyst(){
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
		if (has_catalyst()){
			size += ProtoOutputStream.computeMessageSize(1, _catalyst);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_catalyst()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_catalyst()){
			output.writeMessage(1, _catalyst);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000012:{
					set_catalyst((CatalystTableCommonBinExtend)input.readMessage(CatalystTableCommonBinExtend.newInstance()));
					break;
				}
				default:{
					System.out.println(String.format("[CatalystTableCommonBin] NEW_TAG : TAG(%d)", tag));
					return this;
				}
			}
		}
		return this;
	}

	@Override
	public void dispose(){
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	
	public static class CatalystTableCommonBinExtend implements ProtoMessage{
		public static CatalystTableCommonBinExtend newInstance(){
			return new CatalystTableCommonBinExtend();
		}
		private int _id;
		private CatalystTableT _catalyst;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private CatalystTableCommonBinExtend(){
		}
		public int get_id() {
			return _id;
		}
		public void set_id(int id) {
			_id = id;
			_bit |= 0x1;
		}
		public boolean has_id() {
			return (_bit & 0x1) == 0x1;
		}
		
		public CatalystTableT get_catalyst(){
			return _catalyst;
		}
		public void set_catalyst(CatalystTableT val){
			_bit |= 0x2;
			_catalyst = val;
		}
		public boolean has_catalyst(){
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
			if (has_catalyst()) {
				size += ProtoOutputStream.computeMessageSize(2, _catalyst);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_id()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_catalyst()){
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
			if (has_catalyst()){
				output.writeMessage(2, _catalyst);
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
					set_catalyst((CatalystTableT)input.readMessage(CatalystTableT.newInstance()));
					break;
				default:
					System.out.println(String.format("[CatalystTableCommonBinExtend] NEW_TAG : TAG(%d)", tag));
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

