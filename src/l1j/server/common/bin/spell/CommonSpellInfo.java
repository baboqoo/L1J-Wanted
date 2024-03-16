package l1j.server.common.bin.spell;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;
import l1j.server.common.data.CommonBonusDescription;
import l1j.server.common.data.SpellCategory;
import l1j.server.server.utils.StringUtil;

public class CommonSpellInfo implements ProtoMessage{
	public static CommonSpellInfo newInstance(){
		return new CommonSpellInfo();
	}
	
	private int _spell_id;
	private SpellCategory _spell_category;
	private int _on_icon_id;
	private int _off_icon_id;
	private int _duration;
	private int _tooltip_str_id;
	private java.util.LinkedList<CommonBonusDescription> _spell_bonus_list;
	private CompanionSpellBuff _companion_spell_buff;
	private int _delay_group_id;
	private CommonSpellInfo.ExtractItem _extract_item;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	
	private CommonSpellInfo(){
		set_spell_category(SpellCategory.SPELL);
		set_on_icon_id(0);
		set_off_icon_id(0);
		set_duration(0);
		set_tooltip_str_id(0);
	}
	
	public CommonSpellInfo(ResultSet rs) throws SQLException {
		this(rs, false);
	}
	
	public CommonSpellInfo(ResultSet rs, boolean passive) throws SQLException {
		if (passive) {
			this._spell_id						= rs.getInt("passive_id");
			this._duration						= rs.getInt("duration");
			parseBonusList(rs.getString("spell_bonus_list"));
			this._delay_group_id				= rs.getInt("delay_group_id");
			int extract_item_name_id			= rs.getInt("extract_item_name_id");
			int extract_item_count				= rs.getInt("extract_item_count");
			if (extract_item_name_id > 0) {
				this._extract_item				= ExtractItem.newInstance();
				this._extract_item.set_name_id(extract_item_name_id);
				this._extract_item.set_count(extract_item_count);
			}
		} else {
			this._spell_id						= rs.getInt("spell_id");
			String category						= rs.getString("spell_category");
			category							= category.substring(category.indexOf("(") + 1, category.indexOf(")"));
			this._spell_category				= SpellCategory.fromInt(Integer.parseInt(category));
			this._on_icon_id					= rs.getInt("on_icon_id");
			this._off_icon_id					= rs.getInt("off_icon_id");
			this._duration						= rs.getInt("duration");
			this._tooltip_str_id				= rs.getInt("tooltip_str_id");
			parseBonusList(rs.getString("spell_bonus_list"));
			int companion_on_icon_id			= rs.getInt("companion_on_icon_id");
			int companion_off_icon_id			= rs.getInt("companion_off_icon_id");
			int companion_icon_priority			= rs.getInt("companion_icon_priority");
			int companion_tooltip_str_id		= rs.getInt("companion_tooltip_str_id");
			int companion_new_str_id			= rs.getInt("companion_new_str_id");
			int companion_end_str_id			= rs.getInt("companion_end_str_id");
			int companion_is_good				= rs.getInt("companion_is_good");
			int companion_duration_show_type	= rs.getInt("companion_duration_show_type");
			if (companion_on_icon_id >= 0 || companion_off_icon_id >= 0 || companion_icon_priority >= 0
					|| companion_tooltip_str_id >= 0 || companion_new_str_id >= 0 || companion_end_str_id >= 0
					|| companion_is_good >= 0 || companion_duration_show_type >= 0) {
				this._companion_spell_buff		= CompanionSpellBuff.newInstance();
				this._companion_spell_buff.set_on_icon_id(companion_on_icon_id);
				this._companion_spell_buff.set_off_icon_id(companion_off_icon_id);
				this._companion_spell_buff.set_icon_priority(companion_icon_priority);
				this._companion_spell_buff.set_tooltip_str_id(companion_tooltip_str_id);
				this._companion_spell_buff.set_new_str_id(companion_new_str_id);
				this._companion_spell_buff.set_end_str_id(companion_end_str_id);
				this._companion_spell_buff.set_is_good(companion_is_good);
				this._companion_spell_buff.set_duration_show_type(companion_duration_show_type);
			}
			this._delay_group_id				= rs.getInt("delay_group_id");
			int extract_item_name_id			= rs.getInt("extract_item_name_id");
			int extract_item_count				= rs.getInt("extract_item_count");
			if (extract_item_name_id > 0) {
				this._extract_item				= ExtractItem.newInstance();
				this._extract_item.set_name_id(extract_item_name_id);
				this._extract_item.set_count(extract_item_count);
			}
		}
	}
	
