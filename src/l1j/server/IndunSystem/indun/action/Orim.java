package l1j.server.IndunSystem.indun.action;

import l1j.server.IndunSystem.indun.IndunHandler;
import l1j.server.IndunSystem.indun.IndunInfo;
import l1j.server.common.data.ChatType;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.construct.message.L1GreenMessage;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_HPMeter;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.indun.S_ArenaPlayStatusNoti;
import l1j.server.server.serverpackets.message.S_DialogueMessage;
import l1j.server.server.serverpackets.polymorph.S_Polymorph;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.L1SpawnUtil;

/**
 * 글루디오 연구실
 * @author LinOffice
 */
public class Orim extends IndunHandler {
	private int stage					= 1;
	private static final int WAIT_STEP	= 1;
	private static final int START_STEP	= 2;
	private static final int BOSS_STEP	= 3;
	private static final int LAST_STEP	= 4;
	private static final int EVENT_STEP	= 5;
	private static final int END		= 6;
	
	private int startUserCount = 0, stepCheck = 0;
	private L1NpcInstance crystal, sema, balthazar, merkior, caspa, soul, boss, event_boss, orimTrap;
	
	private static final int[] ORIM_BUFF = { 
		L1SkillId.PHYSICAL_ENCHANT_DEX, L1SkillId.PHYSICAL_ENCHANT_STR, L1SkillId.NATURES_TOUCH, L1SkillId.IRON_SKIN 
	};
	
	public Orim(IndunInfo info, short mapId){
		super(info, mapId);
	}

	@Override
	protected void startSpawn() throws Exception {
		startUserCount = _info.infoUserList.size();
		crystal = _util.spawn(_map, 0, 0);		//	흑마법 수정구
		sema = _util.spawn(_map, 1, 0);			//	저주받은 세마
		balthazar = _util.spawn(_map, 2, 0);	//	저주받은 발터자르
		if (startUserCount >= 3) {
			merkior = _util.spawn(_map, 3, 0);	//	저주받은 메르키오르
		}
		if (startUserCount >= 4) {
			caspa = _util.spawn(_map, 4, 0);	//	저주받은 카스파
		}
		spawn_fire();
	}

