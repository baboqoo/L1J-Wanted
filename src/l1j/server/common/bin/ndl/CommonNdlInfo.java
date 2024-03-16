package l1j.server.common.bin.ndl;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class CommonNdlInfo implements ProtoMessage{
	public static CommonNdlInfo newInstance(){
		return new CommonNdlInfo();
	}
	private int _map_number;
	private java.util.LinkedList<CommonNdlInfo.NpcListT> _maker_list;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private CommonNdlInfo(){
	}
	public int get_map_number(){
		return _map_number;
	}
	public void set_map_number(int val){
		_bit |= 0x1;
		_map_number = val;
	}
	public boolean has_map_number(){
		return (_bit & 0x1) == 0x1;
	}
	public java.util.LinkedList<CommonNdlInfo.NpcListT> get_maker_list(){
		return _maker_list;
	}
	public void add_maker_list(CommonNdlInfo.NpcListT val){
		if(!has_maker_list()){
			_maker_list = new java.util.LinkedList<CommonNdlInfo.NpcListT>();
			_bit |= 0x4;
		}
		_maker_list.add(val);
	}
	public boolean has_maker_list(){
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
		if (has_map_number()){
			size += ProtoOutputStream.computeInt32Size(1, _map_number);
		}
		if (has_maker_list()){
			for(CommonNdlInfo.NpcListT val : _maker_list){
				size += ProtoOutputStream.computeMessageSize(3, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_map_number()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_maker_list()){
			for(CommonNdlInfo.NpcListT val : _maker_list){
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
		if (has_map_number()){
			output.wirteInt32(1, _map_number);
		}
		if (has_maker_list()){
			for (CommonNdlInfo.NpcListT val : _maker_list){
				output.writeMessage(3, val);
			}
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000008:{
					set_map_number(input.readInt32());
					break;
				}
				case 0x0000001A:{
					add_maker_list((CommonNdlInfo.NpcListT)input.readMessage(CommonNdlInfo.NpcListT.newInstance()));
					break;
				}
				default:{
					System.out.println(String.format("[CommonNdlInfo] NEW_TAG : TAG(%d)", tag));
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
	public static class AverageNpcInfoT implements ProtoMessage{
		public static AverageNpcInfoT newInstance(){
			return new AverageNpcInfoT();
		}
		private int _average_ac;
		private int _average_level;
		private int _average_wis;
		private int _average_mr;
		private int _average_magic_barrier;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private AverageNpcInfoT(){
		}
		public int get_average_ac(){
			return _average_ac;
		}
		public void set_average_ac(int val){
			_bit |= 0x1;
			_average_ac = val;
		}
		public boolean has_average_ac(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_average_level(){
			return _average_level;
		}
		public void set_average_level(int val){
			_bit |= 0x2;
			_average_level = val;
		}
		public boolean has_average_level(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_average_wis(){
			return _average_wis;
		}
		public void set_average_wis(int val){
			_bit |= 0x4;
			_average_wis = val;
		}
		public boolean has_average_wis(){
			return (_bit & 0x4) == 0x4;
		}
		public int get_average_mr(){
			return _average_mr;
		}
		public void set_average_mr(int val){
			_bit |= 0x8;
			_average_mr = val;
		}
		public boolean has_average_mr(){
			return (_bit & 0x8) == 0x8;
		}
		public int get_average_magic_barrier(){
			return _average_magic_barrier;
		}
		public void set_average_magic_barrier(int val){
			_bit |= 0x10;
			_average_magic_barrier = val;
		}
		public boolean has_average_magic_barrier(){
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
			if (has_average_ac()){
				size += ProtoOutputStream.computeInt32Size(1, _average_ac);
			}
			if (has_average_level()){
				size += ProtoOutputStream.computeInt32Size(2, _average_level);
			}
			if (has_average_wis()){
				size += ProtoOutputStream.computeInt32Size(3, _average_wis);
			}
			if (has_average_mr()){
				size += ProtoOutputStream.computeInt32Size(4, _average_mr);
			}
			if (has_average_magic_barrier()){
				size += ProtoOutputStream.computeInt32Size(5, _average_magic_barrier);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_average_ac()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_average_level()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_average_wis()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_average_mr()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_average_magic_barrier()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_average_ac()){
				output.wirteInt32(1, _average_ac);
			}
			if (has_average_level()){
				output.wirteInt32(2, _average_level);
			}
			if (has_average_wis()){
				output.wirteInt32(3, _average_wis);
			}
			if (has_average_mr()){
				output.wirteInt32(4, _average_mr);
			}
			if (has_average_magic_barrier()){
				output.wirteInt32(5, _average_magic_barrier);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_average_ac(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_average_level(input.readInt32());
						break;
					}
					case 0x00000018:{
						set_average_wis(input.readInt32());
						break;
					}
					case 0x00000020:{
						set_average_mr(input.readInt32());
						break;
					}
					case 0x00000028:{
						set_average_magic_barrier(input.readInt32());
						break;
					}
					default:{
						System.out.println(String.format("[CommonNdlInfo.AverageNpcInfoT] NEW_TAG : TAG(%d)", tag));
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
	public static class TerritoryT implements ProtoMessage{
		public static TerritoryT newInstance(){
			return new TerritoryT();
		}
		private int _startXY;
		private int _endXY;
		private int _location_desc;
		private int _average_npc_value;// 평균
		private CommonNdlInfo.AverageNpcInfoT _average_npc_info;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private TerritoryT(){
		}
		public int get_startXY(){
			return _startXY;
		}
		public void set_startXY(int val){
			_bit |= 0x1;
			_startXY = val;
		}
		public boolean has_startXY(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_endXY(){
			return _endXY;
		}
		public void set_endXY(int val){
			_bit |= 0x2;
			_endXY = val;
		}
		public boolean has_endXY(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_location_desc(){
			return _location_desc;
		}
		public void set_location_desc(int val){
			_bit |= 0x4;
			_location_desc = val;
		}
		public boolean has_location_desc(){
			return (_bit & 0x4) == 0x4;
		}
		public int get_average_npc_value(){
			return _average_npc_value;
		}
		public void set_average_npc_value(int val){
			_bit |= 0x8;
			_average_npc_value = val;
		}
		public boolean has_average_npc_value(){
			return (_bit & 0x8) == 0x8;
		}
		public CommonNdlInfo.AverageNpcInfoT get_average_npc_info(){
			return _average_npc_info;
		}
		public void set_average_npc_info(CommonNdlInfo.AverageNpcInfoT val){
			_bit |= 0x10;
			_average_npc_info = val;
		}
		public boolean has_average_npc_info(){
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
			if (has_startXY()){
				size += ProtoOutputStream.computeInt32Size(1, _startXY);
			}
			if (has_endXY()){
				size += ProtoOutputStream.computeInt32Size(2, _endXY);
			}
			if (has_location_desc()){
				size += ProtoOutputStream.computeInt32Size(3, _location_desc);
			}
			if (has_average_npc_value()){
				size += ProtoOutputStream.computeInt32Size(4, _average_npc_value);
			}
			if (has_average_npc_info()){
				size += ProtoOutputStream.computeMessageSize(5, _average_npc_info);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_startXY()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_endXY()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_startXY()){
				output.wirteInt32(1, _startXY);
			}
			if (has_endXY()){
				output.wirteInt32(2, _endXY);
			}
			if (has_location_desc()){
				output.wirteInt32(3, _location_desc);
			}
			if (has_average_npc_value()){
				output.wirteInt32(4, _average_npc_value);
			}
			if (has_average_npc_info()){
				output.writeMessage(5, _average_npc_info);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_startXY(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_endXY(input.readInt32());
						break;
					}
					case 0x00000018:{
						set_location_desc(input.readInt32());
						break;
					}
					case 0x00000020:{
						set_average_npc_value(input.readInt32());
						break;
					}
					case 0x0000002A:{
						set_average_npc_info((CommonNdlInfo.AverageNpcInfoT)input.readMessage(CommonNdlInfo.AverageNpcInfoT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[CommonNdlInfo.TerritoryT] NEW_TAG : TAG(%d)", tag));
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
	public static class NpcListT implements ProtoMessage{
		public static NpcListT newInstance(){
			return new NpcListT();
		}
		private int _npc_classId;
		private java.util.LinkedList<CommonNdlInfo.TerritoryT> _territory;// 지역
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private NpcListT(){
		}
		public int get_npc_classId(){
			return _npc_classId;
		}
		public void set_npc_classId(int val){
			_bit |= 0x1;
			_npc_classId = val;
		}
		public boolean has_npc_classId(){
			return (_bit & 0x1) == 0x1;
		}
		public java.util.LinkedList<CommonNdlInfo.TerritoryT> get_territory(){
			return _territory;
		}
		public void add_territory(CommonNdlInfo.TerritoryT val){
			if(!has_territory()){
				_territory = new java.util.LinkedList<CommonNdlInfo.TerritoryT>();
				_bit |= 0x2;
			}
			_territory.add(val);
		}
		public boolean has_territory(){
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
			if (has_npc_classId()){
				size += ProtoOutputStream.computeInt32Size(1, _npc_classId);
			}
			if (has_territory()){
				for(CommonNdlInfo.TerritoryT val : _territory){
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
			if (!has_npc_classId()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (has_territory()){
				for(CommonNdlInfo.TerritoryT val : _territory){
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
			if (has_npc_classId()){
				output.wirteInt32(1, _npc_classId);
			}
			if (has_territory()){
				for (CommonNdlInfo.TerritoryT val : _territory){
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
						set_npc_classId(input.readInt32());
						break;
					}
					case 0x00000012:{
						add_territory((CommonNdlInfo.TerritoryT)input.readMessage(CommonNdlInfo.TerritoryT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[CommonNdlInfo.NpcListT] NEW_TAG : TAG(%d)", tag));
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

