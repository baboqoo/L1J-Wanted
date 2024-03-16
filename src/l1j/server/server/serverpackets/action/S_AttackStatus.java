package l1j.server.server.serverpackets.action;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_AttackStatus extends ServerBasePacket {
	private static final String S_ATTACK_STATUS = "[S] S_AttackStatus";
	private byte[] _byte = null;

	public S_AttackStatus(L1PcInstance pc, int objid, int type) {
		buildpacket(pc, objid, type);
	}

	public S_AttackStatus(L1PcInstance pc, int objid, int type, int gfxid) {
		buildpacket(pc, type, gfxid);
	}

	void buildpacket(L1PcInstance pc, int objid, int type) {
		writeC(Opcodes.S_ATTACK);
		writeC(type);
		writeD(pc.getId());
		writeD(objid);
		writeC(0x01);
		writeC(0);
		writeC(pc.getMoveState().getHeading());
		writeD(0x00000000);
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
		return S_ATTACK_STATUS;
	}
}

