package l1j.server.server.utils;

public class NumberUtil {

	public static int randomRound(double number) {
		double percentage = (number - Math.floor(number)) * 100;
		if (percentage == 0) {
			return ((int) number);
		}
		int r = CommonUtil.random(100);
		if (r < percentage) {
			return ((int) number + 1);
		}
		return ((int) number);
	}
}

