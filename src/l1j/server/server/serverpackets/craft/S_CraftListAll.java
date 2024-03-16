package l1j.server.server.serverpackets.craft;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_CraftListAll extends ServerBasePacket {
	private static final String S_CRAFT_LIST_ALL	= "[S] S_CraftListAll";
	private byte[] _byte							= null;
	public static final int LIST_ALL				= 0x0037;
	public static final S_CraftListAll LOAD_FINISH	= new S_CraftListAll(CraftListAllReqResultType.CRAFT_LOAD_FINISH);
	
	public S_CraftListAll(CraftListAllReqResultType result) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(LIST_ALL);
		writeRaw(0x08);// result
		writeRaw(result.value);
		writeH(0x00);
	}
	
	public enum CraftListAllReqResultType{
		CRAFT_LOAD_START(0),
		CRAFT_LOADING(1),
		CRAFT_LOAD_FINISH(2),
		ERROR_SAME_HASH_VAL(3),
		ERROR_INVALID_HASH_VAL(4),
		ERROR_UNKNOWN(9999),
		;
		private int value;
		CraftListAllReqResultType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(CraftListAllReqResultType v){
			return value == v.value;
		}
		public static CraftListAllReqResultType fromInt(int i){
			switch(i){
			case 0:
				return CRAFT_LOAD_START;
			case 1:
				return CRAFT_LOADING;
			case 2:
				return CRAFT_LOAD_FINISH;
			case 3:
				return ERROR_SAME_HASH_VAL;
			case 4:
				return ERROR_INVALID_HASH_VAL;
			case 9999:
				return ERROR_UNKNOWN;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eCraftListAllReqResultType, %d", i));
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
		return S_CRAFT_LIST_ALL;
	}
}

