package l1j.server.server.command.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringTokenizer;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;

public class L1AccountAdd implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1AccountAdd();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1AccountAdd() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st	= new StringTokenizer(arg);
			String user			= st.nextToken();
			String passwd		= st.nextToken();

			if(user.length() < 4){
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("입력하신 계정명의 자릿수가 너무 짧습니다.\n최소 4자 이상 입력해 주십시오."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(148), true), true);
				return false;
			}
			if(passwd.length() < 4){
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("입력하신 암호의 자릿수가 너무 짧습니다.\n최소 4자 이상 입력해 주십시오."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(149), true), true);
				return false;
			}
			if(passwd.length() > 12){
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("입력하신 암호의 자릿수가 너무 깁니다.\n최대 12자 이하로 입력해 주십시오."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(150), true), true);
				return false;
			}
			if(!isDisitAlpha(passwd)){
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("암호에 허용되지 않는 문자가 포함 되어 있습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(151), true), true);
				return false;
			}
			addAccount(pc, user, passwd);
			return true;
		} catch (Exception exception) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [계정명] [암호] 라고 입력해 주세요. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(152), true), true);
			return false;
		}
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
	
	private void addAccount(L1PcInstance pc, String account, String passwd) {
		String login			= null;
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM accounts WHERE login LIKE '" + account + "'");
			rs		= pstm.executeQuery();
			if(rs.next())login = rs.getString(1);
			SQLUtil.close(rs, pstm);
			
			if(login != null){
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("이미 계정이 존재합니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(153), true), true);
				return;
			}else{
				pstm = con.prepareStatement("INSERT INTO accounts SET login=?,password=?,lastactive=NOW(),access_level=0,ip='127.0.0.1',host='127.0.0.1',banned=0,charslot=6,warehouse_password=0,notice=0");
				pstm.setString(1, account);
				pstm.setString(2, passwd);
				pstm.execute();
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("계정 추가가 완료되었습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(154), true), true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
}


