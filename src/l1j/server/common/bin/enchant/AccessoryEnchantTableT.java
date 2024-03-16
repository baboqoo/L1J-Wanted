package l1j.server.common.bin.enchant;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class AccessoryEnchantTableT implements ProtoMessage{
	public static AccessoryEnchantTableT newInstance(){
		return new AccessoryEnchantTableT();
	}
	private java.util.LinkedList<AccessoryEnchantTableT.AccessoryEnchantItemT> _EnchantItems;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private AccessoryEnchantTableT(){
	}
	public java.util.LinkedList<AccessoryEnchantTableT.AccessoryEnchantItemT> get_EnchantItems(){
		return _EnchantItems;
	}
	public void add_EnchantItems(AccessoryEnchantTableT.AccessoryEnchantItemT val){
		if(!has_EnchantItems()){
			_EnchantItems = new java.util.LinkedList<AccessoryEnchantTableT.AccessoryEnchantItemT>();
			_bit |= 0x1;
		}
		_EnchantItems.add(val);
	}
	public boolean has_EnchantItems(){
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
		if (has_EnchantItems()){
			for(AccessoryEnchantTableT.AccessoryEnchantItemT val : _EnchantItems){
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
		if (has_EnchantItems()){
			for(AccessoryEnchantTableT.AccessoryEnchantItemT val : _EnchantItems){
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
		if (has_EnchantItems()){
			for (AccessoryEnchantTableT.AccessoryEnchantItemT val : _EnchantItems){
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
					add_EnchantItems((AccessoryEnchantTableT.AccessoryEnchantItemT)input.readMessage(AccessoryEnchantTableT.AccessoryEnchantItemT.newInstance()));
					break;
				}
				default:{
					System.out.println(String.format("[AccessoryEnchantTableT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class AccessoryEnchantItemT implements ProtoMessage{
		public static AccessoryEnchantItemT newInstance(){
			return new AccessoryEnchantItemT();
		}
		private java.util.LinkedList<AccessoryEnchantTableT.AccessoryEnchantItemT.AccessoryEnchantBonusT> _EnchantBonusLevels;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private AccessoryEnchantItemT(){
		}
		public java.util.LinkedList<AccessoryEnchantTableT.AccessoryEnchantItemT.AccessoryEnchantBonusT> get_EnchantBonusLevels(){
			return _EnchantBonusLevels;
		}
		public void add_EnchantBonusLevels(AccessoryEnchantTableT.AccessoryEnchantItemT.AccessoryEnchantBonusT val){
			if(!has_EnchantBonusLevels()){
				_EnchantBonusLevels = new java.util.LinkedList<AccessoryEnchantTableT.AccessoryEnchantItemT.AccessoryEnchantBonusT>();
				_bit |= 0x1;
			}
			_EnchantBonusLevels.add(val);
		}
		public boolean has_EnchantBonusLevels(){
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
			if (has_EnchantBonusLevels()){
				for(AccessoryEnchantTableT.AccessoryEnchantItemT.AccessoryEnchantBonusT val : _EnchantBonusLevels){
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
			if (has_EnchantBonusLevels()){
				for(AccessoryEnchantTableT.AccessoryEnchantItemT.AccessoryEnchantBonusT val : _EnchantBonusLevels){
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
			if (has_EnchantBonusLevels()){
				for (AccessoryEnchantTableT.AccessoryEnchantItemT.AccessoryEnchantBonusT val : _EnchantBonusLevels){
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
						add_EnchantBonusLevels((AccessoryEnchantTableT.AccessoryEnchantItemT.AccessoryEnchantBonusT)input.readMessage(AccessoryEnchantTableT.AccessoryEnchantItemT.AccessoryEnchantBonusT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[AccessoryEnchantTableT.AccessoryEnchantItemT] NEW_TAG : TAG(%d)", tag));
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
		
		public static class AccessoryEnchantBonusT implements ProtoMessage{
			public static AccessoryEnchantBonusT newInstance(){
				return new AccessoryEnchantBonusT();
			}
			private int _enchantSuccessProb;
			private int _enchantTotalProb;
			private int _bmEnchantSuccessProb;
			private int _bmEnchantRemainProb;
			private int _bmEnchantFailDownProb;
			private int _bmEnchantTotalProb;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private AccessoryEnchantBonusT(){
			}
			public int get_enchantSuccessProb(){
				return _enchantSuccessProb;
			}
			public void set_enchantSuccessProb(int val){
				_bit |= 0x1;
				_enchantSuccessProb = val;
			}
			public boolean has_enchantSuccessProb(){
				return (_bit & 0x1) == 0x1;
			}
			public int get_enchantTotalProb(){
				return _enchantTotalProb;
			}
			public void set_enchantTotalProb(int val){
				_bit |= 0x2;
				_enchantTotalProb = val;
			}
			public boolean has_enchantTotalProb(){
				return (_bit & 0x2) == 0x2;
			}
			public int get_bmEnchantSuccessProb(){
				return _bmEnchantSuccessProb;
			}
			public void set_bmEnchantSuccessProb(int val){
				_bit |= 0x4;
				_bmEnchantSuccessProb = val;
			}
			public boolean has_bmEnchantSuccessProb(){
				return (_bit & 0x4) == 0x4;
			}
			public int get_bmEnchantRemainProb(){
				return _bmEnchantRemainProb;
			}
			public void set_bmEnchantRemainProb(int val){
				_bit |= 0x8;
				_bmEnchantRemainProb = val;
			}
			public boolean has_bmEnchantRemainProb(){
				return (_bit & 0x8) == 0x8;
			}
			public int get_bmEnchantFailDownProb(){
				return _bmEnchantFailDownProb;
			}
			public void set_bmEnchantFailDownProb(int val){
				_bit |= 0x10;
				_bmEnchantFailDownProb = val;
			}
			public boolean has_bmEnchantFailDownProb(){
				return (_bit & 0x10) == 0x10;
			}
			public int get_bmEnchantTotalProb(){
				return _bmEnchantTotalProb;
			}
			public void set_bmEnchantTotalProb(int val){
				_bit |= 0x20;
				_bmEnchantTotalProb = val;
			}
			public boolean has_bmEnchantTotalProb(){
				return (_bit & 0x20) == 0x20;
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
				if (has_enchantSuccessProb()){
					size += ProtoOutputStream.computeInt32Size(1, _enchantSuccessProb);
				}
				if (has_enchantTotalProb()){
					size += ProtoOutputStream.computeInt32Size(2, _enchantTotalProb);
				}
				if (has_bmEnchantSuccessProb()){
					size += ProtoOutputStream.computeInt32Size(3, _bmEnchantSuccessProb);
				}
				if (has_bmEnchantRemainProb()){
					size += ProtoOutputStream.computeInt32Size(4, _bmEnchantRemainProb);
				}
				if (has_bmEnchantFailDownProb()){
					size += ProtoOutputStream.computeInt32Size(5, _bmEnchantFailDownProb);
				}
				if (has_bmEnchantTotalProb()){
					size += ProtoOutputStream.computeInt32Size(6, _bmEnchantTotalProb);
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_enchantSuccessProb()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_enchantTotalProb()){
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(ProtoOutputStream output) throws java.io.IOException{
				if (has_enchantSuccessProb()){
					output.wirteInt32(1, _enchantSuccessProb);
				}
				if (has_enchantTotalProb()){
					output.wirteInt32(2, _enchantTotalProb);
				}
				if (has_bmEnchantSuccessProb()){
					output.wirteInt32(3, _bmEnchantSuccessProb);
				}
				if (has_bmEnchantRemainProb()){
					output.wirteInt32(4, _bmEnchantRemainProb);
				}
				if (has_bmEnchantFailDownProb()){
					output.wirteInt32(5, _bmEnchantFailDownProb);
				}
				if (has_bmEnchantTotalProb()){
					output.wirteInt32(6, _bmEnchantTotalProb);
				}
			}
			@Override
			public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
				while(!input.isAtEnd()){
					int tag = input.readTag();
					switch(tag){
						case 0x00000008:{
							set_enchantSuccessProb(input.readInt32());
							break;
						}
						case 0x00000010:{
							set_enchantTotalProb(input.readInt32());
							break;
						}
						case 0x00000018:{
							set_bmEnchantSuccessProb(input.readInt32());
							break;
						}
						case 0x00000020:{
							set_bmEnchantRemainProb(input.readInt32());
							break;
						}
						case 0x00000028:{
							set_bmEnchantFailDownProb(input.readInt32());
							break;
						}
						case 0x00000030:{
							set_bmEnchantTotalProb(input.readInt32());
							break;
						}
						default:{
							System.out.println(String.format("[AccessoryEnchantTableT.AccessoryEnchantItemT.AccessoryEnchantBonusT] NEW_TAG : TAG(%d)", tag));
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
}

