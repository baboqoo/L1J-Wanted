package l1j.server.server.clientpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.arca.L1Arca;
import l1j.server.GameSystem.arca.L1ArcaGrade;
import l1j.server.GameSystem.attendance.bean.AttendanceAccount;
import l1j.server.GameSystem.deathpenalty.DeathPenaltyTable;
import l1j.server.GameSystem.eventpush.EventPushLoader;
import l1j.server.GameSystem.freebuffshield.FreeBuffShieldLoader;
import l1j.server.GameSystem.huntingquest.HuntingQuestObject;
import l1j.server.GameSystem.huntingquest.HuntingQuestTable;
import l1j.server.GameSystem.huntingquest.user.HuntingQuestUser;
import l1j.server.GameSystem.huntingquest.user.HuntingQuestUserTable;
import l1j.server.GameSystem.huntingquest.user.HuntingQuestUserTemp;
import l1j.server.GameSystem.inn.InnHelper;
import l1j.server.GameSystem.inter.L1InterServerModel;
import l1j.server.GameSystem.shoplimit.ShopLimitLoader;
import l1j.server.IndunSystem.occupy.OccupyHandler;
import l1j.server.IndunSystem.occupy.OccupyManager;
import l1j.server.IndunSystem.occupy.OccupyType;
import l1j.server.IndunSystem.occupy.OccupyUtil;
import l1j.server.IndunSystem.treasureisland.TreasureIsland;
import l1j.server.LFCSystem.InstanceSpace;
import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.common.data.ePolymorphAnonymityType;
import l1j.server.server.Account;
import l1j.server.server.ActionCodes;
import l1j.server.server.GameClient;
import l1j.server.server.GameServer;
import l1j.server.server.GameServerSetting;
import l1j.server.server.command.executor.L1Invisible;
import l1j.server.server.construct.L1InterServer;
import l1j.server.server.construct.L1Status;
import l1j.server.server.construct.message.L1GreenMessage;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.controller.LoginController;
import l1j.server.server.controller.action.UserRanking;
import l1j.server.server.controller.action.War;
import l1j.server.server.datatables.CharBuffTable;
import l1j.server.server.datatables.CharBuffTable.BuffInfo;
import l1j.server.server.datatables.CharacterEinhasadStatTable;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ClanAttentionTable;
import l1j.server.server.datatables.EquipSetTable;
import l1j.server.server.datatables.FreePVPRegionTable;
import l1j.server.server.datatables.GetBackRestartTable;
import l1j.server.server.datatables.LetterTable;
import l1j.server.server.datatables.NotificationTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.datatables.SpamTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1CharacterConfig;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Cooking;
import l1j.server.server.model.L1ExcludingList;
import l1j.server.server.model.L1ExpPotion;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1War;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.shop.L1PersonalShop;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.model.skill.L1SkillDelay;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.model.skill.L1SkillUseType;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.S_BookMarkLoad;
import l1j.server.server.serverpackets.S_ChangeCharName;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_CharacterConfig;
import l1j.server.server.serverpackets.S_EnterWorldCheck;
import l1j.server.server.serverpackets.S_ExpBoostingInfo;
import l1j.server.server.serverpackets.S_Extended;
import l1j.server.server.serverpackets.S_FairlyConfig;
import l1j.server.server.serverpackets.S_FreePVPRegionNoti;
import l1j.server.server.serverpackets.S_Karma;
import l1j.server.server.serverpackets.S_LetterList;
import l1j.server.server.serverpackets.S_Liquor;
import l1j.server.server.serverpackets.S_LocRefresh;
import l1j.server.server.serverpackets.S_LoginUnknown;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_SiegeInjuryTimeNoti;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SpecialResistance;
import l1j.server.server.serverpackets.S_TeamIdServerNoMappingInfo;
import l1j.server.server.serverpackets.S_UnityIcon;
import l1j.server.server.serverpackets.S_War;
import l1j.server.server.serverpackets.S_Weather;
import l1j.server.server.serverpackets.S_WorldPut;
import l1j.server.server.serverpackets.action.S_Userform;
import l1j.server.server.serverpackets.alchemy.S_EnchantPotentialRestartNoti;
import l1j.server.server.serverpackets.einhasad.S_RestExpInfoNoti;
import l1j.server.server.serverpackets.einhasadpoint.S_EinhasadPointStatInfoNoti;
import l1j.server.server.serverpackets.equip.S_ExtendSlotInfo;
import l1j.server.server.serverpackets.gamegate.S_GameGatePCCafeCharge;
import l1j.server.server.serverpackets.gamegate.S_UserStartSundry;
import l1j.server.server.serverpackets.huntingquest.S_HuntingQuestMapInfo;
import l1j.server.server.serverpackets.huntingquest.S_HuntingQuestMapList;
import l1j.server.server.serverpackets.inventory.S_AddInventoryNoti;
import l1j.server.server.serverpackets.inventory.S_NotiToggleInfo;
import l1j.server.server.serverpackets.message.S_MessageYN;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.object.S_PCObject;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeEnterNoticeNoti;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeInfo;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeUserInfo;
import l1j.server.server.serverpackets.pledge.S_PledgeWatch;
import l1j.server.server.serverpackets.polymorph.S_PolymorphEvent;
import l1j.server.server.serverpackets.returnedstat.S_ReturnedStatLogin;
import l1j.server.server.serverpackets.returnedstat.S_ReturnedStatLoginUi;
import l1j.server.server.serverpackets.returnedstat.S_ReturnedStatStart;
import l1j.server.server.serverpackets.returnedstat.S_StatusBaseInfo;
import l1j.server.server.serverpackets.returnedstat.S_StatusBaseNoti;
import l1j.server.server.serverpackets.returnedstat.S_StatusCarryWeightInfoNoti;
import l1j.server.server.serverpackets.returnedstat.S_StatusRenewalInfo;
import l1j.server.server.serverpackets.returnedstat.S_TotalDrinkedElixirNoti;
import l1j.server.server.serverpackets.spell.S_AllSpellPassiveNoti;
import l1j.server.server.serverpackets.spell.S_AvailableSpellNoti;
import l1j.server.server.serverpackets.spell.S_ElfIcon;
import l1j.server.server.serverpackets.spell.S_MassTeleportState;
import l1j.server.server.serverpackets.spell.S_SkillIconGFX;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.serverpackets.spell.S_SpellDelay;
import l1j.server.server.serverpackets.spell.S_SpellLateHandlingNoti;
import l1j.server.server.serverpackets.system.S_PCMasterFavorUpdateNoti;
import l1j.server.server.templates.L1BookMark;
import l1j.server.server.templates.L1GetBackRestart;
import l1j.server.server.templates.L1PassiveSkills;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.CheckInitStat;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.FormatterUtil;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;
import l1j.server.server.utils.SystemUtil;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti.SkillIconNotiType;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti.SkillIconDurationShowType;
import l1j.server.server.GeneralThreadPool;
import java.lang.Math;
//import manager.Manager;  // MANAGER DISABLED

public class C_LoginToServer extends ClientBasePacket {
	private static final String C_LOGIN_TO_SERVER = "[C] C_LoginToServer";
	private static Logger _log = Logger.getLogger(C_LoginToServer.class.getName());
	private static final String CHANGE_CHAR_NAME_FLAG = "_L";
	
	private long sysTime = System.currentTimeMillis();
	private GameClient client;
	private L1InterServer inter;
	private Account account;
	private L1PcInstance pc;
	private L1CharacterConfig config;
	private L1Clan clan;
	private L1InterServerModel interModel;
	
	public C_LoginToServer(String charName, GameClient client) throws Exception {
		if (client == null) {
			return;
		}
		this.client = client;
		login(charName);
	}

