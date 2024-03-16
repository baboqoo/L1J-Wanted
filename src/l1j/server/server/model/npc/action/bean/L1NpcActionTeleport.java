package l1j.server.server.model.npc.action.bean;

import java.util.LinkedList;

import javolution.util.FastTable;
import l1j.server.GameSystem.dungeontimer.bean.L1DungeonTimerInfo;
import l1j.server.GameSystem.dungeontimer.loader.L1DungeonTimerLoader;
import l1j.server.server.ActionCodes;
import l1j.server.server.GameServerSetting;
import l1j.server.server.construct.L1Alignment;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1WhaleBonusTimer;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_TurnOnTeleportFlagNoti;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.L1SpawnUtil;

public class L1NpcActionTeleport {
	public static enum TelType {
		NORMAL, INTER, RANDOM
	}
	
	private int npcId;
	private String actionName;
	private int needLevel, limitLevel;
	private int needTimerId;
	private FastTable<NeedItem> needItem;
	private FastTable<Integer> needBuff;
	private boolean needPcroomBuff;
	private TeleportInfo tel;
	private String successActionName, failLevelActionName, failItemActionName, failBuffActionName;
	private L1Alignment failAlign;
	
	public L1NpcActionTeleport(int npcId, String actionName, int needLevel, int limitLevel, int needTimerId,
			FastTable<NeedItem> needItem, FastTable<Integer> needBuff, boolean needPcroomBuff, TeleportInfo tel,
			String successActionName, String failLevelActionName, String failItemActionName, String failBuffActionName, L1Alignment failAlign) {
		this.npcId					= npcId;
		this.actionName				= actionName;
		this.needLevel				= needLevel;
		this.limitLevel				= limitLevel;
		this.needTimerId			= needTimerId;
		this.needItem				= needItem;
		this.needBuff				= needBuff;
		this.needPcroomBuff			= needPcroomBuff;
		this.tel					= tel;
		this.successActionName		= successActionName;
		this.failLevelActionName	= failLevelActionName;
		this.failItemActionName		= failItemActionName;
		this.failBuffActionName		= failBuffActionName;
		this.failAlign				= failAlign;
	}
	
	public FastTable<NeedItem> getNeedItem() {
		return needItem;
	}
	public FastTable<Integer> getNeedBuff() {
		return needBuff;
	}

	public static class NeedItem {
		private int itemId, count;
		private boolean remove;
		
		public NeedItem(int itemId, int count, boolean remove) {
			this.itemId	= itemId;
			this.count	= count;
			this.remove	= remove;
		}
	}
	
	public static class TeleportInfo {
		private int telX, telY, telMapId, telRange, telTownId;
		private TelType telType;
		private LinkedList<TeleportInfoRandom> randomMap;
		
		public TeleportInfo(int telX, int telY, int telMapId, int telRange, int telTownId, TelType telType) {
			this.telX		= telX;
			this.telY		= telY;
			this.telMapId	= telMapId;
			this.telRange	= telRange;
			this.telTownId	= telTownId;
			this.telType	= telType;
		}
		
		public TelType getTelType() {
			return telType;
		}
		public void setRandomMap(LinkedList<TeleportInfoRandom> val) {
			randomMap = val;
		}
	}
	
	public static class TeleportInfoRandom {
		private int x, y, number, range, prob;

		public TeleportInfoRandom(int x, int y, int number, int range, int prob) {
			this.x = x;
			this.y = y;
			this.number = number;
			this.range = range;
			this.prob = prob;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("X:").append(x).append(", Y:").append(y).append(", NUMBER:").append(number).append(", RANGE:").append(range).append(", PROB:").append(prob);
			return sb.toString();
		}
	}
	
