package l1j.server.server.serverpackets.action;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Character;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_AttackMissPacket extends ServerBasePacket {
	private static final String S_ATTACK_MISS_PACKET = "[S] S_AttackMissPacket";
	private byte[] _byte = null;

	public S_AttackMissPacket(L1Character attacker, int targetId) {
		writeC(Opcodes.S_ATTACK);
		writeC(0x1);
		writeD(attacker.getId());
		writeD(targetId);
		writeC(0);
		writeC(0);
		writeC(attacker.getMoveState().getHeading());
		writeD(0);
		writeC(0);
	}

	public S_AttackMissPacket(L1Character attacker, int targetId, int actId) {
		writeC(Opcodes.S_ATTACK);
		writeC(actId);
		writeD(attacker.getId());
		writeD(targetId);
		writeC(0);
		writeC(0);
		writeC(attacker.getMoveState().getHeading());
		writeD(0);
		writeC(0);
	}

	public S_AttackMissPacket(int attackId, int targetId) {
		writeC(Opcodes.S_ATTACK);
		writeC(1);
		writeD(attackId);
		writeD(targetId);
		writeC(0);
		writeC(0);
		writeC(0);
		writeD(0);
	}

	public S_AttackMissPacket(int attackId, int targetId, int actId) {
		writeC(Opcodes.S_ATTACK);
		writeC(actId);
		writeD(attackId);
		writeD(targetId);
		writeC(0);
		writeC(0);
		writeC(0);
		writeD(0);
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
		return S_ATTACK_MISS_PACKET;
	}
}

