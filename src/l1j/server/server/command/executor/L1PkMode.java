package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.Config;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1PkMode implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1PkMode();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1PkMode() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String type = st.nextToken();
			//if (type.equals("켬")) {
			if (type.equals("on")) {
				Config.ALT.ALT_NONPVP = true;
//AUTO SRM: 				L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "지금이후 PvP가 정상적으로 가능합니다."), true); // CHECKED OK
				L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,S_SystemMessage.getRefText(525)), true);
				return true;
			}
			//if (type.equals("끔")){
			if (type.equals("off")){
				Config.ALT.ALT_NONPVP = false;
//AUTO SRM: 				L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "지금이후 PvP가 일정시간동안 불가능 합니다."), true); // CHECKED OK
				L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,S_SystemMessage.getRefText(526)), true);
				return true;
			}
			return false;
		} catch (Exception exception) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [켬/끔] 라고 입력해 주세요. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(447), true), true);
			return false;
		}
	}
}


