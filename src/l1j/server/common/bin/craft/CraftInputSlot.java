package l1j.server.common.bin.craft;

public class CraftInputSlot {
	private int _slot_id;
	private int _name_id;
	private int _count;
	private int _enchant;
	private int _elemental_enchant_type;
	private int _elemental_enchant_value;
	private int _bless;
	
	public int get_slot_id() {
		return _slot_id;
	}
	public void set_slot_id(int _slot_id) {
		this._slot_id = _slot_id;
	}

	public int get_name_id() {
		return _name_id;
	}
	public void set_name_id(int _name_id) {
		this._name_id = _name_id;
	}

	public int get_count() {
		return _count;
	}
	public void set_count(int _count) {
		this._count = _count;
	}

	public int get_enchant() {
		return _enchant;
	}
	public void set_enchant(int _enchant) {
		this._enchant = _enchant;
	}

	public int get_elemental_enchant_type() {
		return _elemental_enchant_type;
	}
	public void set_elemental_enchant_type(int _elemental_enchant_type) {
		this._elemental_enchant_type = _elemental_enchant_type;
	}

	public int get_elemental_enchant_value() {
		return _elemental_enchant_value;
	}
	public void set_elemental_enchant_value(int _elemental_enchant_value) {
		this._elemental_enchant_value = _elemental_enchant_value;
	}

	public int get_bless() {
		return _bless;
	}
	public void set_bless(int _bless) {
		this._bless = _bless;
	}

	public boolean validation(CraftInputItem input) {
		if (input.get_name_id() != _name_id) {
			return false;
		}
		int bless = input.get_bless();
		if (bless != 3 && bless != _bless) {
			return false;
		}
		if (!input.get_all_enchants_allowed() && input.get_enchant() != _enchant) {
			return false;
		}
		int elementalType = input.get_elemental_enchant_type();
		if ((elementalType >= 1 && elementalType <= 4) && (elementalType != _elemental_enchant_type || input.get_elemental_enchant_value() != _elemental_enchant_value)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString(){
		return String.format(
				"slot_id : %d\r\nname_id : %d\r\ncount : %d\r\nenchant : %d\r\nelemental_enchant_type : %d\r\nelemental_enchant_value : %d\r\nbless : %d", 
				_slot_id, _name_id, _count, _enchant, _elemental_enchant_type, _elemental_enchant_value, _bless);
	}
}

