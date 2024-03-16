package l1j.server.server.templates;

import java.io.Serializable;

import l1j.server.GameSystem.beginnerquest.bean.L1QuestCollectItem;
import l1j.server.common.bin.item.CommonItemInfo;
import l1j.server.common.data.Material;
import l1j.server.server.construct.L1Alignment;
import l1j.server.server.construct.L1Grade;
import l1j.server.server.construct.item.L1ItemLimitType;
import l1j.server.server.construct.item.L1ItemSpellBookAttr;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.construct.item.L1ItemWeaponType;
import l1j.server.server.model.item.ablity.enchant.L1EnchantFactory;
import l1j.server.server.model.item.function.L1HealingPotion;
import l1j.server.server.model.skill.L1SkillType;

public abstract class L1Item implements Serializable {
	private static final long serialVersionUID = 1L;

	public L1Item() {
	}

	// ■■■■■■ L1EtcItem, L1Weapon, L1Armor 에 공통되는 항목 ■■■■■■
	private L1ItemType _itemType;
	/**
	 * L1EtcItem, L1Weapon, L1Armor
	 * 아이템 대분류
	 * @return L1ItemType
	 */
	public L1ItemType getItemType() {
		return _itemType;
	}
	public void setItemType(L1ItemType type) {
		_itemType = type;
	}

	private int _itemId;
	/**
	 * db_id
	 * @return int
	 */
	public int getItemId() {
		return _itemId;
	}
	public void setItemId(int val) {
		_itemId = val;
	}
	
	private int _item_name_Id;
	/**
	 * 리니지 월드 아이템 고유번호(item-common.bin : name_id)
	 * @return int
	 */
	public int getItemNameId() {
		return _item_name_Id;
	}
	public void setItemNameId(int val) {
		_item_name_Id = val;
	}

	private String _desc_kr;
	/**
	 * desc_id의 한글명
	 * @return String
	 */
	public String getDescKr() {
		return _desc_kr;
	}
	public void setDescKr(String val) {
		_desc_kr = val;
	}

	private String _desc_en;
	/**
	 * desc_id의 한글명
	 * @return String
	 */
	public String getDescEn() {
		return _desc_en;
	}
	public void setDescEn(String val) {
		_desc_en = val;
	}
	
	private String _desc;
	/**
	 * desc-k.tbl의 아이템 치환 명 $%d
	 * @return String
	 */
	public String getDesc() {
		return _desc;
	}
	public void setDesc(String val) {
		_desc = val;
	}
	
	private String _magicName;
	public String getMagicName() {
		return _magicName;
	}
	public void setMagicName(String name) {
		_magicName = name;
	}
	
	private L1Grade itemGrade;
	/**
	 * 일반, 고급, 희귀, 영웅, 전설, 신화, 유일
	 * @return L1ItemGrade
	 */
	public L1Grade getItemGrade(){
		return itemGrade;
	}
	public void setItemGrade(L1Grade value){
		itemGrade = value;
	}

	private int _type;
	/**
	 * 아이템의 종류를 돌려준다.<br>
	 * 
	 * @return
	 * <p>
	 * [etcitem]<br>
	 * 0:arrow, 1:wand, 2:light, 3:gem, 4:totem, 5:firecracker, 6:potion,
	 * 7:food, 8:scroll, 9:questitem, 10:spellbook, 11:petitem, 12:other,
	 * 13:material, 14:event, 15:sting
	 * </p>
	 * <p>
	 * [weapon]<br>
	 * 1:sword, 2:dagger, 3:tohandsword, 4:bow, 5:spear, 6:blunt, 7:staff,
	 * 8:throwingknife, 9:arrow, 10:gauntlet, 11:claw, 12:edoryu, 13:singlebow,
	 * 14:singlespear, 15:tohandblunt, 16:tohandstaff, 17:keyring, 18:chainsword
	 * </p>
	 * <p>
	 * [armor]<br>
	 * 1:helm, 2:armor, 3:T, 4:cloak, 5:glove, 6:boots, 7:shield, 8:amulet목걸이,
	 * 9:ring반지, 10:belt벨트, 11:ring2반지, 12:earring귀걸이, 13:garder가더, 14:ron룬, 15:pair각반, 
	 * 16:sentence문장, 17:shoulder견갑, 18:badge휘장, 19:펜던트
	 */
	public int getType() {
		return _type;
	}
	public void setType(int type) {
		_type = type;
	}

