package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_CharCreateStatus extends ServerBasePacket {
	private static final String S_CHAR_CREATE_STATUS = "[S] S_CharCreateStatus";

	public static final int REASON_OK				= 0x02;
	public static final int REASON_ALREADY_EXSISTS	= 0x06;
	public static final int REASON_INVALID_NAME		= 0x09;
	public static final int REASON_WRONG_AMOUNT		= 0x15;
	public static final int REASON_FAIL				= 0x7F;
	private byte[] _byte;
	
	public static final S_CharCreateStatus NAME_FAIL		= new S_CharCreateStatus(S_CharCreateStatus.REASON_INVALID_NAME);
	public static final S_CharCreateStatus ALREADY_EXSISTS	= new S_CharCreateStatus(S_CharCreateStatus.REASON_ALREADY_EXSISTS);
	public static final S_CharCreateStatus AMOUNT_MAX		= new S_CharCreateStatus(S_CharCreateStatus.REASON_WRONG_AMOUNT);
	public static final S_CharCreateStatus OK				= new S_CharCreateStatus(S_CharCreateStatus.REASON_OK);
	public static final S_CharCreateStatus FAIL				= new S_CharCreateStatus(S_CharCreateStatus.REASON_FAIL);

	public S_CharCreateStatus(int reason) {
		writeC(Opcodes.S_CREATE_CHARACTER_CHECK);
		writeC(reason);
		writeD(0x00000000);
		writeD(0x0000);
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
		return S_CHAR_CREATE_STATUS;
	}
}

