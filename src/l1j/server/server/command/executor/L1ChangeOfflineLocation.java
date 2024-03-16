package l1j.server.server.command.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;

public class L1ChangeOfflineLocation implements L1CommandExecutor  {
	private static Logger _log = Logger.getLogger(L1ChangeOfflineLocation.class.getName());
	
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1ChangeOfflineLocation();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1ChangeOfflineLocation() {}
	
	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try{
			StringTokenizer st = new StringTokenizer(arg);
			String charname = st.nextToken();
	
			if (L1World.getInstance().getPlayer(charname) != null){
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage(charname + " 캐릭터가 월드에 존재합니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(charname  + S_SystemMessage.getRefText(240), true), true);
				return false;
			}
			Connection conn = null;
			PreparedStatement pstm = null;
			
			try {
				conn = L1DatabaseFactory.getInstance().getConnection();
				pstm = conn.prepareStatement("update characters set LocX = 33429, LocY = 32807, MapID = 4 where char_name = ?");
				pstm.setString(1, charname);
				pstm.execute();
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} finally {
				SQLUtil.close(pstm, conn);
			}
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(charname + " 캐릭터의 좌표를 변경하였습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(charname  + S_SystemMessage.getRefText(241), true), true);
			return true;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [아이디]로 입력해 주세요. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(242), true), true);
			return false;
		}
	}
}


