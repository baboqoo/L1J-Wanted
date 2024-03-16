package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.GameServerSetting;
import l1j.server.server.controller.DollRaceController;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StringUtil;

public class L1DollRace implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1DollRace();
	}

	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	
	private L1DollRace() {}

//AUTO SRM: 	private static final S_SystemMessage COMMAND_MESSAGE = new S_SystemMessage(".인경 [조작/확률/우승/티켓] 으로 입력해주세요.");
	
	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String type = st.nextToken();
			switch(type){
			//case "조작":
			case "operation":
				try{
					int type2 = Integer.parseInt(st.nextToken());
					int c1 = Integer.parseInt(st.nextToken());
					int c2 = Integer.parseInt(st.nextToken());
					int c3 = Integer.parseInt(st.nextToken());
					if (type2 == 0) {
						DollRaceController.ratio_normal = c1;
						DollRaceController.ratio_gura = c2;
						DollRaceController.ratio_big_gura= c2;
//AUTO SRM: 						pc.sendPackets(new S_SystemMessage("구라 판정을" + DollRaceController.ratio_normal + StringUtil.CommaString + DollRaceController.ratio_gura + StringUtil.CommaString + DollRaceController.ratio_big_gura+ " 로 설정하였습니다.", true), true);// CHECKED OK
						pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(357) + DollRaceController.ratio_normal  + StringUtil.CommaString  + DollRaceController.ratio_gura  + StringUtil.CommaString  + DollRaceController.ratio_big_gura + S_SystemMessage.getRefText(358), true), true);
						return true;
					}
					if (type2 == 1) {
						if (c1 >= DollRaceController.ratio_good_maximum || c2 >= DollRaceController.ratio_average_maximum || c3 >= DollRaceController.ratio_bad_maximum) {
//AUTO SRM: 							pc.sendPackets(new S_SystemMessage("최저속도가 최고속도보다 높을수 없습니다.", true), true); // CHECKED OK
							pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(359), true), true);
							return false;
						}

						DollRaceController.ratio_good_minimum = c1;
						DollRaceController.ratio_average_minimum = c2;
						DollRaceController.ratio_bad_minimum = c3;
//AUTO SRM: 						pc.sendPackets(new S_SystemMessage("최저속도를" + DollRaceController.ratio_good_minimum + StringUtil.CommaString + DollRaceController.ratio_average_minimum + StringUtil.CommaString + DollRaceController.ratio_bad_minimum + " 으로 설정하였습니다.", true), true);// CHECKED OK
						pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(360) + DollRaceController.ratio_good_minimum  + StringUtil.CommaString  + DollRaceController.ratio_average_minimum  + StringUtil.CommaString  + DollRaceController.ratio_bad_minimum  + S_SystemMessage.getRefText(361), true), true);
						return true;
					}
					if (type2 == 2) {
						DollRaceController.ratio_good_down = c1;
						DollRaceController.ratio_average_down = c2;
						DollRaceController.ratio_bad_down = c3;
//AUTO SRM: 						pc.sendPackets(new S_SystemMessage("넘어짐 판정을" + DollRaceController.ratio_good_down + StringUtil.CommaString + DollRaceController.ratio_average_down + StringUtil.CommaString + DollRaceController.ratio_bad_down + " 으로 설정하였습니다.", true), true);// CHECKED OK
						pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(362) + DollRaceController.ratio_good_down  + StringUtil.CommaString  + DollRaceController.ratio_average_down  + StringUtil.CommaString  + DollRaceController.ratio_bad_down  + S_SystemMessage.getRefText(361), true), true);
						return true;
					}
					if (type2 == 3) {
						if (DollRaceController.ratio_good_minimum >= c1 || DollRaceController.ratio_average_minimum >= c2 || DollRaceController.ratio_bad_minimum >= c3) {
//AUTO SRM: 							pc.sendPackets(new S_SystemMessage("최고속도가 최저속도보다 낮을수 없습니다.", true), true); // CHECKED OK
							pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(363), true), true);
							return false;
						}
						DollRaceController.ratio_good_maximum = c1;
						DollRaceController.ratio_average_maximum = c2;
						DollRaceController.ratio_bad_maximum = c3;
