package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.CommonUtil;

public class Miscarriage extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public Miscarriage(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			getItem(pc);
		}
	}
	
	private void getItem(L1PcInstance pc){
		if (pc.getInventory().getSize() > L1PcInventory.MAX_SIZE - 2 || pc.getInventory().getWeightPercent() > 98) {
			pc.sendPackets(L1ServerMessage.sm82);// 무게 게이지가 부족하거나 인벤토리가 꽉차서 더 들 수 없습니다.
			return;
		}
		int item_id = this.getItem().getItemId();
		L1ItemInstance create = null;
		switch(item_id){
		case 40304:	create = pc.getInventory().storeItem(L1ItemId.MAGIC_STONE, CommonUtil.random(6) + 5);	break;
		case 40305:	create = pc.getInventory().storeItem(40320, CommonUtil.random(6) + 5);	break;
		case 40306:	create = pc.getInventory().storeItem(L1ItemId.ELF_ATTR_STONE, CommonUtil.random(6) + 5);	break;
		case 40307:	
			int chance = CommonUtil.random(100) + 1;
			if (chance >= 1 && chance < 25) {
				create = pc.getInventory().storeItem(L1ItemId.MAGIC_STONE, CommonUtil.random(20) + 1); // 마돌
			} else if (chance >= 25 && chance < 50) {
				create = pc.getInventory().storeItem(L1ItemId.ELF_ATTR_STONE, CommonUtil.random(30) + 1); // 정령옥
			} else if (chance >= 50 && chance < 75) {
				create = pc.getInventory().storeItem(40320, CommonUtil.random(20) + 1); // 흑마석
			} else {
				create = pc.getInventory().storeItem(40031, CommonUtil.random(5) + 1); // 악마의피
			}
			break;
		}
		if (create != null) {
			pc.sendPackets(new S_ServerMessage(143, create.getLogNameRef()), true);
		}
		pc.getInventory().removeItem(this, 1);
	}
}

