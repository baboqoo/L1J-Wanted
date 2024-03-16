package l1j.server.server.model;

import java.util.ArrayList;

import l1j.server.Config;
import l1j.server.server.ActionCodes;
import l1j.server.server.construct.item.L1ItemArmorType;
import l1j.server.server.construct.item.L1ItemWeaponType;
import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_Ability;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.inventory.S_ItemStatus;
import l1j.server.server.serverpackets.inventory.S_NotiToggleInfo;
import l1j.server.server.serverpackets.spell.S_AvailableSpellNoti;
import l1j.server.server.serverpackets.spell.S_SkillIconBlessOfEva;
import l1j.server.server.serverpackets.spell.S_SlayerDelay;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.templates.L1Item;

public class L1EquipmentSlot {
	private L1PcInstance _owner;
	private ArrayList<L1ArmorSet> _currentArmorSet;
	private L1ItemInstance _weapon;
	private ArrayList<L1ItemInstance> _armors;

	public L1EquipmentSlot(L1PcInstance owner) {
		_owner				= owner;
		_armors				= new ArrayList<L1ItemInstance>();
		_currentArmorSet	= new ArrayList<L1ArmorSet>();
	}

	/**
	 * 무기류 착용
	 * @param weapon
	 */
	void setWeapon(L1ItemInstance weapon) {
		int itemId = weapon.getItem().getItemId();
		weapon.startEquipmentTimer(_owner);
		_weapon = weapon;
		_owner.setWeapon(weapon);
		L1ItemWeaponType weaponType = weapon.getItem().getWeaponType();
		if (weapon.getDurability() > 0) {
			_owner.sendPackets(new S_PacketBox(S_PacketBox.WEAPON_DAMAGED_GFX, weapon.getDurability()), true);// 손상도
		}
		
		_owner.setCurrentWeapon(_owner._isLancerForm ? ActionCodes.ACTION_WalkSpear : weaponType.getAction());
		
		if (weaponType == L1ItemWeaponType.TOHAND_SWORD && _owner.isPassiveStatus(L1PassiveId.RAIGING_WEAPON)) {
			_owner.addAttackSpeedDelayRate(Config.SPELL.RAIGING_WEAPONE_ATTACK_SPEED_RATE);
			_owner.sendPackets(S_SpellBuffNoti.RAIGING_WEAPONE_ON);
		} else if (L1ItemWeaponType.isBluntWeapon(weaponType) && _owner.isPassiveStatus(L1PassiveId.SLAYER)) {
			_owner.addAttackSpeedDelayRate(Config.SPELL.SLAYER_ATTACK_SPEED_RATE);
			_owner.sendPackets(S_SlayerDelay.SLAYER_ON);
		}
		
		if (itemId == 203003) {
			L1PolyMorph.doPoly(_owner, 12232, 0, L1PolyMorph.MORPH_BY_ITEMMAGIC);// 데스나이트의 불검:진
		}
	}

	public L1ItemInstance getWeapon() {
		return _weapon;
	}

	public boolean isWeapon(L1ItemInstance weapon) {
		return _weapon == weapon;
	}
	