//AUTO SRM: 						pc.sendPackets(new S_SystemMessage("최고속도를 " + DollRaceController.ratio_good_maximum + StringUtil.CommaString + DollRaceController.ratio_average_maximum + StringUtil.CommaString + DollRaceController.ratio_bad_maximum + " 으로 설정하였습니다.", true), true);// CHECKED OK
						pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(364) + DollRaceController.ratio_good_maximum  + StringUtil.CommaString  + DollRaceController.ratio_average_maximum  + StringUtil.CommaString  + DollRaceController.ratio_bad_maximum  + S_SystemMessage.getRefText(361), true), true);
						return true;
					}
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage(".인경 [조작] type[0=구라,1=최저속도,2=다운,3=최고속도] 좋음 보통 나쁨"), true);// CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(365), true), true);
					return false;
				}catch (Exception e) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage(".인경 [조작] type[0=구라,1=최저속도,2=다운,3=최고속도] 좋음 보통 나쁨"), true);// CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(365), true), true);
					return false;
				}
			//case "확률":
			case "percentage":
//AUTO SRM: 				if(GameServerSetting.CHANGE_DOLL_RACE_SPEED == 0)		pc.sendPackets(new S_SystemMessage("인경 확률: 정상"), true);// CHECKED OK
				if(GameServerSetting.CHANGE_DOLL_RACE_SPEED == 0)		pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(366), true), true);
//AUTO SRM: 				else if(GameServerSetting.CHANGE_DOLL_RACE_SPEED == 1)	pc.sendPackets(new S_SystemMessage("인경 확률: 구라"), true);// CHECKED OK
				else if(GameServerSetting.CHANGE_DOLL_RACE_SPEED == 1)	pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(367), true), true);
//AUTO SRM: 				else													pc.sendPackets(new S_SystemMessage("인경 확률: 개구라"), true);// CHECKED OK
				else													pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(368), true), true);
				return true;
			//case "우승":
			case "championship":			
				try{
					int value = Integer.parseInt(st.nextToken());
					if (value < 1 || value > 5) {
//AUTO SRM: 						pc.sendPackets(new S_SystemMessage("1에서 5 사이에서 입력해주세요."), true);// CHECKED OK
						pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(369), true), true);
						return false;
					}
					DollRaceController.getInstance().setWinDoll(value);
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage(value + " 레인의 인형이 우승 지목되었습니다."), true);// CHECKED OK
					pc.sendPackets(new S_SystemMessage(value  + S_SystemMessage.getRefText(370), true), true);
					return true;
				}catch (Exception e) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage(".인경 [우승] [1~5] 으로 입력해주세요."), true);// CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(371), true), true);
					return false;
				}
			//case "티켓":
			case "ticket":
				String cast = st.nextToken();
				if (cast.equalsIgnoreCase("on")) {
					if (DollRaceController.getInstance()._GMBroadcast) {
//AUTO SRM: 						pc.sendPackets(new S_SystemMessage("이미 켬 상태입니다."), true);// CHECKED OK
						pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(348), true), true);
						return false;
					}
					DollRaceController.getInstance()._GMBroadcast = true;
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("인형경주 티켓 현황이 출력됨니다."), true);// CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(372), true), true);
					return true;
				}
				if (cast.equalsIgnoreCase("off")) {
					if (!DollRaceController.getInstance()._GMBroadcast) {
//AUTO SRM: 						pc.sendPackets(new S_SystemMessage("이미 끔 상태입니다."), true);// CHECKED OK
						pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(350), true), true);
						return false;
					}
					DollRaceController.getInstance()._GMBroadcast = false;
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("인형경주 티켓 현황을 종료합니다."), true);// CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(373), true), true);
					return true;
				}
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage(".인경 [티켓] [켬/끔] 으로 입력해주세요."), true);// CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(352), true), true);
				return false;
			default:
				//pc.sendPackets(COMMAND_MESSAGE);
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(356), true), true);
				return false;
			}
		} catch (Exception e) {
			//pc.sendPackets(COMMAND_MESSAGE);
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(356), true), true);
			return false;
		}
	}
}


