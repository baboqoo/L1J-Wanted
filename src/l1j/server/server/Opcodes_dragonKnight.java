package l1j.server.server; 

import java.lang.reflect.Field;
import java.util.HashMap;

// 2022.04.08 옵코드
public class Opcodes_dragonKnight {

	// client code
	public static final int C_SERVER_SELECT				= 0x03;
	public static final int C_FAR_ATTACK				= 0x04;// 활공격 부분
	public static final int C_ASK_XCHG					= 0x05;// [교환]
	public static final int C_BOARD_LIST				= 0x06;// 게시판 리스트 (현재 미사용)
	public static final int C_EXCLUDE					= 0x07;// 차단 (현재 미사용)
	public static final int C_CHECK_PK					= 0x09;// [checkpk]
	public static final int C_RANK_CONTROL				= 0x0a;// [계급]
	public static final int C_MERCENARYSELECT			= 0x0e;
	public static final int C_SAY						= 0x10;
	public static final int C_LOGIN_RESULT				= 0x12;
	public static final int C_CHANGE_PASSWORD			= 0x13;
	public static final int C_UPLOAD_EMBLEM				= 0x14;// 문장데이타를 서버에 요청함
	public static final int C_MARRIAGE					= 0x15;// [청혼]
	public static final int C_GET						= 0x17;// 아이템 줍기.
	public static final int C_BUYABLE_SPELL				= 0x18;// 스킬 구입 리스트
	public static final int C_CREATE_PLEDGE				= 0x1a;// 혈맹 창설 (현재 미사용)
	public static final int C_READ_NOTICE				= 0x1b;// 로그인 후 패킷
	public static final int C_EMBLEM					= 0x1c;// 가시범위의 혈맹 마크 요청[폴더내 emblem삭제]
	public static final int C_RESTART					= 0x1d;// 겜중에 리스창으로 빠짐. [ C_CHARACTERCONFIG 다음에 날라옴 ]
	public static final int C_TELEPORT_USER				= 0x1f;
	public static final int C_ANSWER					= 0x21;// [ Y , N ] 선택 부분
	public static final int C_CREATE_CUSTOM_CHARACTER	= 0x23;// 케릭 생성
	public static final int C_DEPOSIT					= 0x24;// 성 공금 입금 (현재 미사용)
	public static final int C_NPC_ITEM_CONTROL			= 0x25;
	public static final int C_TAX						= 0x29;// 세금 조정 (현재 미사용)
	public static final int C_INVITE_PARTY_TARGET		= 0x2a;// 파티 초대 (현재 미사용 )
	public static final int C_ATTACK					= 0x2b;// 일반공격 부분
	public static final int C_SLAVE_CONTROL				= 0x2d;// 펫 공격 목표 지정
	public static final int C_BAN						= 0x2e;
	public static final int C_ACTION					= 0x32;// <알트+1 ~ 5 까지 액션 >
	public static final int C_DIALOG					= 0x33;// Npc와 대화부분
	public static final int C_NEW_ACCOUNT				= 0x34;
	public static final int C_EXTENDED					= 0x35;// 아덴상점 아이콘 클릭, 부가 아이템, 혈맹창
	public static final int C_CHANNEL					= 0x37;// 불량 유저 신고(신고)
	public static final int C_QUERY_BUDDY				= 0x38;// 친구리스트
	public static final int C_DEAD_RESTART				= 0x3a;// 겜중에 죽어서 리셋 눌럿을때
	public static final int C_WITHDRAW					= 0x3b;// 공금 출금[자금을 인출한다]
	public static final int C_LEAVE_SHIP				= 0x3c;// 배 내리기 (현재 미사용)
	public static final int C_CHANGE_CASTLE_SECURITY	= 0x3f;// 성내 치안 관리 (현재 미사용)
	public static final int C_ADDR						= 0x40;
	public static final int C_BANISH_PARTY				= 0x41;// 파티 추방 (현재 미사용)
	public static final int C_TELL						= 0x43;
	public static final int C_QUIT						= 0x44;// 종료
	public static final int C_WAREHOUSE_CONTROL			= 0x47;// 창고 비번.
	public static final int C_LOGIN_TEST				= 0x48;
	public static final int C_CHANGE_DIRECTION			= 0x4f;// 방향전환
	public static final int C_DELETE_CHARACTER			= 0x51;// 케릭터 삭제
	public static final int C_INVITE_PARTY				= 0x52;// 파티 초대 (현재 미사용)
	public static final int C_BOOKMARK					= 0x54;// [기억 OO]
	public static final int C_LOGIN						= 0x55;// v 계정정보가 담긴 패킷(클라이언트 로그인 방식 현재 미사용)
	public static final int C_QUERY_PERSONAL_SHOP		= 0x56;// 개인상점 buy, sell
	public static final int C_SHOP_WAREHOUSE_RESULT		= 0x59;// 상점 결과 처리
	public static final int C_BOARD_WRITE				= 0x5b;// 게시판 작성 (현재 미사용)
	public static final int C_BOARD_READ				= 0x5d;// 게시판 읽기 (현재 미사용)
	public static final int C_MAIL						= 0x5f;// 편지 읽기
	public static final int C_ALT_ATTACK				= 0x60;
	public static final int C_DROP						= 0x61;// 아이템 떨구기
	public static final int C_PERSONAL_SHOP				= 0x62;// 개인상점 (현재 미사용)
	public static final int C_REGISTER_QUIZ				= 0x64;
	public static final int C_ENTER_PORTAL				= 0x65;// (ENTER_PORTAL)
	public static final int C_CHANGE_ACCOUNTINFO		= 0x68;
	public static final int C_BAN_MEMBER				= 0x69;// 혈맹 추방 명령어
	public static final int C_DELETE_BOOKMARK			= 0x6a;// [기억 후 기억목록클릭 delete]
	public static final int C_WHO_PARTY					= 0x6c;// 찾음[본섭] (현재 미사용)
	public static final int C_SELECT_TIME				= 0x6d;
	public static final int C_READ_NEWS					= 0x6e;// 공지사항 확인 눌럿을때
	public static final int C_ACCEPT_XCHG				= 0x6f;// 교환 OK
	public static final int C_LEAVE_PLEDGE				= 0x70;// 혈맹 탈퇴
	public static final int C_DUEL						= 0x76;// [결투]
	public static final int C_LEAVE_PARTY				= 0x77;// 파티 탈퇴
	public static final int C_CHAT_PARTY_CONTROL		= 0x78;// 채팅파티채팅초대 (현재 미사용)
	public static final int C_PLATE						= 0x79;
	public static final int C_VERSION					= 0x7a;// 서버버전 (현재 미사용)
	public static final int C_BUY_SPELL					= 0x7b;// 스킬 구입 OK
	public static final int C_REMOVE_BUDDY				= 0x7c;// 친구삭제
	public static final int C_EXCHANGEABLE_SPELL		= 0x7d;// 호런 마법 리스트 (리마부터 html 먹통 확인불가)
	public static final int C_BOOK						= 0x7e;// 찾음[본섭] (현재 미사용)
	public static final int C_SAVEIO					= 0x80;// 캐릭인벤슬롯정보
	public static final int C_ATTACK_CONTINUE			= 0x82;// 자동칼질
	public static final int C_OPEN						= 0x83;// 문짝 클릭 부분.
	public static final int C_CONTROL_WEATHER			= 0x84;
	public static final int C_WHO_PLEDGE				= 0x87;// [혈맹]
	public static final int C_GIVE						= 0x89;// 강제로 아이템 주기
	public static final int C_ADD_BUDDY					= 0x8a;// 친구추가
	public static final int C_BLINK						= 0x8b;
	public static final int C_SHUTDOWN					= 0x8c;// 혈맹창 공지, 메모
	public static final int C_JOIN_PLEDGE				= 0x8d;
	public static final int C_START_CASTING				= 0x90;
	public static final int C_THROW						= 0x92;// 낚시 종료
	public static final int C_EXIT_GHOST				= 0x93;// 무한대전 관람모드 탈출
	public static final int C_MERCENARYNAME				= 0x94;
	public static final int C_SAVE						= 0x96;
	public static final int C_EXTENDED_PROTOBUF			= 0x9d;// 종합 패킷
	public static final int C_CHECK_INVENTORY			= 0xa0;
	public static final int C_BUILDER_CONTROL			= 0xa1;
	public static final int C_SUMMON					= 0xa4;// CALL버튼 .감시
	public static final int C_SELECTABLE_TIME			= 0xa7;
	public static final int C_ENTER_WORLD				= 0xa8;// 리스창에서 케릭 선택
	public static final int C_MERCENARYARRANGE			= 0xa9;
	public static final int C_CHAT						= 0xaa;// 채팅 (현재 미사용 )
	public static final int C_QUERY_CASTLE_SECURITY		= 0xab;// 성내 치안관리 (현재 미사용)
	public static final int C_WHO						= 0xae;// [누구]
	public static final int C_MOVE						= 0xaf;// 이동요청 부분
	public static final int C_ALIVE						= 0xb0;// 1분마다 한번씩 옴
	public static final int C_MONITOR_CONTROL			= 0xb2;
	public static final int C_ONOFF						= 0xb8;// [환경설정->전챗켬,끔]
	public static final int C_ENTER_SHIP				= 0xb9;// 배타기
	public static final int C_HYPERTEXT_INPUT_RESULT	= 0xbc;// 수량성 아이템 제작 갯수
	public static final int C_ARCHERARRANGE				= 0xbe;
	public static final int C_RESTART_COMPLETE			= 0xc2;// 리스시 한번더 옴
	public static final int C_CANCEL_XCHG				= 0xc3;// 교환 취소
	public static final int C_WAR						= 0xc5;// 전쟁
	public static final int C_DESTROY_ITEM				= 0xc6;// 휴지통에 아이템 삭제
	public static final int C_RETURN_SUMMON				= 0xc8;// 텔레포트 사용
	public static final int C_PLEDGE_WATCH				= 0xc9;// 문장 주시 혈맹 목록
	public static final int C_CLIENT_READY				= 0xca;// 찾음[본섭]  (현재 미사용)
	public static final int C_EXTENDED_HYBRID			= 0xcf;// 찾음[본섭] (현재 미사용)
	public static final int C_USE_ITEM					= 0xd0;// 아이템 사용 부분
	public static final int C_EXCHANGE_SPELL			= 0xd2;// 호런 마법목록에서 OK누르기(리마부터 html 먹통 확인불가)
	public static final int C_TITLE						= 0xd6;// 호칭 명령어
	public static final int C_USE_SPELL					= 0xd8;// 스킬 사용 부분
	public static final int C_INCLUDE					= 0xd9;
	public static final int C_MATCH_MAKING				= 0xdd;// 찾음[본섭] (현재 미사용)
	public static final int C_BOARD_DELETE				= 0xde;// 게시판 삭제 (현재 미사용)
	public static final int C_SILENCE					= 0xdf;
	public static final int C_SMS						= 0xe0;
	public static final int C_TELEPORT					= 0xe2;// S_TELEPORT에서 호출됨(현재 미사용)
	public static final int C_HACTION					= 0xe3;// Npc 대화 액션 부분
	public static final int C_FIX						= 0xe4;// 무기수리, 펫인출
	public static final int C_WANTED					= 0xe7;
	public static final int C_VOICE_CHAT				= 0xe8;// 스텟 초기화.
	public static final int C_FIXABLE_ITEM				= 0xe9;// 무기수리리스트
	public static final int C_LOGOUT					= 0xeb;// 다시 로긴창으로 넘어갈때 (현재 미사용)
	public static final int C_REQUEST_ROLL				= 0xec;
	public static final int C_ADD_XCHG					= 0xf7;// 교환창에 아이템 추가
	public static final int C_KICK						= 0xfc;
	public static final int C_MERCENARYEMPLOY			= 0xfd;// 용병구매 (현재 미사용)
	public static final int C_WISH						= 0xfe;
	
///////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// server code
	public static final int S_AGIT_LIST					= 0x00;// 아지트 리스트
	public static final int S_MASTER					= 0x01;
	public static final int S_ADD_BOOKMARK				= 0x02;// 기억 리스트
	public static final int S_MAGE_STRENGTH				= 0x04;// 힘업
	public static final int S_ATTACK					= 0x05;// 공격 표현 부분
	public static final int S_ROLL_RESULT				= 0x06;
	public static final int S_CHANGE_ITEM_TYPE			= 0x08;
	public static final int S_WITHDRAW					= 0x0c;// 공금 출금 (현재 미사용)
	public static final int S_MERCENARYARRANGE			= 0x0d;// 선택한 용병 주기 (현재 미사용)
	public static final int S_DECREE					= 0x0e;
	public static final int S_CHANGE_COUNT				= 0x0f;
	public static final int S_POLYMORPH					= 0x10;// 변신
	public static final int S_BREATH					= 0x11;// 에바 아이콘
	public static final int S_AGIT_MAP					= 0x12;
	public static final int S_EXTENDED_PROTOBUF			= 0x13;// 종합 패킷
	public static final int S_ADD_SPELL					= 0x15;
	public static final int S_SLAVE_CONTROL				= 0x16;// 펫 공격 목표지정
	public static final int S_CRIMINAL					= 0x18;// 보라돌이
	public static final int S_MAGIC_STATUS				= 0x19;// sp와 mr변경
	public static final int S_ADD_INVENTORY				= 0x1e;
	public static final int S_CHARACTER_INFO			= 0x1f;// v 케릭터리스트의 케릭정보
	public static final int S_PARALYSE					= 0x21;// 행동 제한 (커스패럴 상태) 사막 스콜피온한테 가서 커스당하기
	public static final int S_TITLE						= 0x24;// 호칭 변경
	public static final int S_CHANGE_ATTR				= 0x26;// 위치값을 이동가능&불가능 조작 부분
	public static final int S_EXTENDED_HYBRID			= 0x27;
	public static final int S_SELL_LIST					= 0x28;// 상점에 판매 부분
	public static final int S_EXP						= 0x29;// 경험치 갱신
	public static final int S_CHANGE_PASSWORD_CHECK		= 0x2a;
	public static final int S_VOICE_CHAT				= 0x2b;// v 스텟 초기화 길이
	public static final int S_BUYABLE_SPELL_LIST		= 0x2c;// 스킬 구입 창
	public static final int S_WARNING_CODE				= 0x2d;
	public static final int S_SERVER_LIST				= 0x2e;
	public static final int S_HIT_POINT					= 0x2f;// HP 업데이트
	public static final int S_DEPOSIT					= 0x31;// 공금 입금 (현재 미사용)
	public static final int S_REMOVE_OBJECT				= 0x33;// 오브젝트 삭제 (토글etc)
	public static final int S_REQUEST_SUMMON			= 0x34;// 텔레포트[손바닥]
	public static final int S_MERCENARYNAME				= 0x39;
	public static final int S_CHANGE_LEVEL				= 0x3c;
	public static final int S_SAY_CODE					= 0x3d;// 샤우팅 글
	public static final int S_WAR						= 0x3f;// 전쟁[혈맹끼리 전쟁]
	public static final int S_MERCENARYEMPLOY			= 0x42;// 성 용병 고용 (현재 미사용)
	public static final int S_CHANGE_ACCOUNTINFO_CHECK	= 0x43;
	public static final int S_MAGE_SHIELD				= 0x45;// 쉴드
	public static final int S_CHANGE_ITEM_BLESS			= 0x46;// 봉인 주문서, 봉인줌서 사서 상아탑무기에 바르기
	public static final int S_CHANGE_DESC				= 0x47;// S_ChangeShape 통합됨 (현재 미사용)
	public static final int S_PLEDGE_WATCH				= 0x4e;// 혈맹 문장 주시
	public static final int S_LOGIN_CHECK				= 0x51;// 포탈이동시 날라오는 패킷(호런동굴들어갈때)확실치않음
	public static final int S_REMOVE_SPELL				= 0x52;
	public static final int S_NEW_ACCOUNT_CHECK			= 0x53;
	public static final int S_CHANGE_ALIGNMENT			= 0x54;// 라우풀
	public static final int S_ASK						= 0x55;// [ Y , N ] 메세지
	public static final int S_EFFECT					= 0x56;// 이팩트 부분 (헤이스트등)
	public static final int S_XCHG_START				= 0x58;// 거래창 부분
	public static final int S_RESTART					= 0x59;// 찾음[본섭]
	public static final int S_SOUND_EFFECT				= 0x5b;// 사운드 이팩트 부분[펫 호루라기로 찾기]
	public static final int S_EMOTION					= 0x5c;// 용기
	public static final int S_SPEED						= 0x5d;// 헤이스트
	public static final int S_TAX						= 0x5f;// 세율 조정 (현재 미사용)
	public static final int S_REMOVE_INVENTORY			= 0x62;// 인벤토리 아이템 삭제
	public static final int S_MAIL_INFO					= 0x63;// 편지 읽기.
	public static final int S_MESSAGE_CODE				= 0x66;// 서버 메세지[방어구중복으로체크]
	public static final int S_SAY						= 0x68;
	public static final int S_AC						= 0x6a;// AC 및 속성방어 갱신 [블레스트 아머 사용시 나옴]
	public static final int S_VERSION_CHECK				= 0x6b;
	public static final int S_EVENT						= 0x6c;// v 통합 패킷 관리 담당
	public static final int S_EXTENDED					= 0x6d;// 아덴상점생존의 외침 등등
	public static final int S_PLEDGE					= 0x6e;// 클랜 업데이트, 아무 혈맹이나 가입하기
	public static final int S_GM_MESSAGE				= 0x70;// 매니저용 귓말
	public static final int S_BLIND						= 0x72;// 눈멀기 효과
	public static final int S_STATUS					= 0x74;// 케릭 정보 갱신
	public static final int S_ENTER_WORLD_CHECK			= 0x75;// 접속담당
	public static final int S_CHANGE_DIRECTION			= 0x78;// 방향 전환 부분 [움직이는 엔피씨에 말걸어서 체크]
	public static final int S_CHANGE_ABILITY			= 0x7a;// 이반, 소반 인프라 사용
	public static final int S_RETRIEVE_LIST				= 0x7f;// 창고리스트
	public static final int S_CLIENT_READY				= 0x80;
	public static final int S_NOT_ENOUGH_FOR_SPELL		= 0x81;// 마법을 배우기 위해 기부할 재료가 부족합니다.
	public static final int S_BUY_LIST					= 0x83;// 상점 구입 부분[BUY]
	public static final int S_MATCH_MAKING				= 0x85;// 찾음[본섭] (현재 미사용)
	public static final int S_FIXABLE_ITEM_LIST			= 0x88;// 무기수리 리스트
	public static final int S_ATTACK_MANY				= 0x89;// 파톰 어퀘등의 스킬
	public static final int S_WORLD						= 0x8d;// 물속 호흡
	public static final int S_DELETE_CHARACTER_CHECK	= 0x8f;// 케릭 삭제
	public static final int S_HYPERTEXT					= 0x90;// Npc클릭 Html열람
	public static final int S_ADD_XCHG					= 0x91;// 거래창 아이템 추가 부분 (현재 미사용)
	public static final int S_BOARD_LIST				= 0x98;// 게시판 클릭 (현재 미사용)
	public static final int S_MESSAGE					= 0x99;// 시스템 메세지 (장사 채팅)
	public static final int S_READ_MAIL					= 0x9b;// 찾음[본섭]  (현재 미사용)
	public static final int S_TIME						= 0x9c;// 게임 시간
	public static final int S_MERCENARYSELECT			= 0x9e;// 고용한 용병을 배치 (현재 미사용)
	public static final int S_INVISIBLE					= 0xa0;// 투명
	public static final int S_EMBLEM					= 0xa1;// 클라에 혈문장 요청
	public static final int S_CHANGE_ITEM_DESC_EX		= 0xa4;// 인벤 아이템 갱신
	public static final int S_SELECTABLE_TIME_LIST		= 0xa6;// 공성시간 지정 (현재 미사용)
	public static final int S_XCHG_RESULT				= 0xa7;// 거래 취소, 완료
	public static final int S_CLONE						= 0xac;
	public static final int S_EXCHANGEABLE_SPELL_LIST	= 0xaf;// 호런 마법 배우기창
	public static final int S_WEATHER					= 0xb0;// 날씨 조작하기
	public static final int S_NEWS						= 0xb3;// 공지
	public static final int S_IDENTIFY_CODE				= 0xb5;// 확인주문서
	public static final int S_PUT_OBJECT				= 0xba;
	public static final int S_BLINK						= 0xbd;// 손바닥(바로 C패킷 호출됨 현재 미사용)
	public static final int S_NUM_CHARACTER				= 0xbe;// v 해당 계정의 케릭 갯수
	public static final int S_WIELD						= 0xc1;// 무기 착,탈 부분
	public static final int S_CHANGE_ITEM_DESC			= 0xc2;// 아이템 착용 (E표시) 0x62? 0x75?
	public static final int S_MAGE_DEXTERITY			= 0xc4;// 덱스업
	public static final int S_RESURRECT					= 0xc5;// 부활 처리 부분
	public static final int S_PING						= 0xc6;// PING체크
	public static final int S_PORTAL					= 0xca;// 포털 이동 텔레포트 요청
	public static final int S_NEW_CHAR_INFO				= 0xcc;// 케릭 새로 만든거 보내기
	public static final int S_CHANGE_LIGHT				= 0xce;// 밝기
	public static final int S_MOVE_OBJECT				= 0xd3;// 이동 오브젝트
	public static final int S_NOTICE					= 0xd4;// 로그인후 패킷
	public static final int S_POISON					= 0xd5;// 독과 굳은 상태 : 표현 비취나 큐어포이즌 써보면 됨
	public static final int S_CREATE_CHARACTER_CHECK	= 0xd7;// 캐릭터 생성시 처리부분
	public static final int S_ADD_INVENTORY_BATCH		= 0xd8;
	public static final int S_EFFECT_LOC				= 0xdb;// 트랩 (좌표위 이펙트)  포우 슬레이어로 따면 됨
	public static final int S_ABILITY_SCORES			= 0xdf;// 스테이터스 갱신(디크리즈,민투)
	public static final int S_HYPERTEXT_INPUT			= 0xe0;// 수량성 아이템 제작 갯수 [여관]  확실치 않음 찾아야함
	public static final int S_WANTED_LOGIN				= 0xe1;
	public static final int S_ACTION					= 0xe2;// 액션 부분(맞는모습등)
	public static final int S_COMMAND_TARGET			= 0xe4;
	public static final int S_DRUNKEN					= 0xe5;// 술, 드래곤진주
	public static final int S_KEY						= 0xe7;// 최초 패킷
	public static final int S_CHANGE_ITEM_USE			= 0xed;
	public static final int S_BOOK_LIST					= 0xee;
	public static final int S_ATTACK_ALL				= 0xf0;
	public static final int S_KICK						= 0xf5;// 해당 케릭 강제 종료
	public static final int S_BOARD_READ				= 0xf8;// 게시판 읽기 (현재 미사용)
	public static final int S_ARCHERARRANGE				= 0xf9;
	public static final int S_HIT_RATIO					= 0xfa;// 미니 HP표현 부분
	public static final int S_CASTLE_OWNER				= 0xfc;// 성소유목록 세팅
	public static final int S_MANA_POINT				= 0xfe;// MP 업데이트
	
///////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// first code
	public static final int FIRST_KEY					= S_KEY;// 최초 패킷
	
