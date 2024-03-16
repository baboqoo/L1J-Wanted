package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1DungeonTimeReset implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1DungeonTimeReset();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1DungeonTimeReset() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String pcName = st.nextToken();
			String dun = st.nextToken();
			L1PcInstance target = L1World.getInstance().getPlayer(pcName);
			if (target == null) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage(pcName + "는 접속중인 캐릭이 아닙니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(pcName  + S_SystemMessage.getRefText(383), true), true);
				return false;
			}
			int type = 0;
			/*if(dun.equalsIgnoreCase("일반"))		type = 1;
			else if(dun.equalsIgnoreCase("피방"))	type = 2;
			else if(dun.equalsIgnoreCase("전함"))	type = 5;
			else if(dun.equalsIgnoreCase("말던"))	type = 4;*/

			if(dun.equalsIgnoreCase("normal"))			type = 1;
			else if(dun.equalsIgnoreCase("PCRoom"))		type = 2;
			else if(dun.equalsIgnoreCase("battleship"))	type = 5;
			else if(dun.equalsIgnoreCase("ti"))		type = 4;
			
			if(type != 0)target.getDungoenTimer().resetTimer(type, true);

//AUTO SRM: 			target.sendPackets(new S_SystemMessage("GM 에 의해 " + dun + " 시간이 초기화 되었습니다."), true); // CHECKED OK
			target.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(384) + dun  + S_SystemMessage.getRefText(385), true), true);
			//pc.sendPackets(new S_SystemMessage(target.getName() + "의 " + dun + "시간을 초기화 시켰습니다."), true); // CHECKED OK
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(105), target.getName(), dun), true);
			st = null;
			return true;
		} catch (Exception exception) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [케릭명] [던전] 라고 입력해 주세요.\r\n던전: 일반 / 피방 / 말던 / 전함"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(387), true), true);
			return false;
		}
	}
}


