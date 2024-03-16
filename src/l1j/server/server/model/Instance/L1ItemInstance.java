package l1j.server.server.model.Instance;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.Config;
import l1j.server.GameSystem.charactertrade.CharacterTradeManager;
import l1j.server.GameSystem.charactertrade.bean.CharacterTradeObject;
import l1j.server.GameSystem.charactertrade.loader.CharacterTradeLoader;
import l1j.server.GameSystem.shoplimit.ShopLimitUser;
import l1j.server.GameSystem.shoplimit.ShopLimitLoader;
import l1j.server.GameSystem.shoplimit.bean.ShopLimitInformation;
import l1j.server.GameSystem.shoplimit.bean.ShopLimitObject;
import l1j.server.common.data.Material;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.L1Grade;
import l1j.server.server.construct.item.L1ItemArmorType;
import l1j.server.server.construct.item.L1ItemNormalType;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.datatables.ArmorSetTable;
import l1j.server.server.datatables.CharacterCompanionTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.WeaponSkillTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1EquipmentTimer;
import l1j.server.server.model.L1ItemOwnerTimer;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1WeaponSkill;
import l1j.server.server.model.L1WeaponSkill.WeaponSkillAttackType;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.item.ablity.ItemAbility;
import l1j.server.server.model.item.ablity.ItemAbilityFactory;
import l1j.server.server.model.item.ablity.enchant.L1EnchantAblity;
import l1j.server.server.model.item.ablity.enchant.L1EnchantFactory;
import l1j.server.server.model.item.potential.L1Potential;
import l1j.server.server.serverpackets.inventory.S_UpdateInventoryNoti;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Armor;
import l1j.server.server.templates.L1ArmorSets;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1Pet;
import l1j.server.server.templates.L1WeaponSkillTemp;
import l1j.server.server.utils.BinaryOutputStreamCompanionCard;
import l1j.server.server.utils.BinaryOutputStreamExtraDescription;
import l1j.server.server.utils.BinaryOutputStreamItemInfo;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.StringUtil;

public class L1ItemInstance extends L1Object {
	private byte[] extraDescription;
	private byte str, dex, con, inti, wis, cha;
	private int carryBonus, magicRegist,
				maxHp, maxMp, baseHpRate, baseMpRate, hpRegen, mpRegen, 
				spellpower, shortHit, longHit, shortDamage, longDamage, damageReduction, damageReductionEgnor,
				shortCritical, longCritical, magicCritical, acBonus, hprAbsol32Second, mprAbsol64Second, mprAbsol16Second,
				PVPDamage, PVPDamageReduction, PVPDamageReductionPercent, PVPMagicDamageReduction, PVPDamageReductionEgnor, PVPMagicDamageReductionEgnor, 
				restExpReduceEfficiency, expBonus,
				evasion, evasionRegist, magicEvasion, fowslayerDamage, titanUp, damageChanceEtc,
				registFreeze, registStone, registSleep, registBlind,
				toleranceSkill, toleranceSpirit, toleranceDragon, toleranceFear, toleranceAll,
				hitupSkill, hitupSpirit, hitupDragon, hitupFear, hitupAll, magicHit,
				attrFire, attrWater, attrWind, attrEarth, attrAll, poisonRegist, polyDescId,
				buffDurationSecond, addExpPercent, addExpPercentPCCafe,
				hpDrain, mpDrain, dragonDamageReduction, blessExp, elixerBooster,
				potionRegist, potionPercent, potionValue,
				thirdSpeed, fourthSpeed, magicDamageReduction, emunEgnor, stunDuration,
				damageReductionPercent, magicDamage, vanguardTime, magicCriticalDamageAdd, tripleArrowStun, reflectEmasculate, 
				strangeTimeIncrease, strangeTimeDecrease, hpPotionDelayDecrease, hpPotionCriticalProb, increaseArmorSkillProb, returnLockDuraion,
				attackSpeedDelayRate, moveSpeedDelayRate, 
				abnormalStatusDamageReduction, abnormalStatusPVPDamageReduction, PVPDamagePercent;
	private int acSub = -1, foodType = -1;
	private int[] damageReductionChance = new int[2], damageChance = new int[2];
	private String magicName;
	private boolean undead, demon, dranium;
	
	public int getAcSub(){								return acSub;							}
	public int getCarryBonus(){							return carryBonus;						}
	public byte getStr(){								return str;								}
	public byte getDex(){								return dex;								}
	public byte getCon(){								return con;								}
	public byte getInt(){								return inti;							}
	public byte getWis(){								return wis;								}
	public byte getCha(){								return cha;								}
	public int getMagicRegist(){						return magicRegist;						}
	public int getMaxHp(){								return maxHp;							}
	public int getMaxMp(){								return maxMp;							}
	public int getBaseHpRate(){							return baseHpRate;						}
	public int getBaseMpRate(){							return baseMpRate;						}
	public int getHpRegen(){							return hpRegen;							}
	public int getMpRegen(){							return mpRegen;							}
	public int getSpellpower(){							return spellpower;						}
	public int getShortHit(){							return shortHit;						}
	public int getLongHit(){							return longHit;							}
	public int getShortDamage(){						return shortDamage;						}
	public int getLongDamage(){							return longDamage;						}
	public int getDamageReduction(){					return damageReduction;					}
	public int[] getDamageReductionChance(){			return damageReductionChance;			}
	public int getDamageReductionEgnor(){				return damageReductionEgnor;			}
	public int[] getDamageChance(){						return damageChance;					}
	public int getShortCritical(){						return shortCritical;					}
	public int getLongCritical(){						return longCritical;					}
	public int getMagicCritical(){						return magicCritical;					}
	public int getAcBonus(){							return acBonus;							}
	public int getHprAbsol32Second(){					return hprAbsol32Second;				}
	public int getMprAbsol64Second(){					return mprAbsol64Second;				}
	public int getMprAbsol16Second(){					return mprAbsol16Second;				}
	public int getPVPDamage(){							return PVPDamage;						}
	public int getPVPDamageReduction(){					return PVPDamageReduction;				}
	public int getPVPDamageReductionPercent(){			return PVPDamageReductionPercent;		}
	public int getPVPMagicDamageReduction(){			return PVPMagicDamageReduction;			}
	public int getPVPDamageReductionEgnor(){			return PVPDamageReductionEgnor;			}
	public int getPVPMagicDamageReductionEgnor(){		return PVPMagicDamageReductionEgnor;	}
	public int getRestExpReduceEfficiency(){			return restExpReduceEfficiency;			}
	public int getExpBonus(){							return expBonus;						}
	public int getEvasion(){							return evasion;							}
	public int getEvasionRegist(){						return evasionRegist;					}
	public int getMagicEvasion(){						return magicEvasion;					}
	public int getFowslayerDamage(){					return fowslayerDamage;					}
	public int getTitanUp(){							return titanUp;							}
	public int getDamageChanceEtc(){					return damageChanceEtc;					}
	public int getRegistFreeze(){						return registFreeze;					}
	public int getRegistStone(){						return registStone;						}
	public int getRegistSleep(){						return registSleep;						}
	public int getRegistBlind(){						return registBlind;						}
	public int getToleranceSkill(){						return toleranceSkill;					}
	public int getToleranceSpirit(){					return toleranceSpirit;					}
	public int getToleranceDragon(){					return toleranceDragon;					}
	public int getToleranceFear(){						return toleranceFear;					}
	public int getToleranceAll(){						return toleranceAll;					}
	public int getHitupSkill(){							return hitupSkill;						}
	public int getHitupSpirit(){						return hitupSpirit;						}
	public int getHitupDragon(){						return hitupDragon;						}
	public int getHitupFear(){							return hitupFear;						}
	public int getHitupAll(){							return hitupAll;						}
	public int getMagicHit(){							return magicHit;						}
	public int getAttrFire(){							return attrFire;						}
	public int getAttrWater(){							return attrWater;						}
	public int getAttrWind(){							return attrWind;						}
	public int getAttrEarth(){							return attrEarth;						}
	public int getAttrAll(){							return attrAll;							}
	public int getPoisonRegist(){						return poisonRegist;					}
	public int getPolyDescId(){							return polyDescId;						}
	public boolean isUndead(){							return undead;							}
	public boolean isDemon(){							return demon;							}
	public int getBuffDurationSecond(){					return buffDurationSecond;				}
	public int getAddExpPercent(){						return addExpPercent;					}
	public int getAddExpPercentPCCafe(){				return addExpPercentPCCafe;				}
	public int getFoodType(){							return foodType;						}
	public int getHpDrain(){							return hpDrain;							}
	public int getMpDrain(){							return mpDrain;							}
	public int getDragonDamageReduction(){				return dragonDamageReduction;			}
	public int getBlessExp(){							return blessExp;						}
	public int getElixerBooster(){						return elixerBooster;					}
	public int getPotionRegist(){						return potionRegist;					}
	public int getPotionPercent(){						return potionPercent;					}
	public int getPotionValue(){						return potionValue;						}
	public String getMagicName(){						return magicName;						}
	public int getThirdSpeed(){							return thirdSpeed;						}
	public int getFourthSpeed(){						return fourthSpeed;						}
	public int getMagicDamageReduction(){				return magicDamageReduction;			}
	public int getEmunEgnor(){							return emunEgnor;						}
	public int getStunDuration(){						return stunDuration;					}
	public boolean getDranium(){						return dranium;							}
	public int getDamageReductionPercent(){				return damageReductionPercent;			}
	public int getMagicDamage(){						return magicDamage;						}
	public int getVanguardTime(){						return vanguardTime;					}
	public int getMagicCriticalDamageAdd(){				return magicCriticalDamageAdd;			}
	public int getTripleArrowStun(){					return tripleArrowStun;					}
	public int getReflectEmasculate(){					return reflectEmasculate;				}
	public int getStrangeTimeIncrease(){				return strangeTimeIncrease;				}
	public int getStrangeTimeDecrease(){				return strangeTimeDecrease;				}
	public int getHpPotionDelayDecrease(){				return hpPotionDelayDecrease;			}
	public int getHpPotionCriticalProb(){				return hpPotionCriticalProb;			}
	public int getIncreaseArmorSkillProb(){				return increaseArmorSkillProb;			}
	public int getReturnLockDuraion(){					return returnLockDuraion;				}
	public int getAttackSpeedDelayRate(){				return attackSpeedDelayRate;			}
	public int getMoveSpeedDelayRate(){					return moveSpeedDelayRate;				}
	public int getAbnormalStatusDamageReduction(){		return abnormalStatusDamageReduction;	}
	public int getAbnormalStatusPVPDamageReduction(){	return abnormalStatusPVPDamageReduction;}
	public int getPVPDamagePercent(){					return PVPDamagePercent;				}
	
	private ConcurrentHashMap<ItemAbilityFactory, ItemAbility> ABILITYS;
	
