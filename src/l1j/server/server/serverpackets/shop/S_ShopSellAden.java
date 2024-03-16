package l1j.server.server.serverpackets.shop;

import java.io.IOException;
import java.util.List;

import l1j.server.Config;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1ShopItem;
import l1j.server.server.utils.StringUtil;

public class S_ShopSellAden extends ServerBasePacket {
	
	public S_ShopSellAden(int objId, L1PcInstance pc) {
		writeC(Opcodes.S_EXTENDED);
		writeD(objId);
		writeH(0);
		writeD(6000);
		writeH(0);
	}
	
	public S_ShopSellAden(L1PcInstance pc) {
		writeC(Opcodes.S_BUY_LIST);
		writeD(7626);// objId C_ShopAndWarehouse.java 에서 지정해줘야함
		writeC(SHOP_TYPE.NORMAL.toInt());// nomal type
		 //아덴상점 엔피씨번호 
		int npcId = Config.ALT.ADEN_SHOP_NPC_ID; //아덴상점 하고자하는 엔피씨번호 월드상에 없어도됨 

		//System.out.println("Using the aden shop of the NPC: " + npcId);

		L1Shop shop = ShopTable.getInstance().get(npcId);
		List<L1ShopItem> shopItems = shop.getSellingItems();
		writeH(shopItems.size());

		L1ItemInstance dummy	= new L1ItemInstance();
		L1ShopItem shopItem		= null;
		L1Item item				= null;
		L1Item template			= null;
		ItemTable it			= ItemTable.getInstance();
		for (int i = 0; i < shopItems.size(); i++) {
			shopItem = (L1ShopItem) shopItems.get(i);
			item = shopItem.getItem();
			int price = shopItem.getPrice();
			writeD(i);
			writeD(shopItem.getItem().getItemNameId());
			writeH(shopItem.getItem().getIconId());
			writeD(price);
			
			StringBuilder sb = new StringBuilder();
			if (shopItem.getEnchant() > 0) {
				sb.append(StringUtil.PlusString).append(shopItem.getEnchant()).append(StringUtil.EmptyOneString);
			}
			if (shopItem.getItem().getBless() != 1) { 
				if (shopItem.getItem().getBless() == 0) {
					sb.append(L1ItemInstance.BLESS_STATUS_STRING[0]);
				} else if (shopItem.getItem().getBless() == 2) {
					sb.append(L1ItemInstance.BLESS_STATUS_STRING[1]);
				}
			}
			//sb.append(item.getDescKr());
			sb.append(item.getDesc());
			if (shopItem.getPackCount() > 1) {
				sb.append(" (").append(shopItem.getPackCount()).append(")");
			}
			if (shopItem.getItem().getMaxUseTime() > 0) {
				sb.append(" [").append(item.getMaxUseTime()).append("]");
			}
			writeS(sb.toString());
			sb = null;
			
			int type = shopItem.getItem().get_interaction_type();
			if (type < 0) {
				type = 0;
			}
			writeD(type);
			
			template = it.getTemplate(item.getItemId());
			if (template == null) {
				writeC(0);
			} else {
				dummy.setItem(template);
				dummy.setEnchantLevel(shopItem.getEnchant());
				dummy.updateItemAbility(pc);
				writeD(dummy.getAttributeBitSet());
				writeD(0x00);
				writeC(0x00);
				writeBytesWithLength(dummy.getStatusBytes(pc));
			}
		}
		writeH(7);// 표기 nameid
	}

	public byte[] getContent() throws IOException {
		return this._bao.toByteArray();
	}
}
