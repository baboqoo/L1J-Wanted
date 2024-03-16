package l1j.server.server.clientpackets;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Random;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.GameSystem.charactertrade.CharacterTradeHandler;
import l1j.server.GameSystem.charactertrade.CharacterTradeManager;
import l1j.server.server.ActionCodes;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.construct.L1InterServer;
import l1j.server.server.construct.L1RegionStatus;
import l1j.server.server.construct.item.L1ItemNormalType;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.construct.skill.L1SkillInfo;
import l1j.server.server.controller.CrockController;
import l1j.server.server.datatables.HouseTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.ServerExplainTable;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1HouseLocation;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1ItemDelay;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1TowerInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.item.L1TreasureBox;
import l1j.server.server.model.item.function.Additem;
import l1j.server.server.model.item.function.PledgeItem;
import l1j.server.server.model.item.function.NBuff;
import l1j.server.server.model.poison.L1DamagePoison;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.model.skill.L1SkillUseType;
import l1j.server.server.serverpackets.S_Board;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_Sound;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.serverpackets.action.S_UseAttackSkill;
import l1j.server.server.serverpackets.inventory.S_ChangeItemUse;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.polymorph.S_ShowPolyList;
import l1j.server.server.serverpackets.shop.S_ShopSellAden;
import l1j.server.server.templates.L1DogFightTicket;
import l1j.server.server.templates.L1EtcItem;
import l1j.server.server.templates.L1House;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1RaceTicket;
import l1j.server.server.utils.L1SpawnUtil;

public class C_ItemUSe extends ClientBasePacket {
	private static final String C_ITEM_USE = "[C] C_ItemUSe";
	private static Logger _log = Logger.getLogger(C_ItemUSe.class.getName());
	private static final Random random = new Random(System.nanoTime());
	
	private L1PcInstance pc;
	private L1PcInventory inv;
	private L1ItemInstance useItem;
	private int itemId;
	
	@SuppressWarnings("unused")
	private L1ItemInstance targetItem;
	private int etcValue;
	
	/**
	 * 상속 처리된 아이템 인스턴스 업무 처리
	 */
	void doWork() {
		int minLevel = useItem.getItem().getMinLevel();
		int maxLevel = useItem.getItem().getMaxLevel();
		if (minLevel != 0 && minLevel > pc.getLevel() && !pc.isGm()) {
			//pc.sendPackets(new S_SystemMessage(String.format("%d레벨 이상이 되면 사용할 수 있는 아이템입니다.", minLevel)), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(20), String.valueOf(minLevel)), true);
			return;
		}
		if (maxLevel != 0 && maxLevel < pc.getLevel() && !pc.isGm()) {
			//pc.sendPackets(new S_SystemMessage(String.format("%d레벨 이하일 때만 사용할 수 있는 아이템입니다.", maxLevel)), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(21), String.valueOf(maxLevel)), true);
			return;
		}
		useItem.clickItem(pc, this);// 실행
	}

	public C_ItemUSe(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);
		pc = client.getActiveChar();
		if (pc == null || pc.isGhost() || pc.isDead() || pc.getCurrentHp() <= 0 || pc.getTeleport().isTeleport() || pc.getMapId() == 5166) {
			return;
		}
		// 존재버그 관련 추가
		L1PcInstance other = L1World.getInstance().getPlayer(pc.getName());
		if (other == null && pc.getAccessLevel() != Config.ALT.GMCODE) {
			pc.sendPackets(L1SystemMessage.TWO_CHAR_CHECK);
			pc.denals_disconnect(String.format("[C_ItemUSe] WORLD_NOT_FOUND_USER : NAME(%s)", pc.getName()));
			return;
		}
		if (!pc.getMap().isUsableItem()) {
			pc.sendPackets(L1ServerMessage.sm563);// 여기에서는 사용할 수 없습니다.
			return;
		}
		
		int itemObjid	= readD();// 오브젝트 아이디 취득
		inv				= pc.getInventory();
		useItem			= inv.getItem(itemObjid);
		
		if (useItem == null || useItem.getItem() == null || useItem.getItem() instanceof L1RaceTicket || useItem.getItem() instanceof L1DogFightTicket) {
			return;
		}
		
		L1ItemType interaction_type = L1ItemType.fromInt(useItem.getItem().get_interaction_type());
		if (interaction_type == L1ItemType.NONE) {// none:사용할 수 없는 아이템
			pc.sendPackets(new S_ServerMessage(74, useItem.getLogNameRef()), true);// %s은 사용할 수 없습니다.
			return;
		}
		try {
			itemId = useItem.getItem().getItemId();
		} catch (Exception e) {
			return;
		}
		
		if (useItem.getItem() instanceof L1EtcItem) {
			switch(useItem.getItem().getLimitType()){
			case BEGIN_ZONE:
				if (!pc.getMap().isBeginZone()) {
					if (itemId == 40099 || itemId == 30086 || itemId == 40095) {
						pc.sendPackets(S_Paralysis.TELEPORT_UNLOCK);
					}
					pc.sendPackets(L1ServerMessage.sm563); // \f1 여기에서는 사용할 수 없습니다.
					return;
				}
				break;
			case WORLD_WAR:
				if (!pc.getMap().isInterWarZone()) {
					if (itemId == 130043 || itemId == 130044) {
						pc.sendPackets(S_Paralysis.TELEPORT_UNLOCK);
					}
					pc.sendPackets(L1ServerMessage.sm563); // \f1 여기에서는 사용할 수 없습니다.
					return;
				}
				break;
			default:
				break;
			}
		}
		
		// 아이템 사용 기능 상속 여부
		if (useItem.isWorking()) {
			doWork();
			return;
		}
		
		etcValue =  useItem.getItem().getEtcValue();
		
		if (itemId == CharacterTradeManager.MARBLE_LOAD_ID) {
			CharacterTradeHandler.load(pc, useItem);
			return;
		}
		
		int targetId = 0;
		int spellscObjId = 0, spellscX = 0, spellscY = 0;
		//int cookStatus = 0, cookNo = 0;
		int useObjId = 0;
		switch (itemId) {
		case 410016:
			targetId			= readD();
			break;
		default:
			if (itemId == CharacterTradeManager.MARBLE_STORE_ID) {// 캐릭터 저장 구슬
				useObjId		= readD();
			} else if (interaction_type == L1ItemType.SPELL_LONG || interaction_type == L1ItemType.SPELL_SHORT) {// 대상 선택
				spellscObjId	= readD();
				spellscX		= readH();
				spellscY		= readH();
			} else if (itemId >= 41255 && itemId <= 41259) {// 요리책
				//cookStatus	= readC();
				//cookNo		= readC();
			} else {
				targetId		= readC();
			}
			break;
		}

		int delayId = useItem.getItem().getItemType() == L1ItemType.NORMAL && useItem.getItem() instanceof L1EtcItem ? ((L1EtcItem) useItem.getItem()).getDelayId() : 0;
		if (delayId != 0 && pc.hasItemDelay(delayId)) {// 지연 설정 있어
			return;
		}
		
