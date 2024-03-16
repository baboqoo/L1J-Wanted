package l1j.server.server.utils;

public class CalcDexStat {
	
	/**
	 * 원거리 대미지
	 * @param dex
	 * @return 원거리 대미지
	 */
	public static int longDamage(int dex) {
		if (dex < 9) {
			return 2;
		}
		int temp = 2 + ((dex + ~0x00000005) / 3);
		if (dex >= 20) {
			temp++;
		}
		if (dex >= 25) {
			temp++;
		}
		if (dex >= 35) {
			temp++;
		}
		if (dex >= 45) {
			temp += 3;
		}
		if (dex >= 55) {
			temp += 5;
		}
		if (dex >= 60) {
			temp += 5;
		}
		return temp;
	}
	
	/**
	 * 원거리 명중
	 * @param dex
	 * @return 원거리 명중
	 */
	public static int longHitup(int dex) {
		if (dex <= 7) {
			return -3;
		}
		int temp = -3 + (dex + ~0x00000006);
		if (dex >= 20) {
			temp++;
		}
		if (dex >= 25) {
			temp++;
		}
		if (dex >= 35) {
			temp++;
		}
		if (dex >= 45) {
			temp += 3;
		}
		if (dex >= 55) {
			temp += 5;
		}
		if (dex >= 60) {
			temp += 5;
		}
		return temp;
	}
	
	/**
	 * 원거리 치명타
	 * @param dex
	 * @return 원거리 치명타
	 */
	public static int longCritical(int dex) {
		int i = 0;
		if (dex >= 40) {
			i++;
		}
		if (dex >= 45) {
			i++;
		}
		if (dex >= 50) {
			i++;
		}
		if (dex >= 55) {
			i += 2;
		}
		if (dex >= 60) {
			i += 2;
		}
		if (dex >= 65) {
			i++;
		}
		if (dex >= 70) {
			i++;
		}
		if (dex >= 75) {
			i++;
		}
		return i;
	}
	
	/**
	 * AC보너스
	 * @param dex
	 * @return AC보너스
	 */
	public static int acBonus(int dex) {
		if (dex < 9) {
			return -2;
		}
		return -2 + (((dex + ~0x00000005) / 3) * -1);
	}
	
	private static final int[] ER_ARRAY = {
		3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 5,		// 0~10
		5, 6, 6, 7, 7, 8, 8, 9, 9, 10,			// 11~20
		10, 11, 11, 12, 12, 13, 13, 14, 14, 15,	// 21~30
		15, 16, 16, 17, 17, 18, 18, 19, 19, 20,	// 31~40
		20, 21, 21, 22, 22						// 41~45
	};
	
	/**
	 * er 보너스
	 * @param dex
	 * @return er 보너스
	 */
	public static int evasionBonus(int dex) {
		if (dex < 0) {
			return ER_ARRAY[0];
		}
		if (dex > ER_ARRAY.length + ~0x00000000) {
			return ER_ARRAY[ER_ARRAY.length + ~0x00000000];
		}
		return ER_ARRAY[dex];
	}
	
	private CalcDexStat() {}
	
}

