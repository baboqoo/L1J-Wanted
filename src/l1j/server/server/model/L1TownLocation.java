package l1j.server.server.model;

import l1j.server.server.datatables.TownTable;
import l1j.server.server.templates.L1Town;
import l1j.server.server.types.Point;
import l1j.server.server.utils.CommonUtil;

public class L1TownLocation {
	// town_id
	public static final int TOWNID_TALKING_ISLAND		= 1;// 말하는 섬
	public static final int TOWNID_SILVER_KNIGHT		= 2;// 은기사
	public static final int TOWNID_GLUDIO				= 3;// 글루디오
	public static final int TOWNID_ORCISH_FOREST		= 4;// 오크
	public static final int TOWNID_WINDAWOOD			= 5;// 윈다우드
	public static final int TOWNID_KENT					= 6;// 켄트
	public static final int TOWNID_GIRAN				= 7;// 기란
	public static final int TOWNID_HEINE				= 8;// 하이네
	public static final int TOWNID_WERLDAN				= 9;// 웰던
	public static final int TOWNID_OREN					= 10;// 오렌
	public static final int TOWNID_ELVEN_FOREST			= 11;// 요정 숲
	public static final int TOWNID_ADEN					= 12;// 아덴
	public static final int TOWNID_SILENT_CAVERN		= 13;//	침묵의 동굴
	public static final int TOWNID_BEHEMOTH				= -1;//	배히모스
	public static final int TOWNID_SILVERIA				= -2;//	실베리아
	public static final int TOWNID_OUM_DUNGEON			= 14;//	오움던전
	public static final int TOWNID_RESISTANCE			= 15;//	대공동
	public static final int TOWNID_PIRATE_ISLAND		= 16;//	해적섬
	public static final int TOWNID_RECLUSE_VILLAGE		= 17;//	저항군
	public static final int TOWNID_HIDDEN_VALLEY		= 18;//	숨겨진계곡
	public static final int TOWNID_CLAUDIA				= 19;//	클라우디아
	public static final int TOWNID_REDSOLDER			= 20;//	붉은기사단 훈련소
	public static final int FORGOTTEN_ISLAND			= 21;//	잊혀진섬 대기실
	public static final int TOWNID_SKYGARDEN			= 22;//	수상한 하늘 정원
	public static final int TOWNID_LUUN					= 36;//	루운성 마을
	
	public static final int WOLRDWAR_GIRAN				= 23;//	월드공성전 기란
	public static final int WOLRDWAR_ORC				= 24;//	월드공성전 오크
	public static final int WOLRDWAR_HEINE				= 25;//	월드공성전 하이네
	
	public static final int ERJABE_CROWN				= 26;//	은폐된 주둔지 군주
	public static final int ERJABE_KNIGHT				= 27;//	은폐된 주둔지 기사
	public static final int ERJABE_ELF					= 28;//	은폐된 주둔지 요정
	public static final int ERJABE_MAGE					= 29;//	은폐된 주둔지 법사
	public static final int ERJABE_DARKELF				= 30;//	은폐된 주둔지 다크엘프
	public static final int ERJABE_DRAGONKNIGHT			= 31;//	은폐된 주둔지 용기사
	public static final int ERJABE_ILLOSIONIST			= 32;//	은폐된 주둔지 환술사
	public static final int ERJABE_WARRIOR				= 33;//	은폐된 주둔지 전사
	public static final int ERJABE_FENCER				= 34;//	은폐된 주둔지 검사
	public static final int ERJABE_LANCER				= 35;//	은폐된 주둔지 창기사
	
	public static final int WOLRDWAR_HEINE_BASE			= 40;//	하이네 수호탑 점령전
	public static final int WOLRDWAR_HEINE_RED			= 41;//	하이네 수호탑 점령전 붉은 기사단 진영
	public static final int WOLRDWAR_HEINE_BLACK		= 42;//	하이네 수호탑 점령전 검은 기사단 진영
	public static final int WOLRDWAR_HEINE_GOLD			= 43;//	하이네 수호탑 점령전 황금 성단 진영
	public static final int WOLRDWAR_HEINE_EVA			= 44;//	하이네 수호탑 점령전 에바의 정원
	
	public static final int ABANDON_INTER_WEST_BASE		= 45;// 버림받은 자들의 땅(인터) 서쪽
	public static final int ABANDON_INTER_EAST_BASE		= 46;// 버림받은 자들의 땅(인터) 동쪽
	
	public static final int WOLRDWAR_WINDAWOOD_BASE		= 47;//	윈다우드 수호탑 점령전
	public static final int WOLRDWAR_WINDAWOOD_RED		= 48;//	윈다우드 수호탑 점령전 붉은 기사단 진영
	public static final int WOLRDWAR_WINDAWOOD_BLACK	= 49;//	윈다우드 수호탑 점령전 검은 기사단 진영
	public static final int WOLRDWAR_WINDAWOOD_GOLD		= 50;//	윈다우드 수호탑 점령전 황금 성단 진영
	public static final int WOLRDWAR_WINDAWOOD_AZUR		= 51;//	윈다우드 수호탑 점령전 아주르의 정원
	
	public static final int TOWNID_TREASURE_ISLAND		= 52;//	만월의 보물섬

	private static final short GETBACK_MAP_TALKING_ISLAND = 0;
	private static final Point[] GETBACK_LOC_TALKING_ISLAND = {
			new Point(32600, 32942), new Point(32574, 32944),
			new Point(32580, 32923), new Point(32557, 32975),
			new Point(32594, 32916), new Point(32580, 32974), };

