package l1j.server.server.serverpackets.pledge;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_BloodPledgeJoinOptionChange extends ServerBasePacket {
	private static final String S_BLOODPLEDGE_JOIN_OPTION_CHANGE = "[S] S_BloodPledgeJoinOptionChange";
	private byte[] _byte = null;
	public static final int JOIN_OPTION_CHANGE	= 0x0147;
	
	public static final S_BloodPledgeJoinOptionChange SUCCESS	= new S_BloodPledgeJoinOptionChange(ePLEDGE_JOIN_OPTION_CHANGE_ACK_RESULT.ePLEDGE_JOIN_OPTION_CHANGE_ACK_RESULT_OK);
	public static final S_BloodPledgeJoinOptionChange FAIL		= new S_BloodPledgeJoinOptionChange(ePLEDGE_JOIN_OPTION_CHANGE_ACK_RESULT.ePLEDGE_JOIN_OPTION_CHANGE_ACK_RESULT_INVALID_JOIN_TYPE);
	
	public S_BloodPledgeJoinOptionChange(ePLEDGE_JOIN_OPTION_CHANGE_ACK_RESULT result) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(JOIN_OPTION_CHANGE);
		writeC(0x08);// result
		writeC(result.value);// 0:세팅 성공, 1:세팅 실패
		writeH(0x00);
	}

	public enum ePLEDGE_JOIN_OPTION_CHANGE_ACK_RESULT{
		ePLEDGE_JOIN_OPTION_CHANGE_ACK_RESULT_OK(0),
		ePLEDGE_JOIN_OPTION_CHANGE_ACK_RESULT_INVALID_JOIN_TYPE(1),
		ePLEDGE_JOIN_OPTION_CHANGE_ACK_RESULT_INVALID_PASSWORD(2),
		ePLEDGE_JOIN_OPTION_CHANGE_ACK_RESULT_PLEDGE_NOT_EXIST(3),
		ePLEDGE_JOIN_OPTION_CHANGE_ACK_RESULT_NOT_ALLOWED(4),
		ePLEDGE_JOIN_OPTION_CHANGE_ACK_RESULT_UNKNOWN_ERROR(9999),
		;
		private int value;
		ePLEDGE_JOIN_OPTION_CHANGE_ACK_RESULT(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(ePLEDGE_JOIN_OPTION_CHANGE_ACK_RESULT v){
			return value == v.value;
		}
		public static ePLEDGE_JOIN_OPTION_CHANGE_ACK_RESULT fromInt(int i){
			switch(i){
			case 0:
				return ePLEDGE_JOIN_OPTION_CHANGE_ACK_RESULT_OK;
			case 1:
				return ePLEDGE_JOIN_OPTION_CHANGE_ACK_RESULT_INVALID_JOIN_TYPE;
			case 2:
				return ePLEDGE_JOIN_OPTION_CHANGE_ACK_RESULT_INVALID_PASSWORD;
			case 3:
				return ePLEDGE_JOIN_OPTION_CHANGE_ACK_RESULT_PLEDGE_NOT_EXIST;
			case 4:
				return ePLEDGE_JOIN_OPTION_CHANGE_ACK_RESULT_NOT_ALLOWED;
			case 9999:
				return ePLEDGE_JOIN_OPTION_CHANGE_ACK_RESULT_UNKNOWN_ERROR;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments ePLEDGE_JOIN_OPTION_CHANGE_ACK_RESULT, %d", i));
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
		return S_BLOODPLEDGE_JOIN_OPTION_CHANGE;
	}
}

