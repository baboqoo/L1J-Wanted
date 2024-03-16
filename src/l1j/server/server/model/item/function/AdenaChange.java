package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;

public class AdenaChange extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public AdenaChange(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			switch(this.getItemId()){
			case 400253:			toScroll(pc, 100000000, L1ItemId.CHEQUE);	break;// 1억 아데나 금고
			case 31110:				toScroll(pc, 50000000, 31109);				break;// 5천만 아데나 금고
			case 31132:				toScroll(pc, 10000000, 31131);				break;// 1천만 아데나 금고
			case L1ItemId.CHEQUE:	toAdena(pc, 100000000);						break;// 1억 아데나 주머니
			case 31109:				toAdena(pc, 50000000);						break;// 5천만 아데나 주머니
			case 31131:				toAdena(pc, 10000000);						break;// 1천만 아데나 주머니
			}
		}
	}
	
	private void toScroll(L1PcInstance pc, int count, int output){
		if (pc.getInventory().consumeItem(L1ItemId.ADENA, count)) {
			L1ItemInstance outputItem = pc.getInventory().storeItem(output, 1);
			pc.sendPackets(new S_ServerMessage(403, String.format("%s (%d)", outputItem.getItem().getDesc(), count)), true);
		} else {
			pc.sendPackets(L1ServerMessage.sm189);
		}
	}
	
	private void toAdena(L1PcInstance pc, int count){
		if (pc.getInventory().findItemIdCount(L1ItemId.ADENA) + count >= L1Inventory.MAX_AMOUNT) {
			pc.sendPackets(L1SystemMessage.ADENA_OVER_MAX_FAIL);
			return;
		}
		long curtime = System.currentTimeMillis() / 1000;
		if (pc.getQuizTime() + 1 > curtime) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("1초 후에 시도해 주십시오."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1062), true), true);
			return;
		}
		L1ItemInstance outputItem = pc.getInventory().storeItem(L1ItemId.ADENA, count);
		pc.sendPackets(new S_ServerMessage(403, String.format("%s (%d)", outputItem.getItem().getDesc(), count)), true);
		pc.setQuizTime(curtime);
		pc.getInventory().removeItem(this, 1);
	}
}


