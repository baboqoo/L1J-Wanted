package l1j.server.server.utils;

import l1j.server.Config;

public class CalcWisStat {
	
	/**
	 * MP 상승 보너스
	 * @param wis
	 * @return MP
	 */
	public static int increaseMpBonus(int wis) {
		if (wis >= 55) {
			return 200;
		}
		if (wis >= 45) {
			return 150;
		}
		if (wis >= 35) {
			return 100;
		}
		if (wis >= 25) {
			return 50;
		}
		return 0;
	}
	
	/**
	 * 각 클래스의 LVUP시의 MP상승치를 돌려준다
	 * 
	 * @param charType
	 * @param wis
	 * @return MP상승치 min max
	 */
	public static int[] levelUpMp(int charType, int wis) {
		switch (charType) {
		case 0:// 군주
			return crownMp(wis);
		case 1:// 기사
			return knightMp(wis);
		case 2:// 요정
			return elfMp(wis);
		case 3:// 법사
			return wizardMp(wis);
		case 4:// 다크엘프
			return darkelfMp(wis);
		case 5:// 용기사
			return dragonknightMp(wis);
		case 6:// 환술사
			return illusionistMp(wis);
		case 7:// 전사
			return warriorMp(wis);
		case 8:// 검사
			return fencerMp(wis);
		case 9:// 창기사
			return lancerMp(wis);
		default:
			return null;
		}
	}
	
	static final int[][] CROWN_MP_ARRAY = {
		new int[]{3, 4}, new int[]{3, 5}, new int[]{4, 6}, new int[]{5, 7}, new int[]{5, 8}, 
		new int[]{5, 9}, new int[]{6, 9}, new int[]{6, 10}, new int[]{7, 11}, new int[]{7, 12}, 
		new int[]{8, 12}, new int[]{8, 13}, new int[]{8, 14}, new int[]{9, 14}, new int[]{9, 15}, 
		new int[]{10, 15}, new int[]{10, 16}, new int[]{10, 17}, new int[]{11, 17}
	};
	
	static int[] crownMp(int wis) {
		if (wis <= 11) {
			return CROWN_MP_ARRAY[0];
		}
		if (wis <= 14) {
			return CROWN_MP_ARRAY[1];
		}
		if (wis <= 19) {
			return CROWN_MP_ARRAY[2];
		}
		if (wis <= 20) {
			return CROWN_MP_ARRAY[3];
		}
		if (wis <= 23) {
			return CROWN_MP_ARRAY[4];
		}
		if (wis <= 24) {
			return CROWN_MP_ARRAY[5];
		}
		if (wis <= 26) {
			return CROWN_MP_ARRAY[6];
		}
		if (wis <= 29) {
			return CROWN_MP_ARRAY[7];
		}
		if (wis <= 32) {
			return CROWN_MP_ARRAY[8];
		}
		if (wis <= 34) {
			return CROWN_MP_ARRAY[9];
		}
		if (wis <= 35) {
			return CROWN_MP_ARRAY[10];
		}
		if (wis <= 38) {
			return CROWN_MP_ARRAY[11];
		}
		if (wis <= 39) {
			return CROWN_MP_ARRAY[12];
		}
		if (wis <= 41) {
			return CROWN_MP_ARRAY[13];
		}
		if (wis <= 44) {
			return CROWN_MP_ARRAY[14];
		}
		if (wis <= 47) {
			return CROWN_MP_ARRAY[15];
		}
		if (wis <= 50) {
			return CROWN_MP_ARRAY[16];
		}
		if (wis <= 54) {
			return CROWN_MP_ARRAY[17];
		}
		return CROWN_MP_ARRAY[18];
	}
	
	static final int[][] KNIGHT_MP_ARRAY = {
		new int[]{0, 2}, new int[]{1, 2}, new int[]{2, 3}, new int[]{2, 4}, new int[]{2, 5},
		new int[]{3, 5}, new int[]{3, 6}, new int[]{4, 6}, new int[]{4, 7}, new int[]{4, 8},
		new int[]{5, 8}, new int[]{5, 9}, new int[]{6, 10}
	};
	
