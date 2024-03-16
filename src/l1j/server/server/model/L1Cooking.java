package l1j.server.server.model;

import static l1j.server.server.model.skill.L1SkillId.*;

import java.util.Arrays;
import java.util.List;

import l1j.server.server.GameServerSetting;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.message.S_ServerMessage;

public class L1Cooking {
	private L1Cooking() {}
	
	/**
	 * 요리
	 * @param itemId
	 * @return boolean
	 */
	public static boolean isCooking(int itemId) {
		return itemId >= 41277 && itemId <= 41283 // 1차 요리
				|| itemId >= 49049 && itemId <= 49056// 2차 요리
				|| itemId >= 210048 && itemId <= 210055//3차 요리
				|| itemId >= 41285 && itemId <= 41291// 1차 환상의 요리
				|| itemId >= 49057 && itemId <= 49064// 2차 환상의 요리
				|| itemId >= 210056 && itemId <= 210062 // 3차 환상의 요리
				|| itemId >= 30051 && itemId <= 30053 // 리뉴얼 요리
				|| itemId >= 1030051 && itemId <= 1030053 // 리뉴얼 축복받은 요리
				|| itemId >= 130055 && itemId <= 130057 // 수련자 요리
				|| itemId == 3110121// 메티스의 정성스런 요리
				|| itemId >= 31846 && itemId <= 31848 // 아덴의 요리
				|| itemId >= 31850 && itemId <= 31852 // 축복받은 아덴의 요리
				;
	}
	
	/**
	 * 스프
	 * @param itemId
	 * @return boolean
	 */
	public static boolean isSoup(int itemId) {
		return itemId == 41284 //버섯 스프 
				|| itemId == 49056//크랩살 스프 
				|| itemId == 49064//환상의 크랩살 스프 
				|| itemId == 41292//환상의 버섯 스프
				|| itemId == 210055 //바실리스크 알 스프
				|| itemId == 210063 //환상의 바실리스크 알 스프
				|| itemId == 30054 // 수련의 닭고기 스프
				|| itemId == 1030054 // 축복받은 수련의 닭고기 스프
				|| itemId == 130054 //수련자의 닭고기 스프
				|| itemId == 3110122// 메티스의 정성스런 스프
				|| itemId == 31849// 아덴의 토마토 스프
				|| itemId == 31853// 축복받은 아덴의 토마토 스프
				;
	}
	
	private static final List<Integer> OLD_SOUP_ITEMS = Arrays.asList(new Integer[] {
			41284, 49056, 49064, 41292, 210055, 210063
	}); 

