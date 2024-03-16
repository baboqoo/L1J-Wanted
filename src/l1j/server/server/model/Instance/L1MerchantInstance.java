package l1j.server.server.model.Instance;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import l1j.server.Config;
import l1j.server.IndunSystem.minigame.BattleZone;
import l1j.server.IndunSystem.occupy.OccupyHandler;
import l1j.server.IndunSystem.occupy.OccupyManager;
import l1j.server.IndunSystem.occupy.OccupyType;
import l1j.server.common.data.Gender;
import l1j.server.server.GameServerSetting;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.construct.skill.L1SkillInfo;
import l1j.server.server.controller.CrockController;
import l1j.server.server.controller.DogFightController;
import l1j.server.server.controller.DollRaceController;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.datatables.NpcActionTeleportTable;
import l1j.server.server.datatables.TownTable;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1NpcTalkData;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1World;
import l1j.server.server.model.gametime.GameTimeClock;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.action.bean.L1NpcActionTeleport;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.action.S_ChangeHeading;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.object.S_NPCObject;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.StringUtil;

public class L1MerchantInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;
	
	protected static final List<Integer> AI_MERCHANT_ID = Arrays.asList(new Integer[]{
			5095, 7210040, 5177, 5178, 800702, 800703, 800200
	});
	
	private int clanid;
	private String clanname;
	public int getClanid() {
		return clanid;
	}
	public void setClanid(int val) {
		clanid = val;
	}
	public String getClanname() {
		return clanname;
	}
	public void setClanname(String val) {
		clanname = val;
	}
	
	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		if (perceivedFrom == null || this == null) {
			return;
		}
		perceivedFrom.addKnownObject(this);
		perceivedFrom.sendPackets(new S_NPCObject(this), true);
		if (AI_MERCHANT_ID.contains(getNpcId()) || getPassispeed() > 0) {
			onNpcAI();
		}
	}
	
	public L1MerchantInstance(L1Npc template) {
		super(template);
		_restCallCount = new AtomicInteger(0);
	}

	@Override
	public void onAction(L1PcInstance pc) {
		L1Attack attack = new L1Attack(pc, this);
		if (attack.calcHit()) {
			attack.calcDamage();
		}
		attack.action();
	}

	@Override
	public void onNpcAI() {
		if (isAiRunning()) {
			return;
		}
		_actived = false;
		startAI();
	}

	@Override
	public void onTalkAction(L1PcInstance player) {
		long curtime = System.currentTimeMillis() / 1000;
		if (player.getNpcActionTime() + 2 > curtime) {
			return;
		}
		player.setNpcActionTime(curtime);

		if (getNpcTemplate().isChangeHead()) {
			talkChangeHead(player);
		}
		int objid		= getId();
		L1Object obj	= L1World.getInstance().findObject(objid);
		String desc		= ((L1NpcInstance) obj).getNpcTemplate().getDesc();

		int npcid			= getNpcTemplate().getNpcId();
		String htmlid		= null;
		String[] htmldata	= null;
		
		if (npcid == 7000096) {
			if (player.getLevel() >= Config.DUNGEON.BATTLE_ZONE_LIMIT_LEVEL) {
				DuelZone(player);
			} else {
//AUTO SRM: 				player.sendPackets(new S_SystemMessage("\\aG[!] : 레벨 " + Config.DUNGEON.BATTLE_ZONE_LIMIT_LEVEL + " 이상만 입장할수 있습니다."), true); // CHECKED OK
				player.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1034) + Config.DUNGEON.BATTLE_ZONE_LIMIT_LEVEL  + S_SystemMessage.getRefText(1035), true), true);
			}
		}

		// 성혈버프사 
		if (npcid == 810852) {
			if (player.getInventory().checkItem(L1ItemId.ADENA, 200000)) {
				L1Clan clan = player.getClan();
				if (clan != null && clan.getCastleId() != 0) {
					player.getInventory().consumeItem(L1ItemId.ADENA, 200000);   
					player.setCurrentHp(player.getMaxHp());
					player.setCurrentMp(player.getMaxMp());
					player.sendPackets(L1ServerMessage.sm77);
					player.send_effect_self(11722);
					L1BuffUtil.skillArrayAction(player, L1SkillInfo.CASTLE_BUFF_ARRAY);
					htmlid = "ep6ev_p3";
				} else {
//AUTO SRM: 					player.sendPackets(new S_SystemMessage("성을 소유하고있는 혈원만 사용 가능합니다."), true); // CHECKED OK
					player.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1036), true), true);
				}
			} else {
				player.sendPackets(L1ServerMessage.sm189);
			}
		}
		
		L1NpcActionTeleport teleport	= NpcActionTeleportTable.getTeleport(npcid, StringUtil.EmptyString);
		if (teleport != null) {
			teleport.action(player);
			return;
		}
		
		L1NpcTalkData talking			= NPCTalkDataTable.getInstance().getTemplate(npcid);
		if (talking == null) {
			return;
		}
		L1Quest quest			= player.getQuest();
		switch (npcid) {
		case 11093:// 텔레포터^유리아
			if (!player.getInventory().checkItem(31235)) {
				htmlid = "tel_soloagit2";
			}
			break;
		case 11095:// 훈련소 관리인^에르민
			if (!player.getInventory().checkItem(31235)) {
				htmlid = "boss_recall2";
			}
			break;
		case 18309:// 달의 기사 질리언 만월의 보물섬
			if (player.getLevel() < 60 || !GameServerSetting.TREASURE_ISLAND) {
				htmlid = "priest_inter_tel2";
			}
			break;
		case 7800100:// 게렝 악어섬의 비밀
			if (!player.getInventory().checkItem(420113)) {
				htmlid = "indun_losus_gerengf";
			}
			break;
		case 70760://엘뤼온
		case 9291://샤베스
		case 9292://엘라리온
			if (!player.isElf()) {
				htmlid = "ellyonne15";
			} else {
			    if (player.getElfAttr() != 0) {
			    	htmlid = "ellyonne11";
			    } else if (player.isPassiveStatus(L1PassiveId.GLORY_EARTH)) {
					htmlid = "ellyonne16";
				}
			}
			break;
		case 750068:// 100레벨 버프 동상
			if (player.getLevel() < 55 || player.getLevel() > 100) {
				htmlid = "lv100_buff02";
			}
			break;
			
	/*	case 800500: // 균열의 마법사 - 월드 공성전
	 * 		WarTimeController war = WarTimeController.getInstance();
			if (!war._isWolrdWar_Giran && !war._isWolrdWar_Orc && !war._isWolrdWar_Heine) {
				htmlid = "i_siege_evf";
			}
			break; */
		case 800650: // 기란성 문지기 - 월드 공성전
			boolean hascastlegiran = checkHasCastle(player, L1CastleLocation.GIRAN_CASTLE_ID);
			if (hascastlegiran || player.isGm()) { 
				htmlid = "gateokeeper";
				htmldata = new String[] { player.getName() };
			} else {
				htmlid = "gatekeeperop";
			}
			break;
		case 800651: // 오크요새 문지기 - 월드 공성전
			boolean hascastleorc = checkHasCastle(player, L1CastleLocation.OT_CASTLE_ID);
			htmlid = hascastleorc ? "orckeeper" : "orckeeperop";
			break;
		case 800652: // 하이네성 문지기 - 월드 공성전
			boolean hascastleheine = checkHasCastle(player, L1CastleLocation.HEINE_CASTLE_ID);
			if (hascastleheine || player.isGm()) { 
				htmlid = "gateokeeper";
				htmldata = new String[] { player.getName() };
			} else {
				htmlid = "gatekeeperop";
			}
			break;
		case 800800:case 800803:case 800806:case 800819:case 800820:case 800821:
			OccupyHandler handler = OccupyManager.getInstance().getHandler(player.getMapId() == L1TownLocation.MAP_WOLRDWAR_HEINE_TOWER ? OccupyType.HEINE : OccupyType.WINDAWOOD);
			if (handler == null || (handler.isBossStage() && !handler.isWinnerTeam(player._occupyTeamType))) {// 진입 제한
				htmlid = "gtw_enterfail";
			}
			break;
			
		case 146060:{// 켈튼
			//LocalTime now = LocalTime.now(ZoneId.of("Asia/Seoul"));
			LocalTime now = LocalTime.now(ZoneId.of(Config.SERVER.TIME_ZONE));			
			if (!player.isGm() && !(player.getLevel() >= 90 && player.getLevel() <= 91 && now.isAfter(LocalTime.of(18, 30)) && now.isBefore(LocalTime.of(21, 00)))) {
				htmlid = "inter_abandon2";
			}
		}
			break;
		case 146068:{// 찰리
			//LocalTime now = LocalTime.now(ZoneId.of("Asia/Seoul"));
			LocalTime now = LocalTime.now(ZoneId.of(Config.SERVER.TIME_ZONE));			
			if (!player.isGm() && !(player.getLevel() >= 90 && player.getLevel() <= 93 && now.isAfter(LocalTime.of(18, 30)) && now.isBefore(LocalTime.of(21, 00)))) {
				htmlid = "inter_abandon4";
			}
		}
			break;
		case 202056: // 기사단의 잡화 상인 세린
			if (player.getLevel() > 79) {
				htmlid = "incence_serene";
			}
			break;
		case 5000: // 기사단의 강화 마법사 테르
			if (player.getLevel() > 79) {
				htmlid = "ter_fail01";
			}
			break;
		case 100600: // 말하는 섬 스킬 보급
			if (!player.isFencer() && !player.isLancer()) {
				htmlid = "incence_rennes";
			}
			break;
			
		case 900005: //드루가 베일
			int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			if (hour >= 2 && hour <= 7) {
				htmlid = "noveil2";
			}
			break;
			
		case 7220071: //지하통로 입장
			player.getTeleport().start(32921, 32860, (short) 12014, player.getMoveState().getHeading(), true);
			break;
		case 70005:// 파고
			htmlid = "pago";
			break;
	/*	case 81210:
			if (!player.PC방_버프)
				player.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1037), true), true);
			break;*/
		case 70088: //스크와티
			if (!player.isDarkelf()) {
				htmlid = "scwaty2";
			}
			break;
		case 70841:
			if (player.isElf()) {
				htmlid = "luudielE1";
			} else if (player.isDarkelf()) {
				htmlid = "luudielCE1";
			} else {
				htmlid = "luudiel1";
			}
			break;
		case 70522: // 군터
			if (player.isCrown()) {
				if (player.getLevel() >= 15) {
					int lv15_step = quest.getStep(L1Quest.QUEST_LEVEL15);
					htmlid = lv15_step == 2 || lv15_step == L1Quest.QUEST_END ? "gunterp11" : "gunterp9";
				} else {
					htmlid = "gunterp12";
				}
			} else if (player.isKnight()) {
				int lv30_step = quest.getStep(L1Quest.QUEST_LEVEL30);
				if (lv30_step == 0) {
					htmlid = "gunterk9";
				} else if (lv30_step == 1) {
					htmlid = "gunterkE1";
				} else if (lv30_step == 2) {
					htmlid = "gunterkE2";
				} else if (lv30_step >= 3) {
					htmlid = "gunterkE3";
				}
			} else if (player.isElf()) {
				htmlid = "guntere1";
			} else if (player.isWizard()) {
				htmlid = "gunterw1";
			} else if (player.isDarkelf()) {
				htmlid = "gunterde1";
			}
			break;
		case 70653: // 마샤
			if (player.isCrown()) {
				if (player.getLevel() >= 45 && quest.isEnd(L1Quest.QUEST_LEVEL30)) {
					int lv45_step = quest.getStep(L1Quest.QUEST_LEVEL45);
					if (lv45_step == L1Quest.QUEST_END) {
						htmlid = "masha4";
					} else if (lv45_step >= 1) {
						htmlid = "masha3";
					} else {
						htmlid = "masha1";
					}
				}
			} else if (player.isKnight()) {
				if (player.getLevel() >= 45 && quest.isEnd(L1Quest.QUEST_LEVEL30)) {
					int lv45_step = quest.getStep(L1Quest.QUEST_LEVEL45);
					if (lv45_step == L1Quest.QUEST_END) {
						htmlid = "mashak3";
					} else if (lv45_step == 0) {
						htmlid = "mashak1";
					} else if (lv45_step >= 1) {
						htmlid = "mashak2";
					}
				}
			} else if (player.isElf()) {
				if (player.getLevel() >= 45 && quest.isEnd(L1Quest.QUEST_LEVEL30)) {
					int lv45_step = quest.getStep(L1Quest.QUEST_LEVEL45);
					if (lv45_step == L1Quest.QUEST_END) {
						htmlid = "mashae3";
					} else if (lv45_step >= 1) {
						htmlid = "mashae2";
					} else {
						htmlid = "mashae1";
					}
				}
			}
			break;
		case 70554: // 제로
			if (player.isCrown()) {
				if (player.getLevel() >= 15) {
					int lv15_step = quest.getStep(L1Quest.QUEST_LEVEL15);
					if (lv15_step == 1) {
						htmlid = "zero5";
					} else if (lv15_step == L1Quest.QUEST_END) {
						htmlid = "zero1";
					} else {
						htmlid = "zero1";
					}
				} else {
					htmlid = "zero6";
				}
			}
			break;
		case 70783: // 아리아
			if (player.isCrown() && player.getLevel() >= 30 && quest.isEnd(L1Quest.QUEST_LEVEL15)) {
				int lv30_step = quest.getStep(L1Quest.QUEST_LEVEL30);
				if (lv30_step == L1Quest.QUEST_END) {
					htmlid = "aria3";
				} else if (lv30_step == 1) {
					htmlid = "aria2";
				} else {
					htmlid = "aria1";
				}
			}
			break;
		case 70000: // 마빈
			htmlid = player.getLevel() < 52 ? "marbinquestA" : player.getInventory().checkItem(700012, 1) ? "marbinquest3" : "marbinquest1";
			break;
		case 70782: // 수색개미
			if (player.getSpriteId() == 1037) {
				htmlid = player.isCrown() && quest.getStep(L1Quest.QUEST_LEVEL30) == 1 ? "ant1" : "ant3";
			}
			break;
		case 70545: // 리차드
			if (player.isCrown()) {
				int lv45_step = quest.getStep(L1Quest.QUEST_LEVEL45);
				if (lv45_step >= 1 && lv45_step != L1Quest.QUEST_END) {
					htmlid = player.getInventory().checkItem(40586) ? "richard4" : "richard1";
				}
			}
			break;
		case 70776: // 맥
			if (player.isCrown()) {
				int lv45_step = quest.getStep(L1Quest.QUEST_LEVEL45);
				if (lv45_step == 1) {
					htmlid = "meg1";
				} else if (lv45_step == 2 && lv45_step <= 3) {
					htmlid = "meg2";
				} else if (lv45_step >= 4) {
					htmlid = "meg3";
				}
			}
			break;
		case 71200: // 백마법사피에타
			if (player.isCrown()) {
				int lv45_step = quest.getStep(L1Quest.QUEST_LEVEL45);
				if (lv45_step == 2 && player.getInventory().consumeItem(41422, 1)) {
					player.getInventory().storeItem(40568, 1);
				}
			}
			break;
		case 70802:// 아논
			if (player.isKnight() && player.getLevel() >= 15) {
				int lv15_step = quest.getStep(L1Quest.QUEST_LEVEL15);
				if (lv15_step == L1Quest.QUEST_END) {
					htmlid = "aanon7";
				} else if (lv15_step == 1) {
					htmlid = "aanon4";
				}
			}
			break;
		case 70775:// 마크
			if (player.isKnight() && player.getLevel() >= 30 && quest.isEnd(L1Quest.QUEST_LEVEL15)) {
				int lv30_step = quest.getStep(L1Quest.QUEST_LEVEL30);
				htmlid = lv30_step == 0 ? "mark1" : "mark2";
			}
			break;
		/*case 70794: // 게라드
			if (player.isCrown()) {
				htmlid = "gerardp1";
			} else if (player.isKnight()) {
				int lv30_step = quest.getStep(L1Quest.QUEST_LEVEL30);
				if (lv30_step == L1Quest.QUEST_END) {
					htmlid = "gerardkEcg";
				} else if (lv30_step < 3) {
					htmlid = "gerardk7";
				} else if (lv30_step == 3) {
					htmlid = "gerardkE1";
				} else if (lv30_step == 4) {
					htmlid = "gerardkE2";
				} else if (lv30_step == 5) {
					htmlid = "gerardkE3";
				} else if (lv30_step >= 6) {
					htmlid = "gerardkE4";
				}
			} else if (player.isElf()) {
				htmlid = "gerarde1";
			} else if (player.isWizard()) {
				htmlid = "gerardw1";
			} else if (player.isDarkelf()) {
				htmlid = "gerardde1";
			}
			break;*/
		case 70555:
			if (player.getSpriteId() == 2374) {
				htmlid = player.isKnight() && quest.getStep(L1Quest.QUEST_LEVEL30) == 6 ? "jim2" : "jim4";
			}
			break;
		case 70715:
			if (player.isKnight()) {
				int lv45_step = quest.getStep(L1Quest.QUEST_LEVEL45);
				if (lv45_step == 1) {
					htmlid = "jimuk1";
				} else if (lv45_step >= 2) {
					htmlid = "jimuk2";
				}
			}
			break;
		case 70711:
			if (player.isKnight()) {
				int lv45_step = quest.getStep(L1Quest.QUEST_LEVEL45);
				if (lv45_step == 2) {
					if (player.getInventory().checkItem(20026)) {
						htmlid = "giantk1";
					}
				} else if (lv45_step == 3) {
					htmlid = "giantk2";
				} else if (lv45_step >= 4) {
					htmlid = "giantk3";
				}
			}
			break;
		case 70826:
			if (player.isElf()) {
				if (player.getLevel() >= 15) {
					htmlid = quest.isEnd(L1Quest.QUEST_LEVEL15) ? "oth5" : "oth1";
				} else {
					htmlid = "oth6";
				}
			}
			break;
		case 70844:
			if (player.isElf()) {
				if (player.getLevel() >= 30) {
					if (quest.isEnd(L1Quest.QUEST_LEVEL15)) {
						int lv30_step = quest.getStep(L1Quest.QUEST_LEVEL30);
						if (lv30_step == L1Quest.QUEST_END) {
							htmlid = "motherEE3";
						} else if (lv30_step >= 1) {
							htmlid = "motherEE2";
						} else if (lv30_step <= 0) {
							htmlid = "motherEE1";
						}
					} else {
						htmlid = "mothere1";
					}
				} else {
					htmlid = "mothere1";
				}
			}
			break;
		case 70724:
			if (player.isElf()) {
				int lv45_step = quest.getStep(L1Quest.QUEST_LEVEL45);
				if (lv45_step >= 4) {
					htmlid = "heit5";
				} else if (lv45_step >= 3) {
					htmlid = "heit3";
				} else if (lv45_step >= 2) {
					htmlid = "heit2";
				} else if (lv45_step >= 1) {
					htmlid = "heit1";
				}
			}
			break;
		case 70531:
			if (player.isWizard() && player.getLevel() >= 15) {
				htmlid = quest.isEnd(L1Quest.QUEST_LEVEL15) ? "jem6" : "jem1";
			}
			break;
		case 70009:
			if (player.isCrown()) {
				htmlid = "gerengp1";
			} else if (player.isKnight() || player.isWarrior()) {
				htmlid = "gerengk1";
			} else if (player.isElf()) {
				htmlid = "gerenge1";
			} else if (player.isWizard()){
				if (player.getLevel() >= 30) {
					if (quest.isEnd(L1Quest.QUEST_LEVEL15)) {
						int lv30_step = quest.getStep(L1Quest.QUEST_LEVEL30);
						if (lv30_step >= 4) {
							htmlid = "gerengw3";
						} else if (lv30_step >= 3) {
							htmlid = "gerengT4";
						} else if (lv30_step >= 2) {
							htmlid = "gerengT3";
						} else if (lv30_step >= 1) {
							htmlid = "gerengT2";
						} else {
							htmlid = "gerengT1";
						}
					} else {
						htmlid = "gerengw3";
					}
				} else {
					htmlid = "gerengw3";
				}
			} else if (player.isDarkelf()) {
				htmlid = "gerengde1";
			}
			break;
		/*case 70763:
			if (player.isWizard()) {
				int lv30_step = quest.getStep(L1Quest.QUEST_LEVEL30);
				if (lv30_step == L1Quest.QUEST_END) {
					if (player.getLevel() >= 45) {
						int lv45_step = quest.getStep(L1Quest.QUEST_LEVEL45);
						if (lv45_step >= 1 && lv45_step != L1Quest.QUEST_END) {
							htmlid = "talassmq2";
						} else if (lv45_step <= 0) {
							htmlid = "talassmq1";
						}
					}
				} else if (lv30_step == 4) {
					htmlid = "talassE1";
				} else if (lv30_step == 5) {
					htmlid = "talassE2";
				}
			}
			break;*/
		case 81105:
			if (player.isWizard()) {
				int lv45_step = quest.getStep(L1Quest.QUEST_LEVEL45);
				if (lv45_step >= 3) {
					htmlid = "stoenm3";
				} else if (lv45_step >= 2) {
					htmlid = "stoenm2";
				} else if (lv45_step >= 1) {
					htmlid = "stoenm1";
				}
			}
			break;
		case 70739: // 디가르뎅
			if (player.getLevel() >= 50) {
				int lv50_step = quest.getStep(L1Quest.QUEST_LEVEL50);
				if (lv50_step == L1Quest.QUEST_END) {
					if (player.isCrown()) {
						htmlid = "dicardingp3";
					} else if (player.isKnight()) {
						htmlid = "dicardingk3";
					} else if (player.isElf()) {
						htmlid = "dicardinge3";
					} else if (player.isWizard()) {
						htmlid = "dicardingw3";
					} else if (player.isDarkelf()) {
						htmlid = "dicarding";
					}
				} else if (lv50_step >= 1) {
					if (player.isCrown()) {
						htmlid = "dicardingp2";
					} else if (player.isKnight()) {
						htmlid = "dicardingk2";
					} else if (player.isElf()) {
						htmlid = "dicardinge2";
					} else if (player.isWizard()) {
						htmlid = "dicardingw2";
					} else if (player.isDarkelf()) {
						htmlid = "dicarding";
					}
				} else if (lv50_step >= 0) {
					if (player.isCrown()) {
						htmlid = "dicardingp1";
					} else if (player.isKnight()) {
						htmlid = "dicardingk1";
					} else if (player.isElf()) {
						htmlid = "dicardinge1";
					} else if (player.isWizard()) {
						htmlid = "dicardingw1";
					} else if (player.isDarkelf()) {
						htmlid = "dicarding";
					}
				} else {
					htmlid = "dicarding";
				}
			} else {
				htmlid = "dicarding";
			}
			break;
		case 70885:
			if (player.isDarkelf()) {
				if (player.getLevel() >= 15) {
					int lv15_step = quest.getStep(L1Quest.QUEST_LEVEL15);
					if (lv15_step == L1Quest.QUEST_END) {
						htmlid = "kanguard3";
					} else if (lv15_step >= 1) {
						htmlid = "kanguard2";
					} else {
						htmlid = "kanguard1";
					}
				} else {
					htmlid = "kanguard5";
				}
			}
			break;
		case 70892:
			if (player.isDarkelf()) {
				if (player.getLevel() >= 30) {
					if (quest.isEnd(L1Quest.QUEST_LEVEL15)) {
						int lv30_step = quest.getStep(L1Quest.QUEST_LEVEL30);
						if (lv30_step == L1Quest.QUEST_END) {
							htmlid = "ronde5";
						} else if (lv30_step >= 2) {
							htmlid = "ronde3";
						} else if (lv30_step >= 1) {
							htmlid = "ronde2";
						} else {
							htmlid = "ronde1";
						}
					} else {
						htmlid = "ronde7";
					}
				} else {
					htmlid = "ronde7";
				}
			}
			break;
		
		case 70904:
			if (player.isDarkelf() && quest.getStep(L1Quest.QUEST_LEVEL45) == 1) {
				htmlid = "koup12";
			}
			break;
		case 70824:
			if (player.isDarkelf()) {
				if (player.getSpriteId() == 3634) {
					int lv45_step = quest.getStep(L1Quest.QUEST_LEVEL45);
					if (lv45_step == 1) {
						htmlid = "assassin1";
					} else if (lv45_step == 2) {
						htmlid = "assassin2";
					} else {
						htmlid = "assassin3";
					}
				} else {
					htmlid = "assassin3";
				}
			}
			break;
		case 70744:
			if (player.isDarkelf()) {
				int lv45_step = quest.getStep(L1Quest.QUEST_LEVEL45);
				if (lv45_step >= 5) {
					htmlid = "roje14";
				} else if (lv45_step >= 4) {
					htmlid = "roje13";
				} else if (lv45_step >= 3) {
					htmlid = "roje12";
				} else if (lv45_step >= 2) {
					htmlid = "roje11";
				} else {
					htmlid = "roje15";
				}
			}
			break;
		case 3000003: // 장로 프로켈
			if (player.isDragonknight()) {
				if (player.getLevel() >= 15) {
					int lv15_step = quest.getStep(L1Quest.QUEST_LEVEL15);
					if (lv15_step == 1) {
						htmlid = "prokel4";
					} else if (lv15_step == 2 || lv15_step == L1Quest.QUEST_END) {
						htmlid = "prokel7";
					} else {
						htmlid = "prokel2";
					}
				} else {
					htmlid = "prokel1"; // 레벨 15이하
				}
			}
			break;
		case 3100004: // 장로 실레인
			if (player.isIllusionist()) {
				if (player.getLevel() >= 15) {
					int lv15_step = quest.getStep(L1Quest.QUEST_LEVEL15);
					if (lv15_step == 1) {
						htmlid = "silrein4";
					} else if (lv15_step == 2 || lv15_step == L1Quest.QUEST_END) {
						htmlid = "silrein5";
					} else {
						htmlid = "silrein2";
					}
				} else {
					htmlid = "prokel1"; // 레벨 15이하
				}
			}
			break;
		// case 70811:
		// htmlid = quest.getStep(L1Quest.QUEST_LYRA) >= 1 ? "lyraEv3" : "lyraEv1";
		// break;
		case 70087:
			if (player.isDarkelf()) {
				htmlid = "sedia";
			}
			break;
		case 70099:
			if (!quest.isEnd(L1Quest.QUEST_OILSKINMANT) && player.getLevel() > 13) {
				htmlid = "kuper1";
			}
			break;
		case 70796:
			if (!quest.isEnd(L1Quest.QUEST_OILSKINMANT) && player.getLevel() > 13) {
				htmlid = "dunham1";
			}
			break;
		case 70011:
			long time = GameTimeClock.getInstance().getGameTime().getSeconds() % 86400;
			if (time < 3600 * 6 || time > 3600 * 20) {// 20:00?6:00
				htmlid = "shipEvI6";
			}
			break;
		case 70553:
			boolean hascastle = checkHasCastle(player, L1CastleLocation.KENT_CASTLE_ID);
			if (player.isGm()) {
				htmlid = "ishmael1";
			} else if (hascastle) {
				if (checkClanLeader(player)) {
					htmlid = "ishmael1";
				} else {
					htmlid = "ishmael6";
					htmldata = new String[] { player.getName() };
				}
			} else {
				htmlid = "ishmael7";
			}
			break;
		case 70822:
			boolean hascastle1 = checkHasCastle(player, L1CastleLocation.OT_CASTLE_ID);
			if (player.isGm()) {
				htmlid = "seghem1";
			} else if (hascastle1) {
				if (checkClanLeader(player)) {
					htmlid = "seghem1";
				} else {
					htmlid = "seghem6";
					htmldata = new String[] { player.getName() };
				}
			} else {
				htmlid = "seghem7";
			}
			break;
		case 70784:
			boolean hascastle2 = checkHasCastle(player, L1CastleLocation.WW_CASTLE_ID);
			if (player.isGm()) {
				htmlid = "othmond1";
			} else if (hascastle2) {
				if (checkClanLeader(player)) {
					htmlid = "othmond1";
				} else {
					htmlid = "othmond6";
					htmldata = new String[] { player.getName() };
				}
			} else {
				htmlid = "othmond7";
			}
			break;
		case 70623:
			boolean hascastle3 = checkHasCastle(player, L1CastleLocation.GIRAN_CASTLE_ID);
			if (player.isGm()) {
				htmlid = "orville1";
			} else if (hascastle3) {
				if (checkClanLeader(player)) {
					htmlid = "orville1";
				} else {
					htmlid = "orville6";
					htmldata = new String[] { player.getName() };
				}
			} else {
				htmlid = "orville7";
			}
			break;
		case 70880:
			boolean hascastle4 = checkHasCastle(player, L1CastleLocation.HEINE_CASTLE_ID);
			if (player.isGm()) {
				htmlid = "fisher1";
			} else if (hascastle4) {
				if (checkClanLeader(player)) {
					htmlid = "fisher1";
				} else {
					htmlid = "fisher6";
					htmldata = new String[] { player.getName() };
				}
			} else {
				htmlid = "fisher7";
			}
			break;
		case 70665:
			boolean hascastle5 = checkHasCastle(player, L1CastleLocation.DOWA_CASTLE_ID);
			if (player.isGm()) {
				htmlid = "potempin1";
			} else if (hascastle5) {
				if (checkClanLeader(player)) {
					htmlid = "potempin1";
				} else {
					htmlid = "potempin6";
					htmldata = new String[] { player.getName() };
				}
			} else {
				htmlid = "potempin7";
			}
			break;
		case 70721:
			boolean hascastle6 = checkHasCastle(player, L1CastleLocation.ADEN_CASTLE_ID);
			if (player.isGm()) {
				htmlid = "timon1";
			} else if (hascastle6) {
				if (checkClanLeader(player)) {
					htmlid = "timon1";
				} else {
					htmlid = "timon6";
					htmldata = new String[] { player.getName() };
				}
			} else {
				htmlid = "timon7";
			}
			break;
		case 81155:
			boolean hascastle7 = checkHasCastle(player, L1CastleLocation.DIAD_CASTLE_ID);
			if (player.isGm()) {
				htmlid = "olle1";
			} else if (hascastle7) {
				if (checkClanLeader(player)) {
					htmlid = "olle1";
				} else {
					htmlid = "olle6";
					htmldata = new String[] { player.getName() };
				}
			} else {
				htmlid = "olle7";
			}
			break;
		case 80057:
			switch (player.getKarmaLevel()) {
			case 0:htmlid = "alfons1";break;
			case -1:htmlid = "cyk1";break;
			case -2:htmlid = "cyk2";break;
			case -3:htmlid = "cyk3";break;
			case -4:htmlid = "cyk4";break;
			case -5:htmlid = "cyk5";break;
			case -6:htmlid = "cyk6";break;
			case -7:htmlid = "cyk7";break;
			case -8:htmlid = "cyk8";break;
			case 1:htmlid = "cbk1";break;
			case 2:htmlid = "cbk2";break;
			case 3:htmlid = "cbk3";break;
			case 4:htmlid = "cbk4";break;
			case 5:htmlid = "cbk5";break;
			case 6:htmlid = "cbk6";break;
			case 7:htmlid = "cbk7";break;
			case 8:htmlid = "cbk8";break;
			default:htmlid = "alfons1";break;
			}
			break;
		case 80048:
			int level = player.getLevel();
			if (level <= 44) {
				htmlid = "entgate3";
			} else if (level <= 51) {
				htmlid = "entgate2";
			} else {
				htmlid = "entgate";
			}
			break;
		case 80058:
			int level5 = player.getLevel();
			if (level5 <= 44) {
				htmlid = "cpass03";
			} else if (level5 <= 51) {
				htmlid = "cpass02";
			} else {
				htmlid = "cpass01";
			}
			break;
		case 80059:
			if (player.getKarmaLevel() >= 3) {
				htmlid = "cpass03";
			} else if (player.getInventory().checkItem(40921)) {
				htmlid = "wpass02";
			} else if (player.getInventory().checkItem(40917)) {
				htmlid = "wpass14";
			} else if (player.getInventory().checkItem(40912) || player.getInventory().checkItem(40910) || player.getInventory().checkItem(40911)) {
				htmlid = "wpass04";
			} else if (player.getInventory().checkItem(40909)) {
				int count = getNecessarySealCount(player);
				if (player.getInventory().checkItem(40913, count)) {
					createRuler(player, 1, count);
					htmlid = "wpass06";
				} else {
					htmlid = "wpass03";
				}
			} else if (player.getInventory().checkItem(40913)) {
				htmlid = "wpass08";
			} else {
				htmlid = "wpass05";
			}
			break;
		case 80060:
			if(player.getKarmaLevel() >= 3)
				htmlid = "cpass03";
			else if(player.getInventory().checkItem(40921))
				htmlid = "wpass02";
			else if(player.getInventory().checkItem(40920))
				htmlid = "wpass13";
			else if(player.getInventory().checkItem(40909) || player.getInventory().checkItem(40910) || player.getInventory().checkItem(40911))
				htmlid = "wpass04";
			else if(player.getInventory().checkItem(40912)){
				int count = getNecessarySealCount(player);
				if(player.getInventory().checkItem(40916, count)){
					createRuler(player, 8, count);
					htmlid = "wpass06";
				}else
					htmlid = "wpass03";
			}else if (player.getInventory().checkItem(40916))
				htmlid = "wpass08";
			else
				htmlid = "wpass05";
			break;
		case 80061:
			if(player.getKarmaLevel() >= 3)
				htmlid = "cpass03";
			else if(player.getInventory().checkItem(40921))
				htmlid = "wpass02";
			else if(player.getInventory().checkItem(40918))
				htmlid = "wpass11";
			else if(player.getInventory().checkItem(40909) || player.getInventory().checkItem(40912) || player.getInventory().checkItem(40911))
				htmlid = "wpass04";
			else if(player.getInventory().checkItem(40910)){
				int count = getNecessarySealCount(player);
				if(player.getInventory().checkItem(40914, count)){
					createRuler(player, 4, count);
					htmlid = "wpass06";
				}else
					htmlid = "wpass03";
			}else if(player.getInventory().checkItem(40914))
				htmlid = "wpass08";
			else
				htmlid = "wpass05";
			break;
		case 80062:
			if(player.getKarmaLevel() >= 3)
				htmlid = "cpass03";
			else if(player.getInventory().checkItem(40921))
				htmlid = "wpass02";
			else if(player.getInventory().checkItem(40919))
				htmlid = "wpass12";
			else if(player.getInventory().checkItem(40909) || player.getInventory().checkItem(40912) || player.getInventory().checkItem(40910))
				htmlid = "wpass04";
			else if(player.getInventory().checkItem(40911)){
				int count = getNecessarySealCount(player);
				if(player.getInventory().checkItem(40915, count)){
					createRuler(player, 2, count);
					htmlid = "wpass06";
				}else
					htmlid = "wpass03";
			}else if(player.getInventory().checkItem(40915))
				htmlid = "wpass08";
			else
				htmlid = "wpass05";
			break;
		case 80065:
			htmlid = player.getKarmaLevel() < 3 ? "uturn0" : "uturn1";
			break;
		case 80047:
			htmlid = player.getKarmaLevel() > -3 ? "uhelp1" : "uhelp2";
			break;
		case 80049:
			htmlid = player.getKarma() <= -10000000 ? "betray11" : "betray12";
			break;
		case 80050:
			htmlid = player.getKarmaLevel() > -1 ? "meet103" : "meet101";
			break;
		case 80053:
			int karmaLevel = player.getKarmaLevel();
			if(karmaLevel == 0)htmlid = "aliceyet";
			else if(karmaLevel >= 1){
					htmlid = (player.getInventory().checkItem(196) || player.getInventory().checkItem(197) || player.getInventory().checkItem(198)
							|| player.getInventory().checkItem(199) || player.getInventory().checkItem(200) || player.getInventory().checkItem(201)
							|| player.getInventory().checkItem(202) || player.getInventory().checkItem(203)) ? "alice_gd" : "gd";
			}else if(karmaLevel <= -1){
				if(player.getInventory().checkItem(40991)){
					if(karmaLevel <= -1)htmlid = "Mate_1";
				}else if(player.getInventory().checkItem(196))
					htmlid = karmaLevel <= -2 ? "Mate_2" : "alice_1";
				else if(player.getInventory().checkItem(197))
					htmlid = karmaLevel <= -3 ? "Mate_3" : "alice_2";
				else if(player.getInventory().checkItem(198))
					htmlid = karmaLevel <= -4 ? "Mate_4" : "alice_3";
				else if(player.getInventory().checkItem(199))
					htmlid = karmaLevel <= -5 ? "Mate_5" : "alice_4";
				else if(player.getInventory().checkItem(200))
					htmlid = karmaLevel <= -6 ? "Mate_6" : "alice_5";
				else if(player.getInventory().checkItem(201))
					htmlid = karmaLevel <= -7 ? "Mate_7" : "alice_6";
				else if(player.getInventory().checkItem(202))
					htmlid = karmaLevel <= -8 ? "Mate_8" : "alice_7";
				else if(player.getInventory().checkItem(203))
					htmlid = "alice_8";
				else
					htmlid = "alice_no";
			}
			break;
		case 80055:
			int amuletLevel = 0;
			if(player.getInventory().checkItem(20358))		amuletLevel = 1;
			else if(player.getInventory().checkItem(20359))	amuletLevel = 2;
			else if(player.getInventory().checkItem(20360))	amuletLevel = 3;
			else if(player.getInventory().checkItem(20361))	amuletLevel = 4;
			else if(player.getInventory().checkItem(20362))	amuletLevel = 5;
			else if(player.getInventory().checkItem(20363))	amuletLevel = 6;
			else if(player.getInventory().checkItem(20364))	amuletLevel = 7;
			else if(player.getInventory().checkItem(20365))	amuletLevel = 8;
			if(player.getKarmaLevel() == -1)
				htmlid = amuletLevel >= 1 ? "uamuletd" : "uamulet1";
			else if(player.getKarmaLevel() == -2)
				htmlid = amuletLevel >= 2 ? "uamuletd" : "uamulet2";
			else if(player.getKarmaLevel() == -3)
				htmlid = amuletLevel >= 3 ? "uamuletd" : "uamulet3";
			else if(player.getKarmaLevel() == -4)
				htmlid = amuletLevel >= 4 ? "uamuletd" : "uamulet4";
			else if(player.getKarmaLevel() == -5)
				htmlid = amuletLevel >= 5 ? "uamuletd" : "uamulet5";
			else if(player.getKarmaLevel() == -6)
				htmlid = amuletLevel >= 6 ? "uamuletd" : "uamulet6";
			else if(player.getKarmaLevel() == -7)
				htmlid = amuletLevel >= 7 ? "uamuletd" : "uamulet7";
			else if(player.getKarmaLevel() == -8)
				htmlid = amuletLevel >= 8 ? "uamuletd" : "uamulet8";
			else
				htmlid = "uamulet0";
			break;
		case 80056:
			htmlid = player.getKarma() <= -10000000 ? "infamous11" : "infamous12";
			break;
		case 80066:
			htmlid = player.getKarma() >= 10000000 ? "betray01" : "betray02";
			break;
		case 80071:
			int earringLevel = 0;
			if(player.getInventory().checkItem(21020))		earringLevel = 1;
			else if(player.getInventory().checkItem(21021))	earringLevel = 2;
			else if(player.getInventory().checkItem(21022))	earringLevel = 3;
			else if(player.getInventory().checkItem(21023))	earringLevel = 4;
			else if(player.getInventory().checkItem(21024))	earringLevel = 5;
			else if(player.getInventory().checkItem(21025))	earringLevel = 6;
			else if(player.getInventory().checkItem(21026))	earringLevel = 7;
			else if(player.getInventory().checkItem(21027))	earringLevel = 8;
			if(player.getKarmaLevel() == 1)
				htmlid = earringLevel >= 1 ? "lringd" : "lring1";
			else if(player.getKarmaLevel() == 2)
				htmlid = earringLevel >= 2 ? "lringd" : "lring2";
			else if(player.getKarmaLevel() == 3)
				htmlid = earringLevel >= 3 ? "lringd" : "lring3";
			else if(player.getKarmaLevel() == 4)
				htmlid = earringLevel >= 4 ? "lringd" : "lring4";
			else if(player.getKarmaLevel() == 5)
				htmlid = earringLevel >= 5 ? "lringd" : "lring5";
			else if(player.getKarmaLevel() == 6)
				htmlid = earringLevel >= 6 ? "lringd" : "lring6";
			else if(player.getKarmaLevel() == 7)
				htmlid = earringLevel >= 7 ? "lringd" : "lring7";
			else if(player.getKarmaLevel() == 8)
				htmlid = earringLevel >= 8 ? "lringd" : "lring8";
			else
				htmlid = "lring0";
			break;
		case 80072:
			int karmaLevel1 = player.getKarmaLevel();
			if(karmaLevel1 == 1)		htmlid = "lsmith0";
			else if(karmaLevel1 == 2)	htmlid = "lsmith1";
			else if(karmaLevel1 == 3)	htmlid = "lsmith2";
			else if(karmaLevel1 == 4)	htmlid = "lsmith3";
			else if(karmaLevel1 == 5)	htmlid = "lsmith4";
			else if(karmaLevel1 == 6)	htmlid = "lsmith5";
			else if(karmaLevel1 == 7)	htmlid = "lsmith7";
			else if(karmaLevel1 == 8)	htmlid = "lsmith8";
			else						htmlid = StringUtil.EmptyString;
			break;
		case 80074:
			htmlid = player.getKarma() >= 10000000 ? "infamous01" : "infamous02";
			break;
		case 80104: //기마단원
		case 80105: //기마단원
			if(!player.isCrown())
				htmlid = "horseseller4";
			break;
		case 70528:
			htmlid = talkToTownmaster(player, L1TownLocation.TOWNID_TALKING_ISLAND);
			break;
		case 70546:
			htmlid = talkToTownmaster(player, L1TownLocation.TOWNID_KENT);
			break;
		case 70567:
			htmlid = talkToTownmaster(player, L1TownLocation.TOWNID_GLUDIO);
			break;
		case 70815:
			htmlid = talkToTownmaster(player, L1TownLocation.TOWNID_ORCISH_FOREST);
			break;
		case 70774:
			htmlid = talkToTownmaster(player, L1TownLocation.TOWNID_WINDAWOOD);
			break;
		case 70799:
			htmlid = talkToTownmaster(player, L1TownLocation.TOWNID_SILVER_KNIGHT);
			break;
		case 70594:
			htmlid = talkToTownmaster(player, L1TownLocation.TOWNID_GIRAN);
			break;
		case 70860:
			htmlid = talkToTownmaster(player, L1TownLocation.TOWNID_HEINE);
			break;
		case 70654:
			htmlid = talkToTownmaster(player, L1TownLocation.TOWNID_WERLDAN);
			break;
		case 70748:
			htmlid = talkToTownmaster(player, L1TownLocation.TOWNID_OREN);
			break;
		case 70534:
			htmlid = talkToTownadviser(player, L1TownLocation.TOWNID_TALKING_ISLAND);
			break;
		case 70556:
			htmlid = talkToTownadviser(player, L1TownLocation.TOWNID_KENT);
			break;
		case 70572:
			htmlid = talkToTownadviser(player, L1TownLocation.TOWNID_GLUDIO);
			break;
		case 70830:
			htmlid = talkToTownadviser(player, L1TownLocation.TOWNID_ORCISH_FOREST);
			break;
		case 70788:
			htmlid = talkToTownadviser(player, L1TownLocation.TOWNID_WINDAWOOD);
			break;
		case 70806:
			htmlid = talkToTownadviser(player, L1TownLocation.TOWNID_SILVER_KNIGHT);
			break;
		case 70631:
			htmlid = talkToTownadviser(player, L1TownLocation.TOWNID_GIRAN);
			break;
		case 70876:
			htmlid = talkToTownadviser(player, L1TownLocation.TOWNID_HEINE);
			break;
		case 70663:
			htmlid = talkToTownadviser(player, L1TownLocation.TOWNID_WERLDAN);
			break;
		case 70761:
			htmlid = talkToTownadviser(player, L1TownLocation.TOWNID_OREN);
			break;
		case 70998:
			htmlid = talkToSIGuide(player);
			break;
		case 71005:
			htmlid = talkToPopirea(player);
			break;
		case 71013:
			if (player.isDarkelf()) {
				if (player.getLevel() <= 3) {
					htmlid = "karen1";
				} else if (player.getLevel() > 3 && player.getLevel() < 50) {
					htmlid = "karen3";
				} else if (player.getLevel() >= 50) {
					htmlid = "karen4";
				}
			}
			break;
		case 71031:
			if (player.getLevel() < 25) {
				htmlid = "en0081";
			}
			break;
		case 71021:
			if (player.getLevel() < 12) {
				htmlid = "en0197";
			} else if (player.getLevel() < 25) {
				htmlid = "en0191";
			}
			break;
		case 71022:
			if (player.getLevel() < 12) {
				htmlid = "jpe0155";
			} else if (player.getLevel() >= 12 && player.getLevel() < 25) {
				if (player.getInventory().checkItem(41230) || player.getInventory().checkItem(41231) || player.getInventory().checkItem(41232)
						|| player.getInventory().checkItem(41233) || player.getInventory().checkItem(41235) || player.getInventory().checkItem(41238)
						|| player.getInventory().checkItem(41239) || player.getInventory().checkItem(41240)) {
					htmlid = "jpe0158";
				}
			}
			break;
		case 71023:
			if (player.getLevel() < 12) {
				htmlid = "jpe0145";
			} else if (player.getLevel() >= 12 && player.getLevel() < 25) {
				if (player.getInventory().checkItem(41233) || player.getInventory().checkItem(41234)) {
					htmlid = "jpe0143";
				} else if (player.getInventory().checkItem(41238) || player.getInventory().checkItem(41239) || player.getInventory().checkItem(41240)) {
					htmlid = "jpe0147";
				} else if (player.getInventory().checkItem(41235) || player.getInventory().checkItem(41236) || player.getInventory().checkItem(41237)) {
					htmlid = "jpe0144";
				}
			}
			break;
		case 71020:
			if (player.getLevel() < 12) {
				htmlid = "jpe0125";
			} else if (player.getLevel() >= 12 && player.getLevel() < 25) {
				if (player.getInventory().checkItem(41231)) {
					htmlid = "jpe0123";
				} else if (player.getInventory().checkItem(41232) || player.getInventory().checkItem(41233) || player.getInventory().checkItem(41234)
						|| player.getInventory().checkItem(41235) || player.getInventory().checkItem(41238) || player.getInventory().checkItem(41239)
						|| player.getInventory().checkItem(41240)) {
					htmlid = "jpe0126";
				}
			}
			break;
		case 71019:
			if (player.getLevel() < 12) {
				htmlid = "jpe0114";
			} else if (player.getLevel() < 25) {
				htmlid = player.getInventory().checkItem(41239) ? "jpe0113" : "jpe0111";
			}
			break;
		case 71018:
			if (player.getLevel() < 12) {
				htmlid = "jpe0133";
			} else if (player.getLevel() < 25) {
				htmlid = player.getInventory().checkItem(41240) ? "jpe0132" : "jpe0131";
			}
			break;
		case 71025:
			if (player.getLevel() < 10) {
				htmlid = "jpe0086";
			} else if (player.getLevel() >= 10 && player.getLevel() < 25) {
				if (player.getInventory().checkItem(41226)) {
					htmlid = "jpe0084";
				} else if (player.getInventory().checkItem(41225)) {
					htmlid = "jpe0083";
				} else if (player.getInventory().checkItem(40653) || player.getInventory().checkItem(40613)) {
					htmlid = "jpe0081";
				}
			}
			break;
		/*
		 * } else if (npcid == 70512) { if (player.getLevel() >= 25) { htmlid = "jpe0102"; } } else if (npcid == 70514) { if (player.getLevel() >= 25) { htmlid = "jpe0092"; }
		 * 
		 * case 70035: case 70041: // 버경 상인 case 70042: if(BugRaceController.getInstance().getBugState() == 1){ htmlid = "maeno3"; }else
		 * if(BugRaceController.getInstance().getBugState() == 2){ htmlid = "maeno5"; }else{ htmlid = "pandora"; } break;
		 */
		case 70035:
		case 70041:
		case 70042:
			DollRaceController doll = DollRaceController.getInstance();
			if (doll.getDollState() == 1) {
				htmlid = "maeno3";
			} else if (doll.getDollState() == 2) {
				htmlid = "maeno5";
			}
			break;
			
		case 170041:
			DogFightController dog = DogFightController.getInstance();
			if (dog.getdogState() == 1) {
				htmlid = "maeno3";
			} else if (dog.getdogState() == 2) {
				htmlid = "maeno5";
			}
			break;
			
		case 71038:
			if (player.getInventory().checkItem(41060)) {
				htmlid = player.getInventory().checkItem(41090) || player.getInventory().checkItem(41091) || player.getInventory().checkItem(41092) ? "orcfnoname7" : "orcfnoname8";
			} else {
				htmlid = "orcfnoname1";
			}
			break;
		case 71040:
			if (player.getInventory().checkItem(41060)) {
				if (player.getInventory().checkItem(41065)) {
					htmlid = player.getInventory().checkItem(41086) || player.getInventory().checkItem(41087) || player.getInventory().checkItem(41088)
							|| player.getInventory().checkItem(41089) ? "orcfnoa6" : "orcfnoa5";
				} else {
					htmlid = "orcfnoa2";
				}
			} else {
				htmlid = "orcfnoa1";
			}
			break;
		case 71041:
			if (player.getInventory().checkItem(41060)) {
				if (player.getInventory().checkItem(41064)) {
					htmlid = player.getInventory().checkItem(41081) || player.getInventory().checkItem(41082) || player.getInventory().checkItem(41083)
							|| player.getInventory().checkItem(41084) || player.getInventory().checkItem(41085) ? "orcfhuwoomo2" : "orcfhuwoomo8";
				} else {
					htmlid = "orcfhuwoomo1";
				}
			} else {
				htmlid = "orcfhuwoomo5";
			}
			break;
		case 71042:
			if (player.getInventory().checkItem(41060)) {
				if (player.getInventory().checkItem(41062)) {
					htmlid = player.getInventory().checkItem(41071) || player.getInventory().checkItem(41072) || player.getInventory().checkItem(41073)
							|| player.getInventory().checkItem(41074) || player.getInventory().checkItem(41075) ? "orcfbakumo2" : "orcfbakumo8";
				} else {
					htmlid = "orcfbakumo1";
				}
			} else {
				htmlid = "orcfbakumo5";
			}
			break;
		case 71043:
			if (player.getInventory().checkItem(41060)) {
				if (player.getInventory().checkItem(41063)) {
					htmlid = player.getInventory().checkItem(41076) || player.getInventory().checkItem(41077) || player.getInventory().checkItem(41078)
							|| player.getInventory().checkItem(41079) || player.getInventory().checkItem(41080) ? "orcfbuka2" : "orcfbuka8";
				} else {
					htmlid = "orcfbuka1";
				}
			} else {
				htmlid = "orcfbuka5";
			}
			break;
		case 71044:
			if (player.getInventory().checkItem(41060)) {
				if (player.getInventory().checkItem(41061)) {
					htmlid = player.getInventory().checkItem(41066) || player.getInventory().checkItem(41067) || player.getInventory().checkItem(41068)
							|| player.getInventory().checkItem(41069) || player.getInventory().checkItem(41070) ? "orcfkame2" : "orcfkame8";
				} else {
					htmlid = "orcfkame1";
				}
			} else {
				htmlid = "orcfkame5";
			}
			break;
		case 71055:
			if (player.getQuest().getStep(L1Quest.QUEST_RESTA) == 3) {
				htmlid = "lukein13";
			} else if (player.getQuest().getStep(L1Quest.QUEST_LUKEIN1) == L1Quest.QUEST_END && player.getQuest().getStep(L1Quest.QUEST_RESTA) == 2
					&& player.getInventory().checkItem(40631)) {
				htmlid = "lukein10";
			} else if (player.getQuest().getStep(L1Quest.QUEST_LUKEIN1) == L1Quest.QUEST_END) {
				htmlid = "lukein0";
			} else if (player.getQuest().getStep(L1Quest.QUEST_LUKEIN1) == 11) {
				if (player.getInventory().checkItem(40716)) {
					htmlid = "lukein9";
				}
			} else if (player.getQuest().getStep(L1Quest.QUEST_LUKEIN1) >= 1 && player.getQuest().getStep(L1Quest.QUEST_LUKEIN1) <= 10) {
				htmlid = "lukein8";
			}
			break;
		case 71063:
			if (player.getQuest().getStep(L1Quest.QUEST_LUKEIN1) == 1) {
				htmlid = "maptbox";
			}
			break;
		case 71064:
			if (player.getQuest().getStep(L1Quest.QUEST_LUKEIN1) == 2) {
				htmlid = talkToSecondtbox(player);
			}
			break;
		case 71065:
			if (player.getQuest().getStep(L1Quest.QUEST_LUKEIN1) == 3) {
				htmlid = talkToSecondtbox(player);
			}
			break;
		case 71066:
			if (player.getQuest().getStep(L1Quest.QUEST_LUKEIN1) == 4) {
				htmlid = talkToSecondtbox(player);
			}
			break;
		case 71067:
			if (player.getQuest().getStep(L1Quest.QUEST_LUKEIN1) == 5) {
				htmlid = talkToThirdtbox(player);
			}
			break;
		case 71068:
			if (player.getQuest().getStep(L1Quest.QUEST_LUKEIN1) == 6) {
				htmlid = talkToThirdtbox(player);
			}
			break;
		case 71069:
			if (player.getQuest().getStep(L1Quest.QUEST_LUKEIN1) == 7) {
				htmlid = talkToThirdtbox(player);
			}
			break;
		case 71070:
			if (player.getQuest().getStep(L1Quest.QUEST_LUKEIN1) == 8) {
				htmlid = talkToThirdtbox(player);
			}
			break;
		case 71071:
			if (player.getQuest().getStep(L1Quest.QUEST_LUKEIN1) == 9) {
				htmlid = talkToThirdtbox(player);
			}
			break;
		case 71072:
			if (player.getQuest().getStep(L1Quest.QUEST_LUKEIN1) == 10) {
				htmlid = talkToThirdtbox(player);
			}
			break;
		case 71056:
			if(player.getQuest().getStep(L1Quest.QUEST_RESTA) == 4)							htmlid = player.getInventory().checkItem(40631) ? "SIMIZZ11" : "SIMIZZ0";
			else if(player.getQuest().getStep(L1Quest.QUEST_SIMIZZ) == 2)					htmlid = "SIMIZZ0";
			else if(player.getQuest().getStep(L1Quest.QUEST_SIMIZZ) == L1Quest.QUEST_END)	htmlid = "SIMIZZ15";
			else if(player.getQuest().getStep(L1Quest.QUEST_SIMIZZ) == 1)					htmlid = "SIMIZZ6";
			break;
		case 71057:
			if(player.getQuest().getStep(L1Quest.QUEST_DOIL) == L1Quest.QUEST_END)		htmlid = "doil4b";
			break;
		case 71059:
			if(player.getQuest().getStep(L1Quest.QUEST_RUDIAN) == L1Quest.QUEST_END)	htmlid = "rudian1c";
			else if(player.getQuest().getStep(L1Quest.QUEST_RUDIAN) == 1)				htmlid = "rudian7";
			else if(player.getQuest().getStep(L1Quest.QUEST_DOIL) == L1Quest.QUEST_END)	htmlid = "rudian1b";
			else																		htmlid = "rudian1a";
			break;
		case 71060:
			if(player.getQuest().getStep(L1Quest.QUEST_RESTA) == L1Quest.QUEST_END)
				htmlid = "resta1e";
			else if(player.getQuest().getStep(L1Quest.QUEST_SIMIZZ) == L1Quest.QUEST_END)
				htmlid = "resta14";
			else if(player.getQuest().getStep(L1Quest.QUEST_RESTA) == 4)
				htmlid = "resta13";
			else if(player.getQuest().getStep(L1Quest.QUEST_RESTA) == 3){
				htmlid = "resta11";
				player.getQuest().setStep(L1Quest.QUEST_RESTA, 4);
			}else if(player.getQuest().getStep(L1Quest.QUEST_RESTA) == 2)
				htmlid = "resta16";
			else if(player.getQuest().getStep(L1Quest.QUEST_SIMIZZ) == 2 && player.getQuest().getStep(L1Quest.QUEST_CADMUS) == 1 || player.getInventory().checkItem(40647))
				htmlid = "resta1a";
			else if(player.getQuest().getStep(L1Quest.QUEST_CADMUS) == 1 || player.getInventory().checkItem(40647))
				htmlid = "resta1c";
			else if(player.getQuest().getStep(L1Quest.QUEST_SIMIZZ) == 2)
				htmlid = "resta1b";
			break;
		case 71061:
			if(player.getQuest().getStep(L1Quest.QUEST_CADMUS) == L1Quest.QUEST_END)
				htmlid = "cadmus1c";
			else if(player.getQuest().getStep(L1Quest.QUEST_CADMUS) == 3)
				htmlid = "cadmus8";
			else if(player.getQuest().getStep(L1Quest.QUEST_CADMUS) == 2)
				htmlid = "cadmus1a";
			else if(player.getQuest().getStep(L1Quest.QUEST_DOIL) == L1Quest.QUEST_END)
				htmlid = "cadmus1b";
			break;
		case 71036:
			if(player.getQuest().getStep(L1Quest.QUEST_KAMYLA) == L1Quest.QUEST_END)htmlid = "kamyla26";
			else if(player.getQuest().getStep(L1Quest.QUEST_KAMYLA) == 4 && player.getInventory().checkItem(40717))htmlid = "kamyla15";
			else if(player.getQuest().getStep(L1Quest.QUEST_KAMYLA) == 4)htmlid = "kamyla14";
			else if(player.getQuest().getStep(L1Quest.QUEST_KAMYLA) == 3 && player.getInventory().checkItem(40630))htmlid = "kamyla12";
			else if(player.getQuest().getStep(L1Quest.QUEST_KAMYLA) == 3)htmlid = "kamyla11";
			else if(player.getQuest().getStep(L1Quest.QUEST_KAMYLA) == 2 && player.getInventory().checkItem(40644))htmlid = "kamyla9";
			else if(player.getQuest().getStep(L1Quest.QUEST_KAMYLA) == 1)htmlid = "kamyla8";
			else if(player.getQuest().getStep(L1Quest.QUEST_CADMUS) == L1Quest.QUEST_END && player.getInventory().checkItem(40621))htmlid = "kamyla1";
			break;
		case 71089:
			if(player.getQuest().getStep(L1Quest.QUEST_KAMYLA) == 2)htmlid = "francu12";
			break;
		case 71090:
			if(player.getQuest().getStep(L1Quest.QUEST_CRYSTAL) == 1 && player.getInventory().checkItem(40620))		htmlid = "jcrystal2";
			else if(player.getQuest().getStep(L1Quest.QUEST_CRYSTAL) == 1)											htmlid = "jcrystal3";
			break;
		case 71091:
			if(player.getQuest().getStep(L1Quest.QUEST_CRYSTAL) == 2 && player.getInventory().checkItem(40654))		htmlid = "jcrystall2";
			break;
		case 71074:
			if(player.getQuest().getStep(L1Quest.QUEST_LIZARD) == L1Quest.QUEST_END)								htmlid = "lelder1"; // 퀘스트 무한 재생	// htmlid = "lelder0";
			else if(player.getQuest().getStep(L1Quest.QUEST_LIZARD) == 3 && player.getInventory().checkItem(40634))	htmlid = "lelder12";
			else if(player.getQuest().getStep(L1Quest.QUEST_LIZARD) == 3)											htmlid = "lelder11";
			else if(player.getQuest().getStep(L1Quest.QUEST_LIZARD) == 2 && player.getInventory().checkItem(40633))	htmlid = "lelder7";
			else if(player.getQuest().getStep(L1Quest.QUEST_LIZARD) == 2)											htmlid = "lelder7b";
			else if(player.getQuest().getStep(L1Quest.QUEST_LIZARD) == 1)											htmlid = "lelder7b";
			else if(player.getLevel() >= 40)																		htmlid = "lelder1";
			break;
		case 71076:
			if(player.getQuest().getStep(L1Quest.QUEST_LIZARD) == L1Quest.QUEST_END)htmlid = "ylizardb";
			break;
		case 70840: // 달의장궁 로빈후드
			if(player.getQuest().getStep(L1Quest.QUEST_MOONBOW) == 0)		htmlid = "robinhood1";
			else if(player.getQuest().getStep(L1Quest.QUEST_MOONBOW) == 1)	htmlid = "robinhood8";
			else if(player.getQuest().getStep(L1Quest.QUEST_MOONBOW) == 2)	htmlid = "robinhood13";
			else if(player.getQuest().getStep(L1Quest.QUEST_MOONBOW) == 6)	htmlid = "robinhood9";
			else if(player.getQuest().getStep(L1Quest.QUEST_MOONBOW) == 7)	htmlid = "robinhood11";
			else															htmlid = "robinhood3";
			break;
		case 600005: // 달의장궁 지브릴
			if(player.getQuest().getStep(L1Quest.QUEST_MOONBOW) == 2)		htmlid = "zybril1";
			else if(player.getQuest().getStep(L1Quest.QUEST_MOONBOW) == 3)	htmlid = "zybril7";
			else if(player.getQuest().getStep(L1Quest.QUEST_MOONBOW) == 4)	htmlid = "zybril8";
			else if(player.getQuest().getStep(L1Quest.QUEST_MOONBOW) == 5)	htmlid = "zybril18";
			else															htmlid = "zybril16";
			break;
		case 71168: // 진명황 단테스
			if (player.getInventory().checkItem(41028)) {
				htmlid = "dantes1";
			}
			break;
		case 71180: // 제이프
			htmlid = player.getGender() == Gender.MALE ? "jp1" : "jp3";
			break;
		case 80079:
			if (player.getQuest().getStep(L1Quest.QUEST_KEPLISHA) == L1Quest.QUEST_END && !player.getInventory().checkItem(41312)) {
				htmlid = "keplisha6";
			} else {
				if (player.getInventory().checkItem(41314)) {
					htmlid = "keplisha3";
				} else if (player.getInventory().checkItem(41313)) {
					htmlid = "keplisha2";
				} else if (player.getInventory().checkItem(41312)) {
					htmlid = "keplisha4";
				}
			}
			break;
		case 71167:
			if (player.getSpriteId() == 3887) {
				htmlid = "frim1";
			}
			break;
		case 71141:
			if (player.getSpriteId() == 3887) {
				htmlid = "moumthree1";
			}
			break;
		case 71142:
			if (player.getSpriteId() == 3887) {
				htmlid = "moumtwo1";
			}
			break;
		case 71145:
			if (player.getSpriteId() == 3887) {
				htmlid = "moumone1";
			}
			break;
		case 71198:
			if (player.getQuest().getStep(71198) == 1) {
				htmlid = "tion4";
			} else if (player.getQuest().getStep(71198) == 2) {
				htmlid = "tion5";
			} else if (player.getQuest().getStep(71198) == 3) {
				htmlid = "tion6";
			} else if (player.getQuest().getStep(71198) == 4) {
				htmlid = "tion7";
			} else if (player.getQuest().getStep(71198) == 5) {
				htmlid = "tion5";
			} else if (player.getInventory().checkItem(21059, 1)) {
				htmlid = "tion19";
			}
			break;
		case 71199:
			if (player.getQuest().getStep(71199) == 1) {
				htmlid = "jeron3";
			} else if (player.getInventory().checkItem(21059, 1) || player.getQuest().getStep(71199) == 255) {
				htmlid = "jeron7";
			}
			break;
		case 6000015:// 마야의 그림자
			htmlid = player.getInventory().checkItem(41158) ? "adenshadow1" : "adenshadow2";
			break;
		case 7: // 아놀드
			htmlid = player.getLevel() >= 52 ? "anold1" : "anold2";
			break;
		// 에킨스
		case 7200000:
			if (player.getLevel() >= 52) {
				htmlid = player.getInventory().checkItem(31088, 1) || player.getInventory().checkItem(810001, 1) || player.getInventory().checkItem(810002, 1) ? "ekins2" : "ekins1";
			} else {
				htmlid = "ekins3";
			}
			break;
		case 81200:
			if (player.getInventory().checkItem(21069) || player.getInventory().checkItem(21074)) {
				htmlid = "c_belt";
			}
			break;
	/*	case 900135:// 유리에
			htmlid = player.getInventory().checkItem(410096, 1) || player.getInventory().checkItem(410096, 1) ? "j_html00" : "j_html01";
			break;*/
		case 80076: // 넘어진 항해사
			if (player.getInventory().checkItem(41058)) {// 완성한 항해 일지
				htmlid = "voyager8";
			} else if (player.getInventory().checkItem(49082) // 미완성의 항해 일지
					|| player.getInventory().checkItem(49083)) {
				// 페이지를 추가하고 있지 않는 상태
				htmlid = (player.getInventory().checkItem(41038) // 항해 일지 1 페이지
						|| player.getInventory().checkItem(41039) // 항해 일지 2 페이지
						|| player.getInventory().checkItem(41039) // 항해 일지 3 페이지
						|| player.getInventory().checkItem(41039) // 항해 일지 4 페이지
						|| player.getInventory().checkItem(41039) // 항해 일지 5 페이지
						|| player.getInventory().checkItem(41039) // 항해 일지 6 페이지
						|| player.getInventory().checkItem(41039) // 항해 일지 7 페이지
						|| player.getInventory().checkItem(41039) // 항해 일지 8 페이지
						|| player.getInventory().checkItem(41039) // 항해 일지 9 페이지
						|| player.getInventory().checkItem(41039)) // 항해 일지 10 페이지
					? "voyager9" : "voyager7";
			} else if (player.getInventory().checkItem(49082) // 미완성의 항해 일지
					|| player.getInventory().checkItem(49083) || player.getInventory().checkItem(49084)
					|| player.getInventory().checkItem(49085)
					|| player.getInventory().checkItem(49086) || player.getInventory().checkItem(49087)
					|| player.getInventory().checkItem(49088)
					|| player.getInventory().checkItem(49089) || player.getInventory().checkItem(49090) || player.getInventory().checkItem(49091)) {// 페이지를 추가한 상태
				// 페이지를 추가한 상태
				htmlid = "voyager7";
			}
			break;
		case 205: // 테베 오시리스의 제단 문지기
			/** 보스 공략 시간이 아니라면 */
			if (CrockController.getInstance().isKillBoss()) {
				htmlid = "thebegate5";
			} else if (!CrockController.getInstance().isBoss()) {
				htmlid = "tebegate2";
			} else {/** 보스 공략 시간이라면 */
				if (!player.getInventory().checkItem(100036, 1)) {
					htmlid = "tebegate3";//열쇠가 없다면
				} else if (CrockController.getInstance().size() >= 20) {
					htmlid = "tebegate4";//선착순 인원이 다 찼다면
				} else {
					htmlid = "tebegate1";//만족
				}
			}
			break;
		case 500063: // 티칼 쿠쿨칸
			if (CrockController.getInstance().isKillBoss()) {
				htmlid = "tikalgate5";
			} else if (!CrockController.getInstance().isBoss()) {
				htmlid = "tikalgate2";
			} else if (CrockController.getInstance().isBoss()){
				if (!player.getInventory().checkItem(500210, 1)) {
					htmlid = "tikalgate3";
				} else if (CrockController.getInstance().size() >= 20) {
					htmlid = "tikalgate4";
				} else {
					htmlid = "tikalgate1";
				}
			}
			break;
		case 50112: // 세리안
			if (player.isCrown() || player.isWizard() || player.isDragonknight()) {
				int talk_step = quest.getStep(L1Quest.QUEST_FIRSTQUEST);
				if (talk_step == 1) {
					htmlid = player.getLevel() >= 5 ? "orenb4" : "orenb14";
				} else if (talk_step == 255) {
					htmlid = "orenb11";
				}
			} else {
				htmlid = "orenb12";
			}
			break;
		case 50113: // 레크만
			if (player.isKnight() || player.isElf() || player.isDarkelf() || player.isIllusionist()) {
				int talk_step = quest.getStep(L1Quest.QUEST_FIRSTQUEST);
				if (talk_step == 1) {
					htmlid = player.getLevel() >= 5 ? "orena4" : "orena14";
				} else if (talk_step == 255) {
					htmlid = "orena11";
				}
			} else {
				htmlid = "orena12";
			}
			break;
		case 80067: // 첩보원(욕망의 동굴)
			if (player.getQuest().getStep(L1Quest.QUEST_DESIRE) == L1Quest.QUEST_END) {
				htmlid = "minicod10";
			} else if (player.getKarmaLevel() >= 1) {
				htmlid = "minicod07";
			} else if (player.getQuest().getStep(L1Quest.QUEST_DESIRE) == 1 && player.getSpriteId() == 6034) {// 코라프프리스트 변신
				htmlid = "minicod03";
			} else if (player.getQuest().getStep(L1Quest.QUEST_DESIRE) == 1 && player.getSpriteId() != 6034) {
				htmlid = "minicod05";
			} else if (player.getQuest().getStep(L1Quest.QUEST_SHADOWS) == L1Quest.QUEST_END || player.getInventory().checkItem(41121) || player.getInventory().checkItem(41122)) {// 카헬의 명령서
				htmlid = "minicod01";
			} else if (player.getInventory().checkItem(41130) && player.getInventory().checkItem(41131)) {// 핏자국의 명령서
				htmlid = "minicod06";
			} else if (player.getInventory().checkItem(41130)) {// 핏자국의 명령서
				htmlid = "minicod02";
			}
			break;
		case 4201000: // 환술사 아샤
			htmlid = player.isIllusionist() ? "asha1" : "asha2";
			break;
		case 4202000:
			htmlid = player.isDragonknight() ? "feaena1" : "feaena2";
			break;
		case 3200021:
			if (player.isElf()) {
				if (player.getQuest().getStep(L1Quest.QUEST_MOONBOW) == 0) {
					htmlid = "robinhood1";
				} else if (player.getQuest().getStep(L1Quest.QUEST_MOONBOW) == 1) {
					htmlid = "robinhood8";
				} else if (player.getQuest().getStep(L1Quest.QUEST_MOONBOW) == 2) {
					htmlid = "robinhood13";
				} else if (player.getQuest().getStep(L1Quest.QUEST_MOONBOW) == 6) {
					htmlid = "robinhood9";
				} else if (player.getQuest().getStep(L1Quest.QUEST_MOONBOW) == 7) {
					htmlid = "robinhood11";
				} else {
					htmlid = "robinhood3";
				}
			}
			break;
		case 3200022:
			if (player.isElf()) {
				if (player.getQuest().getStep(L1Quest.QUEST_MOONBOW) == 2) {
					htmlid = "zybril1";
				} else if (player.getQuest().getStep(L1Quest.QUEST_MOONBOW) == 3) {
					htmlid = "zybril7";
				} else if (player.getQuest().getStep(L1Quest.QUEST_MOONBOW) == 4) {
					htmlid = "zybril8";
				} else if (player.getQuest().getStep(L1Quest.QUEST_MOONBOW) == 5) {
					htmlid = "zybril18";
				} else {
					htmlid = "zybril16";
				}
			}
			break;
		case 81202: // 첩보원(그림자의 신전)
			if (player.getQuest().getStep(L1Quest.QUEST_SHADOWS) == L1Quest.QUEST_END) {
				htmlid = "minitos10";
			} else if (player.getKarmaLevel() <= -1) {
				htmlid = "minitos07";
			} else if (player.getQuest().getStep(L1Quest.QUEST_SHADOWS) == 1 && player.getSpriteId() == 6035) {
				htmlid = "minitos03";
			} else if (player.getQuest().getStep(L1Quest.QUEST_SHADOWS) == 1 && player.getSpriteId() != 6035) {
				htmlid = "minitos05";
			} else if (player.getQuest().getStep(L1Quest.QUEST_DESIRE) == L1Quest.QUEST_END || player.getInventory().checkItem(41130) || player.getInventory().checkItem(41131)) {// 핏자국의 명령서
				htmlid = "minitos01";
			} else if (player.getInventory().checkItem(41121) && player.getInventory().checkItem(41122)) {
				htmlid = "minitos06";
			} else if (player.getInventory().checkItem(41121)) {
				htmlid = "minitos02";
			}
			break;
		case 81208: // 더러워진 브롭브
			if (player.getInventory().checkItem(41129) || player.getInventory().checkItem(41138)) {
				htmlid = "minibrob04";
			} else if (player.getInventory().checkItem(41126) // 핏자국의 타락 한 정수
					&& player.getInventory().checkItem(41127) // 핏자국의 무력한 정수
					&& player.getInventory().checkItem(41128) // 핏자국의 아집인 정수
					|| player.getInventory().checkItem(41135) // 카헬의 타락 한 정수
					&& player.getInventory().checkItem(41136) // 카헬의 아집인 정수
					&& player.getInventory().checkItem(41137)) {// 카헬의 아집인 정수
				htmlid = "minibrob02";
			}
			break;

		case 5000006:
			//멘트(player);
			ment(player);
			break;
		case 4200018:// 경험치지급
			//경험치멘트(player);
			experienceMent(player);
			break;
		case 777849: // 킬톤 (호랑이 사육)
			if (player.getInventory().checkItem(87050)) {
				htmlid = "killton2";
			}
			break;
		case 777848: // 메린 (진돗개 사육)
			if (player.getInventory().checkItem(87051)) {
				htmlid = "merin2";
			}
			break;
		case 900015: // 숨겨진 용들의 땅 입구(노랑포탈)
			if(player.getLevel() >= 30 && player.getLevel() <= 51)	htmlid = "dsecret2";
			else if(player.getLevel() > 51)							htmlid = "dsecret1";
			else													htmlid = "dsecret3";
			break;
		// 숨겨진 계곡 리뉴얼
		case 9274: // 초보자 도우미
			if(player.getLevel() < 2)									player.setExp(ExpTable.getExpByLevel(2));
			else if(player.getLevel() >= 5 && player.getLevel() <= 9)	htmlid = "tutorrw3";
			else if(player.getLevel() >= 10 && player.getLevel() <= 14)	htmlid = "tutorrw2";
			else if(player.getLevel() > 14)								htmlid = "tutorrw1";
			if(!player.getQuest().isEnd(L1Quest.QUEST_HIDDENVALLEY) && player.getLevel() > 9){
				player.getQuest().setEnd(L1Quest.QUEST_HIDDENVALLEY);
				player.setExp(player.getExp() + 10000);
				player.getInventory().createItem(desc, L1ItemId.SURYUNJA_WEAPON_SCROLL, 1, 0); // 상아탑의 무기 마법 주문서
				player.getInventory().createItem(desc, L1ItemId.SURYUNJA_ARMOR_SCROLL, 4, 0); // 상아탑의 갑옷 마법 주문서
				htmlid = "tutorrw9";
			}
			if(player.getLevel() < 52){
				L1BuffUtil.skillArrayAction(player, L1SkillInfo.HIDDEN_VALIGE_BUFF_ARRAY);
				player.setCurrentHp(player.getMaxHp());
				player.setCurrentMp(player.getMaxMp());
			}
			break;
		case 9275: // 수련장 관리인
			if(player.getLevel() < 2)									htmlid = "adminrw3";
			else if(player.getLevel() == 2)								player.setExp(ExpTable.getExpByLevel(3));
			else if(player.getLevel() >= 3 && player.getLevel() <= 4)	htmlid = "adminrw1";
			else if (player.getLevel() >= 5){
				if(player.getLevel() == 5){
					player.setExp(player.getExp() + 1000);
					htmlid = "adminrw2";
				}else
					htmlid = "adminrw4";
			}
			break;
		case 9273: // 페이하스
			if(player.isElf())											htmlid = player.getLevel() > 7 ? "siriss" : "sirissnt";
			else if(player.isWizard())									htmlid = player.getLevel() > 3 ? "siriss" : "sirissnt";
			else if(player.isDarkelf())									htmlid = player.getLevel() > 11 ? "siriss" : "sirissnt";
			else if(player.isCrown())									htmlid = player.getLevel() > 9 ? "siriss" : "sirissnt";
			else if(player.isDragonknight() || player.isIllusionist())	htmlid = "sirissnw";
			else if(player.isKnight() || player.isWarrior())			htmlid = "sirissnw";
			break;
		case 900185: // 최종 훈련 심사관
			if(player.getLevel() < 52)htmlid = "highpass42"; // 은기사 마을에 있는 '기초 훈련 교관'에게 훈련을 모두 마쳤다면, 52레벨이 된 후 다시 찾아오도록 하게.
			else{
				if(player.getQuest().isEnd(L1Quest.QUEST_HIGHPASS))
					htmlid = "highpass43";
				else if(player.getQuest().isEnd(L1Quest.QUEST_HPASS)){
					int itemId = 0;
					if(player.isElf())				itemId = 1107; // 엘모어 보우건
					else if(player.isWizard())		itemId = 1103; // 엘모어 지팡이
					else if(player.isDragonknight())itemId = 1104; // 엘모어 체인소드
					else if(player.isDarkelf())		itemId = 1105; // 엘모어 크로우
					else if(player.isIllusionist())	itemId = 1106; // 엘모어 키링크
					else if(player.isWarrior())		itemId = 203009; // 엘모어 도끼
					else							itemId = 1101; // 엘모어 한손검
					player.getInventory().createItem(desc, 60032, 1, 0); // 낡은고서
					player.getInventory().createItem(desc, 1000004, 3, 0); // 드래곤의 다이아몬드
					player.getInventory().createItem(desc, itemId, 1, 0);
					player.getInventory().consumeItem(L1ItemId.BASE_TRAINING_TOKEN, 1); // 기초 수련 증표 삭제
					player.getQuest().setEnd(L1Quest.QUEST_HIGHPASS);
					player.sendPackets(new S_Effect(player.getId(), 8688), true);
					htmlid = "highpass40";
				}else
					htmlid = "highpass41";
			}
			break;
		case 900187: // 토벌 대원 [ 일일 퀘스트 ]
			if(player.getLevel() < 15)htmlid = "highdaily0";
			else if(player.getLevel() >= 15 && player.getLevel() <= 44){
				if(player.getQuest().getStep(L1Quest.QUEST_HIGHDAILY) == 15)htmlid = "highdaily30";
				else if(player.getQuest().getStep(L1Quest.QUEST_HIGHDAILY) >= 1 && !player.getInventory().checkItem(L1ItemId.PUNITIVE_EXPEDITION_BEAD, 1))
					htmlid = "highdaily10";
				else if(player.getQuest().getStep(L1Quest.QUEST_HIGHDAILY) == 1 || player.getQuest().getStep(L1Quest.QUEST_HIGHDAILY) == 3
						|| player.getQuest().getStep(L1Quest.QUEST_HIGHDAILY) == 5 || player.getQuest().getStep(L1Quest.QUEST_HIGHDAILY) == 7
						|| player.getQuest().getStep(L1Quest.QUEST_HIGHDAILY) == 9 || player.getQuest().getStep(L1Quest.QUEST_HIGHDAILY) == 11
						|| player.getQuest().getStep(L1Quest.QUEST_HIGHDAILY) == 13)
					htmlid = "highdaily2";
				else if(player.getQuest().getStep(L1Quest.QUEST_HIGHDAILY) == 2 || player.getQuest().getStep(L1Quest.QUEST_HIGHDAILY) == 4
						|| player.getQuest().getStep(L1Quest.QUEST_HIGHDAILY) == 6 || player.getQuest().getStep(L1Quest.QUEST_HIGHDAILY) == 8
						|| player.getQuest().getStep(L1Quest.QUEST_HIGHDAILY) == 10 || player.getQuest().getStep(L1Quest.QUEST_HIGHDAILY) == 12
						|| player.getQuest().getStep(L1Quest.QUEST_HIGHDAILY) == 14)
					htmlid = "highdaily6";
				else
					htmlid = "highdaily1";
			}else if(player.getLevel() > 44)
				htmlid = player.getQuest().getStep(L1Quest.QUEST_HIGHDAILY) == 15 ? "highdaily30" : "highdaily3";
			break;
		case 900188: // 드래곤뼈 수집가 [ 일일 퀘스트 ]
			if(player.getLevel() < 45)htmlid = "highdailyb0";
			else if(player.getLevel() >= 45 && player.getLevel() <= 51){
				if(player.getQuest().getStep(L1Quest.QUEST_HIGHDAILYB) == 15)
					htmlid = "highdailyb30";
				else if (player.getQuest().getStep(L1Quest.QUEST_HIGHDAILYB) >= 1 && !player.getInventory().checkItem(L1ItemId.DRAGON_BONE_BEAD, 1))
					htmlid = "highdailyb10";
				else if (player.getQuest().getStep(L1Quest.QUEST_HIGHDAILYB) == 1 || player.getQuest().getStep(L1Quest.QUEST_HIGHDAILYB) == 3
						|| player.getQuest().getStep(L1Quest.QUEST_HIGHDAILYB) == 5 || player.getQuest().getStep(L1Quest.QUEST_HIGHDAILYB) == 7
						|| player.getQuest().getStep(L1Quest.QUEST_HIGHDAILYB) == 9 || player.getQuest().getStep(L1Quest.QUEST_HIGHDAILYB) == 11
						|| player.getQuest().getStep(L1Quest.QUEST_HIGHDAILYB) == 13)
					htmlid = "highdailyb2";
				else if (player.getQuest().getStep(L1Quest.QUEST_HIGHDAILYB) == 2 || player.getQuest().getStep(L1Quest.QUEST_HIGHDAILYB) == 4
						|| player.getQuest().getStep(L1Quest.QUEST_HIGHDAILYB) == 6 || player.getQuest().getStep(L1Quest.QUEST_HIGHDAILYB) == 8
						|| player.getQuest().getStep(L1Quest.QUEST_HIGHDAILYB) == 10 || player.getQuest().getStep(L1Quest.QUEST_HIGHDAILYB) == 12
						|| player.getQuest().getStep(L1Quest.QUEST_HIGHDAILYB) == 14)
					htmlid = "highdailyb6";
				else
					htmlid = "highdailyb1";
			}else if(player.getLevel() > 51)
				htmlid = player.getQuest().getStep(L1Quest.QUEST_HIGHDAILYB) == 15 ? "highdailyb30" : "highdailyb3";
			break;
		case 900186: // 기초 훈련 교관 [ 퀘스트 ]
			switch (player.getQuest().getStep(L1Quest.QUEST_HPASS)) {
			case 1:htmlid = player.getLevel() > 19 ? "hpass2" : "hpass8";break;
			case 2:htmlid = player.getLevel() > 24 ? "hpass3" : "hpass8";break;
			case 3:htmlid = player.getLevel() > 29 ? "hpass4" : "hpass8";break;
			case 4:htmlid = player.getLevel() > 34 ? "hpass5" : "hpass8";break;
			case 5:htmlid = player.getLevel() > 44 ? "hpass6" : "hpass8";break;
			case 255:htmlid = "hpass7";break;
			default:htmlid = player.getLevel() < 15 ? "hpass8" : "hpass1";break;
			}
			break;
		case 5062: // 지그프리드
			htmlid = player.getLevel() >= Config.DUNGEON.LASTAVAD_LIMIT_LEVEL ? "zigpride1" : "zigpride2";
			break;
		case 9134:
			htmlid = player.getLevel() < 52 ? "marbinquestA" : player.getInventory().checkItem(46115, 1) ? "marbinquest3" : "marbinquest1";
			break;
		case 5088: // 보석세공사 데이빗(얼녀귀걸이)
			if (player.getInventory().checkItem(49031)) {
				if(player.getInventory().checkItem(21081))							htmlid = "gemout1";
				else if(player.getQuest().getStep(L1Quest.QUEST_ICEQUEENRING) == 1)	htmlid = "gemout2";
				else if(player.getQuest().getStep(L1Quest.QUEST_ICEQUEENRING) == 2)	htmlid = "gemout3";
				else if(player.getQuest().getStep(L1Quest.QUEST_ICEQUEENRING) == 3)	htmlid = "gemout4";
				else if(player.getQuest().getStep(L1Quest.QUEST_ICEQUEENRING) == 4)	htmlid = "gemout5";
				else if(player.getQuest().getStep(L1Quest.QUEST_ICEQUEENRING) == 5)	htmlid = "gemout6";
				else if(player.getQuest().getStep(L1Quest.QUEST_ICEQUEENRING) == 6)	htmlid = "gemout7";
				else if(player.getQuest().getStep(L1Quest.QUEST_ICEQUEENRING) == 7)	htmlid = "gemout8";
				else																htmlid = "gemout17";
			}
			break;
		case 5092: // 엘핀 [ 요정의 숲 잡화상 ]
			htmlid = player.isElf() ? "elfin" : "elfin2";
			break;
		case 5093: // 엘리 [ 요정의 숲 수정 상인 ]
			htmlid = player.isElf() ? "elli" : "elli2";
			break;
		case 70842: // 마르바
			if(player.getAlignment() <= -501)				htmlid = "marba1";
			else if(!player.isElf())						htmlid = "marba2";
			else if(player.getInventory().checkItem(40665)
					&& (player.getInventory().checkItem(40693) || player.getInventory().checkItem(40694) || player.getInventory().checkItem(40695)
							|| player.getInventory().checkItem(40697) || player.getInventory().checkItem(40698) || player.getInventory().checkItem(40699)))
				htmlid = "marba8";
			else if(player.getInventory().checkItem(40665))	htmlid = "marba17";
			else if(player.getInventory().checkItem(40664))	htmlid = "marba19";
			else if(player.getInventory().checkItem(40637))	htmlid = "marba18";
			else											htmlid = "marba3";
			break;
		case 70854: // 후린달렌
			if(!player.isElf())	{
			if(player.isDarkelf())			htmlid = "hurinE3";
			else if(player.isDragonknight())htmlid = "hurinE4";
			else if(player.isIllusionist())	htmlid = "hurinE5";
			else							htmlid = "hurinM1";}
			break;
		case 70839: // 도에트
			if(!player.isElf())	{	
			if(player.isDarkelf())			htmlid = "doettM2";
			else if(player.isDragonknight())htmlid = "doettM3";
			else if(player.isIllusionist())	htmlid = "doettM4";
			else							htmlid = "doettM1";}
			break;
		case 70843: // 모리엔
			if(!player.isElf())	{
			if(player.isDarkelf())			htmlid = "morienM2";
			else if(player.isDragonknight())htmlid = "morienM3";
			else if(player.isIllusionist())	htmlid = "morienM4";
			else							htmlid = "morienM1";}
			break;
		case 70849: // 테오도르
			if(!player.isElf())	{
			if(player.isDarkelf())			htmlid = "theodorM2";
			else if(player.isDragonknight())htmlid = "theodorM3";
			else if(player.isIllusionist())	htmlid = "theodorM4";
			else							htmlid = "theodorM1";}
			break;
		case 5131: // 다크엘프 생존자
			htmlid = !player.isGhost() ? "exitkir1" : "exitkir";
			break;
		case 5133: // 감시자의 눈
			htmlid = !player.isGhost() ? "exitghostel1" : "exitghostel";
			break;
		case 71126: // 이리스
			htmlid = player.getLevel() < 55 ? "eris21" : "eris1";
			break;
		case 7210050: // 낡은 책 더미
			htmlid = player.getLevel() < 55 || player.getQuest().getStep(L1Quest.QUEST_RUN) == 1 ? "oldbook2" : "oldbook1";
			break;
		default:break;
		}
		// html 표시 패킷 송신
		if (htmlid != null) { // htmlid가 지정되고 있는 경우
			if (htmldata != null) {// html 지정이 있는 경우는 표시
				player.sendPackets(new S_NPCTalkReturn(objid, htmlid, htmldata), true);
			} else {
				player.sendPackets(new S_NPCTalkReturn(objid, htmlid), true);
			}
			if (player.isGm())
				player.sendPackets(new S_SystemMessage("Dialog " + htmlid), true);

		} else {
			player.sendPackets(new S_NPCTalkReturn(talking, objid, player.getAlignment() < -500 ? 2 : 1), true);
			if (player.isGm())
				player.sendPackets(new S_SystemMessage("Dialog " + talking.getCaoticAction() + " | " + talking.getNormalAction()), true);				
		}
	}

	private static String talkToTownadviser(L1PcInstance pc, int town_id) {
		return (pc.getHomeTownId() == town_id && TownTable.isLeader(pc, town_id)) ? "secretary1" : "secretary2";
	}

	private static String talkToTownmaster(L1PcInstance pc, int town_id) {
		return pc.getHomeTownId() == town_id ? "hometown" : "othertown";
	}

	@Override
	public void onFinalAction(L1PcInstance player, String action) {}

	public void doFinalAction(L1PcInstance player) {}

	private boolean checkHasCastle(L1PcInstance player, int castle_id) {
		if (player.getClanid() != 0) {
			L1Clan clan = player.getClan();
			if (clan != null && clan.getCastleId() == castle_id) {
				return true;
			}
		}
		return false;
	}

	private boolean checkClanLeader(L1PcInstance player) {
		if (player.isCrown()) {
			L1Clan clan = player.getClan();
			if (clan != null && player.getId() == clan.getLeaderId()) {
				return true;
			}
		}
		return false;
	}

	private int getNecessarySealCount(L1PcInstance pc) {
		int rulerCount = 0;
		if (pc.getInventory().checkItem(40917)) {
			rulerCount++;
		}
		if (pc.getInventory().checkItem(40920)) {
			rulerCount++;
		}
		if (pc.getInventory().checkItem(40918)) {
			rulerCount++;
		}
		if (pc.getInventory().checkItem(40919)) {
			rulerCount++;
		}
		if (rulerCount == 1) {
			return 100;
		}
		if (rulerCount == 2) {
			return 200;
		}
		if (rulerCount == 3) {
			return 500;
		}
		return 10;
	}

	private void ment(L1PcInstance pc) {
//AUTO SRM: 		pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "상인에게서 판매하는 무기는 확률적으로 획득 할 수 있습니다."), true); // CHECKED OK
		pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,S_SystemMessage.getRefText(1044)), true);
	}

	private void experienceMent(L1PcInstance pc) {
//AUTO SRM: 		pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "알림: [경험치 지급은 Lv.("+ Config.ALT.EXPERIENCE_RETURN_MAX_LEVEL +") 까지 가능합니다]"), true); // CHECKED OK
		pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,S_SystemMessage.getRefText(1045) + Config.ALT.EXPERIENCE_RETURN_MAX_LEVEL  + S_SystemMessage.getRefText(1046)), true);
