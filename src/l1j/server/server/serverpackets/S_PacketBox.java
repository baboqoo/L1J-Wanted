package l1j.server.server.serverpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.Account;
import l1j.server.server.Opcodes;
//import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1BookMark;
import l1j.server.server.utils.SQLUtil;

/** 스킬 아이콘이나 차단 리스트의 표시 등 복수의 용도에 사용되는 패킷의 클래스 */
public class S_PacketBox extends ServerBasePacket {
	private static final String S_PACKETBOX = "[S] S_PacketBox";
	private byte[] _byte = null;

	// *** S_107 sub code list ***

	// 1:Kent 2:Orc 3:WW 4:Giran 5:Heine 6:Dwarf 7:Aden 8:Diad 9:성명 9 ...
	/** C(id) H(?): %s의 공성전이 시작되었습니다. */
	public static final int MSG_WAR_BEGIN = 0;

	/** C(id) H(?): %s의 공성전이 종료했습니다. */
	public static final int MSG_WAR_END = 1;

	/** C(id) H(?): %s의 공성전이 진행중입니다. */
	public static final int MSG_WAR_GOING = 2;

	/** -: 성의 주도권을 잡았습니다. (음악이 바뀐다) */
	public static final int MSG_WAR_INITIATIVE = 3;

	/** -: 성을 점거했습니다. */
	public static final int MSG_WAR_OCCUPY = 4;

	/** ?: 결투가 끝났습니다. (음악이 바뀐다) */
	public static final int MSG_DUEL = 5;

	/** C(count): SMS의 송신에 실패했습니다. / 전부%d건송신되었습니다. */
	public static final int MSG_SMS_SENT = 6;

	/** -: 축복안, 2명은 부부로서 연결되었습니다. (음악이 바뀐다) */
	public static final int MSG_MARRIED = 9;

	/** C(weight): 중량(30 단계) */
	public static final int WEIGHT = 10;

	/** C(food): 만복도(30 단계) */
	public static final int FOOD = 11;

	/** C(0) C(level): 이 아이템은%d레벨 이하만 사용할 수 있습니다. (0~49이외는 표시되지 않는다) */
	public static final int MSG_LEVEL_OVER = 12;

	/** UB정보 HTML */
	public static final int HTML_UB = 14;

	/**
	 * C(id)<br>
	 * 1:몸에 담겨져 있던 정령의 힘이 공기안에 녹아 가는 것을 느꼈습니다.<br>
	 * 2:몸의 구석구석에 화의 정령력이 스며들어 옵니다.<br>
	 * 3:몸의 구석구석에 물의 정령력이 스며들어 옵니다.<br>
	 * 4:몸의 구석구석에 바람의 정령력이 스며들어 옵니다.<br>
	 * 5:몸의 구석구석에 땅의 정령력이 스며들어 옵니다.<br>
	 */
	public static final int MSG_ELF = 15;

	/** C(count) S(name)...: 차단 리스트 */
	public static final int SHOW_LIST_EXCLUDE = 17;

	/** S(name): 차단 리스트 추가 */
	public static final int ADD_EXCLUDE = 18;

	/** S(name): 차단 해제 */
	public static final int REM_EXCLUDE = 19;
	
	/** 스킬 아이콘 */
	public static final int ICONS1 = 20;

	/** 스킬 아이콘 */
	public static final int ICONS2 = 21;

	/** 아우라계의 스킬 아이콘 및 이레이즈매직 아이콘 삭제 */
	public static final int ICON_AURA = 22;

	/** S(name): 타운 리더에게%s가 선택되었습니다. */
	public static final int MSG_TOWN_LEADER = 23;

	/** 
	 * D(혈맹원수) (S(혈원이름) C(혈원계급)) 혈맹원 갱신이 된 상태에서의 /혈맹.
	 */
	public static final int PLEDGE_TWO = 24;//추가

	/** 
	 * D(혈맹원이름) C(랭크) 혈맹에 추가된 인원이 있을때 보내주는 패킷
	 */
	public static final int PLEDGE_REFRESH_PLUS = 25;//추가

	/** 
	 * D(혈맹원이름) C(랭크) 혈맹에 삭제된 인원이 있을때 보내주는 패킷
	 */
	public static final int PLEDGE_REFRESH_MINUS = 26;//추가
	/**
	 * C(id): 당신의 랭크가%s로 변경되었습니다.<br>
	 * id - 1:견습 2:일반 3:가디안
	 */
	public static final int MSG_RANK_CHANGED = 27;

	/** 
	 * D(혈맹원수) (S(혈원이름) C(혈원계급)) 혈맹원 갱신이 안된 상태에서의 /혈맹.
	 */
	//public static final int PLEDGE_ONE = 119;//추가

	/** D(?) S(name) S(clanname): %s혈맹의%s가 라스타바드군을 치웠습니다. */
	public static final int MSG_WIN_LASTAVARD = 30;

	/** -: \f1기분이 좋아졌습니다. */
	public static final int MSG_FEEL_GOOD = 31;
	
	/** 불명 */
	public static final int LOGIN_UNKNOWN3 = 32;