	/**
	 * 세트 아이템 착용
	 * @param itemtype
	 * @param armorSet
	 * @param item
	 */
	void setArmorSet(int itemtype, L1ArmorSet armorSet, L1Item item) {
		if (itemtype == L1ItemArmorType.RING.getId()) {// 반지
			if (!armorSet.isEquippedRingOfArmorSet(_owner)) {
				armorSet.giveEffect(_owner);
				_currentArmorSet.add(armorSet);
				if (item.getMainId() != 0) {
					L1ItemInstance main = _owner.getInventory().findItemId(item.getMainId());
					if (main != null) {
						if (main.isEquipped()) {
							_owner.sendPackets(new S_ItemStatus(main,_owner, true, true), true);
						}
					}
				}
				if (item.getMainId2() != 0) {
					L1ItemInstance main = _owner.getInventory().findItemId(item.getMainId2());
					if (main != null) {
						if (main.isEquipped()) {
							_owner.sendPackets(new S_ItemStatus(main,_owner, true, true), true);
						}
					}
				}
				if (item.getMainId3() != 0) {
					L1ItemInstance main = _owner.getInventory().findItemId(item.getMainId3());
					if (main != null) {
						if (main.isEquipped()) {
							_owner.sendPackets(new S_ItemStatus(main,_owner, true, true), true);
						}
					}
				}

			}
		} else {
			armorSet.giveEffect(_owner);
			_currentArmorSet.add(armorSet);
			if (item.getMainId() != 0) {
				L1ItemInstance main = _owner.getInventory().findItemId(item.getMainId());
				if (main != null) {
					if (main.isEquipped()) {
						_owner.sendPackets(new S_ItemStatus(main,_owner, true, true), true);
					}
				}
			}
			if (item.getMainId2() != 0) {
				L1ItemInstance main = _owner.getInventory().findItemId(item.getMainId2());
				if (main != null) {
					if (main.isEquipped()) {
						_owner.sendPackets(new S_ItemStatus(main,_owner, true, true), true);
					}
				}
			}
			if (item.getMainId3() != 0) {
				L1ItemInstance main = _owner.getInventory().findItemId(item.getMainId3());
				if (main != null) {
					if (main.isEquipped()) {
						_owner.sendPackets(new S_ItemStatus(main,_owner, true, true), true);
					}
				}
			}
		}
	}

	/**
	 * 방어구류 착용
	 * @param armor
	 */
	void setArmor(L1ItemInstance armor) {
		L1Item item		= armor.getItem();
		int itemtype	= armor.getItem().getType();
		int itemId		= armor.getItem().getItemId();

		int acSub = armor.getAcSub();
		if (L1ItemArmorType.isAccessary(itemtype)) {
			_owner.getAC().addAc(item.getAc() + armor.getDurability() + acSub);
		} else {
			_owner.getAC().addAc(item.getAc() - (acSub != 0 ? -acSub : armor.getEnchantLevel()) + armor.getDurability());
		}

		_armors.add(armor);

		for (L1ArmorSet armorSet : L1ArmorSet.getAllSet()) {
			if (armorSet.isPartOfSet(itemId) && armorSet.isValid(_owner)) {
				if (!armorSet.isMinEnchant(armor.getEnchantLevel())) {
					break;
				}
				setArmorSet(itemtype, armorSet, item);
			}
		}
		
		if (L1ItemId.isInvisItem(itemId)) {
			_owner.invisibleItem();// 투명망토
		} else if (itemId == 20383 && armor.getChargeCount() != 0) {
			armor.setChargeCount(armor.getChargeCount() - 1);
			_owner.getInventory().updateItem(armor, L1PcInventory.COL_CHARGE_COUNT);
		} else if (itemId == 20288 || itemId == 202811 || itemId == 202812) {
			_owner.sendPackets(S_Ability.ABLITY_TELEPORT_ON);
		} else if (itemId == 20281) {
			_owner.sendPackets(S_Ability.ABLITY_POLY_ON);
		} else if (itemId == 20036) {
			_owner.sendPackets(S_Ability.ABLITY_INFRAVISION_ON);
		} else if (itemId == 20284) {
			_owner.sendPackets(S_Ability.ABLITY_SUMMON_ON);
		} else if (itemId == 20207) {
			_owner.sendPackets(new S_SkillIconBlessOfEva(_owner.getId(), -1), true);
		} else if (itemId == 900005) {
			_owner.sendPackets(S_SpellBuffNoti.SUCUBUS_QUEEN_BUFF_ICON_ON);// 서큐버스 퀸의 계약
		} else if (itemId == 20380) {
			_owner.startHalloweenRegeneration();// 할로윈 축복 모자
		} else if (itemId == 222351) {
			_owner.startElfArmorBlessing();// 신성한 요정족 판금 갑옷
		} else if (itemId >= 22200 && itemId <= 22203) {
			_owner.startPapuBlessing();// 파푸리온 갑옷
		} else if (itemId >= 22204 && itemId <= 22207) {
			_owner.startLindBlessing();// 린드비오르 갑옷
		} else if (itemId >= 23000 && itemId <= 23002) {// 할파스의 갑옷
			boolean isEnable = armor.getLastUsed() == null || (armor.getLastUsed() != null && armor.getLastUsed().getTime() < System.currentTimeMillis());
			_owner.sendPackets(isEnable ? S_SpellBuffNoti.FAITH_OF_HALPAH_ENABLE : S_SpellBuffNoti.FAITH_OF_HALPAH_DISABLE);
		} else if (armor.getDamageReductionChance()[1] > 0 && (itemId == 22226 || itemId == 222332)) {// 스탭퍼의 체력 반지
			_owner.ability.addSnaperRingReduction(armor.getDamageReductionChance()[1]);
		} else if (armor.getDamageReductionChance()[1] > 0 && (itemId == 22229 || itemId == 222337)) {// 룸티스의 붉은빛 귀걸이
			_owner.ability.setRoomtisEarringReduction(armor.getDamageReductionChance());
		} else if (armor.getDamageChance()[1] > 0 && (itemId == 222340 || itemId == 222341)) {// 룸티스의 검은빛 귀걸이
			_owner.ability.setRoomtisEarringDamage(armor.getDamageChance());
		} else if (itemId == 20207) {// 수중 부츠
			_owner.ability.setUnderWater(true);
		}
		
		setMagicHelmet(itemId);
		armor.startEquipmentTimer(_owner);
	}

