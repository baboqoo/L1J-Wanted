package l1j.server.server.model.item.function;

import java.util.Random;

import l1j.server.Config;
import l1j.server.common.bin.EnchantScrollTableInfoCommonBinLoader;
import l1j.server.common.bin.enchant.EnchantScrollTableT;
import l1j.server.common.data.Material;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.item.L1ItemArmorType;
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
import l1j.server.server.templates.L1Armor;
import l1j.server.server.templates.L1Item;

public class EnchantArmor extends Enchant {
	private static final long serialVersionUID	= 1L;
	private static final Random random			= new Random(System.nanoTime());
	
	public EnchantArmor(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc			= (L1PcInstance) cha;
			int itemId				= getItemId();
			L1ItemInstance armor	= pc.getInventory().getItem(packet.readD());
					
			// CHECKS TO AVOID THE ENCHANT IF IS NECESARY
			
			if (armor == null || armor.getItem().getItemType() != L1ItemType.ARMOR) {
				pc.sendPackets(L1ServerMessage.sm79);// 아무것도 일어나지 않았습니다.
				return;
			}
			if (armor.isSlot()) {
				pc.sendPackets(L1ServerMessage.sm9082);// 제련석이 장착된 상태에서는 아이템 강화를 할 수 없습니다.
				return;
			}
			
			int armorId					= armor.getItem().getItemId();
			int armor_type				= armor.getItem().getType();
			L1ItemArmorType armorType	= L1ItemArmorType.fromInt(armor_type);
			int enchantLevel			= armor.getEnchantLevel();
			
			/** 봉인 **/				
			if (armor.getBless() >= 128 && !(itemId >= 810012 && itemId <= 810013)) {
				pc.sendPackets(L1ServerMessage.sm79);// 아무것도 일어나지 않았습니다.
				return;
			}
			
			/** 시간제아이템 러쉬불가 */
			if (armor.getEndTime() != null && !(armorId == 22264 || armorId == 2000031 || armorId == 900050 || armorId == 900051)) {
				pc.sendPackets(L1ServerMessage.sm79); // 아무것도 일어나지 않았습니다.
				return;
			}
			int safeEnchant = ((L1Armor) armor.getItem()).getSafeEnchant();
			if (safeEnchant < 0) { // 강화 불가
				pc.sendPackets(L1ServerMessage.sm79); // 아무것도 일어나지 않았습니다.
				return;
			}
			
			if (is_max_enchant(armor)) {
				pc.sendPackets(L1ServerMessage.sm1453);// 더 이상 장비를 강화할 수 없습니다.
				return;
			}
			
			EnchantScrollTableT.EnchantScrollTypeListT.EnchantScrollT scrollT = EnchantScrollTableInfoCommonBinLoader.getScrollT(getItem().getItemNameId());
			if (scrollT != null && !is_enchant_scroll_enable(pc, scrollT, armor)) {
				return;
			}
			
			/** 기사단의 갑옷 마법 주문서 **/
			if (itemId == 60718 && !(armorId >= 90034 && armorId <= 90041)) {
				pc.sendPackets(L1ServerMessage.sm79);
				return;
			}
			if (armorId >= 90034 && armorId <= 90041) {
				if (itemId != 60718) {
					pc.sendPackets(L1ServerMessage.sm79);
					return;
				}
				if (enchantLevel >= 7) {
					pc.sendPackets(L1ServerMessage.sm79);
					return;
				}
				pc.getQuest().questItemUse(itemId);
			}
			
			/** 아놀드 갑옷 마법 주문서 **/
			if (itemId == 30147 && armorId != 21095) {
				pc.sendPackets(L1ServerMessage.sm79);
				return;
			}
			if (armorId == 21095 && itemId != 30147) {
				pc.sendPackets(L1ServerMessage.sm79);
				return;
			}

			/** 인나드릴 티셔츠 갑옷 마법 주문서 **/
			if ((itemId >= 410066 && itemId <= 410068) && !(armorId >= 22215 && armorId <= 22223 || armorId >= 490000 && armorId <= 490008)) {
				pc.sendPackets(L1ServerMessage.sm79);
				return;
			}
			if ((armorId >= 22215 && armorId <= 22223 || armorId >= 490000 && armorId <= 490008) && !(itemId >= 410066 && itemId <= 410068)) {
				pc.sendPackets(L1ServerMessage.sm79);
				return;
			}

			/** 환상의 갑옷 마법 주문서 **/
			if (itemId == L1ItemId.SCROLL_OF_ENCHANT_FANTASY_ARMOR && !(armorId >= 423000 && armorId <= 423008)) {
				pc.sendPackets(L1ServerMessage.sm79);
				return;
			}
			if ((armorId >= 423000 && armorId <= 423008) && itemId != L1ItemId.SCROLL_OF_ENCHANT_FANTASY_ARMOR) {
				pc.sendPackets(L1ServerMessage.sm79);
				return;
			}
			
			/** 용사의 갑옷 마법 주문서 **/
			if (itemId == 30069 && !(armorId >= 22328 && armorId <= 22335)) {
				pc.sendPackets(L1ServerMessage.sm79); // \f1 아무것도 일어나지 않았습니다.
				return;
			}
			if ((armorId >= 22328 && armorId <= 22335) && itemId != 30069) {
				pc.sendPackets(L1ServerMessage.sm79); // \f1 아무것도 일어나지 않았습니다.
				return;
			}

			/** 문장강화석 **/
			if ((itemId == 3000100 || itemId == 68081 || itemId == 31100) && armorType != L1ItemArmorType.SENTENCE) {
				pc.sendPackets(L1ServerMessage.sm79);
				return;
			}
			if (armorType == L1ItemArmorType.SENTENCE && !(itemId == 3000100 || itemId == 68081 || itemId == 31100)) {
				pc.sendPackets(L1ServerMessage.sm79);
				return;
			}
			
			/** 휘장 강화 주문서 **/
			if ((itemId == 31129 || itemId == 31130 || itemId == 31101) && armorType != L1ItemArmorType.BADGE) {
				pc.sendPackets(L1ServerMessage.sm79);
				return;
			}
			if (armorType == L1ItemArmorType.BADGE && !(itemId == 31129 || itemId == 31130 || itemId == 31101)) {
				pc.sendPackets(L1ServerMessage.sm79);
				return;
			}
			
			/** 룸티스의 펜턴트 강화 주문서 **/
			if ((itemId == 900070 || itemId == 31139) && armorType != L1ItemArmorType.PENDANT) {
				pc.sendPackets(L1ServerMessage.sm79);
				return;
			}
			if (armorType == L1ItemArmorType.PENDANT && !(itemId == 900070 || itemId == 31139)) {
				pc.sendPackets(L1ServerMessage.sm79);
				return;
			}

			/** 창천의 갑옷 마법 주문서 **/
			if (itemId == 210084 && !(armorId >= 22034 && armorId <= 22064)) {
				pc.sendPackets(L1ServerMessage.sm79);
				return;
			}
			if ((armorId >= 22034 && armorId <= 22064) && itemId != 210084) {
				pc.sendPackets(L1ServerMessage.sm79);
				return;
			}

			/** 용의티셔츠 강화 주문서 **/
			if (((itemId >= 3000123 && itemId <= 3000125) || itemId == 31138)
					&& !(armorId >= 900023 && armorId <= 900026 || armorId >= 1900027 && armorId <= 1900030)) {
				pc.sendPackets(L1ServerMessage.sm79);
				return;
			}
			if ((armorId >= 900023 && armorId <= 900026 || armorId >= 1900027 && armorId <= 1900030)
					&& !((itemId >= 3000123 && itemId <= 3000125) || itemId == 31138)) {
				pc.sendPackets(L1ServerMessage.sm79);
				return;
			}

			/** 장신구 강화 주문서 */
			if ((itemId == 210068 || itemId == 43050 || itemId == L1ItemId.SNAPER_SCROLL || itemId == L1ItemId.ROOMTIS_SCROLL || itemId == 810012
					|| itemId == 810013 || itemId == 31103 || itemId == 31104 
					|| itemId == 31135 || itemId == 31136
					|| itemId == L1ItemId.SURYUNJA_ACCESARY_SCROLL)
					&& !(armorType == L1ItemArmorType.AMULET
					|| L1ItemArmorType.isRing(armor_type)
					|| armorType == L1ItemArmorType.BELT
					|| armorType == L1ItemArmorType.EARRING)) {
				pc.sendPackets(L1ServerMessage.sm79);
				return;
			}
			if ((armorType == L1ItemArmorType.AMULET 
					|| L1ItemArmorType.isRing(armor_type)
					|| armorType == L1ItemArmorType.BELT
					|| armorType == L1ItemArmorType.EARRING) 
					&& !(itemId == 210068 || itemId == 43050 || itemId == L1ItemId.SNAPER_SCROLL || itemId == L1ItemId.ROOMTIS_SCROLL || itemId == 810012
							|| itemId == 810013 || itemId == 31103 || itemId == 31104 
							|| itemId == 31135 || itemId == 31136
							|| itemId == L1ItemId.SURYUNJA_ACCESARY_SCROLL)) {
				pc.sendPackets(L1ServerMessage.sm79);
				return;
			}
			
			/** 룸티스 강화 주문서 **/
			if ((itemId == L1ItemId.ROOMTIS_SCROLL || itemId == 31104 || itemId == 31136)
					&& !(armorId >= 22229 && armorId <= 22231 || armorId >= 222337 && armorId <= 222339 || armorId == 222340 || armorId == 222341)) {
				pc.sendPackets(L1ServerMessage.sm79);
				return;
			}
			if ((armorId >= 22229 && armorId <= 22231 || armorId >= 222337 && armorId <= 222339 || armorId == 222340 || armorId == 222341)
					&& !(itemId == L1ItemId.ROOMTIS_SCROLL || itemId == 31104 || itemId == 31136)) {
				pc.sendPackets(L1ServerMessage.sm79);
				return;
			}
			
			/** 스냅퍼의 반지 강화 주문서 **/
			if ((itemId == L1ItemId.SNAPER_SCROLL || itemId == 31103 || itemId == 31135)
					&& !(armorId >= 22224 && armorId <= 22228 || armorId == 222290 || armorId == 222291 || armorId >= 222330 && armorId <= 222336)) {
				pc.sendPackets(L1ServerMessage.sm79);
				return;
			}
			if ((armorId >= 22224 && armorId <= 22228 || armorId == 222290 || armorId == 222291 || armorId >= 222330 && armorId <= 222336)
					&& !(itemId == L1ItemId.SNAPER_SCROLL || itemId == 31103 || itemId == 31135)) {
				pc.sendPackets(L1ServerMessage.sm79);
				return;
			}

			/** 상아탑의 갑옷 마법 주문서 **/
			if (itemId == L1ItemId.SURYUNJA_ARMOR_SCROLL
					&& !(armorId == 20028 || armorId == 20126 || armorId == 20173 || armorId == 20206 
							|| armorId == 20206 || armorId == 20232 || armorId >= 22300 && armorId <= 22312
							|| armorId == 321515)) {
				pc.sendPackets(L1ServerMessage.sm79);
				return;
			}
			if (armorId == 20028 || armorId == 20126 || armorId == 20173 || armorId == 20206 
					|| armorId == 20206 || armorId == 20232 || armorId >= 22300 && armorId <= 22312
					|| armorId == 321515) {
				if (itemId != L1ItemId.SURYUNJA_ARMOR_SCROLL) {
					pc.sendPackets(L1ServerMessage.sm79);
					return;
				}
				if (armorId >= 22300 && armorId <= 22311) {
					if (enchantLevel >= 6) {
						pc.sendPackets(L1ServerMessage.sm79);
						return;
					} 
				} else {
					if (enchantLevel >= 4) {
						pc.sendPackets(L1ServerMessage.sm79);
						return;
					} 
				}
			}
			
			/** 수련자의 장신구 마법 주문서 */
			if (itemId == L1ItemId.SURYUNJA_ACCESARY_SCROLL
					&& !(armorId == 20282 || armorId == 22073 || armorId == 22337 || armorId == 22338 || armorId == 22339)) {
				pc.sendPackets(L1ServerMessage.sm79);
				return;
			}
			if (armorId == 20282 || armorId == 22073 || armorId == 22337 || armorId == 22338 || armorId == 22339) {
				if (itemId != L1ItemId.SURYUNJA_ACCESARY_SCROLL) {
					pc.sendPackets(L1ServerMessage.sm79);
					return;
				}
				if (enchantLevel >= 3) {
					pc.sendPackets(L1ServerMessage.sm79);
					return;
				}
			}

			// 인첸트 제한
			if ((armorId >= 22229 && armorId <= 22231 || armorId >= 222337 && armorId <= 222339 || armorId == 222340 || armorId == 222341) && enchantLevel >= Config.ENCHANT.RUMTIS_LIMIT) {
				pc.sendPackets(L1ServerMessage.sm1453);
				return;
			}
			if ((armorId >= 22224 && armorId <= 22228 || armorId == 222290 || armorId == 222291 || armorId >= 222330 && armorId <= 222336) && enchantLevel >= Config.ENCHANT.SNAPPER_LIMIT) {
				pc.sendPackets(L1ServerMessage.sm1453);
				return;
			}
			if ((armorType == L1ItemArmorType.AMULET 
					|| L1ItemArmorType.isRing(armor_type)
					|| armorType == L1ItemArmorType.BELT
					|| armorType == L1ItemArmorType.EARRING) 
					&& enchantLevel >= Config.ENCHANT.ACCESORY_LIMIT) {
				pc.sendPackets(L1ServerMessage.sm1453);
				return;
			}
			if (armorType == L1ItemArmorType.GARDER && enchantLevel >= Config.ENCHANT.GARDER_LIMIT) {
				pc.sendPackets(L1ServerMessage.sm1453);
				return;
			}
			if (armorType == L1ItemArmorType.SENTENCE && enchantLevel >= Config.ENCHANT.SENTENCE_LIMIT) {
				pc.sendPackets(L1ServerMessage.sm1453);
				return;
			}
			if (armorType == L1ItemArmorType.BADGE && enchantLevel >= Config.ENCHANT.INSIGNIA_LIMIT) {
				pc.sendPackets(L1ServerMessage.sm1453);
				return;
			}
			if (armorType == L1ItemArmorType.PENDANT && enchantLevel >= Config.ENCHANT.PENDANT_LIMIT) {
				pc.sendPackets(L1ServerMessage.sm1453);
				return;
			}
			if (enchantLevel >= Config.ENCHANT.ARMOR_ENCHANT_LIMIT) {
				pc.sendPackets(L1ServerMessage.sm1453);
				return;
			}

			// ***END*** CHECKS TO AVOID THE ENCHANT IF IS NECESARY ***END***
			
			// NOT Accesories
			if (!L1ItemArmorType.isAccessary(armor_type) && safeEnchant == 0 && enchantLevel >= Config.ENCHANT.ADVANCED_ARMOR_ENCHANT_LIMIT) {
				if (itemId == L1ItemId.C_SCROLL_OF_ENCHANT_ARMOR) {// c-dai
					pc.getInventory().removeItem(this, 1);
					successEnchant(pc, armor, -1);
				} else {
					pc.sendPackets(L1ServerMessage.sm1453);
				}
				return;
			}

			/** C-ZEL **/
			if (itemId == L1ItemId.C_SCROLL_OF_ENCHANT_ARMOR || itemId == L1ItemId.INADRIL_T_SCROLL_C || itemId == 3000125 || itemId == 3000132) {// 저주 갑옷 마법 주문서류
				pc.getInventory().removeItem(this, 1);
				int rnd = random.nextInt(100) + 1;
				if (safeEnchant == 0 && rnd <= 30) {
					failureEnchant(pc, armor);
					return;
				} 
				if (enchantLevel < -1) {// 기본적인템들은 -2에서 저젤 바를시증발
					failureEnchant(pc, armor);
				} else {
					successEnchant(pc, armor, -1);
				}
			}
			/** Ancient Tome: Armor **/
			else if (itemId == 68079 || itemId == 68083) {
				pc.getInventory().removeItem(this, 1);
				int rnd = random.nextInt(100);
				if (rnd <= Config.ENCHANT.ANCIENT_ARMOR_LIMIT) { // +1 될확율 10%
					successEnchant(pc, armor, +1);
					pc.sendPackets(L1SystemMessage.ENCHANT_PLUS);
				} else {
					pc.sendPackets(new S_ServerMessage(4056, armor.getLogNameRef()), true);// 인챈트: %0%s 소멸의 굴레를 극복 하였습니다.
				}
			} 
			/** Ancient Book **/
			else if (itemId == 31122) {
				pc.getInventory().removeItem(this, 1);
				int rnd = random.nextInt(100);
				if (rnd <= 15) { // -1 될 확율 15%
					successEnchant(pc, armor, -1);
					pc.sendPackets(L1SystemMessage.ENCHANT_MINUS);
				} else if (rnd <= 36) { // +1 될확율 10%
					successEnchant(pc, armor, +1);
					pc.sendPackets(L1SystemMessage.ENCHANT_PLUS);
				} else {
					pc.sendPackets(new S_ServerMessage(4056, armor.getLogNameRef()), true);// 인챈트: %0%s 소멸의 굴레를 극복 하였습니다.
				}
			}
			/** Enchat scroll 100% **/
			else if (itemId == 31103 || itemId == 31104 || itemId == 68081 || itemId == 31130 || itemId == 68077) {
				pc.getInventory().removeItem(this, 1);
				successEnchant(pc, armor, +1);
				pc.sendPackets(L1SystemMessage.ENCHANT_PLUS);
			} 
			/** Protection Order **/
			else if (itemId == 31135 || itemId == 31136 || itemId == 31100 || itemId == 31101 || itemId == 31139) {
				if (enchantLevel >= Config.ENCHANT.ENCHANT_SCROLL_LIMIT) {
					pc.sendPackets(L1ServerMessage.sm1453);
					return;
				}
				pc.getInventory().removeItem(this, 1);
				int prob = (int)getProbSafePaper(enchantLevel);
				
				/** 축복 / 합성문장 / 합성휘장 확률 반감 **/
				if (armor.getItem().getBless() == 0
						|| armorId == 900108 || armorId == 900109 || armorId == 900110
						|| armorId == 900045 || armorId == 900046 || armorId == 900047) {
					prob >>= 1;
				}
				if (random.nextInt(1000) + 1 <= prob) {
					successEnchant(pc, armor, +1);
				} else {
					if (pc.getConfig()._enchantGfx) {
						pc.sendPackets(new S_EnchantResult(armor, this, S_EnchantResult.EnchantResult.FAIL_REMAIN, 0, 0), true);	//	인첸트연출
					}
					pc.sendPackets(new S_ServerMessage(4056, armor.getLogNameRef()), true);
				}
			}
			/** Dragon T-shirt Protection Order **/
			else if (itemId == 31138) {
				if (enchantLevel < safeEnchant || enchantLevel >= Config.ENCHANT.DRAGON_TSHIRT_ENCHANT_SCROLL_LIMIT) {
					pc.sendPackets(L1ServerMessage.sm1453);
					return;
				}
				pc.getInventory().removeItem(this, 1);
				int prob = (int)getProbSafePaperFromTshirts(enchantLevel);
				
				// 축복 받은 용의 티셔츠 확률 반감
				if (armor.getItem().getBless() == 0) {
					prob >>= 1;
				}
				if (random.nextInt(1000) + 1 <= prob) {
					successEnchant(pc, armor, +1);
				} else {
					if (pc.getConfig()._enchantGfx) {
						pc.sendPackets(new S_EnchantResult(armor, this, S_EnchantResult.EnchantResult.FAIL_REMAIN, 0, 0), true);	//	인첸트연출
					}
					pc.sendPackets(new S_ServerMessage(4056, armor.getLogNameRef()), true);
				}
			}
			/** Artisan's Armor Magic Scroll **/
			else if (itemId == 850003) {
				if (!Material.isNotSafeEnchantMaterial(armor.getItem().getMaterial())) {
					if (enchantLevel == 9) {
						pc.getInventory().removeItem(this, 1);
						int rnd = random.nextInt(100);
						if (rnd <= Config.ENCHANT.EXPERT_ARMOR_LIMIT) {
							successEnchant(pc, armor, 1);
						} else {
							if (pc.getConfig()._enchantGfx) {
								pc.sendPackets(new S_EnchantResult(armor, this, S_EnchantResult.EnchantResult.FAIL_REMAIN, 0, 0), true);	//	인첸트연출
							}
							pc.sendPackets(L1ServerMessage.sm1310);// 인챈트: 강렬하게 빛났지만 아무 일도 없었습니다.
						}
					} else {
						pc.sendPackets(L1ServerMessage.sm79);// 인챈트 +9 무기만 사용 가능
					}
				} else {
					pc.sendPackets(L1ServerMessage.sm1294);// 인챈트: 해당 강화 주문서 사용 불가
				}
			}
			/** Enchant an item that is still below its safety enchantment level **/
			else if (enchantLevel < safeEnchant) {
				pc.getInventory().removeItem(this, 1);
				successEnchant(pc, armor, getRandomEnchantValue(armor, itemId));
			}
			/** Normal Enchant (NZEL / NDAI) **/
			else {
				int rnd = random.nextInt(1000) + 1;
				int enchantLevelTemp = safeEnchant == 0 ? 2 : 1;// 뼈, 브락크미스릴용 보정
				int prob = (int)getProb(armor_type, safeEnchant, enchantLevel, enchantLevelTemp);
				pc.getInventory().removeItem(this, 1);
				if (pc.isGm()) {
					//pc.sendPackets(new S_SystemMessage(String.format("\\aD확률[ %d ], 찬스[ %d ]", prob, rnd)), true);
					pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(93), String.valueOf(prob), String.valueOf(rnd)), true);
				}