		// 재사용 체크
		boolean isDelayEffect = false;
		if (useItem.getItem().getItemType() == L1ItemType.NORMAL) {
			int delayEffect = ((L1EtcItem) useItem.getItem()).getDelayEffect();
			if (delayEffect > 0) {
				isDelayEffect = true;
				Timestamp lastUsed = useItem.getLastUsed();
				if (lastUsed != null) {
					long ms		= Calendar.getInstance().getTimeInMillis();
					long last	= lastUsed.getTime();
					if ((ms - last) / 1000 <= delayEffect) {
						//String message = String.format("%d분 %d초 후에 사용할 수 있습니다.", (delayEffect - (ms - last) / 1000) / 60, (delayEffect - (ms - last) / 1000) % 60);
						pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(98), String.valueOf((delayEffect - (ms - last) / 1000) / 60), String.valueOf((delayEffect - (ms - last) / 1000) % 60)), true);
						return;
					}
				}
			}
		}
		
		if (targetId > 0) {
			targetItem = inv.getItem(targetId);
			_log.finest("request item use (obj) = " + itemObjid + " action = " + targetId);
		}

		if (useItem.getItem().getItemType() == L1ItemType.NORMAL) { // 종별：그 외의 아이템
			int minLevel = ((L1EtcItem) useItem.getItem()).getMinLevel();
			int maxLevel = ((L1EtcItem) useItem.getItem()).getMaxLevel();
			if (minLevel != 0 && minLevel > pc.getLevel() && !pc.isGm()) {
				//pc.sendPackets(new S_SystemMessage(String.format("%d레벨 이상이 되면 사용할 수 있는 아이템입니다.", minLevel)), true);
				pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(20), String.valueOf(minLevel)), true);
				pc.sendPackets(S_Paralysis.TELEPORT_UNLOCK);
				return;
			}
			if (maxLevel != 0 && maxLevel < pc.getLevel() && !pc.isGm()) {
				//pc.sendPackets(new S_SystemMessage(String.format("%d레벨 이하일 때만 사용할 수 있는 아이템입니다.", maxLevel)), true);
				pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(21), String.valueOf(maxLevel)), true);
				pc.sendPackets(S_Paralysis.TELEPORT_UNLOCK);
				return;
			}
			if ((itemId == 40576 && !pc.isElf()) || (itemId == 40577 && !pc.isWizard()) // 영혼의 결정의 파편(흑)
					|| (itemId == 40578 && !pc.isKnight())) { // 영혼의 결정의 파편(빨강)
				pc.sendPackets(L1ServerMessage.sm264); // \f1당신의 클래스에서는 이 아이템은 사용할 수 없습니다.
				return;
			}

			if (useItem.getItem().getType() == L1ItemNormalType.TREASURE_BOX.getId()) { // treasure_box
				if (inv.getSize() > L1PcInventory.MAX_SIZE - 5 || inv.getWeightPercent() >= 98) {
					pc.sendPackets(L1ServerMessage.sm82); // 무게 게이지가 부족하거나 인벤토리가 꽉차서 더 들 수 없습니다.
					return;
				}
				if ((itemId == 420093 && inv.checkItem(420092))// 시공의 열쇠 상자
						|| (itemId == 420095 && inv.checkItem(420096))) {// 완전한 시공의 열쇠 상자 
					pc.sendPackets(L1ServerMessage.sm2887);// 특정 아이템이 있어 아직은 사용할 수 없습니다.
					return;
				}
				L1TreasureBox box = L1TreasureBox.get(itemId);
				if (box != null && box.open(pc)) {
					if (((L1EtcItem) useItem.getItem()).getDelayEffect() > 0) {
						isDelayEffect = true;
					} else {
						inv.removeItem(useItem.getId(), 1);
					}
				}
			}

			switch(itemId){// 스위치문시작
			case 43000:// 환생의 물약
				reincarnationPotion();
				break;
			case 40858:// 술
				pc.setDrink(true);
				inv.removeItem(useItem, 1);
				break;
			case 30060:// 픽시 변신막대
				pc.sendPackets(new S_ShowPolyList(pc.getId(), "pixies"), true);
				if (!pc.isMagicItem()) {
					pc.setMagicItem(true);
					pc.setMagicItemId(itemId);
				}
				break;
			case 600213:case 600214:case 600215:case 600216:case 600217:case 600198:case 600199:
			case 600200:case 600201:case 600202:case 600203:case 600204:case 600205:case 600206:case 600207:
			case 600208:case 600209:case 600210:
				NBuff.excute(pc, itemId, useItem);
				break;
			case 600218:
				NBuff.reset(pc, itemId, useItem);
				break;
			case CharacterTradeManager.MARBLE_STORE_ID:
				CharacterTradeHandler.save(pc, useItem, useObjId);
				break;
			case 40097:// 상아탑의 저주 풀기 주문서
			case 40119:// 저주 풀기 주문서
			case 140119:
			case 140329:// 원주민의 토템
				curseSolveScroll();
				break;
			case 830032:case 830033:case 830034:case 830035:case 830036:case 830037:case 830038:case 830039:case 830040:case 830041:// 봉인된 오만의 탑 이동 부적 1~10층
				inv.removeItem(useItem, 1);
				L1ItemInstance item1 = inv.storeItem(itemId - 20, 1);
				if (item1 != null) {
					pc.sendPackets(new S_ServerMessage(403, item1.getLogNameRef()), true);
				}
				break;
			case 40566:// 신비한 소라 껍데기
				if (pc.isElf()
						&& (pc.getX() >= 33971 && pc.getX() <= 33975) && (pc.getY() >= 32324 && pc.getY() <= 32328) && pc.getMapId() == 4 // 상아의 탑의 마을의 남쪽에 있는 매직 스퀘어의 좌표
						&& !inv.checkItem(40548)) {// 망령의 봉투
					spawnMonster(45300, 0);// 고대인의 망령
				} else {
					pc.sendPackets(L1ServerMessage.sm79); // \f1 아무것도 일어나지 않았습니다.
				}
				break;
			case 40557:// 살생부 (글루딘 마을)
				if (pc.getX() == 32620 && pc.getY() == 32641 && pc.getMapId() == 4) {
					spawnMonster(45883, 300000);
				} else {
					pc.sendPackets(L1ServerMessage.sm79);
				}
				break;
			case 40558:// 살생부 (기란 마을)
				if (pc.getX() == 33513 && pc.getY() == 32890 && pc.getMapId() == 4) {
					spawnMonster(45889, 300000);
				} else {
					pc.sendPackets(L1ServerMessage.sm79);
				}
				break;
			case 40559:// 살생부 (아덴 마을)
				if (pc.getX() == 34215 && pc.getY() == 33195 && pc.getMapId() == 4) {
					spawnMonster(45888, 300000);
				} else {
					pc.sendPackets(L1ServerMessage.sm79);
				}
				break;
			case 40560:// 살생부 (우드벡 마을)
				if (pc.getX() == 32580 && pc.getY() == 33260 && pc.getMapId() == 4) {
					spawnMonster(45886, 300000);
				} else {
					pc.sendPackets(L1ServerMessage.sm79);
				}
				break;
			case 40561:// 살생부 (켄트 마을)
				if (pc.getX() == 33046 && pc.getY() == 32806 && pc.getMapId() == 4) {
					spawnMonster(45885, 300000);
				} else {
					pc.sendPackets(L1ServerMessage.sm79);
				}
				break;
			case 40562:// 살생부 (하이네 마을)
				if (pc.getX() == 33447 && pc.getY() == 33476 && pc.getMapId() == 4) {
					spawnMonster(45887, 300000);
				} else {
					pc.sendPackets(L1ServerMessage.sm79);
				}
				break;
			case 40563:// 살생부 (화전민 마을)
				if (pc.getX() == 32730 && pc.getY() == 32426 && pc.getMapId() == 4) {
					spawnMonster(45884, 300000);
				} else {
					pc.sendPackets(L1ServerMessage.sm79);
				}
				break;
			case 40700:// 실버 플룻
				pc.broadcastPacketWithMe(S_Sound.SILVER_FLOT_SOUND);
				if ((pc.getX() >= 32619 && pc.getX() <= 32623) && (pc.getY() >= 33120 && pc.getY() <= 33124) && pc.getMapId() == 440) {// 해적 시마마에반매직 스퀘어 좌표
					spawnMonster(45875, 0);
				}
				break;
			case 41121:// 카헬의 계약서
				if (pc.getQuest().getStep(L1Quest.QUEST_SHADOWS) == L1Quest.QUEST_END || inv.checkItem(41122, 1)) {
					pc.sendPackets(L1ServerMessage.sm79);
				} else {
					inv.createItem(41122, 1);
				}
				break;
			case 41130:// 혈흔의 계약서
				if (pc.getQuest().getStep(L1Quest.QUEST_DESIRE) == L1Quest.QUEST_END || inv.checkItem(41131, 1)) {
					pc.sendPackets(L1ServerMessage.sm79);
				} else {
					inv.createItem(41131, 1);
				}
				break;
			case 810010:// 중앙사원황금상자
				fantasyGoldBox();
				break;
			case 810006:case 810007:case 30055:case 420103:case 420105:case 420106:case 420107:case 420109:case 420110:
				wandSpellNormal();
				break;
			case 420104:case 420108:case 420111:case 810011:
				wandSpellLong(spellscObjId, spellscX, spellscY);
				break;
			
			case 41345:// 산성의 유액
				if (pc.getRegion() == L1RegionStatus.SAFETY) {
					pc.sendPackets(L1ServerMessage.sm79); // \f1 아무것도 일어나지 않았습니다.
					return;
				}
				L1DamagePoison.doInfection(pc, pc, 3000, 5);
				inv.removeItem(useItem, 1);
				break;
			case 7777:// 아덴 상점
				long curtime = System.currentTimeMillis() / 1000;
				if (pc.getQuizTime() + 2 > curtime) {
					return;
				}
				pc.sendPackets(new S_ShopSellAden(pc), true);
				pc.setQuizTime(curtime);
				break;
			case 1000010:// 지급템상자
			case 2300080:// 영웅의 유물 상자
				Additem.clickItem(pc, itemId, useItem);
				break;
			case 140050:// 요리사의 간식 주머니
				cookerPackage();
				break;
			case 2000032:// 영웅의 부츠 상자
			case 2000033:// 영웅의 투구 상자
			case 3000020:// 드래곤의 부츠 상자
			case 900058:// 영웅의 각반 상자
			case 22265:// 봉인된 반역자의 투구
			case 2300151:// 아덴의 신속 가호 상자
				cashTimeItemCreate();
				break;
			case 1300072:// 진 데스나이트의 유물주머니 
			case 1300073:// 강화된 진 데스나이트의 유물주머니 완력
			case 1300074:// 강화된 진 데스나이트의 유물주머니 민첩
			case 1300075:// 강화된 진 데스나이트의 유물주머니 지식
			case 2300072:// 영웅의 유물주머니 
			case 2300073:// 강화된 영웅의 유물주머니 완력
			case 2300074:// 강화된 영웅의 유물주머니 민첩
			case 2300075:// 강화된 영웅의 유물주머니 지식
			case 2300088:// 향상된 영웅의 유물주머니 완력
			case 2300089:// 향상된 영웅의 유물주머니 민첩
			case 2300090:// 향상된 영웅의 유물주머니 지식
				runInventoryCreate();
				break;
			case 41303:// 큰 은빛 베리아나
				bigSilverBerryana();
				break;
			case 41304:// 큰 금빛 베리아나
				bigGoldBerryana();
				break;
			case 600334:// 고대의 은빛 베리아나
				godSilverBerryana();
				break;
			case 600337:// 고대의 금빛 베리아나
				godGoldBerryana();
				break;
			case 400246:// 킬뎃 초기화
				resetKillAndDeath();
				break;
			case 490028:// 라우풀물약
				alignmentPotion(true);
				break;
			case 490029:// 카오틱물약
				alignmentPotion(false);
				break;
			case 90053:// 예술가의 마안
				artMaan();
			    break;
			case 87054: // 호랑이 사육장
			case 87055: // 진돗개 바구니
				spawnMonster(itemId == 87054 ? 45313 : 45711, 120000);
				inv.removeItem(useItem, 1);
				break;
			case 42501:// 스톰 워크
				pc.getTeleport().start(spellscX, spellscY, pc.getMapId(), pc.getMoveState().getHeading(), false);
				break;
			case 40615:// 그림자의 신전 2층의 열쇠
				if ((pc.getX() >= 32701 && pc.getX() <= 32705) && (pc.getY() >= 32894 && pc.getY() <= 32898) && pc.getMapId() == 522) {// 그림자의 신전  1F
					pc.getTeleport().start(((L1EtcItem) useItem.getItem()).getLocX(), ((L1EtcItem) useItem.getItem()).getLocY(), ((L1EtcItem) useItem.getItem()).getMapId(), 5, true);
				} else {
					pc.sendPackets(L1ServerMessage.sm279);
				}
				break;
			case 40616:case 40782:case 40783:// 그림자의 신전 3층의 열쇠
				if ((pc.getX() >= 32698 && pc.getX() <= 32702) && (pc.getY() >= 32894 && pc.getY() <= 32898) && pc.getMapId() == 523) {// 그림자의 신전 2층
					pc.getTeleport().start(((L1EtcItem) useItem.getItem()).getLocX(), ((L1EtcItem) useItem.getItem()).getLocY(), ((L1EtcItem) useItem.getItem()).getMapId(), 5, true);
				} else {
					pc.sendPackets(L1ServerMessage.sm279);
				}
				break;
			case 40692:// 완성된 보물의 지도
				if (inv.checkItem(40621)) {
					pc.sendPackets(L1ServerMessage.sm79);// \f1 아무것도 일어나지 않았습니다.
					return;
				}
				if ((pc.getX() >= 32856 && pc.getX() <= 32858) && (pc.getY() >= 32857 && pc.getY() <= 32858) && pc.getMapId() == 443) {// 해적섬의 지하 감옥 3층
					pc.getTeleport().start(((L1EtcItem) useItem.getItem()).getLocX(), ((L1EtcItem) useItem.getItem()).getLocY(), ((L1EtcItem) useItem.getItem()).getMapId(), 5, true);
				} else {
					pc.sendPackets(L1ServerMessage.sm279);
				}
				break;
			case 41208:// 사그러지는 영혼
				if ((pc.getX() >= 32844 && pc.getX() <= 32845) && (pc.getY() >= 32693 && pc.getY() <= 32694) && pc.getMapId() == 550) {// 배의 묘지:지상층
					pc.getTeleport().start(((L1EtcItem) useItem.getItem()).getLocX(), ((L1EtcItem) useItem.getItem()).getLocY(), ((L1EtcItem) useItem.getItem()).getMapId(), 5, true);
				} else {
					pc.sendPackets(L1ServerMessage.sm279);
				}
				break;
			case 40572:// 어쌔신의 증표
				if (pc.getX() == 32778 && pc.getY() == 32738 && pc.getMapId() == 21) {
					pc.getTeleport().start(32781, 32728, (short) 21, 5, true);
				} else if (pc.getX() == 32781 && pc.getY() == 32728 && pc.getMapId() == 21) {
					pc.getTeleport().start(32778, 32738, (short) 21, 5, true);
				} else {
					pc.sendPackets(L1ServerMessage.sm79);
				}
				break;
			case 40417:// 정령의 결정
				if ((pc.getX() >= 32667 && pc.getX() <= 32673) && (pc.getY() >= 32978 && pc.getY() <= 32984) && pc.getMapId() == 440) {// 해적섬
					pc.getTeleport().start(32745, 32807, (short)444, pc.getMoveState().getHeading(), true);//해적섬 던전 4층
				} else {
					pc.sendPackets(L1ServerMessage.sm79);// \f1 아무것도 일어나지 않았습니다.
				}
				break;
			case 40555:// 비밀의 방의 키
				if (pc.isKnight() && (pc.getX() >= 32806 && pc.getX() <= 32814) && (pc.getY() >= 32798 && pc.getY() <= 32807) && pc.getMapId() == 13) {// 오림 방
					pc.getTeleport().start(32815, 32810, (short)13, 5, false);
				} else {
					pc.sendPackets(L1ServerMessage.sm79);// \f1 아무것도 일어나지 않았습니다.
				}
				break;
			case 40079:// 귀환 주문서
			case 40095:// 수련자의 귀환 주문서
			case 40521:// 페어리의 날개
			case 130043:// 귀환 주문서 [공성전]
				returnScroll();
				break;
			case 40124: // 혈맹 귀환 주문서
			case 30086: // 수련자의 혈맹 귀환 주문서
			case 130044: // 혈맹 귀환 주문서 [공성전]
				returnPledgeScroll();
				break;
			case 301067:// 벚꽃마을 귀환 부적
				if (pc.isNotTeleport() || pc.getMapId() == 5166) {
					return;
				}
				if (pc.getConfig().getDuelLine() != 0 || pc.getMap().getInter() != null) {
					pc.sendPackets(L1ServerMessage.sm563); // \f1 여기에서는 사용할 수 없습니다.
					return;
				}
				if (pc.getMap().isEscapable() || pc.isGm()) {
					pc.getTeleport().start(32756, 32867, (short) 610, pc.getMoveState().getHeading(), true);
					pc.cancelAbsoluteBarrier();
				}
				break;
			case 600232:case 42049:// 잊혀진 귀환 주문서
				townScroll(L1TownLocation.FORGOTTEN_ISLAND);
				break;
			case 202099:// 클라우디아 마을 귀환 부적
				townScroll(L1TownLocation.TOWNID_CLAUDIA);
				break;
			case 30106:// 숨겨진 계곡 마을 귀환 부적
				townScroll(L1TownLocation.TOWNID_HIDDEN_VALLEY);
				break;
			case 301068:// 붉은 기사단 수련장 귀환 부적
				townScroll(L1TownLocation.TOWNID_REDSOLDER);
				break;
			case 40081:case 301066:// 기란마을 귀환 주문서
				townScroll(L1TownLocation.TOWNID_GIRAN);
				break;
			case 40117:// 은기사마을 귀환 주문서
				townScroll(L1TownLocation.TOWNID_SILVER_KNIGHT);
				break;
				
			/** 패키지이동주문서 **/ 
			case 800100:case 3000119:
				cashZoneTeleport(1, 32672, 32793, (short) 514);
				break;
			case 800101:
				cashZoneTeleport(2, 32672, 32793, (short) 515);
				break;
			case 800102:
				cashZoneTeleport(3, 32672, 32793, (short) 516);
				break;
			case 30121:// 초보자 가이드북
				L1NpcInstance findNpc = L1World.getInstance().findNpc(4200015);
				if (findNpc != null) {
					pc.sendPackets(new S_Board(findNpc), true);
				}
				break;
			case 40070:// 진화의 열매
				pc.sendPackets(new S_ServerMessage(76, useItem.getLogNameRef()), true);
				inv.removeItem(useItem, 1);
				break;
				
			case 350198:// 티칼달력
				String html_dialog = CrockController.getInstance().isTimeCrock() ? "tcalendaro" : "tcalendarc";
				if (pc.isGm())
					pc.sendPackets(new S_SystemMessage("Dialog " + html_dialog), true);													

				pc.sendPackets(new S_NPCTalkReturn(pc.getId(), html_dialog), true);
				break;
			case 40701:// 작은 보물의 지도
				switch(pc.getQuest().getStep(L1Quest.QUEST_LUKEIN1)){
				case 1:pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "firsttmap"), true);break;
				case 2:pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "secondtmapa"), true);break;
				case 3:pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "secondtmapb"), true);break;
				case 4:pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "secondtmapc"), true);break;
				case 5:pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmapd"), true);break;
				case 6:pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmape"), true);break;
				case 7:pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmapf"), true);break;
				case 8:pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmapg"), true);break;
				case 9:pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmaph"), true);break;
				case 10:pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmapi"), true);break;
				}
				break;
			case 500219:// 발록봉헌주문서(우호도)
			/*	if (pc.getKarma() <= 10000000) {
					pc.addKarma((int) (+15000 * Config.RATE_KARMA));
					pc.sendPackets(new S_Karma(pc), true);
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage(pc.getName() + "님의 우호도가 향상되었습니다."), true);
					pc.sendPackets(new S_SystemMessage(pc.getName()  + S_SystemMessage.getRefText(1), true), true);
					pc.getInventory().removeItem(l1iteminstance, 1);
				} else*/
					pc.sendPackets(L1ServerMessage.sm79);
				break;
			case 500218:// 야히봉헌주문서(우호도)
			/*	if (pc.getKarma() >= -10000000) {
					pc.addKarma((int) (-15000 * Config.RATE_KARMA));
					pc.sendPackets(new S_Karma(pc), true);
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage(pc.getName() + "님의 우호도가 향상되었습니다."), true);
					pc.sendPackets(new S_SystemMessage(pc.getName()  + S_SystemMessage.getRefText(1), true), true);
					pc.getInventory().removeItem(l1iteminstance, 1);
				} else*/
					pc.sendPackets(L1ServerMessage.sm79);
				break;
			case 840040:// 타라스의 마도서
			case 840041:// 타라스의 상급 마도서
				tarasBookBuff();
				break;
			case 5989:// 혈맹공헌도 물약
				pledgeContributionPotion();
				break;
			case 5990:case 5991:case 5992:case 5993:case 5998:case 5999:
				PledgeItem.contributionBuff(pc, useItem, itemId);
				break;
			case 5994:case 5995:case 5997:
				PledgeItem.pledgeDungeonKey(pc, useItem, itemId);
				break;
			case 40000:// 영웅의 코인
				ServerExplainTable.getInstance().explain(pc, 6);
				break;
			default:
				if (((itemId >= 40859 && itemId <= 40898) && itemId != 40863) || itemId == 130045) {// 스펠 스크롤
					// useType 5: spell_long, 17: spell_short, 30: spell_buff
					if (pc.getSkill().isSkillDelay(0)) {
						return;
					}
					inv.removeItem(useItem, 1);
					pc.cancelAbsoluteBarrier();
					if (!pc.isWizard() && !pc.isElf() && !pc.isIllusionist() && random.nextInt(10) + 1 <= 3) {// 사용 실패
						pc.sendPackets(L1ServerMessage.sm280);// 마법: 실패(성공하지 못함)
						return;
					}
					int skillid = itemId == 130045 ? L1SkillId.COUNTER_MAGIC : itemId - 40859;
					L1SkillUse l1skilluse = new L1SkillUse(true);
					l1skilluse.handleCommands(pc, skillid, spellscObjId, spellscX, spellscY, 0, L1SkillUseType.SPELLSC);
					l1skilluse = null;
				} else {
					int locX = ((L1EtcItem) useItem.getItem()).getLocX();
					int locY = ((L1EtcItem) useItem.getItem()).getLocY();
					short mapId = ((L1EtcItem) useItem.getItem()).getMapId();
					if (locX != 0 && locY != 0) {
						if (pc.isNotTeleport() || pc.getMapId() == 5166)
							return;
						if (pc.getConfig().getDuelLine() != 0 || pc.getMap().getInter() != null) {
							pc.sendPackets(L1ServerMessage.sm647);
							return;
						}
						if (pc.getMap().isEscapable() || pc.isGm()) {
							pc.getTeleport().start(locX, locY, mapId, pc.getMoveState().getHeading(), true);
							pc.getSkill().setSkillEffect(L1SkillId.ABSOLUTE_BARRIER, 3000);
							inv.removeItem(useItem, 1);
						} else {
							pc.sendPackets(L1ServerMessage.sm647);
						}
						pc.cancelAbsoluteBarrier();
					} else {
						if (useItem.getCount() < 1) {
							pc.sendPackets(new S_ServerMessage(329, useItem.getLogNameRef()), true);
						}
						//else pc.sendPackets(new S_ServerMessage(74, l1iteminstance.getLogName()), true);
					}
				}
				break;
			}
		}

		// 효과 지연이 있는 경우는 현재 시간을 세트
		if (isDelayEffect) {
			if (useItem.getChargeCount() > 0) {// 횟수 제한 아이템
				useItem.setChargeCount(useItem.getChargeCount() - 1);
				if (useItem.getChargeCount() <= 0) {
					if (itemId == 600245) {
						inv.createItem(60331, 1);// 행 베리의 낚시 가방 마지막 황금 상자
					}
					inv.removeItem(useItem, 1);
				} else {
					if (useItem.getLastUsed() != null) {
						useItem.getLastUsed().setTime(System.currentTimeMillis());
					} else {
						useItem.setLastUsed(new Timestamp(System.currentTimeMillis()));
					}
					inv.updateItem(useItem, L1PcInventory.COL_CHARGE_COUNT);
					inv.saveItem(useItem, L1PcInventory.COL_CHARGE_COUNT);
				}
			} else {
				if (useItem.getLastUsed() != null) {
					useItem.getLastUsed().setTime(System.currentTimeMillis());
				} else {
					useItem.setLastUsed(new Timestamp(System.currentTimeMillis()));
				}
				inv.updateItem(useItem, L1PcInventory.COL_DELAY_EFFECT);
				inv.saveItem(useItem, L1PcInventory.COL_DELAY_EFFECT);
			}
		}
		L1ItemDelay.onItemUse(pc, useItem); // 아이템 지연 개시
	}
	
	/**
	 * 혈맹 공헌도 물약
	 */
	void pledgeContributionPotion() {
		L1Clan clan = pc.getClan();
		if (clan == null || clan.getClanId() == Config.PLEDGE.BEGINNER_PLEDGE_ID || clan.isBot()) {// 제외할 혈맹
			pc.sendPackets(L1SystemMessage.CLAN_EXP_USE_NOT_CLAN);
			return;
		}
		pc.getClan().addContribution(10000);
//AUTO SRM: 		pc.sendPackets(new S_SystemMessage("혈맹 공헌도(10000) 획득"), true); // CHECKED OK
		pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(90), true), true);
		inv.removeItem(useItem, 1);
	}
	
	/**
	 * 타라스의 마도서
	 */
	void tarasBookBuff() {
		if (!inv.consumeItem(L1ItemId.GEMSTONE, 100)) {
			pc.sendPackets(L1ServerMessage.sm337_GEMSTONE);
			return;
		}
		skillItemArray(false, itemId == 840040 ? L1SkillInfo.TARAS_NORMAL_BUFF_ARRAY : L1SkillInfo.TARAS_HIGHT_BUFF_ARRAY);
		pc.sendPackets(new S_ChangeItemUse(useItem.getId()), true);
	}
	
	void skillItemArray(boolean delete, int[] skillids){
		L1BuffUtil.skillArrayAction(pc, skillids);
		if (delete) {
			inv.removeItem(useItem, 1);
		}
	}
	
	/**
	 * 예술가의 마안
	 */
	void artMaan() {
		if (pc.getSkill().hasSkillEffect(L1SkillId.STATUS_ART_MAAN)){
			//pc.sendPackets(new S_SystemMessage(String.format( "%d초 후에 사용 할수 있습니다.", pc.getSkill().getSkillEffectTimeSec(L1SkillId.STATUS_ART_MAAN))), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(22), String.valueOf(pc.getSkill().getSkillEffectTimeSec(L1SkillId.STATUS_ART_MAAN))), true);
			return;
		}
		if (inv.consumeItem(L1ItemId.GEMSTONE, 100)){
			pc.sendPackets(L1ServerMessage.sm337_GEMSTONE);
			return;
		}
		L1BuffUtil.skillArrayAction(pc, L1SkillInfo.ART_MAAN_BUFF_ARRAY);
	    pc.getSkill().setSkillEffect(L1SkillId.STATUS_ART_MAAN, 300000);
	}
	
	void spawnMonster(int monsterId, int deleteTime){
		L1NpcInstance npc = L1World.getInstance().findNpc(monsterId);
		if (npc != null) {
			pc.sendPackets(L1ServerMessage.sm79);
			return;
		}
		L1SpawnUtil.spawn(pc, monsterId, 0, deleteTime);
	}
	
	void runInventoryCreate(){
		if (inv.checkItemOne(L1ItemId.RUN_INVENTORY_ITEMS)) {
			pc.sendPackets(L1ServerMessage.sm2887);// 특정 아이템이 있어 아직은 사용할 수 없습니다.
			return;
		}
		if (inv.checkAddItem(ItemTable.getInstance().getTemplate(etcValue), 1) != L1Inventory.OK) return;
		L1ItemInstance item = inv.storeItem(etcValue, 1);
		pc.sendPackets(new S_ServerMessage(403, item.getItem().getDesc()), true);
		inv.removeItem(useItem, 1);
	}
	
	void cashTimeItemCreate(){
		if (inv.checkItem(etcValue)) {
			pc.sendPackets(L1ServerMessage.sm2887);// 특정 아이템이 있어 아직은 사용할 수 없습니다.
			return;
		}
		if (inv.checkAddItem(ItemTable.getInstance().getTemplate(etcValue), 1) != L1Inventory.OK) return;
		L1ItemInstance item = inv.storeItem(etcValue, 1);
		pc.sendPackets(new S_ServerMessage(403, item.getItem().getDesc()), true);
		inv.removeItem(useItem, 1);
	}
	
	/**
	 * 귀환 주문서
	 */
	void returnScroll() {
		if (pc.isNotTeleport()) {
			return;
		}
		if (pc.getConfig().getDuelLine() != 0) {
			pc.sendPackets(L1ServerMessage.sm563); // \f1 여기에서는 사용할 수 없습니다.
			return;
		}
		if (pc.getMap().isEscapable() || pc.isGm()) {
			int[] loc = Getback.GetBack_Location(pc);
			pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true);
			inv.removeItem(useItem, 1);
			if (itemId == 40095) {
				pc.getQuest().questItemUse(itemId);
			}
		} else {
			pc.sendPackets(S_Paralysis.TELEPORT_UNLOCK);
			pc.sendPackets(L1ServerMessage.sm647);
		}
		pc.cancelAbsoluteBarrier();
	}
	
	/**
	 * 혈맹 귀환 주문서
	 */
	void returnPledgeScroll() {
		if (pc.isNotTeleport() || pc.getMapId() == 5166) {
			return;
		}
		if (pc.getConfig().getDuelLine() != 0) {
			pc.sendPackets(L1ServerMessage.sm563); // \f1 여기에서는 사용할 수 없습니다.
			return;
		}
		if (pc.getMap().isEscapable() || pc.isGm()) {
			int castle_id = 0, house_id = 0;
			if (pc.getClanid() != 0) { // 크란 소속
				L1Clan clan = pc.getClan();
				if (clan != null) {
					castle_id	= clan.getCastleId();
					house_id	= clan.getHouseId();
				}
			}
			
			L1InterServer inter = pc.getMap().getInter();
			int[] loc = null;
			if (inter != null) {
				loc = inter == L1InterServer.WORLD_WAR && (castle_id == 2 || castle_id == 4 || castle_id == 5) ? 
						L1CastleLocation.getCastleLoc(castle_id)
						: Getback.GetBack_Location(pc);
			} else if (castle_id != 0) { // 성주 크란원
				loc = L1CastleLocation.getCastleLoc(castle_id);
			} else if (house_id != 0) { // 아지트 소유 크란원
				L1House house = HouseTable.getInstance().getHouseTable(pc.getClan().getHouseId());
				loc = house.isPurchaseBasement() ? 
						L1HouseLocation.getBasementLoc(house_id)
						: L1HouseLocation.getHouseLoc(house_id);
			} else {
				loc = pc.getHomeTownId() > 0 ? 
						L1TownLocation.getGetBackLoc(pc.getHomeTownId()) 
						: Getback.GetBack_Location(pc);
			}
			pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true);
			inv.removeItem(useItem, 1);
		} else {
			pc.sendPackets(S_Paralysis.TELEPORT_UNLOCK);
			pc.sendPackets(L1ServerMessage.sm647);
		}
		pc.cancelAbsoluteBarrier();
	}
	
	/**
	 * 마을 귀환 주문서
	 * @param townId
	 */
	void townScroll(int townId){
		if (pc.isNotTeleport()) {
			return;
		}
		if (pc.getConfig().getDuelLine() != 0) {
			pc.sendPackets(L1ServerMessage.sm563); // \f1 여기에서는 사용할 수 없습니다.
			return;
		}
		if (itemId == 600232 && pc.getMap().getInter() != L1InterServer.FORGOTTEN_ISLAND) {
			pc.sendPackets(L1ServerMessage.sm563); // \f1 여기에서는 사용할 수 없습니다.
			return;
		}
		if (pc.getMap().isEscapable() || pc.isGm()) {
			int[] loc = pc.getMap().getInter() != null ? Getback.GetBack_Location(pc) : L1TownLocation.getGetBackLoc(townId);
			pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true);
			if (itemId != 301066 && itemId != 301068 && itemId != 30106 && itemId != 202099 && itemId != 42049) {
				inv.removeItem(useItem, 1);
			}
			pc.cancelAbsoluteBarrier();
		} else {
			pc.sendPackets(S_Paralysis.TELEPORT_UNLOCK);
			pc.sendPackets(L1ServerMessage.sm647);
		}
	}
	
	/**
	 * 환생의 물약
	 * @throws Exception 
	 */
	void reincarnationPotion() throws Exception {
		pc.setExp(1);
		pc.resetLevel();
		pc.setBonusStats(0);
		pc.send_effect(191);
		pc.sendPackets(new S_OwnCharStatus(pc), true);
		inv.removeItem(useItem, 1);
		pc.save();
	}
	
	void cashZoneTeleport(int step, int x, int y, short mapId){
		if (pc.isNotTeleport() || pc.getMap().getInter() != null) {
			return;
		}
		if (pc.getMap().isEscapable() || pc.isGm()) {
		    pc.setCashStep(step);
		    pc.getTeleport().start(x, y, mapId, 5, true);
		    if (useItem.getItemId() != 3000119) {
		    	inv.removeItem(useItem, 1);
		    }
		}
	}
	
	void alignmentPotion(boolean law) throws Exception {
		if (law && pc.getAlignment() > 0) {
			pc.sendPackets(L1SystemMessage.CAOTIC_USE_MSG);
			return;
		}
		if (!law && pc.getAlignment() < 0) {
			pc.sendPackets(L1SystemMessage.LAWFUL_USE_MSG);
			return;
		}
		pc.setAlignment(law ? Short.MAX_VALUE : Short.MIN_VALUE);
		pc.sendPackets(L1ServerMessage.sm674);
		inv.removeItem(useItem, 1);
		pc.save();// DB에 캐릭터 정보를 기입한다
	}
	
	/**
	 * 저주 풀기 주문서
	 */
	void curseSolveScroll() {
		L1Item template = null;
		for (L1ItemInstance eachItem : inv.getItems()) {
			if (eachItem.getItem().getBless() != 2) {
				continue;
			}
			if (!eachItem.isEquipped() && (itemId == 40119 || itemId == 40097)) {
				continue;// n해주는 장비 하고 있는 것 밖에 해주 하지 않는다
			}
			int id_normal = eachItem.getItemId() - 200000;
			template = ItemTable.getInstance().getTemplate(id_normal);
			if (template == null) {
				continue;
			}
			if (inv.checkItem(id_normal) && template.isMerge()) {
				inv.storeItem(id_normal, eachItem.getCount());
				inv.removeItem(eachItem, eachItem.getCount());
			} else {
				eachItem.setItem(template);
				inv.updateItem(eachItem, L1PcInventory.COL_ITEMID);
				inv.saveItem(eachItem, L1PcInventory.COL_ITEMID);
				eachItem.setBless(eachItem.getBless() - 1);
				inv.updateItem(eachItem, L1PcInventory.COL_BLESS);
				inv.saveItem(eachItem, L1PcInventory.COL_BLESS);
			}
		}
		inv.removeItem(useItem, 1);
		pc.sendPackets(L1ServerMessage.sm155); // \f1누군가가 도와 준 것 같습니다.
	}
	
	/**
	 * 킬 데스 초기화
	 * @throws Exception
	 */
	void resetKillAndDeath() throws Exception {
		pc.setKills(0);
		pc.setDeaths(0);
		pc.save();
		pc.sendPackets(L1SystemMessage.KILL_DEATH_RESET);
		inv.removeItem(useItem, 1);
	}
	
	/**
	 * 요리사의 간식 주머니
	 */
	void cookerPackage() {
		if (inv.getSize() > L1PcInventory.MAX_SIZE - 2 || inv.getWeightPercent() > 99) {
			pc.sendPackets(L1ServerMessage.sm82); // 무게 게이지가 부족하거나 인벤토리가 꽉차서 더 들 수 없습니다.
			return;
		}
        if (random.nextBoolean()) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(130054), 1) != L1Inventory.OK) return;
        	L1ItemInstance item = inv.storeItem(130054, 1);
        	pc.sendPackets(new S_ServerMessage(403, item.getItem().getDesc()), true);
        } else {
        	L1ItemInstance item = null;
        	if (pc.isElf()) {
        		if (inv.checkAddItem(ItemTable.getInstance().getTemplate(130056), 1) != L1Inventory.OK) return;
				item = inv.storeItem(130056, 1);
        	} else if(pc.isWizard() || pc.isIllusionist()) {
        		if (inv.checkAddItem(ItemTable.getInstance().getTemplate(130057), 1) != L1Inventory.OK) return;
				item = inv.storeItem(130057, 1);
        	} else {
				if (inv.checkAddItem(ItemTable.getInstance().getTemplate(130055), 1) != L1Inventory.OK) return;
        		item = inv.storeItem(130055, 1);
        	}
        	pc.sendPackets(new S_ServerMessage(403, item.getItem().getDesc()), true);
        }
        inv.removeItem(useItem, 1);
	}
	
	/**
	 * 큰 은빛 베리아나
	 */
	void bigSilverBerryana() {
		if (inv.getSize() > L1PcInventory.MAX_SIZE - 5 || inv.getWeightPercent() > 98) {
			pc.sendPackets(L1ServerMessage.sm82); // 무게 게이지가 부족하거나 인벤토리가 꽉차서 더 들 수 없습니다.
			return;
		}
		int rnd = random.nextInt(120) + 1;
		
		if (inv.checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), 500000) != L1Inventory.OK) return;
		inv.storeItem(L1ItemId.ADENA, 500000);
