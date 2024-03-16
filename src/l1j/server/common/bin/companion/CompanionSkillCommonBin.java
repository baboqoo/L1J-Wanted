package l1j.server.common.bin.companion;

import java.util.HashMap;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class CompanionSkillCommonBin implements ProtoMessage{
	public static CompanionSkillCommonBin newInstance(){
		return new CompanionSkillCommonBin();
	}
	private HashMap<Integer, CompanionSkillCommonBinExtend> _skill;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CompanionSkillCommonBin(){
	}
	public HashMap<Integer, CompanionSkillCommonBinExtend> get_skill(){
		return _skill;
	}
	public void add_skill(CompanionSkillCommonBinExtend val){
		if(!has_skill()){
			_skill = new HashMap<Integer, CompanionSkillCommonBinExtend>();
			_bit |= 0x1;
		}
		_skill.put(val._id, val);
	}
	public boolean has_skill(){
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
		if (has_skill()){
			for (CompanionSkillCommonBinExtend val : _skill.values()) {
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
		if (has_skill()){
			for(CompanionSkillCommonBinExtend val : _skill.values()){
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
		if (has_skill()){
			for (CompanionSkillCommonBinExtend val : _skill.values()) {
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
					add_skill((CompanionSkillCommonBinExtend)input.readMessage(CompanionSkillCommonBinExtend.newInstance()));
					break;
				}
				default:{
					System.out.println(String.format("[CompanionSkillCommonBin] NEW_TAG : TAG(%d)", tag));
					return this;
				}
			}
		}
		return this;
	}

	@Override
	public void dispose(){
		if (has_skill()) {
			for (CompanionSkillCommonBinExtend val : _skill.values()) {
				val.dispose();
			}
			_skill.clear();
			_skill = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	
	public static class CompanionSkillCommonBinExtend implements ProtoMessage{
		public static CompanionSkillCommonBinExtend newInstance(){
			return new CompanionSkillCommonBinExtend();
		}
		private int _id;
		private CompanionT.WildSkillT.SkillT _skill;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private CompanionSkillCommonBinExtend(){
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
		public CompanionT.WildSkillT.SkillT get_skill(){
			return _skill;
		}
		public void set_skill(CompanionT.WildSkillT.SkillT val){
			_bit |= 0x2;
			_skill = val;
		}
		public boolean has_skill(){
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
			if (has_skill()) {
				size += ProtoOutputStream.computeMessageSize(2, _skill);
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
			if (!has_skill()){
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
			if (has_skill()){
				output.writeMessage(2, _skill);
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
						set_skill((CompanionT.WildSkillT.SkillT)input.readMessage(CompanionT.WildSkillT.SkillT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[CompanionSkillCommonBinExtend] NEW_TAG : TAG(%d)", tag));
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

