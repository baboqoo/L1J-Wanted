package l1j.server.server.serverpackets.inventory;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_TimeCollectionAdenaRefill extends ServerBasePacket {
	private static final String S_TIME_COLLECTION_ADENA_REFILL = "[S] S_TimeCollectionAdenaRefill";
	private byte[] _byte = null;
	public static final int REFILL = 0x0a66;
	
	public static final S_TimeCollectionAdenaRefill FAIL_RECYCLE_COUNT_CHECK		= new S_TimeCollectionAdenaRefill(S_TimeCollectionAdenaRefill.ADENA_REFILL_ACK_RESULT.TCAR_FAIL_RECYCLE_COUNT_CHECK, 0);
	public static final S_TimeCollectionAdenaRefill FAIL_CAN_NOT_REFILL_SET			= new S_TimeCollectionAdenaRefill(S_TimeCollectionAdenaRefill.ADENA_REFILL_ACK_RESULT.TCAR_FAIL_CAN_NOT_REFILL_SET, 0);
	public static final S_TimeCollectionAdenaRefill FAIL_NOT_BUFF_STATE				= new S_TimeCollectionAdenaRefill(S_TimeCollectionAdenaRefill.ADENA_REFILL_ACK_RESULT.TCAR_FAIL_NOT_BUFF_STATE, 0);
	public static final S_TimeCollectionAdenaRefill FAIL_NOT_ENOUGH_ADENA			= new S_TimeCollectionAdenaRefill(S_TimeCollectionAdenaRefill.ADENA_REFILL_ACK_RESULT.TCAR_FAIL_NOT_ENOUGH_ADENA, 0);
	public static final S_TimeCollectionAdenaRefill FAIL_DECREASE_ADENA_ERROR		= new S_TimeCollectionAdenaRefill(S_TimeCollectionAdenaRefill.ADENA_REFILL_ACK_RESULT.TCAR_FAIL_DECREASE_ADENA_ERROR, 0);
	public static final S_TimeCollectionAdenaRefill FAIL_NOT_ENOUGH_REFILL_COUNT	= new S_TimeCollectionAdenaRefill(S_TimeCollectionAdenaRefill.ADENA_REFILL_ACK_RESULT.TCAR_FAIL_NOT_ENOUGH_REFILL_COUNT, 0);
	public static final S_TimeCollectionAdenaRefill DATA_ERROR						= new S_TimeCollectionAdenaRefill(S_TimeCollectionAdenaRefill.ADENA_REFILL_ACK_RESULT.TCAR_DATA_ERROR, 0);
	
	public S_TimeCollectionAdenaRefill(S_TimeCollectionAdenaRefill.ADENA_REFILL_ACK_RESULT result, int refllCount) {
		write_init();
		write_result(result);
		write_refllCount(refllCount);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(REFILL);
	}
	
	void write_result(S_TimeCollectionAdenaRefill.ADENA_REFILL_ACK_RESULT result) {
		writeRaw(0x08);
		writeRaw(result.value);
	}
	
	void write_refllCount(int refllCount) {
		writeRaw(0x10);
		writeRaw(refllCount);
	}
	
	public enum ADENA_REFILL_ACK_RESULT{
		TCAR_SUCCESS(1),
		TCAR_FAIL_RECYCLE_COUNT_CHECK(2),
		TCAR_FAIL_CAN_NOT_REFILL_SET(3),
		TCAR_FAIL_NOT_BUFF_STATE(4),
		TCAR_FAIL_NOT_ENOUGH_ADENA(5),
		TCAR_FAIL_DECREASE_ADENA_ERROR(6),
		TCAR_FAIL_NOT_ENOUGH_REFILL_COUNT(7),
		TCAR_DATA_ERROR(100),
		;
		private int value;
		ADENA_REFILL_ACK_RESULT(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(ADENA_REFILL_ACK_RESULT v){
			return value == v.value;
		}
		public static ADENA_REFILL_ACK_RESULT fromInt(int i){
			switch(i){
			case 1:
				return TCAR_SUCCESS;
			case 2:
				return TCAR_FAIL_RECYCLE_COUNT_CHECK;
			case 3:
				return TCAR_FAIL_CAN_NOT_REFILL_SET;
			case 4:
				return TCAR_FAIL_NOT_BUFF_STATE;
			case 5:
				return TCAR_FAIL_NOT_ENOUGH_ADENA;
			case 6:
				return TCAR_FAIL_DECREASE_ADENA_ERROR;
			case 7:
				return TCAR_FAIL_NOT_ENOUGH_REFILL_COUNT;
			case 100:
				return TCAR_DATA_ERROR;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments ADENA_REFILL_ACK_RESULT, %d", i));
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
		return S_TIME_COLLECTION_ADENA_REFILL;
	}
}