	public ArrayList<L1ItemInstance> getArmors() {
		return _armors;
	}

	/**
	 * 무기류 착용 해제
	 * @param weapon
	 */
	void removeWeapon(L1ItemInstance weapon) {
		_owner.setWeapon(null);
		_owner.setCurrentWeapon(0);
		int itemId = weapon.getItem().getItemId();
		L1ItemWeaponType weaponType = weapon.getItem().getWeaponType();
		_owner.getInventory().setArrow(null);
	    _owner.getInventory().setSting(null);
		
		if (weapon.getDurability() > 0) {
			_owner.sendPackets(S_PacketBox.WEAPONE_DAMAGE_OFF);
		}

		weapon.stopEquipmentTimer(_owner);
		_weapon = null;
		_owner.setCurrentWeapon(0);
		
		if (weaponType == L1ItemWeaponType.TOHAND_SWORD && _owner.isPassiveStatus(L1PassiveId.RAIGING_WEAPON)) {
			_owner.addAttackSpeedDelayRate(-Config.SPELL.RAIGING_WEAPONE_ATTACK_SPEED_RATE);
			_owner.sendPackets(S_SpellBuffNoti.RAIGING_WEAPONE_OFF);
		} else if (L1ItemWeaponType.isBluntWeapon(weaponType) && _owner.isPassiveStatus(L1PassiveId.SLAYER)) {
			_owner.addAttackSpeedDelayRate(-Config.SPELL.SLAYER_ATTACK_SPEED_RATE);
			_owner.sendPackets(S_SlayerDelay.SLAYER_OFF);
		}
		
		if (itemId == 203003) {
			L1PolyMorph.undoPoly(_owner);// 데스나이트의 불검:진
		}
	}
	
	/**
	 * 세트 아이템 착용 해제
	 * @param armorSet
	 * @param item
	 */
	void removeArmorSet(L1ArmorSet armorSet, L1Item item) {
		armorSet.cancelEffect(_owner);
		_currentArmorSet.remove(armorSet);
		if (item.getMainId() != 0) {
			L1ItemInstance main = _owner.getInventory().findItemId(item.getMainId());
			if (main != null) {
				_owner.sendPackets(new S_ItemStatus(main, _owner, true, false), true);
			}
		}
		if (item.getMainId2() != 0) {
			L1ItemInstance main = _owner.getInventory().findItemId(item.getMainId2());
			if (main != null) {
				_owner.sendPackets(new S_ItemStatus(main, _owner, true, false), true);
			}
		}
		if (item.getMainId3() != 0) {
			L1ItemInstance main = _owner.getInventory().findItemId(item.getMainId3());
			if (main != null) {
				_owner.sendPackets(new S_ItemStatus(main, _owner, true, false), true);
			}
		}
	}

