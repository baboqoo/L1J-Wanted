package l1j.server.server.command.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringTokenizer;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1CheckPassword implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1CheckPassword();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1CheckPassword() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer stringtokenizer = new StringTokenizer(arg);
			String target = stringtokenizer.nextToken();

			Connection con = null;
			PreparedStatement pstm = null;
			PreparedStatement pstm2 = null;
			ResultSet rs = null;
			ResultSet rs2 = null;
			String login = null;
			String pass = null;
			String lastactive = null;
			String quiz = null;
			String ip = null;
			String host = null;


			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT account_name FROM characters WHERE char_name=?");
			pstm.setString(1, target);
			rs = pstm.executeQuery();

			if (rs.next()) {
				login = rs.getString(1);
			}
			pstm2 = con.prepareStatement("SELECT password, quiz, lastactive, ip, host FROM accounts WHERE login= '"+ login + "'");
			rs2 = pstm2.executeQuery();
			
			if (rs2.next()) {
				pass = rs2.getString(1);
				quiz = rs2.getString(2);
				lastactive = rs2.getString(3);
				ip = rs2.getString(4);
				host = rs2.getString(5);
			}
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("캐릭명: " + target + "\n계정: " + login + "\n비번: " + pass+"\n퀴즈: " + quiz+ "\n최근접속: " + lastactive+ "\n접속아이피: "+ip+"\n생성아이피: "+host), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(296) + target  + S_SystemMessage.getRefText(297) + login  + S_SystemMessage.getRefText(298) + pass + S_SystemMessage.getRefText(299) + quiz + S_SystemMessage.getRefText(300) + lastactive + S_SystemMessage.getRefText(301) + ip + S_SystemMessage.getRefText(302) + host, true), true);
			
			rs.close();
			rs2.close();
			pstm.close();
			pstm2.close();
			con.close();
			return true;
		} catch (Exception exception) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName +" [캐릭명] 을 입력하세요."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(157), true), true);
			return false;
		}
	}
}