				if (rnd < prob){
					int randomEnchantLevel = enchantLevel >= 0 &&  safeEnchant == 0 ? 1 : getRandomEnchantValue(armor, itemId);//안전인첸 0인 방어구 무조건 1씩 뜨도록
					successEnchant(pc, armor, randomEnchantLevel);
				} else if (enchantLevel >= 9 && rnd < (prob << 1)) {
					pc.sendPackets(new S_ServerMessage(160, armor.getLogNameRef(), "$245", "$248"), true);// 강렬하게 빛났지만 아무일도 잃어나지 않앗습니다.
				} else if (itemId == 810012) { // Orim's Accessory Magic Spellbook
					if (enchantLevel <= -2) {
						return;
					}
					pc.sendPackets(new S_ServerMessage(4056, armor.getLogNameRef()), true);// 인챈트: %0%s 소멸의 굴레를 극복 하였습니다.
					if (random.nextBoolean()) {
						if (pc.getConfig()._enchantGfx) {
							pc.sendPackets(new S_EnchantResult(armor, this, S_EnchantResult.EnchantResult.FAIL_REMAIN, 0, 0), true);	//	인첸트연출
						}
					} else {
						successEnchant(pc, armor, -1);
					}
				} else if (itemId == 810013) {// Blessed Orim's Accessory Magic Spellbook
					if (pc.getConfig()._enchantGfx) {
						pc.sendPackets(new S_EnchantResult(armor, this, S_EnchantResult.EnchantResult.FAIL_REMAIN, 0, 0), true);	//	인첸트연출
					}
					pc.sendPackets(new S_ServerMessage(4056, armor.getLogNameRef()), true);// 인챈트: %0%s 소멸의 굴레를 극복 하였습니다.						
				} else {
					failureEnchant(pc, armor);
				}
			}
		}
	}
	
	/**
	 * 인챈트 확률
	 * @param armorType
	 * @param safeEnchant
	 * @param enchantLevel
	 * @param enchantLevelTemp
	 * @return prob
	 */
	double getProb(int armorType, int safeEnchant, int enchantLevel, int enchantLevelTemp) {
		if (L1ItemArmorType.isAccessary(armorType)) {// 반지, 벨트, 귀걸이, 목걸이, 문장, 휘장, 펜던트
			return getProbAccessory(enchantLevel, enchantLevelTemp);
		}
		if (safeEnchant != 0) {
			return getProbDefault(safeEnchant, enchantLevel, enchantLevelTemp);
		}
		return getProbSafeEnchantZero(safeEnchant, enchantLevel, enchantLevelTemp);
	}
	
	/**
	 * 일반 확률
	 * @param safeEnchant
	 * @param enchantLevel
	 * @param enchantLevelTemp
	 * @return prob
	 */
	double getProbDefault(int safeEnchant, int enchantLevel, int enchantLevelTemp) {
		if (enchantLevel >= 9) {
			return Config.ENCHANT.ARMOR_ENCHANT_PROBABILITY_9 * 10;
		}
		if (enchantLevel >= 8) {
			return Config.ENCHANT.ARMOR_ENCHANT_PROBABILITY_8 * 10;
		}
		if (enchantLevel >= 7) {
			return Config.ENCHANT.ARMOR_ENCHANT_PROBABILITY_7 * 10;
		}
		if (enchantLevel >= 6) {
			return Config.ENCHANT.ARMOR_ENCHANT_PROBABILITY_6 * 10;
		}
		if (enchantLevel >= 5) {
			return Config.ENCHANT.ARMOR_ENCHANT_PROBABILITY_5 * 10;
		}
		if (enchantLevel >= 4) {
			return Config.ENCHANT.ARMOR_ENCHANT_PROBABILITY_4 * 10;
		}
		return (90 / ((enchantLevel - safeEnchant + 1) << 1) / (enchantLevel / 7 != 0 ? 2 : 1) / enchantLevelTemp + Config.ENCHANT.ENCHANT_CHANCE_ARMOR_DEFAULT) * 10;
	}
	
	/**
	 * 악세서리 확률
	 * @param enchantLevel
	 * @param enchantLevelTemp
	 * @return prob
	 */
	double getProbAccessory(int enchantLevel, int enchantLevelTemp) {
		if (enchantLevel >= 9) {
			return Config.ENCHANT.ACCESORY_ENCHANT_PROBABILITY9 * 10;
		}
		if (enchantLevel >= 8) {
			return Config.ENCHANT.ACCESORY_ENCHANT_PROBABILITY8 * 10;
		}
		if (enchantLevel >= 7) {
			return Config.ENCHANT.ACCESORY_ENCHANT_PROBABILITY7 * 10;
		}
		if (enchantLevel >= 6) {
			return Config.ENCHANT.ACCESORY_ENCHANT_PROBABILITY6 * 10;
		}
		if (enchantLevel >= 5) {
			return Config.ENCHANT.ACCESORY_ENCHANT_PROBABILITY5 * 10;
		}
		if (enchantLevel >= 4) {
			return Config.ENCHANT.ACCESORY_ENCHANT_PROBABILITY4 * 10;
		}
		if (enchantLevel >= 3) {
			return Config.ENCHANT.ACCESORY_ENCHANT_PROBABILITY3 * 10;
		}
		if (enchantLevel >= 2) {
			return Config.ENCHANT.ACCESORY_ENCHANT_PROBABILITY2 * 10;
		}
		if (enchantLevel >= 1) {
			return Config.ENCHANT.ACCESORY_ENCHANT_PROBABILITY1 * 10;
		}
		if (enchantLevel >= 0) {
			return Config.ENCHANT.ACCESORY_ENCHANT_PROBABILITY0 * 10;
		}
		return ((70 + enchantLevelTemp * Config.ENCHANT.ENCHANT_CHANCE_ACCESSORY_DEFAULT) / (enchantLevelTemp * (enchantLevel - 1))) * 10;
	}
	
	/**
	 * 안전 인챈트가 0인 아이템
	 * @param safeEnchant
	 * @param enchantLevel
	 * @param enchantLevelTemp
	 * @return prob
	 */
	double getProbSafeEnchantZero(int safeEnchant, int enchantLevel, int enchantLevelTemp) {
		if (enchantLevel >= 9) {
			return Config.ENCHANT.ARMOR_SAFETY_ENCHANT9 * 10;
		}
		if (enchantLevel >= 8) {
			return Config.ENCHANT.ARMOR_SAFETY_ENCHANT8 * 10;
		}
		if (enchantLevel >= 7) {
			return Config.ENCHANT.ARMOR_SAFETY_ENCHANT7 * 10;
		}
		if (enchantLevel >= 6) {
			return Config.ENCHANT.ARMOR_SAFETY_ENCHANT6 * 10;
		}
		if (enchantLevel >= 5) {
			return Config.ENCHANT.ARMOR_SAFETY_ENCHANT5 * 10;
		}
		if (enchantLevel >= 4) {
			return Config.ENCHANT.ARMOR_SAFETY_ENCHANT4 * 10;
		}
		if (enchantLevel >= 3) {
			return Config.ENCHANT.ARMOR_SAFETY_ENCHANT3 * 10;
		}
		if (enchantLevel >= 2) {
			return Config.ENCHANT.ARMOR_SAFETY_ENCHANT2 * 10;
		}
		if (enchantLevel >= 1) {
			return Config.ENCHANT.ARMOR_SAFETY_ENCHANT1 * 10;
		}
		if (enchantLevel >= 0) {
			return Config.ENCHANT.ARMOR_SAFETY_ENCHANT0 * 10;
		}
		return (90 / ((enchantLevel - safeEnchant + 1) << 1) / (enchantLevel / 7 != 0 ? 2 : 1) / enchantLevelTemp + Config.ENCHANT.ENCHANT_CHANCE_ARMOR_DEFAULT) * 10;
	}
	
	/**
	 * 보호 주문서 확률
	 * @param enchantLevel
	 * @return prob
	 */
	double getProbSafePaper(int enchantLevel) {
		if (enchantLevel >= 8) {
			return Config.ENCHANT.ENCHANT_SCROLL_PROBABILITY_9 * 10;
		}
		if (enchantLevel >= 7) {
			return Config.ENCHANT.ENCHANT_SCROLL_PROBABILITY_8 * 10;
		}
		if (enchantLevel >= 6) {
			return Config.ENCHANT.ENCHANT_SCROLL_PROBABILITY_7 * 10;
		}
		if (enchantLevel >= 5) {
			return Config.ENCHANT.ENCHANT_SCROLL_PROBABILITY_6 * 10;
		}
		if (enchantLevel >= 4) {
			return Config.ENCHANT.ENCHANT_SCROLL_PROBABILITY_5 * 10;
		}
		if (enchantLevel >= 3) {
			return Config.ENCHANT.ENCHANT_SCROLL_PROBABILITY_4 * 10;
		}
		if (enchantLevel >= 2) {
			return Config.ENCHANT.ENCHANT_SCROLL_PROBABILITY_3 * 10;
		}
		if (enchantLevel >= 1) {
			return Config.ENCHANT.ENCHANT_SCROLL_PROBABILITY_2 * 10;
		}
		if (enchantLevel >= 0) {
			return Config.ENCHANT.ENCHANT_SCROLL_PROBABILITY_1 * 10;
		}
		return 0D;
	}
	
	/**
	 * 용의 티셔츠 보호 주문서 확률
	 * @param enchantLevel
	 * @return prob
	 */
	double getProbSafePaperFromTshirts(int enchantLevel) {
		if (enchantLevel >= 8) {
			return Config.ENCHANT.DRAGON_TSHIRT_ENCHANT_SCROLL_PROBABILITY_9 * 10;
		}
		if (enchantLevel >= 7) {
			return Config.ENCHANT.DRAGON_TSHIRT_ENCHANT_SCROLL_PROBABILITY_8 * 10;
		}
		if (enchantLevel >= 6) {
			return Config.ENCHANT.DRAGON_TSHIRT_ENCHANT_SCROLL_PROBABILITY_7 * 10;
		}
		if (enchantLevel >= 5) {
			return Config.ENCHANT.DRAGON_TSHIRT_ENCHANT_SCROLL_PROBABILITY_6 * 10;
		}
		if (enchantLevel >= 4) {
			return Config.ENCHANT.DRAGON_TSHIRT_ENCHANT_SCROLL_PROBABILITY_5 * 10;
		}
		return 0D;
	}
}


