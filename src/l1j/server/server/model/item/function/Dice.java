package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.CommonUtil;

public class Dice extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public Dice(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (!pc.getInventory().consumeItem(L1ItemId.MAGIC_STONE, 1)) {
				pc.sendPackets(L1ServerMessage.sm79);// \f1 아무것도 일어나지 않았습니다.
				return;
			}
			switch(this.getItemId()){
			case 40325:pc.send_effect(3237 + CommonUtil.random(2));break;// 2단계 마법주사위
			case 40326:pc.send_effect(3229 + CommonUtil.random(3));break;// 3단계 마법주사위
			case 40327:pc.send_effect(3241 + CommonUtil.random(4));break;// 4단계 마법주사위
			case 40328:pc.send_effect(3204 + CommonUtil.random(6));break;// 6단계 마법주사위
			}	
		}
	}
}

