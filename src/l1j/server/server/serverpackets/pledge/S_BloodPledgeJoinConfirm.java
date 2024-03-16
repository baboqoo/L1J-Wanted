package l1j.server.server.serverpackets.pledge;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_BloodPledgeJoinConfirm extends ServerBasePacket {
	private static final String S_BLOODPLEDGE_JOIN_CONFIRM = "[S] S_BloodPledgeJoinConfirm";
	private byte[] _byte = null;
	public static final int CONFIRM	= 0x0149;
	
	public static final S_BloodPledgeJoinConfirm OK						= new S_BloodPledgeJoinConfirm(ePLEDGE_JOIN_CONFIRM_ACK_RESULT.ePLEDGE_JOIN_CONFIRM_ACK_RESULT_OK);
	public static final S_BloodPledgeJoinConfirm ERROR					= new S_BloodPledgeJoinConfirm(ePLEDGE_JOIN_CONFIRM_ACK_RESULT.ePLEDGE_JOIN_CONFIRM_ACK_RESULT_ERROR);
	public static final S_BloodPledgeJoinConfirm USER_OFFLINE			= new S_BloodPledgeJoinConfirm(ePLEDGE_JOIN_CONFIRM_ACK_RESULT.ePLEDGE_JOIN_CONFIRM_ACK_RESULT_USER_OFFLINE);
	public static final S_BloodPledgeJoinConfirm NO_PRIVILEGE			= new S_BloodPledgeJoinConfirm(ePLEDGE_JOIN_CONFIRM_ACK_RESULT.ePLEDGE_JOIN_CONFIRM_ACK_RESULT_NO_PRIVILEGE);
	public static final S_BloodPledgeJoinConfirm PLEDGE_NOT_EXIST		= new S_BloodPledgeJoinConfirm(ePLEDGE_JOIN_CONFIRM_ACK_RESULT.ePLEDGE_JOIN_CONFIRM_ACK_RESULT_PLEDGE_NOT_EXIST);
	public static final S_BloodPledgeJoinConfirm PLEDGE_ALREADY_EXIST	= new S_BloodPledgeJoinConfirm(ePLEDGE_JOIN_CONFIRM_ACK_RESULT.ePLEDGE_JOIN_CONFIRM_ACK_RESULT_PLEDGE_ALREADY_EXIST);
	public static final S_BloodPledgeJoinConfirm JOIN_REQ_NOT_EXIST		= new S_BloodPledgeJoinConfirm(ePLEDGE_JOIN_CONFIRM_ACK_RESULT.ePLEDGE_JOIN_CONFIRM_ACK_RESULT_JOIN_REQ_NOT_EXIST);
	public static final S_BloodPledgeJoinConfirm PLEDGE_IS_FULL			= new S_BloodPledgeJoinConfirm(ePLEDGE_JOIN_CONFIRM_ACK_RESULT.ePLEDGE_JOIN_CONFIRM_ACK_RESULT_PLEDGE_IS_FULL);
	
	public S_BloodPledgeJoinConfirm(ePLEDGE_JOIN_CONFIRM_ACK_RESULT result) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(CONFIRM);
		writeC(0x08);// result
		writeC(result.value);
		writeH(0x00);
	}
	
	public enum ePLEDGE_JOIN_CONFIRM_ACK_RESULT{
		ePLEDGE_JOIN_CONFIRM_ACK_RESULT_OK(0),
		ePLEDGE_JOIN_CONFIRM_ACK_RESULT_ERROR(1),
		ePLEDGE_JOIN_CONFIRM_ACK_RESULT_USER_OFFLINE(2),		// 대상이 접속중이지 않음
		ePLEDGE_JOIN_CONFIRM_ACK_RESULT_NO_PRIVILEGE(3),
		ePLEDGE_JOIN_CONFIRM_ACK_RESULT_PLEDGE_NOT_EXIST(4),
		ePLEDGE_JOIN_CONFIRM_ACK_RESULT_PLEDGE_ALREADY_EXIST(5),// 이미 가입한 상태
		ePLEDGE_JOIN_CONFIRM_ACK_RESULT_JOIN_REQ_NOT_EXIST(6),
		ePLEDGE_JOIN_CONFIRM_ACK_RESULT_PLEDGE_IS_FULL(7),		// 인원 포화
		;
		private int value;
		ePLEDGE_JOIN_CONFIRM_ACK_RESULT(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(ePLEDGE_JOIN_CONFIRM_ACK_RESULT v){
			return value == v.value;
		}
		public static ePLEDGE_JOIN_CONFIRM_ACK_RESULT fromInt(int i){
			switch(i){
			case 0:
				return ePLEDGE_JOIN_CONFIRM_ACK_RESULT_OK;
			case 1:
				return ePLEDGE_JOIN_CONFIRM_ACK_RESULT_ERROR;
			case 2:
				return ePLEDGE_JOIN_CONFIRM_ACK_RESULT_USER_OFFLINE;
			case 3:
				return ePLEDGE_JOIN_CONFIRM_ACK_RESULT_NO_PRIVILEGE;
			case 4:
				return ePLEDGE_JOIN_CONFIRM_ACK_RESULT_PLEDGE_NOT_EXIST;
			case 5:
				return ePLEDGE_JOIN_CONFIRM_ACK_RESULT_PLEDGE_ALREADY_EXIST;
			case 6:
				return ePLEDGE_JOIN_CONFIRM_ACK_RESULT_JOIN_REQ_NOT_EXIST;
			case 7:
				return ePLEDGE_JOIN_CONFIRM_ACK_RESULT_PLEDGE_IS_FULL;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments ePLEDGE_JOIN_CONFIRM_ACK_RESULT, %d", i));
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
		return S_BLOODPLEDGE_JOIN_CONFIRM;
	}
}