	private Material _material;
	/**
	 * 아이템의 소재를 돌려준다
	 *
	 * @return 0:none 1:액체 2:web 3:식물성 4:동물성 5:지 6:포 7:피 8:목 9:골 10:용비늘 린 11:철
	 *         12:강철 13:동 14:은 15:금 16:플라티나 17:미스릴 18:블랙미스릴 19:유리 20:보석
	 *         21:광물 22:오리하르콘 23:드라니움
	 */
	public Material getMaterial() {
		return _material;
	}
	public void setMaterial(Material material) {
		_material = material;
	}

	private int _weight;
	public int getWeight() {
		return _weight;
	}
	public void setWeight(int weight) {
		_weight = weight;
	}

	private int _iconId;
	public int getIconId() {
		return _iconId;
	}
	public void setIconId(int val) {
		_iconId = val;
	}
	
	private int _mainid;
	public int getMainId() {
		return _mainid;
	}
	public void setMainId(int m) {
		_mainid = m;
	}

	private int _mainid2;
	public int getMainId2() {
		return _mainid2;
	}
	public void setMainId2(int m) {
		_mainid2 = m;
	}

	private int _mainid3;
	public int getMainId3() {
		return _mainid3;
	}
	public void setMainId3(int m) {
		_mainid3 = m;
	}

	private int _setId;
	public int getSetId() {
		return _setId;
	}
	public void setSetId(int m) {
		_setId = m;
	}

	private int _spriteId;
	public int getSpriteId() {
		return _spriteId;
	}
	public void setSpriteId(int val) {
		_spriteId = val;
	}
	
	private int _minLevel;
	public int getMinLevel() {
		return _minLevel;
	}
	public void setMinLevel(int level) {
		_minLevel = level;
	}

	private int _maxLevel;
	public int getMaxLevel() {
		return _maxLevel;
	}
	public void setMaxLevel(int maxlvl) {
		_maxLevel = maxlvl;
	}

	private int _bless;
	public int getBless() {
		return _bless;
	}
	public void setBless(int i) {
		_bless = i;
	}

	/** 교환 **/
	private boolean _tradable;
	public boolean isTradable() {
		return _tradable;
	}
	public void setTradable(boolean flag) {
		_tradable = flag;
	}
	
	/** 창고 저장 **/
	private boolean _retrieve;
	public boolean isRetrieve() {
		return _retrieve;
	}
	public void setRetrieve(boolean flag) {
		_retrieve = flag;
	}
	
	/** 특수창고 저장 **/
	private boolean _specialRetrieve;
	public boolean isSpecialRetrieve() {
		return _specialRetrieve;
	}
	public void setSpecialRetrieve(boolean flag) {
		_specialRetrieve = flag;
	}

	/** 삭제 **/
	private boolean _cantDelete;
	public boolean isCantDelete() {
		return _cantDelete;
	}
	public void setCantDelete(boolean flag) {
		_cantDelete = flag;
	}
	
	/** 판매 **/
	private boolean _cantSell;
	public boolean isCantSell() {
		return _cantSell;
	}
	public void setCantSell(boolean flag) {
		_cantSell = flag;
	}

	/**
	 * 아이템의 개수가 변화했을 때에 곧바로 DB에 기입해야할 것인가를 돌려준다.
	 */
	private boolean _saveAtOnce;
	public boolean isToBeSavedAtOnce() {
		return _saveAtOnce;
	}
	public void setToBeSavedAtOnce(boolean flag) {
		_saveAtOnce = flag;
	}
	
