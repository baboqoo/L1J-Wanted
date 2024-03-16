package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StringUtil;

public class L1Process implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Process();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1Process() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st	= new StringTokenizer(arg);
			String targetName	= st.nextToken();
			L1PcInstance target	= L1World.getInstance().getPlayer(targetName);
			if (target == null) {
				//pc.sendPackets(new S_SystemMessage(String.format("%s 라는 케릭터는 월드상에 존재하지 않습니다.", targetName)), true);
				pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(47), targetName), true);
				return false;
			}
			if (target.getNetConnection() == null) {
				//pc.sendPackets(new S_SystemMessage(String.format("%s 라는 케릭터는 정상적인 유저가 아닙니다.", targetName)), true);
				pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(71), targetName), true);
				return false;
			}
			if (target.getNetConnection().getLoginSession() == null) {
				//pc.sendPackets(new S_SystemMessage(String.format("%s 라는 케릭터는 세션이 존재하지 않습니다.", targetName)), true);
				pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(58), targetName), true);
				return false;
			}
			String process = target.getNetConnection().getLoginSession().getProcess();
			if (StringUtil.isNullOrEmpty(process)) {
				//pc.sendPackets(new S_SystemMessage(String.format("%s 라는 케릭터는 프로세스 정보가 존재하지 않습니다.", targetName)), true);
				pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(72), targetName), true);
				return false;
			}
			StringBuilder sb = new StringBuilder();
			/*sb.append("======================\r\n");
			sb.append(" 캐릭터 : ").append(targetName).append(" 세션 정보 확인\r\n");
			sb.append("======================\r\n");
			sb.append(" 시스템 정보\r\n");
			sb.append(target.getNetConnection().getLoginSession().toString()).append(StringUtil.LineString);
			sb.append("======================\r\n");
			sb.append(" 프로세스 정보\r\n");
			sb.append(process.replaceAll(StringUtil.CommaString, StringUtil.LineString)).append(StringUtil.LineString);
			sb.append("======================");*/

			sb.append("======================\r\n");
			sb.append(" Character : ").append(targetName).append(" Check session information\r\n");
			sb.append("======================\r\n");
			sb.append(" System Information\r\n");
			sb.append(target.getNetConnection().getLoginSession().toString()).append(StringUtil.LineString);
			sb.append("======================\r\n");
			sb.append(" Process Information\r\n");			
			sb.append(process.replaceAll(StringUtil.CommaString, StringUtil.LineString)).append(StringUtil.LineString);
			sb.append("======================");

			pc.sendPackets(new S_SystemMessage(sb.toString()), true);
			sb = null;
			st = null;
			return true;
		} catch (Exception exception) {
			//pc.sendPackets(new S_SystemMessage(String.format("%s [캐릭터] 라고 입력해 주세요.", cmdName)), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(73), cmdName), true);
			return false;
		}
	}
}


