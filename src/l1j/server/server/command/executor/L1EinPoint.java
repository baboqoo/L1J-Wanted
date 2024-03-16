package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1EinPoint implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1EinPoint();
	}

	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	
	private L1EinPoint() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st	= new StringTokenizer(arg);
			String name			= st.nextToken();
			int id				= Integer.parseInt(st.nextToken());
			L1PcInstance user	= L1World.getInstance().getPlayer(name);
			if (user != null) {
				user.addEinPoint(id);
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage(user.getName() + "에게 아인포인트 " + id + "개를 주었습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(user.getName()  + S_SystemMessage.getRefText(393) + id  + S_SystemMessage.getRefText(320), true), true);
				return true;
			}
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("존재하지 않는 유저 입니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(394), true), true);
			return false;
		} catch (Exception exception) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [캐릭터명] [수량] 라고 입력해 주세요. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(395), true), true);
			return false;
		}
	}
}