	@Override
	protected void process() throws Exception {
		switch(stage){
		case WAIT_STEP:
			for (L1PcInstance pc : _pclist) {
				pc.sendPackets(S_DialogueMessage.ORIM_START_MENT);	//	오림멘트
			}
			Thread.sleep(10000L);
			for (L1PcInstance pc : _pclist) {
				pc.sendPackets(L1GreenMessage.ORIM_MENT_1);	//	중앙에 있는 수정구를 보호하세요.
				pc.sendPackets(L1GreenMessage.ORIM_MENT_2);	//	시간이 초과되거나 수정구가 파괴되면 실패입니다.
			}
			
			Thread.sleep(20000L);
			for (L1PcInstance pc : _pclist) {
				pc.sendPackets(L1GreenMessage.ORIM_MENT_3);	//	오림이 당신에게 작은 힘을 보탭니다.
				pc.send_effect(9009);
				L1BuffUtil.skillArrayLogin(pc, ORIM_BUFF);
			}
			Thread.sleep(10000L);
			for (L1PcInstance pc : _pclist) {
				pc.sendPackets(S_PacketBox.WAKE);	//	진동
				pc.sendPackets(L1GreenMessage.ORIM_MENT_4);	//	저주받은 무리가 몰려오기 시작합니다.
				pc.sendPackets(new S_ArenaPlayStatusNoti(_info, null), true);	//	타이머 시작
			}
			GeneralThreadPool.getInstance().execute(new TimerCheck());	//	제한시간 타이머 시작
			stage = START_STEP;
			break;
		case START_STEP:
			break;
		case BOSS_STEP:
			if (soul != null && !soul.isDead() && ((soul.getX() > 32790 && soul.getX() < 32808) && (soul.getY() > 32855 && soul.getY() < 32872) && soul.getMapId() == _map)) {
		        for (L1PcInstance pc : _pclist) {
		        	pc.sendPackets(new S_NpcChatPacket(soul, "$30000", ChatType.CHAT_NORMAL), true);	//	으으.. 괴로워.. 도와줘..
		        }
		        Thread.sleep(2000L);
				for (L1PcInstance pc : _pclist) {
					pc.sendPackets(new S_NpcChatPacket(soul, "$30001", ChatType.CHAT_NORMAL), true);	//	머리가 검은 분노로 가득 차 있어...
				}
				Thread.sleep(2000L);
				for (L1PcInstance pc : _pclist) {
					pc.sendPackets(new S_NpcChatPacket(soul, "$30002", ChatType.CHAT_NORMAL), true);	//	두려워.. 무엇인가.. 나를 집어 삼키는 것 같아..
				}
				Thread.sleep(2000L);
				for (L1PcInstance pc : _pclist) {
					pc.sendPackets(new S_NpcChatPacket(soul, "$30003", ChatType.CHAT_NORMAL), true);	//	...
				}
				Thread.sleep(1000L);
		        boss = L1SpawnUtil.spawnCount(soul.getX(), soul.getY(), _map, 7800008, 0, 0, 1);	//	저주받은 아리오크
		        soul.deleteMe();
		        soul = null;
		        Thread.sleep(2000L);
		        for (int i = 0; i < 2; i++) {
		        	_util.spawn(_map, 22, 15000);	//	대미지 불길
		        }
		        Thread.sleep(10000L);
		        for (int i = 0; i < 2; i++) {
		        	_util.spawn(_map, 22, 15000);	//	대미지 불길
		        }
		        stage = LAST_STEP;
	        }
			break;
		case LAST_STEP:
			if (boss == null || boss.isDead()) {
				if (CommonUtil.random(10) < _info.infoUserList.size()) {	//	히든보스
					for (L1PcInstance pc : _pclist) {
						pc.sendPackets(S_PacketBox.LIGHTING);	//	번쩍임
					}
					Thread.sleep(2000L);
					for (L1PcInstance pc : _pclist) {
						pc.sendPackets(S_PacketBox.LIGHTING);	//	번쩍임
					}
					Thread.sleep(4000L);
					event_boss = L1SpawnUtil.spawnCount(boss.getX(), boss.getY(), _map, 7800065, 0, 0, 1);	//	대마법사 하딘
					stage = EVENT_STEP;
				} else {
					stage = END;
				}
				if (boss != null) {
					boss = null;
				}
			}
			break;
		case EVENT_STEP:
			if (event_boss == null || event_boss.isDead()) {
				if (event_boss != null) {
					event_boss = null;
				}
				for (L1PcInstance pc : _pclist) {
					pc.sendPackets(S_DialogueMessage.ORIM_GERANG_MENT);	//	게렝 멘트
				}
				Thread.sleep(4000L);
				stage = END;
			}
			break;
		case END:
			for (L1PcInstance pc : _pclist) {
				pc.sendPackets(L1GreenMessage.ORIM_MENT_5);	//	모든 공격을 막아냈습니다.
				pc.sendPackets(S_DialogueMessage.ORIM_END_MENT);	//	오림멘트
			}
			Thread.sleep(10000L);
			for (L1PcInstance pc : _pclist) {
				pc.sendPackets(L1GreenMessage.ORIM_MENT_6);	//	오림을 돕는데 성공하였습니다.
			}
			Thread.sleep(10000L);
			for (L1PcInstance pc : _pclist) {
				pc.sendPackets(L1GreenMessage.INDUN_CLOSE_MENT_3);	//	이제 시공 여행이 종료됩니다.
			}
			Thread.sleep(10000L);
			for (L1PcInstance pc : _pclist) {
				pc.sendPackets(L1GreenMessage.ORIM_MENT_7);	//	오림 앞에 생성된 텔레포트 마법진으로 이동하세요.
			}
			orimTrap = _util.spawn(_map, 7, 60000);	//	오림법진
			_util.spawn(_map, 8, 60000);	//	오림
			Thread.sleep(10000L);
			for (L1PcInstance pc : _pclist) {
				pc.sendPackets(L1GreenMessage.ORIM_MENT_8);	//	당신의 앞 날에 아인하사드의 축복이 있기를..
			}
			Thread.sleep(10000L);
			for (L1PcInstance pc : _pclist) {
				pc.sendPackets(L1GreenMessage.ORIM_MENT_9);	//	이제 당신의 세계로 돌아가십시오.
			}
			Thread.sleep(10000L);
			for (L1PcInstance pc : _pclist) {
				pc.sendPackets(L1ServerMessage.sm1259);	//	잠시 후 마을로 이동됩니다.
			}
			Thread.sleep(10000L);
			crystal.deleteMe();
			closeTeleport();
			break;
		default:break;
		}
	}
	
