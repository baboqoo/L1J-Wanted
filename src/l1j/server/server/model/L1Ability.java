package l1j.server.server.model;

import l1j.server.common.bin.EinhasadPointCommonBinLoader;
import l1j.server.common.bin.einhasadpoint.EinhasadPointStatInfoT;
import l1j.server.common.data.eEinhasadStatType;
import l1j.server.server.construct.L1BaseStatus;
import l1j.server.server.construct.L1CharacterInfo;
import l1j.server.server.construct.L1Status;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_MagicEvasion;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.returnedstat.S_StatusBaseNoti;
import l1j.server.server.serverpackets.returnedstat.S_StatusRenewalInfo;
import l1j.server.server.utils.CalcConStat;
import l1j.server.server.utils.CalcDexStat;
import l1j.server.server.utils.CalcIntelStat;
import l1j.server.server.utils.IntRange;

public class L1Ability {
	private static final int LIMIT_MINUS_MIN	= -128;
	private static final int LIMIT_MIN			= 0;
	private static final int LIMIT_MAX			= 127;

	private byte str; // 베이스 힘 + 레벨업 또는 엘릭서로 인해 상승한 힘
	private byte baseStr; // 베이스 힘
	private byte addedStr; // 마법 또는 아이템으로 상승한 힘

	private byte con;
	private byte baseCon;
	private byte addedCon;

	private byte dex;
	private byte baseDex;
	private byte addedDex;

	private byte cha;
	private byte baseCha;
	private byte addedCha;

	private byte intel;
	private byte baseInt;
	private byte addedInt;

	private byte wis;
	private byte baseWis;
	private byte addedWis;
	
	private int total_ein_stat;
	private byte statBless, statLucky, statVital, statItemSpellProb, statAbsoluteRegen, statPotion;
	private int blessEfficiency, blessExp;
	private int luckyItem, luckyAdena;
	private int vitalPotion, vitalHeal;
	private int itemSpellProbArmor, itemSpellProbWeapon;
	private int absoluteRegenHp, absoluteRegenMp;
	private int potionCritical, potionDelay;
	
	private int _shortDmgup, _trueShortDmgup, _longDmgup, _trueLongDmgup, _magicDmgup, sp;
	private int _shortHitup, _trueShortHitup,_longHitup, _trueLongHitup, _magicHitup, _trueMagicHitup;
	private int _shortCritical, _longCritical, _magicCritical, _magicCriticalDmgAdd;
	private int _damageReduction, _damageReductionEgnor, _magicDamageReduction, _damageReductionPercent;
    private int _PVPDamage, _PVPDamageReduction, _PVPDamageReductionPercent, _PVPMagicDamageReduction, _PVPDamageReductionEgnor, _PVPMagicDamageReductionEgnor, _PVPDamagePercent;
    private int _abnormalStatusDamageReduction, _abnormalStatusPVPDamageReduction;
    private int _fowSlayerDamage, _titanUp, _emunEgnor, _stunDuration, _vanguardDecrease, _tripleArrowStun, _reflectEmasculate;
    private int _dg, _me, _er;
    private int _base_hp_rate, _base_mp_rate;
    private int _statusPotionPlus, _itemPotionRegist, _itemPotionPercent, _itemPotionValue, _potionRecoveryRate;
    private int _spellCooltimeDecrease, _spellDurationDecrease, _strangeTimeIncrease, _strangeTimeDecrease, _returnLockDuraion;
    private int _hpPotionDelayDecrease, _hpPotionCriticalProb;
    private int _increaseArmorSkillProb;
    private boolean _underWater;
    private int _poison_regist;
    
    private int[] _roomtisEarringDamage, _roomtisEarringReduction;
    private int _snaperRingReduction;
    private int _valakasShortCritical, _valakasLongCritical, _valakasMagicCritical;

	private L1Character character;
	
    L1Ability(L1Character cha) {
        character = cha;
    }

	public void init() {
		str = baseStr = addedStr = 0;
		dex = baseDex = addedDex = 0;
		con = baseCon = addedCon = 0;
		intel = baseInt = addedInt = 0;
		wis = baseWis = addedWis = 0;
		cha = baseCha = addedCha = 0;
	}

	private byte checkRange(int i) {
		if (i == 0) {
			return 0;
		}
		return checkRange(i, 0);
	}

	private byte checkRange(int i, int base) {
		return (byte) IntRange.ensure(i, LIMIT_MIN + base, LIMIT_MAX);
	}

