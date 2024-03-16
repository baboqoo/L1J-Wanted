package l1j.server.server.serverpackets.message;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.ColorUtil;

public class S_NotificationMessageNoti extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_NOTIFICATION_MESSAGE_NOTI = "[S] S_NotificationMessageNoti";
	public static final int MESSAGE_NOTI	= 0x0040;
	
	public S_NotificationMessageNoti(int suffileNumber, String notificationMessage, String messageRGB, int duration) {
		this(suffileNumber, notificationMessage, ColorUtil.getColorRgbBytes(messageRGB), duration);
	}
	
	public S_NotificationMessageNoti(int suffileNumber, String notificationMessage, byte[] messageRGB, int duration) {
		write_init();
		write_suffileNumber(suffileNumber << 1);// 6298:군주, 6299:질리언, 6300:공주, 6301:남법사, 6302:남용기사, 6303:?, 6304:크리스터, 6993:리니지마크
		write_notificationMessage(notificationMessage);
		write_messageRGB(messageRGB);
		write_duration(duration);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(MESSAGE_NOTI);
	}
	
	void write_suffileNumber(int suffileNumber) {
		writeRaw(0x08);// suffileNumber
		writeBit(suffileNumber);
	}
	
	void write_notificationMessage(String notificationMessage) {
		writeRaw(0x12);// notificationMessage
		writeS2(notificationMessage);
	}
	
	void write_messageRGB(byte[] messageRGB) {
		writeRaw(0x1a);// messageRGB
		writeBytesWithLength(messageRGB);
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
		return S_NOTIFICATION_MESSAGE_NOTI;
	}
}
