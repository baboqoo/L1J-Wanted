package l1j.server.server.serverpackets.shop;

public enum SHOP_TYPE {
	NORMAL(0),
	PLEDGE(1);
	private int _value;
	SHOP_TYPE(int val) {
		_value = val;
	}
	public int toInt() {
		return _value;
	}
}