	private CommonItemInfo _bin;
	public CommonItemInfo getBin(){
		return _bin;
	}
	public void setBin(CommonItemInfo val) {
		_bin = val;
	}
	
	private L1QuestCollectItem _questCollectItem;
	public L1QuestCollectItem getQuestCollectItem() {
		return _questCollectItem;
	}
	public void setQuestCollectItem(L1QuestCollectItem questCollectItem) {
		_questCollectItem = questCollectItem;
	}

	// ■■■■■■ L1EtcItem, L1Weapon 에 공통되는 항목 ■■■■■■


	private int _dmgSmall; 
	public int getDmgSmall() {
		return _dmgSmall;
	}
	public void setDmgSmall(int dmgSmall) {
		_dmgSmall = dmgSmall;
	}

	private int _dmgLarge;
	public int getDmgLarge() {
		return _dmgLarge;
	}
	public void setDmgLarge(int dmgLarge) {
		_dmgLarge = dmgLarge;
	}

	// ■■■■■■ L1EtcItem, L1Armor 에 공통되는 항목 ■■■■■■

	// ■■■■■■ L1Weapon, L1Armor 에 공통되는 항목 ■■■■■■

	private int _safeEnchant;
	public int getSafeEnchant() {
		return _safeEnchant;
	}
	public void setSafeEnchant(int safeenchant) {
		_safeEnchant = safeenchant;
	}
	
	private boolean _useRoyal;
	public boolean isUseRoyal() {
		return _useRoyal;
	}
	public void setUseRoyal(boolean flag) {
		_useRoyal = flag;
	}

	private boolean _useKnight;
	public boolean isUseKnight() {
		return _useKnight;
	}
	public void setUseKnight(boolean flag) {
		_useKnight = flag;
	}

	private boolean _useElf;
	public boolean isUseElf() {
		return _useElf;
	}
	public void setUseElf(boolean flag) {
		_useElf = flag;
	}

	private boolean _useMage;
	public boolean isUseMage() {
		return _useMage;
	}
	public void setUseMage(boolean flag) {
		_useMage = flag;
	}

	private boolean _useDarkelf;
	public boolean isUseDarkelf() {
		return _useDarkelf;
	}
	public void setUseDarkelf(boolean flag) {
		_useDarkelf = flag;
	}
	
	private boolean _useDragonKnight;
	public boolean isUseDragonKnight() {
		return _useDragonKnight;
	}
	public void setUseDragonKnight(boolean flag) {
		_useDragonKnight = flag;
	}
	
	private boolean _useIllusionist;
	public boolean isUseIllusionist() {
		return _useIllusionist;
	}
	public void setUseIllusionist(boolean flag) {
		_useIllusionist = flag;
	}
	
	private boolean _useWarrior;
	public boolean isUseWarrior() {
		return _useWarrior;
	}
	public void setUseWarrior(boolean flag) {
		_useWarrior = flag;
	}
	
	private boolean _useFencer;
	public boolean isUseFencer() {
		return _useFencer;
	}
	public void setUseFencer(boolean flag) {
		_useFencer = flag;
	}
	
	private boolean _useLancer;
	public boolean isUseLancer() {
		return _useLancer;
	}
	public void setUseLancer(boolean flag) {
		_useLancer = flag;
	}
	
	private boolean _useHighPet;
	public boolean isUseHighPet() {
		return _useHighPet;
	}
	public void setUseHighPet(boolean flag) {
		_useHighPet = flag;
	}

	private byte _addStr;
	public byte getAddStr() {
		return _addStr;
	}
	public void setAddStr(byte addstr) {
		_addStr = addstr;
	}

	private byte _addDex;
	public byte getAddDex() {
		return _addDex;
	}
	public void setAddDex(byte adddex) {
		_addDex = adddex;
	}

	private byte _addCon; 
	public byte getAddCon() {
		return _addCon;
	}
	public void setAddCon(byte addcon) {
		_addCon = addcon;
	}

	private byte _addInt; 
	public byte getAddInt() {
		return _addInt;
	}
	public void setAddInt(byte addint) {
		_addInt = addint;
	}