	private static final short GETBACK_MAP_SILVER_KNIGHT_TOWN = 4;
	private static final Point[] GETBACK_LOC_SILVER_KNIGHT_TOWN = {
			new Point(33071, 33402), new Point(33084, 33392),
			new Point(33085, 33402), new Point(33097, 33366),
			new Point(33110, 33365), new Point(33072, 33392), };

	private static final short GETBACK_MAP_GLUDIO = 4;
	private static final Point[] GETBACK_LOC_GLUDIO = {
			new Point(32601, 32757), new Point(32625, 32809),
			new Point(32618, 32744), new Point(32612, 32781),
			new Point(32605, 32761), new Point(32614, 32739),
			new Point(32611, 32795), };

	private static final short GETBACK_MAP_ORCISH_FOREST = 4;
	private static final Point[] GETBACK_LOC_ORCISH_FOREST = {
			new Point(32750, 32435), new Point(32745, 32447),
			new Point(32738, 32452), new Point(32741, 32436),
			new Point(32749, 32446), };

	private static final short GETBACK_MAP_WINDAWOOD = 4;
	private static final Point[] GETBACK_LOC_WINDAWOOD = {
			new Point(32613, 33176), new Point(32624, 33187),
			new Point(32630, 33179), new Point(32634, 33201),
			new Point(32638, 33203), new Point(32621, 33179), };

	private static final short GETBACK_MAP_KENT = 4;
	private static final Point[] GETBACK_LOC_KENT = { 
		    new Point(33048, 32750),
			new Point(33059, 32768), new Point(33047, 32761),
			new Point(33059, 32759), new Point(33051, 32775),
			new Point(33048, 32778), new Point(33064, 32773),
			new Point(33057, 32748), };

	private static final short GETBACK_MAP_GIRAN = 4;
	private static final Point[] GETBACK_LOC_GIRAN = { 
		    new Point(33435, 32803),
			new Point(33439, 32817), new Point(33440, 32809),
			new Point(33419, 32810), new Point(33426, 32823),
			new Point(33418, 32818), new Point(33432, 32824), };

	private static final short GETBACK_MAP_HEINE = 4;
	private static final Point[] GETBACK_LOC_HEINE = { 
		    new Point(33597, 33255),
			new Point(33593, 33248), new Point(33604, 33236),
			new Point(33599, 33236), new Point(33610, 33247),
			new Point(33610, 33241), new Point(33599, 33252),
			new Point(33605, 33252), };

	private static final short GETBACK_MAP_WERLDAN = 4;
	private static final Point[] GETBACK_LOC_WERLDAN = {
			new Point(33702, 32492), new Point(33747, 32508),
			new Point(33696, 32498), new Point(33723, 32512),
			new Point(33710, 32521), new Point(33724, 32488),
			new Point(33693, 32513), };

	private static final short GETBACK_MAP_OREN = 4;
	private static final Point[] GETBACK_LOC_OREN = { 
		    new Point(34077, 32285),
			new Point(34048, 32261), new Point(34039, 32274),
			new Point(34044, 32271), new Point(34044, 32290),
			new Point(34053, 32299), new Point(34078, 32266),
			new Point(34075, 32294), new Point(34061, 32271), };

	private static final short GETBACK_MAP_ELVEN_FOREST = 4;
	private static final Point[] GETBACK_LOC_ELVEN_FOREST = {
			new Point(33065, 32358), new Point(33052, 32313),
			new Point(33030, 32342), new Point(33068, 32320),
			new Point(33071, 32314), new Point(33030, 32370),
			new Point(33076, 32324), new Point(33068, 32336), };

	private static final short GETBACK_MAP_ADEN = 4;
	private static final Point[] GETBACK_LOC_ADEN = { 
		    new Point(33933, 33361),
			new Point(33917, 33355), new Point(33918, 33339),
			new Point(33937, 33334), new Point(33930, 33348),
			new Point(33927, 33339), new Point(33926, 33363),
			new Point(33923, 33350), };

	private static final short GETBACK_MAP_SILENT_CAVERN = 304;
	private static final Point[] GETBACK_LOC_SILENT_CAVERN = {
			new Point(32856, 32898), new Point(32860, 32916),
			new Point(32868, 32893), new Point(32875, 32903),
			new Point(32855, 32898), };

	private static final short GETBACK_MAP_OUM_DUNGEON = 310;
	private static final Point[] GETBACK_LOC_OUM_DUNGEON = {
			new Point(32818, 32805), new Point(32800, 32798),
			new Point(32815, 32819), new Point(32823, 32811),
			new Point(32817, 32828), };

	private static final short GETBACK_MAP_RESISTANCE = 400;
	private static final Point[] GETBACK_LOC_RESISTANCE = {
			new Point(32570, 32667), new Point(32559, 32678),
			new Point(32564, 32683), new Point(32574, 32661),
			new Point(32576, 32669), new Point(32572, 32662), };

	private static final short GETBACK_MAP_PIRATE_ISLAND = 440;
	private static final Point[] GETBACK_LOC_PIRATE_ISLAND = {
			new Point(32431, 33058), new Point(32407, 33054), };

	private static final short GETBACK_MAP_RECLUSE_VILLAGE = 400;
	private static final Point[] GETBACK_LOC_RECLUSE_VILLAGE = {
			new Point(32599, 32916), new Point(32599, 32923),
			new Point(32603, 32908), new Point(32595, 32908),
			new Point(32591, 32918), };
	
	private static final short GETBACK_MAP_LUUN = 4;
	private static final Point[] GETBACK_LOC_LUUN = { 
		    new Point(34417, 32184), new Point(34427, 32184),
			new Point(34410, 32177), new Point(34400, 32177),
			new Point(34393, 32184), new Point(34397, 32199),
			new Point(34411, 32204), new Point(34416, 32194) };
	
