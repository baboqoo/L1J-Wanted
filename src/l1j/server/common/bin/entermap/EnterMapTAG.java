package l1j.server.common.bin.entermap;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;
import l1j.server.common.data.ConditionsTAG;
import l1j.server.common.data.ItemTAG;
import l1j.server.common.data.SpellBuffTAG;
import l1j.server.common.data.SpellTAG;
import l1j.server.server.utils.CharsetUtil;
import l1j.server.server.utils.StringUtil;

public class EnterMapTAG implements ProtoMessage{
	public static EnterMapTAG newInstance(){
		return new EnterMapTAG();
	}
	private int _ID;
	private java.util.LinkedList<EnterMapTAG.MapDataTAG> _MapData;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private EnterMapTAG(){
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
	public java.util.LinkedList<EnterMapTAG.MapDataTAG> get_MapData(){
		return _MapData;
	}
	public void add_MapData(EnterMapTAG.MapDataTAG val){
		if(!has_MapData()){
			_MapData = new java.util.LinkedList<EnterMapTAG.MapDataTAG>();
			_bit |= 0x2;
		}
		_MapData.add(val);
	}
	public boolean has_MapData(){
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
		if (has_ID()){
			size += ProtoOutputStream.computeInt32Size(1, _ID);
		}
		if (has_MapData()){
			for(EnterMapTAG.MapDataTAG val : _MapData){
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
		if (!has_ID()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_MapData()){
			for(EnterMapTAG.MapDataTAG val : _MapData){
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
		if (has_MapData()){
			for (EnterMapTAG.MapDataTAG val : _MapData){
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
					set_ID(input.readInt32());
					break;
				}
				case 0x00000012:{
					add_MapData((EnterMapTAG.MapDataTAG)input.readMessage(EnterMapTAG.MapDataTAG.newInstance()));
					break;
				}
				default:{
					System.out.println(String.format("[EnterMapTAG] NEW_TAG : TAG(%d)", tag));
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
	
	public static class MapDataTAG implements ProtoMessage{
		public static MapDataTAG newInstance(){
			return new MapDataTAG();
		}
		private String _Action;
		private int _Number;
		private int _X;
		private int _Y;
		private int _Range;
		private EnterMapTAG.PriorityEnum _Priority;
		private int _MaxUser;
		private java.util.LinkedList<ConditionsTAG> _Conditions;
		private java.util.LinkedList<DestinationTAG> _Destinations;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private MapDataTAG(){
		}
		public String get_Action(){
			return _Action;
		}
		public void set_Action(String val){
			_bit |= 0x1;
			_Action = val;
		}
		public boolean has_Action(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_Number(){
			return _Number;
		}
		public void set_Number(int val){
			_bit |= 0x2;
			_Number = val;
		}
		public boolean has_Number(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_X(){
			return _X;
		}
		public void set_X(int val){
			_bit |= 0x4;
			_X = val;
		}
		public boolean has_X(){
			return (_bit & 0x4) == 0x4;
		}
		public int get_Y(){
			return _Y;
		}
		public void set_Y(int val){
			_bit |= 0x8;
			_Y = val;
		}
		public boolean has_Y(){
			return (_bit & 0x8) == 0x8;
		}
		public int get_Range(){
			return _Range;
		}
		public void set_Range(int val){
			_bit |= 0x10;
			_Range = val;
		}
		public boolean has_Range(){
			return (_bit & 0x10) == 0x10;
		}
		public EnterMapTAG.PriorityEnum get_Priority(){
			return _Priority;
		}
		public void set_Priority(EnterMapTAG.PriorityEnum val){
			_bit |= 0x20;
			_Priority = val;
		}
		public boolean has_Priority(){
			return (_bit & 0x20) == 0x20;
		}
		public int get_MaxUser(){
			return _MaxUser;
		}
		public void set_MaxUser(int val){
			_bit |= 0x40;
			_MaxUser = val;
		}
		public boolean has_MaxUser(){
			return (_bit & 0x40) == 0x40;
		}
		public java.util.LinkedList<ConditionsTAG> get_Conditions(){
			return _Conditions;
		}
		public void add_Conditions(ConditionsTAG val){
			if(!has_Conditions()){
				_Conditions = new java.util.LinkedList<ConditionsTAG>();
				_bit |= 0x80;
			}
			_Conditions.add(val);
		}
		public String get_Conditions_toString() {
			if (_Conditions == null || _Conditions.isEmpty()) {
				return null;
			}
			StringBuilder sb = new StringBuilder();
			for (ConditionsTAG tag : _Conditions) {
				if (tag == null) {
					continue;
				}
				if (sb.length() > 0) {
					sb.append(StringUtil.LineString);
				}

				ConditionsTAG.LevelTAG levelTag = tag.get_Level();
				if (levelTag != null) {
					sb.append("LEVEL (");
					sb.append("MIN = ").append(levelTag.get_Minimum());
					sb.append(" & MAX = ").append(levelTag.get_Maximum());
					sb.append(")");
				}
				
				java.util.LinkedList<ConditionsTAG.UserTAG> userTags = tag.get_User();
				if (userTags != null && !userTags.isEmpty()) {
					sb.append(", USER (");
					int userTagCnt = 0;
					for (ConditionsTAG.UserTAG userTag : userTags) {
						if (userTagCnt++ > 0) {
							sb.append(" OR ");
						}
						sb.append("PC_CAFE = ").append(userTag.get_PCCafe());
						
						java.util.LinkedList<ConditionsTAG.VIPTAG> vips = userTag.get_VIP();
						if (vips != null && !vips.isEmpty()) {
							sb.append(" & VIP {");
							int vipCnt = 0;
							for (ConditionsTAG.VIPTAG vipTag : vips) {
								if (vipCnt++ > 0) {
									sb.append(" OR ");
								}
								sb.append("GRADE = ").append(vipTag.get_Grade());
							}
							sb.append("}");
						}
					}
					sb.append(")");
				}
				
				java.util.LinkedList<ItemTAG> itemTags = tag.get_Item();
				if (itemTags != null && !itemTags.isEmpty()) {
					sb.append(", ITEM (");
					int itemTagCnt = 0;
					for (ItemTAG itemTag : itemTags) {
						if (itemTagCnt++ > 0) {
							sb.append(" OR ");
						}
						sb.append("NAME_ID = ").append(itemTag.get_NameID());
						sb.append(" & COUNT = ").append(itemTag.get_Count());
						sb.append(" & DELETE = ").append(itemTag.get_Delete() == null ? 0 : itemTag.get_Delete().toInt());
						sb.append(" & ENCHANT = ").append(itemTag.get_Enchant());
						sb.append(" & RANDOM = ").append(itemTag.get_Random());
						sb.append(" & PROB = ").append(itemTag.get_Prob());
						sb.append(" & IDEN = ").append(itemTag.get_Ident());
					}
					sb.append(")");
				}
				
				java.util.LinkedList<ConditionsTAG.NonExistentItemTAG> nonExistentItems = tag.get_NonExistentItem();
				if (nonExistentItems != null && !nonExistentItems.isEmpty()) {
					sb.append(", NON_EXISTENT_ITEM (");
					int nonCnt = 0;
					for (ConditionsTAG.NonExistentItemTAG nonTag : nonExistentItems) {
						if (nonCnt++ > 0) {
							sb.append(" OR ");
						}
						sb.append("NAME_ID = ").append(nonTag.get_NameID());
						sb.append(" & COUNT = ").append(nonTag.get_Count());
						sb.append(" & BLESS = ").append(nonTag.get_Bless() == null ? 1 : nonTag.get_Bless().toInt());
						sb.append(" & ENCHANT = ").append(nonTag.get_Enchant());
					}
					sb.append(")");
				}
				
				ConditionsTAG.QuestTAG quest = tag.get_Quest();
				if (quest != null) {
					sb.append(", QUEST (");
					int questCnt = 0;
					for (int progressId : quest.get_ProgressQuestIDs()) {
						if (questCnt++ > 0) {
							sb.append(" OR ");
						}
						
						sb.append("PROGRESS_ID = ").append(progressId);
					}
					sb.append(")");
				}
				
				
				java.util.LinkedList<SpellBuffTAG> spellBuffs = tag.get_SpellBuff();
				if (spellBuffs != null) {
					sb.append(", SPELL_BUFF (");
					int spellBuffCnt = 0;
					for (SpellBuffTAG spellBuff : spellBuffs) {
						if (spellBuffCnt++ > 0) {
							sb.append(" OR ");
						}
						sb.append("ID = ").append(spellBuff.get_ID());
						sb.append(" & EFFECT = ").append(spellBuff.get_Effect());
						sb.append(" & FAIL_HTML = ").append(spellBuff.get_FailHtml() == null ? "" : new String(spellBuff.get_FailHtml(), CharsetUtil.MS_949));
					}
					sb.append(")");
				}
				
				SpellTAG spell = tag.get_Spell();
				if (spell != null) {
					sb.append(", SPELL (");
					sb.append("ID = ").append(spell.get_ID());
					sb.append(" & EFFECT = ").append(spell.get_Effect());
					sb.append(" & FAIL_HTML = ").append(spell.get_FailHtml() == null ? "" : new String(spell.get_FailHtml(), CharsetUtil.MS_949));
					sb.append(")");
				}
				
				java.util.LinkedList<ConditionsTAG.ItemListTAG> itemList = tag.get_ItemList();
				if (itemList != null && !itemList.isEmpty()) {
					sb.append(", ITEM_LIST (");
					int itemListCnt = 0;
					for (ConditionsTAG.ItemListTAG itemT : itemList) {
						if (itemListCnt++ > 0) {
							sb.append(" OR ");
						}
						java.util.LinkedList<Integer> nameIDs = itemT.get_NameIDs();
						if (nameIDs != null && !nameIDs.isEmpty()) {
							sb.append("NAME_IDS [");
							int nameIdCnt = 0;
							for (int nameId : nameIDs) {
								if (nameIdCnt++ > 0) {
									sb.append(" OR ");
								}
								sb.append(nameId);
							}
							sb.append("]");
						}

						sb.append(" & COUNT = ").append(itemT.get_Count());
						if (itemT.get_Connective() != null) {
							sb.append(" & CONNECTIVE = ").append(itemT.get_Connective().name());
						}
					}
					sb.append(")");
				}
				
				java.util.LinkedList<ConditionsTAG.MultiTypeItemTAG> multiTypeItems = tag.get_MultiTypeItem();
				if (multiTypeItems != null && !multiTypeItems.isEmpty()) {
					sb.append(", MULTI_TYPE_ITEM (");
					int multiCnt = 0;
					for (ConditionsTAG.MultiTypeItemTAG multiTypeItem : multiTypeItems) {
						if (multiCnt++ > 0) {
							sb.append(" OR ");
						}
						
						if (multiTypeItem.get_TypeID() != null) {
							sb.append("TYPE_ID = ").append(multiTypeItem.get_TypeID().name());
						}
						sb.append(" & NAME_ID = ").append(multiTypeItem.get_NameID());
						sb.append(" & COUNT = ").append(multiTypeItem.get_Count());
						if (multiTypeItem.get_Delete() != null) {
							sb.append(" & DELETE = ").append(multiTypeItem.get_Delete().name());
						}
					}
					sb.append(")");
				}
				
				ConditionsTAG.AlignTAG align = tag.get_Align();
				if (align != null) {
					sb.append(", ALIGN (");
					sb.append("LAWFUL = ").append(align.get_Lawful());
					sb.append(" & NEUTRAL = ").append(align.get_Neutral());
					sb.append(" & CHAOTIC = ").append(align.get_Chaotic());
					sb.append(")");
				}
			}
			return sb.toString();
		}
		public boolean has_Conditions(){
			return (_bit & 0x80) == 0x80;
		}
		
		public java.util.LinkedList<DestinationTAG> get_Destinations(){
			return _Destinations;
		}
		public void add_Destinations(DestinationTAG val){
			if(!has_Destinations()){
				_Destinations = new java.util.LinkedList<DestinationTAG>();
				_bit |= 0x100;
			}
			_Destinations.add(val);
		}
		public String get_Destinations_toString() {
			if (_Destinations == null || _Destinations.isEmpty()) {
				return null;
			}
			StringBuilder sb = new StringBuilder();
			for (DestinationTAG tag : _Destinations) {
				if (tag == null) {
					continue;
				}
				if (sb.length() > 0) {
					sb.append(StringUtil.LineString);
				}
				sb.append("NUMBER = ").append(tag.get_Number());
				sb.append(" & X = ").append(tag.get_X());
				sb.append(" & Y = ").append(tag.get_Y());
				sb.append(" & RANGE = ").append(tag.get_Range());
				sb.append(" & PROB = ").append(tag.get_Prob());
			}
			return sb.toString();
		}
		public boolean has_Destinations(){
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
			if (has_Action()){
				size += ProtoOutputStream.computeStringSize(1, _Action);
			}
			if (has_Number()){
				size += ProtoOutputStream.computeInt32Size(2, _Number);
			}
			if (has_X()){
				size += ProtoOutputStream.computeInt32Size(3, _X);
			}
			if (has_Y()){
				size += ProtoOutputStream.computeInt32Size(4, _Y);
			}
			if (has_Range()){
				size += ProtoOutputStream.computeInt32Size(5, _Range);
			}
			if (has_Priority()){
				size += ProtoOutputStream.computeEnumSize(6, _Priority.toInt());
			}
			if (has_MaxUser()){
				size += ProtoOutputStream.computeInt32Size(7, _MaxUser);
			}
			if (has_Conditions()){
				for(ConditionsTAG val : _Conditions){
					size += ProtoOutputStream.computeMessageSize(8, val);
				}
			}
			if (has_Destinations()){
				for(EnterMapTAG.MapDataTAG.DestinationTAG val : _Destinations){
					size += ProtoOutputStream.computeMessageSize(9, val);
				}
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_Action()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_Number()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_X()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_Y()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (has_Conditions()){
				for(ConditionsTAG val : _Conditions){
					if (!val.isInitialized()){
						_memorizedIsInitialized = -1;
						return false;
					}
				}
			}
			if (has_Destinations()) {
				for(DestinationTAG val : _Destinations){
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
			if (has_Action()){
				output.writeString(1, _Action);
			}
			if (has_Number()){
				output.wirteInt32(2, _Number);
			}
			if (has_X()){
				output.wirteInt32(3, _X);
			}
			if (has_Y()){
				output.wirteInt32(4, _Y);
			}
			if (has_Range()){
				output.wirteInt32(5, _Range);
			}
			if (has_Priority()){
				output.writeEnum(6, _Priority.toInt());
			}
			if (has_MaxUser()){
				output.wirteInt32(7, _MaxUser);
			}
			if (has_Conditions()){
				for (ConditionsTAG val : _Conditions){
					output.writeMessage(8, val);
				}
			}
			if (has_Destinations()){
				for (EnterMapTAG.MapDataTAG.DestinationTAG val : _Destinations){
					output.writeMessage(9, val);
				}
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x0000000A:{
						set_Action(input.readString());
						break;
					}
					case 0x00000010:{
						set_Number(input.readInt32());
						break;
					}
					case 0x00000018:{
						set_X(input.readInt32());
						break;
					}
					case 0x00000020:{
						set_Y(input.readInt32());
						break;
					}
					case 0x00000028:{
						set_Range(input.readInt32());
						break;
					}
					case 0x00000030:{
						set_Priority(EnterMapTAG.PriorityEnum.fromInt(input.readEnum()));
						break;
					}
					case 0x00000038:{
						set_MaxUser(input.readInt32());
						break;
					}
					case 0x00000042:{
						add_Conditions((ConditionsTAG)input.readMessage(ConditionsTAG.newInstance()));
						break;
					}
					case 0x0000004a:{
						add_Destinations((DestinationTAG)input.readMessage(DestinationTAG.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[EnterMapTAG.MapDataTAG] NEW_TAG : TAG(%d), ACTION(%s), NUMBER(%d)", tag, get_Action(), get_Number()));
						return this;
					}
				}
			}
			return this;
		}
		
		public static final int aa = 0x0000004a;

		@Override
		public void dispose(){
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
		
		public static class DestinationTAG implements ProtoMessage{
			public static DestinationTAG newInstance(){
				return new DestinationTAG();
			}
			private int _Number;
			private int _X;
			private int _Y;
			private int _Range;
			private int _Prob;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private DestinationTAG(){
			}
			public int get_Number(){
				return _Number;
			}
			public void set_Number(int val){
				_bit |= 0x1;
				_Number = val;
			}
			public boolean has_Number(){
				return (_bit & 0x1) == 0x1;
			}
			public int get_X(){
				return _X;
			}
			public void set_X(int val){
				_bit |= 0x2;
				_X = val;
			}
			public boolean has_X(){
				return (_bit & 0x2) == 0x2;
			}
			public int get_Y(){
				return _Y;
			}
			public void set_Y(int val){
				_bit |= 0x4;
				_Y = val;
			}
			public boolean has_Y(){
				return (_bit & 0x4) == 0x4;
			}
			public int get_Range(){
				return _Range;
			}
			public void set_Range(int val){
				_bit |= 0x8;
				_Range = val;
			}
			public boolean has_Range(){
				return (_bit & 0x8) == 0x8;
			}
			public int get_Prob(){
				return _Prob;
			}
			public void set_Prob(int val){
				_bit |= 0x10;
				_Prob = val;
			}
			public boolean has_Prob(){
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
				if (has_Number()){
					size += ProtoOutputStream.computeInt32Size(1, _Number);
				}
				if (has_X()){
					size += ProtoOutputStream.computeInt32Size(2, _X);
				}
				if (has_Y()){
					size += ProtoOutputStream.computeInt32Size(3, _Y);
				}
				if (has_Range()){
					size += ProtoOutputStream.computeInt32Size(4, _Range);
				}
				if (has_Prob()){
					size += ProtoOutputStream.computeInt32Size(5, _Prob);
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_Number()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_X()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_Y()){
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
				if (has_Number()){
					output.wirteInt32(1, _Number);
				}
				if (has_X()){
					output.wirteInt32(2, _X);
				}
				if (has_Y()){
					output.wirteInt32(3, _Y);
				}
				if (has_Range()){
					output.wirteInt32(4, _Range);
				}
				if (has_Prob()){
					output.wirteInt32(5, _Prob);
				}
			}
			@Override
			public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
				while(!input.isAtEnd()){
					int tag = input.readTag();
					switch(tag){
						case 0x00000008:{
							set_Number(input.readInt32());
							break;
						}
						case 0x00000010:{
							set_X(input.readInt32());
							break;
						}
						case 0x00000018:{
							set_Y(input.readInt32());
							break;
						}
						case 0x00000020:{
							set_Range(input.readInt32());
							break;
						}
						case 0x00000028:{
							set_Prob(input.readInt32());
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
	public enum PriorityEnum{
		PRIORITY_LIMIT(1),
		PRIORITY_MINCOUNT(2),
		PRIORITY_MAXCOUNT(3),
		;
		private int value;
		PriorityEnum(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(PriorityEnum v){
			return value == v.value;
		}
		public static PriorityEnum fromInt(int i){
			switch(i){
			case 1:
				return PRIORITY_LIMIT;
			case 2:
				return PRIORITY_MINCOUNT;
			case 3:
				return PRIORITY_MAXCOUNT;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments PriorityEnum, %d", i));
			}
		}
	}
}

