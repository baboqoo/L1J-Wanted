package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.datatables.ClanTable;
import l1j.server.server.datatables.HouseTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1House;

public class L1GiveHouse implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1GiveHouse();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1GiveHouse() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String pobyname = st.nextToken();
			int pobyhouseid = Integer.parseInt(st.nextToken());
			L1PcInstance target = L1World.getInstance().getPlayer(pobyname);
			if (target != null) {
				if (target.getClanid() != 0) {
					L1Clan TargetClan = target.getClan();
					L1House pobyhouse = HouseTable.getInstance().getHouseTable(pobyhouseid);
					TargetClan.setHouseId(pobyhouseid);
					ClanTable.getInstance().updateClan(TargetClan);
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage(target.getClanName() + " 혈맹에게 " + pobyhouse.getHouseName() + "번을 지급하였습니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(target.getClanName()  + S_SystemMessage.getRefText(420) + pobyhouse.getHouseName()  + S_SystemMessage.getRefText(421), true), true);
					for (L1PcInstance tc : TargetClan.getOnlineClanMember()) {
//AUTO SRM: 						tc.sendPackets(new S_SystemMessage("게임마스터로부터 " + pobyhouse.getHouseName() + "번을 지급 받았습니다."), true); // CHECKED OK
						tc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(422) + pobyhouse.getHouseName()  + S_SystemMessage.getRefText(423), true), true);
					}
				} else {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage(target.getName() + "님은 혈맹에 속해 있지 않습니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(target.getName()  + S_SystemMessage.getRefText(424), true), true);
				}
				return true;
			}
			pc.sendPackets(new S_ServerMessage(73, pobyname), true);
			return false;
		} catch (Exception exception) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [지급할혈맹원] [아지트번호] 라고 입력해 주세요. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(425), true), true);
			return false;
		}
	}
}