	@Override
	protected void timerCheck() throws Exception {
		if (limitTime <= 0) {
			for (L1PcInstance pc : _pclist) {
				pc.sendPackets(L1GreenMessage.INDUN_CLOSE_MENT);//	시공 여행이 종료 됩니다.
				pc.sendPackets(L1GreenMessage.INDUN_CLOSE_MENT_2);//	다음에 다시 도전해주세요.
			}
			closeTeleport();
			return;
		}
		limitTime--;
		if (stage == START_STEP) {
			if (limitTime == 900 - 1 || limitTime == 900 - 20 || limitTime == 900 - 40 || limitTime == 900 - 60 
					|| limitTime == 900 - 80 || limitTime == 900 - 100 || limitTime == 900 - 120
					|| limitTime == 900 - 140 || limitTime == 900 - 160 || limitTime == 900 - 180 
					|| limitTime == 900 - 240 || limitTime == 900 - 260 || limitTime == 900 - 280 || limitTime == 900 - 300 
					|| limitTime == 900 - 320 || limitTime == 900 - 340 || limitTime == 900 - 360 || limitTime == 900 - 380 
					|| limitTime == 900 - 400 || limitTime == 900 - 420 
					|| limitTime == 900 - 440 || limitTime == 900 - 460 || limitTime == 900 - 480 || limitTime == 900 - 500) {
				spwanMonster();
				if (limitTime == 900 - 40) {
					_util.spawn(_map, 19, 0);	//	어둠에 물든 병사
					for (L1PcInstance pc : _pclist) {
						pc.sendPackets(L1GreenMessage.ORIM_MENT_10);//	저주받은 안개는 심각한 오염 피해를 줍니다.
					}
					_util.spawn(_map, 23, 15000);	//	대미지 필드
				} else if (limitTime == 900 - 60) {
					_util.spawn(_map, 32, 0);	//	체력 회복 구슬
					_util.spawn(_map, 19, 0);	//	어둠에 물든 병사
				} else if (limitTime == 900 - 120) {
					_util.spawn(_map, 33, 0);	//	마나 회복 구슬
				} else if (limitTime == 900 - 140) {
					_util.spawn(_map, 23, 15000);	//	대미지 필드
				} else if (limitTime == 900 - 240) {
					for (L1PcInstance pc : _pclist) {
						pc.sendPackets(L1GreenMessage.ORIM_MENT_11);// 저주의 기운이 더욱 더 거세지고 있습니다. 
					}
					for (int i = 0; i < 2; i++) {
						_util.spawn(_map, 22, 15000);	//	대미지 불길
					}
				} else if (limitTime == 900 - 260) {
					_util.spawn(_map, 24, 0);	//	빙염우 폭탄
					_util.spawn(_map, 34, 0);	//	리덕션 버프 구슬
				} else if (limitTime == 900 - 280) {
					_util.spawn(_map, 19, 0);	//	어둠에 물든 병사
				} else if (limitTime == 900 - 340) {
					_util.spawn(_map, 19, 0);	//	어둠에 물든 병사
					_util.spawn(_map, 35, 0);	//	어드밴스 버프 구슬
				} else if (limitTime == 900 - 380) {
					_util.spawn(_map, 38, 0);	//	토네이도 폭탄
				} else if (limitTime == 900 - 440) {
					_util.spawn(_map, 23, 15000);	//	대미지 필드
				} else if (limitTime == 900 - 460) {
					_util.spawn(_map, 36, 0);	//	가속 버프 구슬
				} else if (limitTime == 900 - 480) {
					_util.spawn(_map, 20, 0);	//	어둠에 물든 투사
					_util.spawn(_map, 23, 15000);	//	대미지 필드
					changeBoss();// 탐욕의 포식자 변신
				} else if (limitTime == 900 - 500) {
					for (int i = 0; i < 2; i++) {
						_util.spawn(_map, 22, 15000);	//	대미지 불길
					}
					_util.spawn(_map, 21, 0);	//	파도의 소환사
					_util.spawn(_map, 37, 0);	//	블레스웨폰 버프 구슬
				}
				stepCheck++;
			} else if (limitTime == 900 - 200) {
				for (L1PcInstance pc : _pclist) {
					pc.sendPackets(L1GreenMessage.ORIM_MENT_12);	// 이제 다음 공격을 대비해주세요.
				}
			} else if (limitTime == 900 - 450) {// 거점 제압
				for (L1PcInstance pc : _pclist) {
					pc.sendPackets(L1GreenMessage.ORIM_MENT_13);	// 오림의 눈물이... 화염의 벽을 잠재웁니다...
					pc.sendPackets(L1GreenMessage.ORIM_MENT_14);	// 벽 너머에는 저주를 소환하는 슬픈 사제들이 있습니다.
				}
				_util.deleteDoor(_map);
			} else if (limitTime == 900 - 490) {
				for(int i = 0; i < 2; i++)_util.spawn(_map, 22, 15000);	//	대미지 불길
			} else if (limitTime == 900 - 520) {
				for (L1PcInstance pc : _pclist) {
					pc.sendPackets(L1GreenMessage.ORIM_MENT_15);	//	저주받은 기운이 다가오기 시작합니다.
				}
				soul = _util.spawn(_map, 29, 0);	//	저주받은 영혼
				_util.spawn(_map, 30, 15000);	//	대미지 불길
				stage = BOSS_STEP;
			}
		}
		checkCrystal();
		checkPc();
		checkBoss();
	}
	
