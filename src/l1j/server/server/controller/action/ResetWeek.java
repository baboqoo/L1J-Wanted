package l1j.server.server.controller.action;

import java.sql.Connection;
import java.sql.PreparedStatement;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

/**
 * 수용일 정기정검 초기화 컨틀롤러
 * @author LinOffice
 */
public class ResetWeek implements ControllerInterface {
	private static class newInstance {
		public static final ResetWeek INSTANCE = new ResetWeek();
	}
	public static ResetWeek getInstance() {
		return newInstance.INSTANCE;
	}
	private ResetWeek(){}

	@Override
	public void execute() {
		try {
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc == null || pc.getNetConnection() == null || pc.getAccount() == null || pc.isAutoClanjoin() || pc.noPlayerCK) {
					continue;
				}
				pc.getAccount().setShopOpenCount(0);
				pc.setClanWeekContribution(0);
				pc.getDungoenTimer().resetTimer(8, false);
			}
			reset_weekly();
		} catch(Exception e2){
			e2.printStackTrace();
		}
	}
	
	@Override
	public void execute(L1PcInstance pc) {
	}
	
	/**
	 * DB프로시저 호출(일괄 처리)
	 */
	void reset_weekly() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("CALL RESET_WEEKLY()");
			if (pstm.executeUpdate() > 0) {
				//System.out.println("[시스템 알림] 수요일 정기정검 초기화 완료");
				System.out.println("[System Notification] Wednesday regular maintenance initialization completed");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
}

