package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1TowerInstance;
import l1j.server.server.serverpackets.message.S_MessageYN;
import l1j.server.server.templates.L1Item;

public class ResurrectionScroll extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public ResurrectionScroll(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			L1Object resobject = (L1Object) L1World.getInstance().findObject(packet.readD());
			if (resobject != null) {
				if (resobject instanceof L1PcInstance) {
					L1PcInstance target = (L1PcInstance) resobject;
					if (pc.getId() == target.getId()) {
						return;
					}
					int castle_id = L1CastleLocation.getCastleIdByArea(pc);// 공성장에서는 부활불가능하도록 
					if (castle_id != 0) {
						pc.sendPackets(L1ServerMessage.sm563); // 여기에서는 사용할 수 없습니다.
						return;
					}
					if (L1World.getInstance().getVisiblePlayer(target, 0).size() > 0) {
						for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(target, 0)) {
							if (!visiblePc.isDead()) {
								pc.sendPackets(L1ServerMessage.sm592);// 그 자리에 다른 사람이 서 있으므로 부활시킬 수가 없습니다.
								return;
							}
						}
					}
					if (target.getCurrentHp() == 0 && target.isDead()) {
						if (!pc.getMap().isUseResurrection()) {
							return;
						}
						target.setTempID(pc.getId());
						target.sendPackets(this.getItemId() == 140089 ? S_MessageYN.RESURRECT_BLESS_YN : S_MessageYN.RESURRECT_YN);// 또 부활하고 싶습니까? (Y/N)
					}
				} else if (resobject instanceof L1NpcInstance) {
					if (!(resobject instanceof L1TowerInstance)) {
						L1NpcInstance npc = (L1NpcInstance) resobject;
						if (npc.getNpcTemplate().isCantResurrect()) {// 부활 불가
							pc.getInventory().removeItem(this, 1);
							return;
						}
						if (npc instanceof L1PetInstance) {
							pc.sendPackets(L1ServerMessage.sm79);
							pc.getInventory().removeItem(this, 1);
							return;
						}
						if (L1World.getInstance().getVisiblePlayer(npc, 0).size() > 0) {
							for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(npc, 0)) {
								if (!visiblePc.isDead()) {
									pc.sendPackets(L1ServerMessage.sm592);// \f1그 자리에 다른 사람이 서 있으므로 부활시킬 수가 없습니다.
									return;
								}
							}
						} 
						if (npc.getCurrentHp() == 0 && npc.isDead()) {
							npc.resurrect(npc.getMaxHp() >> 2);
							npc.setResurrect(true);
						}
					}
				} else {
					pc.sendPackets(L1ServerMessage.sm79);
				}
			}
			pc.getInventory().removeItem(this, 1);
		}
	}
}