	private byte _addWis;
	public byte getAddWis() {
		return _addWis;
	}
	public void setAddWis(byte addwis) {
		_addWis = addwis;
	}

	private byte _addCha;
	public byte getAddCha() {
		return _addCha;
	}
	public void setAddCha(byte addcha) {
		_addCha = addcha;
	}

	private int _addHp;
	public int getAddHp() {
		return _addHp;
	}
	public void setAddHp(int addhp) {
		_addHp = addhp;
	}

	private int _addMp;
	public int getAddMp() {
		return _addMp;
	}
	public void setAddMp(int addmp) {
		_addMp = addmp;
	}

	private int _addHpr;
	public int getAddHpr() {
		return _addHpr;
	}
	public void setAddHpr(int addhpr) {
		_addHpr = addhpr;
	}

	private int _addMpr;
	public int getAddMpr() {
		return _addMpr;
	}
	public void setAddMpr(int addmpr) {
		_addMpr = addmpr;
	}

	private int _addSp;
	public int getAddSp() {
		return _addSp;
	}
	public void setAddSp(int addsp) {
		_addSp = addsp;
	}

	private int _mr;
	public int getMr() {
		return _mr;
	}
	public void setMr(int i) {
		this._mr = i;
	}

	private boolean _isHasteItem;
	public boolean isHasteItem() {
		return _isHasteItem;
	}
	public void setHasteItem(boolean flag) {
		_isHasteItem = flag;
	}
	
	private int _maxUseTime;
	public int getMaxUseTime() {
		return _maxUseTime;
	}
	public void setMaxUseTime(int i) {
		_maxUseTime = i;
	}

	private int _interaction_type;// 사용했을 때의 리액션을 결정하는 타입을 돌려준다.
	public int get_interaction_type() {
		return _interaction_type;
	}
	public void set_interaction_type(int val) {
		_interaction_type = val;
	}

	private int _foodVolume;// 고기등의 아이템으로 설정되어 있는 배고픔게이지를 돌려준다.
	public int getFoodVolume() {
		return _foodVolume;
	}
	public void setFoodVolume(int volume) {
		_foodVolume = volume;
	}
	
	private int _toleranceSkill, _toleranceSpirit, _toleranceDragon, _toleranceFear, _toleranceAll;
	private int _hitupSkill, _hitupSpirit, _hitupDragon, _hitupFear, _hitupAll, _hitupMagic;
	public int getToleranceSkill() {
		return _toleranceSkill;
	}
	public void setToleranceSkill(int i) {
		_toleranceSkill = i;
	}
	
	public int getToleranceSpirit() {
		return _toleranceSpirit;
	}
	public void setToleranceSpirit(int i) {
		_toleranceSpirit = i;
	}
	
	public int getToleranceDragon() {
		return _toleranceDragon;
	}
	public void setToleranceDragon(int i) {
		_toleranceDragon = i;
	}
	
	public int getToleranceFear() {
		return _toleranceFear;
	}
	public void setToleranceFear(int i) {
		_toleranceFear = i;
	}
	
	public int getToleranceAll() {
		return _toleranceAll;
	}
	public void setToleranceAll(int i) {
		_toleranceAll = i;
	}
	
	public int getHitupSkill() {
		return _hitupSkill;
	}
	public void setHitupSkill(int i) {
		_hitupSkill = i;
	}
	
	public int getHitupSpirit() {
		return _hitupSpirit;
	}
	public void setHitupSpirit(int i) {
		_hitupSpirit = i;
	}
	
	public int getHitupDragon() {
		return _hitupDragon;
	}
	public void setHitupDragon(int i) {
		_hitupDragon = i;
	}
	
	public int getHitupFear() {
		return _hitupFear;
	}
	public void setHitupFear(int i) {
		_hitupFear = i;
	}
	
	public int getHitupAll() {
		return _hitupAll;
	}
	public void setHitupAll(int i) {
		_hitupAll = i;
	}
	
