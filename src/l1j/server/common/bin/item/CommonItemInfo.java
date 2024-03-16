package l1j.server.common.bin.item;

import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;
import l1j.server.common.data.BodyPart;
import l1j.server.common.data.CharacterClass;
import l1j.server.common.data.CommonBonusDescription;
import l1j.server.common.data.ExtendedWeaponType;
import l1j.server.common.data.Material;
import l1j.server.server.utils.StringUtil;

public class CommonItemInfo implements ProtoMessage{
	public static CommonItemInfo newInstance(){
		return new CommonItemInfo();
	}
	
	private int _name_id;
	private int _icon_id;
	private int _sprite_id;
	private String _desc;
	private String _real_desc;
	private Material _material;
	private int _weight_1000ea;
	private int _level_limit_min;
	private int _level_limit_max;
	private java.util.LinkedList<CharacterClass> _class_permit;
	private java.util.LinkedList<CommonBonusDescription> _equip_bonus_list;
	private int _interaction_type;
	private int _real_weight;
	private int _spell_range;
	private CommonItemInfo.eItemCategory _item_category;
	private BodyPart _body_part;
	private int _ac;
	private ExtendedWeaponType _extended_weapon_type;
	private int _large_damage;
	private int _small_damage;
	private int _hit_bonus;
	private int _damage_bonus;
	private CommonItemInfo.ArmorSeriesInfo _armor_series_info;
	private int _cost;
	private boolean _can_set_mage_enchant;
	private boolean _merge;
	private boolean _pss_event_item;
	private boolean _market_searching_item;
	private java.util.LinkedList<BmItemProbData> _lucky_bag_reward_list;
	private java.util.LinkedList<CommonItemInfo.EnchantProbData> _enchant_prob_list;
	private int _element_enchant_table;
	private int _accessory_enchant_table;
	private int _bm_prob_open;
	private int _enchant_type;
	private boolean _is_elven;
	private int _forced_elemental_enchant;
	private int _max_enchant;
	private boolean _energy_lost;
	private int _prob;
	private boolean _pss_heal_item;
	private long _useInterval;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private long _bit;
	private CommonItemInfo(){
	}
	
	public CommonItemInfo(ResultSet rs) throws SQLException {
		this._name_id					= rs.getInt("name_id");
		this._icon_id					= rs.getInt("icon_id");
		this._sprite_id					= rs.getInt("sprite_id");
		this._desc						= rs.getString("desc_id");
		this._real_desc					= rs.getString("real_desc");
		String material					= rs.getString("material");
		material						= material.substring(material.indexOf("(") + 1, material.indexOf(")"));
		this._material					= Material.fromInt(Integer.parseInt(material));
		this._weight_1000ea				= rs.getInt("weight_1000ea");
		this._level_limit_min			= rs.getInt("level_limit_min");
		this._level_limit_max			= rs.getInt("level_limit_max");
		boolean prince_permit			= Boolean.valueOf(rs.getString("prince_permit"));
		boolean knight_permit			= Boolean.valueOf(rs.getString("knight_permit"));
		boolean elf_permit				= Boolean.valueOf(rs.getString("elf_permit"));
		boolean magician_permit			= Boolean.valueOf(rs.getString("magician_permit"));
		boolean darkelf_permit			= Boolean.valueOf(rs.getString("darkelf_permit"));
		boolean dragonknight_permit		= Boolean.valueOf(rs.getString("dragonknight_permit"));
		boolean illusionist_permit		= Boolean.valueOf(rs.getString("illusionist_permit"));
		boolean warrior_permit			= Boolean.valueOf(rs.getString("warrior_permit"));
		boolean fencer_permit			= Boolean.valueOf(rs.getString("fencer_permit"));
		boolean lancer_permit			= Boolean.valueOf(rs.getString("lancer_permit"));
		if (prince_permit) {
			add_class_permit(CharacterClass.PRINCE);
		}
		if (knight_permit) {
			add_class_permit(CharacterClass.KNIGHT);
		}
		if (elf_permit) {
			add_class_permit(CharacterClass.ELF);
		}
		if (magician_permit) {
			add_class_permit(CharacterClass.MAGICIAN);
		}
		if (darkelf_permit) {
			add_class_permit(CharacterClass.DARKELF);
		}
		if (dragonknight_permit) {
			add_class_permit(CharacterClass.DRAGON_KNIGHT);
		}
		if (illusionist_permit) {
			add_class_permit(CharacterClass.ILLUSIONIST);
		}
		if (warrior_permit) {
			add_class_permit(CharacterClass.WARRIOR);
		}
		if (fencer_permit) {
			add_class_permit(CharacterClass.FENCER);
		}
		if (lancer_permit) {
			add_class_permit(CharacterClass.LANCER);
		}
		
		this._interaction_type			= rs.getInt("interaction_type");
		this._real_weight				= rs.getInt("real_weight");
		this._spell_range				= rs.getInt("spell_range");
		String category					= rs.getString("item_category");
		category						= category.substring(category.indexOf("(") + 1, category.indexOf(")"));
		this._item_category				= eItemCategory.fromInt(Integer.parseInt(category));
		String bodyPart					= rs.getString("body_part");
		bodyPart						= bodyPart.substring(bodyPart.indexOf("(") + 1, bodyPart.indexOf(")"));
		this._body_part					= BodyPart.fromInt(Integer.parseInt(bodyPart));
		this._ac						= rs.getInt("ac");
		String weaponType				= rs.getString("extended_weapon_type");
		weaponType						= weaponType.substring(weaponType.indexOf("(") + 1, weaponType.indexOf(")"));
		this._extended_weapon_type		= ExtendedWeaponType.fromInt(Integer.parseInt(weaponType));
		this._large_damage				= rs.getInt("large_damage");
		this._small_damage				= rs.getInt("small_damage");
		this._hit_bonus					= rs.getInt("hit_bonus");
		this._damage_bonus				= rs.getInt("damage_bonus");
		this._cost						= rs.getInt("cost");
		this._can_set_mage_enchant		= Boolean.valueOf(rs.getString("can_set_mage_enchant"));
		this._merge						= Boolean.valueOf(rs.getString("merge"));
		this._pss_event_item			= Boolean.valueOf(rs.getString("pss_event_item"));
		this._market_searching_item		= Boolean.valueOf(rs.getString("market_searching_item"));
		this._element_enchant_table		= rs.getInt("element_enchant_table");
		this._accessory_enchant_table	= rs.getInt("accessory_enchant_table");
		this._bm_prob_open				= rs.getInt("bm_prob_open");
		this._enchant_type				= rs.getInt("enchant_type");
		this._is_elven					= Boolean.valueOf(rs.getString("is_elven"));
		this._forced_elemental_enchant	= rs.getInt("forced_elemental_enchant");
		this._max_enchant				= rs.getInt("max_enchant");
		this._energy_lost				= Boolean.valueOf(rs.getString("energy_lost"));
		this._prob						= rs.getInt("prob");
		this._pss_heal_item				= Boolean.valueOf(rs.getString("pss_heal_item"));
		this._useInterval				= rs.getLong("useInterval");
	}

