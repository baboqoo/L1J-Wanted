package l1j.server.common.bin.einhasadpoint;

import java.util.Random;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class EinhasadPointStatInfoT implements ProtoMessage{
	public static EinhasadPointStatInfoT newInstance(){
		return new EinhasadPointStatInfoT();
	}
	private int _eachStatMax;
	private int _totalStatMax;
	private java.util.LinkedList<EinhasadPointStatInfoT.EnchantCostT> _EnchantCost;
	private java.util.LinkedList<EinhasadPointStatInfoT.StatMetaDataT> _StatMetaData;
	private java.util.LinkedList<EinhasadPointStatInfoT.StatT> _Stat;
	private java.util.LinkedList<EinhasadPointStatInfoT.EinhasadProb> _einhasadProb;
	private java.util.LinkedList<EinhasadPointStatInfoT.StatMaxInfoT> _StatMaxInfo;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private EinhasadPointStatInfoT(){
	}
	public int get_eachStatMax(){
		return _eachStatMax;
	}
	public void set_eachStatMax(int val){
		_bit |= 0x1;
		_eachStatMax = val;
	}
	public boolean has_eachStatMax(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_totalStatMax(){
		return _totalStatMax;
	}
	public void set_totalStatMax(int val){
		_bit |= 0x2;
		_totalStatMax = val;
	}
	public boolean has_totalStatMax(){
		return (_bit & 0x2) == 0x2;
	}
	public java.util.LinkedList<EinhasadPointStatInfoT.EnchantCostT> get_EnchantCost(){
		return _EnchantCost;
	}
	public void add_EnchantCost(EinhasadPointStatInfoT.EnchantCostT val){
		if(!has_EnchantCost()){
			_EnchantCost = new java.util.LinkedList<EinhasadPointStatInfoT.EnchantCostT>();
			_bit |= 0x4;
		}
		_EnchantCost.add(val);
	}
	public boolean has_EnchantCost(){
		return (_bit & 0x4) == 0x4;
	}
	public java.util.LinkedList<EinhasadPointStatInfoT.StatMetaDataT> get_StatMetaData(){
		return _StatMetaData;
	}
	public void add_StatMetaData(EinhasadPointStatInfoT.StatMetaDataT val){
		if(!has_StatMetaData()){
			_StatMetaData = new java.util.LinkedList<EinhasadPointStatInfoT.StatMetaDataT>();
			_bit |= 0x8;
		}
		_StatMetaData.add(val);
	}
	public boolean has_StatMetaData(){
		return (_bit & 0x8) == 0x8;
	}
	public java.util.LinkedList<EinhasadPointStatInfoT.StatT> get_Stat(){
		return _Stat;
	}
	public void add_Stat(EinhasadPointStatInfoT.StatT val){
		if(!has_Stat()){
			_Stat = new java.util.LinkedList<EinhasadPointStatInfoT.StatT>();
			_bit |= 0x10;
		}
		_Stat.add(val);
	}
	public boolean has_Stat(){
		return (_bit & 0x10) == 0x10;
	}
	public java.util.LinkedList<EinhasadPointStatInfoT.EinhasadProb> get_einhasadProb(){
		return _einhasadProb;
	}
	public void add_einhasadProb(EinhasadPointStatInfoT.EinhasadProb val){
		if(!has_einhasadProb()){
			_einhasadProb = new java.util.LinkedList<EinhasadPointStatInfoT.EinhasadProb>();
			_bit |= 0x20;
		}
		_einhasadProb.add(val);
	}
	public boolean has_einhasadProb(){
		return (_bit & 0x20) == 0x20;
	}
	public java.util.LinkedList<EinhasadPointStatInfoT.StatMaxInfoT> get_StatMaxInfo(){
		return _StatMaxInfo;
	}
	public void add_StatMaxInfo(EinhasadPointStatInfoT.StatMaxInfoT val){
		if(!has_StatMaxInfo()){
			_StatMaxInfo = new java.util.LinkedList<EinhasadPointStatInfoT.StatMaxInfoT>();
			_bit |= 0x40;
		}
		_StatMaxInfo.add(val);
	}
	public boolean has_StatMaxInfo(){
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
		if (has_eachStatMax()){
			size += ProtoOutputStream.computeInt32Size(1, _eachStatMax);
		}
		if (has_totalStatMax()){
			size += ProtoOutputStream.computeInt32Size(2, _totalStatMax);
		}
		if (has_EnchantCost()){
			for(EinhasadPointStatInfoT.EnchantCostT val : _EnchantCost){
				size += ProtoOutputStream.computeMessageSize(3, val);
			}
		}
		if (has_StatMetaData()){
			for(EinhasadPointStatInfoT.StatMetaDataT val : _StatMetaData){
				size += ProtoOutputStream.computeMessageSize(4, val);
			}
		}
		if (has_Stat()){
			for(EinhasadPointStatInfoT.StatT val : _Stat){
				size += ProtoOutputStream.computeMessageSize(5, val);
			}
		}
		if (has_einhasadProb()){
			for(EinhasadPointStatInfoT.EinhasadProb val : _einhasadProb){
				size += ProtoOutputStream.computeMessageSize(6, val);
			}
		}
		if (has_StatMaxInfo()){
			for(EinhasadPointStatInfoT.StatMaxInfoT val : _StatMaxInfo){
				size += ProtoOutputStream.computeMessageSize(7, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_eachStatMax()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_totalStatMax()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_EnchantCost()){
			for(EinhasadPointStatInfoT.EnchantCostT val : _EnchantCost){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_StatMetaData()){
			for(EinhasadPointStatInfoT.StatMetaDataT val : _StatMetaData){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_Stat()){
			for(EinhasadPointStatInfoT.StatT val : _Stat){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_einhasadProb()){
			for(EinhasadPointStatInfoT.EinhasadProb val : _einhasadProb){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_StatMaxInfo()){
			for(EinhasadPointStatInfoT.StatMaxInfoT val : _StatMaxInfo){
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
		if (has_eachStatMax()){
			output.wirteInt32(1, _eachStatMax);
		}
		if (has_totalStatMax()){
			output.wirteInt32(2, _totalStatMax);
		}
		if (has_EnchantCost()){
			for (EinhasadPointStatInfoT.EnchantCostT val : _EnchantCost){
				output.writeMessage(3, val);
			}
		}
		if (has_StatMetaData()){
			for (EinhasadPointStatInfoT.StatMetaDataT val : _StatMetaData){
				output.writeMessage(4, val);
			}
		}
		if (has_Stat()){
			for (EinhasadPointStatInfoT.StatT val : _Stat){
				output.writeMessage(5, val);
			}
		}
		if (has_einhasadProb()){
			for (EinhasadPointStatInfoT.EinhasadProb val : _einhasadProb){
				output.writeMessage(6, val);
			}
		}
		if (has_StatMaxInfo()){
			for (EinhasadPointStatInfoT.StatMaxInfoT val : _StatMaxInfo){
				output.writeMessage(7, val);
			}
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000008:{
					set_eachStatMax(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_totalStatMax(input.readInt32());
					break;
				}
				case 0x0000001A:{
					add_EnchantCost((EinhasadPointStatInfoT.EnchantCostT)input.readMessage(EinhasadPointStatInfoT.EnchantCostT.newInstance()));
					break;
				}
				case 0x00000022:{
					add_StatMetaData((EinhasadPointStatInfoT.StatMetaDataT)input.readMessage(EinhasadPointStatInfoT.StatMetaDataT.newInstance()));
					break;
				}
				case 0x0000002A:{
					add_Stat((EinhasadPointStatInfoT.StatT)input.readMessage(EinhasadPointStatInfoT.StatT.newInstance()));
					break;
				}
				case 0x00000032:{
					add_einhasadProb((EinhasadPointStatInfoT.EinhasadProb)input.readMessage(EinhasadPointStatInfoT.EinhasadProb.newInstance()));
					break;
				}
				case 0x0000003A:{
					add_StatMaxInfo((EinhasadPointStatInfoT.StatMaxInfoT)input.readMessage(EinhasadPointStatInfoT.StatMaxInfoT.newInstance()));
					break;
				}
				default:{
					System.out.println(String.format("[EinhasadPointStatInfoT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class EnchantCostT implements ProtoMessage{
		public static EnchantCostT newInstance(){
			return new EnchantCostT();
		}
		private int _value;
		private int _point;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private EnchantCostT(){
		}
		public int get_value(){
			return _value;
		}
		public void set_value(int val){
			_bit |= 0x1;
			_value = val;
		}
		public boolean has_value(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_point(){
			return _point;
		}
		public void set_point(int val){
			_bit |= 0x2;
			_point = val;
		}
		public boolean has_point(){
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
			if (has_value()){
				size += ProtoOutputStream.computeInt32Size(1, _value);
			}
			if (has_point()){
				size += ProtoOutputStream.computeInt32Size(2, _point);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_value()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_point()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_value()){
				output.wirteInt32(1, _value);
			}
			if (has_point()){
				output.wirteInt32(2, _point);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_value(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_point(input.readInt32());
						break;
					}
					default:{
						System.out.println(String.format("[EinhasadPointStatInfoT.EnchantCostT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class StatMetaDataT implements ProtoMessage{
		public static StatMetaDataT newInstance(){
			return new StatMetaDataT();
		}
		private int _index;
		private EinhasadPointStatInfoT.StatMetaDataT.AbilityMetaDataT _AbilityMetaData1;
		private EinhasadPointStatInfoT.StatMetaDataT.AbilityMetaDataT _AbilityMetaData2;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private StatMetaDataT(){
		}
		public int get_index(){
			return _index;
		}
		public void set_index(int val){
			_bit |= 0x1;
			_index = val;
		}
		public boolean has_index(){
			return (_bit & 0x1) == 0x1;
		}
		public EinhasadPointStatInfoT.StatMetaDataT.AbilityMetaDataT get_AbilityMetaData1(){
			return _AbilityMetaData1;
		}
		public void set_AbilityMetaData1(EinhasadPointStatInfoT.StatMetaDataT.AbilityMetaDataT val){
			_bit |= 0x2;
			_AbilityMetaData1 = val;
		}
		public boolean has_AbilityMetaData1(){
			return (_bit & 0x2) == 0x2;
		}
		public EinhasadPointStatInfoT.StatMetaDataT.AbilityMetaDataT get_AbilityMetaData2(){
			return _AbilityMetaData2;
		}
		public void set_AbilityMetaData2(EinhasadPointStatInfoT.StatMetaDataT.AbilityMetaDataT val){
			_bit |= 0x4;
			_AbilityMetaData2 = val;
		}
		public boolean has_AbilityMetaData2(){
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
			if (has_index()){
				size += ProtoOutputStream.computeInt32Size(1, _index);
			}
			if (has_AbilityMetaData1()){
				size += ProtoOutputStream.computeMessageSize(2, _AbilityMetaData1);
			}
			if (has_AbilityMetaData2()){
				size += ProtoOutputStream.computeMessageSize(3, _AbilityMetaData2);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_index()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_AbilityMetaData1()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_AbilityMetaData2()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_index()){
				output.wirteInt32(1, _index);
			}
			if (has_AbilityMetaData1()){
				output.writeMessage(2, _AbilityMetaData1);
			}
			if (has_AbilityMetaData2()){
				output.writeMessage(3, _AbilityMetaData2);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_index(input.readInt32());
						break;
					}
					case 0x00000012:{
						set_AbilityMetaData1((EinhasadPointStatInfoT.StatMetaDataT.AbilityMetaDataT)input.readMessage(EinhasadPointStatInfoT.StatMetaDataT.AbilityMetaDataT.newInstance()));
						break;
					}
					case 0x0000001A:{
						set_AbilityMetaData2((EinhasadPointStatInfoT.StatMetaDataT.AbilityMetaDataT)input.readMessage(EinhasadPointStatInfoT.StatMetaDataT.AbilityMetaDataT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[EinhasadPointStatInfoT.StatMetaDataT] NEW_TAG : TAG(%d)", tag));
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
		
		public static class AbilityMetaDataT implements ProtoMessage{
			public static AbilityMetaDataT newInstance(){
				return new AbilityMetaDataT();
			}
			private String _token;
			private boolean _x100;
			private EinhasadPointStatInfoT.StatMetaDataT.AbilityMetaDataT.Unit _unit;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private AbilityMetaDataT(){
			}
			public String get_token(){
				return _token;
			}
			public void set_token(String val){
				_bit |= 0x1;
				_token = val;
			}
			public boolean has_token(){
				return (_bit & 0x1) == 0x1;
			}
			public boolean get_x100(){
				return _x100;
			}
			public void set_x100(boolean val){
				_bit |= 0x2;
				_x100 = val;
			}
			public boolean has_x100(){
				return (_bit & 0x2) == 0x2;
			}
			public EinhasadPointStatInfoT.StatMetaDataT.AbilityMetaDataT.Unit get_unit(){
				return _unit;
			}
			public void set_unit(EinhasadPointStatInfoT.StatMetaDataT.AbilityMetaDataT.Unit val){
				_bit |= 0x4;
				_unit = val;
			}
			public boolean has_unit(){
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
				if (has_token()){
					size += ProtoOutputStream.computeStringSize(1, _token);
				}
				if (has_x100()){
					size += ProtoOutputStream.computeBoolSize(2, _x100);
				}
				if (has_unit()){
					size += ProtoOutputStream.computeEnumSize(3, _unit.toInt());
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_token()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_x100()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_unit()){
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(ProtoOutputStream output) throws java.io.IOException{
				if (has_token()){
					output.writeString(1, _token);
				}
				if (has_x100()){
					output.writeBool(2, _x100);
				}
				if (has_unit()){
					output.writeEnum(3, _unit.toInt());
				}
			}
			@Override
			public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
				while(!input.isAtEnd()){
					int tag = input.readTag();
					switch(tag){
						case 0x0000000A:{
							set_token(input.readString());
							break;
						}
						case 0x00000010:{
							set_x100(input.readBool());
							break;
						}
						case 0x00000018:{
							set_unit(EinhasadPointStatInfoT.StatMetaDataT.AbilityMetaDataT.Unit.fromInt(input.readEnum()));
							break;
						}
						default:{
							System.out.println(String.format("[EinhasadPointStatInfoT.StatMetaDataT.AbilityMetaDataT] NEW_TAG : TAG(%d)", tag));
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
			public enum Unit{
				None(1),
				Percent(2),
				;
				private int value;
				Unit(int val){
					value = val;
				}
				public int toInt(){
					return value;
				}
				public boolean equals(Unit v){
					return value == v.value;
				}
				public static Unit fromInt(int i){
					switch(i){
					case 1:
						return None;
					case 2:
						return Percent;
					default:
						throw new IllegalArgumentException(String.format("invalid arguments Unit, %d", i));
					}
				}
			}
		}
	}
	public static class StatT implements ProtoMessage{
		public static StatT newInstance(){
			return new StatT();
		}
		private int _index;
		private int _value;
		private EinhasadPointStatInfoT.StatT.AbilityT _Ability1;
		private EinhasadPointStatInfoT.StatT.AbilityT _Ability2;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private StatT(){
		}
		public int get_index(){
			return _index;
		}
		public void set_index(int val){
			_bit |= 0x1;
			_index = val;
		}
		public boolean has_index(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_value(){
			return _value;
		}
		public void set_value(int val){
			_bit |= 0x2;
			_value = val;
		}
		public boolean has_value(){
			return (_bit & 0x2) == 0x2;
		}
		public EinhasadPointStatInfoT.StatT.AbilityT get_Ability1(){
			return _Ability1;
		}
		public void set_Ability1(EinhasadPointStatInfoT.StatT.AbilityT val){
			_bit |= 0x4;
			_Ability1 = val;
		}
		public boolean has_Ability1(){
			return (_bit & 0x4) == 0x4;
		}
		public EinhasadPointStatInfoT.StatT.AbilityT get_Ability2(){
			return _Ability2;
		}
		public void set_Ability2(EinhasadPointStatInfoT.StatT.AbilityT val){
			_bit |= 0x8;
			_Ability2 = val;
		}
		public boolean has_Ability2(){
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
			if (has_index()){
				size += ProtoOutputStream.computeInt32Size(1, _index);
			}
			if (has_value()){
				size += ProtoOutputStream.computeInt32Size(2, _value);
			}
			if (has_Ability1()){
				size += ProtoOutputStream.computeMessageSize(3, _Ability1);
			}
			if (has_Ability2()){
				size += ProtoOutputStream.computeMessageSize(4, _Ability2);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_index()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_value()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_Ability1()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_Ability2()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_index()){
				output.wirteInt32(1, _index);
			}
			if (has_value()){
				output.wirteInt32(2, _value);
			}
			if (has_Ability1()){
				output.writeMessage(3, _Ability1);
			}
			if (has_Ability2()){
				output.writeMessage(4, _Ability2);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_index(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_value(input.readInt32());
						break;
					}
					case 0x0000001A:{
						set_Ability1((EinhasadPointStatInfoT.StatT.AbilityT)input.readMessage(EinhasadPointStatInfoT.StatT.AbilityT.newInstance()));
						break;
					}
					case 0x00000022:{
						set_Ability2((EinhasadPointStatInfoT.StatT.AbilityT)input.readMessage(EinhasadPointStatInfoT.StatT.AbilityT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[EinhasadPointStatInfoT.StatT] NEW_TAG : TAG(%d)", tag));
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
		
		public static class AbilityT implements ProtoMessage{
			public static AbilityT newInstance(){
				return new AbilityT();
			}
			
			private static final Random _rnd	= new Random(System.nanoTime());
			
			/**
			 * 증가 수치를 조사한다.
			 * 최대값 최소값 계산하여 랜덤 수치 조사
			 * @return value
			 */
			public int get_IncValue() {
				int add = 0;
				if (_maxIncValue > _minIncValue) {
					add = _rnd.nextInt(_maxIncValue - _minIncValue + 1);
				}
				return _minIncValue + add;
			}
			
			private int _minIncValue;
			private int _maxIncValue;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private AbilityT(){
			}
			public int get_minIncValue(){
				return _minIncValue;
			}
			public void set_minIncValue(int val){
				_bit |= 0x1;
				_minIncValue = val;
			}
			public boolean has_minIncValue(){
				return (_bit & 0x1) == 0x1;
			}
			public int get_maxIncValue(){
				return _maxIncValue;
			}
			public void set_maxIncValue(int val){
				_bit |= 0x2;
				_maxIncValue = val;
			}
			public boolean has_maxIncValue(){
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
				if (has_minIncValue()){
					size += ProtoOutputStream.computeInt32Size(1, _minIncValue);
				}
				if (has_maxIncValue()){
					size += ProtoOutputStream.computeInt32Size(2, _maxIncValue);
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_minIncValue()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_maxIncValue()){
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(ProtoOutputStream output) throws java.io.IOException{
				if (has_minIncValue()){
					output.wirteInt32(1, _minIncValue);
				}
				if (has_maxIncValue()){
					output.wirteInt32(2, _maxIncValue);
				}
			}
			@Override
			public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
				while(!input.isAtEnd()){
					int tag = input.readTag();
					switch(tag){
						case 0x00000008:{
							set_minIncValue(input.readInt32());
							break;
						}
						case 0x00000010:{
							set_maxIncValue(input.readInt32());
							break;
						}
						default:{
							System.out.println(String.format("[EinhasadPointStatInfoT.StatT.AbilityT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class EinhasadProb implements ProtoMessage{
		public static EinhasadProb newInstance(){
			return new EinhasadProb();
		}
		private java.util.LinkedList<EinhasadPointStatInfoT.EinhasadProb.EinhasadLevel> _NormalLevels;
		private java.util.LinkedList<EinhasadPointStatInfoT.EinhasadProb.EinhasadLevel> _OverStatMaxPerLevels;
		private java.util.LinkedList<EinhasadPointStatInfoT.EinhasadProb.EinhasadBonusPointProbTable> _probTable;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private EinhasadProb(){
		}
		public java.util.LinkedList<EinhasadPointStatInfoT.EinhasadProb.EinhasadLevel> get_NormalLevels(){
			return _NormalLevels;
		}
		public void add_NormalLevels(EinhasadPointStatInfoT.EinhasadProb.EinhasadLevel val){
			if(!has_NormalLevels()){
				_NormalLevels = new java.util.LinkedList<EinhasadPointStatInfoT.EinhasadProb.EinhasadLevel>();
				_bit |= 0x1;
			}
			_NormalLevels.add(val);
		}
		public boolean has_NormalLevels(){
			return (_bit & 0x1) == 0x1;
		}
		public java.util.LinkedList<EinhasadPointStatInfoT.EinhasadProb.EinhasadLevel> get_OverStatMaxPerLevels(){
			return _OverStatMaxPerLevels;
		}
		public void add_OverStatMaxPerLevels(EinhasadPointStatInfoT.EinhasadProb.EinhasadLevel val){
			if(!has_OverStatMaxPerLevels()){
				_OverStatMaxPerLevels = new java.util.LinkedList<EinhasadPointStatInfoT.EinhasadProb.EinhasadLevel>();
				_bit |= 0x2;
			}
			_OverStatMaxPerLevels.add(val);
		}
		public boolean has_OverStatMaxPerLevels(){
			return (_bit & 0x2) == 0x2;
		}
		public java.util.LinkedList<EinhasadPointStatInfoT.EinhasadProb.EinhasadBonusPointProbTable> get_probTable(){
			return _probTable;
		}
		public void add_probTable(EinhasadPointStatInfoT.EinhasadProb.EinhasadBonusPointProbTable val){
			if(!has_probTable()){
				_probTable = new java.util.LinkedList<EinhasadPointStatInfoT.EinhasadProb.EinhasadBonusPointProbTable>();
				_bit |= 0x4;
			}
			_probTable.add(val);
		}
		public boolean has_probTable(){
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
			if (has_NormalLevels()){
				for(EinhasadPointStatInfoT.EinhasadProb.EinhasadLevel val : _NormalLevels){
					size += ProtoOutputStream.computeMessageSize(1, val);
				}
			}
			if (has_OverStatMaxPerLevels()){
				for(EinhasadPointStatInfoT.EinhasadProb.EinhasadLevel val : _OverStatMaxPerLevels){
					size += ProtoOutputStream.computeMessageSize(2, val);
				}
			}
			if (has_probTable()){
				for(EinhasadPointStatInfoT.EinhasadProb.EinhasadBonusPointProbTable val : _probTable){
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
			if (has_NormalLevels()){
				for(EinhasadPointStatInfoT.EinhasadProb.EinhasadLevel val : _NormalLevels){
					if (!val.isInitialized()){
						_memorizedIsInitialized = -1;
						return false;
					}
				}
			}
			if (has_OverStatMaxPerLevels()){
				for(EinhasadPointStatInfoT.EinhasadProb.EinhasadLevel val : _OverStatMaxPerLevels){
					if (!val.isInitialized()){
						_memorizedIsInitialized = -1;
						return false;
					}
				}
			}
			if (has_probTable()){
				for(EinhasadPointStatInfoT.EinhasadProb.EinhasadBonusPointProbTable val : _probTable){
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
			if (has_NormalLevels()){
				for (EinhasadPointStatInfoT.EinhasadProb.EinhasadLevel val : _NormalLevels){
					output.writeMessage(1, val);
				}
			}
			if (has_OverStatMaxPerLevels()){
				for (EinhasadPointStatInfoT.EinhasadProb.EinhasadLevel val : _OverStatMaxPerLevels){
					output.writeMessage(2, val);
				}
			}
			if (has_probTable()){
				for (EinhasadPointStatInfoT.EinhasadProb.EinhasadBonusPointProbTable val : _probTable){
					output.writeMessage(3, val);
				}
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x0000000A:{
						add_NormalLevels((EinhasadPointStatInfoT.EinhasadProb.EinhasadLevel)input.readMessage(EinhasadPointStatInfoT.EinhasadProb.EinhasadLevel.newInstance()));
						break;
					}
					case 0x00000012:{
						add_OverStatMaxPerLevels((EinhasadPointStatInfoT.EinhasadProb.EinhasadLevel)input.readMessage(EinhasadPointStatInfoT.EinhasadProb.EinhasadLevel.newInstance()));
						break;
					}
					case 0x0000001A:{
						add_probTable((EinhasadPointStatInfoT.EinhasadProb.EinhasadBonusPointProbTable)input.readMessage(EinhasadPointStatInfoT.EinhasadProb.EinhasadBonusPointProbTable.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[EinhasadPointStatInfoT.EinhasadProb] NEW_TAG : TAG(%d)", tag));
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
		
		public static class EinhasadLevel implements ProtoMessage{
			public static EinhasadLevel newInstance(){
				return new EinhasadLevel();
			}
			private int _level;
			private int _prob;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private EinhasadLevel(){
			}
			public int get_level(){
				return _level;
			}
			public void set_level(int val){
				_bit |= 0x1;
				_level = val;
			}
			public boolean has_level(){
				return (_bit & 0x1) == 0x1;
			}
			public int get_prob(){
				return _prob;
			}
			public void set_prob(int val){
				_bit |= 0x2;
				_prob = val;
			}
			public boolean has_prob(){
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
				if (has_level()){
					size += ProtoOutputStream.computeInt32Size(1, _level);
				}
				if (has_prob()){
					size += ProtoOutputStream.computeInt32Size(2, _prob);
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_level()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_prob()){
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(ProtoOutputStream output) throws java.io.IOException{
				if (has_level()){
					output.wirteInt32(1, _level);
				}
				if (has_prob()){
					output.wirteInt32(2, _prob);
				}
			}
			@Override
			public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
				while(!input.isAtEnd()){
					int tag = input.readTag();
					switch(tag){
						case 0x00000008:{
							set_level(input.readInt32());
							break;
						}
						case 0x00000010:{
							set_prob(input.readInt32());
							break;
						}
						default:{
							System.out.println(String.format("[EinhasadPointStatInfoT.EinhasadProb.EinhasadLevel] NEW_TAG : TAG(%d)", tag));
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
		
		public static class EinhasadBonusPointProbTable implements ProtoMessage{
			public static EinhasadBonusPointProbTable newInstance(){
				return new EinhasadBonusPointProbTable();
			}
			private boolean _isLastChance;
			private java.util.LinkedList<EinhasadPointStatInfoT.EinhasadProb.EinhasadBonusPointProbTable.EinhasadBonusPointProb> _probList;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private EinhasadBonusPointProbTable(){
			}
			public boolean get_isLastChance(){
				return _isLastChance;
			}
			public void set_isLastChance(boolean val){
				_bit |= 0x1;
				_isLastChance = val;
			}
			public boolean has_isLastChance(){
				return (_bit & 0x1) == 0x1;
			}
			public java.util.LinkedList<EinhasadPointStatInfoT.EinhasadProb.EinhasadBonusPointProbTable.EinhasadBonusPointProb> get_probList(){
				return _probList;
			}
			public void add_probList(EinhasadPointStatInfoT.EinhasadProb.EinhasadBonusPointProbTable.EinhasadBonusPointProb val){
				if(!has_probList()){
					_probList = new java.util.LinkedList<EinhasadPointStatInfoT.EinhasadProb.EinhasadBonusPointProbTable.EinhasadBonusPointProb>();
					_bit |= 0x2;
				}
				_probList.add(val);
			}
			public boolean has_probList(){
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
				if (has_isLastChance()){
					size += ProtoOutputStream.computeBoolSize(1, _isLastChance);
				}
				if (has_probList()){
					for(EinhasadPointStatInfoT.EinhasadProb.EinhasadBonusPointProbTable.EinhasadBonusPointProb val : _probList){
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
				if (!has_isLastChance()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (has_probList()){
					for(EinhasadPointStatInfoT.EinhasadProb.EinhasadBonusPointProbTable.EinhasadBonusPointProb val : _probList){
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
				if (has_isLastChance()){
					output.writeBool(1, _isLastChance);
				}
				if (has_probList()){
					for (EinhasadPointStatInfoT.EinhasadProb.EinhasadBonusPointProbTable.EinhasadBonusPointProb val : _probList){
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
							set_isLastChance(input.readBool());
							break;
						}
						case 0x00000012:{
							add_probList((EinhasadPointStatInfoT.EinhasadProb.EinhasadBonusPointProbTable.EinhasadBonusPointProb)input.readMessage(EinhasadPointStatInfoT.EinhasadProb.EinhasadBonusPointProbTable.EinhasadBonusPointProb.newInstance()));
							break;
						}
						default:{
							System.out.println(String.format("[EinhasadPointStatInfoT.EinhasadProb.EinhasadBonusPointProbTable] NEW_TAG : TAG(%d)", tag));
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
			
			public static class EinhasadBonusPointProb implements ProtoMessage{
				public static EinhasadBonusPointProb newInstance(){
					return new EinhasadBonusPointProb();
				}
				private int _bonusPoint;
				private int _prob;
				private int _memorizedSerializedSize = -1;
				private byte _memorizedIsInitialized = -1;
				private int _bit;
				private EinhasadBonusPointProb(){
				}
				public int get_bonusPoint(){
					return _bonusPoint;
				}
				public void set_bonusPoint(int val){
					_bit |= 0x1;
					_bonusPoint = val;
				}
				public boolean has_bonusPoint(){
					return (_bit & 0x1) == 0x1;
				}
				public int get_prob(){
					return _prob;
				}
				public void set_prob(int val){
					_bit |= 0x2;
					_prob = val;
				}
				public boolean has_prob(){
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
					if (has_bonusPoint()){
						size += ProtoOutputStream.computeInt32Size(1, _bonusPoint);
					}
					if (has_prob()){
						size += ProtoOutputStream.computeInt32Size(2, _prob);
					}
					_memorizedSerializedSize = size;
					return size;
				}
				@Override
				public boolean isInitialized(){
					if(_memorizedIsInitialized == 1)
						return true;
					if (!has_bonusPoint()){
						_memorizedIsInitialized = -1;
						return false;
					}
					if (!has_prob()){
						_memorizedIsInitialized = -1;
						return false;
					}
					_memorizedIsInitialized = 1;
					return true;
				}
				@Override
				public void writeTo(ProtoOutputStream output) throws java.io.IOException{
					if (has_bonusPoint()){
						output.wirteInt32(1, _bonusPoint);
					}
					if (has_prob()){
						output.wirteInt32(2, _prob);
					}
				}
				@Override
				public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
					while(!input.isAtEnd()){
						int tag = input.readTag();
						switch(tag){
							case 0x00000008:{
								set_bonusPoint(input.readInt32());
								break;
							}
							case 0x00000010:{
								set_prob(input.readInt32());
								break;
							}
							default:{
								System.out.println(String.format("[EinhasadPointStatInfoT.EinhasadProb.EinhasadBonusPointProbTable.EinhasadBonusPointProb] NEW_TAG : TAG(%d)", tag));
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
	
	public static class StatMaxInfoT implements ProtoMessage{
		public static StatMaxInfoT newInstance(){
			return new StatMaxInfoT();
		}
		private int _level;
		private int _statMax;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private StatMaxInfoT(){
		}
		public int get_level(){
			return _level;
		}
		public void set_level(int val){
			_bit |= 0x1;
			_level = val;
		}
		public boolean has_level(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_statMax(){
			return _statMax;
		}
		public void set_statMax(int val){
			_bit |= 0x2;
			_statMax = val;
		}
		public boolean has_statMax(){
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
			if (has_level()){
				size += ProtoOutputStream.computeInt32Size(1, _level);
			}
			if (has_statMax()){
				size += ProtoOutputStream.computeInt32Size(2, _statMax);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_level()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_statMax()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_level()){
				output.wirteInt32(1, _level);
			}
			if (has_statMax()){
				output.wirteInt32(2, _statMax);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_level(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_statMax(input.readInt32());
						break;
					}
					default:{
						System.out.println(String.format("[EinhasadPointStatInfoT.StatMaxInfoT] NEW_TAG : TAG(%d)", tag));
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

