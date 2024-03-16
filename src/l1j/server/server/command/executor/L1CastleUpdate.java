package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1CastleUpdate implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1CastleUpdate();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1CastleUpdate() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String name = st.nextToken();
			int number = Integer.parseInt(st.nextToken());
			L1Clan clan = L1World.getInstance().getClan(name);
			if (clan == null) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("혈맹이 존재하지 않습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(236), true), true);
				return false;
			}
			clan.setCastleId(number);
			L1World.getInstance().removeClan(clan);
			L1World.getInstance().storeClan(clan);
			ClanTable.getInstance().updateClan(clan);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(name + " 혈맹정보가 변경됐습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(name  + S_SystemMessage.getRefText(237), true), true);
			return true;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [혈이름] [성번호] 입력"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(238), true), true);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("켄트1,오크2,윈다3,기란4,하이5,웰던6,아덴7,디아드8"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(239), true), true);
			return false;
		}
	}
}