	static int[] knightMp(int wis) {
		if (wis <= 9) {
			return KNIGHT_MP_ARRAY[0];
		}
		if (wis <= 14) {
			return KNIGHT_MP_ARRAY[1];
		}
		if (wis <= 17) {
			return KNIGHT_MP_ARRAY[2];
		}
		if (wis <= 23) {
			return KNIGHT_MP_ARRAY[3];
		}
		if (wis <= 24) {
			return KNIGHT_MP_ARRAY[4];
		}
		if (wis <= 26) {
			return KNIGHT_MP_ARRAY[5];
		}
		if (wis <= 29) {
			return KNIGHT_MP_ARRAY[6];
		}
		if (wis <= 32) {
			return KNIGHT_MP_ARRAY[7];
		}
		if (wis <= 35) {
			return KNIGHT_MP_ARRAY[8];
		}
		if (wis <= 39) {
			return KNIGHT_MP_ARRAY[9];
		}
		if (wis <= 41) {
			return KNIGHT_MP_ARRAY[10];
		}
		if (wis <= 44) {
			return KNIGHT_MP_ARRAY[11];
		}
		return KNIGHT_MP_ARRAY[12];
	}
	
	static final int[][] ELF_MP_ARRAY = {
		new int[]{4, 7}, new int[]{5, 8}, new int[]{5, 10}, new int[]{7, 10}, new int[]{7, 11},
		new int[]{7, 13}, new int[]{8, 13}, new int[]{8, 14}, new int[]{10, 16}, new int[]{10, 17},
		new int[]{11, 17}, new int[]{11, 18}, new int[]{12, 18}, new int[]{14, 20}, new int[]{14, 21},
		new int[]{15, 21}
	};
	
	static int[] elfMp(int wis) {
		if (wis <= 14) {
			return ELF_MP_ARRAY[0];
		}
		if (wis <= 17) {
			return ELF_MP_ARRAY[1];
		}
		if (wis <= 19) {
			return ELF_MP_ARRAY[2];
		}
		if (wis <= 20) {
			return ELF_MP_ARRAY[3];
		}
		if (wis <= 23) {
			return ELF_MP_ARRAY[4];
		}
		if (wis <= 24) {
			return ELF_MP_ARRAY[5];
		}
		if (wis <= 26) {
			return ELF_MP_ARRAY[6];
		}
		if (wis <= 29) {
			return ELF_MP_ARRAY[7];
		}
		if (wis <= 32) {
			return ELF_MP_ARRAY[8];
		}
		if (wis <= 34) {
			return ELF_MP_ARRAY[9];
		}
		if (wis <= 35) {
			return ELF_MP_ARRAY[10];
		}
		if (wis <= 38) {
			return ELF_MP_ARRAY[11];
		}
		if (wis <= 39) {
			return ELF_MP_ARRAY[12];
		}
		if (wis <= 41) {
			return ELF_MP_ARRAY[13];
		}
		if (wis <= 44) {
			return ELF_MP_ARRAY[14];
		}
		return ELF_MP_ARRAY[15];
	}
	
	static final int[][] WIZARD_MP_ARRAY = {
		new int[]{6, 10}, new int[]{8, 12}, new int[]{8, 14}, new int[]{10, 14}, new int[]{10, 16},
		new int[]{10, 18}, new int[]{12, 18}, new int[]{12, 20}, new int[]{14, 22}, new int[]{14, 24},
		new int[]{16, 24}, new int[]{16, 26}, new int[]{16, 28}, new int[]{18, 28}, new int[]{18, 30},
		new int[]{20, 32}, new int[]{20, 34}, new int[]{22, 34}, new int[]{22, 36}, new int[]{22, 38},
		new int[]{24, 38}
	};
	
