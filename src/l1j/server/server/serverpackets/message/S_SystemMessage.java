package l1j.server.server.serverpackets.message;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.Config;

public class S_SystemMessage extends ServerBasePacket {
	private static final String S_SYSTEM_MESSAGE = "[S] S_SystemMessage";
	private byte[] _byte = null;

	/**
	 * 클라이언트에 데이터의 존재하지 않는 오리지날의 메세지를 표시한다.
	 * 메세지에 nameid($xxx)가 포함되어 있는 경우는 overload 된 이제(벌써) 한편을 사용한다.
	 * 
	 * @param msg - 표시하는 캐릭터 라인
	 */
	public S_SystemMessage(String msg) {
		writeC(Opcodes.S_MESSAGE);
		writeC(0x09);
		writeS(msg);
	}
	/**
	 * 클라이언트에 데이터의 존재하지 않는 오리지날의 메세지를 표시한다.
	 * 
	 * @param msg - 표시하는 캐릭터 라인
	 * @param nameid - 캐릭터 라인에 nameid($xxx)가 포함되어 있는 경우 true로 한다.
	 */
	public S_SystemMessage(String msg, boolean nameid) {
		writeC(Opcodes.S_SAY_CODE);
		writeC(2);
		writeD(0);
		writeS(msg);
		// NPC 채팅 패킷이면 nameid가 해석되기 (위해)때문에 이것을 이용한다
	}

	public static String getRefText(Integer index) {
		//return "$" + (Config.ALT.DESC_ORIGINAL_LINES + Config.ALT.ANNOUNCE_CYCLE_COUNT + Config.ALT.FIXED_SYSTEM_MESSAGES + Config.ALT.DESC_DB_LINES + Config.ALT.DESC_SEPARATORS + index - 1);
		return "$" + (Config.ALT.SERVER_MESSAGES_START_LINE + index - 3) + " ";
	}

	public static String getRefTextNS(Integer index) {
		//return "$" + (Config.ALT.DESC_ORIGINAL_LINES + Config.ALT.ANNOUNCE_CYCLE_COUNT + Config.ALT.FIXED_SYSTEM_MESSAGES + Config.ALT.DESC_DB_LINES + Config.ALT.DESC_SEPARATORS + index - 1);
		return "$" + (Config.ALT.SERVER_MESSAGES_START_LINE + index - 3);
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
		return S_SYSTEM_MESSAGE;
	}
}

