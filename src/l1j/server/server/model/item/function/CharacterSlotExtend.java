package l1j.server.server.model.item.function;

import l1j.server.Config;
import l1j.server.server.Account;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;

public class CharacterSlotExtend extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public CharacterSlotExtend(L1Item item) {
		super(item);
	}
	
	private static final S_SystemMessage[] MESSAGES = {
//AUTO SRM: 		new S_SystemMessage("캐릭터 슬롯 확장 완료"), new S_SystemMessage("캐릭터 슬롯이 이미 가득찼습니다.") }; // CHECKED OK
		new S_SystemMessage(S_SystemMessage.getRefText(1064), true), 
		new S_SystemMessage(S_SystemMessage.getRefText(1268), true)};

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			Account account = pc.getAccount();
			int slotCount	= account.getCharSlot();
			if (slotCount >= Config.SERVER.CHARACTER_SLOT_MAX_COUNT) {
				pc.sendPackets(MESSAGES[1]);
				return;
			}
			account.updateCharSlot(slotCount + 1);
			account.setCharSlotChange(true);
			pc.getInventory().removeItem(this, 1);
			pc.sendPackets(MESSAGES[0]);
		}
	}
}


