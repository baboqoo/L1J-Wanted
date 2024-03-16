package l1j.server.server.serverpackets.inventory;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_BMTypeDelCheck extends ServerBasePacket {
	private static final String S_BM_TYPE_DEL_CHECK = "[S] S_BMTypeDelCheck";
	private byte[] _byte = null;
	public static final int CHECK = 0x025d;
	
	public static final S_BMTypeDelCheck SUCCESS	= new S_BMTypeDelCheck(S_BMTypeDelCheck.eResult.Success);
	public static final S_BMTypeDelCheck INVALID	= new S_BMTypeDelCheck(S_BMTypeDelCheck.eResult.InvalidPacket);
	public static final S_BMTypeDelCheck FAILURE	= new S_BMTypeDelCheck(S_BMTypeDelCheck.eResult.BMTypeFail);

	public S_BMTypeDelCheck(S_BMTypeDelCheck.eResult result) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(CHECK);
		writeRaw(0x08);// result
		writeRaw(result.value);
		writeH(0x00);
	}
	
	public enum eResult{
		Success(0),
		InvalidPacket(1),
		BMTypeFail(2),
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
				return Success;
			case 1:
				return InvalidPacket;
			case 2:
				return BMTypeFail;
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
		return S_BM_TYPE_DEL_CHECK;
	}
}