	private void spwanMonster(){
		for (L1PcInstance pc : _pclist) {
			pc.sendPackets(S_PacketBox.LIGHTING);	//	번쩍임
		}
		if (sema != null && !sema.isDead()) {
			for (int i = 0; i < 2 + (stepCheck >= 10 ? 2 : 0); i++) {
				_util.spawn(_map, 9, 0);	//	화염의 잔해
			}
			for (int i = 0; i < 2; i++) {
				_util.spawn(_map, 10, 0);	//	화염의 살기
			}
		}
		if (merkior != null && !merkior.isDead()) {
			for (int i = 0; i < 2 + (stepCheck >= 10 ? 2 : 0); i++) {
				_util.spawn(_map, 11, 0);	//	심해의 덫
			}
			for (int i = 0; i < 2; i++) {
				_util.spawn(_map, 12, 0);	//	심해의 유혹
			}
		}
		if (balthazar != null && !balthazar.isDead()) {
			for (int i = 0; i < 2 + (stepCheck >= 10 ? 2 : 0); i++) {
				_util.spawn(_map, 13, 0);	//	대지의 불안
			}
			for (int i = 0; i < 2; i++) {
				_util.spawn(_map, 14, 0);	//	대지의 노예
			}
		}
		if (caspa != null && !caspa.isDead()) {
			for (int i = 0; i < 2 + (stepCheck >= 10 ? 2 : 0); i++) {
				_util.spawn(_map, 15, 0);	//	풍랑의 저주
			}
			for (int i = 0; i < 2; i++){
				int firetype = 16;
				if (stepCheck >= 10) {
					firetype = 17;
				} else if (stepCheck >= 20) {
					firetype = 18;
				}
				_util.spawn(_map, firetype, ((13 + CommonUtil.random(5)) * 1000));	//	화염의 상처
			}
		}
	}
	