	public int getBaseStatsAmount() {
		return baseStr + baseCon + baseDex + baseCha + baseInt + baseWis;
	}

	public int getStatsAmount() {
		return str + con + dex + cha + intel + wis;
	}
	
	public byte getStr() {
		return str;
	}
	
	public void setStr(int i) {
		str = checkRange(i, baseStr);
		if (character instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) character;
			pc.Stat_Reset_Str();
		}
	}

	public void addStr(int i) {
		setStr(getStr() + i);
	}

	public byte getBaseStr() {
		return baseStr;
	}

	public void addBaseStr(int i) {
		setBaseStr(getBaseStr() + i);
	}

	public void setBaseStr(int i) {
		byte newBaseStr = checkRange(i);
		addStr(newBaseStr - baseStr);
		baseStr = newBaseStr;
	}

	public byte getAddedStr() {
		return addedStr;
	}

	public void addAddedStr(int i) {
		addedStr = checkRange(addedStr + i, LIMIT_MINUS_MIN);
		if (character instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) character;
			pc.Stat_Reset_Str();
		}
	}

	public byte getTotalStr() {
		return checkRange(getStr() + getAddedStr());
	}

	public byte getCon() {
		return con;
	}

	public void setCon(int i) {
		con = checkRange(i, baseCon);
		if (character instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) character;
			pc.Stat_Reset_Con();
		}
	}

	public void addCon(int i) {
		setCon(getCon() + i);
	}

	public byte getBaseCon() {
		return baseCon;
	}

	public void addBaseCon(int i) {
		setBaseCon(getBaseCon() + i);
	}

	public void setBaseCon(int i) {
		byte newBaseCon = checkRange(i);
		addCon(newBaseCon - baseCon);
		baseCon = newBaseCon;
	}

	public byte getAddedCon() {
		return addedCon;
	}

	public void addAddedCon(int i) {
		addedCon = checkRange(addedCon + i, LIMIT_MINUS_MIN);
		if (character instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) character;
			pc.Stat_Reset_Con();
		}
	}

	public byte getTotalCon() {
		return checkRange(getCon() + getAddedCon());
	}

	public byte getDex() {
		return dex;
	}

	public void setDex(int i) {
		dex = checkRange(i, baseDex);
		if (character instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) character;
			pc.Stat_Reset_Dex();
		}
	}

	public void addDex(int i) {
		setDex(getDex() + i);
	}

	public byte getBaseDex() {
		return baseDex;
	}

	public void addBaseDex(int i) {
		setBaseDex(getBaseDex() + i);
	}

	public void setBaseDex(int i) {
		byte newBaseDex = checkRange(i);
		addDex(newBaseDex - baseDex);
		baseDex = newBaseDex;
	}

	public byte getAddedDex() {
		return addedDex;
	}

	public void addAddedDex(int i) {
		addedDex = checkRange(addedDex + i, LIMIT_MINUS_MIN);
		if (character instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) character;
			pc.Stat_Reset_Dex();
		}
	}

	public byte getTotalDex() {
		return checkRange(getDex() + getAddedDex());
	}

	public byte getCha() {
		return cha;
	}

	public void setCha(int i) {
		cha = checkRange(i, baseCha);
		if (character instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) character;
			pc.Stat_Reset_Cha();
		}
	}

	public void addCha(int i) {
		setCha(getCha() + i);
	}

	public byte getBaseCha() {
		return baseCha;
	}

	public void addBaseCha(int i) {
		setBaseCha(getBaseCha() + i);
	}

	public void setBaseCha(int i) {
		byte newBaseCha = checkRange(i);
		addCha(newBaseCha - baseCha);
		baseCha = newBaseCha;
	}

	public byte getAddedCha() {
		return addedCha;
	}

	public void addAddedCha(int i) {
		addedCha = checkRange(addedCha + i, LIMIT_MINUS_MIN);
		if (character instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) character;
			pc.Stat_Reset_Cha();
		}
	}

	public byte getTotalCha() {
		return checkRange(getCha() + getAddedCha());
	}

	public byte getInt() {
		return intel;
	}

	public void setInt(int i) {
		intel = checkRange(i, baseInt);
	}

	public void addInt(int i) {
		setInt(getInt() + i);
		if (character instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) character;
			if (pc.getNetConnection() != null) {
				pc.sendPackets(new S_StatusBaseNoti(this), true);// 현재순수스탯
				pc.sendPackets(new S_SPMR(pc), true);
				pc.sendPackets(new S_StatusRenewalInfo(pc , 8, L1Status.INT), true);
			}
		}
	}

	public byte getBaseInt() {
		return baseInt;
	}

	public void addBaseInt(int i) {
		setBaseInt(getBaseInt() + i);
	}

	public void setBaseInt(int i) {
		byte newBaseInt = checkRange(i);
		addInt(newBaseInt - baseInt);
		baseInt = newBaseInt;
	}

	public byte getAddedInt() {
		return addedInt;
	}

	public void addAddedInt(int i) {
		addedInt = checkRange(addedInt + i, LIMIT_MINUS_MIN);
		if (character instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) character;
			if (pc.getNetConnection() != null) {
				pc.sendPackets(new S_SPMR(pc), true);
				pc.sendPackets(new S_StatusRenewalInfo(pc , 8, L1Status.INT), true);
			}
		}
	}

	public byte getTotalInt() {
		return checkRange(getInt() + getAddedInt());
	}

	public byte getWis() {
		return wis;
	}

	public void setWis(int i) {
		wis = checkRange(i, baseWis);
		if (character instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) character;
			pc.Stat_Reset_Wis();
		}
	}

	public void addWis(int i) {
		setWis(getWis() + i);
	}

	public byte getBaseWis() {
		return baseWis;
	}

	public void addBaseWis(int i) {
		setBaseWis(getBaseWis() + i);
	}

	public void setBaseWis(int i) {
		byte newBaseWis = checkRange(i);
		addWis(newBaseWis - baseWis);
		baseWis = newBaseWis;
	}

	public byte getAddedWis() {
		return addedWis;
	}

	public void addAddedWis(int i) {
		addedWis = checkRange(addedWis + i, LIMIT_MINUS_MIN);
		if (character instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) character;
			pc.Stat_Reset_Wis();
		}
	}

	public byte getTotalWis() {
		return checkRange(getWis() + getAddedWis());
	}
	
	// TODO 아인하사드 스탯 능력치
	public byte getStatBless() {
		return statBless;
	}
	public void setStatBless(byte val) {
		statBless = val;
		add_total_ein_stat(val);
	}
	public void addStatBless(byte val) {
		statBless += val;
		EinhasadPointStatInfoT.StatT statT = EinhasadPointCommonBinLoader.getStatT(statBless, eEinhasadStatType.BLESS);
		addBlessEfficiency(statT.get_Ability1().get_IncValue());
		addBlessExp(statT.get_Ability2().get_IncValue());
		add_total_ein_stat(val);
	}
	
	public byte getStatLucky() {
		return statLucky;
	}
	public void setStatLucky(byte val) {
		statLucky = val;
		add_total_ein_stat(val);
	}
	public void addStatLucky(byte val) {
		statLucky += val;
		EinhasadPointStatInfoT.StatT statT = EinhasadPointCommonBinLoader.getStatT(statLucky, eEinhasadStatType.LUCKY);
		addLuckyItem(statT.get_Ability1().get_IncValue());
		addLuckyAdena(statT.get_Ability2().get_IncValue());
		add_total_ein_stat(val);
	}
	
	public byte getStatVital() {
		return statVital;
	}
	public void setStatVital(byte val) {
		statVital = val;
		add_total_ein_stat(val);
	}
	public void addStatVital(byte val) {
		statVital += val;
		EinhasadPointStatInfoT.StatT statT = EinhasadPointCommonBinLoader.getStatT(statVital, eEinhasadStatType.VITAL);
		addVitalPotion(statT.get_Ability1().get_IncValue());
		addVitalHeal(statT.get_Ability2().get_IncValue());
		add_total_ein_stat(val);
	}
	
	public byte getStatItemSpellProb() {
		return statItemSpellProb;
	}
	public void setStatItemSpellProb(byte val) {
		statItemSpellProb = val;
		add_total_ein_stat(val);
	}
	public void addStatItemSpellProb(byte val) {
		statItemSpellProb += val;
		EinhasadPointStatInfoT.StatT statT = EinhasadPointCommonBinLoader.getStatT(statItemSpellProb, eEinhasadStatType.ITEM_SPELL_PROB);
		addItemSpellProbArmor(statT.get_Ability1().get_IncValue());
		addItemSpellProbWeapon(statT.get_Ability2().get_IncValue());
		add_total_ein_stat(val);
	}
	
	public byte getStatAbsoluteRegen() {
		return statAbsoluteRegen;
	}
	public void setStatAbsoluteRegen(byte val) {
		statAbsoluteRegen = val;
		add_total_ein_stat(val);
	}
	public void addStatAbsoluteRegen(byte val) {
		statAbsoluteRegen += val;
		EinhasadPointStatInfoT.StatT statT = EinhasadPointCommonBinLoader.getStatT(statAbsoluteRegen, eEinhasadStatType.ABSOLUTE_REGEN);
		addAbsoluteRegenHp(statT.get_Ability1().get_IncValue());
		addAbsoluteRegenMp(statT.get_Ability2().get_IncValue());
		add_total_ein_stat(val);
	}
	
	public byte getStatPotion() {
		return statPotion;
	}
	public void setStatPotion(byte val) {
		statPotion = val;
		add_total_ein_stat(val);
	}
	public void addStatPotion(byte val) {
		statPotion += val;
		EinhasadPointStatInfoT.StatT statT = EinhasadPointCommonBinLoader.getStatT(statPotion, eEinhasadStatType.POTION);
		addPotionCritical(statT.get_Ability1().get_IncValue());
		addPotionDelay(statT.get_Ability2().get_IncValue());
		add_total_ein_stat(val);
	}
	
	public int getBlessEfficiency() {
		return blessEfficiency;
	}
	public void setBlessEfficiency(int val) {
		this.blessEfficiency = val;
	}
	public void addBlessEfficiency(int val) {
		this.blessEfficiency += val;
	}

	public int getBlessExp() {
		return blessExp;
	}
	public void setBlessExp(int val) {
		this.blessExp = val;
	}
	public void addBlessExp(int val) {
		this.blessExp += val;
	}

	public int getLuckyItem() {
		return luckyItem;
	}
	public void setLuckyItem(int val) {
		this.luckyItem = val;
	}
	public void addLuckyItem(int val) {
		this.luckyItem += val;
	}

	public int getLuckyAdena() {
		return luckyAdena;
	}
	public void setLuckyAdena(int val) {
		this.luckyAdena = val;
	}
	public void addLuckyAdena(int val) {
		this.luckyAdena += val;
	}

	public int getVitalPotion() {
		return vitalPotion;
	}
	public void setVitalPotion(int val) {
		this.vitalPotion = val;
	}
	public void addVitalPotion(int val) {
		this.vitalPotion += val;
	}

	public int getVitalHeal() {
		return vitalHeal;
	}
	public void setVitalHeal(int val) {
		this.vitalHeal = val;
	}
	public void addVitalHeal(int val) {
		this.vitalHeal += val;
	}

	public int getItemSpellProbArmor() {
		return itemSpellProbArmor;
	}
	public void setItemSpellProbArmor(int val) {
		this.itemSpellProbArmor = val;
	}
	public void addItemSpellProbArmor(int val) {
		this.itemSpellProbArmor += val;
	}

	public int getItemSpellProbWeapon() {
		return itemSpellProbWeapon;
	}
	public void setItemSpellProbWeapon(int val) {
		this.itemSpellProbWeapon = val;
	}
	public void addItemSpellProbWeapon(int val) {
		this.itemSpellProbWeapon += val;
	}

	public int getAbsoluteRegenHp() {
		return absoluteRegenHp;
	}
	public void setAbsoluteRegenHp(int val) {
		this.absoluteRegenHp = val;
	}
	public void addAbsoluteRegenHp(int val) {
		this.absoluteRegenHp += val;
	}

	public int getAbsoluteRegenMp() {
		return absoluteRegenMp;
	}
	public void setAbsoluteRegenMp(int val) {
		this.absoluteRegenMp = val;
	}
	public void addAbsoluteRegenMp(int val) {
		this.absoluteRegenMp += val;
	}

	public int getPotionCritical() {
		return potionCritical;
	}
	public void setPotionCritical(int potionCritical) {
		this.potionCritical = potionCritical;
	}
	public void addPotionCritical(int potionCritical) {
		this.potionCritical += potionCritical;
	}

	public int getPotionDelay() {
		return potionDelay;
	}
	public void setPotionDelay(int potionDelay) {
		this.potionDelay = potionDelay;
	}
	public void addPotionDelay(int potionDelay) {
		this.potionDelay += potionDelay;
	}

	public void resetEinStat(){
		total_ein_stat = 0;
		statBless = statLucky = statVital = statItemSpellProb = statAbsoluteRegen = statPotion = 0;
		blessEfficiency = blessExp = 0;
		luckyItem = luckyAdena = 0;
		vitalPotion = vitalHeal = 0;
		itemSpellProbArmor = itemSpellProbWeapon = 0;
		absoluteRegenHp = absoluteRegenMp = 0;
		potionCritical = potionDelay = 0;
	}
	
	public int get_total_ein_stat() {
		return total_ein_stat;
	}
	public void add_total_ein_stat(int val) {
		total_ein_stat += val;
	}
	
	//TODO 능력치
	
	public int getShortDmgup() {
		return _shortDmgup;
	}
	public void addShortDmgup(int i) {
		_trueShortDmgup += i;
		if (_trueShortDmgup > 127) {
			_shortDmgup = 127;
		} else if (_trueShortDmgup < -128) {
			_shortDmgup = -128;
		} else {
			_shortDmgup = _trueShortDmgup;
		}
	}

	public int getLongDmgup() {
		return _longDmgup;
	}
	public void addLongDmgup(int i) {
		_trueLongDmgup += i;
		if (_trueLongDmgup > 127) {
			_longDmgup = 127;
		} else if (_trueLongDmgup < -128) {
			_longDmgup = -128;
		} else {
			_longDmgup = _trueLongDmgup;
		}
	}
	
	public int getMagicDmgup() {
		return _magicDmgup;
	}
    public int addMagicDmgup(int i) {
    	return _magicDmgup += i;
    }
    
    public void addSp(int i) {
		sp += i;
		if (character instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) character;
			pc.sendPackets(new S_SPMR(pc), true);
		}
	}

	public int getSp() {
		return getTrueSp() + sp;
	}
	
	public int getTrueSp() {
		return getMagicLevel() + getMagicBonus();
	}

	public int getMagicLevel() {
		if (character instanceof L1PcInstance && ((L1PcInstance) character).getClassFeature() != null) {
			return ((L1PcInstance) character).getClassFeature().getMagicLevel(character.getLevel());
		}
		return character.getLevel() >> 2;
	}

	public int getMagicBonus() {
		return CalcIntelStat.magicBonus(getTotalInt());
	}

	public int getShortHitup() {
		return _shortHitup;
	}
	public void addShortHitup(int i) {
		_trueShortHitup += i;
		if (_trueShortHitup > 127) {
			_shortHitup = 127;
		} else if (_trueShortHitup < -128) {
			_shortHitup = -128;
		} else {
			_shortHitup = _trueShortHitup;
		}
	}

	public int getLongHitup() {
		return _longHitup;
	}
	public void addLongHitup(int i) {
		_trueLongHitup += i;
		if (_trueLongHitup > 127) {
			_longHitup = 127;
		} else if (_trueLongHitup < -128) {
			_longHitup = -128;
		} else {
			_longHitup = _trueLongHitup;
		}
	}
	
	public int getMagicHitup() {
		return _magicHitup;
	}
    public void addMagicHitup(int i) {
    	_trueMagicHitup += i;
    	if (_trueMagicHitup > 127) {
    		_magicHitup = 127;
    	} else if (_trueMagicHitup < -128) {
    		_magicHitup = -128;
    	} else {
    		_magicHitup = _trueMagicHitup;
    	}
    }
    
    public int getDamageReduction() {
    	return _damageReduction;
    }
    public void addDamageReduction(int i) {
    	_damageReduction += i;
    }
    
    public int getDamageReductionEgnor() {
    	return _damageReductionEgnor;
    }
    public void addDamageReductionEgnor(int i) {
    	_damageReductionEgnor += i;
    }
    
	public int getMagicDamageReduction() {
		return _magicDamageReduction;
	}
    public void addMagicDamageReduction(int i) {
    	_magicDamageReduction += i;
    }

    public int getPVPDamage() {
    	return _PVPDamage;
    }
    public void addPVPDamage(int i) {
    	_PVPDamage += i;
    }
	
	public int getPVPDamageReduction() {
		return _PVPDamageReduction;
	}
    public void addPVPDamageReduction(int i) {
    	_PVPDamageReduction += i;
    }
    
    public int getPVPDamageReductionPercent() {
		return _PVPDamageReductionPercent;
	}
    public void addPVPDamageReductionPercent(int i) {
    	_PVPDamageReductionPercent += i;
    }
    
	public int getPVPMagicDamageReduction() {
		return _PVPMagicDamageReduction;
	}
    public void addPVPMagicDamageReduction(int i) {
    	_PVPMagicDamageReduction += i;
    }
    
	public int getPVPDamageReductionEgnor() {
		return _PVPDamageReductionEgnor;
	}
    public void addPVPDamageReductionEgnor(int i) {
    	_PVPDamageReductionEgnor += i;
    }
    
    public int getPVPMagicDamageReductionEgnor() {
    	return _PVPMagicDamageReductionEgnor;
    }
    public void addPVPMagicDamageReductionEgnor(int i) {
    	_PVPMagicDamageReductionEgnor += i;
    }
    
    public int getAbnormalStatusDamageReduction() {
    	return _abnormalStatusDamageReduction;
    }
    public void addAbnormalStatusDamageReduction(int i) {
    	_abnormalStatusDamageReduction += i;
    }
    
    public int getAbnormalStatusPVPDamageReduction() {
    	return _abnormalStatusPVPDamageReduction;
    }
    public void addAbnormalStatusPVPDamageReduction(int i) {
    	_abnormalStatusPVPDamageReduction += i;
    }
    
    public int getPVPDamagePercent() {
    	return _PVPDamagePercent;
    }
    public void addPVPDamagePercent(int i) {
    	_PVPDamagePercent += i;
    }
    
	public int getDamageReductionPercent() {
		return _damageReductionPercent;
	}
    public void addDamageReductionPercent(int i) {
    	_damageReductionPercent += i;
    }
    
    public int getEmunEgnor() {
    	return _emunEgnor;
    }
    public void addEmunEgnor(int i) {
    	_emunEgnor += i;
    }
    
    public int getStunDuration() {
    	return _stunDuration;
    }
    public void addStunDuration(int i) {
    	_stunDuration += i;
    }
    
    public int getMagicCriticalDmgAdd() {
    	return _magicCriticalDmgAdd;
    }
    public void addMagicCriticalDmgAdd(int i) {
    	_magicCriticalDmgAdd += i;
    }
    
    public int getFowSlayerDamage(){
    	return _fowSlayerDamage;
    }
    public void addFowSlayerDamage(int i){
    	_fowSlayerDamage += i;
    }
    
    public int get_poison_regist() {
    	return _poison_regist;
    }
    public void add_poison_regist(int val) {
    	_poison_regist += val;
    }
    
    public int getTitanUp() {
		return _titanUp;
	}
	public int addTitanUp(int i) {
		return _titanUp += i;
	}
	public void setTitanUp(int i) {
		_titanUp = i;
	}
    
    public int getVanguardDecrease() {
    	return _vanguardDecrease;
    }
    public void addVanguardDecrease(int i) {
    	_vanguardDecrease += i;
    }
    public void setVanguardDecrease(int i) {
    	_vanguardDecrease = i;
    }
    
    public int getTripleArrowStun() {
    	return _tripleArrowStun;
    }
    public void addTripleArrowStun(int i) {
    	_tripleArrowStun += i;
    }
    public void setTripleArrowStun(int i) {
    	_tripleArrowStun = i;
    }
    
    public int getReflectEmasculate() {
    	return _reflectEmasculate;
    }
    public void addReflectEmasculate(int i) {
    	_reflectEmasculate += i;
    }
    public void setReflectEmasculate(int i) {
    	_reflectEmasculate = i;
    }
	
    public int getShortCritical() {
    	return _shortCritical;
    }
    public void addShortCritical(int i) {
    	_shortCritical += i;
    }
    public void setShortCritical(int i) {
    	_shortCritical = i;
    }
    
    public int getLongCritical() {
    	return _longCritical;
    }
    public void addLongCritical(int i) {
    	_longCritical += i;
    }
    public void setLongCritical(int i) {
    	_longCritical = i;
    }
    
    public int getMagicCritical() {
    	return _magicCritical;
    }
    public void addMagicCritical(int i) {
    	_magicCritical += i;
    }
    public void setMagicCritical(int i) {
    	_magicCritical = i;
    }
    
    public int getBaseEr() {
		return (getTotalDex() - 8) >> 1;
	}
	public int getEr() {
		int acEr = 0;
		int baseEr = CalcDexStat.evasionBonus(getTotalDex());
		if (character.getAC().getAc() < -100) {
			acEr = -((character.getAC().getAc() + 100) / 20);
		}
		return baseEr + acEr;
	}
	public int getAddEr() {
		return _er;
	}
	public void addEr(int i) {
		_er += i;
		if (character instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) character;
			pc.sendPackets(new S_OwnCharAttrDef(pc), true);
		}
	}
	public int getPlusEr() {
		int er = 0;
		er += getEr();
		er += getAddEr();
		if (er < 0) {
			er = 0;
		} else {
			if (character.getSkill().hasSkillEffect(L1SkillId.STRIKER_GALE)) {
				er = er / 3;
			}
		}
		return er;
	}
    
    public void addDg(int i) {
    	_dg += i;
    	if (character instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) character;
			pc.sendPackets(new S_OwnCharAttrDef(pc), true);
		}
    }
    public int getDg() {
    	int acDg = 0;
		if (character.getAC().getAc() < -100) {
			acDg = -((character.getAC().getAc() + 100) / 20);
		}
        return _dg + acDg;
    }
    
 	public int getMe() {
 		return _me;
 	}
 	public void addMe(int i) {
 		_me += i;
 		if (character instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) character;
			pc.sendPackets(new S_MagicEvasion(_me), true);
 		}
 	}
 	
    public int getBaseHpRate() {
    	return _base_hp_rate;
    }
    public void setBaseHpRate(int i) {
    	_base_hp_rate = i;
    }
    
    public int getBaseMpRate() {
    	return _base_mp_rate;
    }
    public void setBaseMpRate(int i) {
    	_base_mp_rate = i;
    }
 	
    public int getStatusPotionPlus() {
        return _statusPotionPlus;
    }
    public void setStatusPotionPlus() {
    	_statusPotionPlus = CalcConStat.hpIncPotion(getTotalCon());
    }
    
    public int getItemPotionRegist(){
    	return _itemPotionRegist;
    }
    public void addItemPotionRegist(int val){
    	_itemPotionRegist += val;
    }
    
    public int getItemPotionPercent(){
    	return _itemPotionPercent;
    }
    public void addItemPotionPercent(int val){
    	_itemPotionPercent += val;
    }
    
    public int getItemPotionValue(){
    	return _itemPotionValue;
    }
    public void addItemPotionValue(int val){
    	_itemPotionValue += val;
    }
    
    public int getPotionRecoveryRatePct() {
    	return _potionRecoveryRate;
    }
    public void addPotionRecoveryRatePct(int i) {
    	_potionRecoveryRate += i;
    }
 	
 	public int getSpellCooltimeDecrease(){
 		return _spellCooltimeDecrease;
 	}
 	public void setSpellCooltimeDecrease(int val){
 		_spellCooltimeDecrease = val;
 	}
 	
    public int getSpellDurationDecrease(){
    	return _spellDurationDecrease;
    }
    public void setSpellDurationDecrease(int val){
    	_spellDurationDecrease = val;
    }
    
    public int getStrangeTimeIncrease(){
    	return _strangeTimeIncrease;
    }
    public void addStrangeTimeIncrease(int value){
    	_strangeTimeIncrease += value;
    }
    
    public int getStrangeTimeDecrease(){
    	return _strangeTimeDecrease;
    }
    public void addStrangeTimeDecrease(int value){
    	_strangeTimeDecrease += value;
    }
    
    public boolean isUnderWater(){
    	return _underWater;
    }
    public void setUnderWater(boolean flag){
    	_underWater = flag;
    }
    
    public int getReturnLockDuraion(){
    	return _returnLockDuraion;
    }
    public void setReturnLockDuraion(int value){
    	_returnLockDuraion = value;
    }
    public void addReturnLockDuraion(int value){
    	_returnLockDuraion += value;
    }
    
    public int getHpPotionDelayDecrease(){
    	return _hpPotionDelayDecrease;
    }
    public void setHpPotionDelayDecrease(int value){
    	_hpPotionDelayDecrease = value;
    }
    public void addHpPotionDelayDecrease(int value){
    	_hpPotionDelayDecrease += value;
    }
    
    public int getHpPotionCriticalProb(){
    	return _hpPotionCriticalProb;
    }
    public void setHpPotionCriticalProb(int value){
    	_hpPotionCriticalProb = value;
    }
    public void addHpPotionCriticalProb(int value){
    	_hpPotionCriticalProb += value;
    }
    
    public int getIncreaseArmorSkillProb(){
    	return _increaseArmorSkillProb;
    }
    public void setIncreaseArmorSkillProb(int value){
    	_increaseArmorSkillProb = value;
    }
    public void addIncreaseArmorSkillProb(int value){
    	_increaseArmorSkillProb += value;
    }
    
    public int[] getRoomtisEarringDamage(){
    	return _roomtisEarringDamage;
    }
    public void setRoomtisEarringDamage(int[] value){
    	_roomtisEarringDamage = value;
    }
    
    public int[] getRoomtisEarringReduction(){
    	return _roomtisEarringReduction;
    }
    public void setRoomtisEarringReduction(int[] value){
    	_roomtisEarringReduction = value;
    }
    
    public int getSnaperRingReduction(){
    	return _snaperRingReduction;
    }
    public void addSnaperRingReduction(int value){
    	_snaperRingReduction += value;
    }
    
    public int getValakasShortCritical(){
    	return _valakasShortCritical;
    }
    public void addValakasShortCritical(int value){
    	_valakasShortCritical += value;
    }
    
    public int getValakasLongCritical(){
    	return _valakasLongCritical;
    }
    public void addValakasLongCritical(int value){
    	_valakasLongCritical += value;
    }
    
    public int getValakasMagicCritical(){
    	return _valakasMagicCritical;
    }
    public void addValakasMagicCritical(int value){
    	_valakasMagicCritical += value;
    }
	
	private int[] getBaseStatDiff(L1BaseStatus value) {
		int[] result = new int[6];
		result[0] = getBaseStr() - value.get_base_str();
		result[1] = getBaseDex() - value.get_base_dex();
		result[2] = getBaseCon() - value.get_base_con();
		result[3] = getBaseWis() - value.get_base_wis();
		result[4] = getBaseCha() - value.get_base_cha();
		result[5] = getBaseInt() - value.get_base_int();
		return result;
	}
	
	private L1BaseStatus getDefaultStat(int classId){
		switch (classId) {
		case L1CharacterInfo.CLASSID_PRINCE:
		case L1CharacterInfo.CLASSID_PRINCESS:
			return L1BaseStatus.CROWN;
		case L1CharacterInfo.CLASSID_KNIGHT_MALE:
		case L1CharacterInfo.CLASSID_KNIGHT_FEMALE:
			return L1BaseStatus.KNIGHT;
		case L1CharacterInfo.CLASSID_ELF_MALE:
		case L1CharacterInfo.CLASSID_ELF_FEMALE:
			return L1BaseStatus.ELF;
		case L1CharacterInfo.CLASSID_WIZARD_MALE:
		case L1CharacterInfo.CLASSID_WIZARD_FEMALE:
			return L1BaseStatus.WIZARD;
		case L1CharacterInfo.CLASSID_DARK_ELF_MALE:
		case L1CharacterInfo.CLASSID_DARK_ELF_FEMALE:
			return L1BaseStatus.DARKELF;
		case L1CharacterInfo.CLASSID_DRAGONKNIGHT_MALE:
		case L1CharacterInfo.CLASSID_DRAGONKNIGHT_FEMALE:
			return L1BaseStatus.DRAGONKNIGHT;
		case L1CharacterInfo.CLASSID_ILLUSIONIST_MALE:
		case L1CharacterInfo.CLASSID_ILLUSIONIST_FEMALE:
			return L1BaseStatus.ILLUSIONIST;
		case L1CharacterInfo.CLASSID_WARRIOR_MALE:
		case L1CharacterInfo.CLASSID_WARRIOR_FEMALE:
			return L1BaseStatus.WARRIOR;
		case L1CharacterInfo.CLASSID_FENCER_MALE:
		case L1CharacterInfo.CLASSID_FENCER_FEMALE:
			return L1BaseStatus.FENCER;
		case L1CharacterInfo.CLASSID_LANCER_MALE:
		case L1CharacterInfo.CLASSID_LANCER_FEMALE:
			return L1BaseStatus.LANCER;
		default:
			return null;
		}
	}

	public int[] getMinStat(final int classId) {
		return getBaseStatDiff(getDefaultStat(classId));
	}
	
	public L1BaseStatus getClassChangeMinStat(final int classId) {
		return getDefaultStat(classId);
	}

}

