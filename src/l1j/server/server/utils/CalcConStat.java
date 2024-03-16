package l1j.server.server.utils;

import l1j.server.Config;

public class CalcConStat {
	
	/**
	 * HP 상승 보너스
	 * @param con
	 * @return HP
	 */
	public static int increaseHpBonus(int con) {
		if (con >= 60) {
			return 1000;
		}
		if (con >= 55) {
			return 800;
		}
		if (con >= 45) {
			return 600;
		}
		if (con >= 35) {
			return 400;
		}
		if (con >= 25) {
			return 200;
		}
		return 0;
	}
	
	/**
	 * 각 클래스의 레벨업시 증가되는 hp
	 * @param charType
	 * @param con
	 * @return HP상승치
	 */
	public static int levelUpHp(int charType, int totalCon){
		int defaultHp = 0, defaultStat	= 0;
		switch (charType) {
		case 0:defaultHp = 19;defaultStat	= 12;break;// 군주
		case 1:defaultHp = 27;defaultStat	= 16;break;// 기사
		case 2:defaultHp = 16;defaultStat	= 12;break;// 요정
		case 3:defaultHp = 7;defaultStat	= 12;break;// 법사
		case 4:defaultHp = 17;defaultStat	= 12;break;// 다엘
		case 5:defaultHp = 24;defaultStat	= 14;break;// 용기사
		case 6:defaultHp = 13;defaultStat	= 12;break;// 환술사
		case 7:defaultHp = 28;defaultStat	= 16;break;// 전사
		case 8:defaultHp = 20;defaultStat	= 15;break;// 검사
		case 9:defaultHp = 31;defaultStat	= 16;break;// 창기사
		default:return 0;
		}
		if (totalCon > defaultStat && totalCon <= 25) {
			defaultHp += totalCon - defaultStat;
		} else if (totalCon > 25 && totalCon <= 55) {
			defaultHp += (25 - defaultStat) + ((totalCon + ~0x00000018) >> 0x00000001);
		} else if (totalCon > 55) {
			defaultHp += (25 - defaultStat) + 15;
		}
		return defaultHp;
	}

	public static int levelUpHp(int charType, int baseMaxHp, int totalCon) {
		int returnhp = levelUpHp(charType, totalCon) + CommonUtil.random(2);
		switch (charType) {
		case 0:// 군주
			if (baseMaxHp + returnhp > Config.CHA.PRINCE_MAX_HP) {
				return (Config.CHA.PRINCE_MAX_HP - baseMaxHp);
			}
			return returnhp;
		case 1:// 기사
			if (baseMaxHp + returnhp > Config.CHA.KNIGHT_MAX_HP) {
				return (Config.CHA.KNIGHT_MAX_HP - baseMaxHp);
			}
			return returnhp;
		case 2:// 요정
			if (baseMaxHp + returnhp > Config.CHA.ELF_MAX_HP) {
				return (Config.CHA.ELF_MAX_HP - baseMaxHp);
			}
			return returnhp;
		case 3:// 법사
			if (baseMaxHp + returnhp > Config.CHA.WIZARD_MAX_HP) {
				return (Config.CHA.WIZARD_MAX_HP - baseMaxHp);
			}
			return returnhp;
		case 4:// 다엘
			if (baseMaxHp + returnhp > Config.CHA.DARKELF_MAX_HP) {
				return (Config.CHA.DARKELF_MAX_HP - baseMaxHp);
			}
			return returnhp;
		case 5:// 용기사
			if (baseMaxHp + returnhp > Config.CHA.DRAGONKNIGHT_MAX_HP) {
				return (Config.CHA.DRAGONKNIGHT_MAX_HP - baseMaxHp);
			}
			return returnhp;
		case 6:// 환술사
			if (baseMaxHp + returnhp > Config.CHA.ILLUSIONIST_MAX_HP) {
				return (Config.CHA.ILLUSIONIST_MAX_HP - baseMaxHp);
			}
			return returnhp;
		case 7:// 전사
			if (baseMaxHp + returnhp > Config.CHA.WARRIOR_MAX_HP) {
				return (Config.CHA.WARRIOR_MAX_HP - baseMaxHp);
			}
			return returnhp;
		case 8:// 검사
			if (baseMaxHp + returnhp > Config.CHA.FENCER_MAX_HP) {
				return (Config.CHA.FENCER_MAX_HP - baseMaxHp);
			}
			return returnhp;
		case 9:// 창기사
			if (baseMaxHp + returnhp > Config.CHA.LANCER_MAX_HP) {
				return (Config.CHA.LANCER_MAX_HP - baseMaxHp);
			}
			return returnhp;
		default:
			return returnhp;
		}
	}
	
	private static final int[] HPR_ARRAY = { 
		5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,		// 0~10
		5, 6, 6, 7, 7, 8, 8, 9, 9, 10,			// 11~20
		10, 11, 11, 12, 13, 14, 14, 15, 15, 16,	// 21~30
		16, 17, 17, 18, 19, 20, 20, 21, 21, 22,	// 31~40
		22, 23, 23, 24, 27, 27, 27, 27, 27, 28,	// 41~50 
		28, 28, 28, 28, 33, 33, 33, 33, 33, 38	// 51~60
	};
	
	/**
	 * HP 회복(틱)
	 * @param con
	 * @return 회복량
	 */
	public static int hpRegen(int con) {
		if (con < 0) {
			return HPR_ARRAY[0];
		}
		if (con > HPR_ARRAY.length + ~0x00000000) {
			return HPR_ARRAY[HPR_ARRAY.length + ~0x00000000];
		}
		return HPR_ARRAY[con];
	}
	
	/**
	 * HP 물약 회복 증가
	 * @param con
	 * @return 회복량
	 */
	public static int hpIncPotion(int con) {
		int i = 0;
		if (con >= 20) {
			i++;
		}
		if (con >= 30) {
			i++;
		}
		if (con >= 35) {
			i++;
		}
		if (con >= 40) {
			i++;
		}
		if (con >= 45) {
			i += 2;
		}
		if (con >= 55) {
			i += 4;
		}
		if (con >= 60) {
			i += 4;
		}
		return i;
	}
	
	private CalcConStat(){}
	
}

