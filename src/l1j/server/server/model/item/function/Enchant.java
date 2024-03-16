package l1j.server.server.model.item.function;

import l1j.server.Config;
import l1j.server.common.bin.enchant.EnchantScrollTableT;
import l1j.server.common.bin.item.CommonItemInfo;
import l1j.server.server.construct.L1Grade;
import l1j.server.server.construct.item.L1ItemArmorType;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.LogEnchantTable;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.S_EnchantResult;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.message.S_GlobalMessege;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.StringUtil;
//import manager.Manager;  // MANAGER DISABLED

public class Enchant extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public Enchant(L1Item item) {
		super(item);
	}
	
	/**
	 * 대상의 최대 인챈트 검증
	 * @param target
	 * @return boolean
	 */
	protected boolean is_max_enchant(L1ItemInstance target) {
		CommonItemInfo bin = target.getItem().getBin();
		if (bin != null && (bin.get_max_enchant() == 0 || bin.get_max_enchant() <= target.getEnchantLevel())) {
			return true;
		}
		return false;
	}
	
	/**
	 * 주문서 사용 가능 검증
	 * @param pc
	 * @param target
	 * @return boolean
	 */
	protected boolean is_enchant_scroll_enable(L1PcInstance pc, EnchantScrollTableT.EnchantScrollTypeListT.EnchantScrollT scrollT, L1ItemInstance target) {
		// 사용 가능한 타겟 인첸트 레벨 검증
		int targetEnchant = scrollT.get_targetEnchant();
		if (targetEnchant > 0 && targetEnchant > target.getEnchantLevel()) {
			pc.sendPackets(L1ServerMessage.sm79);
			return false;
		}
		// 사용 불가 아이템 재질 검증
		java.util.LinkedList<Integer> noTargetMaterialList = scrollT.get_noTargetMaterialList();
		if (noTargetMaterialList != null && noTargetMaterialList.contains(target.getItem().getMaterial().toInt())) {
			pc.sendPackets(L1ServerMessage.sm79);
			return false;
		}
		switch (scrollT.get_target_category()) {
		case WEAPON:// 무기류
			if (target.getItem().getItemType() != L1ItemType.WEAPON) {
				pc.sendPackets(L1ServerMessage.sm79);
				return false;
			}
			break;
		case ARMOR:// 방어구류
			if (target.getItem().getItemType() != L1ItemType.ARMOR) {
				pc.sendPackets(L1ServerMessage.sm79);
				return false;
			}
			break;
		case ACCESSORY:// 악세서리류
			if (target.getItem().getItemType() != L1ItemType.ARMOR) {
				pc.sendPackets(L1ServerMessage.sm79);
				return false;
			}
			if (!L1ItemArmorType.isAccessary(target.getItem().getType())) {
				pc.sendPackets(L1ServerMessage.sm79);
				return false;
			}
			break;
		case ELEMENT:// 속성주문서류
			if (target.getItem().getItemType() != L1ItemType.WEAPON) {
				pc.sendPackets(L1ServerMessage.sm79);
				return false;
			}
			int target_elemental_value = target.getAttrEnchantLevel();
			if (target_elemental_value > 0 && scrollT.get_elementalType() != L1ItemInstance.getElementalEnchantType(target_elemental_value)) {
				pc.sendPackets(L1ServerMessage.sm1294);
				return false;
			}
			break;
		}
		return true;
	}
	
	/**
	 * 인챈트 성공
	 * @param pc
	 * @param item
	 * @param i
	 */
	protected void successEnchant(L1PcInstance pc, L1ItemInstance item, int i) {
		String s, sa, sb, pm, s1;
		s = sa = sb = pm = StringUtil.EmptyString;
		s1 = item.getItem().getDesc();
		if (item.getEnchantLevel() > 0) {
			pm = StringUtil.PlusString;
		}
		if (item.getItem().getItemType() == L1ItemType.WEAPON) {
			if (!item.isIdentified() || item.getEnchantLevel() == 0) {
				switch (i) {
				case -1:
					s = s1;	sa = "$246";	sb = "$247";
					break;
				case 1: // '\001'
					s = s1;	sa = "$245";	sb = "$247";
					break;
				case 2: // '\002'
					s = s1;	sa = "$245";	sb = "$248";
					break;
				case 3: // '\003'
					s = s1;	sa = "$245";	sb = "$248";
					break;
				}
			} else {
				switch (i) {
				case -1:
					s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(StringUtil.EmptyOneString).append(s1).toString();
					// \f1%0이%2%1 빛납니다.
					sa = "$246";	sb = "$247";
					break;
				case 1: // '\001'
					s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(StringUtil.EmptyOneString).append(s1).toString();
					// \f1%0이%2%1 빛납니다.
					sa = "$245";	sb = "$247";
					break;
				case 2: // '\002'
					s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(StringUtil.EmptyOneString).append(s1).toString();
					// \f1%0이%2%1 빛납니다.
					sa = "$245";	sb = "$248";
					break;
				case 3: // '\003'
					s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(StringUtil.EmptyOneString).append(s1).toString();
					// \f1%0이%2%1 빛납니다.
					sa = "$245";	sb = "$248";
					break;
				}
			}
		} else if (item.getItem().getItemType() == L1ItemType.ARMOR) {
			if (!item.isIdentified() || item.getEnchantLevel() == 0) {
				switch (i) {
				case -1:
					s = s1;	sa = "$246";	sb = "$247";
					break;
				case 1: // '\001'
					s = s1;	sa = "$252";	sb = "$247 ";
					break;
				case 2: // '\002'
					s = s1;	sa = "$252";	sb = "$248 ";
					break;
				case 3: // '\003'
					s = s1;	sa = "$252";	sb = "$248 ";
					break;
				}
			} else {
				switch (i) {
				case -1:
					s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(StringUtil.EmptyOneString).append(s1).toString();
					// \f1%0이%2%1 빛납니다.
					sa = "$246";	sb = "$247";
					break;

				case 1: // '\001'
					s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(StringUtil.EmptyOneString).append(s1).toString();
					// \f1%0이%2%1 빛납니다.
					sa = "$252";	sb = "$247 ";
					break;

				case 2: // '\002'
					s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(StringUtil.EmptyOneString).append(s1).toString();
					// \f1%0이%2%1 빛납니다.
					sa = "$252";	sb = "$248 ";
					break;

				case 3: // '\003'
					s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(StringUtil.EmptyOneString).append(s1).toString();
					// \f1%0이%2%1 빛납니다.
					sa = "$252";	sb = "$248 ";
					break;
				}
			}
		}
		if (pc.getConfig()._enchantGfx) {
			pc.sendPackets(new S_EnchantResult(item, this, S_EnchantResult.EnchantResult.SUCCESS, i, 0), true);	//	인첸트연출
		}
		pc.sendPackets(new S_ServerMessage(161, s, sa, sb), true);
		int oldEnchantLvl	= item.getEnchantLevel();
		int newEnchantLvl	= item.getEnchantLevel() + i;
		int safe_enchant	= item.getItem().getSafeEnchant();
		
		/** 전체월드메세지 뿌리기 **/
		if (Config.ENCHANT.ENCHANT_MESSAGE && i > 0) {
			if (item.getItem().getItemType() == L1ItemType.WEAPON && newEnchantLvl >= 10) {// 무기
				if (pc.getConfig().isGlobalMessege()) {
					pc.sendPackets(new S_GlobalMessege(4446, item.getLogName(), item.getItem().getIconId()), true);
				}
				L1World.getInstance().broadcastPacket(pc, new S_GlobalMessege(4446, item.getLogName(), item.getItem().getIconId()), true);
			}
			if (item.getItem().getItemType() == L1ItemType.ARMOR) {
				if (item.getItem().getType() >= L1ItemArmorType.AMULET.getId() && item.getItem().getType() <= L1ItemArmorType.EARRING.getId()) {// 장신구
					if (newEnchantLvl >= 8) {
						if (pc.getConfig().isGlobalMessege()) {
							pc.sendPackets(new S_GlobalMessege(4445, item.getLogName(), item.getItem().getIconId()), true);
						}
						L1World.getInstance().broadcastPacket(pc, new S_GlobalMessege(4445, item.getLogName(), item.getItem().getIconId()), true);
					}
				} else if (item.getItem().getType() == L1ItemArmorType.SENTENCE.getId() || item.getItem().getType() == L1ItemArmorType.BADGE.getId() || item.getItem().getType() == L1ItemArmorType.PENDANT.getId()) {// 문장, 휘장, 펜던트
					if (newEnchantLvl >= 7) {
						if (pc.getConfig().isGlobalMessege()) {
							pc.sendPackets(new S_GlobalMessege(4445, item.getLogName(), item.getItem().getIconId()), true);
						}
						L1World.getInstance().broadcastPacket(pc, new S_GlobalMessege(4445, item.getLogName(), item.getItem().getIconId()), true);
					}
				} else if (newEnchantLvl >= 9){// 방어구
					if (pc.getConfig().isGlobalMessege()) {
						pc.sendPackets(new S_GlobalMessege(4445, item.getLogName(), item.getItem().getIconId()), true);
					}
					L1World.getInstance().broadcastPacket(pc, new S_GlobalMessege(4445, item.getLogName(), item.getItem().getIconId()), true);
				}
			}
		}
		
		if (item.isEquipped()) {// 착용중인 아이템일 경우
			L1PcInventory inventory = pc.getInventory();
			int oldACSub = item.getAcSub();
			inventory.removeItemAblity(item);// 능력치 제거
			enchantLevelChange(pc, item, newEnchantLvl, safe_enchant, i > 0);
			int newACSub = item.getAcSub();
			if ((item.getItem().getItemType() == L1ItemType.ARMOR) && (i != 0)) {				
				if (L1ItemArmorType.isAccessary(item.getItem().getType()))
					pc.getAC().addAc(newACSub - oldACSub);
				else
					pc.getAC().addAc(-i);			
			}
			inventory.setItemAblity(item);// 능력치 부여
		} else {
			enchantLevelChange(pc, item, newEnchantLvl, safe_enchant, i > 0);
		}

		if (item.getItem().getItemType() == L1ItemType.WEAPON && Config.SERVER.LOGGING_WEAPON_ENCHANT != 0) {
			if (safe_enchant == 0 || newEnchantLvl >= Config.SERVER.LOGGING_WEAPON_ENCHANT) {
				LogEnchantTable.storeLogEnchant(pc.getId(), item.getId(), oldEnchantLvl, newEnchantLvl);
			}
		} else if (item.getItem().getItemType() == L1ItemType.ARMOR && Config.SERVER.LOGGING_ARMOR_ENCHANT != 0) {
			if (safe_enchant == 0 || newEnchantLvl >= Config.SERVER.LOGGING_ARMOR_ENCHANT) {
				LogEnchantTable.storeLogEnchant(pc.getId(), item.getId(), oldEnchantLvl, newEnchantLvl);
			}
		}
		/** 매니저 로그 */
		/*if (item.getItem().getItemType() == L1ItemType.WEAPON 
				&& (newEnchantLvl >= 8 || newEnchantLvl >= Config.ENCHANT.WEAPON_LIMIT || newEnchantLvl >= Config.ENCHANT.ADVANCED_WEAPON_LIMIT)) {
			//Manager.getInstance().EnchantAppend(item.getDescKr(), oldEnchantLvl, newEnchantLvl, pc.getName(), i > 0 ? 0 : 1);
		} else if (item.getItem().getItemType() == L1ItemType.ARMOR 
				&& (newEnchantLvl >= 8 || newEnchantLvl >= Config.ENCHANT.ARMOR_ENCHANT_LIMIT)) {
			//Manager.getInstance().EnchantAppend(item.getDescKr(), oldEnchantLvl, newEnchantLvl, pc.getName(), i > 0 ? 0 : 1);
		}*/  // MANAGER DISABLED
	}
	
	/**
	 * 인챈트 수치를 변경한다.
	 * @param pc
	 * @param item
	 * @param newEnchantLvl
	 * @param safe_enchant
	 * @param result
	 */
	void enchantLevelChange(L1PcInstance pc, L1ItemInstance item, int newEnchantLvl, int safe_enchant, boolean result){
		item.setEnchantLevel(newEnchantLvl);
		pc.getInventory().updateItem(item, L1PcInventory.COL_ENCHANTLVL);
		pc.getInventory().saveItem(item, L1PcInventory.COL_ENCHANTLVL);
		if (newEnchantLvl > safe_enchant) {
			LoggerInstance.getInstance().addEnchant(pc, item, result);
		}
	}
	
	/**
	 * 인챈트 실패
	 * @param pc
	 * @param item
	 */
	protected void failureEnchant(L1PcInstance pc, L1ItemInstance item) {
		String s	= StringUtil.EmptyString;
		String sa	= StringUtil.EmptyString;
		if (!pc.getConfig()._enchantGfx) {
			L1ItemType itemType = item.getItem().getItemType();
			String desc = item.getItem().getDesc();
			String pm = StringUtil.EmptyString;
			if (itemType == L1ItemType.WEAPON) {// 무기
				if (!item.isIdentified() || item.getEnchantLevel() == 0) {
					s = desc;
					sa = "$245";	// \f1%0이 강렬하게%1 빛난 뒤, 증발하고 있지 않게 됩니다.
				} else {
					if (item.getEnchantLevel() > 0) {
						pm = StringUtil.PlusString;
					}
					s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(StringUtil.EmptyOneString).append(desc).toString();
					sa = "$245";	// \f1%0이 강렬하게%1 빛난 뒤, 증발하고 있지 않게 됩니다.
				}
			} else if (itemType == L1ItemType.ARMOR) {// 방어구
				if (!item.isIdentified() || item.getEnchantLevel() == 0) {
					s = desc;
					sa = " $252";	// \f1%0이 강렬하게%1 빛난 뒤, 증발하고 있지 않게 됩니다.
				} else {
					if (item.getEnchantLevel() > 0) {
						pm = StringUtil.PlusString;
					}
					s = (new StringBuilder()).append(pm + item.getEnchantLevel()).append(StringUtil.EmptyOneString).append(desc).toString();
					sa = " $252";	// \f1%0이 강렬하게%1 빛난 뒤, 증발하고 있지 않게 됩니다.
				}
			}
		}
		
		int itemId = item.getItem().getItemId();
		if ((itemId >= 1115 && itemId <= 1118) || (itemId >= 22250 && itemId <= 22252)) {
			pc.sendPackets(L1ServerMessage.sm1310);
			pc.getInventory().setEquipped(item, false);
			item.setEnchantLevel(0);
			pc.getInventory().updateItem(item, L1PcInventory.COL_ENCHANTLVL);
			pc.getInventory().saveItem(item, L1PcInventory.COL_ENCHANTLVL);
			//Manager.getInstance().EnchantAppend(item.getDescKr(), item.getEnchantLevel(), 0, pc.getName(), 1); // MANAGER DISABLED
		} else {
			if (pc.getConfig()._enchantGfx) {
				pc.sendPackets(new S_EnchantResult(item, this, S_EnchantResult.EnchantResult.FAIL_DESTROY, 0, 0), true);//	인첸트연출 실패멘트 자동출력
			} else {
				pc.sendPackets(new S_ServerMessage(164, s, sa), true);
			}			
			pc.getInventory().removeItem(item, item.getCount());
			
			//Manager.getInstance().EnchantAppend(item.getDescKr(), item.getEnchantLevel(), 0, pc.getName(), 1); // MANAGER DISABLED
			LoggerInstance.getInstance().addEnchant(pc, item, false);
		}
		pc.sendPackets(new S_OwnCharStatus(pc), true);
	}

	/**
	 * 축복 인챈트 주문서의 랜덤 증가 수치
	 * @param item
	 * @param itemId
	 * @return int
	 */
	protected int getRandomEnchantValue(L1ItemInstance item, int itemId) {
		final int a = 1, b = 2, c = 3;
		if (item.getItem().getItemType() == L1ItemType.ARMOR && item.getItem().getSafeEnchant() == 0 && item.getDescKr().indexOf("가더") > 0) {
			return a;
		}

		int j = CommonUtil.random(100) + 1;
		if (itemId == L1ItemId.B_SCROLL_OF_ENCHANT_ARMOR || itemId == L1ItemId.B_SCROLL_OF_ENCHANT_WEAPON || itemId == L1ItemId.INADRIL_T_SCROLL_B || itemId == 3000124 || itemId == 3000131) {
			if (item.getEnchantLevel() <= -1) {
				return a;
			}
			if (item.getEnchantLevel() <= 2) {
				if (j < 32) {
					return a;
				}
				if (j >= 33 && j <= 76) {
					return b;
				}
				if (j >= 77 && j <= 100) {
					return c;
				}
			}
			if (item.getEnchantLevel() >= 3 && item.getEnchantLevel() <= 5) {
				return j < 50 ? b : a;
			}
			return a;
		}
		if (itemId == 140129 || itemId == 140130) {
			if (item.getEnchantLevel() < 0) {
				return j < 30 ? b : a;
			}
			if (item.getEnchantLevel() <= 2) {
				if (j < 32) {
					return a;
				}
				if (j >= 33 && j <= 60) {
					return b;
				}
				if (j >= 61 && j <= 100) {
					return c;
				}
			}
			if (item.getEnchantLevel() >= 3 && item.getEnchantLevel() <= 5) {
				return j < 60 ? b : a;
			}
			return a;
		}
		return a;
	}
	
	/**
	 * 속성 주문서 인챈트
	 * @param pc
	 * @param item
	 * @param item_id
	 */
	protected void elementalEnchant(L1PcInstance pc, L1ItemInstance item, int elemental_type) {
		int attrLevel	= item.getAttrEnchantLevel();
		int prob		= CommonUtil.random(100) + 1;
		int resultLevel	= -1;
		switch (elemental_type) {
		case 1:// 화령의 무기 강화 주문서
			resultLevel = calcElementalEnchant(5, attrLevel, prob, item.getItem().getItemGrade(), item.getEnchantLevel());
			break;
		case 2:// 수령의 무기 강화 주문서
			resultLevel = calcElementalEnchant(10, attrLevel, prob, item.getItem().getItemGrade(), item.getEnchantLevel());
			break;
		case 3:// 풍령의 무기 강화 주문서
			resultLevel = calcElementalEnchant(15, attrLevel, prob, item.getItem().getItemGrade(), item.getEnchantLevel());
			break;
		case 4:// 지령의 무기 강화 주문서
			resultLevel = calcElementalEnchant(20, attrLevel, prob, item.getItem().getItemGrade(), item.getEnchantLevel());
			break;
		default:
			System.out.println(String.format("[Enchant] UNDEFINED_ELEMENTAL_TYPE : TYPE(%d)", elemental_type));
			return;
		}
		
		switch (resultLevel) {
		case -1:
			// 인챈트 불가
			pc.sendPackets(L1ServerMessage.sm79);
			break;
		case 0:
			pc.getInventory().consumeItem(this, 1);
			// 인챈트 실패
			if (pc.getConfig()._enchantGfx) {
				pc.sendPackets(new S_EnchantResult(item, this, S_EnchantResult.EnchantResult.FAIL_REMAIN, 0, elemental_type), true);
			}
			pc.sendPackets(new S_ServerMessage(1411, item.getLogNameRef()), true);
			break;
		default:
			pc.getInventory().consumeItem(this, 1);
			// 인챈트 성공
			if (pc.getConfig()._enchantGfx) {
				pc.sendPackets(new S_EnchantResult(item, this, S_EnchantResult.EnchantResult.SUCCESS, 1, elemental_type), true);
			}
			pc.sendPackets(new S_ServerMessage(1410, item.getLogNameRef()), true);
			item.setAttrEnchantLevel(resultLevel);
			pc.getInventory().updateItem(item, L1PcInventory.COL_ATTRENCHANTLVL);
			pc.getInventory().saveItem(item, L1PcInventory.COL_ATTRENCHANTLVL);
			break;
		}
	}
	
	/**
	 * 속성 주문서의 인챈트 결과를 계산한다.
	 * @param maxLevel
	 * @param attrLevel
	 * @param prob
	 * @param level
	 * @param enchantLevel
	 * @return int
	 */
	int calcElementalEnchant(int maxLevel, int attrLevel, int prob, L1Grade grade, int enchantLevel){
		if (attrLevel >= maxLevel) {
			return -1;
		}
		if (attrLevel == 0) {
			return prob <= 30 ? maxLevel - 4 : 0;
		}
		if (attrLevel == maxLevel - 4) {
			return prob <= 10 ? maxLevel - 3 : 0;
		}
		if (attrLevel == maxLevel - 3) {
			return prob <= 5 ? maxLevel - 2 : 0;
		}
		if (attrLevel == maxLevel - 2) {
			if (grade == L1Grade.LEGEND || grade == L1Grade.MYTH || grade == L1Grade.ONLY) {// 전설급 무기 이상
				return prob <= 5 ? maxLevel - 1 : 0;
			}
			if (enchantLevel >= 9) {
				return prob <= 5 ? maxLevel - 1 : 0;
			}
		} else if (attrLevel == maxLevel - 1) {
			if (grade == L1Grade.LEGEND || grade == L1Grade.MYTH || grade == L1Grade.ONLY) {// 전설급 무기 이상
				return prob <= 5 ? maxLevel : 0;
			}
			if (enchantLevel >= 10) {
				return prob <= 5 ? maxLevel : 0;
			}
		}
		return -1;
	}

	/**
	 * 속성 변환 주문서
	 * @param pc
	 * @param item
	 * @param itemId
	 */
	protected void elementalChange(L1PcInstance pc, L1ItemInstance item, int itemId) {
		int attrLevel = item.getAttrEnchantLevel();
		int attrScroll = 0;
		switch(itemId){
		case 560030:
			attrScroll = 0;// 화령의 속성 변환 주문서
			break;
		case 560031:
			attrScroll = 5;// 수령의 속성 변환 주문서
			break;
		case 560032:
			attrScroll = 10;// 풍령의 속성 변환 주문서
			break;
		case 560033:
			attrScroll = 15;// 지령의 속성 변환 주문서
			break;
		default:
			pc.sendPackets(L1ServerMessage.sm79);// 아무일도 일어나지 않았습니다.
			return;
		}
		
		if (!pc.getInventory().checkItem(itemId, 1)) {
			return;
		}
		if (attrLevel > 0) {
			if (attrScroll + 1 <= attrLevel && attrLevel <= attrScroll + 5) {
				pc.sendPackets(L1ServerMessage.sm3319);// 동일한 속성에는 사용하실 수없습니다.
				return;
			}
			if (attrLevel % 5 == 0) {
				pc.sendPackets(new S_ServerMessage(3296, item.getLogNameRef()), true);// 인챈트: %0에 찬란한 대자연의 힘이 스며듭니다.
				item.setAttrEnchantLevel(attrScroll + 5);
			} else {
				pc.sendPackets(new S_ServerMessage(1410, item.getLogNameRef()), true);// 인챈트: %0에 영롱한 대자연의 힘이 스며듭니다.
				item.setAttrEnchantLevel(attrLevel % 5 + attrScroll);
			}
			pc.getInventory().consumeItem(itemId, 1);
			pc.getInventory().updateItem(item, L1PcInventory.COL_ATTRENCHANTLVL);
			pc.getInventory().saveItem(item, L1PcInventory.COL_ATTRENCHANTLVL);
		} else {
			pc.sendPackets(L1ServerMessage.sm79);// 아무일도 일어나지 않았습니다.
		}
	}
}