	private static final short GETBACK_MAP_HIDDEN_VALLEY  = 2005;
	private static final Point[] GETBACK_LOC_HIDDEN_VALLEY = {
		    new Point(32691, 32853), new Point(32692, 32864),
		    new Point(32688, 32876), new Point(32673, 32871),
		    new Point(32670, 32857) };
	
	private static final short TOWNID_MAP_CLAUDIA = 7783;//클라우디아
	private static final Point[] GETBACK_LOC_CLAUDIA = { 
		    new Point(32640, 32872),
			new Point(32638, 32869), new Point(32639, 32857),
			new Point(32647, 32865), new Point(32648, 32861),
			new Point(32631, 32862), new Point(32635, 32865), };
	
	private static final short TOWNID_MAP_REDSOLDER = 3;//붉은 기사단 훈련소
	private static final Point[] GETBACK_LOC_REDSOLDER = { 
		    new Point(32734, 32851),
			new Point(32725, 32846), new Point(32726, 32853),
			new Point(32728, 32859), new Point(32732, 32840),
			new Point(32717, 32839), new Point(32716, 32850), };
	
	private static final short TOWNID_MAP_FORGOTTEN_ISLAND = 1710;//잊혀진 섬 대기실
	private static final Point[] GETBACK_LOC_FORGOTTEN_ISLAND = { 
		    new Point(32786, 32764),
			new Point(32779, 32770), new Point(32775, 32761),
			new Point(32782, 32754), new Point(32789, 32755),
			new Point(32773, 32774), new Point(32768, 32762), };
	
	private static final short TOWNID_MAP_SKYGARDEN = 622;//수상한 하늘 정원
	private static final Point[] GETBACK_LOC_SKYGARDEN = { 
		    new Point(32767, 32837),
			new Point(32759, 32836), new Point(32762, 32824),
			new Point(32774, 32820), new Point(32768, 32815),
			new Point(32780, 32829), new Point(32772, 32843), };
	
	private static final short MAP_WOLRDWAR_GIRAN = 15482;//
	private static final Point[] GETBACK_LOC_WOLRDWAR_GIRAN = { 
		    new Point(33628, 32792),
			new Point(33627, 32782), new Point(33638, 32776),
			new Point(33640, 32785), new Point(33640, 32796),
			new Point(33648, 32796), new Point(33627, 32786), };
	
	private static final short MAP_WOLRDWAR_ORC = 15483;//
	private static final Point[] GETBACK_LOC_WOLRDWAR_ORC = { 
		    new Point(32790, 32407),
			new Point(32794, 32409), new Point(32801, 32406),
			new Point(32801, 32396), new Point(32791, 32394),
			new Point(32784, 32393), new Point(32778, 32400), };
	
	private static final short MAP_WOLRDWAR_HEINE = 15484;//
	private static final Point[] GETBACK_LOC_WOLRDWAR_HEINE = { 
		    new Point(32740, 32695),
			new Point(32734, 32698), new Point(32735, 32704),
			new Point(32745, 32705), new Point(32755, 32705),
			new Point(32764, 32699), new Point(32757, 32694), };
	
	public static final short MAP_ERJABE_CROWN = 15871;
	private static final Point[] GETBACK_LOC_ERJABE_CROWN = { 
		    new Point(32825, 32761),
			new Point(32840, 32760), new Point(32841, 32772),
			new Point(32832, 32776), new Point(32826, 32777),
			new Point(32821, 32771), new Point(32823, 32764), };
	
	public static final short MAP_ERJABE_KNIGHT = 15872;
	private static final Point[] GETBACK_LOC_ERJABE_KNIGHT = { 
		    new Point(32825, 32761),
			new Point(32840, 32760), new Point(32841, 32772),
			new Point(32832, 32776), new Point(32826, 32777),
			new Point(32821, 32771), new Point(32823, 32764), };
	
	public static final short MAP_ERJABE_ELF = 15873;
	private static final Point[] GETBACK_LOC_ERJABE_ELF = { 
		    new Point(32825, 32761),
			new Point(32840, 32760), new Point(32841, 32772),
			new Point(32832, 32776), new Point(32826, 32777),
			new Point(32821, 32771), new Point(32823, 32764), };
	
	public static final short MAP_ERJABE_MAGE = 15874;
	private static final Point[] GETBACK_LOC_ERJABE_MAGE = { 
		    new Point(32825, 32761),
			new Point(32840, 32760), new Point(32841, 32772),
			new Point(32832, 32776), new Point(32826, 32777),
			new Point(32821, 32771), new Point(32823, 32764), };
	
	public static final short MAP_ERJABE_DARKELF = 15875;
	private static final Point[] GETBACK_LOC_ERJABE_DARKELF = { 
		    new Point(32825, 32761),
			new Point(32840, 32760), new Point(32841, 32772),
			new Point(32832, 32776), new Point(32826, 32777),
			new Point(32821, 32771), new Point(32823, 32764), };
	
	public static final short MAP_ERJABE_DRAGONKNIGHT = 15876;
	private static final Point[] GETBACK_LOC_ERJABE_DRAGONKNIGHT = { 
		    new Point(32825, 32761),
			new Point(32840, 32760), new Point(32841, 32772),
			new Point(32832, 32776), new Point(32826, 32777),
			new Point(32821, 32771), new Point(32823, 32764), };
	
