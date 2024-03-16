package l1j.server.IndunSystem.dragonraid;

public enum DragonRaildType {
	ANTARAS(1180),
	FAFURION(1181),
	RINDVIOR(1182),
	VALAKAS(1183),
	HALPAS(1191),
	;
	protected int _mapId;
	DragonRaildType(int _mapId) {
		this._mapId = _mapId;
	}
	
	public static final DragonRaildType[] ARRAY = DragonRaildType.values();
}