//AUTO SRM: 		pc.sendPackets(new S_SystemMessage("\\aA알림: 경험치 지급은 \\aG[" + Config.ALT.EXPERIENCE_RETURN_MAX_LEVEL + "]\\aA 까지 가능합니다"), true); // CHECKED OK
		pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1038) + Config.ALT.EXPERIENCE_RETURN_MAX_LEVEL  + S_SystemMessage.getRefText(1039), true), true);
	}

	private void createRuler(L1PcInstance pc, int attr, int sealCount) {
		int rulerId = 0;
		int protectionId = 0;
		int sealId = 0;
		if (attr == 1) {
			rulerId = 40917;
			protectionId = 40909;
			sealId = 40913;
		} else if (attr == 2) {
			rulerId = 40919;
			protectionId = 40911;
			sealId = 40915;
		} else if (attr == 4) {
			rulerId = 40918;
			protectionId = 40910;
			sealId = 40914;
		} else if (attr == 8) {
			rulerId = 40920;
			protectionId = 40912;
			sealId = 40916;
		}
		pc.getInventory().consumeItem(protectionId, 1);
		pc.getInventory().consumeItem(sealId, sealCount);
		L1ItemInstance item = pc.getInventory().storeItem(rulerId, 1);
		if (item != null) {
			pc.sendPackets(new S_ServerMessage(143, getNpcTemplate().getDesc(), item.getLogNameRef()), true);
		}
	}

	private String talkToSIGuide(L1PcInstance pc) {
		if (pc.getLevel() < 3) {
			return "en0301";
		}
		if (pc.getLevel() < 7) {
			return "en0302";
		}
		if (pc.getLevel() < 9) {
			return "en0303";
		}
		if (pc.getLevel() < 12) {
			return "en0304";
		}
		if (pc.getLevel() < 13) {
			return "en0305";
		}
		if (pc.getLevel() < 25) {
			return "en0306";
		}
		return "en0307";
	}

	private String talkToPopirea(L1PcInstance pc) {
		if (pc.getLevel() < 25) {
			if (pc.getInventory().checkItem(41209) || pc.getInventory().checkItem(41210) || pc.getInventory().checkItem(41211) || pc.getInventory().checkItem(41212)) {
				return "jpe0043";
			}
			if (pc.getInventory().checkItem(41213)) {
				return "jpe0044";
			}
			return "jpe0041";
		}
		return "jpe0045";
	}

	private String talkToSecondtbox(L1PcInstance pc) {
		return pc.getQuest().getStep(L1Quest.QUEST_TBOX1) == L1Quest.QUEST_END && pc.getInventory().checkItem(40701) ? "maptboxa" : "maptbox0";
	}

	private void DuelZone(L1PcInstance pc) {
		// 배틀존이 열려 있고 , 입장이 가능하다면
		if (BattleZone.getInstance().getEntryStatus()) {
			if (pc.getConfig().getDuelLine() != 0 || BattleZone.getInstance().isBattleZoneUser(pc)) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("배틀존에서 나왔다가 다시 들어갈 수 없습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1040), true), true);
				return;
			}
			if (BattleZone.getInstance().getBattleZoneUserCount() > 50) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("배틀존의 인원이 모두 찼습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1041), true), true);
				return;
			}
			if (pc.isInParty()) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("파티중에는 배틀존 입장이 불가능합니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1042), true), true);
				return;
			}
			// 라인을 나누자..
			pc.getConfig().setDuelLine(BattleZone.getInstance().getBattleZoneUserCount() % 2 == 0 ? 2 : 1);
			BattleZone.getInstance().addBattleZoneUser(pc);
			if (pc.getConfig().getDuelLine() == 1) {
				int ranx = 32628 + CommonUtil.random(4);
				int rany = 32896 + CommonUtil.random(5);
				pc.getTeleport().start(ranx, rany, (short) 5153, 1, true);
			} else {
				int ranx2 = 32650 - CommonUtil.random(4);
				int rany2 = 32893 + CommonUtil.random(5);
				pc.getTeleport().start(ranx2, rany2, (short) 5153, 5, true);
			}
		} else {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("현재 배틀존이 열리지 않았습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1043), true), true);
		}
	}

	private String talkToThirdtbox(L1PcInstance pc) {
		return pc.getQuest().getStep(L1Quest.QUEST_TBOX2) == L1Quest.QUEST_END && pc.getInventory().checkItem(40701) ? "maptboxd" : "maptbox0";
	}

	private static final long REST_MILLISEC = 10000;

	private AtomicInteger _restCallCount;
	
	private void talkChangeHead(L1PcInstance pc){
		int pcX = pc.getX();
		int pcY = pc.getY();
		int npcX = getX();
		int npcY = getY();
		if (pcX == npcX && pcY < npcY) {
			getMoveState().setHeading(0);
		} else if (pcX > npcX && pcY < npcY) {
			getMoveState().setHeading(1);
		} else if (pcX > npcX && pcY == npcY) {
			getMoveState().setHeading(2);
		} else if (pcX > npcX && pcY > npcY) {
			getMoveState().setHeading(3);
		} else if (pcX == npcX && pcY > npcY) {
			getMoveState().setHeading(4);
		} else if (pcX < npcX && pcY > npcY) {
			getMoveState().setHeading(5);
		} else if (pcX < npcX && pcY == npcY) {
			getMoveState().setHeading(6);
		} else if (pcX < npcX && pcY < npcY) {
			getMoveState().setHeading(7);
		}
		broadcastPacket(new S_ChangeHeading(this), true);

		if (_restCallCount.getAndIncrement() == 0) {
			setRest(true);
		}
		GeneralThreadPool.getInstance().schedule(new RestMonitor(), REST_MILLISEC);
	}

	public class RestMonitor implements Runnable {
		@Override
		public void run() {
			if (_restCallCount.decrementAndGet() == 0) {
				setRest(false);
			}
		}
	}
}


