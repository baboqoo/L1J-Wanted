package l1j.server.server.model.item.function;

import l1j.server.common.bin.EinhasadPointCommonBinLoader;
import l1j.server.common.bin.einhasadpoint.EinhasadPointStatInfoT;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.einhasadpoint.S_EinhasadPointEnchantStat;
import l1j.server.server.serverpackets.message.S_MessageYN;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;

public class EinStatusCharge extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public EinStatusCharge(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			EinhasadPointStatInfoT infoT = EinhasadPointCommonBinLoader.getInfo();
			if (infoT == null) {
				System.out.println(String.format("[EinStatusCharge] BIN_INFO_EMPTY : CHAR_NAME(%s)", pc.getName()));
				return;
			}
			int value = getItem().getEtcValue();
			if (pc.getEinTotalStat() + value > infoT.get_totalStatMax()) {
				//pc.sendPackets(new S_SystemMessage(String.format("최대 아인하사드 스탯 %d을 넘으므로 사용할 수 없습니다.", infoT.get_totalStatMax())), true);
				pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(92), String.valueOf(infoT.get_totalStatMax())), true);
				return;
			}
			pc.addEinTotalStat(value);
			pc.sendPackets(new S_EinhasadPointEnchantStat(pc, 0, value), true);
			
			// status UI print
			int upstat = (pc.getLevel() - 50) - (pc.getBonusStats());
			if (upstat < 0) {
				upstat = 0;
			}
            pc.sendPackets(new S_MessageYN(479, Integer.toString(upstat)), true);
            pc.getInventory().removeItem(this, 1);
		}
	}
}