	public int getHitupMagic() {
		return _hitupMagic;
	}
	public void setHitupMagic(int i) {
		_hitupMagic = i;
	}
	
	private int shortCritical;
	public int getShortCritical() {
		return shortCritical;
	}
	public void setShortCritical(int i) {
		shortCritical = i;
	}
	
	private int longCritical;
	public int getLongCritical() {
		return longCritical;
	}
	public void setLongCritical(int i) {
		longCritical = i;
	}
	
	private int magicCritical;
	public int getMagicCritical() {
		return magicCritical;
	}
	public void setMagicCritical(int i) {
		magicCritical = i;
	}
	
	private int damageReduction;
	public int getDamageReduction() {
		return damageReduction;
	}
	public void setDamageReduction(int i) {
		damageReduction = i;
	}
	
	private int magicDamageReduction;
	public int getMagicDamageReduction() {
		return magicDamageReduction;
	}
	public void setMagicDamageReduction(int i) {
		magicDamageReduction = i;
	}
	
	private int damageReductionEgnor;
	public int getDamageReductionEgnor() {
		return damageReductionEgnor;
	}
	public void setDamageReductionEgnor(int i) {
		damageReductionEgnor = i;
	}
	
	private int damageReductionPercent;
	public int getDamageReductionPercent() {
		return damageReductionPercent;
	}
	public void setDamageReductionPercent(int i) {
		damageReductionPercent = i;
	}
	
	private int pvpDamage;
	public int getPVPDamage() {
		return pvpDamage;
	}
	public void setPVPDamage(int i) {
		pvpDamage = i;
	}
	
	private int pvpDamageReduction;
	public int getPVPDamageReduction() {
		return pvpDamageReduction;
	}
	public void setPVPDamageReduction(int i) {
		pvpDamageReduction = i;
	}
	
	private int pvpDamageReductionPercent;
	public int getPVPDamageReductionPercent() {
		return pvpDamageReductionPercent;
	}
	public void setPVPDamageReductionPercent(int i) {
		pvpDamageReductionPercent = i;
	}
	
	private int pvpMagicDamageReduction;
	public int getPVPMagicDamageReduction() {
		return pvpMagicDamageReduction;
	}
	public void setPVPMagicDamageReduction(int i) {
		pvpMagicDamageReduction = i;
	}
	
	private int pvpReductionEgnor;
	public int getPVPReductionEgnor() {
		return pvpReductionEgnor;
	}
	public void setPVPReductionEgnor(int i) {
		pvpReductionEgnor = i;
	}
	
	private int pvpMagicDamageReductionEgnor;
	public int getPVPMagicDamageReductionEgnor() {
		return pvpMagicDamageReductionEgnor;
	}
	public void setPVPMagicDamageReductionEgnor(int i) {
		pvpMagicDamageReductionEgnor = i;
	}
	
	private int abnormalStatusDamageReduction;
	public int getAbnormalStatusDamageReduction() {
		return abnormalStatusDamageReduction;
	}
	public void setAbnormalStatusDamageReduction(int i) {
		abnormalStatusDamageReduction = i;
	}
	
	private int abnormalStatusPVPDamageReduction;
	public int getAbnormalStatusPVPDamageReduction() {
		return abnormalStatusPVPDamageReduction;
	}
	public void setAbnormalStatusPVPDamageReduction(int i) {
		abnormalStatusPVPDamageReduction = i;
	}
	
	private int pvpDamagePercent;
	public int getPVPDamagePercent() {
		return pvpDamagePercent;
	}
	public void setPVPDamagePercent(int i) {
		pvpDamagePercent = i;
	}
	
	private int expBonus;
	public int getExpBonus() {
		return expBonus;
	}
	public void setExpBonus(int i){
		expBonus = i;
	}
	
	private int restExpReduceEfficiency;
	public int getRestExpReduceEfficiency() {
		return restExpReduceEfficiency;
	}
	public void setRestExpReduceEfficiency(int i) {
		restExpReduceEfficiency = i;
	}
	
