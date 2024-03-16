package l1j.server.common.bin.huntingquest;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class HuntingQuestCommonBin implements ProtoMessage{
	public static HuntingQuestCommonBin newInstance(){
		return new HuntingQuestCommonBin();
	}
	private HuntingQuestCommonBinExtend _quest;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private HuntingQuestCommonBin(){
	}

	public HuntingQuestCommonBinExtend get_quest(){
		return _quest;
	}
	public void set_quest(HuntingQuestCommonBinExtend val){
		_bit |= 0x1;
		_quest = val;
	}
	public boolean has_quest(){
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
		if (has_quest()) {
			size += ProtoOutputStream.computeMessageSize(1, _quest);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_quest()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_quest()){
			output.writeMessage(1, _quest);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000012:{
					set_quest((HuntingQuestCommonBinExtend)input.readMessage(HuntingQuestCommonBinExtend.newInstance()));
					break;
				}
				default:{
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
	
	public static class HuntingQuestCommonBinExtend implements ProtoMessage{
		public static HuntingQuestCommonBinExtend newInstance(){
			return new HuntingQuestCommonBinExtend();
		}
		private int _id;
		private HuntingQuestConfigT _config;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private HuntingQuestCommonBinExtend(){
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

		public HuntingQuestConfigT get_config(){
			return _config;
		}
		public void set_config(HuntingQuestConfigT val){
			_bit |= 0x2;
			_config = val;
		}
		public boolean has_config(){
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
			if (has_config()) {
				size += ProtoOutputStream.computeMessageSize(2, _config);
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
			if (!has_config()){
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
			if (has_config()){
				output.writeMessage(2, _config);
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
						set_config((HuntingQuestConfigT)input.readMessage(HuntingQuestConfigT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[HuntingQuestCommonBin] NEW_TAG : TAG(%d)", tag));
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
	}
}

