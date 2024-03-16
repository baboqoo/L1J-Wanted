package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.templates.L1Item;

public class TeleportBook extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	private static final int[][] TOWN_BOOK = {
		{ 34060, 32281, 4 },	// 오렌
		{ 33079, 33390, 4 },	// 은기사
		{ 32750, 32439, 4 },	// 오크숲
		{ 32612, 33188, 4 },	// 윈다우드
		{ 33720, 32492, 4 },	// 웰던
		{ 32872, 32912, 304 },	// 침묵의 동굴
		{ 32612, 32781, 4 },	// 글루디오
		{ 33067, 32803, 4 },	// 켄트
		{ 33933, 33358, 4 },	// 아덴
		{ 33601, 33232, 4 },	// 하이네
		{ 32574, 32942, 0 },	// 말하는 섬
		{ 33430, 32815, 4 }, 	// 기란
	};
	
	private static final int[][] OMAN_BOOK = { 
		{ 32735, 32798, 101 },	// 오만1
		{ 32730, 32802, 102 },	// 오만2
		{ 32726, 32803, 103 },	// 오만3
		{ 32621, 32858, 104 },	// 오만4
		{ 32599, 32866, 105 },	// 오만5
		{ 32611, 32862, 106 },	// 오만6
		{ 32618, 32866, 107 },	// 오만7
		{ 32600, 32866, 108 },	// 오만8
		{ 32612, 32866, 109 },	// 오만9
		{ 32729, 32802, 110 },	// 오만10
	};
	
	private static final int[][] JOW_BOOK = { 
		{ 00000, 00000, 0 },	// 저 레벨 추천사냥터
			
		{ 32680, 32851, 9 },	// 말섬 북쪽섬 
		{ 32491, 32858, 9 },	// 말섬 던전 입구
		{ 32382, 32947, 9 },	// 말섬 오크 망루 지대
		{ 32707, 33147, 9 },	// 말섬 흑기사 전초 기지
		{ 32882, 32652, 4 },	// 본토 죽음의 폐허
		{ 32886, 32917, 4 },	// 본토 망자의 무덤
		{ 32811, 32724, 807 },	// 글루디오 던전 1층
		{ 32713, 32316, 4 },	// 본토 오크 부락
		{ 32724, 33138, 4 },	// 본토 사막(샌드웜)
		{ 32805, 32724, 19 },	// 요정 숲 던전 1층
			
		{ 00000, 00000, 0 },	// 중 레벨 추천 사냥터
			
		{ 33799, 32776, 4 },	// 본토 밀림 지대
		{ 33167, 32625, 4 },	// 본토 흑기사 출몰 지역
		{ 32766, 32796, 20 },	// 요정 숲 던전 2층
		{ 33768, 33408, 4 },	// 본토 거울의 숲
		{ 33159, 32969, 4 },	// 본토 암흑용의 상흔
		{ 34121, 32803, 4 },	// 본토 풍룡의 둥지입구
		{ 34122, 32190, 4 },	// 본토 얼음 설벽 입구
		{ 32764, 32842, 77 },	// 상아탑 4층 입구
		{ 32810, 32810, 30 },	// 용의 계곡 던전 1층
		{ 32709, 32817, 32 },	// 용의 계곡 던전 3층
			
		{ 00000, 00000, 0 },	// 고 레벨 추천 사냥터
			
		{ 33430, 32827, 4 },	// 기란 감옥 입구
		{ 32744, 32802, 35 },	// 용의 계곡 던전 5층
		{ 32763, 32774, 810 },	// 글루디오 던전 4층
		{ 33337, 32466, 4 },	// 본토 용의 계곡 입구
		{ 34051, 32561, 4 },	// 본토 엘모어 격전지
		{ 32804, 32726, 812 },	// 글루디오 던전 6층
		{ 33636, 32420, 4 },	// 본토 화룡의 둥지 입구
		{ 32804, 32267, 4 },	// 본토 아투바 오크 은신처 입구
		{ 32876, 32652, 4 },	// 본토 암흑룡의 던전 입구
		{ 34387, 32303, 4 },	// 본토 저주받은 광산
		{ 34469, 32191, 4 }	// 본토 루운 광장 
	};
	
	private static final int[][] OMAN_AMULET = { 
		{ 32735, 32798, 101 },	// 오만1
		{ 32730, 32802, 102 },	// 오만2
		{ 32726, 32803, 103 },	// 오만3
		{ 32621, 32858, 104 },	// 오만4
		{ 32599, 32866, 105 },	// 오만5
		{ 32611, 32862, 106 },	// 오만6
		{ 32618, 32866, 107 },	// 오만7
		{ 32600, 32866, 108 },	// 오만8
		{ 32612, 32866, 109 },	// 오만9
		{ 32729, 32802, 110 },	// 오만10
		//{ 32638, 32803, 111 },// 오만정상 시작지점
		//{ 32804, 32962, 111 },// 오만정상 중간지점
	};
	
	private static final int[][] JIBAE_AMULET = { 
		{ 32727, 32794, 12852 },	// 지배1
		{ 32727, 32794, 12853 },	// 지배2
		{ 32727, 32794, 12854 },	// 지배3
		{ 32596, 32857, 12855 },	// 지배4
		{ 32592, 32862, 12856 },	// 지배5
		{ 32597, 32864, 12857 },	// 지배6
		{ 32597, 32867, 12858 },	// 지배7
		{ 32588, 32865, 12859 },	// 지배8
		{ 32597, 32866, 12860 },	// 지배9
		{ 32726, 32801, 12861 },	// 지배10
	};
	
	private static final int[][] WORLD_WAR = {
		{ 00000, 00000, 0 },		// 기란성
			
		{ 33592, 32752, 15482 },	// 성 좌측 끝
		{ 33614, 32752, 15482 },	// 성 입구 좌측
		{ 33630, 32752, 15482 },	// 성 입구 중앙
		{ 33646, 32752, 15482 },	// 성 입구 우측
		{ 33663, 32752, 15482 },	// 성 우측 끝
		{ 33620, 32785, 15482 },	// 기란성 마을
			
		{ 00000, 00000, 0 },		// 오크요새
			
		{ 32753, 32356, 15483 },	// 성 좌측 끝
		{ 32779, 32356, 15483 },	// 성 입구 좌측
		{ 32788, 32356, 15483 },	// 성 입구 중앙
		{ 32806, 32356, 15483 },	// 성 입구 우측
		{ 32825, 32356, 15483 },	// 성 우측 끝
		{ 32777, 32401, 15483 },	// 오크요새 마을
			
		{ 00000, 00000, 0 },		// 하이네성
			
		{ 32712, 32725, 15484 },	// 성 좌측 끝
		{ 32725, 32725, 15484 },	// 성 입구 좌측
		{ 32741, 32725, 15484 },	// 성 입구 중앙
		{ 32757, 32725, 15484 },	// 성 입구 우측
		{ 32777, 32725, 15484 },	// 성 우측 끝
		{ 32736, 32699, 15484 },	// 하이네성 마을
	};
	
	private static final int[][] ORC_CASTLE = { 
		{ 00000, 00000, 0 },		// 오크요새
			
		{ 32754, 32342, 15483 },	// 요새 좌측 끝
		{ 32769, 32361, 15483 },	// 요새 입구 좌측
		{ 32785, 32371, 15483 },	// 요새 입구 중앙
		{ 32821, 32357, 15483 },	// 요새 입구 우측
		{ 32823, 32343, 15483 },	// 요새 우측 끝
		{ 32777, 32401, 15483 },	// 오크요새 마을
	};
	
	private static final int[][] HERO_RING = {
		{ 33224, 32597, 4 },		// 본토 흑기사 출몰 지역
		{ 33903, 32267, 4 },		// 본토 난쟁이 부락
		{ 33408, 33192, 4 },		// 본토 하이네 필드
		{ 33794, 32598, 4 },		// 본토 산적 소굴
		{ 33768, 33408, 4 },		// 본토 거울의 숲
		{ 34162, 32194, 15420 },	// 본토 오렌 설벽1
		{ 34226, 32260, 15420 },	// 본토 오렌 설벽2
		{ 34249, 32378, 15420 },	// 본토 오렌 설벽3
		{ 34274, 32224, 15420 },	// 본토 오렌 설벽4
		{ 34265, 32795, 15410 },	// 본토 풍룡의 둥지 정상
		{ 34147, 32794, 15410 },	// 본토 풍룡의 둥지1
		{ 34203, 32856, 15410 },	// 본토 풍룡의 둥지2
			
		{ 33270, 32402, 15430 },	// 본토 용의 계곡 작은 뼈1
		{ 33260, 32415, 15430 },	// 본토 용의 계곡 작은 뼈2
		{ 33250, 32400, 15430 },	// 본토 용의 계곡 작은 뼈3
		{ 33261, 32388, 15430 },	// 본토 용의 계곡 작은 뼈4
		{ 33401, 32343, 15430 },	// 본토 용의 계곡 큰 뼈1
		{ 33394, 32361, 15430 },	// 본토 용의 계곡 큰 뼈2
		{ 33381, 32355, 15430 },	// 본토 용의 계곡 큰 뼈3
		{ 33369, 32338, 15430 },	// 본토 용의 계곡 큰 뼈4
		{ 33386, 32331, 15430 },	// 본토 용의 계곡 큰 뼈5
		{ 33334, 32438, 15430 },	// 본토 용의 계곡1
		{ 33355, 32405, 15430 },	// 본토 용의 계곡2
		{ 33351, 32298, 15430 },	// 본토 용의 계곡3
		{ 33726, 32251, 15440 },	// 본토 화룡의 둥지 정상1
		{ 33750, 32273, 15440 },	// 본토 화룡의 둥지 정상2
		{ 33662, 32400, 15440 },	// 본토 화룡의 둥지1
		{ 33687, 32285, 15440 },	// 본토 화룡의 둥지2
		{ 33764, 32397, 15440 },	// 본토 화룡의 둥지3
	};

	public TeleportBook(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.isNotTeleport()) {
				pc.sendPackets(S_Paralysis.TELEPORT_UNLOCK);
				return;
			}
			if (!pc.getMap().isEscapable() && !pc.isGm()) {
				pc.sendPackets(S_Paralysis.TELEPORT_UNLOCK);
				pc.sendPackets(L1ServerMessage.sm647);
				return;
			}
			int itemId = this.getItemId();
			if (pc.getConfig().getDuelLine() != 0 || pc.getMapId() == 5166 || pc.getNetConnection().isInterServer() && itemId != 560026 && itemId != 560023 && itemId != 560022) {
				pc.sendPackets(S_Paralysis.TELEPORT_UNLOCK);
				pc.sendPackets(L1ServerMessage.sm647);
				return;
			}
			
			int bookIndex	= packet.readC();// 책 순번
			
			if (itemId == 560025) {// 마을 기억책
				int[] a = TOWN_BOOK[bookIndex];
				if (a != null) {
					pc.getTeleport().start(a[0], a[1], (short) a[2], pc.getMoveState().getHeading(), true);
					pc.getInventory().removeItem(this, 1);
				}
			} else if (itemId == 560024) {
				int[] c = OMAN_BOOK[bookIndex];
				if (c != null) {
					pc.getTeleport().start(c[0], c[1], (short) c[2], pc.getMoveState().getHeading(), true);
					pc.getInventory().removeItem(this, 1);
				}
			} else if (itemId == 560027 || itemId == 560029) {// 던전기억책, 조우의이동기억책
				int[] c = JOW_BOOK[bookIndex];
				if (c != null) {
					pc.getTeleport().start(c[0], c[1], (short) c[2], pc.getMoveState().getHeading(), true);
					pc.getInventory().removeItem(this, 1);
				}
			} else if (itemId == 560028) {// 환상의 오만의 탑 이동 부적
				int[] c = OMAN_AMULET[bookIndex];
				if (c != null) {
					pc.getTeleport().start(c[0], c[1], (short) c[2], pc.getMoveState().getHeading(), true);
				}
			} else if (itemId == 560026) {// 환상의 지배의 탑 이동 부적
	            int[] c = JIBAE_AMULET[bookIndex];
	            if (c != null) {
		            if (pc.isGm() || pc.getMapId() >= 12852 && pc.getMapId() <= 12862) {
		            	pc.getTeleport().start(c[0], c[1], (short) c[2], pc.getMoveState().getHeading(), true);
		            } else {
		            	pc.sendPackets(S_Paralysis.TELEPORT_UNLOCK);
		            	pc.sendPackets(L1ServerMessage.sm5377);
		            }
	            }
	        } else if (itemId == 560023) {// 공성전 이동 부적 
				int[] c = WORLD_WAR[bookIndex];
				if (c != null) {
					pc.getTeleport().start(c[0], c[1], (short) c[2], pc.getMoveState().getHeading(), true);
				}
			} else if (itemId == 560022) {// 공성전 이동 부적(오크성)
				int[] c = ORC_CASTLE[bookIndex];
				if (c != null) {
					pc.getTeleport().start(c[0], c[1], (short) c[2], pc.getMoveState().getHeading(), true);
				}
			} else if (itemId == 560040 || itemId == 560041) {// 영웅의 지배 반지
				int[] c = HERO_RING[bookIndex];
				if (c != null) {
					pc.getTeleport().start(c[0], c[1], (short) c[2], pc.getMoveState().getHeading(), true);
				}
			}
		}
	}
}

