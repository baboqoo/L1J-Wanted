package l1j.server.common.data;

public enum ExtendedWeaponType{
	NONE(-1),
	WEAPON_EX_ONEHAND_SWORD(0),
	WEAPON_EX_AXE(1),
	WEAPON_EX_BOW(2),
	WEAPON_EX_SPEAR(3),
	WEAPON_EX_STAFF(4),
	WEAPON_EX_DAGGER(5),
	WEAPON_EX_LARGE_SWORD(6),
	WEAPON_EX_DOUBLE_SWORD(7),
	WEAPON_EX_CRAW(8),
	WEAPON_EX_GAUNTLET(9),
	WEAPON_EX_CHAIN_SWORD(10),
	WEAPON_EX_DOUBLE_AXE(11),
	WEAPON_EX_KIRINGKU(12),
	WEAPON_EX_NOT_EQUIPPED(13),
	;
	private int value;
	ExtendedWeaponType(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(ExtendedWeaponType v){
		return value == v.value;
	}
	public static ExtendedWeaponType fromInt(int i){
		switch(i){
		case -1:
			return NONE;
		case 0:
			return WEAPON_EX_ONEHAND_SWORD;
		case 1:
			return WEAPON_EX_AXE;
		case 2:
			return WEAPON_EX_BOW;
		case 3:
			return WEAPON_EX_SPEAR;
		case 4:
			return WEAPON_EX_STAFF;
		case 5:
			return WEAPON_EX_DAGGER;
		case 6:
			return WEAPON_EX_LARGE_SWORD;
		case 7:
			return WEAPON_EX_DOUBLE_SWORD;
		case 8:
			return WEAPON_EX_CRAW;
		case 9:
			return WEAPON_EX_GAUNTLET;
		case 10:
			return WEAPON_EX_CHAIN_SWORD;
		case 11:
			return WEAPON_EX_DOUBLE_AXE;
		case 12:
			return WEAPON_EX_KIRINGKU;
		case 13:
			return WEAPON_EX_NOT_EQUIPPED;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments ExtendedWeaponType, %d", i));
		}
	}
	
}

