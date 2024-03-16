package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.controller.DogFightController;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1DogFight implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1DogFight();
	}

	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	
	private L1DogFight() {}

//AUTO SRM: 	private static final S_SystemMessage COMMAND_MESSAGE = new S_SystemMessage(".투견 [우승/티켓/자동] 으로 입력해주세요."); // CHECKED OK
	
	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String type = st.nextToken();
			switch(type){
			//case "우승":
			case "championship":
				String target = st.nextToken();
				//if (target.equalsIgnoreCase("홀")) {
				if (target.equalsIgnoreCase("first")) {
					DogFightController.getInstance().setWinDog(0);
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage(target + " 투견이 우승 지목되었습니다."), true);// CHECKED OK
					pc.sendPackets(new S_SystemMessage(target  + S_SystemMessage.getRefText(346), true), true);
					return true;
				}
				//if (target.equalsIgnoreCase("짝")) {
				if (target.equalsIgnoreCase("second")) {
					DogFightController.getInstance().setWinDog(1);
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage(target + " 투견이 우승 지목되었습니다."), true);// CHECKED OK
					pc.sendPackets(new S_SystemMessage(target  + S_SystemMessage.getRefText(346), true), true);
					return true;
				}
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage(".투견 [우승] [홀/짝] 으로 입력해주세요."), true);// CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(347), true), true);
				return false;
			//case "티켓":
			case "ticket":
				String cast = st.nextToken();
				if (cast.equalsIgnoreCase("on")){
					if (DogFightController.getInstance()._GMBroadcast) {
//AUTO SRM: 						pc.sendPackets(new S_SystemMessage("이미 켬 상태입니다."), true);// CHECKED OK
						pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(348), true), true);
						return false;
					}
					DogFightController.getInstance()._GMBroadcast = true;
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("투견 티켓 현황이 출력됨니다."), true);// CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(349), true), true);
					return true;
				}
				if (cast.equalsIgnoreCase("off")){
					if (!DogFightController.getInstance()._GMBroadcast) {
//AUTO SRM: 						pc.sendPackets(new S_SystemMessage("이미 끔 상태입니다."), true);// CHECKED OK
						pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(350), true), true);
						return false;
					}
					DogFightController.getInstance()._GMBroadcast = false;
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("투견 티켓 현황을 종료합니다."), true);// CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(351), true), true);
					return true;
				}
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage(".투견 [티켓] [켬/끔] 으로 입력해주세요."), true);// CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(352), true), true);
				return false;
			//case "자동":
			case "auto":
				String auto = st.nextToken();
				if (auto.equalsIgnoreCase("on")) {
					if (DogFightController.getInstance()._autowin) {
//AUTO SRM: 						pc.sendPackets(new S_SystemMessage("이미 켬 상태입니다."), true);// CHECKED OK
						pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(348), true), true);
						return false;
					}
					DogFightController.getInstance()._autowin = true;
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("투견 자동 승리 시스템이 시작되엇습니다.(티켓판매 갯수에따라 결정)"), true);// CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(353), true), true);
					return true;
				}
				if (auto.equalsIgnoreCase("off")) {
					if (!DogFightController.getInstance()._autowin) {
//AUTO SRM: 						pc.sendPackets(new S_SystemMessage("이미 끔 상태입니다."), true);// CHECKED OK
						pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(350), true), true);
						return false;
					}
					DogFightController.getInstance()._autowin = false;
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("투견 자동 승리 시스템이 종료 되엇습니다.(무조작)"), true);// CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(354), true), true);
					return true;
				}
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage(".투견 [자동] [켬/끔] 으로 입력해주세요."), true);// CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(355), true), true);
				return false;
			default:
				//pc.sendPackets(COMMAND_MESSAGE);
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(345), true), true);
				return false;
			}
		} catch (Exception e) {
			//pc.sendPackets(COMMAND_MESSAGE);
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(345), true), true);
			return false;
		}
	}
}


