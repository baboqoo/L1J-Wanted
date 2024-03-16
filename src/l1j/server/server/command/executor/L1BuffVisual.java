package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.SpecialEventHandler;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1BuffVisual implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1BuffVisual();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1BuffVisual() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try{
			StringTokenizer st = new StringTokenizer(arg);
			String type = st.nextToken();
			switch(type){
			case "1":
				SpecialEventHandler.getInstance().screenPullUp(pc);
				return true;
			case "2":
				SpecialEventHandler.getInstance().screenBlessing(pc);
				return true;
			case "3":
				SpecialEventHandler.getInstance().screenRawHorse(pc);
				return true;
			case "4":
				SpecialEventHandler.getInstance().screenBlack(pc);
				return true;
			case "5":
				SpecialEventHandler.getInstance().screenComa(pc);
				return true;
			default:
				pc.sendPackets(new S_SystemMessage("---------------------------------------------------"), true);
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage(" 화면1~화면4 (1:풀업 2:축복 3:생마 4:흑사 5:코마)"), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(231), true), true);
				pc.sendPackets(new S_SystemMessage("---------------------------------------------------"), true);
				return false;
			}
		}catch (Exception e) {
			pc.sendPackets(new S_SystemMessage("---------------------------------------------------"), true);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(" 화면1~화면4 (1:풀업 2:축복 3:생마 4:흑사 5:코마)"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(231), true), true);
			pc.sendPackets(new S_SystemMessage("---------------------------------------------------"), true);
			return false;
		}
	}
}


