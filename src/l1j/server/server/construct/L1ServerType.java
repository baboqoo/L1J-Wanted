package l1j.server.server.construct;

import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.utils.CommonUtil;

public enum L1ServerType {
	/*DEPOROJYOO(		1,		"데포로쥬"),
	KENRAUHELL(		2,		"켄라우헬"),
	ZILLIAN(		3,		"질리언"),
	ISYLOTE(		4,		"이실로테"),
	JYOU(			5,		"조우"),
	HADIN(			6,		"하딘"),
	CRISTER(		9,		"크리스터"),
	ATUN(			10,		"아툰"),
	GAUDRIA(		11,		"가드리아"),
	GUNTER(			12,		"군터"),
	ASTEIAR(		13,		"아스테어"),
	DYUKDEFEEL(		14,		"듀크데필"),
	BALSEN(			15,		"발센"),
	ARAIN(			16,		"어레인"),
	CAESTOL(		17,		"캐스톨"),
	SEVASCHAN(		18,		"세바스찬"),
	DECAN(			19,		"데컨"),
	AINHASAD(		20,		"아인하사드"),
	PAAGRIO(		21,		"파아그리오"),
	EVA(			22,		"에바"),
	SAIHA(			23,		"사이하"),
	MAFR(			24,		"마프르"),
	LINDEL(			25,		"린델"),
	ROENGREEN(		30,		"로엔그린"),
	ORC(			48,		"오크"),
	PHIONEX(		53,		"피닉스"),
	SUCUBUS(		55,		"서큐버스"),
	DEATHKNIGHT(	61,		"데스나이트"),
	KNIGHTBARD(		64,		"나이트발드"),
	ADEN(			65,		"아덴");*/

	DEPOROJYOO(     1,      "Deporojyoo"),
	KENRAUHELL(     2,      "Kenrauhell"),
	ZILLIAN(        3,      "Zillian"),
	ISYLOTE(        4,      "Isylote"),
	JYOU(           5,      "Jyou"),
	HADIN(          6,      "Hadin"),
	CRISTER(        9,      "Crister"),
	ATUN(           10,     "Atun"),
	GAUDRIA(        11,     "Gaudria"),
	GUNTER(         12,     "Gunter"),
	ASTEIAR(        13,     "Asteiar"),
	DYUKDEFEEL(     14,     "Dyukdefeel"),
	BALSEN(         15,     "Balsen"),
	ARAIN(          16,     "Arain"),
	CAESTOL(        17,     "Caestol"),
	SEVASCHAN(      18,     "Sevaschan"),
	DECAN(          19,     "Decan"),
	AINHASAD(       20,     "Ainhasad"),
	PAAGRIO(        21,     "Paagrio"),
	EVA(            22,     "Eva"),
	SAIHA(          23,     "Saiha"),
	MAFR(           24,     "Mafr"),
	LINDEL(         25,     "Lindel"),
	ROENGREEN(      30,     "Roengreen"),
	ORC(            48,     "Orc"),
	PHIONEX(        53,     "Phionex"),
	SUCUBUS(        55,     "Sucubus"),
	DEATHKNIGHT(    61,     "Death Knight"),
	KNIGHTBARD(     64,     "Knight Bard"),
	ADEN(           65,     "Aden");
	

	private int _id;
	private String _name;
	L1ServerType(int id, String name) {
		_id		= id;
		_name	= name;
	}
	public int getId(){
		return _id;
	}
	public String getName(){
		return _name;
	}
	
	public static final int PVP_TEST_NUMBER			= 100;// PVP 테스트서버
	public static final int BATTLE_TEST_NUMBER		= 200;// 전투특화 테스트서버
	public static final int TREASURE_ISLAND_NUMBER	= 153;// 만월의 보물섬 서버번호
	
	public static final L1ServerType[] ARRAY = L1ServerType.values();
	private static final ConcurrentHashMap<Integer, L1ServerType> DATA;
	
	static {
		DATA = new ConcurrentHashMap<Integer, L1ServerType>();
		for (L1ServerType server : ARRAY) {
			DATA.put(server._id, server);
		}
	}
	
	public static L1ServerType getRandomServer(){
		return ARRAY[CommonUtil.random(ARRAY.length)];
	}
	
	public static String getServerName(int number){
		if (number == PVP_TEST_NUMBER) {
			//return "PVP 테스트";
			return "PVP Test";
		}
		if (number == BATTLE_TEST_NUMBER) {
			//return "전투 특화 테스트";
			return "Battle Specialized Test";
		}
		if (number == TREASURE_ISLAND_NUMBER) {
			//return "만월의 보물섬";
			return "Treasure Island of Manwol";
		}
		if (!DATA.containsKey(number)) {
			return "UNKNOWN_SERVER";
		}
		return DATA.get(number)._name;
	}
	
	public static void init(){}
}

