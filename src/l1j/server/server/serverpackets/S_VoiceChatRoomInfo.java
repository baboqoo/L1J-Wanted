package l1j.server.server.serverpackets;

import l1j.server.Config;
import l1j.server.server.Opcodes;

public class S_VoiceChatRoomInfo extends ServerBasePacket {
	private static final String S_VOICE_CHAT_ROOM_INFO = "[S] S_VoiceChatRoomInfo";
	private byte[] _byte = null;
	public static final int INFO	= 0x091b;
	
	public S_VoiceChatRoomInfo(S_VoiceChatRoomInfo.eChatRoomType roomType, int roomKey, long gameRoomId) {
		write_init();
		write_roomType(roomType);
		write_roomKey(roomKey);
		write_serverKey(Config.VERSION.SERVER_NUMBER);
		write_gameRoomId(gameRoomId);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(INFO);
	}
	
	void write_roomType(S_VoiceChatRoomInfo.eChatRoomType roomType) {
		writeC(0x08);// roomType
		writeC(roomType.value);
	}
	
	void write_roomKey(int roomKey) {
		writeC(0x10);// roomKey
		writeBit(roomKey);
	}
	
	void write_serverKey(int serverKey) {
		writeC(0x18);// serverKey
		writeBit(serverKey);
	}
	
	void write_gameRoomId(long gameRoomId) {
		writeC(0x20);// gameRoomId
		writeBit(gameRoomId);
	}
	
	public enum eChatRoomType{
		NONE(0),
		PARTY(1),
		BLOOD_PLEDGE(2),
		;
		private int value;
		eChatRoomType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eChatRoomType v){
			return value == v.value;
		}
		public static eChatRoomType fromInt(int i){
			switch(i){
			case 0:
				return NONE;
			case 1:
				return PARTY;
			case 2:
				return BLOOD_PLEDGE;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eChatRoomType, %d", i));
			}
		}
	}
	
	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_VOICE_CHAT_ROOM_INFO;
	}
}

