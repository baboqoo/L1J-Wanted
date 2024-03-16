package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_UnityIcon extends ServerBasePacket {

	public S_UnityIcon(
			int DECREASE, int DECAY_POTION, int SILENCE, int VENOM_RESIST, int WEAKNESS, int DISEASE,
			int DRESS_EVASION, int BERSERKERS, int NATURES_TOUCH, int WIND_SHACKLE, 
			int ERASE_MAGIC, int ADDITIONAL_FIRE, int ELEMENTAL_FALL_DOWN, int ELEMENTAL_FIRE,
			int STRIKER_GALE, int SOUL_OF_FLAME, int POLLUTE_WATER,
			int EXP_POTION, int SCROLL, int SCROLLTPYE,
			int CONCENTRATION, int INSIGHT, int PANIC,
			int MORTAL_BODY, int HORROR_OF_DEATH, int FEAR,
			int PATIENCE, int GUARD_BREAK, int DRAGON_SKIN, int STATUS_FRUIT,
			int COMA, int COMA_TYPE, int CRAY_TIME, int CRAY, 
			int MAAN_TIME, int MAAN, int FEATHER_BUFF, int FEATHER_TYPE, 
			int STAT_BUFF_TIME, int STAT_BUFF_TYPE, int SCROLL_TIME) {
		writeC(Opcodes.S_EVENT);
		writeC(0x14);

		writeC(0xe5);
		writeH(0x00);
		writeD(0);
		
		writeC(DECREASE);					// 디크리즈 웨이트 DECREASE
		writeC(DECAY_POTION);				// 디케이 포션
		writeC(0x00);						// 앱솔루트배리어
		writeC(SILENCE);					// 사일런스
		writeC(VENOM_RESIST);				// 베놈 레지스트 10
		writeC(WEAKNESS);					// 위크니스
		writeC(DISEASE);					// 디지즈
		
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);						// 아바타
		writeC(0x00);
		writeC(0x00);
		
		writeC(DRESS_EVASION);				// 드레스이베이전
		writeC(BERSERKERS);					// 버서커스 !
		writeC(NATURES_TOUCH);				// 네이쳐스터치
		writeC(WIND_SHACKLE);				// 윈드셰클 10
		writeC(ERASE_MAGIC);				// 이레이즈매직
		writeC(0x00);						// 카운터미러
		writeC(ADDITIONAL_FIRE);			// 어디셔널 파이어
		writeC(ELEMENTAL_FALL_DOWN);		// 엘리맨탈폴다운   
		writeC(0x00);
		writeC(ELEMENTAL_FIRE);				// 엘리맨탈 파이어
		writeC(0x00);
		// 30
		writeC(0x00);						// 기척제거제
		writeC(0x00);
		writeC(STRIKER_GALE);				// 스트라이커게일
		writeC(SOUL_OF_FLAME);				// 소울오브 프레임
		writeC(POLLUTE_WATER);				// 플루투워터 
		writeC(0x00);
		writeC(0x00);						// 공격가능시간
		writeC(0x00);						// 1-거대한마족  2-거대한마족 3-무녀사엘 4-원한찬유령 5-원한찬하라장 6-재생의제단
		writeC(0x00);						// 속성저항력 10? 
		writeC(0x00);						// 0-모든속성저항 1-최대HP30 2-MP회복3 3-AC1 4-최대MP20 5-HP회복3 6-MR5
		
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);						// 지혜의물약
		writeC(EXP_POTION);					// exp
		writeC(SCROLL);						// 전투강화주문서 123 다있음?
		writeC(SCROLLTPYE);					// 0-hp50hpr4, 1-mp40mpr4, 2-추타3공성3sp3  20 50
		
		writeC(0x00);
		writeC(0x00);
		writeC(STAT_BUFF_TIME);				// 증강 물약 시간
		writeC(STAT_BUFF_TYPE);				// 증강 물약 타입 0x41:인트, 0x45:지식
		
		writeC(CONCENTRATION);				// 컨센트레이션
		writeC(INSIGHT);					// 인사이트
		writeC(PANIC);						// 패닉
		writeC(MORTAL_BODY);				// 모탈바디                 
		writeC(HORROR_OF_DEATH);			// 호어오브데스
		writeC(FEAR);						// 피어 60
		
		writeC(PATIENCE);					// 페이션스
		writeC(GUARD_BREAK);				// 가드브레이크
		writeC(DRAGON_SKIN);				// 드래곤스킨
		writeC(STATUS_FRUIT);				// 유그드라  30
		
		writeC(0x14);
		writeC(0x00);
		writeC(COMA);						// 시간
		writeC(COMA_TYPE);					// 타입
		writeC(0x00);
		writeC(0x00);
		
		writeC(0x1a);
		writeC(0x35);
		writeC(0x0d);
		writeC(0x00);
		writeD(System.currentTimeMillis());
		writeC(CRAY_TIME);					// (int)(codetest+0.5) / 32
		writeC(CRAY);						// 45크레이축복, 60무녀사엘축복80
		
		writeC(MAAN_TIME);					//(int)(codetest+0.5) / 32
		writeC(MAAN);						// 46지룡, 47수룡, 48풍룡, 49화룡, 50지룡,수룡 51지룡,수룡,풍룡 52지룡,수룡,풍룡,화룡
		
		writeC(0xa1);
		writeC(0x09);
		writeC(0x35);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);//90
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		
		writeC(FEATHER_BUFF);				//(int)(codetest+0.5) / 16
		writeC(FEATHER_TYPE);				// 70= 전부 71공성,주술력,최대HP/MP 뎀지감소 증가, 72최대HP,MP증가 AC향상, 73AC향상
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(EXP_POTION > 0 ? 0x01 : 0x00);// 2?
		writeC(0x00);
		writeC(SCROLL_TIME);				// 전강 13분이상 1, 13분이하 0
		
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		// 120
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		// 130
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		// 140
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		// 150
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		// 160
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		// 170
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		// 180
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		// 190
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		// 200
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		// 210
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		// 210
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}