	private int _addDg;
	public int getAddDg() {
		return _addDg;
	}
	public void setAddDg(int i) {
		_addDg = i;
	}
	
	private int _addEr;
	public int getAddEr() {
		return _addEr;
	}
	public void setAddEr(int i) {
		_addEr = i;
	}
	
	private int _addMe;
	public int getAddMe() {
		return _addMe;
	}
	public void setAddMe(int i) {
		_addMe = i;
	}
	
	private int imunEgnor;
	public int getImunEgnor() {
		return imunEgnor;
	}
	public void setImunEgnor(int val) {
		imunEgnor = val;
	}
	
	private int stunDuration;
	public int getStunDuration() {
		return stunDuration;
	}
	public void setStunDuration(int val) {
		stunDuration = val;
	}
	
	private int tripleArrowStun;
	public int getTripleArrowStun() {
		return tripleArrowStun;
	}
	public void setTripleArrowStun(int val) {
		tripleArrowStun = val;
	}
	
	private int _strangeTimeIncrease;
	public int getStrangeTimeIncrease() {
		return _strangeTimeIncrease;
	}
	public void setStrangeTimeIncrease(int i) {
		_strangeTimeIncrease = i;
	}
	
	private int _strangeTimeDecrease;
	public int getStrangeTimeDecrease() {
		return _strangeTimeDecrease;
	}
	public void setStrangeTimeDecrease(int i) {
		_strangeTimeDecrease = i;
	}
	
	private int _potionRegist;
	public int getPotionRegist() {
		return _potionRegist;
	}
	public void setPotionRegist(int val) {
		_potionRegist = val;
	}
	
	private int _potionPercent;
	public int getPotionPercent() {
		return _potionPercent;
	}
	public void setPotionPercent(int val) {
		_potionPercent = val;
	}
	
	private int _potionValue;
	public int getPotionValue() {
		return _potionValue;
	}
	public void setPotionValue(int val) {
		_potionValue = val;
	}
	
	private int _hprAbsol32Second;
	public int getHprAbsol32Second() {
		return _hprAbsol32Second;
	}
	public void setHprAbsol32Second(int val) {
		_hprAbsol32Second = val;
	}
	
	private int _mprAbsol64Second;
	public int getMprAbsol64Second() {
		return _mprAbsol64Second;
	}
	public void setMprAbsol64Second(int val) {
		_mprAbsol64Second = val;
	}
	
	private int _mprAbsol16Second;
	public int getMprAbsol16Second() {
		return _mprAbsol16Second;
	}
	public void setMprAbsol16Second(int val) {
		_mprAbsol16Second = val;
	}
	
	private int _hpPotionDelayDecrease;
	public int getHpPotionDelayDecrease() {
		return _hpPotionDelayDecrease;
	}
	public void setHpPotionDelayDecrease(int val) {
		_hpPotionDelayDecrease = val;
	}
	
	private int _hpPotionCriticalProb;
	public int getHpPotionCriticalProb() {
		return _hpPotionCriticalProb;
	}
	public void setHpPotionCriticalProb(int val) {
		_hpPotionCriticalProb = val;
	}
	
	private int _increaseArmorSkillProb;
	public int getIncreaseArmorSkillProb() {
		return _increaseArmorSkillProb;
	}
	public void setIncreaseArmorSkillProb(int val) {
		_increaseArmorSkillProb = val;
	}
	
	private int _attackSpeedDelayRate;
	public int getAttackSpeedDelayRate() {
		return _attackSpeedDelayRate;
	}
	public void setAttackSpeedDelayRate(int val) {
		_attackSpeedDelayRate = val;
	}
	
	private int _moveSpeedDelayRate;
	public int getMoveSpeedDelayRate() {
		return _moveSpeedDelayRate;
	}
	public void setMoveSpeedDelayRate(int val) {
		_moveSpeedDelayRate = val;
	}
	
