package l1j.server.common.bin.enchant;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class ElementEnchantTableT implements ProtoMessage{
	public static ElementEnchantTableT newInstance(){
		return new ElementEnchantTableT();
	}
	private java.util.LinkedList<ElementEnchantTableT.ElementalEnchantProbT> _probs;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private ElementEnchantTableT(){
	}
	public java.util.LinkedList<ElementEnchantTableT.ElementalEnchantProbT> get_probs(){
		return _probs;
	}
	public void add_probs(ElementEnchantTableT.ElementalEnchantProbT val){
		if(!has_probs()){
			_probs = new java.util.LinkedList<ElementEnchantTableT.ElementalEnchantProbT>();
			_bit |= 0x1;
		}
		_probs.add(val);
	}
	public boolean has_probs(){
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
		if (has_probs()){
			for(ElementEnchantTableT.ElementalEnchantProbT val : _probs){
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
		if (has_probs()){
			for(ElementEnchantTableT.ElementalEnchantProbT val : _probs){
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
		if (has_probs()){
			for (ElementEnchantTableT.ElementalEnchantProbT val : _probs){
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
					add_probs((ElementEnchantTableT.ElementalEnchantProbT)input.readMessage(ElementEnchantTableT.ElementalEnchantProbT.newInstance()));
					break;
				}
				default:{
					System.out.println(String.format("[ElementEnchantTableT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class ElementalEnchantProbT implements ProtoMessage{
		public static ElementalEnchantProbT newInstance(){
			return new ElementalEnchantProbT();
		}
		private java.util.LinkedList<ElementEnchantTableT.ElementalEnchantProbT.ElementEnchantTypeT> _Types;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private ElementalEnchantProbT(){
		}
		public java.util.LinkedList<ElementEnchantTableT.ElementalEnchantProbT.ElementEnchantTypeT> get_Types(){
			return _Types;
		}
		public void add_Types(ElementEnchantTableT.ElementalEnchantProbT.ElementEnchantTypeT val){
			if(!has_Types()){
				_Types = new java.util.LinkedList<ElementEnchantTableT.ElementalEnchantProbT.ElementEnchantTypeT>();
				_bit |= 0x1;
			}
			_Types.add(val);
		}
		public boolean has_Types(){
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
			if (has_Types()){
				for(ElementEnchantTableT.ElementalEnchantProbT.ElementEnchantTypeT val : _Types){
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
			if (has_Types()){
				for(ElementEnchantTableT.ElementalEnchantProbT.ElementEnchantTypeT val : _Types){
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
			if (has_Types()){
				for (ElementEnchantTableT.ElementalEnchantProbT.ElementEnchantTypeT val : _Types){
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
						add_Types((ElementEnchantTableT.ElementalEnchantProbT.ElementEnchantTypeT)input.readMessage(ElementEnchantTableT.ElementalEnchantProbT.ElementEnchantTypeT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[ElementEnchantTableT.ElementalEnchantProbT] NEW_TAG : TAG(%d)", tag));
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
		
		public static class ElementEnchantTypeT implements ProtoMessage{
			public static ElementEnchantTypeT newInstance(){
				return new ElementEnchantTypeT();
			}
			private java.util.LinkedList<ElementEnchantTableT.ElementalEnchantProbT.ElementEnchantTypeT.ElementEnchantLevelT> _Levels;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private ElementEnchantTypeT(){
			}
			public java.util.LinkedList<ElementEnchantTableT.ElementalEnchantProbT.ElementEnchantTypeT.ElementEnchantLevelT> get_Levels(){
				return _Levels;
			}
			public void add_Levels(ElementEnchantTableT.ElementalEnchantProbT.ElementEnchantTypeT.ElementEnchantLevelT val){
				if(!has_Levels()){
					_Levels = new java.util.LinkedList<ElementEnchantTableT.ElementalEnchantProbT.ElementEnchantTypeT.ElementEnchantLevelT>();
					_bit |= 0x1;
				}
				_Levels.add(val);
			}
			public boolean has_Levels(){
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
				if (has_Levels()){
					for(ElementEnchantTableT.ElementalEnchantProbT.ElementEnchantTypeT.ElementEnchantLevelT val : _Levels){
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
				if (has_Levels()){
					for(ElementEnchantTableT.ElementalEnchantProbT.ElementEnchantTypeT.ElementEnchantLevelT val : _Levels){
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
				if (has_Levels()){
					for (ElementEnchantTableT.ElementalEnchantProbT.ElementEnchantTypeT.ElementEnchantLevelT val : _Levels){
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
							add_Levels((ElementEnchantTableT.ElementalEnchantProbT.ElementEnchantTypeT.ElementEnchantLevelT)input.readMessage(ElementEnchantTableT.ElementalEnchantProbT.ElementEnchantTypeT.ElementEnchantLevelT.newInstance()));
							break;
						}
						default:{
							System.out.println(String.format("[ElementEnchantTableT.ElementEnchantTypeT] NEW_TAG : TAG(%d)", tag));
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
			
			public static class ElementEnchantLevelT implements ProtoMessage{
				public static ElementEnchantLevelT newInstance(){
					return new ElementEnchantLevelT();
				}
				private int _IncreaseProb;
				private int _DecreaseProb;
				private int _memorizedSerializedSize = -1;
				private byte _memorizedIsInitialized = -1;
				private int _bit;
				private ElementEnchantLevelT(){
				}
				public int get_IncreaseProb(){
					return _IncreaseProb;
				}
				public void set_IncreaseProb(int val){
					_bit |= 0x1;
					_IncreaseProb = val;
				}
				public boolean has_IncreaseProb(){
					return (_bit & 0x1) == 0x1;
				}
				public int get_DecreaseProb(){
					return _DecreaseProb;
				}
				public void set_DecreaseProb(int val){
					_bit |= 0x2;
					_DecreaseProb = val;
				}
				public boolean has_DecreaseProb(){
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
					if (has_IncreaseProb()){
						size += ProtoOutputStream.computeInt32Size(1, _IncreaseProb);
					}
					if (has_DecreaseProb()){
						size += ProtoOutputStream.computeInt32Size(2, _DecreaseProb);
					}
					_memorizedSerializedSize = size;
					return size;
				}
				@Override
				public boolean isInitialized(){
					if(_memorizedIsInitialized == 1)
						return true;
					if (!has_IncreaseProb()){
						_memorizedIsInitialized = -1;
						return false;
					}
					if (!has_DecreaseProb()){
						_memorizedIsInitialized = -1;
						return false;
					}
					_memorizedIsInitialized = 1;
					return true;
				}
				@Override
				public void writeTo(ProtoOutputStream output) throws java.io.IOException{
					if (has_IncreaseProb()){
						output.wirteInt32(1, _IncreaseProb);
					}
					if (has_DecreaseProb()){
						output.wirteInt32(2, _DecreaseProb);
					}
				}
				@Override
				public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
					while(!input.isAtEnd()){
						int tag = input.readTag();
						switch(tag){
							case 0x00000008:{
								set_IncreaseProb(input.readInt32());
								break;
							}
							case 0x00000010:{
								set_DecreaseProb(input.readInt32());
								break;
							}
							default:{
								System.out.println(String.format("[ElementEnchantTableT.ElementEnchantLevelT] NEW_TAG : TAG(%d)", tag));
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
}

