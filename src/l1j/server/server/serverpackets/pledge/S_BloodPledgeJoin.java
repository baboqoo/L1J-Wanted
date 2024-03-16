package l1j.server.server.serverpackets.pledge;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_BloodPledgeJoin extends ServerBasePacket {
	private static final String S_BLOODPLEDGE_JOIN = "[S] S_BloodPledgeJoin";
	private byte[] _byte = null;
	public static final int JOIN = 0x0143;
	
	public static final S_BloodPledgeJoin JOIN_OK					= new S_BloodPledgeJoin(ePLEDGE_JOIN_ACK_RESULT.ePLEDGE_JOIN_ACK_RESULT_JOIN_OK);	// 가입완료
	public static final S_BloodPledgeJoin CONFIRMING				= new S_BloodPledgeJoin(ePLEDGE_JOIN_ACK_RESULT.ePLEDGE_JOIN_ACK_RESULT_CONFIRMING);	// 가입요청
	public static final S_BloodPledgeJoin INVALID_PASSWORD			= new S_BloodPledgeJoin(ePLEDGE_JOIN_ACK_RESULT.ePLEDGE_JOIN_ACK_RESULT_INVALID_PASSWORD);	// 혈맹 비번 틀림
	public static final S_BloodPledgeJoin PLEDGE_NOT_EXIST			= new S_BloodPledgeJoin(ePLEDGE_JOIN_ACK_RESULT.ePLEDGE_JOIN_ACK_RESULT_PLEDGE_NOT_EXIST);	// 존재하지 않는 혈맹입니다.
	public static final S_BloodPledgeJoin PLEDGE_IS_FULL			= new S_BloodPledgeJoin(ePLEDGE_JOIN_ACK_RESULT.ePLEDGE_JOIN_ACK_RESULT_PLEDGE_IS_FULL);	// 가입인원 초과
	public static final S_BloodPledgeJoin ALREADY_JOINED			= new S_BloodPledgeJoin(ePLEDGE_JOIN_ACK_RESULT.ePLEDGE_JOIN_ACK_RESULT_ALREADY_JOINED);	// 이미 혈맹에 가입한 상태 입니다.
	public static final S_BloodPledgeJoin PLEDGE_NOT_OPENED			= new S_BloodPledgeJoin(ePLEDGE_JOIN_ACK_RESULT.ePLEDGE_JOIN_ACK_RESULT_PLEDGE_NOT_OPENED);// 가입불가
	public static final S_BloodPledgeJoin NO_PLEDGE_MEMBER_IN_WORLD	= new S_BloodPledgeJoin(ePLEDGE_JOIN_ACK_RESULT.ePLEDGE_JOIN_ACK_RESULT_NO_PLEDGE_MEMBER_IN_WORLD);// 군주 없음
	public static final S_BloodPledgeJoin PRINCE_NEED_OTHER_METHOD	= new S_BloodPledgeJoin(ePLEDGE_JOIN_ACK_RESULT.ePLEDGE_JOIN_ACK_RESULT_PRINCE_NEED_OTHER_METHOD);// 대상 혈맹 없음
	public static final S_BloodPledgeJoin JOIN_LIMIT_LEVEL			= new S_BloodPledgeJoin(ePLEDGE_JOIN_ACK_RESULT.ePLEDGE_JOIN_ACK_RESULT_JOIN_LIMIT_LEVEL);// 레벨 제한
	public static final S_BloodPledgeJoin JOIN_LIMIT_USER			= new S_BloodPledgeJoin(ePLEDGE_JOIN_ACK_RESULT.ePLEDGE_JOIN_ACK_RESULT_JOIN_LIMIT_USER);// 차단 목록
	
	public S_BloodPledgeJoin(ePLEDGE_JOIN_ACK_RESULT subtype) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(JOIN);
		
		writeC(0x08);// result
		writeC(subtype.value);
		
		// 0x12 pledge_name
		// 0x18 need_join_type
		// 0x22 msg
		
		writeH(0x00);
	}
	
	public enum ePLEDGE_JOIN_ACK_RESULT{
		ePLEDGE_JOIN_ACK_RESULT_JOIN_OK(0),						// 가입이 완료 되었습니다.
		ePLEDGE_JOIN_ACK_RESULT_CONFIRMING(1),					// [혈맹이름]에 가입 신청 하였습니다.
		ePLEDGE_JOIN_ACK_RESULT_INVALID_JOIN_TYPE(2),			// 신청 메시지를 남기면 가입 신청이 완료 됩니다.
		ePLEDGE_JOIN_ACK_RESULT_INVALID_PASSWORD(3),			// 입력한 암호가 정확하지 않습니다.
		ePLEDGE_JOIN_ACK_RESULT_PLEDGE_NOT_EXIST(4),			// 현재 혈맹원이 접속해 있지 않거나 없는 혈맹으로 가입이 불가 합니다.
		ePLEDGE_JOIN_ACK_RESULT_USER_JOINING_COUNT_IS_OVER(5),	// 가입신청은 최대 3개의 혈맹에 할 수 있습니다.
		ePLEDGE_JOIN_ACK_RESULT_PLEDGE_JOINING_COUNT_IS_OVER(6),// [혈맹이름]혈맹은 현재 가입 할 수 없습니다.
		ePLEDGE_JOIN_ACK_RESULT_PLEDGE_IS_FULL(7),				// [혈맹이름]혈맹은 현재 가입 할 수 없습니다.
		ePLEDGE_JOIN_ACK_RESULT_NEED_LEVEL(8),					// [혈맹이름]혈맹은 현재 가입 할 수 없습니다.
		ePLEDGE_JOIN_ACK_RESULT_ALREADY_JOINED(9),				// 이미 혈맹에 가입한 상태 입니다.
		ePLEDGE_JOIN_ACK_RESULT_PLEDGE_NOT_OPENED(10),			// [혈맹이름]혈맹은 현재 가입 할 수 없습니다.
		ePLEDGE_JOIN_ACK_RESULT_NO_PLEDGE_MEMBER_IN_WORLD(11),	// 현재 혈맹원이 접속해 있지 않거나 없는 혈맹으로 가입이 불가 합니다.
		ePLEDGE_JOIN_ACK_RESULT_DIED(12),						// [혈맹이름]혈맹은 현재 가입 할 수 없습니다.
		ePLEDGE_JOIN_ACK_RESULT_PRINCE_NEED_OTHER_METHOD(13),	// [혈맹이름]군주를 만나 가입해주세요.
		ePLEDGE_JOIN_ACK_RESULT_JOIN_MSG_NOT_EXIST(14),			// 내용 없음.
		ePLEDGE_JOIN_ACK_RESULT_IDIP_BANNED(20),
		ePLEDGE_JOIN_ACK_RESULT_JOIN_LIMIT_LEVEL(21),
		ePLEDGE_JOIN_ACK_RESULT_JOIN_LIMIT_USER(22),
		ePLEDGE_JOIN_ACK_RESULT_UNKNOWN_ERROR(9999),
		;
		private int value;
		ePLEDGE_JOIN_ACK_RESULT(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(ePLEDGE_JOIN_ACK_RESULT v){
			return value == v.value;
		}
		public static ePLEDGE_JOIN_ACK_RESULT fromInt(int i){
			switch(i){
			case 0:
				return ePLEDGE_JOIN_ACK_RESULT_JOIN_OK;
			case 1:
				return ePLEDGE_JOIN_ACK_RESULT_CONFIRMING;
			case 2:
				return ePLEDGE_JOIN_ACK_RESULT_INVALID_JOIN_TYPE;
			case 3:
				return ePLEDGE_JOIN_ACK_RESULT_INVALID_PASSWORD;
			case 4:
				return ePLEDGE_JOIN_ACK_RESULT_PLEDGE_NOT_EXIST;
			case 5:
				return ePLEDGE_JOIN_ACK_RESULT_USER_JOINING_COUNT_IS_OVER;
			case 6:
				return ePLEDGE_JOIN_ACK_RESULT_PLEDGE_JOINING_COUNT_IS_OVER;
			case 7:
				return ePLEDGE_JOIN_ACK_RESULT_PLEDGE_IS_FULL;
			case 8:
				return ePLEDGE_JOIN_ACK_RESULT_NEED_LEVEL;
			case 9:
				return ePLEDGE_JOIN_ACK_RESULT_ALREADY_JOINED;
			case 10:
				return ePLEDGE_JOIN_ACK_RESULT_PLEDGE_NOT_OPENED;
			case 11:
				return ePLEDGE_JOIN_ACK_RESULT_NO_PLEDGE_MEMBER_IN_WORLD;
			case 12:
				return ePLEDGE_JOIN_ACK_RESULT_DIED;
			case 13:
				return ePLEDGE_JOIN_ACK_RESULT_PRINCE_NEED_OTHER_METHOD;
			case 14:
				return ePLEDGE_JOIN_ACK_RESULT_JOIN_MSG_NOT_EXIST;
			case 9999:
				return ePLEDGE_JOIN_ACK_RESULT_UNKNOWN_ERROR;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments ePLEDGE_JOIN_ACK_RESULT, %d", i));
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
		return S_BLOODPLEDGE_JOIN;
	}
}

