package l1j.server.server.command.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;

public class L1SearchBanList implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1SearchBanList();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1SearchBanList() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		if (pc == null) {
			return false;
		}
		String str1 = null;
		String str2 = null;
		int i = 0;
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT accounts.login, characters.char_name FROM accounts, characters WHERE accounts.banned=1 AND accounts.login=characters.account_name ORDER BY accounts.login ASC");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				str1 = rs.getString(1);
				str2 = rs.getString(2);
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage(new StringBuilder().append("계정:[").append(str1).append("], 캐릭터명:[").append(str2).append("]").toString()), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(new StringBuilder().append(S_SystemMessage.getRefText(660)).append(str1).append(S_SystemMessage.getRefText(661)).append(str2).append("]").toString(), true), true);
				++i;
			}
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(new StringBuilder().append("총 [").append(i).append("]개의 압류계정/캐릭터가  검색되었습니다.").toString()), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(new StringBuilder().append(S_SystemMessage.getRefText(658)).append(i).append(S_SystemMessage.getRefText(662)).toString(), true), true);
			return true;
		} catch (Exception e)  {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return false;
	}
}


