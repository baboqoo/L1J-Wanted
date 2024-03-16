package l1j.server.server.command.executor;

import l1j.server.server.GMCommandsConfig;
import l1j.server.server.GMCommandsConfig.GMRoom;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StringUtil;

public class L1GMRoom implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1GMRoom();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1GMRoom() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			int i = 0;
			try {
				i = Integer.parseInt(arg);
			} catch (NumberFormatException e) {
			}
			
			GMRoom room = null;
			if (i > 0) {
				room = GMCommandsConfig.ROOMS_FROM_ID.get(i);
				if (room != null) {
					gmTeleport(pc, room.loc.getX(), room.loc.getY(), (short) room.loc.getMapId());
					//pc.sendPackets(new S_SystemMessage(String.format("운영자 귀환 [%d번 '%s']으로 이동했습니다.", i, room.name)), true);
					pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(57), String.valueOf(i), room.name), true);
					return true;
				}
			}
			
			room = GMCommandsConfig.ROOMS_FROM_NAME.get(arg.toLowerCase());
			if (room == null) {
				StringBuilder sb = new StringBuilder();
				//sb.append("==================<귀환 장소>===================\r\n");
				sb.append("=================<Return location>=================\r\n ");
				int index = 0;
				for (GMRoom val : GMCommandsConfig.ROOMS_FROM_NAME.values()) {
					if (index++ % 4 == 0) {
						sb.append(StringUtil.LineString);
					} else {
						if (index > 1) {
							sb.append(", ");
						}
					}
					sb.append(val.id).append(".").append(val.name);
				}
				pc.sendPackets(new S_SystemMessage(sb.toString()), true);
				return false;
			}
			gmTeleport(pc, room.loc.getX(), room.loc.getY(), (short) room.loc.getMapId());
			//pc.sendPackets(new S_SystemMessage(String.format("운영자 귀환 [%d번 '%s']으로 이동했습니다.", i, room.name)), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(57), String.valueOf(i), room.name), true);
			return true;
		} catch (Exception exception) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".귀환 [장소명]을 입력 해주세요.(장소명은 GMCommands.xml을 참조)"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(437), true), true);
			return false;
		}
	}
	
	private void gmTeleport(L1PcInstance pc, int x, int y, short mapId){
		pc.getTeleport().start(x, y, mapId, 5, false);
	}
}


