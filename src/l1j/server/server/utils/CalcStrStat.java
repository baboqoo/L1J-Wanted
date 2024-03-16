package l1j.server.server.utils;

public class CalcStrStat {
	
	/**
	 * 근거리 대미지
	 * @param str
	 * @return 근거리 대미지
	 */
	public static int shortDamage(int str) {
		if (str < 10) {
			return 2;
		}
		int temp = 2 + ((str + ~0x00000007) >> 0x00000001);
		if (str >= 20) {
			temp++;
		}
		if (str >= 25) {
			temp++;
		}
		if (str >= 35) {
			temp++;
		}
		if (str >= 45) {
			temp += 3;
		}
		if (str >= 55) {
			temp += 5;
		}
		if (str >= 60) {
			temp += 5;
		}
		return temp;
	}
	
	/**
	 * 근거리 명중
	 * @param str
	 * @return 근거리 명중
	 */
	public static int shortHitup(int str) {
		if (str < 9) {
			return 5;
		}
		int temp = 5 + (((str + ~0x00000005) / 3) << 0x00000001);
		if (str >= 20) {
			temp++;
		}
		if (str >= 25) {
			temp++;
		}
		if (str >= 35) {
			temp++;
		}
		if (str >= 45) {
			temp += 3;
		}
		if (str >= 55) {
			temp += 5;
		}
		if (str >= 60) {
			temp += 5;
		}
		return temp;
	}
	
	/**
	 * 근거리 치명타
	 * @param str
	 * @return 근거리 치명타
	 */
	public static int shortCritical(int str) {
		if (str < 40) {
			return 0;
		}
		return (str + ~0x0000001D) / 10;
	}
	
	private CalcStrStat() {}
	
}