	public static final short MAP_ERJABE_ILLOSIONIST = 15877;
	private static final Point[] GETBACK_LOC_ERJABE_ILLOSIONIST = { 
		    new Point(32825, 32761),
			new Point(32840, 32760), new Point(32841, 32772),
			new Point(32832, 32776), new Point(32826, 32777),
			new Point(32821, 32771), new Point(32823, 32764), };
	
	public static final short MAP_ERJABE_WARRIOR = 15878;
	private static final Point[] GETBACK_LOC_ERJABE_WARRIOR = { 
		    new Point(32825, 32761),
			new Point(32840, 32760), new Point(32841, 32772),
			new Point(32832, 32776), new Point(32826, 32777),
			new Point(32821, 32771), new Point(32823, 32764), };
	
	public static final short MAP_ERJABE_FENCER = 15879;
	private static final Point[] GETBACK_LOC_ERJABE_FENCER = { 
		    new Point(32825, 32761),
			new Point(32840, 32760), new Point(32841, 32772),
			new Point(32832, 32776), new Point(32826, 32777),
			new Point(32821, 32771), new Point(32823, 32764), };
	
	public static final short MAP_ERJABE_LANCER = 15900;
	private static final Point[] GETBACK_LOC_ERJABE_LANCER = { 
		    new Point(32825, 32761),
			new Point(32840, 32760), new Point(32841, 32772),
			new Point(32832, 32776), new Point(32826, 32777),
			new Point(32821, 32771), new Point(32823, 32764), };
	
	public static final short MAP_WOLRDWAR_HEINE_TOWER = 15490;
	private static final Point[] GETBACK_LOC_WOLRDWAR_HEINE_BASE = { new Point(32733, 32825) };
	private static final Point[] GETBACK_LOC_WOLRDWAR_HEINE_RED = { 
		    new Point(32686, 32810), new Point(32691, 32823), 
			new Point(32680, 32812), new Point(32673, 32819),
			new Point(32675, 32824), new Point(32675, 32832),
			new Point(32681, 32836), new Point(32689, 32830), 
			new Point(32685, 32822), new Point(32680, 32821), 
			new Point(32675, 32827), new Point(32682, 32826) };
	
	private static final Point[] GETBACK_LOC_WOLRDWAR_HEINE_BLACK = { 
	    	new Point(32792, 32825), new Point(32790, 32823), 
	    	new Point(32792, 32819), new Point(32788, 32818),
	    	new Point(32784, 32823), new Point(32786, 32828),
	    	new Point(32790, 32834), new Point(32797, 32832), 
	    	new Point(32800, 32827), new Point(32798, 32820) };
	
	private static final Point[] GETBACK_LOC_WOLRDWAR_HEINE_GOLD = { 
			new Point(32735, 32871), new Point(32733, 32877), 
			new Point(32736, 32880), new Point(32740, 32876),
			new Point(32730, 32872), new Point(32726, 32877),
			new Point(32726, 32882), new Point(32733, 32884), 
			new Point(32739, 32882), new Point(32744, 32879) };
	
	public static final short MAP_WOLRDWAR_HEINE_EVA = 15489;
	private static final Point[] GETBACK_LOC_WOLRDWAR_HEINE_EVA = { 
		new Point(32768, 32846), new Point(32775, 32841), 
		new Point(32780, 32830), new Point(32771, 32829),
		new Point(32783, 32822), new Point(32761, 32822),
		new Point(32753, 32827), new Point(32761, 32836), 
		new Point(32755, 32842), new Point(32759, 32848) };
	
	private static final short MAP_ABANDON_INTER_WEST = 12900;
	private static final Point[] GETBACK_LOC_ABANDON_INTER_WEST = { 
		new Point(32737, 32958), new Point(32733, 32958), 
		new Point(32730, 32961), new Point(32730, 32967),
		new Point(32735, 32967), new Point(32734, 32963) };
	
	private static final short MAP_ABANDON_INTER_EAST = 11900;
	private static final Point[] GETBACK_LOC_ABANDON_INTER_EAST = { 
		new Point(32737, 32958), new Point(32733, 32958), 
		new Point(32730, 32961), new Point(32730, 32967),
		new Point(32735, 32967), new Point(32734, 32963) };
	
	public static final short MAP_WOLRDWAR_WINDAWOOD_TOWER = 15498;
	private static final Point[] GETBACK_LOC_WOLRDWAR_WINDAWOOD_BASE = { new Point(32674, 32805) };
	private static final Point[] GETBACK_LOC_WOLRDWAR_WINDAWOOD_RED = { 
		    new Point(32677, 32832), new Point(32672, 32836), 
			new Point(32679, 32842), new Point(32667, 32845),
			new Point(32673, 32852), new Point(32680, 32852) };
	
	private static final Point[] GETBACK_LOC_WOLRDWAR_WINDAWOOD_BLACK = { 
	    	new Point(32678, 32753), new Point(32668, 32753), 
	    	new Point(32674, 32745), new Point(32678, 32759),
	    	new Point(32667, 32755), new Point(32669, 32769),
	    	new Point(32674, 32773), new Point(32669, 32776) };
	
	private static final Point[] GETBACK_LOC_WOLRDWAR_WINDAWOOD_GOLD = { 
			new Point(32631, 32801), new Point(32636, 32806), 
			new Point(32628, 32814), new Point(32622, 32807),
			new Point(32617, 32803), new Point(32642, 32808) };
	
	public static final short MAP_WOLRDWAR_WINDAWOOD_AZUR = 15499;
	private static final Point[] GETBACK_LOC_WOLRDWAR_WINDAWOOD_AZUR = { 
		new Point(32759, 32834), new Point(32754, 32840), 
		new Point(32764, 32843), new Point(32770, 32832),
		new Point(32777, 32835), new Point(32781, 32847) };
	
