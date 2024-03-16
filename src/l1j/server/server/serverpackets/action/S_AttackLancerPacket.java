package l1j.server.server.serverpackets.action;

import java.util.concurrent.atomic.AtomicInteger;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_AttackLancerPacket extends ServerBasePacket {
	private static final String S_ATTACK_LANCER_PACKET = "[S] S_AttackLancerPacket";
	private static AtomicInteger _sequentialNumber = new AtomicInteger(0);
	private byte[] _byte = null;
	
	public S_AttackLancerPacket(l1j.server.server.model.L1Character cha, int targetobj, int spellgfx, int x, int y, boolean isHit) {// 창기사
		writeC(Opcodes.S_ATTACK);
		writeC(0x01);
		writeD(cha.getId());
		writeD(targetobj);
		writeC(isHit ? 0x6 : 0x0);
		writeC(0x00);
		writeC(cha.getMoveState().getHeading());
		writeD(_sequentialNumber.incrementAndGet());
		writeH(spellgfx);
		writeC(0x00);// 스킬 사용시의 광원의 넓이?
		writeH(cha.getX());
		writeH(cha.getY());
		writeH(x);
		writeH(y);
		writeH(0x00);
		writeC(0x65);
		writeH(0x00);
		writeH(0x00);
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
		return S_ATTACK_LANCER_PACKET;
	}

}

