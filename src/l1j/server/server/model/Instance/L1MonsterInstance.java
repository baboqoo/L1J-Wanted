package l1j.server.server.model.Instance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.GameSystem.beginnerquest.bean.L1QuestDropItem;
import l1j.server.GameSystem.beginnerquest.bean.L1QuestKillNpc;
import l1j.server.GameSystem.beginnerquest.bean.L1QuestProgress;
import l1j.server.GameSystem.huntingquest.user.HuntingQuestUserTemp;
import l1j.server.IndunSystem.ruun.Ruun;
import l1j.server.common.data.ChatType;
import l1j.server.server.ActionCodes;
import l1j.server.server.GameServerSetting;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.construct.L1CastleType;
import l1j.server.server.construct.L1CharacterInfo;
import l1j.server.server.construct.L1InterServer;
import l1j.server.server.construct.message.L1GreenMessage;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.controller.CrockController;
import l1j.server.server.controller.action.BossSpawn;
import l1j.server.server.controller.action.TimeMap;
import l1j.server.server.controller.action.War;
import l1j.server.server.datatables.BossSpawnTable;
import l1j.server.server.datatables.BossSpawnTable.BossTemp;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.datatables.DoorSpawnTable;
import l1j.server.server.datatables.DropTable;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.MapBalanceTable.MapBalanceData;
import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.datatables.NpcInfoTable;
import l1j.server.server.datatables.NpcInfoTable.ScriptInfo;
import l1j.server.server.datatables.NpcInfoTable.ScriptType;
import l1j.server.server.datatables.UBTable;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1EffectSpawn;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1NpcTalkData;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PinkName;
import l1j.server.server.model.L1UltimateBattle;
import l1j.server.server.model.L1War;
import l1j.server.server.model.L1WarSpawn;
import l1j.server.server.model.L1World;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.model.skill.L1SkillUseType;
import l1j.server.server.serverpackets.S_CastleMaster;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.S_Karma;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SceneNoti;
import l1j.server.server.serverpackets.S_SiegeInjuryTimeNoti;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.action.S_ChangeHeading;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.serverpackets.huntingquest.S_HuntingQuestMapList;
import l1j.server.server.serverpackets.huntingquest.S_HuntingQuestUpdateAmount;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.object.S_NPCObject;
import l1j.server.server.serverpackets.quest.S_QuestProgressUpdateNoti;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1TimeMap;
import l1j.server.server.utils.L1SpawnUtil;
import l1j.server.server.utils.StringUtil;
//import manager.Manager;  // MANAGER DISABLED