	/** 불명.C_30 패킷이 난다 */
	public static final int SOMETHING1 = 33;

	/** H(time): 블루 일부의 아이콘이 표시된다. */
	public static final int ICON_BLUEPOTION = 34;

	/** H(time): 변신의 아이콘이 표시된다. */
	public static final int ICON_POLYMORPH = 35;

	/** H(time): 채팅 금지의 아이콘이 표시된다. */
	public static final int ICON_CHATBAN = 36;

	/** 불명.C_7 패킷이 난다.C_7은 애완동물의 메뉴를 열었을 때에도 난다. */
	public static final int SOMETHING2 = 37;

	/** 혈맹 정보의 HTML가 표시된다 */
	public static final int HTML_CLAN1 = 38;

	/** H(time): 이뮤의 아이콘이 표시된다 */
	public static final int ICON_I2H = 40;

	/** 캐릭터의 게임 옵션, 쇼트 컷 정보등을 보낸다 */
	public static final int CHARACTER_CONFIG = 41;

	/** 캐릭터 선택 화면으로 돌아간다 */
	public static final int LOGOUT = 42;

	/** 전투중에 재시 동요할 수 없습니다. */
	public static final int MSG_CANT_LOGOUT = 43;

	/**
	 * C(count) D(time) S(name) S(info):<br>
	 * [CALL] 버튼이 붙은 윈도우가 표시된다.이것은 BOT등의 부정자 체크에
	 * 사용되는 기능한 것같다.이름을 더블 클릭 하면(자) C_RequestWho가 날아, 클라이언트의
	 * 폴더에 bot_list.txt가 생성된다.이름을 선택해+키를 누르면(자) 새로운 윈도우가 열린다.
	 */
	public static final int CALL_SOMETHING = 45;

	/**
	 * C(id): 배틀 콜롯세움, 카오스 대전이―<br>
	 * id - 1:개시합니다 2:삭제되었던 3:종료합니다
	 */
	public static final int MSG_COLOSSEUM = 49;

	// 혈맹 정보의 HTML
	public static final int HTML_CLAN2 = 51;

	// 요리 윈도우를 연다
	public static final int COOK_WINDOW = 52;

	/** C(type) H(time): 요리 아이콘이 표시된다 */
	public static final int ICON_COOKING = 53;

	/** 물고기찌 흔들림포시 */
	public static final int FISHING = 55;

	/** 아이콘 삭제 */
	public static final int DEL_ICON = 59;

	/** 드래곤의 진주 (3단가속) */
	public static final int DRAGON_PEARL = 60;

	/** 동맹 목록 */
	public static final int ALLIANCE_LIST = 62;
	
	/** 미니게임 : 5,4,3,2,1 카운트 */
	public static final int MINIGAME_START_COUNT = 64;
	
	/** 미니게임 : 타임(0:00시작) */
	public static final int MINIGAME_TIME2 = 65;

	/** 미니게임 : 게임자 리스트 */
	public static final int MINIGAME_LIST = 66;

	/** 미니게임 : 잠시 후 마을로 이동됩니다(10초 음) * */
	public static final int MINIGAME_10SECOND_COUNT = 69;
	
	/** 미니게임 : 종료 */
	public static final int MINIGAME_END = 70;

	/** 미니게임 : 타임 */
	public static final int MINIGAME_TIME = 71;

	/** 미니게임 : 타임삭제 */
	public static final int MINIGAME_TIME_CLEAR = 72;

	/** 용기사 : 약점 노출 */
	public static final int EXPOSE_WEAKNESS = 75;

	public static final int WAR_START_MSG = 78;// 공성전이 시작 되었습니다.
	public static final int WAR_END_MSG = 79;// 공성전이 종료 되었습니다.
	public static final int WAR_PLAYING_MSG = 80;// 공성전이 진행중 입니다.

	/** 1:분홍색테두리, 2:흔들림, 3:폭죽 **/
	public static final int HADIN_DISPLAY = 83;	
	/** 인던 녹색 메세지 **/
	public static final int GREEN_MESSAGE = 84;
	/** 인던 노랑색 메세지 **/
	public static final int YELLOW_MESSAGE = 61; // 인던 챕터2 대기
	/** 인던 빨간 메세지 **/
	public static final int RED_MESSAGE = 51; // 레드메세지
	/** 인던 점수판 **/	
	public static final int SCORE_MARK = 4;
	
	/** 경험치물약 에메랄드 자수정 토파즈 **/
	public static final int EMERALD_ICON = 86;
	
	/** 우호도 UI 표시 
	 * + 욕망의 동굴
	 * - 그림자 신전 
	 */	
	public static final int KARMA = 87;

	/** 스테이터스 닷지 표시 */
	public static final int INIT_DODGE = 88;

	/** 드래곤 혈흔 (안타:82 , 파푸:85) */
	public static final int DRAGONBLOOD = 100;	

	public static final int DODGE = 101;

	public static final int DRAGON_MENU = 102;

	/** 위치 전송 **/
	public static final int MAP_LOC_SEND = 111;
	
	/** 혈맹 창고리스트 */
	public static final int CLAN_WAREHOUSE_LIST = 117;// 
	
