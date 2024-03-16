package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_SiegePromoteNoti extends ServerBasePacket {
	private static final String S_SIEGE_PROMOTE_NOTI = "[S] S_SiegePromoteNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x0066;
	
	public S_SiegePromoteNoti(int surfNumber, int stringIndexMesage, byte[] messageRGB, int duration, int descCastle, String descNPC) {
		write_init();
		write_surfNumber(surfNumber);
		write_stringIndexMesage(stringIndexMesage);
		write_messageRGB(messageRGB);
		write_duration(duration);
		write_descCastle(descCastle);
		write_descNPC(descNPC);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_surfNumber(int surfNumber) {
		writeRaw(0x08);
		writeBit(surfNumber);
	}
	
	void write_stringIndexMesage(int stringIndexMesage) {
		writeRaw(0x10);
		writeBit(stringIndexMesage);
	}
	
	void write_messageRGB(byte[] messageRGB) {
		writeRaw(0x1a);
		writeBytesWithLength(messageRGB);
	}
	
	void write_duration(int duration) {
		writeRaw(0x20);
		writeBit(duration);
	}
	
	void write_descCastle(int descCastle) {
		writeRaw(0x28);
		writeRaw(descCastle);
	}
	
	void write_descNPC(String descNPC) {
		writeRaw(0x32);
		writeStringWithLength(descNPC);
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
		return S_SIEGE_PROMOTE_NOTI;
	}
}