	/**
	 * 아이템 세부 옵션 갱신
	 */
	public void updateItemAbility(L1PcInstance pc){
		if (ABILITYS == null) {
			ABILITYS = ItemAbilityFactory.getAblitys();
		}
		_owner							= pc;
		acSub							= (int) ABILITYS.get(ItemAbilityFactory.AC_SUB).info(this, _owner);
		carryBonus						= (int) ABILITYS.get(ItemAbilityFactory.CARRY_BONUS).info(this, _owner);
		str								= (byte) ABILITYS.get(ItemAbilityFactory.STR).info(this, _owner);
		dex								= (byte) ABILITYS.get(ItemAbilityFactory.DEX).info(this, _owner);
		con								= (byte) ABILITYS.get(ItemAbilityFactory.CON).info(this, _owner);
		inti							= (byte) ABILITYS.get(ItemAbilityFactory.INT).info(this, _owner);
		wis								= (byte) ABILITYS.get(ItemAbilityFactory.WIS).info(this, _owner);
		cha								= (byte) ABILITYS.get(ItemAbilityFactory.CHA).info(this, _owner);
		magicRegist						= (int) ABILITYS.get(ItemAbilityFactory.MAGIC_REGIST).info(this, _owner);
		maxHp							= (int) ABILITYS.get(ItemAbilityFactory.MAX_HP).info(this, _owner);
		maxMp							= (int) ABILITYS.get(ItemAbilityFactory.MAX_MP).info(this, _owner);
		baseHpRate						= (int) ABILITYS.get(ItemAbilityFactory.BASE_HP_RATE).info(this, _owner);
		baseMpRate						= (int) ABILITYS.get(ItemAbilityFactory.BASE_MP_RATE).info(this, _owner);
		hpRegen							= (int) ABILITYS.get(ItemAbilityFactory.HP_REGEN).info(this, _owner);
		mpRegen							= (int) ABILITYS.get(ItemAbilityFactory.MP_REGEN).info(this, _owner);
		spellpower						= (int) ABILITYS.get(ItemAbilityFactory.SPELLPOWER).info(this, _owner);
		shortHit						= (int) ABILITYS.get(ItemAbilityFactory.SHORT_HIT).info(this, _owner);
		longHit							= (int) ABILITYS.get(ItemAbilityFactory.LONG_HIT).info(this, _owner);
		shortDamage						= (int) ABILITYS.get(ItemAbilityFactory.SHORT_DAMAGE).info(this, _owner);
		longDamage						= (int) ABILITYS.get(ItemAbilityFactory.LONG_DAMAGE).info(this, _owner);
		damageReduction					= (int) ABILITYS.get(ItemAbilityFactory.DAMAGE_REDUCTION).info(this, _owner);
		damageReductionChance			= (int[]) ABILITYS.get(ItemAbilityFactory.DAMAGE_REDUCTION_CHANCE).info(this, _owner);
		damageReductionEgnor			= (int) ABILITYS.get(ItemAbilityFactory.DAMAGE_REDUCTION_EGNOR).info(this, _owner);
		damageChance					= (int[]) ABILITYS.get(ItemAbilityFactory.DAMAGE_CHANCE).info(this, _owner);
		shortCritical					= (int) ABILITYS.get(ItemAbilityFactory.SHORT_CRITICAL).info(this, _owner);
		longCritical					= (int) ABILITYS.get(ItemAbilityFactory.LONG_CRITICAL).info(this, _owner);
		magicCritical					= (int) ABILITYS.get(ItemAbilityFactory.MAGIC_CRITICAL).info(this, _owner);
		acBonus							= (int) ABILITYS.get(ItemAbilityFactory.AC_BONUS).info(this, _owner);
		hprAbsol32Second				= (int) ABILITYS.get(ItemAbilityFactory.HP_ABSOL_REGEN_32_SECOND).info(this, _owner);
		mprAbsol64Second				= (int) ABILITYS.get(ItemAbilityFactory.MP_ABSOL_REGEN_64_SECOND).info(this, _owner);
		mprAbsol16Second				= (int) ABILITYS.get(ItemAbilityFactory.MP_ABSOL_REGEN_16_SECOND).info(this, _owner);
		PVPDamage						= (int) ABILITYS.get(ItemAbilityFactory.PVP_DAMAGE).info(this, _owner);
		PVPDamageReduction				= (int) ABILITYS.get(ItemAbilityFactory.PVP_DAMAGE_REDUCTION).info(this, _owner);
		PVPDamageReductionPercent		= (int) ABILITYS.get(ItemAbilityFactory.PVP_DAMAGE_REDUCTION_PERCENT).info(this, _owner);
		PVPMagicDamageReduction			= (int) ABILITYS.get(ItemAbilityFactory.PVP_MAGIC_DAMAGE_REDUCTION).info(this, _owner);
		PVPDamageReductionEgnor			= (int) ABILITYS.get(ItemAbilityFactory.PVP_DAMAGE_REDUCTION_EGNOR).info(this, _owner);
		PVPMagicDamageReductionEgnor	= (int) ABILITYS.get(ItemAbilityFactory.PVP_MAGIC_DAMAGE_REDUCTION_EGNOR).info(this, _owner);
		restExpReduceEfficiency			= (int) ABILITYS.get(ItemAbilityFactory.REST_EXP_REDUCE_EFFICIENCY).info(this, _owner);
		expBonus						= (int) ABILITYS.get(ItemAbilityFactory.EXP_BONUS).info(this, _owner);
		evasion							= (int) ABILITYS.get(ItemAbilityFactory.EVASION).info(this, _owner);
		evasionRegist					= (int) ABILITYS.get(ItemAbilityFactory.EVASION_REGIST).info(this, _owner);
		magicEvasion					= (int) ABILITYS.get(ItemAbilityFactory.MAGIC_EVASION).info(this, _owner);
		fowslayerDamage					= (int) ABILITYS.get(ItemAbilityFactory.FOW_DAMAGE).info(this, _owner);
		titanUp							= (int) ABILITYS.get(ItemAbilityFactory.TITAN_UP).info(this, _owner);
		damageChanceEtc					= (int) ABILITYS.get(ItemAbilityFactory.DAMAGE_CHANCE_ETC).info(this, _owner);
		registFreeze					= (int) ABILITYS.get(ItemAbilityFactory.REGIST_FREEZE).info(this, _owner);
		registStone						= (int) ABILITYS.get(ItemAbilityFactory.REGIST_STONE).info(this, _owner);
		registSleep						= (int) ABILITYS.get(ItemAbilityFactory.REGIST_SLEEP).info(this, _owner);
		registBlind						= (int) ABILITYS.get(ItemAbilityFactory.REGIST_BLIND).info(this, _owner);
		toleranceSkill					= (int) ABILITYS.get(ItemAbilityFactory.TOLERANCE_SKILL).info(this, _owner);
		toleranceSpirit					= (int) ABILITYS.get(ItemAbilityFactory.TOLERANCE_SPIRIT).info(this, _owner);
		toleranceDragon					= (int) ABILITYS.get(ItemAbilityFactory.TOLERANCE_DRAGON).info(this, _owner);
		toleranceFear					= (int) ABILITYS.get(ItemAbilityFactory.TOLERANCE_FEAR).info(this, _owner);
		toleranceAll					= (int) ABILITYS.get(ItemAbilityFactory.TOLERANCE_ALL).info(this, _owner);
		hitupSkill						= (int) ABILITYS.get(ItemAbilityFactory.HITUP_SKILL).info(this, _owner);
		hitupSpirit						= (int) ABILITYS.get(ItemAbilityFactory.HITUP_SPIRIT).info(this, _owner);
		hitupDragon						= (int) ABILITYS.get(ItemAbilityFactory.HITUP_DRAGON).info(this, _owner);
		hitupFear						= (int) ABILITYS.get(ItemAbilityFactory.HITUP_FEAR).info(this, _owner);
		hitupAll						= (int) ABILITYS.get(ItemAbilityFactory.HITUP_ALL).info(this, _owner);
		magicHit						= (int) ABILITYS.get(ItemAbilityFactory.MAGIC_HIT).info(this, _owner);
		attrFire						= (int) ABILITYS.get(ItemAbilityFactory.ATTR_FIRE).info(this, _owner);
		attrWater						= (int) ABILITYS.get(ItemAbilityFactory.ATTR_WATER).info(this, _owner);
		attrWind						= (int) ABILITYS.get(ItemAbilityFactory.ATTR_WIND).info(this, _owner);
		attrEarth						= (int) ABILITYS.get(ItemAbilityFactory.ATTR_EARTH).info(this, _owner);
		attrAll							= (int) ABILITYS.get(ItemAbilityFactory.ATTR_ALL).info(this, _owner);
		poisonRegist					= (int) ABILITYS.get(ItemAbilityFactory.POISON_REGIST).info(this, _owner);
		polyDescId						= (int) ABILITYS.get(ItemAbilityFactory.POLY_DESC).info(this, _owner);
		undead							= (boolean) ABILITYS.get(ItemAbilityFactory.UNDEAD).info(this, _owner);
		demon							= (boolean) ABILITYS.get(ItemAbilityFactory.DEMON).info(this, _owner);
		buffDurationSecond				= (int) ABILITYS.get(ItemAbilityFactory.BUFF_DURATION_SECOND).info(this, _owner);
		addExpPercent					= (int) ABILITYS.get(ItemAbilityFactory.ADD_EXP_PERCENT).info(this, _owner);
		addExpPercentPCCafe				= (int) ABILITYS.get(ItemAbilityFactory.ADD_EXP_PERCENT_PC_CAFE).info(this, _owner);
		foodType						= (int) ABILITYS.get(ItemAbilityFactory.FOOD_TYPE).info(this, _owner);
		hpDrain							= (int) ABILITYS.get(ItemAbilityFactory.HP_DRAIN).info(this, _owner);
		mpDrain							= (int) ABILITYS.get(ItemAbilityFactory.MP_DRAIN).info(this, _owner);
		dragonDamageReduction			= (int) ABILITYS.get(ItemAbilityFactory.DRAGON_DAMAGE_REDUCTION).info(this, _owner);
		blessExp						= (int) ABILITYS.get(ItemAbilityFactory.BLESS_EXP).info(this, _owner);
		elixerBooster					= (int) ABILITYS.get(ItemAbilityFactory.ELIXER_BOOSTER).info(this, _owner);
		potionRegist					= (int) ABILITYS.get(ItemAbilityFactory.POTION_REGIST).info(this, _owner);
		potionPercent					= (int) ABILITYS.get(ItemAbilityFactory.POTION_PERCENT).info(this, _owner);
		potionValue						= (int) ABILITYS.get(ItemAbilityFactory.POTION_VALUE).info(this, _owner);
		magicName						= (String) ABILITYS.get(ItemAbilityFactory.MAGIC_NAME).info(this, _owner);
		thirdSpeed						= (int) ABILITYS.get(ItemAbilityFactory.THIRD_SPEED).info(this, _owner);
		fourthSpeed						= (int) ABILITYS.get(ItemAbilityFactory.FOURTH_SPEED).info(this, _owner);
		magicDamageReduction			= (int) ABILITYS.get(ItemAbilityFactory.MAGIC_DAMAGE_REDUCTION).info(this, _owner);
		emunEgnor						= (int) ABILITYS.get(ItemAbilityFactory.EMUN_EGNOR).info(this, _owner);
		stunDuration					= (int) ABILITYS.get(ItemAbilityFactory.STUN_DURATION).info(this, _owner);
		dranium							= (boolean) ABILITYS.get(ItemAbilityFactory.DRANIUM).info(this, _owner);
		damageReductionPercent			= (int) ABILITYS.get(ItemAbilityFactory.DAMAGE_REDUCTION_PERCENT).info(this, _owner);
		magicDamage						= (int) ABILITYS.get(ItemAbilityFactory.MAGIC_DAMAGE).info(this, _owner);
		vanguardTime					= (int) ABILITYS.get(ItemAbilityFactory.VANGUARD_TIME).info(this, _owner);
		magicCriticalDamageAdd			= (int) ABILITYS.get(ItemAbilityFactory.MAGIC_CRITICAL_DMG_ADD).info(this, _owner);
		tripleArrowStun					= (int) ABILITYS.get(ItemAbilityFactory.TRIPLE_ARROW_STUN).info(this, _owner);
		reflectEmasculate				= (int) ABILITYS.get(ItemAbilityFactory.REFLECT_EMASCULATE).info(this, _owner);
		strangeTimeIncrease				= (int) ABILITYS.get(ItemAbilityFactory.STRANGE_TIME_INCREASE).info(this, _owner);
		strangeTimeDecrease				= (int) ABILITYS.get(ItemAbilityFactory.STRANGE_TIME_DECREASE).info(this, _owner);
		hpPotionDelayDecrease			= (int) ABILITYS.get(ItemAbilityFactory.HP_POTION_DELAY_DECREASE).info(this, _owner);
		hpPotionCriticalProb			= (int) ABILITYS.get(ItemAbilityFactory.HP_POTION_CRITICAL_PROB).info(this, _owner);
		increaseArmorSkillProb			= (int) ABILITYS.get(ItemAbilityFactory.INCREASE_ARMOR_SKILL_PROB).info(this, _owner);
		returnLockDuraion				= (int) ABILITYS.get(ItemAbilityFactory.RETURNLOCK_DURATION).info(this, _owner);
		attackSpeedDelayRate			= (int) ABILITYS.get(ItemAbilityFactory.ATTACK_SPEED_DELAY_RATE).info(this, _owner);
		moveSpeedDelayRate				= (int) ABILITYS.get(ItemAbilityFactory.MOVE_SPEED_DELAY_RATE).info(this, _owner);
		abnormalStatusDamageReduction	= (int) ABILITYS.get(ItemAbilityFactory.ABNORMAL_STATUS_DAMAGE_REDUCTION).info(this, _owner);
		abnormalStatusPVPDamageReduction= (int) ABILITYS.get(ItemAbilityFactory.ABNORMAL_STATUS_PVP_DAMAGE_REDUCTION).info(this, _owner);
		PVPDamagePercent				= (int) ABILITYS.get(ItemAbilityFactory.PVP_DAMAGE_PERCENT).info(this, _owner);
		
		// stream 갱신
		updateExtraDescription();
	}
	
	/**
	 * 아이템 상태가 변경되면 표기 데이터를 갱신한다.
	 */
	public void updateExtraDescription() {
		extraDescription	= getExtraDescription();
	}
	
	public static final String[] ATTR_DESCRIPTIONS = {
		StringUtil.EmptyString,										// 무속성
		"$6115",	"$6116",	"$6117",	"$14361",	"$14365",	// 화령
		"$6118",	"$6119",	"$6120",	"$14362",	"$14366",	// 수령
		"$6121",	"$6122",	"$6123",	"$14363",	"$14367",	// 풍령
		"$6124",	"$6125",	"$6126",	"$14364",	"$14368",	// 지령
    };

