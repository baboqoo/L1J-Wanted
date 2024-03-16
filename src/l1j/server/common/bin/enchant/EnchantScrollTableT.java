package l1j.server.common.bin.enchant;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;
import l1j.server.server.utils.StringUtil;

public class EnchantScrollTableT implements ProtoMessage{
	public static EnchantScrollTableT newInstance(){
		return new EnchantScrollTableT();
	}
	private java.util.LinkedList<EnchantScrollTableT.EnchantScrollTypeListT> _enchatScrollTypeList;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private EnchantScrollTableT(){
	}
	public java.util.LinkedList<EnchantScrollTableT.EnchantScrollTypeListT> get_enchatScrollTypeList(){
		return _enchatScrollTypeList;
	}
	public void add_enchatScrollTypeList(EnchantScrollTableT.EnchantScrollTypeListT val){
		if(!has_enchatScrollTypeList()){
			_enchatScrollTypeList = new java.util.LinkedList<EnchantScrollTableT.EnchantScrollTypeListT>();
			_bit |= 0x1;
		}
		_enchatScrollTypeList.add(val);
	}
	public boolean has_enchatScrollTypeList(){
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
		if (has_enchatScrollTypeList()){
			for(EnchantScrollTableT.EnchantScrollTypeListT val : _enchatScrollTypeList){
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
		if (has_enchatScrollTypeList()){
			for(EnchantScrollTableT.EnchantScrollTypeListT val : _enchatScrollTypeList){
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
		if (has_enchatScrollTypeList()){
			for (EnchantScrollTableT.EnchantScrollTypeListT val : _enchatScrollTypeList){
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
					add_enchatScrollTypeList((EnchantScrollTableT.EnchantScrollTypeListT)input.readMessage(EnchantScrollTableT.EnchantScrollTypeListT.newInstance()));
					break;
				}
				default:{
					System.out.println(String.format("[EnchantScrollTableT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class EnchantScrollTypeListT implements ProtoMessage{
		public static EnchantScrollTypeListT newInstance(){
			return new EnchantScrollTypeListT();
		}
		private int _enchantType;
		private java.util.LinkedList<EnchantScrollTableT.EnchantScrollTypeListT.EnchantScrollT> _scrollList;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private EnchantScrollTypeListT(){
		}
		public int get_enchantType(){
			return _enchantType;
		}
		public void set_enchantType(int val){
			_bit |= 0x1;
			_enchantType = val;
		}
		public boolean has_enchantType(){
			return (_bit & 0x1) == 0x1;
		}
		public java.util.LinkedList<EnchantScrollTableT.EnchantScrollTypeListT.EnchantScrollT> get_scrollList(){
			return _scrollList;
		}
		public void add_scrollList(EnchantScrollTableT.EnchantScrollTypeListT.EnchantScrollT val){
			if(!has_scrollList()){
				_scrollList = new java.util.LinkedList<EnchantScrollTableT.EnchantScrollTypeListT.EnchantScrollT>();
				_bit |= 0x2;
			}
			_scrollList.add(val);
		}
		public boolean has_scrollList(){
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
			if (has_enchantType()){
				size += ProtoOutputStream.computeInt32Size(1, _enchantType);
			}
			if (has_scrollList()){
				for(EnchantScrollTableT.EnchantScrollTypeListT.EnchantScrollT val : _scrollList){
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
			if (!has_enchantType()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (has_scrollList()){
				for(EnchantScrollTableT.EnchantScrollTypeListT.EnchantScrollT val : _scrollList){
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
			if (has_enchantType()){
				output.wirteInt32(1, _enchantType);
			}
			if (has_scrollList()){
				for (EnchantScrollTableT.EnchantScrollTypeListT.EnchantScrollT val : _scrollList){
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
						set_enchantType(input.readInt32());
						break;
					}
					case 0x00000012:{
						add_scrollList((EnchantScrollTableT.EnchantScrollTypeListT.EnchantScrollT)input.readMessage(EnchantScrollTableT.EnchantScrollTypeListT.EnchantScrollT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[EnchantScrollTableT.EnchantScrollTypeListT] NEW_TAG : TAG(%d)", tag));
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
		public static class EnchantScrollT implements ProtoMessage{
			public static EnchantScrollT newInstance(){
				return new EnchantScrollT();
			}
			private int _nameid;
			private int _targetEnchant;
			private java.util.LinkedList<Integer> _noTargetMaterialList;
			private EnchantScrollTableT.EnchantScrollTypeListT.EnchantScrollT.eTargetCategory _target_category;
			private boolean _isBmEnchantScroll;
			private int _elementalType;
			private int _useBlesscodeScroll;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private EnchantScrollT(){
			}
			public int get_nameid(){
				return _nameid;
			}
			public void set_nameid(int val){
				_bit |= 0x1;
				_nameid = val;
			}
			public boolean has_nameid(){
				return (_bit & 0x1) == 0x1;
			}
			public int get_targetEnchant(){
				return _targetEnchant;
			}
			public void set_targetEnchant(int val){
				_bit |= 0x2;
				_targetEnchant = val;
			}
			public boolean has_targetEnchant(){
				return (_bit & 0x2) == 0x2;
			}
			public java.util.LinkedList<Integer> get_noTargetMaterialList(){
				return _noTargetMaterialList;
			}
			public void add_noTargetMaterialList(int val){
				if(!has_noTargetMaterialList()){
					_noTargetMaterialList = new java.util.LinkedList<Integer>();
					_bit |= 0x4;
				}
				_noTargetMaterialList.add(val);
			}
			public String get_noTargetMaterialList_toString() {
				if (_noTargetMaterialList == null || _noTargetMaterialList.isEmpty()) {
					return null;
				}
				StringBuilder sb = new StringBuilder();
				for (int val : _noTargetMaterialList) {
					if (sb.length() > 0) {
						sb.append(StringUtil.CommaString);
					}
					sb.append(val);
				}
				return sb.toString();
			}
			public boolean has_noTargetMaterialList(){
				return (_bit & 0x4) == 0x4;
			}
			public EnchantScrollTableT.EnchantScrollTypeListT.EnchantScrollT.eTargetCategory get_target_category(){
				return _target_category;
			}
			public void set_target_category(EnchantScrollTableT.EnchantScrollTypeListT.EnchantScrollT.eTargetCategory val){
				_bit |= 0x8;
				_target_category = val;
			}
			public String get_target_category_toString() {
				if (_target_category == null) {
					return "NONE(0)";
				}
				return String.format("%s(%d)", _target_category.name(), _target_category.toInt());
			}
			public boolean has_target_category(){
				return (_bit & 0x8) == 0x8;
			}
			public boolean get_isBmEnchantScroll(){
				return _isBmEnchantScroll;
			}
			public void set_isBmEnchantScroll(boolean val){
				_bit |= 0x10;
				_isBmEnchantScroll = val;
			}
			public boolean has_isBmEnchantScroll(){
				return (_bit & 0x10) == 0x10;
			}
			public int get_elementalType(){
				return _elementalType;
			}
			public void set_elementalType(int val){
				_bit |= 0x20;
				_elementalType = val;
			}
			public boolean has_elementalType(){
				return (_bit & 0x20) == 0x20;
			}
			public int get_useBlesscodeScroll(){
				return _useBlesscodeScroll;
			}
			public void set_useBlesscodeScroll(int val){
				_bit |= 0x40;
				_useBlesscodeScroll = val;
			}
			public boolean has_useBlesscodeScroll(){
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
				if (has_nameid()){
					size += ProtoOutputStream.computeInt32Size(1, _nameid);
				}
				if (has_targetEnchant()){
					size += ProtoOutputStream.computeInt32Size(2, _targetEnchant);
				}
				if (has_noTargetMaterialList()){
					for(int val : _noTargetMaterialList){
						size += ProtoOutputStream.computeInt32Size(3, val);
					}
				}
				if (has_target_category()){
					size += ProtoOutputStream.computeEnumSize(4, _target_category.toInt());
				}
				if (has_isBmEnchantScroll()){
					size += ProtoOutputStream.computeBoolSize(5, _isBmEnchantScroll);
				}
				if (has_elementalType()){
					size += ProtoOutputStream.computeInt32Size(6, _elementalType);
				}
				if (has_useBlesscodeScroll()){
					size += ProtoOutputStream.computeInt32Size(7, _useBlesscodeScroll);
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_nameid()){
					_memorizedIsInitialized = -1;
					return false;
				}
				/*if (has_noTargetMaterialList()){
					for(int val : _noTargetMaterialList){
						if (!val.isInitialized()){
							_memorizedIsInitialized = -1;
							return false;
						}
					}
				}*/
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(ProtoOutputStream output) throws java.io.IOException{
				if (has_nameid()){
					output.wirteInt32(1, _nameid);
				}
				if (has_targetEnchant()){
					output.wirteInt32(2, _targetEnchant);
				}
				if (has_noTargetMaterialList()){
					for (int val : _noTargetMaterialList){
						output.wirteInt32(3, val);
					}
				}
				if (has_target_category()){
					output.writeEnum(4, _target_category.toInt());
				}
				if (has_isBmEnchantScroll()){
					output.writeBool(5, _isBmEnchantScroll);
				}
				if (has_elementalType()){
					output.wirteInt32(6, _elementalType);
				}
				if (has_useBlesscodeScroll()){
					output.wirteInt32(7, _useBlesscodeScroll);
				}
			}
			@Override
			public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
				while(!input.isAtEnd()){
					int tag = input.readTag();
					switch(tag){
						case 0x00000008:{
							set_nameid(input.readInt32());
							break;
						}
						case 0x00000010:{
							set_targetEnchant(input.readInt32());
							break;
						}
						case 0x00000018:{
							add_noTargetMaterialList(input.readInt32());
							break;
						}
						case 0x00000020:{
							set_target_category(EnchantScrollTableT.EnchantScrollTypeListT.EnchantScrollT.eTargetCategory.fromInt(input.readEnum()));
							break;
						}
						case 0x00000028:{
							set_isBmEnchantScroll(input.readBool());
							break;
						}
						case 0x00000030:{
							set_elementalType(input.readInt32());
							break;
						}
						case 0x00000038:{
							set_useBlesscodeScroll(input.readInt32());
							break;
						}
						default:{
							System.out.println(String.format("[EnchantScrollTableT.EnchantScrollT] NEW_TAG : TAG(%d)", tag));
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
			public enum eTargetCategory{
				WEAPON(1),
				ARMOR(2),
				ACCESSORY(3),
				ELEMENT(4),
				;
				private int value;
				eTargetCategory(int val){
					value = val;
				}
				public int toInt(){
					return value;
				}
				public boolean equals(eTargetCategory v){
					return value == v.value;
				}
				public static eTargetCategory fromInt(int i){
					switch(i){
					case 1:
						return WEAPON;
					case 2:
						return ARMOR;
					case 3:
						return ACCESSORY;
					case 4:
						return ELEMENT;
					default:
						throw new IllegalArgumentException(String.format("invalid arguments eTargetCategory, %d", i));
					}
				}
			}
		}
	}
}

