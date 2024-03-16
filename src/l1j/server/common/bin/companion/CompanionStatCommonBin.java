package l1j.server.common.bin.companion;

import java.util.HashMap;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class CompanionStatCommonBin implements ProtoMessage{
	public static CompanionStatCommonBin newInstance(){
		return new CompanionStatCommonBin();
	}
	private HashMap<Integer, CompanionStatCommonBinExtend> _stat;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CompanionStatCommonBin(){
	}
	public HashMap<Integer, CompanionStatCommonBinExtend> get_stat(){
		return _stat;
	}
	public void add_stat(CompanionStatCommonBinExtend val){
		if(!has_stat()){
			_stat = new HashMap<Integer, CompanionStatCommonBinExtend>();
			_bit |= 0x1;
		}
		_stat.put(val._id, val);
	}
	public boolean has_stat(){
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
		if (has_stat()){
			for (CompanionStatCommonBinExtend val : _stat.values()) {
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
		if (has_stat()){
			for(CompanionStatCommonBinExtend val : _stat.values()){
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
		if (has_stat()){
			for (CompanionStatCommonBinExtend val : _stat.values()) {
				output.writeMessage(1, val);
			}
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000012:{
					add_stat((CompanionStatCommonBinExtend)input.readMessage(CompanionStatCommonBinExtend.newInstance()));
					break;
				}
				default:{
					System.out.println(String.format("[CompanionStatCommonBin] NEW_TAG : TAG(%d)", tag));
					return this;
				}
			}
		}
		return this;
	}

	@Override
	public void dispose(){
		if (has_stat()) {
			for (CompanionStatCommonBinExtend val : _stat.values()) {
				val.dispose();
			}
			_stat.clear();
			_stat = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	
	public static class CompanionStatCommonBinExtend implements ProtoMessage{
		public static CompanionStatCommonBinExtend newInstance(){
			return new CompanionStatCommonBinExtend();
		}
		private int _id;
		private CompanionT.StatT.BaseStatBonusT _stat;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private CompanionStatCommonBinExtend(){
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
		public CompanionT.StatT.BaseStatBonusT get_stat(){
			return _stat;
		}
		public void set_stat(CompanionT.StatT.BaseStatBonusT val){
			_bit |= 0x2;
			_stat = val;
		}
		public boolean has_stat(){
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
			if (has_stat()) {
				size += ProtoOutputStream.computeMessageSize(2, _stat);
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
			if (!has_stat()){
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
			if (has_stat()){
				output.writeMessage(2, _stat);
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
						set_stat((CompanionT.StatT.BaseStatBonusT)input.readMessage(CompanionT.StatT.BaseStatBonusT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[CompanionStatCommonBinExtend] NEW_TAG : TAG(%d)", tag));
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

