package l1j.server.common.data;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class ConditionsTAG implements ProtoMessage{
	public static ConditionsTAG newInstance(){
		return new ConditionsTAG();
	}
	private ConditionsTAG.LevelTAG _Level;
	private java.util.LinkedList<ConditionsTAG.UserTAG> _User;
	private java.util.LinkedList<ItemTAG> _Item;
	private java.util.LinkedList<ConditionsTAG.NonExistentItemTAG> _NonExistentItem;
	private ConditionsTAG.QuestTAG _Quest;
	private OpenPeriodTAG _OpenPeriod;
	private java.util.LinkedList<SpellBuffTAG> _SpellBuff;
	private SpellTAG _Spell;
	private java.util.LinkedList<ConditionsTAG.ItemListTAG> _ItemList;
	private java.util.LinkedList<ConditionsTAG.MultiTypeItemTAG> _MultiTypeItem;
	private ConditionsTAG.AlignTAG _Align;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private ConditionsTAG(){
	}
	public ConditionsTAG.LevelTAG get_Level(){
		return _Level;
	}
	public void set_Level(ConditionsTAG.LevelTAG val){
		_bit |= 0x1;
		_Level = val;
	}
	public boolean has_Level(){
		return (_bit & 0x1) == 0x1;
	}
	public java.util.LinkedList<ConditionsTAG.UserTAG> get_User(){
		return _User;
	}
	public void add_User(ConditionsTAG.UserTAG val){
		if(!has_User()){
			_User = new java.util.LinkedList<ConditionsTAG.UserTAG>();
			_bit |= 0x2;
		}
		_User.add(val);
	}
	public boolean has_User(){
		return (_bit & 0x2) == 0x2;
	}
	public java.util.LinkedList<ItemTAG> get_Item(){
		return _Item;
	}
	public void add_Item(ItemTAG val){
		if(!has_Item()){
			_Item = new java.util.LinkedList<ItemTAG>();
			_bit |= 0x4;
		}
		_Item.add(val);
	}
	public boolean has_Item(){
		return (_bit & 0x4) == 0x4;
	}
	public java.util.LinkedList<ConditionsTAG.NonExistentItemTAG> get_NonExistentItem(){
		return _NonExistentItem;
	}
	public void add_NonExistentItem(ConditionsTAG.NonExistentItemTAG val){
		if(!has_NonExistentItem()){
			_NonExistentItem = new java.util.LinkedList<ConditionsTAG.NonExistentItemTAG>();
			_bit |= 0x8;
		}
		_NonExistentItem.add(val);
	}
	public boolean has_NonExistentItem(){
		return (_bit & 0x8) == 0x8;
	}
	public ConditionsTAG.QuestTAG get_Quest(){
		return _Quest;
	}
	public void set_Quest(ConditionsTAG.QuestTAG val){
		_bit |= 0x10;
		_Quest = val;
	}
	public boolean has_Quest(){
		return (_bit & 0x10) == 0x10;
	}
	public OpenPeriodTAG get_OpenPeriod(){
		return _OpenPeriod;
	}
	public void set_OpenPeriod(OpenPeriodTAG val){
		_bit |= 0x20;
		_OpenPeriod = val;
	}
	public boolean has_OpenPeriod(){
		return (_bit & 0x20) == 0x20;
	}
	public java.util.LinkedList<SpellBuffTAG> get_SpellBuff(){
		return _SpellBuff;
	}
	public void add_SpellBuff(SpellBuffTAG val){
		if(!has_SpellBuff()){
			_SpellBuff = new java.util.LinkedList<SpellBuffTAG>();
			_bit |= 0x40;
		}
		_SpellBuff.add(val);
	}
	public boolean has_SpellBuff(){
		return (_bit & 0x40) == 0x40;
	}
	public SpellTAG get_Spell(){
		return _Spell;
	}
	public void set_Spell(SpellTAG val){
		_bit |= 0x80;
		_Spell = val;
	}
	public boolean has_Spell(){
		return (_bit & 0x80) == 0x80;
	}
	public java.util.LinkedList<ConditionsTAG.ItemListTAG> get_ItemList(){
		return _ItemList;
	}
	public void add_ItemList(ConditionsTAG.ItemListTAG val){
		if(!has_ItemList()){
			_ItemList = new java.util.LinkedList<ConditionsTAG.ItemListTAG>();
			_bit |= 0x100;
		}
		_ItemList.add(val);
	}
	public boolean has_ItemList(){
		return (_bit & 0x100) == 0x100;
	}
	public java.util.LinkedList<ConditionsTAG.MultiTypeItemTAG> get_MultiTypeItem(){
		return _MultiTypeItem;
	}
	public void add_MultiTypeItem(ConditionsTAG.MultiTypeItemTAG val){
		if(!has_MultiTypeItem()){
			_MultiTypeItem = new java.util.LinkedList<ConditionsTAG.MultiTypeItemTAG>();
			_bit |= 0x200;
		}
		_MultiTypeItem.add(val);
	}
	public boolean has_MultiTypeItem(){
		return (_bit & 0x200) == 0x200;
	}
	public ConditionsTAG.AlignTAG get_Align(){
		return _Align;
	}
	public void set_Align(ConditionsTAG.AlignTAG val){
		_bit |= 0x400;
		_Align = val;
	}
	public boolean has_Align(){
		return (_bit & 0x400) == 0x400;
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
		if (has_Level()){
			size += ProtoOutputStream.computeMessageSize(1, _Level);
		}
		if (has_User()){
			for(ConditionsTAG.UserTAG val : _User){
				size += ProtoOutputStream.computeMessageSize(2, val);
			}
		}
		if (has_Item()){
			for(ItemTAG val : _Item){
				size += ProtoOutputStream.computeMessageSize(3, val);
			}
		}
		if (has_NonExistentItem()){
			for(ConditionsTAG.NonExistentItemTAG val : _NonExistentItem){
				size += ProtoOutputStream.computeMessageSize(4, val);
			}
		}
		if (has_Quest()){
			size += ProtoOutputStream.computeMessageSize(5, _Quest);
		}
		if (has_OpenPeriod()){
			size += ProtoOutputStream.computeMessageSize(6, _OpenPeriod);
		}
		if (has_SpellBuff()){
			for(SpellBuffTAG val : _SpellBuff){
				size += ProtoOutputStream.computeMessageSize(7, val);
			}
		}
		if (has_Spell()){
			size += ProtoOutputStream.computeMessageSize(8, _Spell);
		}
		if (has_ItemList()){
			for(ConditionsTAG.ItemListTAG val : _ItemList){
				size += ProtoOutputStream.computeMessageSize(9, val);
			}
		}
		if (has_MultiTypeItem()){
			for(ConditionsTAG.MultiTypeItemTAG val : _MultiTypeItem){
				size += ProtoOutputStream.computeMessageSize(10, val);
			}
		}
		if (has_Align()){
			size += ProtoOutputStream.computeMessageSize(11, _Align);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (has_User()){
			for(ConditionsTAG.UserTAG val : _User){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_Item()){
			for(ItemTAG val : _Item){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_NonExistentItem()){
			for(ConditionsTAG.NonExistentItemTAG val : _NonExistentItem){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_SpellBuff()){
			for(SpellBuffTAG val : _SpellBuff){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_ItemList()){
			for(ConditionsTAG.ItemListTAG val : _ItemList){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_MultiTypeItem()){
			for(ConditionsTAG.MultiTypeItemTAG val : _MultiTypeItem){
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
		if (has_Level()){
			output.writeMessage(1, _Level);
		}
		if (has_User()){
			for (ConditionsTAG.UserTAG val : _User){
				output.writeMessage(2, val);
			}
		}
		if (has_Item()){
			for (ItemTAG val : _Item){
				output.writeMessage(3, val);
			}
		}
		if (has_NonExistentItem()){
			for (ConditionsTAG.NonExistentItemTAG val : _NonExistentItem){
				output.writeMessage(4, val);
			}
		}
		if (has_Quest()){
			output.writeMessage(5, _Quest);
		}
		if (has_OpenPeriod()){
			output.writeMessage(6, _OpenPeriod);
		}
		if (has_SpellBuff()){
			for (SpellBuffTAG val : _SpellBuff){
				output.writeMessage(7, val);
			}
		}
		if (has_Spell()){
			output.writeMessage(8, _Spell);
		}
		if (has_ItemList()){
			for (ConditionsTAG.ItemListTAG val : _ItemList){
				output.writeMessage(9, val);
			}
		}
		if (has_MultiTypeItem()){
			for (ConditionsTAG.MultiTypeItemTAG val : _MultiTypeItem){
				output.writeMessage(10, val);
			}
		}
		if (has_Align()){
			output.writeMessage(11, _Align);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x0000000A:{
					set_Level((ConditionsTAG.LevelTAG)input.readMessage(ConditionsTAG.LevelTAG.newInstance()));
					break;
				}
				case 0x00000012:{
					add_User((ConditionsTAG.UserTAG)input.readMessage(ConditionsTAG.UserTAG.newInstance()));
					break;
				}
				case 0x0000001A:{
					add_Item((ItemTAG)input.readMessage(ItemTAG.newInstance()));
					break;
				}
				case 0x00000022:{
					add_NonExistentItem((ConditionsTAG.NonExistentItemTAG)input.readMessage(ConditionsTAG.NonExistentItemTAG.newInstance()));
					break;
				}
				case 0x0000002A:{
					set_Quest((ConditionsTAG.QuestTAG)input.readMessage(ConditionsTAG.QuestTAG.newInstance()));
					break;
				}
				case 0x00000032:{
					set_OpenPeriod((OpenPeriodTAG)input.readMessage(OpenPeriodTAG.newInstance()));
					break;
				}
				case 0x0000003A:{
					add_SpellBuff((SpellBuffTAG)input.readMessage(SpellBuffTAG.newInstance()));
					break;
				}
				case 0x00000042:{
					set_Spell((SpellTAG)input.readMessage(SpellTAG.newInstance()));
					break;
				}
				case 0x0000004A:{
					add_ItemList((ConditionsTAG.ItemListTAG)input.readMessage(ConditionsTAG.ItemListTAG.newInstance()));
					break;
				}
				case 0x00000052:{
					add_MultiTypeItem((ConditionsTAG.MultiTypeItemTAG)input.readMessage(ConditionsTAG.MultiTypeItemTAG.newInstance()));
					break;
				}
				case 0x0000005A:{
					set_Align((ConditionsTAG.AlignTAG)input.readMessage(ConditionsTAG.AlignTAG.newInstance()));
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
	public static class LevelTAG implements ProtoMessage{
		public static LevelTAG newInstance(){
			return new LevelTAG();
		}
		private int _Minimum;
		private int _Maximum;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private LevelTAG(){
		}
		public int get_Minimum(){
			return _Minimum;
		}
		public void set_Minimum(int val){
			_bit |= 0x1;
			_Minimum = val;
		}
		public boolean has_Minimum(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_Maximum(){
			return _Maximum;
		}
		public void set_Maximum(int val){
			_bit |= 0x2;
			_Maximum = val;
		}
		public boolean has_Maximum(){
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
			if (has_Minimum()){
				size += ProtoOutputStream.computeInt32Size(1, _Minimum);
			}
			if (has_Maximum()){
				size += ProtoOutputStream.computeInt32Size(2, _Maximum);
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
			if (has_Minimum()){
				output.wirteInt32(1, _Minimum);
			}
			if (has_Maximum()){
				output.wirteInt32(2, _Maximum);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_Minimum(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_Maximum(input.readInt32());
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
	public static class VIPTAG implements ProtoMessage{
		public static VIPTAG newInstance(){
			return new VIPTAG();
		}
		private int _Grade;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private VIPTAG(){
		}
		public int get_Grade(){
			return _Grade;
		}
		public void set_Grade(int val){
			_bit |= 0x1;
			_Grade = val;
		}
		public boolean has_Grade(){
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
			if (has_Grade()){
				size += ProtoOutputStream.computeInt32Size(1, _Grade);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_Grade()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_Grade()){
				output.wirteInt32(1, _Grade);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_Grade(input.readInt32());
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
	public static class UserTAG implements ProtoMessage{
		public static UserTAG newInstance(){
			return new UserTAG();
		}
		private boolean _PCCafe;
		private java.util.LinkedList<ConditionsTAG.VIPTAG> _VIP;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private UserTAG(){
		}
		public boolean get_PCCafe(){
			return _PCCafe;
		}
		public void set_PCCafe(boolean val){
			_bit |= 0x1;
			_PCCafe = val;
		}
		public boolean has_PCCafe(){
			return (_bit & 0x1) == 0x1;
		}
		public java.util.LinkedList<ConditionsTAG.VIPTAG> get_VIP(){
			return _VIP;
		}
		public void add_VIP(ConditionsTAG.VIPTAG val){
			if(!has_VIP()){
				_VIP = new java.util.LinkedList<ConditionsTAG.VIPTAG>();
				_bit |= 0x2;
			}
			_VIP.add(val);
		}
		public boolean has_VIP(){
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
			if (has_PCCafe()){
				size += ProtoOutputStream.computeBoolSize(1, _PCCafe);
			}
			if (has_VIP()){
				for(ConditionsTAG.VIPTAG val : _VIP){
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
			if (has_VIP()){
				for(ConditionsTAG.VIPTAG val : _VIP){
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
			if (has_PCCafe()){
				output.writeBool(1, _PCCafe);
			}
			if (has_VIP()){
				for (ConditionsTAG.VIPTAG val : _VIP){
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
						set_PCCafe(input.readBool());
						break;
					}
					case 0x00000012:{
						add_VIP((ConditionsTAG.VIPTAG)input.readMessage(ConditionsTAG.VIPTAG.newInstance()));
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
	public static class NonExistentItemTAG implements ProtoMessage{
		public static NonExistentItemTAG newInstance(){
			return new NonExistentItemTAG();
		}
		private int _NameID;
		private int _Count;
		private ItemTAG.BlessTYPE _Bless;
		private int _Enchant;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private NonExistentItemTAG(){
			set_Bless(ItemTAG.BlessTYPE.NORMAL);
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
		public ItemTAG.BlessTYPE get_Bless(){
			return _Bless;
		}
		public void set_Bless(ItemTAG.BlessTYPE val){
			_bit |= 0x4;
			_Bless = val;
		}
		public boolean has_Bless(){
			return (_bit & 0x4) == 0x4;
		}
		public int get_Enchant(){
			return _Enchant;
		}
		public void set_Enchant(int val){
			_bit |= 0x8;
			_Enchant = val;
		}
		public boolean has_Enchant(){
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
			if (has_NameID()){
				size += ProtoOutputStream.computeInt32Size(1, _NameID);
			}
			if (has_Count()){
				size += ProtoOutputStream.computeInt32Size(2, _Count);
			}
			if (has_Bless()){
				size += ProtoOutputStream.computeEnumSize(3, _Bless.toInt());
			}
			if (has_Enchant()){
				size += ProtoOutputStream.computeInt32Size(4, _Enchant);
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
			if (has_Bless()){
				output.writeEnum(3, _Bless.toInt());
			}
			if (has_Enchant()){
				output.wirteInt32(4, _Enchant);
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
					case 0x00000018:{
						set_Bless(ItemTAG.BlessTYPE.fromInt(input.readEnum()));
						break;
					}
					case 0x00000020:{
						set_Enchant(input.readInt32());
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
	public static class QuestTAG implements ProtoMessage{
		public static QuestTAG newInstance(){
			return new QuestTAG();
		}
		private java.util.LinkedList<Integer> _ProgressQuestIDs;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private QuestTAG(){
		}
		public java.util.LinkedList<Integer> get_ProgressQuestIDs(){
			return _ProgressQuestIDs;
		}
		public void add_ProgressQuestIDs(int val){
			if(!has_ProgressQuestIDs()){
				_ProgressQuestIDs = new java.util.LinkedList<Integer>();
				_bit |= 0x1;
			}
			_ProgressQuestIDs.add(val);
		}
		public boolean has_ProgressQuestIDs(){
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
			if (has_ProgressQuestIDs()){
				for(int val : _ProgressQuestIDs){
					size += ProtoOutputStream.computeInt32Size(1, val);
				}
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			/*if (has_ProgressQuestIDs()){
				for(int val : _ProgressQuestIDs){
					if (!val.isInitialized()){
						_memorizedIsInitialized = -1;
						return false;
					}
				}
			}*/
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_ProgressQuestIDs()){
				for (int val : _ProgressQuestIDs){
					output.wirteInt32(1, val);
				}
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						add_ProgressQuestIDs(input.readInt32());
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
	
	public static class ItemListTAG implements ProtoMessage{
		public static ItemListTAG newInstance(){
			return new ItemListTAG();
		}
		private java.util.LinkedList<Integer> _NameIDs;
		private int _Count;
		private ConditionsTAG.ItemListTAG.eConnective _Connective;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private ItemListTAG(){
			set_Connective(ConditionsTAG.ItemListTAG.eConnective.And);
		}
		public java.util.LinkedList<Integer> get_NameIDs(){
			return _NameIDs;
		}
		public void add_NameIDs(int val){
			if(!has_NameIDs()){
				_NameIDs = new java.util.LinkedList<Integer>();
				_bit |= 0x1;
			}
			_NameIDs.add(val);
		}
		public boolean has_NameIDs(){
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
		public ConditionsTAG.ItemListTAG.eConnective get_Connective(){
			return _Connective;
		}
		public void set_Connective(ConditionsTAG.ItemListTAG.eConnective val){
			_bit |= 0x4;
			_Connective = val;
		}
		public boolean has_Connective(){
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
			if (has_NameIDs()){
				for(int val : _NameIDs){
					size += ProtoOutputStream.computeUInt32Size(1, val);
				}
			}
			if (has_Count()){
				size += ProtoOutputStream.computeUInt32Size(2, _Count);
			}
			if (has_Connective()){
				size += ProtoOutputStream.computeEnumSize(3, _Connective.toInt());
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			/*if (has_NameIDs()){
				for(int val : _NameIDs){
					if (!val.isInitialized()){
						_memorizedIsInitialized = -1;
						return false;
					}
				}
			}*/
			if (!has_Count()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_NameIDs()){
				for (int val : _NameIDs){
					output.writeUInt32(1, val);
				}
			}
			if (has_Count()){
				output.writeUInt32(2, _Count);
			}
			if (has_Connective()){
				output.writeEnum(3, _Connective.toInt());
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						add_NameIDs(input.readUInt32());
						break;
					}
					case 0x00000010:{
						set_Count(input.readUInt32());
						break;
					}
					case 0x00000018:{
						set_Connective(ConditionsTAG.ItemListTAG.eConnective.fromInt(input.readEnum()));
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
		public enum eConnective{
			And(1),
			Or(2),
			;
			private int value;
			eConnective(int val){
				value = val;
			}
			public int toInt(){
				return value;
			}
			public boolean equals(eConnective v){
				return value == v.value;
			}
			public static eConnective fromInt(int i){
				switch(i){
				case 1:
					return And;
				case 2:
					return Or;
				default:
					throw new IllegalArgumentException(String.format("invalid arguments eConnective, %d", i));
				}
			}
		}
	}
	
	public static class MultiTypeItemTAG implements ProtoMessage{
		public static MultiTypeItemTAG newInstance(){
			return new MultiTypeItemTAG();
		}
		private ConditionsTAG.MultiTypeItemTAG.eMultiType _TypeID;
		private int _NameID;
		private int _Count;
		private DELETE_TYPE _Delete;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private MultiTypeItemTAG(){
			set_Delete(DELETE_TYPE.DELETE_NOTHING);
		}
		public ConditionsTAG.MultiTypeItemTAG.eMultiType get_TypeID(){
			return _TypeID;
		}
		public void set_TypeID(ConditionsTAG.MultiTypeItemTAG.eMultiType val){
			_bit |= 0x1;
			_TypeID = val;
		}
		public boolean has_TypeID(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_NameID(){
			return _NameID;
		}
		public void set_NameID(int val){
			_bit |= 0x2;
			_NameID = val;
		}
		public boolean has_NameID(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_Count(){
			return _Count;
		}
		public void set_Count(int val){
			_bit |= 0x4;
			_Count = val;
		}
		public boolean has_Count(){
			return (_bit & 0x4) == 0x4;
		}
		public DELETE_TYPE get_Delete(){
			return _Delete;
		}
		public void set_Delete(DELETE_TYPE val){
			_bit |= 0x8;
			_Delete = val;
		}
		public boolean has_Delete(){
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
			if (has_TypeID()){
				size += ProtoOutputStream.computeEnumSize(1, _TypeID.toInt());
			}
			if (has_NameID()){
				size += ProtoOutputStream.computeInt32Size(2, _NameID);
			}
			if (has_Count()){
				size += ProtoOutputStream.computeInt32Size(3, _Count);
			}
			if (has_Delete()){
				size += ProtoOutputStream.computeEnumSize(4, _Delete.toInt());
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_TypeID()){
				_memorizedIsInitialized = -1;
				return false;
			}
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
			if (has_TypeID()){
				output.writeEnum(1, _TypeID.toInt());
			}
			if (has_NameID()){
				output.wirteInt32(2, _NameID);
			}
			if (has_Count()){
				output.wirteInt32(3, _Count);
			}
			if (has_Delete()){
				output.writeEnum(4, _Delete.toInt());
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_TypeID(ConditionsTAG.MultiTypeItemTAG.eMultiType.fromInt(input.readEnum()));
						break;
					}
					case 0x00000010:{
						set_NameID(input.readInt32());
						break;
					}
					case 0x00000018:{
						set_Count(input.readInt32());
						break;
					}
					case 0x00000020:{
						set_Delete(DELETE_TYPE.fromInt(input.readEnum()));
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
		public enum eMultiType{
			PCCafe(1),
			NonePCCafe(2),
			;
			private int value;
			eMultiType(int val){
				value = val;
			}
			public int toInt(){
				return value;
			}
			public boolean equals(eMultiType v){
				return value == v.value;
			}
			public static eMultiType fromInt(int i){
				switch(i){
				case 1:
					return PCCafe;
				case 2:
					return NonePCCafe;
				default:
					throw new IllegalArgumentException(String.format("invalid arguments eMultiType, %d", i));
				}
			}
		}
	}
	public static class AlignTAG implements ProtoMessage{
		public static AlignTAG newInstance(){
			return new AlignTAG();
		}
		private boolean _Lawful;
		private boolean _Neutral;
		private boolean _Chaotic;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private AlignTAG(){
		}
		public boolean get_Lawful(){
			return _Lawful;
		}
		public void set_Lawful(boolean val){
			_bit |= 0x1;
			_Lawful = val;
		}
		public boolean has_Lawful(){
			return (_bit & 0x1) == 0x1;
		}
		public boolean get_Neutral(){
			return _Neutral;
		}
		public void set_Neutral(boolean val){
			_bit |= 0x2;
			_Neutral = val;
		}
		public boolean has_Neutral(){
			return (_bit & 0x2) == 0x2;
		}
		public boolean get_Chaotic(){
			return _Chaotic;
		}
		public void set_Chaotic(boolean val){
			_bit |= 0x4;
			_Chaotic = val;
		}
		public boolean has_Chaotic(){
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
			if (has_Lawful()){
				size += ProtoOutputStream.computeBoolSize(1, _Lawful);
			}
			if (has_Neutral()){
				size += ProtoOutputStream.computeBoolSize(2, _Neutral);
			}
			if (has_Chaotic()){
				size += ProtoOutputStream.computeBoolSize(3, _Chaotic);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_Lawful()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_Neutral()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_Chaotic()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_Lawful()){
				output.writeBool(1, _Lawful);
			}
			if (has_Neutral()){
				output.writeBool(2, _Neutral);
			}
			if (has_Chaotic()){
				output.writeBool(3, _Chaotic);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_Lawful(input.readBool());
						break;
					}
					case 0x00000010:{
						set_Neutral(input.readBool());
						break;
					}
					case 0x00000018:{
						set_Chaotic(input.readBool());
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

