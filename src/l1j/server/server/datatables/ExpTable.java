package l1j.server.server.datatables;

import l1j.server.Config;

public final class ExpTable {
	public static final int MAX_LEVEL			= Config.CHA.LIMIT_LEVEL;// 107레벨 경험치 2111521206 이후는 변수의 타입의 변경이 필요함(101레벨 부터 클라이언트에서 처리못함)
	public static final int MAX_EXP				= 2147483647;// int type max value
	private static final long[] _expTable		= new long[108];
	private static final double[] _expPenalty	= new double[108];
	
	private static class newInstance {
		public static final ExpTable INSTANCE = new ExpTable();
	}
	public static ExpTable getInstance() {
		return newInstance.INSTANCE;
	}
	
	public void loadExp(boolean reload) {
		java.sql.Connection con = null;
		java.sql.PreparedStatement pstm = null;
		java.sql.ResultSet rs = null;
		try {
			con = l1j.server.L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM exp");
			rs = pstm.executeQuery();
			while (rs.next()) {
				int level				= rs.getInt("level");
				long exp				= rs.getLong("exp");
				double penalty			= rs.getDouble("panalty");
				_expTable[level - 1]	= exp;
				_expPenalty[level - 1]	= penalty;
			}
		} catch (Exception e) {
			e.getStackTrace();
		} finally {
			l1j.server.server.utils.SQLUtil.close(rs, pstm, con);
		}
	}

	/**
	 * 지정된 레벨이 되는데 필요한 누적 경험치를 요구한다.
	 * 
	 * @param level
	 *            레벨
	 * @return 필요한 누적 경험치
	 */
	public static long getExpByLevel(int level) {
		return _expTable[level - 1];
	}

	/**
	 * 다음의 레벨이 되는데 필요한 경험치를 요구한다.
	 * 
	 * @param level
	 *            현재의 레벨
	 * @return 필요한 경험치
	 */
	public static long getNeedExpNextLevel(int level) {
		return getExpByLevel(level + 1) - getExpByLevel(level);
	}

	/**
	 * 누적 경험치로부터 레벨을 요구한다.
	 * 
	 * @param exp
	 *            누적 경험치
	 * @return 요구된 레벨
	 */
	public static int getLevelByExp(long exp) {
		int level;
		for (level = 1; level < _expTable.length; level++) {
			if (exp < _expTable[level]) {
				break;
			}
		}
		return Math.min(level, MAX_LEVEL);
	}

	public static int getExpPercentage(int level, long exp) {
		return (int) (100.0D * ((double) (exp - getExpByLevel(level)) / (double) getNeedExpNextLevel(level)));
	}
	public static double getExpPercentagedouble(int level, long exp) {
		return 100.0D * ((exp - getExpByLevel(level)) / getNeedExpNextLevel(level));
	}

	/**
	 * 현재의 레벨로부터, 경험치의 페널티 레이트를 요구한다
	 * 
	 * @param level
	 *            현재의 레벨
	 * @return 요구된 경험치의 페널티 레이트
	 */
	public static double getPenalty(int level){
		return _expPenalty[level - 1];
	}
	
	/**
	 * 요청 레벨의 퍼센트에 해당하는 경험치를 요구한다.
	 * @param level
	 * @param percent
	 * @return percent_exp
	 */
	public static long getExpFromLevelAndPercent(int currentLevel, int level, double percent){
		long percentExp = (long)(getNeedExpNextLevel(level) * (percent * 0.01D));// 해당 레벨의 퍼센트 경험치
		if (currentLevel != level) {
			percentExp *= getPenalty(currentLevel);
		}
		return percentExp;
	}
}
