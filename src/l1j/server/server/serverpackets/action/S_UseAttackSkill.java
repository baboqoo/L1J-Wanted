package l1j.server.server.serverpackets.action;

import java.util.concurrent.atomic.AtomicInteger;

import l1j.server.server.ActionCodes;
import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_UseAttackSkill extends ServerBasePacket {
	private static final String S_USE_ATTACK_SKILL = "[S] S_UseAttackSkill";
	private static AtomicInteger _sequentialNumber = new AtomicInteger(0);
	private byte[] _byte = null;
	
	public S_UseAttackSkill(L1Character cha, int targetobj, int spellgfx, int x, int y, int actionId) {
		buildPacket(cha, targetobj, spellgfx, x, y, actionId, 6, true);
	}

	public S_UseAttackSkill(int spellgfx, int x, int y) {
		writeC(Opcodes.S_ATTACK);
		writeC(0);
		writeD(0);
		writeD(0);
		writeC(0);
		writeC(0);
		writeC(0);
		writeD(_sequentialNumber.incrementAndGet()); // 번호가 겹치지 않게 보낸다
		writeH(spellgfx);
		writeC(6); // 타켓지종:6, 범위&타켓지종:8, 범위:0
		writeH(x);
		writeH(y);
		writeH(x);
		writeH(y);
		writeC(0);
		writeC(0);
		writeC(0);
		writeC(0);
		writeC(0);
	}
	public S_UseAttackSkill(L1Character cha, int targetobj, int spellgfx, int x, int y, int actionId, boolean motion) {
		buildPacket(cha, targetobj, spellgfx, x, y, actionId, 0, motion);
	}

	public S_UseAttackSkill(L1Character cha, int targetobj, int spellgfx, int x, int y, int actionId, int isHit) {
		buildPacket(cha, targetobj, spellgfx, x, y, actionId, isHit, true);
	}

	private void buildPacket(L1Character cha, int targetobj, int spellgfx, int x, int y, int actionId, int isHit, boolean withCastMotion) {
		if (cha instanceof L1PcInstance) {
			// 그림자계 변신중에 공격 마법을 사용하면(자) 클라이언트가 떨어지기 (위해)때문에 잠정 대응
			if (cha.isShapeChange() && actionId == ActionCodes.ACTION_SkillAttack) {
				int spriteId = cha.getSpriteId();
				if (spriteId == 5727 || spriteId == 5730) {
					actionId = ActionCodes.ACTION_SkillBuff;
				} else if (spriteId == 5733 || spriteId == 5736) {
					actionId = ActionCodes.ACTION_Attack;// 보조 마법 모션으로 하면(자) 공격 마법의 그래픽과 대상에의 대미지 모션이 발생하지 않게 되기 (위해)때문에 공격 모션으로 대용
				}
			}
		}
		// 화령의 주인이 디폴트라면 공격 마법의 그래픽이 발생하지 않기 때문에 강제 치환 어딘가 별개로 관리하는 것이 좋아?
		if (cha.getSpriteId() == 4013) {
			actionId = ActionCodes.ACTION_Attack;
		}
		
		if (cha.getX() != x || cha.getY() != y) {
			int newheading = cha.calcheading(cha.getX(), cha.getY(), x, y);
			if (cha.getMoveState().getHeading() != newheading) {
				cha.getMoveState().setHeading(newheading);
			}
		}
		writeC(Opcodes.S_ATTACK);
		writeC(actionId);
		writeD(withCastMotion ? cha.getId() : 0);
		writeD(targetobj);
		writeC(isHit);
		writeC(0);
		writeC(cha.getMoveState().getHeading());
		writeD(_sequentialNumber.incrementAndGet()); // 번호가 겹치지 않게 보낸다
		writeH(spellgfx);
		writeC(6); // 타켓지종:6, 범위&타켓지종:8, 범위:0
		writeH(cha.getX());
		writeH(cha.getY());
		writeH(x);
		writeH(y);
		writeC(0);
		writeC(0);
		writeC(0);
		writeC(0);
		writeC(0);
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
		return S_USE_ATTACK_SKILL;
	}

}
