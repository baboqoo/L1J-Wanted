package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Weather;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1ChangeWeather implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1ChangeWeather();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1ChangeWeather() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer tok = new StringTokenizer(arg);
			
			int weather = Integer.parseInt(tok.nextToken());
			L1World.getInstance().setWeather(weather);
			L1World.getInstance().broadcastPacketToAll(new S_Weather(weather), true);
			return true;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " 0~3(눈), 16~19(비)라고 입력 해주세요."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(265), true), true);
			return false;
		}
	}
}


