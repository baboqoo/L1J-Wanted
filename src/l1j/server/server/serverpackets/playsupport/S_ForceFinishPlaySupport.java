package l1j.server.server.serverpackets.playsupport;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_ForceFinishPlaySupport extends ServerBasePacket {
	private static final String S_FORCE_FINISH_PLAY_SUPPORT = "[S] S_ForceFinishPlaySupport";
	private byte[] _byte = null;
	public static final int FORCE_FINISH 	= 0x0839;
	
	public S_ForceFinishPlaySupport(S_ForceFinishPlaySupport.eReason reason, int remain_time){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(FORCE_FINISH);
		writeC(0x08);// reason
		writeC(reason.value);
		writeC(0x10);// remain_time
		writeBit(remain_time);
		writeH(0x00);
	}
	
	public enum eReason{
		INVALID_MAP(1),				// 이 곳에서는 사용할 수 없습니다.
		INVALID_LEVEL(2),			// 레벨 불가
		TIME_EXPIRE(3),				// 사용 시간이 다 되었습니다
		USER_DEAD(4),
		MAP_TIME_EXPIRED(5),
		NOT_ENOUGH_REST_GAUGE(6),
		AUTO_OUT_FINISH(7),
		;
		private int value;
		eReason(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eReason v){
			return value == v.value;
		}
		public static eReason fromInt(int i){
			switch(i){
			case 1:
				return INVALID_MAP;
			case 2:
				return INVALID_LEVEL;
			case 3:
				return TIME_EXPIRE;
			case 4:
				return USER_DEAD;
			case 5:
				return MAP_TIME_EXPIRED;
			case 6:
				return NOT_ENOUGH_REST_GAUGE;
			case 7:
				return AUTO_OUT_FINISH;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eReason, %d", i));
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
		return S_FORCE_FINISH_PLAY_SUPPORT;
	}
}

