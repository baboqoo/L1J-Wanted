package l1j.server.server.serverpackets.pledge;

import l1j.server.common.data.ePLEDGE_JOIN_REQ_TYPE;
import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Clan;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.StringUtil;

public class S_BloodPledgeJoinOption extends ServerBasePacket {
	private static final String S_BLOODPLEDGE_JOIN_OPTION = "[S] S_BloodPledgeJoinOption";
	private byte[] _byte = null;
	public static final int JOIN_OPTION = 0x014d;
	
	private static final byte[] EMPTY_PWD_BYTES		= {
		(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
		(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00
	};
	
	public S_BloodPledgeJoinOption(ePLEDGE_JOIN_OPTION_RESULT result, L1Clan clan) {
		write_init();
		write_result(result);
		if (clan != null) {
			write_enable_join(clan.isEnableJoin());
			write_join_type(clan.getJoinType());
			if (clan.getJoinType() == ePLEDGE_JOIN_REQ_TYPE.ePLEDGE_JOIN_REQ_TYPE_PASSWORD && !StringUtil.isNullOrEmpty(clan.getJoinPassword())) {
				String[] array = new String[clan.getJoinPassword().length() >> 1];
				for (int i=0; i<array.length; i++) {
					array[i] = clan.getJoinPassword().substring(i << 1, (i + 1) << 1);
				}
				byte[] pwdBytes = new byte[array.length];
				for (int i=0; i<pwdBytes.length; i++) {
					pwdBytes[i] = (byte)(Integer.parseInt(array[i], 16) & 0xff);
				}
				write_hashed_password(pwdBytes);
				array		= null;
				pwdBytes	= null;
			} else {
				write_hashed_password(EMPTY_PWD_BYTES);
			}
			write_introduction_message(clan.getIntroductionMessage());
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(JOIN_OPTION);
	}
	
	void write_result(ePLEDGE_JOIN_OPTION_RESULT result) {
		writeC(0x08);// result
		writeC(result.value);
	}
	
	void write_enable_join(boolean enable_join) {
		writeC(0x10);// enable_join 혈맹원 모집 체크
		writeB(enable_join);
	}
	
	void write_join_type(ePLEDGE_JOIN_REQ_TYPE join_type) {
		writeC(0x18);// join_type 가입 유형
		writeC(join_type.toInt());
	}
	
	void write_hashed_password(byte[] hashed_password) {
		writeC(0x22);// hashed_password 암호
		writeBytesWithLength(hashed_password);
	}
	
	void write_introduction_message(String introduction_message) {
		writeC(0x2a);// introduction_message 소개글
		writeStringWithLength(introduction_message);
	}
	
	public enum ePLEDGE_JOIN_OPTION_RESULT{
		eRESULT_OK(1),
		eRESULT_ERROR_NOT_LORD(2),
		eRESULT_ERROR_HAVE_NO_PLEDGE(3),
		;
		private int value;
		ePLEDGE_JOIN_OPTION_RESULT(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(ePLEDGE_JOIN_OPTION_RESULT v){
			return value == v.value;
		}
		public static ePLEDGE_JOIN_OPTION_RESULT fromInt(int i){
			switch(i){
			case 1:
				return eRESULT_OK;
			case 2:
				return eRESULT_ERROR_NOT_LORD;
			case 3:
				return eRESULT_ERROR_HAVE_NO_PLEDGE;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eRESULT, %d", i));
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
		return S_BLOODPLEDGE_JOIN_OPTION;
	}
}