    public static String getAttributeEnchantDescription(int attrLevel){
    	return attrLevel >= ATTR_DESCRIPTIONS.length ? StringUtil.EmptyString : attrLevel < 0 ? StringUtil.EmptyString : ATTR_DESCRIPTIONS[attrLevel];
    }
    private static final int[] _attrMask = new int[] { 
    	0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4 
    };
	public static int attrEnchantToElementalType(int attrLevel){
		return _attrMask[attrLevel];
	}
	public static int attrEnchantToElementalType(L1ItemInstance item){
		return attrEnchantToElementalType(item.getAttrEnchantLevel());
	}
	public static int pureAttrEnchantLevel(int attrLevel){
		return attrLevel <= 0 ? attrLevel : attrLevel - ((attrEnchantToElementalType(attrLevel) - 1) * 5);
	}
	public static int pureAttrEnchantLevel(L1ItemInstance item){
		return pureAttrEnchantLevel(item.getAttrEnchantLevel());
	}
	
	public static boolean equalsElement(L1ItemInstance item, int elementalType, int elementalValue){
		int attr = item.getAttrEnchantLevel();
		int type = attrEnchantToElementalType(attr);
		if (type != elementalType) {
			return false;
		}
		int value = attr - ((type - 1) * 5);
		return value == elementalValue;
	}
	
	public static int calculateElementalEnchant(int elementalType, int elementalValue){
		if (elementalType <= 0 || elementalValue <= 0) {
			return 0;
		}
		return ((elementalType - 1) * 5) + elementalValue;
	}

	private static final long serialVersionUID = 1L;
	private L1Item _item;
	private final LastStatus _lastStatus = new LastStatus();
	public L1PcInstance _owner;

	public L1ItemInstance() {
		_count = _bless = 1;
		_enchantLevel = _specialEnchant = 0;
	}
	
	public L1ItemInstance(L1Item item) {
		this(item, 1);
	}

	public L1ItemInstance(L1Item item, int count) {
		this();
		setItem(item);
		_count = count;
		_bless = item.getBless();
	}

	public void clickItem(L1Character cha, ClientBasePacket packet) {
	}
	
	@Override
	public void onAction(L1PcInstance player) {
	}
	
	public String getDesc() {
		return _item.getDesc();
	}
	
	public String getDescKr() {
		return _item.getDescKr();
	}

	public String getDescEn() {
		return _item.getDescEn();
	}
	
	public L1Item getItem() {
		return _item;
	}
	
	public void setItem(L1Item item) {
		_item	= item;
		_itemId	= item.getItemId();
	}

	private int _specialEnchant;
	public int getSpecialEnchant() {
		return _specialEnchant;
	}
	public void setSpecialEnchant(int enchant) {
		_specialEnchant = enchant;
	}

	private boolean _isIdentified;
	public boolean isIdentified() {
		return _isIdentified;
	}
	public void setIdentified(boolean identified) {
		_isIdentified = identified;
	}

	private int _count;
	public int getCount() {
		return _count;
	}
	public void setCount(int count) {
		_count = count;
	}

	private boolean _isEquipped;
	public boolean isEquipped() {
		return _isEquipped;
	}
	public void setEquipped(boolean equipped) {
		_isEquipped = equipped;
	}

	private int _itemId;
	public int getItemId() {
		return _itemId;
	}
	public void setItemId(int itemId) {
		_itemId = itemId;
	}

	public boolean isMerge() {
		return _item.isMerge();
	}

	private int _enchantLevel;
	public int getEnchantLevel() {
		return _enchantLevel;
	}
	public void setEnchantLevel(int enchantLevel) {
		_enchantLevel = enchantLevel;
	}
	
	private int _attrenchantLevel;
	public int getAttrEnchantLevel() {
		return _attrenchantLevel;
	}
	public void setAttrEnchantLevel(int attrenchantLevel) {
		_attrenchantLevel = attrenchantLevel;
	}

	public int getIconId() {
		return _item.getIconId();
	}

	private int _durability;
	public int getDurability() {
		return _durability;
	}
	public void setDurability(int i){
		_durability = IntRange.ensure(i, 0, 127);
	}

	private int _chargeCount;
	public int getChargeCount() {
		return _chargeCount;
	}
	public void setChargeCount(int i) {
		_chargeCount = i;
	}

	private int _remainingTime;
	public int getRemainingTime() {
		return _remainingTime;
	}
	public void setRemainingTime(int i) {
		_remainingTime = i;
	}

	private Timestamp _lastUsed;
	public void setLastUsed(Timestamp t) {
		_lastUsed = t;
	}
	public Timestamp getLastUsed() {
		return _lastUsed;
	}

	private int _bless;
	public int getBless() {
		return _bless;
	}
	public void setBless(int i) {
		_bless = i;
	}

	private int _lastWeight;
	public int getLastWeight() {
		return _lastWeight;
	}
	public void setLastWeight(int weight) {
		_lastWeight = weight;
	}
	
	private int _key;
	public int getKey() {
		return _key;
	}
	public void setKey(int t) {
		_key = t;
	}

	private Timestamp _endTime;
	public Timestamp getEndTime() {
		return _endTime;
	}
	public void setEndTime(Timestamp t) {
		_endTime = t;
	}
	
	private String _createrName;
	public String getCreaterName() {
		return _createrName;
	}
	public void setCreaterName(String name) {
		_createrName = name;
	}
	
	private boolean _isPackage;
	public boolean isPackage() {
		return _isPackage;
	}
	public void setPackage(boolean _isPackage) {
		this._isPackage = _isPackage;
	}

	private long _itemDelay;
	public long getItemDelay() {
		return _itemDelay;
	}
	public void setItemDelay(long itemdelay) {
		_itemDelay = itemdelay;
	}
	
	private byte[] _companion_card;
	public byte[] get_companion_card_byte() {
		if (_companion_card == null) {
			_companion_card = getCompanionCardBytes();
		}
		return _companion_card;
	}
	public void update_companion_card_byte(L1Pet companion) {
		_companion_card = getCompanionCardBytes(companion);
	}
	
	private S_UpdateInventoryNoti _companion_card_cache;
	public S_UpdateInventoryNoti get_companion_card_cache() {
		if (_companion_card_cache == null) {
			_companion_card_cache = new S_UpdateInventoryNoti(getId(), get_companion_card_byte());
		}
		return _companion_card_cache;
	}
	public void update_companion_card_cache(L1Pet companion) {
		if (_companion_card_cache != null) {
			_companion_card_cache.clear();
		}
		update_companion_card_byte(companion);
		_companion_card_cache = new S_UpdateInventoryNoti(getId(), _companion_card);
	}
	
	private boolean _scheduled;
	public boolean isScheduled() {
		return _scheduled;
	}
	public void setScheduled(boolean flag) {
		_scheduled = flag;
	}
	
	private boolean _engrave;
	public boolean isEngrave(){
		return _engrave;
	}
	public void setEngrave(boolean flag){
		_engrave = flag;
	}
	
	private L1Potential _potential;
	public L1Potential getPotential(){
		return _potential;
	}
	public void setPotential(L1Potential obj){
		_potential = obj;
	}
	
	private HashMap<Integer, L1Item> _slots;
	
	/**
	 * 슬롯(제련석)
	 * @return HashMap<Integer, L1Item>
	 */
	public HashMap<Integer, L1Item> getSlots() {
		return _slots;
	}
	
	/**
	 * 아이템 슬롯에 제련석을 장착한다.
	 * @param slot_no
	 * @param stone
	 * @return success: slot_no, failure: -1
	 */
	public int insertSlot(int slot_no, L1Item stone) {
		if (stone == null) {
			return -1;
		}
		if (_slots == null) {
			_slots = new HashMap<>(Config.SMELTING.SMELTING_LIMIT_SLOT_VALUE);
		}
		if (_slots.size() >= Config.SMELTING.SMELTING_LIMIT_SLOT_VALUE) {
			return -1;
		}
		_slots.put(slot_no, stone);
		return slot_no;
	}
	
	/**
	 * 아이템 슬롯에서 제련석 추출
	 * @param slot_no
	 * @return success: L1Item, failure: null
	 */
	public L1Item ejectSlot(int slot_no) {
		return _slots == null ? null : _slots.remove(slot_no);
	}
	
	/**
	 * 슬롯 장착 조사
	 * @return boolean
	 */
	public boolean isSlot() {
		return _slots != null && !_slots.isEmpty();
	}
	
	/**
	 * 인첸트에 해당하는 보너스 옵션 조사
	 * @return L1EnchantAblity
	 */
	public L1EnchantAblity getEnchantAblity(){
		L1EnchantFactory factory = _item.getEnchantInfo();
		return factory == null ? null : factory.get(_enchantLevel);
	}
	
	private L1WeaponSkillTemp _weaponSkill;
	
	/**
	 * 무기 스킬 조사
	 * @param target
	 * @return L1WeaponSkill
	 */
	public L1WeaponSkill getWeaponSkill(L1Character target){
		if (_weaponSkill == null) {
			return null;
		}
		return target instanceof L1PcInstance ? _weaponSkill.getSkillPVP() : _weaponSkill.getSkillPVE();
	}
	
	/**
	 * 무기 스킬을 설정한다.
	 */
	public void setWeaponSkill(){
		if (_item.getItemType() != L1ItemType.WEAPON) {
			return;
		}
		ConcurrentHashMap<WeaponSkillAttackType, L1WeaponSkill> skills = WeaponSkillTable.getWeaponSkill(this.getItemId());
		if (skills == null || skills.isEmpty()) {
			return;
		}
		
		_weaponSkill = new L1WeaponSkillTemp();
		L1WeaponSkill skill = skills.get(WeaponSkillAttackType.ALL);
		if (skill != null) {
			_weaponSkill.setSkillPVP(skill);
			_weaponSkill.setSkillPVE(skill);
			return;
		}
		skill = skills.get(WeaponSkillAttackType.PVP);
		if (skill != null) {
			_weaponSkill.setSkillPVP(skill);
		}
		skill = skills.get(WeaponSkillAttackType.PVE);
		if (skill != null) {
			_weaponSkill.setSkillPVE(skill);
		}
	}

	public int getWeight() {
		int val = _item.getWeight();
		return val == 0 ? 0 : Math.max(getCount() * val / 1000, 1);
	}

	public class LastStatus {
		public int count, itemId, bless, enchantLevel, attrenchantLevel, specialEnchant, durability, chargeCount, remainingTime;
		public boolean isEquipped, isIdentified = true, isScheduled;
		public Timestamp lastUsed, endTime;
		public L1Potential potential;
		public HashMap<Integer, L1Item> slots;

		public void updateAll() {
			count				= getCount();
			itemId				= getItemId();
			isEquipped			= isEquipped();
			isIdentified		= isIdentified();
			enchantLevel		= getEnchantLevel();
			durability			= getDurability();
			chargeCount			= getChargeCount();
			remainingTime		= getRemainingTime();
			lastUsed			= getLastUsed();
			bless				= getBless();
			attrenchantLevel	= getAttrEnchantLevel();
			specialEnchant		= getSpecialEnchant();
			potential			= getPotential();
			slots				= getSlots();
			endTime				= getEndTime();
			isScheduled			= isScheduled();
		}

		public void updateCount() {
			count = getCount();
		}
		public void updateItemId() {
			itemId = getItemId();
		}
		public void updateEquipped() {
			isEquipped = isEquipped();
		}
		public void updateIdentified() {
			isIdentified = isIdentified();
		}
		public void updateEnchantLevel() {
			enchantLevel = getEnchantLevel();
		}
		public void updateDuraility() {
			durability = getDurability();
		}
		public void updateChargeCount() {
			chargeCount = getChargeCount();
		}
		public void updateRemainingTime() {
			remainingTime = getRemainingTime();
		}
		public void updateLastUsed() {
			lastUsed = getLastUsed();
		}
		public void updateBless() {
			bless = getBless();
		}
		public void updateAttrEnchantLevel() {
			attrenchantLevel = getAttrEnchantLevel();
		}
		public void updateSpecialEnchant() {
			specialEnchant = getSpecialEnchant();
		}
		public void updatePotential() {
			potential = getPotential();
		}
		public void updateSlots() {
			slots = getSlots();
		}
		public void updateEndTime() {
			endTime = getEndTime();
		}
		public void updateScheduled() {
			isScheduled = isScheduled();
		}
	}

	public LastStatus getLastStatus() {
		return _lastStatus;
	}

