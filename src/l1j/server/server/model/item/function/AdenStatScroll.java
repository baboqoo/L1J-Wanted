package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.templates.L1Item;

public class AdenStatScroll extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public AdenStatScroll(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance == false) {
			return;
		}
		L1PcInstance pc = (L1PcInstance) cha;
		switch (this.getItemId()) {
		case 28889:// 아덴의 완력 주문서
			L1BuffUtil.skillAction(pc, L1SkillId.BUFF_STR_ADD);
			break;
		case 28890:// 아덴의 민첩 주문서
			L1BuffUtil.skillAction(pc, L1SkillId.BUFF_DEX_ADD);
			break;
		case 28891:// 아덴의 지식 주문서
			L1BuffUtil.skillAction(pc, L1SkillId.BUFF_INT_ADD);
			break;
		}
		pc.getInventory().removeItem(this, 1);
	}
}

