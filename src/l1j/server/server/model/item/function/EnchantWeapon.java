package l1j.server.server.model.item.function;

import java.util.Random;

import l1j.server.Config;
import l1j.server.common.bin.EnchantScrollTableInfoCommonBinLoader;
import l1j.server.common.bin.enchant.EnchantScrollTableT;
import l1j.server.common.bin.enchant.EnchantScrollTableT.EnchantScrollTypeListT.EnchantScrollT.eTargetCategory;
import l1j.server.common.data.Material;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_EnchantResult;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;

public class EnchantWeapon extends Enchant {
	private static final long serialVersionUID	= 1L;
	private static final Random random			= new Random(System.nanoTime());
	
	public EnchantWeapon(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc			= (L1PcInstance) cha;
			int itemId				= getItemId();
			L1ItemInstance weapon	= pc.getInventory().getItem(packet.readD());
			
			if (weapon == null || weapon.getItem().getItemType() != L1ItemType.WEAPON) {
				pc.sendPackets(L1ServerMessage.sm79); // \f1 아무것도 일어나지 않았습니다.
				return;
			}
			
			int weaponId		= weapon.getItemId();
			int enchantLevel	= weapon.getEnchantLevel();
			
			/** 시간제아이템 러쉬불가 */
			if (!(weaponId >= 52020 && weaponId <= 52029) && weapon.getEndTime() != null) {
				pc.sendPackets(L1ServerMessage.sm79); // \f1 아무것도 일어나지 않았습니다.
				return;
			}
			
			/** 봉인 **/
			if (weapon.getBless() >= 128 && (!(itemId >= 210064 && itemId <= 210067 || (itemId >= 560030 && itemId <= 560033) || itemId == 810003))) {// 봉인템
				pc.sendPackets(L1ServerMessage.sm79); // \f1 아무것도 일어나지 않았습니다.
				return;
			}

			int safeEnchant = weapon.getItem().getSafeEnchant();
			if (safeEnchant < 0) { // 강화 불가
				pc.sendPackets(L1ServerMessage.sm79); // \f1 아무것도 일어나지 않았습니다.
				return;
			}
			
			if (is_max_enchant(weapon)) {
				pc.sendPackets(L1ServerMessage.sm1453);// 더 이상 장비를 강화할 수 없습니다.
				return;
			}
			
			EnchantScrollTableT.EnchantScrollTypeListT.EnchantScrollT scrollT = EnchantScrollTableInfoCommonBinLoader.getScrollT(getItem().getItemNameId());
			boolean is_elemet_scroll = scrollT != null && scrollT.get_target_category() == eTargetCategory.ELEMENT;
			if (scrollT != null && !is_enchant_scroll_enable(pc, scrollT, weapon)) {
				return;
			}
			
			/** 시련의 스크롤 **/
			if (itemId == L1ItemId.SCROLL_OF_ENCHANT_QUEST_WEAPON && !(weaponId >= 246 && weaponId <= 249)) {
				pc.sendPackets(L1ServerMessage.sm79); // \f1 아무것도 일어나지 않았습니다.
				return;
			}
			if ((weaponId >= 246 && weaponId <= 249) && itemId != L1ItemId.SCROLL_OF_ENCHANT_QUEST_WEAPON) {
				pc.sendPackets(L1ServerMessage.sm79); // \f1 아무것도 일어나지 않았습니다.
				return;
			}
			
			/** 기사단의 무기 마법 주문서 **/
			if ((itemId == 60717 || (itemId >= 60719 && itemId <= 60722))
					&& !(weaponId >= 90042 && weaponId <= 90049)) {
				pc.sendPackets(L1ServerMessage.sm79);
				return;
			}
			if (weaponId >= 90042 && weaponId <= 90049) {
				if (!(itemId == 60717 || (itemId >= 60719 && itemId <= 60722))) {
					pc.sendPackets(L1ServerMessage.sm79);
					return;
				}
				if (itemId == 60717 && enchantLevel >= 9) { //9제한
					pc.sendPackets(L1ServerMessage.sm79);
					return;
				}
				pc.getQuest().questItemUse(itemId);
			}
			
			/** 아놀드 무기 마법 주문서 **/
			if (itemId == 30146 && !(weaponId >= 307 && weaponId <= 314)) {
				pc.sendPackets(L1ServerMessage.sm79); // \f1 아무것도 일어나지 않았습니다.
				return;
			}
			if ((weaponId >= 307 && weaponId <= 314) && itemId != 30146) {
				pc.sendPackets(L1ServerMessage.sm79); // \f1 아무것도 일어나지 않았습니다.
				return;
			}

			/** 환상의 무기 마법 주문서 **/
			if (itemId == L1ItemId.SCROLL_OF_ENCHANT_FANTASY_WEAPON && !(weaponId >= 413000 && weaponId <= 413007)) {
				pc.sendPackets(L1ServerMessage.sm79); // \f1 아무것도 일어나지 않았습니다.
				return;
			}
			if ((weaponId >= 413000 && weaponId <= 413007) && itemId != L1ItemId.SCROLL_OF_ENCHANT_FANTASY_WEAPON) {
				pc.sendPackets(L1ServerMessage.sm79); // \f1 아무것도 일어나지 않았습니다.
				return;
			}

			/** 용사의 무기 마법 주문서 **/
			if (itemId == 30068 && !(weaponId >= 1126 && weaponId <= 1133)) {
				pc.sendPackets(L1ServerMessage.sm79); // \f1 아무것도 일어나지 않았습니다.
				return;
			}
			if ((weaponId >= 1126 && weaponId <= 1133) && itemId != 30068) {
				pc.sendPackets(L1ServerMessage.sm79); // \f1 아무것도 일어나지 않았습니다.
				return;
			}

			/** 창천 무기 마법 주문서 **/
			if (itemId == 210085 && !((weaponId >= 231 && weaponId <= 240) || (weaponId >= 510 && weaponId <= 539))) {
				pc.sendPackets(L1ServerMessage.sm79);
				return;
			}
			if (((weaponId >= 231 && weaponId <= 240) || (weaponId >= 510 && weaponId <= 539)) && itemId != 210085) {
				pc.sendPackets(L1ServerMessage.sm79);
				return;
			}
			
			/** 영웅의 무기 마법 주문서 속성 무기 강화 주문서**/
			if ((itemId >= 52000 && itemId <= 52004) && !(weaponId >= 52020 && weaponId <= 52029)) {
				pc.sendPackets(L1ServerMessage.sm79); // \f1 아무것도 일어나지 않았습니다.
				return;
			}
			if ((weaponId >= 52020 && weaponId <= 52029) && !(itemId >= 52000 && itemId <= 52004)) {
				pc.sendPackets(L1ServerMessage.sm79); // \f1 아무것도 일어나지 않았습니다.
				return;
			}

			/** 상아탑의 무기 마법 주문서 **/
			if (itemId == L1ItemId.SURYUNJA_WEAPON_SCROLL
					&& !(weaponId == 7 || weaponId == 35 || weaponId == 48 || weaponId == 73 || weaponId == 105 || weaponId == 120
						|| weaponId == 147 || weaponId == 156 || weaponId == 174 || weaponId == 175 || weaponId == 224
						|| weaponId == 203012 || weaponId == 203000 || weaponId == 203001 || (weaponId >= 40105 && weaponId <= 40109))) {
				pc.sendPackets(L1ServerMessage.sm79);
				return;
			}
			if (weaponId == 7 || weaponId == 35 || weaponId == 48 || weaponId == 73 || weaponId == 105 || weaponId == 120
					|| weaponId == 147 || weaponId == 156 || weaponId == 174 || weaponId == 175 || weaponId == 224
					|| weaponId == 203012 || weaponId == 203000 || weaponId == 203001 || (weaponId >= 40105 && weaponId <= 40109)) {
				if (itemId != L1ItemId.SURYUNJA_WEAPON_SCROLL) {
					pc.sendPackets(L1ServerMessage.sm79);
					return;
				}
				if (enchantLevel >= 6) {
					pc.sendPackets(L1ServerMessage.sm79);
					return;
				}
			}

			if (enchantLevel >= Config.ENCHANT.WEAPON_LIMIT 
					&& !(is_elemet_scroll || itemId >= 560030 && itemId <= 560033)) { // 인챈트 제한
				pc.sendPackets(L1ServerMessage.sm1453);
				return;
			}

			if (safeEnchant == 0) {
				if (enchantLevel >= Config.ENCHANT.ADVANCED_WEAPON_LIMIT 
						&& !(is_elemet_scroll || itemId >= 560030 && itemId <= 560033)) {
					if (itemId == L1ItemId.C_SCROLL_OF_ENCHANT_WEAPON) { // c-dai
						pc.getInventory().removeItem(this, 1);
						successEnchant(pc, weapon, -1);
					} else {
						pc.sendPackets(L1ServerMessage.sm1453);
					}
					return;
				}
			}
			if (itemId == L1ItemId.C_SCROLL_OF_ENCHANT_WEAPON) { // c-dai
				pc.getInventory().removeItem(this, 1);
				int rnd = random.nextInt(100) + 1;
				if (safeEnchant == 0 && rnd <= 30) {
					failureEnchant(pc, weapon);
					return;
				}
				if (enchantLevel < -6) {// -7이상은 할 수 없다.
					failureEnchant(pc, weapon);
				} else {
					successEnchant(pc, weapon, -1);
				}
			} 
			/** 속성 강화 주문서 **/
			else if (is_elemet_scroll) {
				elementalEnchant(pc, weapon, scrollT.get_elementalType());
			}
			/** 속성 변환 주문서 **/
			else if (itemId >= 560030 && itemId <= 560033) {
				elementalChange(pc, weapon, itemId);
			}
			/** 고대의 서: 무기 100% **/
			else if (itemId == 68076) {
				if (enchantLevel >= Config.ENCHANT.WEAPON_LIMIT) { // 사용최대 인챈수치
					pc.sendPackets(L1ServerMessage.sm1453);
					return;
				}
				pc.getInventory().removeItem(this, 1);
				successEnchant(pc, weapon, +1);
				pc.sendPackets(L1SystemMessage.ENCHANT_PLUS);
			} 
			/** 고대의 서: 무기 확률 **/
			else if (itemId == 68078 || itemId == 68082) {
				if (enchantLevel >= Config.ENCHANT.WEAPON_LIMIT) { // 사용최대 인챈수치
					pc.sendPackets(L1ServerMessage.sm1453);
					return;
				}
				pc.getInventory().removeItem(this, 1);
				int rnd = random.nextInt(100);
				if (rnd <= Config.ENCHANT.ANCIENT_WEAPON_LIMIT) { // +1 될확율 5%
					successEnchant(pc, weapon, +1);
					pc.sendPackets(L1SystemMessage.ENCHANT_PLUS);
				} else {
					pc.sendPackets(new S_ServerMessage(4056, weapon.getLogNameRef()), true);// 인챈트: %0%s 소멸의 굴레를 극복 하였습니다.
				}
			} 
			/** 고대의 서 **/
			else if (itemId == 31121) {
				if (enchantLevel >= Config.ENCHANT.WEAPON_LIMIT) {
					pc.sendPackets(L1ServerMessage.sm1453);
					return;
				}
				pc.getInventory().removeItem(this, 1);
				int rnd = random.nextInt(100);
				if (rnd <= 15) { // -1 될 확율 15%
					successEnchant(pc, weapon, -1);
					pc.sendPackets(L1SystemMessage.ENCHANT_MINUS);
				} else if (rnd >= 16 && rnd <= 36) { // +1 될확율 5%
					successEnchant(pc, weapon, +1);
					pc.sendPackets(L1SystemMessage.ENCHANT_PLUS);
				} else {
					pc.sendPackets(new S_ServerMessage(4056, weapon.getLogNameRef()), true);// 인챈트: %0%s 소멸의 굴레를 극복 하였습니다.
				}
			} 
			
			/** 영웅의 무기 마법 주문서 **/
			else if (itemId == 52000) {
				if (weaponId >= 52020 && weaponId <= 52029) {
					if (enchantLevel >= 9) {
						pc.sendPackets(L1ServerMessage.sm1453);
						return;
					}
					pc.getInventory().removeItem(this, 1);
					int rnd = random.nextInt(100) + 1;
					if (rnd <= 5) { // +1 될확율 5%
						successEnchant(pc, weapon, + 1);
						pc.sendPackets(L1SystemMessage.ENCHANT_PLUS);
					} else {
						pc.sendPackets(new S_ServerMessage(4056, weapon.getLogNameRef()), true);// 인챈트: %0%s 소멸의 굴레를 극복 하였습니다.
					}
				} else {
					pc.sendPackets(L1ServerMessage.sm79);
				}
			}
			
			/** 장인의 무기 마법 주문서 **/
			else if (itemId == 810003) {
				if (!Material.isNotSafeEnchantMaterial(weapon.getItem().getMaterial())) {
					if (enchantLevel == 9) {
						pc.getInventory().removeItem(this, 1);
						int rnd = random.nextInt(100) + 1;
						if (rnd <= Config.ENCHANT.EXPERT_WEAPON_LIMIT) {
							successEnchant(pc, weapon, 1);
						} else {
							if (pc.getConfig()._enchantGfx) {
								pc.sendPackets(new S_EnchantResult(weapon, this, S_EnchantResult.EnchantResult.FAIL_REMAIN, 0, 0), true);	//	인첸트연출
							}
							pc.sendPackets(L1ServerMessage.sm1310); // 인챈트: 강렬하게 빛났지만 아무 일도 없었습니다.
						}
					} else {
						pc.sendPackets(L1ServerMessage.sm79); // 인챈트 +9 무기만 사용 가능
					}
				} else {
					pc.sendPackets(L1ServerMessage.sm1294); // 인챈트: 해당 강화 주문서 사용 불가
				}
			} else if (enchantLevel < safeEnchant) {
				pc.getInventory().removeItem(this, 1);
				successEnchant(pc, weapon, getRandomEnchantValue(weapon, itemId));
			} else {
				pc.getInventory().removeItem(this, 1);
				int rnd = random.nextInt(1000) + 1;
				int prob = (int)getProb(safeEnchant, enchantLevel);
				if (pc.isGm()) {
					//pc.sendPackets(new S_SystemMessage(String.format("\\fY확률[ %d ], 찬스[ %d ]", prob, rnd)), true);
					pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(94), String.valueOf(prob), String.valueOf(rnd)), true);
				}
				if (rnd < prob) {
					int randomEnchantLevel = getRandomEnchantValue(weapon, itemId);
					successEnchant(pc, weapon, randomEnchantLevel);
				} else {
					failureEnchant(pc, weapon);
				}
			}
		}
	}
	
	/**
	 * 인챈트 확률
	 * @param safeEnchant
	 * @param enchantLevel
	 * @return prob
	 */
	double getProb(int safeEnchant, int enchantLevel) {
		if (safeEnchant != 0) {
			return getProbDefault(safeEnchant, enchantLevel);
		}
		return getProbSafeEnchantZero(safeEnchant, enchantLevel);
	}
	
	/**
	 * 일반 확률
	 * @param safeEnchant
	 * @param enchantLevel
	 * @return prob
	 */
	double getProbDefault(int safeEnchant, int enchantLevel) {
		if (enchantLevel >= 9) {
			return Config.ENCHANT.WEAPON_ENCHANT_PROBABILITY_9 * 10;
		}
		if (enchantLevel >= 8) {
			return Config.ENCHANT.WEAPON_ENCHANT_PROBABILITY_8 * 10;
		}
		if (enchantLevel >= 7) {
			return Config.ENCHANT.WEAPON_ENCHANT_PROBABILITY_7 * 10;
		}
		if (enchantLevel >= 6) {
			return Config.ENCHANT.WEAPON_ENCHANT_PROBABILITY_6 * 10;
		}
		return (90 / ((enchantLevel - safeEnchant + 1) << 1) / (enchantLevel / 9 != 0 ? 2 : 1) + Config.ENCHANT.ENCHANT_CHANCE_WEAPON_DEFAULT) * 10;
	}
	
	/**
	 * 안전 인챈트가 0인 아이템
	 * @param safeEnchant
	 * @param enchantLevel
	 * @return prob
	 */
	double getProbSafeEnchantZero(int safeEnchant, int enchantLevel) {
		if (enchantLevel >= 9) {
			return Config.ENCHANT.WEAPON_SAFETY_ENCHANT_9 * 10;
		}
		if (enchantLevel >= 8) {
			return Config.ENCHANT.WEAPON_SAFETY_ENCHANT_8 * 10;
		}
		if (enchantLevel >= 7) {
			return Config.ENCHANT.WEAPON_SAFETY_ENCHANT_7 * 10;
		}
		if (enchantLevel >= 6) {
			return Config.ENCHANT.WEAPON_SAFETY_ENCHANT_6 * 10;
		}
		if (enchantLevel >= 5) {
			return Config.ENCHANT.WEAPON_SAFETY_ENCHANT_5 * 10;
		}
		if (enchantLevel >= 4) {
			return Config.ENCHANT.WEAPON_SAFETY_ENCHANT_4 * 10;
		}
		if (enchantLevel >= 3) {
			return Config.ENCHANT.WEAPON_SAFETY_ENCHANT_3 * 10;
		}
		if (enchantLevel >= 2) {
			return Config.ENCHANT.WEAPON_SAFETY_ENCHANT_2 * 10;
		}
		if (enchantLevel >= 1) {
			return Config.ENCHANT.WEAPON_SAFETY_ENCHANT_1 * 10;
		}
		if (enchantLevel >= 0) {
			return Config.ENCHANT.WEAPON_SAFETY_ENCHANT_0 * 10;
		}
		return (90 / ((enchantLevel - safeEnchant + 1) << 1) / (enchantLevel / 9 != 0 ? 2 : 1) + Config.ENCHANT.ENCHANT_CHANCE_WEAPON_DEFAULT) * 10;
	}
	

}


