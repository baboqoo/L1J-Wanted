package l1j.server.server.construct;

import java.util.concurrent.ConcurrentHashMap;

public enum L1BaseStatus {
	CROWN(			0,	13,	9,	9,	11,	11,	11),
	KNIGHT(			1,	16,	12,	8,	9,	16,	10),
	ELF(			2,	10,	12,	12,	12,	12,	9),
	WIZARD(			3,	8,	7,	14,	14,	12,	8),
	DARKELF(		4,	15,	12,	11,	10,	12,	7),
	DRAGONKNIGHT(	5,	13,	11,	10,	10,	14,	8),
	ILLUSIONIST(	6,	9,	10,	12,	14,	12,	8),
	WARRIOR(		7,	16,	13,	10,	7,	16,	9),
	FENCER(			8,	16,	13,	11,	11,	15,	5),
	LANCER(			9,	14,	12,	9,	12,	16,	6),
	;
	private int _class_id;
	private int _base_str;
	private int _base_dex;
	private int _base_int;
	private int _base_wis;
	private int _base_con;
	private int _base_cha;
	L1BaseStatus(int class_id, int base_str, int base_dex, int base_int, int base_wis, int base_con, int base_cha) {
		_class_id = class_id;
		_base_str = base_str;
		_base_dex = base_dex;
		_base_int = base_int;
		_base_wis = base_wis;
		_base_con = base_con;
		_base_cha = base_cha;
	}
	public int get_class_id() {
		return _class_id;
	}
	public int get_base_str() {
		return _base_str;
	}
	public int get_base_dex() {
		return _base_dex;
	}
	public int get_base_int() {
		return _base_int;
	}
	public int get_base_wis() {
		return _base_wis;
	}
	public int get_base_con() {
		return _base_con;
	}
	public int get_base_cha() {
		return _base_cha;
	}
	
	private static final ConcurrentHashMap<Integer, L1BaseStatus> DATA;
	static {
		DATA = new ConcurrentHashMap<Integer, L1BaseStatus>();
		for (L1BaseStatus val : L1BaseStatus.values()) {
			DATA.put(val._class_id, val);
		}
	}
	public static L1BaseStatus fromInt(int class_id) {
		return DATA.get(class_id);
	}
}

