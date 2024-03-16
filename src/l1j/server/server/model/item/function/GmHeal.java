package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_MessageYN;
import l1j.server.server.templates.L1Item;

public class GmHeal extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public GmHeal(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			int objid = pc.getId();
			pc.send_effect(objid, 4856);
			for (L1PcInstance tg : L1World.getInstance().getVisiblePlayer(pc)) {
				if (tg.getCurrentHp() == 0 && tg.isDead()) {
					tg.sendPackets(L1SystemMessage.GM_RESURECT_MSG);
					tg.send_effect(3944);
					// 축복된 부활 스크롤과 같은 효과
					tg.setTempID(objid);
					tg.sendPackets(S_MessageYN.RESURRECT_BLESS_YN); // 또 부활하고 싶습니까? (Y/N)
				} else {
					tg.send_effect(832);
					tg.setCurrentHp(tg.getMaxHp());
					tg.setCurrentMp(tg.getMaxMp());
				}
			}
		}
	}
}

