package l1j.server.common.bin.npc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

import l1j.server.common.DescKLoader;
import l1j.server.common.bin.ItemCommonBinLoader;
import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;
import l1j.server.common.bin.item.CommonItemInfo;
import l1j.server.common.data.ElementalResistance;
import l1j.server.server.utils.StringUtil;

public class CommonNPCInfo implements ProtoMessage {
	public static CommonNPCInfo newInstance() {
		return new CommonNPCInfo();
	}
	
	private CommonNPCInfo() {
	}
	
	public CommonNPCInfo(ResultSet rs) throws SQLException {
		this._class_id				= rs.getInt("class_id");
		this._sprite_id				= rs.getInt("sprite_id");
		this._desc					= rs.getString("desc_id");
		this._level					= rs.getInt("level");
		this._ac					= rs.getInt("ac");
		this._hp					= rs.getInt("hp");
		this._mp					= rs.getInt("mp");
		this._dex					= rs.getInt("dex");
		this._str					= rs.getInt("str");
		this._int					= rs.getInt("inti");
		this._wis					= rs.getInt("wis");
		this._con					= rs.getInt("con");
		this._cha					= rs.getInt("cha");
		this._mr					= rs.getInt("mr");
		this._magic_level			= rs.getInt("magic_level");
		this._magic_bonus			= rs.getInt("magic_bonus");
		this._magic_evasion			= rs.getInt("magic_evasion");
		ElementalResistance elemental_resistance = ElementalResistance.newInstance();
		elemental_resistance.set_fire(rs.getInt("resistance_fire"));
		elemental_resistance.set_water(rs.getInt("resistance_water"));
		elemental_resistance.set_air(rs.getInt("resistance_air"));
		elemental_resistance.set_earth(rs.getInt("resistance_earth"));
		this._elemental_resistance	= elemental_resistance;
		this._alignment				= rs.getInt("alignment");
		this._big					= Boolean.valueOf(rs.getString("big"));
		parseDropItems(rs.getString("drop_items"));
		String tendency				= rs.getString("tendency");
		tendency					= tendency.substring(tendency.indexOf("(") + 1, tendency.indexOf(")"));
		this._tendency				= CommonNPCInfo.eTendency.fromInt(Integer.parseInt(tendency));
		this._category				= rs.getInt("category");
		this._is_bossmonster		= Boolean.valueOf(rs.getString("is_bossmonster"));
		this._can_turnundead		= Boolean.valueOf(rs.getString("can_turnundead"));
	}

	private void parseDropItems(String str) {
		if (StringUtil.isNullOrEmpty(str)) {
			return;
		}
		str = str.replaceAll("name_id:", StringUtil.EmptyString).replaceAll("bless:", StringUtil.EmptyString);
		StringTokenizer st = new StringTokenizer(str, StringUtil.LineString);
		while (st.hasMoreTokens()) {
			String[] array = st.nextToken().split(StringUtil.CommaString);
			if (array.length != 2) {
				continue;
			}
			int name_id = Integer.parseInt(array[0].trim());
			int bless	= Integer.parseInt(array[1].trim());
			DropItemT drop = DropItemT.newInstance();
			drop.set_name_id(name_id);
			drop.set_bless(bless);
			add_drop_items(drop);
		}
	}

