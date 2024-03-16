package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1InventoryClear implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1InventoryClear();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1InventoryClear() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			
			if (st.hasMoreTokens()) {
				L1PcInstance target = L1World.getInstance().getPlayer(st.nextToken());
				if (target == null) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("해당 캐릭터를 찾을 수 없습니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(286), true), true);
					return false;
				}
				for (L1ItemInstance item : target.getInventory().getItems()) {
					if (!item.isEquipped()) {
						target.getInventory().setEquipped(item, false);
					}
					target.getInventory().removeItem(item);
				}
				return true;
			}
			for (L1ItemInstance item : pc.getInventory().getItems()) {
				if (!item.isEquipped()) {
					pc.getInventory().removeItem(item);
				}
			}
			return true;
		} catch (Exception exception) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [케릭명] 라고 입력해 주세요. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(157), true), true);
			return false;
		}
	}
}


