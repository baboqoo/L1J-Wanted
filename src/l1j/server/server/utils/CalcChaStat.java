package l1j.server.server.utils;

public class CalcChaStat {
	
	/**
	 * 모든 적중
	 * @param cha
	 * @return 모든 적중
	 */
	public static int pierceAll(int cha) {
		if (cha < 4) {
			return 0;
		}
		int temp = cha >> 0x00000002;
		if (cha >= 12) {
			temp++;
		}
		if (cha >= 35) {
			temp++;
		}
		if (cha >= 45) {
			temp++;
		}
		if (cha >= 55) {
			temp++;
		}
		if (cha >= 60) {
			temp++;
		}
		return temp;
	}
	
	/**
	 * 스킬 쿨타임 감소
	 * @param cha
	 * @return 스킬 쿨타임 감소
	 */
	public static int decreaseSpellCoolTime(int cha) {
		if (cha < 7) {
			return 0;
		}
		int temp = 1 + ((cha + ~0x00000006) >> 0x00000003);
		if (cha >= 12) {
			temp++;
		}
		if (cha >= 25) {
			temp++;
		}
		if (cha >= 60) {
			temp++;
		}
		return temp * 100;
	}
	
	/**
	 * 상태 이상 시간 감소
	 * @param cha
	 * @return 상태 이상 시간 감소
	 */
	public static int decreaseCCDuration(int cha) {
		if (cha < 5) {
			return 0;
		}
		int temp = 1 + ((cha + ~0x00000004) >> 0x00000003);
		if (cha >= 12) {
			temp++;
		}
		if (cha >= 25) {
			temp++;
		}
		if (cha >= 60) {
			temp++;
		}
		return temp * 100;
	}
	
	private CalcChaStat(){}
	
}

