package l1j.server.server.construct.item;

import javolution.util.FastMap;

public enum L1ItemType {
	NONE(				"NONE",				-1),// 사용 불가능
	NORMAL(				"NORMAL",			0),
	WEAPON(				"WEAPON",			1),
	ARMOR(				"ARMOR",			2),
	WAND1(				"WAND1",			3),
	WAND(				"WAND",				4),
	SPELL_LONG(			"SPELL_LONG",		5),// 지면 / 오브젝트 선택(원거리)
	NTELE(				"NTELE",			6),
	IDENTIFY(			"IDENTIFY",			7),
	RES(				"RES",				8),
	TELEPORT(			"TELEPORT",			9),// 텔포땜에
	INVISABLE(			"INVISABLE",		10),
	LETTER(				"LETTER",			12),
	LETTER_W(			"LETTER_W",			13),
	CHOICE(				"CHOICE",			14),
	INSTRUMENT(			"INSTRUMENT",		15),
	SOSC(				"SOSC",				16),
	SPELL_SHORT(		"SPELL_SHORT",		17),// 지면 / 오브젝트 선택(근거리)
	T_SHIRT(			"T_SHIRT",			18),
	CLOAK(				"CLOAK",			19),
	GLOVE(				"GLOVE",			20),
	BOOTS(				"BOOTS",			21),
	HELMET(				"HELMET",			22),
	RING(				"RING",				23),
	AMULET(				"AMULET",			24),
	SHIELD(				"SHIELD",			25),
	GARDER(				"GARDER",			25),
	DAI(				"DAI",				26),
	ZEL(				"ZEL",				27),
	BLANK(				"BLANK",			28),
	BTELE(				"BTELE",			29),
	SPELL_BUFF(			"SPELL_BUFF",		30),// 오브젝트 선택(자기자신)
	CCARD(				"CCARD",			31),
	CCARD_W(			"CCARD_W",			32),
	VCARD(				"VCARD",			33),
	VCARD_W(			"VCARD_W",			34),
	WCARD(				"WCARD",			35),
	WCARD_W(			"WCARD_W",			36),
	BELT(				"BELT",				37),
	SPELL_LONG2(		"SPELL_LONG2",		39),// 지면 / 오브젝트 선택(원거리) 5로 같은?
	EARRING(			"EARRING",			40),// 귀걸이
	FISHING_ROD(		"FISHING_ROD",		42),// 낚싯대
	RON(				"RON",				44),// 룬1
	RON_2(				"RON_2",			45),// 성장/회복의 문장
	ACCZEL(				"ACCZEL",			46),// 바르는주문서
	PAIR(				"PAIR",				47),// 각반
	HEALING(			"HEALING",			51),// 포션
	SHOULDER(			"SHOULDER",			52),// 견갑
	BADGE(				"BADGE",			53),// 휘장
	POTENTIAL_SCROLL(	"POTENTIAL_SCROLL",	55),// 잠재력 강화 주문서
	SPELLMELT(			"SPELLMELT",		64),// 스킬 용해제
	ELIXER_RON(			"ELIXER_RON",		66),// 엘릭서 룬
	INVENTORY_BONUS(	"INVENTORY_BONUS",	67),// 가호, 성물
	TAM_FRUIT(			"TAM_FRUIT",		68),// 탐 열매 0x44
	RACE_TICKET(		"RACE_TICKET",		69),// 레이스 표
	PAIR_2(				"PAIR_2",			70),// 각반
    MAGICDOLL(			"MAGICDOLL",		73),// 인형
	SENTENCE(			"SENTENCE",			74),// 문장 0x4a
	SHOULDER_2(			"SHOULDER_2",		75),// 견갑
	BADGE_2(			"BADGE_2",			76),// 휘장
	PET_POTION(			"PET_POTION",		77),// 펫 포션 0x4d
	GARDER_2(			"GARDER_2",			78),// 가더
	DOMINATION_POLY(	"DOMINATION_POLY",	79),// 변신 지배 반지
	PENDANT(			"PENDANT",			80),// 팬던트
	SHOVEL(				"SHOVEL",			81),// 보물 탐지 삽
	LEV_100_POLY(		"LEV_100_POLY",		84),// [%i] 변신 지배 반지
	SMELTING(			"SMELTING",			85),// 제련석
	PURIFY(				"PURIFY",			86),// 정화제
	CHARGED_MAP_TIME(	"CHARGED_MAP_TIME",	88),// 충전 맵 시간 아이템
	;
	private String _key;
	private int _interaction_type;
	L1ItemType(String name, int interaction_type) {
		_key				= name;
		_interaction_type	= interaction_type;
	}
	public String getKey(){
		return _key;
	}
	public int getInteractionType(){
		return _interaction_type;
	}
	
	private static final L1ItemType[] ARRAY = L1ItemType.values();
	private static final FastMap<Integer, L1ItemType> DATA;
	private static final FastMap<String, L1ItemType> KEY_DATA;
	static {
		DATA		= new FastMap<Integer, L1ItemType>(ARRAY.length);
		KEY_DATA	= new FastMap<String, L1ItemType>(ARRAY.length);
		for (L1ItemType use : ARRAY) {
			DATA.put(use._interaction_type, use);
			KEY_DATA.put(use._key, use);
		}
	}
	
	public static L1ItemType fromInt(int id){
		return DATA.get(id);
	}
	
	public static L1ItemType fromString(String key){
		return KEY_DATA.get(key);
	}
}

