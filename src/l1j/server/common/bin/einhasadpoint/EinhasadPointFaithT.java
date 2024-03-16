package l1j.server.common.bin.einhasadpoint;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class EinhasadPointFaithT implements ProtoMessage{
	public static EinhasadPointFaithT newInstance(){
		return new EinhasadPointFaithT();
	}
	private EinhasadPointFaithT.BuffInfoT _BuffInfo;
	private EinhasadPointFaithT.GroupListT _GroupList;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private EinhasadPointFaithT(){
	}
	public EinhasadPointFaithT.BuffInfoT get_BuffInfo(){
		return _BuffInfo;
	}
	public void set_BuffInfo(EinhasadPointFaithT.BuffInfoT val){
		_bit |= 0x1;
		_BuffInfo = val;
	}
	public boolean has_BuffInfo(){
		return (_bit & 0x1) == 0x1;
	}
	public EinhasadPointFaithT.GroupListT get_GroupList(){
		return _GroupList;
	}
	public void set_GroupList(EinhasadPointFaithT.GroupListT val){
		_bit |= 0x2;
		_GroupList = val;
	}
	public boolean has_GroupList(){
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
		if (has_BuffInfo()){
			size += ProtoOutputStream.computeMessageSize(1, _BuffInfo);
		}
		if (has_GroupList()){
			size += ProtoOutputStream.computeMessageSize(2, _GroupList);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_BuffInfo()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_GroupList()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_BuffInfo()){
			output.writeMessage(1, _BuffInfo);
		}
		if (has_GroupList()){
			output.writeMessage(2, _GroupList);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x0000000A:
					set_BuffInfo((EinhasadPointFaithT.BuffInfoT)input.readMessage(EinhasadPointFaithT.BuffInfoT.newInstance()));
					break;
				case 0x00000012:
					set_GroupList((EinhasadPointFaithT.GroupListT)input.readMessage(EinhasadPointFaithT.GroupListT.newInstance()));
					break;
				default:
					System.out.println(String.format("[EinhasadPointFaithT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class BuffInfoT implements ProtoMessage{
		public static BuffInfoT newInstance(){
			return new BuffInfoT();
		}
		private int _tooltipStrId;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private BuffInfoT(){
		}
		public int get_tooltipStrId(){
			return _tooltipStrId;
		}
		public void set_tooltipStrId(int val){
			_bit |= 0x1;
			_tooltipStrId = val;
		}
		public boolean has_tooltipStrId(){
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
			if (has_tooltipStrId()){
				size += ProtoOutputStream.computeInt32Size(1, _tooltipStrId);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_tooltipStrId()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_tooltipStrId()){
				output.wirteInt32(1, _tooltipStrId);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:
						set_tooltipStrId(input.readInt32());
						break;
					default:
						System.out.println(String.format("[EinhasadPointFaithT.BuffInfoT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class GroupListT implements ProtoMessage{
		public static GroupListT newInstance(){
			return new GroupListT();
		}
		private java.util.LinkedList<EinhasadPointFaithT.GroupListT.GroupT> _Group;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private GroupListT(){
		}
		public java.util.LinkedList<EinhasadPointFaithT.GroupListT.GroupT> get_Group(){
			return _Group;
		}
		public void add_Group(EinhasadPointFaithT.GroupListT.GroupT val){
			if(!has_Group()){
				_Group = new java.util.LinkedList<EinhasadPointFaithT.GroupListT.GroupT>();
				_bit |= 0x1;
			}
			_Group.add(val);
		}
		public boolean has_Group(){
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
			if (has_Group()){
				for(EinhasadPointFaithT.GroupListT.GroupT val : _Group){
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
			if (has_Group()){
				for(EinhasadPointFaithT.GroupListT.GroupT val : _Group){
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
			if (has_Group()){
				for (EinhasadPointFaithT.GroupListT.GroupT val : _Group){
					output.writeMessage(1, val);
				}
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x0000000A:
						add_Group((EinhasadPointFaithT.GroupListT.GroupT)input.readMessage(EinhasadPointFaithT.GroupListT.GroupT.newInstance()));
						break;
					default:
						System.out.println(String.format("[EinhasadPointFaithT.GroupListT] NEW_TAG : TAG(%d)", tag));
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
		
		public static class GroupT implements ProtoMessage{
			public static GroupT newInstance(){
				return new GroupT();
			}
			private int _GroupId;
			private int _spellId;
			private java.util.LinkedList<byte[]> _extra_desc;
			private java.util.LinkedList<EinhasadPointFaithT.GroupListT.GroupT.IndexT> _index;
			private int _additional_desc;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private GroupT(){
			}
			public int get_GroupId(){
				return _GroupId;
			}
			public void set_GroupId(int val){
				_bit |= 0x1;
				_GroupId = val;
			}
			public boolean has_GroupId(){
				return (_bit & 0x1) == 0x1;
			}
			public int get_spellId(){
				return _spellId;
			}
			public void set_spellId(int val){
				_bit |= 0x2;
				_spellId = val;
			}
			public boolean has_spellId(){
				return (_bit & 0x2) == 0x2;
			}
			public java.util.LinkedList<byte[]> get_extra_desc(){
				return _extra_desc;
			}
			public void add_extra_desc(byte[] val){
				if(!has_extra_desc()){
					_extra_desc = new java.util.LinkedList<byte[]>();
					_bit |= 0x4;
				}
				_extra_desc.add(val);
			}
			public boolean has_extra_desc(){
				return (_bit & 0x4) == 0x4;
			}
			public java.util.LinkedList<EinhasadPointFaithT.GroupListT.GroupT.IndexT> get_index(){
				return _index;
			}
			public void add_index(EinhasadPointFaithT.GroupListT.GroupT.IndexT val){
				if(!has_index()){
					_index = new java.util.LinkedList<EinhasadPointFaithT.GroupListT.GroupT.IndexT>();
					_bit |= 0x8;
				}
				_index.add(val);
			}
			public boolean has_index(){
				return (_bit & 0x8) == 0x8;
			}
			public int get_additional_desc(){
				return _additional_desc;
			}
			public void set_additional_desc(int val){
				_bit |= 0x10;
				_additional_desc = val;
			}
			public boolean has_additional_desc(){
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
				if (has_GroupId()){
					size += ProtoOutputStream.computeInt32Size(1, _GroupId);
				}
				if (has_spellId()){
					size += ProtoOutputStream.computeInt32Size(2, _spellId);
				}
				if (has_extra_desc()){
					for(byte[] val : _extra_desc){
						size += ProtoOutputStream.computeBytesSize(3, val);
					}
				}
				if (has_index()){
					for(EinhasadPointFaithT.GroupListT.GroupT.IndexT val : _index){
						size += ProtoOutputStream.computeMessageSize(4, val);
					}
				}
				if (has_additional_desc()){
					size += ProtoOutputStream.computeUInt32Size(5, _additional_desc);
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_GroupId()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_spellId()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (has_extra_desc()){
					for(byte[] val : _extra_desc){
						if (val == null){
							_memorizedIsInitialized = -1;
							return false;
						}
					}
				}
				if (has_index()){
					for(EinhasadPointFaithT.GroupListT.GroupT.IndexT val : _index){
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
				if (has_GroupId()){
					output.wirteInt32(1, _GroupId);
				}
				if (has_spellId()){
					output.wirteInt32(2, _spellId);
				}
				if (has_extra_desc()){
					for (byte[] val : _extra_desc){
						output.writeBytes(3, val);
					}
				}
				if (has_index()){
					for (EinhasadPointFaithT.GroupListT.GroupT.IndexT val : _index){
						output.writeMessage(4, val);
					}
				}
				if (has_additional_desc()){
					output.writeUInt32(5, _additional_desc);
				}
			}
			@Override
			public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
				while(!input.isAtEnd()){
					int tag = input.readTag();
					switch(tag){
						case 0x00000008:
							set_GroupId(input.readInt32());
							break;
						case 0x00000010:
							set_spellId(input.readInt32());
							break;
						case 0x0000001A:
							add_extra_desc(input.readBytes());
							break;
						case 0x00000022:
							add_index((EinhasadPointFaithT.GroupListT.GroupT.IndexT)input.readMessage(EinhasadPointFaithT.GroupListT.GroupT.IndexT.newInstance()));
							break;
						case 0x00000028:
							set_additional_desc(input.readUInt32());
							break;
						default:
							System.out.println(String.format("[EinhasadPointFaithT.GroupListT.GroupT] NEW_TAG : TAG(%d)", tag));
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
			
			public static class IndexT implements ProtoMessage{
				public static IndexT newInstance(){
					return new IndexT();
				}
				private int _indexId;
				private int _spellId;
				private int _cost;
				private int _duration;
				private java.util.LinkedList<byte[]> _extra_desc;
				private int _additional_desc;
				private int _memorizedSerializedSize = -1;
				private byte _memorizedIsInitialized = -1;
				private int _bit;
				private IndexT(){
				}
				public int get_indexId(){
					return _indexId;
				}
				public void set_indexId(int val){
					_bit |= 0x1;
					_indexId = val;
				}
				public boolean has_indexId(){
					return (_bit & 0x1) == 0x1;
				}
				public int get_spellId(){
					return _spellId;
				}
				public void set_spellId(int val){
					_bit |= 0x2;
					_spellId = val;
				}
				public boolean has_spellId(){
					return (_bit & 0x2) == 0x2;
				}
				public int get_cost(){
					return _cost;
				}
				public void set_cost(int val){
					_bit |= 0x4;
					_cost = val;
				}
				public boolean has_cost(){
					return (_bit & 0x4) == 0x4;
				}
				public int get_duration(){
					return _duration;
				}
				public void set_duration(int val){
					_bit |= 0x8;
					_duration = val;
				}
				public boolean has_duration(){
					return (_bit & 0x8) == 0x8;
				}
				public java.util.LinkedList<byte[]> get_extra_desc(){
					return _extra_desc;
				}
				public void add_extra_desc(byte[] val){
					if(!has_extra_desc()){
						_extra_desc = new java.util.LinkedList<byte[]>();
						_bit |= 0x10;
					}
					_extra_desc.add(val);
				}
				public boolean has_extra_desc(){
					return (_bit & 0x10) == 0x10;
				}
				public int get_additional_desc(){
					return _additional_desc;
				}
				public void set_additional_desc(int val){
					_bit |= 0x20;
					_additional_desc = val;
				}
				public boolean has_additional_desc(){
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
					if (has_indexId()){
						size += ProtoOutputStream.computeInt32Size(1, _indexId);
					}
					if (has_spellId()){
						size += ProtoOutputStream.computeInt32Size(2, _spellId);
					}
					if (has_cost()){
						size += ProtoOutputStream.computeInt32Size(3, _cost);
					}
					if (has_duration()){
						size += ProtoOutputStream.computeUInt32Size(4, _duration);
					}
					if (has_extra_desc()){
						for(byte[] val : _extra_desc){
							size += ProtoOutputStream.computeBytesSize(5, val);
						}
					}
					if (has_additional_desc()){
						size += ProtoOutputStream.computeUInt32Size(6, _additional_desc);
					}
					_memorizedSerializedSize = size;
					return size;
				}
				@Override
				public boolean isInitialized(){
					if(_memorizedIsInitialized == 1)
						return true;
					if (!has_indexId()){
						_memorizedIsInitialized = -1;
						return false;
					}
					if (!has_spellId()){
						_memorizedIsInitialized = -1;
						return false;
					}
					if (!has_cost()){
						_memorizedIsInitialized = -1;
						return false;
					}
					if (has_extra_desc()){
						for(byte[] val : _extra_desc){
							if (val == null){
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
					if (has_indexId()){
						output.wirteInt32(1, _indexId);
					}
					if (has_spellId()){
						output.wirteInt32(2, _spellId);
					}
					if (has_cost()){
						output.wirteInt32(3, _cost);
					}
					if (has_duration()){
						output.writeUInt32(4, _duration);
					}
					if (has_extra_desc()){
						for (byte[] val : _extra_desc){
							output.writeBytes(5, val);
						}
					}
					if (has_additional_desc()){
						output.writeUInt32(6, _additional_desc);
					}
				}
				@Override
				public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
					while(!input.isAtEnd()){
						int tag = input.readTag();
						switch(tag){
							case 0x00000008:
								set_indexId(input.readInt32());
								break;
							case 0x00000010:
								set_spellId(input.readInt32());
								break;
							case 0x00000018:
								set_cost(input.readInt32());
								break;
							case 0x00000020:
								set_duration(input.readUInt32());
								break;
							case 0x0000002A:
								add_extra_desc(input.readBytes());
								break;
							case 0x00000030:
								set_additional_desc(input.readUInt32());
								break;
							default:
								System.out.println(String.format("[EinhasadPointFaithT.GroupListT.GroupT.IndexT] NEW_TAG : TAG(%d)", tag));
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
	}
}

