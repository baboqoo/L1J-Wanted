package l1j.server.server.controller.action;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.datatables.AuctionBoardTable;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.datatables.HouseTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.storage.CharactersItemStorage;
import l1j.server.server.templates.L1AuctionBoard;
import l1j.server.server.templates.L1House;

public class AuctionTime implements ControllerInterface {
	private static Logger _log = Logger.getLogger(AuctionTime.class.getName());
	private static class newInstance {
		public static final AuctionTime INSTANCE = new AuctionTime();
	}
	public static AuctionTime getInstance() {
		return newInstance.INSTANCE;
	}
	private AuctionTime(){}

	@Override
	public void execute() {
		checkAuctionDeadline();
	}
	
	@Override
	public void execute(L1PcInstance pc) {
	}
	
	public Calendar getRealTime() {
		TimeZone tz = TimeZone.getTimeZone(Config.SERVER.TIME_ZONE);
		Calendar cal = Calendar.getInstance(tz);
		return cal;
	}

	private void checkAuctionDeadline() {
		AuctionBoardTable boardTable = new AuctionBoardTable();
		for (L1AuctionBoard board : boardTable.getAuctionBoardTableList()) {
			if (board.getDeadline().before(getRealTime())) {
				endAuction(board);
			}
		}
	}

	private void endAuction(L1AuctionBoard board) {
		int houseId = board.getHouseId();
		int price = board.getPrice();
		int oldOwnerId = board.getOldOwnerId();
		String bidder = board.getBidder();
		int bidderId = board.getBidderId();

		if(oldOwnerId != 0 && bidderId != 0){ // 이전의 소유자 있어·낙찰자 있어
			L1PcInstance oldOwnerPc = (L1PcInstance) L1World.getInstance().findObject(oldOwnerId);
			int payPrice = (int) (price * 0.9);
			if (oldOwnerPc != null) { // 이전의 소유자가 온라인중
				oldOwnerPc.getInventory().storeItem(L1ItemId.ADENA, payPrice);
				// 당신이 소유하고 있던 집이 최종 가격%1아데나로 낙찰되었습니다.%n
				// 수수료10%%를 제외한 나머지의 금액%0아데나를 드립니다.%n 감사합니다.%n%n
				oldOwnerPc.sendPackets(new S_ServerMessage(527, String.valueOf(payPrice)), true);
			} else { // 이전의 소유자가 오프 라인중
				L1ItemInstance item = ItemTable.getInstance().createItem(L1ItemId.ADENA);
				item.setCount(payPrice);
				try {
					CharactersItemStorage storage = CharactersItemStorage.create();
					storage.storeItem(oldOwnerId, item);
				} catch (Exception e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
			}

			L1PcInstance bidderPc = (L1PcInstance) L1World.getInstance().findObject(bidderId);
			if (bidderPc != null) { // 낙찰자가 온라인중
				// 축하합니다.%n당신이 참가된 경매는 최종 가격%0아데나의 가격으로 낙찰되었습니다.%n
				// 모양이 구입하신 집은 곧바로 이용하실 수 있습니다.%n 감사합니다.%n%n
				bidderPc.sendPackets(new S_ServerMessage(524, String.valueOf(price), bidder), true);
			}
			deleteHouseInfo(houseId);
			setHouseInfo(houseId, bidderId);
			deleteNote(houseId);
		} else if (oldOwnerId == 0 && bidderId != 0) { // 이전의 소유자 없음·낙찰자 있어
			L1PcInstance bidderPc = (L1PcInstance) L1World.getInstance().findObject(bidderId);
			if (bidderPc != null) { // 낙찰자가 온라인중
				// 축하합니다.%n당신이 참가된 경매는 최종 가격%0아데나의 가격으로 낙찰되었습니다.%n
				// 모양이 구입하신 집은 곧바로 이용하실 수 있습니다.%n 감사합니다.%n%n
				bidderPc.sendPackets(new S_ServerMessage(524, String.valueOf(price), bidder), true);
			}
			setHouseInfo(houseId, bidderId);
			deleteNote(houseId);
		} else if (oldOwnerId != 0 && bidderId == 0) { // 이전의 소유자 있어·낙찰자 없음
			L1PcInstance oldOwnerPc = (L1PcInstance) L1World.getInstance()
					.findObject(oldOwnerId);
			if (oldOwnerPc != null) { // 이전의 소유자가 온라인중
				// 당신이 신청 하신 경매는, 경매 기간내에 제시한 금액 이상에서의 지불을 표명하는 것이 나타나지 않았기 (위해)때문에, 결국 삭제되었습니다.%n
				// 따라서, 소유권이 당신에게 되돌려진 것을 알려 드리겠습니다.%n 감사합니다.%n%n
				oldOwnerPc.sendPackets(new S_ServerMessage(528), true);
			}
			deleteNote(houseId);
		} else if (oldOwnerId == 0 && bidderId == 0) { // 이전의 소유자 없음·낙찰자 없음
			// 마감을 5일 후로 설정해 재차 경매에 붙인다
			Calendar cal = getRealTime();
			cal.add(Calendar.DATE, 1); 
			cal.set(Calendar.MINUTE, 0); 
			cal.set(Calendar.SECOND, 0);
			board.setDeadline(cal);
			AuctionBoardTable boardTable = new AuctionBoardTable();
			boardTable.updateAuctionBoard(board);
		}
	}

	/**
	 * 이전의 소유자의 아지트를 지운다
	 * 
	 * @param houseId
	 * 
	 * @return
	 */
	private void deleteHouseInfo(int houseId) {
		for (L1Clan clan : L1World.getInstance().getAllClans()) {
			if (clan.getHouseId() == houseId) {
				clan.setHouseId(0);
				ClanTable.getInstance().updateClan(clan);
			}
		}
	}

	/**
	 * 낙찰자의 아지트를 설정한다
	 * 
	 * @param houseId
	 *            bidderId
	 * 
	 * @return
	 */
	private void setHouseInfo(int houseId, int bidderId) {
		for (L1Clan clan : L1World.getInstance().getAllClans()) {
			if (clan.getLeaderId() == bidderId) {
				clan.setHouseId(houseId);
				ClanTable.getInstance().updateClan(clan);
				break;
			}
		}
	}

	/**
	 * 아지트의 경매 상태를 OFF로 설정해, 경매 게시판으로부터 지운다
	 * 
	 * @param houseId
	 * 
	 * @return
	 */
	private void deleteNote(int houseId) {
		// 아지트의 경매 상태를 OFF로 설정한다
		L1House house = HouseTable.getInstance().getHouseTable(houseId);
		house.setOnSale(false);
		Calendar cal = getRealTime();
		cal.add(Calendar.DATE, Config.PLEDGE.HOUSE_TAX_INTERVAL_DAY);
		cal.set(Calendar.MINUTE, 0); // 분 , 초는 잘라서 버림
		cal.set(Calendar.SECOND, 0);
		house.setTaxDeadline(cal);
		HouseTable.getInstance().updateHouse(house);
		
		// 경매 게시판으로부터 지운다
		AuctionBoardTable boardTable = new AuctionBoardTable();
		boardTable.deleteAuctionBoard(houseId);
	}

}

