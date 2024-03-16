package l1j.server.common.bin.favorbook;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class FavorBookCommonBin implements ProtoMessage {
	public static FavorBookCommonBin newInstance(){
		return new FavorBookCommonBin();
	}
	private FavorBookCommonBinExtend _extend;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private FavorBookCommonBin(){
	}
	public FavorBookCommonBinExtend get_extend(){
		return _extend;
	}
	public void set_extend(FavorBookCommonBinExtend val){
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
					set_extend((FavorBookCommonBinExtend) input.readMessage(FavorBookCommonBinExtend.newInstance()));
					break;
				default:
					System.out.println(String.format("[FavorBookCommonBin] NEW_TAG : TAG(%d)", tag));
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
	
	public static class FavorBookCommonBinExtend implements ProtoMessage{
		public static FavorBookCommonBinExtend newInstance(){
			return new FavorBookCommonBinExtend();
		}
		private int _id;
		private AUBIBookInfoForClient _favor;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private FavorBookCommonBinExtend(){
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
		public AUBIBookInfoForClient get_favor(){
			return _favor;
		}
		public void set_favor(AUBIBookInfoForClient val){
			_bit |= 0x2;
			_favor = val;
		}
		public boolean has_favor(){
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
			if (has_favor()) {
				size += ProtoOutputStream.computeMessageSize(2, _favor);
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
			if (!has_favor()){
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
			if (has_favor()){
				output.writeMessage(2, _favor);
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
						set_favor((AUBIBookInfoForClient) input.readMessage(AUBIBookInfoForClient.newInstance()));
						break;
					default:
						System.out.println(String.format("[FavorBookCommonBinExtend] NEW_TAG : TAG(%d)", tag));
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

