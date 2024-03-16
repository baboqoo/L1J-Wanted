package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_CharPacks extends ServerBasePacket {
	private static final String S_CHAR_PACKS = "[S] S_CharPacks";

	private static final String CHAR_CODE = "3F9A3FB1-A736-436F-862B-8152F489FBCC";
	
	public S_CharPacks(String name, String clanName, int game_class_type, int gender,
			int align, int hp, int mp, int ac, int lv, int str, int dex,
			int con, int wis, int cha, int intel, int birth, int deleteTime) {
		writeC(Opcodes.S_CHARACTER_INFO);
		writeS(name);
		writeS(clanName);
		writeC(game_class_type);
		writeC(gender);
		writeH(align);
		writeH(hp);
		writeH(mp);
		writeC(ac);
		writeC(lv);
		writeC(str);
		writeC(dex);
		writeC(con);
		writeC(wis);
		writeC(cha);
		writeC(intel);
		writeC(0);
		writeD(birth);
		int code = lv ^ str ^ dex ^ con ^ wis ^ cha ^ intel;
		writeC(code & 0xFF);
		writeD(deleteTime);
		writeD(0);// 서버의 모든 케릭터 순번 번호 임의 지정
		writeS(CHAR_CODE);// 케릭터의 고유 코드번호 임의 지정
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_CHAR_PACKS;
	}
}

