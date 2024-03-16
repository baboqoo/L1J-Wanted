package l1j.server.GameSystem.inn;

public enum InnLoc {
	TALKING_ISLAND_ROOM(		new int[] { 32746, 32803 }		),
	TALKING_ISLAND_HALL(		new int[] { 32744, 32808 }		),
	GLUDIO_ROOM(				new int[] { 32744, 32803 }		),
	GLUDIO_HALL(				new int[] { 32745, 32807 }		),
	GIRAN_ROOM(					new int[] { 32745, 32803 }		),
	GIRAN_HALL(					new int[] { 32745, 32807 }		),
	ADEN_ROOM(					new int[] { 32745, 32803 }		),
	ADEN_HALL(					new int[] { 32745, 32807 }		),
	OREN_ROOM(					new int[] { 32745, 32803 }		),
	OREN_HALL(					new int[] { 32745, 32807 }		),
	WINDAWOOD_ROOM(				new int[] { 32745, 32803 }		),
	WINDAWOOD_HALL(				new int[] { 32745, 32807 }		),
	SILVER_KNIGHT_ROOM(			new int[] { 32745, 32803 }		),
	SILVER_KNIGHT_HALL(			new int[] { 32745, 32807 }		),
	HEINE_ROOM(					new int[] { 32745, 32803 }		),
	HEINE_HALL(					new int[] { 32745, 32807 }		),
	RUUN_ROOM(					new int[] { 32736, 32795 }		),
	RUUN_HALL(					new int[] { 32729, 32791 }		),
	
	TALKING_ISLAND_OUT(			new int[] { 32598, 32931, 0 }	),
	GLUDIO_OUT(					new int[] { 32631, 32761, 4 }	),
	GIRAN_OUT(					new int[] { 33437, 32788, 4 }	),
	ADEN_OUT(					new int[] { 33985, 33313, 4 }	),
	OREN_OUT(					new int[] { 34067, 32255, 4 }	),
	WINDAWOOD_OUT(				new int[] { 32630, 33165, 4 }	),
	SILVER_KNIGHT_OUT(			new int[] { 33112, 33377, 4 }	),
	HEINE_OUT(					new int[] { 33604, 33276, 4 }	),
	RUUN_OUT(					new int[] { 34426, 32199, 4 }	),
	
	TALKING_ISLAND_INN_CHECK(	new int[] { 32600, 32931, 0 }	),
	GLUDIO_INN_CHECK(			new int[] { 32632, 32761, 4 }	),
	GIRAN_INN_CHECK(			new int[] { 33437, 32786, 4 }	),
	ADEN_INN_CHECK(				new int[] { 33986, 33312, 4 }	),
	OREN_INN_CHECK(				new int[] { 34067, 32254, 4 }	),
	WINDAWOOD_INN_CHECK(		new int[] { 32632, 33165, 4 }	),
	SILVER_KNIGHT_INN_CHECK(	new int[] { 33112, 33376, 4 }	),
	HEINE_INN_CHECK(			new int[] { 33604, 33274, 4 }	),
	RUUN_INN_CHECK(				new int[] { 34426, 32198, 4 }	),
	;
	private int[] _loc;
	InnLoc(int[] loc){
		_loc = loc;
	}
	public int[] getLoc(){
		return _loc;
	}
	
	public static void init(){}
}

