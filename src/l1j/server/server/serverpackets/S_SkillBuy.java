package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_SkillBuy extends ServerBasePacket {
	public S_SkillBuy(int o, L1PcInstance player) {
		int count = skillCount(player);
		writeC(Opcodes.S_BUYABLE_SPELL_LIST);
		writeD(100);
		writeH(count);
		for (int k = 0; k < count; k++) {
			writeD(k);
		}
	}

	public int skillCount(L1PcInstance player) {
		int count = 0;
		switch (player.getType()) {
		case 0: // 군주
		case 4: // 다크엘프
		case 8: // 검사
			count = 16;
			break;
		case 1: // 기사
		case 7: // 전사
		case 9: // 창기사
			count = 8;
			break;
		case 2: // 요정
		case 3: // 법사
			count = 23;
			break;
		default:break;
		}
		return count;
	}
	
	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_SKILL_BUY;
	}

	private static final String S_SKILL_BUY = "[S] S_SkillBuy";
}

