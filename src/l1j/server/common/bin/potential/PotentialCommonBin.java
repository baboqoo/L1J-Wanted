package l1j.server.common.bin.potential;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class PotentialCommonBin implements ProtoMessage{
	public static PotentialCommonBin newInstance(){
		return new PotentialCommonBin();
	}

	private PotentialCommonBinExtend _potential;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private PotentialCommonBin(){
	}

	public PotentialCommonBinExtend get_potential(){
		return _potential;
	}
	public void set_potential(PotentialCommonBinExtend val){
		_bit |= 0x1;
		_potential = val;
	}
	public boolean has_potential(){
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
		if (has_potential()) {
			size += ProtoOutputStream.computeMessageSize(1, _potential);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_potential()){
			output.writeMessage(1, _potential);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000012:{
					set_potential((PotentialCommonBinExtend)input.readMessage(PotentialCommonBinExtend.newInstance()));
					break;
				}
				default:{
					System.out.println(String.format("[PotentialCommonBin] NEW_TAG : TAG(%d)", tag));
					return this;
				}
			}
		}
		return this;
	}

	@Override
	public void dispose(){
		if (has_potential()) {
			_potential.dispose();
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	
	public static class PotentialCommonBinExtend implements ProtoMessage{
		public static PotentialCommonBinExtend newInstance(){
			return new PotentialCommonBinExtend();
		}

		private int _id;
		private CommonPotentialInfo _potential;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private PotentialCommonBinExtend(){
		}

		public int get_id(){
			return _id;
		}
		public void set_id(int val){
			_bit |= 0x1;
			_id = val;
		}
		public boolean has_id(){
			return (_bit & 0x1) == 0x1;
		}
		
		public CommonPotentialInfo get_potential(){
			return _potential;
		}
		public void set_potential(CommonPotentialInfo val){
			_bit |= 0x2;
			_potential = val;
		}
		public boolean has_potential(){
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
			if (has_potential()) {
				size += ProtoOutputStream.computeMessageSize(2, _potential);
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
			if (!has_potential()) {
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
			if (has_potential()){
				output.writeMessage(2, _potential);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_id(input.readInt32());
						break;
					}
					case 0x00000012:{
						set_potential((CommonPotentialInfo)input.readMessage(CommonPotentialInfo.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[PotentialCommonBinExtend] NEW_TAG : TAG(%d)", tag));
						return this;
					}
				}
			}
			return this;
		}

		@Override
		public void dispose(){
			if (has_potential()) {
				_potential.dispose();
			}
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
	}
}

