package l1j.server.server.serverpackets.message;

import l1j.server.server.utils.BinaryOutputStream;

public class NotificationTeleportDataStream extends BinaryOutputStream {
	public static final byte[] STRING_K	= "4654".getBytes();// %s 지역으로 이동하시겠습니까? \n %d 아데나가 필요합니다.
	public static final int ADENA_COUNT	= 1000;// 텔레포트 비용

	public NotificationTeleportDataStream() {
		super();
		write_stringk(STRING_K);
		write_adenacount(ADENA_COUNT);
	}
	
	void write_stringk(byte[] stringk) {
		writeC(0x0a);// stringk
		writeBytesWithLength(stringk);
	}
	
	void write_adenacount(int adenacount) {
		writeC(0x10);// adenacount
		writeBit(adenacount);
	}
}

