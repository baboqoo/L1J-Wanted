package l1j.server.server.command.executor;

import java.util.List;
import java.util.StringTokenizer;

import l1j.server.server.GMCommandsConfig;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1ItemSetItem;

public class L1CreateItemSet implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1CreateItemSet();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1CreateItemSet() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (pc == null) {
				return false;
			}
			String name = new StringTokenizer(arg). nextToken();
			List<L1ItemSetItem> list = GMCommandsConfig.ITEM_SETS.get(name);
			if (list == null) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage(name + "은 없습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(name  + S_SystemMessage.getRefText(329), true), true);
				return false;
			}
			L1Item temp = null;
			L1ItemInstance inst = null;
			for (L1ItemSetItem item : list) {
				temp = ItemTable.getInstance(). getTemplate(item.getId());
				if (! temp.isMerge() && 0 != item.getEnchant()) {
					for (int i = 0; i < item.getAmount(); i++) {
						inst = ItemTable.getInstance().createItem(item.getId());
						inst.setEnchantLevel(item.getEnchant());
						pc.getInventory().storeItem(inst);
					}
				} else {
					pc.getInventory().storeItem(item.getId(), item.getAmount());
				}
			}
			return true;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".세트아이템 세트명으로 입력해 주세요. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(330), true), true);
			return false;
		}
	}
}


