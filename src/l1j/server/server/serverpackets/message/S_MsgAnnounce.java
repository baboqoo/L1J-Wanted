package l1j.server.server.serverpackets.message;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.ColorUtil;

public class S_MsgAnnounce extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_MSG_ANNOUNCE = "[S] S_MsgAnnounce";
	public static final int ANNOUNCE	= 0x0342;
	
	public S_MsgAnnounce(String message, String color) {
		this(message, ColorUtil.getColorRgbBytes(color));
	}
	
	public S_MsgAnnounce(String message, byte[] color) {
		write_init();
		write_pos(S_MsgAnnounce.eMsgPos.TOP_CHAT_POS);
		write_message(message);
		write_color(color);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(ANNOUNCE);
	}
	
	void write_pos(S_MsgAnnounce.eMsgPos pos) {
		writeRaw(0x08);// pos
		writeRaw(pos.value);
	}
	
	void write_message(String message) {
		writeRaw(0x12);// message
		writeStringWithLength(message);
	}
	
	void write_color(byte[] color) {
		writeRaw(0x1a);// color
		writeBytesWithLength(color);
	}
	
	public enum eMsgPos{
		TOP_POS(0),
		TOP_CHAT_POS(1),
		;
		private int value;
		eMsgPos(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eMsgPos v){
			return value == v.value;
		}
		public static eMsgPos fromInt(int i){
			switch(i){
			case 0:
				return TOP_POS;
			case 1:
				return TOP_CHAT_POS;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eMsgPos, %d", i));
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
		return S_MSG_ANNOUNCE;
	}
}
