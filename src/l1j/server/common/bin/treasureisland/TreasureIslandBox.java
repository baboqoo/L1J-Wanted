package l1j.server.common.bin.treasureisland;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class TreasureIslandBox implements ProtoMessage{
	public static TreasureIslandBox newInstance(){
		return new TreasureIslandBox();
	}
	private TreasureIslandBox.TreasureBoxInfoListT _TreasureIslandBoxes;
	private TreasureIslandBox.StartNotiT _StartNoti;
	private TreasureIslandBox.RewardItemT _Reward;
	private TreasureIslandBox.EndNotiT _EndNoti;
	private TreasureIslandBox.RewardBoxInfoListT _RewardBoxInfos;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private TreasureIslandBox(){
	}
	public TreasureIslandBox.TreasureBoxInfoListT get_TreasureIslandBoxes(){
		return _TreasureIslandBoxes;
	}
	public void set_TreasureIslandBoxes(TreasureIslandBox.TreasureBoxInfoListT val){
		_bit |= 0x1;
		_TreasureIslandBoxes = val;
	}
	public boolean has_TreasureIslandBoxes(){
		return (_bit & 0x1) == 0x1;
	}
	public TreasureIslandBox.StartNotiT get_StartNoti(){
		return _StartNoti;
	}
	public void set_StartNoti(TreasureIslandBox.StartNotiT val){
		_bit |= 0x2;
		_StartNoti = val;
	}
	public boolean has_StartNoti(){
		return (_bit & 0x2) == 0x2;
	}
	public TreasureIslandBox.RewardItemT get_Reward(){
		return _Reward;
	}
	public void set_Reward(TreasureIslandBox.RewardItemT val){
		_bit |= 0x4;
		_Reward = val;
	}
	public boolean has_Reward(){
		return (_bit & 0x4) == 0x4;
	}
	public TreasureIslandBox.EndNotiT get_EndNoti(){
		return _EndNoti;
	}
	public void set_EndNoti(TreasureIslandBox.EndNotiT val){
		_bit |= 0x8;
		_EndNoti = val;
	}
	public boolean has_EndNoti(){
		return (_bit & 0x8) == 0x8;
	}
	public TreasureIslandBox.RewardBoxInfoListT get_RewardBoxInfos(){
		return _RewardBoxInfos;
	}
	public void set_RewardBoxInfos(TreasureIslandBox.RewardBoxInfoListT val){
		_bit |= 0x10;
		_RewardBoxInfos = val;
	}
	public boolean has_RewardBoxInfos(){
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
		if (has_TreasureIslandBoxes()){
			size += ProtoOutputStream.computeMessageSize(1, _TreasureIslandBoxes);
		}
		if (has_StartNoti()){
			size += ProtoOutputStream.computeMessageSize(2, _StartNoti);
		}
		if (has_Reward()){
			size += ProtoOutputStream.computeMessageSize(3, _Reward);
		}
		if (has_EndNoti()){
			size += ProtoOutputStream.computeMessageSize(4, _EndNoti);
		}
		if (has_RewardBoxInfos()){
			size += ProtoOutputStream.computeMessageSize(5, _RewardBoxInfos);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_TreasureIslandBoxes()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_TreasureIslandBoxes()){
			output.writeMessage(1, _TreasureIslandBoxes);
		}
		if (has_StartNoti()){
			output.writeMessage(2, _StartNoti);
		}
		if (has_Reward()){
			output.writeMessage(3, _Reward);
		}
		if (has_EndNoti()){
			output.writeMessage(4, _EndNoti);
		}
		if (has_RewardBoxInfos()){
			output.writeMessage(5, _RewardBoxInfos);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x0000000A:{
					set_TreasureIslandBoxes((TreasureIslandBox.TreasureBoxInfoListT)input.readMessage(TreasureIslandBox.TreasureBoxInfoListT.newInstance()));
					break;
				}
				case 0x00000012:{
					set_StartNoti((TreasureIslandBox.StartNotiT)input.readMessage(TreasureIslandBox.StartNotiT.newInstance()));
					break;
				}
				case 0x0000001A:{
					set_Reward((TreasureIslandBox.RewardItemT)input.readMessage(TreasureIslandBox.RewardItemT.newInstance()));
					break;
				}
				case 0x00000022:{
					set_EndNoti((TreasureIslandBox.EndNotiT)input.readMessage(TreasureIslandBox.EndNotiT.newInstance()));
					break;
				}
				case 0x0000002A:{
					set_RewardBoxInfos((TreasureIslandBox.RewardBoxInfoListT)input.readMessage(TreasureIslandBox.RewardBoxInfoListT.newInstance()));
					break;
				}
				default:{
					System.out.println(String.format("[TreasureIslandBox] NEW_TAG : TAG(%d)", tag));
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
	
	public static class StartNotiT implements ProtoMessage{
		public static StartNotiT newInstance(){
			return new StartNotiT();
		}
		private int _desc;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private StartNotiT(){
		}
		public int get_desc(){
			return _desc;
		}
		public void set_desc(int val){
			_bit |= 0x1;
			_desc = val;
		}
		public boolean has_desc(){
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
			if (has_desc()){
				size += ProtoOutputStream.computeUInt32Size(1, _desc);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_desc()){
				output.writeUInt32(1, _desc);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_desc(input.readUInt32());
						break;
					}
					default:{
						System.out.println(String.format("[TreasureIslandBox.StartNotiT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class EndNotiT implements ProtoMessage{
		public static EndNotiT newInstance(){
			return new EndNotiT();
		}
		private int _desc;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private EndNotiT(){
		}
		public int get_desc(){
			return _desc;
		}
		public void set_desc(int val){
			_bit |= 0x1;
			_desc = val;
		}
		public boolean has_desc(){
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
			if (has_desc()){
				size += ProtoOutputStream.computeUInt32Size(1, _desc);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_desc()){
				output.writeUInt32(1, _desc);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_desc(input.readUInt32());
						break;
					}
					default:{
						System.out.println(String.format("[TreasureIslandBox.EndNotiT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class RewardItemT implements ProtoMessage{
		public static RewardItemT newInstance(){
			return new RewardItemT();
		}
		private int _nameid;
		private int _count;
		private int _restgaugePercent;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private RewardItemT(){
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
		public int get_count(){
			return _count;
		}
		public void set_count(int val){
			_bit |= 0x2;
			_count = val;
		}
		public boolean has_count(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_restgaugePercent(){
			return _restgaugePercent;
		}
		public void set_restgaugePercent(int val){
			_bit |= 0x4;
			_restgaugePercent = val;
		}
		public boolean has_restgaugePercent(){
			return (_bit & 0x4) == 0x4;
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
				size += ProtoOutputStream.computeUInt32Size(1, _nameid);
			}
			if (has_count()){
				size += ProtoOutputStream.computeUInt32Size(2, _count);
			}
			if (has_restgaugePercent()){
				size += ProtoOutputStream.computeUInt32Size(3, _restgaugePercent);
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
			if (!has_count()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_restgaugePercent()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_nameid()){
				output.writeUInt32(1, _nameid);
			}
			if (has_count()){
				output.writeUInt32(2, _count);
			}
			if (has_restgaugePercent()){
				output.writeUInt32(3, _restgaugePercent);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_nameid(input.readUInt32());
						break;
					}
					case 0x00000010:{
						set_count(input.readUInt32());
						break;
					}
					case 0x00000018:{
						set_restgaugePercent(input.readUInt32());
						break;
					}
					default:{
						System.out.println(String.format("[TreasureIslandBox.RewardItemT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class TreasureBoxInfoListT implements ProtoMessage{
		public static TreasureBoxInfoListT newInstance(){
			return new TreasureBoxInfoListT();
		}
		private TreasureIslandBox.TreasureBoxInfoListT.TreasureBoxInfoT _BoxInfoList;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private TreasureBoxInfoListT(){
		}
		public TreasureIslandBox.TreasureBoxInfoListT.TreasureBoxInfoT get_BoxInfoList(){
			return _BoxInfoList;
		}
		public void set_BoxInfoList(TreasureIslandBox.TreasureBoxInfoListT.TreasureBoxInfoT val){
			_bit |= 0x1;
			_BoxInfoList = val;
		}
		public boolean has_BoxInfoList(){
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
			if (has_BoxInfoList()){
				size += ProtoOutputStream.computeMessageSize(1, _BoxInfoList);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_BoxInfoList()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_BoxInfoList()){
				output.writeMessage(1, _BoxInfoList);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x0000000A:{
						set_BoxInfoList((TreasureIslandBox.TreasureBoxInfoListT.TreasureBoxInfoT)input.readMessage(TreasureIslandBox.TreasureBoxInfoListT.TreasureBoxInfoT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[TreasureIslandBox.TreasureBoxInfoListT] NEW_TAG : TAG(%d)", tag));
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
		
		public static class TreasureBoxInfoT implements ProtoMessage{
			public static TreasureBoxInfoT newInstance(){
				return new TreasureBoxInfoT();
			}
			private java.util.LinkedList<TreasureIslandBox.TreasureBoxInfoListT.TreasureBoxInfoT.BoxT> _Box;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private TreasureBoxInfoT(){
			}
			public java.util.LinkedList<TreasureIslandBox.TreasureBoxInfoListT.TreasureBoxInfoT.BoxT> get_Box(){
				return _Box;
			}
			public void add_Box(TreasureIslandBox.TreasureBoxInfoListT.TreasureBoxInfoT.BoxT val){
				if(!has_Box()){
					_Box = new java.util.LinkedList<TreasureIslandBox.TreasureBoxInfoListT.TreasureBoxInfoT.BoxT>();
					_bit |= 0x1;
				}
				_Box.add(val);
			}
			public boolean has_Box(){
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
				if (has_Box()){
					for(TreasureIslandBox.TreasureBoxInfoListT.TreasureBoxInfoT.BoxT val : _Box){
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
				if (has_Box()){
					for(TreasureIslandBox.TreasureBoxInfoListT.TreasureBoxInfoT.BoxT val : _Box){
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
				if (has_Box()){
					for (TreasureIslandBox.TreasureBoxInfoListT.TreasureBoxInfoT.BoxT val : _Box){
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
							add_Box((TreasureIslandBox.TreasureBoxInfoListT.TreasureBoxInfoT.BoxT)input.readMessage(TreasureIslandBox.TreasureBoxInfoListT.TreasureBoxInfoT.BoxT.newInstance()));
							break;
						}
						default:{
							System.out.println(String.format("[TreasureIslandBox.TreasureBoxInfoListT.TreasureBoxInfoT] NEW_TAG : TAG(%d)", tag));
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
			
			public static class BoxT implements ProtoMessage{
				public static BoxT newInstance(){
					return new BoxT();
				}
				private String _name;
				private int _excavateTime;
				private String _desc;
				private TreasureIslandBox.TreasureBoxGrade _grade;
				private int _memorizedSerializedSize = -1;
				private byte _memorizedIsInitialized = -1;
				private int _bit;
				private BoxT(){
				}
				public String get_name(){
					return _name;
				}
				public void set_name(String val){
					_bit |= 0x1;
					_name = val;
				}
				public boolean has_name(){
					return (_bit & 0x1) == 0x1;
				}
				public int get_excavateTime(){
					return _excavateTime;
				}
				public void set_excavateTime(int val){
					_bit |= 0x2;
					_excavateTime = val;
				}
				public boolean has_excavateTime(){
					return (_bit & 0x2) == 0x2;
				}
				public String get_desc(){
					return _desc;
				}
				public void set_desc(String val){
					_bit |= 0x4;
					_desc = val;
				}
				public boolean has_desc(){
					return (_bit & 0x4) == 0x4;
				}
				public TreasureIslandBox.TreasureBoxGrade get_grade(){
					return _grade;
				}
				public void set_grade(TreasureIslandBox.TreasureBoxGrade val){
					_bit |= 0x8;
					_grade = val;
				}
				public boolean has_grade(){
					return (_bit & 0x8) == 0x8;
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
					if (has_name()){
						size += ProtoOutputStream.computeStringSize(1, _name);
					}
					if (has_excavateTime()){
						size += ProtoOutputStream.computeUInt32Size(2, _excavateTime);
					}
					if (has_desc()){
						size += ProtoOutputStream.computeStringSize(3, _desc);
					}
					if (has_grade()){
						size += ProtoOutputStream.computeEnumSize(4, _grade.toInt());
					}
					_memorizedSerializedSize = size;
					return size;
				}
				@Override
				public boolean isInitialized(){
					if(_memorizedIsInitialized == 1)
						return true;
					if (!has_name()){
						_memorizedIsInitialized = -1;
						return false;
					}
					if (!has_excavateTime()){
						_memorizedIsInitialized = -1;
						return false;
					}
					if (!has_desc()){
						_memorizedIsInitialized = -1;
						return false;
					}
					_memorizedIsInitialized = 1;
					return true;
				}
				@Override
				public void writeTo(ProtoOutputStream output) throws java.io.IOException{
					if (has_name()){
						output.writeString(1, _name);
					}
					if (has_excavateTime()){
						output.writeUInt32(2, _excavateTime);
					}
					if (has_desc()){
						output.writeString(3, _desc);
					}
					if (has_grade()){
						output.writeEnum(4, _grade.toInt());
					}
				}
				@Override
				public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
					while(!input.isAtEnd()){
						int tag = input.readTag();
						switch(tag){
							case 0x0000000A:{
								set_name(input.readString());
								break;
							}
							case 0x00000010:{
								set_excavateTime(input.readUInt32());
								break;
							}
							case 0x0000001A:{
								set_desc(input.readString());
								break;
							}
							case 0x00000020:{
								set_grade(TreasureIslandBox.TreasureBoxGrade.fromInt(input.readEnum()));
								break;
							}
							default:{
								System.out.println(String.format("[TreasureIslandBox.TreasureBoxInfoListT.TreasureBoxInfoT.BoxT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class RewardBoxInfoListT implements ProtoMessage{
		public static RewardBoxInfoListT newInstance(){
			return new RewardBoxInfoListT();
		}
		private TreasureIslandBox.RewardBoxInfoListT.RewardBoxInfoT _RewardBoxInfoList;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private RewardBoxInfoListT(){
		}
		public TreasureIslandBox.RewardBoxInfoListT.RewardBoxInfoT get_RewardBoxInfoList(){
			return _RewardBoxInfoList;
		}
		public void set_RewardBoxInfoList(TreasureIslandBox.RewardBoxInfoListT.RewardBoxInfoT val){
			_bit |= 0x1;
			_RewardBoxInfoList = val;
		}
		public boolean has_RewardBoxInfoList(){
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
			if (has_RewardBoxInfoList()){
				size += ProtoOutputStream.computeMessageSize(1, _RewardBoxInfoList);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_RewardBoxInfoList()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_RewardBoxInfoList()){
				output.writeMessage(1, _RewardBoxInfoList);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x0000000A:{
						set_RewardBoxInfoList((TreasureIslandBox.RewardBoxInfoListT.RewardBoxInfoT)input.readMessage(TreasureIslandBox.RewardBoxInfoListT.RewardBoxInfoT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[TreasureIslandBox.RewardBoxInfoListT] NEW_TAG : TAG(%d)", tag));
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
		
		public static class RewardBoxInfoT implements ProtoMessage{
			public static RewardBoxInfoT newInstance(){
				return new RewardBoxInfoT();
			}
			private java.util.LinkedList<TreasureIslandBox.RewardBoxInfoListT.RewardBoxInfoT.RewardBoxT> _RewardBox;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private RewardBoxInfoT(){
			}
			public java.util.LinkedList<TreasureIslandBox.RewardBoxInfoListT.RewardBoxInfoT.RewardBoxT> get_RewardBox(){
				return _RewardBox;
			}
			public void add_RewardBox(TreasureIslandBox.RewardBoxInfoListT.RewardBoxInfoT.RewardBoxT val){
				if(!has_RewardBox()){
					_RewardBox = new java.util.LinkedList<TreasureIslandBox.RewardBoxInfoListT.RewardBoxInfoT.RewardBoxT>();
					_bit |= 0x1;
				}
				_RewardBox.add(val);
			}
			public boolean has_RewardBox(){
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
				if (has_RewardBox()){
					for(TreasureIslandBox.RewardBoxInfoListT.RewardBoxInfoT.RewardBoxT val : _RewardBox){
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
				if (has_RewardBox()){
					for(TreasureIslandBox.RewardBoxInfoListT.RewardBoxInfoT.RewardBoxT val : _RewardBox){
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
				if (has_RewardBox()){
					for (TreasureIslandBox.RewardBoxInfoListT.RewardBoxInfoT.RewardBoxT val : _RewardBox){
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
							add_RewardBox((TreasureIslandBox.RewardBoxInfoListT.RewardBoxInfoT.RewardBoxT)input.readMessage(TreasureIslandBox.RewardBoxInfoListT.RewardBoxInfoT.RewardBoxT.newInstance()));
							break;
						}
						default:{
							System.out.println(String.format("[TreasureIslandBox.RewardBoxInfoListT.RewardBoxInfoT] NEW_TAG : TAG(%d)", tag));
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
			
			public static class RewardBoxT implements ProtoMessage{
				public static RewardBoxT newInstance(){
					return new RewardBoxT();
				}
				private int _nameid;
				private TreasureIslandBox.TreasureBoxGrade _grade;
				private int _memorizedSerializedSize = -1;
				private byte _memorizedIsInitialized = -1;
				private int _bit;
				private RewardBoxT(){
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
				public TreasureIslandBox.TreasureBoxGrade get_grade(){
					return _grade;
				}
				public void set_grade(TreasureIslandBox.TreasureBoxGrade val){
					_bit |= 0x2;
					_grade = val;
				}
				public boolean has_grade(){
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
					if (has_nameid()){
						size += ProtoOutputStream.computeInt32Size(1, _nameid);
					}
					if (has_grade()){
						size += ProtoOutputStream.computeEnumSize(2, _grade.toInt());
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
					_memorizedIsInitialized = 1;
					return true;
				}
				@Override
				public void writeTo(ProtoOutputStream output) throws java.io.IOException{
					if (has_nameid()){
						output.wirteInt32(1, _nameid);
					}
					if (has_grade()){
						output.writeEnum(2, _grade.toInt());
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
								set_grade(TreasureIslandBox.TreasureBoxGrade.fromInt(input.readEnum()));
								break;
							}
							default:{
								System.out.println(String.format("[TreasureIslandBox.RewardBoxInfoListT.RewardBoxInfoT.RewardBoxT] NEW_TAG : TAG(%d)", tag));
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
	
	public enum TreasureBoxGrade{
		Common(0),
		Good(1),
		Prime(2),
		Legendary(3),
		;
		private int value;
		TreasureBoxGrade(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(TreasureBoxGrade v){
			return value == v.value;
		}
		public static TreasureBoxGrade fromInt(int i){
			switch(i){
			case 0:
				return Common;
			case 1:
				return Good;
			case 2:
				return Prime;
			case 3:
				return Legendary;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments TreasureBoxGrade, %d", i));
			}
		}
	}
}

