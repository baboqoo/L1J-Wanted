package l1j.server.server.serverpackets.shop;

import java.util.ArrayList;
import java.util.List;

import l1j.server.server.Opcodes;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.NpcShopTable;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1PrivateShopBuyList;
import l1j.server.server.templates.L1PrivateShopSellList;
import l1j.server.server.templates.L1ShopItem;

public class S_PersonalShop extends ServerBasePacket {
	private static final int PERSONAL_SHOP = 0x0407;

	public S_PersonalShop(L1PcInstance pc, L1PcInstance seller, ePersonalShopType type) {
		try {
			if (seller == null) {
				return;
			}
			write_init();
			write_head(type, seller.getId());
			
			ArrayList<?> list = null;
			int size = 0;
			L1ItemInstance item = null;
			switch (type) {
			case TRADE_BUY:
				list = seller.getSellList();
				size = list.size();
				if (size == 0) {
					pc.sendPackets(L1ServerMessage.sm908);// 등록 아이템 없음
					return;
				}
				pc.setPartnersPrivateShopItemCount(size);
				write_size(size);
				
				L1PrivateShopSellList pss = null;
				for (int index = 0; index < size; index++) {
					pss = (L1PrivateShopSellList) list.get(index);
					item = seller.getInventory().getItem(pss.getItemObjectId());
					if (item == null) {
						continue;
					}
					write_trade_item_info(pc, item, index, pss.getSellTotalCount() - pss.getSellCount(), pss.getSellPrice());
				}
				break;
			case TRADE_SELL:
				list = seller.getBuyList();
				size = list.size();
				if (size == 0) {
					pc.sendPackets(L1ServerMessage.sm908);// 등록 아이템 없음
					return;
				}
				pc.setPartnersPrivateShopItemCount(size);
				write_size(size);
				
				L1PrivateShopBuyList psb = null;
				for (int index = 0; index < size; index++) {
					psb = (L1PrivateShopBuyList) list.get(index);
					item = seller.getInventory().getItem(psb.getItemObjectId());
					if (item == null) {
						continue;
					}
					write_trade_item_info(pc, item, index, psb.getBuyTotalCount() - psb.getBuyCount(), psb.getBuyPrice());
				}
				break;
			}
			writeH(0x00);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 영자 상점 전용
	 * @param pc
	 * @param seller
	 * @param type
	 */
	public S_PersonalShop(L1PcInstance pc, L1NpcInstance seller, ePersonalShopType type) {
		try {
			if (seller == null) {
				return;
			}
			write_init();
			write_head(type, seller.getId());
			
			int npcId = 0;
			L1Shop shop = null;
			List<L1ShopItem> shopItems = null;
			int size = 0;
			L1ItemInstance item	= null;
			L1Item temp;

			//System.out.println("Using the personal shop");

			switch (type) {
			case TRADE_BUY:
				npcId	= seller.getNpcTemplate().getNpcId();
				if (seller instanceof L1NpcShopInstance) {
					shop = NpcShopTable.getInstance().get(npcId);
				} else {
					shop = ShopTable.getInstance().get(npcId);
				}
				
				if (shop == null) {
					//System.out.println("엔피시 상점 오류 : 번호" + seller.getNpcId() + " x :" + seller.getX() + " y :" + seller.getY() + " map :" + seller.getMapId());
					System.out.println("NPC Shop Error - NPC ID: " + seller.getNpcId() + " X: " + seller.getX() + " Y: " + seller.getY() + " Map: " + seller.getMapId());
					return;
				}
				shopItems = shop.getSellingItems();
				size = shopItems.size();
				if (size == 0) {
					pc.sendPackets(L1ServerMessage.sm908);// 등록 아이템 없음
					return;
				}
				break;
			case TRADE_SELL:
				npcId = seller.getNpcTemplate().getNpcId();
				if (seller instanceof L1NpcShopInstance) {
					shop = NpcShopTable.getInstance().get(npcId);
				} else {
					shop = ShopTable.getInstance().get(npcId);
				}
				if (shop == null) {
					return;
				}
				shopItems = shop.getBuyingItems();
				size = shopItems.size();
				if (size == 0) {
					pc.sendPackets(L1ServerMessage.sm908);// 등록 아이템 없음
					return;
				}
				break;
			}
			write_size(size);
			pc.setPartnersPrivateShopItemCount(size);

			for (int index = 0; index < size; index++) {
				L1ShopItem shopItem = shopItems.get(index);
				temp = shopItem.getItem();
				item = ItemTable.getInstance().createItem(temp.getItemId());
				if (item == null) {
					continue;
				}
				item.setCount(shopItem.getCount());
				item.setIdentified(true);
				item.setEnchantLevel(shopItem.getEnchant());
				int count = item.getCount();
				if (npcId == 81008 || npcId == 200004 || npcId == 900171 || npcId == 70010 || npcId == 200002
						|| npcId == 5000009 || npcId == 5000007 || npcId == 5000008 || npcId == 5000010
						|| npcId >= 5000001 && npcId <= 5000004 || npcId >= 5000011 && npcId <= 5000018) {
					count = 100;
				}
				write_trade_item_info(pc, item, index, count, shopItem.getPrice());
			}
			
			writeH(0x00);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(PERSONAL_SHOP);
	}
	
	void write_head(ePersonalShopType type, int seller_id) {
		writeRaw(0x08);// type
		writeRaw(type.value);
		
		writeRaw(0x10);// seller_id
		writeBit(seller_id);
	}
	
	void write_size(int size) {
		writeRaw(0x18);// size
		writeBit(size);
	}
	
	void write_trade_item_info(L1PcInstance pc, L1ItemInstance item, int index, int count, int price) {
		byte[] item_info_bytes = item.getItemInfo(pc, count);
		
		int length = getBitSize(item_info_bytes.length) + item_info_bytes.length + getBitSize(count) + getBitSize(price) + 5;
		writeRaw(0x22);// item_list
		writeBit(length);
		
		writeRaw(0x08);// index
		writeRaw(index);
		
		writeRaw(0x10);// count
		writeBit(count);
		
		writeRaw(0x18);// price
		writeBit(price);
		
		writeRaw(0x22);// item_info
		writeBytesWithLength(item_info_bytes);
		
		// 0x28 trade_info
		// 0x30 buy_count
		// 0x38 potential_bonus_id
		// 0x40 curreny_type
		// 0x48 seller_id
		// 0x50 store_time
	}
	
	public enum ePersonalShopType{
		TRADE_BUY(0),
		TRADE_SELL(1),
		;
		private int value;
		ePersonalShopType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(ePersonalShopType v){
			return value == v.value;
		}
		public static ePersonalShopType fromInt(int i){
			switch(i){
			case 0:
				return TRADE_BUY;
			case 1:
				return TRADE_SELL;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments ePersonalShopType, %d", i));
			}
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}

