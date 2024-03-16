package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.ResolventTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.CommonUtil;

public class Resolvent extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public Resolvent(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			useResolvent(pc, pc.getInventory().getItem(packet.readD()));
		}
	}
	
	private void useResolvent(L1PcInstance pc, L1ItemInstance target) {
		if (target == null) {
			pc.sendPackets(L1ServerMessage.sm79);// \f1 아무것도 일어나지 않았습니다.
			return;
		}
		if (target.getEnchantLevel() != 0 || target.isEquipped() || target.getEndTime() != null || target.getBless() >= 128 || target.isSlot()) {
			pc.sendPackets(L1ServerMessage.sm1161);// 용해할 수 없습니다.
			return;
		}
		int crystalCount = ResolventTable.getInstance().getCrystalCount(target.getItem().getItemId());
		if (crystalCount == 0) {
			pc.sendPackets(L1ServerMessage.sm1161);// 용해할 수 없습니다.
			return;
		}

		int rnd = CommonUtil.random(100) + 1;
		if (rnd <= 20) {
			crystalCount = 0;
			pc.sendPackets(new S_ServerMessage(158, target.getItem().getDesc()), true);// \f1%0이 증발하고 있지 않게 되었습니다.
		} else if (rnd <= 30) {
			crystalCount += crystalCount >> 1;// 1.5배
		}
		if (crystalCount != 0) {
			L1ItemInstance crystal = ItemTable.getInstance().createItem(L1ItemId.GEMSTONE);
			crystal.setCount(crystalCount);
			if (pc.getInventory().checkAddItem(crystal, 1) == L1Inventory.OK) {
				pc.getInventory().storeItem(crystal);
				pc.sendPackets(new S_ServerMessage(403, crystal.getLogNameRef()), true);// %0를 손에 넣었습니다.
			} else {// 가질 수 없는 경우는 지면에 떨어뜨리는 처리의 캔슬은 하지 않는다(부정 방지)
				L1World.getInstance().getInventory(pc.getX(), pc.getY(), pc.getMapId()).storeItem(crystal);
			}
		}
		pc.getQuest().questItemUse(this.getItemId());
		pc.getInventory().removeItem(target, 1);
		pc.getInventory().removeItem(this, 1);
	}

}

