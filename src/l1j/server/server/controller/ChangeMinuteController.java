package l1j.server.server.controller;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TimeZone;

import l1j.server.Config;
import l1j.server.GameSystem.arca.L1Arca;
import l1j.server.GameSystem.attendance.AttendanceGroupType;
import l1j.server.GameSystem.attendance.AttendanceTable;
import l1j.server.GameSystem.attendance.bean.AttendanceAccount;
import l1j.server.GameSystem.attendance.bean.AttendanceItem;
import l1j.server.GameSystem.attendance.bean.AttendanceRandomItem;
import l1j.server.GameSystem.freebuffshield.FreeBuffShieldHandler;
import l1j.server.common.data.AttendanceBonusType;
import l1j.server.server.GameServerSetting;
import l1j.server.server.construct.L1InterServer;
import l1j.server.server.construct.L1RegionStatus;
import l1j.server.server.construct.message.L1GreenMessage;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.controller.action.War;
import l1j.server.server.datatables.KeyTable;
import l1j.server.server.model.L1Einhasad;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.item.collection.favor.L1FavorBookInventory;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookUserObject;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_ExpBoostingInfo;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_TamPointNoti;
import l1j.server.server.serverpackets.attendance.S_AttenDanceCompleteNoti;
import l1j.server.server.serverpackets.attendance.S_AttenDanceInfoNoti;
import l1j.server.server.serverpackets.einhasad.S_RestExpInfoNoti;
import l1j.server.server.serverpackets.gamegate.S_GameGatePCCafeCharge;
import l1j.server.server.serverpackets.gamegate.S_UserStartSundry;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti.SkillIconNotiType;

public class ChangeMinuteController implements l1j.server.server.model.gametime.TimeListener {// 1분마다 호출되는 액션
	private l1j.server.server.controller.action.ControllerInterface fairly		= l1j.server.server.controller.action.FairlyQueen.getInstance();
	private l1j.server.server.controller.action.ControllerInterface npcChat		= l1j.server.server.controller.action.NpcChatTime.getInstance();
	private l1j.server.server.controller.action.ControllerInterface auction		= l1j.server.server.controller.action.AuctionTime.getInstance();
	private l1j.server.server.controller.action.ControllerInterface special		= l1j.server.server.controller.action.SpecialDungeon.getInstance();
	private l1j.server.server.controller.action.ControllerInterface ultimate	= l1j.server.server.controller.action.UltimateBattleTime.getInstance();
	private l1j.server.server.controller.action.ControllerInterface night		= l1j.server.server.controller.action.GameTimeNight.getInstance();
	private l1j.server.server.controller.action.ControllerInterface tax			= l1j.server.server.controller.action.HouseTaxTime.getInstance();
	private l1j.server.server.controller.action.ControllerInterface contri		= l1j.server.server.controller.action.PledgeContributionSave.getInstance();
	private l1j.server.server.controller.action.ControllerInterface ship		= l1j.server.server.controller.action.ShipTime.getInstance();
	private l1j.server.server.controller.action.ControllerInterface event		= l1j.server.server.controller.action.Event.getInstance();
	private l1j.server.server.controller.action.ControllerInterface reward		= l1j.server.server.controller.action.ConnectReward.getInstance();
	private l1j.server.server.controller.action.ControllerInterface message		= l1j.server.server.controller.action.SystemMessage.getInstance();
	private l1j.server.server.controller.action.ControllerInterface logger		= l1j.server.server.monitor.LoggerInstance.getInstance();
	
	private static class newInstance {
		public static final ChangeMinuteController INSTANCE = new ChangeMinuteController();
	}
	public static ChangeMinuteController getInstance() {
		return newInstance.INSTANCE;
	}
	
	public void start(){
		l1j.server.server.model.gametime.RealTimeClock.getInstance().addListener(newInstance.INSTANCE, java.util.Calendar.MINUTE);
	}
	
	@Override
	public void onMonthChanged(l1j.server.server.model.gametime.BaseTime time) {}