	// 오픈대기 차단 패킷
	private static final java.util.List<Integer> STANBY_BLOCK_CODES = java.util.Arrays.asList(new Integer[]{
			C_ATTACK, C_FAR_ATTACK, C_ATTACK_CONTINUE,	// 공격
			C_ASK_XCHG,									// 교환
			C_DROP,										// 아이템 떨구기
			C_GET,										// 아이템 줍기
			C_GIVE,										// 아이템 주기(몬스터)
			C_USE_SPELL,								// 스킬
			C_SHOP_WAREHOUSE_RESULT,					// 상점 창고 이용
	});
	
	// 인터서버 접속처리시 차단 패킷
	private static final java.util.List<Integer> INTER_CONNECT_BLOCK_CODES = java.util.Arrays.asList(new Integer[]{
			C_MOVE,
			C_ATTACK, C_FAR_ATTACK, C_ATTACK_CONTINUE,
			C_ASK_XCHG,
			C_DROP,
			C_GET,
			C_GIVE,
			C_USE_SPELL
	});
	
	/**
	 * 오픈대기 상태에서 차단되는 패킷 코드
	 * @param code
	 * @return boolean
	 */
	public static boolean isStanbyBlockCode(int code){
		return STANBY_BLOCK_CODES.contains(code);
	}
	
	/**
	 * 인터서버 접속중인 상태에서 차단되는 패킷 코드
	 * @param code
	 * @return boolean
	 */
	public static boolean isInterConnectBlockCode(int code){
		return INTER_CONNECT_BLOCK_CODES.contains(code);
	}
	
	static final HashMap<Integer, String> C_OPCODE_LIST, S_OPCODE_LIST;
	static {
		C_OPCODE_LIST = new HashMap<>();
		S_OPCODE_LIST = new HashMap<>();
		try {
			Opcodes op = new Opcodes();
			Field[] fieldArray = op.getClass().getFields();
			for (Field field : fieldArray) {
				String name = field.getName();
				if (name.startsWith("C_") || name.startsWith("S_")) {
					int opcode = (int) field.get(op.getClass());
					if (name.startsWith("C_")) {
						C_OPCODE_LIST.put(opcode, field.getName());
					} else if (name.startsWith("S_")) {
						S_OPCODE_LIST.put(opcode, field.getName());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String get_c_opcode_name(int code) {
		return C_OPCODE_LIST.get(code);
	}
	
	public static String get_s_opcode_name(int code) {
		return S_OPCODE_LIST.get(code);
	}
}