	private int _class_id;
	private int _sprite_id;
	private String _desc;
	private int _level;
	private int _ac;
	private int _hp;
	private int _mp;
	private int _dex;
	private int _str;
	private int _int;
	private int _wis;
	private int _con;
	private int _cha;
	private int _mr;
	private int _magic_level;
	private int _magic_bonus;
	private int _magic_evasion;
	private ElementalResistance _elemental_resistance;
	private int _alignment;
	private boolean _big;
	private java.util.LinkedList<DropItemT> _drop_items;
	private CommonNPCInfo.eTendency _tendency;
	private int _category;
	private boolean _is_bossmonster;
	private boolean _can_turnundead;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	
	public int get_class_id(){
		return _class_id;
	}
	public void set_class_id(int val){
		_bit |= 0x1;
		_class_id = val;
	}
	public boolean has_class_id(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_sprite_id(){
		return _sprite_id;
	}
	public void set_sprite_id(int val){
		_bit |= 0x2;
		_sprite_id = val;
	}
	public boolean has_sprite_id(){
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
	public int get_level(){
		return _level;
	}
	public void set_level(int val){
		_bit |= 0x8;
		_level = val;
	}
	public boolean has_level(){
		return (_bit & 0x8) == 0x8;
	}
	public int get_ac(){
		return _ac;
	}
	public void set_ac(int val){
		_bit |= 0x10;
		_ac = val;
	}
	public boolean has_ac(){
		return (_bit & 0x10) == 0x10;
	}
	public int get_hp(){
		return _hp;
	}
	public void set_hp(int val){
		_bit |= 0x20;
		_hp = val;
	}
	public boolean has_hp(){
		return (_bit & 0x20) == 0x20;
	}
	public int get_mp(){
		return _mp;
	}
	public void set_mp(int val){
		_bit |= 0x40;
		_mp = val;
	}
	public boolean has_mp(){
		return (_bit & 0x40) == 0x40;
	}
	public int get_dex(){
		return _dex;
	}
	public void set_dex(int val){
		_bit |= 0x80;
		_dex = val;
	}
	public boolean has_dex(){
		return (_bit & 0x80) == 0x80;
	}
	public int get_str(){
		return _str;
	}
	public void set_str(int val){
		_bit |= 0x100;
		_str = val;
	}
	public boolean has_str(){
		return (_bit & 0x100) == 0x100;
	}
	public int get_int(){
		return _int;
	}
	public void set_int(int val){
		_bit |= 0x200;
		_int = val;
	}
	public boolean has_int(){
		return (_bit & 0x200) == 0x200;
	}
	public int get_wis(){
		return _wis;
	}
	public void set_wis(int val){
		_bit |= 0x400;
		_wis = val;
	}
	public boolean has_wis(){
		return (_bit & 0x400) == 0x400;
	}
	public int get_con(){
		return _con;
	}
	public void set_con(int val){
		_bit |= 0x800;
		_con = val;
	}
	public boolean has_con(){
		return (_bit & 0x800) == 0x800;
	}
	public int get_cha(){
		return _cha;
	}
	public void set_cha(int val){
		_bit |= 0x1000;
		_cha = val;
	}
	public boolean has_cha(){
		return (_bit & 0x1000) == 0x1000;
	}
	public int get_mr(){
		return _mr;
	}
	public void set_mr(int val){
		_bit |= 0x2000;
		_mr = val;
	}
	public boolean has_mr(){
		return (_bit & 0x2000) == 0x2000;
	}
	public int get_magic_level(){
		return _magic_level;
	}
	public void set_magic_level(int val){
		_bit |= 0x4000;
		_magic_level = val;
	}
	public boolean has_magic_level(){
		return (_bit & 0x4000) == 0x4000;
	}
	public int get_magic_bonus(){
		return _magic_bonus;
	}
	public void set_magic_bonus(int val){
		_bit |= 0x8000;
		_magic_bonus = val;
	}
	public boolean has_magic_bonus(){
		return (_bit & 0x8000) == 0x8000;
	}
	public int get_magic_evasion(){
		return _magic_evasion;
	}
	public void set_magic_evasion(int val){
		_bit |= 0x10000;
		_magic_evasion = val;
	}
	public boolean has_magic_evasion(){
		return (_bit & 0x10000) == 0x10000;
	}
	public ElementalResistance get_elemental_resistance(){
		return _elemental_resistance;
	}
	public void set_elemental_resistance(ElementalResistance val){
		_bit |= 0x20000;
		_elemental_resistance = val;
	}
	public boolean has_elemental_resistance(){
		return (_bit & 0x20000) == 0x20000;
	}
	public int get_alignment(){
		return _alignment;
	}
	public void set_alignment(int val){
		_bit |= 0x40000;
		_alignment = val;
	}
	public boolean has_alignment(){
		return (_bit & 0x40000) == 0x40000;
	}
	public boolean get_big(){
		return _big;
	}
	public void set_big(boolean val){
		_bit |= 0x80000;
		_big = val;
	}
	public boolean has_big(){
		return (_bit & 0x80000) == 0x80000;
	}
	public java.util.LinkedList<DropItemT> get_drop_items(){
		return _drop_items;
	}
	public void add_drop_items(DropItemT val){
		if(!has_drop_items()){
			_drop_items = new java.util.LinkedList<DropItemT>();
			_bit |= 0x100000;
		}
		_drop_items.add(val);
	}
	public boolean has_drop_items(){
		return (_bit & 0x100000) == 0x100000;
	}
	public String get_drop_items_toString(){
		if (_drop_items == null || _drop_items.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (DropItemT drop : _drop_items) {
			if (sb.length() > 0) {
				sb.append(StringUtil.LineString);
			}
			sb.append("name_id: ").append(drop.get_name_id());
			if (drop.get_name_id() > 0) {
				CommonItemInfo item_info = ItemCommonBinLoader.getCommonInfo(drop.get_name_id());
				sb.append(", desc_kr: ").append(item_info != null ? DescKLoader.getDesc(item_info.get_desc()) : "UNKNOWN");
			}
			sb.append(", bless: ").append(drop.get_bless());
		}
		return sb.toString();
	}
	
	public CommonNPCInfo.eTendency get_tendency(){
		return _tendency;
	}
	public void set_tendency(CommonNPCInfo.eTendency val){
		_bit |= 0x200000;
		_tendency = val;
	}
	public boolean has_tendency(){
		return (_bit & 0x200000) == 0x200000;
	}
	public int get_category(){
		return _category;
	}
	public void set_category(int val){
		_bit |= 0x400000;
		_category = val;
	}
	public boolean has_category(){
		return (_bit & 0x400000) == 0x400000;
	}
	public boolean get_is_bossmonster(){
		return _is_bossmonster;
	}
	public void set_is_bossmonster(boolean val){
		_bit |= 0x800000;
		_is_bossmonster = val;
	}
	public boolean has_is_bossmonster(){
		return (_bit & 0x800000) == 0x800000;
	}
	public boolean get_can_turnundead(){
		return _can_turnundead;
	}
	public void set_can_turnundead(boolean val){
		_bit |= 0x1000000;
		_can_turnundead = val;
	}
	public boolean has_can_turnundead(){
		return (_bit & 0x1000000) == 0x1000000;
	}
	
	@Override
	public long getInitializeBit() {
		return (long) _bit;
	}
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;
	}
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_class_id()){
			size += ProtoOutputStream.computeUInt32Size(1, _class_id);
		}
		if (has_sprite_id()){
			size += ProtoOutputStream.computeUInt32Size(2, _sprite_id);
		}
		if (has_desc()){
			size += ProtoOutputStream.computeStringSize(3, _desc);
		}
		if (has_level()){
			size += ProtoOutputStream.computeInt32Size(4, _level);
		}
		if (has_ac()){
			size += ProtoOutputStream.computeInt32Size(5, _ac);
		}
		if (has_hp()){
			size += ProtoOutputStream.computeInt32Size(6, _hp);
		}
		if (has_mp()){
			size += ProtoOutputStream.computeInt32Size(7, _mp);
		}
		if (has_dex()){
			size += ProtoOutputStream.computeUInt32Size(8, _dex);
		}
		if (has_str()){
			size += ProtoOutputStream.computeUInt32Size(9, _str);
		}
		if (has_int()){
			size += ProtoOutputStream.computeUInt32Size(10, _int);
		}
		if (has_wis()){
			size += ProtoOutputStream.computeUInt32Size(11, _wis);
		}
		if (has_con()){
			size += ProtoOutputStream.computeUInt32Size(12, _con);
		}
		if (has_cha()){
			size += ProtoOutputStream.computeUInt32Size(13, _cha);
		}
		if (has_mr()){
			size += ProtoOutputStream.computeInt32Size(14, _mr);
		}
		if (has_magic_level()){
			size += ProtoOutputStream.computeInt32Size(15, _magic_level);
		}
		if (has_magic_bonus()){
			size += ProtoOutputStream.computeInt32Size(16, _magic_bonus);
		}
		if (has_magic_evasion()){
			size += ProtoOutputStream.computeInt32Size(17, _magic_evasion);
		}
		if (has_elemental_resistance()){
			size += ProtoOutputStream.computeMessageSize(18, _elemental_resistance);
		}
		if (has_alignment()){
			size += ProtoOutputStream.computeInt32Size(19, _alignment);
		}
		if (has_big()){
			size += ProtoOutputStream.computeBoolSize(20, _big);
		}
		if (has_drop_items()){
			for(DropItemT val : _drop_items){
				size += ProtoOutputStream.computeMessageSize(21, val);
			}
		}
		if (has_tendency()){
			size += ProtoOutputStream.computeEnumSize(22, _tendency.toInt());
		}
		if (has_category()){
			size += ProtoOutputStream.computeInt32Size(23, _category);
		}
		if (has_is_bossmonster()){
			size += ProtoOutputStream.computeBoolSize(24, _is_bossmonster);
		}
		if (has_can_turnundead()){
			size += ProtoOutputStream.computeBoolSize(25, _can_turnundead);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_class_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_drop_items()){
			for(DropItemT val : _drop_items){
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
		if (has_class_id()){
			output.writeUInt32(1, _class_id);
		}
		if (has_sprite_id()){
			output.writeUInt32(2, _sprite_id);
		}
		if (has_desc()){
			output.writeString(3, _desc);
		}
		if (has_level()){
			output.wirteInt32(4, _level);
		}
		if (has_ac()){
			output.wirteInt32(5, _ac);
		}
		if (has_hp()){
			output.wirteInt32(6, _hp);
		}
		if (has_mp()){
			output.wirteInt32(7, _mp);
		}
		if (has_dex()){
			output.writeUInt32(8, _dex);
		}
		if (has_str()){
			output.writeUInt32(9, _str);
		}
		if (has_int()){
			output.writeUInt32(10, _int);
		}
		if (has_wis()){
			output.writeUInt32(11, _wis);
		}
		if (has_con()){
			output.writeUInt32(12, _con);
		}
		if (has_cha()){
			output.writeUInt32(13, _cha);
		}
		if (has_mr()){
			output.wirteInt32(14, _mr);
		}
		if (has_magic_level()){
			output.wirteInt32(15, _magic_level);
		}
		if (has_magic_bonus()){
			output.wirteInt32(16, _magic_bonus);
		}
		if (has_magic_evasion()){
			output.wirteInt32(17, _magic_evasion);
		}
		if (has_elemental_resistance()){
			output.writeMessage(18, _elemental_resistance);
		}
		if (has_alignment()){
			output.wirteInt32(19, _alignment);
		}
		if (has_big()){
			output.writeBool(20, _big);
		}
		if (has_drop_items()){
			for (DropItemT val : _drop_items){
				output.writeMessage(21, val);
			}
		}
		if (has_tendency()){
			output.writeEnum(22, _tendency.toInt());
		}
		if (has_category()){
			output.wirteInt32(23, _category);
		}
		if (has_is_bossmonster()){
			output.writeBool(24, _is_bossmonster);
		}
		if (has_can_turnundead()){
			output.writeBool(25, _can_turnundead);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000008:{
					set_class_id(input.readUInt32());
					break;
				}
				case 0x00000010:{
					set_sprite_id(input.readUInt32());
					break;
				}
				case 0x0000001A:{
					set_desc(input.readString());
					break;
				}
				case 0x00000020:{
					set_level(input.readInt32());
					break;
				}
				case 0x00000028:{
					set_ac(input.readInt32());
					break;
				}
				case 0x00000030:{
					set_hp(input.readInt32());
					break;
				}
				case 0x00000038:{
					set_mp(input.readInt32());
					break;
				}
				case 0x00000040:{
					set_dex(input.readUInt32());
					break;
				}
				case 0x00000048:{
					set_str(input.readUInt32());
					break;
				}
				case 0x00000050:{
					set_int(input.readUInt32());
					break;
				}
				case 0x00000058:{
					set_wis(input.readUInt32());
					break;
				}
				case 0x00000060:{
					set_con(input.readUInt32());
					break;
				}
				case 0x00000068:{
					set_cha(input.readUInt32());
					break;
				}
				case 0x00000070:{
					set_mr(input.readInt32());
					break;
				}
				case 0x00000078:{
					set_magic_level(input.readInt32());
					break;
				}
				case 0x00000080:{
					set_magic_bonus(input.readInt32());
					break;
				}
				case 0x00000088:{
					set_magic_evasion(input.readInt32());
					break;
				}
				case 0x00000092:{
					set_elemental_resistance((ElementalResistance)input.readMessage(ElementalResistance.newInstance()));
					break;
				}
				case 0x00000098:{
					set_alignment(input.readInt32());
					break;
				}
				case 0x000000A0:{
					set_big(input.readBool());
					break;
				}
				case 0x000000AA:{
					add_drop_items((DropItemT)input.readMessage(DropItemT.newInstance()));
					break;
				}
				case 0x000000B0:{
					set_tendency(CommonNPCInfo.eTendency.fromInt(input.readEnum()));
					break;
				}
				case 0x000000B8:{
					set_category(input.readInt32());
					break;
				}
				case 0x000000C0:{
					set_is_bossmonster(input.readBool());
					break;
				}
				case 0x000000C8:{
					set_can_turnundead(input.readBool());
					break;
				}
				default:{
					System.out.println(String.format("[CommonNPCInfo] NEW_TAG : TAG(%d)", tag));
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
	
	public enum eTendency{
		NEUTRAL(0),
		PASSIVE(1),
		AGGRESSIVE(2),
		;
		private int value;
		eTendency(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eTendency v){
			return value == v.value;
		}
		public static eTendency fromInt(int i){
			switch(i){
			case 0:
				return NEUTRAL;
			case 1:
				return PASSIVE;
			case 2:
				return AGGRESSIVE;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eTendency, %d", i));
			}
		}
	}
}

