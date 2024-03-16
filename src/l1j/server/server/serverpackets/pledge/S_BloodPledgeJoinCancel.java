package l1j.server.server.serverpackets.pledge;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_BloodPledgeJoinCancel extends ServerBasePacket {
	private static final String S_BLOODPLEDGE_JOIN_CANCEL = "[S] S_BloodPledgeJoinCancel";
	private byte[] _byte = null;
	public static final int CANCEL	= 0x014B;
	
	public static final S_BloodPledgeJoinCancel OK		= new S_BloodPledgeJoinCancel(ePLEDGE_JOIN_CANCEL_ACK_RESULT.ePLEDGE_JOIN_CANCEL_ACK_RESULT_OK);
	public static final S_BloodPledgeJoinCancel ERROR	= new S_BloodPledgeJoinCancel(ePLEDGE_JOIN_CANCEL_ACK_RESULT.ePLEDGE_JOIN_CANCEL_ACK_RESULT_ERROR);
	public static final S_BloodPledgeJoinCancel NOT		= new S_BloodPledgeJoinCancel(ePLEDGE_JOIN_CANCEL_ACK_RESULT.ePLEDGE_JOIN_CANCEL_ACK_RESULT_NOT_EXIST);
	
	public S_BloodPledgeJoinCancel(ePLEDGE_JOIN_CANCEL_ACK_RESULT result) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(CANCEL);
		writeC(0x08);// result
		writeC(result.value);
		writeH(0x00);
	}
	
	public enum ePLEDGE_JOIN_CANCEL_ACK_RESULT{
		ePLEDGE_JOIN_CANCEL_ACK_RESULT_OK(0),
		ePLEDGE_JOIN_CANCEL_ACK_RESULT_ERROR(1),
		ePLEDGE_JOIN_CANCEL_ACK_RESULT_NOT_EXIST(2),
		;
		private int value;
		ePLEDGE_JOIN_CANCEL_ACK_RESULT(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(ePLEDGE_JOIN_CANCEL_ACK_RESULT v){
			return value == v.value;
		}
		public static ePLEDGE_JOIN_CANCEL_ACK_RESULT fromInt(int i){
			switch(i){
			case 0:
				return ePLEDGE_JOIN_CANCEL_ACK_RESULT_OK;
			case 1:
				return ePLEDGE_JOIN_CANCEL_ACK_RESULT_ERROR;
			case 2:
				return ePLEDGE_JOIN_CANCEL_ACK_RESULT_NOT_EXIST;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments ePLEDGE_JOIN_CANCEL_ACK_RESULT, %d", i));
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
		return S_BLOODPLEDGE_JOIN_CANCEL;
	}
}

