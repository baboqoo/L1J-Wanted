package l1j.server.common.bin.huntingquest;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class HuntingQuestConfigT implements ProtoMessage{
	public static HuntingQuestConfigT newInstance(){
		return new HuntingQuestConfigT();
	}
	private int _MaxQuestCount;
	private int _GoalKillCount;
	private HuntingQuestConfigT.SystemT _System;
	private int _EnterMapID;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private HuntingQuestConfigT(){
	}
	public int get_MaxQuestCount(){
		return _MaxQuestCount;
	}
	public void set_MaxQuestCount(int val){
		_bit |= 0x1;
		_MaxQuestCount = val;
	}
	public boolean has_MaxQuestCount(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_GoalKillCount(){
		return _GoalKillCount;
	}
	public void set_GoalKillCount(int val){
		_bit |= 0x2;
		_GoalKillCount = val;
	}
	public boolean has_GoalKillCount(){
		return (_bit & 0x2) == 0x2;
	}
	public HuntingQuestConfigT.SystemT get_System(){
		return _System;
	}
	public void set_System(HuntingQuestConfigT.SystemT val){
		_bit |= 0x4;
		_System = val;
	}
	public boolean has_System(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_EnterMapID(){
		return _EnterMapID;
	}
	public void set_EnterMapID(int val){
		_bit |= 0x8;
		_EnterMapID = val;
	}
	public boolean has_EnterMapID(){
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
		if (has_MaxQuestCount()){
			size += ProtoOutputStream.computeInt32Size(1, _MaxQuestCount);
		}
		if (has_GoalKillCount()){
			size += ProtoOutputStream.computeInt32Size(2, _GoalKillCount);
		}
		if (has_System()){
			size += ProtoOutputStream.computeMessageSize(3, _System);
		}
		if (has_EnterMapID()){
			size += ProtoOutputStream.computeInt32Size(4, _EnterMapID);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_MaxQuestCount()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_GoalKillCount()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_System()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_MaxQuestCount()){
			output.wirteInt32(1, _MaxQuestCount);
		}
		if (has_GoalKillCount()){
			output.wirteInt32(2, _GoalKillCount);
		}
		if (has_System()){
			output.writeMessage(3, _System);
		}
		if (has_EnterMapID()){
			output.wirteInt32(4, _EnterMapID);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000008:{
					set_MaxQuestCount(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_GoalKillCount(input.readInt32());
					break;
				}
				case 0x0000001A:{
					set_System((HuntingQuestConfigT.SystemT)input.readMessage(HuntingQuestConfigT.SystemT.newInstance()));
					break;
				}
				case 0x00000020:{
					set_EnterMapID(input.readInt32());
					break;
				}
				default:{
					System.out.println(String.format("[HuntingQuestConfigT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class SystemT implements ProtoMessage{
		public static SystemT newInstance(){
			return new SystemT();
		}
		private HuntingQuestConfigT.SystemT.ResetTimeT _ResetTime;
		private HuntingQuestConfigT.SystemT.RewardListT _RewardList;
		private java.util.LinkedList<HuntingQuestConfigT.SystemT.RequiredConditionT> _RequiredCondition;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private SystemT(){
		}
		public HuntingQuestConfigT.SystemT.ResetTimeT get_ResetTime(){
			return _ResetTime;
		}
		public void set_ResetTime(HuntingQuestConfigT.SystemT.ResetTimeT val){
			_bit |= 0x1;
			_ResetTime = val;
		}
		public boolean has_ResetTime(){
			return (_bit & 0x1) == 0x1;
		}
		public HuntingQuestConfigT.SystemT.RewardListT get_RewardList(){
			return _RewardList;
		}
		public void set_RewardList(HuntingQuestConfigT.SystemT.RewardListT val){
			_bit |= 0x2;
			_RewardList = val;
		}
		public boolean has_RewardList(){
			return (_bit & 0x2) == 0x2;
		}
		public java.util.LinkedList<HuntingQuestConfigT.SystemT.RequiredConditionT> get_RequiredCondition(){
			return _RequiredCondition;
		}
		public void add_RequiredCondition(HuntingQuestConfigT.SystemT.RequiredConditionT val){
			if(!has_RequiredCondition()){
				_RequiredCondition = new java.util.LinkedList<HuntingQuestConfigT.SystemT.RequiredConditionT>();
				_bit |= 0x4;
			}
			_RequiredCondition.add(val);
		}
		public boolean has_RequiredCondition(){
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
			if (has_ResetTime()){
				size += ProtoOutputStream.computeMessageSize(1, _ResetTime);
			}
			if (has_RewardList()){
				size += ProtoOutputStream.computeMessageSize(2, _RewardList);
			}
			if (has_RequiredCondition()){
				for(HuntingQuestConfigT.SystemT.RequiredConditionT val : _RequiredCondition){
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
			if (has_RequiredCondition()){
				for(HuntingQuestConfigT.SystemT.RequiredConditionT val : _RequiredCondition){
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
			if (has_ResetTime()){
				output.writeMessage(1, _ResetTime);
			}
			if (has_RewardList()){
				output.writeMessage(2, _RewardList);
			}
			if (has_RequiredCondition()){
				for (HuntingQuestConfigT.SystemT.RequiredConditionT val : _RequiredCondition){
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
						set_ResetTime((HuntingQuestConfigT.SystemT.ResetTimeT)input.readMessage(HuntingQuestConfigT.SystemT.ResetTimeT.newInstance()));
						break;
					}
					case 0x00000012:{
						set_RewardList((HuntingQuestConfigT.SystemT.RewardListT)input.readMessage(HuntingQuestConfigT.SystemT.RewardListT.newInstance()));
						break;
					}
					case 0x0000001A:{
						add_RequiredCondition((HuntingQuestConfigT.SystemT.RequiredConditionT)input.readMessage(HuntingQuestConfigT.SystemT.RequiredConditionT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[HuntingQuestConfigT.SystemT] NEW_TAG : TAG(%d)", tag));
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
		
		public static class ResetTimeT implements ProtoMessage{
			public static ResetTimeT newInstance(){
				return new ResetTimeT();
			}
			private int _HourOfDay;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private ResetTimeT(){
			}
			public int get_HourOfDay(){
				return _HourOfDay;
			}
			public void set_HourOfDay(int val){
				_bit |= 0x2;
				_HourOfDay = val;
			}
			public boolean has_HourOfDay(){
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
				if (has_HourOfDay()){
					size += ProtoOutputStream.computeUInt32Size(2, _HourOfDay);
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_HourOfDay()){
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(ProtoOutputStream output) throws java.io.IOException{
				if (has_HourOfDay()){
					output.writeUInt32(2, _HourOfDay);
				}
			}
			@Override
			public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
				while(!input.isAtEnd()){
					int tag = input.readTag();
					switch(tag){
						case 0x00000010:{
							set_HourOfDay(input.readUInt32());
							break;
						}
						default:{
							System.out.println(String.format("[HuntingQuestConfigT.ResetTimeT] NEW_TAG : TAG(%d)", tag));
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
		
		public static class RewardListT implements ProtoMessage{
			public static RewardListT newInstance(){
				return new RewardListT();
			}
			private HuntingGradeRewardT.ConditionalRewardsT _Normal;
			private HuntingGradeRewardT.ConditionalRewardsT _Dragon;
			private HuntingGradeRewardT.ConditionalRewardsT _HighDragon;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private RewardListT(){
			}
			public HuntingGradeRewardT.ConditionalRewardsT get_Normal(){
				return _Normal;
			}
			public void set_Normal(HuntingGradeRewardT.ConditionalRewardsT val){
				_bit |= 0x1;
				_Normal = val;
			}
			public boolean has_Normal(){
				return (_bit & 0x1) == 0x1;
			}
			public HuntingGradeRewardT.ConditionalRewardsT get_Dragon(){
				return _Dragon;
			}
			public void set_Dragon(HuntingGradeRewardT.ConditionalRewardsT val){
				_bit |= 0x2;
				_Dragon = val;
			}
			public boolean has_Dragon(){
				return (_bit & 0x2) == 0x2;
			}
			public HuntingGradeRewardT.ConditionalRewardsT get_HighDragon(){
				return _HighDragon;
			}
			public void set_HighDragon(HuntingGradeRewardT.ConditionalRewardsT val){
				_bit |= 0x4;
				_HighDragon = val;
			}
			public boolean has_HighDragon(){
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
				if (has_Normal()){
					size += ProtoOutputStream.computeMessageSize(1, _Normal);
				}
				if (has_Dragon()){
					size += ProtoOutputStream.computeMessageSize(2, _Dragon);
				}
				if (has_HighDragon()){
					size += ProtoOutputStream.computeMessageSize(3, _HighDragon);
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_Normal()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_Dragon()){
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(ProtoOutputStream output) throws java.io.IOException{
				if (has_Normal()){
					output.writeMessage(1, _Normal);
				}
				if (has_Dragon()){
					output.writeMessage(2, _Dragon);
				}
				if (has_HighDragon()){
					output.writeMessage(3, _HighDragon);
				}
			}
			@Override
			public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
				while(!input.isAtEnd()){
					int tag = input.readTag();
					switch(tag){
						case 0x0000000A:{
							set_Normal((HuntingGradeRewardT.ConditionalRewardsT)input.readMessage(HuntingGradeRewardT.ConditionalRewardsT.newInstance()));
							break;
						}
						case 0x00000012:{
							set_Dragon((HuntingGradeRewardT.ConditionalRewardsT)input.readMessage(HuntingGradeRewardT.ConditionalRewardsT.newInstance()));
							break;
						}
						case 0x0000001A:{
							set_HighDragon((HuntingGradeRewardT.ConditionalRewardsT)input.readMessage(HuntingGradeRewardT.ConditionalRewardsT.newInstance()));
							break;
						}
						default:{
							System.out.println(String.format("[HuntingQuestConfigT.RewardListT] NEW_TAG : TAG(%d)", tag));
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
		
		public static class RequirementT implements ProtoMessage{
			public static RequirementT newInstance(){
				return new RequirementT();
			}
			private HuntingQuestConfigT.SystemT.RequirementT.ItemT _Normal;
			private HuntingQuestConfigT.SystemT.RequirementT.ItemT _Rare;
			private HuntingQuestConfigT.SystemT.RequirementT.ItemT _Boss;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private RequirementT(){
			}
			public HuntingQuestConfigT.SystemT.RequirementT.ItemT get_Normal(){
				return _Normal;
			}
			public void set_Normal(HuntingQuestConfigT.SystemT.RequirementT.ItemT val){
				_bit |= 0x1;
				_Normal = val;
			}
			public boolean has_Normal(){
				return (_bit & 0x1) == 0x1;
			}
			public HuntingQuestConfigT.SystemT.RequirementT.ItemT get_Rare(){
				return _Rare;
			}
			public void set_Rare(HuntingQuestConfigT.SystemT.RequirementT.ItemT val){
				_bit |= 0x2;
				_Rare = val;
			}
			public boolean has_Rare(){
				return (_bit & 0x2) == 0x2;
			}
			public HuntingQuestConfigT.SystemT.RequirementT.ItemT get_Boss(){
				return _Boss;
			}
			public void set_Boss(HuntingQuestConfigT.SystemT.RequirementT.ItemT val){
				_bit |= 0x4;
				_Boss = val;
			}
			public boolean has_Boss(){
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
				if (has_Normal()){
					size += ProtoOutputStream.computeMessageSize(1, _Normal);
				}
				if (has_Rare()){
					size += ProtoOutputStream.computeMessageSize(2, _Rare);
				}
				if (has_Boss()){
					size += ProtoOutputStream.computeMessageSize(3, _Boss);
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_Normal()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_Rare()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_Boss()){
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(ProtoOutputStream output) throws java.io.IOException{
				if (has_Normal()){
					output.writeMessage(1, _Normal);
				}
				if (has_Rare()){
					output.writeMessage(2, _Rare);
				}
				if (has_Boss()){
					output.writeMessage(3, _Boss);
				}
			}
			@Override
			public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
				while(!input.isAtEnd()){
					int tag = input.readTag();
					switch(tag){
						case 0x0000000A:{
							set_Normal((HuntingQuestConfigT.SystemT.RequirementT.ItemT)input.readMessage(HuntingQuestConfigT.SystemT.RequirementT.ItemT.newInstance()));
							break;
						}
						case 0x00000012:{
							set_Rare((HuntingQuestConfigT.SystemT.RequirementT.ItemT)input.readMessage(HuntingQuestConfigT.SystemT.RequirementT.ItemT.newInstance()));
							break;
						}
						case 0x0000001A:{
							set_Boss((HuntingQuestConfigT.SystemT.RequirementT.ItemT)input.readMessage(HuntingQuestConfigT.SystemT.RequirementT.ItemT.newInstance()));
							break;
						}
						default:{
							System.out.println(String.format("[HuntingQuestConfigT.RequirementT] NEW_TAG : TAG(%d)", tag));
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
				private int _Easy;
				private int _Normal;
				private int _Hard;
				private int _memorizedSerializedSize = -1;
				private byte _memorizedIsInitialized = -1;
				private int _bit;
				private ItemT(){
				}
				public int get_Easy(){
					return _Easy;
				}
				public void set_Easy(int val){
					_bit |= 0x1;
					_Easy = val;
				}
				public boolean has_Easy(){
					return (_bit & 0x1) == 0x1;
				}
				public int get_Normal(){
					return _Normal;
				}
				public void set_Normal(int val){
					_bit |= 0x2;
					_Normal = val;
				}
				public boolean has_Normal(){
					return (_bit & 0x2) == 0x2;
				}
				public int get_Hard(){
					return _Hard;
				}
				public void set_Hard(int val){
					_bit |= 0x4;
					_Hard = val;
				}
				public boolean has_Hard(){
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
					if (has_Easy()){
						size += ProtoOutputStream.computeUInt32Size(1, _Easy);
					}
					if (has_Normal()){
						size += ProtoOutputStream.computeUInt32Size(2, _Normal);
					}
					if (has_Hard()){
						size += ProtoOutputStream.computeUInt32Size(3, _Hard);
					}
					_memorizedSerializedSize = size;
					return size;
				}
				@Override
				public boolean isInitialized(){
					if(_memorizedIsInitialized == 1)
						return true;
					if (!has_Easy()){
						_memorizedIsInitialized = -1;
						return false;
					}
					if (!has_Normal()){
						_memorizedIsInitialized = -1;
						return false;
					}
					if (!has_Hard()){
						_memorizedIsInitialized = -1;
						return false;
					}
					_memorizedIsInitialized = 1;
					return true;
				}
				@Override
				public void writeTo(ProtoOutputStream output) throws java.io.IOException{
					if (has_Easy()){
						output.writeUInt32(1, _Easy);
					}
					if (has_Normal()){
						output.writeUInt32(2, _Normal);
					}
					if (has_Hard()){
						output.writeUInt32(3, _Hard);
					}
				}
				@Override
				public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
					while(!input.isAtEnd()){
						int tag = input.readTag();
						switch(tag){
							case 0x00000008:{
								set_Easy(input.readUInt32());
								break;
							}
							case 0x00000010:{
								set_Normal(input.readUInt32());
								break;
							}
							case 0x00000018:{
								set_Hard(input.readUInt32());
								break;
							}
							default:{
								System.out.println(String.format("[HuntingQuestConfigT.RequirementT.ItemT] NEW_TAG : TAG(%d)", tag));
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
		
		public static class ProbT implements ProtoMessage{
			public static ProbT newInstance(){
				return new ProbT();
			}
			private int _RareProb;
			private int _BossProb;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private ProbT(){
			}
			public int get_RareProb(){
				return _RareProb;
			}
			public void set_RareProb(int val){
				_bit |= 0x1;
				_RareProb = val;
			}
			public boolean has_RareProb(){
				return (_bit & 0x1) == 0x1;
			}
			public int get_BossProb(){
				return _BossProb;
			}
			public void set_BossProb(int val){
				_bit |= 0x2;
				_BossProb = val;
			}
			public boolean has_BossProb(){
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
				if (has_RareProb()){
					size += ProtoOutputStream.computeInt32Size(1, _RareProb);
				}
				if (has_BossProb()){
					size += ProtoOutputStream.computeInt32Size(2, _BossProb);
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_RareProb()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_BossProb()){
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(ProtoOutputStream output) throws java.io.IOException{
				if (has_RareProb()){
					output.wirteInt32(1, _RareProb);
				}
				if (has_BossProb()){
					output.wirteInt32(2, _BossProb);
				}
			}
			@Override
			public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
				while(!input.isAtEnd()){
					int tag = input.readTag();
					switch(tag){
						case 0x00000008:{
							set_RareProb(input.readInt32());
							break;
						}
						case 0x00000010:{
							set_BossProb(input.readInt32());
							break;
						}
						default:{
							System.out.println(String.format("[HuntingQuestConfigT.ProbT] NEW_TAG : TAG(%d)", tag));
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
		
		public static class RequiredConditionT implements ProtoMessage{
			public static RequiredConditionT newInstance(){
				return new RequiredConditionT();
			}
			private int _MinLevel;
			private int _MaxLevel;
			private int _Map;
			private int _LocationDesc;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private RequiredConditionT(){
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
			public int get_Map(){
				return _Map;
			}
			public void set_Map(int val){
				_bit |= 0x4;
				_Map = val;
			}
			public boolean has_Map(){
				return (_bit & 0x4) == 0x4;
			}
			public int get_LocationDesc(){
				return _LocationDesc;
			}
			public void set_LocationDesc(int val){
				_bit |= 0x8;
				_LocationDesc = val;
			}
			public boolean has_LocationDesc(){
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
				if (has_MinLevel()){
					size += ProtoOutputStream.computeUInt32Size(1, _MinLevel);
				}
				if (has_MaxLevel()){
					size += ProtoOutputStream.computeUInt32Size(2, _MaxLevel);
				}
				if (has_Map()){
					size += ProtoOutputStream.computeInt32Size(3, _Map);
				}
				if (has_LocationDesc()){
					size += ProtoOutputStream.computeInt32Size(4, _LocationDesc);
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_MinLevel()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_MaxLevel()){
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(ProtoOutputStream output) throws java.io.IOException{
				if (has_MinLevel()){
					output.writeUInt32(1, _MinLevel);
				}
				if (has_MaxLevel()){
					output.writeUInt32(2, _MaxLevel);
				}
				if (has_Map()){
					output.wirteInt32(3, _Map);
				}
				if (has_LocationDesc()){
					output.wirteInt32(4, _LocationDesc);
				}
			}
			@Override
			public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
				while(!input.isAtEnd()){
					int tag = input.readTag();
					switch(tag){
						case 0x00000008:{
							set_MinLevel(input.readUInt32());
							break;
						}
						case 0x00000010:{
							set_MaxLevel(input.readUInt32());
							break;
						}
						case 0x00000018:{
							set_Map(input.readInt32());
							break;
						}
						case 0x00000020:{
							set_LocationDesc(input.readInt32());
							break;
						}
						default:{
							System.out.println(String.format("[HuntingQuestConfigT.RequiredConditionT] NEW_TAG : TAG(%d)", tag));
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

