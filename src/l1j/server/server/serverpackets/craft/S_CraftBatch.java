package l1j.server.server.serverpackets.craft;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_CraftBatch extends ServerBasePacket {
	private static final String S_CRAFT_BATCH			= "[S] S_CraftBatch";
	private byte[] _byte								= null;
	public static final int CRAFT_BATCH					= 0x005d;
	private static long BATCH_TRANSACTION				= 0L;
	
	public S_CraftBatch(S_CraftBatch.eCraftBatchAckResultType eResult) {
		write_init();
		write_eResult(eResult);
		write_batch_transaction_id(++BATCH_TRANSACTION);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(CRAFT_BATCH);
	}
	
	void write_eResult(S_CraftBatch.eCraftBatchAckResultType eResult) {
		writeRaw(0x08);// eResult
		writeRaw(eResult.value);
	}
	
	void write_batch_transaction_id(long batch_transaction_id) {
		writeRaw(0x10);// batch_transaction_id
		writeBit(batch_transaction_id);
	}
	
	public enum eCraftBatchAckResultType{
		RP_SUCCESS(0),
		RP_FAILURE(1),// 연속 제작을 시작할 수 없습니다.
		;
		private int value;
		eCraftBatchAckResultType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eCraftBatchAckResultType v){
			return value == v.value;
		}
		public static eCraftBatchAckResultType fromInt(int i){
			switch(i){
			case 0:
				return RP_SUCCESS;
			case 1:
				return RP_FAILURE;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eCraftBatchAckResultType, %d", i));
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
		return S_CRAFT_BATCH;
	}
}

