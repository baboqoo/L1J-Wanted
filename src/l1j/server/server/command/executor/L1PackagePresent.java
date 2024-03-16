package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.warehouse.PackageWarehouse;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;

public class L1PackagePresent implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1PackagePresent();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1PackagePresent() {}
	
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			if (pc.isGm()) {
				String account = st.nextToken();
				int itemid = Integer.parseInt(st.nextToken(), 10);
				int enchant = Integer.parseInt(st.nextToken(), 10);
				int count = Integer.parseInt(st.nextToken(), 10);
				L1Item temp = ItemTable.getInstance().getTemplate(itemid);
				if (temp == null) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("존재하지 않는 아이템 ID입니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(506), true), true);
					return false;
				}
				PackageWarehouse.present(account, itemid, enchant, count);
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage(temp.getDesc() + "를" + count+ "개 선물 했습니다.", true), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(temp.getDesc()  + S_SystemMessage.getRefText(507) + count + S_SystemMessage.getRefText(508), true), true);
				return true;
			}
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("당신은 운영자가 될 조건이 되지 않습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(509), true), true);
			return false;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".패키지 [계정명] [아이템 ID] [인챈트수] [아이템수]를 입력 해주세요.(계정명을 *으로 하면 전체 지급)"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(510), true), true);
			return false;
		}
	}
}