//AUTO SRM: 		pc.sendPackets(new S_SystemMessage("아데나 (500,000)을 얻었습니다."), true); // CHECKED OK
		pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(92), true), true);
		L1ItemInstance silverberry = null;
		if (rnd <= 12) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(20315), 1) != L1Inventory.OK) return;
			silverberry = inv.storeItem(20315, 1);
		} else if (rnd <= 24) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(20262), 1) != L1Inventory.OK) return;
			silverberry = inv.storeItem(20262, 1);
		} else if (rnd <= 36) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(20291), 1) != L1Inventory.OK) return;
			silverberry = inv.storeItem(20291, 1);
		} else if (rnd <= 48) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(40087), 1) != L1Inventory.OK) return;
			silverberry = inv.storeItem(40087, 1);
		} else if (rnd <= 59) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(40074), 1) != L1Inventory.OK) return;
			silverberry = inv.storeItem(40074, 1);
		} else if (rnd <= 65) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(41248), 1) != L1Inventory.OK) return;
			silverberry = inv.storeItem(41248, 1);
		} else if (rnd <= 71) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(210096), 1) != L1Inventory.OK) return;
			silverberry = inv.storeItem(210096, 1);
		} else if (rnd <= 74) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(210105), 1) != L1Inventory.OK) return;
			silverberry = inv.storeItem(210105, 1);
		} else if (rnd <= 77) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(20422), 1) != L1Inventory.OK) return;
			silverberry = inv.storeItem(20422, 1);
		} else if (rnd <= 79) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(22000), 1) != L1Inventory.OK) return;
			silverberry = inv.storeItem(22000, 1);
		} else if (rnd <= 81) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(22003), 1) != L1Inventory.OK) return;
			silverberry = inv.storeItem(22003, 1);
		} else if (rnd <= 86) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(30127), 1) != L1Inventory.OK) return;
			silverberry = inv.storeItem(30127, 1);
		}
		if (silverberry != null) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(silverberry.getLogNameRef() + "을 얻었습니다.", true), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(silverberry.getLogNameRef()  + S_SystemMessage.getRefText(93), true), true);
		}
		inv.removeItem(useItem, 1);
	}
	
	/**
	 * 큰 금빛 베리아나
	 */
	void bigGoldBerryana() {
		if (inv.getSize() > L1PcInventory.MAX_SIZE - 5 || inv.getWeightPercent() > 98) {
			pc.sendPackets(L1ServerMessage.sm82); // 무게 게이지가 부족하거나 인벤토리가 꽉차서 더 들 수 없습니다.
			return;
		}
		int rnd = random.nextInt(170) + 1;
		if (inv.checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), 500000) != L1Inventory.OK) return;
		inv.storeItem(L1ItemId.ADENA, 5000000);
