package l1j.server.server.serverpackets.treasure;

import l1j.server.Config;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_LegendaryBoxHasExcavatedNoti extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_LEGENDARY_BOX_HAS_EXCAVATED_NOTI = "[S] S_LegendaryBoxHasExcavatedNoti";
	public static final int NOTI = 0x0a1c;
	
	public S_LegendaryBoxHasExcavatedNoti(int x, int y, String name) {
		write_init();
		write_server_id(Config.VERSION.SERVER_NUMBER);
		write_location(x, y);
		write_name(name);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_server_id(int server_id) {
		writeRaw(0x08);// server_id
		writeBit(server_id);
	}
	
	void write_location(int x, int y) {
		writeRaw(0x10);// location
		writeLongLocationReverse(x, y);
	}
	
	void write_name(String name) {
		writeRaw(0x1a);// name
		writeStringWithLength(name);
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
		return S_LEGENDARY_BOX_HAS_EXCAVATED_NOTI;
	}
}
