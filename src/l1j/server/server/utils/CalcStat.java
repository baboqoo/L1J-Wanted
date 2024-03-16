package l1j.server.server.utils;

import l1j.server.server.model.Instance.L1PcInstance;

public class CalcStat {
	
	/**
	 * 무게 보너스
	 * @param str
	 * @param con
	 * @return 무게 보너스
	 */
	public static int carryBonus(int str, int con) {
		int maxWeight = 1000;
		int stat = (str + con) >> 0x00000001;
		if (stat <= 0) {
			return maxWeight;
		}
		maxWeight += stat * 100;
		return maxWeight;
	}
	
	/**
	 * 각 클래스의 시작 hp
	 * @param charType
	 * @return
	 */
	public static int startHp(int charType){
		switch (charType) {
		case 0:return 14;// 군주
		case 1:return 16;// 기사
		case 2:return 15;// 요정
		case 3:return 12;// 법사
		case 4:return 12;// 다엘
		case 5:return 16;// 용기사
		case 6:return 14;// 환술사
		case 7:return 16;// 전사
		case 8:return 15;// 검사
		case 9:return 15;// 창기사
		default:return 0;
		}
	}
	
	/**
	 * 각 클래스의 시작 mp
	 * @param charType
	 * @param wis
	 * @return
	 */
	public static int startMp(int charType, int wis){
		switch (charType) {
		case 0:// 군주
			switch(wis){
			case 0:case 1:case 2:case 3:case 4:case 5:case 6:case 7:case 8:case 9:case 10:case 11:
				return 2 + CommonUtil.random(2);
			case 12:case 13:case 14:
				return 3 + CommonUtil.random(3);
			case 15:case 16:case 17:case 18:case 19:
				return 4 + CommonUtil.random(3);
			case 20:
				return 5 + CommonUtil.random(3);
			}
			break;
		case 1:// 기사
			switch(wis){
			case 0:case 1:case 2:case 3:case 4:case 5:case 6:case 7:case 8:case 9:
				return CommonUtil.random(3);
			case 10:case 11:case 12:case 13:
				return 1 + CommonUtil.random(3);
			}
			break;
		case 2:// 요정
			switch(wis){
			case 0:case 1:case 2:case 3:case 4:case 5:case 6:case 7:case 8:case 9:case 10:case 11:case 12:case 13:case 14:
				return 4 + CommonUtil.random(4);
			case 15:case 16:case 17:
				return 5 + CommonUtil.random(4);
			case 18:case 19:
				return 5 + CommonUtil.random(6);
			case 20:
				return 7 + CommonUtil.random(4);
			}
			break;
		case 3:// 법사
			switch(wis){
			case 0:case 1:case 2:case 3:case 4:case 5:case 6:case 7:case 8:case 9:case 10:case 11:case 12:case 13:case 14:
				return 6 + CommonUtil.random(5);
			case 15:case 16:case 17:
				return 8 + CommonUtil.random(5);
			case 18:case 19:
				return 8 + CommonUtil.random(7);
			case 20:
				return 10 + CommonUtil.random(5);
			}
			break;
		case 4:// 다엘
			switch(wis){
			case 0:case 1:case 2:case 3:case 4:case 5:case 6:case 7:case 8:case 9:case 10:case 11:
				return 4 + CommonUtil.random(2);
			case 12:case 13:case 14:
				return 4 + CommonUtil.random(4);
			case 15:case 16:case 17:
				return 5 + CommonUtil.random(4);
			}
			break;
		case 5:// 용기사
			switch(wis){
			case 0:case 1:case 2:case 3:case 4:case 5:case 6:case 7:case 8:case 9:case 10:case 11:case 12:case 13:case 14:
				return 2 + CommonUtil.random(2);
			case 15:case 16:case 17:
				return 3 + CommonUtil.random(2);
			case 18:
				return 3 + CommonUtil.random(3);
			}
			break;
		case 6:// 환술사
			switch(wis){
			case 0:case 1:case 2:case 3:case 4:case 5:case 6:case 7:case 8:case 9:case 10:case 11:case 12:case 13:case 14:
				return 4 + CommonUtil.random(4);
			case 15:case 16:case 17:
				return 6 + CommonUtil.random(4);
			case 18:case 19:
				return 6 + CommonUtil.random(6);
			case 20:
				return 7 + CommonUtil.random(5);
			}
			break;
		case 7:// 전사
			switch(wis){
			case 0:case 1:case 2:case 3:case 4:case 5:case 6:case 7:case 8:
				return CommonUtil.random(2);
			case 9:
				return CommonUtil.random(3);
			case 10:case 11:
				return 1 + CommonUtil.random(2);
			}
			break;
		case 8:// 검사
			switch(wis){
			case 0:case 1:case 2:case 3:case 4:case 5:case 6:case 7:case 8:case 9:case 10:case 11:case 12:case 13:case 14:
				return 2 + CommonUtil.random(2);
			case 15:
				return 3 + CommonUtil.random(2);
			}
			break;
		case 9:// 창기사
			switch(wis){
			case 0:case 1:case 2:case 3:case 4:case 5:case 6:case 7:case 8:case 9:case 10:case 11:case 12:case 13:case 14:
				return 2 + CommonUtil.random(2);
			case 15:case 16:case 17:
				return 3 + CommonUtil.random(2);
			case 18:
				return 3 + CommonUtil.random(3);
			}
			break;
		}
		return 0;
	}
	