	private void parseBonusList(String str) {
		if (StringUtil.isNullOrEmpty(str)) {
			return;
		}
		CommonBonusDescription bonus = null;
		StringTokenizer st = new StringTokenizer(str, StringUtil.LineString);
		while (st.hasMoreTokens()) {
			String[] array = st.nextToken().split(StringUtil.CommaString);
			if (array.length != 4) {
				continue;
			}
			bonus = CommonBonusDescription.newInstance();
			bonus.set_name(array[0].replace("name: ", StringUtil.EmptyString).trim());
			bonus.set_enum_desc(Integer.parseInt(array[1].replace("enum_desc: ", StringUtil.EmptyString).trim()));
			String values = array[2].replace("value: ", StringUtil.EmptyString).trim();
			if (!StringUtil.isNullOrEmpty(values)) {
				String[] valueArray = values.split("|");
				for (String val : valueArray) {
					bonus.add_value(val);
				}
			}
			bonus.set_name(array[3].replace("user_Level: ", StringUtil.EmptyString).trim());
			this.add_spell_bonus_list(bonus);
		}
	}

	public int get_spell_id(){
		return _spell_id;
	}
	public void set_spell_id(int val){
		_bit |= 0x1;
		_spell_id = val;
	}
	public boolean has_spell_id(){
		return (_bit & 0x1) == 0x1;
	}
	public SpellCategory get_spell_category(){
		return _spell_category;
	}
	public void set_spell_category(SpellCategory val){
		_bit |= 0x2;
		_spell_category = val;
	}
	public boolean has_spell_category(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_on_icon_id(){
		return _on_icon_id;
	}
	public void set_on_icon_id(int val){
		_bit |= 0x4;
		_on_icon_id = val;
	}
	public boolean has_on_icon_id(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_off_icon_id(){
		return _off_icon_id;
	}
	public void set_off_icon_id(int val){
		_bit |= 0x8;
		_off_icon_id = val;
	}
	public boolean has_off_icon_id(){
		return (_bit & 0x8) == 0x8;
	}
	public int get_duration(){
		return _duration;
	}
	public void set_duration(int val){
		_bit |= 0x20;
		_duration = val;
	}
	public boolean has_duration(){
		return (_bit & 0x20) == 0x20;
	}
	public int get_tooltip_str_id(){
		return _tooltip_str_id;
	}
	public void set_tooltip_str_id(int val){
		_bit |= 0x40;
		_tooltip_str_id = val;
	}
	public boolean has_tooltip_str_id(){
		return (_bit & 0x40) == 0x40;
	}
	public java.util.LinkedList<CommonBonusDescription> get_spell_bonus_list(){
		return _spell_bonus_list;
	}
	public void add_spell_bonus_list(CommonBonusDescription val){
		if(!has_spell_bonus_list()){
			_spell_bonus_list = new java.util.LinkedList<CommonBonusDescription>();
			_bit |= 0x80;
		}
		_spell_bonus_list.add(val);
	}
	public String get_spell_bonus_list_toString() {
		if (_spell_bonus_list == null || _spell_bonus_list.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (CommonBonusDescription bonus : _spell_bonus_list) {
			if (sb.length() > 0) {
				sb.append(StringUtil.LineString);
			}
			sb.append("name: ").append(bonus.get_name());
			sb.append(", enum_desc: ").append(bonus.get_enum_desc());
			sb.append(", value: ");
			java.util.LinkedList<String> value = bonus.get_value();
			if (value != null && !value.isEmpty()) {
				int valCnt = 0;
				for (String val : value) {
					if (valCnt > 0) {
						sb.append("|");
					}
					sb.append(val);
					valCnt++;
				}
			}
			sb.append(", user_Level: ").append(bonus.get_user_Level());
		}
		return sb.toString();
	}
	public boolean has_spell_bonus_list(){
		return (_bit & 0x80) == 0x80;
	}
	public CompanionSpellBuff get_companion_spell_buff(){
		return _companion_spell_buff;
	}
	public void set_companion_spell_buff(CompanionSpellBuff val){
		_bit |= 0x100;
		_companion_spell_buff = val;
	}
	public boolean has_companion_spell_buff(){
		return (_bit & 0x100) == 0x100;
	}
	public int get_delay_group_id(){
		return _delay_group_id;
	}
	public void set_delay_group_id(int val){
		_bit |= 0x200;
		_delay_group_id = val;
	}
	public boolean has_delay_group_id(){
		return (_bit & 0x200) == 0x200;
	}
	public CommonSpellInfo.ExtractItem get_extract_item(){
		return _extract_item;
	}
	public void set_extract_item(CommonSpellInfo.ExtractItem val){
		_bit |= 0x400;
		_extract_item = val;
	}
	public boolean has_extract_item(){
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
		if (has_spell_id()){
			size += ProtoOutputStream.computeUInt32Size(1, _spell_id);
		}
		if (has_spell_category()){
			size += ProtoOutputStream.computeEnumSize(2, _spell_category.toInt());
		}
		if (has_on_icon_id()){
			size += ProtoOutputStream.computeInt32Size(3, _on_icon_id);
		}
		if (has_off_icon_id()){
			size += ProtoOutputStream.computeInt32Size(4, _off_icon_id);
		}
		if (has_duration()){
			size += ProtoOutputStream.computeInt32Size(6, _duration);
		}
		if (has_tooltip_str_id()){
			size += ProtoOutputStream.computeUInt32Size(7, _tooltip_str_id);
		}
		if (has_spell_bonus_list()){
			for(CommonBonusDescription val : _spell_bonus_list){
				size += ProtoOutputStream.computeMessageSize(8, val);
			}
		}
		if (has_companion_spell_buff()){
			size += ProtoOutputStream.computeMessageSize(9, _companion_spell_buff);
		}
		if (has_delay_group_id()){
			size += ProtoOutputStream.computeInt32Size(10, _delay_group_id);
		}
		if (has_extract_item()){
			size += ProtoOutputStream.computeMessageSize(11, _extract_item);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_spell_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_spell_bonus_list()){
			for(CommonBonusDescription val : _spell_bonus_list){
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
		if (has_spell_id()){
			output.writeUInt32(1, _spell_id);
		}
		if (has_spell_category()){
			output.writeEnum(2, _spell_category.toInt());
		}
		if (has_on_icon_id()){
			output.wirteInt32(3, _on_icon_id);
		}
		if (has_off_icon_id()){
			output.wirteInt32(4, _off_icon_id);
		}
		if (has_duration()){
			output.wirteInt32(6, _duration);
		}
		if (has_tooltip_str_id()){
			output.writeUInt32(7, _tooltip_str_id);
		}
		if (has_spell_bonus_list()){
			for (CommonBonusDescription val : _spell_bonus_list){
				output.writeMessage(8, val);
			}
		}
		if (has_companion_spell_buff()){
			output.writeMessage(9, _companion_spell_buff);
		}
		if (has_delay_group_id()){
			output.wirteInt32(10, _delay_group_id);
		}
		if (has_extract_item()){
			output.writeMessage(11, _extract_item);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000008:{
					set_spell_id(input.readUInt32());
					break;
				}
				case 0x00000010:{
					set_spell_category(SpellCategory.fromInt(input.readEnum()));
					break;
				}
				case 0x00000018:{
					set_on_icon_id(input.readInt32());
					break;
				}
				case 0x00000020:{
					set_off_icon_id(input.readInt32());
					break;
				}
				case 0x00000030:{
					set_duration(input.readInt32());
					break;
				}
				case 0x00000038:{
					set_tooltip_str_id(input.readUInt32());
					break;
				}
				case 0x00000042:{
					add_spell_bonus_list((CommonBonusDescription)input.readMessage(CommonBonusDescription.newInstance()));
					break;
				}
				case 0x0000004A:{
					set_companion_spell_buff((CompanionSpellBuff)input.readMessage(CompanionSpellBuff.newInstance()));
					break;
				}
				case 0x00000050:{
					set_delay_group_id(input.readInt32());
					break;
				}
				case 0x0000005A:{
					set_extract_item((CommonSpellInfo.ExtractItem)input.readMessage(CommonSpellInfo.ExtractItem.newInstance()));
					break;
				}
				default:{
					System.out.println(String.format("[CommonSpellInfo] NEW_TAG : TAG(%d)", tag));
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
	public static class ExtractItem implements ProtoMessage{
		public static ExtractItem newInstance(){
			return new ExtractItem();
		}
		private int _name_id;
		private int _count;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private ExtractItem(){
		}
		public int get_name_id(){
			return _name_id;
		}
		public void set_name_id(int val){
			_bit |= 0x1;
			_name_id = val;
		}
		public boolean has_name_id(){
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
			if (has_name_id()){
				size += ProtoOutputStream.computeInt32Size(1, _name_id);
			}
			if (has_count()){
				size += ProtoOutputStream.computeInt32Size(2, _count);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_name_id()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_count()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_name_id()){
				output.wirteInt32(1, _name_id);
			}
			if (has_count()){
				output.wirteInt32(2, _count);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_name_id(input.readInt32());
						break;
					}
					case 0x00000010:{
						set_count(input.readInt32());
						break;
					}
					default:{
						System.out.println(String.format("[CommonSpellInfo.ExtractItem] NEW_TAG : TAG(%d)", tag));
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