	public int get_name_id(){
		return _name_id;
	}
	public void set_name_id(int val){
		_bit |= 0x1L;
		_name_id = val;
	}
	public boolean has_name_id(){
		return (_bit & 0x1L) == 0x1L;
	}
	public int get_icon_id(){
		return _icon_id;
	}
	public void set_icon_id(int val){
		_bit |= 0x2L;
		_icon_id = val;
	}
	public boolean has_icon_id(){
		return (_bit & 0x2L) == 0x2L;
	}
	public int get_sprite_id(){
		return _sprite_id;
	}
	public void set_sprite_id(int val){
		_bit |= 0x4L;
		_sprite_id = val;
	}
	public boolean has_sprite_id(){
		return (_bit & 0x4L) == 0x4L;
	}
	public String get_desc(){
		return _desc;
	}
	public void set_desc(String val){
		_bit |= 0x8L;
		_desc = val;
	}
	public boolean has_desc(){
		return (_bit & 0x8L) == 0x8L;
	}
	public String get_real_desc(){
		return _real_desc;
	}
	public void set_real_desc(String val){
		_bit |= 0x10L;
		_real_desc = val;
	}
	public boolean has_real_desc(){
		return (_bit & 0x10L) == 0x10L;
	}
	public Material get_material(){
		return _material;
	}
	public void set_material(Material val){
		_bit |= 0x20L;
		_material = val;
	}
	public boolean has_material(){
		return (_bit & 0x20L) == 0x20L;
	}
	public int get_weight_1000ea(){
		return _weight_1000ea;
	}
	public void set_weight_1000ea(int val){
		_bit |= 0x40L;
		_weight_1000ea = val;
	}
	public boolean has_weight_1000ea(){
		return (_bit & 0x40L) == 0x40L;
	}
	public int get_level_limit_min(){
		return _level_limit_min;
	}
	public void set_level_limit_min(int val){
		_bit |= 0x80L;
		_level_limit_min = val;
	}
	public boolean has_level_limit_min(){
		return (_bit & 0x80L) == 0x80L;
	}
	public int get_level_limit_max(){
		return _level_limit_max;
	}
	public void set_level_limit_max(int val){
		_bit |= 0x100L;
		_level_limit_max = val;
	}
	public boolean has_level_limit_max(){
		return (_bit & 0x100L) == 0x100L;
	}
	public java.util.LinkedList<CharacterClass> get_class_permit(){
		return _class_permit;
	}
	public void add_class_permit(CharacterClass val){
		if(!has_class_permit()){
			_class_permit = new java.util.LinkedList<CharacterClass>();
			_bit |= 0x200L;
		}
		_class_permit.add(val);
	}
	public boolean has_class_permit(){
		return (_bit & 0x200L) == 0x200L;
	}
	public java.util.LinkedList<CommonBonusDescription> get_equip_bonus_list(){
		return _equip_bonus_list;
	}
	public void add_equip_bonus_list(CommonBonusDescription val){
		if(!has_equip_bonus_list()){
			_equip_bonus_list = new java.util.LinkedList<CommonBonusDescription>();
			_bit |= 0x400L;
		}
		_equip_bonus_list.add(val);
	}
	public String get_equip_bonus_list_toString() {
		if (_equip_bonus_list == null || _equip_bonus_list.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (CommonBonusDescription bonus : _equip_bonus_list) {
			if (sb.length() > 0) {
				sb.append(StringUtil.LineString);
			}
			sb.append("name: ").append(bonus.get_name()).append(", enum_desc: ").append(bonus.get_enum_desc());
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
	public boolean has_equip_bonus_list(){
		return (_bit & 0x400L) == 0x400L;
	}
	public int get_interaction_type(){
		return _interaction_type;
	}
	public void set_interaction_type(int val){
		_bit |= 0x800L;
		_interaction_type = val;
	}
	public boolean has_interaction_type(){
		return (_bit & 0x800L) == 0x800L;
	}
	public int get_real_weight(){
		return _real_weight;
	}
	public void set_real_weight(int val){
		_bit |= 0x1000L;
		_real_weight = val;
	}
	public boolean has_real_weight(){
		return (_bit & 0x1000L) == 0x1000L;
	}
	public int get_spell_range(){
		return _spell_range;
	}
	public void set_spell_range(int val){
		_bit |= 0x2000L;
		_spell_range = val;
	}
	public boolean has_spell_range(){
		return (_bit & 0x2000L) == 0x2000L;
	}
	public CommonItemInfo.eItemCategory get_item_category(){
		return _item_category;
	}
	public void set_item_category(CommonItemInfo.eItemCategory val){
		_bit |= 0x40000L;
		_item_category = val;
	}
	public boolean has_item_category(){
		return (_bit & 0x40000L) == 0x40000L;
	}
	public BodyPart get_body_part(){
		return _body_part;
	}
	public void set_body_part(BodyPart val){
		_bit |= 0x80000L;
		_body_part = val;
	}
	public boolean has_body_part(){
		return (_bit & 0x80000L) == 0x80000L;
	}
	public int get_ac(){
		return _ac;
	}
	public void set_ac(int val){
		_bit |= 0x100000L;
		_ac = val;
	}
	public boolean has_ac(){
		return (_bit & 0x100000L) == 0x100000L;
	}
	public ExtendedWeaponType get_extended_weapon_type(){
		return _extended_weapon_type;
	}
	public void set_extended_weapon_type(ExtendedWeaponType val){
		_bit |= 0x20000000L;
		_extended_weapon_type = val;
	}
	public boolean has_extended_weapon_type(){
		return (_bit & 0x20000000L) == 0x20000000L;
	}
	public int get_large_damage(){
		return _large_damage;
	}
	public void set_large_damage(int val){
		_bit |= 0x40000000L;
		_large_damage = val;
	}
	public boolean has_large_damage(){
		return (_bit & 0x40000000L) == 0x40000000L;
	}
	public int get_small_damage(){
		return _small_damage;
	}
	public void set_small_damage(int val){
		_bit |= 0x80000000L;
		_small_damage = val;
	}
	public boolean has_small_damage(){
		return (_bit & 0x80000000L) == 0x80000000L;
	}
	public int get_hit_bonus(){
		return _hit_bonus;
	}
	public void set_hit_bonus(int val){
		_bit |= 0x100000000L;
		_hit_bonus = val;
	}
	public boolean has_hit_bonus(){
		return (_bit & 0x100000000L) == 0x100000000L;
	}
	public int get_damage_bonus(){
		return _damage_bonus;
	}
	public void set_damage_bonus(int val){
		_bit |= 0x200000000L;
		_damage_bonus = val;
	}
	public boolean has_damage_bonus(){
		return (_bit & 0x200000000L) == 0x200000000L;
	}
	public CommonItemInfo.ArmorSeriesInfo get_armor_series_info(){
		return _armor_series_info;
	}
	public void set_armor_series_info(CommonItemInfo.ArmorSeriesInfo val){
		_bit |= 0x8000000000L;
		_armor_series_info = val;
	}
	public String get_armor_series_info_toString() {
		if (_armor_series_info == null) {
			return StringUtil.EmptyString;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("serise_number: ").append(_armor_series_info.get_series_number());
		sb.append(", is_main: ").append(_armor_series_info._is_main);
		sb.append(", series_part: ").append(_armor_series_info._series_part);
		return sb.toString();
	}
	public boolean has_armor_series_info(){
		return (_bit & 0x8000000000L) == 0x8000000000L;
	}
	public int get_cost(){
		return _cost;
	}
	public void set_cost(int val){
		_bit |= 0x10000000000L;
		_cost = val;
	}
	public boolean has_cost(){
		return (_bit & 0x10000000000L) == 0x10000000000L;
	}
	public boolean get_can_set_mage_enchant(){
		return _can_set_mage_enchant;
	}
	public void set_can_set_mage_enchant(boolean val){
		_bit |= 0x20000000000L;
		_can_set_mage_enchant = val;
	}
	public boolean has_can_set_mage_enchant(){
		return (_bit & 0x20000000000L) == 0x20000000000L;
	}
	public boolean get_merge(){
		return _merge;
	}
	public void set_merge(boolean val){
		_bit |= 0x40000000000L;
		_merge = val;
	}
	public boolean has_merge(){
		return (_bit & 0x40000000000L) == 0x40000000000L;
	}
	public boolean get_pss_event_item(){
		return _pss_event_item;
	}
	public void set_pss_event_item(boolean val){
		_bit |= 0x80000000000L;
		_pss_event_item = val;
	}
	public boolean has_pss_event_item(){
		return (_bit & 0x80000000000L) == 0x80000000000L;
	}
	public boolean get_market_searching_item(){
		return _market_searching_item;
	}
	public void set_market_searching_item(boolean val){
		_bit |= 0x100000000000L;
		_market_searching_item = val;
	}
	public boolean has_market_searching_item(){
		return (_bit & 0x100000000000L) == 0x100000000000L;
	}
	public java.util.LinkedList<BmItemProbData> get_lucky_bag_reward_list(){
		return _lucky_bag_reward_list;
	}
	public void add_lucky_bag_reward_list(BmItemProbData val){
		if(!has_lucky_bag_reward_list()){
			_lucky_bag_reward_list = new java.util.LinkedList<BmItemProbData>();
			_bit |= 0x200000000000L;
		}
		_lucky_bag_reward_list.add(val);
	}
	public String get_lucky_bag_reward_list_toString() {
		if (_lucky_bag_reward_list == null || _lucky_bag_reward_list.isEmpty()) {
			return StringUtil.EmptyString;
		}
		StringBuilder sb = new StringBuilder();
		for (BmItemProbData bm : _lucky_bag_reward_list) {
			if (sb.length() > 0) {
				sb.append(StringUtil.LineString);
			}
			sb.append("name_id: ").append(bm.get_nameId());
			sb.append(", prob: ").append(bm.get_prob());
			sb.append(", enchant: ").append(bm.get_enchant());
			sb.append(", elemental: ").append(bm.get_elemental());
			sb.append(", state: ").append(bm.get_state());
			sb.append(", count: ").append(bm.get_count());
		}
		return sb.toString();
	}
	public boolean has_lucky_bag_reward_list(){
		return (_bit & 0x200000000000L) == 0x200000000000L;
	}
	public java.util.LinkedList<CommonItemInfo.EnchantProbData> get_enchant_prob_list(){
		return _enchant_prob_list;
	}
	public void add_enchant_prob_list(CommonItemInfo.EnchantProbData val){
		if(!has_enchant_prob_list()){
			_enchant_prob_list = new java.util.LinkedList<CommonItemInfo.EnchantProbData>();
			_bit |= 0x400000000000L;
		}
		_enchant_prob_list.add(val);
	}
	public boolean has_enchant_prob_list(){
		return (_bit & 0x400000000000L) == 0x400000000000L;
	}
	public int get_element_enchant_table(){
		return _element_enchant_table;
	}
	public void set_element_enchant_table(int val){
		_bit |= 0x800000000000L;
		_element_enchant_table = val;
	}
	public boolean has_element_enchant_table(){
		return (_bit & 0x800000000000L) == 0x800000000000L;
	}
	public int get_accessory_enchant_table(){
		return _accessory_enchant_table;
	}
	public void set_accessory_enchant_table(int val){
		_bit |= 0x1000000000000L;
		_accessory_enchant_table = val;
	}
	public boolean has_accessory_enchant_table(){
		return (_bit & 0x1000000000000L) == 0x1000000000000L;
	}
	public int get_bm_prob_open(){
		return _bm_prob_open;
	}
	public void set_bm_prob_open(int val){
		_bit |= 0x2000000000000L;
		_bm_prob_open = val;
	}
	public boolean has_bm_prob_open(){
		return (_bit & 0x2000000000000L) == 0x2000000000000L;
	}
	public int get_enchant_type(){
		return _enchant_type;
	}
	public void set_enchant_type(int val){
		_bit |= 0x4000000000000L;
		_enchant_type = val;
	}
	public boolean has_enchant_type(){
		return (_bit & 0x4000000000000L) == 0x4000000000000L;
	}
	public boolean get_is_elven(){
		return _is_elven;
	}
	public void set_is_elven(boolean val){
		_bit |= 0x8000000000000L;
		_is_elven = val;
	}
	public boolean has_is_elven(){
		return (_bit & 0x8000000000000L) == 0x8000000000000L;
	}
	public int get_forced_elemental_enchant(){
		return _forced_elemental_enchant;
	}
	public void set_forced_elemental_enchant(int val){
		_bit |= 0x10000000000000L;
		_forced_elemental_enchant = val;
	}
	public boolean has_forced_elemental_enchant(){
		return (_bit & 0x10000000000000L) == 0x10000000000000L;
	}
	public int get_max_enchant(){
		return _max_enchant;
	}
	public void set_max_enchant(int val){
		_bit |= 0x20000000000000L;
		_max_enchant = val;
	}
	public boolean has_max_enchant(){
		return (_bit & 0x20000000000000L) == 0x20000000000000L;
	}
	public boolean get_energy_lost(){
		return _energy_lost;
	}
	public void set_energy_lost(boolean val){
		_bit |= 0x40000000000000L;
		_energy_lost = val;
	}
	public boolean has_energy_lost(){
		return (_bit & 0x40000000000000L) == 0x40000000000000L;
	}
	
	public int get_prob(){
		return _prob;
	}
	public void set_prob(int val){
		_bit |= 0x80000000000000L;
		_prob = val;
	}
	public boolean has_prob(){
		return (_bit & 0x80000000000000L) == 0x80000000000000L;
	}
	public boolean get_pss_heal_item(){
		return _pss_heal_item;
	}
	public void set_pss_heal_item(boolean val){
		_bit |= 0x100000000000000L;
		_pss_heal_item = val;
	}
	public boolean has_pss_heal_item(){
		return (_bit & 0x100000000000000L) == 0x100000000000000L;
	}
	public long get_useInterval(){
		return _useInterval;
	}
	public void set_useInterval(long val){
		_bit |= 0x200000000000000L;
		_useInterval = val;
	}
	public boolean has_useInterval(){
		return (_bit & 0x200000000000000L) == 0x200000000000000L;
	}
	
	@Override
	public long getInitializeBit(){
		return _bit;
	}
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;
	}
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_name_id()){
			size += ProtoOutputStream.computeUInt32Size(1, _name_id);
		}
		if (has_icon_id()){
			size += ProtoOutputStream.computeUInt32Size(2, _icon_id);
		}
		if (has_sprite_id()){
			size += ProtoOutputStream.computeUInt32Size(3, _sprite_id);
		}
		if (has_desc()){
			size += ProtoOutputStream.computeStringSize(4, _desc);
		}
		if (has_real_desc()){
			size += ProtoOutputStream.computeStringSize(5, _real_desc);
		}
		if (has_material()){
			size += ProtoOutputStream.computeEnumSize(6, _material.toInt());
		}
		if (has_weight_1000ea()){
			size += ProtoOutputStream.computeUInt32Size(7, _weight_1000ea);
		}
		if (has_level_limit_min()){
			size += ProtoOutputStream.computeUInt32Size(8, _level_limit_min);
		}
		if (has_level_limit_max()){
			size += ProtoOutputStream.computeUInt32Size(9, _level_limit_max);
		}
		if (has_class_permit()){
			for(CharacterClass val : _class_permit){
				size += ProtoOutputStream.computeEnumSize(10, val.toInt());
			}
		}
		if (has_equip_bonus_list()){
			for(CommonBonusDescription val : _equip_bonus_list){
				size += ProtoOutputStream.computeMessageSize(11, val);
			}
		}
		if (has_interaction_type()){
			size += ProtoOutputStream.computeUInt32Size(12, _interaction_type);
		}
		if (has_real_weight()){
			size += ProtoOutputStream.computeUInt32Size(13, _real_weight);
		}
		if (has_spell_range()){
			size += ProtoOutputStream.computeUInt32Size(14, _spell_range);
		}
		if (has_item_category()){
			size += ProtoOutputStream.computeEnumSize(19, _item_category.toInt());
		}
		if (has_body_part()){
			size += ProtoOutputStream.computeEnumSize(20, _body_part.toInt());
		}
		if (has_ac()){
			size += ProtoOutputStream.computeUInt32Size(21, _ac);
		}
		if (has_extended_weapon_type()){
			size += ProtoOutputStream.computeEnumSize(30, _extended_weapon_type.toInt());
		}
		if (has_large_damage()){
			size += ProtoOutputStream.computeUInt32Size(31, _large_damage);
		}
		if (has_small_damage()){
			size += ProtoOutputStream.computeUInt32Size(32, _small_damage);
		}
		if (has_hit_bonus()){
			size += ProtoOutputStream.computeUInt32Size(33, _hit_bonus);
		}
		if (has_damage_bonus()){
			size += ProtoOutputStream.computeUInt32Size(34, _damage_bonus);
		}
		if (has_armor_series_info()){
			size += ProtoOutputStream.computeMessageSize(40, _armor_series_info);
		}
		if (has_cost()){
			size += ProtoOutputStream.computeInt32Size(41, _cost);
		}
		if (has_can_set_mage_enchant()){
			size += ProtoOutputStream.computeBoolSize(42, _can_set_mage_enchant);
		}
		if (has_merge()){
			size += ProtoOutputStream.computeBoolSize(43, _merge);
		}
		if (has_pss_event_item()){
			size += ProtoOutputStream.computeBoolSize(44, _pss_event_item);
		}
		if (has_market_searching_item()){
			size += ProtoOutputStream.computeBoolSize(45, _market_searching_item);
		}
		if (has_lucky_bag_reward_list()){
			for(BmItemProbData val : _lucky_bag_reward_list){
				size += ProtoOutputStream.computeMessageSize(46, val);
			}
		}
		if (has_enchant_prob_list()){
			for(CommonItemInfo.EnchantProbData val : _enchant_prob_list){
				size += ProtoOutputStream.computeMessageSize(47, val);
			}
		}
		if (has_element_enchant_table()){
			size += ProtoOutputStream.computeInt32Size(48, _element_enchant_table);
		}
		if (has_accessory_enchant_table()){
			size += ProtoOutputStream.computeInt32Size(49, _accessory_enchant_table);
		}
		if (has_bm_prob_open()){
			size += ProtoOutputStream.computeInt32Size(50, _bm_prob_open);
		}
		if (has_enchant_type()){
			size += ProtoOutputStream.computeInt32Size(51, _enchant_type);
		}
		if (has_is_elven()){
			size += ProtoOutputStream.computeBoolSize(52, _is_elven);
		}
		if (has_forced_elemental_enchant()){
			size += ProtoOutputStream.computeInt32Size(53, _forced_elemental_enchant);
		}
		if (has_max_enchant()){
			size += ProtoOutputStream.computeInt32Size(54, _max_enchant);
		}
		if (has_energy_lost()){
			size += ProtoOutputStream.computeBoolSize(55, _energy_lost);
		}
		if (has_prob()){
			size += ProtoOutputStream.computeInt32Size(56, _prob);
		}
		if (has_pss_heal_item()){
			size += ProtoOutputStream.computeBoolSize(57, _pss_heal_item);
		}
		if (has_useInterval()){
			size += ProtoOutputStream.computeInt64Size(58, _useInterval);
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
		/*if (has_class_permit()){
			for(CharacterClass val : _class_permit){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}*/
		if (has_equip_bonus_list()){
			for(CommonBonusDescription val : _equip_bonus_list){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_lucky_bag_reward_list()){
			for(BmItemProbData val : _lucky_bag_reward_list){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_enchant_prob_list()){
			for(CommonItemInfo.EnchantProbData val : _enchant_prob_list){
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
		if (has_name_id()){
			output.writeUInt32(1, _name_id);
		}
		if (has_icon_id()){
			output.writeUInt32(2, _icon_id);
		}
		if (has_sprite_id()){
			output.writeUInt32(3, _sprite_id);
		}
		if (has_desc()){
			output.writeString(4, _desc);
		}
		if (has_real_desc()){
			output.writeString(5, _real_desc);
		}
		if (has_material()){
			output.writeEnum(6, _material.toInt());
		}
		if (has_weight_1000ea()){
			output.writeUInt32(7, _weight_1000ea);
		}
		if (has_level_limit_min()){
			output.writeUInt32(8, _level_limit_min);
		}
		if (has_level_limit_max()){
			output.writeUInt32(9, _level_limit_max);
		}
		if (has_class_permit()){
			for (CharacterClass val : _class_permit){
				output.writeEnum(10, val.toInt());
			}
		}
		if (has_equip_bonus_list()){
			for (CommonBonusDescription val : _equip_bonus_list){
				output.writeMessage(11, val);
			}
		}
		if (has_interaction_type()){
			output.writeUInt32(12, _interaction_type);
		}
		if (has_real_weight()){
			output.writeUInt32(13, _real_weight);
		}
		if (has_spell_range()){
			output.writeUInt32(14, _spell_range);
		}
		if (has_item_category()){
			output.writeEnum(19, _item_category.toInt());
		}
		if (has_body_part()){
			output.writeEnum(20, _body_part.toInt());
		}
		if (has_ac()){
			output.writeUInt32(21, _ac);
		}
		if (has_extended_weapon_type()){
			output.writeEnum(30, _extended_weapon_type.toInt());
		}
		if (has_large_damage()){
			output.writeUInt32(31, _large_damage);
		}
		if (has_small_damage()){
			output.writeUInt32(32, _small_damage);
		}
		if (has_hit_bonus()){
			output.writeUInt32(33, _hit_bonus);
		}
		if (has_damage_bonus()){
			output.writeUInt32(34, _damage_bonus);
		}
		if (has_armor_series_info()){
			output.writeMessage(40, _armor_series_info);
		}
		if (has_cost()){
			output.wirteInt32(41, _cost);
		}
		if (has_can_set_mage_enchant()){
			output.writeBool(42, _can_set_mage_enchant);
		}
		if (has_merge()){
			output.writeBool(43, _merge);
		}
		if (has_pss_event_item()){
			output.writeBool(44, _pss_event_item);
		}
		if (has_market_searching_item()){
			output.writeBool(45, _market_searching_item);
		}
		if (has_lucky_bag_reward_list()){
			for (BmItemProbData val : _lucky_bag_reward_list){
				output.writeMessage(46, val);
			}
		}
		if (has_enchant_prob_list()){
			for (CommonItemInfo.EnchantProbData val : _enchant_prob_list){
				output.writeMessage(47, val);
			}
		}
		if (has_element_enchant_table()){
			output.wirteInt32(48, _element_enchant_table);
		}
		if (has_accessory_enchant_table()){
			output.wirteInt32(49, _accessory_enchant_table);
		}
		if (has_bm_prob_open()){
			output.wirteInt32(50, _bm_prob_open);
		}
		if (has_enchant_type()){
			output.wirteInt32(51, _enchant_type);
		}
		if (has_is_elven()){
			output.writeBool(52, _is_elven);
		}
		if (has_forced_elemental_enchant()){
			output.wirteInt32(53, _forced_elemental_enchant);
		}
		if (has_max_enchant()){
			output.wirteInt32(54, _max_enchant);
		}
		if (has_energy_lost()){
			output.writeBool(55, _energy_lost);
		}
		if (has_prob()){
			output.wirteInt32(56, _prob);
		}
		if (has_pss_heal_item()){
			output.writeBool(57, _pss_heal_item);
		}
		if (has_useInterval()){
			output.writeInt64(58, _useInterval);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000008:{
					set_name_id(input.readUInt32());
					break;
				}
				case 0x00000010:{
					set_icon_id(input.readUInt32());
					break;
				}
				case 0x00000018:{
					set_sprite_id(input.readUInt32());
					break;
				}
				case 0x00000022:{
					set_desc(input.readString());
					break;
				}
				case 0x0000002A:{
					set_real_desc(input.readString());
					break;
				}
				case 0x00000030:{
					set_material(Material.fromInt(input.readEnum()));
					break;
				}
				case 0x00000038:{
					set_weight_1000ea(input.readUInt32());
					break;
				}
				case 0x00000040:{
					set_level_limit_min(input.readUInt32());
					break;
				}
				case 0x00000048:{
					set_level_limit_max(input.readUInt32());
					break;
				}
				case 0x00000050:{
					add_class_permit(CharacterClass.fromInt(input.readEnum()));
					break;
				}
				case 0x0000005A:{
					add_equip_bonus_list((CommonBonusDescription)input.readMessage(CommonBonusDescription.newInstance()));
					break;
				}
				case 0x00000060:{
					set_interaction_type(input.readUInt32());
					break;
				}
				case 0x00000068:{
					set_real_weight(input.readUInt32());
					break;
				}
				case 0x00000070:{
					set_spell_range(input.readUInt32());
					break;
				}
				case 0x00000098:{
					set_item_category(CommonItemInfo.eItemCategory.fromInt(input.readEnum()));
					break;
				}
				case 0x000000A0:{
					set_body_part(BodyPart.fromInt(input.readEnum()));
					break;
				}
				case 0x000000A8:{
					set_ac(input.readUInt32());
					break;
				}
				case 0x000000F0:{
					set_extended_weapon_type(ExtendedWeaponType.fromInt(input.readEnum()));
					break;
				}
				case 0x000000F8:{
					set_large_damage(input.readUInt32());
					break;
				}
				case 0x00000100:{
					set_small_damage(input.readUInt32());
					break;
				}
				case 0x00000108:{
					set_hit_bonus(input.readUInt32());
					break;
				}
				case 0x00000110:{
					set_damage_bonus(input.readUInt32());
					break;
				}
				case 0x00000142:{
					set_armor_series_info((CommonItemInfo.ArmorSeriesInfo)input.readMessage(CommonItemInfo.ArmorSeriesInfo.newInstance()));
					break;
				}
				case 0x00000148:{
					set_cost(input.readInt32());
					break;
				}
				case 0x00000150:{
					set_can_set_mage_enchant(input.readBool());
					break;
				}
				case 0x00000158:{
					set_merge(input.readBool());
					break;
				}
				case 0x00000160:{
					set_pss_event_item(input.readBool());
					break;
				}
				case 0x00000168:{
					set_market_searching_item(input.readBool());
					break;
				}
				case 0x00000172:{
					add_lucky_bag_reward_list((BmItemProbData)input.readMessage(BmItemProbData.newInstance()));
					break;
				}
				case 0x0000017A:{
					add_enchant_prob_list((CommonItemInfo.EnchantProbData)input.readMessage(CommonItemInfo.EnchantProbData.newInstance()));
					break;
				}
				case 0x00000180:{
					set_element_enchant_table(input.readInt32());
					break;
				}
				case 0x00000188:{
					set_accessory_enchant_table(input.readInt32());
					break;
				}
				case 0x00000190:{
					set_bm_prob_open(input.readInt32());
					break;
				}
				case 0x00000198:{
					set_enchant_type(input.readInt32());
					break;
				}
				case 0x000001A0:{
					set_is_elven(input.readBool());
					break;
				}
				case 0x000001A8:{
					set_forced_elemental_enchant(input.readInt32());
					break;
				}
				case 0x000001B0:{
					set_max_enchant(input.readInt32());
					break;
				}
				case 0x000001B8:{
					set_energy_lost(input.readBool());
					break;
				}
				case 0x000001C0:{
					set_prob(input.readInt32());
					break;
				}
				case 0x000001C8:{
					set_pss_heal_item(input.readBool());
					break;
				}
				case 0x000001D0:{
					set_useInterval(input.readInt64());
					break;
				}
				default:{
					System.out.println(String.format("[CommonItemInfo] NEW_TAG : TAG(%d)", tag));
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
	
	public static class ArmorSeriesInfo implements ProtoMessage{
		public static ArmorSeriesInfo newInstance(){
			return new ArmorSeriesInfo();
		}
		private int _series_number;
		private boolean _is_main;
		private java.util.LinkedList<CommonBonusDescription> _series_bonus_list;
		private int _series_part;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private ArmorSeriesInfo(){
		}
		public int get_series_number(){
			return _series_number;
		}
		public void set_series_number(int val){
			_bit |= 0x1;
			_series_number = val;
		}
		public boolean has_series_number(){
			return (_bit & 0x1) == 0x1;
		}
		public boolean get_is_main(){
			return _is_main;
		}
		public void set_is_main(boolean val){
			_bit |= 0x2;
			_is_main = val;
		}
		public boolean has_is_main(){
			return (_bit & 0x2) == 0x2;
		}
		public java.util.LinkedList<CommonBonusDescription> get_series_bonus_list(){
			return _series_bonus_list;
		}
		public void add_series_bonus_list(CommonBonusDescription val){
			if(!has_series_bonus_list()){
				_series_bonus_list = new java.util.LinkedList<CommonBonusDescription>();
				_bit |= 0x4;
			}
			_series_bonus_list.add(val);
		}
		public boolean has_series_bonus_list(){
			return (_bit & 0x4) == 0x4;
		}
		public int get_series_part(){
			return _series_part;
		}
		public void set_series_part(int val){
			_bit |= 0x8;
			_series_part = val;
		}
		public boolean has_series_part(){
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
			if (has_series_number()){
				size += ProtoOutputStream.computeUInt32Size(1, _series_number);
			}
			if (has_is_main()){
				size += ProtoOutputStream.computeBoolSize(2, _is_main);
			}
			if (has_series_bonus_list()){
				for(CommonBonusDescription val : _series_bonus_list){
					size += ProtoOutputStream.computeMessageSize(3, val);
				}
			}
			if (has_series_part()){
				size += ProtoOutputStream.computeInt32Size(4, _series_part);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_series_number()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_is_main()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (has_series_bonus_list()){
				for(CommonBonusDescription val : _series_bonus_list){
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
			if (has_series_number()){
				output.writeUInt32(1, _series_number);
			}
			if (has_is_main()){
				output.writeBool(2, _is_main);
			}
			if (has_series_bonus_list()){
				for (CommonBonusDescription val : _series_bonus_list){
					output.writeMessage(3, val);
				}
			}
			if (has_series_part()){
				output.wirteInt32(4, _series_part);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_series_number(input.readUInt32());
						break;
					}
					case 0x00000010:{
						set_is_main(input.readBool());
						break;
					}
					case 0x0000001A:{
						add_series_bonus_list((CommonBonusDescription)input.readMessage(CommonBonusDescription.newInstance()));
						break;
					}
					case 0x00000020:{
						set_series_part(input.readInt32());
						break;
					}
					default:{
						System.out.println(String.format("[CommonItemInfo.ArmorSeriesInfo] NEW_TAG : TAG(%d)", tag));
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
	
	public static class EnchantProbData implements ProtoMessage{
		public static EnchantProbData newInstance(){
			return new EnchantProbData();
		}
		private BmItemProbData _probResultData;
		private boolean _isBrittle;
		private int _curEnchant;
		private int _blessCode;
		private boolean _isElven;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private EnchantProbData(){
		}
		public BmItemProbData get_probResultData(){
			return _probResultData;
		}
		public void set_probResultData(BmItemProbData val){
			_bit |= 0x1;
			_probResultData = val;
		}
		public boolean has_probResultData(){
			return (_bit & 0x1) == 0x1;
		}
		public boolean get_isBrittle(){
			return _isBrittle;
		}
		public void set_isBrittle(boolean val){
			_bit |= 0x2;
			_isBrittle = val;
		}
		public boolean has_isBrittle(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_curEnchant(){
			return _curEnchant;
		}
		public void set_curEnchant(int val){
			_bit |= 0x4;
			_curEnchant = val;
		}
		public boolean has_curEnchant(){
			return (_bit & 0x4) == 0x4;
		}
		public int get_blessCode(){
			return _blessCode;
		}
		public void set_blessCode(int val){
			_bit |= 0x8;
			_blessCode = val;
		}
		public boolean has_blessCode(){
			return (_bit & 0x8) == 0x8;
		}
		public boolean get_isElven(){
			return _isElven;
		}
		public void set_isElven(boolean val){
			_bit |= 0x10;
			_isElven = val;
		}
		public boolean has_isElven(){
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
			if (has_probResultData()){
				size += ProtoOutputStream.computeMessageSize(1, _probResultData);
			}
			if (has_isBrittle()){
				size += ProtoOutputStream.computeBoolSize(2, _isBrittle);
			}
			if (has_curEnchant()){
				size += ProtoOutputStream.computeInt32Size(3, _curEnchant);
			}
			if (has_blessCode()){
				size += ProtoOutputStream.computeInt32Size(4, _blessCode);
			}
			if (has_isElven()){
				size += ProtoOutputStream.computeBoolSize(5, _isElven);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_probResultData()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_isBrittle()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_curEnchant()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_blessCode()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_probResultData()){
				output.writeMessage(1, _probResultData);
			}
			if (has_isBrittle()){
				output.writeBool(2, _isBrittle);
			}
			if (has_curEnchant()){
				output.wirteInt32(3, _curEnchant);
			}
			if (has_blessCode()){
				output.wirteInt32(4, _blessCode);
			}
			if (has_isElven()){
				output.writeBool(5, _isElven);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x0000000A:{
						set_probResultData((BmItemProbData)input.readMessage(BmItemProbData.newInstance()));
						break;
					}
					case 0x00000010:{
						set_isBrittle(input.readBool());
						break;
					}
					case 0x00000018:{
						set_curEnchant(input.readInt32());
						break;
					}
					case 0x00000020:{
						set_blessCode(input.readInt32());
						break;
					}
					case 0x00000028:{
						set_isElven(input.readBool());
						break;
					}
					default:{
						System.out.println(String.format("[CommonItemInfo.EnchantProbData] NEW_TAG : TAG(%d)", tag));
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
	public enum eItemCategory{
		NONE(0),
		WEAPON(1),
		ARMOR(19),
		FOOD(21),
		LIGHT(22),
		ITEM(23),
		POTION(1000),
		POTION_HEAL(1001),
		SCROLL_TELEPORT_TOWN(1002),
		SCROLL_TELEPORT_HOME(1003),
		SCROLL(1004),
		ARMOR_SERIES(1005),
		ARMOR_SERIES_MAIN(1006),
		WAND_CALL_LIGHTNING(1007),
		LUCKY_BAG(1008),
		POTION_MANA(1009),
		ARROW(1010),
		SPELL_EXTRACTOR(1011),
		AUTO_USED_BY_BUFF_ITEM(1012),
		WAND(1013),
		;
		private int value;
		eItemCategory(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eItemCategory v){
			return value == v.value;
		}
		public static eItemCategory fromInt(int i){
			switch(i){
			case 0:
				return NONE;
			case 1:
				return WEAPON;
			case 19:
				return ARMOR;
			case 21:
				return FOOD;
			case 22:
				return LIGHT;
			case 23:
				return ITEM;
			case 1000:
				return POTION;
			case 1001:
				return POTION_HEAL;
			case 1002:
				return SCROLL_TELEPORT_TOWN;
			case 1003:
				return SCROLL_TELEPORT_HOME;
			case 1004:
				return SCROLL;
			case 1005:
				return ARMOR_SERIES;
			case 1006:
				return ARMOR_SERIES_MAIN;
			case 1007:
				return WAND_CALL_LIGHTNING;
			case 1008:
				return LUCKY_BAG;
			case 1009:
				return POTION_MANA;
			case 1010:
				return ARROW;
			case 1011:
				return SPELL_EXTRACTOR;
			case 1012:
				return AUTO_USED_BY_BUFF_ITEM;
			case 1013:
				return WAND;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eItemCategory, %d", i));
			}
		}
		
	}
	
}