	@Override
	public void onDayChanged(l1j.server.server.model.gametime.BaseTime time) {}

	@Override
	public void onHourChanged(l1j.server.server.model.gametime.BaseTime time) {}

	@Override
	public void onMinuteChanged(l1j.server.server.model.gametime.BaseTime time) {
		try {
			int minute = time.get(java.util.Calendar.MINUTE);
			user_check();
			ship.execute();
			fairly.execute();
			npcChat.execute();
			auction.execute();
			special.execute();
			ultimate.execute();
			night.execute();
			event.execute();
			reward.execute();
			if (minute % 10 == 0) {// 10분마다
				tax.execute();
				contri.execute();
			}
			if (minute % Config.MESSAGE.SYSTEM_MESSAGE_INTERVAL == 0) {
				message.execute();
			}
			logger.execute();// 로그 저장
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onSecondChanged(l1j.server.server.model.gametime.BaseTime time) {}
	
	void user_check() {
		long currentTime = System.currentTimeMillis();
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc == null || pc.getNetConnection() == null) {
				continue;
			}
			pcInventoryDelete(pc, currentTime);
			if (Config.COLLECTION.FAVOR_BOOK_ACTIVE) {
				checkFavorBookInventory(pc, currentTime);
			}
			if (Config.ATTEND.ATTENDANCE_ACTIVE) {
				checkAttendance(pc);
			}
			checkDenalsUser(pc);
			checkPCCafe(pc, currentTime);
			checkArca(pc, currentTime);
			checkMap(pc);
			if (Config.FREEBUFF.FEATHER_ACTIVE) {
				checkFeather(pc);
			}
			if (Config.FATIGUE.FATIGUE_ACTIVE) {
				checkFatigue(pc);
			}
			checkHalpahArmor(pc, currentTime);
		}
	}
	
	/**
	 * 인벤토리 기간제한 제거
	 */
	void pcInventoryDelete(L1PcInstance pc, long currentTime) {
		L1Inventory inv = pc.getInventory();
		if (inv == null) {
			return;
		}
		for (L1ItemInstance item : inv.getItems()) {
			if (item == null || item.getEndTime() == null || currentTime < item.getEndTime().getTime()) {
				continue;
			}
			int itemId = item.getItemId();
			if (itemId == L1ItemId.MERIN_CONTRACT) {
				pc.getInventory().storeItem(L1ItemId.MERIN_PIPE, 1);
				pc.sendPackets(L1ServerMessage.sm1823);
			} else if (itemId == L1ItemId.KILLTON_CONTRACT) {
				pc.getInventory().storeItem(L1ItemId.KILLTON_PIPE, 1);
				pc.sendPackets(L1ServerMessage.sm1823);
			} else if (itemId == 80500) {// 훈련소 열쇠
				KeyTable.deleteKeyId(item.getKeyId());
			} else if (pc.getDoll() != null && item.getId() == pc.getDoll().getItemObjId()) {
				pc.getDoll().deleteDoll(true);
			}
			//pc.sendPackets(new S_SystemMessage(String.format("%s의 사용시간이 만료 되어 소멸되었습니다.", item.getDesc())), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(81), item.getDesc()), true);
			inv.removeItem(item);
		}
	}
	
	/**
	 * 성물 인벤토리 기간제한 제거
	 */
	void checkFavorBookInventory(L1PcInstance pc, long currentTime){
		L1FavorBookInventory favorInv = pc.getFavorBook();
		if (favorInv == null) {
			return;
		}
		LinkedList<L1FavorBookUserObject> delList = null;
		for (L1FavorBookUserObject user : favorInv.getList()) {
			if (user == null || user.getCategory().getEndDate() == null || user.getCategory().getEndDate().getTime() > currentTime) {
				continue;
			}
			if (delList == null) {
				delList = new LinkedList<L1FavorBookUserObject>();
			}
			delList.add(user);
		}
		if (delList == null) {
			return;
		}
		for (L1FavorBookUserObject user : delList) {
			favorInv.delete(user);
		}
	}
	
