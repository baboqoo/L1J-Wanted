package l1j.server.common.bin.companion;

import java.util.HashMap;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class CompanionSkillEnchantCommonBin implements ProtoMessage{
	public static CompanionSkillEnchantCommonBin newInstance(){
		return new CompanionSkillEnchantCommonBin();
	}
	private HashMap<Integer, CompanionSkillEnchantCommonBinExtend> _enchant;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CompanionSkillEnchantCommonBin(){
	}
	public HashMap<Integer, CompanionSkillEnchantCommonBinExtend> get_enchant(){
		return _enchant;
	}
	public void add_enchant(CompanionSkillEnchantCommonBinExtend val){
		if(!has_enchant()){
			_enchant = new HashMap<Integer, CompanionSkillEnchantCommonBinExtend>();
			_bit |= 0x1;
		}
		_enchant.put(val._tier, val);
	}
	public boolean has_enchant(){
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
		if (has_enchant()){
			for (CompanionSkillEnchantCommonBinExtend val : _enchant.values()) {
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
		if (has_enchant()){
			for(CompanionSkillEnchantCommonBinExtend val : _enchant.values()){
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
		if (has_enchant()){
			for (CompanionSkillEnchantCommonBinExtend val : _enchant.values()) {
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
					add_enchant((CompanionSkillEnchantCommonBinExtend)input.readMessage(CompanionSkillEnchantCommonBinExtend.newInstance()));
					break;
				}
				default:{
					System.out.println(String.format("[CompanionSkillEnchantCommonBin] NEW_TAG : TAG(%d)", tag));
					return this;
				}
			}
		}
		return this;
	}

	@Override
	public void dispose(){
		if (has_enchant()) {
			for (CompanionSkillEnchantCommonBinExtend val : _enchant.values()) {
				val.dispose();
			}
			_enchant.clear();
			_enchant = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	
	public static class CompanionSkillEnchantCommonBinExtend implements ProtoMessage{
		public static CompanionSkillEnchantCommonBinExtend newInstance(){
			return new CompanionSkillEnchantCommonBinExtend();
		}
		private int _tier;
		private CompanionT.SkillEnchantTierT _enchant;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private CompanionSkillEnchantCommonBinExtend(){
		}
		public int get_tier() {
			return _tier;
		}
		public void set_tier(int val) {
			_bit |= 0x1;
			_tier = val;
		}
		public boolean has_tier() {
			return (_bit & 0x1) == 0x1;
		}
		public CompanionT.SkillEnchantTierT get_enchant(){
			return _enchant;
		}
		public void set_enchant(CompanionT.SkillEnchantTierT val){
			_bit |= 0x2;
			_enchant = val;
		}
		public boolean has_enchant(){
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
			if (has_tier()){
				size += ProtoOutputStream.computeUInt32Size(1, _tier);
			}
			if (has_enchant()) {
				size += ProtoOutputStream.computeMessageSize(2, _enchant);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_tier()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_enchant()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_tier()){
				output.writeUInt32(1, _tier);
			}
			if (has_enchant()){
				output.writeMessage(2, _enchant);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_tier(input.readInt32());
						break;
					}
					case 0x00000012:{
						set_enchant((CompanionT.SkillEnchantTierT)input.readMessage(CompanionT.SkillEnchantTierT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[CompanionSkillEnchantCommonBinExtend] NEW_TAG : TAG(%d)", tag));
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

