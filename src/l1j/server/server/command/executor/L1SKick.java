package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.GameServer;
import l1j.server.server.datatables.IpTable;
import l1j.server.server.datatables.IpTable.BanIpReason;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StringUtil;

public class L1SKick implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1SKick();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1SKick() {}
	
	static S_SystemMessage MESSAGE;

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			
			String targetName = st.nextToken();
			
			int reasonCode = 0;
			try {
				reasonCode = Integer.parseInt(st.nextToken());
			} catch (Exception e) {
			}
			L1PcInstance target = L1World.getInstance().getPlayer(targetName);

			IpTable iptable = IpTable.getInstance();
			if (target != null) {
				if( target.getNetConnection() != null ){
					iptable.insert(target.getNetConnection().getIp(), BanIpReason.getReason(reasonCode));
				}
				if (pc != null) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage((new StringBuilder()).append(target.getName()).append(" 를 강력 추방 했습니다. ").toString()), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage((new StringBuilder()).append(target.getName()).append(S_SystemMessage.getRefText(675)).toString(), true), true);
				}
				target.setX(33080);
				target.setY(33392);
				target.setMap((short) 4);
				GameServer.disconnectChar(target);
				target.sendPackets(S_Disconnect.DISCONNECT);
				return true;
			}
			if (pc != null) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("그러한 이름의 캐릭터는 월드내에는 존재하지 않습니다. "), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(156), true), true);
			}
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
			if (pc != null) {
				pc.sendPackets(MESSAGE);
			}
			return false;
		}
	}
}

