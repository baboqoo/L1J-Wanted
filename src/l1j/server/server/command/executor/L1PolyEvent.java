package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.GameServerSetting;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.serverpackets.message.S_NotificationMessageNoti;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.polymorph.S_PolymorphEvent;

public class L1PolyEvent implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1PolyEvent();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1PolyEvent() {}
	
	private static final ServerBasePacket[] MSG = {
//AUTO SRM: 		new S_NotificationMessageNoti(0, "변신 이벤트가 시작되었습니다. 변신 레벨이 조정됩니다.", "22 b1 4c", 30), new S_NotificationMessageNoti(0, "변신 이벤트가 종료되었습니다. 변신 레벨이 조정됩니다.", "22 b1 4c", 30) }; // CHECKED OK
		new S_NotificationMessageNoti(0, S_SystemMessage.getRefText(1285), "22 b1 4c", 30), 
		new S_NotificationMessageNoti(0, S_SystemMessage.getRefText(1293), "22 b1 4c", 30) 
	};

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String onoff = st.nextToken();
			switch(onoff){
			//case "켬":
			case "on":
				L1World.getInstance().broadcastPacketToAll(MSG[0]);
				L1World.getInstance().broadcastPacketToAll(S_PolymorphEvent.POLY_EVENT_ON);
				GameServerSetting.POLY_LEVEL_EVENT = true;
				return true;
			//case "끔":
			case "off":
				L1World.getInstance().broadcastPacketToAll(MSG[1]);
				L1World.getInstance().broadcastPacketToAll(S_PolymorphEvent.POLY_EVENT_OFF);
				GameServerSetting.POLY_LEVEL_EVENT = false;
				return false;
			default:
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage(cmdName + " [켬/끔] 라고 입력해 주세요. "), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(447), true), true);
				return true;
			}
		} catch (Exception exception) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [켬/끔] 라고 입력해 주세요. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(447), true), true);
			return false;
		}
	}
}



