package l1j.server.common.bin.timecollection;

import l1j.server.common.DescKLoader;
import l1j.server.common.bin.ItemCommonBinLoader;
import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;
import l1j.server.common.bin.item.CommonItemInfo;
import l1j.server.common.data.NPCDialogInfoT;
import l1j.server.common.data.TimeCollectionSetType;
import l1j.server.server.model.item.ablity.ItemAbilityFactory;
import l1j.server.server.utils.StringUtil;

public class TimeCollection implements ProtoMessage{
	public static TimeCollection newInstance(){
		return new TimeCollection();
	}
	private TimeCollection.BuffSelectT _BuffSelect;
	private TimeCollection.RewardListT _RewardList;
	private TimeCollection.EnchantSectionT _EnchantSection;
	private java.util.LinkedList<TimeCollection.GroupT> _Group;
	private TimeCollection.ExtraTimeSectionT _ExtraTimeSection;
	private NPCDialogInfoT _NPCDialogInfo;
	private TimeCollection.AlarmSettingT _AlarmSetting;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private TimeCollection(){
	}
	public TimeCollection.BuffSelectT get_BuffSelect(){
		return _BuffSelect;
	}
	public void set_BuffSelect(TimeCollection.BuffSelectT val){
		_bit |= 0x1;
		_BuffSelect = val;
	}
	public boolean has_BuffSelect(){
		return (_bit & 0x1) == 0x1;
	}
	public TimeCollection.RewardListT get_RewardList(){
		return _RewardList;
	}
	public void set_RewardList(TimeCollection.RewardListT val){
		_bit |= 0x2;
		_RewardList = val;
	}
	public boolean has_RewardList(){
		return (_bit & 0x2) == 0x2;
	}
	public TimeCollection.EnchantSectionT get_EnchantSection(){
		return _EnchantSection;
	}
	public void set_EnchantSection(TimeCollection.EnchantSectionT val){
		_bit |= 0x4;
		_EnchantSection = val;
	}
	public boolean has_EnchantSection(){
		return (_bit & 0x4) == 0x4;
	}
	public java.util.LinkedList<TimeCollection.GroupT> get_Group(){
		return _Group;
	}
	public void add_Group(TimeCollection.GroupT val){
		if(!has_Group()){
			_Group = new java.util.LinkedList<TimeCollection.GroupT>();
			_bit |= 0x8;
		}
		_Group.add(val);
	}
	public boolean has_Group(){
		return (_bit & 0x8) == 0x8;
	}
	public TimeCollection.ExtraTimeSectionT get_ExtraTimeSection(){
		return _ExtraTimeSection;
	}
	public void set_ExtraTimeSection(TimeCollection.ExtraTimeSectionT val){
		_bit |= 0x10;
		_ExtraTimeSection = val;
	}

