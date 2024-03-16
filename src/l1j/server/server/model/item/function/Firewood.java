package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1EffectSpawn;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1EffectInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;

public class Firewood extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public Firewood(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			for (L1Object object : L1World.getInstance().getVisibleObjects(pc, 3)) {
				if (object instanceof L1EffectInstance && ((L1NpcInstance) object).getNpcTemplate().getNpcId() == 81170) {
					pc.sendPackets(L1ServerMessage.sm1162);// 벌써 주위에 모닥불이 있습니다.
					return;
				}
			}
			int[] loc = pc.getFrontLoc();
			L1EffectSpawn.getInstance().spawnEffect(81170, 300000, loc[0], loc[1], pc.getMapId());
			pc.getInventory().removeItem(this, 1);
		}
	}
}