	static int[] wizardMp(int wis) {
		if (wis <= 14) {
			return WIZARD_MP_ARRAY[0];
		}
		if (wis <= 17) {
			return WIZARD_MP_ARRAY[1];
		}
		if (wis <= 19) {
			return WIZARD_MP_ARRAY[2];
		}
		if (wis <= 20) {
			return WIZARD_MP_ARRAY[3];
		}
		if (wis <= 23) {
			return WIZARD_MP_ARRAY[4];
		}
		if (wis <= 24) {
			return WIZARD_MP_ARRAY[5];
		}
		if (wis <= 26) {
			return WIZARD_MP_ARRAY[6];
		}
		if (wis <= 29) {
			return WIZARD_MP_ARRAY[7];
		}
		if (wis <= 32) {
			return WIZARD_MP_ARRAY[8];
		}
		if (wis <= 34) {
			return WIZARD_MP_ARRAY[9];
		}
		if (wis <= 35) {
			return WIZARD_MP_ARRAY[10];
		}
		if (wis <= 38) {
			return WIZARD_MP_ARRAY[11];
		}
		if (wis <= 39) {
			return WIZARD_MP_ARRAY[12];
		}
		if (wis <= 41) {
			return WIZARD_MP_ARRAY[13];
		}
		if (wis <= 44) {
			return WIZARD_MP_ARRAY[14];
		}
		if (wis <= 47) {
			return WIZARD_MP_ARRAY[15];
		}
		if (wis <= 49) {
			return WIZARD_MP_ARRAY[16];
		}
		if (wis <= 50) {
			return WIZARD_MP_ARRAY[17];
		}
		if (wis <= 53) {
			return WIZARD_MP_ARRAY[18];
		}
		if (wis <= 54) {
			return WIZARD_MP_ARRAY[19];
		}
		return WIZARD_MP_ARRAY[20];
	}
	
	static final int[][] DARKELF_MP_ARRAY = {
		new int[]{4, 5}, new int[]{4, 7}, new int[]{5, 8}, new int[]{5, 10}, new int[]{7, 10},
		new int[]{7, 11}, new int[]{7, 13}, new int[]{8, 13}, new int[]{8, 14}, new int[]{10, 16},
		new int[]{10, 17}, new int[]{11, 17}, new int[]{11, 18}, new int[]{12, 18}, new int[]{14, 20},
		new int[]{14, 21}, new int[]{15, 21}
	};
	
	static int[] darkelfMp(int wis) {
		if (wis <= 11) {
			return DARKELF_MP_ARRAY[0];
		}
		if (wis <= 14) {
			return DARKELF_MP_ARRAY[1];
		}
		if (wis <= 17) {
			return DARKELF_MP_ARRAY[2];
		}
		if (wis <= 19) {
			return DARKELF_MP_ARRAY[3];
		}
		if (wis <= 20) {
			return DARKELF_MP_ARRAY[4];
		}
		if (wis <= 23) {
			return DARKELF_MP_ARRAY[5];
		}
		if (wis <= 24) {
			return DARKELF_MP_ARRAY[6];
		}
		if (wis <= 26) {
			return DARKELF_MP_ARRAY[7];
		}
		if (wis <= 29) {
			return DARKELF_MP_ARRAY[8];
		}
		if (wis <= 32) {
			return DARKELF_MP_ARRAY[9];
		}
		if (wis <= 34) {
			return DARKELF_MP_ARRAY[10];
		}
		if (wis <= 35) {
			return DARKELF_MP_ARRAY[11];
		}
		if (wis <= 38) {
			return DARKELF_MP_ARRAY[12];
		}
		if (wis <= 39) {
			return DARKELF_MP_ARRAY[13];
		}
		if (wis <= 41) {
			return DARKELF_MP_ARRAY[14];
		}
		if (wis <= 44) {
			return DARKELF_MP_ARRAY[15];
		}
		return DARKELF_MP_ARRAY[16];
	}
	
	static final int[][] DRAGONKNIGHT_MP_ARRAY = {
		new int[]{2, 3}, new int[]{3, 4}, new int[]{3, 5}, new int[]{3, 6}, new int[]{4, 6},
		new int[]{4, 7}, new int[]{5, 8}, new int[]{5, 9}, new int[]{5, 10}, new int[]{6, 10},
		new int[]{7, 11}
	};
	