	public static final short GETBACK_MAP_TREASURE_ISLAND = 15601;
	private static final Point[] GETBACK_LOC_TREASURE_ISLAND = { 
		    new Point(33218, 33081), new Point(33229, 33101),
			new Point(33218, 33088), new Point(33209, 33082),
			new Point(33208, 33090), new Point(33214, 33097),
			new Point(33201, 33089), new Point(33203, 33096) };
	
//	private static final int[][] START_LOC_X = new int [][] { { 32691, 32692, 32688, 32673, 32670 }  };//리뉴얼 숨계 통합 X
//	private static final int[][] START_LOC_Y = new int [][] { { 32853, 32864, 32876, 32871, 32857 }  };//리뉴얼 숨계 통합 Y

	private L1TownLocation() {
	}

	public static int[] getGetBackLoc(int town_id) { 
		int[] loc = new int[3];
		switch(town_id){
		case TOWNID_TALKING_ISLAND:{
			int rnd = CommonUtil.random(GETBACK_LOC_TALKING_ISLAND.length);
			loc[0] = GETBACK_LOC_TALKING_ISLAND[rnd].getX();
			loc[1] = GETBACK_LOC_TALKING_ISLAND[rnd].getY();
			loc[2] = GETBACK_MAP_TALKING_ISLAND;}
			break;
		case TOWNID_SILVER_KNIGHT:{
			int rnd = CommonUtil.random(GETBACK_LOC_SILVER_KNIGHT_TOWN.length);
			loc[0] = GETBACK_LOC_SILVER_KNIGHT_TOWN[rnd].getX();
			loc[1] = GETBACK_LOC_SILVER_KNIGHT_TOWN[rnd].getY();
			loc[2] = GETBACK_MAP_SILVER_KNIGHT_TOWN;}
			break;
		case TOWNID_KENT:{
			int rnd = CommonUtil.random(GETBACK_LOC_KENT.length);
			loc[0] = GETBACK_LOC_KENT[rnd].getX();
			loc[1] = GETBACK_LOC_KENT[rnd].getY();
			loc[2] = GETBACK_MAP_KENT;}
			break;
		case TOWNID_GLUDIO:{
			int rnd = CommonUtil.random(GETBACK_LOC_GLUDIO.length);
			loc[0] = GETBACK_LOC_GLUDIO[rnd].getX();
			loc[1] = GETBACK_LOC_GLUDIO[rnd].getY();
			loc[2] = GETBACK_MAP_GLUDIO;}
			break;
		case TOWNID_ORCISH_FOREST:{ // 오크마을
			int rnd = CommonUtil.random(GETBACK_LOC_ORCISH_FOREST.length);
			loc[0] = GETBACK_LOC_ORCISH_FOREST[rnd].getX();
			loc[1] = GETBACK_LOC_ORCISH_FOREST[rnd].getY();
			loc[2] = GETBACK_MAP_ORCISH_FOREST;}
			break;
		case TOWNID_WINDAWOOD:{ // 윈다우드
			int rnd = CommonUtil.random(GETBACK_LOC_WINDAWOOD.length);
			loc[0] = GETBACK_LOC_WINDAWOOD[rnd].getX();
			loc[1] = GETBACK_LOC_WINDAWOOD[rnd].getY();
			loc[2] = GETBACK_MAP_WINDAWOOD;}
			break;
		case TOWNID_GIRAN:{ // 기란
			int rnd = CommonUtil.random(GETBACK_LOC_GIRAN.length);
			loc[0] = GETBACK_LOC_GIRAN[rnd].getX();
			loc[1] = GETBACK_LOC_GIRAN[rnd].getY();
			loc[2] = GETBACK_MAP_GIRAN;}
			break;
		case TOWNID_HEINE:{ // 하이네
			int rnd = CommonUtil.random(GETBACK_LOC_HEINE.length);
			loc[0] = GETBACK_LOC_HEINE[rnd].getX();
			loc[1] = GETBACK_LOC_HEINE[rnd].getY();
			loc[2] = GETBACK_MAP_HEINE;}
			break;
		case TOWNID_WERLDAN:{ // 웰던
			int rnd = CommonUtil.random(GETBACK_LOC_WERLDAN.length);
			loc[0] = GETBACK_LOC_WERLDAN[rnd].getX();
			loc[1] = GETBACK_LOC_WERLDAN[rnd].getY();
			loc[2] = GETBACK_MAP_WERLDAN;}
			break;
		case TOWNID_OREN:{ // 오렌
			int rnd = CommonUtil.random(GETBACK_LOC_OREN.length);
			loc[0] = GETBACK_LOC_OREN[rnd].getX();
			loc[1] = GETBACK_LOC_OREN[rnd].getY();
			loc[2] = GETBACK_MAP_OREN;}
			break;
		case TOWNID_ELVEN_FOREST:{ // 요정숲
			int rnd = CommonUtil.random(GETBACK_LOC_ELVEN_FOREST.length);
			loc[0] = GETBACK_LOC_ELVEN_FOREST[rnd].getX();
			loc[1] = GETBACK_LOC_ELVEN_FOREST[rnd].getY();
			loc[2] = GETBACK_MAP_ELVEN_FOREST;}
			break;
		case TOWNID_ADEN:{ // 아덴
			int rnd = CommonUtil.random(GETBACK_LOC_ADEN.length);
			loc[0] = GETBACK_LOC_ADEN[rnd].getX();
			loc[1] = GETBACK_LOC_ADEN[rnd].getY();
			loc[2] = GETBACK_MAP_ADEN;}
			break;
		case TOWNID_SILENT_CAVERN:{ // 은기사
			int rnd = CommonUtil.random(GETBACK_LOC_SILENT_CAVERN.length);
			loc[0] = GETBACK_LOC_SILENT_CAVERN[rnd].getX();
			loc[1] = GETBACK_LOC_SILENT_CAVERN[rnd].getY();
			loc[2] = GETBACK_MAP_SILENT_CAVERN;}
			break;
		case TOWNID_OUM_DUNGEON:{ // 오움던전
			int rnd = CommonUtil.random(GETBACK_LOC_OUM_DUNGEON.length);
			loc[0] = GETBACK_LOC_OUM_DUNGEON[rnd].getX();
			loc[1] = GETBACK_LOC_OUM_DUNGEON[rnd].getY();
			loc[2] = GETBACK_MAP_OUM_DUNGEON;}
			break;
		case TOWNID_RESISTANCE:{ // 대공동
			int rnd = CommonUtil.random(GETBACK_LOC_RESISTANCE.length);
			loc[0] = GETBACK_LOC_RESISTANCE[rnd].getX();
			loc[1] = GETBACK_LOC_RESISTANCE[rnd].getY();
			loc[2] = GETBACK_MAP_RESISTANCE;}
			break;
		case TOWNID_PIRATE_ISLAND:{ // 해적섬
			int rnd = CommonUtil.random(GETBACK_LOC_PIRATE_ISLAND.length);
			loc[0] = GETBACK_LOC_PIRATE_ISLAND[rnd].getX();
			loc[1] = GETBACK_LOC_PIRATE_ISLAND[rnd].getY();
			loc[2] = GETBACK_MAP_PIRATE_ISLAND;}
			break;
		case TOWNID_RECLUSE_VILLAGE:{ // 대공동
			int rnd = CommonUtil.random(GETBACK_LOC_RECLUSE_VILLAGE.length);
			loc[0] = GETBACK_LOC_RECLUSE_VILLAGE[rnd].getX();
			loc[1] = GETBACK_LOC_RECLUSE_VILLAGE[rnd].getY();
			loc[2] = GETBACK_MAP_RECLUSE_VILLAGE;}
			break;
		case TOWNID_LUUN:{ // 루운성
			int rnd = CommonUtil.random(GETBACK_LOC_LUUN.length);
			loc[0] = GETBACK_LOC_LUUN[rnd].getX();
			loc[1] = GETBACK_LOC_LUUN[rnd].getY();
			loc[2] = GETBACK_MAP_LUUN;}
			break;
		case TOWNID_HIDDEN_VALLEY:{ // 숨계진 계곡
			int rnd = CommonUtil.random(GETBACK_LOC_HIDDEN_VALLEY.length);
			loc[0] = GETBACK_LOC_HIDDEN_VALLEY[rnd].getX();
			loc[1] = GETBACK_LOC_HIDDEN_VALLEY[rnd].getY();
			loc[2] = GETBACK_MAP_HIDDEN_VALLEY;}
			break;
		case TOWNID_CLAUDIA:{ // 클라우디아
			int rnd = CommonUtil.random(GETBACK_LOC_CLAUDIA.length);
			loc[0] = GETBACK_LOC_CLAUDIA[rnd].getX();
			loc[1] = GETBACK_LOC_CLAUDIA[rnd].getY();
			loc[2] = TOWNID_MAP_CLAUDIA;}
			break;
		case TOWNID_REDSOLDER:{ // 붉은 기사단 훈련소
			int rnd = CommonUtil.random(GETBACK_LOC_REDSOLDER.length);
			loc[0] = GETBACK_LOC_REDSOLDER[rnd].getX();
			loc[1] = GETBACK_LOC_REDSOLDER[rnd].getY();
			loc[2] = TOWNID_MAP_REDSOLDER;}
			break;
		case FORGOTTEN_ISLAND:{ // 잊혀진 섬
			int rnd = CommonUtil.random(GETBACK_LOC_FORGOTTEN_ISLAND.length);
			loc[0] = GETBACK_LOC_FORGOTTEN_ISLAND[rnd].getX();
			loc[1] = GETBACK_LOC_FORGOTTEN_ISLAND[rnd].getY();
			loc[2] = TOWNID_MAP_FORGOTTEN_ISLAND;}
			break;
		case TOWNID_SKYGARDEN:{ // 수상한 하늘 정원
			int rnd = CommonUtil.random(GETBACK_LOC_SKYGARDEN.length);
			loc[0] = GETBACK_LOC_SKYGARDEN[rnd].getX();
			loc[1] = GETBACK_LOC_SKYGARDEN[rnd].getY();
			loc[2] = TOWNID_MAP_SKYGARDEN;}
			break;
		case WOLRDWAR_GIRAN:{ // 월드 공성전 기란
			int rnd = CommonUtil.random(GETBACK_LOC_WOLRDWAR_GIRAN.length);
			loc[0] = GETBACK_LOC_WOLRDWAR_GIRAN[rnd].getX();
			loc[1] = GETBACK_LOC_WOLRDWAR_GIRAN[rnd].getY();
			loc[2] = MAP_WOLRDWAR_GIRAN;}
			break;
		case WOLRDWAR_ORC:{ // 월드 공성전 오크요새
			int rnd = CommonUtil.random(GETBACK_LOC_WOLRDWAR_ORC.length);
			loc[0] = GETBACK_LOC_WOLRDWAR_ORC[rnd].getX();
			loc[1] = GETBACK_LOC_WOLRDWAR_ORC[rnd].getY();
			loc[2] = MAP_WOLRDWAR_ORC;}
			break;
		case WOLRDWAR_HEINE:{ // 월드 공성전 하이네
			int rnd = CommonUtil.random(GETBACK_LOC_WOLRDWAR_HEINE.length);
			loc[0] = GETBACK_LOC_WOLRDWAR_HEINE[rnd].getX();
			loc[1] = GETBACK_LOC_WOLRDWAR_HEINE[rnd].getY();
			loc[2] = MAP_WOLRDWAR_HEINE;}
			break;
		case ERJABE_CROWN:{
			int rnd = CommonUtil.random(GETBACK_LOC_ERJABE_CROWN.length);
			loc[0] = GETBACK_LOC_ERJABE_CROWN[rnd].getX();
			loc[1] = GETBACK_LOC_ERJABE_CROWN[rnd].getY();
			loc[2] = MAP_ERJABE_CROWN;}
			break;
		case ERJABE_KNIGHT:{
			int rnd = CommonUtil.random(GETBACK_LOC_ERJABE_KNIGHT.length);
			loc[0] = GETBACK_LOC_ERJABE_KNIGHT[rnd].getX();
			loc[1] = GETBACK_LOC_ERJABE_KNIGHT[rnd].getY();
			loc[2] = MAP_ERJABE_KNIGHT;}
			break;
		case ERJABE_ELF:{
			int rnd = CommonUtil.random(GETBACK_LOC_ERJABE_ELF.length);
			loc[0] = GETBACK_LOC_ERJABE_ELF[rnd].getX();
			loc[1] = GETBACK_LOC_ERJABE_ELF[rnd].getY();
			loc[2] = MAP_ERJABE_ELF;}
			break;
		case ERJABE_MAGE:{
			int rnd = CommonUtil.random(GETBACK_LOC_ERJABE_MAGE.length);
			loc[0] = GETBACK_LOC_ERJABE_MAGE[rnd].getX();
			loc[1] = GETBACK_LOC_ERJABE_MAGE[rnd].getY();
			loc[2] = MAP_ERJABE_MAGE;}
			break;
		case ERJABE_DARKELF:{
			int rnd = CommonUtil.random(GETBACK_LOC_ERJABE_DARKELF.length);
			loc[0] = GETBACK_LOC_ERJABE_DARKELF[rnd].getX();
			loc[1] = GETBACK_LOC_ERJABE_DARKELF[rnd].getY();
			loc[2] = MAP_ERJABE_DARKELF;}
			break;
		case ERJABE_DRAGONKNIGHT:{
			int rnd = CommonUtil.random(GETBACK_LOC_ERJABE_DRAGONKNIGHT.length);
			loc[0] = GETBACK_LOC_ERJABE_DRAGONKNIGHT[rnd].getX();
			loc[1] = GETBACK_LOC_ERJABE_DRAGONKNIGHT[rnd].getY();
			loc[2] = MAP_ERJABE_DRAGONKNIGHT;}
			break;
		case ERJABE_ILLOSIONIST:{
			int rnd = CommonUtil.random(GETBACK_LOC_ERJABE_ILLOSIONIST.length);
			loc[0] = GETBACK_LOC_ERJABE_ILLOSIONIST[rnd].getX();
			loc[1] = GETBACK_LOC_ERJABE_ILLOSIONIST[rnd].getY();
			loc[2] = MAP_ERJABE_ILLOSIONIST;}
			break;
		case ERJABE_WARRIOR:{
			int rnd = CommonUtil.random(GETBACK_LOC_ERJABE_WARRIOR.length);
			loc[0] = GETBACK_LOC_ERJABE_WARRIOR[rnd].getX();
			loc[1] = GETBACK_LOC_ERJABE_WARRIOR[rnd].getY();
			loc[2] = MAP_ERJABE_WARRIOR;}
			break;
		case ERJABE_FENCER:{
			int rnd = CommonUtil.random(GETBACK_LOC_ERJABE_FENCER.length);
			loc[0] = GETBACK_LOC_ERJABE_FENCER[rnd].getX();
			loc[1] = GETBACK_LOC_ERJABE_FENCER[rnd].getY();
			loc[2] = MAP_ERJABE_FENCER;}
			break;
		case ERJABE_LANCER:{
			int rnd = CommonUtil.random(GETBACK_LOC_ERJABE_LANCER.length);
			loc[0] = GETBACK_LOC_ERJABE_LANCER[rnd].getX();
			loc[1] = GETBACK_LOC_ERJABE_LANCER[rnd].getY();
			loc[2] = MAP_ERJABE_LANCER;}
			break;
		case WOLRDWAR_HEINE_BASE:{
			loc[0] = GETBACK_LOC_WOLRDWAR_HEINE_BASE[0].getX();
			loc[1] = GETBACK_LOC_WOLRDWAR_HEINE_BASE[0].getY();
			loc[2] = MAP_WOLRDWAR_HEINE_TOWER;}
			break;
		case WOLRDWAR_HEINE_RED:{
			int rnd = CommonUtil.random(GETBACK_LOC_WOLRDWAR_HEINE_RED.length);
			loc[0] = GETBACK_LOC_WOLRDWAR_HEINE_RED[rnd].getX();
			loc[1] = GETBACK_LOC_WOLRDWAR_HEINE_RED[rnd].getY();
			loc[2] = MAP_WOLRDWAR_HEINE_TOWER;}
			break;
		case WOLRDWAR_HEINE_BLACK:{
			int rnd = CommonUtil.random(GETBACK_LOC_WOLRDWAR_HEINE_BLACK.length);
			loc[0] = GETBACK_LOC_WOLRDWAR_HEINE_BLACK[rnd].getX();
			loc[1] = GETBACK_LOC_WOLRDWAR_HEINE_BLACK[rnd].getY();
			loc[2] = MAP_WOLRDWAR_HEINE_TOWER;}
			break;
		case WOLRDWAR_HEINE_GOLD:{
			int rnd = CommonUtil.random(GETBACK_LOC_WOLRDWAR_HEINE_GOLD.length);
			loc[0] = GETBACK_LOC_WOLRDWAR_HEINE_GOLD[rnd].getX();
			loc[1] = GETBACK_LOC_WOLRDWAR_HEINE_GOLD[rnd].getY();
			loc[2] = MAP_WOLRDWAR_HEINE_TOWER;}
			break;
		case WOLRDWAR_HEINE_EVA:{
			int rnd = CommonUtil.random(GETBACK_LOC_WOLRDWAR_HEINE_EVA.length);
			loc[0] = GETBACK_LOC_WOLRDWAR_HEINE_EVA[rnd].getX();
			loc[1] = GETBACK_LOC_WOLRDWAR_HEINE_EVA[rnd].getY();
			loc[2] = MAP_WOLRDWAR_HEINE_EVA;}
			break;
		case ABANDON_INTER_WEST_BASE:{
			int rnd = CommonUtil.random(GETBACK_LOC_ABANDON_INTER_WEST.length);
			loc[0] = GETBACK_LOC_ABANDON_INTER_WEST[rnd].getX();
			loc[1] = GETBACK_LOC_ABANDON_INTER_WEST[rnd].getY();
			loc[2] = MAP_ABANDON_INTER_WEST;}
			break;
		case ABANDON_INTER_EAST_BASE:{
			int rnd = CommonUtil.random(GETBACK_LOC_ABANDON_INTER_EAST.length);
			loc[0] = GETBACK_LOC_ABANDON_INTER_EAST[rnd].getX();
			loc[1] = GETBACK_LOC_ABANDON_INTER_EAST[rnd].getY();
			loc[2] = MAP_ABANDON_INTER_EAST;}
			break;
		case WOLRDWAR_WINDAWOOD_BASE:{
			loc[0] = GETBACK_LOC_WOLRDWAR_WINDAWOOD_BASE[0].getX();
			loc[1] = GETBACK_LOC_WOLRDWAR_WINDAWOOD_BASE[0].getY();
			loc[2] = MAP_WOLRDWAR_WINDAWOOD_TOWER;}
			break;
		case WOLRDWAR_WINDAWOOD_RED:{
			int rnd = CommonUtil.random(GETBACK_LOC_WOLRDWAR_WINDAWOOD_RED.length);
			loc[0] = GETBACK_LOC_WOLRDWAR_WINDAWOOD_RED[rnd].getX();
			loc[1] = GETBACK_LOC_WOLRDWAR_WINDAWOOD_RED[rnd].getY();
			loc[2] = MAP_WOLRDWAR_WINDAWOOD_TOWER;}
			break;
		case WOLRDWAR_WINDAWOOD_BLACK:{
			int rnd = CommonUtil.random(GETBACK_LOC_WOLRDWAR_WINDAWOOD_BLACK.length);
			loc[0] = GETBACK_LOC_WOLRDWAR_WINDAWOOD_BLACK[rnd].getX();
			loc[1] = GETBACK_LOC_WOLRDWAR_WINDAWOOD_BLACK[rnd].getY();
			loc[2] = MAP_WOLRDWAR_WINDAWOOD_TOWER;}
			break;
		case WOLRDWAR_WINDAWOOD_GOLD:{
			int rnd = CommonUtil.random(GETBACK_LOC_WOLRDWAR_WINDAWOOD_GOLD.length);
			loc[0] = GETBACK_LOC_WOLRDWAR_WINDAWOOD_GOLD[rnd].getX();
			loc[1] = GETBACK_LOC_WOLRDWAR_WINDAWOOD_GOLD[rnd].getY();
			loc[2] = MAP_WOLRDWAR_WINDAWOOD_TOWER;}
			break;
		case WOLRDWAR_WINDAWOOD_AZUR:{
			int rnd = CommonUtil.random(GETBACK_LOC_WOLRDWAR_WINDAWOOD_AZUR.length);
			loc[0] = GETBACK_LOC_WOLRDWAR_WINDAWOOD_AZUR[rnd].getX();
			loc[1] = GETBACK_LOC_WOLRDWAR_WINDAWOOD_AZUR[rnd].getY();
			loc[2] = MAP_WOLRDWAR_WINDAWOOD_AZUR;}
			break;
		case TOWNID_TREASURE_ISLAND:{// 만월의 보물섬
			int rnd = CommonUtil.random(GETBACK_LOC_TREASURE_ISLAND.length);
			loc[0] = GETBACK_LOC_TREASURE_ISLAND[rnd].getX();
			loc[1] = GETBACK_LOC_TREASURE_ISLAND[rnd].getY();
			loc[2] = GETBACK_MAP_TREASURE_ISLAND;}
			break;
		default:{ // 은기사
			int rnd = CommonUtil.random(GETBACK_LOC_SILVER_KNIGHT_TOWN.length);
			loc[0] = GETBACK_LOC_SILVER_KNIGHT_TOWN[rnd].getX();
			loc[1] = GETBACK_LOC_SILVER_KNIGHT_TOWN[rnd].getY();
			loc[2] = GETBACK_MAP_SILVER_KNIGHT_TOWN;}
			break;
		}
		return loc;
	}

	/** 세금 징수 **/
	public static int getTownTaxRateByNpcid(int npcid) { 
		L1Town town = TownTable.getTownFromNpcId(npcid);
		if (town == null) {
			return 0;
		}
		return town.get_tax_rate() + 2;
	}

}

