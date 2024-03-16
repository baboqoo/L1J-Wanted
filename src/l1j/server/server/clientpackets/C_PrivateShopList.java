package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.shop.S_PersonalShop;
import l1j.server.server.serverpackets.shop.S_PersonalShop.ePersonalShopType;

public class C_PrivateShopList extends ClientBasePacket {
	private static final String C_PRIVATE_SHOP_LIST = "[C] C_PrivateShopList";

	public C_PrivateShopList(byte abyte0[], GameClient client) {
		super(abyte0);
		L1PcInstance pc = client.getActiveChar();
		if (pc == null || pc.isGhost()) {
			return;
		}
		ePersonalShopType type	= ePersonalShopType.fromInt(readC());
		int objectId			= readD();
		L1Object shopPc			= L1World.getInstance().findObject(objectId);
		if (shopPc instanceof L1PcInstance) {
			L1PcInstance seller = (L1PcInstance) shopPc;
			if (pc.getAccountName().equalsIgnoreCase(seller.getAccountName())) {
				pc.sendPackets(L1SystemMessage.PRIVATE_SHOP_SELF_FIAL);
				return;
			}
			pc.sendPackets(new S_PersonalShop(pc, seller, type), true);
		} else if (shopPc instanceof L1NpcShopInstance) {
			pc.sendPackets(new S_PersonalShop(pc, (L1NpcShopInstance)shopPc, type), true);
		} else {
			pc.sendPackets(L1SystemMessage.TARGET_PC_EMPTY);
		}
	}

	@Override
	public String getType() {
		return C_PRIVATE_SHOP_LIST;
	}

}

