package l1j.server.server.command.executor;

import l1j.server.server.clientpackets.C_Attr;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_MessageYN;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1ServerReset implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1ServerReset();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1ServerReset() {}

	//private static final S_MessageYN CONFIRM = new S_MessageYN(C_Attr.MSGCODE_SERVER_RESET, C_Attr.YN_MESSAGE_CODE, "정말로 서버 초기화를 진행하시겠습니까?\n(※ 경고 : 데이터가 삭제됩니다.)");
	private static final S_MessageYN CONFIRM = new S_MessageYN(C_Attr.MSGCODE_SERVER_RESET, C_Attr.YN_MESSAGE_CODE, "Are you sure you want to proceed with a server reset?\n(Warning: Data will be deleted.");
	
	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (pc == null) {
				return false;
			}
			pc.sendPackets(CONFIRM);
			return true;
		} catch (Exception exception) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " 라고 입력해 주세요. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(669) + cmdName, true), true);
			return false;
		}
	}
}


