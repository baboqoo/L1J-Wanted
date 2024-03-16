package l1j.server.server.serverpackets.teaminfo;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_InfinityBattleLeaveMapNoti extends ServerBasePacket {
	private static final String S_INFINITY_BATTLE_LEAVE_MAP_NOTI = "[S] S_InfinityBattleLeaveMapNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x093d;
	
	public static final S_InfinityBattleLeaveMapNoti LEAVE = new S_InfinityBattleLeaveMapNoti();
	
	public S_InfinityBattleLeaveMapNoti() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
		writeH(0x00);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_INFINITY_BATTLE_LEAVE_MAP_NOTI;
	}
}

