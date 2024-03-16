package l1j.server.server.command.executor;

import l1j.server.Config;
import l1j.server.GameSystem.attendance.AttendanceTable;
import l1j.server.GameSystem.beginnerquest.BeginnerQuestTable;
import l1j.server.GameSystem.craft.CraftInfoLoader;
import l1j.server.GameSystem.dungeontimer.loader.L1DungeonTimerLoader;
import l1j.server.GameSystem.huntingquest.HuntingQuestTable;
import l1j.server.GameSystem.smelting.SmeltingLoader;
import l1j.server.IndunSystem.ruun.RuunLoader;
import l1j.server.LFCSystem.Loader.LFCCompensateLoader;
import l1j.server.RobotSystem.RobotAIThread;
import l1j.server.common.bin.ShipCommonBinLoader;
import l1j.server.server.controller.action.UserRanking;
import l1j.server.server.controller.action.War;
import l1j.server.server.datatables.AdenShopTable;
import l1j.server.server.datatables.ArmorSetTable;
import l1j.server.server.datatables.BalanceTable;
import l1j.server.server.datatables.BossSpawnTable;
import l1j.server.server.datatables.CastleTable;
import l1j.server.server.datatables.CatalystTable;
import l1j.server.server.datatables.CharactersGiftItemTable;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.datatables.ConnectRewardTable;
import l1j.server.server.datatables.DropTable;
import l1j.server.server.datatables.EnchantResultTable;
import l1j.server.server.datatables.EventTable;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.FreePVPRegionTable;
import l1j.server.server.datatables.IpTable;
import l1j.server.server.datatables.ItemBoxTable;
import l1j.server.server.datatables.ItemBuffTable;
import l1j.server.server.datatables.ItemClickMessageTable;
import l1j.server.server.datatables.ItemSelectorTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.MagicDollInfoTable;
import l1j.server.server.datatables.ManagerUserTeleportTable;
import l1j.server.server.datatables.MapFixKeyTable;
import l1j.server.server.datatables.MapTypeTable;
import l1j.server.server.datatables.MapsTable;
import l1j.server.server.datatables.MobSkillTable;
import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.datatables.NotificationTable;
import l1j.server.server.datatables.NpcActionTeleportTable;
import l1j.server.server.datatables.NpcCashShopSpawnTable;
import l1j.server.server.datatables.NpcChatTable;
import l1j.server.server.datatables.NpcInfoTable;
import l1j.server.server.datatables.NpcShopSpawnTable;
import l1j.server.server.datatables.NpcShopTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.PenaltyItemTable;
import l1j.server.server.datatables.PlaySupportTable;
import l1j.server.server.datatables.PolyTable;
import l1j.server.server.datatables.ResolventTable;
import l1j.server.server.datatables.ServerExplainTable;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.datatables.SkillsInfoTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.datatables.SpawnTable;
import l1j.server.server.datatables.SpellMeltTable;
import l1j.server.server.datatables.SpellProbabilityTable;
import l1j.server.server.datatables.UBTable;
import l1j.server.server.datatables.WarTimeTable;
import l1j.server.server.datatables.WeaponAddDamage;
import l1j.server.server.datatables.WeaponSkillTable;
import l1j.server.server.model.Beginner;
import l1j.server.server.model.Dungeon;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.exp.L1ExpPlayer;
import l1j.server.server.model.item.L1Fishing;
import l1j.server.server.model.item.L1TreasureBox;
import l1j.server.server.model.item.ablity.enchant.L1EnchantLoader;
import l1j.server.server.model.item.collection.L1CollectionLoader;
import l1j.server.server.model.item.collection.time.loader.L1TimeCollectionLoader;
import l1j.server.server.model.item.function.L1HealingPotion;
import l1j.server.server.model.skill.L1SkillActionFactory;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1Reload implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Reload();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1Reload() {}

	@Override
	public boolean execute(L1PcInstance gm, String cmdName, String arg) {
		switch(arg){
		case "spellprobability":
			SpellProbabilityTable.getInstance();
			gm.sendPackets(new S_SystemMessage("Reload: The SpellProbability table has been reloaded."), true);
			return true;
		//case "세트아이템":
		case "armorset":
			ArmorSetTable.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: ArmorSetTable 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(565), true), true);
			return true;
		//case "스폰리스트":
		case "spawnlist":
			SpawnTable.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: SpawnTable 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(566), true), true);
			return true;
		//case "몹드랍":
		case "mobdrop":
			DropTable.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: droplist 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(567), true), true);
			return true;
		//case "엔피씨액션":
		case "npcaction":
			NPCTalkDataTable.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: NpcAction 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(568), true), true);
			return true;
		//case "엔피씨텔":
		case "npctel":
			NpcActionTeleportTable.getInstance().reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: NpcActionTeleportTable 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(569), true), true);
			return true;
		//case "엔피씨":
		case "npc":
			NpcTable.reload();
			NpcInfoTable.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: NpcTable 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(570), true), true);
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: NpcInfoTable 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(571), true), true);
			return true;
		//case "변신":
		case "poly":
			PolyTable.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: polymorphs 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(572), true), true);
			return true;
		//case "용해제":
		case "solvent":
			ResolventTable.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: resolvent 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(573), true), true);
			return true;
		//case "박스":
		case "treasure":
			L1TreasureBox.load();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: TreasureBox.xml 파일이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(574), true), true);
			return true;
		//case "물약회복":
		case "healpotion":
			L1HealingPotion.load();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: HealingPotion.xml 파일이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(575), true), true);
			return true;
		//case "컨피그":case "서버설정":case "콘피그":
		case "config":case "settings":
			Config.load();
			L1ExpPlayer.reloadLimitExp();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: config 폴더에 파일이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(576), true), true);
			return true;
		//case "촉매":
		case "catalyst":
			CatalystTable.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: CatalystTable 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(577), true), true);
			return true;
		//case "무한대전":
		case "infinitewar":
			UBTable.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: 무한대전 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(578), true), true);
			return true;
		//case "경험치":
		case "experience":
			ExpTable.getInstance().loadExp(true);
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: Exp 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(579), true), true);
			return true;
		//case "스킬":
		case "skill":
			SkillsTable.reload();
			SkillsInfoTable.getInstance().reload();
			L1SkillActionFactory.getInstance().reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: Skill 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(580), true), true);
			return true;
		//case "몹스킬":
		case "mobskill":
			MobSkillTable.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: mobskill 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(581), true), true);
			return true;
		//case "영자상점":
		case "npcshop":
			NpcShopSpawnTable.reloding();
			NpcShopTable.reloding();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: NpcShopTable 테이블이 리로드 되었습니다."), true);	 // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(582), true), true);
			return true;
		//case "무기스킬":
		case "weaponskill":
			WeaponSkillTable.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: WeaponSkill 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(583), true), true);
			return true;
		//case "레벨퀘스트":
		case "quest":
			CharactersGiftItemTable.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: characters_levelup_item 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(584), true), true);
			return true;
		//case "밴아이피":
		case "banip":
			IpTable.getInstance();
			IpTable.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: banIp 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(585), true), true);
			return true;
		//case "아이템":
		case "item":
			ItemTable.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: 아이템 정보가 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(586), true), true);
			return true;
		//case "상점":
		case "shop":
			ShopTable.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: shop 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(587), true), true);
			return true;
		//case "무기대미지":
		case "weapondamage":
			WeaponAddDamage.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: weapon_damege 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(588), true), true);
			return true;
		//case "클랜데이터":
		case "pledge":
			ClanTable.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: clan_data 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(589), true), true);
			return true;
		//case "공성":
		case "castle":		
			CastleTable.reload();
			War.getInstance().reloadTime();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: castle 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(590), true), true);
			return true;
		//case "엔캐샵스폰리스트":
		case "npccashshop":
			NpcCashShopSpawnTable.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: spawnlist_npc_cash_shop 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(591), true), true);
			return true;
		//case "엔피씨채팅":
		case "npcchat":
			NpcChatTable.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: npcchat 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(592), true), true);
			return true;
		//case "던전":
		case "dungeon":
			Dungeon.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: dungeon 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(593), true), true);
			return true;
		//case "맵":
		case "maps":
			MapsTable.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: mapids 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(594), true), true);
			return true;
		//case "맵픽스":
		case "mapfix":
			MapFixKeyTable.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: MapFixKey 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(595), true), true);
			return true;
		//case "맵타입":
		case "maptype":
			MapTypeTable.getInstance().reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: MapTypeTable 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(596), true), true);
			return true;
		//case "신규지급템":
		case "beginner":
			Beginner.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: beginner 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(597), true), true);
			return true;
		//case "보스":
		case "boss":
			BossSpawnTable.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: BossSpawnTable 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(598), true), true);
			return true;
		//case "아덴상점":
		case "adenshop":
			AdenShopTable.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: AdenShop 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(599), true), true);
			return true;
		//case "로봇":
		case "ai":
			RobotAIThread.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: RobotAIThread 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(600), true), true);
			return true;
		//case "출첵":
		case "attendance":
			AttendanceTable.getInstance().reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: AttendanceTable 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(601), true), true);
			return true;
		//case "매니저텔":
		case "teleport":
			ManagerUserTeleportTable.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: manager_user_teleport 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(602), true), true);
			return true;
		case "lfc":
			LFCCompensateLoader.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: TB_LFCCOMPENSATE 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(603), true), true);
			return true;
		//case "제작":
		case "craft":
			CraftInfoLoader.reload(); 
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: CraftInfoLoader 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(604), true), true);
			return true;
		//case "플레이서포트":
		case "pss":
			PlaySupportTable.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: PlaySupportTable 테이블이 리로드 되었습니다."), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(605), true), true);
			return true;
		//case "퀘스트":
		case "beginnerquest":
			BeginnerQuestTable.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: BeginnerQuestTable 테이블이 리로드 되었습니다"), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(606), true), true);
			return true;
		//case "인형정보":
		case "doll":
			MagicDollInfoTable.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: MagicDollInfoTable 테이블이 리로드 되었습니다"), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(607), true), true);
			return true;
		//case "패널티":
		case "penalty":
			PenaltyItemTable.getInstance().reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: PenaltyItemTable 테이블이 리로드 되었습니다"), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(608), true), true);
			return true;
		//case "아이템클릭":
		case "itemclickmessage":
			ItemClickMessageTable.getInstance().reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: ItemClickMessageTable 테이블이 리로드 되었습니다"), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(609), true), true);
			return true;
		//case "랭킹":
		case "rank":
			if (Config.RANKING.RANKING_SYSTEM_ACTIVE) {
				UserRanking.reload();
//AUTO SRM: 				gm.sendPackets(new S_SystemMessage("랭킹이 갱신 되었습니다."), true); // CHECKED OK
				gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(610), true), true);
			} else {
//AUTO SRM: 				gm.sendPackets(new S_SystemMessage("랭킹시스템이 미작동중입니다."), true); // CHECKED OK
				gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(611), true), true);
			}
			return true;
		//case "사냥터도감":
		case "huntingquest":
			HuntingQuestTable.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: HuntCollectTable 테이블이 리로드 되었습니다"), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(612), true), true);
			return true;
		//case "스폰루운":
		case "ruun":
			RuunLoader.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: spawnlist_ruun 테이블이 리로드 되었습니다"), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(613), true), true);
			return true;
		//case "아이템셀렉트":
		case "itemselector":
			ItemSelectorTable.getInstance().reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: ItemSelectorTable 테이블이 리로드 되었습니다"), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(614), true), true);
			return true;
		//case "인챈트이미지":
		case "enchantresult":
			EnchantResultTable.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: EnchantResultTable 테이블이 리로드 되었습니다"), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(615), true), true);
			return true;
		//case "서버설명":
		case "serverexplain":
			ServerExplainTable.getInstance().reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: ServerExplainTable 테이블이 리로드 되었습니다"), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(616), true), true);
			return true;
		//case "낚시":
		case "fishing":
			L1Fishing.load();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: Fishing.xml 테이블이 리로드 되었습니다"), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(617), true), true);
			return true;
		//case "스킬용해":
		case "spellmelt":
			SpellMeltTable.getInstance().reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: SpellMeltTable 테이블이 리로드 되었습니다"), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(618), true), true);
			return true;
		//case "공성시간":
		case "wartime":
			WarTimeTable.getInstance().reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: WarTimeTable 테이블이 리로드 되었습니다"), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(619), true), true);
			return true;
		//case "배시간":
		case "shipcommonbin":
			ShipCommonBinLoader.getInstance().reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: ShipContentLoader 테이블이 리로드 되었습니다"), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(620), true), true);
			return true;
		//case "컬렉션":
		case "collection":
			L1CollectionLoader.getInstance().reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: L1CollectionLoader 테이블이 리로드 되었습니다"), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(621), true), true);
			return true;
		//case "셀렉티스":
		case "timecollection":
			L1TimeCollectionLoader.getInstance().reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: L1TimeCollectionLoader 테이블이 리로드 되었습니다"), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(622), true), true);
			return true;
		//case "박스아이템":
		case "itembox":
			ItemBoxTable.getInstance().reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: ItemBoxTable 테이블이 리로드 되었습니다"), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(623), true), true);
			return true;
		//case "버프아이템":
		case "itembuff":
			ItemBuffTable.getInstance().reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: ItemBuffTable 테이블이 리로드 되었습니다"), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(624), true), true);
			return true;
		//case "인챈트옵션":
		case "enchant":
			L1EnchantLoader.getInstance().reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: L1EnchantAblityLoader 테이블이 리로드 되었습니다"), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(625), true), true);
			return true;
		//case "던전타이머":
		case "dungeontimer":
			L1DungeonTimerLoader.getInstance().reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: L1DungeonTimerLoader 테이블이 리로드 되었습니다"), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(626), true), true);
			return true;
		//case "밸런스":
		case "balance":
			BalanceTable.getInstance().reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: BalanceTable 테이블이 리로드 되었습니다"), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(627), true), true);
			return true;
		//case "이벤트":
		case "event":
			EventTable.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: EventTable 테이블이 리로드 되었습니다"), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(628), true), true);
			return true;
		//case "알림":
		case "notification":
			NotificationTable.getInstance().reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: NotificationTable 테이블이 리로드 되었습니다"), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(629), true), true);
			return true;
		//case "제련석":
		case "smelting":
			SmeltingLoader.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: SmeltingLoader 테이블이 리로드 되었습니다"), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(630), true), true);
			return true;
		case "FREEPVP":
			FreePVPRegionTable.getInstance().reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: FreePVPRegionTable 테이블이 리로드 되었습니다"), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(631), true), true);
			return true;
		//case "접속보상":
		case "connectreward":
			ConnectRewardTable.reload();
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage("리로드: ConnectRewardTable 테이블이 리로드 되었습니다"), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(632), true), true);
			return true;
		default:
			print(gm, cmdName);
			return false;
		}		
	}
	
	private static S_SystemMessage MESSAGE;
	private void print(L1PcInstance gm, String cmdName){
		if (MESSAGE == null) {
			MESSAGE = createMessage();
		}
		gm.sendPackets(MESSAGE);
	}
	
	private S_SystemMessage createMessage(){
		StringBuilder sb = new StringBuilder();
		/*sb.append("	★☆★ RELOAD COMMAND ★☆★\n")
			.append(" : 몹드랍.변신.상점.박스.스폰리스트.세트아이템\n")
			.append(" : 스킬.몹스킬\n")
			.append(" : 아이템.무기대미지.무기스킬.레벨퀘스트.인챈트옵션\n")
			.append(" : 컨피그.물약회복.밴아이피.용해제.배시간\n")
			.append(" : 엔피씨.엔피씨액션.보스\n")
			.append(" : 엔캐샵스폰리스트.엔피씨채팅.엔피씨텔.사냥터도감\n")
			.append(" : 던전.던전타이머.맵.맵픽스.맵타입.이벤트\n")
			.append(" : 클랜데이터.아지트.공성.공성시간\n")
			.append(" : 아덴상점.영자상점.출첵.제작.랭킹.엔피씨텔\n")
			.append(" : 밸런스.신규지급템.촉매.알림\n")
			.append(" : 무한대전.변신.경험치.플레이서포트.퀘스트\n")
			.append(" : 아이템클릭.아이템셀렉트.인형정보.패널티\n")
			.append(" : 인챈트이미지.서버설명.낚시.스킬용해\n")
			.append(" : 스폰루운.엔샵.컬렉션.박스아이템.버프아이템\n")
			.append(" : 셀렉티스.제련석.FREEPVP.접속보상");*/
			sb.append("-------- RELOAD COMMAND --------\n")
			.append(" : armorset, spawnlist, mobdrop, npcaction, npctel, npc, poly, solvent, shop\n")
			.append(" : treasure, healpotion, config, settings, catalyst, infinitewar, experience\n")
			.append(" : skill, mobskill, npcshop, weaponskill, quest, banip, item, connectreward\n")
			.append(" : weapondamage, pledge, castle, npccashshop, npcchat, dungeon, maps, mapfix\n")
			.append(" : maptype, beginner, boss, adenshop, ai, attendance, teleport, lfc, craft\n")
			.append(" : pss, beginnerquest, doll, penalty, itemclickmessage, rank, huntingquest\n")
			.append(" : ruun, itemselector, enchantresult, serverexplain, fishing, spellmelt\n")
			.append(" : wartime, shipcommonbin, collection, timecollection, itembox, itembuff\n")
			.append(" : enchant, dungeontimer, balance, event, notification, smelting, FREEPVP\n");
		
		return new S_SystemMessage(sb.toString());
	}
}


