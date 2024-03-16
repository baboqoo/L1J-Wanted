package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.Config;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.RepeatTask;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.StringUtil;

public class L1Userinc implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Userinc();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1Userinc() {}

	private static int _buffMaxCount = 0;
	private static int _remainBuffTime = 0;
	private static int _totalBuffTime = 0;

	class UserCountBuffTimer extends RepeatTask {
		public UserCountBuffTimer() {
			super(2000);
		}

		@Override
		public void execute() {

			_remainBuffTime = _remainBuffTime - 2;

			if (_remainBuffTime < 1) {
				_remainBuffTime = 0;

				Config.ALT.WHOIS_CONTER = _buffMaxCount;
				cancel();
				_UserCountBuffTimer = null;
			} else {
				int incCount = (_buffMaxCount * 2 / _totalBuffTime);

				int additionalBuffRatio = ((_buffMaxCount * 1000) / _totalBuffTime) % 1000;

				if (CommonUtil.random(1000) < additionalBuffRatio) {
					incCount += 2;
				}

				Config.ALT.WHOIS_CONTER += incCount;
			}
		}
	}

	private static UserCountBuffTimer _UserCountBuffTimer = null;

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String inc = st.nextToken();

			//if (inc.equalsIgnoreCase("초기화")) {
			if (inc.equalsIgnoreCase("reset")) {
				if (_UserCountBuffTimer != null) {
					_UserCountBuffTimer.cancel();
					_UserCountBuffTimer = null;
				}

				Config.ALT.WHOIS_CONTER = 0;
				_buffMaxCount = 0;
				_remainBuffTime = 0;
				_totalBuffTime = 0;
				return true;
			} else if (inc.equalsIgnoreCase(StringUtil.ZeroString)) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("현재 뻥튀기 인원 : " + Config.ALT.WHOIS_CONTER), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(715) + Config.ALT.WHOIS_CONTER, true), true);
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("전체 뻥튀기 인원 : " + _buffMaxCount), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(716) + _buffMaxCount, true), true);
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("남은 뻥튀기 인원 : " + (_buffMaxCount - Config.ALT.WHOIS_CONTER)), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(717) + (_buffMaxCount - Config.ALT.WHOIS_CONTER), true), true);
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("전체 뻥튀기 시간 : " + _totalBuffTime + "초"), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(718) + _totalBuffTime  + S_SystemMessage.getRefText(719), true), true);
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("남은 뻥튀기 시간 : " + _remainBuffTime + "초"), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(720) + _remainBuffTime  + S_SystemMessage.getRefText(719), true), true);
				return true;
			}

			int count = Integer.parseInt(st.nextToken());

			if (count < 0) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("0 이상의 숫자를 넣어주세요."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(721), true), true);
				return false;
			}

			if (inc.equalsIgnoreCase("~")) {
				int time = Integer.parseInt(st.nextToken());

				if (time < 1) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("0 이상의 시간를 넣어주세요."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(722), true), true);
					return false;
				}

				if (time < _totalBuffTime) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("입력된 시간이 현재 설정된 뻥튀기 시간보다 적습니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(723), true), true);
					return false;
				}

				if (count < _buffMaxCount) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("입력된 숫자가 현재 설정된 뻥튀기 인원보다 적습니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(724), true), true);
					return false;
				}

				if (_UserCountBuffTimer != null) {
					_UserCountBuffTimer.cancel();
					_UserCountBuffTimer = null;
				}

				_remainBuffTime += time - _totalBuffTime;
				_totalBuffTime = time;
				_buffMaxCount = count;

				_UserCountBuffTimer = new UserCountBuffTimer();
				GeneralThreadPool.getInstance().execute(_UserCountBuffTimer);

				return true;

			}
			return false;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("+뻥 [0] 로 현재 상태 확인."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(725), true), true);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("+뻥 [~] [숫자] [시간] (시간 단위는 초)으로 자동 뻥."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(726), true), true);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("+뻥 [초기화] 로 초기화."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(727), true), true);
			return false;
		}
	}
}


