package l1j.server.server.command.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.GameClient;
import l1j.server.server.datatables.IpTable;
import l1j.server.server.datatables.IpTable.BanIpReason;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class L1GlobalBan implements L1CommandExecutor {
	private static Logger _log = Logger.getLogger(L1QueryCharacter.class.getName());

	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1GlobalBan();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1GlobalBan() {}
	
	static S_SystemMessage MESSAGE;

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			
			String targetName = st.nextToken();
			
			L1PcInstance target = L1World.getInstance().getPlayer(targetName);

			if (target != null) {
				int reasonCode = 0;
				try {
					reasonCode = Integer.parseInt(st.nextToken());
				} catch (Exception e) {
				}
				
				GameClient client = target.getNetConnection();

				if (client == null) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("접속중이지 않은 캐릭터에 대해 조회할 수 없습니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(426), true), true);
					return false;
				}

				String targetIp = client.getIp();
				String cClass = getCClass(targetIp);

				Collection<L1PcInstance> pcs = L1World.getInstance().getAllPlayers();

				for (L1PcInstance otherPc : pcs) {

					if (otherPc.getNetConnection() != null) {
						String otherPcIp = otherPc.getNetConnection().getIp();

						if (cClass.equals(getCClass(otherPcIp))) {
							otherPc.sendPackets(S_Disconnect.DISCONNECT);
//AUTO SRM: 							pc.sendPackets(new S_SystemMessage("접속 중인 캐릭터 [" + otherPc.getName() + "]를 추방했습니다."), true); // CHECKED OK
							pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(427) + otherPc.getName()  + S_SystemMessage.getRefText(428), true), true);
						}
					}
				}

				banGlobalIp(targetIp, reasonCode);
				banGlobalAccounts(targetIp);
				return true;
			}
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("그러한 이름의 캐릭터는 월드내에는 존재하지 않습니다. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(156), true), true);
			return false;
		} catch (Exception e) {
			if (MESSAGE == null) {
				StringBuilder sb = new StringBuilder();
				//sb.append(cmdName).append(" [캐릭터명] [사유코드] 라고 입력해 주세요.\r\n사유코드 \r\n");
				sb.append(S_SystemMessage.getRefTextNS(121)).append(cmdName).append(S_SystemMessage.getRefText(1352));
				for (BanIpReason reason : BanIpReason.getAllReason()) {
					sb.append(reason.getCode()).append(StringUtil.PeriodString).append(reason.getReason()).append(StringUtil.LineString);
				}
				MESSAGE = new S_SystemMessage(sb.toString(), true);
			}
			pc.sendPackets(MESSAGE);
			return false;
		}
	}

	private void banGlobalIp(String ip, int reasonCode) {
		IpTable.getInstance().insertRange(ip, BanIpReason.getReason(reasonCode));
	}

	private void banGlobalAccounts(String ip) {
		Connection conn = null;
		PreparedStatement pstm = null;
		try {

			conn = L1DatabaseFactory.getInstance().getConnection();
			pstm = conn.prepareStatement("UPDATE accounts SET banned = 1 WHERE ip like CONCAT(?, '.%')");
			pstm.setString(1, getCClass(ip));
			pstm.execute();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, conn);
		}

	}

	static private String getCClass(String ip) {
		return ip.substring(0, ip.lastIndexOf('.'));
	}
}


