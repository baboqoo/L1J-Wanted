package l1j.server.common.bin.pcmaster;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class PCMasterCommonBin implements ProtoMessage{
	public static PCMasterCommonBin newInstance(){
		return new PCMasterCommonBin();
	}
	private PCMasterCommonBinExtend _pc_master;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private PCMasterCommonBin(){
	}
	public PCMasterCommonBinExtend get_pc_master(){
		return _pc_master;
	}
	public void set_pc_master(PCMasterCommonBinExtend val) {
		_bit |= 0x1;
		_pc_master = val;
	}
	public boolean has_pc_master(){
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
		if (has_pc_master()) {
			size += ProtoOutputStream.computeMessageSize(1, _pc_master);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_pc_master()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_pc_master()){
			output.writeMessage(1, _pc_master);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000012:
					set_pc_master((PCMasterCommonBinExtend) input.readMessage(PCMasterCommonBinExtend.newInstance()));
					break;
				default:
					System.out.println(String.format("[PCMasterCommonBin] NEW_TAG : TAG(%d)", tag));
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
	
	public static class PCMasterCommonBinExtend implements ProtoMessage{
		public static PCMasterCommonBinExtend newInstance(){
			return new PCMasterCommonBinExtend();
		}
		private int _id;
		private PCMasterInfoForClient _info;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private PCMasterCommonBinExtend(){
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
		
		public PCMasterInfoForClient get_info() {
			return _info;
		}
		public void set_info(PCMasterInfoForClient val) {
			_bit |= 0x2;
			_info = val;
		}
		public boolean has_info() {
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
			if (!has_id()){
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
						set_info((PCMasterInfoForClient) input.readMessage(PCMasterInfoForClient.newInstance()));
						break;
					default:
						System.out.println(String.format("[PCMasterCommonBin.PCMasterCommonBinExtend] NEW_TAG : TAG(%d)", tag));
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

