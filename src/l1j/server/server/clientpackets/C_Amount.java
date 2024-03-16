package l1j.server.server.clientpackets;

import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.TimeZone;

import l1j.server.Config;
import l1j.server.GameSystem.inn.InnHandler;
import l1j.server.GameSystem.inn.InnType;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.datatables.AuctionBoardTable;
import l1j.server.server.datatables.HouseTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.datatables.NpcActionTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1NpcTalkData;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcHtml;
import l1j.server.server.model.npc.action.L1NpcAction;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.storage.CharactersItemStorage;
import l1j.server.server.templates.L1AuctionBoard;
import l1j.server.server.templates.L1House;
import l1j.server.server.utils.StringUtil;

public class C_Amount extends ClientBasePacket {
	private static final String C_AMOUNT = "[C] C_Amount";

	public C_Amount(byte[] decrypt, GameClient client) throws Exception {
		super(decrypt);
		if (client == null) {
			return;
		}
		L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		try {
			int objectId	= readD();
			int amount		= readD();
			if (amount <= 0) {
				return;
			}
			
			readC();
			String s	= readS();
	
			L1NpcInstance npc = (L1NpcInstance) L1World.getInstance().findObject(objectId);	
			if (npc == null) {
				return;
			}
	
			String s1 = StringUtil.EmptyString;
			String s2 = StringUtil.EmptyString;
			try {
				StringTokenizer stringtokenizer = new StringTokenizer(s);
				s1 = stringtokenizer.nextToken();
				s2 = stringtokenizer.nextToken();
			} catch (NoSuchElementException e) {
				s1 = StringUtil.EmptyString;
				s2 = StringUtil.EmptyString;
			}

			L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(npc.getNpcId());
			String htmlid = "inn";
			if (talking != null)
				htmlid = talking.getNormalAction();
			
			if (s.equalsIgnoreCase(htmlid + "2")) {// 방 대여
				InnHandler.getInstance().INNStart(pc, objectId, npc.getNpcId(), InnType.ROOM, amount);
				return;
			}
			if (s.equalsIgnoreCase(htmlid + "12")) {// 홀 대여
				InnHandler.getInstance().INNStart(pc, objectId, npc.getNpcId(), InnType.HALL, amount);
				return;
			}
			
			htmlid = "agapply";
			if (s1.equalsIgnoreCase(htmlid)) {// 경매에 입찰했을 경우
				String pcName = pc.getName();
				AuctionBoardTable boardTable = new AuctionBoardTable();
				for (L1AuctionBoard board : boardTable.getAuctionBoardTableList()) {
					if (pcName.equalsIgnoreCase(board.getBidder())) {
						pc.sendPackets(L1ServerMessage.sm523);// 벌써 다른 집의 경매에 참가하고 있습니다.
						return;
					}
				}
				int houseId = Integer.valueOf(s2);
				L1AuctionBoard board = boardTable.getAuctionBoardTable(houseId);
				if (board != null) {
					int nowPrice = board.getPrice();
				    if (nowPrice <= 0 || amount < nowPrice) {
				    	return;
				    }
				    if (pc.getInventory().findItemId(L1ItemId.ADENA).getCount() < amount) {
				    	return;
				    }
					int nowBidderId = board.getBidderId();
					L1PcInstance bidPc = (L1PcInstance) L1World.getInstance().findObject(nowBidderId);					
					if (bidPc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), nowPrice) != L1Inventory.OK) return;

					if (pc.getInventory().consumeItem(L1ItemId.ADENA, amount)) {
						// 경매 게시판을 갱신
						board.setPrice(amount);
						board.setBidder(pcName);
						board.setBidderId(pc.getId());
						boardTable.updateAuctionBoard(board);
						if (nowBidderId != 0) {
							// 입찰자에게 아데나를 환불							
							if (bidPc != null) {// 온라인중
								bidPc.getInventory().storeItem(L1ItemId.ADENA, nowPrice);
								// 당신이 제시된 금액보다 좀 더 비싼 금액을 제시한 (분)편이 나타났기 때문에, 유감스럽지만 입찰에 실패했습니다. %n
								// 당신이 경매에 맡긴%0아데나를 답례합니다. %n 감사합니다. %n%n
								bidPc.sendPackets(new S_ServerMessage(525, String.valueOf(nowPrice)), true);
							} else {// 오프 라인중
								L1ItemInstance item = ItemTable.getInstance().createItem(L1ItemId.ADENA);
								item.setCount(nowPrice);
								CharactersItemStorage storage = CharactersItemStorage.create();
								storage.storeItem(nowBidderId, item);
							}
						}
					} else {
						pc.sendPackets(L1ServerMessage.sm189); // \f1아데나가 부족합니다.
					}
				}
				return;
			}
			
			htmlid = "agsell";
			if (s1.equalsIgnoreCase(htmlid)) {// 가를 팔았을 경우
				int houseId = Integer.valueOf(s2);
				AuctionBoardTable boardTable = new AuctionBoardTable();
				L1AuctionBoard board = new L1AuctionBoard();
	
				L1Clan ownerClan = null;
				for (L1Clan clan : L1World.getInstance().getAllClans()) {
					if (clan.getLeaderId() == pc.getId()) {
						ownerClan = clan;
						break;
					}
				}
				
				if (ownerClan == null) {
					return;
				}
				L1House house = HouseTable.getInstance().getHouseTable(houseId);
				if (house == null || ownerClan.getHouseId() != house.getHouseId()) {
					return;
				}
				if (pc.getInventory().findItemId(L1ItemId.ADENA).getCount() < amount) {
					pc.sendPackets(L1SystemMessage.HOUSE_PRICE_SETTING_FAIL);
					return;
				}
	
				if (board != null) {
					// 경매 게시판에 신규 기입
					board.setHouseId(houseId);
					board.setHouseName(house.getHouseName());
					board.setHouseArea(house.getHouseArea());
					TimeZone tz = TimeZone.getTimeZone(Config.SERVER.TIME_ZONE);
					Calendar cal = Calendar.getInstance(tz);
					cal.add(Calendar.DATE, 1); // 5일 후
					cal.set(Calendar.MINUTE, 0); // 분 , 초는 잘라서 버림
					cal.set(Calendar.SECOND, 0);
					board.setDeadline(cal);
					board.setPrice(amount);
					board.setLocation(house.getLocation());
					board.setOldOwner(pc.getName());
					board.setOldOwnerId(pc.getId());
					board.setBidder(StringUtil.EmptyString);
					board.setBidderId(0);
					boardTable.insertAuctionBoard(board);
	
					house.setOnSale(true);  // 경매중으로 설정
					house.setPurchaseBasement(false); // 지하 아지트미구입으로 설정
					HouseTable.getInstance().updateHouse(house); // DB에 기입해
				}
				return;
			}
			
		    L1NpcAction action = NpcActionTable.getInstance().get(s, pc, npc);
		    if (action != null) {
				L1NpcHtml result = action.executeWithAmount(s, pc, npc, amount);
				if (result != null) {
					if (pc.isGm())
						pc.sendPackets(new S_SystemMessage("Dialog " + result), true);													
					pc.sendPackets(new S_NPCTalkReturn(npc.getId(), result), true);
				}
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			clear();
		}
	}

	@Override
	public String getType() {
		return C_AMOUNT;
	}
}

