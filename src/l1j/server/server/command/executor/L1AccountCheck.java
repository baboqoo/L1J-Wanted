package l1j.server.server.command.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringTokenizer;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;

public class L1AccountCheck implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1AccountCheck();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1AccountCheck() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
            StringTokenizer tok = new StringTokenizer(arg);
            String name = tok.nextToken();
            search(pc, name);
            return true;
        } catch (Exception e) {
//AUTO SRM:         	pc.sendPackets(new S_SystemMessage(cmdName + " [아이디]"), true); // CHECKED OK
        	pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(158), true), true);
        	return false;
        }
	}
	
	private void search(L1PcInstance gm, String param) {
        try {
            String s_account = null;
            String s_name = param;
            String s_level = null;
            String s_clan = null;
            String s_online = null;
            String s_hp = null;
            String s_mp = null;
            String s_type = null;//추가
            int count = 0;
            int count0 = 0;
            Connection con = null; // 이름으로 objid를 검색하기 위해
            PreparedStatement statement0 = null;
            PreparedStatement statement = null;
            ResultSet rs0 = null;
            ResultSet rs = null;
            try {
            	con = L1DatabaseFactory.getInstance().getConnection();
                statement0 = con.prepareStatement("SELECT account_name, Clanname FROM characters WHERE char_name = '" + s_name + "'");
                rs0 = statement0.executeQuery();
                while (rs0.next()) {
                    s_account = rs0.getString(1);
                    s_clan = rs0.getString(2);
                    gm.sendPackets(new S_SystemMessage("\\aD------------------------------------------"), true);
//AUTO SRM:                     gm.sendPackets(new S_SystemMessage("\\aE캐릭 : " + s_name + "("+ s_account +")  혈맹 : " + s_clan), true); // CHECKED OK
                    gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(159) + s_name  + "(" + s_account  + S_SystemMessage.getRefText(160) + s_clan, true), true);
                    if(count0 != 0)
                        count0++;
                }
                statement = con.prepareStatement("SELECT char_name, level, Clanname, OnlineStatus, MaxHp, MaxMp, Type FROM characters WHERE account_name = '" + s_account + "'");
                gm.sendPackets(new S_SystemMessage("\\aD------------------------------------------"), true);
                rs = statement.executeQuery();
                while(rs.next()){
                    s_name = rs.getString(1);
                    s_level = rs.getString(2);
                    s_clan = rs.getString(3);
                    s_online = rs.getString(4);
                    s_hp = rs.getString(5);
                    s_mp = rs.getString(6);
                    s_type = rs.getString(7);
//AUTO SRM:                     gm.sendPackets(new S_SystemMessage("접속[" + s_online + "] 레벨 " + s_level + " [" + s_name + "] 클래스 " + s_type + " HP: " + s_hp + " MP: " + s_mp)); // CHECKED OK
                    gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(161) + s_online  + S_SystemMessage.getRefText(162) + s_level  + " [" + s_name  + S_SystemMessage.getRefText(163) + s_type  + " HP: " + s_hp  + " MP: " + s_mp, true));
                    if(count != 0)
                        count++;
                }
//AUTO SRM:                 gm.sendPackets(new S_SystemMessage("\\aF0군주 1기사 2요정 3법사 4다엘 5용기사 6환술 7전사"), true); // CHECKED OK
                gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(164), true), true);
                gm.sendPackets(new S_SystemMessage("\\aD--------------------------------------"), true);
            } catch (Exception e) {
            } finally {
    			SQLUtil.close(rs0);
    			SQLUtil.close(rs);
    			SQLUtil.close(statement0);
    			SQLUtil.close(statement);
    			SQLUtil.close(con);
    		}
        } catch (Exception e) {
        }
    }
}


