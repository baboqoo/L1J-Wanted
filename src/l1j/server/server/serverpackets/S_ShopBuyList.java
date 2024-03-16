package l1j.server.server.serverpackets;

import java.util.List;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.datatables.ShopTable.L1ShopInfo;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.shop.L1AssessedItem;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.serverpackets.S_NoSell;

public class S_ShopBuyList extends ServerBasePacket {

	private static final String S_SHOP_BUY_LIST = "[S] S_ShopBuyList";

	public S_ShopBuyList(int objid, L1PcInstance pc) {
		L1Object object		= L1World.getInstance().findObject(objid);
		if (!(object instanceof L1NpcInstance)) {
			return;
		}
		L1NpcInstance npc	= (L1NpcInstance) object;
		int npcId			= npc.getNpcTemplate().getNpcId();
		L1Shop shop			= ShopTable.getInstance().get(npcId);
		if (shop == null) {
			pc.sendPackets(new S_NoSell(npc), true);
			return;
		}

		List<L1AssessedItem> assessedItems = shop.assessItems(pc.getInventory());
//		if(assessedItems.isEmpty()){
//			pc.sendPackets(new S_NoSell(npc), true);
//			return;
//		}
		writeC(Opcodes.S_SELL_LIST);
		writeD(objid);
		writeH(assessedItems.size());

		for (L1AssessedItem item : assessedItems) {
			writeD(item.getTargetId());
			writeD(item.getAssessedPrice());
		}
		
		L1ShopInfo info = ShopTable.getInstance().getShopInfo(npcId);
		if (info != null) {
			switch(info.getType()){
			case ITEM:
			case BERRY:
			case EIN_POINT:
			case CLAN:
				if (info.getCurrencyDescId() == 0) {
					writeC(255);
					writeC(255);
					writeC(0);
					writeC(0);
				} else {
					writeH(info.getCurrencyDescId());
				}
				break;
			case TAM:
				writeC(253);
				writeC(255);
				break;
			case N_COIN:
				writeC(254);
				writeC(255);
				break;
			}
		} else if ((npcId >= 6100000 && npcId <= 6100013) || (npcId >= 6100042 && npcId <= 6100045) || (npcId >= 6100014 && npcId <= 6100027) || (npcId >= 6100028 && npcId <= 6100041)) {// 패키지상점
			writeC(255);
			writeC(255);
			writeC(0);
			writeC(0);
		} else {
		    writeH(0x07); // 0x00:kaimo 0x01:pearl 0x07:adena
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	
	@Override
	public String getType() {
		return S_SHOP_BUY_LIST;
	}
}
