package l1j.server.common.bin.treasureisland;

import java.util.HashMap;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class TreasureIslandBoxRewardCommonBin implements ProtoMessage {
	public static TreasureIslandBoxRewardCommonBin newInstance(){
		return new TreasureIslandBoxRewardCommonBin();
	}
	private HashMap<Integer, TreasureIslandBoxRewardCommonBinExtend> _extend;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private TreasureIslandBoxRewardCommonBin(){
	}
	public HashMap<Integer, TreasureIslandBoxRewardCommonBinExtend> get_extend(){
		return _extend;
	}
	public void add_extend(TreasureIslandBoxRewardCommonBinExtend val){
		if (!has_extend()) {
			_extend = new HashMap<>();
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
		if (has_extend()){
			for (TreasureIslandBoxRewardCommonBinExtend val : _extend.values()) {
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
			for (TreasureIslandBoxRewardCommonBinExtend val : _extend.values()) {
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
					add_extend((TreasureIslandBoxRewardCommonBinExtend) input.readMessage(TreasureIslandBoxRewardCommonBinExtend.newInstance()));
					break;
				default:
					System.out.println(String.format("[TreasureIslandBoxRewardCommonBin] NEW_TAG : TAG(%d)", tag));
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
	
	public static class TreasureIslandBoxRewardCommonBinExtend implements ProtoMessage{
		public static TreasureIslandBoxRewardCommonBinExtend newInstance(){
			return new TreasureIslandBoxRewardCommonBinExtend();
		}
		private int _id;
		private TreasureIslandBox.RewardBoxInfoListT.RewardBoxInfoT.RewardBoxT _reward;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private TreasureIslandBoxRewardCommonBinExtend(){
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
		public TreasureIslandBox.RewardBoxInfoListT.RewardBoxInfoT.RewardBoxT get_reward(){
			return _reward;
		}
		public void set_reward(TreasureIslandBox.RewardBoxInfoListT.RewardBoxInfoT.RewardBoxT val){
			_bit |= 0x2;
			_reward = val;
		}
		public boolean has_reward(){
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
			if (has_reward()) {
				size += ProtoOutputStream.computeMessageSize(2, _reward);
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
			if (!has_reward()){
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
			if (has_reward()){
				output.writeMessage(2, _reward);
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
						set_reward((TreasureIslandBox.RewardBoxInfoListT.RewardBoxInfoT.RewardBoxT) input.readMessage(TreasureIslandBox.RewardBoxInfoListT.RewardBoxInfoT.RewardBoxT.newInstance()));
						break;
					default:
						System.out.println(String.format("[TreasureIslandBoxRewardCommonBinExtend] NEW_TAG : TAG(%d)", tag));
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

