package l1j.server.server.templates;

import java.util.Calendar;

public class L1AuctionBoard {
	public L1AuctionBoard() {}

	private int _houseId;

	public int getHouseId() {
		return _houseId;
	}

	public void setHouseId(int i) {
		_houseId = i;
	}

	private String _houseName;

	public String getHouseName() {
		return _houseName;
	}

	public void setHouseName(String s) {
		_houseName = s;
	}

	private int _houseArea;

	public int getHouseArea() {
		return _houseArea;
	}

	public void setHouseArea(int i) {
		_houseArea = i;
	}

	private Calendar _deadline;

	public Calendar getDeadline() {
		return _deadline;
	}

	public void setDeadline(Calendar i) {
		_deadline = i;
	}

	private int _price;

	public int getPrice() {
		return _price;
	}

	public void setPrice(int i) {
		_price = i;
	}

	private String _location;

	public String getLocation() {
		return _location;
	}

	public void setLocation(String s) {
		_location = s;
	}

	private String _oldOwner;

	public String getOldOwner() {
		return _oldOwner;
	}

	public void setOldOwner(String s) {
		_oldOwner = s;
	}

	private int _oldOwnerId;

	public int getOldOwnerId() {
		return _oldOwnerId;
	}

	public void setOldOwnerId(int i) {
		_oldOwnerId = i;
	}

	private String _bidder;

	public String getBidder() {
		return _bidder;
	}

	public void setBidder(String s) {
		_bidder = s;
	}

	private int _bidderId;

	public int getBidderId() {
		return _bidderId;
	}

	public void setBidderId(int i) {
		_bidderId = i;
	}

}
