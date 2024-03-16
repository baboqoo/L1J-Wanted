package l1j.server.server.command.executor;

import java.util.Map;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.GMCommands;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StringUtil;

public class L1Favorite implements L1CommandExecutor {
	private static Logger _log = Logger.getLogger(L1Favorite.class.getName());
	private static final Map<Integer, String> _faviCom = new HashMap<Integer, String>();

	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Favorite();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1Favorite() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (!_faviCom.containsKey(pc.getId())) {
				_faviCom.put(pc.getId(), StringUtil.EmptyString);
			}
			String faviCom = _faviCom.get(pc.getId());
			//if (arg.startsWith("셋팅")) {
				if (arg.startsWith("config")) {
				// 커멘드의 등록
				StringTokenizer st = new StringTokenizer(arg);
				st.nextToken();
				if (!st.hasMoreTokens()) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("커멘드가 없습니다. "), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(408), true), true);
					return false;
				}
				StringBuilder cmd = new StringBuilder();
				String temp = st.nextToken(); // 커멘드 타입
				if (temp.equalsIgnoreCase(cmdName)) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage(cmdName + " 자신은 등록할 수 없습니다. "), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(409), true), true);
					return false;
				}
				cmd.append(temp + StringUtil.EmptyOneString);
				while (st.hasMoreTokens()) {
					cmd.append(st.nextToken() + StringUtil.EmptyOneString);
				}
				faviCom = cmd.toString().trim();
				_faviCom.put(pc.getId(), faviCom);
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage(faviCom + " 를 등록했습니다. "), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(faviCom  + S_SystemMessage.getRefText(410), true), true);
				return true;
			}
			//if (arg.startsWith("보기")) {
			if (arg.startsWith("view")) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("현재의 등록 커멘드: " + faviCom), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(411) + faviCom, true), true);
				return true;
			}
			if (faviCom.isEmpty()) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("등록하고 있는 커멘드가 없습니다. "), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(412), true), true);
				return true;
			}
			StringBuilder cmd = new StringBuilder();
			StringTokenizer st = new StringTokenizer(arg);
			StringTokenizer st2 = new StringTokenizer(faviCom);
			while (st2.hasMoreTokens()) {
				String temp = st2.nextToken();
				if (temp.startsWith("%")) {
					cmd.append(st.nextToken() + StringUtil.EmptyOneString);
				} else {
					cmd.append(temp + StringUtil.EmptyOneString);
				}
			}
			while (st.hasMoreTokens()) {
				cmd.append(st.nextToken() + StringUtil.EmptyOneString);
			}
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmd + " 를 실행합니다. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(cmd  + S_SystemMessage.getRefText(413), true), true);
			GMCommands.getInstance().handleCommands(pc, cmd.toString());
			return true;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " 셋팅 [커멘드명] " + "| " + cmdName + " 보기 | " + cmdName + " [인수] 라고 입력해 주세요. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(414) + "| " + cmdName  + S_SystemMessage.getRefText(415) + cmdName  + S_SystemMessage.getRefText(416), true), true);
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			return false;
		}
	}
}