	static int[] dragonknightMp(int wis) {
		if (wis <= 14) {
			return DRAGONKNIGHT_MP_ARRAY[0];
		}
		if (wis <= 17) {
			return DRAGONKNIGHT_MP_ARRAY[1];
		}
		if (wis <= 23) {
			return DRAGONKNIGHT_MP_ARRAY[2];
		}
		if (wis <= 24) {
			return DRAGONKNIGHT_MP_ARRAY[3];
		}
		if (wis <= 26) {
			return DRAGONKNIGHT_MP_ARRAY[4];
		}
		if (wis <= 29) {
			return DRAGONKNIGHT_MP_ARRAY[5];
		}
		if (wis <= 35) {
			return DRAGONKNIGHT_MP_ARRAY[6];
		}
		if (wis <= 38) {
			return DRAGONKNIGHT_MP_ARRAY[7];
		}
		if (wis <= 39) {
			return DRAGONKNIGHT_MP_ARRAY[8];
		}
		if (wis <= 44) {
			return DRAGONKNIGHT_MP_ARRAY[9];
		}
		return DRAGONKNIGHT_MP_ARRAY[10];
	}
	
	static final int[][] ILLUSIONIST_MP_ARRAY = {
		new int[]{4, 7}, new int[]{6, 9}, new int[]{6, 11}, new int[]{7, 11}, new int[]{7, 12},
		new int[]{7, 14}, new int[]{9, 14}, new int[]{9, 16}, new int[]{11, 18}, new int[]{11, 19},
		new int[]{12, 19}, new int[]{12, 21}, new int[]{12, 23}, new int[]{14, 23}, new int[]{14, 24},
		new int[]{16, 24}
	};
	
	static int[] illusionistMp(int wis) {
		if (wis <= 14) {
			return ILLUSIONIST_MP_ARRAY[0];
		}
		if (wis <= 17) {
			return ILLUSIONIST_MP_ARRAY[1];
		}
		if (wis <= 19) {
			return ILLUSIONIST_MP_ARRAY[2];
		}
		if (wis <= 20) {
			return ILLUSIONIST_MP_ARRAY[3];
		}
		if (wis <= 23) {
			return ILLUSIONIST_MP_ARRAY[4];
		}
		if (wis <= 24) {
			return ILLUSIONIST_MP_ARRAY[5];
		}
		if (wis <= 26) {
			return ILLUSIONIST_MP_ARRAY[6];
		}
		if (wis <= 29) {
			return ILLUSIONIST_MP_ARRAY[7];
		}
		if (wis <= 32) {
			return ILLUSIONIST_MP_ARRAY[8];
		}
		if (wis <= 34) {
			return ILLUSIONIST_MP_ARRAY[9];
		}
		if (wis <= 35) {
			return ILLUSIONIST_MP_ARRAY[10];
		}
		if (wis <= 38) {
			return ILLUSIONIST_MP_ARRAY[11];
		}
		if (wis <= 39) {
			return ILLUSIONIST_MP_ARRAY[12];
		}
		if (wis <= 41) {
			return ILLUSIONIST_MP_ARRAY[13];
		}
		if (wis <= 44) {
			return ILLUSIONIST_MP_ARRAY[14];
		}
		return ILLUSIONIST_MP_ARRAY[15];
	}
	
	static final int[][] WARRIOR_MP_ARRAY = {
		new int[]{0, 1}, new int[]{0, 2}, new int[]{1, 2}, new int[]{2, 3}, new int[]{2, 4},
		new int[]{2, 5}, new int[]{3, 5}, new int[]{3, 6}, new int[]{4, 6}, new int[]{4, 7},
		new int[]{4, 8}, new int[]{5, 8}, new int[]{5, 9}, new int[]{6, 10}
	};
	
	static int[] warriorMp(int wis) {
		if (wis <= 8) {
			return WARRIOR_MP_ARRAY[0];
		}
		if (wis <= 9) {
			return WARRIOR_MP_ARRAY[1];
		}
		if (wis <= 14) {
			return WARRIOR_MP_ARRAY[2];
		}
		if (wis <= 17) {
			return WARRIOR_MP_ARRAY[3];
		}
		if (wis <= 23) {
			return WARRIOR_MP_ARRAY[4];
		}
		if (wis <= 24) {
			return WARRIOR_MP_ARRAY[5];
		}
		if (wis <= 26) {
			return WARRIOR_MP_ARRAY[6];
		}
		if (wis <= 29) {
			return WARRIOR_MP_ARRAY[7];
		}
		if (wis <= 32) {
			return WARRIOR_MP_ARRAY[8];
		}
		if (wis <= 35) {
			return WARRIOR_MP_ARRAY[9];
		}
		if (wis <= 39) {
			return WARRIOR_MP_ARRAY[10];
		}
		if (wis <= 41) {
			return WARRIOR_MP_ARRAY[11];
		}
		if (wis <= 44) {
			return WARRIOR_MP_ARRAY[12];
		}
		return WARRIOR_MP_ARRAY[13];
	}
	
