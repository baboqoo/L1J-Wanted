package l1j.server.server.serverpackets.message;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.ColorUtil;

public class S_NotificationStringKIndex extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_NOTIFICATION_STRING_K_INDEX = "[S] S_NotificationStringKIndex";
	public static final int STRING_K_INDEX	= 0x0067;
	
	public S_NotificationStringKIndex(int suffileNumber, int stringIndex, String rgb, int duration) {
		this(suffileNumber, stringIndex, ColorUtil.getColorRgbBytes(rgb), duration);
	}
	
	public S_NotificationStringKIndex(int suffileNumber, int stringIndex, byte[] rgb, int duration) {
		write_init();
		write_suffileNumber(suffileNumber << 1);
		write_stringIndex(stringIndex << 1);
		write_rgb(rgb);
		write_duration(duration);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(STRING_K_INDEX);
	}
	
	void write_suffileNumber(int suffileNumber) {
		writeRaw(0x08);// suffileNumber
		writeBit(suffileNumber);
	}
	
	void write_stringIndex(int stringIndex) {
		writeRaw(0x10);// stringIndex
		writeBit(stringIndex);
	}
	
	void write_rgb(byte[] rgb) {
		writeRaw(0x1a);// rgb
		writeBytesWithLength(rgb);
	}
	
	void write_duration(int duration) {
		writeRaw(0x20);// duration
		writeRaw(duration);
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
		return S_NOTIFICATION_STRING_K_INDEX;
	}
}
