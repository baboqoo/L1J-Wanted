package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_CharPass extends ServerBasePacket {
	private static final String S_CHAR_PASS			= "[S] S_CharPass";
	/*
	public static final int _캐릭선택창진입				= 0x40;
	public static final int _캐릭선택창진입2			= 0x0a;
	public static final int _OTP입력창생성				= 0x0d;
	public static final int _캐릭선택창진입3			= 0x16;
	public static final int _비번생성창					= 0x17;
	public static final int _비번생성완료창				= 0x11;
	public static final int _비번입력창					= 0x14;
	public static final int _비번입력비번틀림			= 0x15;
	public static final int _비번변경답변				= 0x13;
	public static final int _비번인증완료				= 0x3f;
	*/
	public static final int _CharacterSelectionWindowEntry		= 0x40;
	public static final int _CharacterSelectionWindowEntry2		= 0x0a;
	public static final int _OTPEntryWindowCreation				= 0x0d;
	public static final int _CharacterSelectionWindowEntry3		= 0x16;
	public static final int _PasswordCreationWindow				= 0x17;
	public static final int _PasswordCreationCompletionWindow	= 0x11;
	public static final int _PasswordEntryWindow				= 0x14;
	public static final int _PasswordEntryIncorrect				= 0x15;
	public static final int _PasswordChangeResponse				= 0x13;
	public static final int _PasswordAuthenticationComplete		= 0x3f;
	
	
	public static final S_CharPass CHAR_LOGIN_NOTICE		= new S_CharPass();
	public static final S_CharPass CHAR_SELECT_READY		= new S_CharPass(S_CharPass._CharacterSelectionWindowEntry2);
	public static final S_CharPass CHAR_SELECT_START		= new S_CharPass(S_CharPass._CharacterSelectionWindowEntry);
	public static final S_CharPass CHAR_PWD_UI_CREATE		= new S_CharPass(S_CharPass._PasswordCreationWindow);
	public static final S_CharPass CHAR_PWD_UI_READY		= new S_CharPass(S_CharPass._PasswordEntryWindow);
	public static final S_CharPass CHAR_PWD_READY			= new S_CharPass(S_CharPass._CharacterSelectionWindowEntry3, true);
	public static final S_CharPass CHAR_PWD_EMPTY			= new S_CharPass(S_CharPass._CharacterSelectionWindowEntry3, false);

	public S_CharPass() {
		writeC(Opcodes.S_NOTICE);
		writeC(0xb5);
		writeC(0x01);
	}

	public S_CharPass(int val) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(val);
		switch (val) {
		case _PasswordEntryIncorrect:
			writeD(0xa5);
			writeH(0x01);
			writeH(0x05);
			writeD(0x00);
			break;
		case _PasswordCreationCompletionWindow:
			writeD(0x00);
			break;
		case _CharacterSelectionWindowEntry2:
			writeD(0x02);
			break;
		case _CharacterSelectionWindowEntry3:
			writeD(170);
			writeD(0x00);
			writeD(0x00);
			writeH(0x00);
			writeC(0x01);
			writeC(0x00);
			break;
		default:break;
		}
	}

	public S_CharPass(int val, boolean ck) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(val);
		switch (val) {
		case _PasswordChangeResponse:// fe 13 00 00 00 00 00 00 05 00 00 00 00 00 ..............
			if (ck) {
				writeD(0);
				writeH(0);
				writeH(0x05);
				writeD(0);
			} else {
				writeD(0xa5);
				writeH(0x01);
				writeH(0x05);
				writeD(0);
			}
			break;
		case _CharacterSelectionWindowEntry3:
			if (ck) {
				writeD(0x00);
				writeH(0x00);
				writeD(0x05);
				writeD(0x00);
				writeH(0x10);
			} else {
				writeD(0xaa);
				writeD(0x00);
				writeD(0x00);
				writeH(0x00);
				writeH(0x01);
			}
			break;
		default:break;
		}
	}
	
	public static void init(){}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return S_CHAR_PASS;
	}
}

