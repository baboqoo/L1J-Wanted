package l1j.server.server.model;

public class L1ObjectAmount<T> {
	private final T _obj;
	private final int _amount;
	private final int _en;
	private final int _chance;

	public L1ObjectAmount(T obj, int amount, int en, int chance) {
		_obj = obj;
		_amount = amount;
		_en = en;
		_chance = chance;
	}

	public T getObject() {
		return _obj;
	}

	public int getAmount() {
		return _amount;
	}

	public int getEnchant() {
		return _en;
	}
	
	public int getChance() {
		return _chance;
	}
}