	/** 바포메트서버 패킷*/
	public static final int BAPO_BUFF = 114;

	public static final int ICON_SECURITY_SERVICES = 125; // 보안버프
	
	/** PC방버프 */
	public static final int PC_ROOM_BUFF = 127;

	public static final int ER_UPDATE = 132;
	
	public static final int WEAPON_DAMAGED_GFX = 138;// 무기 손상

	public static final int BOOKMARK_SIZE_PLUS_10 = 141;// 기억 확장
	public static final int BOOKMARK_ITEM = 142;
	
	/** 아이콘 표시 **/
	public static final int UNLIMITED_ICON = 147;
	
	/** 봉인 실시간 */
    public static final int ITEM_STATUS = 149;
	
	public static final int TIME_COUNT = 153;
	
	/** 나비켓의 castgfx 값의 버프이미지를 버프창에 표시 **/
	public static final int BUFFICON = 154;
	
	public static final int ROUND = 156;
	
	public static final int DUNGEON_TIME = 159; //던전 패킷
	
	/** 스킬 활성화 부분도 여기서 처리 **/
	public static final int ATTACK_RANGE = 160;
	
	/** 독관련 아이콘 표시 UI6 **/
	public static final int POSION_ICON = 161;
	
	/** 혈맹 버프 아이콘 */
    public static final int CLAN_BUFF_ICON = 165;
	
	public static final int ITEM_ENCHANT_UPDATE = 172;
	
	public static final int TOWN_TELEPORT = 176;
	
	public static final int SHOP_SEARCH_TELEPORT = 177;
	
	public static final int DRAGON_RAID_BUFF = 179;
	
	public static final int NONE_TIME_ICON = 180;
	
	public static final int BATTLE_SHOT = 181;
	
	public static final int WORLDMAP_UNKNOWN1 = 184;
	
	public static final int JUGUN_DAMAGE_BUFF = 188;// 주군의대미지버프
	
	public static final int INVENTORY_SAVE = 189;
		
	public static final int MOVE_BACK_STEP = 193;
	
	public static final int EFFECT_DURATOR = 194;//
	
	public static final int SHOP_OPEN_COUNT = 198;
	
	public static final int CHARACTER_WHEREHOUSE_SIZE = 202;// 특수 창고 
	
	public static final int COMBO_BUFF = 204;
		
	public static final int ITEM_ON_OFF = 205; //화살 온 오프
	
	
	public static final S_PacketBox WORLD_MAP_LOGIN			= new S_PacketBox(WORLDMAP_UNKNOWN1);// 케릭터 월드접속
	public static final S_PacketBox LOGIN_UNKNOWN			= new S_PacketBox(LOGIN_UNKNOWN3);// 케릭터 접속 세팅
	public static final S_PacketBox INVEN_SAVE				= new S_PacketBox(INVENTORY_SAVE);// 인벤토리 저장
	public static final S_PacketBox EMERALD_ICON_1			= new S_PacketBox(EMERALD_ICON, 0);
	public static final S_PacketBox EMERALD_ICON_2			= new S_PacketBox(EMERALD_ICON, 1);
	public static final S_PacketBox INIT_DODGE_LOGIN		= new S_PacketBox(INIT_DODGE, 0x0000);	// 로그인 dg
	public static final S_PacketBox DODGE_LOGIN				= new S_PacketBox(DODGE, 0);			// 로그인 dg
	public static final S_PacketBox JUGUN_BUFF_ON			= new S_PacketBox(NONE_TIME_ICON, 490, true);
	public static final S_PacketBox JUGUN_BUFF_OFF			= new S_PacketBox(NONE_TIME_ICON, 490, false);
	public static final S_PacketBox BAPHO_SYSTEM_BUFF		= new S_PacketBox(BAPO_BUFF, 6, true);// 바포버프
	public static final S_PacketBox CLAN_BUFF_ON			= new S_PacketBox(CLAN_BUFF_ICON, 1);
	public static final S_PacketBox CLAN_BUFF_OFF			= new S_PacketBox(CLAN_BUFF_ICON, 0);
	public static final S_PacketBox ANTARAS_BUFF			= new S_PacketBox(DRAGONBLOOD, 82, 7200 / 60);// 안타라스 혈흔 버프
	public static final S_PacketBox FAFURION_BUFF			= new S_PacketBox(DRAGONBLOOD, 85, 7200 / 60);// 파루리온 혈흔 버프
	public static final S_PacketBox EIN_PUPLE_ON			= new S_PacketBox(1800, 1, true, true);
	public static final S_PacketBox COUNTER_MAGIC_OFF		= new S_PacketBox(NONE_TIME_ICON, 393, false);
	public static final S_PacketBox ERASE_MAGIC_OFF			= new S_PacketBox(NONE_TIME_ICON, 55, false);
	public static final S_PacketBox WEAPONE_DAMAGE_OFF		= new S_PacketBox(WEAPON_DAMAGED_GFX, 0);
	public static final S_PacketBox DRAGON_PEARL_ON			= new S_PacketBox(NONE_TIME_ICON, 73, true);
	public static final S_PacketBox DRAGON_PEARL_OFF		= new S_PacketBox(NONE_TIME_ICON, 73, false);
	public static final S_PacketBox ICON_AURA_OFF			= new S_PacketBox(ICON_AURA);
	public static final S_PacketBox ICON_DEL				= new S_PacketBox(DEL_ICON);
	public static final S_PacketBox EXPOSE_WEAKNESS_DISABLE	= new S_PacketBox(EXPOSE_WEAKNESS, 0);// 약점 노출 제거
	public static final S_PacketBox EXPOSE_WEAKNESS_NORMAL	= new S_PacketBox(EXPOSE_WEAKNESS, 1);// 약점 노출 일반
	public static final S_PacketBox EXPOSE_WEAKNESS_BRAVE	= new S_PacketBox(EXPOSE_WEAKNESS, 2);// 약점 노출 브레이브
	public static final S_PacketBox EXPOSE_WEAKNESS_FORCE	= new S_PacketBox(EXPOSE_WEAKNESS, 3);// 약점 노출 포스
	public static final S_PacketBox WAKE					= new S_PacketBox(HADIN_DISPLAY, 2);// 진동
	public static final S_PacketBox LIGHTING				= new S_PacketBox(HADIN_DISPLAY, 7);// 번쩍임
	public static final S_PacketBox AURA_BUFF_ON			= new S_PacketBox(NONE_TIME_ICON, 479, true);
	public static final S_PacketBox AURA_BUFF_OFF			= new S_PacketBox(NONE_TIME_ICON, 479, false);
	public static final S_PacketBox POISON_ICON_ON			= new S_PacketBox(POSION_ICON, 1, 30);
	public static final S_PacketBox POISON_ICON_OFF			= new S_PacketBox(POSION_ICON, 0, 0);
	public static final S_PacketBox RUUN_INVITE_ON			= new S_PacketBox(NONE_TIME_ICON, 913, true);
	public static final S_PacketBox RUUN_INVITE_OFF			= new S_PacketBox(NONE_TIME_ICON, 913, false);
	public static final S_PacketBox SHOP_TELEPORT_SUCCESS	= new S_PacketBox(SHOP_SEARCH_TELEPORT, 1);
	public static final S_PacketBox SHOP_TELEPORT_FAIL		= new S_PacketBox(SHOP_SEARCH_TELEPORT, 128);

