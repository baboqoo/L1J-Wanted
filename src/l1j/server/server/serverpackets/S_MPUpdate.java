package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.utils.IntRange;

public class S_MPUpdate extends ServerBasePacket {
	private static final String S_MP_UPDATE = "[S] S_MPUpdate";
	private byte[] _byte = null;
	
	private static final IntRange mpRange = new IntRange(1, 32767);
	
	public S_MPUpdate(int currentmp, int maxmp) {
		writeC(Opcodes.S_MANA_POINT);
		writeH(mpRange.ensure(currentmp));
		writeH(mpRange.ensure(maxmp));
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
		return S_MP_UPDATE;
	}
}

