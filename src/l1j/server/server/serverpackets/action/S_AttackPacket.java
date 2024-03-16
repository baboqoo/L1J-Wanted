package l1j.server.server.serverpackets.action;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Character;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_AttackPacket extends ServerBasePacket {
	private static final String S_ATTACK_PACKET = "[S] S_AttackPacket";
	private byte[] _byte = null;

	public S_AttackPacket(L1Character cha, int objid, int type) {
		buildpacket(cha, objid, type);
	}
	
	public S_AttackPacket(L1Character cha, int objid, int type, int attacktype) {
		buildpacket(cha, objid, type, attacktype);
	}

	public S_AttackPacket(L1Character cha, int objid, int type, int attacktype, int gfx) {
		buildpacket(cha, objid, type, attacktype, gfx);
	}
	
	void buildpacket(L1Character cha, int objid, int type) {
		writeC(Opcodes.S_ATTACK);
		writeC(type);
		writeD(objid);
		writeD(cha.getId());
		writeC(0x4E);
		writeC(0);
		writeC(cha.getMoveState().getHeading());
		writeD(0x00000000);
		writeC(0x00);
		writeH(0);
		writeD(0);
	}
	
	void buildpacket(L1Character cha, int objid, int type, int attacktype) {
		writeC(Opcodes.S_ATTACK);
		writeC(type);
		writeD(cha.getId());
		writeD(objid);
		writeH(0x02); // damage
		writeC(cha.getMoveState().getHeading());
		writeH(0x0000); // target x
		writeH(0x0000); // target y
		writeC(attacktype); // 0:none 2:크로우 4:이도류 0x08:CounterMirror
		writeH(0);
		writeD(0);
	}

	void buildpacket(L1Character cha, int objid, int type, int attacktype, int gfx) {
		writeC(Opcodes.S_ATTACK);
		writeC(type);
		writeD(cha.getId());
		writeD(objid);
		writeC(0x01); // damage
		writeC(0x00);
		writeC(cha.getMoveState().getHeading());
		writeH(0x0000); // target x
		writeH(0x0000); // target y
		writeC(attacktype); // 0:none 2:크로우 4:이도류 0x08:CounterMirror
		writeH(gfx);
		writeH(0x00);
		writeH(0x00);
	}
	
	public S_AttackPacket(L1Character cha, int objid, int type, boolean j) {
		writeC(Opcodes.S_ATTACK);
		writeC(type);
		writeD(cha.getId());
		writeD(objid);
		writeC(0x01); // damage
		writeC(0x00);
		writeC(cha.getMoveState().getHeading());
		writeH(0x0000); // target x
		writeH(0x0000); // target y
		writeC(0); // 0:none 2:크로우 4:이도류 0x08:CounterMirror
		writeH(0x00);
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
		return S_ATTACK_PACKET;
	}
}

