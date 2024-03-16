package l1j.server.server.command.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringTokenizer;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;

public class L1UnlockAccount implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1UnlockAccount();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1UnlockAccount() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st		= new StringTokenizer(arg);
			String pcName			= st.nextToken();
			Connection con			= null;
			PreparedStatement pstm	= null;
			ResultSet rs			= null;
			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				pstm = con.prepareStatement("UPDATE accounts SET banned=0 WHERE login=COALESCE((SELECT account_name FROM characters WHERE char_name=?), '')");
				pstm.setString(1, pcName);
				if (pstm.executeUpdate() > 0) {
					//pc.sendPackets(new S_SystemMessage(pcName + " 의 계정압류를 해제 하였습니다"), true);
					pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(123), pcName), true);
					return true;
				} else {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("DB에 [" + pcName + "] 캐릭명이 존재 하지 않습니다"), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(709) + pcName  + S_SystemMessage.getRefText(710), true), true);
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				SQLUtil.close(rs, pstm, con);
			}
		} catch (Exception exception) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [캐릭명] 으로 입력해주세요."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(157), true), true);
			return false;
		}
		return false;
	}
}


