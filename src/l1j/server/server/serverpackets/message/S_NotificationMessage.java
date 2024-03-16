package l1j.server.server.serverpackets.message;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.ColorUtil;

public class S_NotificationMessage extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_NOTIFICATION_MESSAGE = "[S] S_NotificationMessage";
	public static final int MESSAGE	= 0x013c;
	
	public S_NotificationMessage(display_position pos, String desc, String messageRGB, int duration) {
		this(pos, desc, ColorUtil.getColorRgbBytes(messageRGB), duration);
	}
	
	public S_NotificationMessage(display_position pos, String desc, byte[] messageRGB, int duration) {
		write_init();
		write_pos(pos);
		write_desc(desc);
		write_messageRGB(messageRGB);
		write_duration(duration);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(MESSAGE);
	}
	
	void write_pos(display_position pos) {
		writeRaw(0x08);// pos
		writeRaw(pos.value);
	}
	
	void write_desc(String desc) {
		writeRaw(0x12);// desc
		writeS2(desc);
	}
	
	void write_messageRGB(byte[] messageRGB) {
		writeRaw(0x1a);// messageRGB
		writeBytesWithLength(messageRGB);
	}
	
	void write_duration(int duration) {
		writeRaw(0x20);// duration
		writeRaw(duration);
	}
	
	void write_serverno(int serverno) {
		writeRaw(0x28);
		writeBit(serverno);
	}
	
	public enum display_position{
		screen_top(1),
		screen_middle(2),
		screen_bottom(3);
		private int value;
		display_position(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(display_position v){
			return value == v.value;
		}
		public static display_position fromInt(int i){
			switch(i){
			case 1:
				return screen_top;
			case 2:
				return screen_middle;
			case 3:
				return screen_bottom;
			default:
				return null;
			}
		}
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
		return S_NOTIFICATION_MESSAGE;
	}
}