	private void checkCrystal() {
		if (crystal.isDead()) {// 수정구가 부서졌을시
			for (L1PcInstance pc : _pclist) {
				pc.sendPackets(L1GreenMessage.ORIM_MENT_16);//	아..아.. 수정구가 파괴되었습니다...
			}
			closeTeleport();
			return;
		}
		if ((crystal.getMaxHp() >> 2) > crystal.getCurrentHp()) {
			crystal.broadcastPacket(new S_Polymorph(crystal.getId(), 17815, 0), true);
		} else if (((crystal.getMaxHp() << 1) >> 2) > crystal.getCurrentHp()) {
			crystal.broadcastPacket(new S_Polymorph(crystal.getId(), 17814, 0), true);
		} else if ((crystal.getMaxHp() * 3 >> 2) > crystal.getCurrentHp()) {
			crystal.broadcastPacket(new S_Polymorph(crystal.getId(), 17813, 0), true);
		}
		
		crystal.broadcastPacket(new S_HPMeter(crystal), true);
	}

	private void checkPc() throws Exception {
		if (_pclist.isEmpty()) {
			endIndun();
			return;
		}
		int diechck = 0;
		for (L1PcInstance pc : _pclist) {
			if (pc != null && pc.isDead()) {
				diechck++;
			}
		}
		if (_pclist.size() <= diechck) {// 사망 체크
			for (L1PcInstance pc : _pclist) {
				pc.sendPackets(L1GreenMessage.INDUN_PARTY_DEATH);	//	모든 파티원이 사망하였습니다.
			}
			Thread.sleep(10000L);
			endIndun();
			return;
		}
		for (int i=0; i<_pclist.size(); i++) {
			L1PcInstance pc = _pclist.get(i);// pc타입으로 변환
			if (pc == null || pc.getMapId() != _map) {
				removePc(_pclist.get(i));// 존재여부 체크
			}
		}
		if (orimTrap != null) {
			for (int i=0; i<_pclist.size(); i++) {
				L1PcInstance pc = _pclist.get(i);
				if (pc == null) {
					continue;
				}
				if (!pc.isDead() && (pc.getX() >= orimTrap.getX() - 1 && pc.getX() <= orimTrap.getX() + 1) && (pc.getY() >= orimTrap.getY() - 1 && pc.getY() <= orimTrap.getY() + 1) && pc.getMapId() == orimTrap.getMapId()) {
					if (pc.isInParty()) {
						pc.getParty().leaveMember(pc);//	파티탈퇴
					}
					removePc(pc);
					pc.getTeleport().start(33464, 32757, (short) 4, pc.getMoveState().getHeading(), true, true);
				}
			}
		}
	}
	
	private void checkBoss() {
    	if (sema != null && sema.isDead()) {
			if (sema.getNpcId() == 7800003) {
				for (L1PcInstance pc : _pclist) {
					pc.sendPackets(L1GreenMessage.ORIM_MENT_17);	//	세마.. 미안해..
					pc.sendPackets(L1GreenMessage.ORIM_MENT_18);	//	화염의 몬스터 소환이 멈췄습니다.
				}
			}
			sema = null;
		}
		if (balthazar != null && balthazar.isDead()) {
			if (balthazar.getNpcId() == 7800004) {
				for (L1PcInstance pc : _pclist) {
					pc.sendPackets(L1GreenMessage.ORIM_MENT_19);	//	발터자르.. 그 고통에서 널 구해줄게..
					pc.sendPackets(L1GreenMessage.ORIM_MENT_20);	//	대지의 몬스터 소환이 멈췄습니다.
				}
			}
			balthazar = null;
		}
		if (merkior != null && merkior.isDead()) {
			if (merkior.getNpcId() == 7800005) {
				for (L1PcInstance pc : _pclist) {
					pc.sendPackets(L1GreenMessage.ORIM_MENT_21);	//	메르키오르.. 조금만 기다려..
					pc.sendPackets(L1GreenMessage.ORIM_MENT_22);	//	심해의 몬스터 소환이 멈췄습니다.
				}
			}
			merkior = null;
		}
		if (caspa != null && caspa.isDead()) {
			if (caspa.getNpcId() == 7800006) {
				for (L1PcInstance pc : _pclist) {
					pc.sendPackets(L1GreenMessage.ORIM_MENT_23);	//	카스파.. 진실을 알게 될거야..
					pc.sendPackets(L1GreenMessage.ORIM_MENT_24);	//	풍랑의 몬스터 소환이 멈췄습니다.
				}
			}
			caspa = null;
		}
	}
	
