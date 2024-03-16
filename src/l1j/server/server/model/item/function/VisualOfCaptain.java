package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.templates.L1Item;

public class VisualOfCaptain extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public VisualOfCaptain(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance == false) {
			return;
		}
		L1PcInstance pc = (L1PcInstance) cha;
		boolean pre = pc.getSkill().hasSkillEffect(L1SkillId.BUFF_VISUAL_OF_CAPTAIN);
		L1BuffUtil.skillAction(pc, L1SkillId.BUFF_VISUAL_OF_CAPTAIN);
		if (!pre) {
			visible(pc);
		}
		pc.getInventory().removeItem(this, 1);
		
	}
	
	/**
	 * 은신몬스터 오브젝트 갱신
	 * @param pc
	 */
	void visible(L1PcInstance pc) {
		for (L1Object obj : pc.getKnownObjects()) {
			if (obj instanceof L1MonsterInstance == false) {
				continue;
			}
			L1MonsterInstance mon = (L1MonsterInstance) obj;
			if (!mon.getNpcTemplate().isHide()) {
				continue;
			}
			pc.removeKnownObject(mon);// 기존 인식 제거
			mon.onPerceive(pc);// 화면 출력
		}
	}
	
}

