package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_War extends ServerBasePacket {
	private static final String S_WAR = "[S] S_War";
	private byte[] _byte = null;

	public S_War(int type, String clan_name1, String clan_name2) {
		buildPacket(type, clan_name1, clan_name2);
	}

	private void buildPacket(int type, String clan_name1, String clan_name2) {
		// 1 : %s혈맹이 %s혈맹에 선전포고했습니다.
		// 2 : %s혈맹이 %s혈맹에 항복했습니다.
		// 3 : %s혈맹과 %s혈맹과의 전쟁이 종결했습니다.
		// 4 : %s혈맹이 %s혈맹과의 전쟁으로 승리했습니다.
		// 6 : %s혈맹과 %s혈맹이 동맹을 맺었습니다.
		// 7 : %s혈맹과 %s혈맹과의 동맹 관계가 해제되었습니다.
		// 8 : 당신의 혈맹이 현재 %s혈맹과 교전중입니다.

		writeC(Opcodes.S_WAR);
		writeC(type);
		writeS(clan_name1);
		writeS(clan_name2);
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
		return S_WAR;
	}
}

