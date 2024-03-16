package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.GameServerSetting;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;

public class L1SafetyMode implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1SafetyMode();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1SafetyMode() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String status = st.nextToken();
			if (status.equalsIgnoreCase("on")) {
				if (GameServerSetting.SAFETY_MODE) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("이미 보호 모드 중입니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(652), true), true);
					return false;
				}
				GameServerSetting.SAFETY_MODE = true;
//AUTO SRM: 				L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "[시스템]:\\aA보호 모드가 실행 되었습니다."), true); // CHECKED OK
				L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,S_SystemMessage.getRefText(655)), true);
				L1World.getInstance().broadcastPacketToAll(S_SpellBuffNoti.SAFTY_BUFF_ICON_ON);
				return true;
			}
			if (status.equalsIgnoreCase("off")) {
				if (!GameServerSetting.SAFETY_MODE) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("보호 모드 상태가 아닙니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(653), true), true);
					return false;
				}
				GameServerSetting.SAFETY_MODE = false;
//AUTO SRM: 				L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "[시스템]:\\aA보호 모드가 해제 되었습니다."), true); // CHECKED OK
				L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,S_SystemMessage.getRefText(656)), true);
				L1World.getInstance().broadcastPacketToAll(S_SpellBuffNoti.SAFTY_BUFF_ICON_OFF);
				return true;
			}
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(arg + " [켬/끔] 으로 입력하세요."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(447), true), true);
			return false;
		} catch (Exception eee) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(arg + " [켬/끔] 으로 입력하세요."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(447), true), true);
			return false;
		}
	}
}


