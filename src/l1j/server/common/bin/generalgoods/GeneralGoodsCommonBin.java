package l1j.server.common.bin.generalgoods;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class GeneralGoodsCommonBin implements ProtoMessage {
	public static GeneralGoodsCommonBin newInstance(){
		return new GeneralGoodsCommonBin();
	}
	private GeneralGoodsCommonBinExtend _extend;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private GeneralGoodsCommonBin(){
	}
	public GeneralGoodsCommonBinExtend get_extend(){
		return _extend;
	}
	public void set_extend(GeneralGoodsCommonBinExtend val){
		_bit |= 0x1;
		_extend = val;
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
			size += ProtoOutputStream.computeMessageSize(1, _extend);
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
			output.writeMessage(1, _extend);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000012:
					set_extend((GeneralGoodsCommonBinExtend) input.readMessage(GeneralGoodsCommonBinExtend.newInstance()));
					break;
				default:
					System.out.println(String.format("[GeneralGoodsCommonBin] NEW_TAG : TAG(%d)", tag));
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
	
	public static class GeneralGoodsCommonBinExtend implements ProtoMessage{
		public static GeneralGoodsCommonBinExtend newInstance(){
			return new GeneralGoodsCommonBinExtend();
		}
		private int _id;
		private GeneralGoodsList _list;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private GeneralGoodsCommonBinExtend(){
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
		public GeneralGoodsList get_list(){
			return _list;
		}
		public void set_list(GeneralGoodsList val){
			_bit |= 0x2;
			_list = val;
		}
		public boolean has_list(){
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
			if (has_list()) {
				size += ProtoOutputStream.computeMessageSize(2, _list);
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
			if (!has_list()){
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
			if (has_list()){
				output.writeMessage(2, _list);
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
						set_list((GeneralGoodsList) input.readMessage(GeneralGoodsList.newInstance()));
						break;
					default:
						System.out.println(String.format("[GeneralGoodsCommonBinExtend] NEW_TAG : TAG(%d)", tag));
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

