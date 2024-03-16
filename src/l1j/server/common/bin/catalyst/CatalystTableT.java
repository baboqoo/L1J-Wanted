package l1j.server.common.bin.catalyst;

import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class CatalystTableT implements ProtoMessage{
	public static CatalystTableT newInstance(){
		return new CatalystTableT();
	}
	private java.util.LinkedList<CatalystTableT.CatalystRewardInfoT> _rewardList;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CatalystTableT(){
	}
	public java.util.LinkedList<CatalystTableT.CatalystRewardInfoT> get_rewardList(){
		return _rewardList;
	}
	public void add_rewardList(CatalystTableT.CatalystRewardInfoT val){
		if(!has_rewardList()){
			_rewardList = new java.util.LinkedList<CatalystTableT.CatalystRewardInfoT>();
			_bit |= 0x1;
		}
		_rewardList.add(val);
	}
	public boolean has_rewardList(){
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
		if (has_rewardList()){
			for(CatalystTableT.CatalystRewardInfoT val : _rewardList){
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
		if (has_rewardList()){
			for(CatalystTableT.CatalystRewardInfoT val : _rewardList){
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
		if (has_rewardList()){
			for (CatalystTableT.CatalystRewardInfoT val : _rewardList){
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
					add_rewardList((CatalystTableT.CatalystRewardInfoT)input.readMessage(CatalystTableT.CatalystRewardInfoT.newInstance()));
					break;
				}
				default:{
					System.out.println(String.format("[CatalystTableT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class CatalystRewardInfoT implements ProtoMessage{
		public static CatalystRewardInfoT newInstance(){
			return new CatalystRewardInfoT();
		}
		private int _nameId;
		private int _input;
		private int _output;
		private int _successProb;
		private int _rewardCount;
		private int _preserveProb;
		private int _failOutput;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private CatalystRewardInfoT(){
		}
		public CatalystRewardInfoT(ResultSet rs) throws SQLException {
			_nameId			= rs.getInt("nameId");
			_input			= rs.getInt("input");
			_output			= rs.getInt("output");
			_successProb	= rs.getInt("successProb");
			_rewardCount	= rs.getInt("rewardCount");
			_preserveProb	= rs.getInt("preserveProb");
			_failOutput		= rs.getInt("failOutput");
		}
		public int get_nameId(){
			return _nameId;
		}
		public void set_nameId(int val){
			_bit |= 0x1;
			_nameId = val;
		}
		public boolean has_nameId(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_input(){
			return _input;
		}
		public void set_input(int val){
			_bit |= 0x2;
			_input = val;
		}
		public boolean has_input(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_output(){
			return _output;
		}
		public void set_output(int val){
			_bit |= 0x4;
			_output = val;
		}
		public boolean has_output(){
			return (_bit & 0x4) == 0x4;
		}
		public int get_successProb(){
			return _successProb;
		}
		public void set_successProb(int val){
			_bit |= 0x8;
			_successProb = val;
		}
		public boolean has_successProb(){
			return (_bit & 0x8) == 0x8;
		}
		public int get_rewardCount(){
			return _rewardCount;
		}
		public void set_rewardCount(int val){
			_bit |= 0x10;
			_rewardCount = val;
		}
		public boolean has_rewardCount(){
			return (_bit & 0x10) == 0x10;
		}
		public int get_preserveProb(){
			return _preserveProb;
		}
		public void set_preserveProb(int val){
			_bit |= 0x20;
			_preserveProb = val;
		}
		public boolean has_preserveProb(){
			return (_bit & 0x20) == 0x20;
		}
		public int get_failOutput(){
			return _failOutput;
		}
		public void set_failOutput(int val){
			_bit |= 0x40;
			_failOutput = val;
		}
		public boolean has_failOutput(){
			return (_bit & 0x40) == 0x40;
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
			if (has_nameId()){
				size += ProtoOutputStream.computeInt32Size(1, _nameId);
			}
			if (has_input()){
				size += ProtoOutputStream.computeInt32Size(2, _input);
			}
			if (has_output()){
				size += ProtoOutputStream.computeInt32Size(3, _output);
			}
			if (has_successProb()){
				size += ProtoOutputStream.computeInt32Size(4, _successProb);
			}
			if (has_rewardCount()){
				size += ProtoOutputStream.computeInt32Size(5, _rewardCount);
			}
			if (has_preserveProb()){
				size += ProtoOutputStream.computeInt32Size(6, _preserveProb);
			}
			if (has_failOutput()){
				size += ProtoOutputStream.computeInt32Size(7, _failOutput);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_nameId()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_input()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_output()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_nameId()){
				output.wirteInt32(1, _nameId);
			}
			if (has_input()){
				output.wirteInt32(2, _input);
			}
			if (has_output()){
				output.wirteInt32(3, _output);
			}
			if (has_successProb()){
				output.wirteInt32(4, _successProb);
			}
			if (has_rewardCount()){
				output.wirteInt32(5, _rewardCount);
			}
			if (has_preserveProb()){
				output.wirteInt32(6, _preserveProb);
			}
			if (has_failOutput()){
				output.wirteInt32(7, _failOutput);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_nameId(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_input(input.readInt32());
						break;
					}
					case 0x00000018:{
						set_output(input.readInt32());
						break;
					}
					case 0x00000020:{
						set_successProb(input.readInt32());
						break;
					}
					case 0x00000028:{
						set_rewardCount(input.readInt32());
						break;
					}
					case 0x00000030:{
						set_preserveProb(input.readInt32());
						break;
					}
					case 0x00000038:{
						set_failOutput(input.readInt32());
						break;
					}
					default:{
						System.out.println(String.format("[CatalystTableT.CatalystRewardInfoT] NEW_TAG : TAG(%d)", tag));
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