	/**
	 * 맵 체크
	 */
	void checkMap(L1PcInstance pc){
		if (pc.isGm()) {
			return;
		}
		short mapId = pc.getMapId();
		if (mapId == 5167 && !GameServerSetting.DEVIL_ZONE) {// 악마왕 영토
			int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_GIRAN);
			pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true);
		} else if (mapId == 5153 && pc.getConfig().getDuelLine() == 0) {// 배틀존
			int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_GIRAN);
			pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true);
		} else if (pc.getMap().getInter() == L1InterServer.FORGOTTEN_ISLAND 
				&& !GameServerSetting.FORGOTTEN_ISLAND) {// 잊혀진 섬
			int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_HEINE);
			pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true, true);
			pc.sendPackets(L1SystemMessage.ISLAND_CLOSE);
		} else if (pc.getMap().getInter() == L1InterServer.DOMINATION_TOWER 
				&& !GameServerSetting.DOMINATION_TOWER) {// 지배의 탑
			int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_ADEN);
			pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true, true);
			pc.sendPackets(L1SystemMessage.INTER_SERVER_CLOSE);
		} else if ((mapId == 1318 || mapId == 1319) 
				&& !GameServerSetting.BLACK_DRAGON) {// 암흑룡의 던전
			int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_GIRAN);
			pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true, true);
			pc.sendPackets(L1SystemMessage.BLACK_DRAGON_CLOSE);
		} else if (((mapId == 15482 || mapId == 15492) && !War.getInstance()._isWolrdWarGiran)
				|| ((mapId == 15483 || mapId == 15493) && !War.getInstance()._isWolrdWarOrc)
				|| ((mapId == 15484 || mapId == 15494) && !War.getInstance()._isWolrdWarHeine)) {// 월드 공성전
			int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_ADEN);
			pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true, true);
			pc.sendPackets(L1SystemMessage.INTER_SERVER_CLOSE);
		} else if (pc.getMap().getInter() == L1InterServer.ANT_QUEEN 
				&& !GameServerSetting.ANT_QUEEN) {// 여왕개미 은신처
			int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_WINDAWOOD);
			pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true, true);
			pc.sendPackets(L1SystemMessage.INTER_SERVER_CLOSE);
		} else if (mapId == 12900) {// 버림받은 자들의 땅(인터) 서쪽
			//LocalTime now = LocalTime.now(ZoneId.of("Asia/Seoul"));
			LocalTime now = LocalTime.now(ZoneId.of(Config.SERVER.TIME_ZONE));			
			if (!(pc.getLevel() >= 90 && pc.getLevel() <= 91 
					&& now.isAfter(LocalTime.of(18, 30)) && now.isBefore(LocalTime.of(21, 00)))) {
				int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_OREN);
				pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true);
			}
		} else if (mapId == 11900) {// 버림받은 자들의 땅(인터) 동쪽
			//LocalTime now = LocalTime.now(ZoneId.of("Asia/Seoul"));
			LocalTime now = LocalTime.now(ZoneId.of(Config.SERVER.TIME_ZONE));
			if (!(pc.getLevel() >= 90 && pc.getLevel() <= 93 
					&& now.isAfter(LocalTime.of(18, 30)) && now.isBefore(LocalTime.of(21, 00)))) {
				int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_OREN);
				pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true);
			}
		} else if ((mapId == 70 || mapId == 83) 
				&& !(pc.getLevel() >= 85 && GameServerSetting.FORGOTTEN_ISLAND_LOCAL)) {// 잊혀진 섬(로컬), 잊혀진 섬행 배(로컬)
			int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_HEINE);
			pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true);
		}
	}
	
	/**
	 * 출석 체크 시간 경과
	 */
	void checkAttendance(L1PcInstance pc){
		AttendanceAccount attendAccount	= pc.getAccount().getAttendance();
		if (!attendAccount.isProgress()) {
			return;
		}
		for (AttendanceGroupType type : AttendanceGroupType.getUseList()) {
			byte[] attendBytes	= attendAccount.getGroupData(type);
			Map<Integer, AttendanceItem> items = attendAccount.getGroupItems(type);
			Map<Integer, HashMap<Integer, ArrayList<AttendanceRandomItem>>> randoms = attendAccount.getRandomItems(type);
			if (attendBytes == null) {
				continue;
			}
			boolean isRandom = false;
			for (int i=0; i<attendBytes.length; i++) {
				if (attendBytes[i] != 0) {// 진행중인 출석 체크
					continue;
				}
				attendBytes[i] = 1;// 시간 완료
				AttendanceItem item = items.get(i + 1);
				if (item != null && item.getBonusType() == AttendanceBonusType.RandomDiceItem) {
					attendRandomItemSetting(attendAccount, i + 1, type);
					isRandom = true;
				}
				pc.sendPackets(new S_AttenDanceCompleteNoti(i + 1, type, isRandom && randoms != null && randoms.containsKey(i + 1) ? randoms.get(i + 1) : null), true);
				break;
			}
		}
		attendAccount.updateCurrentIndex();
	}
	
	void attendRandomItemSetting(AttendanceAccount account, int index, AttendanceGroupType attendType){
		ArrayList<AttendanceRandomItem> randomList = AttendanceTable.getRandomItemListSelector(attendType);// 5개의 랜덤한 아이템
		HashMap<Integer, ArrayList<AttendanceRandomItem>> randomData = new HashMap<>(1);
		randomData.put(0, randomList);
		account.getRandomItems(attendType).put(index, randomData);
	}
	
	/**
	 * 할파스 갑옷류 발동 시간
	 */
	void checkHalpahArmor(L1PcInstance pc, long currentTime){
		L1ItemInstance item = pc.getInventory().getEquippedArmor();// 착용중인 갑옷
		if (item == null) {
			return;
		}
		int armorId = item.getItemId();
		if (armorId >= 23000 && armorId <= 23002 && item.getLastUsed() != null) {
			if (item.getLastUsed().getTime() < currentTime && !pc._halpasLoyaltyCheck) {
				pc._halpasLoyaltyCheck = true;
				pc.sendPackets(new S_SpellBuffNoti(true, L1SkillId.FAITH_OF_HALPAH, SkillIconNotiType.NEW), true);
			} else {
				pc._halpasLoyaltyCheck = false;
			}
		}
	}
	
	/**
	 * 버그 유저 체크
	 */
	void checkDenalsUser(L1PcInstance pc) {
		if (pc.isGm()) {
			return;
		}
		int totalS = pc.getAbility().getStatsAmount();
		int bonusS = pc.getHighLevel() - 50;
		if (bonusS < 0) {
			bonusS = 0;
		}
		int calst = totalS - (bonusS + pc.getElixirStats() + 75);
		if (calst > 0) {
			//String bugMent = String.format("%s은 스텟버그 의심 케릭터입니다!", pc.getName());
			String bugMent = String.format("%s is a character with a suspected bug!", pc.getName());
			for (L1PcInstance gm : L1World.getInstance().getAllGms()) {
				gm.sendPackets(new S_PacketBox(84, bugMent), true);
				gm.sendPackets(new S_SystemMessage(bugMent), true);
			}
		}
		
		if (pc.getReturnStatus() == 0 &&  pc.getHighLevel() < pc.getLevel()) {
			//String bugMent = String.format("%s은 레벨버그 의심 케릭터입니다! HIGHT_LV(%d), CUR_LV(%D)", pc.getName(), pc.getHighLevel(), pc.getLevel());
			String bugMent = String.format("%s is a suspected level bug character! HIGHT_LV(%d), CUR_LV(%D)", pc.getName(), pc.getHighLevel(), pc.getLevel());
			for (L1PcInstance gm : L1World.getInstance().getAllGms()) {
				gm.sendPackets(new S_PacketBox(84, bugMent), true);
				gm.sendPackets(new S_SystemMessage(bugMent), true);
			}
		}
	}
	
	/**
	 * PC방 버프 시간 조사
	 */
	void checkPCCafe(L1PcInstance pc, long currentTime) {
		if (!pc.isPCCafe() || pc.getAccount().getBuff_PCCafe() == null) {
			return;
		}

		long buffTime = pc.getAccount().getBuff_PCCafe().getTime();
		if (currentTime <= buffTime) {
			long restTime = buffTime - currentTime;
			Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(Config.SERVER.TIME_ZONE));
			calendar.setTimeInMillis(restTime);
			int d = calendar.get(Calendar.DATE) - 1;
			int h = calendar.get(Calendar.HOUR_OF_DAY);
			int m = calendar.get(Calendar.MINUTE);
			int sc = calendar.get(Calendar.SECOND);
			if (d == 0) {
				if (h > 0) {
					if (h == 1 && m == 0) {
//AUTO SRM: 						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, String.format( "[PC방 이용 시간] %d시간 %d분 %d초 남았습니다.", h, m, sc )), true); // CHECKED OK
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,String.format( S_SystemMessage.getRefText(927) + " %d " + S_SystemMessage.getRefText(664) + " %d " + S_SystemMessage.getRefText(106) + " %d " + S_SystemMessage.getRefText(928), h, m, sc )), true);
					}
				} else {
					if (m % 10 == 0) {
//AUTO SRM: 						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, String.format( "[PC방 이용 시간] %d분 %d초 남았습니다.", m, sc )), true); // CHECKED OK
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,String.format( S_SystemMessage.getRefText(927) + " %d " + S_SystemMessage.getRefText(106) + " %d " + S_SystemMessage.getRefText(928), m, sc )), true);
					}
				}
			}
			
			goldFeather(pc);
			return;
		}
		
		pc.setPCCafe(false);
		pc.getConfig().get_free_buff_shield().end_pccafe();
		
		L1Einhasad ein = pc.getAccount().getEinhasad();
		if (pc._isDragonFavorPCCafe) {
			ein.setDragonFavorPCCafe(pc, false);
		}
		
		pc.sendPackets(new S_RestExpInfoNoti(pc), true);
		pc.sendPackets(new S_ExpBoostingInfo(pc), true);
		pc.sendPackets(new S_UserStartSundry(false), true);
		pc.sendPackets(S_GameGatePCCafeCharge.STOP);
		if (Config.ATTEND.ATTENDANCE_ACTIVE && Config.ATTEND.ATTENDANCE_PCROOM_USE) {
			pc.sendPackets(new S_AttenDanceInfoNoti(pc, pc.getAccount().getAttendance(), 0), true);
		}
		if (pc.getSkill().hasSkillEffect(L1SkillId.EINHASAD_FAVOR) 
				&& !pc._isDragonFavor && ein.getPoint() < Config.EIN.REST_EXP_DEFAULT_RATION
				&& !pc.isPCCafe()) {
			ein.setDragonFavor(pc, true);
		}
		
		pc.sendPackets(L1GreenMessage.PCCAFE_BUFF_END);
		pc.sendPackets(L1SystemMessage.PCCAFE_BUFF_END);
		pc.getAccount().setBuff_PCCafe(null);
		pc.getAccount().updatePCCafeBuff();
	}
	
	/** 
	 * 픽시의 금빛 깃털 
	 */
	void goldFeather(L1PcInstance pc) {
		if (pc.isPrivateShop() || pc.isAutoClanjoin() || pc.getInventory().getSize() >= L1PcInventory.MAX_SIZE - 1 || pc.getInventory().getWeightPercent() >= 100) {
			return;
		}
    	if (++pc._pixyGoldFeatherCount >= Config.FREEBUFF.GOLD_FEATHER_TIME) {
    		pc._pixyGoldFeatherCount = 0;
    		// 최대 보유가능 수량
    		if (Config.FREEBUFF.GOLD_FEATHER_INVEN_MAX_VALUE > 0 
    				&& pc.getInventory().findItemIdCount(L1ItemId.PIXIE_GOLD_FEATHER) >= Config.FREEBUFF.GOLD_FEATHER_INVEN_MAX_VALUE) {
    			return;
    		}
    		FreeBuffShieldHandler handler = pc.getConfig().get_free_buff_shield();
    		if (handler == null) {
    			System.out.println(String.format("[C_KeepALIVE.goldFeather] FREE_BUFF_SHIELD_NOT_FOUND : CHAR_NAME(%s)", pc.getName()));
    			return;
    		}
    		// 일일 획득가능 수량
    		if (Config.FREEBUFF.GOLD_FEATHER_DAILY_MAX_VALUE > 0 
    				&& handler.get_pccafe_reward_item_count() >= Config.FREEBUFF.GOLD_FEATHER_DAILY_MAX_VALUE) {
    			return;
    		}
    		handler.add_pccafe_reward_item_count(Config.FREEBUFF.GOLD_FEATHER_COUNT);
			L1ItemInstance item =  pc.getInventory().storeItem(L1ItemId.PIXIE_GOLD_FEATHER, Config.FREEBUFF.GOLD_FEATHER_COUNT);
			pc.sendPackets(new S_ServerMessage(403, String.format("%s (%d)", item.getItem().getDesc(), Config.FREEBUFF.GOLD_FEATHER_COUNT)), true);
	    }
	}
	
	/**
	 * 픽시의 깃털 
	 */
	void checkFeather(L1PcInstance pc) {
		if (pc.isPrivateShop() || pc.isAutoClanjoin() || pc.getInventory().getSize() >= L1PcInventory.MAX_SIZE - 1 || pc.getInventory().getWeightPercent() >= 100) {
			return;
		}
		if (++pc._pixyFeatherCount >= Config.FREEBUFF.FEATHER_TIME) {
			L1ItemInstance item = pc.getInventory().storeItem(L1ItemId.PIXIE_FEATHER, Config.FREEBUFF.FEATHER_COUNT);
			pc.sendPackets(new S_ServerMessage(403, String.format("%s (%d)", item.getItem().getDesc(), Config.FREEBUFF.FEATHER_COUNT)), true);
		    pc._pixyFeatherCount = 0;
		}
	}
	
	/**
	 * 아르카(Tam)
	 */
	void checkArca(L1PcInstance pc, long currentTime){
		if (pc.isPrivateShop() || pc.isAutoClanjoin()) {
			return;
		}
		L1Arca arca = pc.getAccount().getArca();
		int activeCount = arca.getActiveCount(currentTime);
		if (activeCount > 0 
				&& ++pc._arcaDelayCount >= Config.ALT.ARCA_TIME) {
			int addPoint = Config.ALT.ARCA_REWARD_COUNT * activeCount;
			arca.addPoint(addPoint);
			//pc.sendPackets(new S_SystemMessage(String.format("[성장의 고리] %d단계 : Tam포인트(%d) 획득", activeCount, addPoint)), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(82), String.valueOf(activeCount), String.valueOf(addPoint)), true);
			pc.sendPackets(new S_TamPointNoti(arca.getPoint()), true);
			pc._arcaDelayCount = 0;
		}
	}
	
	/**
	 * 그랑카인(피로도)
	 */
	void checkFatigue(L1PcInstance pc) {
		if (pc.getRegion() == L1RegionStatus.SAFETY) {
			pc.getFatigue().addPoint(-1);
		} else {
			if (Config.FATIGUE.FATIGUE_EINHASAD) {
				if (pc.getAccount().getEinhasad().getPoint() <= Config.EIN.REST_EXP_DEFAULT_RATION) {
					pc.getFatigue().addPoint(1);
				}
			} else {
				pc.getFatigue().addPoint(1);
			}
		}
	}
}