	private void changeBoss(){// 탐욕의 포식자 변신
		if ((sema != null && !sema.isDead()) || (balthazar != null && !balthazar.isDead()) || (merkior != null && !merkior.isDead()) || (caspa != null && !caspa.isDead())) {
			for (L1PcInstance pc : _pclist) {
				pc.sendPackets(L1GreenMessage.ORIM_MENT_25);	//	으어어어어 괴물이다아아아아아
			}
		}
		if (sema != null && !sema.isDead()) {
			sema.deleteMe();
			sema =_util.spawn(_map, 25, 0);
		}
		if (balthazar != null && !balthazar.isDead()) {
			balthazar.deleteMe();
			balthazar = _util.spawn(_map, 27, 0);
		}
		if (merkior != null && !merkior.isDead()) {
			merkior.deleteMe();
			merkior = _util.spawn(_map, 26, 0);
		}
		if (caspa != null && !caspa.isDead()) {
			caspa.deleteMe();
			caspa = _util.spawn(_map, 28, 0);
		}
	}
	
	private void spawn_fire(){
		_util.doorSpawn(12754, 32802, 32851, (short) _map, true);
		_util.doorSpawn(12754, 32801, 32851, (short) _map, true);
		_util.doorSpawn(12754, 32800, 32851, (short) _map, true);
		_util.doorSpawn(12754, 32799, 32851, (short) _map, true);
		_util.doorSpawn(12754, 32798, 32851, (short) _map, true);
		_util.doorSpawn(12754, 32797, 32851, (short) _map, true);
		
		_util.doorSpawn(12754, 32812, 32860, (short) _map, false);
		_util.doorSpawn(12754, 32812, 32861, (short) _map, false);
		_util.doorSpawn(12754, 32812, 32862, (short) _map, false);
		_util.doorSpawn(12754, 32812, 32863, (short) _map, false);
		_util.doorSpawn(12754, 32812, 32864, (short) _map, false);
		_util.doorSpawn(12754, 32812, 32865, (short) _map, false);
		_util.doorSpawn(12754, 32812, 32866, (short) _map, false);
		
		_util.doorSpawn(12754, 32796, 32875, (short) _map, true);
		_util.doorSpawn(12754, 32797, 32875, (short) _map, true);
		_util.doorSpawn(12754, 32798, 32875, (short) _map, true);
		_util.doorSpawn(12754, 32799, 32875, (short) _map, true);
		_util.doorSpawn(12754, 32800, 32875, (short) _map, true);				
		_util.doorSpawn(12754, 32801, 32875, (short) _map, true);
		_util.doorSpawn(12754, 32802, 32875, (short) _map, true);
		
		_util.doorSpawn(12754, 32787, 32860, (short) _map, false);
		_util.doorSpawn(12754, 32787, 32861, (short) _map, false);
		_util.doorSpawn(12754, 32787, 32862, (short) _map, false);
		_util.doorSpawn(12754, 32787, 32863, (short) _map, false);
		_util.doorSpawn(12754, 32787, 32864, (short) _map, false);
		_util.doorSpawn(12754, 32787, 32865, (short) _map, false);
		_util.doorSpawn(12754, 32787, 32866, (short) _map, false);
	}
	
	@Override
	protected void startIndun() {
		System.out.println("■■■■■■ Orim's laboratory (in-dung) start map number " + _map + " room number " + _info.room_id + " ■■■■■■");
		GeneralThreadPool.getInstance().schedule(this, 4000);
	}

	@Override
	protected void endIndun() {
		if (running) {
			running = false;
		    System.out.println("■■■■■■ Orim's Lab (Indone) End Map Number " + _map + " Room Number " + _info.room_id + " ■■■■■■");
			dispose();
		}
	}
	
	@Override
	protected void dispose() {
		super.dispose();
	}
}

