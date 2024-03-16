package l1j.server.server.model.item.function;

import l1j.server.Config;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.item.L1ItemArmorType;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.inventory.S_ItemStatus;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.CommonUtil;

public class BlessScroll extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public BlessScroll(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			L1ItemInstance target = pc.getInventory().getItem(packet.readD());
			if (target == null) {
				return;
			}
			blessPaper(pc, target, this.getItemId() == 7010);
		}
	}
	
	private void blessPaper(L1PcInstance pc, L1ItemInstance target, boolean isChance){
		if (target == null || target.getItem().getItemType() == L1ItemType.NORMAL || target.getBless() >= 128 || target.getBless() == 0) {
			pc.sendPackets(L1ServerMessage.sm79);
			return;
		}
		if (target.getItem().getType() == 14) {
			pc.sendPackets(L1ServerMessage.sm79);
			return;
		}
		if (target.getItem().getItemType() == L1ItemType.ARMOR && (target.getItem().getType() >= L1ItemArmorType.AMULET.getId() && target.getItem().getType() <= L1ItemArmorType.EARRING.getId())) {
			pc.sendPackets(L1ServerMessage.sm79);
			return;
		}
		
		if (isChance && CommonUtil.random(100) + 1 >= Config.ENCHANT.BLESSED_SCROLL_PROBABILITY) {
			pc.sendPackets(L1SystemMessage.ITEM_BLESS_CHAGE_FAIL);
			pc.getInventory().removeItem(this, 1);
			return;
		}
		
		target.setBless(0);
		pc.sendPackets(new S_ItemStatus(target, pc), true);
		pc.getInventory().updateItem(target, L1PcInventory.COL_BLESS);
		pc.getInventory().saveItem(target, L1PcInventory.COL_BLESS);
		pc.getInventory().removeItem(this, 1);
		pc.send_effect_self(9268);
		//pc.sendPackets(new S_SystemMessage(String.format("%s에 축복의 기운이 스며듭니다.", target.getLogNameRef()), true), true);
		pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(91), target.getLogNameRef()), true);
	}
	
}


