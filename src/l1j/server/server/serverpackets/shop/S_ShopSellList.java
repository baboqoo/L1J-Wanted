package l1j.server.server.serverpackets.shop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import l1j.server.Config;
import l1j.server.server.Opcodes;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1TaxCalculator;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.L1CollectionModel;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1ShopItem;
import l1j.server.server.utils.StringUtil;

public class S_ShopSellList extends ServerBasePacket {
	
	public S_ShopSellList(int objId, L1PcInstance pc) {
		writeC(Opcodes.S_BUY_LIST);
		writeD(objId);
		writeC(SHOP_TYPE.NORMAL.toInt());// normal type
		
		L1Object npcObj = L1World.getInstance().findObject(objId);
		if (!(npcObj instanceof L1NpcInstance)) {
			writeH(0);
			return;
		}
		int npcId = ((L1NpcInstance) npcObj).getNpcTemplate().getNpcId();

		//System.out.println("Using the shop of the NPC: " + npcId);

		L1TaxCalculator calc = new L1TaxCalculator(npcId);

		ShopTable shopTable = ShopTable.getInstance();
		L1Shop shop = null;
		if (npcId == 100800 || npcId == 81011) {// 인센스 스킬상인
			shop = shopTable.get(pc.getType() + 10);
		} else if (npcId == 202054) {// 케슨 수련자 장비
			shop = shopTable.get(pc.getType() + 60);
		} else if (npcId == 70073 || npcId == 5157 || npcId == 70032 || npcId == 70024 || npcId == 5156 || npcId == 70016 || npcId == 70044 || npcId == 70061 || npcId == 70083 || npcId == 70058) {// 무기 방어구 상인
			shop = shopTable.get(pc.getType() + 70);
		} else if (npcId == 100600 && pc.isLancer()) {// 신규 클래스 스킬북 상인
			shop = shopTable.get(100601);
		} else {
			shop = shopTable.get(npcId);
		}
		
		List<L1ShopItem> shopItems = null;
		List<L1ShopItem> passList = new ArrayList<L1ShopItem>();
		
		try {
			shopItems = shop.getSellingItems();
		} catch (Exception e) {
		}
		if (shopItems != null) {
			// 상점 리스트에서 감추기
			for (L1ShopItem si : shopItems) {
				int itemId = si.getItem().getItemId();
				if (itemId == 8003 && pc.getInventory().checkItem(8003, 1)) {// 랭커 변신 주문서
					passList.add(si);
				} else if (itemId == 2300140 && pc.getInventory().getCollection().getStatus(L1CollectionModel.TYPE_BLACK_TIGER)) {// 기운을 잃은 흑호
					passList.add(si);
				} else if (itemId == 31236 && !pc.getInventory().checkItem(31235)) {// 영광의 아지트 초대장
					passList.add(si);
				}
			}
			writeH(shopItems.size() - passList.size());
		} else {
			writeH(0);
			return;
		}

		L1ItemInstance dummy	= new L1ItemInstance();
		L1ShopItem shopItem		= null;
		L1Item item				= null;
		L1Item template			= null;
		ItemTable it			= ItemTable.getInstance();
		for (int i = 0; i < shopItems.size(); i++) {
			shopItem = (L1ShopItem) shopItems.get(i);
			if (passList.contains(shopItem)) {
				continue;
			}
			item = shopItem.getItem();
			int price = calc.layTax((int) (shopItem.getPrice() * Config.RATE.RATE_SHOP_SELLING_PRICE));
			int oriPrice1 = shopItem.getPrice();
			writeD(i);
			
			try {
				writeD(shopItem.getItem().getItemNameId());
				writeH(shopItem.getItem().getIconId());
			} catch (Exception e) {
				//System.out.println("엔피시 상점 오류 엔피시 번호 :" + npcId);
				System.out.println("NPC Shop Error - NPC ID: " + npcId);
			}
			writeD((npcId == 70035 || npcId == 70041 || npcId == 70042) ? oriPrice1 : price);
			
			StringBuilder sb = new StringBuilder();
			if (shopItem.getEnchant() > 0) {
				sb.append(StringUtil.PlusString).append(shopItem.getEnchant()).append(StringUtil.EmptyOneString);
			}
			// 축, 저주 표시
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
			
			if (item.getType() == 6 && item.getItemType() == L1ItemType.NORMAL) {
				writeD(0x33);
			} else if (item.getType() == 15 && item.getItemType() == L1ItemType.ARMOR) {
				writeD(0x02);
			} else if (item.getType() == 8 && item.getItemType() == L1ItemType.NORMAL) {
				writeD(0x06);
			} else {
				writeD(item.get_interaction_type());
			}
			
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
				writeBytesWithLength(dummy.getStatusBytesShopItem(pc, npcId));
			}
		}
		writeH(7);// 표기 nameid
		dummy = null;
	}

	public byte[] getContent() throws IOException {
		return this._bao.toByteArray();
	}
}
