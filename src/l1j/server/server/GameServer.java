package l1j.server.server;

import l1j.server.server.serverpackets.message.S_SystemMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.AuthIP;
import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.ai.AiLoader;
import l1j.server.GameSystem.ai.AiManager;
import l1j.server.GameSystem.attendance.AttendanceAccountTable;
import l1j.server.GameSystem.attendance.AttendanceGroupType;
import l1j.server.GameSystem.attendance.AttendanceTable;
import l1j.server.GameSystem.beginnerquest.BeginnerQuestTable;
import l1j.server.GameSystem.beginnerquest.BeginnerQuestUserTable;
import l1j.server.GameSystem.charactertrade.CharacterTradeManager;
import l1j.server.GameSystem.craft.CraftInfoLoader;
import l1j.server.GameSystem.craft.CraftSuccessCountLoader;
import l1j.server.GameSystem.deathpenalty.DeathPenaltyTable;
import l1j.server.GameSystem.dungeontimer.loader.L1DungeonTimerLoader;
import l1j.server.GameSystem.dungeontimer.loader.L1DungeonTimerUserLoader;
import l1j.server.GameSystem.eventpush.EventPushLoader;
import l1j.server.GameSystem.freebuffshield.FreeBuffShieldLoader;
import l1j.server.GameSystem.huntingquest.HuntingQuestTable;
import l1j.server.GameSystem.huntingquest.user.HuntingQuestUserTable;
import l1j.server.GameSystem.inn.InnHandler;
import l1j.server.GameSystem.inn.InnHelper;
import l1j.server.GameSystem.inn.InnLoc;
import l1j.server.GameSystem.shoplimit.ShopLimitLoader;
import l1j.server.GameSystem.smelting.SmeltingLoader;
import l1j.server.GameSystem.tjcoupon.TJCouponLoader;
import l1j.server.IndunSystem.clandungeon.ClanDungeonTable;
import l1j.server.IndunSystem.fantasyisland.FantasyIslandUtil;
import l1j.server.IndunSystem.indun.IndunUtill;
import l1j.server.IndunSystem.minigame.BattleZone;
import l1j.server.IndunSystem.ruun.RuunLoader;
import l1j.server.LFCSystem.Loader.InstanceLoadManager;
import l1j.server.RobotSystem.RobotAIThread;
import l1j.server.common.DescKLoader;
import l1j.server.common.StringKLoader;
import l1j.server.common.bin.ArmorElementCommonBinLoader;
import l1j.server.common.bin.CatalystTableCommonBinLoader;
import l1j.server.common.bin.ChargedTimeMapCommonBinLoader;
import l1j.server.common.bin.CompanionCommonBinLoader;
import l1j.server.common.bin.CraftCommonBinLoader;
import l1j.server.common.bin.EinhasadPointCommonBinLoader;
import l1j.server.common.bin.EinhasadPointFaithCommonBinLoader;
import l1j.server.common.bin.ElementEnchantCommonBinLoader;
import l1j.server.common.bin.EnchantScrollTableInfoCommonBinLoader;
import l1j.server.common.bin.EnchantTableInfoCommonBinLoader;
import l1j.server.common.bin.EnterMapsCommonBinLoader;
import l1j.server.common.bin.FavorBookCommonBinLoader;
import l1j.server.common.bin.GeneralGoodsCommonBinLoader;
import l1j.server.common.bin.HuntingQuestCommonBinLoader;
import l1j.server.common.bin.IndunCommonBinLoader;
import l1j.server.common.bin.ItemCommonBinLoader;
import l1j.server.common.bin.NdlCommonBinLoader;
import l1j.server.common.bin.NpcCommonBinLoader;
import l1j.server.common.bin.PCMasterCommonBinLoader;
import l1j.server.common.bin.PassiveSpellCommonBinLoader;
import l1j.server.common.bin.PortraitCommonBinLoader;
import l1j.server.common.bin.PotentialCommonBinLoader;
import l1j.server.common.bin.QuestCommonBinLoader;
import l1j.server.common.bin.ShipCommonBinLoader;
import l1j.server.common.bin.SpellCommonBinLoader;
import l1j.server.common.bin.TimeCollectionCommonBinLoader;
import l1j.server.common.bin.TreasureBoxCommonBinLoader;
import l1j.server.server.clientpackets.proto.ProtoPacketLoader;
import l1j.server.server.command.L1Commands;
import l1j.server.server.construct.L1CharacterInfo;
import l1j.server.server.construct.L1InterServer;
import l1j.server.server.construct.L1ServerType;
import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.construct.skill.L1SkillInfo;
import l1j.server.server.controller.AnoldEventController;
import l1j.server.server.controller.AutoCharacterSaveController;
import l1j.server.server.controller.ChangeHoursController;
import l1j.server.server.controller.ChangeMinuteController;
import l1j.server.server.controller.ChangeSecondController;
import l1j.server.server.controller.CrockController;
import l1j.server.server.controller.DogFightController;
import l1j.server.server.controller.DollRaceController;
import l1j.server.server.controller.LoginController;
import l1j.server.server.datatables.AutoLoot;
import l1j.server.server.datatables.BalanceTable;
import l1j.server.server.datatables.BanAccountTable;
import l1j.server.server.datatables.BossSpawnTable;
import l1j.server.server.datatables.CastleTable;
import l1j.server.server.datatables.CatalystTable;
import l1j.server.server.datatables.CharacterEinhasadStatTable;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ChatLogTable;
import l1j.server.server.datatables.ClanAttentionTable;
import l1j.server.server.datatables.ClanBlessBuffTable;
import l1j.server.server.datatables.ClanContributionBuffTable;
import l1j.server.server.datatables.ClanHistoryTable;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.datatables.ConnectRewardTable;
import l1j.server.server.datatables.DogFightTable;
import l1j.server.server.datatables.DoorSpawnTable;
import l1j.server.server.datatables.DropTable;
import l1j.server.server.datatables.EnchantResultTable;
import l1j.server.server.datatables.EventTable;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.FreePVPRegionTable;
import l1j.server.server.datatables.FurnitureSpawnTable;
import l1j.server.server.datatables.GetBackRestartTable;
import l1j.server.server.datatables.HarddriveTable;
import l1j.server.server.datatables.InterRaceRegionTable;
import l1j.server.server.datatables.IpTable;
import l1j.server.server.datatables.ItemBoxTable;
import l1j.server.server.datatables.ItemBuffTable;
import l1j.server.server.datatables.ItemClickMessageTable;
import l1j.server.server.datatables.ItemMentTable;
import l1j.server.server.datatables.ItemSelectorTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.KeyTable;
import l1j.server.server.datatables.LightSpawnTable;
import l1j.server.server.datatables.MagicDollInfoTable;
import l1j.server.server.datatables.MapBalanceTable;
import l1j.server.server.datatables.MapTypeTable;
import l1j.server.server.datatables.MapsTable;
import l1j.server.server.datatables.MatherBoardTable;
import l1j.server.server.datatables.MobGroupTable;
import l1j.server.server.datatables.MobSkillTable;
import l1j.server.server.datatables.NPCTalkConversionTable;
import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.datatables.NotificationTable;
import l1j.server.server.datatables.NpcActionTable;
import l1j.server.server.datatables.NpcActionTeleportTable;
import l1j.server.server.datatables.NpcCashShopSpawnTable;
import l1j.server.server.datatables.NpcChatTable;
import l1j.server.server.datatables.NpcInfoTable;
import l1j.server.server.datatables.NpcShopSpawnTable;
import l1j.server.server.datatables.NpcShopTable;
import l1j.server.server.datatables.NpcSpawnTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.PenaltyItemTable;
import l1j.server.server.datatables.PlaySupportTable;
import l1j.server.server.datatables.PolyTable;
import l1j.server.server.datatables.RaceTable;
import l1j.server.server.datatables.ReportTable;
import l1j.server.server.datatables.ResolventTable;
import l1j.server.server.datatables.RevengeTable;
import l1j.server.server.datatables.ServerExplainTable;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.datatables.SkillsInfoTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.datatables.SpawnTable;
import l1j.server.server.datatables.SpellMeltTable;
import l1j.server.server.datatables.SpellProbabilityTable;
import l1j.server.server.datatables.TownTable;
import l1j.server.server.datatables.UBSpawnTable;
import l1j.server.server.datatables.WarTimeTable;
import l1j.server.server.datatables.WeaponAddDamage;
import l1j.server.server.datatables.WeaponSkillTable;
import l1j.server.server.model.Dungeon;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1DeleteItemOnGround;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.GameTimeClock;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.model.item.L1Fishing;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.item.L1TreasureBox;
import l1j.server.server.model.item.ablity.ItemAbilityFactory;
import l1j.server.server.model.item.ablity.enchant.L1EnchantLoader;
import l1j.server.server.model.item.collection.L1CollectionLoader;
import l1j.server.server.model.item.collection.favor.loader.L1FavorBookLoader;
import l1j.server.server.model.item.collection.favor.loader.L1FavorBookUserLoader;
import l1j.server.server.model.item.collection.time.loader.L1TimeCollectionLoader;
import l1j.server.server.model.item.collection.time.loader.L1TimeCollectionUserLoader;
import l1j.server.server.model.item.function.L1HealingPotion;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.npc.L1NpcHtmlFactory;
import l1j.server.server.model.npc.L1NpcIdFactory;
import l1j.server.server.model.skill.L1SkillActionFactory;
import l1j.server.server.model.sprite.SpriteLoader;
import l1j.server.server.model.trap.L1WorldTraps;
import l1j.server.server.serverpackets.S_CharPass;
import l1j.server.server.serverpackets.S_LoginUnknown;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_TeamIdServerNoMappingInfo;
import l1j.server.server.serverpackets.inter.S_ConnectHibreedServer;
import l1j.server.server.serverpackets.message.S_DialogueMessage;
import l1j.server.server.serverpackets.message.S_MsgAnnounce;
import l1j.server.server.serverpackets.message.S_Notification;
import l1j.server.server.serverpackets.party.PartyUISpellInfo;
import l1j.server.server.serverpackets.spell.S_SpellPassiveOnOff;
import l1j.server.server.utils.ColorUtil;
import l1j.server.server.utils.DeadLockDetector;
import l1j.server.server.utils.SQLUtil;
import l1j.server.web.WebServer;
//import manager.Manager;  // MANAGER DISABLED
//import manager.ManagerInfoThread;  // MANAGER DISABLED
import xnetwork.BufferPool;