	static final int[][] FENCER_MP_ARRAY = {
		new int[]{2, 3}, new int[]{3, 4}, new int[]{3, 5}, new int[]{3, 6}, new int[]{4, 6},
		new int[]{4, 7}, new int[]{5, 7}, new int[]{5, 8}, new int[]{5, 9}, new int[]{5, 10},
		new int[]{6, 10}, new int[]{7, 11}
	};
	
	static int[] fencerMp(int wis) {
		if (wis <= 14) {
			return FENCER_MP_ARRAY[0];
		}
		if (wis <= 17) {
			return FENCER_MP_ARRAY[1];
		}
		if (wis <= 23) {
			return FENCER_MP_ARRAY[2];
		}
		if (wis <= 24) {
			return FENCER_MP_ARRAY[3];
		}
		if (wis <= 26) {
			return FENCER_MP_ARRAY[4];
		}
		if (wis <= 29) {
			return FENCER_MP_ARRAY[5];
		}
		if (wis <= 32) {
			return FENCER_MP_ARRAY[6];
		}
		if (wis <= 35) {
			return FENCER_MP_ARRAY[7];
		}
		if (wis <= 38) {
			return FENCER_MP_ARRAY[8];
		}
		if (wis <= 39) {
			return FENCER_MP_ARRAY[9];
		}
		if (wis <= 44) {
			return FENCER_MP_ARRAY[10];
		}
		return FENCER_MP_ARRAY[11];
	}
	
	static final int[][] LANCER_MP_ARRAY = {
		new int[]{2, 3}, new int[]{3, 4}, new int[]{3, 5}, new int[]{3, 6}, new int[]{4, 6},
		new int[]{4, 7}, new int[]{5, 7}, new int[]{5, 8}, new int[]{5, 9}, new int[]{6, 9},
		new int[]{6, 10}, new int[]{7, 11}
	};
	
	static int[] lancerMp(int wis) {
		if (wis <= 14) {
			return LANCER_MP_ARRAY[0];
		}
		if (wis <= 17) {
			return LANCER_MP_ARRAY[1];
		}
		if (wis <= 23) {
			return LANCER_MP_ARRAY[2];
		}
		if (wis <= 24) {
			return LANCER_MP_ARRAY[3];
		}
		if (wis <= 26) {
			return LANCER_MP_ARRAY[4];
		}
		if (wis <= 29) {
			return LANCER_MP_ARRAY[5];
		}
		if (wis <= 32) {
			return LANCER_MP_ARRAY[6];
		}
		if (wis <= 35) {
			return LANCER_MP_ARRAY[7];
		}
		if (wis <= 39) {
			return LANCER_MP_ARRAY[8];
		}
		if (wis <= 41) {
			return LANCER_MP_ARRAY[9];
		}
		if (wis <= 44) {
			return LANCER_MP_ARRAY[10];
		}
		return LANCER_MP_ARRAY[11];
	}
	
