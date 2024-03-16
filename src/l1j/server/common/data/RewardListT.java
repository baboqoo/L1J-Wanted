package l1j.server.common.data;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class RewardListT implements ProtoMessage{
	public static RewardListT newInstance(){
		return new RewardListT();
	}
	private java.util.LinkedList<RewardT> _Reward;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private RewardListT(){
	}
	public java.util.LinkedList<RewardT> get_Reward(){
		return _Reward;
	}
	public void add_Reward(RewardT val){
		if(!has_Reward()){
			_Reward = new java.util.LinkedList<RewardT>();
			_bit |= 0x1;
		}
		_Reward.add(val);
	}
	public boolean has_Reward(){
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
		if (has_Reward()){
			for(RewardT val : _Reward){
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
		if (has_Reward()){
			for(RewardT val : _Reward){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_Reward()){
			for (RewardT val : _Reward){
				output.writeMessage(1, val);
			}
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x0000000A:{
					add_Reward((RewardT)input.readMessage(RewardT.newInstance()));
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
}

