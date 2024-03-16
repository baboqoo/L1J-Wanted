package l1j.server.server.serverpackets.message;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.StringUtil;

public class S_MessageYN extends ServerBasePacket {
	private byte[] _byte = null;
	
	public static final S_MessageYN POLY_YN					= new S_MessageYN(180);
	public static final S_MessageYN MARRY_DIVORCE_YN		= new S_MessageYN(653);
	public static final S_MessageYN RESURRECT_YN			= new S_MessageYN(321);
	public static final S_MessageYN RESURRECT_BLESS_YN		= new S_MessageYN(322);
	public static final S_MessageYN CHAR_NAME_CHANGE_YN		= new S_MessageYN(325);
	public static final S_MessageYN HOUSE_NAME_CHANGE_YN	= new S_MessageYN(512);
	public static final S_MessageYN CALL_CLAN_YN			= new S_MessageYN(729, StringUtil.EmptyString);
	public static final S_MessageYN ALLIANCE_CANCEL_YN		= new S_MessageYN(1210);
	public static final S_MessageYN EXP_REFAIR_PAPER_YN		= new S_MessageYN(2551, StringUtil.EmptyString);
	public static final S_MessageYN BOOKMARK_SAVE_YN		= new S_MessageYN(2935);// 기억 저장 구슬에 당신의 기억 장소 목록을 저장하시겠습니까?
	public static final S_MessageYN RAID_ENTER_YN			= new S_MessageYN(3401);// 드래곤 레이드: 드래곤 레어에 진입하시겠습니까?
	public static final S_MessageYN SHELTER_OWNER_CHANGE_YN	= new S_MessageYN(9134);// 아지트 양도를 원하는 캐릭터명을 입력하세요.
	
	public S_MessageYN(int type) {
		buildPacket(type, null, null, null, 1);
	}

	public S_MessageYN(int type, String msg1) {
		buildPacket(type, msg1, null, null, 1);
	}

	public S_MessageYN(int type, String msg1, String msg2) {
		buildPacket(type, msg1, msg2, null, 2);
	}

	public S_MessageYN(int type, String msg1, String msg2, String msg3) {
		buildPacket(type, msg1, msg2, msg3, 3);
	}

	private void buildPacket(int type, String msg1, String msg2, String msg3, int check) {
		writeC(Opcodes.S_ASK);
		writeH(0x0000);
		writeD(0);
		writeH(type);
		switch(check){
		case 1:
			writeS(msg1);
			break;
		case 2:
			writeS(msg1);
			writeS(msg2);
			break;
		case 3:
			writeS(msg1);
			writeS(msg2);
			writeS(msg3);
			break;
		}
	}

	public S_MessageYN(int idx, int type, String msg){
		writeC(Opcodes.S_ASK);
		writeH(0);
		writeD(idx);
		writeH(type);
		writeS(msg);
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
		return "[S] S_MessageYN";
	}
}

