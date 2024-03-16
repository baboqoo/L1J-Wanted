package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.Config;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1PlaySupport implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1PlaySupport();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1PlaySupport() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer tok = new StringTokenizer(arg);
			String onoff = tok.nextToken();
			//if (onoff.equals("켬")) {
			if (onoff.equals("on")) {
				if (Config.PSS.PLAY_SUPPORT_ACTIVE) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("이미 플레이서포트가 가동중입니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(527), true), true);
					return false;
				}
				Config.PSS.PLAY_SUPPORT_ACTIVE = true;
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("플레이서포트를 가동시켰습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(528), true), true);
				return true;
			}
			//if (onoff.equals("끔")) {
			if (onoff.equals("off")) {
				if (!Config.PSS.PLAY_SUPPORT_ACTIVE) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("이미 플레이서포트가 가동중이지 않습니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(529), true), true);
					return false;
				}
			    Config.PSS.PLAY_SUPPORT_ACTIVE = false;
//AUTO SRM: 			    pc.sendPackets(new S_SystemMessage("플레이서포트를 중지시켰습니다."), true); // CHECKED OK
			    pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(530), true), true);
			    for (L1PcInstance target : L1World.getInstance().getAllPlayers()) {
			    	target.getConfig().finishPlaySupport();
			    }
//AUTO SRM: 			    L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "운영자가 플레이서포트를 종료 시켰습니다."), true); // CHECKED OK
			    L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,S_SystemMessage.getRefText(531)), true);
			    return true;
			}
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [켬/끔] 라고 입력해 주세요. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(447), true), true);
			return false;
		} catch (Exception exception) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [켬/끔] 라고 입력해 주세요. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(447), true), true);
			return false;
		}
	}
}