	public int getRecordingColumns() {
		int column = 0;
		if (getCount() != _lastStatus.count) {
			column += L1PcInventory.COL_COUNT;
		}
		if (getItemId() != _lastStatus.itemId) {
			column += L1PcInventory.COL_ITEMID;
		}
		if (isEquipped() != _lastStatus.isEquipped) {
			column += L1PcInventory.COL_EQUIPPED;
		}
		if (getEnchantLevel() != _lastStatus.enchantLevel) {
			column += L1PcInventory.COL_ENCHANTLVL;
		}
		if (getDurability() != _lastStatus.durability) {
			column += L1PcInventory.COL_DURABILITY;
		}
		if (getChargeCount() != _lastStatus.chargeCount) {
			column += L1PcInventory.COL_CHARGE_COUNT;
		}
		if (getLastUsed() != _lastStatus.lastUsed) {
			column += L1PcInventory.COL_DELAY_EFFECT;
		}
		if (isIdentified() != _lastStatus.isIdentified) {
			column += L1PcInventory.COL_IS_ID;
		}
		if (getRemainingTime() != _lastStatus.remainingTime) {
			column += L1PcInventory.COL_REMAINING_TIME;
		}
		if (getBless() != _lastStatus.bless) {
			column += L1PcInventory.COL_BLESS;
		}
		if (getAttrEnchantLevel() != _lastStatus.attrenchantLevel) {
			column += L1PcInventory.COL_ATTRENCHANTLVL;
		}
		if (getSpecialEnchant() != _lastStatus.specialEnchant) {
			column += L1PcInventory.COL_SPECIAL_ENCHANT;
		}
		if (getPotential() != _lastStatus.potential) {
			column += L1PcInventory.COL_DOLL_POTENTIAL;
		}
		if (isRecordingSlot()) {
			column += L1PcInventory.COL_SLOTS;
		}
		return column;
	}
	
