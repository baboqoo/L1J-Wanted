package l1j.server.server.model.item.function;

import javolution.util.FastMap;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.datatables.ItemClickMessageTable;
import l1j.server.server.datatables.ItemClickMessageTable.ItemClickMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;

public class ItemClickMessageSend extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public ItemClickMessageSend(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			ItemClickMessage clickData = ItemClickMessageTable.getData(this.getItemId());
			if (clickData == null) {
				return;
			}
			if (clickData._type) {
				if (pc.isGm())
					pc.sendPackets(new S_SystemMessage("Dialog " + clickData._msg), true);											
				
				pc.sendPackets(new S_NPCTalkReturn(pc.getId(), clickData._msg), true);
			} else {
				pc.sendPackets(getSystemMessage(clickData._msg));
			}
			if (clickData._delete) {
				pc.getInventory().removeItem(this, 1);
			}
		}
	}
	
	private static final FastMap<String, S_SystemMessage> MESSAGES = new FastMap<String, S_SystemMessage>();
	private static S_SystemMessage getSystemMessage(String msg){
		S_SystemMessage pck = MESSAGES.get(msg);
		if (pck == null) {
			pck = new S_SystemMessage(msg, true);
			MESSAGES.put(msg, pck);
		}
		return pck;
	}
}

