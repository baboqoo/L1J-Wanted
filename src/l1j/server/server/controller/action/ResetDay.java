package l1j.server.server.controller.action;

import java.sql.Connection;
import java.sql.PreparedStatement;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.attendance.bean.AttendanceAccount;
import l1j.server.GameSystem.freebuffshield.FreeBuffShieldHandler;
import l1j.server.GameSystem.huntingquest.user.HuntingQuestUser;
import l1j.server.GameSystem.huntingquest.user.HuntingQuestUserTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.attendance.S_AttenDanceInfoNoti;
import l1j.server.server.serverpackets.huntingquest.S_HuntingQuestMapList;
import l1j.server.server.utils.SQLUtil;

/**
 * 일일 초기화 컨트롤러
 * @author LinOffice
 */
public class ResetDay implements ControllerInterface {
	private static class newInstance {
		public static final ResetDay INSTANCE = new ResetDay();
	}
	public static ResetDay getInstance() {
		return newInstance.INSTANCE;
	}
	private ResetDay(){}

	@Override
	public void execute() {
		try {
			AttendanceAccount attendAccount	= null;
			HuntingQuestUser hunt			= null;
			FreeBuffShieldHandler shield	= null;
			long currentTime = System.currentTimeMillis();
			HuntingQuestUserTable huntUser	= HuntingQuestUserTable.getInstance();
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc == null || pc.getNetConnection() == null || pc.getAccount() == null || pc.isAutoClanjoin()) {
					continue;
				}
				attendAccount = pc.getAccount().getAttendance();
				attendAccount.reset(currentTime);
				pc.sendPackets(new S_AttenDanceInfoNoti(pc, attendAccount, 0), true);
				
				if (Config.QUEST.HUNTING_QUEST_ACTIVE) {
					hunt = pc.getHuntingQuest();
					hunt.reset();
					pc.sendPackets(new S_HuntingQuestMapList(hunt.getInfo().values()), true);
				}
				
				shield = pc.getConfig().get_free_buff_shield();
				if (shield != null) {
					shield.reset();
				}
			}
			huntUser.reset();
			reset_daily();
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
	void reset_daily() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("CALL RESET_DAILY()");
			if (pstm.executeUpdate() > 0) {
				//System.out.println("[시스템 알림] 일일초기화 완료");
				System.out.println("[System Notification] Daily reset completed");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

}

