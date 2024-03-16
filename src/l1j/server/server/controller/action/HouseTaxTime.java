package l1j.server.server.controller.action;

import java.util.Calendar;
import java.util.TimeZone;

import l1j.server.Config;
import l1j.server.server.datatables.AuctionBoardTable;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.datatables.HouseTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.templates.L1AuctionBoard;
import l1j.server.server.templates.L1House;
import l1j.server.server.utils.StringUtil;

/**
 * 세금 지불 컨트롤러
 * @author LinOffice
 */
public class HouseTaxTime implements ControllerInterface {
	private static class newInstance {
		public static final HouseTaxTime INSTANCE = new HouseTaxTime();
	}
	public static HouseTaxTime getInstance() {
		return newInstance.INSTANCE;
	}
	private HouseTaxTime(){}

	@Override
	public void execute() {
		checkTaxDeadline();
	}
	
	@Override
	public void execute(l1j.server.server.model.Instance.L1PcInstance pc) {
	}
	
	public Calendar getRealTime() {
		TimeZone tz = TimeZone.getTimeZone(Config.SERVER.TIME_ZONE);
		Calendar cal = Calendar.getInstance(tz);
		return cal;
	}

	private void checkTaxDeadline() {
		for (L1House house : HouseTable.getInstance().getHouseTableList()) {
			if (!house.isOnSale()) { // 경매중의 아지트는 체크하지 않는다
				if (house.getTaxDeadline().before(getRealTime())) {
					sellHouse(house);
				}
			}
		}
	}

	private void sellHouse(L1House house) {
		AuctionBoardTable boardTable = new AuctionBoardTable();
		L1AuctionBoard board = new L1AuctionBoard();
		if (board != null) {
			// 경매 게시판에 신규 기입
			int houseId = house.getHouseId();
			board.setHouseId(houseId);
			board.setHouseName(house.getHouseName());
			board.setHouseArea(house.getHouseArea());
			TimeZone tz = TimeZone.getTimeZone(Config.SERVER.TIME_ZONE);
			Calendar cal = Calendar.getInstance(tz);
			cal.add(Calendar.DATE, 5); // 5일 후
			cal.set(Calendar.MINUTE, 0); // 분 , 초는 잘라서 버림
			cal.set(Calendar.SECOND, 0);
			board.setDeadline(cal);
			board.setPrice(100000);
			board.setLocation(house.getLocation());
			board.setOldOwner(StringUtil.EmptyString);
			board.setOldOwnerId(0);
			board.setBidder(StringUtil.EmptyString);
			board.setBidderId(0);
			boardTable.insertAuctionBoard(board);
			house.setOnSale(true); // 경매중으로 설정
			house.setPurchaseBasement(false); // 지하 아지트미구입으로 설정
			cal.add(Calendar.DATE, Config.PLEDGE.HOUSE_TAX_INTERVAL_DAY);
			house.setTaxDeadline(cal);
			HouseTable.getInstance().updateHouse(house); // DB에 기입해
			// 이전의 소유자의 아지트를 지운다
			for (L1Clan clan : L1World.getInstance().getAllClans()) {
				if (clan.getHouseId() == houseId) {
					clan.setHouseId(0);
					ClanTable.getInstance().updateClan(clan);
				}
			}
		}
	}

}

