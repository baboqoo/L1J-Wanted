package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.IntRange;

public class S_HPUpdate extends ServerBasePacket {
	private static final String S_HP_UPDATE = "[S] S_HPUpdate";
	private byte[] _byte = null;
	
	private static final IntRange hpRange = new IntRange(1, 32767);

	public S_HPUpdate(int currentHp, int maxHp) {
		buildPacket(currentHp, maxHp);
	}

	public S_HPUpdate(L1PcInstance pc) {
		buildPacket(pc.getCurrentHp(), pc.getMaxHp());
	}

	public void buildPacket(int currentHp, int maxHp) {
		writeC(Opcodes.S_HIT_POINT);
		writeH(hpRange.ensure(currentHp));
		writeH(hpRange.ensure(maxHp));
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
		return S_HP_UPDATE;
	}
}