	/**
	 * 방어구류 착용 해제
	 * @param armor
	 */
	void removeArmor(L1ItemInstance armor) {
		L1Item item		= armor.getItem();
		int itemId		= armor.getItem().getItemId();
		int itemtype	= armor.getItem().getType();
		
		int acSub = armor.getAcSub();
		if (L1ItemArmorType.isAccessary(itemtype)) {
			_owner.getAC().addAc(-(item.getAc() + armor.getDurability() + acSub));
		} else {
			_owner.getAC().addAc(-(item.getAc() - (acSub != 0 ? -acSub : armor.getEnchantLevel()) + armor.getDurability()));
		}

		for (L1ArmorSet armorSet : L1ArmorSet.getAllSet()) {
			if (armorSet.isPartOfSet(itemId) && _currentArmorSet.contains(armorSet) && !armorSet.isValid(_owner)) {
				removeArmorSet(armorSet, item);
			}
		}

		// 셋트아이템 메소드로 변경..
		//removeSetItems(itemId);

		if (L1ItemId.isInvisItem(itemId)) {// 투명망토
			if (System.currentTimeMillis() - _owner.getInventory().timeVisible < _owner.getInventory().timeVisibleDelay) {
				return;// 딜레이검사
			}
			_owner.delInvis();
		} else if (itemId == 20288 || itemId == 202811 || itemId == 202812) {
			_owner.sendPackets(S_Ability.ABLITY_TELEPORT_OFF);
		} else if (itemId == 20281) {
			_owner.sendPackets(S_Ability.ABLITY_POLY_OFF);
		} else if (itemId == 20036) {
			_owner.sendPackets(S_Ability.ABLITY_INFRAVISION_OFF);
		} else if (itemId == 20284) {
			_owner.sendPackets(S_Ability.ABLITY_SUMMON_OFF);
		} else if (itemId == 20207) {
			_owner.sendPackets(new S_SkillIconBlessOfEva(_owner.getId(), 0), true);
		} else if (itemId == 900005) {
			_owner.sendPackets(S_SpellBuffNoti.SUCUBUS_QUEEN_BUFF_ICON_OFF);// 서큐버스 퀸의 계약
		} else if (itemId == 20380) {
			_owner.stopHalloweenRegeneration();// 할로윈 축복 모자
		} else if (itemId == 222351) {
			_owner.stopElfArmorBlessing();// 신성한 요정족 판금 갑옷
		} else if (itemId >= 22200 && itemId <= 22203) {
			_owner.stopPapuBlessing();// 파푸리온 갑옷
		} else if (itemId >= 22204 && itemId <= 22207) {
			_owner.stopLindBlessing();// 린드비오르 갑옷
		} else if (itemId >= 23000 && itemId <= 23002) {
			_owner.sendPackets(S_SpellBuffNoti.FAITH_OF_HALPAH_END);// 할파스의 갑옷
			_owner.sendPackets(S_NotiToggleInfo.DEFAULT_FAITH_OF_HALPAH);
		} else if (armor.getDamageReductionChance()[1] > 0 && (itemId == 22226 || itemId == 222332)) {// 스탭퍼의 체력 반지
			_owner.ability.addSnaperRingReduction(-armor.getDamageReductionChance()[1]);
		} else if (armor.getDamageReductionChance()[1] > 0 && (itemId == 22229 || itemId == 222337)) {// 룸티스의 붉은빛 귀걸이
			_owner.ability.setRoomtisEarringReduction(null);
		} else if (armor.getDamageChance()[1] > 0 && (itemId == 222340 || itemId == 222341)) {// 룸티스의 검은빛 귀걸이
			_owner.ability.setRoomtisEarringDamage(null);
		} else if (itemId == 20207) {// 수중부츠
			_owner.ability.setUnderWater(false);
		}
		
		removeMagicHelmet(itemId);
		armor.stopEquipmentTimer(_owner);
		_armors.remove(armor);
	}

	/**
	 * 아이템 착용
	 * @param equipment
	 */
	public void set(L1ItemInstance equipment) {
		switch (equipment.getItem().getItemType()) {
		case WEAPON:
			setWeapon(equipment);
			break;
		case ARMOR:
			setArmor(equipment);
			break;
		default:
			return;
		}
		_owner.getInventory().setItemAblity(equipment);
	}