	public static void useCookingItem(L1PcInstance pc, L1ItemInstance item) {
		int itemId = item.getItem().getItemId();
		
		// (구)스프 배고픔게이지 100%일때 사용가능하다.
		if (OLD_SOUP_ITEMS.contains(itemId) && pc.getFood() != GameServerSetting.MAX_FOOD_VALUE) {
			pc.sendPackets(new S_ServerMessage(74, item.getNumberedName(1)), true);
			return;
		}

		// 기존 요리 버프를 제거한다.
		if (isCooking(itemId)) {
			int cookingId = pc.getCookingId();
			if (cookingId != 0) {
				pc.getSkill().removeSkillEffect(cookingId);
			}
		}

		if (isSoup(itemId)) {
			int soupId = pc.getSoupId();
			if (soupId != 0) {
				pc.getSkill().removeSkillEffect(soupId);			
			}
		}
		
		int time = item.getItem().getBuffDurationSecond();
		switch(itemId){
		case 41277:case 41285:		eatCooking(pc, itemId == 41277 ? COOKING_1_0_N : COOKING_1_0_S, time);break;
		case 41278:case 41286:		eatCooking(pc, itemId == 41278 ? COOKING_1_1_N : COOKING_1_1_S, time);break;
		case 41279:case 41287:		eatCooking(pc, itemId == 41279 ? COOKING_1_2_N : COOKING_1_2_S, time);break;
		case 41280:case 41288:		eatCooking(pc, itemId == 41280 ? COOKING_1_3_N : COOKING_1_3_S, time);break;
		case 41281:case 41289:		eatCooking(pc, itemId == 41281 ? COOKING_1_4_N : COOKING_1_4_S, time);break;
		case 41282:case 41290:		eatCooking(pc, itemId == 41282 ? COOKING_1_5_N : COOKING_1_5_S, time);break;
		case 41283:case 41291:		eatCooking(pc, itemId == 41283 ? COOKING_1_6_N : COOKING_1_6_S, time);break;
		case 41284:case 41292:		eatCooking(pc, itemId == 41284 ? COOKING_1_7_N : COOKING_1_7_S, time);break;
		case 49049:case 49057:		eatCooking(pc, itemId == 49049 ? COOKING_1_8_N : COOKING_1_8_S, time);break;
		case 49050:case 49058:		eatCooking(pc, itemId == 49050 ? COOKING_1_9_N : COOKING_1_9_S, time);break;
		case 49051:case 49059:		eatCooking(pc, itemId == 49051 ? COOKING_1_10_N : COOKING_1_10_S, time);break;
		case 49052:case 49060:		eatCooking(pc, itemId == 49052 ? COOKING_1_11_N : COOKING_1_11_S, time);break;
		case 49053:case 49061:		eatCooking(pc, itemId == 49053 ? COOKING_1_12_N : COOKING_1_12_S, time);break;
		case 49054:case 49062:		eatCooking(pc, itemId == 49054 ? COOKING_1_13_N : COOKING_1_13_S, time);break;
		case 49055:case 49063:		eatCooking(pc, itemId == 49055 ? COOKING_1_14_N : COOKING_1_14_S, time);break;
		case 49056:case 49064:		eatCooking(pc, itemId == 49056 ? COOKING_1_15_N : COOKING_1_15_S, time);break;
		case 210048:case 210056:	eatCooking(pc, itemId == 210048 ? COOKING_1_16_N : COOKING_1_16_S, time);break;
		case 210049:case 210057:	eatCooking(pc, itemId == 210049 ? COOKING_1_17_N : COOKING_1_17_S, time);break;
		case 210050:case 210058:	eatCooking(pc, itemId == 210050 ? COOKING_1_18_N : COOKING_1_18_S, time);break;
		case 210051:case 210059:	eatCooking(pc, itemId == 210051 ? COOKING_1_19_N : COOKING_1_19_S, time);break;
		case 210052:case 210060:	eatCooking(pc, itemId == 210052 ? COOKING_1_20_N : COOKING_1_20_S, time);break;
		case 210053:case 210061:	eatCooking(pc, itemId == 210053 ? COOKING_1_21_N : COOKING_1_21_S, time);break;
		case 210054:case 210062:	eatCooking(pc, itemId == 210054 ? COOKING_1_22_N : COOKING_1_22_S, time);break;
		case 210055:case 210063:	eatCooking(pc, itemId == 210055 ? COOKING_1_23_N : COOKING_1_23_S, time);break;
			
		case 30051:					eatCooking(pc, COOK_STR, time);break;// 힘센 한우 스테이크
		case 30052:					eatCooking(pc, COOK_DEX, time);break;// 날샌 연어 찜
		case 30053:					eatCooking(pc, COOK_INT, time);break;// 영리한 칠면조 구이
		case 30054:					eatCooking(pc, COOK_GROW, time);break;// 수련의 닭고기 스프
		case 1030051:				eatCooking(pc, BLESS_COOK_STR, time);break;// 축복받은 힘센 한우 스테이크
		case 1030052:				eatCooking(pc, BLESS_COOK_DEX, time);break;// 축복받은 날샌 연어 찜
		case 1030053:				eatCooking(pc, BLESS_COOK_INT, time);break;// 축복받은 영리한 칠면조 구이
		case 1030054:				eatCooking(pc, BLESS_COOK_GROW, time);break;// 축복받은 수련의 닭고기 스프
		case 130054:				eatCooking(pc, COOK_GROW, time);break;// 수련자의 닭고기 스프
		case 130055:				eatCooking(pc, COOK_STR, time);break;// 수련자의 한우 스테이크
		case 130056:				eatCooking(pc, COOK_DEX, time);break;// 수련자 연어 찜
		case 130057:				eatCooking(pc, COOK_INT, time);break;// 수련자 칠면조 구이
		case 3110121:				eatCooking(pc, COOK_METIS_NORMAL, time);break;// 메티스의 정성스런 요리
		case 3110122:				eatCooking(pc, COOK_METIS_GROW, time);break;// 메티스의 정성스런 스프
		
		case 31846:					eatCooking(pc, COOK_ADEN_SPECIAL_STEAK, time);break;// 아덴의 특제 스테이크(완력)
		case 31847:					eatCooking(pc, COOK_ADEN_SPECIAL_CANAPE, time);break;// 아덴의 특제 카나페(민첩)
		case 31848:					eatCooking(pc, COOK_ADEN_SPECIAL_SALAD, time);break;// 아덴의 특제 샐러드(지식)
		case 31849:					eatCooking(pc, COOK_ADEN_TOMATO_SOUP, time);break;// 아덴의 토마토 스프
		case 31850:					eatCooking(pc, BLESS_COOK_ADEN_SPECIAL_STEAK, time);break;// 축복받은 아덴의 특제 스테이크(완력)
		case 31851:					eatCooking(pc, BLESS_COOK_ADEN_SPECIAL_CANAPE, time);break;// 축복받은 아덴의 특제 카나페(민첩)
		case 31852:					eatCooking(pc, BLESS_COOK_ADEN_SPECIAL_SALAD, time);break;// 축복받은 아덴의 특제 샐러드(지식)
		case 31853:					eatCooking(pc, BLESS_COOK_ADEN_TOMATO_SOUP, time);break;// 축복받은 아덴의 토마토 스프
		default:break;
		}
		pc.sendPackets(new S_EffectLocation(pc.getX(), pc.getY(), 6392), true);
		pc.sendPackets(new S_ServerMessage(76, item.getNumberedName(1)), true);
		pc.getInventory().removeItem(item, 1);
	}

