package l1j.server.common.bin.einhasadpoint;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class EinhasadPointCommonBin implements ProtoMessage{
	public static EinhasadPointCommonBin newInstance(){
		return new EinhasadPointCommonBin();
	}
	private EinhasadPointCommonBinExtend _data;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private EinhasadPointCommonBin(){
	}

	public EinhasadPointCommonBinExtend get_data(){
		return _data;
	}
	public void set_data(EinhasadPointCommonBinExtend val){
		_data = val;
		_bit |= 0x1;
	}
	public boolean has_data(){
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
		if (has_data()) {
			size += ProtoOutputStream.computeMessageSize(1, _data);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_data()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_data()){
			output.writeMessage(1, _data);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000012:
					set_data((EinhasadPointCommonBinExtend)input.readMessage(EinhasadPointCommonBinExtend.newInstance()));
					break;
				default:
					System.out.println(String.format("[EinhasadPointCommonBinExtend] NEW_TAG : TAG(%d)", tag));
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
	
	public static class EinhasadPointCommonBinExtend implements ProtoMessage{
		public static EinhasadPointCommonBinExtend newInstance(){
			return new EinhasadPointCommonBinExtend();
		}
		private int _id;
		private EinhasadPointStatInfoT _info;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private EinhasadPointCommonBinExtend(){
		}
		public int get_id() {
			return _id;
		}
		public void set_id(int id){
			_id = id;
			_bit |= 0x1;
		}
		public boolean has_id(){
			return (_bit & 0x1) == 0x1;
		}
		public EinhasadPointStatInfoT get_info(){
			return _info;
		}
		public void set_info(EinhasadPointStatInfoT val){
			_info = val;
			_bit |= 0x2;
		}
		public boolean has_info(){
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
			if (has_info()) {
				size += ProtoOutputStream.computeMessageSize(2, _info);
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
			if (!has_info()){
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
			if (has_info()){
				output.writeMessage(2, _info);
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
						set_info((EinhasadPointStatInfoT)input.readMessage(EinhasadPointStatInfoT.newInstance()));
						break;
					default:
						System.out.println(String.format("[EinhasadPointCommonBinExtend] NEW_TAG : TAG(%d)", tag));
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

