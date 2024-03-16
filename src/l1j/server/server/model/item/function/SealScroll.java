package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.templates.L1Item;

public class SealScroll extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public SealScroll(L1Item item) {
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
			int itemId = this.getItemId();
			if (itemId == 50020) {
				sealPaper(pc, target, true);
			} else if (itemId == 50021) {
				sealPaper(pc, target, false);
			}
		}
	}
	
	private void sealPaper(L1PcInstance pc, L1ItemInstance target, boolean result){
		if (result) {
			if (target.getBless() == 0 || target.getBless() == 1 || target.getBless() == 2 || target.getBless() == 3) {
				if (target.getItem().getItemType() == L1ItemType.NORMAL && target.getItem().get_interaction_type() != L1ItemType.MAGICDOLL.getInteractionType() 
						&& target.getItem().getItemId() != 202810 && target.getItem().getItemId() != 202813) {// etc 아이템이라면
					pc.sendPackets(L1ServerMessage.sm79); // \f1 아무것도 일어나지 않았습니다.
					return;
				}
				if (target.getEndTime() != null) {
					pc.sendPackets(L1ServerMessage.sm79); // \f1 아무것도 일어나지 않았습니다.
					return;
				}
				int bless = 0;
				switch (target.getBless()) {
				case 0:	bless = 128;	break; // 축
				case 1:	bless = 129;	break; // 보통
				case 2:	bless = 130;	break; // 저주
				case 3:	bless = 131;	break; // 미확인
				}
				target.setBless(bless);
				int st = 0;
				if (target.isIdentified()) {
					st += 1;
				}
				if (!target.getItem().isTradable()) {
					st += 2;
				}
				if (target.getItem().isCantDelete()) {
					st += 4;
				}
				if (target.getItem().getSafeEnchant() < 0) {
					st += 8;
				}
				if (target.getBless() >= 128) {
					st = target.isIdentified() ? 47 : 46;
				}
				pc.sendPackets(new S_PacketBox(S_PacketBox.ITEM_STATUS, target, st), true);
				pc.getInventory().updateItem(target, L1PcInventory.COL_IS_ID);
				pc.getInventory().saveItem(target, L1PcInventory.COL_IS_ID);
				pc.getInventory().removeItem(this, 1);
			} else {
				pc.sendPackets(L1ServerMessage.sm79); // \f1 아무것도 일어나지 않았습니다.
			}
		} else {
			if (target.getBless() == 128 || target.getBless() == 129 || target.getBless() == 130 || target.getBless() == 131) {
				if (target.getEndTime() != null) {
					pc.sendPackets(L1ServerMessage.sm79);
					return;
				}
				int bless = 0;
				switch(target.getBless()){
				case 128:	bless = 0;	break;// 축
				case 129:	bless = 1;	break;// 보통
				case 130:	bless = 2;	break;// 저주
				case 131:	bless = 3;	break; // 미확인
				}
				target.setBless(bless);
				int st = 0;
				if (target.isIdentified()) {
					st += 1;
				}
				if (!target.getItem().isTradable()) {
					st += 2;
				}
				if (target.getItem().isCantDelete()) {
					st += 4;
				}
				if (target.getItem().getSafeEnchant() < 0) {
					st += 8;
				}
				if (target.getBless() >= 128) {
					st = target.isIdentified() ? 47 : 46;
				}
				pc.sendPackets(new S_PacketBox(S_PacketBox.ITEM_STATUS, target, st), true);
				pc.getInventory().updateItem(target, L1PcInventory.COL_IS_ID);
				pc.getInventory().saveItem(target, L1PcInventory.COL_IS_ID);
				pc.getInventory().removeItem(this, 1);
			} else {
				pc.sendPackets(L1ServerMessage.sm79);// \f1 아무것도 일어나지 않았습니다.
			}
		}
	}
	
}