	/**
	 * 요리 복용
	 * @param pc
	 * @param cookingId
	 * @param time
	 */
	public static void eatCooking(L1PcInstance pc, int cookingId, int time) {
		doBonus(pc, cookingId, time, true);
		pc.getSkill().setSkillEffect(cookingId, time * 1000);
	}
	
	/**
	 * 요리 능력치 설정
	 * @param pc
	 * @param cookingId
	 * @param flag
	 * @param time
	 */
	public static void doBonus(L1PcInstance pc, int cookingId, int time, boolean flag) {
		int flag_val = flag ? 1 : -1;
		int cookingType = 0;		
		switch(cookingId){		
		/** 구 1차요리  **/
		case COOKING_1_0_N:
			cookingType = 0;
			pc.resistance.addAllNaturalResistance(10 * flag_val);
			break;	
		case COOKING_1_0_S:
			cookingType = 0;
			pc.resistance.addAllNaturalResistance(10 * flag_val);
			pc.ability.addDamageReduction(5 * flag_val);
			break;		
		case COOKING_1_1_N:
			cookingType = 1;
			pc.addMaxHp(30 * flag_val);
			break;
		case COOKING_1_1_S:
			cookingType = 1;
			pc.addMaxHp(30 * flag_val);
			pc.ability.addDamageReduction(5 * flag_val);
			break;
		case COOKING_1_2_N:
			cookingType = 2;
			pc.addMpRegen(3 * flag_val);
			break;
		case COOKING_1_2_S:
			cookingType = 2;
			pc.addMpRegen(3 * flag_val);
			pc.ability.addDamageReduction(5 * flag_val);
			break;
		case COOKING_1_3_N:
			cookingType = 3;
			pc.ac.addAc(-1 * flag_val);
			break;
		case COOKING_1_3_S:
			cookingType = 3;
			pc.ac.addAc(-1 * flag_val);
			pc.ability.addDamageReduction(5 * flag_val);
			break;
		case COOKING_1_4_N:
			cookingType = 4;
			pc.addMaxMp(20 * flag_val);
			break;
		case COOKING_1_4_S:
			cookingType = 4;
			pc.addMaxMp(20 * flag_val);
			pc.ability.addDamageReduction(5 * flag_val);
			break;
		case COOKING_1_5_N:
			cookingType = 5;
			break;
		case COOKING_1_5_S:
			cookingType = 5;
			pc.ability.addDamageReduction(5 * flag_val);
			break;
		case COOKING_1_6_N:
			cookingType = 6;
			pc.resistance.addMr(5 * flag_val);
			break;
		case COOKING_1_6_S:
			cookingType = 6;
			pc.resistance.addMr(5 * flag_val);
			pc.ability.addDamageReduction(5 * flag_val);
			break;
		case COOKING_1_7_N:
			cookingType = 7;
			pc.add_exp_boosting_ratio(1 * flag_val);
			break;
		case COOKING_1_7_S:
			cookingType = 7;
			pc.add_exp_boosting_ratio(1 * flag_val);
			pc.ability.addDamageReduction(5 * flag_val);
			break;
			
		/** 구 2차요리  **/
		case COOKING_1_8_N:
			cookingType = 16;
			pc.ability.addLongHitup(2 * flag_val); 
			pc.ability.addLongDmgup(1 * flag_val); 
			break;
		case COOKING_1_8_S:
			cookingType = 16;
			pc.ability.addLongHitup(2 * flag_val); 
			pc.ability.addLongDmgup(1 * flag_val);
			pc.ability.addDamageReduction(5 * flag_val);
			break;
		case COOKING_1_9_N:
			cookingType = 17;
			pc.addMaxHp(30 * flag_val);
			pc.addMaxMp(30 * flag_val);			
			break;
		case COOKING_1_9_S:
			cookingType = 17;
			pc.addMaxHp(30 * flag_val);
			pc.addMaxMp(30 * flag_val);
			pc.ability.addDamageReduction(5 * flag_val);
			break;
		case COOKING_1_10_N:
			cookingType = 18;
			pc.ac.addAc(-2 * flag_val);
			break;
		case COOKING_1_10_S:
			cookingType = 18;
			pc.ac.addAc(-2 * flag_val);
			pc.ability.addDamageReduction(5 * flag_val);
			break;
		case COOKING_1_11_N:
			cookingType = 19;
			break;
		case COOKING_1_11_S:
			cookingType = 19;
			pc.ability.addDamageReduction(5 * flag_val);
			break;
		case COOKING_1_12_N:
			cookingType = 20;
			pc.addHpRegen(2 * flag_val);
			pc.addMpRegen(2 * flag_val);
			break;
		case COOKING_1_12_S:
			cookingType = 20;
			pc.addHpRegen(2 * flag_val);
			pc.addMpRegen(2 * flag_val);
			pc.ability.addDamageReduction(5 * flag_val);
			break;
		case COOKING_1_13_N:
			cookingType = 21;
			pc.resistance.addMr(10 * flag_val);
			break;
		case COOKING_1_13_S:
			cookingType = 21;
			pc.resistance.addMr(10 * flag_val);
			pc.ability.addDamageReduction(5 * flag_val);
			break;
		case COOKING_1_14_N:
			cookingType = 22;
			pc.ability.addSp(1 * flag_val);
			break;
		case COOKING_1_14_S:
			cookingType = 22;
			pc.ability.addSp(1 * flag_val);
			pc.ability.addDamageReduction(5 * flag_val);
			break;
		case COOKING_1_15_N:
			cookingType = 23;
			pc.add_exp_boosting_ratio(5 * flag_val);
			break;
		case COOKING_1_15_S:
			cookingType = 23;
			pc.add_exp_boosting_ratio(5 * flag_val);
			pc.ability.addDamageReduction(5 * flag_val);
			break;
			
		/** 구 3차요리  **/
		case COOKING_1_16_N:
			cookingType = 45; 
			pc.ability.addLongHitup(2 * flag_val);
			pc.ability.addLongDmgup(1 * flag_val);
			break;
		case COOKING_1_16_S:
			cookingType = 45; 
			pc.ability.addLongHitup(2 * flag_val);
			pc.ability.addLongDmgup(1 * flag_val);
			pc.ability.addDamageReduction(5 * flag_val);
			break;
		case COOKING_1_17_N:
			cookingType = 46;
			pc.addMaxHp(50 * flag_val);
			pc.addMaxMp(50 * flag_val);
			break;
		case COOKING_1_17_S:
			cookingType = 46;
			pc.addMaxHp(50 * flag_val);
			pc.addMaxMp(50 * flag_val);
			pc.ability.addDamageReduction(5 * flag_val);
			break;
		case COOKING_1_18_N:
			cookingType = 47;
			pc.ability.addShortHitup(2 * flag_val);
			pc.ability.addShortDmgup(1 * flag_val);
			break;
		case COOKING_1_18_S:
			cookingType = 47;
			pc.ability.addShortHitup(2 * flag_val);
			pc.ability.addShortDmgup(1 * flag_val);
			pc.ability.addDamageReduction(5 * flag_val);
			break;
		case COOKING_1_19_N:
			cookingType = 48;
			pc.ac.addAc(-3 * flag_val);
			pc.addHpRegen(2 * flag_val);
			break;
		case COOKING_1_19_S:
			cookingType = 48;
			pc.ac.addAc(-3 * flag_val);
			pc.addHpRegen(2 * flag_val);
			pc.ability.addDamageReduction(5 * flag_val);
			break;
		case COOKING_1_20_N:
			cookingType = 49;
			pc.addMpRegen(2 * flag_val);
			pc.resistance.addAllNaturalResistance(10 * flag_val);
			pc.resistance.addMr(15 * flag_val);
			break;
		case COOKING_1_20_S:
			cookingType = 49;
			pc.addMpRegen(2 * flag_val);
			pc.resistance.addAllNaturalResistance(10 * flag_val);
			pc.resistance.addMr(15 * flag_val);
			pc.ability.addDamageReduction(5 * flag_val);
			break;
		case COOKING_1_21_N:
			cookingType = 50;
			pc.ability.addSp(2 * flag_val);
			break;
		case COOKING_1_21_S:
			cookingType = 50;
			pc.ability.addSp(2 * flag_val);
			pc.ability.addDamageReduction(5 * flag_val);
			break;
		case COOKING_1_22_N:
			cookingType = 51;
			pc.addMaxHp(30 * flag_val);
			break;
		case COOKING_1_22_S:
			cookingType = 51;
			pc.addMaxHp(30 * flag_val);
			pc.ability.addDamageReduction(5 * flag_val);
			break;
		case COOKING_1_23_N:
			cookingType = 52;
			pc.add_exp_boosting_ratio(9 * flag_val);
			break;
		case COOKING_1_23_S:
			cookingType = 52;
			pc.add_exp_boosting_ratio(9 * flag_val);
			pc.ability.addDamageReduction(5 * flag_val);
			break;
		
		case COOK_STR:
			cookingType = 157;
			pc.add_exp_boosting_ratio(2 * flag_val);
			pc.ability.addShortDmgup(2 * flag_val);
			pc.ability.addShortHitup(1 * flag_val);
			pc.addHpRegen(2 * flag_val);
			pc.addMpRegen(2 * flag_val);
			pc.resistance.addMr(10 * flag_val);
			pc.resistance.addAllNaturalResistance(10 * flag_val);
			break;
		case COOK_DEX:
			cookingType = 158;
			pc.add_exp_boosting_ratio(2 * flag_val);
			pc.ability.addLongDmgup(2 * flag_val);
			pc.ability.addLongHitup(1 * flag_val);
			pc.addHpRegen(2 * flag_val);
			pc.addMpRegen(2 * flag_val);
			pc.resistance.addMr(10 * flag_val);
			pc.resistance.addAllNaturalResistance(10 * flag_val);
			break;
		case COOK_INT:
			cookingType = 159;
			pc.add_exp_boosting_ratio(2 * flag_val);
			pc.ability.addSp(2 * flag_val);
			pc.addHpRegen(2 * flag_val);
			pc.addMpRegen(3 * flag_val);
			pc.resistance.addMr(10 * flag_val);
			pc.resistance.addAllNaturalResistance(10 * flag_val);
			break;
		case COOK_GROW:
			cookingType = 160;
			pc.add_exp_boosting_ratio(4 * flag_val);
			pc.ability.addDamageReduction(2 * flag_val);
			break;
		case BLESS_COOK_STR:
			cookingType = 215;
			pc.add_exp_boosting_ratio(2 * flag_val);
			pc.resistance.addHitupAll(3 * flag_val);
			pc.ability.addShortDmgup(2 * flag_val);
			pc.ability.addShortHitup(1 * flag_val);
			pc.addHpRegen(2 * flag_val);
			pc.addMpRegen(2 * flag_val);
			pc.resistance.addMr(10 * flag_val);
			pc.resistance.addAllNaturalResistance(10 * flag_val);
			break;
		case BLESS_COOK_DEX:
			cookingType = 216;
			pc.add_exp_boosting_ratio(2 * flag_val);
			pc.resistance.addHitupAll(3 * flag_val);
			pc.ability.addLongDmgup(2 * flag_val);
			pc.ability.addLongHitup(1 * flag_val);
			pc.addHpRegen(2 * flag_val);
			pc.addMpRegen(2 * flag_val);
			pc.resistance.addMr(10 * flag_val);
			pc.resistance.addAllNaturalResistance(10 * flag_val);
			break;
		case BLESS_COOK_INT:
			cookingType = 217;
			pc.add_exp_boosting_ratio(2 * flag_val);
			pc.resistance.addHitupAll(3 * flag_val);
			pc.getAbility().addSp(2 * flag_val);
			pc.addHpRegen(2 * flag_val);
			pc.addMpRegen(3 * flag_val);
			pc.resistance.addMr(10 * flag_val);
			pc.resistance.addAllNaturalResistance(10 * flag_val);
			break;
		case BLESS_COOK_GROW:
			cookingType = 218;
			pc.add_exp_boosting_ratio(4 * flag_val);
			pc.resistance.addToleranceAll(2 * flag_val);
			pc.ability.addDamageReduction(2 * flag_val);
			pc.ability.addPVPDamageReduction(2 * flag_val);
			break;
			
		case COOK_METIS_NORMAL:
			cookingType = 151;
			pc.ability.addDamageReduction(5 * flag_val);
			pc.ability.addShortHitup(2 * flag_val);
			pc.ability.addShortDmgup(2 * flag_val);
			pc.ability.addLongHitup(2 * flag_val);
			pc.ability.addLongDmgup(2 * flag_val);
			pc.ability.addSp(2 * flag_val);
			pc.addHpRegen(3 * flag_val);
			pc.addMpRegen(4 * flag_val);
			pc.resistance.addMr(15 * flag_val);
			pc.resistance.addAllNaturalResistance(10 * flag_val);
			break;
		case COOK_METIS_GROW:
			cookingType = 162;
			pc.add_exp_boosting_ratio(5 * flag_val);
			pc.ability.addDamageReduction(5 * flag_val);
			break;
		case DOGAM_BUFF:
			cookingType = 187;
			pc.add_exp_boosting_ratio(20 * flag_val);
			pc.ability.addDamageReduction(5 * flag_val);
			break;
		case NARUTER_CANDY:
			cookingType = 187;
			pc.ability.addStr((pc.getLevel() <= 60 ? 7 : 6) * flag_val);
			pc.ability.addDex((pc.getLevel() <= 60 ? 7 : 6) * flag_val);
			break;
			
		case COOK_ADEN_SPECIAL_STEAK:
			cookingType = 226;
			pc.ability.addShortDmgup(3 * flag_val);
			pc.ability.addShortHitup(2 * flag_val);
			pc.ability.addDamageReduction(2 * flag_val);
			pc.resistance.addMr(10 * flag_val);
			pc.addMaxHp(50 * flag_val);
			pc.addHpRegen(5 * flag_val);
			pc.addMpRegen(2 * flag_val);
			pc.add_exp_boosting_ratio(4 * flag_val);
			pc.resistance.addAllNaturalResistance(10 * flag_val);
			break;
		case COOK_ADEN_SPECIAL_CANAPE:
			cookingType = 227;
			pc.ability.addLongDmgup(3 * flag_val);
			pc.ability.addLongHitup(2 * flag_val);
			pc.ability.addDamageReduction(2 * flag_val);
			pc.resistance.addMr(10 * flag_val);
			pc.addMaxHp(25 * flag_val);
			pc.addMaxMp(25 * flag_val);
			pc.addHpRegen(3 * flag_val);
			pc.addMpRegen(3 * flag_val);
			pc.add_exp_boosting_ratio(4 * flag_val);
			pc.resistance.addAllNaturalResistance(10 * flag_val);
			break;
		case COOK_ADEN_SPECIAL_SALAD:
			cookingType = 228;
			pc.ability.addSp(3 * flag_val);
			pc.ability.addDamageReduction(2 * flag_val);
			pc.resistance.addMr(10 * flag_val);
			pc.addMaxMp(50 * flag_val);
			pc.addHpRegen(2 * flag_val);
			pc.addMpRegen(5 * flag_val);
			pc.add_exp_boosting_ratio(4 * flag_val);
			pc.resistance.addAllNaturalResistance(10 * flag_val);
			break;
		case COOK_ADEN_TOMATO_SOUP:
			cookingType = 229;
			pc.ability.addDamageReduction(3 * flag_val);
			pc.addMaxHp(50 * flag_val);
			pc.add_exp_boosting_ratio(6 * flag_val);
			break;
			
		case BLESS_COOK_ADEN_SPECIAL_STEAK:
			cookingType = 230;
			pc.ability.addShortDmgup(3 * flag_val);
			pc.ability.addShortHitup(2 * flag_val);
			pc.ability.addDamageReduction(2 * flag_val);
			pc.resistance.addMr(10 * flag_val);
			pc.addMaxHp(50 * flag_val);
			pc.addHpRegen(5 * flag_val);
			pc.addMpRegen(2 * flag_val);
			pc.add_exp_boosting_ratio(4 * flag_val);
			pc.resistance.addAllNaturalResistance(10 * flag_val);
			pc.resistance.addHitupAll(3 * flag_val);
			break;
		case BLESS_COOK_ADEN_SPECIAL_CANAPE:
			cookingType = 231;
			pc.ability.addLongDmgup(3 * flag_val);
			pc.ability.addLongHitup(2 * flag_val);
			pc.ability.addDamageReduction(2 * flag_val);
			pc.resistance.addMr(10 * flag_val);
			pc.addMaxHp(25 * flag_val);
			pc.addMaxMp(25 * flag_val);
			pc.addHpRegen(3 * flag_val);
			pc.addMpRegen(3 * flag_val);
			pc.add_exp_boosting_ratio(4 * flag_val);
			pc.resistance.addAllNaturalResistance(10 * flag_val);
			pc.resistance.addHitupAll(3 * flag_val);
			break;
		case BLESS_COOK_ADEN_SPECIAL_SALAD:
			cookingType = 232;
			pc.ability.addSp(3 * flag_val);
			pc.ability.addDamageReduction(2 * flag_val);
			pc.resistance.addMr(10 * flag_val);
			pc.addMaxMp(50 * flag_val);
			pc.addHpRegen(2 * flag_val);
			pc.addMpRegen(5 * flag_val);
			pc.add_exp_boosting_ratio(4 * flag_val);
			pc.resistance.addAllNaturalResistance(10 * flag_val);
			pc.resistance.addHitupAll(3 * flag_val);
			break;
		case BLESS_COOK_ADEN_TOMATO_SOUP:
			cookingType = 233;
			pc.ability.addDamageReduction(3 * flag_val);
			pc.addMaxHp(50 * flag_val);
			pc.add_exp_boosting_ratio(6 * flag_val);
			pc.ability.addPVPDamageReduction(2 * flag_val);
			pc.resistance.addToleranceAll(2 * flag_val);
			break;
		default:
			break;
		}
		
		if (cookingId >= COOKING_1_0_N && cookingId <= COOKING_1_6_N
				|| cookingId >= COOKING_1_0_S && cookingId <= COOKING_1_6_S
				|| cookingId >= COOKING_1_8_N && cookingId <= COOKING_1_14_N
				|| cookingId >= COOKING_1_8_S && cookingId <= COOKING_1_14_S
				|| cookingId >= COOKING_1_16_N && cookingId <= COOKING_1_22_N
				|| cookingId >= COOKING_1_16_S && cookingId <= COOKING_1_22_S
				|| cookingId >= COOK_STR && cookingId <= COOK_INT
				|| cookingId >= BLESS_COOK_STR && cookingId <= BLESS_COOK_INT
				|| cookingId == COOK_METIS_NORMAL
				|| cookingId >= COOK_ADEN_SPECIAL_STEAK && cookingId <= COOK_ADEN_SPECIAL_SALAD
				|| cookingId >= BLESS_COOK_ADEN_SPECIAL_STEAK && cookingId <= BLESS_COOK_ADEN_SPECIAL_SALAD) {
			pc.setCookingId(flag ? cookingId : 0);
		} else {
			pc.setSoupId(flag ? cookingId : 0);
		}
		
		pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_COOKING, cookingType, flag ? time : 0), true);// 버프 아이콘
		pc.sendPackets(new S_OwnCharStatus(pc), true);
		pc.sendPackets(new S_OwnCharAttrDef(pc), true);
	}
}
