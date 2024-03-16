package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.beginnerquest.BeginnerQuestTable;
import l1j.server.GameSystem.dungeontimer.loader.L1DungeonTimerLoader;
import l1j.server.common.bin.CatalystTableCommonBinLoader;
import l1j.server.common.bin.ItemCommonBinLoader;
import l1j.server.common.data.Material;
import l1j.server.server.IdFactory;
import l1j.server.server.construct.L1Alignment;
import l1j.server.server.construct.L1Grade;
import l1j.server.server.construct.item.L1ItemArmorType;
import l1j.server.server.construct.item.L1ItemLimitType;
import l1j.server.server.construct.item.L1ItemNormalType;
import l1j.server.server.construct.item.L1ItemSpellBookAttr;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.construct.item.L1ItemWeaponType;
import l1j.server.server.controller.DollRaceController;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.item.L1Fishing;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.item.ablity.enchant.L1EnchantLoader;
import l1j.server.server.model.item.function.ItemSelector.SelectorType;
import l1j.server.server.model.item.function.L1HealingPotion;
import l1j.server.server.model.skill.L1SkillType;
import l1j.server.server.templates.L1Armor;
import l1j.server.server.templates.L1DogFightTicket;
import l1j.server.server.templates.L1EtcItem;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1RaceTicket;
import l1j.server.server.templates.L1Weapon;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class ItemTable {
	private static Logger _log = Logger.getLogger(ItemTable.class.getName());
	public static final String COLOR_REPLACE_STR = "[\\\\]+[a]+[E?F?G?H?I?J]";

	private static ItemTable _instance;
	private final Map<Integer, L1RaceTicket> _race;// 레이스
	private final Map<Integer, L1DogFightTicket> _dogfight;// 투견
	public static final int RACE_TICKET_ID_DEFAULT	= 10000000;
	public static final int FIGHT_TICKET_ID_DEFAULT	= 15000000;
	private int _raceTicketId		= RACE_TICKET_ID_DEFAULT;
	private int _dogfightTicketId	= FIGHT_TICKET_ID_DEFAULT;

	public static ItemTable getInstance() {
		if (_instance == null) {
			_instance = new ItemTable();
		}
		return _instance;
	}

	private HashMap<Integer, L1Item> _allTemplates;
	private HashMap<Integer, L1Item> _allTemplateToNameIds;
	private HashMap<String, L1Item> _allTemplateToDescKrs;

	public final Map<Integer, L1EtcItem> _etcitems;
	public final Map<Integer, L1Armor> _armors;
	public final Map<Integer, L1Weapon> _weapons;
	
	public final Map<Integer, L1EtcItem> _etcitemToNameIds				= new HashMap<>();
	public final Map<Integer, L1Armor> _armorToNameIds					= new HashMap<>();
	public final Map<Integer, L1Weapon> _weaponToNameIds				= new HashMap<>();
	
	public final Map<String, L1EtcItem> _etcitemToDescKrs				= new HashMap<>();
	public final Map<String, L1Armor> _armorToDescKrs					= new HashMap<>();
	public final Map<String, L1Weapon> _weaponToDescKrs					= new HashMap<>();
	
	private static final Map<Integer, Integer> TERM_ITEMS				= new HashMap<>();
	private static final Map<Integer, L1Item> SMELTING_ITEMS			= new HashMap<>();
	private static final Map<Integer, ArrayList<L1Item>> SMELTING_LIST	= new HashMap<>();
	
	/**
	 * 아이템 조사
	 * @param itemId
	 * @return L1Item
	 */
	public L1Item getTemplate(int itemId) {
		return _allTemplates.get(itemId);
	}
	
	/**
	 * 아이템 조사
	 * @param itemId
	 * @param bless
	 * @return L1Item
	 */
	public L1Item getTemplate(int itemId, int bless) {
		L1Item temp = _allTemplates.get(itemId);
		return temp == null || temp.getBless() != bless ? null : temp;
	}
	
	/**
	 * 아이템 한글명 조사
	 * @param itemid
	 * @return String
	 */
	public String findDescKrByItemId(int itemid) {
		L1Item item = _allTemplates.get(itemid);
		return item == null ? null : item.getDescKr();
	}

	/**
	 * 아이템 아이디 조사
	 * @param desc
	 * @return itemId
	 */
	public int findItemIdByDescKr(String desc) {
		L1Item item = _allTemplateToDescKrs.get(desc);
		return item == null ? null : item.getItemId();
	}

	/**
	 * 아이템 아이디 조사
	 * @param desc
	 * @return itemId
	 */
	public int findItemIdByDescWithoutSpace(String desc) {
		String searchDesc	= desc.replace(StringUtil.EmptyOneString, StringUtil.EmptyString);
		for (L1Item item : _allTemplates.values()) {
			if (item == null) {
				continue;
			}
			String tempDesc	= item.getDescKr().replace(StringUtil.EmptyOneString, StringUtil.EmptyString).replaceAll(COLOR_REPLACE_STR, StringUtil.EmptyString);
			if (tempDesc.equals(searchDesc)) {
				return item.getItemId();
			}
		}
		return 0;
	}
	
	/**
	 * 아이템 조사
	 * @param item_name_id
	 * @return L1Item
	 */
	public L1Item findItemByNameId(int item_name_id) {
		return _allTemplateToNameIds.get(item_name_id);
	}
	
	/**
	 * 아이템 조사
	 * @param item_name_id
	 * @param bless
	 * @return L1Item
	 */
	public L1Item findItemByNameIdAndBless(int item_name_id, int bless) {
		for (L1Item item : _allTemplates.values()) {
			if (item != null && item.getItemNameId() == item_name_id && item.getBless() == bless) {
				return item;
			}
		}
		return null;
	}
	
	/**
	 * 아이템 삭제 기간 조사
	 * @param itemId
	 * @return 기간
	 */
	public static int getTerm(int itemId){
		return TERM_ITEMS.containsKey(itemId) ? TERM_ITEMS.get(itemId) : 0;
	}
	
	/**
	 * 제련석 조사
	 * @param item_name_id
	 * @return L1Item
	 */
	public static L1Item getSmelting(int item_name_id) {
		return SMELTING_ITEMS.get(item_name_id);
	}
	
	/**
	 * 제련석 등급 리스트 조사
	 * @param alchemy_id
	 * @return ArrayList<L1Item>
	 */
	public static ArrayList<L1Item> getSmeltingList(int alchemy_id) {
		return SMELTING_LIST.get(alchemy_id);
	}

	private ItemTable() {
		_etcitems	= allEtcItem();
		_weapons	= allWeapon();
		_armors		= allArmor();
		_race		= allRace();
		_dogfight	= allDogFight();
		loadTerms();
		buildFastLookupTable();
	}
	
	private void loadTerms(){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select * from item_terms");
			rs = pstm.executeQuery();
			while(rs.next()){
				TERM_ITEMS.put(rs.getInt("itemId"), rs.getInt("termMinut"));
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public void InitDollTickets() {
		for (L1RaceTicket item : _race.values()) {
			DollRaceController.getInstance().SetWinRaceTicketPrice(item.getItemId(), (float) item.getTicketPrice() / 500.f);
		}
	}
	
	public void updateTicketPrice(int item_id, int price) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE race_tickets SET price = ? WHERE item_id = ?");
			pstm.setInt(1, price);
			pstm.setInt(2, item_id);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	private Map<Integer, L1RaceTicket> allRace() {
		Map<Integer, L1RaceTicket> result = new HashMap<Integer, L1RaceTicket>();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		L1RaceTicket item = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM race_tickets");
			rs = pstm.executeQuery();
			while(rs.next()){
				item = new L1RaceTicket();
				item.setItemType(L1ItemType.NORMAL);
				item.setItemId(rs.getInt("item_id"));
				item.setDescKr(rs.getString("name"));
				//item.setDesc(item.getDescKr());
				item.setDesc(item.getDescEn());
				item.setType(12);
				item.setMaterial(Material.PAPER);
				item.setWeight(0);
				item.setTicketPrice(rs.getInt("price"));
				item.setIconId(5427);
				item.setSpriteId(151);
				item.setMinLevel(0);
				item.setMaxLevel(0);
				item.setBless(1);
				item.setTradable(false);
				item.setDmgSmall(0);
				item.setDmgLarge(0);
				item.set_stackable(true);
				if (_raceTicketId <= item.getItemId()) {
					_raceTicketId = item.getItemId() + 1;
				}
				result.put(new Integer(item.getItemId()), item);
			}
		} catch (NullPointerException e) {
			//_log.log(Level.SEVERE, new StringBuilder().append(item.getDescKr()).append("(" + item.getItemId() + ")").append("의 읽어 들이기에 실패했습니다.").toString());
			_log.log(Level.SEVERE, new StringBuilder().append("Failed to load ").append(item.getDescEn()).append(" (" + item.getItemId() + ")").toString());

		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return result;
	}
	
	/*public void updateDogFightTicketPrice(int item_id, int price) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE dogfight_tickets SET price = ? WHERE item_id = ?");
			pstm.setInt(1, price);
			pstm.setInt(2, item_id);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}*/
	
	private Map<Integer, L1DogFightTicket> allDogFight() {
		Map<Integer, L1DogFightTicket> result = new HashMap<Integer, L1DogFightTicket>();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		L1DogFightTicket item = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM dogfight_tickets");
			rs = pstm.executeQuery();
			while(rs.next()){
				item = new L1DogFightTicket();
				item.setItemType(L1ItemType.NORMAL);
				item.setItemId(rs.getInt("item_id"));
				item.setDescKr(rs.getString("name"));
				item.setDesc(item.getDescKr());
				item.setType(12);
				item.setMaterial(Material.PAPER);
				item.setWeight(0);
				item.setTicketPrice(rs.getInt("price"));
				item.setIconId(143);
				item.setSpriteId(151);
				item.setMinLevel(0);
				item.setMaxLevel(0);
				item.setBless(1);
				item.setTradable(false);
				item.setDmgSmall(0);
				item.setDmgLarge(0);
				item.setMerge(true);
				if (_dogfightTicketId <= item.getItemId()) {
					_dogfightTicketId = item.getItemId() + 1;
				}
				result.put(new Integer(item.getItemId()), item);
			}
		} catch (NullPointerException e) {
			//_log.log(Level.SEVERE, new StringBuilder().append(item.getDescKr()).append("(" + item.getItemId() + ")").append("의 읽어 들이기에 실패했습니다.").toString());
			_log.log(Level.SEVERE, new StringBuilder().append("Failed to load ").append(item.getDescEn()).append(" (" + item.getItemId() + ")").toString());

		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return result;
	}

	public static void reload() {
		TERM_ITEMS.clear();
		SMELTING_ITEMS.clear();
		SMELTING_LIST.clear();
		ItemTable oldInstance = _instance;
		_instance = new ItemTable();
		oldInstance._etcitems.clear();
		oldInstance._weapons.clear();
		oldInstance._armors.clear();
		oldInstance._etcitemToNameIds.clear();
		oldInstance._armorToNameIds.clear();
		oldInstance._weaponToNameIds.clear();
		oldInstance._etcitemToDescKrs.clear();
		oldInstance._armorToDescKrs.clear();
		oldInstance._weaponToDescKrs.clear();
		oldInstance._allTemplates.clear();
		oldInstance._allTemplateToNameIds.clear();
		oldInstance._allTemplateToDescKrs.clear();
		
		L1EnchantLoader.getInstance().reload();
		L1HealingPotion.reload();
		BeginnerQuestTable.reload();
	}
	
	public int getDefaultNameId(int namd_id) {
		return namd_id == 0 ? -1 : namd_id;
	}

	private Map<Integer, L1EtcItem> allEtcItem() {
		Map<Integer, L1EtcItem> result = new HashMap<Integer, L1EtcItem>();
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		L1EtcItem item			= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM etcitem");
			rs = pstm.executeQuery();
			while(rs.next()){
				item = new L1EtcItem();
				item.setItemId(rs.getInt("item_id"));
				item.setItemNameId(getDefaultNameId(rs.getInt("item_name_id")));
				item.setDescKr(rs.getString("desc_kr"));
				item.setDescEn(rs.getString("desc_en"));
				item.setDesc(rs.getString("desc_id"));
				item.setItemGrade(L1Grade.fromString(rs.getString("itemGrade")));
				item.setType(L1ItemNormalType.fromString(rs.getString("item_type")).getId());
				item.set_interaction_type(L1ItemType.fromString(rs.getString("use_type")).getInteractionType());
				item.setItemType(L1ItemType.NORMAL);
				item.setMaterial(Material.fromString(rs.getString("material")));
				item.setWeight(rs.getInt("weight"));
				item.setIconId(rs.getInt("iconId"));
				item.setSpriteId(rs.getInt("spriteId"));
				item.setMinLevel(rs.getInt("min_lvl"));
				item.setMaxLevel(rs.getInt("max_lvl"));
				item.setBless(rs.getInt("bless"));
				item.setTradable(rs.getInt("trade") == 0);
				item.setRetrieve(rs.getInt("retrieve") == 0);
				item.setSpecialRetrieve(rs.getInt("specialretrieve") == 0);
				item.setCantDelete(rs.getInt("cant_delete") == 1);
				item.setCantSell(rs.getInt("cant_sell") == 0);
				
				item.setDmgSmall(rs.getInt("dmg_small"));
				item.setDmgLarge(rs.getInt("dmg_large"));
				item.setAcBonus(rs.getInt("ac_bonus"));
				item.setDmgRate(rs.getInt("shortDmg"));
				item.setHitRate(rs.getInt("shortHit"));
				item.setBowDmgRate(rs.getInt("longDmg"));
				item.setBowHitRate(rs.getInt("longHit"));
				item.setAddStr(rs.getByte("add_str"));
				item.setAddCon(rs.getByte("add_con"));
				item.setAddDex(rs.getByte("add_dex"));
				item.setAddInt(rs.getByte("add_int"));
				item.setAddWis(rs.getByte("add_wis"));
				item.setAddCha(rs.getByte("add_cha"));
				item.setAddHp(rs.getInt("add_hp"));
				item.setAddMp(rs.getInt("add_mp"));
				item.setAddHpr(rs.getInt("add_hpr"));
				item.setAddMpr(rs.getInt("add_mpr"));
				item.setAddSp(rs.getInt("add_sp"));
				item.setMr(rs.getInt("m_def"));
				item.setCarryBonus(rs.getInt("carryBonus"));
				item.setAttrEarth(rs.getInt("defense_earth"));
				item.setAttrWater(rs.getInt("defense_water"));
				item.setAttrWind(rs.getInt("defense_wind"));
				item.setAttrFire(rs.getInt("defense_fire"));
				item.setAttrAll(rs.getInt("attr_all"));
				item.setRegistStone(rs.getInt("regist_stone"));
				item.setRegistSleep(rs.getInt("regist_sleep"));
				item.setRegistFreeze(rs.getInt("regist_freeze"));
				item.setRegistBlind(rs.getInt("regist_blind"));
				item.setToleranceSkill(rs.getInt("regist_skill"));
				item.setToleranceSpirit(rs.getInt("regist_spirit"));
				item.setToleranceDragon(rs.getInt("regist_dragon"));
				item.setToleranceFear(rs.getInt("regist_fear"));
				item.setToleranceAll(rs.getInt("regist_all"));
				item.setHitupSkill(rs.getInt("hitup_skill"));
				item.setHitupSpirit(rs.getInt("hitup_spirit"));
				item.setHitupDragon(rs.getInt("hitup_dragon"));
				item.setHitupFear(rs.getInt("hitup_fear"));
				item.setHitupAll(rs.getInt("hitup_all"));
				item.setHitupMagic(rs.getInt("hitup_magic"));
				item.setDamageReduction(rs.getInt("damage_reduction"));
				item.setMagicDamageReduction(rs.getInt("MagicDamageReduction"));
				item.setDamageReductionEgnor(rs.getInt("reductionEgnor"));
				item.setDamageReductionPercent(rs.getInt("reductionPercent"));
				item.setPVPDamage(rs.getInt("PVPDamage"));
				item.setPVPDamageReduction(rs.getInt("PVPDamageReduction"));
				item.setPVPDamageReductionPercent(rs.getInt("PVPDamageReductionPercent"));
				item.setPVPMagicDamageReduction(rs.getInt("PVPMagicDamageReduction"));
				item.setPVPReductionEgnor(rs.getInt("PVPReductionEgnor"));
				item.setPVPMagicDamageReductionEgnor(rs.getInt("PVPMagicDamageReductionEgnor"));
				item.setAbnormalStatusDamageReduction(rs.getInt("abnormalStatusDamageReduction"));
				item.setAbnormalStatusPVPDamageReduction(rs.getInt("abnormalStatusPVPDamageReduction"));
				item.setPVPDamagePercent(rs.getInt("PVPDamagePercent"));
				item.setExpBonus(rs.getInt("expBonus"));
				item.setRestExpReduceEfficiency(rs.getInt("rest_exp_reduce_efficiency"));
				item.setShortCritical(rs.getInt("shortCritical"));
				item.setLongCritical(rs.getInt("longCritical"));
				item.setMagicCritical(rs.getInt("magicCritical"));
				item.setAddDg(rs.getInt("addDg"));
				item.setAddEr(rs.getInt("addEr"));
				item.setAddMe(rs.getInt("addMe"));
				item.setImunEgnor(rs.getInt("imunEgnor"));
				item.setStunDuration(rs.getInt("stunDuration"));
				item.setTripleArrowStun(rs.getInt("tripleArrowStun"));
				item.setStrangeTimeIncrease(rs.getInt("strangeTimeIncrease"));
				item.setStrangeTimeDecrease(rs.getInt("strangeTimeDecrease"));
				item.setPotionRegist(rs.getInt("potionRegist"));
				item.setPotionPercent(rs.getInt("potionPercent"));
				item.setPotionValue(rs.getInt("potionValue"));
				item.setHprAbsol32Second(rs.getInt("hprAbsol32Second"));
				item.setMprAbsol64Second(rs.getInt("mprAbsol64Second"));
				item.setMprAbsol16Second(rs.getInt("mprAbsol16Second"));
				item.setHpPotionDelayDecrease(rs.getInt("hpPotionDelayDecrease"));
				item.setHpPotionCriticalProb(rs.getInt("hpPotionCriticalProb"));
				item.setIncreaseArmorSkillProb(rs.getInt("increaseArmorSkillProb"));
				item.setAttackSpeedDelayRate(rs.getInt("attackSpeedDelayRate"));
				item.setMoveSpeedDelayRate(rs.getInt("moveSpeedDelayRate"));
				item.setPoisonRegist(Boolean.parseBoolean(rs.getString("poisonRegist")));
				item.setBuffDurationSecond(rs.getInt("buffDurationSecond"));
				
				item.setMerge(Boolean.parseBoolean(rs.getString("merge")));
				item.setMaxChargeCount(rs.getInt("max_charge_count"));
				item.set_locx(rs.getInt("locx"));
				item.set_locy(rs.getInt("locy"));
				item.set_mapid(rs.getShort("mapid"));
				item.setDelayId(rs.getInt("delay_id"));
				item.setDelayTime(rs.getInt("delay_time"));
				item.setDelayEffect(rs.getInt("delay_effect"));
				item.setFoodVolume(rs.getInt("food_volume"));
				item.setToBeSavedAtOnce(rs.getInt("save_at_once") == 1);
				item.setMagicName(rs.getString("Magic_name"));
				item.setSkillLevel(rs.getInt("level"));
				item.setSkillAttr(L1ItemSpellBookAttr.fromString(rs.getString("attr")));
				item.setAlignment(L1Alignment.fromString(rs.getString("alignment")));
				item.setUseRoyal(rs.getInt("use_royal") == 1);
				item.setUseKnight(rs.getInt("use_knight") == 1);
				item.setUseElf(rs.getInt("use_elf") == 1);
				item.setUseMage(rs.getInt("use_mage") == 1);
				item.setUseDarkelf(rs.getInt("use_darkelf") == 1);
				item.setUseDragonKnight(rs.getInt("use_dragonknight") == 1);
				item.setUseIllusionist(rs.getInt("use_illusionist") == 1);
				item.setUseWarrior(rs.getInt("use_warrior") == 1);
				item.setUseFencer(rs.getInt("use_fencer") == 1);
				item.setUseLancer(rs.getInt("use_lancer") == 1);
				if (item.getType() == L1ItemNormalType.SPELL_BOOK.getId()) {
					item.setSkillType(L1SkillType.fromString(rs.getString("skill_type")));
				}
				item.setEtcValue(rs.getInt("etc_value"));
				item.setLimitType(L1ItemLimitType.fromString(rs.getString("limit_type")));
				item.setProb(rs.getInt("prob"));
				
				if (item.getItemNameId() > 0) {
					item.setBin(ItemCommonBinLoader.getCommonInfo(item.getItemNameId()));
				}
				if (item.get_interaction_type() == L1ItemType.SMELTING.getInteractionType()) {
					SMELTING_ITEMS.put(item.getItemNameId(), item);
					
					ArrayList<L1Item> smelting_list = SMELTING_LIST.get(item.getEtcValue());
					if (smelting_list == null) {
						smelting_list = new ArrayList<>();
						SMELTING_LIST.put(item.getEtcValue(), smelting_list);
					}
					smelting_list.add(item);
				}
				result.put(new Integer(item.getItemId()), item);
				_etcitemToNameIds.put(item.getItemNameId(), item);
				_etcitemToDescKrs.put(item.getDescKr(), item);
			}
		} catch (NullPointerException e) {
			//_log.log(Level.SEVERE, new StringBuilder().append(item.getDescKr()).append("(" + item.getItemId() + ")").append("의 읽어 들이기에 실패했습니다.").toString());
			_log.log(Level.SEVERE, new StringBuilder().append("Failed to load ").append(item.getDescEn()).append(" (" + item.getItemId() + ")").toString());
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return result;
	}

	private Map<Integer, L1Weapon> allWeapon() {
		Map<Integer, L1Weapon> result = new HashMap<Integer, L1Weapon>();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		L1Weapon weapon = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM weapon");
			rs = pstm.executeQuery();
			while (rs.next()) {
				weapon = new L1Weapon();
				weapon.setItemId(rs.getInt("item_id"));
				weapon.setItemNameId(getDefaultNameId(rs.getInt("item_name_id")));
				weapon.setDescKr(rs.getString("desc_kr"));
				weapon.setDescEn(rs.getString("desc_en"));
				weapon.setDesc(rs.getString("desc_id"));
				weapon.setItemGrade(L1Grade.fromString(rs.getString("itemGrade")));
				L1ItemWeaponType weaponType = L1ItemWeaponType.fromString(rs.getString("type"));
				weapon.setType(weaponType.getId());
				weapon.setItemType(L1ItemType.WEAPON);
				weapon.setWeaponType(weaponType);
				weapon.set_interaction_type(L1ItemType.WEAPON.getInteractionType());
				weapon.setMaterial(Material.fromString(rs.getString("material")));
				weapon.setWeight(rs.getInt("weight"));
				weapon.setIconId(rs.getInt("iconId"));
				weapon.setSpriteId(rs.getInt("spriteId"));
				weapon.setDmgSmall(rs.getInt("dmg_small"));
				weapon.setDmgLarge(rs.getInt("dmg_large"));
				weapon.setSafeEnchant(rs.getInt("safenchant"));
				weapon.setUseRoyal(rs.getInt("use_royal") == 0 ? false : true);
				weapon.setUseKnight(rs.getInt("use_knight") == 0 ? false : true);
				weapon.setUseElf(rs.getInt("use_elf") == 0 ? false : true);
				weapon.setUseMage(rs.getInt("use_mage") == 0 ? false : true);
				weapon.setUseDarkelf(rs.getInt("use_darkelf") == 0 ? false : true);
				weapon.setUseDragonKnight(rs.getInt("use_dragonknight") == 0 ? false : true);
				weapon.setUseIllusionist(rs.getInt("use_illusionist") == 0 ? false : true);
				weapon.setUseWarrior(rs.getInt("use_warrior") == 0 ? false : true);
				weapon.setUseFencer(rs.getInt("use_fencer") == 0 ? false : true);
				weapon.setUseLancer(rs.getInt("use_lancer") == 0 ? false : true);
				if (L1ItemWeaponType.isLongWeapon(weaponType)) {
					weapon.setBowHitRate(rs.getInt("hitmodifier"));
					weapon.setBowDmgRate(rs.getInt("dmgmodifier"));
				} else {
					weapon.setHitRate(rs.getInt("hitmodifier"));
					weapon.setDmgRate(rs.getInt("dmgmodifier"));
				}
				weapon.setAddStr(rs.getByte("add_str"));
				weapon.setAddDex(rs.getByte("add_dex"));
				weapon.setAddCon(rs.getByte("add_con"));
				weapon.setAddInt(rs.getByte("add_int"));
				weapon.setAddWis(rs.getByte("add_wis"));
				weapon.setAddCha(rs.getByte("add_cha"));
				weapon.setAddHp(rs.getInt("add_hp"));
				weapon.setAddMp(rs.getInt("add_mp"));
				weapon.setAddHpr(rs.getInt("add_hpr"));
				weapon.setAddMpr(rs.getInt("add_mpr"));
				weapon.setAddSp(rs.getInt("add_sp"));
				weapon.setMr(rs.getInt("m_def"));
				weapon.setDoubleDmgChance(rs.getInt("double_dmg_chance"));
				weapon.setMagicDmgModifier(rs.getInt("magicdmgmodifier"));
				weapon.setCanbeDmg(rs.getInt("canbedmg"));
				weapon.setMinLevel(rs.getInt("min_lvl"));
				weapon.setMaxLevel(rs.getInt("max_lvl"));
				weapon.setBless(rs.getInt("bless"));
				weapon.setTradable(rs.getInt("trade") == 0 ? true : false);
				weapon.setRetrieve(rs.getInt("retrieve") == 0 ? true : false);
				weapon.setSpecialRetrieve(rs.getInt("specialretrieve") == 0 ? true : false);
				weapon.setCantDelete(rs.getInt("cant_delete") == 1 ? true : false);
				weapon.setCantSell(rs.getInt("cant_sell") == 0 ? true : false);
				weapon.setHasteItem(rs.getInt("haste_item") == 0 ? false : true);
				weapon.setMaxUseTime(rs.getInt("max_use_time"));
				weapon.setToleranceSkill(rs.getInt("regist_skill"));
				weapon.setToleranceSpirit(rs.getInt("regist_spirit"));
				weapon.setToleranceDragon(rs.getInt("regist_dragon"));
				weapon.setToleranceFear(rs.getInt("regist_fear"));
				weapon.setToleranceAll(rs.getInt("regist_all"));
				weapon.setHitupSkill(rs.getInt("hitup_skill"));
				weapon.setHitupSpirit(rs.getInt("hitup_spirit"));
				weapon.setHitupDragon(rs.getInt("hitup_dragon"));
				weapon.setHitupFear(rs.getInt("hitup_fear"));
				weapon.setHitupAll(rs.getInt("hitup_all"));
				weapon.setHitupMagic(rs.getInt("hitup_magic"));
				weapon.setDamageReduction(rs.getInt("damage_reduction"));
				weapon.setMagicDamageReduction(rs.getInt("MagicDamageReduction"));
				weapon.setDamageReductionEgnor(rs.getInt("reductionEgnor"));
				weapon.setDamageReductionPercent(rs.getInt("reductionPercent"));
				weapon.setPVPDamage(rs.getInt("PVPDamage"));
				weapon.setPVPDamageReduction(rs.getInt("PVPDamageReduction"));
				weapon.setPVPDamageReductionPercent(rs.getInt("PVPDamageReductionPercent"));
				weapon.setPVPMagicDamageReduction(rs.getInt("PVPMagicDamageReduction"));
				weapon.setPVPReductionEgnor(rs.getInt("PVPReductionEgnor"));
				weapon.setPVPMagicDamageReductionEgnor(rs.getInt("PVPMagicDamageReductionEgnor"));
				weapon.setAbnormalStatusDamageReduction(rs.getInt("abnormalStatusDamageReduction"));
				weapon.setAbnormalStatusPVPDamageReduction(rs.getInt("abnormalStatusPVPDamageReduction"));
				weapon.setPVPDamagePercent(rs.getInt("PVPDamagePercent"));
				weapon.setExpBonus(rs.getInt("expBonus"));
				weapon.setRestExpReduceEfficiency(rs.getInt("rest_exp_reduce_efficiency"));
				weapon.setShortCritical(rs.getInt("shortCritical"));
				weapon.setLongCritical(rs.getInt("longCritical"));
				weapon.setMagicCritical(rs.getInt("magicCritical"));
				weapon.setAddDg(rs.getInt("addDg"));
				weapon.setAddEr(rs.getInt("addEr"));
				weapon.setAddMe(rs.getInt("addMe"));
				weapon.setImunEgnor(rs.getInt("imunEgnor"));
				weapon.setStunDuration(rs.getInt("stunDuration"));
				weapon.setTripleArrowStun(rs.getInt("tripleArrowStun"));
				weapon.setStrangeTimeIncrease(rs.getInt("strangeTimeIncrease"));
				weapon.setStrangeTimeDecrease(rs.getInt("strangeTimeDecrease"));
				weapon.setPotionRegist(rs.getInt("potionRegist"));
				weapon.setPotionPercent(rs.getInt("potionPercent"));
				weapon.setPotionValue(rs.getInt("potionValue"));
				weapon.setHprAbsol32Second(rs.getInt("hprAbsol32Second"));
				weapon.setMprAbsol64Second(rs.getInt("mprAbsol64Second"));
				weapon.setMprAbsol16Second(rs.getInt("mprAbsol16Second"));
				weapon.setHpPotionDelayDecrease(rs.getInt("hpPotionDelayDecrease"));
				weapon.setHpPotionCriticalProb(rs.getInt("hpPotionCriticalProb"));
				weapon.setIncreaseArmorSkillProb(rs.getInt("increaseArmorSkillProb"));
				weapon.setAttackSpeedDelayRate(rs.getInt("attackSpeedDelayRate"));
				weapon.setMoveSpeedDelayRate(rs.getInt("moveSpeedDelayRate"));
				weapon.setPoisonRegist(Boolean.parseBoolean(rs.getString("poisonRegist")));
				
				weapon.setMagicName(rs.getString("Magic_name"));
				weapon.setTwohandedWeapon(L1ItemWeaponType.isTwohandWeapon(weaponType));
				
				if (weapon.getItemNameId() > 0) {
					weapon.setBin(ItemCommonBinLoader.getCommonInfo(weapon.getItemNameId()));
				}
				
				result.put(new Integer(weapon.getItemId()), weapon);
				_weaponToNameIds.put(weapon.getItemNameId(), weapon);
				_weaponToDescKrs.put(weapon.getDescKr(), weapon);
			}
		} catch (NullPointerException e) {
			//_log.log(Level.SEVERE, new StringBuilder().append(weapon.getDescKr()).append("(" + weapon.getItemId() + ")").append("의 읽어 들이기에 실패했습니다.").toString());
			_log.log(Level.SEVERE, new StringBuilder().append("Failed to load ").append(weapon.getDescEn()).append(" (" + weapon.getItemId() + ")").toString());

		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return result;
	}

	private Map<Integer, L1Armor> allArmor() {
		Map<Integer, L1Armor> result = new HashMap<Integer, L1Armor>();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		L1Armor armor = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM armor");
			rs = pstm.executeQuery();
			while (rs.next()) {
				armor = new L1Armor();
				armor.setItemId(rs.getInt("item_id"));
				armor.setItemNameId(getDefaultNameId(rs.getInt("item_name_id")));
				armor.setDescKr(rs.getString("desc_kr"));
				armor.setDescKr(rs.getString("desc_en"));
				armor.setDesc(rs.getString("desc_id"));
				armor.setItemGrade(L1Grade.fromString(rs.getString("itemGrade")));
				String type = rs.getString("type");
				armor.setType(L1ItemArmorType.fromString(type).getId());
				armor.set_interaction_type(L1ItemType.fromString(type).getInteractionType());
				armor.setItemType(L1ItemType.ARMOR);
				armor.setGrade(rs.getInt("grade"));
				armor.setMaterial(Material.fromString(rs.getString("material")));
				armor.setWeight(rs.getInt("weight"));
				armor.setIconId(rs.getInt("iconId"));
				armor.setSpriteId(rs.getInt("spriteId"));
				armor.setAc(rs.getInt("ac"));
				armor.setAcSub(rs.getInt("ac_sub"));
				armor.setSafeEnchant(rs.getInt("safenchant"));
				armor.setUseRoyal(rs.getInt("use_royal") == 0 ? false : true);
				armor.setUseKnight(rs.getInt("use_knight") == 0 ? false : true);
				armor.setUseElf(rs.getInt("use_elf") == 0 ? false : true);
				armor.setUseMage(rs.getInt("use_mage") == 0 ? false : true);
				armor.setUseDarkelf(rs.getInt("use_darkelf") == 0 ? false : true);
				armor.setUseDragonKnight(rs.getInt("use_dragonknight") == 0 ? false : true);
				armor.setUseIllusionist(rs.getInt("use_illusionist") == 0 ? false : true);
				armor.setUseWarrior(rs.getInt("use_warrior") == 0 ? false : true);
				armor.setUseFencer(rs.getInt("use_fencer") == 0 ? false : true);
				armor.setUseLancer(rs.getInt("use_lancer") == 0 ? false : true);
				armor.setAddStr(rs.getByte("add_str"));
				armor.setAddCon(rs.getByte("add_con"));
				armor.setAddDex(rs.getByte("add_dex"));
				armor.setAddInt(rs.getByte("add_int"));
				armor.setAddWis(rs.getByte("add_wis"));
				armor.setAddCha(rs.getByte("add_cha"));
				armor.setAddHp(rs.getInt("add_hp"));
				armor.setAddMp(rs.getInt("add_mp"));
				armor.setAddHpr(rs.getInt("add_hpr"));
				armor.setAddMpr(rs.getInt("add_mpr"));
				armor.setAddSp(rs.getInt("add_sp"));
				armor.setMinLevel(rs.getInt("min_lvl"));
				armor.setMaxLevel(rs.getInt("max_lvl"));
				armor.setMr(rs.getInt("m_def"));
				armor.setCarryBonus(rs.getInt("carryBonus"));
				armor.setHitRate(rs.getInt("hit_rate"));
				armor.setDmgRate(rs.getInt("dmg_rate"));
				armor.setBowHitRate(rs.getInt("bow_hit_rate"));
				armor.setBowDmgRate(rs.getInt("bow_dmg_rate"));
				armor.setHasteItem(rs.getInt("haste_item") == 0 ? false : true);
				armor.setBless(rs.getInt("bless"));
				armor.setTradable(rs.getInt("trade") == 0 ? true : false);
				armor.setRetrieve(rs.getInt("retrieve") == 0 ? true : false);
				armor.setSpecialRetrieve(rs.getInt("specialretrieve") == 0 ? true : false);
				armor.setRetrieveEnchantLevel(rs.getInt("retrieveEnchant"));
				armor.setCantDelete(rs.getInt("cant_delete") == 1 ? true : false);
				armor.setCantSell(rs.getInt("cant_sell") == 0 ? true : false);
				armor.setAttrEarth(rs.getInt("defense_earth"));
				armor.setAttrWater(rs.getInt("defense_water"));
				armor.setAttrWind(rs.getInt("defense_wind"));
				armor.setAttrFire(rs.getInt("defense_fire"));
				armor.setAttrAll(rs.getInt("attr_all"));
				armor.setRegistStone(rs.getInt("regist_stone"));
				armor.setRegistSleep(rs.getInt("regist_sleep"));
				armor.setRegistFreeze(rs.getInt("regist_freeze"));
				armor.setRegistBlind(rs.getInt("regist_blind"));
				armor.setToleranceSkill(rs.getInt("regist_skill"));
				armor.setToleranceSpirit(rs.getInt("regist_spirit"));
				armor.setToleranceDragon(rs.getInt("regist_dragon"));
				armor.setToleranceFear(rs.getInt("regist_fear"));
				armor.setToleranceAll(rs.getInt("regist_all"));
				armor.setHitupSkill(rs.getInt("hitup_skill"));
				armor.setHitupSpirit(rs.getInt("hitup_spirit"));
				armor.setHitupDragon(rs.getInt("hitup_dragon"));
				armor.setHitupFear(rs.getInt("hitup_fear"));
				armor.setHitupAll(rs.getInt("hitup_all"));
				armor.setHitupMagic(rs.getInt("hitup_magic"));
				armor.setDamageReduction(rs.getInt("damage_reduction"));
				armor.setMagicDamageReduction(rs.getInt("MagicDamageReduction"));
				armor.setDamageReductionEgnor(rs.getInt("reductionEgnor"));
				armor.setDamageReductionPercent(rs.getInt("reductionPercent"));
				armor.setPVPDamage(rs.getInt("PVPDamage"));
				armor.setPVPDamageReduction(rs.getInt("PVPDamageReduction"));
				armor.setPVPDamageReductionPercent(rs.getInt("PVPDamageReductionPercent"));
				armor.setPVPMagicDamageReduction(rs.getInt("PVPMagicDamageReduction"));
				armor.setPVPReductionEgnor(rs.getInt("PVPReductionEgnor"));
				armor.setPVPMagicDamageReductionEgnor(rs.getInt("PVPMagicDamageReductionEgnor"));
				armor.setAbnormalStatusDamageReduction(rs.getInt("abnormalStatusDamageReduction"));
				armor.setAbnormalStatusPVPDamageReduction(rs.getInt("abnormalStatusPVPDamageReduction"));
				armor.setPVPDamagePercent(rs.getInt("PVPDamagePercent"));
				armor.setExpBonus(rs.getInt("expBonus"));
				armor.setRestExpReduceEfficiency(rs.getInt("rest_exp_reduce_efficiency"));
				armor.setShortCritical(rs.getInt("shortCritical"));
				armor.setLongCritical(rs.getInt("longCritical"));
				armor.setMagicCritical(rs.getInt("magicCritical"));
				armor.setAddDg(rs.getInt("addDg"));
				armor.setAddEr(rs.getInt("addEr"));
				armor.setAddMe(rs.getInt("addMe"));
				armor.setImunEgnor(rs.getInt("imunEgnor"));
				armor.setStunDuration(rs.getInt("stunDuration"));
				armor.setTripleArrowStun(rs.getInt("tripleArrowStun"));
				armor.setStrangeTimeIncrease(rs.getInt("strangeTimeIncrease"));
				armor.setStrangeTimeDecrease(rs.getInt("strangeTimeDecrease"));
				armor.setPotionRegist(rs.getInt("potionRegist"));
				armor.setPotionPercent(rs.getInt("potionPercent"));
				armor.setPotionValue(rs.getInt("potionValue"));
				armor.setHprAbsol32Second(rs.getInt("hprAbsol32Second"));
				armor.setMprAbsol64Second(rs.getInt("mprAbsol64Second"));
				armor.setMprAbsol16Second(rs.getInt("mprAbsol16Second"));
				armor.setHpPotionDelayDecrease(rs.getInt("hpPotionDelayDecrease"));
				armor.setHpPotionCriticalProb(rs.getInt("hpPotionCriticalProb"));
				armor.setIncreaseArmorSkillProb(rs.getInt("increaseArmorSkillProb"));
				armor.setAttackSpeedDelayRate(rs.getInt("attackSpeedDelayRate"));
				armor.setMoveSpeedDelayRate(rs.getInt("moveSpeedDelayRate"));
				armor.setPoisonRegist(Boolean.parseBoolean(rs.getString("poisonRegist")));
				armor.setMaxUseTime(rs.getInt("max_use_time"));
				armor.setMainId(rs.getInt("MainId"));
				armor.setMainId2(rs.getInt("MainId2"));
				armor.setMainId3(rs.getInt("MainId3"));
				armor.setSetId(rs.getInt("Set_Id"));
				armor.setPolyDescId(rs.getInt("polyDescId"));
				armor.setMagicName(rs.getString("Magic_name"));
				
				if (armor.getItemNameId() > 0) {
					armor.setBin(ItemCommonBinLoader.getCommonInfo(armor.getItemNameId()));
				}
				
				result.put(new Integer(armor.getItemId()), armor);
				_armorToNameIds.put(armor.getItemNameId(), armor);
				_armorToDescKrs.put(armor.getDescKr(), armor);
			}
		} catch (NullPointerException e) {
			//_log.log(Level.SEVERE, new StringBuilder().append(armor.getDescKr()).append("(" + armor.getItemId() + ")").append("의 읽어 들이기에 실패했습니다.").toString());
			_log.log(Level.SEVERE, new StringBuilder().append("Failed to load ").append(armor.getDescEn()).append(" (" + armor.getItemId() + ")").toString());
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return result;
	}

	private void buildFastLookupTable() {
		_allTemplates = new HashMap<>(_etcitems.size() + _weapons.size() + _armors.size() + _race.size() + _dogfight.size());
		_allTemplates.putAll(_etcitems);
		for (int i : _weapons.keySet()) {
			if (_allTemplates.containsKey(i)) {
				System.out.println(String.format("[ItemTable] CONTAINS_TEMPLATE_ID(%d)", i));
			}
		}
		_allTemplates.putAll(_weapons);
		for (int i : _armors.keySet()) {
			if (_allTemplates.containsKey(i)) {
				System.out.println(String.format("[ItemTable] CONTAINS_TEMPLATE_ID(%d)", i));
			}
		}
		_allTemplates.putAll(_armors);
		_allTemplates.putAll(_race);
		_allTemplates.putAll(_dogfight);
		
		_allTemplateToNameIds = new HashMap<>(_etcitemToNameIds.size() + _weaponToNameIds.size() + _armorToNameIds.size());
		_allTemplateToNameIds.putAll(_etcitemToNameIds);
		_allTemplateToNameIds.putAll(_weaponToNameIds);
		_allTemplateToNameIds.putAll(_armorToNameIds);
		
		_allTemplateToDescKrs = new HashMap<>(_etcitemToDescKrs.size() + _weaponToDescKrs.size() + _armorToDescKrs.size());
		_allTemplateToDescKrs.putAll(_etcitemToDescKrs);
		_allTemplateToDescKrs.putAll(_weaponToDescKrs);
		_allTemplateToDescKrs.putAll(_armorToDescKrs);
	}
	
	/**
	 * 아이템 생성(기능을 담당할 instance를 지정해 준다)
	 * @param L1Item
	 * @return L1ItemInstance
	 */
	public L1ItemInstance FunctionItem(L1Item temp) {
		L1ItemInstance item	= null;
		L1ItemType itemType	= temp.getItemType();
		switch(itemType){
		case WEAPON:
			item = new l1j.server.server.model.item.function.Weapon(temp);
			break;
		case ARMOR:
			item = new l1j.server.server.model.item.function.Armor(temp);
			break;
		case NORMAL:
			int item_name_id = temp.getItemNameId();
			
			L1ItemNormalType type = L1ItemNormalType.fromInt(temp.getType());
			int interactionType = temp.get_interaction_type();
			switch(type){
			case ARROW:
				item = new l1j.server.server.model.item.function.Arrow(temp);
				break;
			case WAND:
				switch (temp.getItemId()) {
				case 31141:case 31142:case 31143:case 46161:case 46162:case 50101:case 50102:case 46160:
					item = new l1j.server.server.model.item.function.GMWand(temp);
					break;
				case 40006:case 140006:case 40007:case 40412:
					item = new l1j.server.server.model.item.function.ThunderWand(temp);
					break;
				case 40008:case 140008:case 40410:
					item = new l1j.server.server.model.item.function.PolyWand(temp);
					break;
				case 40009:
					item = new l1j.server.server.model.item.function.BanishWand(temp);
					break;
				case 41401:
					item = new l1j.server.server.model.item.function.FurnitureItem(temp);
					break;
				case 410014:case 410015:
					item = new l1j.server.server.model.item.function.NpcCallWand(temp);
					break;
				}
				break;
			case LIGHT:
				item = new l1j.server.server.model.item.function.Light(temp);
				break;
			case FIRE_CRACKER:
				item = new l1j.server.server.model.item.function.Firecracker(temp);
				break;
			case POTION:
				switch (temp.getItemId()) {
				case 40010:case 40011:case 40012:case 40019:case 40020:case 40021:case 40022:case 40023:case 40024:
				case 40026:case 40027:case 40028:case 40029:case 140029:case 240029:case 40043:case 40058:
				case 40071:case 40506:case 40930:case 41141:case 41337:case 60029:case 60030:
				case 140010:case 140011:case 140012:case 140506:case 240010:case 41403:case 410000:case 410003:
				case 30062:case 30056:case 3000085:case 60412:case 520283:case 560020:case 140022:case 140023:case 140024:
				case 60723:case 60724:case 60725: case 60413:case 130022:case 130023:case 130024:case 130046:case 31251:
				case 31839:case 31840:case 31841:case 31842:case 31843:case 31844:case 31845:
					item = new l1j.server.server.model.item.function.HealingPotion(temp);
					break;
				case 40013:case 40018:case 40039:case 40040:case 40030:case 41338:case 41261:case 41262:case 41268:case 41269:
				case 41271:case 41272:case 41273:case 41342:case 30067:case 140013:case 140018:case 410520:case 130028:
					item = new l1j.server.server.model.item.function.GreenPotion(temp);
					break;
				case 40014:case 140014:case 41415:case 30073:case 130030:// 용기의 물약, 복지 용기의 물약, 상아탑의 용기의 물약, 용기의 물약 [공성전]
				case L1ItemId.ELBEN_WAFER:case 140068:case 210110:case 30076:case 130031:// 엘븐 와퍼, 복지 엘븐 와퍼, 상아탑의 엘븐 와퍼, 엘븐 와퍼 [공성전]
				case 40031:case 30075:case 210115:case 130029:// 악마의 피, 상아탑의 악마의 피 , 복지 악마의피, 악마의 피 [공성전]
				case 210036:case 30077:case 130032:// 유그드라 열매, 상아탑의 유그드라 열매, 유그드라 열매 [공성전]
				case 40733:// 명예의 코인
					item = new l1j.server.server.model.item.function.BravePotion(temp);
					break;
				case L1ItemId.DRAGON_PEARL:case L1ItemId.DRAGON_PEARL1:case 130021:// 드래곤의 진주
					item = new l1j.server.server.model.item.function.DragonPearl(temp);
					break;
				case 40015:case 140015:case 30083:case 40736:case 41142:case 210114:case 130025:case 60415:// 마나 회복 물약
					item = new l1j.server.server.model.item.function.BluePotion(temp);
					break;
				case 40016:case 140016:case 30089:case 210113:case 130026:// 지혜의 물약
					item = new l1j.server.server.model.item.function.WisdomPotion(temp);
					break;
				case 40032:case 40041:case 41344:case 3110160:// 에바의 축복
					item = new l1j.server.server.model.item.function.BlessOfEvaPotion(temp);
					break;
				case 40025:// 불투명 물약
					item = new l1j.server.server.model.item.function.BlindPotion(temp);
					break;
				case 40017:case 30084:case 130027:// 해독제
					item = new l1j.server.server.model.item.function.CurePotion(temp);
					break;
				case 40033:case 40034:case 40035:case 40036:case 40037:case 40038:case 820020:case 820021:// 엘릭서
					item = new l1j.server.server.model.item.function.Elixir(temp);
					break;
				case 40066:case 41413:case 40067:case 41414:case 410002:case 40735:case 40042:case 41404:case 41412:case 60414:case 500220:case 43054:// 송편
					item = new l1j.server.server.model.item.function.MpPlus(temp);
					break;
				case 700000:case 700001:case 700083:case 31731:// 경험치 물약
					item = new l1j.server.server.model.item.function.ExpPlusItem(temp);
					break;
				case 410009:// 성별 전환 물약
					item = new l1j.server.server.model.item.function.TransGender(temp);
					break;
				case 210094:case 30105:case 520282:case 600346:case 30205:case 30207:case 30208:case 43051:case 30209:case 600347:// 경험치 보너스 물약
					item = new l1j.server.server.model.item.function.ExpBonusPotion(temp);
					break;
				case 51093:case 51094:case 51095:case 51096:case 51097:case 51098:case 51099:case 51100:case 51101:case 51102:// 클래스 변경
					item = new l1j.server.server.model.item.function.ClassChangeItem(temp);
					break;
				}
				break;
			case FOOD:
				item = new l1j.server.server.model.item.function.Food(temp);
				break;
			case SCROLL:
				switch (temp.getItemId()) {
				case 140100:case 40100:case 40099:case 40086:case 40863:case 340100:case 240100:
					item = new l1j.server.server.model.item.function.TeleportScroll(temp);
					break;
				case 560030:case 560031:case 560032:case 560033:case 40130:case 140130:case 40077:
				case L1ItemId.SCROLL_OF_ENCHANT_WEAPON:case L1ItemId.SCROLL_OF_ENCHANT_QUEST_WEAPON:case L1ItemId.SCROLL_OF_ENCHANT_FANTASY_WEAPON:
				case L1ItemId.B_SCROLL_OF_ENCHANT_WEAPON:case L1ItemId.C_SCROLL_OF_ENCHANT_WEAPON:case L1ItemId.SURYUNJA_WEAPON_SCROLL:
				case 60717:case 60719:case 60720:case 60721:case 60722:
				case 210085:case 210064:case 210065:case 210066:case 210067:
				case 810003:case 68076:case 31121:case 30146:case 68078:
				case 52000:case 52001:case 52002:case 52003:case 52004:case 30068:
					item = new l1j.server.server.model.item.function.EnchantWeapon(temp);
					break;
				case 40078:case L1ItemId.SCROLL_OF_ENCHANT_ARMOR:case 40129:case 140129:
				case L1ItemId.SCROLL_OF_ENCHANT_FANTASY_ARMOR:case L1ItemId.B_SCROLL_OF_ENCHANT_ARMOR:case L1ItemId.C_SCROLL_OF_ENCHANT_ARMOR:
				case L1ItemId.INADRIL_T_SCROLL_A:case L1ItemId.INADRIL_T_SCROLL_B:case L1ItemId.INADRIL_T_SCROLL_C:
				case L1ItemId.SNAPER_SCROLL:case L1ItemId.ROOMTIS_SCROLL:case L1ItemId.SURYUNJA_ARMOR_SCROLL:case L1ItemId.SURYUNJA_ACCESARY_SCROLL:
				case 900070:case 60718:case 31103:case 31104:case 3000123:case 3000124:case 3000125:
				case 210084:case 3000100:case 210068:case 68077:case 43050:
				case 31122:case 31135:case 31136:case 31100:case 31101:case 68079:case 30069:case 30147:case 68081:case 31138:case 31139:
				case 3000130:case 3000131:case 3000132:case 31129:case 31130:case 810012:case 810013:case 850003:
					item = new l1j.server.server.model.item.function.EnchantArmor(temp);
					break;
				case 40088:case 140088:case 40096:case 210112:case 60359:case 202810:case 202813:case 130041:case 60751:case 60770:case 520281:case 60771:case 60772:case 60773:case 60774:case 60775:case 8025:
					item = new l1j.server.server.model.item.function.PolyScroll(temp);
					break;
				case 40126:case 40098:// 확인 스크롤
					item = new l1j.server.server.model.item.function.IdentiScroll(temp);
					break;
				case 40089:case 140089:case 30074:case 130042:// 부활 주문서
					item = new l1j.server.server.model.item.function.ResurrectionScroll(temp);
					break;
				case 50020:case 50021:// 봉인 주문서, 봉인 해제 주문서
					item = new l1j.server.server.model.item.function.SealScroll(temp);
					break;
				case 700021:// 봉인 해제 주문서 신청서
					item = new l1j.server.server.model.item.function.SealSolveRequestScroll(temp);
					break;
				case 7010:case 7011:// 축복 주문서
					item = new l1j.server.server.model.item.function.BlessScroll(temp);
					break;
				case 830001:case 830002:case 830003:case 830004:case 830005:case 830006:case 830007:case 830008:case 830009:case 830010:case 830011:// 오만의 탑 이동 주문서
					item = new l1j.server.server.model.item.function.OmanPaper(temp);
					break;
				case 51165:case 51166:case 51167:case 51168:case 51160:case 51161:case 51162:case 51170:case 51171:case 51172:// 가호 버프
					item = new l1j.server.server.model.item.function.FavorBuff(temp);
					break;
				case 210104:// 좌표 복구 주문서
					item = new l1j.server.server.model.item.function.LocReset(temp);
					break;
				case 500035:case 210118:// 혈맹 가입 아이템
					item = new l1j.server.server.model.item.function.PledgeJoinItem(temp);
					break;
				case 200000:case 200010:// 스탯 초기화
					item = new l1j.server.server.model.item.function.ReturnStatus(temp);
					break;
				case 40090:case 40091:case 40092:case 40093:case 40094:// 빈 주문서
					item = new l1j.server.server.model.item.function.BlankPaper(temp);
					break;
				case 52005:// 영웅의 모래시계
					item = new l1j.server.server.model.item.function.sandclockOfHeroItem(temp);
					break;
				case 31162:case 410010:case 410011:case 410012:case 30063:case 31159:case 31160:case 31161:case 31252:// 전투 강화 주문서
					item = new l1j.server.server.model.item.function.BattlePaper(temp);
					break;
				case 704:case 705:// 잠재력 부여 주문서
					item = new l1j.server.server.model.item.function.PotentialScroll(temp);
					break;
				case 5564:case 5565:
					item = new l1j.server.server.model.item.function.RobotScroll(temp);
					break;
				default:
					if (interactionType == L1ItemType.SMELTING.getInteractionType()) {
						item = new l1j.server.server.model.item.function.SmeltingStone(temp);
					} else if (interactionType == L1ItemType.PURIFY.getInteractionType()) {
						item = new l1j.server.server.model.item.function.Purify(temp);
					}
					break;
				}
				break;
			case QUEST_ITEM:
				switch(temp.getItemId()){
				case 210083:// 태고의 옥쇄
					item = new l1j.server.server.model.item.function.CharacterSlotExtend(temp);
					break;
				case 41315:// 성수
					item = new l1j.server.server.model.item.function.HolysWater(temp);
					break;
				case 41354:// 신성한 에바의 물
					item = new l1j.server.server.model.item.function.HolysEvaWater(temp);
					break;
				case 41316:// 신성한 미스릴 가루
					item = new l1j.server.server.model.item.function.HolysMithrilDust(temp);
					break;
				}
				break;
			case SPELL_BOOK:
				item = new l1j.server.server.model.item.function.Spellbook(temp);
				break;
			case OTHER:
				switch(temp.getItemId()){
				case 560024:case 560025:case 560027:case 560028:case 560029:case 560026:case 560023:case 560022:case 560040:case 560041:
					item = new l1j.server.server.model.item.function.TeleportBook(temp);
					break;
				case L1ItemId.INN_ROOM_KEY:case L1ItemId.INN_HALL_KEY:item = new l1j.server.server.model.item.function.InnKey(temp);break;
				case 89136:item = new l1j.server.server.model.item.function.CharacterWarehouseKey(temp);break;
				case 41260:item = new l1j.server.server.model.item.function.Firewood(temp);break;// 장착
				case 40003:item = new l1j.server.server.model.item.function.OilLanterns(temp);break;// 랜턴오일
				case 30107:case 30108:case 30109:case 30110:case 30111:case 30112:case 30113:case 30114:case 30115:case 30116:
					item = new l1j.server.server.model.item.function.WhiteTScroll(temp);
					break;
				case 40903:case 40904:case 40905:case 40906:case 40907:case 40908:// 각종 약혼 반지
					item = new l1j.server.server.model.item.function.MarryRing(temp);
					break;
				case 410064:case 410139:case 410138:case 1000002:case 60768:case 1000003:case 1000004:case 1000044:
				case 1000007:case 510139:case 1000009:case 7241:case 60255:case 600342:case 600343:case 600344:case 600348:
					item = new l1j.server.server.model.item.function.DragonGemstone(temp);
					break;
				case 210097:case 210098:case 210099:case 210100:case 210101:case 210102:case 210103:case 210116:case 210117:case 8003:
					item = new l1j.server.server.model.item.function.PolyItem(temp);
					break;
				case 41383:case 41384:case 41385:case 41386:case 41387:case 41388:case 41389:case 41390:case 41391:
				case 41392:case 41393:case 41394:case 41395:case 41396:case 41397:case 41398:case 41399:case 41400:
				case 49072:case 49073:case 49074:case 49075:case 49076:
					item = new l1j.server.server.model.item.function.FurnitureItem(temp);
					break;
				case 40304:case 40305:case 40306:case 40307:// 유산
					item = new l1j.server.server.model.item.function.Miscarriage(temp);
					break;
				case 40317:case 30087:case 130039:// 숫돌
					item = new l1j.server.server.model.item.function.Fix(temp);
					break;
				case 600226:case 600227:// 탐
					item = new l1j.server.server.model.item.function.TamFruit(temp);
					break;
				case 830012:case 830013:case 830014:case 830015:case 830016:case 830017:case 830018:case 830019:case 830020:case 830021:
				case 830022:case 830023:case 830024:case 830025:case 830026:case 830027:case 830028:case 830029:case 830030:case 830031:
				case 840022:case 840023:case 840024:case 840025:case 840026:case 840027:case 840028:case 840029:case 840030:case 840031:
					item = new l1j.server.server.model.item.function.OmanAmulet(temp);// 오만의 탑 이동 부적
					break;
				case 830042:case 830043:case 830044:case 830045:case 830046:case 830047:case 830048:case 830049:case 830050:case 830051:
				case 830052:case 830053:case 830054:case 830055:case 830056:case 830057:case 830058:case 830059:case 830060:case 830061:
					item = new l1j.server.server.model.item.function.OmanRandomAmulet(temp);// 혼돈의, 변이된 오만의 탑 부적
					break;
				case 41245:item = new l1j.server.server.model.item.function.Resolvent(temp);break;
				case 5558:case 6670:case 6671:case 6672:case 6673:case 6674:case 6675:case 6676:case 6677:case 6678:case 6679:
				case 6690:case 6691:case 6692:case 6693:case 6694:case 6695:case 6696:case 6697:case 6698:case 6699:
				case 6710:case 6711:case 6712:case 6713:case 6714:case 6715:case 6716:case 6717:case 6718:case 6719:
					item = new l1j.server.server.model.item.function.RangkingBuff(temp);
					break;
				case 410057:case 410058:case 410059:case 410060:
					item = new l1j.server.server.model.item.function.FeatheerLuckyBuff(temp);
					break;
				case 31118:case 31119:case 31120:
					item = new l1j.server.server.model.item.function.IceStatBuff(temp);
					break;
				case 40325:case 40326:case 40327:case 40328:// 주사위
					item = new l1j.server.server.model.item.function.Dice(temp);
					break;
				case 600222:case 600223:case 600225:// PC방 버프
					item = new l1j.server.server.model.item.function.PCCafeBuff(temp);
					break;
				case 410032:case 410033:case 410034:case 410035:case 410036:case 410037:case 410038:case 3000031:case 450011:case 450012:// 마안
					item = new l1j.server.server.model.item.function.MaanBuff(temp);
					break;
				case 490012: case 490013: case 490014: case 490015: case 490016:case 6022:// 드래곤 키
					item = new l1j.server.server.model.item.function.DragonKey(temp);
					break;
				case 3200001:case L1ItemId.PET_AMULET:
				case 3200015:case 3200016:case 3200017:
				case 3200101:case 3200102:case 3200103:case 3200104:
				case 3200009:case 3200006:case 3200020:case 3200021:case 3200007:
				case 3200008:case 3200010:case 3200013:
				case 3200051:case 3200052:case 3200053:case 3200054:case 3200055:case 3200056:case 3200057:case 3200058:case 3200059:
				case 3200060:case 3200061:case 3200062:case 3200063:case 3200064:case 3200065:case 3200066:case 3200067:case 3200068:
				case 3200069:case 3200070:case 3200071:
					item = new l1j.server.server.model.item.function.CompanionItem(temp);
					break;
				case 1000024:case 1000025:case 1000026:case 1000027:case 1000028:case 1000029:
					item = new l1j.server.server.model.item.function.NcoinCharge(temp);
					break;
				case 400253:case 31110:case 31132:case L1ItemId.CHEQUE:case 31109:case 31131:
					item = new l1j.server.server.model.item.function.AdenaChange(temp);
					break;
				case 1000041:case 1000042:case 1000043:case 1000033:case 1000034:// 아인하사드 포인트 충전
					item = new l1j.server.server.model.item.function.EinPointChargeItem(temp);
					break;
				case 1000045:case 1000046:case 1000047:// 아인하사드 스페셜 주문서
					item = new l1j.server.server.model.item.function.EinStatusCharge(temp);
					break;
				case 700022:// 기억 확장 구슬
					item = new l1j.server.server.model.item.function.RememberExtendMarble(temp);
					break;
				case 700023:case 700024:case 700025:// 기억의 구슬
					item = new l1j.server.server.model.item.function.RememberAddMarble(temp);
					break;
				case 700028:// 케플리샤의 기억 저장 구슬
					item = new l1j.server.server.model.item.function.RememberSaveMarble(temp);
					break;
				case 700078:// 메티스의 가호
					item = new l1j.server.server.model.item.function.GmHeal(temp);
					break;
				case 700079:// 몬스터 청소기
					item = new l1j.server.server.model.item.function.GmMonsterClear(temp);
					break;
				case L1ItemId.TREASURE_SHOVEL:// 보물 탐지 삽
					item = new l1j.server.server.model.item.function.TreasureDetectShovel(temp);
					break;
				case L1ItemId.SPELL_MELT:// 스킬 용해제
					item = new l1j.server.server.model.item.function.SpellMelt(temp);
					break;
				case 31480:// 잭 선장의 수경
					item = new l1j.server.server.model.item.function.VisualOfCaptain(temp);
					break;
				case 31481:// 먹음직스러운 복어
					item = new l1j.server.server.model.item.function.PufferFish(temp);
					break;
				case 31235:// 영광의 아지트 열쇠
				case 31236:// 영광의 아지트 초대장
					item = new l1j.server.server.model.item.function.SoloAgitKey(temp);
					break;
				case 28889:case 28890:case 28891:// 아덴의 완력 주문서, 아덴의 민첩 주문서, 아덴의 지식 주문서
					item = new l1j.server.server.model.item.function.AdenStatScroll(temp);
					break;
				default:
					break;
				}
				break;
			case MATERIAL:
				switch(temp.getItemId()){
				case 3200003:case 3200004:case 3200005:
					item = new l1j.server.server.model.item.function.CompanionItem(temp);
					break;
				case 40493:
					item = new l1j.server.server.model.item.function.MagicFlute(temp);
					break;
				case 40507:// 엔트의 줄기
					item = new l1j.server.server.model.item.function.CurePotion(temp);
					break;
				}
				break;
			case STING:
				item = new l1j.server.server.model.item.function.Sting(temp);
				break;
			default:
				break;// NORMAL TYPE END
			}
			if (interactionType == L1ItemType.MAGICDOLL.getInteractionType()) {
				item = new l1j.server.server.model.item.function.MagicDoll(temp);
			} else if (L1Fishing.isRil(temp.getItemId())) {
				item = new l1j.server.server.model.item.function.FishingRil(temp);
			} else if (L1Fishing.isRod(temp.getItemId())) {
				item = new l1j.server.server.model.item.function.FishingRod(temp);
			} else if (SpellMeltTable.isSpellMeltItem(temp.getItemId())) {
				item = new l1j.server.server.model.item.function.ItemSelector(temp, SelectorType.SPELL);
			} else if (ItemSelectorTable.isSelectorInfo(temp.getItemId())) {
				item = new l1j.server.server.model.item.function.ItemSelector(temp);
			} else if (ItemSelectorTable.isSelectorWareInfo(temp.getItemId())) {
				item = new l1j.server.server.model.item.function.ItemSelectorWarehouse(temp);
			} else if (temp.getItemId() == Config.TJ.TJ_COUPON_ITEMID) {
				item = new l1j.server.server.model.item.function.TJCouponUse(temp);
			} else if (ItemClickMessageTable.isMessageItem(temp.getItemId())) {
				item = new l1j.server.server.model.item.function.ItemClickMessageSend(temp);
			} else if (PolyTable.isPolyItem(temp.getItemId())) {
				item = new l1j.server.server.model.item.function.PolyItem(temp, PolyTable.getPolyItem(temp.getItemId()));
			} else if (ItemBoxTable.isBoxItem(temp.getItemId())) {
				item = new l1j.server.server.model.item.function.ItemBoxOpen(temp);
			} else if (L1DungeonTimerLoader.isDungeonTimerItem(temp.getItemId())) {
				item = new l1j.server.server.model.item.function.ChargeDungeonTime(temp);
			} else if (CatalystTable.isCatalystCustom(temp.getItemId())) {
				item = new l1j.server.server.model.item.function.CatalystCustom(temp);
			} else if (CatalystTableCommonBinLoader.isCatalyst(item_name_id)) {
				item = new l1j.server.server.model.item.function.Catalyst(temp);
			} else if (ItemBuffTable.isItemBuff(temp.getItemId())) {
				item = new l1j.server.server.model.item.function.ItemBuffUse(temp);
			}
			break;// NORMAL END
		default:
			break;
		}
		if (item != null) {
			item.setWorking(true);// flag 설정
			return item;
		}
		return new L1ItemInstance(temp);
	}
	
	public L1ItemInstance createItem(int itemId) {
		L1Item temp = getTemplate(itemId);
		if (temp == null) {
			return null;
		}
		L1ItemInstance item = FunctionItem(temp);
		item.setId(IdFactory.getInstance().nextId());
		item.setItem(temp);
		item.setBless(temp.getBless());
		L1World.getInstance().storeObject(item);
		return item;
	}
	
	public L1ItemInstance createItem(L1Item temp) {
		if (temp == null) {
			return null;
		}
		L1ItemInstance item = FunctionItem(temp);
		item.setId(IdFactory.getInstance().nextId());
		item.setItem(temp);
		item.setBless(temp.getBless());
		L1World.getInstance().storeObject(item);
		return item;
	}
	
	public L1ItemInstance createItem(String desc) {
		int itemId = findItemIdByDescKr(desc);
		L1Item temp = getTemplate(itemId);
		if (temp == null) {
			return null;
		}
		L1ItemInstance item = FunctionItem(temp);
		item.setId(IdFactory.getInstance().nextId());
		item.setItem(temp);
		item.setBless(temp.getBless());
		L1World.getInstance().storeObject(item);
		return item;
	}
	
	public L1ItemInstance createItem(int itemId, int objId) {
		L1Item temp = getTemplate(itemId);
		if (temp == null) {
			return null;
		}
		L1ItemInstance item = FunctionItem(temp);
		item.setId(objId);
		item.setItem(temp);
		item.setBless(temp.getBless());
		L1World.getInstance().storeObject(item);
		return item;
	}

	/*인형 경주 추가*/
	public void AddTicket(L1RaceTicket race){
		_race.put(race.getItemId(), race);
		_allTemplates.put(race.getItemId(), race);
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO race_tickets VALUES(?, ?, 500)");
			pstm.setInt(1, race.getItemId());
			//pstm.setString(2, race.getDescKr());
			pstm.setString(2, race.getDescEn());
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	public int GetRaceTicketId(){
		return _raceTicketId++;
	}
	
	/*투견 추가*/
	public void AddDogFightTicket(L1DogFightTicket fight){
		_dogfight.put(fight.getItemId(), fight);
		_allTemplates.put(fight.getItemId(), fight);
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO dogfight_tickets VALUES(?, ?, 500)");
			pstm.setInt(1, fight.getItemId());
			//pstm.setString(2, fight.getDescKr());
			pstm.setString(2, fight.getDescEn());
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	public int GetDogFightTicketId(){
		return _dogfightTicketId++;
	}

	/** 새로운 Template 객체를 생성(복사) */
	public L1Item clone(L1Item item, String name) {
		switch (item.getItemType()) {
		case WEAPON:
			L1Weapon weapon = new L1Weapon();
			weapon.setItemId(item.getItemId());
			weapon.setItemNameId(item.getItemNameId());
			//weapon.setDescKr(item.getDescKr());
			weapon.setDescKr(item.getDescEn());
			weapon.setDesc(item.getDesc());
			weapon.setItemGrade(item.getItemGrade());
			weapon.setType(item.getType());
			weapon.setItemType(item.getItemType());
			weapon.setWeaponType(item.getWeaponType());
			weapon.set_interaction_type(item.get_interaction_type());
			weapon.setMaterial(item.getMaterial());
			weapon.setWeight(item.getWeight());
			weapon.setIconId(item.getIconId());
			weapon.setSpriteId(item.getSpriteId());
			weapon.setDmgSmall(item.getDmgSmall());
			weapon.setDmgLarge(item.getDmgLarge());
			weapon.setSafeEnchant(item.getSafeEnchant());
			weapon.setUseRoyal(item.isUseRoyal());
			weapon.setUseKnight(item.isUseKnight());
			weapon.setUseElf(item.isUseElf());
			weapon.setUseMage(item.isUseMage());
			weapon.setUseDarkelf(item.isUseDarkelf());
			weapon.setUseDragonKnight(item.isUseDragonKnight());
			weapon.setUseIllusionist(item.isUseIllusionist());
			weapon.setUseWarrior(item.isUseWarrior());
			weapon.setUseFencer(item.isUseFencer());
			weapon.setUseLancer(item.isUseLancer());
			if (L1ItemWeaponType.isLongWeapon(weapon.getWeaponType())) {
				weapon.setBowHitRate(weapon.getBowHitRate());
				weapon.setBowDmgRate(weapon.getBowDmgRate());
			} else {
				weapon.setHitRate(weapon.getHitRate());
				weapon.setDmgRate(weapon.getDmgRate());
			}
			weapon.setAddStr(item.getAddStr());
			weapon.setAddDex(item.getAddDex());
			weapon.setAddCon(item.getAddCon());
			weapon.setAddInt(item.getAddInt());
			weapon.setAddWis(item.getAddWis());
			weapon.setAddCha(item.getAddCha());
			weapon.setAddHp(item.getAddHp());
			weapon.setAddMp(item.getAddMp());
			weapon.setAddHpr(item.getAddHpr());
			weapon.setAddMpr(item.getAddMpr());
			weapon.setAddSp(item.getAddSp());
			weapon.setMr(item.getMr());
			weapon.setDoubleDmgChance(item.getDoubleDmgChance());
			weapon.setMagicDmgModifier(item.getMagicDmgModifier());
			weapon.setCanbeDmg(item.getCanbeDmg());
			weapon.setMinLevel(item.getMinLevel());
			weapon.setMaxLevel(item.getMaxLevel());
			weapon.setBless(item.getBless());
			weapon.setTradable(item.isTradable());
			weapon.setRetrieve(item.isRetrieve());
			weapon.setSpecialRetrieve(item.isSpecialRetrieve());
			weapon.setCantDelete(item.isCantDelete());
			weapon.setCantSell(item.isCantSell());
			weapon.setHasteItem(item.isHasteItem());
			weapon.setMaxUseTime(item.getMaxUseTime());
			weapon.setToleranceSkill(item.getToleranceSkill());
			weapon.setToleranceSpirit(item.getToleranceSpirit());
			weapon.setToleranceDragon(item.getToleranceDragon());
			weapon.setToleranceFear(item.getToleranceFear());
			weapon.setToleranceAll(item.getToleranceAll());
			weapon.setHitupSkill(item.getHitupSkill());
			weapon.setHitupSpirit(item.getHitupSpirit());
			weapon.setHitupDragon(item.getHitupDragon());
			weapon.setHitupFear(item.getHitupFear());
			weapon.setHitupAll(item.getHitupAll());
			weapon.setHitupMagic(item.getHitupMagic());
			weapon.setDamageReduction(item.getDamageReduction());
			weapon.setMagicDamageReduction(item.getMagicDamageReduction());
			weapon.setDamageReductionEgnor(item.getDamageReductionEgnor());
			weapon.setDamageReductionPercent(item.getDamageReductionPercent());
			weapon.setPVPDamage(item.getPVPDamage());
			weapon.setPVPDamageReduction(item.getPVPDamageReduction());
			weapon.setPVPDamageReductionPercent(item.getPVPDamageReductionPercent());
			weapon.setPVPMagicDamageReduction(item.getPVPMagicDamageReduction());
			weapon.setPVPReductionEgnor(item.getPVPReductionEgnor());
			weapon.setPVPMagicDamageReductionEgnor(item.getPVPMagicDamageReductionEgnor());
			weapon.setAbnormalStatusDamageReduction(item.getAbnormalStatusDamageReduction());
			weapon.setAbnormalStatusPVPDamageReduction(item.getAbnormalStatusPVPDamageReduction());
			weapon.setPVPDamagePercent(item.getPVPDamagePercent());
			weapon.setExpBonus(item.getExpBonus());
			weapon.setRestExpReduceEfficiency(item.getRestExpReduceEfficiency());
			weapon.setShortCritical(item.getShortCritical());
			weapon.setLongCritical(item.getLongCritical());
			weapon.setMagicCritical(item.getMagicCritical());
			weapon.setAddDg(item.getAddDg());
			weapon.setAddEr(item.getAddEr());
			weapon.setAddMe(item.getAddMe());
			weapon.setPoisonRegist(item.isPoisonRegist());
			weapon.setImunEgnor(item.getImunEgnor());
			weapon.setStunDuration(item.getStunDuration());
			weapon.setTripleArrowStun(item.getTripleArrowStun());
			weapon.setStrangeTimeIncrease(item.getStrangeTimeIncrease());
			weapon.setStrangeTimeDecrease(item.getStrangeTimeDecrease());
			weapon.setPotionRegist(item.getPotionRegist());
			weapon.setPotionPercent(item.getPotionPercent());
			weapon.setPotionValue(item.getPotionValue());
			weapon.setHprAbsol32Second(item.getHprAbsol32Second());
			weapon.setMprAbsol64Second(item.getMprAbsol64Second());
			weapon.setMprAbsol16Second(item.getMprAbsol16Second());
			weapon.setIncreaseArmorSkillProb(item.getIncreaseArmorSkillProb());
			weapon.setAttackSpeedDelayRate(item.getAttackSpeedDelayRate());
			weapon.setMoveSpeedDelayRate(item.getMoveSpeedDelayRate());
			weapon.setEnchantInfo(item.getEnchantInfo());
			weapon.setTwohandedWeapon(item.isTwohandedWeapon());
			weapon.setWeaponAddDamage(item.getWeaponAddDamage());
			weapon.setBin(item.getBin());
			return weapon;
		case ARMOR:
			L1Armor armor = new L1Armor();
			armor.setItemId(item.getItemId());
			armor.setItemNameId(item.getItemNameId());
			//armor.setDescKr(item.getDescKr());
			armor.setDescKr(item.getDescEn());
			armor.setDesc(item.getDesc());
			armor.setItemGrade(item.getItemGrade());
			armor.setType(item.getType());
			armor.setItemType(item.getItemType());
			armor.set_interaction_type(item.get_interaction_type());
			armor.setMaterial(item.getMaterial());
			armor.setWeight(item.getWeight());
			armor.setIconId(item.getIconId());
			armor.setSpriteId(item.getSpriteId());
			armor.setAc(item.getAc());
			armor.setAcSub(item.getAcSub());
			armor.setSafeEnchant(item.getSafeEnchant());
			armor.setUseRoyal(item.isUseRoyal());
			armor.setUseKnight(item.isUseKnight());
			armor.setUseElf(item.isUseElf());
			armor.setUseMage(item.isUseMage());
			armor.setUseDarkelf(item.isUseDarkelf());
			armor.setUseDragonKnight(item.isUseDragonKnight());
			armor.setUseIllusionist(item.isUseIllusionist());
			armor.setUseWarrior(item.isUseWarrior());
			armor.setUseFencer(item.isUseFencer());
			armor.setUseLancer(item.isUseLancer());
			armor.setAddStr(item.getAddStr());
			armor.setAddCon(item.getAddCon());
			armor.setAddDex(item.getAddDex());
			armor.setAddInt(item.getAddInt());
			armor.setAddWis(item.getAddWis());
			armor.setAddCha(item.getAddCha());
			armor.setAddHp(item.getAddHp());
			armor.setAddMp(item.getAddMp());
			armor.setAddHpr(item.getAddHpr());
			armor.setAddMpr(item.getAddMpr());
			armor.setAddSp(item.getAddSp());
			armor.setMinLevel(item.getMinLevel());
			armor.setMaxLevel(item.getMaxLevel());
			armor.setMr(item.getMr());
			armor.setCarryBonus(item.getCarryBonus());
			armor.setBowHitRate(item.getBowHitRate());
			armor.setHasteItem(item.isHasteItem());
			armor.setBless(item.getBless());
			armor.setTradable(item.isTradable());
			armor.setRetrieve(item.isRetrieve());
			armor.setSpecialRetrieve(item.isSpecialRetrieve());
			armor.setRetrieveEnchantLevel(item.getRetrieveEnchantLevel());
			armor.setCantDelete(item.isCantDelete());
			armor.setCantSell(item.isCantSell());
			armor.setAttrEarth(item.getAttrEarth());
			armor.setAttrWater(item.getAttrWater());
			armor.setAttrWind(item.getAttrWind());
			armor.setAttrFire(item.getAttrFire());
			armor.setAttrAll(item.getAttrAll());
			armor.setRegistStone(item.getRegistStone());
			armor.setRegistSleep(item.getRegistSleep());
			armor.setRegistFreeze(item.getRegistFreeze());
			armor.setRegistBlind(item.getRegistBlind());
			armor.setToleranceSkill(item.getToleranceSkill());
			armor.setToleranceSpirit(item.getToleranceSpirit());
			armor.setToleranceDragon(item.getToleranceDragon());
			armor.setToleranceFear(item.getToleranceFear());
			armor.setToleranceAll(item.getToleranceAll());
			armor.setHitupSkill(item.getHitupSkill());
			armor.setHitupSpirit(item.getHitupSpirit());
			armor.setHitupDragon(item.getHitupDragon());
			armor.setHitupFear(item.getHitupFear());
			armor.setHitupAll(item.getHitupAll());
			armor.setHitupMagic(item.getHitupMagic());
			armor.setDamageReduction(item.getDamageReduction());
			armor.setMagicDamageReduction(item.getMagicDamageReduction());
			armor.setDamageReductionEgnor(item.getDamageReductionEgnor());
			armor.setDamageReductionPercent(item.getDamageReductionPercent());
			armor.setPVPDamage(item.getPVPDamage());
			armor.setPVPDamageReduction(item.getPVPDamageReduction());
			armor.setPVPDamageReductionPercent(item.getPVPDamageReductionPercent());
			armor.setPVPMagicDamageReduction(item.getPVPMagicDamageReduction());
			armor.setPVPReductionEgnor(item.getPVPReductionEgnor());
			armor.setPVPMagicDamageReductionEgnor(item.getPVPMagicDamageReductionEgnor());
			armor.setAbnormalStatusDamageReduction(item.getAbnormalStatusDamageReduction());
			armor.setAbnormalStatusPVPDamageReduction(item.getAbnormalStatusPVPDamageReduction());
			armor.setPVPDamagePercent(item.getPVPDamagePercent());
			armor.setExpBonus(item.getExpBonus());
			armor.setRestExpReduceEfficiency(item.getRestExpReduceEfficiency());
			armor.setShortCritical(item.getShortCritical());
			armor.setLongCritical(item.getLongCritical());
			armor.setMagicCritical(item.getMagicCritical());
			armor.setAddDg(item.getAddDg());
			armor.setAddEr(item.getAddEr());
			armor.setAddMe(item.getAddMe());
			armor.setPoisonRegist(item.isPoisonRegist());
			armor.setImunEgnor(item.getImunEgnor());
			armor.setStunDuration(item.getStunDuration());
			armor.setTripleArrowStun(item.getTripleArrowStun());
			armor.setStrangeTimeIncrease(item.getStrangeTimeIncrease());
			armor.setStrangeTimeDecrease(item.getStrangeTimeDecrease());
			armor.setPotionRegist(item.getPotionRegist());
			armor.setPotionPercent(item.getPotionPercent());
			armor.setPotionValue(item.getPotionValue());
			armor.setHprAbsol32Second(item.getHprAbsol32Second());
			armor.setMprAbsol64Second(item.getMprAbsol64Second());
			armor.setMprAbsol16Second(item.getMprAbsol16Second());
			armor.setIncreaseArmorSkillProb(item.getIncreaseArmorSkillProb());
			armor.setAttackSpeedDelayRate(item.getAttackSpeedDelayRate());
			armor.setMoveSpeedDelayRate(item.getMoveSpeedDelayRate());
			armor.setMaxUseTime(item.getMaxUseTime());
			armor.setPolyDescId(item.getPolyDescId());
			armor.setEnchantInfo(item.getEnchantInfo());
			armor.setBin(item.getBin());
			return armor;
		case NORMAL:
			L1EtcItem etc = new L1EtcItem();
			etc.setItemId(item.getItemId());
			etc.setItemNameId(item.getItemNameId());
			etc.setDescKr(name);
			etc.setDesc(item.getDesc());
			etc.setItemGrade(item.getItemGrade());
			etc.setType(item.getType());
			etc.set_interaction_type(item.get_interaction_type());
			etc.setItemType(item.getItemType());
			etc.setMaterial(item.getMaterial());
			etc.setWeight(item.getWeight());
			etc.setIconId(item.getIconId());
			etc.setSpriteId(item.getSpriteId());
			etc.setMinLevel(item.getMinLevel());
			etc.setMaxLevel(item.getMaxLevel());
			etc.setBless(item.getBless());
			etc.setTradable(item.isTradable());
			etc.setRetrieve(item.isRetrieve());
			etc.setSpecialRetrieve(item.isSpecialRetrieve());
			etc.setCantDelete(item.isCantDelete());
			etc.setCantSell(item.isCantSell());
			etc.setDmgSmall(item.getDmgSmall());
			etc.setDmgLarge(item.getDmgLarge());
			etc.setAcBonus(item.getAcBonus());
			etc.setDmgRate(item.getDmgRate());
			etc.setHitRate(item.getHitRate());
			etc.setBowDmgRate(item.getBowDmgRate());
			etc.setBowHitRate(item.getBowHitRate());
			etc.setAddStr(item.getAddStr());
			etc.setAddCon(item.getAddCon());
			etc.setAddDex(item.getAddDex());
			etc.setAddInt(item.getAddInt());
			etc.setAddWis(item.getAddWis());
			etc.setAddCha(item.getAddCha());
			etc.setAddHp(item.getAddHp());
			etc.setAddMp(item.getAddMp());
			etc.setAddHpr(item.getAddHpr());
			etc.setAddMpr(item.getAddMpr());
			etc.setAddSp(item.getAddSp());
			etc.setMr(item.getMr());
			etc.setCarryBonus(item.getCarryBonus());
			etc.setAttrEarth(item.getAttrEarth());
			etc.setAttrWater(item.getAttrWater());
			etc.setAttrWind(item.getAttrWind());
			etc.setAttrFire(item.getAttrFire());
			etc.setAttrAll(item.getAttrAll());
			etc.setRegistStone(item.getRegistStone());
			etc.setRegistSleep(item.getRegistSleep());
			etc.setRegistFreeze(item.getRegistFreeze());
			etc.setRegistBlind(item.getRegistBlind());
			etc.setToleranceSkill(item.getToleranceSkill());
			etc.setToleranceSpirit(item.getToleranceSpirit());
			etc.setToleranceDragon(item.getToleranceDragon());
			etc.setToleranceFear(item.getToleranceFear());
			etc.setToleranceAll(item.getToleranceAll());
			etc.setHitupSkill(item.getHitupSkill());
			etc.setHitupSpirit(item.getHitupSpirit());
			etc.setHitupDragon(item.getHitupDragon());
			etc.setHitupFear(item.getHitupFear());
			etc.setHitupAll(item.getHitupAll());
			etc.setHitupMagic(item.getHitupMagic());
			etc.setDamageReduction(item.getDamageReduction());
			etc.setMagicDamageReduction(item.getMagicDamageReduction());
			etc.setDamageReductionEgnor(item.getDamageReductionEgnor());
			etc.setDamageReductionPercent(item.getDamageReductionPercent());
			etc.setPVPDamage(item.getPVPDamage());
			etc.setPVPDamageReduction(item.getPVPDamageReduction());
			etc.setPVPDamageReductionPercent(item.getPVPDamageReductionPercent());
			etc.setPVPMagicDamageReduction(item.getPVPMagicDamageReduction());
			etc.setPVPReductionEgnor(item.getPVPReductionEgnor());
			etc.setPVPMagicDamageReductionEgnor(item.getPVPMagicDamageReductionEgnor());
			etc.setAbnormalStatusDamageReduction(item.getAbnormalStatusDamageReduction());
			etc.setAbnormalStatusPVPDamageReduction(item.getAbnormalStatusPVPDamageReduction());
			etc.setPVPDamagePercent(item.getPVPDamagePercent());
			etc.setExpBonus(item.getExpBonus());
			etc.setRestExpReduceEfficiency(item.getRestExpReduceEfficiency());
			etc.setShortCritical(item.getShortCritical());
			etc.setLongCritical(item.getLongCritical());
			etc.setMagicCritical(item.getMagicCritical());
			etc.setAddDg(item.getAddDg());
			etc.setAddEr(item.getAddEr());
			etc.setAddMe(item.getAddMe());
			etc.setPoisonRegist(item.isPoisonRegist());
			etc.setImunEgnor(item.getImunEgnor());
			etc.setStunDuration(item.getStunDuration());
			etc.setTripleArrowStun(item.getTripleArrowStun());
			etc.setStrangeTimeIncrease(item.getStrangeTimeIncrease());
			etc.setStrangeTimeDecrease(item.getStrangeTimeDecrease());
			etc.setPotionRegist(item.getPotionRegist());
			etc.setPotionPercent(item.getPotionPercent());
			etc.setPotionValue(item.getPotionValue());
			etc.setHprAbsol32Second(item.getHprAbsol32Second());
			etc.setMprAbsol64Second(item.getMprAbsol64Second());
			etc.setMprAbsol16Second(item.getMprAbsol16Second());
			etc.setIncreaseArmorSkillProb(item.getIncreaseArmorSkillProb());
			etc.setAttackSpeedDelayRate(item.getAttackSpeedDelayRate());
			etc.setMoveSpeedDelayRate(item.getMoveSpeedDelayRate());
			etc.setBuffDurationSecond(item.getBuffDurationSecond());
			
			etc.setMerge(item.isMerge());
			etc.setMaxChargeCount(item.getMaxChargeCount());
			etc.set_locx(item.getLocX());
			etc.set_locy(item.getLocY());
			etc.set_mapid(item.getMapId());
			etc.setDelayId(item.getDelayId());
			etc.setDelayTime(item.getDelayTime());
			etc.setDelayEffect(item.getDelayEffect());
			etc.setFoodVolume(item.getFoodVolume());
			etc.setToBeSavedAtOnce(item.isToBeSavedAtOnce());
			etc.setSkillLevel(item.getSkillLevel());
			etc.setSkillAttr(item.getSkillAttr());
			etc.setAlignment(item.getAlignment());
			etc.setUseRoyal(item.isUseRoyal());
			etc.setUseKnight(item.isUseKnight());
			etc.setUseElf(item.isUseElf());
			etc.setUseMage(item.isUseMage());
			etc.setUseDarkelf(item.isUseDarkelf());
			etc.setUseDragonKnight(item.isUseDragonKnight());
			etc.setUseIllusionist(item.isUseIllusionist());
			etc.setUseWarrior(item.isUseWarrior());
			etc.setUseFencer(item.isUseFencer());
			etc.setUseLancer(item.isUseLancer());
			if (etc.getType() == L1ItemNormalType.SPELL_BOOK.getId()) {
				etc.setSkillType(item.getSkillType());
			}
			etc.setEtcValue(item.getEtcValue());
			etc.setLimitType(item.getLimitType());
			etc.setBin(item.getBin());
			return etc;
		default:
			return null;
		}
	}
	
}

