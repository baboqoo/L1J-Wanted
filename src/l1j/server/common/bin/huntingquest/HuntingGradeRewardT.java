package l1j.server.common.bin.huntingquest;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;
import l1j.server.common.data.eMonsterBookV2RewardConnective;
import l1j.server.common.data.eMonsterBookV2RewardGrade;
import l1j.server.common.data.eMonsterBookV2RewardType;
import l1j.server.server.utils.StringUtil;

public class HuntingGradeRewardT implements ProtoMessage{
	public static HuntingGradeRewardT newInstance(){
		return new HuntingGradeRewardT();
	}
	private eMonsterBookV2RewardGrade _grade;
	private HuntingGradeRewardT.ConditionalRewardsT _rewards;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private HuntingGradeRewardT(){
	}
	public eMonsterBookV2RewardGrade get_grade(){
		return _grade;
	}
	public void set_grade(eMonsterBookV2RewardGrade val){
		_bit |= 0x1;
		_grade = val;
	}
	public boolean has_grade(){
		return (_bit & 0x1) == 0x1;
	}
	public HuntingGradeRewardT.ConditionalRewardsT get_rewards(){
		return _rewards;
	}
	public void set_rewards(HuntingGradeRewardT.ConditionalRewardsT val){
		_bit |= 0x2;
		_rewards = val;
	}
	public boolean has_rewards(){
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
		if (has_grade()){
			size += ProtoOutputStream.computeEnumSize(1, _grade.toInt());
		}
		if (has_rewards()){
			size += ProtoOutputStream.computeMessageSize(2, _rewards);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_grade()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_rewards()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_grade()){
			output.writeEnum(1, _grade.toInt());
		}
		if (has_rewards()){
			output.writeMessage(2, _rewards);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000008:{
					set_grade(eMonsterBookV2RewardGrade.fromInt(input.readEnum()));
					break;
				}
				case 0x00000012:{
					set_rewards((HuntingGradeRewardT.ConditionalRewardsT)input.readMessage(HuntingGradeRewardT.ConditionalRewardsT.newInstance()));
					break;
				}
				default:{
					System.out.println(String.format("[HuntingGradeRewardT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class ItemT implements ProtoMessage{
		public static ItemT newInstance(){
			return new ItemT();
		}
		private int _NameID;
		private int _Count;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private ItemT(){
		}
		public int get_NameID(){
			return _NameID;
		}
		public void set_NameID(int val){
			_bit |= 0x1;
			_NameID = val;
		}
		public boolean has_NameID(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_Count(){
			return _Count;
		}
		public void set_Count(int val){
			_bit |= 0x2;
			_Count = val;
		}
		public boolean has_Count(){
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
			if (has_NameID()){
				size += ProtoOutputStream.computeInt32Size(1, _NameID);
			}
			if (has_Count()){
				size += ProtoOutputStream.computeInt32Size(2, _Count);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_NameID()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_Count()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_NameID()){
				output.wirteInt32(1, _NameID);
			}
			if (has_Count()){
				output.wirteInt32(2, _Count);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_NameID(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_Count(input.readInt32());
						break;
					}
					default:{
						System.out.println(String.format("[HuntingGradeRewardT.ItemT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class LevelT implements ProtoMessage{
		public static LevelT newInstance(){
			return new LevelT();
		}
		private int _MinLevel;
		private int _MaxLevel;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private LevelT(){
		}
		public int get_MinLevel(){
			return _MinLevel;
		}
		public void set_MinLevel(int val){
			_bit |= 0x1;
			_MinLevel = val;
		}
		public boolean has_MinLevel(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_MaxLevel(){
			return _MaxLevel;
		}
		public void set_MaxLevel(int val){
			_bit |= 0x2;
			_MaxLevel = val;
		}
		public boolean has_MaxLevel(){
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
			if (has_MinLevel()){
				size += ProtoOutputStream.computeInt32Size(1, _MinLevel);
			}
			if (has_MaxLevel()){
				size += ProtoOutputStream.computeInt32Size(2, _MaxLevel);
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
			if (has_MinLevel()){
				output.wirteInt32(1, _MinLevel);
			}
			if (has_MaxLevel()){
				output.wirteInt32(2, _MaxLevel);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_MinLevel(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_MaxLevel(input.readInt32());
						break;
					}
					default:{
						System.out.println(String.format("[HuntingGradeRewardT.LevelT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class ExpT implements ProtoMessage{
		public static ExpT newInstance(){
			return new ExpT();
		}
		private int _AbsolValue;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private ExpT(){
		}
		public int get_AbsolValue(){
			return _AbsolValue;
		}
		public void set_AbsolValue(int val){
			_bit |= 0x1;
			_AbsolValue = val;
		}
		public boolean has_AbsolValue(){
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
			if (has_AbsolValue()){
				size += ProtoOutputStream.computeInt32Size(1, _AbsolValue);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_AbsolValue()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_AbsolValue()){
				output.wirteInt32(1, _AbsolValue);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_AbsolValue(input.readInt32());
						break;
					}
					default:{
						System.out.println(String.format("[HuntingGradeRewardT.ExpT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class RestGaugeT implements ProtoMessage{
		public static RestGaugeT newInstance(){
			return new RestGaugeT();
		}
		private int _PercentValue;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private RestGaugeT(){
		}
		public int get_PercentValue(){
			return _PercentValue;
		}
		public void set_PercentValue(int val){
			_bit |= 0x1;
			_PercentValue = val;
		}
		public boolean has_PercentValue(){
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
			if (has_PercentValue()){
				size += ProtoOutputStream.computeInt32Size(1, _PercentValue);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_PercentValue()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_PercentValue()){
				output.wirteInt32(1, _PercentValue);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_PercentValue(input.readInt32());
						break;
					}
					default:{
						System.out.println(String.format("[HuntingGradeRewardT.RestGaugeT] NEW_TAG : TAG(%d)", tag));
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
	public static class ConditionsT implements ProtoMessage{
		public static ConditionsT newInstance(){
			return new ConditionsT();
		}
		private java.util.LinkedList<HuntingGradeRewardT.ItemT> _Item;
		private java.util.LinkedList<HuntingGradeRewardT.LevelT> _Level;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private ConditionsT(){
		}
		public java.util.LinkedList<HuntingGradeRewardT.ItemT> get_Item(){
			return _Item;
		}
		public void add_Item(HuntingGradeRewardT.ItemT val){
			if(!has_Item()){
				_Item = new java.util.LinkedList<HuntingGradeRewardT.ItemT>();
				_bit |= 0x1;
			}
			_Item.add(val);
		}
		public boolean has_Item(){
			return (_bit & 0x1) == 0x1;
		}
		public java.util.LinkedList<HuntingGradeRewardT.LevelT> get_Level(){
			return _Level;
		}
		public void add_Level(HuntingGradeRewardT.LevelT val){
			if(!has_Level()){
				_Level = new java.util.LinkedList<HuntingGradeRewardT.LevelT>();
				_bit |= 0x2;
			}
			_Level.add(val);
		}
		public boolean has_Level(){
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
			if (has_Item()){
				for(HuntingGradeRewardT.ItemT val : _Item){
					size += ProtoOutputStream.computeMessageSize(1, val);
				}
			}
			if (has_Level()){
				for(HuntingGradeRewardT.LevelT val : _Level){
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
			if (has_Item()){
				for(HuntingGradeRewardT.ItemT val : _Item){
					if (!val.isInitialized()){
						_memorizedIsInitialized = -1;
						return false;
					}
				}
			}
			if (has_Level()){
				for(HuntingGradeRewardT.LevelT val : _Level){
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
			if (has_Item()){
				for (HuntingGradeRewardT.ItemT val : _Item){
					output.writeMessage(1, val);
				}
			}
			if (has_Level()){
				for (HuntingGradeRewardT.LevelT val : _Level){
					output.writeMessage(2, val);
				}
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x0000000A:{
						add_Item((HuntingGradeRewardT.ItemT)input.readMessage(HuntingGradeRewardT.ItemT.newInstance()));
						break;
					}
					case 0x00000012:{
						add_Level((HuntingGradeRewardT.LevelT)input.readMessage(HuntingGradeRewardT.LevelT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[HuntingGradeRewardT.ConditionsT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class RewardsT implements ProtoMessage{
		public static RewardsT newInstance(){
			return new RewardsT();
		}
		private java.util.LinkedList<HuntingGradeRewardT.ItemT> _Item;
		private java.util.LinkedList<HuntingGradeRewardT.ExpT> _Exp;
		private java.util.LinkedList<HuntingGradeRewardT.RestGaugeT> _RestGauge;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private RewardsT(){
		}
		public java.util.LinkedList<HuntingGradeRewardT.ItemT> get_Item(){
			return _Item;
		}
		public void add_Item(HuntingGradeRewardT.ItemT val){
			if(!has_Item()){
				_Item = new java.util.LinkedList<HuntingGradeRewardT.ItemT>();
				_bit |= 0x1;
			}
			_Item.add(val);
		}
		public boolean has_Item(){
			return (_bit & 0x1) == 0x1;
		}
		public java.util.LinkedList<HuntingGradeRewardT.ExpT> get_Exp(){
			return _Exp;
		}
		public void add_Exp(HuntingGradeRewardT.ExpT val){
			if(!has_Exp()){
				_Exp = new java.util.LinkedList<HuntingGradeRewardT.ExpT>();
				_bit |= 0x2;
			}
			_Exp.add(val);
		}
		public boolean has_Exp(){
			return (_bit & 0x2) == 0x2;
		}
		public java.util.LinkedList<HuntingGradeRewardT.RestGaugeT> get_RestGauge(){
			return _RestGauge;
		}
		public void add_RestGauge(HuntingGradeRewardT.RestGaugeT val){
			if(!has_RestGauge()){
				_RestGauge = new java.util.LinkedList<HuntingGradeRewardT.RestGaugeT>();
				_bit |= 0x4;
			}
			_RestGauge.add(val);
		}
		public boolean has_RestGauge(){
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
			if (has_Item()){
				for(HuntingGradeRewardT.ItemT val : _Item){
					size += ProtoOutputStream.computeMessageSize(1, val);
				}
			}
			if (has_Exp()){
				for(HuntingGradeRewardT.ExpT val : _Exp){
					size += ProtoOutputStream.computeMessageSize(2, val);
				}
			}
			if (has_RestGauge()){
				for(HuntingGradeRewardT.RestGaugeT val : _RestGauge){
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
			if (has_Item()){
				for(HuntingGradeRewardT.ItemT val : _Item){
					if (!val.isInitialized()){
						_memorizedIsInitialized = -1;
						return false;
					}
				}
			}
			if (has_Exp()){
				for(HuntingGradeRewardT.ExpT val : _Exp){
					if (!val.isInitialized()){
						_memorizedIsInitialized = -1;
						return false;
					}
				}
			}
			if (has_RestGauge()){
				for(HuntingGradeRewardT.RestGaugeT val : _RestGauge){
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
			if (has_Item()){
				for (HuntingGradeRewardT.ItemT val : _Item){
					output.writeMessage(1, val);
				}
			}
			if (has_Exp()){
				for (HuntingGradeRewardT.ExpT val : _Exp){
					output.writeMessage(2, val);
				}
			}
			if (has_RestGauge()){
				for (HuntingGradeRewardT.RestGaugeT val : _RestGauge){
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
						add_Item((HuntingGradeRewardT.ItemT)input.readMessage(HuntingGradeRewardT.ItemT.newInstance()));
						break;
					}
					case 0x00000012:{
						add_Exp((HuntingGradeRewardT.ExpT)input.readMessage(HuntingGradeRewardT.ExpT.newInstance()));
						break;
					}
					case 0x0000001A:{
						add_RestGauge((HuntingGradeRewardT.RestGaugeT)input.readMessage(HuntingGradeRewardT.RestGaugeT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[HuntingGradeRewardT.RewardsT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class ConditionRewardSetT implements ProtoMessage{
		public static ConditionRewardSetT newInstance(){
			return new ConditionRewardSetT();
		}
		private HuntingGradeRewardT.ConditionsT _Conditions;
		private HuntingGradeRewardT.RewardsT _Rewards;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private ConditionRewardSetT(){
		}
		public HuntingGradeRewardT.ConditionsT get_Conditions(){
			return _Conditions;
		}
		public void set_Conditions(HuntingGradeRewardT.ConditionsT val){
			_bit |= 0x1;
			_Conditions = val;
		}
		public boolean has_Conditions(){
			return (_bit & 0x1) == 0x1;
		}
		public HuntingGradeRewardT.RewardsT get_Rewards(){
			return _Rewards;
		}
		public void set_Rewards(HuntingGradeRewardT.RewardsT val){
			_bit |= 0x2;
			_Rewards = val;
		}
		public boolean has_Rewards(){
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
			if (has_Conditions()){
				size += ProtoOutputStream.computeMessageSize(1, _Conditions);
			}
			if (has_Rewards()){
				size += ProtoOutputStream.computeMessageSize(2, _Rewards);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_Rewards()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_Conditions()){
				output.writeMessage(1, _Conditions);
			}
			if (has_Rewards()){
				output.writeMessage(2, _Rewards);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x0000000A:{
						set_Conditions((HuntingGradeRewardT.ConditionsT)input.readMessage(HuntingGradeRewardT.ConditionsT.newInstance()));
						break;
					}
					case 0x00000012:{
						set_Rewards((HuntingGradeRewardT.RewardsT)input.readMessage(HuntingGradeRewardT.RewardsT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[HuntingGradeRewardT.ConditionRewardSetT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class ConditionalRewardT implements ProtoMessage{
		public static ConditionalRewardT newInstance(){
			return new ConditionalRewardT();
		}
		private int _ID;
		private eMonsterBookV2RewardType _Type;
		private eMonsterBookV2RewardConnective _Connective;
		private java.util.LinkedList<HuntingGradeRewardT.ConditionRewardSetT> _Set;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private ConditionalRewardT(){
		}
		public int get_ID(){
			return _ID;
		}
		public void set_ID(int val){
			_bit |= 0x1;
			_ID = val;
		}
		public boolean has_ID(){
			return (_bit & 0x1) == 0x1;
		}
		public eMonsterBookV2RewardType get_Type(){
			return _Type;
		}
		public void set_Type(eMonsterBookV2RewardType val){
			_bit |= 0x2;
			_Type = val;
		}
		public boolean has_Type(){
			return (_bit & 0x2) == 0x2;
		}
		public eMonsterBookV2RewardConnective get_Connective(){
			return _Connective;
		}
		public void set_Connective(eMonsterBookV2RewardConnective val){
			_bit |= 0x4;
			_Connective = val;
		}
		public boolean has_Connective(){
			return (_bit & 0x4) == 0x4;
		}
		public java.util.LinkedList<HuntingGradeRewardT.ConditionRewardSetT> get_Set(){
			return _Set;
		}
		public void add_Set(HuntingGradeRewardT.ConditionRewardSetT val){
			if(!has_Set()){
				_Set = new java.util.LinkedList<HuntingGradeRewardT.ConditionRewardSetT>();
				_bit |= 0x8;
			}
			_Set.add(val);
		}
		public boolean has_Set(){
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
			if (has_ID()){
				size += ProtoOutputStream.computeInt32Size(1, _ID);
			}
			if (has_Type()){
				size += ProtoOutputStream.computeEnumSize(2, _Type.toInt());
			}
			if (has_Connective()){
				size += ProtoOutputStream.computeEnumSize(3, _Connective.toInt());
			}
			if (has_Set()){
				for(HuntingGradeRewardT.ConditionRewardSetT val : _Set){
					size += ProtoOutputStream.computeMessageSize(4, val);
				}
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_ID()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_Type()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (has_Set()){
				for(HuntingGradeRewardT.ConditionRewardSetT val : _Set){
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
			if (has_ID()){
				output.wirteInt32(1, _ID);
			}
			if (has_Type()){
				output.writeEnum(2, _Type.toInt());
			}
			if (has_Connective()){
				output.writeEnum(3, _Connective.toInt());
			}
			if (has_Set()){
				for (HuntingGradeRewardT.ConditionRewardSetT val : _Set){
					output.writeMessage(4, val);
				}
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_ID(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_Type(eMonsterBookV2RewardType.fromInt(input.readEnum()));
						break;
					}
					case 0x00000018:{
						set_Connective(eMonsterBookV2RewardConnective.fromInt(input.readEnum()));
						break;
					}
					case 0x00000022:{
						add_Set((HuntingGradeRewardT.ConditionRewardSetT)input.readMessage(HuntingGradeRewardT.ConditionRewardSetT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[HuntingGradeRewardT.ConditionalRewardT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class ConditionalRewardsT implements ProtoMessage{
		public static ConditionalRewardsT newInstance(){
			return new ConditionalRewardsT();
		}
		private java.util.LinkedList<HuntingGradeRewardT.ConditionalRewardT> _ConditionalReward;
		private int _UsedItemID;
		private int _UsedAmount;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private ConditionalRewardsT(){
			set_UsedAmount(1);
		}
		public java.util.LinkedList<HuntingGradeRewardT.ConditionalRewardT> get_ConditionalReward(){
			return _ConditionalReward;
		}
		public void add_ConditionalReward(HuntingGradeRewardT.ConditionalRewardT val){
			if(!has_ConditionalReward()){
				_ConditionalReward = new java.util.LinkedList<HuntingGradeRewardT.ConditionalRewardT>();
				_bit |= 0x1;
			}
			_ConditionalReward.add(val);
		}
		public String get_ConditionalReward_toString() {
			if (_ConditionalReward == null || _ConditionalReward.isEmpty()) {
				return null;
			}
			StringBuilder sb = new StringBuilder();
			for (HuntingGradeRewardT.ConditionalRewardT reward : _ConditionalReward) {
				if (sb.length() > 0) {
					sb.append(StringUtil.LineString);
				}
				sb.append("ID: ").append(reward._ID);
				sb.append(", TYPE: ").append(reward._Type == null ? 0 : reward._Type.toInt());
				sb.append(", CONNECTIVE: ").append(reward._Connective == null ? 0 : reward._Connective.toInt());
				sb.append(", SET[ ");
				if (reward._Set != null && !reward._Set.isEmpty()) {
					int setCnt = 0;
					for (HuntingGradeRewardT.ConditionRewardSetT set : reward._Set) {
						if (setCnt++ > 0) {
							sb.append(" OR ");
						}
						HuntingGradeRewardT.ConditionsT conditions	= set._Conditions;
						if (conditions != null) {
							sb.append("CONDITION( ");
							if (conditions._Item != null && !conditions._Item.isEmpty()) {
								sb.append("ITEMS{");
								int itemCnt = 0;
								for (HuntingGradeRewardT.ItemT item : conditions._Item) {
									if (itemCnt++ > 0) {
										sb.append("OR");
									}
									sb.append("NAMEID: ").append(item._NameID);
									sb.append("&COUNT: ").append(item._Count);
								}
								sb.append("}");
							}
							
							if (conditions._Level != null && !conditions._Level.isEmpty()) {
								sb.append("LEVELS{");
								int levelCnt = 0;
								for (HuntingGradeRewardT.LevelT level : conditions._Level) {
									if (levelCnt++ > 0) {
										sb.append("OR");
									}
									sb.append("MIN: ").append(level._MinLevel);
									sb.append("&MAX: ").append(level._MaxLevel);
								}
								sb.append("}");
							}
							sb.append(")");
						}
						
						HuntingGradeRewardT.RewardsT rewards		= set._Rewards;
						if (rewards != null) {
							sb.append("REWARDS(");
							if (rewards._Item != null && !rewards._Item.isEmpty()) {
								sb.append("ITEMS{");
								int itemCnt = 0;
								for (HuntingGradeRewardT.ItemT item : rewards._Item) {
									if (itemCnt++ > 0) {
										sb.append("OR");
									}
									sb.append("NAMEID: ").append(item._NameID);
									sb.append("&COUNT: ").append(item._Count);
								}
								sb.append("}");
							}
							
							if (rewards._Exp != null && !rewards._Exp.isEmpty()) {
								sb.append("EXPS{");
								int expCnt = 0;
								for (HuntingGradeRewardT.ExpT item : rewards._Exp) {
									if (expCnt++ > 0) {
										sb.append("OR");
									}
									sb.append("VALUE: ").append(item._AbsolValue);
								}
								sb.append("}");
							}
							
							if (rewards._RestGauge != null && !rewards._RestGauge.isEmpty()) {
								sb.append("GAUGES{");
								int gaugeCnt = 0;
								for (HuntingGradeRewardT.RestGaugeT item : rewards._RestGauge) {
									if (gaugeCnt++ > 0) {
										sb.append("OR");
									}
									sb.append("PERCENT: ").append(item._PercentValue);
								}
								sb.append("}");
							}
							sb.append(")");
						}
					}
				}
				sb.append("]");
			}
			return sb.toString();
		}
		public boolean has_ConditionalReward(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_UsedItemID(){
			return _UsedItemID;
		}
		public void set_UsedItemID(int val){
			_bit |= 0x2;
			_UsedItemID = val;
		}
		public boolean has_UsedItemID(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_UsedAmount(){
			return _UsedAmount;
		}
		public void set_UsedAmount(int val){
			_bit |= 0x4;
			_UsedAmount = val;
		}
		public boolean has_UsedAmount(){
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
			if (has_ConditionalReward()){
				for(HuntingGradeRewardT.ConditionalRewardT val : _ConditionalReward){
					size += ProtoOutputStream.computeMessageSize(1, val);
				}
			}
			if (has_UsedItemID()){
				size += ProtoOutputStream.computeInt32Size(2, _UsedItemID);
			}
			if (has_UsedAmount()){
				size += ProtoOutputStream.computeInt32Size(3, _UsedAmount);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (has_ConditionalReward()){
				for(HuntingGradeRewardT.ConditionalRewardT val : _ConditionalReward){
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
			if (has_ConditionalReward()){
				for (HuntingGradeRewardT.ConditionalRewardT val : _ConditionalReward){
					output.writeMessage(1, val);
				}
			}
			if (has_UsedItemID()){
				output.wirteInt32(2, _UsedItemID);
			}
			if (has_UsedAmount()){
				output.wirteInt32(3, _UsedAmount);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x0000000A:{
						add_ConditionalReward((HuntingGradeRewardT.ConditionalRewardT)input.readMessage(HuntingGradeRewardT.ConditionalRewardT.newInstance()));
						break;
					}
					case 0x00000010:{
						set_UsedItemID(input.readInt32());
						break;
					}
					case 0x00000018:{
						set_UsedAmount(input.readInt32());
						break;
					}
					default:{
						System.out.println(String.format("[HuntingGradeRewardT.ConditionalRewardsT] NEW_TAG : TAG(%d)", tag));
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

