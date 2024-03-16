package l1j.server.server.serverpackets.shop;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.datatables.ShopTable.L1ShopInfo;
import l1j.server.server.datatables.ShopTable.ShopType;
import l1j.server.server.model.L1Object;
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

public class S_PremiumShopSellList extends ServerBasePacket {
	//private static final String NCOIN_STRING = "\\a5[소지 N코인] :\\aA %s원";
	private static final String NCOIN_STRING = "\\a5[Owned N-Coins]:\\aA %s Won"; // not necesary to translate becouse there is NO shop configured in DB with info and NCOIN Type, so this literal will not be used.
	private static final DecimalFormat DF = new DecimalFormat("#,###");
	
	// 아인하사드 포인트 2단계 개방 아이템 아이디
	private static final List<Integer> EIN_POINT_SHOP_SECOND_ITEMS = Arrays.asList(new Integer[] {
			420092, 43054, 43055, 43056, 43057
	});

	/** 가게의 물건 리스트를 표시한다. 캐릭터가 BUY 버튼을 눌렀을 때에 보낸다. */
	public S_PremiumShopSellList(int objId, L1PcInstance pc, L1ShopInfo info) {
		writeC(Opcodes.S_BUY_LIST);
		writeD(objId);
		writeC(SHOP_TYPE.NORMAL.toInt());// nomal type
		L1Object npcObj = L1World.getInstance().findObject(objId);
		if (!(npcObj instanceof L1NpcInstance)) {
			writeH(0);
			return;
		}
		int npcId					= ((L1NpcInstance) npcObj).getNpcTemplate().getNpcId();

		//System.out.println("Using the premium shop of the NPC: " + npcId);		
		
		L1Shop shop					= null;
		if (npcId == 100801) {
			shop = ShopTable.getInstance().get(pc.getType() + 20);//사이먼
		} else if (npcId == 100556) {
			shop = ShopTable.getInstance().get(pc.getType() + 30);//에반스
		} else {
			shop = ShopTable.getInstance().get(npcId);
		}
		List<L1ShopItem> shopItems	= null;
		List<L1ShopItem> passList	= new ArrayList<L1ShopItem>();
		
		try {
			shopItems = shop.getSellingItems();
		} catch (Exception e) {
		}
		
		if (shopItems != null) {
			/** 상점 리스트에서 감추기 **/
			boolean einPointSecondOpen = pc.getAbility().getStatBless() >= 25 && pc.getAbility().getStatLucky() >= 25 && pc.getAbility().getStatVital() >= 25;
			for (L1ShopItem si : shopItems) {
				if (npcId == 90079 && !einPointSecondOpen && EIN_POINT_SHOP_SECOND_ITEMS.contains(si.getItem().getItemId())) {// 아인하사드 포인트 상점 2단계 개방
					passList.add(si);
				} else if (npcId == 81026 && si.getItem().getItemId() == 2300210 && (pc.getInventory().getCollection().getStatus(L1CollectionModel.TYPE_HP_GRACE) || pc.getInventory().checkItem(2300210))) {// 체력의 가호 결정체
					passList.add(si);
				}
			}
			writeH(shopItems.size() - passList.size());
		} else {
			writeH(0);
			return;
		}

		// L1ItemInstance의 getStatusBytes를 이용하기 위해(때문에)
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
			int price = shopItem.getPrice();
			writeD(i);// INDEX
			
			if (info != null && info.getType() == ShopType.N_COIN && i == 0) {
				writeD(0x00);// 데스크id 추가
				writeH(1864);// gfxid
				writeD(price);
				writeS(String.format(NCOIN_STRING, DF.format(pc.getNcoin())));
				writeD(0x00);// use type
				writeD(87);
				writeD(0x00);
				writeC(0x00);
		        writeC(0x00);
			} else {
				try {
					writeD(shopItem.getItem().getItemNameId());
					writeH(shopItem.getItem().getIconId());
				} catch (Exception e) {
					//System.out.println("엔피시 상점 오류 엔피시 번호 :" + npcId);
					System.out.println("NPC Shop Error, NPC ID: " + npcId);
				}
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
					writeBytesWithLength(dummy.getStatusBytesShopItem(pc, npcId));
				}
			}
		}
		
		// 상점 화폐 표기 nameid CurrencyData.xml 참조
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
				writeH(-3);
				break;
			case N_COIN:
				writeH(-2);
				break;
			}
		} else if ((npcId >= 6100000 && npcId <= 6100013) || (npcId >= 6100042 && npcId <= 6100045) || (npcId >= 6100014 && npcId <= 6100027) || (npcId >= 6100028 && npcId <= 6100041)) {// 패키지상점
			writeC(255);
			writeC(255);
			writeC(0);
			writeC(0);
		} else {// 그외 깃털로 표시 
			writeH(2671);// 픽시의 깃털
		}
	}
	// 21주년 기념코인:27613, NCoin:-3(254 255), 엽전:29886

	@Override
	public byte[] getContent() throws IOException {
		return _bao.toByteArray();
	}
}