	private boolean _poisonRegist;
	public boolean isPoisonRegist() {
		return _poisonRegist;
	}
	public void setPoisonRegist(boolean val) {
		_poisonRegist = val;
	}

	/**
	 * 램프등의 아이템으로 설정되어 있는 밝음을 돌려준다.
	 */
	public int getLightRange() {
		if (_itemId == 40001) {
			return 11;
		}
		if (_itemId == 40002 || _itemId == 40004 || _itemId == 410517) {
			return 14;
		}
		if (_itemId == 7338) {
			return 19;
		}
		if (_itemId == 40005) {
			return 8;
		}
		return 0;
	}
	
	public int getLightFuel() {
		if (_itemId == 40001 || _itemId == 40003 || _itemId == 40005) {
			return 600;
		}
		return 0;
	}

	// ■■■■■■ L1EtcItem 로 오버라이드(override) 하는 항목 ■■■■■■
	public boolean isMerge() {
		return false;
	}

	public int getLocX() {
		return 0;
	}

	public int getLocY() {
		return 0;
	}

	public short getMapId() {
		return 0;
	}

	public int getDelayId() {
		return 0;
	}

	public int getDelayTime() {
		return 0;
	}

	public int getMaxChargeCount() {
		return 0;
	}
	
	public int getDelayEffect() {
		return 0;
	}
	
	public int getSkillLevel() {
		return 0;
	}
	
	public L1ItemSpellBookAttr getSkillAttr() {
		return null;
	}
	
	public L1SkillType getSkillType(){
		return null;
	}
	
	public L1Alignment getAlignment() {
		return null;
	}
	
	public int getBuffDurationSecond() {
		return 0;
	}
	
	public int getEtcValue() {
		return 0;
	}
	
	public L1ItemLimitType getLimitType(){
		return null;
	}
	
	public int getProb() {
		return 0;
	}
	
	public L1HealingPotion getHealingPotion(){
		return null;
	}

	// ■■■■■■ L1Weapon 로 오버라이드(override) 하는 항목 ■■■■■■
	public int getDoubleDmgChance() {
		return 0;
	}

	public int getMagicDmgModifier() {
		return 0;
	}

	public int getCanbeDmg() {
		return 0;
	}
	
	public L1ItemWeaponType getWeaponType() {
		return null;
	}

	public boolean isTwohandedWeapon() {
		return false;
	}
	
	public int getWeaponAddDamage(){
		return 0;
	}

	// ■■■■■■ L1Armor 로 오버라이드(override) 하는 항목 ■■■■■■
	public int getAc() {
		return 0;
	}
	
	public int getAcSub() {
		return -1;
	}

	public int getCarryBonus() {
		return 0;
	}

	public int getDmgRate() {
		return 0;
	}
	
	public int getHitRate() {
		return 0;
	}
	
	public int getBowHitRate() {
		return 0;
	}
	
	public int getBowDmgRate() {
		return 0;
	}

	public int getAttrWater() {
		return 0;
	}

	public int getAttrFire() {
		return 0;
	}

	public int getAttrEarth() {
		return 0;
	}

	public int getAttrWind() {
		return 0;
	}
	
	public int getAttrAll() {
		return 0;
	}

	public int getRegistStone() {
		return 0;
	}

	public int getRegistSleep() {
		return 0;
	}

	public int getRegistFreeze() {
		return 0;
	}

	public int getRegistBlind() {
		return 0;
	}
	
	public int getRetrieveEnchantLevel(){
		return 0;
	}
	
	public int getAcBonus() {
		return 0;
	}
	
	public int getPolyDescId() {
		return 0;
	}
	
	public L1EnchantFactory getEnchantInfo(){
		return null;
	}
		
	private int _grade; // ● 장신구 단계
	public int getGrade() {
		return _grade;
	}
	public void setGrade(int grade) {
		_grade = grade;
	}

	private int _ticketPrice; // ● 가격
	public int getTicketPrice() {
		return _ticketPrice;
	}
	public void setTicketPrice(int price) {
		_ticketPrice = price;
	}
	
}