//AUTO SRM: 		pc.sendPackets(new S_SystemMessage("아데나 (5,000,000)을 얻었습니다."), true); // CHECKED OK
		pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(94), true), true);
		L1ItemInstance goldberry = null;
		if (rnd <= 25) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(41249), 1) != L1Inventory.OK) return;
			goldberry = inv.storeItem(41249, 1);
		} else if (rnd <= 51) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(41250), 1) != L1Inventory.OK) return;
			goldberry = inv.storeItem(41250, 1);
		} else if (rnd <= 77) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(210070), 1) != L1Inventory.OK) return;
			goldberry = inv.storeItem(210070, 1);
		} else if (rnd <= 88) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(40038), 1) != L1Inventory.OK) return;
			goldberry = inv.storeItem(40038, 1);
		} else if (rnd <= 99) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(140087), 1) != L1Inventory.OK) return;
			goldberry = inv.storeItem(140087, 1);
		} else if (rnd <= 110) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(140074), 1) != L1Inventory.OK) return;
			goldberry = inv.storeItem(140074, 1);
		} else if (rnd <= 112) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(202002), 1) != L1Inventory.OK) return;
			goldberry = inv.storeItem(202002, 1);
		} else if (rnd <= 114) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(504), 1) != L1Inventory.OK) return;
			goldberry = inv.storeItem(504, 1);
		} else if (rnd <= 116) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(205), 1) != L1Inventory.OK) return;
			goldberry = inv.storeItem(205, 1);
		} else if (rnd <= 118) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(20165), 1) != L1Inventory.OK) return;
			goldberry = inv.storeItem(20165, 1);
		} else if (rnd <= 120) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(20197), 1) != L1Inventory.OK) return;
			goldberry = inv.storeItem(20197, 1);
		} else if (rnd <= 122) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(20160), 1) != L1Inventory.OK) return;
			goldberry = inv.storeItem(20160, 1);
		} else if (rnd <= 124) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(20218), 1) != L1Inventory.OK) return;
			goldberry = inv.storeItem(20218, 1);
		} else if (rnd <= 126) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(20298), 1) != L1Inventory.OK) return;
			goldberry = inv.storeItem(20298, 1);
		} else if (rnd <= 131) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(30127), 1) != L1Inventory.OK) return;
			goldberry = inv.storeItem(30127, 1);
		}
		if (goldberry != null) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(goldberry.getLogNameRef() + "을 얻었습니다.", true), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(goldberry.getLogNameRef()  + S_SystemMessage.getRefText(93), true), true);
		}
		inv.removeItem(useItem, 1);
	}
	
	/**
	 * 고대의 은빛 베리아나
	 */
	void godSilverBerryana() {
		if (inv.getSize() > L1PcInventory.MAX_SIZE - 5 || inv.getWeightPercent() > 98){
			pc.sendPackets(L1ServerMessage.sm82); // 무게 게이지가 부족하거나 인벤토리가 꽉차서 더 들 수 없습니다.
			return;
		}
		int rnd = random.nextInt(170) + 1;
		if (inv.checkAddItem(ItemTable.getInstance().getTemplate(41302), 10000) != L1Inventory.OK) return;
		inv.storeItem(41302, 10000);
//AUTO SRM: 		pc.sendPackets(new S_SystemMessage("베리 (10,000)을 얻었습니다."), true); // CHECKED OK
		pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(95), true), true);
		L1ItemInstance silverberry2 = null;
		if (rnd <= 25) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(40393), 1) != L1Inventory.OK) return;
			silverberry2 = inv.storeItem(40393, 1);
		} else if (rnd <= 51) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(40394), 1) != L1Inventory.OK) return;
			silverberry2 = inv.storeItem(40394, 1);
		} else if (rnd <= 77) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(40395), 1) != L1Inventory.OK) return;
			silverberry2 = inv.storeItem(40395, 1);
		} else if (rnd <= 88) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(40396), 1) != L1Inventory.OK) return;
			silverberry2 = inv.storeItem(40396, 1);
		} else if (rnd <= 95) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(31146), 1) != L1Inventory.OK) return;
			silverberry2 = inv.storeItem(31146, 1);
		} else if (rnd <= 100) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(31147), 1) != L1Inventory.OK) return;
			silverberry2 = inv.storeItem(31147, 1);
		} else if (rnd <= 105) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(31149), 1) != L1Inventory.OK) return;
			silverberry2 = inv.storeItem(31149, 1);
		} else if (rnd <= 110) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(31148), 1) != L1Inventory.OK) return;
			silverberry2 = inv.storeItem(31148, 1);
		} else if (rnd <= 115) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(31054), 1) != L1Inventory.OK) return;
			silverberry2 = inv.storeItem(31054, 1);
		}
		if (silverberry2 != null) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(silverberry2.getLogNameRef() + "을 얻었습니다.", true), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(silverberry2.getLogNameRef()  + S_SystemMessage.getRefText(93), true), true);
		}
		inv.removeItem(useItem, 1);
	}
	
	/**
	 * 고대의 금빛 베리아나
	 */
	void godGoldBerryana() {
		if (inv.getSize() > L1PcInventory.MAX_SIZE - 5 || inv.getWeightPercent() > 98){
			pc.sendPackets(L1ServerMessage.sm82); // 무게 게이지가 부족하거나 인벤토리가 꽉차서 더 들 수 없습니다.
			return;
		}
		int rnd = random.nextInt(170) + 1;
		if (inv.checkAddItem(ItemTable.getInstance().getTemplate(41302), 30000) != L1Inventory.OK) return;
		inv.storeItem(41302, 30000);
//AUTO SRM: 		pc.sendPackets(new S_SystemMessage("베리 (30,000)을 얻었습니다."), true); // CHECKED OK
		pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(96), true), true);
		L1ItemInstance goldberry2 = null;
		if (rnd <= 25) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(40393), 1) != L1Inventory.OK) return;
			goldberry2 = inv.storeItem(40393, 1);
		} else if (rnd <= 51) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(40394), 1) != L1Inventory.OK) return;
			goldberry2 = inv.storeItem(40394, 1);
		} else if (rnd <= 77) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(40395), 1) != L1Inventory.OK) return;
			goldberry2 = inv.storeItem(40395, 1);
		} else if (rnd <= 88) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(40396), 1) != L1Inventory.OK) return;
			goldberry2 = inv.storeItem(40396, 1);
		} else if (rnd <= 99) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(31146), 1) != L1Inventory.OK) return;
			goldberry2 = inv.storeItem(31146, 1);
		} else if (rnd <= 110) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(31147), 1) != L1Inventory.OK) return;
			goldberry2 = inv.storeItem(31147, 1);
		} else if (rnd <= 120) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(31149), 1) != L1Inventory.OK) return;
			goldberry2 = inv.storeItem(31149, 1);
		} else if (rnd <= 130) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(31148), 1) != L1Inventory.OK) return;
			goldberry2 = inv.storeItem(31148, 1);
		} else if (rnd <= 140) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(31054), 1) != L1Inventory.OK) return;
			goldberry2 = inv.storeItem(31054, 1);
		} else if (rnd <= 142) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(40346), 1) != L1Inventory.OK) return;
			goldberry2 = inv.storeItem(40346, 1);
		} else if (rnd <= 144) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(40354), 1) != L1Inventory.OK) return;
			goldberry2 = inv.storeItem(40354, 1);
		} else if (rnd <= 146) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(40362), 1) != L1Inventory.OK) return;
			goldberry2 = inv.storeItem(40362, 1);
		} else if (rnd <= 148) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(40370), 1) != L1Inventory.OK) return;
			goldberry2 = inv.storeItem(40370, 1);
		} else if (rnd <= 150) {
			if (inv.checkAddItem(ItemTable.getInstance().getTemplate(40466), 1) != L1Inventory.OK) return;
			goldberry2 = inv.storeItem(40466, 1);
		}
		if (goldberry2 != null) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(goldberry2.getLogNameRef() + "을 얻었습니다.", true), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(goldberry2.getLogNameRef()  + S_SystemMessage.getRefText(93), true), true);
		}
		inv.removeItem(useItem, 1);
	}
	
	void fantasyGoldBox(){
		int boxRnd = random.nextInt(1000);
		if (boxRnd < 500) {
			inv.createItem(L1ItemId.ADENA, random.nextInt(150000) + 50000); // 아데나
		} else if (boxRnd < 780) {
			inv.createItem(L1ItemId.ADENA, random.nextInt(450000) + 50000); // 아데나
		} else if (boxRnd < 880) {
			inv.createItem(L1ItemId.ADENA, random.nextInt(900000) + 100000); // 아데나
		} else if (boxRnd < 920) {
			inv.createItem(L1ItemId.ADENA, random.nextInt(2700000) + 300000); // 아데나
		} else if (boxRnd < 950) {
			inv.createItem(L1ItemId.ADENA, random.nextInt(4500000) + 500000); // 아데나
		} else if (boxRnd < 970) {
			inv.createItem(L1ItemId.ADENA, random.nextInt(9000000) + 1000000); // 아데나
		} else if (boxRnd < 982) {
			inv.createItem(41352, 1); // 유뿔
		} else if (boxRnd < 984) {
			inv.createItem(30180, 1); // 지혜의 벨트
		} else if (boxRnd < 986) {
			inv.createItem(30179, 1); // 민첩의 벨트
		} else if (boxRnd < 988) {
			inv.createItem(30178, 1); // 지식의 벨트
		} else if (boxRnd < 990) {
			inv.createItem(30177, 1); // 완력의 벨트
		} else {
			inv.createItem(810003, 1); // 장인의 무기 마법 주문서
		}
		inv.removeItem(useItem, 1);
	}
	
	void wandSpellNormal(){
		if (((itemId == 810006 || itemId == 810007) && !((pc.getMapId() >= 1936 && pc.getMapId() <= 1986 || pc.getMapId() >= 2936 && pc.getMapId() <= 2986)))
				|| (itemId == 30055 && !(pc.getMapId() >= 3000 && pc.getMapId() <= 3100))
				|| ((itemId == 420103 || itemId == 420105 || itemId == 420106 || itemId == 420107 || itemId == 420109 || itemId == 420110) && !(pc.getMapId() >= 732 && pc.getMapId() <= 752))) {
		    pc.sendPackets(L1ServerMessage.sm563); // \f1 여기에서는 사용할 수 없습니다.
		    return;
		}
		int chargeCount = useItem.getChargeCount();
		if (itemId != 810007 && chargeCount <= 0) {
			pc.sendPackets(L1ServerMessage.sm79);
			return;
		}
		if (pc.isInvisble()) {
			pc.sendPackets(L1ServerMessage.sm1003);
			return;
		}
		int gfx = 0, dmg = 0, range = 0;
		switch (itemId){
		case 810006:	gfx = 1819;	dmg = 250;	range = 3;	break;	// 마법막대(파이어스톰) 
		case 810007:	gfx = 3934;	dmg = 1500;	range = 22;	break;	// 마법막대(정화)
		case 30055:		gfx = 758;	dmg = 500;	range = 4;	break;	// 폭풍의 막대
		case 420103:	gfx = 19124;dmg = 200;	range = 4;	break;	// 시공의 막대(파이어그라운드)
		case 420105:	gfx = 1819;	dmg = 500;	range = 4;	break;	// 시공의 막대(파이어스톰)
		case 420106:	gfx = 757;	dmg = 1000;	range = 18;	break;	// 시공의 막대(블리자드)
		case 420107:	gfx = 19138;dmg = 500;	range = 4;	break;	// 시공의 막대(아이스릿지)
		case 420109:	gfx = 19110;dmg = 1000;	range = 18;	break;	// 시공의 막대(라이트닝블리자드)
		case 420110:	gfx = 18931;dmg = 500;	range = 4;	break;	// 시공의 막대(라이트닝빔)
		}
		if (itemId == 420103 || itemId == 420107 || itemId == 420110) {
			GeneralThreadPool.getInstance().execute(new DamageDuration(pc, pc.getLocation(), range, dmg, 3));
		} else {
			L1MonsterInstance mon = null;
			for (L1Object object : L1World.getInstance().getVisibleObjects(pc, range)) {
				if (object == null || !(object instanceof L1MonsterInstance)) {
					continue;
				}
				mon = (L1MonsterInstance) object;
				int npcId = mon.getNpcId();
				if (npcId == 7200003 || npcId == 7200029) {// 유니콘 제외
					continue;
				}
				if (itemId == 810007// 정화 막대(보스, 대정령 제외)
						&& (npcId == 7200055 || npcId == 7200056
						|| (npcId >= 7200016 && npcId <= 7200020)
						|| (npcId >= 7200037 && npcId <= 7200040))) {
					continue;
				}
				mon.broadcastPacket(new S_DoActionGFX(mon.getId(), ActionCodes.ACTION_Damage), true);
				mon.receiveDamage(pc, dmg);
			}
		}
		pc.broadcastPacketWithMe(new S_UseAttackSkill(pc, 0, gfx, pc.getX(), pc.getY(), 18), true);
		if (itemId == 810007) {
			inv.removeItem(useItem, 1);
			return;
		}
		useItem.setChargeCount(chargeCount - 1);
		inv.updateItem(useItem, L1PcInventory.COL_CHARGE_COUNT);
		if (chargeCount <= 1) {
			inv.removeItem(useItem, 1);
		}
	}
	
	void wandSpellLong(int spellsc_objid, int spellsc_x, int spellsc_y){
		int chargeCount = useItem.getChargeCount();
		if (!pc.isGm() 
				&& (chargeCount <= 0 
				|| !((pc.getMapId() >= 732 && pc.getMapId() <= 752 
				|| (pc.getMapId() >= 2936 && pc.getMapId() <= 2986))))) {
			pc.sendPackets(L1ServerMessage.sm79);
			return;
		}
		if (pc.isInvisble()) {
			pc.sendPackets(L1ServerMessage.sm1003);
			return;
		}
		// 앱솔루트 배리어의 해제
		pc.cancelAbsoluteBarrier();
		L1Object target = L1World.getInstance().findObject(spellsc_objid);
		int gfx = 0, dmg = 0;
		switch (itemId){
		case 420104:case 810011:	gfx = 762;	dmg = 500;break;	// 시공의 막대(미티어 스트라이크), 마법 막대(미티어 스트라이크)
		case 420108:				gfx = 16060;dmg = 500;break;	// 시공의 막대(아이스 미티어)
		case 420111:				gfx = 3924;	dmg = 500;break;	// 시공의 막대(라이트닝 스톰)
		}
		boolean targetLocType = target != null;
		L1Location targetLoc = targetLocType ? target.getLocation() : new L1Location(spellsc_x, spellsc_y, pc.getMapId());
		L1NpcInstance npc = null;
		for (L1Object object : L1World.getInstance().getVisiblePoint(targetLoc, 3)) {
			if (object instanceof L1MonsterInstance) {
				npc = (L1NpcInstance) object;
				if (npc.getNpcId() == 7200003 || npc.getNpcId() == 7200029) {
					continue;// 유니콘
				}
				if ((!targetLocType && !npc.isDead()) || (targetLocType && !npc.isDead() && npc.getId() != target.getId())) {
					npc.broadcastPacket(new S_DoActionGFX(npc.getId(), ActionCodes.ACTION_Damage), true);
				}
				npc.receiveDamage(pc, dmg);
			} else if (object instanceof L1DoorInstance || object instanceof L1TowerInstance) {
				npc = (L1NpcInstance) object;
				npc.receiveDamage(pc, dmg);
			}
		}
		pc.broadcastPacketWithMe(
				targetLocType ? 
						new S_UseAttackSkill(pc, target.getId(), gfx, target.getX(), target.getY(), ActionCodes.ACTION_SkillAttack) 
						: new S_UseAttackSkill(pc, 0, gfx, targetLoc.getX(), targetLoc.getY(), ActionCodes.ACTION_SkillAttack),
						true);
		useItem.setChargeCount(chargeCount - 1);
		inv.updateItem(useItem, L1PcInventory.COL_CHARGE_COUNT);
		if (chargeCount <= 1) {
			inv.removeItem(useItem, 1);
		}
		if (!targetLocType) {
			targetLoc = null;
		}
	}
	
	class DamageDuration implements Runnable {
		L1PcInstance owner;
		L1Location loc;
		int range, dmg, count;
		
		public DamageDuration(L1PcInstance owner, L1Location loc, int range, int dmg, int count) {
			this.owner	= owner;
			this.loc	= loc;
			this.range	= range;
			this.dmg	= dmg;
			this.count	= count;
		}
		
		@Override
		public void run() {
			try {
				for (int i=0; i<count; i++) {
					Thread.sleep(1000L);
					if (owner == null) {
						return;
					}
					L1MonsterInstance mon = null;
					for (L1Object object : L1World.getInstance().getVisiblePoint(loc, range)) {
						if (object == null || !(object instanceof L1MonsterInstance)) {
							continue;
						}
						mon = (L1MonsterInstance) object;
						mon.broadcastPacket(new S_DoActionGFX(mon.getId(), ActionCodes.ACTION_Damage), true);
						mon.receiveDamage(owner, dmg);
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String getType() {
		return C_ITEM_USE;
	}
}

