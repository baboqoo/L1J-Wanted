package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StringUtil;

public class L1ServerName implements L1CommandExecutor{
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1ServerName();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1ServerName() {}

	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (pc == null) {
				return false;
			}
			StringTokenizer st = new StringTokenizer(arg);
			int invid = Integer.parseInt(st.nextToken(), 10);
			int count = Integer.parseInt(st.nextToken(), 10);
			L1ItemInstance item = null;

			for (int i = 0; i < count; i++) {
				item = ItemTable.getInstance().createItem(L1ItemId.ADENA);
				item.getItem().setDescKr(String.valueOf(StringUtil.DollarString + (invid + i)));
				pc.sendPackets(new S_SystemMessage((invid + i)+" : " + StringUtil.DollarString + (invid + i)), true);
			}
			return true;
		} catch (Exception exception) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".네임  [id] [출현시키는 수]로 입력해 주세요. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(230), true), true);
			return false;
		}
	}
}

