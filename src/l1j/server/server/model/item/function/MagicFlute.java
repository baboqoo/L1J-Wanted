package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1GuardianInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Sound;
import l1j.server.server.templates.L1Item;

public class MagicFlute extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public MagicFlute(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.broadcastPacketWithMe(S_Sound.MAGIC_FLUTE_SOUND);
			L1GuardianInstance guardian = null;
			for (L1Object visible : pc.getKnownObjects()) {
				if (visible instanceof L1GuardianInstance) {
					guardian = (L1GuardianInstance) visible;
					if (guardian.getNpcTemplate().getNpcId() == 70850 && pc.getInventory().createItem(88, 1)) {// 판의 뿔
						pc.getInventory().removeItem(this, 1);
					}
				}
			}
		}
	}
}

