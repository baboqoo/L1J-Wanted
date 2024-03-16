package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1Poly implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Poly();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1Poly() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String name = st.nextToken();
			int polyid = Integer.parseInt(st.nextToken());

			L1PcInstance tg = L1World.getInstance(). getPlayer(name);

			if (tg == null) {
				pc.sendPackets(new S_ServerMessage(73, name), true); // \f1%0은 게임을 하고 있지 않습니다.
				return false;
			}
			try {
				L1PolyMorph.doPoly(tg, polyid, 604800, L1PolyMorph.MORPH_BY_GM);
				return true;
			} catch (Exception exception) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage(".변신 [캐릭터명] [그래픽ID] 라고 입력해 주세요. "), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(532), true), true);
				return false;
			}
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [캐릭터명] [그래픽ID] 라고 입력해 주세요. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(532), true), true);
			return false;
		}
	}
}