public class L1MonsterInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;
	private static Logger _log = Logger.getLogger(L1MonsterInstance.class.getName());
	private static final Random random = new Random(System.nanoTime());
	
	public static final String MONSTER_IMPLEMENTS_REGEX = "L1Monster|L1Indun|L1BlackKnight|L1Doppelganger";
	
	// 우호도 야히 진영 보스
	public static final List<Integer> KARMA_YAHEE_BOSS_IDS = Arrays.asList(new Integer[] {
			45675, 81082, 45625, 45674, 45685
	});
	
	// 우호도 발록 진영 보스
	public static final List<Integer> KARMA_BALROG_BOSS_IDS = Arrays.asList(new Integer[] {
			45752, 45753
	});
	
	// 우호도 야히 진영 몬스터
	public static final List<Integer> KARMA_YAHEE_MONSTER_IDS = Arrays.asList(new Integer[] {
			45570, 45571, 45582, 45587, 45605, 45685
	});
	
	// 우호도 발록 진영 몬스터
	public static final List<Integer> KARMA_BALROG_MONSTER_IDS = Arrays.asList(new Integer[] {
			45391, 45450, 45482, 45569, 45579, 45315, 45647
	});
	
	public boolean kirtasAbsolute, kirtasCounterBarrier, kirtasCounterMagic, kirtasPoisonBarrier;
	public boolean titanCounterBarrier, titanCounterMagic, titanPoisonBarrier;
	public boolean _vallacasFly;
	public boolean _sandwarmHiding;
	public boolean _blackCooker;

	private boolean _storeDroped;
	private static final Ruun ruun	= Ruun.getInstance();
	
	private int _currentWeapon;
	public int getCurrentWeapon() {
		return _currentWeapon;
	}
	public void setCurrentWeapon(int i) {
		_currentWeapon = i;
	}
	
	@Override
	public void onItemUse() {
		if (_target != null) {
			useItem(USEITEM_HASTE, 40);
		}
		if (getCurrentHpPercent() < 40) {
			useItem(USEITEM_HEAL, 50);
		}
	}

	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		if (perceivedFrom == null || this == null) {
			return;
		}
		perceivedFrom.addKnownObject(this);
		// 은신 몬스터
		if (_npcTemplate.isHide() && !perceivedFrom.getSkill().hasSkillEffect(L1SkillId.BUFF_VISUAL_OF_CAPTAIN)) {
			return;
		}
		perceivedFrom.sendPackets(new S_NPCObject(this), true);
		if (0 < getCurrentHp()) {
			if (getHiddenStatus() == HIDDEN_STATUS_SINK) {
				perceivedFrom.sendPackets(new S_DoActionGFX(getId(), ActionCodes.ACTION_Hide), true);
			} else if (getHiddenStatus() == HIDDEN_STATUS_FLY) {
				perceivedFrom.sendPackets(new S_DoActionGFX(getId(), ActionCodes.ACTION_Moveup), true);
			}
			onNpcAI();
			if (getMoveState().getBraveSpeed() == 1) {
				perceivedFrom.sendPackets(new S_SkillBrave(getId(), 1, 600000), true);
			}
		}
	}
	
	/**
	 * 몬스터가 인식할 타겟을 강제로 설정한다.(자식 인스턴스에서 처리)
	 * @return boolean
	 */
	protected boolean isSearchTargetForce(){
		return false;
	}

	@Override
	public void searchTarget() {
		// TODO 몬스터가 인식할 타겟을 설정한다.
		L1PcInstance targetPlayer	= null;
		//L1MonsterInstance targetMonster	= null;
		int npcId					= getNpcTemplate().getNpcId();
		short mapId					= getMapId();
		
		/** @설명글
		 *   1.Monster vs Monster 
		 *   2.Monster vs Guard
		 *   3.Monster vs Guardian
		 *   4.Monster vs Npc
		 *  위와같은 상황을 위해 오브젝트를 불러오도록 추가 현재는 1번만을위한 소스임
		 *  간단하게 오브젝트를 인스턴스of로 선언만해주면되게끔 설정 
		 */
		for(L1Object obj : L1World.getInstance().getVisibleObjects(this)){
			if (obj instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) obj;
				if (pc == null || pc.getCurrentHp() <= 0 || pc.isDead() || pc.isGm() || pc.isMonitor() || pc.isGhost() || pc.getSkill().isBlindHidingAssassin()) {
					continue;
				}
				if ((npcId >= 7220081 && npcId <= 7220090) && pc.getLevel() > getLevel()) {// 암흑룡의 던전
					continue;
				}
				if ((mapId == 88 || mapId == 98 || mapId == 92 || mapId == 91 || mapId == 95) && (!pc.isInvisble() || isAgroInvis())) {
					targetPlayer = pc;
					break;
				}

				/*if ((getNpcTemplate().getKarma() < 0 && pc.getKarmaLevel() >= 1) || (getNpcTemplate().getKarma() > 0 && pc.getKarmaLevel() <= -1)) {
					continue;
				}*/

				// 버림받은 땅 퀘스트의 변신중은, 각 진영의 monster로부터 선제 공격받지 않는다
				/*if (pc.getTempCharGfx() == 6034 && getNpcTemplate().getKarma() < 0 
						|| pc.getTempCharGfx() == 6035 && getNpcTemplate().getKarma() > 0 
						|| pc.getTempCharGfx() == 6035 && npcId == 46070 
						|| pc.getTempCharGfx() == 6035 && npcId == 46072) {
					continue;
				}*/
				
				int agroGfx1 = getNpcTemplate().isAgroGfxId1();
				int agroGfx2 = getNpcTemplate().isAgroGfxId2();
				if (!isAgro() && !isAgroPoly() && agroGfx1 < 0 && agroGfx2 < 0) {
					if (pc.getAlignment() < -500) {
						targetPlayer = pc;
						break;
					}
					continue;
				}

				if (!pc.isInvisble() || isAgroInvis()) {
					if (pc.isShapeChange()) {
						if (isAgroPoly()) {
							targetPlayer = pc;
							break;
						}
					} else if (isAgro()) {
						targetPlayer = pc;
						break;
					}

					if (agroGfx1 >= 0 && agroGfx1 <= 4) {
						if (L1CharacterInfo.CLASS_GFX_IDS[agroGfx1][0] == pc.getSpriteId() || L1CharacterInfo.CLASS_GFX_IDS[agroGfx1][1] == pc.getSpriteId()) {
							targetPlayer = pc;
							break;
						}
					} else if (pc.getSpriteId() == agroGfx1) {
						targetPlayer = pc;
						break;
					}

					if (agroGfx2 >= 0 && agroGfx2 <= 4) {
						if (L1CharacterInfo.CLASS_GFX_IDS[agroGfx2][0] == pc.getSpriteId() || L1CharacterInfo.CLASS_GFX_IDS[agroGfx2][1] == pc.getSpriteId()) {
							targetPlayer = pc;
							break;
						}
					} else if (pc.getSpriteId() == agroGfx2) {
						targetPlayer = pc;
						break;
					}
				}
			}/* else if (obj instanceof L1MonsterInstance) {
				L1MonsterInstance mon = (L1MonsterInstance) obj;
				if (mon.getHiddenStatus() != 0 || mon.isDead()) {
					continue;
				}
				int targetNpcId = mon.getNpcTemplate().getNpcId();
				// 적을 인식할 몬스터(야히)
				if (KARMA_YAHEE_MONSTER_IDS.contains(npcId) && KARMA_BALROG_MONSTER_IDS.contains(targetNpcId)) {
					targetMonster = mon;
					break;
				}

				// 적을 인식할 몬스터(발록)
				if (KARMA_BALROG_MONSTER_IDS.contains(npcId) && KARMA_YAHEE_MONSTER_IDS.contains(targetNpcId)) {
					targetMonster = mon;
					break;
				}
			}*/
		}

		if (targetPlayer != null) {
			_hateList.add(targetPlayer, 0);
			_target = targetPlayer;
		}
		/*if (targetMonster != null) { 
			_hateList.add(targetMonster, 0);
			_target = targetMonster;
		}*/
	}

	@Override
	public void setLink(L1Character cha) {
		if (cha != null && _hateList.isEmpty()) { 
			_hateList.add(cha, 0);
			checkTarget();
		}
	}

	public L1MonsterInstance(L1Npc template) {
		super(template);
		_storeDroped = false;
		synchronized (this) {
			if ((this.getNpcTemplate().getNpcId() != 50087 && this.getNpcTemplate().getSpriteId() == 7684) 
					|| this.getNpcTemplate().getSpriteId() == 7805 || this.getNpcTemplate().getSpriteId() == 8063) {
				_PapPearlMonster = new PapPearlMonitor(this);
				_PapPearlMonster.begin();
			} else if (getNpcId() == 100586 || getNpcId() == 100587) {
				GeneralThreadPool.getInstance().execute(new kirtasFireDamage());
			}
			if (_info != null && _info._spawnMsg != null) {
				spawnMsg();
			}
		}
	}

	@Override
	public void onNpcAI() {
		if (isAiRunning()) {
			return;
		}
		if (!_storeDroped) {
			DropTable.getInstance().setDrop(this, getInventory());// 드랍 아이템 세팅
			getInventory().shuffle();
			_storeDroped = true;
		}
		_actived = false;
		startAI();
	}

	@Override
	public void onTalkAction(L1PcInstance pc) {
		if (pc == null) {
			return;
		}
		L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(getNpcTemplate().getNpcId());
		
		// html 표시 패킷 송신
		pc.sendPackets(new S_NPCTalkReturn(talking, getId(), pc.getAlignment() < -500 ? 2 : 1), true);
		if (pc.isGm())
			pc.sendPackets(new S_SystemMessage("Dialog " + talking.getCaoticAction() + " | " + talking.getNormalAction()), true);				
	}

	@Override
	public void onAction(L1PcInstance pc) {
        if (pc == null) {
        	return;
        }
		if (getCurrentHp() > 0 && !isDead()) {
			L1Attack attack = new L1Attack(pc, this);
			if (attack.calcHit()) {
				attack.calcDamage();
				attack.addPcPoisonAttack(pc, this);
			}
			attack.action();
			attack.commit();
			attack = null;
		}
	}

	@Override
	public void receiveManaDamage(L1Character attacker, int mpDamage) {
        if (attacker == null) {
        	return;
        }
		if (mpDamage > 0 && !isDead()) {
			// int Hate = mpDamage / 10 + 10;
			// setHate(attacker, Hate);
			setHate(attacker, mpDamage);
			onNpcAI();
			if (attacker instanceof L1PcInstance) {
				serchLink((L1PcInstance) attacker, getNpcTemplate().getFamily());
			}
			int newMp = getCurrentMp() - mpDamage;
			if (newMp < 0) {
				newMp = 0;
			}
			setCurrentMp(newMp);
		}
	}

	@Override
	public void receiveDamage(L1Character attacker, int damage) { 
        if (attacker == null) {
        	return;
        }
		if (getCurrentHp() > 0 && !isDead()) {
			if (getHiddenStatus() != HIDDEN_STATUS_NONE && getHiddenStatus() != HIDDEN_STATUS_ANCIENTGUARDIAN) {
				return;
			}
			if (damage >= 0 
					&& attacker != this
					&& attacker instanceof L1EffectInstance == false) {
				setHate(attacker, damage);// 공격 공헌도
			}
			
			if (damage > 0) {
				removeSleepSkill();
				pressureCheck(attacker, (double)damage);
			}
			onNpcAI();
			int npcid = getNpcTemplate().getNpcId();
			if (attacker instanceof L1PcInstance) {
				serchLink((L1PcInstance) attacker, getNpcTemplate().getFamily());
			}

			if (attacker instanceof L1PcInstance && damage > 0) {
				L1PcInstance player = (L1PcInstance) attacker;
				player.setPetTarget(this);
				// 보라돌이
				if (this instanceof L1DoppelgangerInstance) {
					L1PinkName.onAction(player);
				}
				if (Config.FATIGUE.FATIGUE_ACTIVE && player.getFatigue().getLevel() == 6) {
					L1PinkName.onAction(player);
				}
				
				// 검은 기사단 요리사
				if (npcid == 8513 && !_blackCooker && getCurrentHpPercent() < 80) {
					blackCookerDamage();
				}
				
				// 텔레포트 몬스터
				if (getNpcTemplate().isTeleport() && !isTeleportDmgAction) {
					teleportMonster(attacker);
				}
				
				if (Config.ETC.BOSS_RECALL && _info != null && _info._recall) {
					recall(player);// 원거리 공격시 보스 주위로 텔레포트시키기
				}
				if (_info != null && _info._reward && player.getConfig().getBossId() != _info._npcId) {
					player.getConfig().setBossId(_info._npcId);
				} else if ((npcid == 30196 || npcid == 30197 || npcid == 30139 || npcid == 30140 || npcid == 30198 || npcid == 30199) && !player.isUltimateBoss()) {
					player.setUltimateBoss(true);// 무한대전 보스
				} else if ((npcid >= 20956 && npcid <= 20958) && !player.isDragonDungenBoss()) {
					player.setDragonDungenBoss(true);// 드래곤 서식지 보스
				}
			}
			
			int newHp = getCurrentHp() - damage;
			
			// TODO death process
			if (newHp <= 0 && !isDead()) {
				if (attacker instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) attacker;
					if (Config.QUEST.HUNTING_QUEST_ACTIVE) {
						increaseHuntingQuest(pc);// 사냥터 도감
					}
					if (Config.QUEST.BEGINNER_QUEST_ACTIVE && getNpcTemplate().getQuestKillNpc() != null) {
						beginnerQuestKillProgress();// 초보 퀘스트
					}
					
/*******************************************************************************************************************************************					
****************************************************** 버프 구슬 **************************************************************************
*******************************************************************************************************************************************/
					if (npcid == 50087) {
						int chace = random.nextInt(100) + 1;
						if (chace >= 0 && chace < 5) {// 3단 가속
							L1BuffUtil.drunken(pc, 600000);
						} else if (chace >= 5 && chace < 30) {// 에이치피회복
							new L1SkillUse(true).handleCommands(pc, L1SkillId.GREATER_HEAL, pc.getId(), pc.getX(), pc.getY(), 0, L1SkillUseType.GMBUFF);
						} else if (chace >= 30 && chace < 50) {// 엠피회복
							pc.setCurrentMp(pc.getCurrentMp() + 100);
							pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
						} else if (chace >= 50 && chace < 70) {// 블레스 웨폰
							new L1SkillUse(true).handleCommands(pc, L1SkillId.BLESS_WEAPON, pc.getId(), pc.getX(), pc.getY(), 1200, L1SkillUseType.GMBUFF);
						} else if (chace >= 70 && chace < 80 && !pc.isPassiveStatus(L1PassiveId.ADVANCE_SPIRIT) && !pc.isPassiveStatus(L1PassiveId.GIGANTIC)) {// 어드밴스 스피릿
							new L1SkillUse(true).handleCommands(pc, L1SkillId.ADVANCE_SPIRIT_1, pc.getId(), pc.getX(), pc.getY(), 1200, L1SkillUseType.GMBUFF);
						} else {// 페이션스
							new L1SkillUse(true).handleCommands(pc, L1SkillId.PATIENCE, pc.getId(), pc.getX(), pc.getY(), 600, L1SkillUseType.GMBUFF);
						}
					} else if (npcid == 101022 || npcid == 7800074) {// 3단 가속
						L1BuffUtil.drunken(pc, 600000);
					} else if (npcid == 101017 || npcid == 7800070) {// 에이치피회복
						new L1SkillUse(true).handleCommands(pc, L1SkillId.GREATER_HEAL, pc.getId(), pc.getX(), pc.getY(), 0, L1SkillUseType.GMBUFF);
					} else if (npcid == 101018 || npcid == 7800071) {// 엠피회복
						pc.setCurrentMp(pc.getCurrentMp() + 100);
						pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
					} else if (npcid == 101019 || npcid == 7800075) {// 블레스웨폰
						new L1SkillUse(true).handleCommands(pc, L1SkillId.BLESS_WEAPON, pc.getId(), pc.getX(), pc.getY(), 1200, L1SkillUseType.GMBUFF);
					} else if (npcid == 101020 || npcid == 7800072) {// 페이션스
						new L1SkillUse(true).handleCommands(pc, L1SkillId.PATIENCE, pc.getId(), pc.getX(), pc.getY(), 600, L1SkillUseType.GMBUFF);
					} else if ((npcid == 101021 || npcid == 7800073) && !pc.isPassiveStatus(L1PassiveId.ADVANCE_SPIRIT) && !pc.isPassiveStatus(L1PassiveId.GIGANTIC)) {// 어드밴스 스피릿
						new L1SkillUse(true).handleCommands(pc, L1SkillId.ADVANCE_SPIRIT_1, pc.getId(), pc.getX(), pc.getY(), 1200, L1SkillUseType.GMBUFF);
					} else if (npcid == 7800053) {
						actionIceBomb(pc);// 빙염우 폭탄
					} else if (npcid == 7800059) {
						actionFireBomb(pc);// 풍랑의 폭발
					} else if (npcid == 7800080) {
						actionWindBomb(pc);// 토네이도 폭탄
					} else if (npcid == 7800081) {
						actionIceRain(pc);// 얼음 폭우
					} else if (npcid == 7800082) {
						actionFireStorm(pc);// 화염 폭풍
					}
	
			/********************************************************************************************************		
			***************************************** 은기사 초보존 퀘스트 ************************************************
			*********************************************************************************************************/
			/*		int rnd = CommonUtil.random(100);
					int quest_num = 0, hpass_ItemId = 0, highdaily_itemId = 0;		
					if(getNpcTemplate().get_npcId() >= 9303 && getNpcTemplate().get_npcId() <= 9310 
							|| getNpcTemplate().get_npcId() >= 9316 && getNpcTemplate().get_npcId() <= 9319){ // 몬스터의 발톱
						quest_num = 1; hpass_ItemId = L1ItemId.MONSTER_TOENAIL; highdaily_itemId = L1ItemId.PUNITIVE_EXPEDITION_TOKEN;
					}else if(getNpcTemplate().get_npcId() == 9309 
							|| getNpcTemplate().get_npcId() >= 9311 && getNpcTemplate().get_npcId() <= 9315){ // 몬스터의 이빨
						quest_num = 2; hpass_ItemId = L1ItemId.MONSTER_TOOTH; highdaily_itemId = L1ItemId.PUNITIVE_EXPEDITION_TOKEN;
					}else if(pc.getMapId() == 25){ // 녹슨 투구
						quest_num = 3; hpass_ItemId = L1ItemId.RUST_HELM; highdaily_itemId = L1ItemId.PUNITIVE_EXPEDITION_TOKEN;
					}else if(pc.getMapId() == 26){ // 녹슨 장갑
						quest_num = 4; hpass_ItemId = L1ItemId.RUST_GLOVE; highdaily_itemId = L1ItemId.PUNITIVE_EXPEDITION_TOKEN;
					}else if(pc.getMapId() == 27 || pc.getMapId() == 28){ // 녹슨 부츠
						quest_num = 5; hpass_ItemId = L1ItemId.RUST_BOOTS; highdaily_itemId = L1ItemId.PUNITIVE_EXPEDITION_TOKEN;
					}
					if(quest_num != 0){
						if(pc.getQuest().get_step(L1Quest.QUEST_HPASS) == quest_num && rnd <= 60)
							pc.getInventory().createItem(this.getName(), hpass_ItemId, 1, 0);
					}
					if((pc.getQuest().get_step(L1Quest.QUEST_HIGHDAILY) >= 1 && pc.getQuest().get_step(L1Quest.QUEST_HIGHDAILY) >= 14) && rnd <= 60)
						pc.getInventory().createItem(this.getName(), highdaily_itemId, 1, 0);
					switch(pc.getQuest().get_step(L1Quest.QUEST_HIGHDAILYB)){
					case 1:case 3:case 5:case 7:case 9:case 11:case 13:
						if(pc.getMapId() == 2010){
							if(rnd <= 60)pc.getInventory().createItem(this.getName(), L1ItemId.VARIETY_DRAGON_BONE, 1, 0); // 변종 드래곤의 뼈
						}
						break;
					default:break;
					}*/			
				}
			/********************************************************************************************************		
			***************************************** 행운의 장소 ******************************************************
			*********************************************************************************************************/	
			/*	if(getNpcTemplate().get_npcId() >= 7000088 && getNpcTemplate().get_npcId() <= 7000090){
					int chance = random1.nextInt(500) + 1;
					if(chance < 3){
						if(attacker instanceof L1PcInstance){
							L1PcInstance player = (L1PcInstance) attacker;
							L1Teleport.teleport(player, 33392, 32345, (short) 15430, player.getHeading(), true); //큰뼈
						}
					}else if(chance < 6){
						if(attacker instanceof L1PcInstance){
							L1PcInstance player = (L1PcInstance) attacker;
							L1Teleport.teleport(player, 33262, 32402, (short) 15430, player.getHeading(), true); //작뼈
						}
					}else if(chance < 9){
						if(attacker instanceof L1PcInstance){
							L1PcInstance player = (L1PcInstance) attacker;
							L1Teleport.teleport(player, 33335, 32437, (short) 15430, player.getHeading(), true); //삼거리
						}
					}else if(chance < 11){
						if(attacker instanceof L1PcInstance){
							L1PcInstance player = (L1PcInstance) attacker;
							L1Teleport.teleport(player, 33457, 32338, (short) 15430, player.getHeading(), true); //아덴의한국민 약물상인 위치
						}
					}
				}*/
		/********************************************************************************************************		
		 ***************************************** 보스의 영혼석 ****************************************************
		 *********************************************************************************************************/	
//				int[] lastabard = { 80453, 80454, 80455, 80456, 80457, 80458, 80459, 80460, 80461, 80462, 80463, 80452 };
//				int[] tower = { 80450, 80451, 80466, 80467 };
//				int[] glu = { 80464, 80465 };
//				int[] oman = { 80468,80469,80470,80471,80472,80473,80474,80475,80476,80477 };
//				int 드랍율 = random.nextInt(2500) + 1;
//				int 라던 = random.nextInt(lastabard.length);
//				int 상아탑 = random.nextInt(tower.length);
//				int 본던 = random.nextInt(glu.length);
//				int 오만 = random.nextInt(oman.length);
//				switch (attacker.getMapId()) {
//				case 479:case 475:case 462:case 453:case 492:
//					if(2 >= 드랍율){
//						attacker.getInventory().storeItem(lastabard[라던], 1);
//						((L1PcInstance) attacker).sendPackets(new S_SystemMessage("보스의 영혼석을 획득하였습니다."), true);
//					}break;
//				case 78:case 79:case 80:case 81:case 82:
//					if(2 >= 드랍율){// 상아탑
//						attacker.getInventory().storeItem(tower[상아탑], 1);
//						((L1PcInstance) attacker).sendPackets(new S_SystemMessage("보스의 영혼석을 획득하였습니다."), true);
//					}break;
//				case 807:case 808:case 809:case 810:case 811:case 812:case 813:
//					if(2 >= 드랍율){// 본던
//						attacker.getInventory().storeItem(glu[본던], 1);
//						((L1PcInstance) attacker).sendPackets(new S_SystemMessage("보스의 영혼석을 획득하였습니다."), true);
//					}break;
//				case 101:case 102:case 103:case 104:case 105:case 106:case 107:case 108:case 109:case 110:case 111:
//					if(2 >= 드랍율){// 오만
//						attacker.getInventory().storeItem(oman[오만], 1);
//						((L1PcInstance) attacker).sendPackets(new S_SystemMessage("보스의 영혼석을 획득하였습니다."), true);
//					}break;
//				}
				
				if (attacker instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) attacker;
					
					/*if (pc.getMapId() == 1931 && (npcid >= 47900 && npcid <= 47909 || npcid >= 45551 && npcid <= 45561)) {// 몽섬 악령의씨앗
						L1ItemInstance inventoryItem = pc.getInventory().findItemId(810008);
						int inventoryItemCount = 0;
						if (inventoryItem != null) {
							inventoryItemCount = inventoryItem.getCount();
						}
						if (inventoryItemCount < 5) {
							if (4 >= random.nextInt(101)) {
								pc.getInventory().storeItem(810008, 1);
								if (inventoryItemCount == 0) {
//AUTO SRM: 									pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "악령의 씨앗이 몸속으로 스며듭니다."), true);
									pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,S_SystemMessage.getRefText(11)), true);
								} else if (inventoryItemCount == 1) {
//AUTO SRM: 									pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "악령의 씨앗 2개가 생겼습니다. 악령의 기운이 느껴집니다."), true);
									pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,S_SystemMessage.getRefText(12)), true);
								} else if (inventoryItemCount == 2) {
//AUTO SRM: 									pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "악령의 씨앗 3개를 생겼습니다. 악령이 말을 걸어옵니다."), true);
									pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,S_SystemMessage.getRefText(13)), true);
								} else if (inventoryItemCount == 3) {
//AUTO SRM: 									pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "악령의 씨앗 4개가 생겼습니다. 너무 많으면 악령에 지배 당할 수 있어요."), true);
									pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,S_SystemMessage.getRefText(14)), true);
								} else if (inventoryItemCount >= 4) {
//AUTO SRM: 									pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "악령의 씨앗이 너무 많이 생겼어요! Tam 샵 에킨스를 만나세요."), true);
									pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,S_SystemMessage.getRefText(15)), true);
								}
							}
						} else {
							pc.getTeleport().start(33970, 32958, (short)  4, pc.getMoveState().getHeading(), true);
						}
					} else */if (npcid == 45609 || npcid == 55535 || npcid == 81201) {// 혹한의 신전 보스
				    	L1ItemInstance tempitem = pc.getInventory().storeItem(410555, 1);
				    	pc.sendPackets(new S_ServerMessage(403, tempitem.getDesc()), true);
					} else if (npcid == 7000075 && pc.getMapId() >= 807 && pc.getMapId() <= 813) {// 본던 용아병의 혼령 파란색 동일층
						pc.getTeleport().randomTeleport(true, 200);
					} else if (npcid == 7000074 && pc.getMapId() >= 807 && pc.getMapId() <= 813) {// 본던 용아병의 혼령 노란색 아래층
						if (pc.getMapId() == 813) {
							pc.getTeleport().randomTeleport(true, 200);
						} else {
							pc.getTeleport().start(new L1Location(pc.getX(), pc.getY(), pc.getMapId() + 1).randomLocation(200, false), pc.getMoveState().getHeading(), true);
						}
					} else if ((npcid == 800553 && getMapId() == 15482) || (npcid == 800551 && getMapId() == 15483) || (npcid == 800554 && getMapId() == 15484)) {
						wolrdWarBoss(pc);// 월드 공성전
					}
				}
				
				// 부활 허수아비
				if (npcid >= 100774 && npcid <= 100776) {
					scarecrowResurrect();
				}
				if (npcid >= 20828 && npcid <= 20838) {
					npcDieAction(attacker, npcid);
				}
				
				int transformId = getNpcTemplate().getTransformId();
				if (_info != null && _info._transformChance > 0 && random.nextInt(100) + 1 <= _info._transformChance) {
					npcTransformAction(attacker);
				} else if (transformId == -1) {// death
					
					// 확률 변신
					if (omanCrackTrans() || oldGodGardenTrans() || dominationTransRiper() || (Config.ALT.EVENT_MONSTER_ACTIVE && eventMonster())) {
						return;
					}
					
					// 정벌의 외침
					if (_info != null && _info._dieMsgPcList) {
						bossDieShoutMessage();
					}
					
					setCurrentHp(0);
					setDead(true);
					setActionStatus(ActionCodes.ACTION_Die);
					if (((getNpcTemplate().getNpcId() != 50087 && getNpcTemplate().getSpriteId() == 7684)
							|| getNpcTemplate().getSpriteId() == 7805 
							|| getNpcTemplate().getSpriteId() == 8063) && _PapPearlMonster != null) {
						_PapPearlMonster.begin();
						_PapPearlMonster = null;
					}
					GeneralThreadPool.getInstance().execute(new Death(attacker));
				} else {// transform
					if (transformId == 45753 && getMapId() == 15404) {// 발록 변신 시
						balrogTransDoorAction();
					} else if ((transformId >= 20956 && transformId <= 20958) && getMapId() == 140) {// 드래곤의 서식지
						for (L1PcInstance pc : L1World.getInstance().getMapPlayer(getMapId())) {
							pc.sendPackets(L1GreenMessage.DRAGON_DUNGEON_TRANS);
						}
					}
					transForm(transformId);
				}
			}
			
			if (newHp > 0) {
				setCurrentHp(newHp);
				hide();
			}
		} else if (!isDead()) {
			setDead(true);
			setActionStatus(ActionCodes.ACTION_Die);
			GeneralThreadPool.getInstance().execute(new Death(attacker));
		}
	}
	
	/**
	 * 지배의 결계2층 발록 변신 이벤트
	 */
	void balrogTransDoorAction(){
		for (L1DoorInstance door : L1World.getInstance().getAllDoor()) {
			if (door.getDoorId() == 227 && door.getOpenStatus() == ActionCodes.ACTION_Open) {// 발록방 중앙문 스폰
				door.close();
			}
		}
	}
	
	/**
	 * 정복의 외침
	 */
	void bossDieShoutMessage(){
		L1Character[] charlist = _dropHateList.getMaxHateCharacterList();
		if (charlist != null) {
			L1World.getInstance().broadcastPacketToAll(new S_ServerMessage(3320, getName(), (charlist[0] != null ? charlist[0].getName() : StringUtil.EmptyString), (charlist[1] != null ? charlist[1].getName() : StringUtil.EmptyString), (charlist[2] != null ? charlist[2].getName() : StringUtil.EmptyString)), true);
			charlist = null;
		}
	}
	
	/**
	 * 몬스터 변신 이벤트
	 * @param attacker
	 */
	void npcTransformAction(L1Character attacker){
		if (_info._transformGfxId > 0) {
			broadcastPacket(new S_Effect(getId(), _info._transformGfxId), true);
		}
		if (_info._scriptInfo != null) {
			scriptAction();
		}
		transForm(_info._transformId);
	}
	
	/**
	 * 스크립트 액션 이벤트
	 */
	void scriptAction(){
		ScriptInfo script = _info._scriptInfo;
		ArrayList<L1PcInstance> pcList = L1World.getInstance().getMapPlayer(this.getMapId());
		long script_start_time = System.currentTimeMillis() / 1000;
		if (script._map != null) {
			scriptAction(script._type, script._map, pcList, script_start_time, true);
			dominationWake(script._map, true);
		}
		if (script._effect != null)	{
			scriptAction(script._type, script._effect, pcList, script_start_time, true);
		}
		if (script._stay != null) {
			scriptAction(script._type, script._stay, pcList, script_start_time, true);
			GeneralThreadPool.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					try {
						scriptAction(script._type, script._stay, pcList, 0, false);
					} catch(Exception e) {}
				}
			}, 10000);
		}
	}
	
	void scriptAction(ScriptType scriptType, Object obj, ArrayList<L1PcInstance> pcList, long script_start_time, boolean is_enable){
		if (pcList == null || pcList.isEmpty()) {
			return;
		}
		S_SceneNoti scene = null;
		switch(scriptType){
		case NUMBER:	scene = new S_SceneNoti(is_enable, script_start_time, (Integer)obj, getX(), getY());break;
		case TEXT:		scene = new S_SceneNoti(!is_enable, (String)obj, script_start_time, getX(), getY());break;
		default:return;
		}
		for (L1PcInstance pc : pcList) {
			pc.sendPackets(scene);
		}
		scene.clear();
		scene = null;
	}
	
	/**
	 * 지배의 탑 각성
	 * @param obj
	 * @param active
	 */
	void dominationWake(Object obj, boolean active){
		short mapId = getMapId();
		if (mapId == 12852 && obj instanceof Integer && (Integer)obj == 128520000) {
			BossSpawn._wakeZenith	= active;
		} else if (mapId == 12853 && obj instanceof Integer && (Integer)obj == 128530000) {
			BossSpawn._wakeSeia		= active;
		} else if (mapId == 12854 && obj instanceof Integer && (Integer)obj == 128540000) {
			BossSpawn._wakeBampha	= active;
		} else if (mapId == 12855 && obj instanceof Integer && (Integer)obj == 128550000) {
			BossSpawn._wakeZombie	= active;
		} else if (mapId == 12856 && obj instanceof Integer && (Integer)obj == 128560000) {
			BossSpawn._wakeCouger	= active;
		} else if (mapId == 12857 && obj instanceof Integer && (Integer)obj == 128570000) {
			BossSpawn._wakeMumy		= active;
		} else if (mapId == 12858 && obj instanceof Integer && (Integer)obj == 128580000) {
			BossSpawn._wakeIris		= active;
		} else if (mapId == 12859 && obj instanceof Integer && (Integer)obj == 128590000) {
			BossSpawn._wakeKnight	= active;
		} else if (mapId == 12860 && obj instanceof Integer && (Integer)obj == 128600000) {
			BossSpawn._wakeRich		= active;
		} else if (mapId == 12861 && obj instanceof Integer && (Integer)obj == 128610000) {
			BossSpawn._wakeUgu		= active;
		} else if (mapId == 12862 && obj instanceof Integer && (Integer)obj == 128620000) {
			BossSpawn._wakeSasin	= active;
		}
	}
	
	public void dominationWakeEnd(){
		Integer mapScriptId = 0;
		int npcId = getNpcId();
		short mapId = getMapId();
		if (npcId == 800250 && mapId == 12852) {
			mapScriptId = 128520000;
		} else if (npcId == 800251 && mapId == 12853) {
			mapScriptId = 128530000;
		} else if (npcId == 800252 && mapId == 12854) {
			mapScriptId = 128540000;
		} else if (npcId == 800253 && mapId == 12855) {
			mapScriptId = 128550000;
		} else if (npcId == 800254 && mapId == 12856) {
			mapScriptId = 128560000;
		} else if (npcId == 800255 && mapId == 12857) {
			mapScriptId = 128570000;
		} else if (npcId == 800256 && mapId == 12858) {
			mapScriptId = 128580000;
		} else if (npcId == 800257 && mapId == 12859) {
			mapScriptId = 128590000;
		} else if (npcId == 800258 && mapId == 12860) {
			mapScriptId = 128600000;
		} else if (npcId == 800259 && mapId == 12861) {
			mapScriptId = 128610000;
		} else if (npcId == 800260 && mapId == 12862) {
			mapScriptId = 128620000;
		}
		if (mapScriptId == 0) {
			return;
		}
		dominationWake(mapScriptId, false);
		scriptAction(ScriptType.NUMBER, mapScriptId, L1World.getInstance().getMapPlayer(getMapId()), 0 , false);
	}
	
	/**
	 * 아이스 폭탄
	 * @param attacker
	 */
	void actionIceBomb(L1PcInstance attacker){
		broadcastPacket(new S_Effect(this.getId(), 7771), true);
		for (L1Object obj : L1World.getInstance().getVisibleObjects(this, -1)) {
			if (obj == this || obj == attacker) {
				continue;
			}
			if (obj instanceof L1MonsterInstance) {
				L1MonsterInstance npc = (L1MonsterInstance) obj;
				if (npc.getNpcTemplate().getNpcId() != 7800000 && !npc.isDead() && !npc._destroyed) {
					npc.broadcastPacket(new S_DoActionGFX(npc.getId(), ActionCodes.ACTION_Damage), true);
					npc.action_damage(attacker, 500);
				}
			}
		}
	}
	
	/**
	 * 화염의 폭탄
	 * @param attacker
	 */
	void actionFireBomb(L1PcInstance attacker){
		broadcastPacket(new S_Effect(this.getId(), 7361), true);
		for (L1Object obj : L1World.getInstance().getVisibleObjects(this, -1)) {
			if (obj == this || obj == attacker) {
				continue;
			}
			if (obj instanceof L1MonsterInstance) {
				L1MonsterInstance npc = (L1MonsterInstance) obj;
				if (npc.getNpcTemplate().getNpcId() != 7800000 && !npc.isDead() && !npc._destroyed) {
					npc.broadcastPacket(new S_DoActionGFX(npc.getId(), ActionCodes.ACTION_Damage), true);
					npc.action_damage(attacker, 500);
				}
			}
		}
	}
	
	/**
	 * 토네이도 폭탄
	 * @param attacker
	 */
	void actionWindBomb(L1PcInstance attacker){
		broadcastPacket(new S_Effect(this.getId(), 1819), true);
		for (L1Object obj : L1World.getInstance().getVisibleObjects(this, -1)) {
			if (obj == this || obj == attacker) {
				continue;
			}
			if (obj instanceof L1MonsterInstance) {
				L1MonsterInstance npc = (L1MonsterInstance) obj;
				if (npc.getNpcTemplate().getNpcId() != 7800000 && !npc.isDead() && !npc._destroyed) {
					npc.broadcastPacket(new S_DoActionGFX(npc.getId(), ActionCodes.ACTION_Damage), true);
					npc.action_damage(attacker, 500);
				}
			}
		}
	}
	
	/**
	 * 얼음 폭우
	 * @param attacker
	 */
	void actionIceRain(L1PcInstance attacker){
		broadcastPacket(new S_Effect(this.getId(), 7771), true);
		for (L1Object obj : L1World.getInstance().getVisibleObjects(this, -1)) {
			if (obj == this || obj == attacker) {
				continue;
			}
			if (obj instanceof L1MonsterInstance) {
				L1MonsterInstance npc = (L1MonsterInstance) obj;
				if (!npc.isDead() && !npc._destroyed) {
					npc.broadcastPacket(new S_DoActionGFX(npc.getId(), ActionCodes.ACTION_Damage), true);
					npc.action_damage(attacker, 500);
				}
			}
		}
	}
	
	/**
	 * 화염 폭풍
	 * @param attacker
	 */
	void actionFireStorm(L1PcInstance attacker){
		broadcastPacket(new S_Effect(this.getId(), 1819), true);
		for (L1Object obj : L1World.getInstance().getVisibleObjects(this, -1)) {
			if (obj == this || obj == attacker) {
				continue;
			}
			if (obj instanceof L1MonsterInstance) {
				L1MonsterInstance npc = (L1MonsterInstance) obj;
				if (!npc.isDead() && !npc._destroyed) {
					npc.broadcastPacket(new S_DoActionGFX(npc.getId(), ActionCodes.ACTION_Damage), true);
					npc.action_damage(attacker, 500);
				}
			}
		}
	}
	
	/**
	 * 몬스터의 대미지에 의한 주위 대미지 처리
	 * 재귀호출 방지
	 * @param attacker
	 * @param damage
	 */
	public void action_damage(L1Character attacker, int damage) {
		if (attacker == null || damage <= 0 || isDead()) {
			return;
		}
		int current_hp = getCurrentHp();
		if (damage > current_hp) {
			damage = current_hp;
		}
		setHate(attacker, damage);// 공격 공헌도
		int after_hp = current_hp - damage;
		if (after_hp <= 0) {
			setCurrentHp(0);
			setDead(true);
			setActionStatus(ActionCodes.ACTION_Die);
			GeneralThreadPool.getInstance().execute(new Death(attacker));
		} else {
			setCurrentHp(after_hp);
		}
	}
	
	/**
	 * 사냥터 도감
	 * @param pc
	 */
	void increaseHuntingQuest(L1PcInstance pc){
		if (getNpcTemplate().getTransformId() != -1) {
			return;
		}
		if (pc.getHuntingQuest() == null || pc.getHuntingQuest().getInfo().isEmpty()) {
			return;
		}
		Collection<HuntingQuestUserTemp> tempList	=	pc.getHuntingQuest().getInfo().values();
		HuntingQuestUserTemp temp					=	null;
		int locationDesc							=	getMapId() == 4 ? getLocationDesc() : 0;
		for (HuntingQuestUserTemp checktemp : tempList) {
			if (checktemp.isComplete() || checktemp.getKillCount() >= Config.QUEST.HUNTING_QUEST_CLEAR_VALUE) {
				continue;// 완료된 퀘스트 제외
			}
			if (checktemp.getMapNumber() == getMapId() && checktemp.getLocationDesc() == locationDesc) {// 좌표 체크
				temp = checktemp;
				break;
			}
		}
		if (temp == null) {
			return;
		}
		temp.addKillCount(1);
		pc.sendPackets(new S_HuntingQuestUpdateAmount(temp), true);
		if (temp.getKillCount() >= Config.QUEST.HUNTING_QUEST_CLEAR_VALUE) {// clear
			pc.sendPackets(new S_HuntingQuestMapList(tempList), true);
		}
	}
	
	/**
	 * 몬스터의 좌표 기준으로 mapDesc를 조사한다.
	 * @return mapDesc
	 */
	int getLocationDesc(){
		// desc : MapSystem86.xml 기준
		// loc : zone3.xml 기준
		int hunX = getX(), huntY = getY();
		if ((hunX >= 33683 && hunX <= 33849) && (huntY >= 33278 && huntY <= 33497)) {
			return 468;// 거울숲
		}
		if ((hunX >= 32936 && hunX <= 33040) && (huntY >= 33289 && huntY <= 33493)) {
			return 481;// 거친황야
		}
		if ((hunX >= 33006 && hunX <= 33243) && (huntY >= 33121 && huntY <= 33306)) {
			return 649;// 고요한숲
		}
		if ((hunX >= 32778 && hunX <= 32980) && (huntY >= 32838 && huntY <= 32977)) {
			return 479;// 망자의무덤
		}
		if ((hunX >= 32677 && hunX <= 32949) && (huntY >= 33079 && huntY <= 33350)) {
			return 657;// 메마른사막
		}
		if ((hunX >= 33721 && hunX <= 33893) && (huntY >= 32617 && huntY <= 32788)) {
			return 466;// 밀림지대
		}
		if ((hunX >= 33165 && hunX <= 33421) && (huntY >= 32953 && huntY <= 33033)) {
			return 465;// 암흑룡의상흔
		}
		if ((hunX >= 33935 && hunX <= 34219) && (huntY >= 32374 && huntY <= 32596)) {
			return 328;// 엘모어격전지
		}
		if ((hunX >= 32546 && hunX <= 32898) && (huntY >= 32219 && huntY <= 32407)) {
			return 463;// 오크부락
		}
		if ((hunX >= 33387 && hunX <= 33523) && (huntY >= 32257 && huntY <= 32508)) {
			return 653;// 용의계곡 해안가
		}
		if ((hunX >= 32960 && hunX <= 33248) && (huntY >= 33131 && huntY <= 33506)) {
			return 650;// 은기사 필드
		}
		if ((hunX >= 32686 && hunX <= 32931) && (huntY >= 32575 && huntY <= 32767)) {
			return 464;// 죽음의 폐허
		}
		if ((hunX >= 33290 && hunX <= 33517) && (huntY >= 33133 && huntY <= 33501)) {
			return 342;// 하이네 숲
		}
		if ((hunX >= 32583 && hunX <= 32932) && (huntY >= 33362 && huntY <= 33494)) {
			return 652;// 황무지
		}
		if ((hunX >= 34304 && hunX <= 34490) && (huntY >= 32240 && huntY <= 32439)) {
			return 666;// 저주받은 광산
		}
		return 0;
	}
	
	/**
	 * 텔레포트 몬스터
	 * @param attacker
	 */
	void teleportMonster(L1Character attacker){
		if (isStop()) {
			return;
		}
		if (attacker.getMapId() == getMapId() 
				&& isHalfHp() && getCurrentMp() >= 10 
				&& getLocation().getTileDistance(attacker.getLocation()) < 3) {
			boolean boss = getNpcTemplate().isBossMonster();
			if (!boss && 1 >= random.nextInt(60) + 1) {// 일반몹 확률
				isTeleportDmgAction = true;
			} else if (boss && 1 >= random.nextInt(300) + 1) {// 보스 확률
				isTeleportDmgAction = true;
			}
		}
	}

	/**
	 * 원거리 공격 유저 소환
	 * @param pc
	 */
	void recall(L1PcInstance pc) {
		if (pc == null || getMapId() != pc.getMapId()) {
			return;
		}
		if (getLocation().getTileLineDistance(pc.getLocation()) > 15) {
			L1Location newLoc = null;
			for (int count = 0; count < 10; count++) {
				newLoc = getLocation().randomLocation(3, 4, false);
				if (glanceCheck(15, newLoc.getX(), newLoc.getY(), false)) {
					pc.getTeleport().start(newLoc.getX(), newLoc.getY(), getMapId(), pc.getMoveState().getHeading(), true);
					break;
				}
			}
		}
	}
	
	/**
	 * 검은 기사단의 요리사
	 */
	void blackCookerDamage(){
		broadcastPacket(new S_NpcChatPacket(this, "$27105", ChatType.CHAT_NORMAL), true);// 살려주시오!
	    _blackCooker = true;
	    GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				if (L1MonsterInstance.this != null && !L1MonsterInstance.this.isDead()) {
					L1MonsterInstance.this._blackCooker = false;
					L1MonsterInstance.this.tagertClear();
				}
			}
		}, 10000L);
	}
	
	/**
	 * 허수아비 재스폰
	 */
	void scarecrowResurrect(){
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				resurrect(getMaxHp());
			}
		}, 4000L);
	}

	@Override
	public void setCurrentHp(int i) {
		super.setCurrentHp(i);
		if (getMaxHp() > getCurrentHp()) {
			startHpRegeneration();
		}
	}

	@Override
	public void setCurrentMp(int i) {
		super.setCurrentMp(i);
		if (getMaxMp() > getCurrentMp()) {
			startMpRegeneration();
		}
	}

	/**
	 * 파푸리온 레이드 사엘
	 * @param Sahel
	 */
	void Sahel(L1NpcInstance Sahel){
		L1NpcInstance Pearl = null;
		L1PcInstance PearlBuff = null;
		Sahel.receiveDamage(Sahel, 1);
		for (L1Object obj : L1World.getInstance().getVisibleObjects(Sahel, 5)) {
			if (obj instanceof L1MonsterInstance) {
				L1MonsterInstance mon = (L1MonsterInstance) obj;
				if ((mon.getNpcTemplate().getNpcId() != 50087 && mon.getNpcTemplate().getSpriteId() == 7684) || mon.getNpcTemplate().getSpriteId() == 7805) {
					Pearl = mon;
				}
			}
			if (obj instanceof L1PcInstance) {
				L1PcInstance Buff = (L1PcInstance) obj;
				if (!(Buff.getSkill().hasSkillEffect(L1SkillId.PAP_FIVEPEARLBUFF) ||Buff.getSkill().hasSkillEffect(L1SkillId.PAP_MAGICALPEARLBUFF))) {
					PearlBuff = Buff;
				}
			}
		}
		if (Pearl.getNpcTemplate().getNpcId() != 50087 && Pearl.getNpcTemplate().getSpriteId() == 7684 && PearlBuff != null	&& Pearl.getCurrentHp() > 0 && PearlBuff.getCurrentHp() > 0) {
			PearlBuff.send_effect(7836);
			PearlBuff.getSkill().setSkillEffect(L1SkillId.PAP_FIVEPEARLBUFF, 60 * 1000);
			Sahel.broadcastPacket(new S_NpcChatPacket(Sahel, "$8468", ChatType.CHAT_NORMAL), true); // 힐을 줍니다
		}
		if (Pearl.getNpcTemplate().getSpriteId() == 7805 && PearlBuff != null	&& Pearl.getCurrentHp() > 0 && PearlBuff.getCurrentHp() > 0) {
			PearlBuff.send_effect(7834);
			PearlBuff.getSkill().setSkillEffect(L1SkillId.PAP_MAGICALPEARLBUFF, 60 * 1000);
			if (PearlBuff.isKnight() || PearlBuff.isCrown() || PearlBuff.isDarkelf() || PearlBuff.isDragonknight() || PearlBuff.isWarrior() || PearlBuff.isFencer() || PearlBuff.isLancer()) {
				Sahel.broadcastPacket(new S_NpcChatPacket(Sahel, "$8471", ChatType.CHAT_NORMAL), true); // 근거리 물리력에
			} else if (PearlBuff.isElf()) {
				Sahel.broadcastPacket(new S_NpcChatPacket(Sahel, "$8472", ChatType.CHAT_NORMAL), true); // 원거리 물리력에
			} else if (PearlBuff.isWizard() || PearlBuff.isIllusionist()) {
				Sahel.broadcastPacket(new S_NpcChatPacket(Sahel, "$8470", ChatType.CHAT_NORMAL), true); // 마법에
			} else {
				Sahel.broadcastPacket(new S_NpcChatPacket(Sahel, "$8469", ChatType.CHAT_NORMAL), true); // 헤이스트
			}
		}
	}

	/**
	 * 파푸리온 레이드 진주
	 * @param Pearl
	 */
	void PapPearl(L1NpcInstance Pearl){
		L1NpcInstance Pap = null;
		L1PcInstance PearlBuff = null;
		for (L1Object obj : L1World.getInstance().getVisibleObjects(Pearl, 10)) {
			if (obj instanceof L1MonsterInstance) {
				L1MonsterInstance mon = (L1MonsterInstance) obj;
				if (mon.getNpcTemplate().getSpriteId() == 7864 || mon.getNpcTemplate().getSpriteId() == 7869 || mon.getNpcTemplate().getSpriteId() == 7870) {
					Pap = mon;
				}
			}
			if (obj instanceof L1PcInstance) {
				L1PcInstance Buff = (L1PcInstance) obj;
				if (!(Buff.getSkill().hasSkillEffect(L1SkillId.PAP_FIVEPEARLBUFF) ||Buff.getSkill().hasSkillEffect(L1SkillId.PAP_MAGICALPEARLBUFF))) {
					PearlBuff = Buff;
				}
			}
		}
		int PearlBuffRandom = random.nextInt(10) + 1;
		if (Pap != null && Pap.getCurrentHp() > 0 
				&& (Pearl.getNpcTemplate().getNpcId() != 50087 && Pearl.getNpcTemplate().getSpriteId() == 7684) 
				&& Pearl.getCurrentHp() > 0) {
			int newHp = Pap.getCurrentHp()+ 3000;
			Pap.setCurrentHp(newHp);
			Pearl.broadcastPacket(new S_Effect(Pearl.getId(), 233), true);
			L1EffectSpawn.getInstance().spawnEffect(900055, 1 * 1000, Pap.getX(), Pap.getY(), Pap.getMapId());
		} else if (Pap != null && Pap.getCurrentHp() > 0 && Pearl.getNpcTemplate().getSpriteId() == 7805 && Pearl.getCurrentHp() > 0) {
			Pap.getMoveState().setMoveSpeed(1);
			Pap.getSkill().setSkillEffect(L1SkillId.STATUS_HASTE, 30 * 1000);
			Pearl.broadcastPacket(new S_Effect(Pearl.getId(), 224), true);
		}
		if (PearlBuff != null && PearlBuffRandom == 3 && (Pearl.getNpcTemplate().getNpcId() != 50087 && Pearl.getNpcTemplate().getSpriteId() == 7684)) { // 오색
			PearlBuff.send_effect(7836);
			PearlBuff.getSkill().setSkillEffect(L1SkillId.PAP_FIVEPEARLBUFF, 60 * 1000);
		} else if (PearlBuff != null && PearlBuffRandom == 5 && Pearl.getNpcTemplate().getSpriteId() == 7805) { // 신비
			PearlBuff.send_effect(7834);
			PearlBuff.getSkill().setSkillEffect(L1SkillId.PAP_MAGICALPEARLBUFF, 60 * 1000);
		}
	}

	/**
	 * 몬스터 처치시 이벤트 몬스터 등장 이벤트
	 * @return boolean
	 */
	boolean eventMonster() {
		if (getLevel() >= Config.ALT.EVENT_MONSTER_LEVEL && getNpcId() != Config.ALT.EVENT_MONSTER_ID && random.nextInt(100) < Config.ALT.EVENT_MONSTER_CHANCE) {
			if (Config.ALT.EVENT_MONSTER_EFFECT > 0) {
				broadcastPacket(new S_Effect(getId(), Config.ALT.EVENT_MONSTER_EFFECT), true);
			}
			transForm(Config.ALT.EVENT_MONSTER_ID);
			return true;
		}
		return false;
	}
	
	/**
	 * 지배의 탑 감시자 리퍼 등장 이벤트
	 * @return boolean
	 */
	boolean dominationTransRiper(){
		if (!(getMapId() >= 12852 && getMapId() <= 12861)) {
			return false;
		}
		int npcId = getNpcId();
		if (!(npcId == 800124 || npcId == 800126 || npcId == 800128 || npcId == 800130 || npcId == 800131 || npcId == 800134 
				|| npcId == 800135 || npcId == 800138 || npcId == 800141 || npcId == 800142 || npcId == 45590)) {
			return false;
		}
		if (npcId == 45590) {// 감시자 리퍼
			if (random.nextInt(10) + 1 == 1) { // 10
				int bossId = 700144 + (getMapId() - 12852);
				broadcastPacket(new S_Effect(getId(), 4784), true);
				transForm(bossId);// 보스 변신
				return true;
			}
		} else {// 일반 몬스터
			if (random.nextInt(10000) + 1 < 1) { // 0.01
				broadcastPacket(new S_Effect(getId(), 4784), true);
				transForm(45590);// 감시자 리퍼
				return true;
			}
		}
		return false;
	}
	
	public void transForm(int transId){
		curePoison();
		transform(transId);
		transSetting(transId);
	}
	
	/**
	 * 균열의 오만의 탑 몬스터 처치 이벤트
	 * @return boolean
	 */
	boolean omanCrackTrans(){
		if (!(getMapId() >= 101 && getMapId() <= 110)) {
			return false;
		}
		if (getNpcId() >= 800190 && getNpcId() <= 800199) {
			return false;
		}
		short crackMap = GameServerSetting.OMAN_CRACK;
		if (crackMap <= 0 || getMapId() != crackMap) {
			return false;
		}
		if (random.nextInt(100) + 1 <= Config.DUNGEON.OMAN_CRACK_TRANS_RATE) {
			int transId = 800190 + (getMapId() - 101);
			broadcastPacket(new S_Effect(getId(), 5477), true);
			transForm(transId);
			return true;
		}
		return false;
	}
	
	/**
	 * 고대 신의 사원 변신 몬스터 이벤트
	 * @return boolean
	 */
	boolean oldGodGardenTrans(){
		if (getMapId() != 1209) {
			return false;
		}
		if (!(getNpcId() >= 800201 && getNpcId() <= 800207)) {
			return false;
		}
		if (random.nextInt(100) + 1 <= Config.DUNGEON.OLD_GOD_TRANS_RATE) {
			broadcastPacket(new S_Effect(getId(), 5477), true);
			transForm(800208);
			return true;
		}
		return false;
	}
	
	/**
	 * 화룡의 둥지 분신 몬스터 이벤트
	 * @param lastAttacker
	 */
	void fireDungeonMonster(L1Character lastAttacker){
		if (getMapId() != 15440 || !(lastAttacker instanceof L1PcInstance)) {
			return;
		}
		if (random.nextInt(10000) + 1 < 5) {// 확률
			L1SpawnUtil.spawn2(this.getX(),this.getY(), this.getMapId(), 5, 7210035, 0, 3600 * 1000, 0);// 화룡의 수호자 스폰
			broadcastPacket(new S_Effect(getId(), 4784), true);
		} else {
			((L1PcInstance) lastAttacker).getTeleport().randomTeleport(true, 200);// 텔레포트
		}
	}

	class Death implements Runnable {
		L1Character _lastAttacker;

		public Death(L1Character lastAttacker) {
			_lastAttacker = lastAttacker;
		}

		@Override
		public void run() {
			int npcId = getNpcTemplate().getNpcId();
			if (Config.QUEST.BEGINNER_QUEST_ACTIVE && getNpcTemplate().getQuestDropNpc() != null) {
				beginnerQuestDropItem();
			}
			setDeathProcessing(true);
			setCurrentHp(0);
			setDead(true);
			setActionStatus(ActionCodes.ACTION_Die);
			getMap().setPassable(getLocation(), true);
			broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_Die), true);
			startChat(CHAT_TIMING_DEAD);
			if (_lastAttacker != null) {
				distributeExpDropKarma(_lastAttacker);
			}
			//distributeExpDropKarma(_lastAttacker);
			//die(_lastAttacker);
			giveUbSeal();
			setDeathProcessing(false);
			setExp(0);
			setAlignment(0);
			setKarma(0);
			allTargetClear();
			startDeleteTimer();
			
			if (_lastAttacker == null || _lastAttacker instanceof L1AiUserInstance) {
				return;
			}
			switch(npcId){
			case 5182:
				if (getMap().getInter() == L1InterServer.ANT_QUEEN) {
					antQueenDie(_lastAttacker);
				}
				break;
			case 20956:case 20957:case 20958:
				dragonDungeonBoss();
				break;
			case 46200:case 46201:case 46202:case 46203:case 46204:case 46205:case 46206:case 46207:case 46208:case 46209:
				fireDungeonMonster(_lastAttacker);
				break;
			default:
				npcDieAction(_lastAttacker, npcId);
				break;
			}
			calcCombo(_lastAttacker);
			
			int doorId = getNpcTemplate().getDoor();
			if (doorId > 0) {
				if (getNpcTemplate().getCountId() > 0) {
					int sleepTime = 7200;// 2시간
					TimeMap.getInstance().add(new L1TimeMap(getNpcTemplate().getCountId(), sleepTime, doorId));
				}
				L1DoorInstance door = DoorSpawnTable.getInstance().getDoor(doorId);
				synchronized(this){
					if (door != null) {
						door.open();
					}
				}
			}
			
			short mapId = getMapId();
			if (_lastAttacker instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) _lastAttacker;
				if (Config.ALT.ALT_RABBITEVENT) {
					L1ItemInstance helmet = pc.getInventory().getEquippedHelmet();
					if (helmet != null && helmet.getItemId() == 22253) {// 변신 토끼 모자
						manwalJungiGet(pc);
					}
				}
				/** 시간의 균열 관련  제단 열쇠 */
				if (mapId == 781) {
					tebaeKeyGet(pc);
				} else if (mapId == 783) {
					tikalKeyGet(pc);
				}
			}

			if (npcId == 400016 || npcId == 400017) {
				tebaeBossDie();
			} else if (npcId == 800018 || npcId == 800019) {
				tikalBossDie();
			} else if ((npcId == 800154 || npcId == 800155) && mapId == 12862) {
				sasinSoulDie();// 지배의 탑 사신의 영혼
			} else if (npcId == 45753 && mapId == 15404) {
				balogDie();// 발록
			} else if (Ruun.isRuunMap(mapId)) {
				ruun.ruunNpcDeathCheck(L1MonsterInstance.this);
			}
		}
	}
	
	/**
	 * 만월의 정기 획득
	 * @param pc
	 */
	void manwalJungiGet(L1PcInstance pc){
		if ((getLevel() >> 1) + 1 < pc.getLevel() && random.nextInt(100)+1 <= Config.RATE.RATE_DROP_RABBIT) {
			L1ItemInstance item = pc.getInventory().storeItem(410093, 1);
			pc.sendPackets(new S_ServerMessage(143, getNpcTemplate().getDesc(), item.getDesc()), true);
		}
	}
	
	/**
	 * 테베보스방 열쇠 획득
	 * @param pc
	 */
	void tebaeKeyGet(L1PcInstance pc){
		if ((int)(Math.random() * 100) + 1 >= 85 && !pc.getInventory().checkItem(100036, 1)) {
			L1ItemInstance item = pc.getInventory().storeItem(100036, 1);
			pc.sendPackets(new S_ServerMessage(143, getNpcTemplate().getDesc(), item.getDesc()), true);
		}
	}
	
	/**
	 * 티칼보스방 열쇠 획득
	 * @param pc
	 */
	void tikalKeyGet(L1PcInstance pc){
		if ((int)(Math.random() * 100) + 1 >= 85 && !pc.getInventory().checkItem(500210, 1)) {
			L1ItemInstance item = pc.getInventory().storeItem(500210, 1);
			pc.sendPackets(new S_ServerMessage(143, getNpcTemplate().getDesc(), item.getDesc()), true);
		}
	}
	
	/**
	 * 테베 보스 처치 이벤트
	 */
	void tebaeBossDie(){
		CrockController crock	= CrockController.getInstance();
		int dieCountTebae		= crock.dieCount();
		switch(dieCountTebae){
		// 2명의 보스중 한명도 죽이지 않았을때 둘중 하나를 죽였다면 +1
		case 0:
			crock.dieCount(1);
			break;
		// 2명의 보스중 이미 한명이 죽였고. 이제 또한명이 죽으니 2
		case 1:
			crock.dieCount(2);
			crock.send();
			break;
		}
	}
	
	/**
	 * 티칼 보스 처치 이벤트
	 */
	void tikalBossDie(){
		CrockController crock	= CrockController.getInstance();
		int dieCountTikal		= crock.dieCount();
		switch(dieCountTikal){
		// 2명의 보스중 한명도 죽이지 않았을때 둘중 하나를 죽였다면 +1
		case 0:
			crock.dieCount(1);
			L1NpcInstance mob = null;
			if (getNpcTemplate().getNpcId() == 800018) {
				mob = L1World.getInstance().findNpc(800019);
				if (mob != null && !mob.isDead()) {
					mob.getSkill().setSkillEffect(800018, 60000);//1분
				}
			} else {
				mob = L1World.getInstance().findNpc(800018);
				if (mob != null && !mob.isDead()) {
					mob.getSkill().setSkillEffect(800019, 60000);//1분
				}
			}
			break;
		// 2명의 보스중 이미 한명이 죽였고. 이제 또한명이 죽으니 2
		case 1:
			crock.dieCount(2);
			crock.sendTikal();
			break;
		}
	}
	
	/**
	 * 지배의 탑 정상 사신의 영혼 처치 이벤트
	 */
	void sasinSoulDie(){
		if (getNpcTemplate().getNpcId() == 800154) {
			BossSpawn._sasinSoulLeft		= true;
			GeneralThreadPool.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					BossSpawn._sasinSoulLeft = false;
				}
			}, 3600 * 1000);
		} else if (getNpcTemplate().getNpcId() == 800155) {
			BossSpawn._sasinSoulRight		= true;
			GeneralThreadPool.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					BossSpawn._sasinSoulRight = false;
				}
			}, 3600 * 1000);
		}
		
		if (BossSpawn._sasinSoulLeft && BossSpawn._sasinSoulRight) {
			BossSpawn._sasinSoulLeft	= BossSpawn._sasinSoulRight	= false;
			GeneralThreadPool.getInstance().schedule(new reaperSpawn(), 5000L);
		}
	}
	
	/**
	 * 지배의 결계 발록 처치 이벤트
	 */
	void balogDie(){
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				for (L1DoorInstance door : L1World.getInstance().getAllDoor()) {
					int doorId = door.getDoorId();
					if ((doorId == 225 || doorId == 226 || doorId == 227) && door.getOpenStatus() == ActionCodes.ACTION_Close) {
						door.open();
					}
				}
			}
		}, 60000);
	}

	// TODO 몬스터 처치에 대한 보상
	void distributeExpDropKarma(L1Character lastAttacker) {
        if (lastAttacker == null) {
        	return;
        }
		L1PcInstance pc = null;
		if (lastAttacker instanceof L1PcInstance) {
			pc = (L1PcInstance) lastAttacker;
		} else if (lastAttacker instanceof L1PetInstance) {
			pc = (L1PcInstance) ((L1PetInstance) lastAttacker).getMaster();
		} else if (lastAttacker instanceof L1SummonInstance) {
			pc = (L1PcInstance) ((L1SummonInstance) lastAttacker).getMaster();
		}
		
		// 도펠겡어 성향치 초기 설정
		if (this instanceof L1DoppelgangerInstance) {
			setAlignment(getNpcTemplate().getAlignment());
		}
		
		if (pc != null) {
			if (pc instanceof L1AiUserInstance) {
				if (isDead()) {
					distributeDrop(pc);
				}
				return;
			}
			if (pc.noPlayerCK || pc.isDead()) {
				return;
			}
			
			ArrayList<L1Character> targetList	= _hateList.toTargetArrayList();
			ArrayList<Integer> hateList			= _hateList.toHateArrayList();
			
			boolean change = false;
			for (int i = hateList.size() - 1; i >= 0; i--) {
				L1Character cha = targetList.get(i);
				if (cha instanceof L1SummonInstance && ((L1SummonInstance) cha).getMaster() != null) {
					_hateList.add(((L1SummonInstance) cha).getMaster(), hateList.get(i));
					_hateList.remove(cha);
					change = true;
				}
			}
			if (change) {
				targetList	= _hateList.toTargetArrayList();
				hateList	= _hateList.toHateArrayList();
			}
			
			long exp = getExp();
			MapBalanceData mapBalance = getMap().getBalance();
			if (mapBalance != null) {
				exp *= mapBalance.getExpValue();
			}
			pc.getExpHandler().calcExp(this, targetList, hateList, exp);
			pc.addMonsterKill(1);// 몬스터 처치수 증가
			
			if (isDead()) {
				distributeDrop(pc);
				giveKarma(pc);
			}
		} else if (lastAttacker instanceof L1EffectInstance) {
			ArrayList<L1Character> targetList	= _hateList.toTargetArrayList();
			ArrayList<Integer> hateList			= _hateList.toHateArrayList();
			if (hateList.size() != 0) {
				int maxHate = 0;
				boolean change = false;
				for (int i = hateList.size() - 1; i >= 0; i--) {
					if (maxHate < ((Integer) hateList.get(i))) {
						maxHate			= (hateList.get(i));
						lastAttacker	= targetList.get(i);
					}
					if (targetList.get(i) instanceof L1SummonInstance && ((L1SummonInstance) targetList.get(i)).getMaster() != null) {
						_hateList.add(((L1SummonInstance) targetList.get(i)).getMaster(), hateList.get(i));
						_hateList.remove(targetList.get(i));
						change = true;
					}
				}
				
				if (change) {
					targetList	= _hateList.toTargetArrayList();
					hateList	= _hateList.toHateArrayList();
				}
				
				if (lastAttacker instanceof L1PcInstance) {
					pc = (L1PcInstance) lastAttacker;
				} else if (lastAttacker instanceof L1PetInstance) {
					pc = (L1PcInstance) ((L1PetInstance) lastAttacker).getMaster();
				} else if (lastAttacker instanceof L1SummonInstance) {
					pc = (L1PcInstance) ((L1SummonInstance) lastAttacker).getMaster();
				}
				
				if (pc != null) {
					long exp = getExp();
					MapBalanceData mapBalance = getMap().getBalance();
					if (mapBalance != null) {
						exp *= mapBalance.getExpValue();
					}
					pc.getExpHandler().calcExp(this, targetList, hateList, exp);
					if (isDead()) {
						distributeDrop(pc);
						giveKarma(pc);
					}
				}
			}
		}
	}

	void distributeDrop(L1PcInstance pc) {
		try {
			if (isResurrect()) {
				return;
			}
			ArrayList<L1Character> dropTargetList	= _dropHateList.toTargetArrayList();
			ArrayList<Integer> dropHateList			= _dropHateList.toHateArrayList();
			/*if (getNpcTemplate().getNpcId() == 45640 && getTempCharGfx() == 2755) {// 구 몽환의 섬 유니콘
				return;
			}*/
			DropTable.getInstance().drop(L1MonsterInstance.this, dropTargetList, dropHateList, pc);
		} catch (Exception e) {
		}
	}
	
	/**
	 * 초급 퀘스트 몬스터 처치
	 */
	void beginnerQuestKillProgress(){
		L1QuestKillNpc kill = getNpcTemplate().getQuestKillNpc();
		if (L1MonsterInstance.this == null || kill == null) {
			return;
		}
		int required		= kill.getRequiredQuantity();
		int add				= Config.QUEST.BEGINNER_QUEST_FAST_PROGRESS ? required : 1;
		int index			= kill.getIndex();
		int questId			= kill.getQuestId();
		L1PcInstance attacker;
		L1QuestProgress kill_progress;
		for (L1Character cha : getHateList().toTargetArrayList()) {// 공격에 참여한 캐릭터
			if (cha == null || cha instanceof L1PcInstance == false || cha instanceof L1AiUserInstance) {
				continue;
			}
			attacker	= (L1PcInstance) cha;
			if (attacker.getLevel() > Config.QUEST.BEGINNER_QUEST_LIMIT_LEVEL) {
				continue;
			}
			if (attacker.getQuest() == null) {
				continue;
			}
			kill_progress	= attacker.getQuest().getQuestProgress(questId);
			if (kill_progress == null || kill_progress.getStartTime() == 0 || kill_progress.getFinishTime() != 0) {
				continue;
			}
			int current = kill_progress.getQuantity(index);
			if (current >= required) {// 이미 목표 달성 상태
				continue;
			}
			int after = current + add;
			if (after > required) {
				after = required;
			}
			kill_progress.setQuantity(index, after);
			attacker.sendPackets(new S_QuestProgressUpdateNoti(kill_progress), true);
		}
	}
	
	/**
	 * 초급 퀘스트 몬스터 처치시 아이템 획득
	 */
	void beginnerQuestDropItem() {
		L1QuestDropItem drop = getNpcTemplate().getQuestDropNpc();
		if (L1MonsterInstance.this == null || drop == null) {
			return;
		}
		L1PcInstance attacker;
		for (L1Character cha : getHateList().toTargetArrayList()) {// 공격에 참여한 캐릭터
			if (cha == null || cha instanceof L1PcInstance == false || cha instanceof L1AiUserInstance) {
				continue;
			}
			attacker	= (L1PcInstance) cha;
			if (attacker.getLevel() > Config.QUEST.BEGINNER_QUEST_LIMIT_LEVEL) {
				continue;
			}
			if (attacker.getQuest() == null) {
				continue;
			}
			attacker.getQuest().questDropItem(L1MonsterInstance.this, drop);
		}
	}

	/**
	 * 우호도 분배
	 * @param pc
	 */
	void giveKarma(L1PcInstance pc) {
		int karma = getKarma();
		if (karma == 0) {
			return;
		}
		int karmaSign = Integer.signum(karma);
		int pcKarmaLevel = pc.getKarmaLevel();
		int pcKarmaLevelSign = Integer.signum(pcKarmaLevel);
		if (pcKarmaLevelSign != 0 && karmaSign != pcKarmaLevelSign) {
			karma *= 5;
		}
		pc.addKarma((int) (karma * Config.RATE.RATE_KARMA));
		pc.sendPackets(new S_Karma(pc), true);
	}

	/**
	 * 무한대전 스테이지 클리어 보상
	 */
	void giveUbSeal() {
		if (getUbSealCount() == 0) {
			return;
		}
		L1UltimateBattle ub = UBTable.getInstance().getUb(getUbId());
		if (ub == null) {
			return;
		}
		if (isUpGateKeeper()) {
			ub._gateKeeperCount++;
		}
		if (!isUpBoss()) {
			return;
		}
		for (L1PcInstance pc : ub.getMembersArray()) {
            int exp = 50000;
			int settingEXP = (int) Config.RATE.RATE_XP;
			double exppenalty = ExpTable.getPenalty(pc.getLevel());
			double PobyExp = getUbSealCount() * exp * settingEXP * exppenalty;
			/** 폭렙 방지 **/
		    if (pc.getLevel() >= 1 && (((long)PobyExp) + pc.getExp() > ExpTable.getExpByLevel(pc.getLevel() + 1))) {
		    	PobyExp = (ExpTable.getExpByLevel(pc.getLevel() + 1) - pc.getExp());
			}
			pc.addExp((int) PobyExp);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(getUbSealCount() + "군 보스 클리어 보상: 경험치 획득"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(getUbSealCount()  + S_SystemMessage.getRefText(1047), true), true);
			pc.send_effect(3944);
			if (getUbSealCount() >= 3 && getUbSealCount() <= 5) {
				ultimateBoss();
			} else if (getUbSealCount() == 6) {
				pc.einGetExcute(200);// 히든 보스
			}
		}
	}

	public boolean isStoreDroped() {
		return _storeDroped;
	}
	public void setStoreDroped(boolean flag) {
		_storeDroped = flag;
	}

	private int _ubSealCount; 
	public int getUbSealCount() {
		return _ubSealCount;
	}
	public void setUbSealCount(int i) {
		_ubSealCount = i;
	}

	private int _ubId; // UBID
	public int getUbId() {
		return _ubId;
	}
	public void setUbId(int i) {
		_ubId = i;
	}
	
	private boolean _upGateKeeper;
	public boolean isUpGateKeeper(){
		return _upGateKeeper;
	}
	public void setUpGateKeeper(boolean bb){
		_upGateKeeper = bb;
	}
	
	private boolean _upBoss;
	public boolean isUpBoss(){
		return _upBoss;
	}
	public void setUpBoss(boolean bb){
		_upBoss = bb;
	}
	
	void hiddenAction(int hedden, int show_action, int real_action) {
		allTargetClear();
		setHiddenStatus(hedden);
		broadcastPacket(new S_DoActionGFX(getId(), show_action), true);
		setActionStatus(real_action);
		broadcastPacket(new S_NPCObject(this), true);
	}

	void hide() {
		int npcid = getNpcTemplate().getNpcId();
		if (npcid == 45061 || npcid == 45161 || npcid == 45181 || npcid == 45455 /*|| npcid == 7310030*/) {// 스파토이
			if (getMaxHp() >> 2 > getCurrentHp() && 1 > random.nextInt(10)) {
				hiddenAction(HIDDEN_STATUS_SINK, ActionCodes.ACTION_Hide, 13);
			}
		} else if (npcid == 45682) {// 안타라스(구형)
			if (getMaxHp() >> 2 > getCurrentHp() && 1 > random.nextInt(50)) {
				hiddenAction(HIDDEN_STATUS_SINK, ActionCodes.ACTION_AntharasHide, 20);
			}
		} else if (npcid == 45067 || npcid == 45264 || npcid == 45452 || npcid == 45090 || npcid == 45321 || npcid == 45445 || npcid == 75000 || npcid == 72014 || npcid == 72015) {// 그리폰, 하피
			if (getMaxHp() >> 2 > getCurrentHp() && 1 > random.nextInt(10)) {
				hiddenAction(HIDDEN_STATUS_FLY, ActionCodes.ACTION_Moveup, 4);
			}
		} else if (npcid == 45681) {// 린드비오르(구형)
			if (getMaxHp() >> 2 > getCurrentHp() && 1 > random.nextInt(50)) {
				hiddenAction(HIDDEN_STATUS_FLY, ActionCodes.ACTION_Moveup, 11);
			}
		} else if (npcid == 5135) {// 샌드웜
			if (!_sandwarmHiding && (getMaxHp() >> 1) > getCurrentHp()) {
				_sandwarmHiding = true;
				hiddenAction(HIDDEN_STATUS_SINK, ActionCodes.ACTION_Hide, 13);
				GeneralThreadPool.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						if (getHiddenStatus() == L1NpcInstance.HIDDEN_STATUS_NONE) {
							return;
						}
						randomWalk();
						GeneralThreadPool.getInstance().schedule(this, getSleepTime());
					}
				}, 500);
				int rnd = random.nextInt(3) + 2;
				for (int i = 0; i < rnd; i++) {
					L1SpawnUtil.spawn(this, 75013, 6, 600 * 1000);// 스콜피온 스폰
				}
			}
		}
	}
	
	void hiddenInit(int hedden, int real_action) {
		setHiddenStatus(hedden);
		setActionStatus(real_action);
	}

	public void initHide() {
		int npcid = getNpcTemplate().getNpcId();
		if (npcid == 45061 || npcid == 45161 || npcid == 45181 || npcid == 45455 || npcid == 400000 || npcid == 400001 /*|| npcid == 7310030*/) {// 스파토이
			if (1 > random.nextInt(3)) {
				hiddenInit(L1NpcInstance.HIDDEN_STATUS_SINK, 13);
			}
		} /*else if (npcid == 45045 || npcid == 45126 || npcid == 45134 || npcid == 75003 || npcid == 5026 || npcid == 76003) {// 골렘
			if (1 > random.nextInt(3)) {
				hiddenInit(L1NpcInstance.HIDDEN_STATUS_SINK, 4);
			}
		} */else if (npcid == 45067 || npcid == 45264 || npcid == 45452 || npcid == 45090 || npcid == 45321 || npcid == 45445 || npcid == 75000 || npcid == 72014 || npcid == 72015) {// 그리폰, 하피
			hiddenInit(L1NpcInstance.HIDDEN_STATUS_FLY, 4);
		} else if (npcid == 45681) {// 린드비오르(구형)
			hiddenInit(L1NpcInstance.HIDDEN_STATUS_FLY, 11);
		}
	}

	public void initHideForMinion(L1NpcInstance leader) {
		int npcid = getNpcTemplate().getNpcId();
		if (leader.getHiddenStatus() == L1NpcInstance.HIDDEN_STATUS_SINK) {
			if (npcid == 45061 || npcid == 45161 || npcid == 45181 || npcid == 45455 || npcid == 400000 || npcid == 400001 /*|| npcid == 7310030*/) {// 스파토이
				hiddenInit(L1NpcInstance.HIDDEN_STATUS_SINK, 13);
			} /*else if (npcid == 45045 || npcid == 45126 || npcid == 45134 || npcid == 75003 || npcid == 5026 || npcid == 76003) {// 골렘
				hiddenInit(L1NpcInstance.HIDDEN_STATUS_SINK, 4);
			}*/
		} else if (leader.getHiddenStatus() == L1NpcInstance.HIDDEN_STATUS_FLY) {
			if (npcid == 45067 || npcid == 45264 || npcid == 45452 || npcid == 45090 || npcid == 45321 || npcid == 45445 || npcid == 75000 || npcid == 72014 || npcid == 72015) {// 그리폰, 하피
				hiddenInit(L1NpcInstance.HIDDEN_STATUS_FLY, 4);
			} else if (npcid == 45681) {// 린드비오르(구형)
				hiddenInit(L1NpcInstance.HIDDEN_STATUS_FLY, 11);
			}
		}
	}

	@Override
	protected void transform(int transformId) {
		super.transform(transformId);
	}
	
	void transSetting(int transformId){
		getInventory().clearItems();
		DropTable.getInstance().setDrop(this, getInventory());// 드랍 아이템 세팅
		getInventory().shuffle();
		_info = NpcInfoTable.getNpcInfo(transformId);
		if (_info != null && _info._spawnMsg != null) {
			spawnMsg();
		}
	}
	
	/**
	 * 몬스터 등장시 메세지 출력
	 */
	void spawnMsg(){
		ArrayList<L1PcInstance> pcList = _info._msgRange.equals(NpcInfoTable.TYPE_SCREEN) ? L1World.getInstance().getVisiblePlayer(this) : _info._msgRange.equals(NpcInfoTable.TYPE_MAP) ? L1World.getInstance().getMapPlayer(this.getMapId()) : null;
		if (pcList != null && !pcList.isEmpty()) {
			S_PacketBox greenMsg = new S_PacketBox(S_PacketBox.GREEN_MESSAGE, _info._spawnMsg.length == 1 ? _info._spawnMsg[0] : _info._spawnMsg[random.nextInt(_info._spawnMsg.length)]);
			for (L1PcInstance pc : pcList) {
				pc.sendPackets(greenMsg);
			}
			greenMsg.clear();
			greenMsg = null;
		}
	}

	PapPearlMonitor _PapPearlMonster;// 행동
	class PapPearlMonitor implements Runnable {
		private final L1MonsterInstance _Pearl;
		public PapPearlMonitor(L1MonsterInstance npc) { 
			_Pearl = npc; 
		}
		public void begin() {
			GeneralThreadPool.getInstance().schedule(this, 3000);
		}
		@Override
		public void run() {
			try {
				if ((_Pearl.getNpcTemplate().getNpcId() != 50087 && _Pearl.getNpcTemplate().getSpriteId() == 7684) || _Pearl.getNpcTemplate().getSpriteId() == 7805) {
					PapPearl(_Pearl);
				} else if (_Pearl.getNpcTemplate().getSpriteId() == 8063) {
					Sahel(_Pearl);
				}
			} catch (Exception exception) { }
		}
	}
	
	/**
	 * 몬스터 처치 이벤트
	 * @param lastAttacker
	 * @param npcId
	 */
	void npcDieAction(L1Character lastAttacker, int npcId){
		if (_info == null) {
			return;
		}
		if (_info._dieMsg != null) {// 멘트 여부
			if (_info._npcId == 81163) {// 기르타스 음성 패킷
				//broadcastPacket(new S_NpcChatPacket(this, _info._dieMsg[0], L1ChatType.SOUND), true); 
			} else {
				S_PacketBox greenMsg = new S_PacketBox(S_PacketBox.GREEN_MESSAGE, _info._dieMsg.length == 1 ? _info._dieMsg[0] : _info._dieMsg[random.nextInt(_info._dieMsg.length)]);
				if (_info._msgRange.equals(NpcInfoTable.TYPE_SELF) && lastAttacker instanceof L1PcInstance) {
					((L1PcInstance) lastAttacker).sendPackets(greenMsg);
				} else {
					for (L1PcInstance pc : _info._msgRange.equals(NpcInfoTable.TYPE_SCREEN) ? L1World.getInstance().getVisiblePlayer(this) : L1World.getInstance().getMapPlayer(this.getMapId())) {
						pc.sendPackets(greenMsg);
					}
				}
				greenMsg.clear();
				greenMsg = null;
			}
		}
		if (_info._reward) {// 보상 여부
			if (_info._rewardRange.equals(NpcInfoTable.TYPE_SELF) && lastAttacker instanceof L1PcInstance) {
				npcReward(((L1PcInstance) lastAttacker));
			} else {
				for (L1PcInstance pc : _info._rewardRange.equals(NpcInfoTable.TYPE_SCREEN) ? L1World.getInstance().getVisiblePlayer(this) : L1World.getInstance().getMapPlayer(this.getMapId())) {
					npcReward(pc);
				}
			}
		}
		// 추가 액션
		switch(_info._npcId){
		case 800157:reaperDie(lastAttacker);break;
		case 5136:BossSpawn._erzabeRun = false;break;
		case 5135:BossSpawn._sandwarmRun = false;break;
		case 46025:tarosDie();break;
		case 800250:case 800251:case 800252:case 800253:case 800254:case 800255:case 800256:case 800257:case 800258:case 800259:case 800260:
			dominationWakeEnd();
			if (_info._npcId == 800260) {
				reaperDie(lastAttacker);
			}
			break;
		}
	}
	
	/**
	 * 몬스터 처치 보상
	 * @param pc
	 */
	void npcReward(L1PcInstance pc){
		if (pc.getConfig().getBossId() != _info._npcId) {
			return;
		}
		if (_info._rewardItemId > 0) {
			L1ItemInstance item = pc.getInventory().storeItem(_info._rewardItemId, _info._rewardItemCount);
			if (item != null) {
				pc.sendPackets(new S_ServerMessage(403, String.format("%s (%d)", item.getDesc(), _info._rewardItemCount)), true);
			}
		}
		if (_info._rewardGfx > 0) {
			pc.send_effect(_info._rewardGfx);// 이팩트
		}
		if (_info._rewardEinhasad > 0) {
			pc.einGetExcute(_info._rewardEinhasad);// 아인하사드
		}
		if (_info._rewardNcoin > 0) {
			pc.addNcoin(_info._rewardNcoin);// ncoin
			pc.sendPackets(new S_ServerMessage(403, String.format("NCOIN (%d)", _info._rewardNcoin)), true);
		}
		pc.getConfig().setBossId(0);
	}
	
	/**
	 * 간수장 타로스 처치 이벤트
	 */
	void tarosDie(){
		if (getMapId() != 54) {
			return;
		}
		BossSpawn._taros = false;
		for (L1PcInstance pc : L1World.getInstance().getMapPlayer(getMapId())) {
			pc.sendPackets(S_SceneNoti.MAP54_DISABLE_END);// 리셋
			pc.sendPackets(S_SceneNoti.KIKAM_FULL_END);// 불켜기
		}
	}
	
	/**
	 * 여왕개미 처치 이벤트
	 * @param lastAttacker
	 */
	void antQueenDie(L1Character lastAttacker){
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
			if (pc == null) {
				continue;
			}
			pc.send_effect(7783);
		}
		GeneralThreadPool.getInstance().execute(new AntQueenTeleport(getMapId()));
	}
	
	class AntQueenTeleport implements Runnable {
		short _map;
		AntQueenTeleport(short map) {
			_map = map;
		}

		@Override
		public void run() {
			try {
				L1World world = L1World.getInstance();
				for (L1PcInstance pc : world.getMapPlayer(_map)) {
					pc.sendPackets(L1GreenMessage.ANTQUEEN_DIE_MENT_1);
					pc.sendPackets(L1GreenMessage.ANTQUEEN_DIE_MENT_2);
				}
				Thread.sleep(60000L);// 1분 딜레이
				for (L1PcInstance pc : world.getMapPlayer(_map)) {
					if (pc == null || pc.isDead()) {
						continue;
					}
					int[] loc = Getback.GetBack_Location(pc);
					pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true);
				}
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
	}
	
	/**
	 * 지배의 탑 사신 그림 리퍼 처치 이벤트
	 * @param lastAttacker
	 */
	void reaperDie(L1Character lastAttacker){
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				try {
					L1World world = L1World.getInstance();
					for (L1PcInstance pc : world.getMapPlayer(12862)) {
						pc.sendPackets(L1GreenMessage.REAPER_DIE_MENT_1);// 그림리퍼의 지배력은 완전히 소멸되지 않앗으며 조만간 회복될 것입니다
					}
					Thread.sleep(8000L);
					for (L1PcInstance pc : world.getMapPlayer(12862)) {
						pc.sendPackets(L1GreenMessage.REAPER_DIE_MENT_2);// 리퍼의 제단에 있는 영웅들은 10분후에 시작지점으로 이동됩니다.
					}
					// 고대 신의 사원
				    /*if (lastAttacker instanceof L1PcInstance) {
				    	crackZone(((L1PcInstance) lastAttacker).getClanid());// 마지막 어택 혈맹만 입장가능하도록
				    }*/
					Thread.sleep(60000 * 5);
				    for (L1PcInstance pc : world.getMapPlayer(12862)) {
						pc.sendPackets(L1GreenMessage.REAPER_DIE_MENT_3);// 리퍼의 제단에 있는 영웅들은 5분후에 시작지점으로 이동됩니다.
				    }
				    Thread.sleep(60000 * 4);
				    for (L1PcInstance pc : world.getMapPlayer(12862)) {
						pc.sendPackets(L1GreenMessage.REAPER_DIE_MENT_4);// 리퍼의 제단에 있는 영웅들은 1분후에 시작지점으로 이동됩니다.
				    }
				    Thread.sleep(30000);
				    for (L1PcInstance pc : world.getMapPlayer(12862)) {
						pc.sendPackets(L1GreenMessage.REAPER_DIE_MENT_5);// 리퍼의 제단에 있는 영웅들은 30초 후에 시작지점으로 이동됩니다.
				    }
				    Thread.sleep(30000);
				    for (L1DoorInstance door : world.getAllDoor()) {
				    	if (door.getDoorId() == 231 && door.getOpenStatus() == ActionCodes.ACTION_Open) {
						    door.close();
						    break;
						}
				    }
				    for (L1PcInstance pc : world.getMapPlayer(12862)) {
						if ((pc.getX() >= 32728 && pc.getX() <= 32749) && (pc.getY() >= 32875 && pc.getY() <= 32898)) {
							pc.getTeleport().start(32638, 32796, (short) 12862, pc.getMoveState().getHeading(), true);
							pc.sendPackets(L1SystemMessage.REAPER_DIE_TELEPORT_MSG);
							pc.sendPackets(L1GreenMessage.REAPER_DIE_MENT_6);
						}
					}
				} catch (Exception e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
			}
		}, 10000);
	}
	
	/*static final int[][] CRACK_SPAWN_XY = { {32743, 32962}, {32780, 32894}, {32633, 32892}, {32713, 32912} };
	void crackZone(int serverId){// 균열의 틈새
		for (L1PcInstance pc : L1World.getInstance().getMapPlayer(12862)) {
			pc.sendPackets(L1GreenMessage.OMAN_CRACK_OPEN_MSG);// 이 곳 어딘가에 알 수 없는 균열이 발생하였습니다.
		}
		int rnd = random.nextInt(CRACK_SPAWN_XY.length);
		L1SpawnUtil.spawn2(CRACK_SPAWN_XY[rnd][0],CRACK_SPAWN_XY[rnd][1], (short) 12862, 5, 800200, 0, 3600 * 1000, serverId);
	}*/
	
	/**
	 * 무한대전 보스 처치 이벤트
	 */
	void ultimateBoss(){
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (getMapId() == pc.getMapId() && pc.isUltimateBoss() && !pc.isDead()) {
				pc.getInventory().createItem(this.getName(), L1ItemId.ULTIMATE_BRAVE_COIN, 1, 0);// 무한 용사의 증표
				pc.send_effect(7783);
			}
			pc.setUltimateBoss(false);
		}
	}
	
	/**
	 * 드래곤의 서식지 보스 처치 이벤트
	 */
	void dragonDungeonBoss(){
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
			if (getMapId() == pc.getMapId() && pc.isDragonDungenBoss() && !pc.isDead()) {
				pc.getInventory().createItem(this.getName(), 6021, 1, 0);// 드래곤의 전리품
				if (!pc.getInventory().checkItem(6020)) {
					pc.getInventory().createItem(this.getName(), 6020, 1, 0);// 용 사냥꾼의 징표
				}
			}
			pc.setDragonDungenBoss(false);
		}
	}
	
	/**
	 * 콤보 시스템
	 * @param lastAttacker
	 */
	void calcCombo(L1Character lastAttacker) {
	    if (lastAttacker instanceof L1PcInstance) {
	        L1PcInstance pc = (L1PcInstance)lastAttacker;
	        if (!pc.getSkill().hasSkillEffect(L1SkillId.COMBO_BUFF)) {
	            if (pc.getAccount() != null && (pc.getAccount().getEinhasad().getPoint() / Config.EIN.REST_EXP_DEFAULT_RATION > 100) && (random.nextInt(100) <= 1)) {// 발동 확률
	                pc.setComboCount(1);
	                pc.getSkill().setSkillEffect(L1SkillId.COMBO_BUFF, 50000);
	                pc.sendPackets(new S_PacketBox(S_PacketBox.COMBO_BUFF, pc.getComboCount()), true);
	            }
	        } else if (pc.getComboCount() < 30) {
	            pc.setComboCount(pc.getComboCount() + 1);
	            pc.sendPackets(new S_PacketBox(S_PacketBox.COMBO_BUFF, pc.getComboCount()), true);
	        } else {
	            pc.sendPackets(new S_PacketBox(S_PacketBox.COMBO_BUFF, 31), true);
	        }
	    }
	}
	
	// 오림 인던 관련
	private boolean _isCurseMimic ;
	public void setCurseMimic(boolean curseMimic) {
		_isCurseMimic = curseMimic;
	}
	public boolean isCurseMimic(){
		return _isCurseMimic;
	}
	
	public void setTarget(L1PcInstance targetPlayer) {
		if (targetPlayer != null) {
			_hateList.add(targetPlayer, 0);
			_target = targetPlayer;
		}
	}
	
	class kirtasFireDamage implements Runnable {
		@Override
		public void run() {
			if (_destroyed || isDead()) {
				return;
			}
			try {
				for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(L1MonsterInstance.this, 2)) {
					if (pc.isDead() || pc.isGhost()) {
						continue;
					}
					L1MonsterInstance.this.broadcastPacket(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Damage), true);
					try {
						pc.receiveDamage(L1MonsterInstance.this, random.nextInt(50) + 50);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				GeneralThreadPool.getInstance().schedule(this, 1000);
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
	}
	
	/**
	 * 지배의 탑 사신 그림리퍼 스폰
	 */
	class reaperSpawn implements Runnable {
		@Override
		public void run() {
			try {
				L1World world = L1World.getInstance();
				for (L1DoorInstance door : world.getAllDoor()) {
					if (door.getDoorId() == 231 && door.getOpenStatus() == ActionCodes.ACTION_Close) {
						door.open();
						break;
					}
				}
				Thread.sleep(5000L);
				for (L1PcInstance pc : world.getMapPlayer(12862)) {
					pc.sendPackets(L1SystemMessage.REAPER_SPAWN_MSG_1);// 때가 왔다
				}
				Thread.sleep(5000L);
				for (L1PcInstance pc : world.getMapPlayer(12862)) {
					pc.sendPackets(L1SystemMessage.REAPER_SPAWN_MSG_2);// 죽음의 문이 열리고
				}
				Thread.sleep(5000L);
				for (L1PcInstance pc : world.getMapPlayer(12862)) {
					pc.sendPackets(L1SystemMessage.REAPER_SPAWN_MSG_3);// 나뉘었던 고대의 힘이 완성되었다
				}
				Thread.sleep(5000L);
				for (L1PcInstance pc : world.getMapPlayer(12862)) {
					pc.sendPackets(L1SystemMessage.REAPER_SPAWN_MSG_4);// 이제 제물을 받을 준비가 되었구나
				}
				Thread.sleep(10000L);
				L1EffectSpawn.getInstance().spawnEffect(800172, 5, 32739, 32886, (short) 12862);// 돌덩이 떨어짐
				for (L1PcInstance pc : world.getMapPlayer(12862)) {
					pc.sendPackets(L1GreenMessage.REAPER_SPAWN_MENT);
				}
				Thread.sleep(5000L);
				L1SpawnUtil.spawn2(32739, 32886, (short) 12862, 5, 800164, 0, 10000, 0);// 스폰이미지
				Thread.sleep(10000L);
				L1NpcInstance reaper = L1SpawnUtil.spawn2(32739, 32886, (short) 12862, 5, 800157, 0, 7200000, 0);// 사신 그림 리퍼
				
				BossTemp boss = BossSpawnTable.getBossInfo(reaper.getNpcId());
				if (boss != null) {
					BossSpawn.putAliveBoss(reaper.getNpcId(), reaper);
					if (boss.notification != null) {
						reaper.set_notification_info(boss.notification);
						reaper.do_notification(true);
					}
				}
				
				//Manager.getInstance().BossAppend(L1MonsterInstance.this.getName()); // MANAGER DISABLED
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
	}
	
	/**
	 * 월드 공성전 보스 처치 이벤트
	 * @param pc
	 */
	void wolrdWarBoss(L1PcInstance pc) {
		if (pc.getClanid() == 0) {
			return;
		}
		String playerClanName = pc.getClanName();
		L1Clan clan = pc.getClan();
		if (clan == null || clan.getCastleId() != 0) {// 성주 혈맹
			return;
		}
		int castle_id = 0;
		// 보스로부터 castle_id를 취득
		if (getNpcId() == 800551) {
			castle_id = L1CastleLocation.OT_CASTLE_ID;
		} else if (getNpcId() == 800553) {
			castle_id = L1CastleLocation.GIRAN_CASTLE_ID;
		} else if (getNpcId() == 800554) {
			castle_id = L1CastleLocation.HEINE_CASTLE_ID;
		}
		
		clan.setCastleId(castle_id);// 성주 세팅
		ClanTable.getInstance().updateClan(clan);
		
		L1World world = L1World.getInstance();
		// 왕관 출력
		if (pc.isCrown()) {
			S_CastleMaster castleMaster = new S_CastleMaster(castle_id, pc.getId());
			world.broadcastPacketToAll(castleMaster);
			pc.sendPackets(castleMaster, true);
		} else {
			S_CastleMaster castleMaster = new S_CastleMaster(castle_id, pc.getClan().getLeaderId());
			world.broadcastPacketToAll(castleMaster);
			pc.sendPackets(castleMaster, true);
		}
		
		// 크란원 이외를 강제 텔레포트
		worldWarTel(pc, castle_id);
		
		StringBuilder sb = new StringBuilder();
		//sb.append(playerClanName).append("혈맹이 ").append(L1CastleType.fromInt(castle_id).getName()).append("을(를) 점거했습니다.");
		sb.append(playerClanName).append(S_SystemMessage.getRefText(99)).append(L1CastleType.fromInt(castle_id).getName()).append(S_SystemMessage.getRefText(91));
		S_SystemMessage msg = new S_SystemMessage(sb.toString(), true);
		for (L1PcInstance _pc : world.getAllPlayers()) {
			if (_pc.getMapId() >= 15482 && _pc.getMapId() <= 15484 || _pc.getMapId() >= 15492 && _pc.getMapId() <= 15494) {
				_pc.sendPackets(msg);
			}
		}
		msg.close();
		msg = null;
		
		if (clan.getOnlineClanMember().length > 0) {
			for (L1PcInstance pc2 : clan.getOnlineClanMember()) {
				pc2.setCurrentHp(pc2.getMaxHp());// 왕관클릭시 군주만피채운다
				pc2.sendPackets(L1ServerMessage.sm643);// 성을 점거했습니다.
			}
		}
		
		L1CastleGuardInstance gard = null;
		for (L1NpcInstance object : world.getAllNpc()) {
			// 월드 공성전 몬스터가 있는 경우 지운다
			if ((castle_id == L1CastleLocation.OT_CASTLE_ID && object.getNpcId() >= 800604 && object.getNpcId() <= 800607)
					|| (castle_id == L1CastleLocation.GIRAN_CASTLE_ID && object.getNpcId() >= 800612 && object.getNpcId() <= 800615)
					|| (castle_id == L1CastleLocation.HEINE_CASTLE_ID && object.getNpcId() >= 800616 && object.getNpcId() <= 800619)) {
				object.deleteMe();
			}
			
			// 성 근위병
			if (object instanceof L1CastleGuardInstance) {
				gard = (L1CastleGuardInstance) object;
				if (gard != null && L1CastleLocation.checkInWarArea(castle_id, gard)) {
					gard.allTargetClear();
					if (gard.getX() != gard.getHomeX() || gard.getY() != gard.getHomeY()) {
						gard.teleport(gard.getHomeX(), gard.getHomeY(), gard.getSpawn().getHeading());
					} else if (gard.getMoveState().getHeading() != gard.getSpawn().getHeading()) {
						gard.getMoveState().setHeading(gard.getSpawn().getHeading());
						gard.broadcastPacket(new S_ChangeHeading(gard), true);
					}
				}
			}
		}
		
		for (L1TowerInstance tower : world.getAllTower()) {
			if (L1CastleLocation.checkInWarArea(castle_id, tower)) {
				tower.deleteMe();
			}
		}
		
		// 타워를 spawn 한다
		L1WarSpawn warspawn = new L1WarSpawn();
		warspawn.SpawnTower(castle_id);
		
		/** 성문 수리 **/
		for (L1DoorInstance door : DoorSpawnTable.getInstance().getDoorList()) {
			if (L1CastleLocation.checkInWarArea(castle_id, door)) {
				door.repairGate();
			}
		}
		
		// 전쟁중 삭제해야 다시 선포할수잇음
		L1War[] warArray = world.get_wars();
		for (int i = 0; i < warArray.length; i++) {
			if (castle_id == warArray[i].GetCastleId()) {
				world.removeWar(warArray[i]);
				continue;
			}
			if (warArray[i].CheckClanInWar(playerClanName)) {
				warArray[i].CeaseWar(playerClanName, warArray[i].GetDefenceClanName());
			}
		}
		warArray = null;
		War war = War.getInstance();
		war.AttackClanSetting(castle_id, playerClanName);

		L1PcInstance defence_clan_member[] = clan.getOnlineClanMember();
		for (L1PcInstance pp : defence_clan_member) {
			int castleid = L1CastleLocation.getCastleIdByArea(pp);
			if (castleid == castle_id) {
				war.WarTime_SendPacket(castleid, pp);
			}
		}	
	}
	
	void worldWarTel(L1PcInstance player, int clanid){
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc.getClanid() != player.getClanid() && !pc.isGm()) {
				if (L1CastleLocation.checkInWarArea(clanid, pc)) {
					int[] loc = L1CastleLocation.getGetBackLoc(clanid);
					pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true);
				}
			} else {
				if (pc.warZone) {
					pc.warZone = false;
					pc.sendPackets(S_SiegeInjuryTimeNoti.CASTLE_WAR_TIME_NONE);
				}
			}
		}
	}
}

