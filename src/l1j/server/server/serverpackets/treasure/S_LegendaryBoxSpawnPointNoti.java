package l1j.server.server.serverpackets.treasure;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_LegendaryBoxSpawnPointNoti extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_LEGENDARY_BOX_SPAWN_POINT_NOTI = "[S] S_LegendaryBoxSpawnPointNoti";
	public static final int NOTI = 0x0a19;
			
	public S_LegendaryBoxSpawnPointNoti(int curPoint, int maxPoint, boolean isFirstSend) {
		write_init();
		write_curPoint(curPoint);
		write_maxPoint(maxPoint);
		write_isFirstSend(isFirstSend);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_curPoint(int curPoint) {
		writeRaw(0x08);// curPoint
		writeBit(curPoint);
	}
	
	void write_maxPoint(int maxPoint) {
		writeRaw(0x10);// maxPoint
		writeBit(maxPoint);
	}
	
	void write_isFirstSend(boolean isFirstSend) {
		writeRaw(0x18);// isFirstSend
		writeB(isFirstSend);
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
		return S_LEGENDARY_BOX_SPAWN_POINT_NOTI;
	}
}