	/**
	 * 슬롯이 마지막 상태와 비교하여 업데이트가 필요한지 검증
	 * @return boolean
	 */
	boolean isRecordingSlot() {
		if (_slots != _lastStatus.slots) {
			return true;
		}
		if (_slots != null && _lastStatus.slots != null) {
			if (_slots.size() != _lastStatus.slots.size()) {
				return true;
			}
			for (int i=0; i<_slots.size(); i++) {
				if (_slots.get(i) != _lastStatus.slots.get(i)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static final String[] BLESS_STATUS_STRING	= { "$227 ", "$228 " };
	private static final String[] LIGHT_STATUS_STRING	= { " ($10)", " ($11)" };
	private static final String[] EQUIPPED_STRING		= { " ($9)", " ($117)" };

	public String getNumberedViewName(int count) {
		StringBuilder name = new StringBuilder();
		name.append(getNumberedName(count));
		L1ItemType itemType	= _item.getItemType();
		int itemId		= _item.getItemId();
		if (itemId == L1ItemId.PET_AMULET) {
			L1Pet pet = CharacterCompanionTable.getInstance().getTemplate(getId());
		    if (pet != null) {
		    	name.append(" (").append(NpcTable.getInstance().getTemplate(pet.getNpcId()).getDesc()).append(" LV ").append(pet.getLevel()).append(")");
		    }
		}
		if (itemType == L1ItemType.NORMAL && _item.getType() == L1ItemNormalType.LIGHT.getId()) {// light
			if (isNowLighting()) {
				name.append(LIGHT_STATUS_STRING[0]);// 켜짐
			}
			if ((itemId == 40001 || itemId == 40002) && getRemainingTime() <= 0) {
				name.append(LIGHT_STATUS_STRING[1]);// 꺼짐
			}
		}
		// 여관열쇠
		if (itemId == L1ItemId.INN_HALL_KEY)
			name.append(S_SystemMessage.getRefText(1074)).append(getInnTownName());
		if (itemId == L1ItemId.INN_ROOM_KEY) 
			name.append(" " + getInnTownName());
		if (isEquipped()) {
			if (itemType == L1ItemType.WEAPON) {
				name.append(EQUIPPED_STRING[0]);
			} else if (itemType == L1ItemType.ARMOR) {
				name.append(EQUIPPED_STRING[1]);
			} else if (itemType == L1ItemType.NORMAL && _item.getType() == L1ItemNormalType.PET_ITEM.getId()) {
				name.append(EQUIPPED_STRING[1]);// petitem
			}
		}
		return name.toString();
	}
	
	private static final String[] TOWN_NAMES = {
		//"(말하는 섬 마을)", "(글루딘 마을)", "(기란 마을)", "(아덴 마을)", "(오렌 마을)", "(우드벡 마을)", "(은기사 마을)", "(하이네 마을)", "(루운성 마을)"
		"($26617)", "($362)", "($26619)", "($26622)", "($26621)", "($26618)", "($2177)", "($26620)", "($34968)"
		
 	};
	
	String getInnTownName(){
		int key = getKey();
		if ((key >= 16384 && key <= 16684) || (key >= 16896 && key <= 17196)) {
			return TOWN_NAMES[0];
		}
		if ((key >= 17408 && key <= 17708) || (key >= 17920 && key <= 18220)) {
			return TOWN_NAMES[1];
		}
		if ((key >= 18432 && key <= 18732) || (key >= 18944 && key <= 19244)) {
			return TOWN_NAMES[2];
		}
		if ((key >= 19456 && key <= 19756) || (key >= 19968 && key <= 20268)) {
			return TOWN_NAMES[3];
		}
		if ((key >= 23552 && key <= 23852) || (key >= 24064 && key <= 24364)) {
			return TOWN_NAMES[4];
		}
		if ((key >= 20480 && key <= 20780) || (key >= 20992 && key <= 21292)) {
			return TOWN_NAMES[5];
		}
		if ((key >= 21504 && key <= 21804) || (key >= 22016 && key <= 22316)) {
			return TOWN_NAMES[6];
		}
		if ((key >= 22528 && key <= 22828) || (key >= 23040 && key <= 23340)) {
			return TOWN_NAMES[7];
		}
		if ((key >= 24576 && key <= 24876) || (key >= 25088 && key <= 25388)) {
			return TOWN_NAMES[8];
		}
		return StringUtil.EmptyString;
	}

	public String getViewName() {
		return getNumberedViewName(_count);
	}
	public String getLogName() {
		return getLogName(_count);
	}
	public String getLogNameRef() {
		return getLogNameRef(_count);
	}
	
	public String getNumberedName(int count) {
		int itemId			= _item.getItemId();
		L1ItemType itemType	= _item.getItemType();
		boolean identified	= isIdentified();
		StringBuilder name	= new StringBuilder();
		if (identified && (itemType == L1ItemType.WEAPON || itemType == L1ItemType.ARMOR)) {
			if (_attrenchantLevel > 0) {
				name.append(ATTR_DESCRIPTIONS[_attrenchantLevel]);// 속성 표시
			}
			if (_enchantLevel >= 0) {
				name.append(StringUtil.PlusString).append(_enchantLevel).append(StringUtil.EmptyOneString);
			} else if (_enchantLevel < 0) {
				name.append(String.valueOf(_enchantLevel)).append(StringUtil.EmptyOneString);
			}
		}
		
		if (!identified && (itemId == 40015 || itemId == 140015)) {// 마나 회복 물약
			name.append("$232");// 파란 물약
		} else if (!identified && (itemId == 40010 || itemId == 140010 || itemId == 240010)) {// 체력 회복제
			name.append("$237");// 빨간 물약
		} else if (!identified && (itemId == 40011 || itemId == 140011)) {// 고급 체력 회복제
			name.append("$235");// 주홍 물약
		} else if (!identified && (itemId == 40012 || itemId == 140012)) {// 강력 체력 회복제
			name.append("$238");// 맑은 물약
		} else if (!identified && (itemId == 40025)) {// 눈멀기 물약
			name.append("$239");// 불투명 물약
		} else if (!identified && itemId == 40017) {// 해독제
			name.append("$233");// 비취 물약
		} else if (!identified && (itemId == 40006 || itemId == 140006)) {// 이계의 번개소환 막대
			name.append("$28");// 백단 막대
		} else if (!identified && itemId == 40007) {// 번개소환 막대
			name.append("$263");// 흑단 막대
		} else if (!identified && (itemId == 40008 || itemId == 140008)) {// 변신 막대
			name.append("$27");// 단풍나무 막대
		} else if (!identified && (itemId == 20011 || itemId == 120011)) {// 마법 방어 투구
			name.append("$128");// 투구
		} else if (!identified && (itemId == 20110 || itemId == 120110)) {// 마법 방어 사슬 갑옷
			name.append("$159");// 사슬 갑옷
		} else if (!identified && itemId == 202810) {// 변신 지배 반지
			name.append("$25459");// 단풍 반지
		} else if (itemId == CharacterTradeManager.MARBLE_LOAD_ID) {// 케릭터 봉인 구슬
			CharacterTradeObject obj = CharacterTradeLoader.getInstance().get(getId());
			if (obj == null) {
				name.append(_item.getDesc());
			} else {
				name.append("[").append(obj.getCharName()).append("] $44026");
			}
		} else {
			name.append(_item.getDesc());// 아이템 네임
		}
		if (_engrave) {// 각인
			name.append("$27358");
		}
		
		if (identified) {
			if (_item.getMaxChargeCount() > 0 || itemId == 20383) {
				name.append(" (").append(getChargeCount()).append(")");
			}
			if (_item.getMaxUseTime() > 0 && itemType != L1ItemType.NORMAL) {
				name.append(" [").append(getRemainingTime()).append("]");
			}
		}
		if (count > 1 && !((itemId == 40006 || itemId == 140006 || itemId == 40007 || itemId == 40008 || itemId == 140008) && !identified)) {
			name.append(" (").append(count).append(")");
		}
		return name.toString();
	}
	
	public String getLogName(int count) {
		int itemId			= _item.getItemId();
		L1ItemType itemType	= _item.getItemType();
		StringBuilder name	= new StringBuilder();
		if (itemType == L1ItemType.WEAPON || itemType == L1ItemType.ARMOR) {
			if (_attrenchantLevel > 0) {
				name.append(ATTR_DESCRIPTIONS[_attrenchantLevel]);// 속성 표시
			}
			if (_enchantLevel >= 0) {
				name.append(StringUtil.PlusString).append(_enchantLevel).append(StringUtil.EmptyOneString);
			} else if (_enchantLevel < 0) {
				name.append(String.valueOf(_enchantLevel)).append(StringUtil.EmptyOneString);
			}
		}
		
		if (itemId == CharacterTradeManager.MARBLE_LOAD_ID) {// 케릭터 봉인 구슬
			CharacterTradeObject obj = CharacterTradeLoader.getInstance().get(getId());
			if (obj == null) {
				//name.append(_item.getDescKr());
				name.append(_item.getDescEn());
			} else {
				name.append("[").append(obj.getCharName()).append("] ").append(S_SystemMessage.getRefText(1307));
			}
		} else {
			//name.append(_item.getDescKr());// 아이템 네임
			name.append(_item.getDescEn());// 아이템 네임
		}
		if (_engrave) {// 각인
			name.append("$27358"); 
		}
		
		if (_item.getMaxChargeCount() > 0 || itemId == 20383) {
			name.append(" (").append(getChargeCount()).append(")");
		}
		if (_item.getMaxUseTime() > 0 && itemType != L1ItemType.NORMAL) {
			name.append(" [").append(getRemainingTime()).append("]");
		}
		
		if (count > 1 && !((itemId == 40006 || itemId == 140006 || itemId == 40007 || itemId == 40008 || itemId == 140008))) {
			name.append(" (").append(count).append(")");
		}
		return name.toString();
	}

	public String getLogNameRef(int count) {
		int itemId			= _item.getItemId();
		L1ItemType itemType	= _item.getItemType();
		StringBuilder name	= new StringBuilder();
		if (itemType == L1ItemType.WEAPON || itemType == L1ItemType.ARMOR) {
			if (_attrenchantLevel > 0) {
				name.append(ATTR_DESCRIPTIONS[_attrenchantLevel]);// 속성 표시
			}
			if (_enchantLevel >= 0) {
				name.append(StringUtil.PlusString).append(_enchantLevel).append(StringUtil.EmptyOneString);
			} else if (_enchantLevel < 0) {
				name.append(String.valueOf(_enchantLevel)).append(StringUtil.EmptyOneString);
			}
		}
		
		if (itemId == CharacterTradeManager.MARBLE_LOAD_ID) {// 케릭터 봉인 구슬
			CharacterTradeObject obj = CharacterTradeLoader.getInstance().get(getId());
			if (obj == null) {
				//name.append(_item.getDescKr());
				name.append(_item.getDescEn());
			} else {
				name.append("[").append(obj.getCharName()).append("] ").append(S_SystemMessage.getRefText(1307));
			}
		} else {
			//name.append(_item.getDescKr());// 아이템 네임
			name.append(_item.getDescEn());// 아이템 네임
		}
		if (_engrave) {// 각인
			name.append("$27358");
		}
		
		if (_item.getMaxChargeCount() > 0 || itemId == 20383) {
			name.append(" (").append(getChargeCount()).append(")");
		}
		if (_item.getMaxUseTime() > 0 && itemType != L1ItemType.NORMAL) {
			name.append(" [").append(getRemainingTime()).append("]");
		}
		
		if (count > 1 && !((itemId == 40006 || itemId == 140006 || itemId == 40007 || itemId == 40008 || itemId == 140008))) {
			name.append(" (").append(count).append(")");
		}
		return name.toString();
	}
	
	private static final String FRONT_COLOR_STRING			= "\\fI";
	private static final String FRONT_FAIL_COLOR_STRING		= "\\f3";
	private static final String BACK_COLOR_STRING			= "\\f>";
	private static final String USE_STRING					= FRONT_COLOR_STRING + S_SystemMessage.getRefText(1347);
	private static final String TRADE_STRING				= FRONT_COLOR_STRING + S_SystemMessage.getRefText(1348);
	private static final String BUY_COUNT_STRING			= FRONT_COLOR_STRING + S_SystemMessage.getRefText(1349);
	private static final String BUY_WEEK_COUNT_STRING		= FRONT_COLOR_STRING + S_SystemMessage.getRefText(1350) + BACK_COLOR_STRING;
	private static final String BUY_DAY_COUNT_STRING		= FRONT_COLOR_STRING + S_SystemMessage.getRefText(1351) + BACK_COLOR_STRING;
	private static final String BUY_ACCOUNT_COUNT_STRING	= FRONT_COLOR_STRING + S_SystemMessage.getRefText(1253) + BACK_COLOR_STRING;
	private static final String BUY_WEEK_FAIL_STRING		= FRONT_FAIL_COLOR_STRING + S_SystemMessage.getRefText(1254);
	private static final String BUY_DAY_FAIL_STRING			= FRONT_FAIL_COLOR_STRING + S_SystemMessage.getRefText(1255);
	private static final String BUY_FAIL_STRING				= FRONT_FAIL_COLOR_STRING + S_SystemMessage.getRefText(1148);
	private static final String BUY_ACCOUNT_FAIL_STRING		= FRONT_FAIL_COLOR_STRING + S_SystemMessage.getRefText(1136);
	private static final String CREATOR_STRING				= S_SystemMessage.getRefText(1089) + "%s";
	
	int getClassPermit(){
		int bit = 0;
		bit |= _item.isUseRoyal() ? 1 : 0;
		bit |= _item.isUseKnight() ? 2 : 0;
		bit |= _item.isUseElf() ? 4 : 0;
		bit |= _item.isUseMage() ? 8 : 0;
		bit |= _item.isUseDarkelf() ? 16 : 0;
		bit |= _item.isUseDragonKnight() ? 32 : 0;
		bit |= _item.isUseIllusionist() ? 64 : 0;
		bit |= _item.isUseWarrior() ? 128 : 0;
		bit |= _item.isUseFencer() ? 256 : 0;
		bit |= _item.isUseLancer() ? 512 : 0;
	    return bit;
	}
	
	int getClassPermitFromGameClass(){
		switch(_owner.getType()){
		case 0:return 1;
		case 1:return 2;
		case 2:return 4;
		case 3:return 8;
		case 4:return 16;
		case 5:return 32;
		case 6:return 64;
		case 7:return 128;
		case 8:return 256;
		case 9:return 512;
		default:return 0;
		}
	}
	
	/**
	 * 아이템 상태로부터 서버 패킷으로 이용하는 형식의 바이트 배열을 생성해, 돌려준다. 
	 * 이미 생성되어 있으면 재사용 처리한다.
	 * @return byte[]
	 */
	public byte[] getStatusBytes(L1PcInstance pc) {
		if (extraDescription == null) {
			updateItemAbility(pc);
		}
		return extraDescription;
	}

	/**
	 * 아이템 상태로부터 서버 패킷으로 이용하는 형식의 바이트 배열을 생성해, 돌려준다. 
	 * @return byte[]
	 */
	byte[] getExtraDescription() {
		// TODO 표기 패킷
		L1ItemType itemType						= _item.getItemType();
		int grade								= _item.getGrade();
		int retrieveEnchant						= _item.getRetrieveEnchantLevel();
		boolean isSlot							= isSlot();
		BinaryOutputStreamExtraDescription os	= null;
		try {
			os	= new BinaryOutputStreamExtraDescription();
			int myth_weapon_enchant_hit_bonus = 0;
			if (itemType == L1ItemType.NORMAL) {// etcitem
				L1ItemNormalType normalType	= L1ItemNormalType.fromInt(_item.getType());
				
				if (_potential != null) {
					os.writePotential(_potential.getInfo());// 마법인형 잠재력
				}
				int class_permit = getClassPermit();
				if (class_permit != 0) {
					os.writeClassPermit(class_permit);
				}
				if (_item.getAlignment() != null) {// 성향
					os.writeAlignment(_item.getAlignment().toInt());
				}
				if (_item.getSkillLevel() != 0) {// 스킬북 단계 표기
					os.writeGrade(_item.getSkillLevel() - 1);
				}
				if (_item.getSkillAttr() != null) {// 정령 수정 속성 표기
					os.writeSkillAttr(_item.getSkillAttr().toInt());
				}
				if (normalType == L1ItemNormalType.SPELL_BOOK && _item.getMinLevel() != 0) {// 사용레벨 표기	
					os.writeUseLevel(_item.getMinLevel());
				}
				
				switch(normalType){
				case LIGHT:
					os.writeLightRange(_item.getLightRange());
					break;
				case FOOD:
					os.writeFoodVolume(_item.getFoodVolume());
					break;
				case ARROW:case STING:
					writeArrowOrStingBytes(os);
					return os.getBytes();
				case EVENT:
					os.writeEtcEvent();
					break;
				default:
					os.writeEtcDefault();
					break;
				}
				
				os.writeMaterial(_item.getMaterial());
				os.writeWeight(getWeight());
				
				switch (_itemId) {
				case 820014:os.writeAttrDamage(8);break;// 속성 화살 물3
				case 820015:os.writeAttrDamage(13);break;// 속성 화살 바람3
				case 820016:os.writeAttrDamage(18);break;// 속성 화살 땅3
				case 820017:os.writeAttrDamage(3);break;// 속성 화살 불3
				case 1000024:case 1000025:case 1000026:case 1000027:case 1000028:case 1000029:case 7777:os.writeString(USE_STRING + BACK_COLOR_STRING + S_SystemMessage.getRefText(1086));break;
				case 7700:case 7701:case 7702:case 7703:case 7704:case 7705:case 7706:os.writeString(TRADE_STRING + BACK_COLOR_STRING + S_SystemMessage.getRefText(1087));break;//아데나 교환권
				case 40000:os.writeString(USE_STRING + BACK_COLOR_STRING + S_SystemMessage.getRefText(1083));break;// 영웅의 코인(후원 코인)
				case 700023:case 700024:case 700025:
					if (!StringUtil.isNullOrEmpty(_createrName)) {
						os.writeString(String.format(CREATOR_STRING, _createrName));
					}
					break;
				case 30208:os.writeLevelStore();break;// 레벨별 차등 지급
				default:break;
				}
				
				if (_itemId == L1ItemId.TREASURE_SHOVEL) {// 보물 탐지 삽 제한: 지역
					os.writeLimit(0, 356);
				} else if (_item.getMinLevel() != 0 || _item.getMaxLevel() != 0) {// 레벨 이상/이하 표기
					os.writeLimit(_item.getMinLevel(), _item.getMaxLevel() == 0 ? 100 : _item.getMaxLevel());
				}
			} else if (itemType == L1ItemType.WEAPON || itemType == L1ItemType.ARMOR) {// weapon | armor
				L1ItemArmorType armorType	= L1ItemArmorType.fromInt(_item.getType());
				L1Grade itemGrade		= _item.getItemGrade();
				int characteristicFlag		= -1;
				switch(itemType){
				case WEAPON:
					os.writeWeaponDefaultDamage(_item.getDmgSmall(), _item.getDmgLarge());
					os.writeMaterial(_item.getMaterial());
					os.writeWeight(getWeight());
					
					if (_enchantLevel != 0) {
						int value = 0;
						if (itemGrade == L1Grade.LEGEND || _itemId == 203066) {
							value = (_itemId != 203066 && _enchantLevel >= 6 ? (_enchantLevel << 1) + _enchantLevel : _enchantLevel << 1) + (_enchantLevel >= 10 ? _enchantLevel - 9 : 0);
						} else if (itemGrade == L1Grade.MYTH) {
							value = ((_enchantLevel << 2) + _enchantLevel) + (_enchantLevel >= 10 ? _enchantLevel - 9 : 0);
							// 신화 등급 무기 인첸트에 대한 명중 보너스
							if (_enchantLevel > 0) {
								myth_weapon_enchant_hit_bonus = _enchantLevel + (_enchantLevel >> 1);
							}
						} else {
							value = _enchantLevel + (_enchantLevel >= 10 ? _enchantLevel - 9 : 0);
						}
						os.writeEnchatValue(_enchantLevel, value);
					}
					
					
					break;
				case ARMOR:
					int ac = -((L1Armor) _item).getAc();
					os.writeArmorDefaultAc(ac - _durability);
					os.writeMaterial(_item.getMaterial());
					
					if (armorType == L1ItemArmorType.AMULET) {
						characteristicFlag = 128;
					} else if (armorType == L1ItemArmorType.EARRING && grade != 4) {
						characteristicFlag = 43;
					} else if (L1ItemArmorType.isRing(armorType.getId()) && grade != 3) {
						characteristicFlag = 44;
					} else if (armorType == L1ItemArmorType.BELT) {
						characteristicFlag = 45;
					} else if (armorType == L1ItemArmorType.PENDANT) {
						characteristicFlag = 134;
					}
					os.writeCharacteristic(characteristicFlag, true);
					os.writeWeight(getWeight());
					
					if (_enchantLevel != 0 && grade < 0) {
						os.writeEnchatValue(_enchantLevel, _enchantLevel);
					} else if (acSub != 0){// AC 추가 설정
						os.writeAcSub(-acSub);
					}
					break;
				default:
					break;
				}
				
				if (_item.isTwohandedWeapon()) {
					os.writeTwohandedWeapon();// 양손무기
				}
				if (_durability != 0) {
					os.writeDurability(_durability);// 손상도
				}

				// 클래스 표기
				if (_itemId >= 222295 && _itemId <= 222299 || _itemId >= 222312 && _itemId <= 222316 || _itemId >= 600051 && _itemId <= 600094 || _itemId >= 600700 && _itemId <= 600720) {
					os.writeClassPermit(getClassPermitFromGameClass());
				} else {
					os.writeClassPermit(getClassPermit());
				}
				
				if (_item.getMinLevel() != 0 || _item.getMaxLevel() != 0) {// 레벨 이상/이하 표기
					os.writeLimit(_item.getMinLevel(), _item.getMaxLevel() == 0 ? 100 : _item.getMaxLevel());
				}
				
				// 슬롯 표기
				if (isSlot) {
					os.writeSlot(_slots);
				}
				
				// 안전 인첸트 표기
				int safeEnchant = _item.getSafeEnchant();
				if (safeEnchant >= 0 && !isSlot) {
					os.writeSafeEnchant(safeEnchant);
				}
				
				// 악세서리 특성
				if (characteristicFlag != -1) {
					os.writeCharacteristic(characteristicFlag, false);
				}
			}
			
			if (str != 0) {
				os.writeStr(str);// 힘 스탯
			}
			if (dex != 0) {
				os.writeDex(dex);// 덱스 스탯
			}
			if (con != 0) {
				os.writeCon(con);// 콘 스탯
			}
			if (wis != 0) {
				os.writeWis(wis);// 위즈 스탯
			}
			if (inti != 0) {
				os.writeInt(inti);// 인트 스탯
			}
			if (cha != 0) {
				os.writeCha(cha);// 카리스마 스탯
			}
			if (maxHp != 0) {
				os.writeMaxHp(maxHp);// 최대 HP증가
			}
			if (magicRegist != 0) {
				os.writeMagicRegist(magicRegist);// 마방
			}
			if (mpDrain != 0) {
				os.writeMpDrain();// 마나 흡수
			}
			if (spellpower != 0) {
				os.writeSpellPower(spellpower);// sp
			}
			if (longHit != 0) {
				os.writeLongHit(longHit + myth_weapon_enchant_hit_bonus);// 원거리명중
			}
			if (attrFire != 0) {
				os.writeAttrFire(attrFire);// 불 속성 저항
			}
			if (attrWater != 0) {
				os.writeAttrWater(attrWater);// 물 속성 저항
			}
			if (attrWind != 0) {
				os.writeAttrWind(attrWind);// 바람 속성 저항
			}
			if (attrEarth != 0) {
				os.writeAttrEarth(attrEarth);// 땅 속성 저항
			}
			if (maxMp != 0) {
				os.writeMaxMp(maxMp);// 최대 MP증가
			}
			if (registFreeze != 0) {
				os.writeRegistFreeze(registFreeze);// 동빙 내성
			}
			if (registStone != 0) {
				os.writeRegistStone(registStone);// 석화 내성
			}
			if (registSleep != 0) {
				os.writeRegistSleep(registSleep);// 수면 내성
			}
			if (registBlind != 0) {
				os.writeRegistBlind(registBlind);// 암흑 내성
			}
			if (hpDrain != 0) {
				os.writeHpDrain();// 피 흡수
			}
			if (longDamage != 0) {
				os.writeLongDamage(longDamage);// 원거리대미지
			}
			if (expBonus != 0) {
				os.writeExpBonus(expBonus);// 경험치 보너스
			}
			if (hpRegen != 0) {
				os.writeHpRegen(hpRegen);// HP획복량
			}
			if (mpRegen != 0) {
				os.writeMpRegen(mpRegen);// MP회복량
			}
			if (magicHit != 0) {
				os.writeMagicHit(magicHit);// 마법 적중
			}
			if (shortDamage != 0) {
				os.writeShortDamage(shortDamage);// 근거리대미지
			}
			if (shortHit != 0) {
				os.writeShortHit(shortHit + myth_weapon_enchant_hit_bonus);// 근거리명중
			}
			if (magicCritical != 0) {
				os.writeMagicCritical(magicCritical);// 마법 치명타
			}
			if (evasion != 0) {
				os.writeEvasion(evasion);// 회피 DG
			}
			if (acBonus != 0) {
				os.writeAcBonus(-acBonus);// 추가 방어력
			}
			if (PVPDamage != 0) {
				os.writePVPDamage(PVPDamage);// PVP 추가 데미지
			}
			if (PVPDamageReduction != 0) {
				os.writePVPDamageReduction(PVPDamageReduction);// PVP 데미지 감소
			}
			if (damageReduction != 0) {
				os.writeDamageReduction(damageReduction);// 대미지 감소
			}
			if (damageReductionChance[0] != 0) {
				os.writeDamageReductionChance(damageReductionChance);// 확률적 대미지리감소 표기
			}
			if (potionRegist > 0) {
				os.writePotionRegist(potionRegist);// 회복 악화 방어
			}
			if (potionPercent > 0 || potionValue > 0) {
				os.writePotionPercentAndValue(potionPercent, potionValue);// 물약 회복량
			}
			if (poisonRegist != 0) {
				os.writePoisonRegist(poisonRegist);// 독 내성 표기
			}
			if (_item.getSetId() == 0 && polyDescId > 0) {
				os.writePolyDesc(polyDescId);// 변신
			}
			
			// 발동
			if (!StringUtil.isNullOrEmpty(magicName)) {
				if (magicName.contains(StringUtil.ColonString)) {
					String[] array = magicName.split(StringUtil.ColonString);
					for (String name : array) {
						os.writeMagicName(name);
					}
				} else {
					os.writeMagicName(magicName);
				}
			}
			
			if (hprAbsol32Second != 0) {
				os.writeHpAbsolRegen32Second(hprAbsol32Second);// Hp절대 회복 +%d(32초)
			}
			if (mprAbsol64Second != 0) {
				os.writeMpAbsolRegen64Second(mprAbsol64Second);// Mp절대 회복 +%d(64초)
			}
			if (mprAbsol16Second != 0) {
				os.writeMpAbsolRegen16Second(mprAbsol16Second);// Mp절대 회복 +%d(16초)
			}
			if (magicEvasion != 0) {
				os.writeMagicEvasion(magicEvasion);// 확률적 마법 회피
			}
			if (carryBonus != 0) {
				os.writeCarryBonus(carryBonus);// 무게 게이지
			}
			if (evasionRegist != 0) {
				os.writeEvasionRegist(evasionRegist);// 회피 ER
			}
			if (damageChance[0] != 0) {
				os.writeDamageChance(damageChance);// 확률적 근거리 대미지
			}
			if (damageReductionEgnor != 0) {
				os.writeDamageReductionEgnor(damageReductionEgnor);// 대미지리덕션 무시
			}
			if (longCritical != 0) {
				os.writeLongCritical(longCritical);// 원거리 치명타
			}
			if (shortCritical != 0) {
				os.writeShortCritical(shortCritical);// 근거리 치명타
			}
			if (fowslayerDamage != 0) {
				os.writeFowSlayerDamage(fowslayerDamage);// 포우대미지
			}
			if (titanUp != 0) {
				os.writeTitanUp(titanUp);// 타이탄 계열 발동 구간
			}
			if (damageChanceEtc != 0) {
				os.writeDamageChanceEtc(damageChanceEtc);// 확률적 근거리대미지
			}
			if (itemType == L1ItemType.WEAPON && _attrenchantLevel != 0) {
				os.writeAttrDamage(getAttrEnchantBit(_attrenchantLevel));// 속성대미지
			}
			if (undead) {
				os.writeUndead(1);// 언데드
			}
			if (demon) {
				os.writeDemon(1);// 데몬
			}
			if (restExpReduceEfficiency != 0) {
				os.writeRestExpReduceEfficiency(restExpReduceEfficiency);// 축복 소모율
			}
			if (toleranceSkill != 0) {
				os.writeToleranceSkill(toleranceSkill);// 기술 내성
			}
			if (toleranceSpirit != 0) {
				os.writeToleranceSpirit(toleranceSpirit);// 정령 내성
			}
			if (toleranceDragon != 0) {
				os.writeToleranceDragon(toleranceDragon);// 용언 내성
			}
			if (toleranceFear != 0) {
				os.writeToleranceFear(toleranceFear);// 공포 내성
			}
			if (toleranceAll != 0) {
				os.writeToleranceAll(toleranceAll);// 전체 내성
			}
			if (hitupSkill != 0) {
				os.writeHitupSkill(hitupSkill);// 기술 적중
			}
			if (hitupSpirit != 0) {
				os.writeHitupSpirit(hitupSpirit);// 정령 적중
			}
			if (hitupDragon != 0) {
				os.writeHitupDragon(hitupDragon);// 용언 적중
			}
			if (hitupFear != 0) {
				os.writeHitupFear(hitupFear);// 공포 적중
			}
			if (hitupAll != 0) {
				os.writeHitupAll(hitupAll);// 전체 적중
			}
			if (itemType == L1ItemType.WEAPON && _item.getCanbeDmg() != 1) {
				os.writeCanbeDamage();// 비손상
			}
			if (PVPMagicDamageReduction != 0) {
				os.writePVPMagicDamageReduction(PVPMagicDamageReduction);// PVP 마법 데미지 감소
			}
			if (PVPDamageReductionEgnor != 0) {
				os.writePVPDamageReductionEgnor(PVPDamageReductionEgnor);// PVP 대미지 감소 무시
			}
			if (PVPMagicDamageReductionEgnor != 0) {
				os.writePVPMagicDamageReductionEgnor(PVPMagicDamageReductionEgnor);// PVP 마법 대미지 감소 무시
			}
			if (buffDurationSecond != 0) {
				os.writeBuffDurationSecond(buffDurationSecond);// 지속시간
			}
			if (addExpPercent != 0) {
				os.writeAddExpPercent(addExpPercent);// 경험치추가:%d
			}
			if (addExpPercentPCCafe != 0) {
				os.writeAddExpPercentPCCafe(addExpPercentPCCafe);// PC방 경험치 추가:%d
			}
			if (foodType != -1) {
				os.writeFoodType(foodType);// 음식 종류
			}
			if (baseHpRate != 0) {
				os.writeBaseHpRate(baseHpRate);// 최대 HP%d
			}
			if (baseMpRate != 0) {
				os.writeBaseMpRate(baseMpRate);// 최대 MP%d
			}
			if (dragonDamageReduction != 0) {
				os.writeDragonDamageReduction(dragonDamageReduction);// 드래곤 피해감소
			}
			if (blessExp != 0) {
				os.writeBlessExp(blessExp);// 축복경험치
			}
			if (attrAll != 0) {
				os.writeAttrAll(attrAll);// 속성 저항 전체
			}
			if (thirdSpeed != 0) {
				os.writeThirdSpeed();// 3단 가속
			}
			if (fourthSpeed != 0) {
				os.writeFourthSpeed();// 4단 가속
			}
			if (magicDamageReduction != 0) {
				os.writeMagicDamageReduction(magicDamageReduction);// 마법 대미지 감소
			}
			if (emunEgnor != 0) {
				os.writeEmunEgnor(emunEgnor);// 이뮨 무시
			}
			if (stunDuration != 0) {
				os.writeStunDuration(stunDuration);// 스턴 지속 시간
			}
			if (dranium) {
				os.writeDranium(1);// 드라니움
			}
			if (damageReductionPercent != 0) {
				os.writeDamageReductionPercent(damageReductionPercent);// 대미지 감소 퍼센트
			}
			if (PVPDamageReductionPercent != 0) {
				os.writePVPDamageReductionPercent(PVPDamageReductionPercent);// PVP 데미지 감소 퍼센트
			}
			if (magicDamage != 0) {
				os.writeMagicDamage(magicDamage);// 마법 대미지
			}
			/*if (vanguardTime != 0) {
				os.writeVanguardTime(vanguardTime);// 뱅가드시간 감소
			}*/
			if (magicCriticalDamageAdd != 0) {
				os.writeMagicCriticalDamageAdd(magicCriticalDamageAdd);// 마법 치명타 대미지 상승
			}
			if (reflectEmasculate != 0) {
				os.writeReflectEmasculate(reflectEmasculate);// 반격, 회피 무시 타격
			}
			if (abnormalStatusDamageReduction != 0) {
				os.writeAbnormalStatus(1316864);// 스턴, 홀드, 귀환불가, 텔레포트불가
				os.writeAbnormalStatusDamageReduction(abnormalStatusDamageReduction);// 상태이상 시 대미지 감소
			}
			if (abnormalStatusPVPDamageReduction != 0) {
				os.writeAbnormalStatus(1316864);// 스턴, 홀드, 귀환불가, 텔레포트불가
				os.writeAbnormalStatusPVPDamageReduction(abnormalStatusPVPDamageReduction);// 상태이상 시 PvP 대미지 감소
			}
			if (PVPDamagePercent != 0) {
				os.writePVPDamagePercent(PVPDamagePercent);// PVP 대미지 증가 %
			}
			if (strangeTimeIncrease != 0) {
				os.writeStrangeTimeIncrease(strangeTimeIncrease);// 상태 이상 시간 증가
			}
			if (strangeTimeDecrease != 0) {
				os.writeStrangeTimeDecrease(strangeTimeDecrease);// 상태 이상 시간 감소
			}
			if (hpPotionDelayDecrease != 0) {
				os.writeHpPotionDelayDecrease(hpPotionDelayDecrease);// HP 포션 딜레이 감소 +%d
			}
			if (hpPotionCriticalProb != 0) {
				os.writeHpPotionCriticalProb(hpPotionCriticalProb);// HP 포션 크리티컬 확률 +%d
			}
			if (increaseArmorSkillProb != 0) {
				os.writeIncreaseArmorSkillProb(increaseArmorSkillProb);// 갑옷 마법 발동 확률
			}
			if (returnLockDuraion != 0) {
				os.writeReturnLockDuraion(returnLockDuraion);// 귀환 불가 지속 시간
			}
			if (attackSpeedDelayRate != 0) {
				os.writeAttackSpeedDelayRate(attackSpeedDelayRate);// 공격 속도 증가
			}
			if (moveSpeedDelayRate != 0) {
				os.writeMoveSpeedDelayRate(moveSpeedDelayRate);// 이동 속도 증가
			}
			if (_endTime != null) {// 제한 시간 or 자동 삭제
				//os.writeLimitTime((int)(_endTime.getTime() / 1000L));// 제한 시간
				os.writeDeleteTime(((int)((_endTime.getTime() - 1483196400065L) / 1000L)) * 6);// 자동 삭제
			}
			if (getIconId() == 9659) {// 아인하사드의 가호
				os.writeScheduled(_scheduled);// 예약 여부
			}
			
			if (retrieveEnchant > 0) {
				if (_enchantLevel < retrieveEnchant) {
					os.writeRetrieveEnchant(retrieveEnchant);
				} else {
					os.writeRetrieve(3);
					os.writeRetrieveEnchant(retrieveEnchant);
					os.writeRetrieveEnchantOver(3);
				}
			} else if (isSlot) {
				os.writeRetrieveEnchantOver(3);
			} else if (_item.isRetrieve()) {
				os.writeRetrieve(1);
			} else {
				os.writeRetrieve(2);
			}
			return os.getBytes();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
					os = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	void writeArmorSetBytes(BinaryOutputStreamExtraDescription os, L1ArmorSets set) throws Exception {
		if (set != null && set.getAc() != 0) {
			os.writeAcBonus(-set.getAc());
		}
		if (polyDescId > 0) {
			os.writePolyDesc(polyDescId);
		}
		
		// 세트 옵션 표기
		if (set != null) {
			if (set.getShortHitup() != 0)
				os.writeShortHit(set.getShortHitup());
			if (set.getShortDmgup() != 0)
				os.writeShortDamage(set.getShortDmgup());
			if (set.getShortCritical() != 0)
				os.writeShortCritical(set.getShortCritical());
			if (set.getLongHitup() != 0)
				os.writeLongHit(set.getLongHitup());
			if (set.getLongDmgup() != 0)
				os.writeLongDamage(set.getLongDmgup());
			if (set.getLongCritical() != 0)
				os.writeLongCritical(set.getLongCritical());
			if (set.getHpr() != 0)
				os.writeHpRegen(set.getHpr());
			if (set.getMpr() != 0)
				os.writeMpRegen(set.getMpr());
			if (set.getHp() != 0)
				os.writeMaxHp(set.getHp());
			if (set.getMp() != 0)
				os.writeMaxMp(set.getMp());
			if (set.getMr() != 0)
				os.writeMagicRegist(set.getMr());
			if (set.getSp() != 0)
				os.writeSpellPower(set.getSp());
			if (set.getMagicHitup() != 0)
				os.writeMagicHit(set.getMagicHitup());
			if (set.getMagicCritical() != 0)
				os.writeMagicCritical(set.getMagicCritical());
			if (set.getFire() != 0)
				os.writeAttrFire(set.getFire());
			if (set.getWater() != 0)
				os.writeAttrWater(set.getWater());
			if (set.getWind() != 0)
				os.writeAttrWind(set.getWind());
			if (set.getEarth() != 0)
				os.writeAttrEarth(set.getEarth());
			if (set.getStr() != 0)
				os.writeStr(set.getStr());
			if (set.getDex() != 0)
				os.writeDex(set.getDex());
			if (set.getCon() != 0)
				os.writeCon(set.getCon());
			if (set.getWis() != 0)
				os.writeWis(set.getWis());
			if (set.getIntl() != 0)
				os.writeInt(set.getIntl());
			if (set.getCha() != 0)
				os.writeCha(set.getCha());
			if (set.getReduc() != 0)
				os.writeDamageReduction(set.getReduc());
			if (set.getReducEgnor() != 0)
				os.writeDamageReductionEgnor(set.getReducEgnor());
			if (set.getMagicReduc() != 0)
				os.writeMagicDamageReduction(set.getMagicReduc());
			if (set.getPVPDamage() != 0)
				os.writePVPDamage(set.getPVPDamage());
			if (set.getPVPReduc() != 0)
				os.writePVPDamageReduction(set.getPVPReduc());
			if (set.getPVPMagicReduc() != 0)
				os.writePVPMagicDamageReduction(set.getPVPMagicReduc());
			if (set.getPVPReducEgnor() != 0)
				os.writePVPDamageReductionEgnor(set.getPVPReducEgnor());
			if (set.getPVPMagicReducEgnor() != 0)
				os.writePVPMagicDamageReductionEgnor(set.getPVPMagicReducEgnor());
			if (set.getExpBonus() != 0)
				os.writeExpBonus(set.getExpBonus());
			if (set.getRestExpReduceEfficiency() != 0)
				os.writeRestExpReduceEfficiency(set.getRestExpReduceEfficiency());
			if (set.getDg() != 0)
				os.writeEvasion(set.getDg());
			if (set.getEr() != 0)
				os.writeEvasionRegist(set.getEr());
			if (set.getMe() != 0)
				os.writeMagicEvasion(set.getMe());
			if (set.getToleranceSkill() != 0)
				os.writeToleranceSkill(set.getToleranceSkill());
			if (set.getToleranceSpirit() != 0)
				os.writeToleranceSpirit(set.getToleranceSpirit());
			if (set.getToleranceDragon() != 0)
				os.writeToleranceDragon(set.getToleranceDragon());
			if (set.getToleranceFear() != 0)
				os.writeToleranceFear(set.getToleranceFear());
			if (set.getToleranceAll() != 0)
				os.writeToleranceAll(set.getToleranceAll());
			if (set.getHitupSkill() != 0)
				os.writeHitupSkill(set.getHitupSkill());
			if (set.getHitupSpirit() != 0)
				os.writeHitupSpirit(set.getHitupSpirit());
			if (set.getHitupDragon() != 0)
				os.writeHitupDragon(set.getHitupDragon());
			if (set.getHitupFear() != 0)
				os.writeHitupFear(set.getHitupFear());
			if (set.getHitupAll() != 0)
				os.writeHitupAll(set.getHitupAll());
			if (set.getAbnormalStatusDamageReduction() != 0) {
				os.writeAbnormalStatus(1316864);
				os.writeAbnormalStatusDamageReduction(set.getAbnormalStatusDamageReduction());
			}
			if (set.getAbnormalStatusPVPDamageReduction() != 0) {
				os.writeAbnormalStatus(1316864);
				os.writeAbnormalStatusPVPDamageReduction(set.getAbnormalStatusPVPDamageReduction());
			}
			if (set.getPVPDamagePercent() != 0) {
				os.writePVPDamagePercent(set.getPVPDamagePercent());
			}
		}
		os.writeC(0x45);
		os.writeC(0);
	}
	
	public byte[] getStatusBytes(L1PcInstance pc, boolean isSet) {
		BinaryOutputStreamExtraDescription os = null;
		try {
			os = new BinaryOutputStreamExtraDescription();
			os.write(getStatusBytes(pc));
			os.writeC(0x45);
			os.writeC(isSet ? 1 : 2);
			writeArmorSetBytes(os, ArmorSetTable.getInstance().getArmorSets(_item.getSetId()));
			
			if (_item.getItemType() == L1ItemType.ARMOR) {
				if (!(_item.getType() >= L1ItemArmorType.AMULET.getId() && _item.getType() <= L1ItemArmorType.EARRING.getId())) {
				    os.writeC(0);
				    os.writeC(-1);
				}
			} else {
				if (_item.getItemId() != L1ItemId.PET_AMULET) {
				    os.writeC(0);
				    os.writeC(0);
				}
			}
			return os.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if (os != null) {
				try {
					os.close();
					os = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public byte[] getStatusBytesSub(L1PcInstance pc) {
		BinaryOutputStreamExtraDescription os = null;
		try {
			os = new BinaryOutputStreamExtraDescription();
			os.write(getStatusBytes(pc));
			L1ArmorSets set = ArmorSetTable.getInstance().getArmorSets(_item.getSetId());
			int itemId = _item.getItemId();
			if (set != null && _item.getMainId() == itemId) {
				os.writeC(0x45);
				os.writeC(2);
				writeArmorSetBytes(os, set);
			}
			if (_item.getItemType() == L1ItemType.ARMOR) {
				if (!(_item.getType() >= L1ItemArmorType.AMULET.getId() && _item.getType() <= L1ItemArmorType.EARRING.getId())) {
				    os.writeC(0);
				    os.writeC(-1);
				}
			} else {
				if (itemId != L1ItemId.PET_AMULET) {
				    os.writeC(0);
				    os.writeC(0);
				}
			}
			return os.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if (os != null) {
				try {
					os.close();
					os = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	void writeArrowOrStingBytes(BinaryOutputStreamExtraDescription os){
		int damage			= _item.getBowDmgRate();
		int hit				= _item.getBowHitRate();
		Material material	= _item.getMaterial();
		os.writeEtcDefault();
		os.writeMaterial(material);
		os.writeWeight(getWeight());
		if (damage > 0) {
			os.writeLongDamage(damage);
		}
		if (hit > 0) {
			os.writeLongHit(hit);
		}
		if (_itemId == 31174 || _itemId == 130040) {
			os.writeAttrDamageSimple(3);
		}
		if (Material.isUndeadMaterial(material)) {
			os.writeUndead(1);
		}
		if (_item.getMaxLevel() > 0) {
			os.writeLimit(_item.getMinLevel(), _item.getMaxLevel());
		}
		os.writeRetrieve(1);
	}
	
	public byte[] getStatusBytesShopItem(L1PcInstance pc, int npcId) {
		BinaryOutputStreamExtraDescription os = null;
		try {
			os = new BinaryOutputStreamExtraDescription();
			os.write(getStatusBytes(pc));// 기본 패킷 세팅
			
			switch(_itemId){
			case 5994:// 멩세의 열쇠(1일) 1일 제한
				os.writePledgeBuyLimitDay(1, CommonUtil.isDayResetTimeCheck(pc.getClan().getClanDayDungeonTime()) ? 0 : 1);
				break;
			case 5995:case 5997:// 멩세의 열쇠(주간) 1주 제한
				os.writePledgeBuyLimitWeek(1, CommonUtil.isWeekResetCheck(pc.getClan().getClanWeekDungeonTime()) ? 0 : 1);
				break;
			case 5996:// 멩세의 영약(1일) 1일 3개 제한
				os.writePledgeBuyLimitDay(3, pc.getClan().getClanVowPotionCount());
				break;
			default:
				ShopLimitInformation limit = ShopLimitLoader.getShopLimit(npcId, _itemId);
				if (limit != null) {
					writeShopLimit(os, limit, pc, npcId);
				}
				break;
			}
			return os.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if (os != null) {
				try {
					os.close();
					os = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	/**
	 * 구매 가능 개수 표기
	 * @param os
	 * @param limit
	 * @param pc
	 * @param npcId
	 */
	void writeShopLimit(BinaryOutputStreamExtraDescription os, ShopLimitInformation limit, L1PcInstance pc, int npcId) {
		ShopLimitUser limitUser		= null;
		ShopLimitObject limitObj	= null;
		switch (limit.getLimitType()) {
		case ACCOUNT:
			limitUser = pc.getAccount().getShopLimit();
			if (limitUser != null) {
				limitObj	= limitUser.getLimit(npcId, _itemId);
			}
			writeShopLimitFromAccount(os, pc, limit, limitObj);
			break;
		case CHARACTER:
			limitUser = pc.getShopLimit();
			if (limitUser != null) {
				limitObj	= limitUser.getLimit(npcId, _itemId);
			}
			writeShopLimitFromCharacter(os, pc, limit, limitObj);
			break;
		}
	}
	
	/**
	 * 구매 가능 개수 표기(계정)
	 * @param os
	 * @param pc
	 * @param limit
	 * @param limitObj
	 */
	void writeShopLimitFromAccount(BinaryOutputStreamExtraDescription os, L1PcInstance pc, ShopLimitInformation limit, ShopLimitObject limitObj) {
		int current			= limitObj != null ? limitObj.getBuyCount() : 0;
		boolean reset = false;
		switch(limit.getLimitTerm()){
		case DAY:
			reset = limitObj != null && CommonUtil.isDayResetTimeCheck(limitObj.getBuyTime()) && ShopLimitLoader.getInstance().resetShopLimit(pc, limitObj, limit.getLimitType());
			if (reset) {
				current = 0;
			}
			os.writeAccountBuyLimitDay(limit.getLimitCount(), current);
			break;
		case WEEK:
			reset = limitObj != null && CommonUtil.isWeekResetCheck(limitObj.getBuyTime()) && ShopLimitLoader.getInstance().resetShopLimit(pc, limitObj, limit.getLimitType());
			if (reset) {
				current = 0;
			}
			os.writeAccountBuyLimitWeek(limit.getLimitCount(), current);
			break;
		default:
			int count = limit.getLimitCount() - current;
			os.writeString(count > 0 ? String.format("%s%d" + S_SystemMessage.getRefText(1073), BUY_ACCOUNT_COUNT_STRING, count) : BUY_ACCOUNT_FAIL_STRING);
			break;
		}
	}
	
	/**
	 * 구매 가능 개수 표기(캐릭터)
	 * @param os
	 * @param pc
	 * @param limit
	 * @param limitObj
	 */
	void writeShopLimitFromCharacter(BinaryOutputStreamExtraDescription os, L1PcInstance pc, ShopLimitInformation limit, ShopLimitObject limitObj) {
		int current			= limitObj != null ? limitObj.getBuyCount() : 0;
		boolean reset = false;
		switch(limit.getLimitTerm()){
		case DAY:
			reset = limitObj != null && CommonUtil.isDayResetTimeCheck(limitObj.getBuyTime()) && ShopLimitLoader.getInstance().resetShopLimit(pc, limitObj, limit.getLimitType());
			if (reset) {
				current = 0;
			}
			int day_count = limit.getLimitCount() - current;
			os.writeString(day_count > 0 ? String.format("%s%d" + S_SystemMessage.getRefText(1073), BUY_DAY_COUNT_STRING, day_count) : BUY_DAY_FAIL_STRING);
			break;
		case WEEK:
			reset = limitObj != null && CommonUtil.isWeekResetCheck(limitObj.getBuyTime()) && ShopLimitLoader.getInstance().resetShopLimit(pc, limitObj, limit.getLimitType());
			if (reset) {
				current = 0;
			}
			int week_count = limit.getLimitCount() - current;
			os.writeString(week_count > 0 ? String.format("%s%d" + S_SystemMessage.getRefText(1073), BUY_WEEK_COUNT_STRING, week_count) : BUY_WEEK_FAIL_STRING);
			break;
		default:
			int count = limit.getLimitCount() - current;
			os.writeString(count > 0 ? String.format("%s%d" + S_SystemMessage.getRefText(1073), BUY_COUNT_STRING, count) : BUY_FAIL_STRING);
			break;
		}
	}

	private L1PcInstance _itemOwner;
	public L1PcInstance getItemOwner() {
		return _itemOwner;
	}
	public void setItemOwner(L1PcInstance pc) {
		_itemOwner = pc;
	}

	public void startItemOwnerTimer(L1PcInstance pc) {
		setItemOwner(pc);
		L1ItemOwnerTimer timer = new L1ItemOwnerTimer(this, 10000);
		timer.begin();
	}
	
	private ConcurrentHashMap<L1PcInstance, Integer> _itemOuter;
	public ConcurrentHashMap<L1PcInstance, Integer> getItemOuter(){
		return _itemOuter;
	}
	public int getItemOuterValue(L1PcInstance pc){
		return _itemOuter == null || !_itemOuter.contains(pc) ? 0 : _itemOuter.get(pc);
	}
	public void putItemOuter(L1PcInstance pc, int hatePercent){
		if (hatePercent <= 0) {
			return;
		}
		if (_itemOuter == null) {
			_itemOuter = new ConcurrentHashMap<L1PcInstance, Integer>();
		}
		_itemOuter.put(pc, hatePercent);
	}
	public void disposeItemOuter(){
		if (_itemOuter == null) {
			return;
		}
		_itemOuter.clear();
		_itemOuter = null;
	}

	private L1EquipmentTimer _equipmentTimer;
	public void startEquipmentTimer(L1PcInstance pc) {
		if (getRemainingTime() > 0) {
			_equipmentTimer = new L1EquipmentTimer(pc, this, 1000L);
			GeneralThreadPool.getInstance().schedule(_equipmentTimer, 1000L);
		}
	}

	public void stopEquipmentTimer(L1PcInstance pc) {
		if (getRemainingTime() > 0) {
			_equipmentTimer.cancel();
			_equipmentTimer = null;
		}
	}

	private boolean _isNowLighting;
	public boolean isNowLighting() {
		return _isNowLighting;
	}
	public void setNowLighting(boolean flag) {
		_isNowLighting = flag;
	}
	
	private L1Npc dropNpc;
	public L1Npc getDropNpc() {
		return dropNpc;
	}
	public void setDropNpc(L1Npc template) {
		dropNpc = template;
	}

	private int _keyId;
	public int getKeyId() {
		return _keyId;
	}
	public void setKeyId(int i) {
		_keyId = i;
	}
	
	private boolean _isWorking;
	public boolean isWorking() {
		return _isWorking;
	}
	public void setWorking(boolean flag) {
		_isWorking = flag;
	}
	
	public static int getAttrEnchantBit(int attr) {
		int attr_bit = 0, result_bit = 0;
        // 1/2/3/33/34  		attr_bit = 1; 
        // 4/5/6/35/36  		attr_bit = 2; 
        // 7/8/9/37/38  		attr_bit = 3; 
        // 10/11/12/39/40       attr_bit = 4;
		switch (attr) {
		case 1:	attr_bit = 1;	attr = 1;break;
		case 2:	attr_bit = 1;	attr = 2;break;
		case 3:	attr_bit = 1;	attr = 3;break;
		case 4:	attr_bit = 1;	attr = 4;break;
		case 5:	attr_bit = 1;	attr = 5;break;
		case 6:	attr_bit = 2;	attr = 1;break;
		case 7:	attr_bit = 2;	attr = 2;break;
		case 8:	attr_bit = 2;	attr = 3;break;
		case 9:	attr_bit = 2;	attr = 4;break;
		case 10:attr_bit = 2;	attr = 5;break;
		case 11:attr_bit = 3;	attr = 1;break;
		case 12:attr_bit = 3;	attr = 2;break;
		case 13:attr_bit = 3;	attr = 3;break;
		case 14:attr_bit = 3;	attr = 4;break;
		case 15:attr_bit = 3;	attr = 5;break;
		case 16:attr_bit = 4;	attr = 1;break;
		case 17:attr_bit = 4;	attr = 2;break;
		case 18:attr_bit = 4;	attr = 3;break;
		case 19:attr_bit = 4;	attr = 4;break;
		case 20:attr_bit = 4;	attr = 5;break;
		default:break;
		}
		if (attr > 0) {
			result_bit = attr_bit + (attr << 4);
		}
		return result_bit;
	}
	
	public static int getElementalEnchantType(int attrLv) {
		if (attrLv >= 1 && attrLv <= 5) {
			return 1;
		}
		if (attrLv >= 6 && attrLv <= 10) {
			return 2;
		}
		if (attrLv >= 11 && attrLv <= 15) {
			return 3;
		}
		if (attrLv >= 16 && attrLv <= 20) {
			return 4;
		}
		return 0;
	}
	
	public static int getElementalEnchantValue(int attrLv) {
		int level = attrLv;
		if (attrLv >= 6 && attrLv <= 10) {
			return level -= 5;
		}
		if (attrLv >= 11 && attrLv <= 15) {
			return level -= 10;
		}
		if (attrLv >= 16 && attrLv <= 20) {
			return level -= 15;
		}
		return level;
	}
	
	public int getDeposit(){
		// 0 창고 사용 불가
		// 2 창고 사용 불가
		// 3 개인창고, 특수창고 사용 가능
		// 7 창고 모두 사용 가능
		if (isSlot()) {
			return 0;
		}
		if (getBless() >= 128) {
			return 3;
		}
		if (!_item.isRetrieve() || !_item.isSpecialRetrieve() || _engrave) {
			return 2;
		}
		return 7;
	}
	
	public int getAttributeBitSet(){
		int b = 0;
		if (isIdentified()) {
			b = 1;
		}
		if (!_item.isTradable() || _engrave) {
			b |= 2;// 이동 불가
		}
		if (_item.isCantDelete()) {
			b |= 4;// 삭제 불가
		}
		if (_item.getSafeEnchant() < 0) {
			b |= 8;// 강화 불가
		}
		if (isSlot()) {
			b |= 14;// 슬롯 장착
		}
		if (!_item.isRetrieve() || !_item.isSpecialRetrieve() || _engrave) {
			b |= 16;// 창고 불가
		}
		
		int bless = getBless();
		if (bless >= 128 && bless <= 131) {
			b |= 2;// 이동불가
			b |= 4;// 삭제불가
			b |= 8;// 강화불가
			b |= 32;
		} else if (bless > 131) {
			b |= 64;
		}
		if (_item.isMerge()) {
			b |= 128;
		}
		return b;
	}
	
	public int getAttributeBitSetEx() {
		if (isSlot()) {
			return 130;
		}
		if (_item.getItemType() != L1ItemType.NORMAL && getBless() >= 128) {
			return 16;
		}
		if (!_item.isSpecialRetrieve() && !_item.isCantSell()) {
			return 130;
		}
		if (!_item.isCantSell()) {
			return 128;
		}
		if (!_item.isSpecialRetrieve()) {
			return 2;
		}
		switch (_itemId) {
		case 41296:case 41297:case 41301:case 41303:case 41304:case 600230:case 820018:
			return 8;
		default:
			/*if (_item.get_interaction_type() == L1ItemType.MAGICDOLL.getId()) {
				return 32;
			}*/
			return 0;
		}
	}
	
	/**
	 * 아이템 정보 프로토 패킷
	 * @param pc
	 * @return byte[]
	 */
	public byte[] getItemInfo(L1PcInstance pc) {
		return getItemInfo(pc, _itemId, _count, getViewName());
	}
	
	/**
	 * 아이템 정보 프로토 패킷
	 * @param pc
	 * @param count
	 * @return byte[]
	 */
	public byte[] getItemInfo(L1PcInstance pc, int count) {
		return getItemInfo(pc, _itemId, count, getNumberedViewName(count));
	}
	
	/**
	 * 아이템 정보 프로토 패킷
	 * @param pc
	 * @param db_id
	 * @param count
	 * @return byte[]
	 */
	public byte[] getItemInfo(L1PcInstance pc, int db_id, int count) {
		return getItemInfo(pc, db_id, count, getNumberedViewName(count));
	}
	
	/**
	 * 아이템 정보 프로토 패킷
	 * @param pc
	 * @param db_id
	 * @param count
	 * @param description
	 * @return byte[]
	 */
	public byte[] getItemInfo(L1PcInstance pc, int db_id, int count, String description) {
		// TODO 아이템 패킷
		BinaryOutputStreamItemInfo os = null;
	    try {
	    	os = new BinaryOutputStreamItemInfo();
	    	
	    	os.write_object_id(getId());
	    	os.write_name_id(_item.getItemNameId());
			os.write_db_id(db_id);
			os.write_count(count);

			int interact_type = _item.get_interaction_type();
			if (interact_type > 0) {
				os.write_interact_type(interact_type);
			}

			if (_chargeCount > 0) {
				os.write_number_of_use(_chargeCount);
			}
			
			os.write_icon_id(_item.getIconId());
			os.write_bless_code_for_display(_bless);
			os.write_attribute_bit_set(getAttributeBitSet());

			// 추가표기    2:특수창고, 16:봉인, 18:봉인 특수창고, 22:이동 삭제 강화 특수창고, 32~63,96~127:팅김(인형이팩트), 
			// 128:상점 , 130:특수창고 상점, 144:봉인 상점, 146:봉인 특수창고 상점
			int attribute_bit_set_ex = getAttributeBitSetEx();
			if (attribute_bit_set_ex > 0) {
				os.write_attribute_bit_set_ex(attribute_bit_set_ex);
			}

			os.write_is_timeout(false);
			os.write_category(_item.getItemType().getInteractionType());

			if (_enchantLevel > 0) {
				os.write_enchant(_enchantLevel);
			}

			int deposit = getDeposit();
			if (deposit > 0) {
				os.write_deposit(deposit);
			}

			if (_attrenchantLevel > 0) {
				os.write_elemental_enchant_type(getElementalEnchantType(_attrenchantLevel));
				os.write_elemental_enchant_value(getElementalEnchantValue(_attrenchantLevel));
			}
			
			os.write_description(description);

			if (_isIdentified) {
				os.write_extra_description(getStatusBytes(pc));
			}
			
			if (_itemId == L1ItemId.PET_AMULET) {
				byte[] companionCard = get_companion_card_byte();
				if (companionCard != null) {
                    os.write_companion_card(companionCard);
				}
	        }
			
			os.write_bless_code(_bless);
			os.write_real_enchant(_enchantLevel);
	        os.write_is_merging(_item.isMerge());
	        os.write_weight(_item.getWeight());
	        os.write_is_identified(_isIdentified);
	        
	        if (_potential != null) {
	        	os.write_potential_grade(_potential.getInfo().get_bonus_grade());
	        }
	        
	        if (isValidationSlot()) {
	        	os.write_slot_count(Config.SMELTING.SMELTING_LIMIT_SLOT_VALUE);
	        	if (isSlot()) {
	        		os.write_slot_info(_slots);
	        	}
	        }
			return os.getBytes();
	    } catch(Exception e) {
	    	e.printStackTrace();
	    } finally {
	    	if (os != null) {
				try {
					os.close();
					os = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	    }
		return null;
	}
	
	/**
	 * 슬롯 장착가능 아이템 검증
	 * @return boolean
	 */
	boolean isValidationSlot() {
		// 불가 상태 아이템 검증
		if (!_item.isTradable() || !_item.isRetrieve() || !_item.isSpecialRetrieve() || _engrave || _endTime != null) {
			return false;
		}
		
		// 무기, 방어구 장착 가능 검증
		if (!Config.SMELTING.SMELTING_EQUIP_TYPES.contains(_item.getItemType())) {
			return false;
		}
		
		// 아이템 타입별 장착 가능 검증
		switch (_item.getItemType()) {
    	case WEAPON:return Config.SMELTING.SMELTING_EQUIP_WEAPON_TYPES.contains(_item.getType());
    	case ARMOR:	return Config.SMELTING.SMELTING_EQUIP_ARMOR_TYPES.contains(_item.getType());
    	default:	return false;
    	}
	}
	
	/**
	 * 펫 목걸이 상세 정보
	 * @return byte[]
	 */
	byte[] getCompanionCardBytes(){
		L1Pet companion = CharacterCompanionTable.getInstance().getTemplate(getId());
		if (companion == null) {
			return null;
		}
    	BinaryOutputStreamCompanionCard os = null;
        try {
        	os = new BinaryOutputStreamCompanionCard();
        	os.write_oblivion(companion.isOblivion());
        	os.write_is_dead(companion.isDead());
        	os.write_is_summoned(companion.is_summoned());
        	os.write_class_id(companion.getNpc().getClassId());
        	os.write_level(companion.getLevel());
        	os.write_name(companion.getName());
        	os.write_elixir_use_count(companion.get_elixir_use_count());
            return os.getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	try {
        		if (os != null) {
        			os.close();
        			os = null;
            	}
        	} catch(Exception e) {
        		e.printStackTrace();
        	}
        }
        return null;
	}
	
	byte[] getCompanionCardBytes(L1Pet pet){
		BinaryOutputStreamCompanionCard os = null;
        try {
        	os = new BinaryOutputStreamCompanionCard(pet);
            return os.getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	try {
        		if (os != null) {
        			os.close();
        			os = null;
            	}
        	} catch(Exception e) {
        		e.printStackTrace();
        	}
        }
        return null;
	}
	
}

