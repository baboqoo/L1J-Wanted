package l1j.server.server.command.executor;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1ServerSave implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1ServerSave();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1ServerSave() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			save();
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("서버저장이 완료되었습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(670), true), true);
			return true;
		} catch (Exception exception) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " 라고 입력해 주세요. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage( S_SystemMessage.getRefText(669) + cmdName, true), true);
			return false;
		}
	}
	
	private void save() {
		for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
			if (player == null) continue;
			try {
				player.save();
				player.saveInventory();
			} catch (Exception ex) {
				player.saveInventory();
				//System.out.println("저장 명령어 에러(인벤만 저장함): " + ex);
				System.out.println("Save command error (save inventory only): " + ex);
			}
		}
	}
}


