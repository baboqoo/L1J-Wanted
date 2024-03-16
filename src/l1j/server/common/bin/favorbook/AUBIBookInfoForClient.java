package l1j.server.common.bin.favorbook;

import java.util.HashMap;
import java.util.LinkedList;

import l1j.server.common.bin.CraftCommonBinLoader;
import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;
import l1j.server.common.bin.craft.Craft;
import l1j.server.common.bin.craft.CraftOutputItem;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.StringUtil;

public class AUBIBookInfoForClient implements ProtoMessage{
	public static AUBIBookInfoForClient newInstance(){
		return new AUBIBookInfoForClient();
	}
	private AUBIBookInfoForClient.SetBonusT _set_bonus;
	private AUBIBookInfoForClient.BookT _book;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private AUBIBookInfoForClient(){
	}

	public AUBIBookInfoForClient.SetBonusT get_set_bonus() {
		return _set_bonus;
	}
	public void set_set_bonus(AUBIBookInfoForClient.SetBonusT val) {
		_bit |= 0x1;
		_set_bonus = val;
	}
	public boolean has_set_bonus() {
		return (_bit & 0x1) == 0x1;
	}
	public AUBIBookInfoForClient.BookT get_book(){
		return _book;
	}
	public void set_book(AUBIBookInfoForClient.BookT val){
		_bit |= 0x2;
		_book = val;
	}
	public boolean has_book(){
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
		if (has_set_bonus()){
			size += ProtoOutputStream.computeMessageSize(1, _set_bonus);
		}
		if (has_book()){
			size += ProtoOutputStream.computeMessageSize(2, _book);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_set_bonus()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_book()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_set_bonus()){
			output.writeMessage(1, _set_bonus);
		}
		if (has_book()){
			output.writeMessage(2, _book);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x0000000A:
					set_set_bonus((AUBIBookInfoForClient.SetBonusT)input.readMessage(AUBIBookInfoForClient.SetBonusT.newInstance()));
					break;
				case 0x00000012:
					set_book((AUBIBookInfoForClient.BookT) input.readMessage(AUBIBookInfoForClient.BookT.newInstance()));
					break;
				default:
					System.out.println(String.format("[AUBIBookInfoForClient] NEW_TAG : TAG(%d)", tag));
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
	
	public static class SetBonusT implements ProtoMessage{
		public static AUBIBookInfoForClient.SetBonusT newInstance(){
			return new AUBIBookInfoForClient.SetBonusT();
		}
		private java.util.LinkedList<AUBIBookInfoForClient.SetBonusT.SetT> _sets;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private SetBonusT(){
		}
		public java.util.LinkedList<AUBIBookInfoForClient.SetBonusT.SetT> get_sets(){
			return _sets;
		}
		public void add_sets(AUBIBookInfoForClient.SetBonusT.SetT val){
			if(!has_sets()){
				_sets = new java.util.LinkedList<AUBIBookInfoForClient.SetBonusT.SetT>();
				_bit |= 0x1;
			}
			_sets.add(val);
		}
		public boolean has_sets(){
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
			if (has_sets()){
				for(AUBIBookInfoForClient.SetBonusT.SetT val : _sets){
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
			if (has_sets()){
				for(AUBIBookInfoForClient.SetBonusT.SetT val : _sets){
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
			if (has_sets()){
				for (AUBIBookInfoForClient.SetBonusT.SetT val : _sets){
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
						add_sets((AUBIBookInfoForClient.SetBonusT.SetT)input.readMessage(AUBIBookInfoForClient.SetBonusT.SetT.newInstance()));
						break;
					}
					default:{
						System.out.println(String.format("[AUBIBookInfoForClient.SetBonusT] NEW_TAG : TAG(%d)", tag));
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
		
		public static class SetT implements ProtoMessage{
			public static AUBIBookInfoForClient.SetBonusT.SetT newInstance(){
				return new AUBIBookInfoForClient.SetBonusT.SetT();
			}
			private int _id;
			private java.util.LinkedList<AUBIBookInfoForClient.SetBonusT.SetT.PartT> _parts;
			private String _start_date;
			private String _end_date;
			private byte[] _bonus;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private SetT(){
			}
			public int get_id(){
				return _id;
			}
			public void set_id(int val){
				_bit |= 0x1;
				_id = val;
			}
			public boolean has_id(){
				return (_bit & 0x1) == 0x1;
			}
			public java.util.LinkedList<AUBIBookInfoForClient.SetBonusT.SetT.PartT> get_parts(){
				return _parts;
			}
			public void add_parts(AUBIBookInfoForClient.SetBonusT.SetT.PartT val){
				if(!has_parts()){
					_parts = new java.util.LinkedList<AUBIBookInfoForClient.SetBonusT.SetT.PartT>();
					_bit |= 0x2;
				}
				_parts.add(val);
			}
			public boolean has_parts(){
				return (_bit & 0x2) == 0x2;
			}
			public String get_start_date(){
				return _start_date;
			}
			public void set_start_date(String val){
				_bit |= 0x4;
				_start_date = val;
			}
			public boolean has_start_date(){
				return (_bit & 0x4) == 0x4;
			}
			public String get_end_date(){
				return _end_date;
			}
			public void set_end_date(String val){
				_bit |= 0x8;
				_end_date = val;
			}
			public boolean has_end_date(){
				return (_bit & 0x8) == 0x8;
			}
			public byte[] get_bonus(){
				return _bonus;
			}
			public void set_bonus(byte[] val){
				_bit |= 0x10;
				_bonus = val;
			}
			public boolean has_bonus(){
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
				if (has_id()){
					size += ProtoOutputStream.computeInt32Size(1, _id);
				}
				if (has_parts()){
					for(AUBIBookInfoForClient.SetBonusT.SetT.PartT val : _parts){
						size += ProtoOutputStream.computeMessageSize(2, val);
					}
				}
				if (has_start_date()){
					size += ProtoOutputStream.computeStringSize(3, _start_date);
				}
				if (has_end_date()){
					size += ProtoOutputStream.computeStringSize(4, _end_date);
				}
				if (has_bonus()){
					size += ProtoOutputStream.computeBytesSize(5, _bonus);
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_id()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (has_parts()){
					for(AUBIBookInfoForClient.SetBonusT.SetT.PartT val : _parts){
						if (!val.isInitialized()){
							_memorizedIsInitialized = -1;
							return false;
						}
					}
				}
				if (!has_bonus()){
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}
			@Override
			public void writeTo(ProtoOutputStream output) throws java.io.IOException{
				if (has_id()){
					output.wirteInt32(1, _id);
				}
				if (has_parts()){
					for (AUBIBookInfoForClient.SetBonusT.SetT.PartT val : _parts){
						output.writeMessage(2, val);
					}
				}
				if (has_start_date()){
					output.writeString(3, _start_date);
				}
				if (has_end_date()){
					output.writeString(4, _end_date);
				}
				if (has_bonus()){
					output.writeBytes(5, _bonus);
				}
			}
			@Override
			public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
				while(!input.isAtEnd()){
					int tag = input.readTag();
					switch(tag){
						case 0x00000008:{
							set_id(input.readInt32());
							break;
						}
						case 0x00000012:{
							add_parts((AUBIBookInfoForClient.SetBonusT.SetT.PartT)input.readMessage(AUBIBookInfoForClient.SetBonusT.SetT.PartT.newInstance()));
							break;
						}
						case 0x0000001A:{
							set_start_date(input.readString());
							break;
						}
						case 0x00000022:{
							set_end_date(input.readString());
							break;
						}
						case 0x0000002A:{
							set_bonus(input.readBytes());
							break;
						}
						default:{
							System.out.println(String.format("[AUBIBookInfoForClient.SetBonusT.SetT] NEW_TAG : TAG(%d)", tag));
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
			
			public static class PartT implements ProtoMessage{
				public static AUBIBookInfoForClient.SetBonusT.SetT.PartT newInstance(){
					return new AUBIBookInfoForClient.SetBonusT.SetT.PartT();
				}
				private int _category;
				private int _slot;
				private int _memorizedSerializedSize = -1;
				private byte _memorizedIsInitialized = -1;
				private int _bit;
				private PartT(){
				}
				public int get_category(){
					return _category;
				}
				public void set_category(int val){
					_bit |= 0x1;
					_category = val;
				}
				public boolean has_category(){
					return (_bit & 0x1) == 0x1;
				}
				public int get_slot(){
					return _slot;
				}
				public void set_slot(int val){
					_bit |= 0x2;
					_slot = val;
				}
				public boolean has_slot(){
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
					if (has_category()){
						size += ProtoOutputStream.computeInt32Size(1, _category);
					}
					if (has_slot()){
						size += ProtoOutputStream.computeInt32Size(2, _slot);
					}
					_memorizedSerializedSize = size;
					return size;
				}
				@Override
				public boolean isInitialized(){
					if(_memorizedIsInitialized == 1)
						return true;
					if (!has_category()){
						_memorizedIsInitialized = -1;
						return false;
					}
					if (!has_slot()){
						_memorizedIsInitialized = -1;
						return false;
					}
					_memorizedIsInitialized = 1;
					return true;
				}
				@Override
				public void writeTo(ProtoOutputStream output) throws java.io.IOException{
					if (has_category()){
						output.wirteInt32(1, _category);
					}
					if (has_slot()){
						output.wirteInt32(2, _slot);
					}
				}
				@Override
				public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
					while(!input.isAtEnd()){
						int tag = input.readTag();
						switch(tag){
							case 0x00000008:{
								set_category(input.readInt32());
								break;
							}
							case 0x00000010:{
								set_slot(input.readInt32());
								break;
							}
							default:{
								System.out.println(String.format("[AUBIBookInfoForClient.SetBonusT.SetT.PartT] NEW_TAG : TAG(%d)", tag));
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
	
	public static class BookT implements ProtoMessage{
		public static AUBIBookInfoForClient.BookT newInstance(){
			return new AUBIBookInfoForClient.BookT();
		}
		private java.util.LinkedList<AUBIBookInfoForClient.BookT.CategoryT> _categories;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private BookT(){
		}
		
		public java.util.LinkedList<AUBIBookInfoForClient.BookT.CategoryT> get_categories(){
			return _categories;
		}
		public void add_categories(AUBIBookInfoForClient.BookT.CategoryT val){
			if (!has_categories()) {
				_bit |= 0x1;
				_categories = new java.util.LinkedList<AUBIBookInfoForClient.BookT.CategoryT>();
			}
			_categories.add(val);
		}
		public boolean has_categories(){
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
			if (has_categories()){
				for(AUBIBookInfoForClient.BookT.CategoryT val : _categories){
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
			if (!has_categories()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_categories()){
				for (AUBIBookInfoForClient.BookT.CategoryT val : _categories){
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
						add_categories((AUBIBookInfoForClient.BookT.CategoryT) input.readMessage(AUBIBookInfoForClient.BookT.CategoryT.newInstance()));
						break;
					default:
						System.out.println(String.format("[AUBIBookInfoForClient.BookT] NEW_TAG : TAG(%d)", tag));
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
		
		public static class CategoryT implements ProtoMessage{
			public static AUBIBookInfoForClient.BookT.CategoryT newInstance(){
				return new AUBIBookInfoForClient.BookT.CategoryT();
			}
			private int _id;
			private String _desc;
			private String _start_date;
			private String _end_date;
			private int _sort;
			private HashMap<Integer, AUBIBookInfoForClient.BookT.CategoryT.SlotT> _slots;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;
			private CategoryT(){
			}
			public int get_id(){
				return _id;
			}
			public void set_id(int val){
				_bit |= 0x1;
				_id = val;
			}
			public boolean has_id(){
				return (_bit & 0x1) == 0x1;
			}
			
			public String get_desc(){
				return _desc;
			}
			public void set_desc(String val){
				_bit |= 0x2;
				_desc = val;
			}
			public boolean has_desc(){
				return (_bit & 0x2) == 0x2;
			}
			
			public String get_start_date(){
				return _start_date;
			}
			public void set_start_date(String val){
				_bit |= 0x4;
				_start_date = val;
			}
			public boolean has_start_date(){
				return (_bit & 0x4) == 0x4;
			}
			
			public String get_end_date(){
				return _end_date;
			}
			public void set_end_date(String val){
				_bit |= 0x8;
				_end_date = val;
			}
			public boolean has_end_date(){
				return (_bit & 0x8) == 0x8;
			}
			
			public int get_sort(){
				return _sort;
			}
			public void set_sort(int val){
				_bit |= 0x10;
				_sort = val;
			}
			public boolean has_sort(){
				return (_bit & 0x10) == 0x10;
			}
			
			public HashMap<Integer, AUBIBookInfoForClient.BookT.CategoryT.SlotT> get_slots() {
				return _slots;
			}
			public void add_slots(AUBIBookInfoForClient.BookT.CategoryT.SlotT val){
				if (!has_slots()) {
					_bit |= 0x20;
					_slots = new HashMap<Integer, AUBIBookInfoForClient.BookT.CategoryT.SlotT>();
				}
				_slots.put(val.get_id(), val);
			}
			public boolean has_slots() {
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
				if (has_id()){
					size += ProtoOutputStream.computeInt32Size(1, _id);
				}
				if (has_desc()){
					size += ProtoOutputStream.computeStringSize(2, _desc);
				}
				if (has_start_date()){
					size += ProtoOutputStream.computeStringSize(3, _start_date);
				}
				if (has_end_date()){
					size += ProtoOutputStream.computeStringSize(4, _end_date);
				}
				if (has_sort()){
					size += ProtoOutputStream.computeInt32Size(5, _sort);
				}
				if (has_slots()){
					for(AUBIBookInfoForClient.BookT.CategoryT.SlotT val : _slots.values()){
						size += ProtoOutputStream.computeMessageSize(6, val);
					}
				}
				_memorizedSerializedSize = size;
				return size;
			}
			@Override
			public boolean isInitialized(){
				if(_memorizedIsInitialized == 1)
					return true;
				if (!has_id()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_desc()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_sort()){
					_memorizedIsInitialized = -1;
					return false;
				}
				if (has_slots()){
					for (AUBIBookInfoForClient.BookT.CategoryT.SlotT val : _slots.values()) {
						if (!val.isInitialized()) {
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
				if (has_id()){
					output.wirteInt32(1, _id);
				}
				if (has_desc()){
					output.writeString(2, _desc);
				}
				if (has_start_date()){
					output.writeString(3, _start_date);
				}
				if (has_end_date()){
					output.writeString(4, _end_date);
				}
				if (has_sort()){
					output.wirteInt32(5, _sort);
				}
				if (has_slots()){
					for (AUBIBookInfoForClient.BookT.CategoryT.SlotT val : _slots.values()){
						output.writeMessage(6, val);
					}
				}
			}
			@Override
			public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
				while(!input.isAtEnd()){
					int tag = input.readTag();
					switch(tag){
						case 0x00000008:
							set_id(input.readInt32());
							break;
						case 0x00000012:
							set_desc(input.readString());
							break;
						case 0x0000001A:
							set_start_date(input.readString());
							break;
						case 0x00000022:
							set_end_date(input.readString());
							break;
						case 0x00000028:
							set_sort(input.readInt32());
							break;
						case 0x00000032:
							add_slots((AUBIBookInfoForClient.BookT.CategoryT.SlotT) input.readMessage(AUBIBookInfoForClient.BookT.CategoryT.SlotT.newInstance()));
							break;
						default:
							System.out.println(String.format("[AUBIBookInfoForClient.BookT.CategoryT] NEW_TAG : TAG(%d)", tag));
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
			
			public static class SlotT implements ProtoMessage{
				public static AUBIBookInfoForClient.BookT.CategoryT.SlotT newInstance(){
					return new AUBIBookInfoForClient.BookT.CategoryT.SlotT();
				}
				
				/**
				 * 다음 상태 정보를 조사한다.
				 * @param craft_id
				 * @return AUBIBookInfoForClient.BookT.CategoryT.SlotT.StateT
				 */
				public AUBIBookInfoForClient.BookT.CategoryT.SlotT.StateT get_next_state_info(int category, int craft_id, L1Item item) {
					AUBIBookInfoForClient.BookT.CategoryT.SlotT.StateT stateT = null;
					int size = _state_infos.size();
					
					for (int i=0; i<size; i++) {
						stateT = _state_infos.get(i);
						int state_craft_id = stateT.get_craft_id();
						if (state_craft_id == craft_id) {
							continue;
						}
						Craft craft = CraftCommonBinLoader.getCraft(state_craft_id);
						if (craft != null) {
							CraftOutputItem outputItem = craft.get_outputs().get_success().get_output_items().getFirst();
							if (outputItem.get_name_id() == item.getItemNameId()) {
								craft_id = state_craft_id;
								break;
							}
							// 축복의 성물
							if (category == 1 && outputItem.get_bless() == item.getBless()) {
								craft_id = state_craft_id;
								break;
							}
						}
					}
					
					for (int i=0; i<size; i++) {
						stateT = _state_infos.get(i);
						if (stateT.get_craft_id() == craft_id) {
							if (size >= i + 2) {
								return _state_infos.get(i + 1);// 다음 상태 정보
							}
							return stateT;
						}
					}
					return null;
				}
				
				private int _id;
				private LinkedList<AUBIBookInfoForClient.BookT.CategoryT.SlotT.StateT> _state_infos;
				private int _red_dot_notice;
				private int _default_display_item_id;
				private int _memorizedSerializedSize = -1;
				private byte _memorizedIsInitialized = -1;
				private int _bit;
				private SlotT(){
				}
				public int get_id() {
					return _id;
				}
				public void set_id(int val) {
					_bit |= 0x1;
					_id = val;
				}
				public boolean has_id() {
					return (_bit & 0x1) == 0x1;
				}
				public LinkedList<AUBIBookInfoForClient.BookT.CategoryT.SlotT.StateT> get_state_infos(){
					return _state_infos;
				}
				public String get_state_infos_toString() {
					if (_state_infos == null || _state_infos.isEmpty()) {
						return null;
					}
					StringBuilder sb = new StringBuilder();
					for (AUBIBookInfoForClient.BookT.CategoryT.SlotT.StateT craft : _state_infos) {
						if (sb.length() > 0) {
							sb.append(StringUtil.LineString);
						}
						sb.append("CRAFT_ID: ").append(craft.get_craft_id()).append(", AWAKENING: ").append(craft.get_awakening());
					}
					return sb.toString();
				}
				public void add_state_infos(AUBIBookInfoForClient.BookT.CategoryT.SlotT.StateT val){
					if (!has_state_infos()) {
						_state_infos = new LinkedList<AUBIBookInfoForClient.BookT.CategoryT.SlotT.StateT>();
					}
					_bit |= 0x2;
					_state_infos.add(val);
				}
				public boolean has_state_infos(){
					return (_bit & 0x2) == 0x2;
				}
				
				public int get_red_dot_notice() {
					return _red_dot_notice;
				}
				public void set_red_dot_notice(int val) {
					_bit |= 0x4;
					_red_dot_notice = val;
				}
				public boolean has_red_dot_notice() {
					return (_bit & 0x4) == 0x4;
				}
				
				public int get_default_display_item_id() {
					return _default_display_item_id;
				}
				public void set_default_display_item_id(int val) {
					_bit |= 0x8;
					_default_display_item_id = val;
				}
				public boolean has_default_display_item_id() {
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
					if (has_id()){
						size += ProtoOutputStream.computeInt32Size(1, _id);
					}
					if (has_state_infos()){
						for(AUBIBookInfoForClient.BookT.CategoryT.SlotT.StateT val : _state_infos){
							size += ProtoOutputStream.computeMessageSize(2, val);
						}
					}
					if (has_red_dot_notice()){
						size += ProtoOutputStream.computeInt32Size(3, _red_dot_notice);
					}
					if (has_default_display_item_id()){
						size += ProtoOutputStream.computeInt32Size(4, _default_display_item_id);
					}
					_memorizedSerializedSize = size;
					return size;
				}
				@Override
				public boolean isInitialized(){
					if(_memorizedIsInitialized == 1)
						return true;
					if (!has_id()){
						_memorizedIsInitialized = -1;
						return false;
					}
					if (has_state_infos()){
						for (AUBIBookInfoForClient.BookT.CategoryT.SlotT.StateT val : _state_infos) {
							if (!val.isInitialized()) {
								_memorizedIsInitialized = -1;
								return false;
							}
						}
					}
					if (!has_red_dot_notice()){
						_memorizedIsInitialized = -1;
						return false;
					}

					_memorizedIsInitialized = 1;
					return true;
				}
				@Override
				public void writeTo(ProtoOutputStream output) throws java.io.IOException{
					if (has_id()){
						output.wirteInt32(1, _id);
					}
					if (has_state_infos()){
						for (AUBIBookInfoForClient.BookT.CategoryT.SlotT.StateT val : _state_infos){
							output.writeMessage(2, val);
						}
					}
					if (has_red_dot_notice()){
						output.wirteInt32(3, _red_dot_notice);
					}
					if (has_default_display_item_id()){
						output.wirteInt32(4, _default_display_item_id);
					}
				}
				@Override
				public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
					while(!input.isAtEnd()){
						int tag = input.readTag();
						switch(tag){
							case 0x00000008:
								set_id(input.readInt32());
								break;
							case 0x00000012:
								add_state_infos((AUBIBookInfoForClient.BookT.CategoryT.SlotT.StateT) input.readMessage(AUBIBookInfoForClient.BookT.CategoryT.SlotT.StateT.newInstance()));
								break;
							case 0x00000018:
								set_red_dot_notice(input.readInt32());
								break;
							case 0x00000020:
								set_default_display_item_id(input.readInt32());
								break;
							default:
								System.out.println(String.format("[AUBIBookInfoForClient.BookT.CategoryT.SlotT] NEW_TAG : TAG(%d)", tag));
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
				
				public static class StateT implements ProtoMessage{
					public static AUBIBookInfoForClient.BookT.CategoryT.SlotT.StateT newInstance(){
						return new AUBIBookInfoForClient.BookT.CategoryT.SlotT.StateT();
					}
					private int _craft_id;
					private int _awakening;
					private int _memorizedSerializedSize = -1;
					private byte _memorizedIsInitialized = -1;
					private int _bit;
					private StateT(){
					}
					public int get_craft_id() {
						return _craft_id;
					}
					public void set_craft_id(int val) {
						_bit |= 0x1;
						_craft_id = val;
					}
					public boolean has_craft_id() {
						return (_bit & 0x1) == 0x1;
					}
					public int get_awakening(){
						return _awakening;
					}
					public void set_awakening(int val){
						_bit |= 0x2;
						_awakening = val;
					}
					public boolean has_awakening(){
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
						if (has_craft_id()){
							size += ProtoOutputStream.computeInt32Size(1, _craft_id);
						}
						if (has_awakening()){
							size += ProtoOutputStream.computeInt32Size(2, _awakening);
						}
						_memorizedSerializedSize = size;
						return size;
					}
					@Override
					public boolean isInitialized(){
						if(_memorizedIsInitialized == 1)
							return true;
						if (!has_craft_id()){
							_memorizedIsInitialized = -1;
							return false;
						}
						if (!has_awakening()){
							_memorizedIsInitialized = -1;
							return false;
						}
						_memorizedIsInitialized = 1;
						return true;
					}
					@Override
					public void writeTo(ProtoOutputStream output) throws java.io.IOException{
						if (has_craft_id()){
							output.wirteInt32(1, _craft_id);
						}
						if (has_awakening()){
							output.wirteInt32(2, _awakening);
						}
					}
					@Override
					public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
						while(!input.isAtEnd()){
							int tag = input.readTag();
							switch(tag){
								case 0x00000008:
									set_craft_id(input.readInt32());
									break;
								case 0x00000010:
									set_awakening(input.readInt32());
									break;
								default:
									System.out.println(String.format("[AUBIBookInfoForClient.BookT.CategoryT.SlotT.StateT] NEW_TAG : TAG(%d)", tag));
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
	
}