	public static int levelUpMp(int charType, int baseMaxMp, int wis) {
		int addMp = 0;
		if (wis <= 0) {
			addMp = CommonUtil.random(2);
		} else {
			int[] levelUp = levelUpMp(charType, wis);
			addMp = levelUp[0] + CommonUtil.random((levelUp[1] - levelUp[0]) + 1);
		}
		switch (charType) {
		case 0:// 군주
			if (baseMaxMp + addMp > Config.CHA.PRINCE_MAX_MP) {
				return (Config.CHA.PRINCE_MAX_MP - baseMaxMp);
			}
			return addMp;
		case 1:// 기사
			if (baseMaxMp + addMp > Config.CHA.KNIGHT_MAX_MP) {
				return (Config.CHA.KNIGHT_MAX_MP - baseMaxMp);
			}
			return addMp;
		case 2:// 요정
			if (baseMaxMp + addMp > Config.CHA.ELF_MAX_MP) {
				return (Config.CHA.ELF_MAX_MP - baseMaxMp);
			}
			return addMp;
		case 3:// 법사
			if (baseMaxMp + addMp > Config.CHA.WIZARD_MAX_MP) {
				return (Config.CHA.WIZARD_MAX_MP - baseMaxMp);
			}
			return addMp;
		case 4:// 다엘
			if (baseMaxMp + addMp > Config.CHA.DARKELF_MAX_MP) {
				return (Config.CHA.DARKELF_MAX_MP - baseMaxMp);
			}
			return addMp;
		case 5:// 용기사
			if (baseMaxMp + addMp > Config.CHA.DRAGONKNIGHT_MAX_MP) {
				return (Config.CHA.DRAGONKNIGHT_MAX_MP - baseMaxMp);
			}
			return addMp;
		case 6:// 환술사
			if (baseMaxMp + addMp > Config.CHA.ILLUSIONIST_MAX_MP) {
				return (Config.CHA.ILLUSIONIST_MAX_MP - baseMaxMp);
			}
			return addMp;
		case 7:// 전사
			if (baseMaxMp + addMp > Config.CHA.WARRIOR_MAX_MP) {
				return (Config.CHA.WARRIOR_MAX_MP - baseMaxMp);
			}
			return addMp;
		case 8:// 검사
			if (baseMaxMp + addMp > Config.CHA.FENCER_MAX_MP) {
				return (Config.CHA.FENCER_MAX_MP - baseMaxMp);
			}
			return addMp;
		case 9:// 창기사
			if (baseMaxMp + addMp > Config.CHA.LANCER_MAX_MP) {
				return (Config.CHA.LANCER_MAX_MP - baseMaxMp);
			}
			return addMp;
		default:
			return addMp;
		}
	}
	
	private static final int[] MPR_ARRAY = {
		1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2,		// 0~10
		2, 2, 2, 2, 3, 3, 3, 3, 3, 4,			// 11~20
		4, 4, 4, 4, 5, 5, 5, 5, 5, 6,			// 21~30
		6, 6, 6, 6, 7, 7, 7, 7, 7, 8,			// 31~40
		8, 8, 8, 8, 11, 11, 11, 11, 11, 12,		// 41~50
		12, 12, 12, 12, 17, 17, 17, 17, 22, 22	// 51~60
	};
	
	/**
	 * MP 회복(틱)
	 * @param wis
	 * @return 회복량
	 */
	public static int mpRegen(int wis) {
		if (wis < 0) {
			return MPR_ARRAY[0];
		}
		if (wis > MPR_ARRAY.length + ~0x00000000) {
			return MPR_ARRAY[MPR_ARRAY.length + ~0x00000000];
		}
		return MPR_ARRAY[wis];
	}
	
	private static final int[] MP_POTION_ARRAY = { 
		1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,		// 1~10
		1, 2, 2, 2, 3, 4, 4, 5, 5, 6,			// 11~20
		6, 7, 7, 8, 9, 10, 10, 11, 11, 12,		// 21~30
		12, 13, 13, 14, 15, 16, 16, 17, 17, 18,	// 31~40
		18, 19, 19, 20, 23, 23, 23, 23, 23, 24,	// 41~50
		24, 24, 24, 24, 29, 29, 29, 29, 29, 34	// 51~55
	};
	
	/**
	 * MP 물약 회복 증가(틱)
	 * @param wis
	 * @return 회복량
	 */
	public static int mpIncPotion(int wis) {
		if (wis < 0) {
			return MP_POTION_ARRAY[0];
		}
		if (wis > MP_POTION_ARRAY.length + ~0x00000000) {
			return MP_POTION_ARRAY[MP_POTION_ARRAY.length + ~0x00000000];
		}
		return MP_POTION_ARRAY[wis];
	}
	
	private static final int[] MR_CLASS_ARRAY = {
		10, 0, 25, 15, 10, 18, 20, 0, 0, 0
	};
	
	/**
	 * MR보너스
	 * @param type
	 * @param wis
	 * @return MR
	 */
	public static int mrBonus(int type, int wis) {
		if (type > MR_CLASS_ARRAY.length) {
			return 0;
		}
		int base = MR_CLASS_ARRAY[type];
		if (wis <= 10) {
			return base;
		}
		int temp = wis + ~0x00000009;
		base += temp << 0x00000002;
		return base;
	}
	
	private CalcWisStat() {}
}

