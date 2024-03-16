package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1Adena implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Adena();
	}

	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	
	private L1Adena() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (pc == null) {
				return false;
			}
			StringTokenizer stringtokenizer = new StringTokenizer(arg);
			
			int count = Integer.parseInt(stringtokenizer.nextToken());
			L1ItemInstance adena = pc.getInventory(). storeItem(L1ItemId.ADENA, count);
			
			if (adena != null) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage((new StringBuilder()).append(count).append("아데나를 생성했습니다.").toString()), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage((new StringBuilder()).append(count).append(S_SystemMessage.getRefText(169)).toString(), true), true);
			}
			stringtokenizer = null;
			return true;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage((new StringBuilder()). append(".아데나 [액수]로 입력해 주세요. "). toString()), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(170), true), true);
			return false;
		}
	}
}