	public boolean has_ExtraTimeSection(){
		return (_bit & 0x10) == 0x10;
	}
	public NPCDialogInfoT get_NPCDialogInfo(){
		return _NPCDialogInfo;
	}
	public void set_NPCDialogInfo(NPCDialogInfoT val){
		_bit |= 0x20;
		_NPCDialogInfo = val;
	}
	public String get_NPCDialogInfo_toString(){
		if (_NPCDialogInfo == null) {
			return null;
		}
		
		java.util.LinkedList<NPCDialogInfoT.LinkerT> Linkers = _NPCDialogInfo.get_Linker();
		if (Linkers == null || Linkers.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (NPCDialogInfoT.LinkerT LinkerT : Linkers) {
			if (sb.length() > 0) {
				sb.append(StringUtil.LineString);
			}
			sb.append("LinkReq: ").append(LinkerT.get_LinkReq());
			sb.append(", Index: ").append(LinkerT.get_Index());
		}
		return sb.toString();
	}
	public boolean has_NPCDialogInfo(){
		return (_bit & 0x20) == 0x20;
	}
	public TimeCollection.AlarmSettingT get_AlarmSetting(){
		return _AlarmSetting;
	}
	public void set_AlarmSetting(TimeCollection.AlarmSettingT val){
		_bit |= 0x40;
		_AlarmSetting = val;
	}
	public String get_AlarmSetting_toString(){
		if (_AlarmSetting == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("AlarmDays: ").append(_AlarmSetting._AlarmDays);
		sb.append(", AlarmMinute: ").append(_AlarmSetting._AlarmMinute);
		return sb.toString();
	}
	public boolean has_AlarmSetting(){
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
		if (has_BuffSelect()){
			size += ProtoOutputStream.computeMessageSize(1, _BuffSelect);
		}
		if (has_RewardList()){
			size += ProtoOutputStream.computeMessageSize(2, _RewardList);
		}
		if (has_EnchantSection()){
			size += ProtoOutputStream.computeMessageSize(3, _EnchantSection);
		}
		if (has_Group()){
			for(TimeCollection.GroupT val : _Group){
				size += ProtoOutputStream.computeMessageSize(4, val);
			}
		}
		if (has_ExtraTimeSection()){
			size += ProtoOutputStream.computeMessageSize(5, _ExtraTimeSection);
		}
		if (has_NPCDialogInfo()){
			size += ProtoOutputStream.computeMessageSize(6, _NPCDialogInfo);
		}
		if (has_AlarmSetting()){
			size += ProtoOutputStream.computeMessageSize(7, _AlarmSetting);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_BuffSelect()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_RewardList()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_EnchantSection()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_Group()){
			for(TimeCollection.GroupT val : _Group){
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
		if (has_BuffSelect()){
			output.writeMessage(1, _BuffSelect);
		}
		if (has_RewardList()){
			output.writeMessage(2, _RewardList);
		}
		if (has_EnchantSection()){
			output.writeMessage(3, _EnchantSection);
		}
		if (has_Group()){
			for (TimeCollection.GroupT val : _Group){
				output.writeMessage(4, val);
			}
		}
		if (has_ExtraTimeSection()){
			output.writeMessage(5, _ExtraTimeSection);
		}
		if (has_NPCDialogInfo()){
			output.writeMessage(6, _NPCDialogInfo);
		}
		if (has_AlarmSetting()){
			output.writeMessage(7, _AlarmSetting);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x0000000A:{
					set_BuffSelect((TimeCollection.BuffSelectT)input.readMessage(TimeCollection.BuffSelectT.newInstance()));
					break;
				}
				case 0x00000012:{
					set_RewardList((TimeCollection.RewardListT)input.readMessage(TimeCollection.RewardListT.newInstance()));
					break;
				}
				case 0x0000001A:{
					set_EnchantSection((TimeCollection.EnchantSectionT)input.readMessage(TimeCollection.EnchantSectionT.newInstance()));
					break;
				}
				case 0x00000022:{
					add_Group((TimeCollection.GroupT)input.readMessage(TimeCollection.GroupT.newInstance()));
					break;
				}
				case 0x0000002A:{
					set_ExtraTimeSection((TimeCollection.ExtraTimeSectionT)input.readMessage(TimeCollection.ExtraTimeSectionT.newInstance()));
					break;
				}
				case 0x00000032:{
					set_NPCDialogInfo((NPCDialogInfoT)input.readMessage(NPCDialogInfoT.newInstance()));
					break;
				}
				case 0x0000003A:{
					set_AlarmSetting((TimeCollection.AlarmSettingT)input.readMessage(TimeCollection.AlarmSettingT.newInstance()));
					break;
				}
				default:{
					System.out.println(String.format("[TimeCollection] NEW_TAG : TAG(%d)", tag));
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
	
	public static class BuffSelectT implements ProtoMessage{
		public static BuffSelectT newInstance(){
			return new BuffSelectT();
		}
		private java.util.LinkedList<TimeCollection.BuffSelectT.UserT> _User;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private BuffSelectT(){
		}
		public java.util.LinkedList<TimeCollection.BuffSelectT.UserT> get_User(){
			return _User;
		}
		public void add_User(TimeCollection.BuffSelectT.UserT val){
			if(!has_User()){
				_User = new java.util.LinkedList<TimeCollection.BuffSelectT.UserT>();
				_bit |= 0x1;
			}
			_User.add(val);
		}
		public String get_user_toString() {
			if (_User == null || _User.isEmpty()) {
				return null;
			}
			StringBuilder sb = new StringBuilder();
			for (TimeCollection.BuffSelectT.UserT user : _User) {
				if (sb.length() > 0) {
					sb.append(StringUtil.LineString);
				}
				sb.append("GAME_CLASS: ").append(user._GameClass);
				sb.append(", BUFF_TYPE: ").append(user._BuffType);
			}
			return sb.toString();
		}
		public boolean has_User(){
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
			if (has_User()){
				for(TimeCollection.BuffSelectT.UserT val : _User){
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
			if (has_User()){
				for(TimeCollection.BuffSelectT.UserT val : _User){
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
			if (has_User()){
				for (TimeCollection.BuffSelectT.UserT val : _User){
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
						add_User((TimeCollection.BuffSelectT.UserT)input.readMessage(TimeCollection.BuffSelectT.UserT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[TimeCollection.BuffSelectT] NEW_TAG : TAG(%d)", tag));
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
		
		public static class UserT implements ProtoMessage{
			public static UserT newInstance(){
				return new UserT();
			}
			private int _GameClass;
			private int _BuffType;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private UserT(){
			}
			public int get_GameClass(){
				return _GameClass;
			}
			public void set_GameClass(int val){
				_bit |= 0x1;
				_GameClass = val;
			}
			public boolean has_GameClass(){
				return (_bit & 0x1) == 0x1;
			}
			public int get_BuffType(){
				return _BuffType;
			}
			public void set_BuffType(int val){
				_bit |= 0x2;
				_BuffType = val;
			}
			public boolean has_BuffType(){
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
				if (has_GameClass()){
					size += ProtoOutputStream.computeInt32Size(1, _GameClass);
				}
				if (has_BuffType()){
					size += ProtoOutputStream.computeInt32Size(2, _BuffType);
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_GameClass()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_BuffType()){
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(ProtoOutputStream output) throws java.io.IOException{
				if (has_GameClass()){
					output.wirteInt32(1, _GameClass);
				}
				if (has_BuffType()){
					output.wirteInt32(2, _BuffType);
				}
			}
			@Override
			public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
				while(!input.isAtEnd()){
					int tag = input.readTag();
					switch(tag){
						case 0x00000008:{
							set_GameClass(input.readInt32());
							break;
						}
						case 0x00000010:{
							set_BuffType(input.readInt32());
							break;
						}
						default:{
							System.out.println(String.format("[TimeCollection.UserT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class RewardListT implements ProtoMessage{
		public static RewardListT newInstance(){
			return new RewardListT();
		}
		private java.util.LinkedList<TimeCollection.RewardListT.RewardT> _Reward;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private RewardListT(){
		}
		public java.util.LinkedList<TimeCollection.RewardListT.RewardT> get_Reward(){
			return _Reward;
		}
		public void add_Reward(TimeCollection.RewardListT.RewardT val){
			if(!has_Reward()){
				_Reward = new java.util.LinkedList<TimeCollection.RewardListT.RewardT>();
				_bit |= 0x1;
			}
			_Reward.add(val);
		}
		public String get_Reward_toString() {
			if (_Reward == null || _Reward.isEmpty()) {
				return null;
			}
			StringBuilder sb = new StringBuilder();
			for (TimeCollection.RewardListT.RewardT reward : _Reward) {
				if (sb.length() > 0) {
					sb.append(StringUtil.LineString);
				}
				sb.append("TYPE: ").append(reward._Type);
				sb.append(", PROB: ").append(reward._Prob);
				sb.append(", BONUSTIME: ").append(reward._BonusTime);
				sb.append(", BONUSTIME_PERCENT: ").append(reward._BonusTimePercent);
			}
			return sb.toString();
		}
		public boolean has_Reward(){
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
			if (has_Reward()){
				for(TimeCollection.RewardListT.RewardT val : _Reward){
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
			if (has_Reward()){
				for(TimeCollection.RewardListT.RewardT val : _Reward){
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
			if (has_Reward()){
				for (TimeCollection.RewardListT.RewardT val : _Reward){
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
						add_Reward((TimeCollection.RewardListT.RewardT)input.readMessage(TimeCollection.RewardListT.RewardT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[TimeCollection.RewardListT] NEW_TAG : TAG(%d)", tag));
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
		
		public static class RewardT implements ProtoMessage{
			public static RewardT newInstance(){
				return new RewardT();
			}
			private int _Type;
			private int _Prob;
			private String _BonusTime;
			private int _BonusTimePercent;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private RewardT(){
			}
			public int get_Type(){
				return _Type;
			}
			public void set_Type(int val){
				_bit |= 0x1;
				_Type = val;
			}
			public boolean has_Type(){
				return (_bit & 0x1) == 0x1;
			}
			public int get_Prob(){
				return _Prob;
			}
			public void set_Prob(int val){
				_bit |= 0x2;
				_Prob = val;
			}
			public boolean has_Prob(){
				return (_bit & 0x2) == 0x2;
			}
			public String get_BonusTime(){
				return _BonusTime;
			}
			public void set_BonusTime(String val){
				_bit |= 0x4;
				_BonusTime = val;
			}
			public boolean has_BonusTime(){
				return (_bit & 0x4) == 0x4;
			}
			public int get_BonusTimePercent(){
				return _BonusTimePercent;
			}
			public void set_BonusTimePercent(int val){
				_bit |= 0x8;
				_BonusTimePercent = val;
			}
			public boolean has_BonusTimePercent(){
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
				if (has_Type()){
					size += ProtoOutputStream.computeInt32Size(1, _Type);
				}
				if (has_Prob()){
					size += ProtoOutputStream.computeInt32Size(2, _Prob);
				}
				if (has_BonusTime()){
					size += ProtoOutputStream.computeStringSize(3, _BonusTime);
				}
				if (has_BonusTimePercent()){
					size += ProtoOutputStream.computeInt32Size(4, _BonusTimePercent);
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_Type()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_Prob()){
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(ProtoOutputStream output) throws java.io.IOException{
				if (has_Type()){
					output.wirteInt32(1, _Type);
				}
				if (has_Prob()){
					output.wirteInt32(2, _Prob);
				}
				if (has_BonusTime()){
					output.writeString(3, _BonusTime);
				}
				if (has_BonusTimePercent()){
					output.wirteInt32(4, _BonusTimePercent);
				}
			}
			@Override
			public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
				while(!input.isAtEnd()){
					int tag = input.readTag();
					switch(tag){
						case 0x00000008:{
							set_Type(input.readInt32());
							break;
						}
						case 0x00000010:{
							set_Prob(input.readInt32());
							break;
						}
						case 0x0000001A:{
							set_BonusTime(input.readString());
							break;
						}
						case 0x00000020:{
							set_BonusTimePercent(input.readInt32());
							break;
						}
						default:{
							System.out.println(String.format("[TimeCollection.RewardT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class EnchantSectionT implements ProtoMessage{
		public static EnchantSectionT newInstance(){
			return new EnchantSectionT();
		}
		private java.util.LinkedList<TimeCollection.EnchantSectionT.EnchantIDT> _EnchantID;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private EnchantSectionT(){
		}
		public java.util.LinkedList<TimeCollection.EnchantSectionT.EnchantIDT> get_EnchantID(){
			return _EnchantID;
		}
		public void add_EnchantID(TimeCollection.EnchantSectionT.EnchantIDT val){
			if(!has_EnchantID()){
				_EnchantID = new java.util.LinkedList<TimeCollection.EnchantSectionT.EnchantIDT>();
				_bit |= 0x1;
			}
			_EnchantID.add(val);
		}
		public String get_EnchantID_toString() {
			if (_EnchantID == null || _EnchantID.isEmpty()) {
				return null;
			}
			StringBuilder sb = new StringBuilder();
			for (TimeCollection.EnchantSectionT.EnchantIDT enchant : _EnchantID) {
				if (sb.length() > 0) {
					sb.append(StringUtil.LineString);
				}
				sb.append("====================\r\nID: ").append(enchant._ID);
				sb.append("\r\nMIN: ").append(enchant._EnchantMin);
				sb.append("\r\nMAX: ").append(enchant._EnchantMax);
				if (enchant._EnchantBonus != null && !enchant._EnchantBonus.isEmpty()) {
					for (TimeCollection.EnchantSectionT.EnchantIDT.EnchantBonusT bonus : enchant._EnchantBonus) {
						sb.append("\r\nBONUS: ENCHANT=").append(bonus._Enchant);
						sb.append(", TIME=").append(bonus._BonusTime);
					}
				}
			}
			return sb.toString();
		}
		public boolean has_EnchantID(){
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
			if (has_EnchantID()){
				for(TimeCollection.EnchantSectionT.EnchantIDT val : _EnchantID){
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
			if (has_EnchantID()){
				for(TimeCollection.EnchantSectionT.EnchantIDT val : _EnchantID){
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
			if (has_EnchantID()){
				for (TimeCollection.EnchantSectionT.EnchantIDT val : _EnchantID){
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
						add_EnchantID((TimeCollection.EnchantSectionT.EnchantIDT)input.readMessage(TimeCollection.EnchantSectionT.EnchantIDT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[TimeCollection.EnchantSectionT] NEW_TAG : TAG(%d)", tag));
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
		
		public static class EnchantIDT implements ProtoMessage{
			public static EnchantIDT newInstance(){
				return new EnchantIDT();
			}
			private int _ID;
			private int _EnchantMin;
			private int _EnchantMax;
			private java.util.LinkedList<TimeCollection.EnchantSectionT.EnchantIDT.EnchantBonusT> _EnchantBonus;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private EnchantIDT(){
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
			public int get_EnchantMin(){
				return _EnchantMin;
			}
			public void set_EnchantMin(int val){
				_bit |= 0x2;
				_EnchantMin = val;
			}
			public boolean has_EnchantMin(){
				return (_bit & 0x2) == 0x2;
			}
			public int get_EnchantMax(){
				return _EnchantMax;
			}
			public void set_EnchantMax(int val){
				_bit |= 0x4;
				_EnchantMax = val;
			}
			public boolean has_EnchantMax(){
				return (_bit & 0x4) == 0x4;
			}
			public java.util.LinkedList<TimeCollection.EnchantSectionT.EnchantIDT.EnchantBonusT> get_EnchantBonus(){
				return _EnchantBonus;
			}
			public void add_EnchantBonus(TimeCollection.EnchantSectionT.EnchantIDT.EnchantBonusT val){
				if(!has_EnchantBonus()){
					_EnchantBonus = new java.util.LinkedList<TimeCollection.EnchantSectionT.EnchantIDT.EnchantBonusT>();
					_bit |= 0x8;
				}
				_EnchantBonus.add(val);
			}
			public boolean has_EnchantBonus(){
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
				if (has_EnchantMin()){
					size += ProtoOutputStream.computeInt32Size(2, _EnchantMin);
				}
				if (has_EnchantMax()){
					size += ProtoOutputStream.computeInt32Size(3, _EnchantMax);
				}
				if (has_EnchantBonus()){
					for(TimeCollection.EnchantSectionT.EnchantIDT.EnchantBonusT val : _EnchantBonus){
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
				if (!has_EnchantMin()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_EnchantMax()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (has_EnchantBonus()){
					for(TimeCollection.EnchantSectionT.EnchantIDT.EnchantBonusT val : _EnchantBonus){
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
				if (has_EnchantMin()){
					output.wirteInt32(2, _EnchantMin);
				}
				if (has_EnchantMax()){
					output.wirteInt32(3, _EnchantMax);
				}
				if (has_EnchantBonus()){
					for (TimeCollection.EnchantSectionT.EnchantIDT.EnchantBonusT val : _EnchantBonus){
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
							set_EnchantMin(input.readInt32());
							break;
						}
						case 0x00000018:{
							set_EnchantMax(input.readInt32());
							break;
						}
						case 0x00000022:{
							add_EnchantBonus((TimeCollection.EnchantSectionT.EnchantIDT.EnchantBonusT)input.readMessage(TimeCollection.EnchantSectionT.EnchantIDT.EnchantBonusT.newInstance()));
							break;
						}
						default:{
							System.out.println(String.format("[TimeCollection.EnchantIDT] NEW_TAG : TAG(%d)", tag));
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
			
			public static class EnchantBonusT implements ProtoMessage{
				public static EnchantBonusT newInstance(){
					return new EnchantBonusT();
				}
				private int _Enchant;
				private String _BonusTime;
				private int _memorizedSerializedSize = -1;
				private byte _memorizedIsInitialized = -1;
				private int _bit;
				private EnchantBonusT(){
				}
				public int get_Enchant(){
					return _Enchant;
				}
				public void set_Enchant(int val){
					_bit |= 0x1;
					_Enchant = val;
				}
				public boolean has_Enchant(){
					return (_bit & 0x1) == 0x1;
				}
				public String get_BonusTime(){
					return _BonusTime;
				}
				public void set_BonusTime(String val){
					_bit |= 0x2;
					_BonusTime = val;
				}
				public boolean has_BonusTime(){
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
					if (has_Enchant()){
						size += ProtoOutputStream.computeInt32Size(1, _Enchant);
					}
					if (has_BonusTime()){
						size += ProtoOutputStream.computeStringSize(2, _BonusTime);
					}
					_memorizedSerializedSize = size;
					return size;
				}
				@Override
				public boolean isInitialized(){
					if(_memorizedIsInitialized == 1)
						return true;
					if (!has_Enchant()){
						_memorizedIsInitialized = -1;
						return false;
					}
					_memorizedIsInitialized = 1;
					return true;
				}
				@Override
				public void writeTo(ProtoOutputStream output) throws java.io.IOException{
					if (has_Enchant()){
						output.wirteInt32(1, _Enchant);
					}
					if (has_BonusTime()){
						output.writeString(2, _BonusTime);
					}
				}
				@Override
				public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
					while(!input.isAtEnd()){
						int tag = input.readTag();
						switch(tag){
							case 0x00000008:{
								set_Enchant(input.readInt32());
								break;
							}
							case 0x00000012:{
								set_BonusTime(input.readString());
								break;
							}
							default:{
								System.out.println(String.format("[TimeCollection.EnchantBonusT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class BonusT implements ProtoMessage{
		public static BonusT newInstance(){
			return new BonusT();
		}
		
		private ItemAbilityFactory _factory;
		public ItemAbilityFactory get_factory() {
			return _factory;
		}
		public void set_factory(byte[] val) {
			_factory = ItemAbilityFactory.getAbility(val[0] & 0xFF);
			if (_factory == null) {
				System.out.println(String.format("[TimeCollection.BonusT] UNDEFINED_ENUM_DESC", val[0] & 0xFF));
			}
		}
		private int _value_int;
		public int get_value_int() {
			return _value_int;
		}
		public void set_value_int(int val) {
			_value_int = val;
		}
		
		private String _Token;
		private String _Value;
		private byte[] _Desc;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private BonusT(){
		}
		public String get_Token(){
			return _Token;
		}
		public void set_Token(String val){
			_bit |= 0x1;
			_Token = val;
		}
		public boolean has_Token(){
			return (_bit & 0x1) == 0x1;
		}
		public String get_Value(){
			return _Value;
		}
		public void set_Value(String val){
			_bit |= 0x2;
			_Value = val;
			set_value_int(Integer.parseInt(_Value));
		}
		public boolean has_Value(){
			return (_bit & 0x2) == 0x2;
		}
		public byte[] get_Desc(){
			return _Desc;
		}
		public void set_Desc(byte[] val){
			_bit |= 0x4;
			_Desc = val;
			set_factory(_Desc);
		}
		public boolean has_Desc(){
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
			if (has_Token()){
				size += ProtoOutputStream.computeStringSize(1, _Token);
			}
			if (has_Value()){
				size += ProtoOutputStream.computeStringSize(2, _Value);
			}
			if (has_Desc()){
				size += ProtoOutputStream.computeBytesSize(3, _Desc);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_Token()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_Value()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_Token()){
				output.writeString(1, _Token);
			}
			if (has_Value()){
				output.writeString(2, _Value);
			}
			if (has_Desc()){
				output.writeBytes(3, _Desc);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x0000000A:{
						set_Token(input.readString());
						break;
					}
					case 0x00000012:{
						set_Value(input.readString());
						break;
					}
					case 0x0000001A:{
						set_Desc(input.readBytes());
						break;
					}
					default:{
						System.out.println(String.format("[TimeCollection.BonusT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class ExtraTimeSectionT implements ProtoMessage{
		public static ExtraTimeSectionT newInstance(){
			return new ExtraTimeSectionT();
		}
		private java.util.LinkedList<TimeCollection.ExtraTimeSectionT.ExtraTimeT> _ExtraTime;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private ExtraTimeSectionT(){
		}
		public java.util.LinkedList<TimeCollection.ExtraTimeSectionT.ExtraTimeT> get_ExtraTime(){
			return _ExtraTime;
		}
		public TimeCollection.ExtraTimeSectionT.ExtraTimeT get_ExtraTime(int id){
			if (_ExtraTime == null) {
				return null;
			}
			for (TimeCollection.ExtraTimeSectionT.ExtraTimeT val : _ExtraTime) {
				if (val.get_ID() == id) {
					return val;
				}
			}
			return null;
		}
		public void add_ExtraTime(TimeCollection.ExtraTimeSectionT.ExtraTimeT val){
			if(!has_ExtraTime()){
				_ExtraTime = new java.util.LinkedList<TimeCollection.ExtraTimeSectionT.ExtraTimeT>();
				_bit |= 0x1;
			}
			_ExtraTime.add(val);
		}
		public boolean has_ExtraTime(){
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
			if (has_ExtraTime()){
				for(TimeCollection.ExtraTimeSectionT.ExtraTimeT val : _ExtraTime){
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
			if (has_ExtraTime()){
				for(TimeCollection.ExtraTimeSectionT.ExtraTimeT val : _ExtraTime){
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
			if (has_ExtraTime()){
				for (TimeCollection.ExtraTimeSectionT.ExtraTimeT val : _ExtraTime){
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
						add_ExtraTime((TimeCollection.ExtraTimeSectionT.ExtraTimeT)input.readMessage(TimeCollection.ExtraTimeSectionT.ExtraTimeT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[TimeCollection.ExtraTimeSectionT] NEW_TAG : TAG(%d)", tag));
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
		
		public static class ExtraTimeT implements ProtoMessage{
			public static ExtraTimeT newInstance(){
				return new ExtraTimeT();
			}
			
			public String get_ExtraTimeT_toString(){
				StringBuilder sb = new StringBuilder();
				sb.append("ID: ").append(_ID).append(", ExtraTime: ").append(_ExtraTime);
				
				java.util.LinkedList<TimeCollection.ExtraTimeSectionT.ExtraTimeT.ExtraTimeLevelT> EnchantLevels = _EnchantLevel;
				if (EnchantLevels != null && !EnchantLevels.isEmpty()) {
					for (TimeCollection.ExtraTimeSectionT.ExtraTimeT.ExtraTimeLevelT EnchantLevelT : EnchantLevels) {
						sb.append("\r\nExtraTimeLevelT: TotalEnchant = ").append(EnchantLevelT._TotalEnchant);
						sb.append(" & Limit = ").append(EnchantLevelT._Limit);
						sb.append(" & Cost = ").append(EnchantLevelT._Cost);
					}
				}
				return sb.toString();
			}
			
			private int _ID;
			private String _ExtraTime;
			private java.util.LinkedList<TimeCollection.ExtraTimeSectionT.ExtraTimeT.ExtraTimeLevelT> _EnchantLevel;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private ExtraTimeT(){
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
			public String get_ExtraTime(){
				return _ExtraTime;
			}
			public void set_ExtraTime(String val){
				_bit |= 0x2;
				_ExtraTime = val;
			}
			public boolean has_ExtraTime(){
				return (_bit & 0x2) == 0x2;
			}
			public java.util.LinkedList<TimeCollection.ExtraTimeSectionT.ExtraTimeT.ExtraTimeLevelT> get_EnchantLevel(){
				return _EnchantLevel;
			}
			public TimeCollection.ExtraTimeSectionT.ExtraTimeT.ExtraTimeLevelT get_EnchantLevel(int totalEnchant){
				if (_EnchantLevel == null) {
					return null;
				}
				for (TimeCollection.ExtraTimeSectionT.ExtraTimeT.ExtraTimeLevelT val : _EnchantLevel) {
					if (val.get_TotalEnchant() == totalEnchant) {
						return val;
					}
				}
				return null;
			}
			public void add_EnchantLevel(TimeCollection.ExtraTimeSectionT.ExtraTimeT.ExtraTimeLevelT val){
				if(!has_EnchantLevel()){
					_EnchantLevel = new java.util.LinkedList<TimeCollection.ExtraTimeSectionT.ExtraTimeT.ExtraTimeLevelT>();
					_bit |= 0x4;
				}
				_EnchantLevel.add(val);
			}
			public boolean has_EnchantLevel(){
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
				if (has_ID()){
					size += ProtoOutputStream.computeInt32Size(1, _ID);
				}
				if (has_ExtraTime()){
					size += ProtoOutputStream.computeStringSize(2, _ExtraTime);
				}
				if (has_EnchantLevel()){
					for(TimeCollection.ExtraTimeSectionT.ExtraTimeT.ExtraTimeLevelT val : _EnchantLevel){
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
				if (!has_ID()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_ExtraTime()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (has_EnchantLevel()){
					for(TimeCollection.ExtraTimeSectionT.ExtraTimeT.ExtraTimeLevelT val : _EnchantLevel){
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
				if (has_ExtraTime()){
					output.writeString(2, _ExtraTime);
				}
				if (has_EnchantLevel()){
					for (TimeCollection.ExtraTimeSectionT.ExtraTimeT.ExtraTimeLevelT val : _EnchantLevel){
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
							set_ID(input.readInt32());
							break;
						}
						case 0x00000012:{
							set_ExtraTime(input.readString());
							break;
						}
						case 0x0000001A:{
							add_EnchantLevel((TimeCollection.ExtraTimeSectionT.ExtraTimeT.ExtraTimeLevelT)input.readMessage(TimeCollection.ExtraTimeSectionT.ExtraTimeT.ExtraTimeLevelT.newInstance()));
							break;
						}
						default:{
							System.out.println(String.format("[TimeCollection.ExtraTimeSectionT.ExtraTimeT] NEW_TAG : TAG(%d)", tag));
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
			
			public static class ExtraTimeLevelT implements ProtoMessage{
				public static ExtraTimeLevelT newInstance(){
					return new ExtraTimeLevelT();
				}
				private int _TotalEnchant;
				private int _Limit;
				private int _Cost;
				private int _memorizedSerializedSize = -1;
				private byte _memorizedIsInitialized = -1;
				private int _bit;
				private ExtraTimeLevelT(){
				}
				public int get_TotalEnchant(){
					return _TotalEnchant;
				}
				public void set_TotalEnchant(int val){
					_bit |= 0x1;
					_TotalEnchant = val;
				}
				public boolean has_TotalEnchant(){
					return (_bit & 0x1) == 0x1;
				}
				public int get_Limit(){
					return _Limit;
				}
				public void set_Limit(int val){
					_bit |= 0x2;
					_Limit = val;
				}
				public boolean has_Limit(){
					return (_bit & 0x2) == 0x2;
				}
				public int get_Cost(){
					return _Cost;
				}
				public void set_Cost(int val){
					_bit |= 0x4;
					_Cost = val;
				}
				public boolean has_Cost(){
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
					if (has_TotalEnchant()){
						size += ProtoOutputStream.computeInt32Size(1, _TotalEnchant);
					}
					if (has_Limit()){
						size += ProtoOutputStream.computeInt32Size(2, _Limit);
					}
					if (has_Cost()){
						size += ProtoOutputStream.computeInt32Size(3, _Cost);
					}
					_memorizedSerializedSize = size;
					return size;
				}
				@Override
				public boolean isInitialized(){
					if(_memorizedIsInitialized == 1)
						return true;
					if (!has_TotalEnchant()){
						_memorizedIsInitialized = -1;
						return false;
					}
					if (!has_Limit()){
						_memorizedIsInitialized = -1;
						return false;
					}
					if (!has_Cost()){
						_memorizedIsInitialized = -1;
						return false;
					}
					_memorizedIsInitialized = 1;
					return true;
				}
				@Override
				public void writeTo(ProtoOutputStream output) throws java.io.IOException{
					if (has_TotalEnchant()){
						output.wirteInt32(1, _TotalEnchant);
					}
					if (has_Limit()){
						output.wirteInt32(2, _Limit);
					}
					if (has_Cost()){
						output.wirteInt32(3, _Cost);
					}
				}
				@Override
				public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
					while(!input.isAtEnd()){
						int tag = input.readTag();
						switch(tag){
							case 0x00000008:{
								set_TotalEnchant(input.readInt32());
								break;
							}
							case 0x00000010:{
								set_Limit(input.readInt32());
								break;
							}
							case 0x00000018:{
								set_Cost(input.readInt32());
								break;
							}
							default:{
								System.out.println(String.format("[TimeCollection.ExtraTimeSectionT.ExtraTimeT.ExtraTimeLevelT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class GroupT implements ProtoMessage{
		public static GroupT newInstance(){
			return new GroupT();
		}
		private int _ID;
		private int _Desc;
		private TimeCollection.GroupT.LevelT _Level;
		private TimeCollection.GroupT.PeriodT _Period;
		private java.util.LinkedList<TimeCollection.GroupT.SetT> _Set;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private GroupT(){
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
		public int get_Desc(){
			return _Desc;
		}
		public void set_Desc(int val){
			_bit |= 0x2;
			_Desc = val;
		}
		public boolean has_Desc(){
			return (_bit & 0x2) == 0x2;
		}
		public TimeCollection.GroupT.LevelT get_Level(){
			return _Level;
		}
		public void set_Level(TimeCollection.GroupT.LevelT val){
			_bit |= 0x4;
			_Level = val;
		}
		public boolean has_Level(){
			return (_bit & 0x4) == 0x4;
		}
		public TimeCollection.GroupT.PeriodT get_Period(){
			return _Period;
		}
		public void set_Period(TimeCollection.GroupT.PeriodT val){
			_bit |= 0x8;
			_Period = val;
		}
		public boolean has_Period(){
			return (_bit & 0x8) == 0x8;
		}
		public java.util.LinkedList<TimeCollection.GroupT.SetT> get_Set(){
			return _Set;
		}
		public void add_Set(TimeCollection.GroupT.SetT val){
			if(!has_Set()){
				_Set = new java.util.LinkedList<TimeCollection.GroupT.SetT>();
				_bit |= 0x10;
			}
			_Set.add(val);
		}

		public boolean has_Set(){
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
			if (has_ID()){
				size += ProtoOutputStream.computeInt32Size(1, _ID);
			}
			if (has_Desc()){
				size += ProtoOutputStream.computeInt32Size(2, _Desc);
			}
			if (has_Level()){
				size += ProtoOutputStream.computeMessageSize(3, _Level);
			}
			if (has_Period()){
				size += ProtoOutputStream.computeMessageSize(4, _Period);
			}
			if (has_Set()){
				for(TimeCollection.GroupT.SetT val : _Set){
					size += ProtoOutputStream.computeMessageSize(5, val);
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
			if (!has_Desc()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_Level()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_Period()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (has_Set()){
				for(TimeCollection.GroupT.SetT val : _Set){
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
			if (has_Desc()){
				output.wirteInt32(2, _Desc);
			}
			if (has_Level()){
				output.writeMessage(3, _Level);
			}
			if (has_Period()){
				output.writeMessage(4, _Period);
			}
			if (has_Set()){
				for (TimeCollection.GroupT.SetT val : _Set){
					output.writeMessage(5, val);
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
						set_Desc(input.readInt32());
						break;
					}
					case 0x0000001A:{
						set_Level((TimeCollection.GroupT.LevelT)input.readMessage(TimeCollection.GroupT.LevelT.newInstance()));
						break;
					}
					case 0x00000022:{
						set_Period((TimeCollection.GroupT.PeriodT)input.readMessage(TimeCollection.GroupT.PeriodT.newInstance()));
						break;
					}
					case 0x0000002A:{
						add_Set((TimeCollection.GroupT.SetT)input.readMessage(TimeCollection.GroupT.SetT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[TimeCollection.GroupT] NEW_TAG : TAG(%d)", tag));
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
		
		public static class LevelT implements ProtoMessage{
			public static LevelT newInstance(){
				return new LevelT();
			}
			private int _LevelMin;
			private int _LevelMax;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private LevelT(){
			}
			public int get_LevelMin(){
				return _LevelMin;
			}
			public void set_LevelMin(int val){
				_bit |= 0x1;
				_LevelMin = val;
			}
			public boolean has_LevelMin(){
				return (_bit & 0x1) == 0x1;
			}
			public int get_LevelMax(){
				return _LevelMax;
			}
			public void set_LevelMax(int val){
				_bit |= 0x2;
				_LevelMax = val;
			}
			public boolean has_LevelMax(){
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
				if (has_LevelMin()){
					size += ProtoOutputStream.computeInt32Size(1, _LevelMin);
				}
				if (has_LevelMax()){
					size += ProtoOutputStream.computeInt32Size(2, _LevelMax);
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_LevelMin()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_LevelMax()){
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(ProtoOutputStream output) throws java.io.IOException{
				if (has_LevelMin()){
					output.wirteInt32(1, _LevelMin);
				}
				if (has_LevelMax()){
					output.wirteInt32(2, _LevelMax);
				}
			}
			@Override
			public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
				while(!input.isAtEnd()){
					int tag = input.readTag();
					switch(tag){
						case 0x00000008:{
							set_LevelMin(input.readInt32());
							break;
						}
						case 0x00000010:{
							set_LevelMax(input.readInt32());
							break;
						}
						default:{
							System.out.println(String.format("[TimeCollection.LevelT] NEW_TAG : TAG(%d)", tag));
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
		
		public static class PeriodT implements ProtoMessage{
			public static PeriodT newInstance(){
				return new PeriodT();
			}
			private String _StartDate;
			private String _EndDate;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private PeriodT(){
			}
			public String get_StartDate(){
				return _StartDate;
			}
			public void set_StartDate(String val){
				_bit |= 0x1;
				_StartDate = val;
			}
			public boolean has_StartDate(){
				return (_bit & 0x1) == 0x1;
			}
			public String get_EndDate(){
				return _EndDate;
			}
			public void set_EndDate(String val){
				_bit |= 0x2;
				_EndDate = val;
			}
			public boolean has_EndDate(){
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
				if (has_StartDate()){
					size += ProtoOutputStream.computeStringSize(1, _StartDate);
				}
				if (has_EndDate()){
					size += ProtoOutputStream.computeStringSize(2, _EndDate);
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_StartDate()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_EndDate()){
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(ProtoOutputStream output) throws java.io.IOException{
				if (has_StartDate()){
					output.writeString(1, _StartDate);
				}
				if (has_EndDate()){
					output.writeString(2, _EndDate);
				}
			}
			@Override
			public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
				while(!input.isAtEnd()){
					int tag = input.readTag();
					switch(tag){
						case 0x0000000A:{
							set_StartDate(input.readString());
							break;
						}
						case 0x00000012:{
							set_EndDate(input.readString());
							break;
						}
						default:{
							System.out.println(String.format("[TimeCollection.PeriodT] NEW_TAG : TAG(%d)", tag));
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
		
		public static class SetT implements ProtoMessage{
			public static SetT newInstance(){
				return new SetT();
			}
			private int _ID;
			private int _Desc;
			private String _DefaultTime;
			private int _Recycle;
			private java.util.LinkedList<TimeCollection.GroupT.SetT.ItemSlotT> _ItemSlot;
			private java.util.LinkedList<TimeCollection.GroupT.SetT.BuffTypeT> _BuffType;
			private boolean _EndBonus;
			private int _ExtraTimeId;
			private TimeCollectionSetType _SetType;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private SetT(){
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
			public int get_Desc(){
				return _Desc;
			}
			public void set_Desc(int val){
				_bit |= 0x2;
				_Desc = val;
			}
			public boolean has_Desc(){
				return (_bit & 0x2) == 0x2;
			}
			public String get_DefaultTime(){
				return _DefaultTime;
			}
			public void set_DefaultTime(String val){
				_bit |= 0x4;
				_DefaultTime = val;
			}
			public boolean has_DefaultTime(){
				return (_bit & 0x4) == 0x4;
			}
			public int get_Recycle(){
				return _Recycle;
			}
			public void set_Recycle(int val){
				_bit |= 0x8;
				_Recycle = val;
			}
			public boolean has_Recycle(){
				return (_bit & 0x8) == 0x8;
			}
			public java.util.LinkedList<TimeCollection.GroupT.SetT.ItemSlotT> get_ItemSlot(){
				return _ItemSlot;
			}
			public void add_ItemSlot(TimeCollection.GroupT.SetT.ItemSlotT val){
				if(!has_ItemSlot()){
					_ItemSlot = new java.util.LinkedList<TimeCollection.GroupT.SetT.ItemSlotT>();
					_bit |= 0x10;
				}
				_ItemSlot.add(val);
			}
			public String get_ItemSlot_toString() {
				if (_ItemSlot == null || _ItemSlot.isEmpty()) {
					return null;
				}
				StringBuilder sb = new StringBuilder();
				for (TimeCollection.GroupT.SetT.ItemSlotT slot : _ItemSlot) {
					if (sb.length() > 0) {
						sb.append(StringUtil.LineString);
					}
					sb.append("====================\r\nSLOT: ").append(slot._Slot);
					if (slot._NameId != null && !slot._NameId.isEmpty()) {
						for (int id : slot._NameId) {
							CommonItemInfo item = ItemCommonBinLoader.getCommonInfo(id);
							sb.append("\r\nNAME_ID: ").append(id).append(" DESC_KR: ").append(item == null ? null : DescKLoader.getDesc(item.get_desc()));
						}
					}
					sb.append("\r\nBLESS: ").append(slot._Bless);
					sb.append("\r\nENCHANT_ID: ").append(slot._EnchantID);
					sb.append("\r\nCOUNT: ").append(slot._Count);
				}
				return sb.toString();
			}
			public boolean has_ItemSlot(){
				return (_bit & 0x10) == 0x10;
			}
			public java.util.LinkedList<TimeCollection.GroupT.SetT.BuffTypeT> get_BuffType(){
				return _BuffType;
			}
			public TimeCollection.GroupT.SetT.BuffTypeT get_BuffType(int type) {
				for (TimeCollection.GroupT.SetT.BuffTypeT val : _BuffType) {
					if (val.get_Type() == type) {
						return val;
					}
				}
				return null;
			}
			public void add_BuffType(TimeCollection.GroupT.SetT.BuffTypeT val){
				if(!has_BuffType()){
					_BuffType = new java.util.LinkedList<TimeCollection.GroupT.SetT.BuffTypeT>();
					_bit |= 0x20;
				}
				_BuffType.add(val);
			}
			public String get_BuffType_toString() {
				if (_ItemSlot == null || _ItemSlot.isEmpty()) {
					return null;
				}
				StringBuilder sb = new StringBuilder();
				for (TimeCollection.GroupT.SetT.BuffTypeT buff : _BuffType) {
					if (sb.length() > 0) {
						sb.append(StringUtil.LineString);
					}
					sb.append("====================\r\nTYPE: ").append(buff._Type);
					if (buff._SetBuff != null) {
						if (buff._SetBuff.get_Bonus() != null && !buff._SetBuff.get_Bonus().isEmpty()) {
							for (TimeCollection.BonusT bonus : buff._SetBuff.get_Bonus()) {
								sb.append("\r\nSET_BUFF: TOKEN = ").append(bonus._Token).append(" & VALUE = ").append(bonus._Value);
								sb.append(" & DESC = ");
								int descCnt = 0;
								for (byte desc : bonus._Desc) {
									if (descCnt++ > 0) {
										sb.append(", ");
									}
									sb.append(desc & 0xFF);
								}
							}
						}
					}
					
					if (buff._EnchantBuffList != null) {
						sb.append("\r\nENCHANT_BUFF ===> ");
						if (buff._EnchantBuffList._EnchantBuff != null && !buff._EnchantBuffList._EnchantBuff.isEmpty()) {
							for (TimeCollection.GroupT.SetT.BuffTypeT.EnchantBuffListT.EnchantBuffT buffs : buff._EnchantBuffList._EnchantBuff) {
								sb.append("\r\nTOTAL_ENCHANT: ").append(buffs._TotalEnchant);
								if (buffs._Bonus != null && !buffs._Bonus.isEmpty()) {
									for (TimeCollection.BonusT bonus : buffs._Bonus) {
										sb.append(", TOTAL_ENCHANT_BONUS: TOKEN = ").append(bonus._Token).append(" & VALUE = ").append(bonus._Value);
										sb.append(" & DESC = ");
										int descCnt = 0;
										for (byte desc : bonus._Desc) {
											if (descCnt++ > 0) {
												sb.append(", ");
											}
											sb.append(desc & 0xFF);
										}
									}
								}
							}
						}
					}
				}
				return sb.toString();
			}
			public boolean has_BuffType(){
				return (_bit & 0x20) == 0x20;
			}
			public boolean get_EndBonus(){
				return _EndBonus;
			}
			public void set_EndBonus(boolean val){
				_bit |= 0x40;
				_EndBonus = val;
			}
			public boolean has_EndBonus(){
				return (_bit & 0x40) == 0x40;
			}
			public int get_ExtraTimeId(){
				return _ExtraTimeId;
			}
			public void set_ExtraTimeId(int val){
				_bit |= 0x80;
				_ExtraTimeId = val;
			}
			public boolean has_ExtraTimeId(){
				return (_bit & 0x80) == 0x80;
			}
			public TimeCollectionSetType get_SetType(){
				return _SetType;
			}
			public void set_SetType(TimeCollectionSetType val){
				_bit |= 0x100;
				_SetType = val;
			}
			public boolean has_SetType(){
				return (_bit & 0x100) == 0x100;
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
				if (has_Desc()){
					size += ProtoOutputStream.computeInt32Size(2, _Desc);
				}
				if (has_DefaultTime()){
					size += ProtoOutputStream.computeStringSize(3, _DefaultTime);
				}
				if (has_Recycle()){
					size += ProtoOutputStream.computeInt32Size(4, _Recycle);
				}
				if (has_ItemSlot()){
					for(TimeCollection.GroupT.SetT.ItemSlotT val : _ItemSlot){
						size += ProtoOutputStream.computeMessageSize(5, val);
					}
				}
				if (has_BuffType()){
					for(TimeCollection.GroupT.SetT.BuffTypeT val : _BuffType){
						size += ProtoOutputStream.computeMessageSize(6, val);
					}
				}
				if (has_EndBonus()){
					size += ProtoOutputStream.computeBoolSize(7, _EndBonus);
				}
				if (has_ExtraTimeId()){
					size += ProtoOutputStream.computeInt32Size(8, _ExtraTimeId);
				}
				if (has_SetType()){
					size += ProtoOutputStream.computeEnumSize(9, _SetType.toInt());
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
				if (!has_Desc()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_DefaultTime()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_Recycle()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (has_ItemSlot()){
					for(TimeCollection.GroupT.SetT.ItemSlotT val : _ItemSlot){
						if (!val.isInitialized()){
							_memorizedIsInitialized = -1;
							return false;
						}
					}
				}
				if (has_BuffType()){
					for(TimeCollection.GroupT.SetT.BuffTypeT val : _BuffType){
						if (!val.isInitialized()){
							_memorizedIsInitialized = -1;
							return false;
						}
					}
				}
				if (!has_EndBonus()){
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(ProtoOutputStream output) throws java.io.IOException{
				if (has_ID()){
					output.wirteInt32(1, _ID);
				}
				if (has_Desc()){
					output.wirteInt32(2, _Desc);
				}
				if (has_DefaultTime()){
					output.writeString(3, _DefaultTime);
				}
				if (has_Recycle()){
					output.wirteInt32(4, _Recycle);
				}
				if (has_ItemSlot()){
					for (TimeCollection.GroupT.SetT.ItemSlotT val : _ItemSlot){
						output.writeMessage(5, val);
					}
				}
				if (has_BuffType()){
					for (TimeCollection.GroupT.SetT.BuffTypeT val : _BuffType){
						output.writeMessage(6, val);
					}
				}
				if (has_EndBonus()){
					output.writeBool(7, _EndBonus);
				}
				if (has_ExtraTimeId()){
					output.wirteInt32(8, _ExtraTimeId);
				}
				if (has_SetType()){
					output.writeEnum(9, _SetType.toInt());
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
							set_Desc(input.readInt32());
							break;
						}
						case 0x0000001A:{
							set_DefaultTime(input.readString());
							break;
						}
						case 0x00000020:{
							set_Recycle(input.readInt32());
							break;
						}
						case 0x0000002A:{
							add_ItemSlot((TimeCollection.GroupT.SetT.ItemSlotT)input.readMessage(TimeCollection.GroupT.SetT.ItemSlotT.newInstance()));
							break;
						}
						case 0x00000032:{
							add_BuffType((TimeCollection.GroupT.SetT.BuffTypeT)input.readMessage(TimeCollection.GroupT.SetT.BuffTypeT.newInstance()));
							break;
						}
						case 0x00000038:{
							set_EndBonus(input.readBool());
							break;
						}
						case 0x00000040:{
							set_ExtraTimeId(input.readInt32());
							break;
						}
						case 0x00000048:{
							set_SetType(TimeCollectionSetType.fromInt(input.readEnum()));
							break;
						}
						default:{
							System.out.println(String.format("[TimeCollection.SetT] NEW_TAG : TAG(%d)", tag));
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
			
			public static class ItemSlotT implements ProtoMessage{
				public static ItemSlotT newInstance(){
					return new ItemSlotT();
				}
				private int _Slot;
				private java.util.LinkedList<Integer> _NameId;
				private int _Bless;
				private int _EnchantID;
				private int _Count;
				private java.util.LinkedList<byte[]> _extra_desc;
				private int _memorizedSerializedSize = -1;
				private byte _memorizedIsInitialized = -1;
				private int _bit;
				private ItemSlotT(){
				}
				public int get_Slot(){
					return _Slot;
				}
				public void set_Slot(int val){
					_bit |= 0x1;
					_Slot = val;
				}
				public boolean has_Slot(){
					return (_bit & 0x1) == 0x1;
				}
				public java.util.LinkedList<Integer> get_NameId(){
					return _NameId;
				}
				public void add_NameId(int val){
					if(!has_NameId()){
						_NameId = new java.util.LinkedList<Integer>();
						_bit |= 0x2;
					}
					_NameId.add(val);
				}
				public boolean has_NameId(){
					return (_bit & 0x2) == 0x2;
				}
				public int get_Bless(){
					return _Bless;
				}
				public void set_Bless(int val){
					_bit |= 0x4;
					_Bless = val;
				}
				public boolean has_Bless(){
					return (_bit & 0x4) == 0x4;
				}
				public int get_EnchantID(){
					return _EnchantID;
				}
				public void set_EnchantID(int val){
					_bit |= 0x8;
					_EnchantID = val;
				}
				public boolean has_EnchantID(){
					return (_bit & 0x8) == 0x8;
				}
				public int get_Count(){
					return _Count;
				}
				public void set_Count(int val){
					_bit |= 0x10;
					_Count = val;
				}
				public boolean has_Count(){
					return (_bit & 0x10) == 0x10;
				}
				public java.util.LinkedList<byte[]> get_extra_desc(){
					return _extra_desc;
				}
				public void add_extra_desc(byte[] val){
					if(!has_extra_desc()){
						_extra_desc = new java.util.LinkedList<byte[]>();
						_bit |= 0x20;
					}
					_extra_desc.add(val);
				}
				public boolean has_extra_desc(){
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
					if (has_Slot()){
						size += ProtoOutputStream.computeInt32Size(1, _Slot);
					}
					if (has_NameId()){
						for(int val : _NameId){
							size += ProtoOutputStream.computeInt32Size(2, val);
						}
					}
					if (has_Bless()){
						size += ProtoOutputStream.computeInt32Size(3, _Bless);
					}
					if (has_EnchantID()){
						size += ProtoOutputStream.computeInt32Size(4, _EnchantID);
					}
					if (has_Count()){
						size += ProtoOutputStream.computeInt32Size(5, _Count);
					}
					if (has_extra_desc()){
						for(byte[] val : _extra_desc){
							size += ProtoOutputStream.computeBytesSize(6, val);
						}
					}
					_memorizedSerializedSize = size;
					return size;
				}
				@Override
				public boolean isInitialized(){
					if(_memorizedIsInitialized == 1)
						return true;
					if (!has_Slot()){
						_memorizedIsInitialized = -1;
						return false;
					}
					if (has_NameId()){
						/*for(int val : _NameId){
							if (!val.isInitialized()){
								_memorizedIsInitialized = -1;
								return false;
							}
						}*/
					}
					if (!has_Bless()){
						_memorizedIsInitialized = -1;
						return false;
					}
					if (has_extra_desc()){
						for(byte[] val : _extra_desc){
							if (val == null || val.length <= 0){
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
					if (has_Slot()){
						output.wirteInt32(1, _Slot);
					}
					if (has_NameId()){
						for (int val : _NameId){
							output.wirteInt32(2, val);
						}
					}
					if (has_Bless()){
						output.wirteInt32(3, _Bless);
					}
					if (has_EnchantID()){
						output.wirteInt32(4, _EnchantID);
					}
					if (has_Count()){
						output.wirteInt32(5, _Count);
					}
					if (has_extra_desc()){
						for (byte[] val : _extra_desc){
							output.writeBytes(6, val);
						}
					}
				}
				@Override
				public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
					while(!input.isAtEnd()){
						int tag = input.readTag();
						switch(tag){
							case 0x00000008:{
								set_Slot(input.readInt32());
								break;
							}
							case 0x00000010:{
								add_NameId(input.readInt32());
								break;
							}
							case 0x00000018:{
								set_Bless(input.readInt32());
								break;
							}
							case 0x00000020:{
								set_EnchantID(input.readInt32());
								break;
							}
							case 0x00000028:{
								set_Count(input.readInt32());
								break;
							}
							case 0x00000032:{
								add_extra_desc(input.readBytes());
								break;
							}
							default:{
								System.out.println(String.format("[TimeCollection.ItemSlotT] NEW_TAG : TAG(%d)", tag));
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
			
			public static class BuffTypeT implements ProtoMessage{
				public static BuffTypeT newInstance(){
					return new BuffTypeT();
				}
				
				/**
				 * 세트완성시 인첸트 수치에 해당하는 보너스 옵션 조사
				 * @param totalEnchant
				 * @return java.util.LinkedList<TimeCollection.BonusT>
				 */
				public java.util.LinkedList<TimeCollection.BonusT> get_bonus_list(int totalEnchant) {
					java.util.LinkedList<TimeCollection.BonusT> list = new java.util.LinkedList<TimeCollection.BonusT>();
					list.addAll(_SetBuff.get_Bonus());// 세트완성 보너스
					for (TimeCollection.GroupT.SetT.BuffTypeT.EnchantBuffListT.EnchantBuffT val : _EnchantBuffList.get_EnchantBuff()) {
						if (val.get_TotalEnchant() <= totalEnchant) {
							for (TimeCollection.BonusT bonus : val.get_Bonus()) {
								list.add(bonus);// 인첸트 보너스
							}
						}
					}
					return list;
				}
				
				private int _Type;
				private TimeCollection.GroupT.SetT.BuffTypeT.SetBuffT _SetBuff;
				private TimeCollection.GroupT.SetT.BuffTypeT.EnchantBuffListT _EnchantBuffList;
				private int _memorizedSerializedSize = -1;
				private byte _memorizedIsInitialized = -1;
				private int _bit;
				private BuffTypeT(){
				}
				public int get_Type(){
					return _Type;
				}
				public void set_Type(int val){
					_bit |= 0x1;
					_Type = val;
				}
				public boolean has_Type(){
					return (_bit & 0x1) == 0x1;
				}
				public TimeCollection.GroupT.SetT.BuffTypeT.SetBuffT get_SetBuff(){
					return _SetBuff;
				}
				public void set_SetBuff(TimeCollection.GroupT.SetT.BuffTypeT.SetBuffT val){
					_bit |= 0x2;
					_SetBuff = val;
				}
				public boolean has_SetBuff(){
					return (_bit & 0x2) == 0x2;
				}
				public TimeCollection.GroupT.SetT.BuffTypeT.EnchantBuffListT get_EnchantBuffList(){
					return _EnchantBuffList;
				}
				public void set_EnchantBuffList(TimeCollection.GroupT.SetT.BuffTypeT.EnchantBuffListT val){
					_bit |= 0x4;
					_EnchantBuffList = val;
				}
				public boolean has_EnchantBuffList(){
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
					if (has_Type()){
						size += ProtoOutputStream.computeInt32Size(1, _Type);
					}
					if (has_SetBuff()){
						size += ProtoOutputStream.computeMessageSize(2, _SetBuff);
					}
					if (has_EnchantBuffList()){
						size += ProtoOutputStream.computeMessageSize(3, _EnchantBuffList);
					}
					_memorizedSerializedSize = size;
					return size;
				}
				@Override
				public boolean isInitialized(){
					if(_memorizedIsInitialized == 1)
						return true;
					if (!has_Type()){
						_memorizedIsInitialized = -1;
						return false;
					}
					if (!has_SetBuff()){
						_memorizedIsInitialized = -1;
						return false;
					}
					if (!has_EnchantBuffList()){
						_memorizedIsInitialized = -1;
						return false;
					}
					_memorizedIsInitialized = 1;
					return true;
				}
				@Override
				public void writeTo(ProtoOutputStream output) throws java.io.IOException{
					if (has_Type()){
						output.wirteInt32(1, _Type);
					}
					if (has_SetBuff()){
						output.writeMessage(2, _SetBuff);
					}
					if (has_EnchantBuffList()){
						output.writeMessage(3, _EnchantBuffList);
					}
				}
				@Override
				public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
					while(!input.isAtEnd()){
						int tag = input.readTag();
						switch(tag){
							case 0x00000008:{
								set_Type(input.readInt32());
								break;
							}
							case 0x00000012:{
								set_SetBuff((TimeCollection.GroupT.SetT.BuffTypeT.SetBuffT)input.readMessage(TimeCollection.GroupT.SetT.BuffTypeT.SetBuffT.newInstance()));
								break;
							}
							case 0x0000001A:{
								set_EnchantBuffList((TimeCollection.GroupT.SetT.BuffTypeT.EnchantBuffListT)input.readMessage(TimeCollection.GroupT.SetT.BuffTypeT.EnchantBuffListT.newInstance()));
								break;
							}
							default:{
								System.out.println(String.format("[TimeCollection.BuffTypeT] NEW_TAG : TAG(%d)", tag));
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
				
				public static class SetBuffT implements ProtoMessage{
					public static SetBuffT newInstance(){
						return new SetBuffT();
					}
					private java.util.LinkedList<TimeCollection.BonusT> _Bonus;
					private int _memorizedSerializedSize = -1;
					private byte _memorizedIsInitialized = -1;
					private int _bit;
					private SetBuffT(){
					}
					public java.util.LinkedList<TimeCollection.BonusT> get_Bonus(){
						return _Bonus;
					}
					public void add_Bonus(TimeCollection.BonusT val){
						if(!has_Bonus()){
							_Bonus = new java.util.LinkedList<TimeCollection.BonusT>();
							_bit |= 0x1;
						}
						_Bonus.add(val);
					}
					public boolean has_Bonus(){
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
						if (has_Bonus()){
							for(TimeCollection.BonusT val : _Bonus){
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
						if (has_Bonus()){
							for(TimeCollection.BonusT val : _Bonus){
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
						if (has_Bonus()){
							for (TimeCollection.BonusT val : _Bonus){
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
									add_Bonus((TimeCollection.BonusT)input.readMessage(TimeCollection.BonusT.newInstance()));
									break;
								}
								default:{
									System.out.println(String.format("[TimeCollection.SetBuffT] NEW_TAG : TAG(%d)", tag));
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
				
				public static class EnchantBuffListT implements ProtoMessage{
					public static EnchantBuffListT newInstance(){
						return new EnchantBuffListT();
					}
					private java.util.LinkedList<TimeCollection.GroupT.SetT.BuffTypeT.EnchantBuffListT.EnchantBuffT> _EnchantBuff;
					private int _memorizedSerializedSize = -1;
					private byte _memorizedIsInitialized = -1;
					private int _bit;
					private EnchantBuffListT(){
					}
					public java.util.LinkedList<TimeCollection.GroupT.SetT.BuffTypeT.EnchantBuffListT.EnchantBuffT> get_EnchantBuff(){
						return _EnchantBuff;
					}
					public void add_EnchantBuff(TimeCollection.GroupT.SetT.BuffTypeT.EnchantBuffListT.EnchantBuffT val){
						if(!has_EnchantBuff()){
							_EnchantBuff = new java.util.LinkedList<TimeCollection.GroupT.SetT.BuffTypeT.EnchantBuffListT.EnchantBuffT>();
							_bit |= 0x1;
						}
						_EnchantBuff.add(val);
					}
					public boolean has_EnchantBuff(){
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
						if (has_EnchantBuff()){
							for(TimeCollection.GroupT.SetT.BuffTypeT.EnchantBuffListT.EnchantBuffT val : _EnchantBuff){
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
						if (has_EnchantBuff()){
							for(TimeCollection.GroupT.SetT.BuffTypeT.EnchantBuffListT.EnchantBuffT val : _EnchantBuff){
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
						if (has_EnchantBuff()){
							for (TimeCollection.GroupT.SetT.BuffTypeT.EnchantBuffListT.EnchantBuffT val : _EnchantBuff){
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
									add_EnchantBuff((TimeCollection.GroupT.SetT.BuffTypeT.EnchantBuffListT.EnchantBuffT)input.readMessage(TimeCollection.GroupT.SetT.BuffTypeT.EnchantBuffListT.EnchantBuffT.newInstance()));
									break;
								}
								default:{
									System.out.println(String.format("[TimeCollection.EnchantBuffListT] NEW_TAG : TAG(%d)", tag));
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
					
					public static class EnchantBuffT implements ProtoMessage{
						public static EnchantBuffT newInstance(){
							return new EnchantBuffT();
						}
						private int _TotalEnchant;
						private java.util.LinkedList<TimeCollection.BonusT> _Bonus;
						private int _memorizedSerializedSize = -1;
						private byte _memorizedIsInitialized = -1;
						private int _bit;
						private EnchantBuffT(){
						}
						public int get_TotalEnchant(){
							return _TotalEnchant;
						}
						public void set_TotalEnchant(int val){
							_bit |= 0x1;
							_TotalEnchant = val;
						}
						public boolean has_TotalEnchant(){
							return (_bit & 0x1) == 0x1;
						}
						public java.util.LinkedList<TimeCollection.BonusT> get_Bonus(){
							return _Bonus;
						}
						public void add_Bonus(TimeCollection.BonusT val){
							if(!has_Bonus()){
								_Bonus = new java.util.LinkedList<TimeCollection.BonusT>();
								_bit |= 0x2;
							}
							_Bonus.add(val);
						}
						public boolean has_Bonus(){
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
							if (has_TotalEnchant()){
								size += ProtoOutputStream.computeInt32Size(1, _TotalEnchant);
							}
							if (has_Bonus()){
								for(TimeCollection.BonusT val : _Bonus){
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
							if (!has_TotalEnchant()){
								_memorizedIsInitialized = -1;
								return false;
							}
							if (has_Bonus()){
								for(TimeCollection.BonusT val : _Bonus){
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
							if (has_TotalEnchant()){
								output.wirteInt32(1, _TotalEnchant);
							}
							if (has_Bonus()){
								for (TimeCollection.BonusT val : _Bonus){
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
										set_TotalEnchant(input.readInt32());
										break;
									}
									case 0x00000012:{
										add_Bonus((TimeCollection.BonusT)input.readMessage(TimeCollection.BonusT.newInstance()));
										break;
									}
									default:{
										System.out.println(String.format("[TimeCollection.EnchantBuffT] NEW_TAG : TAG(%d)", tag));
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
	}
	
	public static class AlarmSettingT implements ProtoMessage{
		public static AlarmSettingT newInstance(){
			return new AlarmSettingT();
		}
		private int _AlarmDays;
		private int _AlarmMinute;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private AlarmSettingT(){
		}
		public int get_AlarmDays(){
			return _AlarmDays;
		}
		public void set_AlarmDays(int val){
			_bit |= 0x1;
			_AlarmDays = val;
		}
		public boolean has_AlarmDays(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_AlarmMinute(){
			return _AlarmMinute;
		}
		public void set_AlarmMinute(int val){
			_bit |= 0x2;
			_AlarmMinute = val;
		}
		public boolean has_AlarmMinute(){
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
			if (has_AlarmDays()){
				size += ProtoOutputStream.computeInt32Size(1, _AlarmDays);
			}
			if (has_AlarmMinute()){
				size += ProtoOutputStream.computeInt32Size(2, _AlarmMinute);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_AlarmDays()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_AlarmMinute()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_AlarmDays()){
				output.wirteInt32(1, _AlarmDays);
			}
			if (has_AlarmMinute()){
				output.wirteInt32(2, _AlarmMinute);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_AlarmDays(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_AlarmMinute(input.readInt32());
						break;
					}
					default:{
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

