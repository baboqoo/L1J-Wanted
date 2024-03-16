package l1j.server.server.command.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.StringTokenizer;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class L1ChangePassword implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1ChangePassword();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1ChangePassword() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer tok = new StringTokenizer(arg);
			String name = tok.nextToken();
			String passwd = tok.nextToken();
			if (StringUtil.isNullOrEmpty(name)) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("캐릭터명을 입력해 주십시오."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(157), true), true);
				return false;
			}
			if (passwd.length() < 4) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("입력하신 암호의 자릿수가 너무 짧습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(244), true), true);
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("최소 4자 이상 입력해 주십시오."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(245), true), true);
				return false;
			}

			if (passwd.length() > 12) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("입력하신 암호의 자릿수가 너무 깁니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(246), true), true);
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("최대 12자 이하로 입력해 주십시오."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(247), true), true);
				return false;
			}

			if (!isDisitAlpha(passwd)) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("암호에 허용되지 않는 문자가 포함되었습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(248), true), true);
				return false;
			}
			L1PcInstance target = L1World.getInstance().getPlayer(name);
			if (target != null) {
				toChangePasswd(pc, target, passwd);
				return true;
			}
			if (!toChangePasswd(pc, name, passwd)) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("그런 이름을 가진 캐릭터는 없습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(249), true), true);
				return false;
			}
			return true;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [캐릭명] [암호]로 입력해주세요."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(250), true), true);
			return false;
		}
	}
	
	private void toChangePasswd(L1PcInstance gm, L1PcInstance pc, String password) {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("UPDATE accounts SET password=? WHERE login=?");
			pstm.setString(1, password);
			pstm.setString(2, pc.getAccountName());
			if(pstm.executeUpdate() > 0){
//AUTO SRM: 				gm.sendPackets(new S_SystemMessage("암호변경 계정: [" + pc.getAccountName() + "] 암호: [" + password + "]"), true); // CHECKED OK
				gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(251) + pc.getAccountName()  + S_SystemMessage.getRefText(252) + password  + "]", true), true);
//AUTO SRM: 				gm.sendPackets(new S_SystemMessage(pc.getName() + " 암호변경 완료."), true); // CHECKED OK
				gm.sendPackets(new S_SystemMessage(pc.getName()  + S_SystemMessage.getRefText(253), true), true);
			}
		} catch (Exception e) {
			System.out.println("to_Change_Passwd() Error : " + e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	private boolean toChangePasswd(L1PcInstance pc, String name, String password) {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("UPDATE accounts SET password=? WHERE login=COALESCE((SELECT account_name FROM characters WHERE char_name=?), '')");
			pstm.setString(1, password);
			pstm.setString(2, name);
			if(pstm.executeUpdate() > 0){
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("암호변경 캐릭터: [" + name + "] 암호: [" + password + "]"), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(254) + name  + S_SystemMessage.getRefText(252) + password  + "]", true), true);
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("해당 캐릭터 암호변경 완료. (미접속중)"), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(255), true), true);
				return true;
			}
		} catch (Exception e) {
			System.out.println("to_Change_Passwd() Error : " + e);
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}
	
	private static boolean isDisitAlpha(String str) {
		boolean check = true;
		for(int i = 0; i < str.length(); i++){
			if(!Character.isDigit(str.charAt(i)) // 숫자가 아니라면
					&& !Character.isUpperCase(str.charAt(i)) // 대문자가 아니라면
					&& !Character.isLowerCase(str.charAt(i))){ // 소문자가 아니라면
				check = false;
				break;
			}
		}
		return check;
	}
}


