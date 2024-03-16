package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1DescIdSearch implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1DescIdSearch();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1DescIdSearch() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (pc == null) {
				return false;
			}
			StringTokenizer st = new StringTokenizer(arg);
			//int 아이템번호	= Integer.parseInt(st.nextToken());
			//int 시작번호	= Integer.parseInt(st.nextToken());
			//int 끝번호		= Integer.parseInt(st.nextToken());
			int itemNumber = Integer.parseInt(st.nextToken());
			int startNumber = Integer.parseInt(st.nextToken());
			int lastNumber = Integer.parseInt(st.nextToken());
			L1ItemInstance item;
			for(int k = startNumber; k < lastNumber + 1; k++){
				item = ItemTable.getInstance().createItem(itemNumber);
				item.getItem().setItemNameId(k);
				item.setIdentified(true);
				pc.getInventory().storeItem(item);
            }
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("아이템번호: " + 아이템번호 + ", 시작번호: " + 시작번호 + ", 끝번호: " + 끝번호), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(336) + itemNumber  + S_SystemMessage.getRefText(337) + startNumber  + S_SystemMessage.getRefText(338) + lastNumber, true), true);
			return true;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [아이템번호] [시작번호] [끝번호] 으로 입력"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(339), true), true);
			return false;
		}
	}
}


