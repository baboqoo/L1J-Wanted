package l1j.server.server.command.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.GameClient;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;

public class L1QueryCharacter implements L1CommandExecutor  {
	private static Logger _log = Logger.getLogger(L1QueryCharacter.class. getName());
	
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1QueryCharacter();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1QueryCharacter() {}

	static private String getCClass( String ip ){
		return ip.substring(0, ip.lastIndexOf('.'));
	}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			L1PcInstance target = L1World.getInstance(). getPlayer(arg);

			if (target != null) {
				long totalAdena = 0;
				GameClient client = target.getNetConnection();
				
				if (client == null){
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("접속중이지 않은 캐릭터에 대해 조회할 수 없습니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(426), true), true);
					return false;
				}
				
				String cClass = getCClass(client.getIp());

				Collection<L1PcInstance> pcs = L1World.getInstance().getAllPlayers();

				for (L1PcInstance otherPc : pcs) {
					
					if (otherPc.getNetConnection() != null){
						String otherPcIp = otherPc.getNetConnection().getIp();
						
						if(cClass.equals(getCClass(otherPcIp))){
							totalAdena += printInfo(pc, otherPc, otherPcIp);
						}
					}
				}
				
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("모든 계정내의 아데나 총합계는 [" + totalAdena + "] 입니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(553) + totalAdena  + S_SystemMessage.getRefText(554), true), true);
				return true;
			}
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("그러한 이름의 캐릭터는 월드내에는 존재하지 않습니다. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(156), true), true);
			return false;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [캐릭터명] 으로 입력해 주세요. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(157), true), true);
			return false;
		}
	}
	
	private long printInfo(L1PcInstance master, L1PcInstance pc, String ip){
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		long storageAdena = 0;
		long characterAdena = 0;
		try {

			conn = L1DatabaseFactory.getInstance().getConnection();
			pstm = conn.prepareStatement(
					"select ifnull(sum(count), 0) as 'adena' from character_warehouse where item_id = 40308 and account_name = ?");
			pstm.setString(1, pc.getAccountName());
			rs = pstm.executeQuery();
			if (rs.next()) {
				storageAdena = rs.getInt("adena");
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, conn);
		}
		
		try {

			conn = L1DatabaseFactory.getInstance().getConnection();
			pstm = conn.prepareStatement(
					"select ifnull(sum(count), 0) as 'adena' from character_items where item_id = 40308 and char_id IN (select objid FROM characters WHERE account_name = ?)");
			pstm.setString(1, pc.getAccountName());
			rs = pstm.executeQuery();
			if (rs.next()) {
				characterAdena = rs.getInt("adena");
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, conn);
		}
	
//AUTO SRM: 		master.sendPackets(new S_SystemMessage( ip + "에서 [" + pc.getName() + "]가 현재 접속중이며 계정 창고 아데나는 [" + storageAdena + "] 캐릭 내 아데나 총 합은 [" + characterAdena + "] 입니다."), true); // CHECKED OK
		master.sendPackets(new S_SystemMessage(ip  + S_SystemMessage.getRefText(556) + pc.getName()  + S_SystemMessage.getRefText(557) + storageAdena  + S_SystemMessage.getRefText(558) + characterAdena  + S_SystemMessage.getRefText(554), true), true);
		
		return storageAdena + characterAdena;
	}
}