	public static short initHp(L1PcInstance pc){
		if (pc.isCrown()) {
			return 14;
		}
		if (pc.isKnight()) {
			return 16;
		}
		if (pc.isElf()) {
			return 15;
		}
		if (pc.isWizard()) {
			return 12;
		}
		if (pc.isDarkelf()) {
			return 12;
		}
		if (pc.isDragonknight()) {
			return 16;
		}
		if (pc.isIllusionist()) {
			return 14;
		}
		if (pc.isWarrior()) {
			byte baseCon = pc.getAbility().getBaseCon();
			return baseCon >= 19 ? (short)18 : (baseCon >= 17 ? (short)17 : (short)16);
		}
		if (pc.isFencer()) {
			return 16;
		}
		if (pc.isLancer()) {
			return 18;
		}
		return 0;
	}
	
	public static short initMp(L1PcInstance pc){
		if (pc.isCrown()) { // CROWN
			switch (pc.getAbility().getBaseWis()) {
			case 11:
				return 2;
			case 12:case 13:case 14:case 15:
				return 3;
			case 16:case 17:case 18:
				return 4;
			default:
				return 2;
			}
		}
		if (pc.isKnight()) { // KNIGHT
			switch (pc.getAbility().getBaseWis()) {
			case 9:case 10:case 11:
				return 1;
			case 12:case 13:
				return 2;
			default:
				return 1;
			}
		}
		if (pc.isElf()) { // ELF
			switch (pc.getAbility().getBaseWis()) {
			case 12:case 13:case 14:case 15:
				return 4;
			case 16:case 17:case 18:
				return 6;
			default:
				return 4;
			}
		}
		if (pc.isWizard()) { // WIZ
			switch (pc.getAbility().getBaseWis()) {
			case 12:case 13:case 14:case 15:
				return 6;
			case 16:case 17:case 18:
				return 8;
			default:
				return 6;
			}
		}
		if (pc.isDarkelf()) { // DE
			switch (pc.getAbility().getBaseWis()) {
			case 10:case 11:
				return 3;
			case 12:case 13:case 14:case 15:
				return 4;
			case 16:case 17:case 18:
				return 6;
			default:
				return 3;
			}
		}
		if (pc.isDragonknight()) { // 용기사
			return 2;
		}
		if (pc.isIllusionist()) { // 환술사
			switch (pc.getAbility().getBaseWis()) {
			case 10:case 11:case 12:case 13:case 14:case 15:
				return 5;
			case 16:case 17:case 18:
				return 6;
			default:
				return 5;
			}
		}
		if(pc.isWarrior()) {
			switch (pc.getAbility().getBaseWis()) {
			case 9:case 10:case 11:
				return 1;
			case 12:case 13:
				return 2;
			default:
				return 1;
			}
		}
		if (pc.isFencer()) {
			switch (pc.getAbility().getBaseWis()) {
			case 11:
				return 2;
			case 12:case 13:
				return 3;
			case 14:case 15:
				return 4;
			default:
				return 2;
			}
		}
		if (pc.isLancer()) {
			switch (pc.getAbility().getBaseWis()) {
			case 11:
				return 2;
			case 12:case 13:
				return 3;
			case 14:case 15:
				return 4;
			default:
				return 2;
			}
		}
		return 0;
	}
	
}

