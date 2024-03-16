package l1j.server.server.model.poison;

public enum L1PoisonType {
	NONE, DAMAGE, PARALYSIS, SILENCE,
	;
	public static L1PoisonType fromString(String val) {
		switch (val) {
		case "NONE":
			return NONE;
		case "DAMAGE":
			return DAMAGE;
		case "PARALYSIS":
			return PARALYSIS;
		case "SILENCE":
			return SILENCE;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments L1PoisonType, %s", val));
		}
	}
}

