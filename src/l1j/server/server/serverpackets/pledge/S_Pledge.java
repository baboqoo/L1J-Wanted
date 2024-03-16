package l1j.server.server.serverpackets.pledge;

import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_Pledge extends ServerBasePacket {
	private static final String S_PLEDGE = "[S] S_Pledge";
	private byte[] _byte = null;

	public S_Pledge(L1PcInstance pc, int emblemId, eBloodPledgeRankType rank) {
		writeC(Opcodes.S_PLEDGE);
		writeD(pc.getId());
		if (rank != null) {
			writeByte(pc.getClanName().getBytes());
		}
		writeH(0x00);
		writeD(emblemId);
		writeC(rank != null ? rank.toInt() : 11);
		writeH(0x00);
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
		return S_PLEDGE;
	}
}

