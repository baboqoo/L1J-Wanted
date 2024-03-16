package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Character;

public class S_HPMeter extends ServerBasePacket {
	private static final String S_HP_METER = "[S] S_HPMeter";
	private byte[] _byte = null;

	public S_HPMeter(int objId, int hpRatio, int mpRatio) {
		buildPacket(objId, hpRatio, mpRatio);
	}

	public S_HPMeter(L1Character cha) {
		buildPacket(cha.getId(), cha.getCurrentHpPercent(), cha.getCurrentMpPercent());
	}

	private void buildPacket(int objId, int hpRatio, int mpRatio) {
		writeC(Opcodes.S_HIT_RATIO);
		writeD(objId);
		writeC(hpRatio);
		writeC(mpRatio);
		writeH(0);
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
		return S_HP_METER;
	}
}