	public C_LoginToServer(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);
		if (client == null) {
			return;
		}
		this.client = client;
		String charName = readS();
		if (charName.startsWith(CHANGE_CHAR_NAME_FLAG)) {
			client.sendPacket(S_ChangeCharName.CHAR_NAME_CHANGE_START);
			return;
		}
		login(charName);
	}
	
	public C_LoginToServer(GameClient client, L1InterServerModel model) {
		if (client == null) {
			return;
		}
		this.client		= client;
		this.interModel	= model;
		login(model.getCharName());
	}
	
	void validateFailPrint(String failMsg){
		System.out.println("─────────────────────────────────");
		System.out.println(failMsg);
		System.out.println("─────────────────────────────────");
		client.kick();
		client.close();
	}
	
	boolean validate(String accountName, String charName){
		if (client.getAccount() == null) {
			//validateFailPrint(String.format("계정이 없는 캐릭터 접속 시도 [%s]", charName));
			validateFailPrint(String.format("Attempting to connect to a character without an account [%s]", charName));
			return false;
		}

		if (client.getActiveChar() != null) {
			//validateFailPrint(String.format("동일 ID의 중복 접속이므로 [%s/%s]/의 접속을 강제 종료합니다.", client.getIp(), charName));
			validateFailPrint(String.format("Because it is a duplicate connection with the same ID, the connection to [%s/%s]/ will be forcibly terminated.", client.getIp(), charName));
			return false;
		}
		
		if (inter == null) {
			GameClient clientByAccount = LoginController.getInstance().getClientByAccount(accountName);
			if (clientByAccount == null || clientByAccount != client) {
				//validateFailPrint(String.format("동일 Account의 중복 접속이므로 [%s/%s]의 접속을 강제 종료합니다.", client.getIp(), charName));
				validateFailPrint(String.format("Because it is a duplicate connection from the same Account, the connection for [%s/%s] will be forcibly terminated.", client.getIp(), charName));
				return false;
			}
		}

		/** 2캐릭 버그 방지 Start */
		L1PcInstance otherPc = L1World.getInstance().getPlayer(charName);
		if (otherPc != null) {
			boolean isPrivateShop = otherPc.isPrivateShop();
			if (isPrivateShop) {
				L1PersonalShop.delete(otherPc);
			}
			boolean isAutoclanjoin = otherPc.isAutoClanjoin();
			GameServer.disconnectChar(otherPc);
			otherPc = null;
			if (!isPrivateShop && !isAutoclanjoin) {
				//validateFailPrint(String.format("동일 ID의 중복 접속이므로 [%s/%s]의 접속을 강제 종료합니다.", client.getIp(), charName));
				validateFailPrint(String.format("Because it is a duplicate connection with the same ID, the connection for [%s/%s] will be forcibly terminated.", client.getIp(), charName));
				return false;
			}
		}

		for (L1PcInstance bugpc : L1World.getInstance().getAllPlayers()) {
			if (bugpc.getAccountName().equals(client.getAccountName())
					&& ((!bugpc.isPrivateShop() && !bugpc.isAutoClanjoin()) || bugpc.getNetConnection() != null)) {
				//validateFailPrint(String.format("동일 Account의 중복 접속이므로 [%s/%s]의 접속을 강제 종료합니다.", client.getIp(), charName));
				validateFailPrint(String.format("Because it is a duplicate connection from the same Account, the connection for [%s/%s] will be forcibly terminated.", client.getIp(), charName));
				GameServer.disconnectChar(bugpc);
				return false;
			}
		}
		/** 2캐릭 버그 방지 End */

		if (pc == null || !accountName.equals(pc.getAccountName())) {
			//validateFailPrint(String.format("현재 계정에 없는 캐릭 접속시도 [%s/%s]", client.getAccountName(), charName));
			validateFailPrint(String.format("Attempt to connect to a character that is not in the current account [%s/%s]", client.getAccountName(), charName));
			return false;
		}

		if (!pc.isGm() && Config.SERVER.LEVEL_DOWN_RANGE != 0 && pc.getHighLevel() - pc.getLevel() >= Config.SERVER.LEVEL_DOWN_RANGE) {
			//validateFailPrint(String.format("렙다운 허용범위 초과 [%s/%s]", client.getAccountName(), charName));
			validateFailPrint(String.format("Level down allowed range exceeded [%s/%s]", client.getAccountName(), charName));
			return false;
		}
		return true;
	}

	void login(String charName) {
		try {
			boolean interServer		= client.isInterServer();
			inter					= client.getInter();
			account					= client.getAccount();
			pc						= L1PcInstance.load(charName);
			config					= pc.getConfig();
			
			// 유효성 체크
			if (!validate(client.getAccountName(), charName)) {
				return;
			}
			
			Calendar cal			= Calendar.getInstance();
			System.out.printf("[Connected] [%s] [%s] [%s] [%s] [Memory: %d] %s\n",
					FormatterUtil.get_formatter_time(),
					//cal.get(Calendar.HOUR), 		
					//String.format("%02d", cal.get(Calendar.MINUTE)), 
					//(cal.get(Calendar.AM_PM) == 0 ? "AM" : "PM"),										
					charName,
					client.getAccountName(),
					client.getIp(),
					SystemUtil.getUsedMemoryMB(),
					//(interServer ? "[인터서버:" + inter.name() + "]" : StringUtil.EmptyString));
					(interServer ? "[InterServer:" + inter.name() + "]" : StringUtil.EmptyString));

			// 로그 저장
			//LoggerInstance.getInstance().addConnection(String.format("접속 캐릭=%s 계정=%s IP=%s", charName, client.getAccountName(), client.getHostname()));			
			//System.out.printf("[Login Proc. 1] [%s]\n", FormatterUtil.get_formatter_time());
			LoggerInstance.getInstance().addConnection(String.format("Connection char=%s account=%s IP=%s", charName, client.getAccountName(), client.getHostname()));


			pc.setOnlineStatus(1);// 접속 상태 활성화
			CharacterTable.updateOnlineStatus(pc);
			
			L1World world = L1World.getInstance();
			world.storeObject(pc);

			// TODO 클라이언트 세팅
			pc.setNetConnection(client);
			pc.setAccount(account);
			client.setActiveChar(pc);
			
			pc.createDungeonTimer();// 던전 타이머 세팅
			
			if (Config.FATIGUE.FATIGUE_ACTIVE) {
				pc.getFatigue().login();// 그랑카인 세팅
			}
			
			pc.setShopLimit(ShopLimitLoader.getShopLimitFromCharacter(pc.getId()));
			
			pc.sendPackets(S_MassTeleportState.OFF);
			
			//pc.sendPackets(S_LoginUnknown.LOGIN_UNKNOWN);// 확인 필요

			pc.sendPackets(S_EnterWorldCheck.ENTER_WORLD);// S_ENTER_WORLD_CHECK

			if (Config.SERVER.CHARACTER_CONFIG_IN_SERVER_SIDE) {
				pc.sendPackets(new S_CharacterConfig(pc.getId()), true);
			}
			if (pc.getSpecialWareHouseSize() > 0) {
				pc.sendPackets(new S_PacketBox(S_PacketBox.CHARACTER_WHEREHOUSE_SIZE, pc.getSpecialWareHouseSize()), true);
			}
			
			pc.createTimeCollection();// 셀렉티스 전시회 세팅
			
			CharacterEinhasadStatTable.getInstance().login(pc);// 아인하사드 스탯
			pc.sendPackets(new S_EinhasadPointStatInfoNoti(pc, pc.getAbility()), true);// 아인하사드스탯
			
			if (Config.QUEST.HUNTING_QUEST_ACTIVE) {
				huntingQuestLogin();// 사냥터 도감
			}
			pc.sendPackets(S_NotiToggleInfo.DEFAULT_FAITH_OF_HALPAH);
			
			DeathPenaltyTable.login(pc);// 사망 패털티 정보
			
			pc.createEinhasadFaith();// 아인하사드의 신의 정보
			
			pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UPDATE, pc.getAbility().getBaseEr()), true);
			pc.sendPackets(new S_OwnCharAttrDef(pc), true);
			pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UPDATE, pc.getAbility().getEr()), true);
			
			// 창기사 원거리 폼
			if (pc.isLancer() && pc.getCurrentWeapon() == ActionCodes.ACTION_WalkSpear) {
				pc._isLancerForm = true;
				pc.sendPackets(S_Userform.LONG);
				pc.broadcastPacketWithMe(new S_CharVisualUpdate(pc), true);
				pc.sendPackets(S_Userform.REFRESH);
			}
			
			pc.createFavorBook();// 성물 인벤토리 세팅
			
			loadItems(false);
			sendItemPacket();
	        loadItems(true);
	        
	        // 아이템 로드 후 착용한 무기가 없다면 무기상태 default
	        if (pc.getWeapon() == null) {
	        	pc.setCurrentWeapon(0);
	        }
			
	        pc.sendPackets(new S_PacketBox(S_PacketBox.ATTACK_RANGE, pc, pc.getWeapon()), true);
			
			pc.sendPackets(new S_ExtendSlotInfo(pc, true, 0), true);	// 개방 슬롯
			pc.sendPackets(S_ExtendSlotInfo.REFRESH);
			
			//pc.sendPackets(S_ReturnedStatLoginUi.LOGIN_UI4);
			pc.sendPackets(S_ReturnedStatLoginUi.LOGIN_UI5);
			
			if (pc.isElf()) {
				loadAttr();// 정령속성
			}
			loadSkills();// 스킬로드
			
			pc.sendPackets(S_PacketBox.WORLD_MAP_LOGIN);

			L1BookMark.bookmarkDB(pc);
			pc.sendPackets(new S_BookMarkLoad(pc), true);
			pc.sendPackets(new S_PacketBox(S_PacketBox.WEIGHT, pc.getInventory().getWeightPercent()), true);

			// 좌표 지정
			if (interModel != null) {
				locSet(interModel.getInterX(), interModel.getInterY(), interModel.getInterMapId());
				pc.getMoveState().setHeading(interModel.getInterHead());
			}
			if (!interServer) {
				loginLoc();
			} else if (L1InterServer.isOccupyInter(inter)) {
				occupyLocSet();
			}
			
			beginerRegionBuff();
			
			// 인던 아이템 삭제
			for (L1ItemInstance item : pc.getInventory().getItems()) {
				if ((item.getItemId() >= 420100 && item.getItemId() <= 420111) || item.getItemId() == 420113) {
					pc.getInventory().removeItem(item, item.getCount());
				}
			}
			
			War.getInstance().checkCastleWar(pc);
			clan = world.getClan(pc.getClanName());
			if (pc.getClanid() != 0) {// 크란 소속중
				if (clan != null) {
					// 온라인 알리기.
					clan.updateClanMemberOnline(pc);
					boolean is_store_allow = pc.getBloodPledgeRank() == eBloodPledgeRankType.RANK_NORMAL_KING || clan.is_store_allow(pc.getName());
					pc.sendPackets(new S_BloodPledgeUserInfo(clan.getClanName(), pc.getBloodPledgeRank(), is_store_allow), true);
					pc.sendPackets(new S_BloodPledgeInfo(clan.getEmblemStatus() == 1), true);
					String[] clanList = ClanAttentionTable.getInstance().getAttentionClanlist(pc.getClanName());
					pc.sendPackets(new S_PledgeWatch(clanList), true);
					if (pc.getClanid() == clan.getClanId() && 
							//	크란을 해산해, 재차, 동명의 크란이 창설되었을 때의 대책
							pc.getClanName().toLowerCase().equals(clan.getClanName().toLowerCase())) {
						pc.setClan(clan);
						S_ServerMessage connectMessage = new S_ServerMessage(843, pc.getName());
						for (L1PcInstance clanMember : clan.getOnlineClanMember()) {
							if (clanMember.getId() != pc.getId()) {
								clanMember.sendPackets(connectMessage);//	혈맹원의 %0%s가 게임에 접속했습니다.
							}
						}
						connectMessage.clear();
						connectMessage = null;
						
						// 혈맹 알림 메세지
						S_BloodPledgeEnterNoticeNoti enter_notice = clan.getEnterNoticePck();
						if (enter_notice != null) {
							pc.sendPackets(enter_notice);
						}
						
						// 전전쟁 리스트를 취득
						for (L1War war : world.getWarList()) {
							boolean ret = war.CheckClanInWar(pc.getClanName());
							if (ret) {// 전쟁에 참가중
								String enemy_clan_name = war.GetEnemyClanName(pc.getClanName());
								if (enemy_clan_name != null) {
									pc.sendPackets(new S_War(8, pc.getClanName(), enemy_clan_name), true);// 당신의 혈맹이 현재_혈맹과 교전중입니다.
								}
								break;
							}
						}
					} else {
						pc.setClanid(0);
						pc.setClanName(StringUtil.EmptyString);
						pc.setBloodPledgeRank(null);
						pc.setPledgeJoinDate(0);
						pc.setPledgeRankDate(0);
						pc.save();// DB에 캐릭터 정보를 기입한다
					}
				} else {
					pc.setClanid(0);
					pc.setClanName(StringUtil.EmptyString);
					pc.setBloodPledgeRank(null);
					pc.setPledgeJoinDate(0);
					pc.setPledgeRankDate(0);
					pc.save();// DB에 캐릭터 정보를 기입한다
				}
			}
			clanCheckMessage();
			
			world.addVisibleObject(pc);
			pc.beginGameTimeCarrier();// 게임시간 thread
			pc.startHpMpRegeneration();// HP/MP회복 thread
			
			pc.sendPackets(new S_OwnCharStatus(pc), true);
			pc.sendPackets(S_WorldPut.get(pc.getMap()));
			pc.sendPackets(new S_PCObject(pc), true);
			pc.sendPackets(new S_FreePVPRegionNoti(pc.getMapId(), false, null), true);
			pc.sendPackets(new S_Weather(world.getWeather()), true);
			
			pc.sendCastleOwner();// 성혈군주 왕관표시
			pc.sendPackets(new S_SPMR(pc), true);
			pc.sendPackets(new S_Karma(pc), true);// 우호도

			pc.sendPackets(S_PacketBox.INIT_DODGE_LOGIN);
			pc.sendPackets(S_PacketBox.DODGE_LOGIN);
			pc.sendPackets(S_PacketBox.LOGIN_UNKNOWN);
			
			pc.sendPackets(S_FairlyConfig.FAIRLY_NOTI);// 페어리시스템
			
			pc.sendPackets(S_PacketBox.EMERALD_ICON_1);// 모름1
			pc.sendPackets(S_PacketBox.EMERALD_ICON_2);// 모름2
			pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UPDATE, pc.getAbility().getEr()), true);
			pc.sendPackets(new S_OwnCharAttrDef(pc), true);
			
			einhasadLogin();//	아인하사드

			processBuff();// 버프 로드
			skillDelayCheck();
			
			renewStat();//	스탯로드
			
			FreeBuffShieldLoader.getInstance().load(pc);// 불멸의 가호(버프) 로드
			pccafeLogin();// PC방 체크
			
			if (!L1InterServer.isNotNotificationInter(inter)) {
				NotificationTable.getInstance().sendNotification(pc);// 알람
			}
			
			// TODO 장비스왑
			EquipSetTable.getInstance().load(pc);
			
			EventPushLoader.getInstance().sendInfoPacket(pc);// 시스템 편지
			
			pc.sendPackets(S_SpellLateHandlingNoti.NOT_CORRECTION);
			
			pccafeCharge();// PC방 충전
			
			survivalCryLogin();// 생존의 외침
			pc.sendPackets(S_PacketBox.INVEN_SAVE);// 인벤저장
			
			pc.sendVisualEffectAtLogin();// 크라운, 독, 수중등의 시각 효과를 표
			pc.getLight().turnOnOffLight();
			pc.sendPackets(new S_SPMR(pc), true);
			pc.sendPackets(new S_ReturnedStatLogin(pc), true);
			
			pc.startObjectAutoUpdate();// 오브젝트업데이트 쓰레드 시작
			client.setCharReStart(false);
			pc.beginExpMonitor();// 경험치체크 쓰레드 시작
			
			L1PcInstance jonje = world.getPlayer(pc.getName());// 존재버그 관련 추가
			if (jonje == null) {
				pc.sendPackets(L1SystemMessage.TWO_CHAR_CHECK);
				client.kick();
				return;
			}

			if (pc.getCurrentHp() > 0) {
				pc.setDead(false);
				pc.setActionStatus(0);
			} else {
				pc.setDead(true);
				pc.setActionStatus(ActionCodes.ACTION_Die);
			}

			if (pc.getLevel() >= 51 && pc.getLevel() - 50 > pc.getBonusStats() && pc.getAbility().getStatsAmount() < 150) {
				int upstat = (pc.getLevel() - 50) - (pc.getBonusStats());
				pc.sendPackets(new S_MessageYN(479, Integer.toString(upstat)), true);
			}

			if (Config.RANKING.RANKING_SYSTEM_ACTIVE) {
				UserRanking.getInstance().setBuffSetting(pc);
			}
			
			//serchSummon(pc);

			if (pc.getPartnerId() != 0) {
				marriedPartnerMessage();//	결혼중
			}
			
			if (StringUtil.isNullOrEmpty(account.getPhone())) {
				pc.sendPackets(L1GreenMessage.SECURITY_BUFF_EXPLAN);
			} else {
				securityBuff();
			}
			
			pc.getSkill().setSkillEffect(L1SkillId.SET_BUFF, 30000);
			if (pc.getLevel() < Config.ALT.BAPHOMET_SYSTEM_LEVEL) {//	바포메트 시스템 관련 처리
				pc.sendPackets(S_PacketBox.BAPHO_SYSTEM_BUFF);
				config.setNBapoLevel(7);
			}
			
			arcaLogin();
			bmTypeBuffLogin();
			
			Timestamp dragonRaid = account.getDragonRaid();
			if (dragonRaid != null && sysTime < dragonRaid.getTime()) {
				long buffTime = dragonRaid.getTime() - sysTime;
				pc.getSkill().setSkillEffect(L1SkillId.DRAGONRAID_BUFF, buffTime);
				pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGON_RAID_BUFF, (int) buffTime / 1000), true);
			}
			
			if (Config.SERVER.STANDBY_SERVER) {// 오픈대기
				pc.sendPackets(L1GreenMessage.STANBY_EXP_EMPTY_MSG);
				pc.sendPackets(L1SystemMessage.STANBY_EXP_EMPTY_MSG);
			}
			if (GameServerSetting.SAFETY_MODE) {// 보호모드
				pc.sendPackets(L1GreenMessage.SAFTY_MODE_PANALTY_MSG);
				pc.sendPackets(S_SpellBuffNoti.SAFTY_BUFF_ICON_ON);
			}
			
			L1ExcludingList exList = SpamTable.getInstance().getExcludeTable(pc.getId());//	차단리스트
			if (exList != null) {
				setExcludeList(exList);
			}

			pc.sendPackets(new S_StatusCarryWeightInfoNoti(pc), true);// 무게게이지
			
			//Manager.getInstance().LogConnectAppend(pc.getName(), client.getHostname()); // MANAGER DISABLED
			for (L1PcInstance gm : world.getAllGms()) {// 운영자알림
				//gm.sendPackets(new S_SystemMessage(String.format("\\fY%s 님이 접속. \\fVIP: %s 계정:%s", pc.getName(), client.getIp(), client.getAccountName())), true);
				gm.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(24), pc.getName(), client.getIp(), client.getAccountName()), true);
			}

			//	3.63아이템패킷처리
			pc.isWorld = true;
			L1ItemInstance temp = null;
			try {
				//	착용한 아이템이 슬롯에 정상적으로 표현하도록 하기위해 임시로 작업함.
				for (L1ItemInstance item : pc.getInventory().getItems()) {
					temp = item;
					if (item.isEquipped()) {
						pc.getInventory().toSlotPacket(pc, item, true);
					}
				}
			} catch (Exception e) {
				//System.out.println("에러남 의심되는 아이템은 ->> " + temp.getItem().getDescKr() + " / 케릭터: " + pc.getName());
				System.out.println("Error suspected item ->> " + temp.getItem().getDesc() + " / Character: " + pc.getName());
			}
			
			// HPMP 설정(아이템 및 버프처리 완료 후 설정한다)
			pc.setCurrentHp(IntRange.ensure(pc._db_current_hp, 1, pc.getMaxHp()));
			pc.setCurrentMp(IntRange.ensure(pc._db_current_mp, 1, pc.getMaxMp()));
			
			pc.getInventory().CheckCloneItem(pc);//	수량성템 복사방지
			int heading = pc.getMoveState().getHeading();
			if (heading < 0 || heading > 7) {
				pc.getMoveState().setHeading(0);
			}
			if (pc.getHellTime() > 0) {
				pc.beginHell(false);
			}
			if (Config.ETC.WANTED_ACTIVE) {
				huntOption();//	수배효과
			}
			letterCheck();//	편지
			if (pc.getReturnStat_exp() != 0) {
				pc.returnStat();
			}
			pc.LoadCheckStatus();
			if (!CheckInitStat.checkBaseStat(pc)) {
				pc.setReturnStatus(1);
				pc.sendPackets(new S_ReturnedStatStart(pc), true);
				return;
			}
			
			if (clan != null) {
				if (clan.getEinhasadBlessBuff() != 0) {
					pc.getSkill().einhasadClanBuff();// 혈맹의 축복
				}
				L1BuffUtil.pledge_contribution_buff(pc);
			}
			
			pc.sendPackets(S_LoginUnknown.QUEST_UNKNOWN_1);// 패킷 확인 필요
			
			// TODO 출석체크
			if (Config.ATTEND.ATTENDANCE_ACTIVE) {
				attendanceLogin();
			}
			if (pc.isAutoClanjoin()) {
				pc.setAutoClanjoin(false);
				L1PolyMorph.undoPoly(pc);
			}
			
			if (pc.isGm()) {
				L1ItemInstance cloak = pc.getInventory().getEquippedCloak();
				if (cloak != null && L1ItemId.isInvisItem(cloak.getItemId())) {
					pc.getInventory().setEquipped(cloak, false);
				}
				//L1Invisible.getInstance().execute(pc, "투명", null);//	GM일 경우 자동투명
				L1Invisible.getInstance().execute(pc, "invisible", null);//	GM일 경우 자동투명
			}
			
			if (interServer) {
				interServerLogin();// 인터서버
			}
			if (Config.ETC.FREE_PVP_REGION_ENABLE) {
				FreePVPRegionTable.sendPacket(pc);
			}
			
			pc.sendPackets(S_LocRefresh.REFRESH);
			pc.sendPackets(new S_ExpBoostingInfo(pc), true);	//	보너스 경험치
			if (GameServerSetting.POLY_LEVEL_EVENT) {
				pc.sendPackets(S_PolymorphEvent.POLY_EVENT_ON);// 변신 이벤트
			}
			
			// 완료 되지 않은 잠재력 결정
			if (config.get_potential_target_id() > 0) {
				remain_potential();
			}

			// PSS. Retrieve the remain time from account and send it to client
			if (Config.PSS.PLAY_SUPPORT_TIME_LIMITED && pc.getAccount().getPSSTime() > 0) {
				pc.sendPackets(new S_SpellBuffNoti(L1SkillId.STATUS_PSS, SkillIconNotiType.NEW, (int)(pc.getAccount().getPSSTime() / 60), SkillIconDurationShowType.TYPE_EFF_MINUTE, 1));
				if (pc.getAccount().getPSSTimeAdded()) {
					GeneralThreadPool.getInstance().schedule(new PSSTimeAnnouncTask(), 90000);
					pc.getAccount().setPSSTimeAdded(false);
				}
			} 
			
			pc.sendPackets(S_SpellLateHandlingNoti.NOT_CORRECTION);			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			clear();
		}
	}

	/** The task launching the message to show the PSS time added */
	class PSSTimeAnnouncTask implements Runnable {
		@Override
		public void run() {
			try {
				//pc.sendPackets(new S_SystemMessage(String.format("\\fYAdded \\fV%d \\fYminutes of time for the PSS. You have a total of \\fV%d \\fYminutes to use", Config.PSS.PLAY_SUPPORT_TIME_DAY_ADD, (int) Math.round(pc.getAccount().getPSSTime() / 60))), true);
				pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(1), String.valueOf(Config.PSS.PLAY_SUPPORT_TIME_DAY_ADD), String.valueOf((int) Math.round(pc.getAccount().getPSSTime() / 60))), true);
			} catch (Exception e) {
				_log.log(Level.WARNING, "", e);
			}
		}
	}

	void beginerRegionBuff(){
		if (pc.getLevel() > 92) {
			return;
		}
		boolean gerad	= pc.getLevel() <= 88 && pc.getMap().isGeradBuffZone();
		boolean grow	= pc.getMap().isGrowBuffZone();
		if (gerad) {
			if (pc.getLevel() > 79 && !(pc.getMapId() >= 7531 && pc.getMapId() <= 7534)) {
				return;
			}
			pc.getTeleport().setGeradBuff(true);
		}
		if (grow) {
			pc.getTeleport().setGrowBuff(true);
		}
	}
	
	void loginLoc(){
		GetBackRestartTable gbrTable	= GetBackRestartTable.getInstance();
		L1GetBackRestart[] gbrList		= gbrTable.getGetBackRestartTableList();
		for (L1GetBackRestart gbr : gbrList) {
			if (pc.getMapId() == gbr.getArea()) {
				locSet(gbr.getLocX(), gbr.getLocY(), gbr.getMapId());
				break;
			}
		}
		
		int baseMapId = pc.getMap().getBaseMapId();
		if (baseMapId == 1936 || baseMapId == 2936) {//	중앙 사원
			int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_GIRAN);
			locSet(loc[0], loc[1], (short) loc[2]);
			pc.getInventory().consumeItem(810006);
			pc.getInventory().consumeItem(810007);
			pc.getInventory().consumeItem(810011);
		} else if (pc.getMapId() == 5153) {// 배틀존
			if (config.getDuelLine() != 0) {
				config.setDuelLine(0);
			}
			int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_GIRAN);
			locSet(loc[0], loc[1], (short) loc[2]);
		} else if (pc.getMapId() == 750) {// 격돌의 콜로세움
			if (pc.getColoTeam() != 0) {
				pc.setColoTeam(0);
			}
			int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_GIRAN);
			locSet(loc[0], loc[1], (short) loc[2]);
		} else if (baseMapId == 3000 || baseMapId == 3050) {// 혹한의 신전
			int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_OREN);
			locSet(loc[0], loc[1], (short) loc[2]);
			pc.getInventory().consumeItem(30055);
			pc.getInventory().consumeItem(30056);
		} else if (pc.getMapId() == 430 || pc.getMapId() == 624 || (pc.getMapId() >= 286 && pc.getMapId() <= 288)) {// pc방 던전
			int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_SKYGARDEN);
			locSet(loc[0], loc[1], (short) loc[2]);
		} else if (baseMapId == 1400) {
			locSet(33491, 32762, (short) 4);// 카이저 훈련소
		} else if (baseMapId == 2600 || baseMapId == 2699) {
			locSet(33705, 32504, (short) 4);// 화룡의 안식처
		} else if (pc.getMapId() == 514 || (pc.getMapId() >= 1161 && pc.getMapId() <= 1168)) {
			locSet(33435, 32814, (short) 4);
		} else if (pc.getMap().getId() >= 1700 && pc.getMap().getId() <= 1710) {
			locSet(33613, 33243, (short) 4);// 잊혀진 섬
		} else if (pc.getMapId() == 13000 || (baseMapId >= 731 && baseMapId <= 736)) {// 대기실, 인던
			locSet(33464, 32757, (short) 4);
		} else if (pc.getMapId() == 5166 || pc.getMapId() == 5167) {
			locSet(32612, 32734, (short) 4);// 회상의 땅, 축복의 땅
		} else if (pc.getMapId() == L1TownLocation.MAP_WOLRDWAR_HEINE_EVA || pc.getMapId() == L1TownLocation.MAP_WOLRDWAR_HEINE_TOWER) {
			int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_HEINE);
			locSet(loc[0], loc[1], (short) loc[2]);
		} else if (pc.getMapId() == L1TownLocation.MAP_WOLRDWAR_WINDAWOOD_TOWER || pc.getMapId() == L1TownLocation.MAP_WOLRDWAR_WINDAWOOD_AZUR) {
			int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_WINDAWOOD);
			locSet(loc[0], loc[1], (short) loc[2]);
		} else if (pc.getMapId() == L1TownLocation.GETBACK_MAP_TREASURE_ISLAND) {
			int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_HEINE);
			locSet(loc[0], loc[1], (short) loc[2]);
			pc.getInventory().consumeItem(L1ItemId.TREASURE_SHOVEL);// 삽 제거
		}
		
		/** 여관에서 접속시 **/
		int[] innOutLoc = InnHelper.getOutLoc(pc.getMapId());
		if (innOutLoc != null) {
			locSet(innOutLoc[0], innOutLoc[1], (short) innOutLoc[2]);
		} else if (pc.getMapId() == 0 && (pc.getX() >= 32704 && pc.getX() <= 32767 && pc.getY() >= 32768 && pc.getY() <= 32831)) {
			locSet(33437, 32799, (short) 4);//맵오류시
		}
		
		/** LFC **/
		InstanceSpace.getInstance().getBackPc(pc);
		
		// altsettings.properties로 GetBack가 true라면 거리에 이동시킨다
		if (Config.ALT.GET_BACK) {
			int[] loc = Getback.GetBack_Location(pc);
			locSet(loc[0], loc[1], (short) loc[2]);
		}

		// 전쟁중의 기내에 있었을 경우, 성주 혈맹이 아닌 경우는 귀환시킨다.
		int castle_id = L1CastleLocation.getCastleIdByArea(pc);
		if (pc.getMapId() == 66) {
			castle_id = 6;
		}
		if (0 < castle_id) {
			if (War.getInstance().isNowWar(castle_id)) {
				String clanname = null;
				if (clan != null && castle_id == clan.getCastleId()) {
					clanname = clan.getClanName();
					pc.sendPackets(new S_SiegeInjuryTimeNoti(S_SiegeInjuryTimeNoti.SIEGE_KIND.SIEGE_DEFFENCE, War.getInstance().endtime(castle_id), clanname), true);
				} else if (clan != null && clan.getCastleId() != castle_id) {
					int[] loc = L1CastleLocation.getGetBackLoc(castle_id);
					locSet(loc[0], loc[1], (short) loc[2]);
					loc = null;
				} else if (pc.getMapId() == 4) {
					int[] loc = L1CastleLocation.getGetBackLoc(castle_id);
					locSet(loc[0], loc[1], (short) loc[2]);
					loc = null;
				}
			}
		}
	}
	
	void locSet(int x, int y, short mapid){
		pc.setX(x);
		pc.setY(y);
		pc.setMap(mapid);
	}
	
	void interServerLogin(){
		switch(inter){
		case LEAVE:
			pc.getNetConnection().releaseInter();
			config.setAnonymityType(null);
			break;
		case INSTANCE_DUNGEON:
			config.setIndunLoginCheck(true);
			break;
		case FORGOTTEN_ISLAND:case LFC:case DOMINATION_TOWER:
			pc.sendPackets(S_TeamIdServerNoMappingInfo.ON);
			break;
		case WORLD_WAR:case ANT_QUEEN:case OCCUPY_HEINE:case OCCUPY_WINDAWOOD:
			L1PolyMorph.worldClassPoly(pc, true);// 고유 쿨래스 변신
			if (L1InterServer.isOccupyInter(inter)) {
				occupyTimer();
				config.setAnonymityType(ePolymorphAnonymityType.eRandomIncludetombstone);
			}
			break;
		case ABADON:
			L1PolyMorph.anonymityPoly(pc, true);// 익명 쿨래스 변신
			pc.getDungoenTimer().sendTimerPacket();
			config.setAnonymityType(ePolymorphAnonymityType.eRandomIncludetombstone);
			break;
		case TREASURE_ISLAND:
			TreasureIsland.getInstance().enter(pc);
			break;
		default:
			break;
		}
	}
	
	void occupyLocSet(){
		OccupyHandler ocHandler = OccupyManager.getInstance().getHandler(inter == L1InterServer.OCCUPY_HEINE ? OccupyType.HEINE : OccupyType.WINDAWOOD);
		if (ocHandler == null) {
			return;
		}
		pc._occupyTeamType = ocHandler.registTeam(pc);// 수호탑 점령전 팀 설정
		int[] loc = OccupyUtil.getTeamLoc(ocHandler, pc._occupyTeamType);
		locSet(loc[0], loc[1], (short) loc[2]);
	}
	
	void occupyTimer(){
		OccupyHandler ocHandler = OccupyManager.getInstance().getHandler(inter == L1InterServer.OCCUPY_HEINE ? OccupyType.HEINE : OccupyType.WINDAWOOD);
		if (ocHandler == null) {
			return;
		}
		ocHandler.playTimer(pc);
		ocHandler.missionTimer(pc);
		ocHandler.penalty(pc);
	}
	
	void survivalCryLogin(){
		if (pc.getSurvivalTime() == null) {
			pc.setSurvivalTime(new Timestamp(sysTime));// 생존의 외침
		}
		int max		= 10800 - (pc.getLevel() >= 90 ? (pc.getLevel() - 89) * 600 : 0);
		int current = 0;
		if (pc.getSurvivalTime() != null) {
			current	= (int)(sysTime - pc.getSurvivalTime().getTime());
			if (current > max) {
				current = max;
			}
		}
		pc.sendPackets(new S_Extended(current, max), true);
	}
	
	void bmTypeBuffLogin() {
		if (pc.getEMETime() != null) {
			einhasadSubBuffSet(pc.getEMETime().getTime(), L1SkillId.EMERALD_YES, 2);
		}
		if (pc.getEMETime2() != null) {
			einhasadSubBuffSet(pc.getEMETime2().getTime(), L1SkillId.EMERALD_NO, 1);
		}
		if (pc.getPUPLETime() != null) {
			einhasadSubBuffSet(pc.getPUPLETime().getTime(), L1SkillId.DRAGON_PUPLE, 1);
		}
		if (pc.getTOPAZTime() != null) {
			einhasadSubBuffSet(pc.getTOPAZTime().getTime(), L1SkillId.DRAGON_TOPAZ, 2);
		}
		
		Timestamp hero = account.getBuff_HERO();
		if (hero != null && sysTime < hero.getTime()) {
			long bufftime = hero.getTime() - sysTime;
			pc.getSkill().setSkillEffect(L1SkillId.HERO_FAVOR, bufftime);
			pc.sendPackets(new S_SpellBuffNoti(pc, L1SkillId.HERO_FAVOR, true, bufftime / 1000), true); // 아이콘 출력
			pc.getAbility().addAddedStr((byte) 1);
			pc.getAbility().addAddedDex((byte) 1);
			pc.getAbility().addAddedInt((byte) 1);
			pc.getAbility().addShortHitup(3);
			pc.getAbility().addLongHitup(3);
			pc.getAbility().addMagicHitup(3);
			pc.getResistance().addHitupAll(3);
			pc.getAbility().addPVPDamage(3);
			pc.sendPackets(new S_SPMR(pc), true);
			pc.sendPackets(new S_OwnCharStatus(pc), true);
		}
		
		Timestamp life = account.getBuff_LIFE();
		if (life != null && sysTime < life.getTime()) {
			long bufftime = life.getTime() - sysTime;
			pc.getSkill().setSkillEffect(L1SkillId.LIFE_FAVOR, bufftime);
			pc.sendPackets(new S_SpellBuffNoti(pc, L1SkillId.LIFE_FAVOR, true, bufftime / 1000), true); // 아이콘 출력
			pc.addMaxHp(300);
			pc.addMaxMp(100);
		}
		
		Timestamp einFavor = pc.getEinhasadGraceTime();
		if (einFavor != null && sysTime < einFavor.getTime()) {
			long bufftime = einFavor.getTime() - sysTime;
			pc.getSkill().setSkillEffect(L1SkillId.EINHASAD_FAVOR, bufftime);
			pc.sendPackets(new S_RestExpInfoNoti(pc), true);
			pc.sendPackets(new S_SpellBuffNoti(pc, L1SkillId.EINHASAD_FAVOR, true, bufftime / 1000), true); // 아이콘 출력
			if (!pc._isDragonFavor && account.getEinhasad().getPoint() < Config.EIN.REST_EXP_DEFAULT_RATION && !pc.isPCCafe()) {
				account.getEinhasad().setDragonFavor(pc, true);
			}
		} else {
			L1ItemInstance scheduled_item = null;
			for (L1ItemInstance item : pc.getInventory().getItems()) {
				if (item == null || !item.isScheduled() || item.getIconId() != 9659) {
					continue;
				}
				scheduled_item = item;
				break;
			}
			if (scheduled_item != null) {
				scheduled_item.clickItem(pc, null);
			}
		}
	}
	
	void einhasadSubBuffSet(long buffTime, int skillid, int flag){
		if (sysTime > buffTime) {
			return;
		}
		long deleteTime = buffTime - sysTime;
		pc.getSkill().removeSkillEffect(skillid);
		pc.getSkill().setSkillEffect(skillid, deleteTime);
		if (skillid == L1SkillId.EMERALD_YES || skillid == L1SkillId.EMERALD_NO) {
			pc.sendPackets(new S_PacketBox(S_PacketBox.EMERALD_ICON, flag, (int) deleteTime / 1000), true);
		} else {
			pc.sendPackets(new S_PacketBox((int) deleteTime / 1000, flag, true, true), true);
		}
		pc.sendPackets(new S_RestExpInfoNoti(pc), true);
	}
	
	void arcaLogin(){
		L1Arca arca		= pc.getAccount().getArca();
		int activeCount	= arca.getActiveCount(sysTime);
		if (activeCount <= 0) {
			return;
		}
		long afterTime = arca.getActiveTime();
		if (afterTime < 0) {
			afterTime = 0;
		}
		L1ArcaGrade grade = L1ArcaGrade.getGrade(activeCount);
		if (grade == null) {
			System.out.println(String.format("[C_LoginToServer] ARCA_GRADE_UNDEFINED : GRADE(%d)", activeCount));
			return;
		}
		L1BuffUtil.arcaBuffEnable(pc, grade, afterTime);
	}
	
	void pccafeLogin(){
		if (pc.getAccount().getBuff_PCCafe() != null && sysTime < pc.getAccount().getBuff_PCCafe().getTime()) {
			if (pc._isDragonFavor) {
				account.getEinhasad().setDragonFavor(pc, false);
			}
			// 활성화
			pc.setPCCafe(true);
		}
		
		if (pc.isPCCafe()) {
			pc.sendPackets(new S_UserStartSundry(true), true);
			
			S_PCMasterFavorUpdateNoti noti = new S_PCMasterFavorUpdateNoti(config.get_free_buff_shield());
			pc.sendPackets(noti, false);
			pc.sendPackets(noti, true);
		} else {
			// PC방 버프가 없으므로 아인하사드의 가호 체크
			if (pc.getSkill().hasSkillEffect(L1SkillId.EINHASAD_FAVOR)
					&& !pc._isDragonFavor
					&& pc.getAccount().getEinhasad().getPoint() < Config.EIN.REST_EXP_DEFAULT_RATION) {
				account.getEinhasad().setDragonFavor(pc, true);
			}
			pc.sendPackets(new S_UserStartSundry(false), true);
		}
	}
	
	void pccafeCharge() {
		if (!pc.isPCCafe()) {
			return;
		}
		if (!pc._isDragonFavorPCCafe) {
			account.getEinhasad().setDragonFavorPCCafe(pc, true);
		}
		
		S_RestExpInfoNoti ein = new S_RestExpInfoNoti(pc);
		pc.sendPackets(ein, false);
		pc.sendPackets(ein, true);
		
		// PC방 시작
		pc.sendPackets(S_GameGatePCCafeCharge.START);
	}
	
	void loadItems(boolean sendOption) {
		// DB로부터 캐릭터와 창고의 아이템을 읽어들인다
		if (sendOption) {// 착용처리
			pc.getInventory().sendOptioon();
		} else {// DB조사
			CharacterTable.getInstance().restoreInventory(pc);
		}
	}

	void sendItemPacket() {
		pc.sendPackets(new S_AddInventoryNoti(pc), true);
	}
	
	/**
	 * 사냥터 도감 로드
	 */
	void huntingQuestLogin(){
		HuntingQuestUser user		= HuntingQuestUserTable.getUser(pc.getId());
		pc.setHuntingQuest(user);
		Collection<HuntingQuestUserTemp> templist = user.getInfo().values();
		pc.sendPackets(new S_HuntingQuestMapList(templist), true);
		if (!templist.isEmpty()) {
			HuntingQuestObject obj = null;
			for (HuntingQuestUserTemp temp : templist) {
				obj = HuntingQuestTable.getHuntInfo(temp.getQuestId());
				pc.sendPackets(new S_HuntingQuestMapInfo(pc, obj.getMapNumber()), true);
			}
		}
	}
	
	void einhasadLogin() {
		try {
            int einhasad = IntRange.ensure(pc.getAccount().getEinhasad().getPoint(), 0, Config.EIN.REST_EXP_LIMIT_CHARGE_VALUE);
			pc.getAccount().getEinhasad().setPoint(einhasad, pc);
			pc.sendPackets(new S_RestExpInfoNoti(pc), true);
			int tokenCount = pc.getInventory().checkItemCount(L1ItemId.ULTIMATE_BRAVE_COIN);
			if (tokenCount > 0) {
				pc.einGetExcute(tokenCount * 100);
				pc.getInventory().consumeItem(L1ItemId.ULTIMATE_BRAVE_COIN);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void attendanceLogin() {
		AttendanceAccount attendAccount = account.getAttendance();
		if (CommonUtil.isDayResetTimeCheck(attendAccount.getResetDate())) {
			attendAccount.reset(sysTime);
		}
		attendAccount.sendPacket(pc);
	}
	
	void huntOption() {
		if (config.getHuntPrice() >= Config.ETC.WANTED_COST) {
			config.initBeWanted();
			int[] beWanted = { 3, 3, 3, 3, 3, 3 };
			config.setBeWanted(beWanted);
			pc.sendPackets(L1SystemMessage.HUNT_OPTION_EXPLAN);
			config.addBeWanted();
		}
	}
	
	void loadAttr(){
		switch(pc.getElfAttr()){
		case 1:	pc.sendPackets(S_SkillIconGFX.ATTR_EARTH);break;
		case 2:	pc.sendPackets(S_SkillIconGFX.ATTR_FIRE);break;
		case 3:	pc.sendPackets(S_SkillIconGFX.ATTR_FIRE_WATER);break;
		case 4:	pc.sendPackets(S_SkillIconGFX.ATTR_WATER);break;
		case 5:	pc.sendPackets(S_SkillIconGFX.ATTR_FIRE_AIR);break;
		case 6:	pc.sendPackets(S_SkillIconGFX.ATTR_WATER_AIR);break;
		case 8:	pc.sendPackets(S_SkillIconGFX.ATTR_AIR);break;
		case 9:	pc.sendPackets(S_SkillIconGFX.ATTR_FIRE_EARTH);break;
		case 10:pc.sendPackets(S_SkillIconGFX.ATTR_WATER_EARTH);break;
		case 12:pc.sendPackets(S_SkillIconGFX.ATTR_AIR_EARTH);break;
		default:pc.sendPackets(S_SkillIconGFX.ATTR_EMPTY);break;
		}
	}
	
	void loadSkills() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			
			// 액티브
			pstm	= con.prepareStatement("SELECT * FROM character_skills_active WHERE char_obj_id=?");
			pstm.setInt(1, pc.getId());
			rs		= pstm.executeQuery();
			L1Skills skill				= null;
			List<Integer> activeList	= null;
			while (rs.next()) {
				skill		= SkillsTable.getTemplate(rs.getInt("skill_id"));
				if (skill == null) {
					continue;
				}
				String name	= skill.getName();
				if (StringUtil.isNullOrEmpty(name) || name.equals("none")) {
					continue;
				}
				if (activeList == null) {
					activeList = new ArrayList<>();
				}
				activeList.add(skill.getSkillId());
			}
			if (activeList != null && !activeList.isEmpty()) {
				pc.sendPackets(new S_AvailableSpellNoti(activeList, true), true);// 액티브
				pc.getSkill().addLearnActives(activeList);
			}
			SQLUtil.close(rs, pstm);
			
			// 패시브
			pstm	= con.prepareStatement("SELECT * FROM character_skills_passive WHERE char_obj_id=?");
			pstm.setInt(1, pc.getId());
			rs		= pstm.executeQuery();
			L1PassiveSkills passive		= null;
			List<Integer> passiveList	= null;
			while (rs.next()) {
				passive			= SkillsTable.getPassiveTemplate(rs.getInt("passive_id"));
				if (passive == null) {
					continue;
				}
				String name	= passive.getName();
				if (StringUtil.isNullOrEmpty(name) || name.equals("none")) {
					continue;
				}
				if (passiveList == null) {
					passiveList = new ArrayList<>();
				}
				passiveList.add(passive.getPassiveId());
				pc.getPassiveSkill().set(passive.getPassive());
			}
			if (passiveList != null && !passiveList.isEmpty()) {
				pc.sendPackets(new S_AllSpellPassiveNoti(passiveList), true);// 패시브
				pc.getSkill().addLearnPassives(passiveList);
			}
			
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	void securityBuff() {
		pc.getAC().addAc(-1);
		pc.addMaxHp(50);
		pc.sendPackets(new S_PacketBox(pc, S_PacketBox.ICON_SECURITY_SERVICES), true);
	}
	
	void letterCheck() {
		LetterTable letter = LetterTable.getInstance();
		if (letter.CheckPrivateMail(pc) > 0) {
			pc.sendPackets(new S_LetterList(pc, 0, 40), true);// 개인편지
		}
		if (letter.CheckClanMail(pc) > 0) {
			pc.sendPackets(new S_LetterList(pc, 1, 80), true);// 혈맹편지
		}
		if (letter.CheckNoReadMail(pc) > 0) {//	안읽은편지
			pc.send_effect_self(1091);
			pc.sendPackets(L1ServerMessage.sm428);// 편지가 도착했습니다.
		}
	}

	/*void serchSummon() {
		try {
			for (L1SummonInstance summon : L1World.getInstance().getAllSummons()) {
				if (summon.getMaster().getId() == pc.getId()) {
					summon.setMaster(pc);
					pc.addPet(summon);
					for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(summon)) {
						visiblePc.sendPackets(new S_SummonPack(summon, visiblePc), true);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	void renewStat() {
		pc.sendPackets(new S_StatusBaseNoti(pc.getAbility()), true);
		pc.sendPackets(new S_StatusRenewalInfo(pc, 1, L1Status.STR), true);
		pc.sendPackets(new S_StatusRenewalInfo(pc, 1, L1Status.INT), true);
		pc.sendPackets(new S_StatusRenewalInfo(pc, 1, L1Status.WIS), true);
		pc.sendPackets(new S_StatusRenewalInfo(pc, 1, L1Status.DEX), true);
		pc.sendPackets(new S_StatusRenewalInfo(pc, 1, L1Status.CON), true);
		pc.sendPackets(new S_StatusRenewalInfo(pc, 1, L1Status.CHA), true);
		pc.sendPackets(new S_StatusCarryWeightInfoNoti(pc), true);
		pc.sendPackets(new S_SpecialResistance(pc.getResistance()), true);
		
		pc.sendPackets(S_StatusBaseInfo.STAT_12);
		pc.sendPackets(S_StatusBaseInfo.STAT_25);
		pc.sendPackets(S_StatusBaseInfo.STAT_35);
		pc.sendPackets(S_StatusBaseInfo.STAT_45);
		pc.sendPackets(S_StatusBaseInfo.STAT_55);
		pc.sendPackets(S_StatusBaseInfo.STAT_60);
		pc.sendPackets(new S_StatusBaseNoti(pc.getAbility()), true);
		pc.sendPackets(new S_TotalDrinkedElixirNoti(pc.getElixirStats()), true);//	엘릭서 섭취량 로드
	}
	
	void marriedPartnerMessage(){
		L1PcInstance partner = (L1PcInstance) L1World.getInstance().findObject(pc.getPartnerId());
		if (partner != null 
				&& partner.getPartnerId() != 0 
				&& pc.getPartnerId() == partner.getId() 
				&& partner.getPartnerId() == pc.getId()) {
			pc.sendPackets(L1ServerMessage.sm548);		//	당신의 파트너는 지금게임중입니다.
			partner.sendPackets(L1ServerMessage.sm549);	//	당신의 파트너는 방금로그인했습니다.
		}
	}

	void clanCheckMessage() {
		if (clan == null && pc.isCrown()) {
			pc.sendPackets(L1ServerMessage.sm3247);// 혈맹을 창설하고 쉽게 알리세요
		} else if (clan != null && pc.isCrown()) {
			pc.sendPackets(L1ServerMessage.sm3246);// 군주의 부름: 혈원을 모집하세요
		} else if (clan == null && !pc.isCrown()) {
			pc.sendPackets(L1ServerMessage.sm3245);// 군주의 부름: 혈맹에 가입하세요
		}
	}

	void processBuff() {
		L1ExpPotion expPotion = pc.getSkill().getExpPotion();
		List<BuffInfo> buffList = CharBuffTable.getInstance().loadBuff(pc);
		int icon[] = { 
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 
		};
		L1SkillUse skilluse = new L1SkillUse();
		for (BuffInfo buffInfo : buffList) {
			int skillid		= buffInfo.getSkillId();
			int remainTime	= buffInfo.getRemainTime();
			if ((skillid >= L1SkillId.COOKING_BEGIN && skillid <= L1SkillId.COOKING_END) || skillid == L1SkillId.DOGAM_BUFF) {
				L1Cooking.eatCooking(pc, skillid, remainTime);
				continue;
			}
			switch (skillid) {
			case L1SkillId.BRAVE_MENTAL:
			case L1SkillId.ELEMENTAL_FIRE:
			case L1SkillId.QUAKE:
			case L1SkillId.CYCLONE:
			case L1SkillId.COUNTER_BARRIER:
			case L1SkillId.FATAL_POTION:
			case L1SkillId.STRIKER_GALE:
			case L1SkillId.PATIENCE:
			case L1SkillId.INFERNO:
				pc.sendPackets(new S_SpellBuffNoti(pc, skillid, true, remainTime), true);
				break;
			
			/** 일반 마법 **/
			case L1SkillId.DECREASE_WEIGHT:
				int decrease = SkillsTable.getTemplate(L1SkillId.DECREASE_WEIGHT).getCastGfx();
				pc.addCarryBonus(180);
				pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, decrease, remainTime), true);
				break;
			case L1SkillId.FREEZING_ARMOR:
				pc.getAbility().addEr(5);
				pc.sendPackets(new S_SpellBuffNoti(pc, skillid, true, remainTime), true);
				break;
			case L1SkillId.ENCHANT_ACCURACY:
				pc.getAbility().addShortHitup(5);
				pc.sendPackets(new S_SpellBuffNoti(pc, skillid, true, remainTime), true);
				break;
			case L1SkillId.SILENCE:
				icon[2] = remainTime >> 2;
				break;
			case L1SkillId.WEAKNESS:
				icon[4] = remainTime >> 2;
				pc.getAbility().addShortDmgup(-5);
				pc.getAbility().addShortHitup(-1);
				break;
			case L1SkillId.DISEASE:
				icon[5] = remainTime >> 2;
				pc.getAbility().addShortDmgup(-6);
				pc.getAC().addAc(12);
				break;
			case L1SkillId.SHAPE_CHANGE: 
				L1PolyMorph.doPoly(pc, buffInfo.getPolyId(), remainTime, L1PolyMorph.MORPH_BY_ITEMMAGIC);
				break;
			case L1SkillId.SHAPE_CHANGE_DOMINATION: 
				L1PolyMorph.doPoly(pc, buffInfo.getPolyId(), remainTime, L1PolyMorph.MORPH_BY_DOMINATION);
				break;
			case L1SkillId.SHAPE_CHANGE_100LEVEL: 
				L1PolyMorph.doPoly(pc, buffInfo.getPolyId(), remainTime, L1PolyMorph.MORPH_BY_100LEVEL);
				break;
				
			/** 기사 마법 **/
			case L1SkillId.BOUNCE_ATTACK:
				pc.getAbility().addShortHitup(6);
				int bounce = SkillsTable.getTemplate(L1SkillId.BOUNCE_ATTACK).getCastGfx();
				pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, bounce, remainTime), true);
				break;
				
			/** 다엘 마법 **/
			case L1SkillId.VENOM_RESIST:
				icon[3] = remainTime >> 2;
				break;
			case L1SkillId.ENCHANT_VENOM:
				int venom = SkillsTable.getTemplate(L1SkillId.ENCHANT_VENOM).getCastGfx();
				pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, venom, remainTime), true);
				break;
			case L1SkillId.SHADOW_ARMOR:
				pc.getResistance().addMr(5);
				pc.sendPackets(new S_SpellBuffNoti(pc, skillid, true, remainTime), true);
				break;
			case L1SkillId.DOUBLE_BRAKE:
				int doublebrake = pc.isPassiveStatus(L1PassiveId.DOUBLE_BREAK_DESTINY) ? 17224 : SkillsTable.getTemplate(L1SkillId.DOUBLE_BRAKE).getCastGfx();
			    pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, doublebrake, remainTime), true);
				break;
			case L1SkillId.UNCANNY_DODGE:
				pc.getAbility().addDg(30);
				int dodge = SkillsTable.getTemplate(L1SkillId.UNCANNY_DODGE).getCastGfx();
				pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, dodge, remainTime), true);
				break;
			case L1SkillId.MOVING_ACCELERATION:
				int speed=pc.isPassiveStatus(L1PassiveId.MOVING_ACCELERATION_LAST) ? 3 : 4;
				pc.getMoveState().setBraveSpeed(speed);
				pc.sendPackets(new S_SkillBrave(pc.getId(), speed, remainTime), true);
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), speed, 0), true);
				break;
				
			/** 요정 마법 **/
			case L1SkillId.RESIST_MAGIC:
				pc.getResistance().addMr(10);
				pc.sendPackets(new S_ElfIcon(remainTime >> 4, 0, 0, 0), true);
				break;
			case L1SkillId.CLEAR_MIND:
				pc.getAbility().addAddedStr((byte) 1);
				pc.getAbility().addAddedDex((byte) 1);
				pc.getAbility().addAddedInt((byte) 1);
				pc.sendPackets(new S_SpellBuffNoti(pc, skillid, true, remainTime), true);
				break;
			case L1SkillId.PROTECTION_FROM_ELEMENTAL:
				switch (pc.getElfAttr()) {
				case 1:
					pc.getResistance().addEarth(50);
					break;
				case 2:
					pc.getResistance().addFire(50);
					break;
				case 4:
					pc.getResistance().addWater(50);
					break;
				case 8:
					pc.getResistance().addWind(50);
					break;
				}
				pc.sendPackets(new S_ElfIcon(0, 0, 0, remainTime >> 4), true);
				break;
			case L1SkillId.NATURES_TOUCH:
				icon[8] = remainTime >> 2;
				break;
			case L1SkillId.WIND_SHACKLE:
				icon[9] = remainTime >> 2;
				break;
			case L1SkillId.ERASE_MAGIC:
				icon[10] = remainTime >> 2;
				break;
			case L1SkillId.ELEMENTAL_FALL_DOWN:
				icon[12] = remainTime >> 2;
				int playerAttr = pc.getElfAttr();
				int i = -50;
				switch (playerAttr) {
				case 0:
					pc.sendPackets(L1ServerMessage.sm79);
					break;
				case 1:
					pc.getResistance().addEarth(i);
					pc.setAddAttrKind(1);
					break;
				case 2:
					pc.getResistance().addFire(i);
					pc.setAddAttrKind(2);
					break;
				case 4:
					pc.getResistance().addWater(i);
					pc.setAddAttrKind(4);
					break;
				case 8:
					pc.getResistance().addWind(i);
					pc.setAddAttrKind(8);
					break;
				default:
					break;
				}
				break;
			case L1SkillId.SOUL_OF_FLAME:
				icon[15] = remainTime >> 2;
				break;
			case L1SkillId.POLLUTE_WATER:
				icon[16] = remainTime >> 2;
				break;
			case L1SkillId.ELVEN_GRAVITY:
				int gravity = SkillsTable.getTemplate(L1SkillId.ELVEN_GRAVITY).getCastGfx();
				pc.addCarryBonus(300);
				pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, gravity, remainTime), true);
				break;
			case L1SkillId.EXOTIC_VITALIZE:
				int exotic = SkillsTable.getTemplate(L1SkillId.EXOTIC_VITALIZE).getCastGfx();
				pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, exotic, remainTime), true);
				break;
			case L1SkillId.ENTANGLE:
				pc.sendPackets(new S_SkillHaste(pc.getId(), 2, remainTime), true);
				pc.broadcastPacket(new S_SkillHaste(pc.getId(), 2, 0), true);
				break;
					
			/** 환술사 마법 **/
			case L1SkillId.MIRROR_IMAGE:
				pc.getAbility().addDg(30);
				int mirror = SkillsTable.getTemplate(L1SkillId.MIRROR_IMAGE).getCastGfx();
				pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, mirror, remainTime), true);
				break;
			case L1SkillId.CONCENTRATION:
				icon[20] = remainTime >> 4;
				pc.addMpRegen(4);
				break;
			case L1SkillId.INSIGHT:
				icon[21] = remainTime >> 4;
				pc.getAbility().addAddedStr((byte) 1);
				pc.getAbility().addAddedDex((byte) 1);
				pc.getAbility().addAddedCon((byte) 1);
				pc.getAbility().addAddedInt((byte) 1);
				pc.getAbility().addAddedWis((byte) 1);
				pc.resetBaseMr();
				break;
			case L1SkillId.PANIC:
				icon[22] = remainTime >> 4;
				pc.getAbility().addAddedStr((byte) -1);
				pc.getAbility().addAddedDex((byte) -1);
				pc.getAbility().addAddedCon((byte) -1);
				pc.getAbility().addAddedInt((byte) -1);
				pc.getAbility().addAddedWis((byte) -1);
				pc.getAbility().addAddedCha((byte) -1);
				pc.resetBaseMr();
				break;
			case L1SkillId.REDUCE_WEIGHT:
				int reduce = SkillsTable.getTemplate(L1SkillId.REDUCE_WEIGHT).getCastGfx();
				pc.addCarryBonus(480);
				pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, reduce, remainTime), true);
				break;
			case L1SkillId.STATUS_HASTE:
				pc.sendPackets(new S_SkillHaste(pc.getId(), 1, remainTime), true);
				pc.broadcastPacket(new S_SkillHaste(pc.getId(), 1, 0), true);
				pc.getMoveState().setMoveSpeed(1);
				break;
			case L1SkillId.STATUS_BRAVE:case L1SkillId.SAND_STORM:
				pc.sendPackets(new S_SkillBrave(pc.getId(), 1, remainTime), true);
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 1, 0), true);
				pc.getMoveState().setBraveSpeed(1);
				break;
			case L1SkillId.BLOOD_LUST:
				pc.sendPackets(new S_SkillBrave(pc.getId(), 6, remainTime), true);
				Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 6, 0), true);
				pc.getMoveState().setBraveSpeed(6);
				break;
			case L1SkillId.STATUS_ELFBRAVE:
				pc.sendPackets(new S_SkillBrave(pc.getId(), 3, remainTime), true);
				pc.broadcastPacket(new S_SkillBrave(pc.getId(), 3, 0), true);
				pc.getMoveState().setBraveSpeed(3);
				break;
			case L1SkillId.HURRICANE:
				pc.sendPackets(new S_SkillBrave(pc.getId(), 9, remainTime), true);
				Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 9, 0), true);
				pc.getMoveState().setBraveSpeed(9);
				break;
			case L1SkillId.STATUS_FRUIT:
				icon[29] = remainTime >> 2;
				int speedtype = pc.isPassiveStatus(L1PassiveId.DARKHORSE) ? 3 : 4;
				pc.sendPackets(new S_SkillBrave(pc.getId(), speedtype, remainTime), true);
				Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), speedtype, 0), true);
				pc.getMoveState().setBraveSpeed(speedtype);
				break;
			
			case L1SkillId.STATUS_DRAGON_PEARL:
				if (pc.getDrunkenItemEquipped() == 0) {
					pc.broadcastPacketWithMe(new S_Liquor(pc.getId(), 8), true);
					pc.getMoveState().setDrunken(8);
					pc.sendPackets(new S_ServerMessage(1065, remainTime), true);
				}
				break;
			case L1SkillId.STATUS_BLUE_POTION:case L1SkillId.STATUS_BLUE_POTION2:
				pc.sendPackets(new S_SkillIconGFX(S_SkillIconGFX.MANA_ICON, remainTime, true), true);
				break;
			case L1SkillId.STATUS_CHAT_PROHIBITED:
				pc.sendPackets(new S_SkillIconGFX(S_SkillIconGFX.CHAT_ICON, remainTime), true);
				break;
			
			case L1SkillId.BUFF_BLACK_SAND:
				pc.getAC().addAc(-2);
				pc.getResistance().addToleranceSpirit(10);
				pc.addMaxHp(20);
				pc.addMaxMp(13);
				pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 4914, remainTime), true);
				break;
			case L1SkillId.COMA_A:
				icon[30] = (remainTime + 16) >> 5;
				icon[31] = 40;
				pc.getAbility().addAddedCon(1);
				pc.getAbility().addAddedDex(5);
				pc.getAbility().addAddedStr(5);
				pc.getAbility().addShortHitup(3);
				pc.getAC().addAc(-3);
				break;
			case L1SkillId.COMA_B:
				icon[30] = (remainTime + 16) >> 5;
				icon[31] = 41;
				pc.getAbility().addSp(1);
				pc.getAbility().addAddedCon(3);
				pc.getAbility().addAddedDex(5);
				pc.getAbility().addAddedStr(5);
				pc.getAbility().addShortHitup(5);
				pc.getAC().addAc(-8);
				break;
			case L1SkillId.STATUS_CASHSCROLL:	//	체력 증강 주문서
				icon[18] = remainTime >> 4;
				icon[19] = 0;
				pc.addMaxHp(50);
				pc.addHpRegen(4);
				break;
			case L1SkillId.STATUS_CASHSCROLL2:	//	마나 증강 주문서
				icon[18] = remainTime >> 4;
				icon[19] = 1;
				pc.addMaxMp(40);
				pc.addMpRegen(4);
				break;
			case L1SkillId.STATUS_CASHSCROLL3:	//	전투 강화 주문서
				pc.getAbility().addShortDmgup(3);
				pc.getAbility().addShortHitup(3);
				pc.getAbility().addSp(3);
				pc.sendPackets(new S_SpellBuffNoti(pc, skillid, true, remainTime), true);
				break;
			case L1SkillId.NEW_CASHSCROLL1:	//	투사의 전강 주문서
				pc.getAbility().addShortDmgup(3);
				pc.getAbility().addShortHitup(5);
				pc.getAbility().addPVPDamageReduction(3);
				pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 16551, remainTime), true);
				break;
			case L1SkillId.NEW_CASHSCROLL2:	//	명궁의 전강 주문서
				pc.getAbility().addLongDmgup(3);
				pc.getAbility().addLongHitup(5);
				pc.getAbility().addPVPDamageReduction(3);
				pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 16552, remainTime), true);
				break;
			case L1SkillId.NEW_CASHSCROLL3:	//	현자의 전강 주문서
				pc.getAbility().addSp(3);
				pc.getAbility().addMagicHitup(5);
				pc.getAbility().addPVPDamageReduction(3);
				pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 16553, remainTime), true);
				break;
			case L1SkillId.KYULJUN_CASHSCROLL:	//	결전의 주문서
				pc.getAC().addAc(-5);
				pc.getAbility().addShortHitup(5);
				pc.getAbility().addLongHitup(5);
				pc.getAbility().addMagicHitup(2);
				pc.getAbility().addPVPDamageReduction(5);
				pc.getAbility().addPVPDamage(5);
				pc.sendPackets(new S_SpellBuffNoti(pc, skillid, true, remainTime), true);
				break;
			case L1SkillId.HP_CASHSCROLL:// 체력 강화 주문서
				pc.addMaxHp(2000);
				pc.sendPackets(new S_SpellBuffNoti(pc, skillid, true, remainTime), true);
				break;
			
			case L1SkillId.ANTA_MAAN:
				//icon[34] = remaining_time / 30;
				//icon[35] = 46;
				pc.getResistance().addToleranceDragon(5);
				pc.sendPackets(new S_SpellBuffNoti(pc, skillid, true, remainTime), true);
				break;
			case L1SkillId.FAFU_MAAN:
				//icon[34] = remaining_time / 30;
				//icon[35] = 47;
				pc.getResistance().addToleranceSpirit(5);
				pc.sendPackets(new S_SpellBuffNoti(pc, skillid, true, remainTime), true);
				break;
			case L1SkillId.LIND_MAAN:
				//icon[34] = remaining_time / 30;
				//icon[35] = 49;
				pc.getAbility().addEr(10);
				pc.getResistance().addToleranceFear(5);
				pc.getAbility().addMagicCritical(2);
				pc.sendPackets(new S_SpellBuffNoti(pc, skillid, true, remainTime), true);
				break;
			case L1SkillId.VALA_MAAN:
				//icon[34] = remaining_time / 30;
				//icon[35] = 48;
				pc.getAbility().addShortDmgup(2);
				pc.getAbility().addLongDmgup(2);
				pc.getResistance().addToleranceSkill(5);
				pc.sendPackets(new S_OwnCharAttrDef(pc), true);
				pc.sendPackets(new S_SpellBuffNoti(pc, skillid, true, remainTime), true);
				break;
			case L1SkillId.BIRTH_MAAN:
				//icon[34] = remaining_time / 30;
				//icon[35] = 50;
				pc.getResistance().addToleranceDragon(5);
				pc.getResistance().addToleranceSpirit(5);
				pc.sendPackets(new S_SpellBuffNoti(pc, skillid, true, remainTime), true);
				break;
			case L1SkillId.SHAPE_MAAN:
				//icon[34] = remaining_time / 30;
				//icon[35] = 51;
				pc.getAbility().addMagicCritical(1);
				pc.getResistance().addToleranceDragon(5);
				pc.getResistance().addToleranceSpirit(5);
				pc.getResistance().addToleranceFear(5);
				pc.sendPackets(new S_SpellBuffNoti(pc, skillid, true, remainTime), true);
				break;
			case L1SkillId.LIFE_MAAN:
				//icon[34] = remaining_time / 30;
				//icon[35] = 52;
				pc.getAbility().addMagicCritical(1);
				pc.getAbility().addShortDmgup(2);
				pc.getAbility().addLongDmgup(2);
				pc.getResistance().addToleranceAll(5);
				pc.getResistance().addHitupAll(3);
				pc.sendPackets(new S_OwnCharAttrDef(pc), true);
				pc.sendPackets(new S_SpellBuffNoti(pc, skillid, true, remainTime), true);
				break;
			case L1SkillId.BLACK_MAAN:
				pc.getAbility().addDamageReduction(5);
				pc.getResistance().addMr(10);
				pc.sendPackets(new S_SpellBuffNoti(pc, skillid, true, remainTime), true);
				break;
			case L1SkillId.ABSOLUTE_MAAN:
				pc.getAbility().addEr(10);
				pc.getAbility().addMagicCritical(1);
				pc.getAbility().addShortDmgup(2);
				pc.getAbility().addLongDmgup(2);
				pc.getResistance().addToleranceAll(5);
				pc.getResistance().addHitupAll(3);
				pc.getAbility().addDamageReduction(5);
				pc.getResistance().addMr(10);
				pc.sendPackets(new S_SpellBuffNoti(pc, skillid, true, remainTime), true);
				break;
			case L1SkillId.FEATHER_BUFF_A:
				icon[36] = remainTime >> 4;
				icon[37] = 70;
				pc.addHpRegen(3);
				pc.addMpRegen(3);
				pc.getAbility().addShortDmgup(2);
				pc.getAbility().addShortHitup(2);
				pc.addMaxHp(50);
				pc.addMaxMp(30);
				pc.getAbility().addSp(2);
				pc.getAbility().addDamageReduction(3);
				break;
			case L1SkillId.FEATHER_BUFF_B:
				icon[36] = remainTime >> 4;
				icon[37] = 71;
				pc.getAbility().addShortHitup(2);
				pc.addMaxHp(50);
				pc.addMaxMp(30);
				pc.getAbility().addSp(1);
				pc.getAbility().addDamageReduction(2);
				break;
			case L1SkillId.FEATHER_BUFF_C:
				icon[36] = remainTime >> 4;
				icon[37] = 72;
				pc.getAC().addAc(-2);
				pc.addMaxHp(50);
				pc.addMaxMp(30);
				break;
			case L1SkillId.FEATHER_BUFF_D:
				icon[36] = remainTime >> 4;
				icon[37] = 73;
				pc.getAC().addAc(-1);
				break;
			case L1SkillId.METIS_BLESS_SCROLL:
				icon[36] = remainTime >> 4;
				icon[37] = 70;
				pc.getAbility().addDamageReduction(3);
				pc.addHpRegen(3);
				pc.addMpRegen(3);
				pc.getAbility().addShortDmgup(2);
				pc.getAbility().addShortHitup(2);
				pc.getAbility().addLongDmgup(2);
				pc.getAbility().addLongHitup(2);
				pc.addMaxHp(50);
				pc.addMaxMp(30);
				pc.getAbility().addSp(2);
				break;
			case L1SkillId.BUFF_GOLD_FEATHER:
				pc.getAC().addAc(-1);
				pc.getAbility().addDamageReduction(3);
				pc.sendPackets(new S_SpellBuffNoti(pc, skillid, true, remainTime), true);
				break;
			case L1SkillId.BUFF_GOLD_FEATHER_GROW:
				pc.add_rest_exp_reduce_efficiency(2);
				pc.sendPackets(new S_SpellBuffNoti(pc, skillid, true, remainTime), true);
				break;
			case L1SkillId.BUFF_MISOPIA_GROW:
				pc.sendPackets(new S_SpellBuffNoti(pc, skillid, true, remainTime), true);
				break;	
			case L1SkillId.BUFF_MISOPIA_DEFFENS:
				pc.getAbility().addDamageReduction(2);
				pc.getResistance().addMr(10);
				pc.addMaxHp(100);
				pc.addHpRegen(2);
				pc.sendPackets(new S_OwnCharAttrDef(pc), true);
				pc.sendPackets(new S_SpellBuffNoti(pc, skillid, true, remainTime), true);
				break;
			case L1SkillId.BUFF_MISOPIA_ATTACK:
				pc.getAbility().addShortDmgup(3);
				pc.getAbility().addLongDmgup(3);
				pc.getAbility().addSp(2);
				pc.addMaxMp(50);
				pc.addMpRegen(2);
				pc.sendPackets(new S_OwnCharAttrDef(pc), true);
				pc.sendPackets(new S_SpellBuffNoti(pc, skillid, true, remainTime), true);
				break;
			case L1SkillId.BUFF_SPECIAL_GROW:
				pc.sendPackets(new S_SpellBuffNoti(pc, skillid, true, remainTime), true);
				break;	
			case L1SkillId.BUFF_SPECIAL_DEFFENS:
				pc.getAbility().addDamageReduction(1);
				pc.getResistance().addMr(5);
				pc.addMaxHp(50);
				pc.sendPackets(new S_OwnCharAttrDef(pc), true);
				pc.sendPackets(new S_SpellBuffNoti(pc, skillid, true, remainTime), true);
				break;
			case L1SkillId.BUFF_SPECIAL_ATTACK:
				pc.getAbility().addShortDmgup(2);
				pc.getAbility().addLongDmgup(2);
				pc.getAbility().addSp(2);
				pc.addMaxMp(30);
				pc.sendPackets(new S_OwnCharAttrDef(pc), true);
				pc.sendPackets(new S_SpellBuffNoti(pc, skillid, true, remainTime), true);
				break;
			case L1SkillId.ANTA_BUFF:
				pc.getAC().addAc(-2);
				pc.getResistance().addWater(50);
				pc.sendPackets(new S_OwnCharStatus(pc), true);
				pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONBLOOD, 82, remainTime / 60), true);
				break;
			case L1SkillId.FAFU_BUFF:
				pc.addHpRegen(3);
				pc.addMpRegen(1);
				pc.getResistance().addWind(50);
				pc.sendPackets(new S_OwnCharStatus(pc), true);
				pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONBLOOD, 85, remainTime / 60), true);
				break;
			case L1SkillId.RIND_BUFF:
				pc.getAbility().addShortHitup(3);
				pc.getAbility().addLongHitup(3);
				pc.getResistance().addFire(50);
				pc.sendPackets(new S_OwnCharStatus(pc), true);
				pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONBLOOD, 88, remainTime / 60), true);
				break;
			case L1SkillId.LEVELUP_BONUS_BUFF:
				pc.add_exp_boosting_ratio(123);
				pc.sendPackets(new S_PacketBox(remainTime, true, true), true);
				break;
			case L1SkillId.GRACE_OF_TOP:
				pc.getAbility().addPVPDamageReduction(8);
				pc.sendPackets(new S_OwnCharAttrDef(pc), true);
				pc.sendPackets(new S_SpellBuffNoti(pc, skillid, true, remainTime), true);
				break;
			case L1SkillId.GRACE_CROWN_1ST:case L1SkillId.GRACE_KNIGHT_1ST:case L1SkillId.GRACE_ELF_1ST:case L1SkillId.GRACE_WIZARD_1ST:case L1SkillId.GRACE_DARKELF_1ST:case L1SkillId.GRACE_DRAGONKNIGHT_1ST:case L1SkillId.GRACE_ILLUSIONIST_1ST:case L1SkillId.GRACE_WARRIOR_1ST:case L1SkillId.GRACE_FENCER_1ST:case L1SkillId.GRACE_LANCER_1ST:
				pc.addMaxMp(300);
				pc.getAbility().addPVPDamageReduction(10);
				pc.sendPackets(new S_OwnCharAttrDef(pc), true);
				pc.sendPackets(new S_SpellBuffNoti(pc, skillid, true, remainTime), true);
				break;
			case L1SkillId.GRACE_CROWN_2ST:case L1SkillId.GRACE_KNIGHT_2ST:case L1SkillId.GRACE_ELF_2ST:case L1SkillId.GRACE_WIZARD_2ST:case L1SkillId.GRACE_DARKELF_2ST:case L1SkillId.GRACE_DRAGONKNIGHT_2ST:case L1SkillId.GRACE_ILLUSIONIST_2ST:case L1SkillId.GRACE_WARRIOR_2ST:case L1SkillId.GRACE_FENCER_2ST:case L1SkillId.GRACE_LANCER_2ST:
				pc.addMaxMp(150);
				pc.getAbility().addPVPDamageReduction(5);
				pc.sendPackets(new S_OwnCharAttrDef(pc), true);
				pc.sendPackets(new S_SpellBuffNoti(pc, skillid, true, remainTime), true);
				break;
			case L1SkillId.GRACE_CROWN_3ST:case L1SkillId.GRACE_KNIGHT_3ST:case L1SkillId.GRACE_ELF_3ST:case L1SkillId.GRACE_WIZARD_3ST:case L1SkillId.GRACE_DARKELF_3ST:case L1SkillId.GRACE_DRAGONKNIGHT_3ST:case L1SkillId.GRACE_ILLUSIONIST_3ST:case L1SkillId.GRACE_WARRIOR_3ST:case L1SkillId.GRACE_FENCER_3ST:case L1SkillId.GRACE_LANCER_3ST:
				pc.addMaxMp(100);
				pc.getAbility().addPVPDamageReduction(3);
				pc.sendPackets(new S_OwnCharAttrDef(pc), true);
				pc.sendPackets(new S_SpellBuffNoti(pc, skillid, true, remainTime), true);
				break;
			case L1SkillId.DRAGON_EXP_POTION:
				pc.add_rest_exp_reduce_efficiency(30);
				pc.add_exp_boosting_ratio(30);
				pc.sendPackets(new S_RestExpInfoNoti(pc), true);
				pc.sendPackets(new S_SpellBuffNoti(pc, skillid, true, remainTime), true);
				break;
			case L1SkillId.EXP_POTION://	성장의 물약
				expPotion.init(skillid);
				pc.sendPackets(new S_PacketBox(S_PacketBox.EMERALD_ICON, remainTime, expPotion.getPotionId(), 1), true);
				break; 
			case L1SkillId.EXP_POTION1://	빛나는 성장의 물약
			case L1SkillId.EXP_POTION2://	진 데스나이트의 성장 물약
			case L1SkillId.EXP_POTION5://	21주년 성장 물약
			case L1SkillId.EXP_POTION6://	21주년 전설 성장 물약
			case L1SkillId.EXP_POTION7://	아인하사드의 성장 물약
			case L1SkillId.EXP_POTION8://	향상된 성장 물약
				expPotion.init(skillid);
				pc.sendPackets(new S_PacketBox(S_PacketBox.EMERALD_ICON, remainTime, expPotion.getPotionId(), 1), true);
				if (pc.getMap().isSafetyZone(pc.getX(), pc.getY())) {
					pc.sendPackets(new S_PacketBox(S_PacketBox.EMERALD_ICON, remainTime, expPotion.getPotionId(), 2), true);
				} else {
					expPotion.setStop(false);
				}
				break; 
			case L1SkillId.EXP_POTION3://	드래곤의 성장 물약
				expPotion.init(skillid);
				pc.sendPackets(new S_PacketBox(S_PacketBox.EMERALD_ICON, remainTime, expPotion.getPotionId(), 1), true);
				if (pc.getMap().isSafetyZone(pc.getX(), pc.getY())) {
					pc.sendPackets(new S_PacketBox(S_PacketBox.EMERALD_ICON, remainTime, expPotion.getPotionId(), 2), true);
				} else {
					if (pc.getAccount().getEinhasad().getPoint() >= Config.EIN.REST_EXP_DEFAULT_RATION) {
						expPotion.setStop(false);
					} else {
						pc.sendPackets(new S_PacketBox(S_PacketBox.EMERALD_ICON, remainTime, expPotion.getPotionId(), 2), true);
					}
				}
				break;
			case L1SkillId.EXP_POTION4://	영웅의 성장 물약
				expPotion.init(skillid);
				pc.getAbility().addDamageReduction(2);
				pc.addMaxHp(100);
				pc.getAbility().addItemPotionPercent(10);
				pc.getAbility().addItemPotionValue(10);
				pc.sendPackets(new S_PacketBox(S_PacketBox.EMERALD_ICON, remainTime, expPotion.getPotionId(), 1), true);
				if (pc.getMap().isSafetyZone(pc.getX(), pc.getY())) {
					pc.sendPackets(new S_PacketBox(S_PacketBox.EMERALD_ICON, remainTime, expPotion.getPotionId(), 2), true);
				} else {
					expPotion.setStop(false);
				}
				break;
			case L1SkillId.BUFF_STR_ADD:
				icon[38] = remainTime >> 4;
				icon[39] = 67;
				pc.getAbility().addShortDmgup(3);
				pc.getAbility().addShortHitup(5);
				pc.getAbility().addAddedStr(1);
				break;
			case L1SkillId.BUFF_DEX_ADD:
				icon[38] = remainTime >> 4;
				icon[39] = 65;
				pc.getAbility().addLongDmgup(3);
				pc.getAbility().addLongHitup(5);
				pc.getAbility().addAddedDex(1);
				break;
			case L1SkillId.BUFF_INT_ADD:
				icon[38] = remainTime >> 4;
				icon[39] = 69;
				pc.getAbility().addSp(1);
				pc.getAbility().addMagicHitup(3);
				pc.getAbility().addAddedInt(1);
				break;
			case L1SkillId.SET_BUFF:
				remainTime = 30;
				break;
			default:
				skilluse.handleCommands(pc, skillid, pc.getId(), pc.getX(), pc.getY(), remainTime, L1SkillUseType.LOGIN);
				break;
			}
			pc.getSkill().setSkillEffect(skillid, remainTime * 1000);
			
			if (skillid >= L1SkillId.EXP_POTION1 && skillid <= L1SkillId.EXP_POTION8) {
				expSafezoneCheck(skillid);
			}
		}
		pc.sendPackets(new S_UnityIcon(
				icon[0], icon[1], icon[2], icon[3], icon[4], icon[5], 
				icon[6], icon[7], icon[8], icon[9], 
				icon[10], icon[11], icon[12], icon[13], 
				icon[14], icon[15], icon[16], 
				icon[17], icon[18], icon[19], 
				icon[20], icon[21], icon[22], 
				icon[23], icon[24], icon[25], 
				icon[26], icon[27], icon[28], icon[29], 
				icon[30], icon[31], icon[32], icon[33], 
				icon[34], icon[35], icon[36], icon[37], 
				icon[38], icon[39], icon[40]), true);
		buffList.clear();
		skilluse = null;
	}
	
	void expSafezoneCheck(int skillId){
		if (pc.getSkill().hasSkillEffect(skillId) 
				&& (pc.getMap().isSafetyZone(pc.getX(), pc.getY()) 
						|| (skillId == L1SkillId.EXP_POTION3 && pc.getAccount().getEinhasad().getPoint() <= Config.EIN.REST_EXP_DEFAULT_RATION))) {
			pc.getSkill().stopSkillEffectTimer(skillId);//	타이머 중지
		}
	}
	
	void skillDelayCheck(){
		if (pc.getSkill().getDefaultSkillDelay() != null && pc.getSkill().getDefaultSkillDelay().getTime() > sysTime) {
			int delay = (int)(pc.getSkill().getDefaultSkillDelay().getTime() - sysTime);
			L1SkillDelay.onSkillUse(pc, (int) delay, 0);
			pc.sendPackets(new S_SpellDelay(0, delay, 0), true);
		}
		if (pc.getSkill().getFirstSkillDelay() != null && pc.getSkill().getFirstSkillDelay().getTime() > sysTime) {
			int delay = (int)(pc.getSkill().getFirstSkillDelay().getTime() - sysTime);
			L1SkillDelay.onSkillUse(pc, (int) delay, 1);
			pc.sendPackets(new S_SpellDelay(1, delay, 0), true);
		}
		if (pc.getSkill().getSecondSkillDelay() != null && pc.getSkill().getSecondSkillDelay().getTime() > sysTime) {
			int delay = (int)(pc.getSkill().getSecondSkillDelay().getTime() - sysTime);
			L1SkillDelay.onSkillUse(pc, (int) delay, 2);
			pc.sendPackets(new S_SpellDelay(2, delay, 0), true);
		}
		if (pc.getSkill().getThirdSkillDelay() != null && pc.getSkill().getThirdSkillDelay().getTime() > sysTime) {
			int delay = (int)(pc.getSkill().getThirdSkillDelay().getTime() - sysTime);
			L1SkillDelay.onSkillUse(pc, (int) delay, 3);
			pc.sendPackets(new S_SpellDelay(3, delay, 0), true);
		}
		if (pc.getSkill().getFourthSkillDelay() != null && pc.getSkill().getFourthSkillDelay().getTime() > sysTime) {
			int delay = (int)(pc.getSkill().getFourthSkillDelay().getTime() - sysTime);
			L1SkillDelay.onSkillUse(pc, (int) delay, 4);
			pc.sendPackets(new S_SpellDelay(4, delay, 0), true);
		}
		if (pc.getSkill().getFiveSkillDelay() != null && pc.getSkill().getFiveSkillDelay().getTime() > sysTime) {
			int delay = (int)(pc.getSkill().getFiveSkillDelay().getTime() - sysTime);
			L1SkillDelay.onSkillUse(pc, (int) delay, 5);
			pc.sendPackets(new S_SpellDelay(5, delay, 0), true);
		}
	}
	
	void setExcludeList(L1ExcludingList exList) {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM character_exclude WHERE char_id = ?");
			pstm.setInt(1, pc.getId());
			rs		= pstm.executeQuery();
			while (rs.next()) {
				int type	= rs.getInt("type");
				String name	= rs.getString("exclude_name");
				if (!exList.contains(type, name)) {
					exList.add(type, name);
				}
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	/**
	 * 잠재력 결정 전 종료가 발생하여 로그인 시 재처리
	 */
	void remain_potential() {
		L1ItemInstance doll = pc.getInventory().getItem(config.get_potential_target_id());
		if (doll == null) {
			config.reset_potential_enchant();
			return;
		}
		pc.getTeleport().setStateLoc(pc.getX(), pc.getY(), pc.getMapId());
		pc.getTeleport().c_start(32761 + CommonUtil.random(10), 32832 + CommonUtil.random(10), (short) 5167, pc.getMoveState().getHeading(), true);// 축복의 땅
		pc.sendPackets(new S_EnchantPotentialRestartNoti(config.get_potential_target_id(), config.get_potential_bonus_grade(), config.get_potential_bonus_id(), doll.getPotential() == null ? 0 : doll.getPotential().getInfo().get_bonus_desc()), true);
	}

	@Override
	public String getType() {
		return C_LOGIN_TO_SERVER;
	}
}


