package l1j.server.server.serverpackets.action;

import java.util.concurrent.atomic.AtomicInteger;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Character;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_RangeSkill extends ServerBasePacket {
	private static final String S_RANGE_SKILL = "[S] S_RangeSkill";
	private static AtomicInteger _sequentialNumber = new AtomicInteger(0);
	private byte[] _byte = null;

	public static final int TYPE_NODIR = 0;
	public static final int TYPE_DIR = 8;

	public S_RangeSkill(L1Character cha, L1Character[] target, int spellgfx, int actionId, int type) {
		buildPacket(cha, target, spellgfx, actionId, type, false);
	}
	
	public S_RangeSkill(L1Character cha, L1Character[] target, int spellgfx, int actionId, int type, boolean hit) {
		buildPacket(cha, target, spellgfx, actionId, type, hit);
	}

	void buildPacket(L1Character cha, L1Character[] target, int spellgfx, int actionId, int type, boolean hit) {
		writeC(Opcodes.S_ATTACK_MANY);
		writeC(actionId);
		writeD(cha.getId());
		writeH(cha.getX());
		writeH(cha.getY());
		if (type == TYPE_NODIR) {
			writeC(cha.getMoveState().getHeading());
		} else if (type == TYPE_DIR) {
			int newHeading = cha.calcheading(cha.getX(), cha.getY(), target[0].getX(), target[0].getY());
			if (cha.getMoveState().getHeading() != newHeading) {
				cha.getMoveState().setHeading(newHeading);
			}
			writeC(cha.getMoveState().getHeading());
		}
		writeD(_sequentialNumber.incrementAndGet()); // 번호가 겹치지 않게 보낸다.
		writeH(spellgfx);
		writeC(type); // 0:범위 6:원거리 8:범위&원거리
		writeH(0);
		writeH(target[0].getX());
		writeH(target[0].getY());
		writeH(target.length);
		for (int i = 0; i < target.length; i++) {
			writeD(target[i].getId());
			writeH(hit ? 0x00 : 0x20); // 0:대미지 모션 있음 0이외:없음
		}
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
		return S_RANGE_SKILL;
	}

}
