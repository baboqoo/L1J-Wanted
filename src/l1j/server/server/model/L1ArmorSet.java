package l1j.server.server.model;

import java.util.ArrayList;
import java.util.StringTokenizer;

import l1j.server.common.data.Gender;
import l1j.server.server.construct.L1CharacterInfo;
import l1j.server.server.construct.item.L1ItemArmorType;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.datatables.ArmorSetTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.spell.S_SkillIconBlessOfEva;
import l1j.server.server.templates.L1ArmorSets;
import l1j.server.server.utils.StringUtil;

public abstract class L1ArmorSet {
	public abstract void giveEffect(L1PcInstance pc);
	public abstract void cancelEffect(L1PcInstance pc);
	public abstract boolean isValid(L1PcInstance pc);
	public abstract boolean isPartOfSet(int id);
	public abstract boolean isEquippedRingOfArmorSet(L1PcInstance pc);
	public abstract boolean isMinEnchant(int val);

	public static ArrayList<L1ArmorSet> getAllSet() {
		return _allSet;
	}

	private static ArrayList<L1ArmorSet> _allSet = new ArrayList<L1ArmorSet>();
	
	static {
		L1ArmorSetImpl impl;
		for (L1ArmorSets armorSets : ArmorSetTable.getInstance().getAllList()) {
			try {
				impl = new L1ArmorSetImpl(getArray(armorSets.getSets(), StringUtil.CommaString), armorSets.getMinEnchant());
				
				if (armorSets.getPolyId() != -1) {
					impl.addEffect(new PolymorphEffect(armorSets.getPolyId()));
				}
				
				if (armorSets.getId() == 128) {
					impl.addEffect(new EvaiconEffect());
				}
				
				impl.addEffect(new DamageEffect(
						armorSets.getShortHitup(), armorSets.getShortDmgup(), armorSets.getShortCritical(),
						armorSets.getLongHitup(), armorSets.getLongDmgup(), armorSets.getLongCritical(),
						armorSets.getSp(), armorSets.getMagicHitup(), armorSets.getMagicCritical()));
				
				impl.addEffect(new AcHpMpBonusEffect(armorSets.getAc(), armorSets.getHp(), armorSets.getMp(), 
						armorSets.getHpr(), armorSets.getMpr(), armorSets.getMr()));
				
				impl.addEffect(new StatBonusEffect(armorSets.getStr(), armorSets.getDex(), armorSets.getCon(), 
						armorSets.getWis(), armorSets.getCha(), armorSets.getIntl()));
				
				impl.addEffect(new AttrEffect(armorSets.getEarth(), armorSets.getFire(), 
						armorSets.getWind(), armorSets.getWater()));
				
				impl.addEffect(new ToleranceEffect(armorSets.getToleranceSkill(), armorSets.getToleranceSpirit(), 
						armorSets.getToleranceDragon(), armorSets.getToleranceFear(), armorSets.getToleranceAll()));
				
				impl.addEffect(new HitupEffect(armorSets.getHitupSkill(), armorSets.getHitupSpirit(), 
						armorSets.getHitupDragon(), armorSets.getHitupFear(), armorSets.getHitupAll()));
				
				impl.addEffect(new ExpEffect(armorSets.getExpBonus(), armorSets.getRestExpReduceEfficiency()));
				
				impl.addEffect(new DefenseEffect(armorSets.getDg(), armorSets.getEr(), armorSets.getMe()));
				
				impl.addEffect(new ReductionEffect(armorSets.getReduc(), armorSets.getReducEgnor(), armorSets.getMagicReduc(),
						armorSets.getAbnormalStatusDamageReduction()));
				
				impl.addEffect(new PVPAblityEffect(armorSets.getPVPDamage(), armorSets.getPVPReduc(), 
						armorSets.getPVPMagicReduc(), armorSets.getPVPReducEgnor(), armorSets.getPVPMagicReducEgnor(),
						armorSets.getAbnormalStatusPVPDamageReduction(), armorSets.getPVPDamagePercent()));
				
				impl.addEffect(new EtcEffect(armorSets.getStrangeTimeIncrease(), armorSets.isUnderWather()));
				_allSet.add(impl);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private static int[] getArray(String s, String sToken) {
		StringTokenizer st = new StringTokenizer(s, sToken);
		int size = st.countTokens();
		String temp = null;
		int[] array = new int[size];
		for (int i = 0; i < size; i++) {
			temp = st.nextToken();
			array[i] = Integer.parseInt(temp);
		}
		return array;
	}
}

interface L1ArmorSetEffect {
	public void giveEffect(L1PcInstance pc);
	public void cancelEffect(L1PcInstance pc);
	public void doEffect(L1PcInstance pc, boolean flag);
}

class L1ArmorSetImpl extends L1ArmorSet {
	private final int _ids[];
	private final ArrayList<L1ArmorSetEffect> _effects;
	private final int _minEnchant;

	protected L1ArmorSetImpl(int ids[], int minEnchant) {
		_ids = ids;
		_effects = new ArrayList<L1ArmorSetEffect>();
		_minEnchant = minEnchant;
	}

	public void addEffect(L1ArmorSetEffect effect) {
		_effects.add(effect);
	}

	public void removeEffect(L1ArmorSetEffect effect) {
		_effects.remove(effect);
	}

	@Override
	public void cancelEffect(L1PcInstance pc) {
		for (L1ArmorSetEffect effect : _effects) {
			effect.cancelEffect(pc);
		}
	}

	@Override
	public void giveEffect(L1PcInstance pc) {
		for (L1ArmorSetEffect effect : _effects) {
			effect.giveEffect(pc);
		}
	}

	@Override
	public final boolean isValid(L1PcInstance pc) {
		return pc.getInventory().checkEquipped(_ids);
	}

	@Override
	public boolean isPartOfSet(int id) {
		for (int i : _ids) {
			if (id == i) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isEquippedRingOfArmorSet(L1PcInstance pc) {
		L1PcInventory pcInventory = pc.getInventory();
		L1ItemInstance armor = null;
		boolean isSetContainRing = false;

		for (int id : _ids) {
			armor = pcInventory.findItemId(id);
			if (armor.getItem().getItemType() == L1ItemType.ARMOR 
					&& armor.getItem().getType() == L1ItemArmorType.RING.getId()) { // ring
				isSetContainRing = true;
				break;
			}
		}

		if (armor != null && isSetContainRing) {
			int itemId = armor.getItem().getItemId();
			if (pcInventory.getTypeEquipped(L1ItemType.ARMOR, L1ItemArmorType.RING.getId()) >= 2) {
				L1ItemInstance ring[] = new L1ItemInstance[4];
				ring = pcInventory.getRingEquipped();
				if (ring != null && ring.length > 0) {
					int count = 0;
					for (L1ItemInstance item : ring) {
						if (item == null) {
							continue;
						}
						if (item.getItemId() == itemId) {
							count++;
						}
					}
					if (count >= 2) {
						return true;
					}
				}
				/*
				 * if (ring[0].getItem().getItemId() == itemId &&
				 * ring[1].getItem().getItemId() == itemId) { return true; }
				 */
			}
		}
		return false;
	}
	
	@Override
	public boolean isMinEnchant(int val) {
		return _minEnchant == 0 || _minEnchant <= val;
	}
}

class AttrEffect implements L1ArmorSetEffect {
	final int _eath, _fire, _wind, _water;
	AttrEffect(int eath, int fire, int wind, int water) {
		_eath	= eath;
		_fire	= fire;
		_wind	= wind;
		_water	= water;
	}

	@Override
	public void giveEffect(L1PcInstance pc) {
		doEffect(pc, true);
	}

	@Override
	public void cancelEffect(L1PcInstance pc) {
		doEffect(pc, false);
	}
	
	@Override
	public void doEffect(L1PcInstance pc, boolean flag) {
		int val = flag ? 1 : -1;
		if (_eath != 0) {
			pc.resistance.addEarth(_eath * val);
		}
		if (_fire != 0) {
			pc.resistance.addFire(_fire * val);
		}
		if (_wind != 0) {
			pc.resistance.addWind(_wind * val);
		}
		if (_water != 0) {
			pc.resistance.addWater(_water * val);
		}
	}
}

class ToleranceEffect implements L1ArmorSetEffect {
	final int _toleranceSkill, _toleranceSpirit, _toleranceDragon, _toleranceFear, _toleranceAll;
	ToleranceEffect(int toleranceSkill, int toleranceSpirit, int toleranceDragon, int toleranceFear, int toleranceAll) {
		_toleranceSkill		= toleranceSkill;
		_toleranceSpirit	= toleranceSpirit;
		_toleranceDragon	= toleranceDragon;
		_toleranceFear		= toleranceFear;
		_toleranceAll		= toleranceAll;
	}
	
	@Override
	public void giveEffect(L1PcInstance pc) {
		doEffect(pc, true);
	}
	
	@Override
	public void cancelEffect(L1PcInstance pc) {
		doEffect(pc, false);
	}
	
	@Override
	public void doEffect(L1PcInstance pc, boolean flag) {
		int val = flag ? 1 : -1;
		if (_toleranceSkill != 0) {
			pc.resistance.addToleranceSkill(_toleranceSkill * val);
		}
		if (_toleranceSpirit != 0) {
			pc.resistance.addToleranceSpirit(_toleranceSpirit * val);
		}
		if (_toleranceDragon != 0) {
			pc.resistance.addToleranceDragon(_toleranceDragon * val);
		}
		if (_toleranceFear != 0) {
			pc.resistance.addToleranceFear(_toleranceFear * val);
		}
		if (_toleranceAll != 0) {
			pc.resistance.addToleranceAll(_toleranceAll * val);
		}
	}
}

class HitupEffect implements L1ArmorSetEffect {
	final int _hitupSkill, _hitupSpirit, _hitupDragon, _hitupFear, _hitupAll;
	HitupEffect(int hitupSkill, int hitupSpirit, int hitupDragon, int hitupFear, int hitupAll) {
		_hitupSkill			= hitupSkill;
		_hitupSpirit		= hitupSpirit;
		_hitupDragon		= hitupDragon;
		_hitupFear			= hitupFear;
		_hitupAll			= hitupAll;
	}
	
	@Override
	public void giveEffect(L1PcInstance pc) {
		doEffect(pc, true);
	}
	
	@Override
	public void cancelEffect(L1PcInstance pc) {
		doEffect(pc, false);
	}
	
	@Override
	public void doEffect(L1PcInstance pc, boolean flag) {
		int val = flag ? 1 : -1;
		if (_hitupSkill != 0) {
			pc.resistance.addHitupSkill(_hitupSkill * val);
		}
		if (_hitupSpirit != 0) {
			pc.resistance.addHitupSpirit(_hitupSpirit * val);
		}
		if (_hitupDragon != 0) {
			pc.resistance.addHitupDragon(_hitupDragon * val);
		}
		if (_hitupFear != 0) {
			pc.resistance.addHitupFear(_hitupFear * val);
		}
		if (_hitupAll != 0) {
			pc.resistance.addHitupAll(_hitupAll * val);
		}
	}
}

class ExpEffect implements L1ArmorSetEffect {
	final int _expBonus, _restExpReduceEfficiency;
	ExpEffect(int expBonus, int restExpReduceEfficiency) {
		_expBonus					= expBonus;
		_restExpReduceEfficiency	= restExpReduceEfficiency;
	}
	
	@Override
	public void giveEffect(L1PcInstance pc) {
		doEffect(pc, true);
	}
	
	@Override
	public void cancelEffect(L1PcInstance pc) {
		doEffect(pc, false);
	}
	
	@Override
	public void doEffect(L1PcInstance pc, boolean flag) {
		int val = flag ? 1 : -1;
		if (_expBonus != 0) {
			pc.add_exp_boosting_ratio(_expBonus * val);
		}
		if (_restExpReduceEfficiency != 0) {
			pc.add_rest_exp_reduce_efficiency(_restExpReduceEfficiency * val);
		}
	}
}

class DefenseEffect implements L1ArmorSetEffect {
	final int _dg, _er, _me;
	DefenseEffect(int dg, int er, int me) {
		_dg		= dg;
		_er		= er;
		_me		= me;
	}
	
	@Override
	public void giveEffect(L1PcInstance pc) {
		doEffect(pc, true);
	}
	
	@Override
	public void cancelEffect(L1PcInstance pc) {
		doEffect(pc, false);
	}
	
	@Override
	public void doEffect(L1PcInstance pc, boolean flag) {
		int val = flag ? 1 : -1;
		if (_dg != 0) {
			pc.ability.addDg(_dg * val);
		}
		if (_er != 0) {
			pc.ability.addEr(_er * val);
		}
		if (_me != 0) {
			pc.ability.addMe(_me * val);
		}
	}
}

class ReductionEffect implements L1ArmorSetEffect {
	final int _reduc, _reducEgnor, _magicReduc, _abnormalStatusDamageReduction;
	ReductionEffect(int reduc, int reducEgnor, int magicReduc, int abnormalStatusDamageReduction) {
		_reduc							= reduc;
		_reducEgnor						= reducEgnor;
		_magicReduc						= magicReduc;
		_abnormalStatusDamageReduction	= abnormalStatusDamageReduction;
	}
	
	@Override
	public void giveEffect(L1PcInstance pc) {
		doEffect(pc, true);
	}
	
	@Override
	public void cancelEffect(L1PcInstance pc) {
		doEffect(pc, false);
	}
	
	@Override
	public void doEffect(L1PcInstance pc, boolean flag) {
		int val = flag ? 1 : -1;
		if (_reduc != 0) {
			pc.ability.addDamageReduction(_reduc * val);
		}
		if (_reducEgnor != 0) {
			pc.ability.addDamageReductionEgnor(_reducEgnor * val);
		}
		if (_magicReduc != 0) {
			pc.ability.addMagicDamageReduction(_magicReduc * val);
		}
		if (_abnormalStatusDamageReduction != 0) {
			pc.ability.addAbnormalStatusDamageReduction(_abnormalStatusDamageReduction * val);
		}
	}
}

class PVPAblityEffect implements L1ArmorSetEffect {
	final int _pvpDmg, _pvpReduc, _pvpMagicReduc, _pvpReducEgnor, _pvpMagicReducEgnor, _abnormalStatusPVPDamageReduction, _pvpDmgPercent;
	PVPAblityEffect(int pvpDmg, int pvpReduc, int pvpMagicReduc, int pvpReducEgnor, int pvpMagicReducEgnor, int abnormalStatusPVPDamageReduction, int pvpDmgPercent) {
		_pvpDmg								= pvpDmg;
		_pvpReduc							= pvpReduc;
		_pvpMagicReduc						= pvpMagicReduc;
		_pvpReducEgnor						= pvpReducEgnor;
		_pvpMagicReducEgnor					= pvpMagicReducEgnor;
		_abnormalStatusPVPDamageReduction	= abnormalStatusPVPDamageReduction;
		_pvpDmgPercent						= pvpDmgPercent;
	}
	
	@Override
	public void giveEffect(L1PcInstance pc) {
		doEffect(pc, true);
	}
	
	@Override
	public void cancelEffect(L1PcInstance pc) {
		doEffect(pc, false);
	}
	
	@Override
	public void doEffect(L1PcInstance pc, boolean flag) {
		int val = flag ? 1 : -1;
		if (_pvpDmg != 0) {
			pc.ability.addPVPDamage(_pvpDmg * val);
		}
		if (_pvpReduc != 0) {
			pc.ability.addPVPDamageReduction(_pvpReduc * val);
		}
		if (_pvpMagicReduc != 0) {
			pc.ability.addPVPMagicDamageReduction(_pvpMagicReduc * val);
		}
		if (_pvpReducEgnor != 0) {
			pc.ability.addPVPDamageReductionEgnor(_pvpReducEgnor * val);
		}
		if (_pvpMagicReducEgnor != 0) {
			pc.ability.addPVPMagicDamageReductionEgnor(_pvpMagicReducEgnor * val);
		}
		if (_abnormalStatusPVPDamageReduction != 0) {
			pc.ability.addAbnormalStatusPVPDamageReduction(_abnormalStatusPVPDamageReduction * val);
		}
		if (_pvpDmgPercent != 0) {
			pc.ability.addPVPDamagePercent(_pvpDmgPercent * val);
		}
	}
}

class DamageEffect implements L1ArmorSetEffect {
	final int _shortHitup, _shortDmgup, _shortCritical;
	final int _longHitup, _longDmgup, _longCritical;
	final int _sp, _magicHitup, _magicCritical;
	DamageEffect(int shortHitup, int shortDmgup, int shortCritical, 
			int longHitup, int longDmgup, int longCritical, 
			int sp, int magicHitup, int magicCritical) {
		_shortHitup			= shortHitup;
		_shortDmgup			= shortDmgup;
		_shortCritical		= shortCritical;
		_longHitup			= longHitup;
		_longDmgup			= longDmgup;
		_longCritical		= longCritical;
		_sp					= sp;
		_magicHitup			= magicHitup;
		_magicCritical		= magicCritical;
	}

	@Override
	public void giveEffect(L1PcInstance pc) {
		doEffect(pc, true);
	}

	@Override
	public void cancelEffect(L1PcInstance pc) {
		doEffect(pc, false);
	}
	
	@Override
	public void doEffect(L1PcInstance pc, boolean flag) {
		int val = flag ? 1 : -1;
		if (_shortHitup != 0) {
			pc.ability.addShortHitup(_shortHitup * val);
		}
		if (_shortDmgup != 0) {
			pc.ability.addShortDmgup(_shortDmgup * val);
		}
		if (_shortCritical != 0) {
			pc.ability.addShortCritical(_shortCritical * val);
		}
		if (_longHitup != 0) {
			pc.ability.addLongHitup(_longHitup * val);
		}
		if (_longDmgup != 0) {
			pc.ability.addLongDmgup(_longDmgup * val);
		}
		if (_longCritical != 0) {
			pc.ability.addLongCritical(_longCritical * val);
		}
		if (_sp != 0) {
			pc.ability.addSp(_sp * val);
		}
		if (_magicHitup != 0) {
			pc.ability.addMagicHitup(_magicHitup * val);
		}
		if (_magicCritical != 0) {
			pc.ability.addMagicCritical(_magicCritical * val);
		}
	}
}

class AcHpMpBonusEffect implements L1ArmorSetEffect {
	final int _ac, _addHp, _addMp, _regenHp, _regenMp, _addMr;

	AcHpMpBonusEffect(int ac, int addHp, int addMp, int regenHp, int regenMp, int addMr) {
		_ac			= ac;
		_addHp		= addHp;
		_addMp		= addMp;
		_regenHp	= regenHp;
		_regenMp	= regenMp;
		_addMr		= addMr;
	}

	@Override
	public void giveEffect(L1PcInstance pc) {
		doEffect(pc, true);
	}

	@Override
	public void cancelEffect(L1PcInstance pc) {
		doEffect(pc, false);
	}
	
	@Override
	public void doEffect(L1PcInstance pc, boolean flag) {
		int val = flag ? 1 : -1;
		if (_ac != 0) {
			pc.ac.addAc(_ac * val);
		}
		if (_addHp != 0) {
			pc.addMaxHp(_addHp * val);
		}
		if (_addMp != 0) {
			pc.addMaxMp(_addMp * val);
		}
		if (_regenHp != 0) {
			pc.addHpRegen(_regenHp * val);
		}
		if (_regenMp != 0) {
			pc.addMpRegen(_regenMp * val);
		}
		if (_addMr != 0) {
			pc.resistance.addMr(_addMr * val);
		}
	}
}

class StatBonusEffect implements L1ArmorSetEffect {
	final int _str, _dex, _con, _wis, _cha, _intl;

	StatBonusEffect(int str, int dex, int con, int wis, int cha, int intl) {
		_str	= str;
		_dex	= dex;
		_con	= con;
		_wis	= wis;
		_cha	= cha;
		_intl	= intl;
	}

	@Override
	public void giveEffect(L1PcInstance pc) {
		doEffect(pc, true);
	}

	@Override
	public void cancelEffect(L1PcInstance pc) {
		doEffect(pc, false);
	}
	
	@Override
	public void doEffect(L1PcInstance pc, boolean flag) {
		int val = flag ? 1 : -1;
		if (_str != 0) {
			pc.ability.addAddedStr((byte) _str * val);
		}
		if (_dex != 0) {
			pc.ability.addAddedDex((byte) _dex * val);
		}
		if (_con != 0) {
			pc.ability.addAddedCon((byte) _con * val);
		}
		if (_wis != 0) {
			pc.ability.addAddedWis((byte) _wis * val);
		}
		if (_cha != 0) {
			pc.ability.addAddedCha((byte) _cha * val);
		}
		if (_intl != 0) {
			pc.ability.addAddedInt((byte) _intl * val);
		}
	}
}

class PolymorphEffect implements L1ArmorSetEffect {
	int _gfxId;
	PolymorphEffect(int gfxId) {
		_gfxId = gfxId;
	}

	@Override
	public void giveEffect(L1PcInstance pc) {
		if (_gfxId == 6080 || _gfxId == 6094) {
			if (!isRemainderOfCharge(pc)) {
				return;
			}
			_gfxId = pc.getGender() == Gender.MALE ? 6094 : 6080;
		}
		if (L1CharacterInfo.RANKING_POLY_LIST.contains(_gfxId)) {
			L1PolyMorph.doPoly(pc, L1CharacterInfo.RANKING_POLYS[pc.getType()][pc.getGender().toInt()], 0, L1PolyMorph.MORPH_BY_ARMOR_SET);
			return;
		}
		L1PolyMorph.doPoly(pc, _gfxId, -1, L1PolyMorph.MORPH_BY_ARMOR_SET);
	}

	@Override
	public void cancelEffect(L1PcInstance pc) {
		if (_gfxId == 6080 && pc.getGender() == Gender.MALE) {
			_gfxId = 6094;
		}
		if (!L1CharacterInfo.RANKING_POLY_LIST.contains(pc.getSpriteId()) && pc.getSpriteId() != _gfxId) {
			return;
		}
		L1PolyMorph.undoPoly(pc);
	}
	
	@Override
	public void doEffect(L1PcInstance pc, boolean flag) {
		// TODO Auto-generated method stub
	}

	private boolean isRemainderOfCharge(L1PcInstance pc) {
		L1ItemInstance item = pc.getInventory().findItemId(20383);// 기마용 투구
		if (item == null || item.getChargeCount() <= 0) {
			return false;
		}
		return true;
	}
}

class EtcEffect implements L1ArmorSetEffect {
	final int _strangeTimeIncrease;
	final boolean _underWater;
	EtcEffect(int strangeTimeIncrease, boolean underWater) {
		_strangeTimeIncrease	= strangeTimeIncrease;
		_underWater				= underWater;
	}
	
	@Override
	public void giveEffect(L1PcInstance pc) {
		doEffect(pc, true);
	}
	
	@Override
	public void cancelEffect(L1PcInstance pc) {
		doEffect(pc, false);
	}
	
	@Override
	public void doEffect(L1PcInstance pc, boolean flag) {
		if (_strangeTimeIncrease != 0) {
			pc.ability.addStrangeTimeIncrease(flag ? _strangeTimeIncrease : -_strangeTimeIncrease);
		}
		if (_underWater) {
			pc.ability.setUnderWater(flag);
		}
	}
}

class EvaiconEffect implements L1ArmorSetEffect {
	EvaiconEffect() {}

	@Override
	public void giveEffect(L1PcInstance pc) {
		pc.sendPackets(new S_SkillIconBlessOfEva(pc.getId(), -1), true);
	}

	@Override
	public void cancelEffect(L1PcInstance pc) {
		pc.sendPackets(new S_SkillIconBlessOfEva(pc.getId(), 0), true);
	}
	
	@Override
	public void doEffect(L1PcInstance pc, boolean flag) {
		// TODO Auto-generated method stub
	}
}
