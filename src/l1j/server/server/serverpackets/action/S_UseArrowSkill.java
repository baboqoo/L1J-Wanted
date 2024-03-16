package l1j.server.server.serverpackets.action;

import java.util.concurrent.atomic.AtomicInteger;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Character;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_UseArrowSkill extends ServerBasePacket {
	private static final String S_USE_ARROW_SKILL = "[S] S_UseArrowSkill";
	private static AtomicInteger _sequentialNumber = new AtomicInteger(0);
	private byte[] _byte = null;

	public S_UseArrowSkill(L1Character cha, int targetobj, int spellgfx, int x, int y, boolean isHit) {
		int actionId = 1;
		int spriteId = cha.getSpriteId();
		if (spriteId == 3105) {// black scouter
			actionId = 21;
		} else if (spriteId == 16198 || spriteId == 16202 || spriteId == 16208) {// 폭탄꽃
			actionId = 18;
		} else if (spriteId == 15724 || spriteId == 15730 || spriteId == 4125 || spriteId == 11714) {// 해골 저격병
			actionId = 30;
		} else if (spriteId == 18913 && cha.isTriple) {// 몽환의 서큐버스 트리플 애로우
			actionId = 75;
		}
		writeC(Opcodes.S_ATTACK);
		writeC(actionId);
		writeD(cha.getId());
		writeD(targetobj);
		writeC(isHit ? 0x2c : 0x00);
		writeC(0x00);
		writeC(cha.getMoveState().getHeading());

		writeD(_sequentialNumber.incrementAndGet());
		writeH(spellgfx);
		writeC(0x00);
		
		writeH(cha.getX());
		writeH(cha.getY());
		writeH(x);
		writeH(y);

		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeC(0x00);
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
		return S_USE_ARROW_SKILL;
	}

}

