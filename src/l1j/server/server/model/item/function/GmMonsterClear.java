package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;

public class GmMonsterClear extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public GmMonsterClear(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			for (L1Object obj : L1World.getInstance().getVisibleObjects(pc, -1)) {
				if (obj instanceof L1MonsterInstance) { // 몬스터라면
					L1MonsterInstance npc = (L1MonsterInstance) obj;
					if (npc.isDead()
							|| npc.getNpcId() == 7200003
							|| npc.getNpcId() == 7200029
							|| npc.getNpcId() == 7800000) {
						continue;
					}
					npc.receiveDamage(pc, 50000); // 대미지
				}
			}
		}
	}
}

