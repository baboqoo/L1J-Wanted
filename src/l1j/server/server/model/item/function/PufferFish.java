package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.templates.L1Item;

public class PufferFish extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public PufferFish(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance == false) {
			return;
		}
		L1PcInstance pc = (L1PcInstance) cha;
		L1BuffUtil.skillAction(pc, L1SkillId.BUFF_PUFFER_FISH);
		pc.getInventory().removeItem(this, 1);
	}
}