	public S_PacketBox(int subCode) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);

		switch (subCode) {
		case JUGUN_DAMAGE_BUFF:
			writeD(0);
			writeD(0);
			break;
		case WORLDMAP_UNKNOWN1:
			writeH(0);
			break;
		case INVENTORY_SAVE:
			//writeD(0x0d);
			break;
		case MSG_WAR_INITIATIVE:
		case MSG_WAR_OCCUPY:
		case MSG_MARRIED:
		case MSG_FEEL_GOOD:
		case MSG_CANT_LOGOUT:
		case LOGOUT:
		case ICON_SECURITY_SERVICES:
			break;
		case FISHING:
		case MINIGAME_TIME2:
			break;
		case CALL_SOMETHING:
			callSomething();
			break;
		case MINIGAME_10SECOND_COUNT:
			writeC(10);
			writeC(109);
			writeC(85);
			writeC(208);
			writeC(2);
			writeC(220);
			break;
		case DEL_ICON:
			writeH(0);
			break;
		case MINIGAME_END:
			writeC(147);
			writeC(92);
			writeC(151);
			writeC(220);
			writeC(42);
			writeC(74);
			break;
		case MINIGAME_START_COUNT:
			writeC(5);
			writeC(129);
			writeC(252);
			writeC(125);
			writeC(110);
			writeC(17);
			break;
		case ICON_AURA:
			writeC(0x98);
			writeC(0);
			writeC(0);
			writeC(0);
			writeC(0);
			writeC(0);
			break;
		case LOGIN_UNKNOWN3:
			/*
			 * 10 17 fc 3f 00 00 00 00 00 00 00 00 00 00 00 00 00
			 */
			writeC(0x10);// writeC(0x17);writeC(0xfc);writeC(0x3f);
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			break;
		default:
			break;
		}
	}
	
	/** 레벨업 버프 **/
	public S_PacketBox(int time, boolean ck, boolean ck2) {
		writeC(Opcodes.S_EVENT);
		writeC(0x56);
		writeC(0xAA);
		writeC(0x01);
		writeH(time >> 4);
		writeH(0x00);
	}
	
	public S_PacketBox(int subCode, int objId, int gfxId, boolean show) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
		case EFFECT_DURATOR:
			writeD(objId); // 오브젝트아이디
			writeD(gfxId); // 이미지 번호
			writeD(show ? 1 : 0); // 삭제 추가
			writeH(0);
			break;
		}
	}

	public S_PacketBox(int subCode, int time1, int time2, int time3, int time4) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
		case DUNGEON_TIME:// 12월14일변경
			writeD(7);
			writeD(1);
			writeS("$12125");// 기감
			writeD(time1);
			writeD(2);
			writeS("$6081");// 상아탑
			writeD(time2);
			writeD(15);
			writeS("$13527");// PC방 발록진영
			writeD(time3);
			writeD(500);
			writeS("$19375");// PC방 정무
			writeD(time4);
			writeD(49200);
			break;
		default:
			break;
		}
	}
	public S_PacketBox(int subCode, L1PcInstance pc){
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch(subCode){
		case TOWN_TELEPORT:
			writeC(0x01);
			writeH(pc.getX());
			writeH(pc.getY());
			break;
		case MOVE_BACK_STEP:
			writeH(pc.getX());
			writeH(pc.getY());
			break;
		case CALL_SOMETHING:
			mapCallSomething(pc);
			break;
		}
	}

	public S_PacketBox(int subCode, int value) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);

		switch (subCode) {
		case SHOP_SEARCH_TELEPORT:
			writeH(value);
			writeH(0);
			break;
		case EMERALD_ICON:
			writeH(value == 0 ? 0x90 : 0xb7);
			writeC(0);
			break;
		case COMBO_BUFF: //콤보시스템
		      writeH(value);
		      break;
		case PC_ROOM_BUFF:
			writeC(value == 1 ? 0x18 : 0);
			break;
		case SHOP_OPEN_COUNT:
			writeD(value);
			writeD(0x28);
			writeD(0x00);
			break;
		case ICON_BLUEPOTION:
		case ICON_CHATBAN:
		case ICON_I2H:
		case ICON_POLYMORPH:
		case MINIGAME_TIME:
		case INIT_DODGE:
			writeH(value); // time
			break;
		case WEAPON_DAMAGED_GFX:
			writeC(value);
			writeH(0);
			break;
		case ITEM_ON_OFF: //인벤아이템 착용 && 해제 이미지
		case BATTLE_SHOT:
			writeD(value);
			break;
		case DRAGON_RAID_BUFF:
			writeC(0x01);
			writeC(0x27);
			writeC(0x0E);
			writeD(value);// 남은초
			writeH(0x63EF);
			break;
		case MSG_WAR_BEGIN:
		case MSG_WAR_END:
		case MSG_WAR_GOING:
			writeC(value); // castle id
			writeH(0); // ?
			break;
		case WEIGHT:
			writeC(value);
			writeD(0);
			writeD(0);
			break;
		case MSG_SMS_SENT:
		case FOOD:
		case DODGE:
		case CHARACTER_WHEREHOUSE_SIZE:
			writeC(value);
			break;
		case MSG_ELF:
		case MSG_COLOSSEUM:
		case EXPOSE_WEAKNESS:
		case ER_UPDATE:
			writeC(value); // msg id
			break;
		case MSG_LEVEL_OVER:
			writeC(0); // ?
			writeC(value); // 0-49이외는 표시되지 않는다
			break;
		case COOK_WINDOW:
			writeC(0xdb); // ?
			writeC(0x31);
			writeC(0xdf);
			writeC(0x02);
			writeC(0x01);
			writeC(value); // level
			break;		
		case TIME_COUNT:
			writeD(value);
			//writeH(value);
			//writeH(0);		
			break;
		case MINIGAME_LIST:
			writeH(0x00); // 참여자수
			writeH(0x00); // 등수
			break;
		case HADIN_DISPLAY:
			writeD(value);
			writeH(0);
			break;
		case BOOKMARK_SIZE_PLUS_10:
			writeC(value);
			break;
		case ROUND:
		     writeD(value);
		     writeD(12);
			break;
		default:
			break;
		}
	}
	
	public S_PacketBox(int subCode, int itemid, String name, List<L1BookMark> list) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
		case BOOKMARK_ITEM: // 142
			writeC(0);
			writeD(itemid);
			writeS(name);
			writeC(list.size());
			for (L1BookMark book : list) {
				writeS(book.getName());// 이름
				writeD(book.getMapId());// 맵 아이디
				writeH(book.getLocX());// X
				writeH(book.getLocY());// Y
			}
			break;
		}
	}
	
	public S_PacketBox(int subCode, int type, int time, boolean second, boolean temp) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
		case BUFFICON:
			writeH(time);
			writeH(type);
			writeH(0x00);
			writeH(second ? 0x01 : 0x00); // 삭제 추가
			break;
		}// b0 04 80 08 00 00 00 00
	}
	
	public S_PacketBox(int subCode, int time, int gfxid, int type) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);	
		switch (subCode) {
		case BUFFICON:
			writeH(time); //시간
			writeD(gfxid); //아이콘
			writeC(type); //타입
			writeC(0x00);
			break;
		case EMERALD_ICON:	//경험치 물약
			writeC(0x3e);
			writeC(type); //1:on 2:off
			writeH(time); //지속시간
			writeC(0x00);
			writeC(0x00);
			writeC(gfxid);
			break;
		}
	}

	public S_PacketBox(int subCode, int type, int time) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {		
		case ICON_COOKING:// 요리 아이콘
			boolean isSoup = type == 7 || type == 160 || type == 218 || type == 162 || type == 229 || type == 233;
			writeH(9);
			writeH(51);
			writeH(21);
			writeH(10);
			writeH(14);
			writeH(8);
			writeH(isSoup ? 390 : 270);
			writeC(type);
			writeC(isSoup ? 38 : 36);//성장, 일반 구분
			writeH(time);
			writeC(0x39);
			break;
		case ICON_AURA: // 우호도버프  1:발록, 2:야히
			writeC(0xdd);
			writeH(time);
			writeC(type);
			break;
		case MSG_DUEL:
			writeD(type); 
			writeD(time);
			break;
		case POSION_ICON:// 161
			writeC(type); // 1독, 2패럴, 6사일
			if (type == 2) {
				writeH(0x00); // 포이즌 시간, 사일
				writeH(time); // 패럴 시간
			} else {
				writeH(time); // 포이즌 시간, 사일
				writeH(0x00); // 패럴 시간
			}
			break;
		case BUFFICON:
			writeH(time);
		    writeH(type);       
		    writeH(0);
		    break;
		case DRAGONBLOOD:
			writeC(type);
			writeD(time);  
			break;
		case ROUND:
			writeD(type); // 현재 라운드 표시
			writeD(time); // 총 라운드 표시
			break;
		case DRAGON_PEARL:
			//writeC(time);
			//writeC(type);
			writeH(time == -1 ? -1 : (int)((time + 3) >> 2));
			writeC(type);
			break;
		case EMERALD_ICON: // 드래곤의 에메랄드 아이콘
			writeC(0x70);
			writeC(0x01);
			writeC(type);
			writeH(time); // time(초)
			break;
			
		default:
			break;
		}
	}
	
	/** 자수정 토파즈 **/
	public S_PacketBox(int time, int val, boolean ck, boolean ck2) {
		writeC(Opcodes.S_EVENT);
		writeC(EMERALD_ICON);
		writeC(0x81);
		writeC(0x01);
		writeC(val);
		writeH(time);
	}// 7e 56 81 01 02 08 07
	
	public S_PacketBox(int i, int time, boolean ck, boolean ck2, boolean ck3) {
		writeC(Opcodes.S_EVENT);
		writeC(EMERALD_ICON);
		writeC(0x3e);
		writeC(i);
		writeH(time);
		writeC(0x14);
		writeC(0x86);
	}// 0f 56 3e 01 08 07 14 86

	public S_PacketBox(int subCode, String name) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
		case MSG_TOWN_LEADER:
			writeS(name);
			break;
		case GREEN_MESSAGE:
			writeC(2);
			writeS(name);
			break;
		default:
			break;
		}
	}

	public S_PacketBox(int subCode, int id, String name, String clanName) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
		case MSG_WIN_LASTAVARD:
			writeD(id); // 크란 ID인가 무엇인가?
			writeS(name);
			writeS(clanName);
			break;
		default:
			break;
		}
	}
	
    public S_PacketBox(int subCode, L1ItemInstance item, int type) {
        writeC(Opcodes.S_EVENT);
        writeC(subCode);
        switch (subCode) {
        case ITEM_STATUS:
            writeD(item.getId());
            writeC(type);
            writeC(0x18);
            writeH(0x00);
            
            writeD(0x00);
            writeD(0x00);
            writeD(item.getAttributeBitSetEx());
            writeD(0x00);
            writeD(item.getDeposit());
            writeD(0x00);
            break;
        }
    }
    
    public S_PacketBox(int subCode1, int subCode2, String name) {
    	writeC(Opcodes.S_EVENT);
    	writeC(subCode1);
    	switch (subCode2) {
    	case RED_MESSAGE:
    	case YELLOW_MESSAGE:
    		writeC(2);
    		writeH(26204);
    		writeC(subCode2);
    		writeS(name);
    		break;
    	case SCORE_MARK:
    		writeC(subCode2);
    		writeS(name);
    		break;
    	default: // ?
    		switch (subCode1) {
	    	case MSG_RANK_CHANGED:
	    		writeC(subCode2);
	    		writeS(name);
	    		break;
	    	}
    		break;
    	}
    }
	
	public S_PacketBox(int subCode, L1ItemInstance item){   
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
		case DRAGON_MENU:// 구형 드키 선택할때
			writeD(item.getId());
			writeC(item.getItemId() == 490012 ? 0x01: 0x00);// 안타0
			writeC(item.getItemId() == 490013 ? 0x01: 0x00);// 파푸1
			writeC(item.getItemId() == 490014 ? 0x01: 0x00);// 린드2
			writeC(0);
			break;
		case ITEM_ENCHANT_UPDATE:
			writeD(item.getId());
			writeC(0x18);
			writeC(0x00);
			writeH(0x00);
			writeH(0x00);
			writeC(item.getEnchantLevel());
			writeD(item.getId());
			writeD(0x00);
			writeD(0x00);
			writeD(item.getBless() >= 128 ? 3 : item.getItem().isTradable() ? 7 : 2);
			writeC(0x00);
			writeC(L1ItemInstance.getAttrEnchantBit(item.getAttrEnchantLevel()));
			writeH(0x00);
			break;
		default:
			break;
		}
	}

	public S_PacketBox(int subCode, L1PcInstance pc, int value1, int value2) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
		case POSION_ICON:
			writeC(value1); // type : 1.포이즌  6:사일런스 
			if (value1 == 2) {
				writeH(0x00);
				writeH(value2);
				writeH(0x00);
			} else {
				writeD(value2); // time (초)
			}
			break;
		default:
			break;
		}
	}

	public S_PacketBox(L1PcInstance pc, int subCode) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
		case CLAN_WAREHOUSE_LIST:
			int count = 0;
			Connection con = null;
			PreparedStatement pstm = null;
			PreparedStatement pstm2 = null;
			PreparedStatement pstm3 = null;
			ResultSet rs = null;
			ResultSet rs3 = null;
			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				
				pstm = con.prepareStatement("SELECT id, time FROM clan_warehouse_log WHERE clan_name='" + pc.getClanName() + "'");
				rs = pstm.executeQuery();
				while (rs.next()) {
					if (System.currentTimeMillis() - rs.getTimestamp(2).getTime() > 4320000) {// 3일
						pstm2 = con.prepareStatement("DELETE FROM clan_warehouse_log WHERE id='" + rs.getInt(1) + "'");
						pstm2.execute();
					} else {
						count++;
					}
				}
				writeD(count);
				pstm3 = con.prepareStatement("SELECT name, item_name, item_count, type, time FROM clan_warehouse_log WHERE clan_name='" + pc.getClanName() + "'");
				rs3 = pstm3.executeQuery();
				while (rs3.next()) {
					writeS(rs3.getString(1));
					writeC(rs3.getInt(4));// 0:맡김 1:찾음
					writeS(rs3.getString(2));
					writeD(rs3.getInt(3));
					writeD((int) (System.currentTimeMillis() - rs3.getTimestamp(5).getTime()) / 60000);				}
			} catch (SQLException e) {
			} finally {
				SQLUtil.close(rs);
				SQLUtil.close(rs3);
				SQLUtil.close(pstm);
				SQLUtil.close(pstm2);
				SQLUtil.close(pstm3);
				SQLUtil.close(con);
			}
			break;
		
		//case PLEDGE_REFRESH_PLUS:
		case PLEDGE_REFRESH_MINUS:
			writeS(pc.getName());
			writeC(pc.getBloodPledgeRank() == null ? 0 : pc.getBloodPledgeRank().toInt());
			writeH(0);
			break;
		case KARMA:
			writeD(pc.getKarma());
			break;
