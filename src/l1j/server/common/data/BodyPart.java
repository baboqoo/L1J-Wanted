package l1j.server.common.data;

public enum BodyPart{
	NONE(0),
	HEAD(1),
	TORSO(2),
	SHIRT(4),
	CLOAK(8),
	LEG(16),
	FOOT(32),
	L_HAND(64),
	R_HAND(128),
	L_HOLD(256),
	R_HOLD(512),
	L_FINGER(1024),
	R_FINGER(2048),
	NECK(4096),
	WAIST(8192),
	EAR(16384),
	R_SHOULDER(32768),
	L_SHOULDER(65536),
	BACK(131072),
	R_WRIST(262144),
	L_WRIST(524288),
	RUNE(1048576),
	EXT_SLOTS(2097152),
	BP_HERALDRY(4194304),
	BP_PAULDRON(8388608),
	BP_INSIGNIA(16777216),
	BP_PENDANT(33554432),
	BODYPART_ALL(-1),
	;
	private int value;
	BodyPart(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(BodyPart v){
		return value == v.value;
	}
	public static BodyPart fromInt(int i){
		switch(i){
		case 0:
			return NONE;
		case 1:
			return HEAD;
		case 2:
			return TORSO;
		case 4:
			return SHIRT;
		case 8:
			return CLOAK;
		case 16:
			return LEG;
		case 32:
			return FOOT;
		case 64:
			return L_HAND;
		case 128:
			return R_HAND;
		case 256:
			return L_HOLD;
		case 512:
			return R_HOLD;
		case 1024:
			return L_FINGER;
		case 2048:
			return R_FINGER;
		case 4096:
			return NECK;
		case 8192:
			return WAIST;
		case 16384:
			return EAR;
		case 32768:
			return R_SHOULDER;
		case 65536:
			return L_SHOULDER;
		case 131072:
			return BACK;
		case 262144:
			return R_WRIST;
		case 524288:
			return L_WRIST;
		case 1048576:
			return RUNE;
		case 2097152:
			return EXT_SLOTS;
		case 4194304:
			return BP_HERALDRY;
		case 8388608:
			return BP_PAULDRON;
		case 16777216:
			return BP_INSIGNIA;
		case 33554432:
			return BP_PENDANT;
		case -1:
			return BODYPART_ALL;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments BodyPart, %d", i));
		}
	}
	
}

