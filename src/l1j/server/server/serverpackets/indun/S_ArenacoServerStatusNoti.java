package l1j.server.server.serverpackets.indun;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_ArenacoServerStatusNoti extends ServerBasePacket {
	private static final String S_ARENACO_SERVER_STATUS_NOTI = "[S] S_ArenacoServerStatusNoti";
	private byte[] _byte = null;
	public static final int NOTI	= 0x02bc;
	
	public static final S_ArenacoServerStatusNoti INTERAL_ERROR	= new S_ArenacoServerStatusNoti(S_ArenacoServerStatusNoti.eStatus.INTERAL_ERROR);
	public static final S_ArenacoServerStatusNoti NOW_CLOSED	= new S_ArenacoServerStatusNoti(S_ArenacoServerStatusNoti.eStatus.NOW_CLOSED);
			
	public S_ArenacoServerStatusNoti(S_ArenacoServerStatusNoti.eStatus status) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
		writeRaw(0x08);// status
		writeRaw(status.toInt());
		writeH(0x00);
	}
	
	public enum eStatus{
		INTERAL_ERROR(1),
		NOW_CLOSED(2),
		;
		private int value;
		eStatus(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eStatus v){
			return value == v.value;
		}
		public static eStatus fromInt(int i){
			switch(i){
			case 1:
				return INTERAL_ERROR;
			case 2:
				return NOW_CLOSED;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eStatus, %d", i));
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
		return S_ARENACO_SERVER_STATUS_NOTI;
	}
}