public class GameServer {
	private static Logger _log = Logger.getLogger(GameServer.class.getName());
	private static GameServer _instance;
	private LoginController _loginController;
	private DeadLockDetector _deadDetectThread;

	private GameServer() {}

	public static GameServer getInstance() {
		if (_instance == null) {
			synchronized (GameServer.class) {
				if (_instance == null) {
					_instance = new GameServer();
				}
			}
		}
		return _instance;
	}

	public void initialize() throws Exception {
		try {
			GeneralThreadPool.getInstance();
			BufferPool.getInstance();// 버퍼풀 init
			IdFactory.getInstance();// id load
			MapsTable.getInstance();
			MapBalanceTable.getInstance();// 맵 밸런스
			L1WorldMap.getInstance();
			DescKLoader.getInstance();// desc-k.tbl
			StringKLoader.getInstance();// string-k.tbl
			
			_loginController = LoginController.getInstance();
			_loginController.setMaxAllowedOnlinePlayers(Config.SERVER.MAX_ONLINE_USERS);
			
			_deadDetectThread = new DeadLockDetector(); 
			_deadDetectThread.setDaemon(true); 
			_deadDetectThread.start(); 
			
			InnLoc.init();// 여관 좌표
			InnHelper.init();
			InnHandler.getInstance().init();
			
			CharacterTable.getInstance().loadAllCharName();// 전체 케릭터 이름 로드
			CharacterTable.clearOnlineStatus();// 온라인 상태 리셋트
			
			SpriteLoader.getInstance();// sprite 정보 로드
			
			initTime();// 게임 시간
			initBin();// bin 데이터
			
			KeyTable.initBossKey();// 보스 키 리셋
			ClanAttentionTable.getInstance().loadEmblemAttention();// 문장주시

			NpcCashShopSpawnTable.getInstance().Start();// 패키지 상점 스폰
			
			// 정령의 돌 타임 컨트롤러
			/*if (Config.ELEMENTAL_STONE_AMOUNT > 0) {
				ElementalStoneGenerator elementalStoneGenerator = ElementalStoneGenerator.getInstance();
				GeneralThreadPool.getInstance().scheduleAtFixedRate(elementalStoneGenerator, 0, ElementalStoneGenerator.SLEEP_TIME);
			}*/
			
			RevengeTable.getInstance();// 리벤지
			
			//LoggerInstance.getInstance();// 로그파일저장 컨트롤러(ChangeMinuteController로 이동)

			//RobotAIThread.init();// 로봇시스템
					
			// npc shop
			NpcShopTable.getInstance();
			//GeneralThreadPool.getInstance().execute(NpcShopSystem.getInstance());
			
			// 캐릭터 자동 저장 쓰레드
			AutoCharacterSaveController autoSave = new AutoCharacterSaveController(Config.SERVER.AUTO_CHAR_SAVE_TIME * 60 * 1000);
			GeneralThreadPool.getInstance().schedule(autoSave, Config.SERVER.AUTO_CHAR_SAVE_TIME * 60 * 1000);
		
			// 배틀존
			if (Config.DUNGEON.BATTLE_ZONE_ACTIVE) {
				GeneralThreadPool.getInstance().execute(BattleZone.getInstance());
			}
			
			WarTimeTable.getInstance();
			
			// 인형 경주
			if (Config.ALT.DOLL_RACE_ENABLED) {
				GeneralThreadPool.getInstance().execute(DollRaceController.getInstance());
			}
			
			// 투견
			if (Config.ALT.DOG_FIGHT_ENABLED) {
				GeneralThreadPool.getInstance().execute(DogFightController.getInstance());		
			}

			// 유령의 집
			//GeneralThreadPool.getInstance().execute(L1HauntedHouse.getInstance());

			//GeneralThreadPool.getInstance().execute(L1Racing.getInstance());
			
			RuunLoader.getInstance();// 루운던전
			AutoLoot.getInstance();
			
			SkillsTable.getInstance();
			SkillsInfoTable.getInstance();
			
			NpcTable npcTable = NpcTable.getInstance();// 엔피씨 데이터 로드
			new L1DeleteItemOnGround().initialize();

			if (!npcTable.isInitialized()) {
				throw new Exception("Could not initialize the npc table");
			}
			NpcChatTable.getInstance();
			MobSkillTable.getInstance();
			MobGroupTable.getInstance();
			
			SpawnTable.getInstance();// 엔피씨를 스폰시킨다.
			
			PolyTable.getInstance();
			ItemTable.getInstance();
			//ItemTable.getInstance().initRace();
			DropTable.getInstance();
			ShopTable.getInstance();
			NPCTalkDataTable.getInstance();
			NPCTalkConversionTable.getInstance();
			L1World.getInstance();
			L1WorldTraps.getInstance();
			Dungeon.getInstance();
			NpcSpawnTable.getInstance();
			IpTable.getInstance();
			UBSpawnTable.getInstance();
			TownTable.getInstance();
			ClanTable.getInstance();
			ClanBlessBuffTable.getInstance();
			ClanContributionBuffTable.getInstance();
			CastleTable.getInstance();
			L1CastleLocation.setCastleTaxRate(); // CastleTable 초기화 다음 아니면 안 된다
			GetBackRestartTable.getInstance();
			DoorSpawnTable.getInstance();
			ChatLogTable.getInstance();
			WeaponSkillTable.getInstance();
			NpcActionTable.load();
			GMCommandsConfig.load();
			Getback.loadGetBack();
			L1TreasureBox.load();
			L1HealingPotion.load();
			L1Fishing.load();
			TownTeleport.load();
			ReportTable.getInstance();
			RaceTable.getInstance();
			DogFightTable.getInstance();
			ResolventTable.getInstance();
			FurnitureSpawnTable.getInstance();
			LightSpawnTable.getInstance();
			L1Commands.getInstance();
			//SoldierTable.getInstance();
			Announcements.getInstance();
			WeaponAddDamage.getInstance();
			ClanHistoryTable.getInstance().dateCheckDelete();
			
			CraftInfoLoader.getInstance();
			CraftSuccessCountLoader.getInstance();
			
			ItemClickMessageTable.getInstance();
			CatalystTable.getInstance();
			
			ExpTable.getInstance().loadExp(false);

			BossSpawnTable.getInstance();// 보스
			PlaySupportTable.getInstance();// 플레이 서포트
			
			// 펫
			//PetTable.getInstance();
			//CharacterPetTable.getInstance();
			
			// 출석체크
			AttendanceGroupType.init();
			AttendanceTable.getInstance();
			AttendanceAccountTable.getInstance();
			
			if (Config.DUNGEON.TIME_CRACK_ACTIVE) {// 테베, 티칼 컨트롤러
				GeneralThreadPool.getInstance().execute(CrockController.getInstance());
			}
			if (Config.ETC.ANOLD_EVENT_ACTIVE) {// 돌아온아놀드이벤트
				AnoldEventController.getInstance();
			}
			if (!Config.ALT.ALT_HALLOWEENIVENT) {// 할로윈 이벤트
				HalloweenDelete();
			}
			if (!Config.ALT.ALT_RABBITEVENT) {// 신묘년 이벤트
				RabbitEventDelete();
			}
			if (Config.ALT.Use_Show_Announcecycle) {// 자동 공지사항
				Announcecycle.getInstance();
			}
			ServerExplainTable.getInstance();
			
			IndunUtill.getInstance();
			
			EnchantResultTable.getInstance();// 인첸트 그래픽
			NpcInfoTable.getInstance();// 엔피씨 정보
			
			FantasyIslandUtil.getInstance();// 중앙 사원
			
			PenaltyItemTable.getInstance();// 보호아이템
			
			// 초급 퀘스트
			BeginnerQuestTable.getInstance();
			BeginnerQuestUserTable.getInstance();
			
			// 몬스터 도감
//			MonsterBookLoader.getInstance();
//			MonsterBookCompensateLoader.getInstance();
//			WeekQuestDateCalculator.getInstance().run();
			
			InstanceLoadManager.getInstance().load();// LFC
			
			CharacterEinhasadStatTable.getInstance();
			
			//L1ClanRanking.getInstance().start();
			ClanDungeonTable.getInstance();
			
			// 사냥터 도감
			if (Config.QUEST.HUNTING_QUEST_ACTIVE) {
				HuntingQuestTable.getInstance();
				HuntingQuestUserTable.getInstance();
			}
			
			DeathPenaltyTable.getInstance();// 사망패널티 시스템
			EventPushLoader.getInstance();// 푸쉬 시스템
			CharacterTradeManager.getInstance().load();// 케릭터 교환 시스템
			MagicDollInfoTable.getInstance();// 인형정보
			ItemSelectorTable.getInstance();// 아이템 셀렉터 시스템
			
			// 던전 타이머
			L1DungeonTimerLoader.getInstance();
			L1DungeonTimerUserLoader.getInstance();
			
			SpellMeltTable.getInstance();// 스킬 용해
			ShopLimitLoader.getInstance();// 상점 제한
			ItemMentTable.getInstance();// 멘트
			L1SkillActionFactory.getInstance();// 스킬 핸들러 로드
			L1CollectionLoader.getInstance();// 컬렉션 시스템
			InterRaceRegionTable.getInstance();
			NpcActionTeleportTable.getInstance();
			if (Config.TJ.TJ_COUPON_ENABLE) {
				TJCouponLoader.getInstance();
			}
			ItemBuffTable.getInstance();
			ItemBoxTable.getInstance();
			MapTypeTable.getInstance();
			L1EnchantLoader.getInstance();
			BalanceTable.getInstance();
			FreePVPRegionTable.getInstance();
			
			// 이벤트 알람
			NotificationTable.getInstance();
			
			EventTable.getInstance();
			ConnectRewardTable.getInstance();
			
			// 성물 시스템
			L1FavorBookLoader.getInstance();
			L1FavorBookUserLoader.getInstance();
			
			// 실렉티스 전시회
			L1TimeCollectionLoader.getInstance();
			L1TimeCollectionUserLoader.getInstance();
			
			SpellProbabilityTable.getInstance();

			// 제련 시스템
			SmeltingLoader.getInstance();
			
			// 가호(버프)
			FreeBuffShieldLoader.getInstance();
			
			ProtoPacketLoader.getInstance();
			
			// ai user
			AiLoader.getInstance();
			AiManager.getInstance();
			
			NpcShopSpawnTable.getInstance();
			
			BanAccountTable.getInstance();// 계정 제한 정보
			AuthIP.getInstance();// 해외 아이피 제한
			HarddriveTable.getInstance();// 하드웨어 밴 정보
			MatherBoardTable.getInstance();// 마더보드 벤 정보
			
			initCache();
			
			ChangeHoursController.getInstance().start();
			ChangeMinuteController.getInstance().start();
			ChangeSecondController.getInstance().start();
			
			// 가비지 컬렉터 실행 (Null) 객체의 해제
			System.gc();
			Runtime.getRuntime().addShutdownHook(Shutdown.getInstance());
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	private void initTime() {
		GameTimeClock.init();// 게임 시간 시계
		RealTimeClock.init();// 현재 시간 시계
	}
	
	/**
	 * 빈 파일 로드
	 */
	private void initBin() {
		ItemCommonBinLoader.getInstance();// item-common.bin
		CraftCommonBinLoader.getInstance();// craft-common.bin
		NpcCommonBinLoader.getInstance();// npc-common.bin
		SpellCommonBinLoader.getInstance();// spell-common.bin
		PassiveSpellCommonBinLoader.getInstance();// passivespell-common.bin
		NdlCommonBinLoader.getInstance();// ndl-common.bin
		PortraitCommonBinLoader.getInstance();// portrait-common.bin
		CompanionCommonBinLoader.getInstance();// companion_class-common.bin
		IndunCommonBinLoader.getInstance();// indun_info-common.bin
		CatalystTableCommonBinLoader.getInstance();// catalyst_table_info-common.bin
		ElementEnchantCommonBinLoader.getInstance();// elemnet_enchant_info-common.bin
		ArmorElementCommonBinLoader.getInstance();// armor_element-common.bin
		EnchantTableInfoCommonBinLoader.getInstance();// enchant_table_info-common.bin
		EnchantScrollTableInfoCommonBinLoader.getInstance();// enchant_scroll_table_info-common.bin
		EnterMapsCommonBinLoader.getInstance();// entermaps-common.bin
		GeneralGoodsCommonBinLoader.getInstance();// general_goods-common.bin
		HuntingQuestCommonBinLoader.getInstance();// huntingquest-common.bin
		PotentialCommonBinLoader.getInstance();// potential-common.bin
		ShipCommonBinLoader.getInstance();// shipInfo-common.bin
		TreasureBoxCommonBinLoader.getInstance();// treasurebox-common.bin, treasureIslandRewardBox-common.bin
		FavorBookCommonBinLoader.getInstance();// favor_book-common.bin
		TimeCollectionCommonBinLoader.getInstance();// time_collection-common.bin
		PCMasterCommonBinLoader.getInstance();// pc_master-common.bin
		EinhasadPointCommonBinLoader.getInstance();// einhasad_point_info-common.bin
		EinhasadPointFaithCommonBinLoader.getInstance();// einhasad_point_faith_info-common.bin
		ChargedTimeMapCommonBinLoader.getInstance();// charged_time_map-common.bin
		
		if (Config.COMMON.COMMON_QUEST_BIN_LOAD) {
			QuestCommonBinLoader.getInstance();// quest-common.bin
		}
	}
	
	/**
	 * cache memory에 적재할 데이터 및 패킷
	 */
	private void initCache(){
		L1ServerType.init();
		L1ItemId.init();
		L1InterServer.init();
		L1CharacterInfo.init();
		L1SkillInfo.init();
		L1PassiveId.init();
		ItemAbilityFactory.init();
		L1NpcHtmlFactory.init();
		L1NpcIdFactory.init();
		S_CharPass.init();
		S_Notification.init();
		S_ConnectHibreedServer.init();
		S_Paralysis.init();
		S_TeamIdServerNoMappingInfo.init();
		PartyUISpellInfo.init();
		S_DialogueMessage.init();
		S_SpellPassiveOnOff.init();
		S_LoginUnknown.init();
	}

	/**
	 * 온라인중의 플레이어 모두에 대해서 kick, 캐릭터 정보의 보존을 한다.
	 */
	public void disconnectAllCharacters() {
		Collection<L1PcInstance> pcList = L1World.getInstance().getAllPlayers();
		for (L1PcInstance pc : pcList) {
			if (pc == null || pc.noPlayerCK) {
				continue;
			}
			try {
				pc.logout();
				if (pc.getNetConnection() != null) {
					pc.getNetConnection().setActiveChar(null);
					pc.getNetConnection().kick();
				}
			} catch (Exception e) {
			}
		}
	}

	public int saveAllCharInfo() {
		// exception 발생하면 -1 리턴, 아니면 저장한 인원 수 리턴
		int cnt = 0;
		try {
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				cnt++;
				pc.save();
				pc.saveInventory();
			}
		} catch (Exception e) {
			return -1;
		}
		return cnt;
	}

	/**
	 * 온라인중의 플레이어에 대해서 kick , 캐릭터 정보의 보존을 한다.
	 */
	public static void disconnectChar(L1PcInstance pc) {
		if (pc.getNetConnection() != null) {
			pc.getNetConnection().kick();
		}
		pc.logout();
	}

	public static void disconnectChar(String name) {
		L1PcInstance pc = L1World.getInstance().getPlayer(name);
		if (pc != null) {
			disconnectChar(pc);
		}
	}

	private class ServerShutdownThread extends Thread {
		private final int _secondsCount;

		public ServerShutdownThread(int secondsCount) {
			_secondsCount = secondsCount;
		}

		@Override
		public void run() {
			L1World world = L1World.getInstance();
			try {
				int secondsCount = _secondsCount;
				String countMsg = null;
				//countMsg = "안녕하세요 리니지입니다. 잠시 후 서버 안정화를 위한 서버다운이 있을 예정입니다. 유저분들께서는 안전한 장소에서 종료해 주시기 바랍니다. 감사합니다.";
				countMsg = S_SystemMessage.getRefText(73);
				world.broadcastServerMessage(countMsg, true);
				world.broadcastPacketToAll(new S_MsgAnnounce(countMsg, ColorUtil.getWhiteRgbBytes()), true);
				
				while (0 < secondsCount) {
					if (secondsCount <= 30) {
						//countMsg = String.format("서버가 %d초 후에 종료됩니다. 안전한 장소에서 종료하십시오.", secondsCount); // CHECKED OK
						countMsg = String.format(S_SystemMessage.getRefText(74) + "%d " + S_SystemMessage.getRefText(75), secondsCount);
						System.out.println(countMsg);
						world.broadcastServerMessage(countMsg, true);
					} else {
						if (secondsCount % 60 == 0) {
							//countMsg = String.format("서버가 %d분 후에 종료됩니다. 안전한 장소에서 종료하십시오.", secondsCount / 60); // CHECKED OK
							countMsg = String.format(S_SystemMessage.getRefText(74) + "%d " + S_SystemMessage.getRefText(76), secondsCount / 60);
							System.out.println(countMsg);
							world.broadcastServerMessage(countMsg, true);
						}
					}
					Thread.sleep(1000);
					secondsCount--;
				}
				shutdown();
			} catch (InterruptedException e) {
//AUTO SRM: 				world.broadcastServerMessage("서버 종료가 중단되었습니다. 서버는 정상 가동중입니다.", true); // CHECKED OK
				world.broadcastServerMessage(S_SystemMessage.getRefText(948), true);
				return;
			}
		}
	}

	protected ServerShutdownThread _shutdownThread = null;

	public synchronized void shutdownWithCountdown(int secondsCount) {
		if (_shutdownThread != null) {
			RobotAIThread.close();
			// 이미 슛다운 요구를 하고 있다
			// TODO 에러 통지가 필요할지도 모른다
			return;
		}
		_shutdownThread = new ServerShutdownThread(secondsCount);
		_shutdownThread.start();
		GameServerSetting.SHUTDOWN_SERVER = true;
	}

	public void shutdown() {
		disconnectAllCharacters();
		// manager.savelog(); //서버 다운시 서버로그 저장여부 2014년7월12일 log디비 폴더 이용 시점에 주석처리
		/** LFC **/
		InstanceLoadManager.getInstance().release();
		//ManagerInfoThread.getInstance().ServerInfoMerge();  // MANAGER DISABLED
		WebServer.getInstance().close();// 앱센터 종료
		/*Manager.display.syncExec(new Runnable() {  // MANAGER DISABLED
		    public void run() {
			    try {
				    Manager.savelog();  // MANAGER DISABLED
			    } catch (Exception localException) {
				    localException.printStackTrace();
			    }
		    }
		});*/  // MANAGER DISABLED
		System.exit(0);
	}

	public synchronized void abortShutdown() {
		if (_shutdownThread == null) {
			// 슛다운 요구를 하지 않았다
			// TODO 에러 통지가 필요할지도 모른다
			return;
		}
		_shutdownThread.interrupt();
		_shutdownThread = null;
		GameServerSetting.SHUTDOWN_SERVER = false;
	}

	private void HalloweenDelete() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			
			pstm = con.prepareStatement("DELETE FROM character_items WHERE item_id IN (20380, 21060, 256, 200172) AND enchantlvl < 0");
			pstm.execute();
			SQLUtil.close(pstm);
			
			pstm = con.prepareStatement("DELETE FROM character_elf_warehouse WHERE item_id IN (20380, 21060, 256, 200172) AND enchantlvl < 0");
			pstm.execute();
			SQLUtil.close(pstm);
			
			pstm = con.prepareStatement("DELETE FROM clan_warehouse WHERE item_id IN (20380, 21060, 256, 200172) AND enchantlvl < 0");
			pstm.execute();
			SQLUtil.close(pstm);
			
			pstm = con.prepareStatement("DELETE FROM character_warehouse WHERE item_id IN (20380, 21060, 256, 200172) AND enchantlvl < 0");
			pstm.execute();
			SQLUtil.close(pstm);
			
			pstm = con.prepareStatement("DELETE FROM character_package_warehouse WHERE item_id IN (20380, 21060, 256, 200172) AND enchantlvl < 0");
			pstm.execute();
			SQLUtil.close(pstm);
			
			pstm = con.prepareStatement("DELETE FROM character_special_warehouse WHERE item_id IN (20380, 21060, 256, 200172) AND enchantlvl < 0");
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	private void RabbitEventDelete() {// 신묘년 이벤트
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			
			pstm = con.prepareStatement("DELETE FROM character_items WHERE item_id IN (1115, 1116, 1117, 1118, 22250, 22251, 22252) AND enchantlvl < 0");
			pstm.execute();
			SQLUtil.close(pstm);
			
			pstm = con.prepareStatement("DELETE FROM character_elf_warehouse WHERE item_id IN (1115, 1116, 1117, 1118, 22250, 22251, 22252) AND enchantlvl < 0");
			pstm.execute();
			SQLUtil.close(pstm);
			
			pstm = con.prepareStatement("DELETE FROM clan_warehouse WHERE item_id IN (1115, 1116, 1117, 1118, 22250, 22251, 22252) AND enchantlvl < 0");
			pstm.execute();
			SQLUtil.close(pstm);
			
			pstm = con.prepareStatement("DELETE FROM character_warehouse WHERE item_id IN (1115, 1116, 1117, 1118, 22250, 22251, 22252) AND enchantlvl < 0");
			pstm.execute();
			SQLUtil.close(pstm);
			
			pstm = con.prepareStatement("DELETE FROM character_package_warehouse WHERE item_id IN (1115, 1116, 1117, 1118, 22250, 22251, 22252) AND enchantlvl < 0");
			pstm.execute();
			SQLUtil.close(pstm);
			
			pstm = con.prepareStatement("DELETE FROM character_special_warehouse WHERE item_id IN (1115, 1116, 1117, 1118, 22250, 22251, 22252) AND enchantlvl < 0");
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	public boolean serverReset(){
		for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
			if (player == null) {
				continue;
			}
			player.getNetConnection().kick();
		}
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("CALL SERVER_RESET()");
			if (pstm.executeUpdate() > 0) {
				//System.out.println("★☆★ 서버 초기화가 완료되었습니다. ★☆★");
				System.out.println("★☆★ Server initialization completed. ★☆★");
				return true;
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
		return false;
	}

}