//		case ALLIANCE_LIST:
//			StringBuffer sb = new StringBuffer();
//			for (int i : pc.getClan().Alliance()) {
//				if (i == 0)
//					continue;
//				L1Clan c = L1World.getInstance().getClan(i);
//				if (c == null)
//					continue;
//				sb.append(c.getClanName() + StringUtil.EmptyOneString);
//			}
//			writeS(sb.toString());
//			break;
//		case PLEDGE_ONE:
//            writeD(clan.getOnlineMemberCount());
//            for (L1PcInstance targetPc : clan.getOnlineClanMember()) {
//                writeS(targetPc.getName());
//                writeC(targetPc.getClanRank());
//            }
//            writeD((int) (System.currentTimeMillis() / 1000L));
//            writeS(clan.getLeaderName());
//            break;
//		case PLEDGE_TWO:
//			writeD(clan.getClanMemberList().size());
//
//			ClanMember member;
//			ArrayList<ClanMember> clanMemberList = clan.getClanMemberList(); 
//			// 모든혈맹원의이름과등급
//			for (int i = 0; i < clanMemberList.size(); i++) {
//				member = clanMemberList.get(i);
//				writeS(member.name);
//				writeC(member.rank);
//			}
//
//			writeD(clan.getOnlineMemberCount());
//			for (L1PcInstance targetPc : clan.getOnlineClanMember()) { // 온라인
//				writeS(targetPc.getName());
//			}
//			break;
		default:
			break;
		}
	}

	public S_PacketBox(int subCode, String name, int mapid, int x, int y, int Mid) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
		case MAP_LOC_SEND:
			writeS(name);
			writeH(mapid);
			writeH(x);
			writeH(y);
			writeD(Mid);
			break;
		default: 
			break;
		}
	}

	public S_PacketBox(int subCode, int value, boolean show) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
		case BAPO_BUFF:
			writeD(value); // 1~7 깃발
			writeD(show ? 0x01 : 0x00); // On Off
			break;
		case UNLIMITED_ICON: // 무제한 패킷 
			writeC(show ? 0x01 : 0x00); // On Off // true false
			writeC(value); // 
			break;
		case NONE_TIME_ICON:
			writeC(show ? 0x01 : 0x00); // On Off // true false
			writeD(value); // 166exp30% 228시원한얼음조각 286exp40% 343기르타스지역사망패널티  409아머브레이크 497붉은기사의증표 이벤트공성존 //477~479, 841:월드공성변신
			writeD(0);
			writeH(0);
			break;
		default:
			break;
		}
	}
	
	public S_PacketBox(int subCode, boolean show) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch(subCode) {
		case CLAN_BUFF_ICON:
			writeC(show ? 0x01 : 0x00);
			break;
		}
	}
	
	public S_PacketBox(int subCode, L1PcInstance pc, L1ItemInstance weapon) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch(subCode) {
		case ATTACK_RANGE:
			//	1:sword, 2:dagger, 3:tohandsword, 4:bow, 5:spear, 6:blunt, 7:staff, 8:throwingknife, 
			//	9:arrow, 10:gauntlet, 11:claw, 12:edoryu, 13:singlebow, 14:singlespear, 15:tohandblunt, 
			//	16:tohandstaff 17:keyring, 18:chainsword
			int range = 1;// 최대 사거리
			int equipType = 0;// 검 1 / 도끼 2 / 활 3 / 이도류 4 / 창 5 / 스태프 6 / 단검 7 / 크로우,키링크 8 / 체인소드 10 / 9번이 뭘까?
			int isTohand = 0;// 양손 무기
			int spriteId = pc.getSpriteId();
			if (weapon != null && weapon.isEquipped()) {
				isTohand	= weapon.getItem().isTwohandedWeapon() ? 1 : 0;// 양손여부. 양손이면 1, 아니면 0
				switch(weapon.getItem().getWeaponType()){
				case SWORD:
				case TOHAND_SWORD:
					equipType = 1;
					break;
				case BLUNT:
				case TOHAND_BLUNT:
					equipType = 2;
					break;
				case BOW:
				case SINGLE_BOW:
				case GAUNTLET:
					range = pc._isBurningShot ? 10 : 15;
					equipType = 3;
					break;
				case EDORYU:
					equipType = 4;
					break;
				case SPEAR:
				case SINGLE_SPEAR:
					range = pc.spearPolyRange(spriteId);
					equipType = 5;
					break;
				case CHAINSWORD:
					range = pc.spearPolyRange(spriteId);
					equipType = range == 1 ? 1 : 10;
					break;
				case STAFF:
				case TOHAND_STAFF:
					equipType = 6;
					break;
				case DAGGER:
					equipType = 7;
					break;
				case CLAW:
					equipType = 8;
					break;
				case KEYRINGK:
					range = 4;
					equipType = 8;
					break;
				default:break;
				}	
			}
			pc.setAttackRange(range);
			writeC(range);// 거리
			writeC(equipType);// 무기타입	
			writeC(isTohand);// 양손여부
			break;
		}
	}
	

	private void callSomething() {
		Iterator<L1PcInstance> itr = L1World.getInstance().getAllPlayers().iterator();
		writeC(L1World.getInstance().getAllPlayers().size());
		L1PcInstance pc = null;
		Account acc = null;
		Calendar cal = null;
		while (itr.hasNext()) {
			pc = itr.next();
			acc = Account.load(pc.getAccountName());
			// 시간 정보 우선 로그인 시간을 넣어 본다655
			if (acc == null) {
				writeD(0);
			} else {
				cal = Calendar.getInstance(TimeZone.getTimeZone(Config.SERVER.TIME_ZONE));
				long lastactive = acc.getLastActive().getTime();
				cal.setTimeInMillis(lastactive);
				cal.set(Calendar.YEAR, 1970);
				int time = (int) (cal.getTimeInMillis() / 1000);
				writeD(time); // JST 1970 1/1 09:00 이 기준
			}
			// 캐릭터 정보
			writeS(pc.getName()); // 반각 12자까지
			writeS(pc.getClanName()); // []내에 표시되는 캐릭터 라인.반각 12자까지
		}
	}
	
	private void mapCallSomething(L1PcInstance gm) {
		Iterator<L1PcInstance> itr = L1World.getInstance().getAllPlayers().iterator();
		int count = 0;
		for (L1Object each1 : L1World.getInstance().getVisibleObjects(gm.getMapId()).values()) {
			if (each1 instanceof L1PcInstance) {
				L1PcInstance eachpc = (L1PcInstance) each1;
				if (eachpc != null && !eachpc.noPlayerCK) {
					count++;
				}
			}
		}
		writeC(count);
		L1PcInstance pc = null;
		Account acc = null;
		Calendar cal = null;
		while(itr.hasNext()){
			pc = itr.next();
			if (pc.getMapId() == gm.getMapId() && !pc.noPlayerCK) {
				acc = Account.load(pc.getAccountName());
				// 시간 정보 우선 로그인 시간을 넣어 본다655
				if (acc == null) {
					writeD(0);
				} else {
					cal = Calendar.getInstance(TimeZone.getTimeZone(Config.SERVER.TIME_ZONE));
					long lastactive = acc.getLastActive().getTime();
					cal.setTimeInMillis(lastactive);
					cal.set(Calendar.YEAR, 1970);
					int time = (int) (cal.getTimeInMillis() / 1000);
					writeD(time); // JST 1970 1/1 09:00 이 기준
				}
				// 캐릭터 정보
				writeS(pc.getName()); // 반각 12자까지
				writeS(pc.getClanName()); // []내에 표시되는 캐릭터 라인.반각 12자까지
			}
		}
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}
	@Override
	public String getType() {
		return S_PACKETBOX;
	}
}