	public String action(L1PcInstance pc){
		if (pc.isNotTeleport()){
			return null;
		}
		// 레벨 제한
		if ((needLevel > 0 && needLevel > pc.getLevel()) || (limitLevel > 0 && limitLevel < pc.getLevel())) {
			pc.sendPackets(L1ServerMessage.sm5359);// 입장에 필요한 조건이 맞지 않습니다.
			pc.getConfig().finishPlaySupport();
			return failLevelActionName;
		}
		// 버프 체크
		if ((needBuff != null && !needBuff.isEmpty() && !validationBuff(pc)) || (needPcroomBuff && !pc.isPCCafe())) {
			pc.sendPackets(L1ServerMessage.sm5359);// 입장에 필요한 조건이 맞지 않습니다.
			pc.getConfig().finishPlaySupport();
			return failBuffActionName;
		}
		// 아이템 체크
		if (needItem != null && !needItem.isEmpty() && !validationItem(pc)) {
			pc.sendPackets(L1ServerMessage.sm5359);// 입장에 필요한 조건이 맞지 않습니다.
			pc.getConfig().finishPlaySupport();
			return failItemActionName;
		}
		// 라우풀 체크
		if (failAlign != null && !validationAlignment(pc)) {
			pc.sendPackets(L1ServerMessage.sm5359);// 입장에 필요한 조건이 맞지 않습니다.
			pc.getConfig().finishPlaySupport();
			return failItemActionName;
		}
		// 타이머 존재
		if (needTimerId > 0) {
			// 은기사 던전
			if (needTimerId == 12) {
				int adenaCount = actionName.equals("c") || actionName.equals("d") ? 30000 : 15000;
				if (!(pc.getInventory().checkItemOne(L1ItemId.DEATH_PENALTY_SHIELD_ITEMS) || (pc.getInventory().checkItemOne(L1ItemId.SILVER_KNIGHT_PAPER_ITEMS) && pc.getInventory().consumeItem(L1ItemId.ADENA, adenaCount)))) {
					pc.sendPackets(L1ServerMessage.sm5359); // 입장에 필요한 조건이 맞지 않습니다.
					pc.getConfig().finishPlaySupport();
					return failItemActionName;
				}
			}
			return teleportTimer(pc);
		}
		return teleportNormal(pc);
	}
	
	String teleportNormal(L1PcInstance pc){
		if (needItem != null && !needItem.isEmpty()) {
			for (NeedItem needItem : needItem) {
				if (needItem.remove) {
					pc.getInventory().consumeItem(needItem.itemId, needItem.count);
				}
			}
		}
		
		if (tel.telType == TelType.RANDOM) {
			return teleportRandom(pc);
		}
		
		int telX = tel.telX, telY = tel.telY, telMapId = tel.telMapId;
		if (tel.telTownId > 0) {
			int[] townLoc	= L1TownLocation.getGetBackLoc(tel.telTownId);
			telX			= townLoc[0];
			telY			= townLoc[1];
			telMapId		= townLoc[2];
		}
		
		if ((telMapId == 1318 || telMapId == 1319) && !GameServerSetting.BLACK_DRAGON) {// 암흑룡의 던전
			return failItemActionName;
		}
		
		L1Location loc = new L1Location(telX, telY, telMapId);
		if (tel.telRange > 0) {
			loc = loc.randomLocation(tel.telRange, false);
		}
		pc.getSkill().setSkillEffect(L1SkillId.ABSOLUTE_BARRIER, 3000);
		if (tel.telType == TelType.INTER) {
			pc.getTeleport().start(loc.getX(), loc.getY(), (short) loc.getMapId(), 5, true, true);
		} else {
			pc.sendPackets(S_TurnOnTeleportFlagNoti.TURN_ON_TELEPORT_NOTI);
			pc.getTeleport().start(loc.getX(), loc.getY(), (short) loc.getMapId(), 5, true);
		}
		loc = null;
		return successActionName;
	}
	
	String teleportTimer(L1PcInstance pc){
		int itemId = 0, itemCount = 0;
		if (needItem != null && !needItem.isEmpty()) {
			itemId		= needItem.get(0).itemId;
			itemCount	= needItem.get(0).count;
		}
		int telX = tel.telX, telY = tel.telY, telMapId = tel.telMapId;
		if (tel.telTownId > 0) {
			int[] townLoc	= L1TownLocation.getGetBackLoc(tel.telTownId);
			telX			= townLoc[0];
			telY			= townLoc[1];
			telMapId		= townLoc[2];
		}
		if (needTimerId > 0) {
			L1DungeonTimerInfo info = L1DungeonTimerLoader.getDungeonTimerId(needTimerId);
			if (info.getMaxChargeCount() > 0) {
				if (!pc.getDungoenTimer().enterCount(telX, telY, telMapId)) {
					pc.sendPackets(L1ServerMessage.sm5359); // 입장에 필요한 조건이 맞지 않습니다.
				}
			} else if (needTimerId == 2) {
				if (!pc.isPCCafe()) {
					if (!pc.getInventory().checkItem(itemId, itemCount)) {
						pc.sendPackets(L1ServerMessage.sm5359); // 입장에 필요한 조건이 맞지 않습니다.
						pc.getConfig().finishPlaySupport();
						return failBuffActionName;
					}
				} else {
					itemId = itemCount = 0;
				}
				pc.getDungoenTimer().enter(telX, telY, telMapId, tel.telType == TelType.INTER, itemId, itemCount);
			} else {
				pc.getDungoenTimer().enter(telX, telY, telMapId, tel.telType == TelType.INTER, itemId, itemCount);
			}
		}
		return null;
	}
	
