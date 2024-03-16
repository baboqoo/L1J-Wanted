package l1j.server.server.model.item.function;

import l1j.server.server.Account;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.StringUtil;

public class NcoinCharge extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public NcoinCharge(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance == false) {
			return;
		}
		L1PcInstance pc = (L1PcInstance) cha;
		Account account = pc.getAccount();
		int count = getItem().getEtcValue();
		account.addNcoin(count);
		account.updateNcoin();
		//pc.sendPackets(new S_SystemMessage(String.format("N코인 %s 원 충전되었습니다.", StringUtil.comma(count))), true);
		pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(99), StringUtil.comma(count)), true);
		pc.getInventory().removeItem(this, 1);
	}
}


