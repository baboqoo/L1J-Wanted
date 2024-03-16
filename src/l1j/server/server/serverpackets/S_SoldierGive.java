package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_SoldierGive extends ServerBasePacket {

	private static final String S_SOLDIER_GIVE = "[S] S_SoldierGive";

	private byte[] _byte = null;

	public S_SoldierGive(L1PcInstance pc, int objid, int type, int count, int iscount) {

		writeC(Opcodes.S_MERCENARYARRANGE);
		writeD(objid);// objid
		writeH(9);// ????(아무숫자나넣음)
		writeH(count);// 해당용병 고용된 수
		writeH(type);// 순번
		writeS(pc.getName());
		writeD(pc.getId());// pc.getId();
		writeH(iscount);// 배치가능 용병수
		writeC(0);
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
		return S_SOLDIER_GIVE;
	}
}
