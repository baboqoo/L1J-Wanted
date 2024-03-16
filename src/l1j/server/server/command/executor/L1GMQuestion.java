package l1j.server.server.command.executor;

import l1j.server.server.model.L1Question;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1GMQuestion implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1GMQuestion();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1GMQuestion() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try{
			if (L1Question.mainstart) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("현재 설문조사가 진행중입니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(431), true), true);
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("진행중인 설문 내용 : " + L1Question.maintext), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(432) + L1Question.maintext, true), true);
			}
			L1Question.getInstance(arg);
			return true;
		} catch(Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".설문 [설문내용] 을 입력 해주세요"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(433), true), true);
			return false;
		}
	}
}


