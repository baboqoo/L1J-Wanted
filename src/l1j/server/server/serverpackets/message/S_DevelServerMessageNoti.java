package l1j.server.server.serverpackets.message;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_DevelServerMessageNoti extends ServerBasePacket {
	private static final String S_DEVELSERVER_MESSAGE_NOTI = "[S] S_DevelServerMessageNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x0047;
	
	public S_DevelServerMessageNoti(S_DevelServerMessageNoti.S_KIND messagegKind, String develMessage) {
		write_init();
		write_messagegKind(messagegKind);
		write_develMessage(develMessage);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_messagegKind(S_DevelServerMessageNoti.S_KIND messagegKind) {
		writeRaw(0x08);
		writeRaw(messagegKind.value);
	}
	
	void write_develMessage(String develMessage) {
		writeRaw(0x12);
		writeStringWithLength(develMessage);
	}
	
	public enum S_KIND{
		S_KIND_NORMAL(1),
		S_KIND_WRITE(2),
		S_KIND_ERROR(3),
		;
		private int value;
		S_KIND(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(S_KIND v){
			return value == v.value;
		}
		public static S_KIND fromInt(int i){
			switch(i){
			case 1:
				return S_KIND_NORMAL;
			case 2:
				return S_KIND_WRITE;
			case 3:
				return S_KIND_ERROR;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments S_KIND, %d", i));
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
		return S_DEVELSERVER_MESSAGE_NOTI;
	}
}

