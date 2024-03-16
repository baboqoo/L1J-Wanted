package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.datatables.IpTable;
import l1j.server.server.datatables.IpTable.BanIp;
import l1j.server.server.datatables.IpTable.BanIpReason;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StringUtil;

public class L1BanIp implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1BanIp();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1BanIp() {}
	
	static S_SystemMessage MESSAGE;

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			// IP를 지정
			String ip = st.nextToken();

			// add/del를 지정(하지 않아도 OK)
			String type = null;
			try {
				type = st.nextToken();
			} catch (Exception e) {
			}
			
			int reasonCode = 0;
			try {
				reasonCode = Integer.parseInt(st.nextToken());
			} catch (Exception e) {
			}

			IpTable iptable = IpTable.getInstance();
			boolean isBanned = IpTable.isBannedIp(ip);

			for (L1PcInstance tg : L1World.getInstance().getAllPlayers()) {
				if (tg.getNetConnection() != null && ip.equals(tg.getNetConnection().getIp())) {
					//String msg = new StringBuilder().append("IP : ").append(ip).append(" 로 접속중의 플레이어 :").append(tg.getName()).toString();
					String msg = new StringBuilder().append("IP : ").append(ip).append(" Player connecting with :").append(tg.getName()).toString();
					pc.sendPackets(new S_SystemMessage(msg, true), true);
				}
			}

			//if ("추가".equals(type) && !isBanned) {
			if ("add".equals(type) && !isBanned) {
				iptable.insert(ip, BanIpReason.getReason(reasonCode)); // BAN 리스트에 IP를 더한다
				//String msg = new StringBuilder().append("IP : ").append(ip).append(" 를 BAN IP에 등록했습니다.").toString();
				String msg = new StringBuilder().append("IP : ").append(ip).append(" has been registered to BAN IP.").toString();
				pc.sendPackets(new S_SystemMessage(msg, true), true);
				for (L1PcInstance tg : L1World.getInstance().getAllPlayers()) {
					if (tg.getNetConnection() != null && ip.equals(tg.getNetConnection().getIp())) {
						tg.getNetConnection().kick();
//AUTO SRM: 						pc.sendPackets(new S_SystemMessage(tg.getName() + "을 추방했습니다."), true); // CHECKED OK
						pc.sendPackets(new S_SystemMessage(tg.getName()  + S_SystemMessage.getRefText(218), true), true);
					}
				}
				return true;
			}
			//if ("삭제".equals(type) && isBanned) {
			if ("delete".equals(type) && isBanned) {
				if (iptable.delete(ip)) { // BAN 리스트로부터 IP를 삭제한다
					//String msg = new StringBuilder().append("IP : ").append(ip).append(" 를 BAN IP로부터 삭제했습니다.").toString();
					String msg = new StringBuilder().append("IP : ").append(ip).append(" has been deleted from BAN IP.").toString();
					pc.sendPackets(new S_SystemMessage(msg, true), true);
					return true;
				}
				return false;
			}
			// BAN의 확인
			if (isBanned) {
				BanIp obj = IpTable.getBanIp(ip);
				//String msg = new StringBuilder().append("IP : ").append(ip).append(" 는 BAN IP에 등록되어 있습니다.\r\n").append(obj.toString()).toString();
				String msg = new StringBuilder().append("IP : ").append(ip).append(" is registered in BAN IP.\r\n").append(obj.toString()).toString( );
				pc.sendPackets(new S_SystemMessage(msg, true), true);
			} else {
				//String msg = new StringBuilder().append("IP : ").append(ip).append(" 는 BAN IP에 등록되어 있지 않습니다.").toString();
				String msg = new StringBuilder().append("IP : ").append(ip).append(" is not registered in BAN IP.").toString();
				pc.sendPackets(new S_SystemMessage(msg, true), true);
			}
			return true;
		} catch (Exception e) {
			if (MESSAGE == null) {
				StringBuilder sb = new StringBuilder();
				//sb.append(cmdName).append(" [아이피] [추가, 삭제] [사유코드] 라고 입력해 주세요.\r\n사유코드 \r\n");
				sb.append(cmdName).append(" Please enter [IP] [Add, Delete] [Reason Code].\r\nReason Code \r\n");
				for (BanIpReason reason : BanIpReason.getAllReason()) {
					sb.append(reason.getCode()).append(StringUtil.PeriodString).append(reason.getReason()).append(StringUtil.LineString);
				}
				MESSAGE = new S_SystemMessage(sb.toString());
			}
			pc.sendPackets(MESSAGE);
			return false;
		}
	}
}


