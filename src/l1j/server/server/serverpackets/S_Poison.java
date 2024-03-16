package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_Poison extends ServerBasePacket {

	/**
	 * 캐릭터의 외관을 독상태에 변경할 때에 송신하는 패킷을 구축한다
	 * 
	 * @param objId
	 *            외관을 바꾸는 캐릭터의 ID
	 * @param type
	 *            외관의 타입 0 = 통상색, 1 = 녹색, 2 = 회색
	 */
	public S_Poison(int objId, int type) {
		writeC(Opcodes.S_POISON);
		writeD(objId);

		if (type == 0) { // 통상
			writeC(0);
			writeC(0);
		} else if (type == 1) { // 녹색
			writeC(1);
			writeC(0);
		} else if (type == 2) { // 회색
			writeC(0);
			writeC(1);
		} else {
			throw new IllegalArgumentException("Invalid argument. type = " + type);
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
		return S_POISON;
	}

	private byte[] _byte = null;
	private static final String S_POISON = "[S] S_Poison";
}