	String teleportRandom(L1PcInstance pc) {
		if (tel.randomMap == null || tel.randomMap.isEmpty()) {
			System.out.println(String.format("[L1NpcActionTeleport] RANDOM_MAP_EMPTY : NPC_ID(%d), ACTION(%s), CHAR_NAME(%s)", npcId, actionName, pc.getName()));
			return null;
		}
		int prob = 0;
		int random = CommonUtil.random(100) + 1;
		TeleportInfoRandom choice = null;
		for (TeleportInfoRandom rndInfo : tel.randomMap) {
			prob += rndInfo.prob;
			if (random < prob) {
				choice = rndInfo;
				break;
			}
		}
		if (choice == null) {
			System.out.println(String.format("[L1NpcActionTeleport] RANDOM_MAP_CHOICE_NULL : NPC_ID(%d), ACTION(%s), CHAR_NAME(%s)", npcId, actionName, pc.getName()));
			return null;
		}
		
		// 굶주린 고래상어 보너스 맵
		if (L1WhaleBonusTimer.MAPS.contains(choice.number) && !isWhaleBonusMap(pc, choice.number)) {
			return null;
		}
		
		L1Location loc = new L1Location(choice.x, choice.y, choice.number);
		if (choice.range > 0) {
			loc = loc.randomLocation(choice.range, false);
		}
		pc.getSkill().setSkillEffect(L1SkillId.ABSOLUTE_BARRIER, 3000);
		pc.sendPackets(S_TurnOnTeleportFlagNoti.TURN_ON_TELEPORT_NOTI);
		pc.getTeleport().start(loc.getX(), loc.getY(), (short) loc.getMapId(), 5, true);
		loc = null;
		return successActionName;
	}
	
	/**
	 * 굶주린 고래상어 보너스 맵
	 * @param map_number
	 * @return boolean
	 */
	boolean isWhaleBonusMap(L1PcInstance pc, int map_number) {
		// 보너스 맵 사용여부 검증
		if (L1World.getInstance().getMapPlayer(map_number).size() > 0) {
			pc.sendPackets(L1SystemMessage.WHALE_MAP_NOT_EMPTY);
			return false;
		}
		if (map_number == 1650) 
		{
			// 보물방
			for (L1Object obj : L1World.getInstance().getVisibleObjects(map_number).values()) {
				if (obj instanceof L1DoorInstance == false) {
					continue;
				}
				L1DoorInstance door = (L1DoorInstance) obj;
				if (door.getOpenStatus() == ActionCodes.ACTION_Open) {
					door.close();
				}
			}
		}
		else
		{
			// 보스방
			boolean isAlive = false;// 보스 생존 여부
			for (L1Object obj : L1World.getInstance().getVisibleObjects(map_number).values()) {
				if (obj instanceof L1MonsterInstance == false) {
					continue;
				}
				L1MonsterInstance boss = (L1MonsterInstance) obj;
				if (boss.getNpcId() == 18731 && !boss.isDead()) {
					isAlive = true;
					break;
				}
			}
			if (!isAlive) {
				L1SpawnUtil.spawn2(32799, 32800, (short) 1601, 5, 18731, 0, 0, 0);// 보스 스폰
			}
		}
		return true;
	}
	
	boolean validationItem(L1PcInstance pc){
		for (NeedItem needItem : needItem) {
			if (!pc.getInventory().checkItem(needItem.itemId, needItem.count)) {
				return false;
			}
		}
		return true;
	}
	
	boolean validationBuff(L1PcInstance pc){
		for (int buffId : needBuff) {
			if (!pc.getSkill().hasSkillEffect(buffId)) {
				return false;
			}
		}
		return true;
	}
	
	boolean validationAlignment(L1PcInstance pc){
		switch(failAlign){
		case LAWFUL:
			if (pc.getAlignment() >= 500) {
				pc.sendPackets(L1ServerMessage.sm8332);// 로우풀 상태에서는 입장이 불가능합니다.
				return false;
			}
			break;
		case CAOTIC:
			if (pc.getAlignment() <= -500) {
				pc.sendPackets(L1ServerMessage.sm8334);// 카오틱 상태에서는 입장이 불가능합니다.
				return false;
			}
			break;
		default:
			if (pc.getAlignment() > -500 && pc.getAlignment() < 500) {
				pc.sendPackets(L1ServerMessage.sm8333);// 뉴트럴 상태에서는 입장이 불가능합니다.
				return false;
			}
			break;
		}
		return true;
	}
}

