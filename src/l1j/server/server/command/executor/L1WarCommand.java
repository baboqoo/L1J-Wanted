package l1j.server.server.command.executor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;

import l1j.server.server.controller.action.War;
import l1j.server.server.datatables.CastleTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StringUtil;

public class L1WarCommand implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1WarCommand();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1WarCommand() {}
	
	//private static final String CASTLE_NAME_REGEX = "켄트|오크|기란|하이네";
	//Kent|Oak|Giran|Heine
	private static final String CASTLE_NAME_REGEX = "Kent|Orc|Giran|Heine";


	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer tok = new StringTokenizer(arg);
			
			String flag = tok.nextToken();
			String name = tok.nextToken();
			if (!name.matches(CASTLE_NAME_REGEX)) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("성이름 목록: 켄트, 오크, 기란, 하이네"), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(728), true), true);
				return false;
			}
			
			boolean isNowWar = War.getInstance().isNowWarFromName(name);
			switch (flag) {
			//case "시작":
			case "start":
				if (isNowWar) {
					//pc.sendPackets(new S_SystemMessage(String.format("'%s' 성은 이미 전쟁중에 있습니다.", name)), true);
					pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(76), name), true);
					return false;
				}
				int minute = Integer.parseInt(tok.nextToken());
				start(pc, name, minute);
				return true;
			//case "종료":
			case "stop":
				if (!isNowWar) {
					//pc.sendPackets(new S_SystemMessage(String.format("'%s' 성은 이미 종료되어 있습니다.", name)), true);
					pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(77), name), true);
					return false;
				}
				end(pc, name);
				return true;
			default:
				//pc.sendPackets(new S_SystemMessage(String.format("%s [시작/종료] [성이름] [대기분]", cmdName)), true);
				pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(78), cmdName), true);
				return false;
			}
		} catch (Exception e) {
			//pc.sendPackets(new S_SystemMessage(String.format("%s [시작/종료] [성이름] [대기분]", cmdName)), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(78), cmdName), true);
			return false;
		}
	}
	
	void start(L1PcInstance pc, String name, int minute) {
		Calendar cal = (Calendar) Calendar.getInstance().clone();
		if (minute != 0) {
			cal.add(Calendar.MINUTE, minute);
		}

		CastleTable.getInstance().updateWarTime(name, cal);
		War.getInstance().setWarStartTime(name, cal);
		
		SimpleDateFormat formatter = new SimpleDateFormat(StringUtil.DateFormatStringMinut);
		//pc.sendPackets(new S_SystemMessage(String.format("공성시간이 %s 으로 변경 되었습니다.", formatter.format(cal.getTime()))), true);
		pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(79), formatter.format(cal.getTime())), true);
		//pc.sendPackets(new S_SystemMessage(String.format("%d분 뒤 공성이 시작합니다.", minute)), true);
		pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(80), String.valueOf(minute)), true);
		formatter = null;
	}
	
	void end(L1PcInstance pc, String name) {
		War.getInstance().setWarExitTime(pc, name);
	}
}


