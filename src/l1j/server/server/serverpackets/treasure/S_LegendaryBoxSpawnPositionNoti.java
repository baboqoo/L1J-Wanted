package l1j.server.server.serverpackets.treasure;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_LegendaryBoxSpawnPositionNoti extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_LEGENDARY_BOX_SPAWN_POSITION_NOTI = "[S] S_LegendaryBoxSpawnPositionNoti";
	public static final int NOTI = 0x0a1a;
	
	public S_LegendaryBoxSpawnPositionNoti(int x, int y) {
		write_init();
		write_location(x, y);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_location(int x, int y) {
		writeRaw(0x08);// location
		writeLongLocationReverse(x, y);
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
		return S_LEGENDARY_BOX_SPAWN_POSITION_NOTI;
	}
}
