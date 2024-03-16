package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_TurnOnTeleportFlagNoti extends ServerBasePacket {
	private static final String S_TURN_ON_TELEPORT_FLAG_NOTI = "[S] S_TurnOnTeleportFlagNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x0091;
	public static final S_TurnOnTeleportFlagNoti TURN_ON_TELEPORT_NOTI	= new S_TurnOnTeleportFlagNoti();

	public S_TurnOnTeleportFlagNoti() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
		writeH(0x00);
	}
	
	void write_empty(int empty) {
		writeRaw(0x08);
		writeRaw(empty);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_TURN_ON_TELEPORT_FLAG_NOTI;
	}
}

