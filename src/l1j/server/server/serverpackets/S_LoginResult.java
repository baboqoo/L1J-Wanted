package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_LoginResult extends ServerBasePacket {
	public static final String S_LOGIN_RESULT = "[S] S_LoginResult";

	public static final int REASON_ACCESS_FAILED			= 0x08;
	public static final int REASON_USER_OR_PASS_WRONG		= 0x08;
	public static final int REASON_WRONG_ACCOUNT			= 0x09;
	public static final int REASON_WRONG_PASSWORD			= 0x0A;
	public static final int REASON_ACCOUNT_IN_USE			= 0x16;
	public static final int REASON_ACCOUNT_ALREADY_EXISTS	= 0x26;
	public static final int REASON_LOGIN_OK					= 0x33;
	public static final int REASON_BUG_WRONG				= 0x39;
	public static final int REASON_BENNED					= 0x3e;
	public static final int BANNED_REASON_COMMERCE			= 0x57;
	public static final int BANNED_REASON_HACK				= 0x5f;
	public static final int REASON_SUCCESS					= 0x68;
	public static final int REASON_MAX_USER					= 0xd9;
	
	public static final S_LoginResult LOGIN_OK				= new S_LoginResult();// 로그인
	public static final S_LoginResult IP_TWO_CHECK			= new S_LoginResult(26);
	public static final S_LoginResult ACCOUNT_NAME_FAIL		= new S_LoginResult(S_LoginResult.REASON_WRONG_ACCOUNT);
	public static final S_LoginResult ACCOUNT_PWD_FAIL		= new S_LoginResult(S_LoginResult.REASON_WRONG_PASSWORD);
	public static final S_LoginResult ACCOUNT_FAIL			= new S_LoginResult(S_LoginResult.REASON_USER_OR_PASS_WRONG);
	
	//  06-이미 존재하거나, 사용할 수 없는 캐릭터명입니다.
	//  07-같은 이름의 계정이 이미 있습니다. 다른 이름을 입력하십시오.
	//	09-이름잘못
	//  10-암호가 잘못되었습니다. 암호에는....
	//  11-전자우편 주소가 잘 못 되었습니다.
	//  12-전자우편 주소가 잘 못 되었습니다.
	//  13-주민등록번호가 잘못되었습니다.
	//  17-나라이름이 잘 못 되었습니다.
	//	24-ip정량제
	//	26-가상 ip복수접속
	//	28-비번변경해라  
	//	29-질문답 
	//  31-접속에 실패했습니다. 3 분후 다시 시도해 보십시오.
	//	32-시간남은게 없다 
	//	34-이캐릭터 사용이 금지
    //	35-게임내비번변경불가 
	//  36-요금문제로정지 
	//  37-도용신고
	//  38-리니지는 가상 IP등을 통하여 복수 접속을 할 경우 접속이 제한될 수 있습니다.
	//  39-이 서버는 접속 할 수 없는 서버이거나, 해당 국가에서는 접속이 제한 되는 서버입니다.
	//  40-현재의 패스워드는 더 이상 사용하실 수 없습니다. 지금 패스워드를 변경해주세요.
	//  43-당신의 계정은 계정도용 신고에 의해 정지 되었습니다.
	//	51-??
	//  52-이 캐릭터는 사용이 금지 되어 있습니다. 해당 서버의 담당 게임마스터에게 이메일로 문의 하시기 바랍니다.
	//  55-고객님의 계정은 계정도용 관련으로 이용이 제한되었습니다.
	//  57-고객님께서는 계정 거래 행위가 확인되어 게임 서비스 이용이 제한 되었습니다.
    //  58-고객님께서는 현금/서버/타게임과의 거래 내용이 확인되어 게임 서비스 이용이 정지 되었습니다.
	//  59-고객님의 계정은 계정도용 허위 신고로 인해 당사의 계정도용 신고 처리 원칙에 의거 이용이 제한 되었습니다.
	//  60-고객님께서는 현금/서버/타게임과의 거래 내용이 확인되어 게임 서비스 이용이 정지 되었습니다.
	//  61-고객님의 계정은 게임내 시스템의 취약점 및 버그 등을 악용하였거나......
	//  62-고객님의 계정은 공공의 안녕, 질서나 미풍양속을 해하는 행위.....
	//  63-고객님의 계정은 공공의 안녕, 질서나 미풍양속을 해하는 행위.....
	//  64-고객님의 계정은 공공의 안녕, 질서나 미풍양속을 해하는 행위.....
	//  65-고객님의 계정은 게임 내 불건전 언어 사용이 확인 되어 ,,,,,,,
	//  66-고객은 회사의 제공하는 서비스를 서비스의 목적 이외의 용도로 다음각호에 해당하는 행위를 해서는 안됩니다.
	//  67-해당 계정은 고객 본인의 요청에 의해 당사의 약관 및 이용이 제한되었습니다.
	//  68-고객님의 계정은 신용카드 도용으로 당사의 서비스 이용약관에 의거 이용이 제한되었습니다.
	//  69-고객님의 계정은 휴대폰 도용으로 당사의 서비스 이용약관에 의거 이용이 제한되었습니다.
	//  71-고객님의 주민등록번호로 등록된 다수의 계정이 이용약관 및 운영정책에 위반된 것이 확인되었기에 일괄적으로 이용이 제한되었습니다.
	//  72-고객님의 계정은 타인의 명의를 무단으로 도용하여 당사의 서비스를 이용약관에 의거 이용이 제한되었습니다.
	//  73-고객의 게정은 1년 동안 한번도 접속 하지 않은 게정으로 고객 약관에 의거 하여 곧 삭제할 예정입니다.
	//  75-지정된 시간 초과로 게임이 종료되었습니다.
	//  79-선택하신 서버는 PC방 전용 서버입니다. 리니지 가맹 PC방에서만 접속하실 수 있습니다.
	//  86-고객님의 게정은 봉인 인증이 필요합니다.
	//  87-고객님께서는 상업적인 목적의 광고 행위 또는 현금/타게임과의 거래 등을 시도 하신 것이 확인되어....
	//  95-고객님의 계정은 불법프로그램 사용으로 인하여 게임 서비스 이용이 제한된 상태입니다.
    //	100-차단된 IP입니다.	
	//	115-고객님의 계정은 NC 코인 관련 버그를 악용한 내역이 확인되어 사용이 제한되었습니다.
	//	117-고객님의 계정은 모든 권한이 제한된 상태입니다.
    //	126-게임 내에서 비정상적인 방법을 이용하여 플레이 한 것으로 추정되어 고객님의 계정이 임시로 제한됐습니다.
	//	127-현재 이 서버에서는 캐릭터를 생성할 수 없습니다.

	// 2B 도용신고

	// public static int REASON_SYSTEM_ERROR = 0x01;

	private byte[] _byte = null;

	public S_LoginResult(int reason) {
		buildPacket(reason);
	}

	private void buildPacket(int reason) {
		writeC(Opcodes.S_LOGIN_CHECK);
		writeC(reason);
		writeD(0x00000000);
		writeD(0x00000000);
		writeD(0x00000000);
	}
	
	static final byte[] LOGIN_BYTES = {
		(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x23, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
		(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0x02, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
		(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x66, (byte)0x00
	};
	
	public S_LoginResult() {
		writeC(Opcodes.S_LOGIN_CHECK);
		writeC(REASON_LOGIN_OK);
		writeByte(LOGIN_BYTES);
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
		return S_LOGIN_RESULT;
	}
}

