package l1j.server.server.command.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringTokenizer;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.Account;
import l1j.server.server.datatables.IpTable;
import l1j.server.server.datatables.IpTable.BanIpReason;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class L1PowerKick implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1PowerKick();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1PowerKick() {}
	
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
				Account.ban(target.getAccountName()); // 계정을 BAN시킨다.
				iptable.insert(target.getNetConnection().getIp(), BanIpReason.getReason(reasonCode)); // BAN 리스트에IP를 추가한다.
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage((new StringBuilder()).append(target.getName()).append(" 를 영구 추방 했습니다. ").toString()), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage((new StringBuilder()).append(target.getName()).append(S_SystemMessage.getRefText(534)).toString(), true), true);
				target.sendPackets(S_Disconnect.DISCONNECT);
				return true;
			}
			String name = loadCharacter(arg);
			if (name != null) {
				Account.ban(name);
				String nc = Account.checkIP(name);
				if (nc != null)
					iptable.insert(nc, BanIpReason.getReason(reasonCode));
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage(name + " 를 계정압류 하였습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(name  + S_SystemMessage.getRefText(155), true), true);
				return true;
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
			pc.sendPackets(MESSAGE);
			return false;
		}
	}

	private String loadCharacter(String charName) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		String name = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM characters WHERE char_name=?");
			pstm.setString(1, charName);

			rs = pstm.executeQuery();

			if (rs.next()) {
				name = rs.getString("account_name");
			}

		} catch (Exception e) {
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return name;
	}
}