	/**
	 * 아이템 착용 해제
	 * @param equipment
	 */
	public void remove(L1ItemInstance equipment) {
		switch (equipment.getItem().getItemType()) {
		case WEAPON:
			removeWeapon(equipment);
			break;
		case ARMOR:
			removeArmor(equipment);
			break;
		default:
			return;
		}
		_owner.getInventory().removeItemAblity(equipment);
	}

	/**
	 * 마법 투구 착용
	 * @param itemId
	 */
	void setMagicHelmet(int itemId) {
		if (itemId == 20013) {// 마법의 투구: 신속
			_owner.sendPackets(S_AvailableSpellNoti.HELMET_OF_DEX);
		} else if (itemId == 20014) {// 마법의 투구: 치유
			_owner.sendPackets(S_AvailableSpellNoti.HELMET_OF_HEAL);
		} else if (itemId == 20015) {// 마법의 투구: 힘
			_owner.sendPackets(S_AvailableSpellNoti.HELMET_OF_STR);
		} else if (itemId == 20008) {// 낡은 바람의 투구
			_owner.sendPackets(S_AvailableSpellNoti.HELMET_OF_OLD_WIND);
		} else if (itemId == 20023) {// 바람의 투구
			_owner.sendPackets(S_AvailableSpellNoti.HELMET_OF_WIND);
		}
	}

	/**
	 * 마법 투구 착용 해제
	 * @param itemId
	 */
	void removeMagicHelmet(int itemId) {
		if (itemId == 20013) {// 마법의 투구: 신속
			if (!_owner.getSkill().isLearnActive(L1SkillId.PHYSICAL_ENCHANT_DEX, false)) {
				_owner.sendPackets(S_AvailableSpellNoti.PHYSICAL_ENCHANT_DEX_OFF);
			}
			if (!_owner.getSkill().isLearnActive(L1SkillId.HASTE, false)) {
				_owner.sendPackets(S_AvailableSpellNoti.HASTE_OFF);
			}
		} else if (itemId == 20014) {// 마법의 투구: 치유
			if (!_owner.getSkill().isLearnActive(L1SkillId.HEAL, false)) {
				_owner.sendPackets(S_AvailableSpellNoti.HEAL_OFF);
			}
			if (!_owner.getSkill().isLearnActive(L1SkillId.EXTRA_HEAL, false)) {
				_owner.sendPackets(S_AvailableSpellNoti.EXTRA_HEAL_OFF);
			}
		} else if (itemId == 20015) {// 마법의 투구: 힘
			if (!_owner.getSkill().isLearnActive(L1SkillId.ENCHANT_WEAPON, false)) {
				_owner.sendPackets(S_AvailableSpellNoti.ENCHANT_WEAPON_OFF);
			}
			if (!_owner.getSkill().isLearnActive(L1SkillId.DETECTION, false)) {
				_owner.sendPackets(S_AvailableSpellNoti.DETECTION_OFF);
			}
			if (!_owner.getSkill().isLearnActive(L1SkillId.PHYSICAL_ENCHANT_STR, false)) {
				_owner.sendPackets(S_AvailableSpellNoti.PHYSICAL_ENCHANT_STR_OFF);
			}
		} else if (itemId == 20008 && !_owner.getSkill().isLearnActive(L1SkillId.HASTE, false)) {// 낡은 바람의 투구
			_owner.sendPackets(S_AvailableSpellNoti.HASTE_OFF);
		} else if (itemId == 20023 && !_owner.getSkill().isLearnActive(L1SkillId.GREATER_HASTE, false)) {// 바람의 투구
			_owner.sendPackets(S_AvailableSpellNoti.GREATER_HASTE_OFF);
		}
	}

	/**
	 * 셋트 아이템 해제
	 * @param itemId
	 */
	public void removeSetItems(int itemId) {
		for (L1ArmorSet armorSet : L1ArmorSet.getAllSet()) {
			if (armorSet.isPartOfSet(itemId) && _currentArmorSet.contains(armorSet) && !armorSet.isValid(_owner)) {
				armorSet.cancelEffect(_owner);
				_currentArmorSet.remove(armorSet);
			}
		}
	}
}
