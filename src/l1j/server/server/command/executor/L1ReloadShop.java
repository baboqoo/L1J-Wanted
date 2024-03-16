package l1j.server.server.command.executor;

import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Npc;

public class L1ReloadShop implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1ReloadShop();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1ReloadShop() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			int npcid = Integer.parseInt(arg);
			L1Npc npc = NpcTable.getInstance().getTemplate(npcid);
			ShopTable.getInstance().Reload(npcid);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("상점 엔피씨 : " + npc.getDesc() + " 물품이 리로드 되었습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(633) + npc.getDesc()  + S_SystemMessage.getRefText(634), true), true);
			return true;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [엔피씨ID]"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(635), true), true);
			return false;
		}
	}
}


