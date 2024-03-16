package l1j.server.server.command.executor;

import l1j.server.server.Account;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1AccountBanKick implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1AccountBanKick();
	}

	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	
	private L1AccountBanKick() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			
			L1PcInstance target = L1World.getInstance().getPlayer(arg);
			if (target == null)
				target = CharacterTable.getInstance().restoreCharacter(arg);

			if (target != null){ // 어카운트를 BAN 한다
				Account.ban(target.getAccountName());
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage(target.getName() + " 를 계정압류 하였습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(target.getName()  + S_SystemMessage.getRefText(155), true), true);
				target.sendPackets(S_Disconnect.DISCONNECT);
				
				if (target.getOnlineStatus() == 1) {
					target.sendPackets(S_Disconnect.DISCONNECT);
				}
				return true;
			}
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("그러한 이름의 캐릭터는 월드내에는 존재하지 않습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(156), true), true);
			return false;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [캐릭터명] 으로 입력해 주세요."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(157), true), true);
			return false;
		}
	}
}


