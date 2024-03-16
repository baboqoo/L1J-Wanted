package l1j.server.common.bin.enchant;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class ArmorElementalEnchantBonus implements ProtoMessage{
	public static ArmorElementalEnchantBonus newInstance(){
		return new ArmorElementalEnchantBonus();
	}
	private int _type;
	private java.util.LinkedList<ArmorElementalEnchantBonus.EnchantBonus> _enchant_bonus_list;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private ArmorElementalEnchantBonus(){
	}
	public int get_type(){
		return _type;
	}
	public void set_type(int val){
		_bit |= 0x1;
		_type = val;
	}
	public boolean has_type(){
		return (_bit & 0x1) == 0x1;
	}
	public java.util.LinkedList<ArmorElementalEnchantBonus.EnchantBonus> get_enchant_bonus_list(){
		return _enchant_bonus_list;
	}
	public void add_enchant_bonus_list(ArmorElementalEnchantBonus.EnchantBonus val){
		if(!has_enchant_bonus_list()){
			_enchant_bonus_list = new java.util.LinkedList<ArmorElementalEnchantBonus.EnchantBonus>();
			_bit |= 0x2;
		}
		_enchant_bonus_list.add(val);
	}
	public boolean has_enchant_bonus_list(){
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
		if (has_type()){
			size += ProtoOutputStream.computeUInt32Size(1, _type);
		}
		if (has_enchant_bonus_list()){
			for(ArmorElementalEnchantBonus.EnchantBonus val : _enchant_bonus_list){
				size += ProtoOutputStream.computeMessageSize(2, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_type()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_enchant_bonus_list()){
			for(ArmorElementalEnchantBonus.EnchantBonus val : _enchant_bonus_list){
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
		if (has_type()){
			output.writeUInt32(1, _type);
		}
		if (has_enchant_bonus_list()){
			for (ArmorElementalEnchantBonus.EnchantBonus val : _enchant_bonus_list){
				output.writeMessage(2, val);
			}
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000008:{
					set_type(input.readUInt32());
					break;
				}
				case 0x00000012:{
					add_enchant_bonus_list((ArmorElementalEnchantBonus.EnchantBonus)input.readMessage(ArmorElementalEnchantBonus.EnchantBonus.newInstance()));
					break;
				}
				default:{
					System.out.println(String.format("[ArmorElementalEnchantBonus] NEW_TAG : TAG(%d)", tag));
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
	
	public static class EnchantBonus implements ProtoMessage{
		public static EnchantBonus newInstance(){
			return new EnchantBonus();
		}
		private int _enchant;
		private int _fr;
		private int _wr;
		private int _ar;
		private int _er;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private EnchantBonus(){
		}
		public int get_enchant(){
			return _enchant;
		}
		public void set_enchant(int val){
			_bit |= 0x1;
			_enchant = val;
		}
		public boolean has_enchant(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_fr(){
			return _fr;
		}
		public void set_fr(int val){
			_bit |= 0x2;
			_fr = val;
		}
		public boolean has_fr(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_wr(){
			return _wr;
		}
		public void set_wr(int val){
			_bit |= 0x4;
			_wr = val;
		}
		public boolean has_wr(){
			return (_bit & 0x4) == 0x4;
		}
		public int get_ar(){
			return _ar;
		}
		public void set_ar(int val){
			_bit |= 0x8;
			_ar = val;
		}
		public boolean has_ar(){
			return (_bit & 0x8) == 0x8;
		}
		public int get_er(){
			return _er;
		}
		public void set_er(int val){
			_bit |= 0x10;
			_er = val;
		}
		public boolean has_er(){
			return (_bit & 0x10) == 0x10;
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
				size += ProtoOutputStream.computeUInt32Size(1, _enchant);
			}
			if (has_fr()){
				size += ProtoOutputStream.computeUInt32Size(2, _fr);
			}
			if (has_wr()){
				size += ProtoOutputStream.computeUInt32Size(3, _wr);
			}
			if (has_ar()){
				size += ProtoOutputStream.computeUInt32Size(4, _ar);
			}
			if (has_er()){
				size += ProtoOutputStream.computeUInt32Size(5, _er);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_enchant()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_enchant()){
				output.writeUInt32(1, _enchant);
			}
			if (has_fr()){
				output.writeUInt32(2, _fr);
			}
			if (has_wr()){
				output.writeUInt32(3, _wr);
			}
			if (has_ar()){
				output.writeUInt32(4, _ar);
			}
			if (has_er()){
				output.writeUInt32(5, _er);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_enchant(input.readUInt32());
						break;
					}
					case 0x00000010:{
						set_fr(input.readUInt32());
						break;
					}
					case 0x00000018:{
						set_wr(input.readUInt32());
						break;
					}
					case 0x00000020:{
						set_ar(input.readUInt32());
						break;
					}
					case 0x00000028:{
						set_er(input.readUInt32());
						break;
					}
					default:{
						System.out.println(String.format("[ArmorElementalEnchantBonus.EnchantBonus] NEW_TAG : TAG(%d)", tag));
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

