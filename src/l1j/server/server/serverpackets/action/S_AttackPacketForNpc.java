package l1j.server.server.serverpackets.action;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Character;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_AttackPacketForNpc extends ServerBasePacket {
	private static final String S_ATTACK_PACKET_FOR_NPC = "[S] S_AttackPacketForNpc";
	private byte[] _byte = null;

	public S_AttackPacketForNpc(L1Character cha, int npcObjectId, int type) {
		writeC(Opcodes.S_ATTACK);
		writeC(type);
		writeD(npcObjectId);
		writeD(cha.getId());
		writeH(0x01); // damage
		writeC(cha.getMoveState().getHeading());
		writeH(0x0000); // target x
		writeH(0x0000); // target y
		writeC(0x00); // 0x00:none 0x04:Claw 0x08:CounterMirror
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
		return S_ATTACK_PACKET_FOR_NPC;
	}
}

