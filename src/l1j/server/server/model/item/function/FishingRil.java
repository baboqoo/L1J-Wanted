package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1Fishing;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.templates.L1Item;

public class FishingRil extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public FishingRil(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance == false) {
			return;
		}
		L1PcInstance pc = (L1PcInstance) cha;
		int targetId	= packet.readD();
		L1ItemInstance targetItem = pc.getInventory().getItem(targetId);
		if (targetItem == null) {
			return;
		}
		useRil(pc, this, targetItem);
	}
	
	public static boolean useRil(L1PcInstance pc, L1ItemInstance rilItem, L1ItemInstance targetRodItem){
		L1Fishing fish		= L1Fishing.fromRod(targetRodItem.getItemId());// 낚싯대 기준
		L1Fishing fishRil	= L1Fishing.fromRil(rilItem.getItemId());// 낚시릴 기준
		if (fish == null || fishRil == null) {
			return false;
		}

		if (fish.getRodId() == 41293) {
			L1ItemInstance create = pc.getInventory().storeItem(fishRil.getRodId(), 1);
			pc.sendPackets(new S_ServerMessage(403, create.getLogNameRef()), true);
			pc.getInventory().removeItem(targetRodItem, 1);
			pc.getInventory().removeItem(rilItem, 1);
			return true;
		} else if (fish.getRodId() == fishRil.getRodId()) {
			if (targetRodItem.getChargeCount() + fish.getRilChargeCount() >= fish.getMaxChargeCount()) {
				pc.sendPackets(L1ServerMessage.sm3457);	// 더 이상 쾌속 릴을 사용할 수 없습니다.
				return true;
			}
			targetRodItem.setChargeCount(targetRodItem.getChargeCount() + fish.getRilChargeCount());
			pc.getInventory().updateItem(targetRodItem, L1PcInventory.COL_CHARGE_COUNT);
			pc.getInventory().removeItem(rilItem, 1);
			return true;
		}
		return false;
	}
}

