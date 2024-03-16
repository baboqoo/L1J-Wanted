package l1j.server.server.serverpackets.playsupport;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_StartPlaySupport extends ServerBasePacket {
	private static final String S_START_PLAY_SUPPORT = "[S] S_StartPlaySupport";
	private byte[] _byte = null;
	public static final int START 							= 0x0836;
	public static final S_StartPlaySupport VALID			= new S_StartPlaySupport(S_StartPlaySupport.eResult.VALID);// 플레이서포트시작
	public static final S_StartPlaySupport INVALID_MAP		= new S_StartPlaySupport(S_StartPlaySupport.eResult.INVALID_MAP);// 사용불가 장소
	public static final S_StartPlaySupport INVALID_LEVEL	= new S_StartPlaySupport(S_StartPlaySupport.eResult.INVALID_LEVEL);// 레벨불가
	public static final S_StartPlaySupport TIME_EXPIRE		= new S_StartPlaySupport(S_StartPlaySupport.eResult.TIME_EXPIRE);// 사용 시간이 다 되었습니다.
	
	public S_StartPlaySupport(S_StartPlaySupport.eResult result){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(START);
		writeRaw(0x08);// result
		writeRaw(result.value);
		writeH(0x00);
	}
	
	public enum eResult{
		VALID(0),
		INVALID_MAP(1),
		INVALID_LEVEL(2),
		TIME_EXPIRE(3),
		UNKNOWN_ERROR(99),
		;
		private int value;
		eResult(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eResult v){
			return value == v.value;
		}
		public static eResult fromInt(int i){
			switch(i){
			case 0:
				return VALID;
			case 1:
				return INVALID_MAP;
			case 2:
				return INVALID_LEVEL;
			case 3:
				return TIME_EXPIRE;
			case 99:
				return UNKNOWN_ERROR;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eResult, %d", i));
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
		return S_START_PLAY_SUPPORT;
	}
}

