package l1j.server.server.model;

import java.util.Random;

import l1j.server.server.construct.item.L1ItemWeaponType;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class L1ArmorSkill {
	static final Random random = new Random(System.nanoTime());
	
	static int getRandomChance(){
		return random.nextInt(1000);
	}
	
	static int getActionChance(L1Character cha, int chance){
		return (chance * 10) + ((cha.ability.getItemSpellProbArmor() + cha.ability.getIncreaseArmorSkillProb()) / 10);
	}
	
	public static double getChanceDamage(L1PcInstance pc){
		// 룸티스의 검은빛 귀걸이 대미지 확률
		int[] roomtisDamage = pc.ability.getRoomtisEarringDamage();
		if (roomtisDamage == null || roomtisDamage[1] <= 0) {
			return 0D;
		}
		if (getRandomChance() < getActionChance(pc, roomtisDamage[1])) {
        	pc.send_effect(13931);
        	return (double)roomtisDamage[0];
        }
		return 0D;
	}
	
	public static double getChanceReduction(L1PcInstance pc, double dmg){
		// 룸티스의 붉은빛 귀걸이 대미지 감소 확률
		if (dmg <= 0) {
			return dmg;
		}
		int[] roomtisReduction = pc.ability.getRoomtisEarringReduction();
		if (roomtisReduction != null && roomtisReduction[1] > 0 && getRandomChance() < getActionChance(pc, roomtisReduction[1])) {
			dmg -= dmg <= roomtisReduction[0] ? dmg : roomtisReduction[0];
			pc.send_effect(12118);
		}
		
		// 스냅퍼의 체력반지 대미지 감소 확률
		if (dmg <= 0) {
			return dmg;
		}
		int snaperReduction = pc.ability.getSnaperRingReduction();
		if (snaperReduction > 0 && getRandomChance() < getActionChance(pc, snaperReduction)) {
			dmg -= dmg <= 20 ? dmg : 20;
			pc.send_effect(12118);
		}
		
		// 투구에 의한 대미지 감소
		if (dmg <= 0) {
			return dmg;
		}
		L1ItemInstance item = pc.getInventory().getEquippedHelmet();// 착용중인 핼멧
		if (item != null) {
			switch(item.getItemId()){
			// 반역자의 투구, 영웅의 투구 대미지 감소 확률
			case 22264:case 2000031:
				if (getRandomChance() <= getActionChance(pc, 1 + item.getEnchantLevel())) {
	            	dmg -= dmg <= 20 ? dmg : 20;
	            	pc.send_effect(6320);
	            }
				break;
			}
		}
		
		// 방패에 의한 대미지 감소
		if (dmg <= 0) {
			return dmg;
		}
		item = pc.getInventory().getEquippedShield();// 착용중인 방패
		if (item != null) {
			switch(item.getItemId()){
			// 반역자의 방패 대미지 감소 확률
			case 22263:
				if (getRandomChance() <= getActionChance(pc, 1 + (item.getEnchantLevel() << 1))) {
	            	dmg -= dmg <= 50 ? dmg : 50;
	            	pc.send_effect(6320);
	            }
				break;
			// 신성한요정족방패 대미지 감소 확률
			case 222355:case 5222355:
				if (getRandomChance() <= getActionChance(pc, 1 + (item.getEnchantLevel() << 1))) {
					dmg -= dmg <= 10 ? dmg : 10;
					pc.send_effect(14543);
				}
				break;
			}
		}
		return dmg;
	}
	
	public static void armorSkillAction(L1PcInstance pc){
		L1ItemInstance item = pc.getInventory().getEquippedArmor();// 착용중인 갑옷
		if (item == null) {
			return;
		}
		switch(item.getItemId()){
		case 222351:// 신성한 요정족 판금 갑옷
			elfArmor(pc, item);
			break;
		case 22200:case 22201:case 22202:case 22203:// 파푸리온 갑옷
			papuArmor(pc, item);
			break;
		case 22204:case 22205:case 22206:case 22207:// 린드비오르 갑옷
			rindArmor(pc, item);
			break;
		case 23000:case 23001:case 23002:// 할파스 갑옷
			halpasArmor(pc, item);
			break;
		default:
			break;
		}
	}
	
	static void elfArmor(L1PcInstance pc, L1ItemInstance item){
		if (getRandomChance() <= getActionChance(pc, 5 + (item.getEnchantLevel() << 1))) {
			int getHp = 25 + random.nextInt(6);
			pc.setCurrentHp(pc.getCurrentHp() + getHp);
			pc.send_effect(13429);
		}
	}
	
	static void papuArmor(L1PcInstance pc, L1ItemInstance item){
		if (getRandomChance() <= getActionChance(pc, 8)) {
			int plus = 0;
			if (item.getEnchantLevel() >= 7 && item.getEnchantLevel() <= 9) {
				plus = item.getEnchantLevel() - 6;
			} else if (item.getEnchantLevel() > 9) {
				plus = 3;
			}
			int getHp = 40 + random.nextInt(15) + (plus * 10);
			pc.setCurrentHp(pc.getCurrentHp() + getHp);
			pc.send_effect(2187);
		}
	}
	
	static void rindArmor(L1PcInstance pc, L1ItemInstance item){
		if (getRandomChance() <= getActionChance(pc, 8)) {
			short getMp = (short)(pc.getCurrentMp() + 5);
			switch (item.getItemId()) {
			case 22205:
				getMp += 3;
				break;
			case 22206:case 22207:
				getMp += 5;
				break;
			default:
				break;
			}
			pc.setCurrentMp(getMp);
			pc.send_effect(2188);
		}
	}

	static void halpasArmor(L1PcInstance pc, L1ItemInstance item){
		int getHp = 0, getMp = 0, plus = 0;
        if (item.getEnchantLevel() >= 0) {
        	plus = item.getEnchantLevel();
        }
    	if (getRandomChance() <= getActionChance(pc, 8 + plus)) {
    		getHp = 40 + random.nextInt(15) + (plus * 10);
            getMp = 10 + (plus << 1);
            pc.setCurrentHp(pc.getCurrentHp() + getHp);
            pc.setCurrentMp(pc.getCurrentMp() + getMp);
            int effectnum = plus >= 5 ? 19072 : (plus >= 3 ? 19070 : 19068);
            pc.send_effect(effectnum);
        }
	}
	
	public static boolean valakasArmor(L1PcInstance pc, L1ItemWeaponType weaponType, boolean magic){
		int critical = 0;
		if (pc.ability.getValakasShortCritical() > 0 
				&& L1ItemWeaponType.isShortWeapon(weaponType)
				&& !magic) {
			critical = pc.ability.getValakasShortCritical();
		} else if (pc.ability.getValakasLongCritical() > 0 
				&& L1ItemWeaponType.isLongWeapon(weaponType)
				&& !magic) {
			critical = pc.ability.getValakasLongCritical();
		} else if (pc.ability.getValakasMagicCritical() > 0 
				&& ((weaponType == L1ItemWeaponType.KEYRINGK && !magic) || magic)) {
			critical = pc.ability.getValakasMagicCritical();
		}
		if (critical <= 0) {
			return false;
		}
		if (getRandomChance() <= getActionChance(pc, critical)) {
			pc.send_effect(15841);
			return true;
		}
		return false;
	}
	
}

