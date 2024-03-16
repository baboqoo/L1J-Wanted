package l1j.server.server.command.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_LetterList;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;

public class L1RemoveLetter implements L1CommandExecutor {
	private static Logger _log = Logger.getLogger(L1RemoveLetter.class.getName());
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1RemoveLetter();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1RemoveLetter() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (pc == null) {
				return false;
			}
			checkLetter(pc.getName());	
			pc.sendPackets(new S_LetterList(pc, 0, 200), true);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("편지를 삭제 하였습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(636), true), true);
			return true;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".편지삭제을 입력해주세요."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(637), true), true);
			return false;
		}
	}

	public void checkLetter(String name) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();			
			pstm = con.prepareStatement("DELETE FROM letter WHERE receiver = ?");
			pstm.setString(1, name);
			pstm.execute();	
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
}


