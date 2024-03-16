package l1j.server.server.utils;

public class CalcIntelStat {
	
	/**
	 * 마법 대미지
	 * @param inte
	 * @return 마법 대미지
	 */
	public static int magicDamage(int inte) {
		if (inte < 15) {
			return 0;
		}
		int temp = (inte + ~0x00000009) / 5;
		if (inte >= 25) {
			temp++;
		}
		if (inte >= 35) {
			temp++;
		}
		if (inte >= 45) {
			temp += 3;
		}
		if (inte >= 55) {
			temp += 5;
		}
		if (inte >= 60) {
			temp += 5;
		}
		return temp;
	}
	
	/**
	 * 마법 명중
	 * @param inte
	 * @return 마법 명중
	 */
	public static int magicHitup(int inte) {
		if (inte < 9) {
			return -4;
		}
		int temp = ((inte + ~0x00000005) / 3) + ~0x00000003;
		if (inte >= 25) {
			temp++;
		}
		if (inte >= 35) {
			temp++;
		}
		if (inte >= 45) {
			temp += 3;
		}
		if (inte >= 55) {
			temp += 5;
		}
		if (inte >= 60) {
			temp += 5;
		}
		return temp;
	}
	
	/**
	 * 마법 치명타
	 * @param inte
	 * @return 마법 치명타
	 */
	public static int magicCritical(int inte) {
		int i = 0;
		if (inte >= 35) {
			i++;
		}
		if (inte >= 40) {
			i++;
		}
		if (inte >= 45) {
			i++;
		}
		if (inte >= 50) {
			i++;
		}
		if (inte >= 55) {
			i += 2;
		}
		if (inte >= 60) {
			i += 2;
		}
		if (inte >= 65) {
			i++;
		}
		if (inte >= 70) {
			i++;
		}
		return i;
	}
	
	/**
	 * 마법 보너스
	 * @param inte
	 * @return 마법 보너스
	 */
	public static int magicBonus(int inte) {
		if (inte < 12) {
			return 2;
		}
		return 2 + ((inte + ~0x00000007) >> 0x00000002);
	}
	
	private static final int[] MP_DECREASE_ARRAY = {
		5, 5, 5, 5, 5, 5, 5, 5, 5, 6, 6,		// 1~10
		7, 8, 8, 9, 10, 10, 11, 12, 12, 13,		// 11~20
		14, 14, 15, 16, 16, 17, 18, 18, 19, 20,	// 21~30
		20, 21, 22, 22, 23, 24, 24, 25, 26, 26,	// 31~40
		27, 28, 28, 29, 30						// 41~45
	};
	
	/**
	 * MP사용 감소
	 * @param inte
	 * @return MP사용 감소
	 */
	public static int reduceMp(int inte) {
		if (inte < 0) {
			return MP_DECREASE_ARRAY[0];
		}
		if (inte > MP_DECREASE_ARRAY.length + ~0x00000000) {
			return MP_DECREASE_ARRAY[MP_DECREASE_ARRAY.length + ~0x00000000];
		}
		return MP_DECREASE_ARRAY[inte];
	}
	
	private CalcIntelStat() {}
	
}

