package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.serverpackets.S_PacketBox;

public class A_SealItem extends ProtoHandler {
	protected A_SealItem(){}
	private A_SealItem(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);
		L1PcInventory inv			= _pc.getInventory();
		L1ItemInstance targetitem	= inv.getItem(read4(read_size()));
		if (targetitem.getItem().getItemType() == L1ItemType.NORMAL 
				&& targetitem.getItem().get_interaction_type() != L1ItemType.MAGICDOLL.getInteractionType() 
				&& targetitem.getItem().getItemId() != 202810 && targetitem.getItem().getItemId() != 202813) {// etc 아이템이라면
			_pc.sendPackets(L1ServerMessage.sm79);// \f1 아무것도 일어나지 않았습니다.
			return;
		}
		if (targetitem.getEndTime() != null) {// 시간제 아이템
			_pc.sendPackets(L1ServerMessage.sm79);// \f1 아무것도 일어나지 않았습니다.
			return;
		}

		int bless = 0;
		switch (targetitem.getBless()) {
		case 0:bless = 128;break;// 축
		case 1:bless = 129;break;// 보통
		case 2:bless = 130;break;// 저주
		case 3:bless = 131;break;// 미확인
		default:
			_pc.sendPackets(L1ServerMessage.sm79);// \f1 아무것도 일어나지 않았습니다.
			return;
		}
		targetitem.setBless(bless);
        int st = 0;
        if (targetitem.isIdentified()) {
        	st += 1;
        }
        if (!targetitem.getItem().isTradable()) {
        	st += 2;
        }
        if (targetitem.getItem().isCantDelete()) {
        	st += 4;
        }
        if (targetitem.getItem().getSafeEnchant() < 0) {
        	st += 8;
        }
        if (targetitem.getBless() >= 128) {
        	st = targetitem.isIdentified() ? 47 : 46;
        }
        _pc.sendPackets(new S_PacketBox(S_PacketBox.ITEM_STATUS, targetitem, st), true);
        inv.updateItem(targetitem, L1PcInventory.COL_IS_ID);
        inv.saveItem(targetitem, L1PcInventory.COL_IS_ID);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_SealItem(data, client);
	}

}

