package l1j.server.GameSystem.dungeontimer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javolution.util.FastMap;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.dungeontimer.bean.L1DungeonTimerAccount;
import l1j.server.GameSystem.dungeontimer.bean.L1DungeonTimerCharacter;
import l1j.server.GameSystem.dungeontimer.bean.L1DungeonTimerInfo;
import l1j.server.GameSystem.dungeontimer.bean.L1DungeonTimerUser;
import l1j.server.GameSystem.dungeontimer.bean.TimerResetType;
import l1j.server.GameSystem.dungeontimer.loader.L1DungeonTimerLoader;
import l1j.server.GameSystem.dungeontimer.loader.L1DungeonTimerUserLoader;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.Instance.L1FieldObjectInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_TurnOnTeleportFlagNoti;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.CommonUtil.DungeonCheckType;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

/**
 * 유저에게 할당된 던전 타이머 핸들러
 * @author LinOffice
 */
public class L1DungeonTimer {
	private L1PcInstance _owner;
	private FastMap<Integer, L1DungeonTimerUser> _timers;
	private FastMap<Integer, L1DungeonTimerInfo> _mapInfos;
	
	public L1DungeonTimer(L1PcInstance owner){
		_owner		= owner;
		_timers		= L1DungeonTimerUserLoader.getDungeonTimer(_owner);
		_mapInfos	= new FastMap<Integer, L1DungeonTimerInfo>(L1DungeonTimerLoader.getTimerMapInfos());
	}
	
	/**
	 * 유저에게 할당된 타이머 리스트
	 * @return list
	 */
	public FastMap<Integer, L1DungeonTimerUser> getTimers(){
		return _timers;
	}
	
	/**
	 * 타이머가 존재하는 맵인지 조사한다.
	 * @param mapId
	 * @return boolean
	 */
	public boolean isTimerInfo(int mapId){
		return _mapInfos.containsKey(mapId);
	}
	
	/**
	 * 타이머가 존재하는 맵인지 조사한다.
	 * @param mapId
	 * @return L1DungeonTimerInfo
	 */
	public L1DungeonTimerInfo getTimerInfo(int mapId){
		return _mapInfos.get(mapId);
	}
	
	/**
	 * 타이머를 초기화 한다.
	 * @param timerId
	 * @param save
	 */
	public void resetTimer(int timerId, boolean save){
		resetTimer(_timers.get(timerId), save);
	}
	void resetTimer(L1DungeonTimerUser timer, boolean save){
		if (timer == null) {
			return;
		}
		timer.reset();// 초기화
		if (save) {
			upsert(timer);// 디비 저장
		}
	}
	
	/**
	 * 던전의 남은 시간을 타이머형태로 출력한다.
	 */
	public void sendTimerPacket(){
		int restTime = getRestTimer((int)_owner.getMapId());
		if (restTime > 0) {
			_owner.sendPackets(new S_PacketBox(S_PacketBox.TIME_COUNT, restTime), true);
		}
	}
	
	/**
	 * 타이머의 최대 시간을 조사한다.
	 * @param L1DungeonTimerInfo
	 * @return time
	 */
	public int getTimerValue(L1DungeonTimerInfo info){
		int value = info.getTimerValue();// 기본 시간
		if (info.getBonusLevel() > 0 && _owner.getLevel() >= info.getBonusLevel()) {
			value += info.getBonusValue();// 보너스 시간
		}
		if (info.getPcCafeBonusValue() > 0 && _owner.isPCCafe()) {
			value += info.getPcCafeBonusValue();
		}
		return value;
	}
	
	/**
	 * 맵에 입장 가능한지 타이머를 조사한다.
	 * @param mapId
	 * @return boolean
	 */
	public boolean isEnter(int mapId){
		L1DungeonTimerInfo info = getTimerInfo(mapId);
		if (info == null) {
			return true;
		}
		if (info.getMinLimitLevel() > 0 && _owner.getLevel() < info.getMinLimitLevel()) {// 최소 입장 레벨
			return false;
		}
		if (info.getMaxLimitLevel() > 0 && _owner.getLevel() > info.getMaxLimitLevel()) {// 최대 입장 레벨
			return false;
		}
		L1DungeonTimerUser timer	= _timers.get(info.getTimerId());
		int current 				= timer.getRemainSecond();// 사용한 시간
		int max						= getTimerValue(info);// 총 시간
		if (info.getMaxChargeCount() != 0 && timer.getChargeCount() > 0) {
			max						+= 3600 * timer.getChargeCount();
		}
		switch(info.getResetType()){
		case DAY:
			if (CommonUtil.getDungeonTimeCheck(timer.getResetTime(), max, current) == DungeonCheckType.FAILE) {
				return false;
			}
			break;
		case WEEK:
			if (CommonUtil.isWeekResetCheck(timer.getResetTime())) {
				return false;
			}
			if (max <= current) {
				return false;
			}
			break;
		case NONE:
			break;
		}
		return true;
	}
	
	/**
	 * 맵의 남은 시간을 조사한다.
	 * @param mapId
	 * @return time
	 */
	public int getRestTimer(int mapId){
		L1DungeonTimerInfo info		= getTimerInfo(mapId);
		if (info == null) {
			return 0;
		}
		L1DungeonTimerUser timer 	= _timers.get(info.getTimerId());
		int current					= timer.getRemainSecond();// 사용한 시간
		int max						= getTimerValue(info);// 총 시간
		if (info.getMaxChargeCount() != 0 && timer.getChargeCount() > 0) {
			max						+= 3600 * timer.getChargeCount();
		}
		int rest = max - current;
		if (rest < 0) {
			return 0;
		}
		return rest;
	}
	
	/**
	 * 타이머의 시간을 출력한다.
	 * @param max
	 * @param current
	 */
	void restTimerPrint(int max, int current){
		int hours	= (int)((max - current) / 3600);
		int minut	= (int)(((max - current) % 3600) / 60);
		_owner.sendPackets(hours > 0 ? new S_ServerMessage(1525, hours + StringUtil.EmptyString, minut + StringUtil.EmptyString) : new S_ServerMessage(1527, minut + StringUtil.EmptyString), true);
	}
	
	/**
	 * 던전 입장
	 * @param x
	 * @param y
	 * @param mapId
	 * @param inter
	 * @param itemid
	 * @param itemCount
	 */
	public void enter(int x, int y, int mapId, boolean inter, int itemid, int itemCount){
		L1DungeonTimerInfo info = getTimerInfo(mapId);
		if (info == null) {
			System.out.println(String.format("[Dungeon Timer] [Enter] not found mapId(%d)", mapId));
			return;
		}
		L1DungeonTimerUser timer	= _timers.get(info.getTimerId());
		int current					= timer.getRemainSecond();
		int max						= getTimerValue(info);
		DungeonCheckType entered	= CommonUtil.getDungeonTimeCheck(timer.getResetTime(), max, current);
		switch(entered){
		case SUCCESS:
			successEnter(max, current, timer, false, x, y, mapId, 3, inter, itemid, itemCount);
			break;
		case FAILE:
			failEnter(max);
			break;
		case RESET:
			successEnter(max, 0, timer, true, x, y, mapId, 3, inter, itemid, itemCount);
			break;
		}
	}
	
	/**
	 * 충전식 던전 입장
	 * @param x
	 * @param y
	 * @param mapId
	 * @return boolean
	 */
	public boolean enterCount(int x, int y, int mapId){
		L1DungeonTimerInfo info	= getTimerInfo(mapId);
		if (info == null) {
			System.out.println(String.format("[Dungeon Timer] [enterCount] not found mapId(%d)", mapId));
			return false;
		}
		L1DungeonTimerUser timer	= _timers.get(info.getTimerId());
		if (info.getResetType() == TimerResetType.DAY) {
			int current					= timer.getRemainSecond();
			int max						= getTimerValue(info);
			if(info.getMaxChargeCount() != 0){
				max						+= 3600 * timer.getChargeCount();
			}
			DungeonCheckType entered	= CommonUtil.getDungeonTimeCheck(timer.getResetTime(), max, current);
			switch(entered){
			case SUCCESS:
				successEnter(max, current, timer, false, x, y, mapId, 3, false, 0, 0);
				return true;
			case FAILE:
				failEnter(max);
				return false;
			case RESET:
				successEnter(max, 0, timer, true, x, y, mapId, 3, false, 0, 0);
				return true;
			}
		} else {
			if (info.getResetType() == TimerResetType.WEEK && CommonUtil.isWeekResetCheck(timer.getResetTime())) {// 초기화
				resetTimer(timer, true);
				_owner.getConfig().finishPlaySupport();
			} else {
				if (timer.getChargeCount() > 0) {
					int current	= timer.getRemainSecond();
					int max		= 3600 * timer.getChargeCount();
					if (max > current) {
						successEnter(max, current, timer, false, x, y, mapId, 5, false, 0, 0);
						return true;
					} else {
						_owner.sendPackets(new S_ServerMessage(1522, timer.getChargeCount() + StringUtil.EmptyString), true); // 입장시간: {0}시간 모두 사용
						_owner.getConfig().finishPlaySupport();
					}
				} else {
					_owner.getConfig().finishPlaySupport();
				}
			}
		}
		return false;
	}
	
	/**
	 * 고대 신의 사원 입장
	 */
	public void enterAncientGodEmployee(){
		L1DungeonTimerInfo info = getTimerInfo(1209);
		if (info == null) {
			System.out.println(String.format("[Dungeon Timer] [enterAncientGodEmployee] not found mapId(%d)", 1209));
			return;
		}
		L1DungeonTimerUser timer	= _timers.get(info.getTimerId());
		int current					= timer.getRemainSecond();
		int max						= getTimerValue(info);
		DungeonCheckType entered = CommonUtil.getDungeonTimeCheck(timer.getResetTime(), max, current);
		switch(entered){
		case SUCCESS:
			successEnter(max, current, timer, false, 32616, 32927, 1209, 2, false, 0, 0);
			break;
		case RESET:
			successEnter(max, 0, timer, true, 32616, 32927, 1209, 2, false, 0, 0);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 드래곤의 서식지 입장
	 * @param field
	 */
	public void enterDragon(L1FieldObjectInstance field){
		if (_owner.getInventory().checkItem(6020)) {
//AUTO SRM: 			_owner.sendPackets(new S_SystemMessage("용 사냥꾼의 징표 소지시 입장 불가"), true); // CHECKED OK
			_owner.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(24), true), true);
			return;
		}
		L1Location loc				= null;
		L1DungeonTimerInfo info		= getTimerInfo(field.getMoveMapId());
		if (info == null) {
			System.out.println(String.format("[Dungeon Timer] [enterDragon] not found mapId(%d)", field.getMoveMapId()));
			return;
		}
		L1DungeonTimerUser timer	= _timers.get(info.getTimerId());
		int current					= timer.getRemainSecond();
		int max						= getTimerValue(info);
		boolean enter				= false;
		DungeonCheckType entered	= CommonUtil.getDungeonTimeCheck(timer.getResetTime(), max, current);
		switch(entered){
		case SUCCESS:
			restTimerPrint(max, current);
			enter = true;
			break;
		case FAILE:
			failEnter(max);
			break;
		case RESET:
			resetTimer(timer, true);
			restTimerPrint(max, 0);
			enter = true;
			break;
		}
		if (enter) {
			loc = new L1Location(32680, 32951, field.getMoveMapId()).randomLocation(5, true);
			enterTeleport(loc.getX(), loc.getY(), loc.getMapId(), false);
			field.setDragonEnterCount(field.getDragonEnterCount() + 1);
			if (field.getDragonEnterCount() >= 16) {
				field.deleteMe();// 포털 제거
			}
			loc = null;
		}
	}
	
	/**
	 * 입장 가능
	 * @param max
	 * @param current
	 * @param user
	 * @param reset
	 * @param x
	 * @param y
	 * @param mapId
	 * @param randomLoc
	 * @param inter
	 * @param consumeId
	 * @param consumeCount
	 */
	void successEnter(int max, int current, L1DungeonTimerUser timer, boolean reset, int x, int y, int mapId, int randomLoc, boolean inter, int consumeId, int consumeCount){
		L1Location loc		= new L1Location(x, y, mapId).randomLocation(randomLoc, true);
		if (reset) {
			resetTimer(timer, true);
		}
		if (consumeId != 0 && consumeCount != 0) {
			_owner.getInventory().consumeItem(consumeId, consumeCount);
		}
		restTimerPrint(max, current);
		enterTeleport(loc.getX(), loc.getY(), loc.getMapId(), inter);
		loc = null;
	}
	
	/**
	 * 입장 불가
	 * @param max
	 */
	void failEnter(int max){
		int hours	= (int)(max / 3600);
		int minut	= (int)((max % 3600) / 60);
		_owner.sendPackets(minut > 0 ? new S_ServerMessage(1524, hours + StringUtil.EmptyString, minut + StringUtil.EmptyString) : new S_ServerMessage(1522, hours + StringUtil.EmptyString), true);// 입장시간: {0}시간 {1}분 모두 사용
		_owner.getConfig().finishPlaySupport();
	}
	
	/**
	 * 텔레포트
	 * @param x
	 * @param y
	 * @param mapId
	 * @param inter
	 */
	void enterTeleport(int x, int y, int mapId, boolean inter){
		if (inter) {
			_owner.getTeleport().start(x, y, (short) mapId, (byte)5, true, true);
		} else {
			_owner.sendPackets(S_TurnOnTeleportFlagNoti.TURN_ON_TELEPORT_NOTI);
			_owner.getTeleport().start(x, y, (short) mapId, (byte)5, true);
		}
		_owner.getSkill().setSkillEffect(L1SkillId.ABSOLUTE_BARRIER, 3000);
	}
	
	/**
	 * 타이머에 머무르는 상태
	 * 현재 맵 기준으로 정보를 취득한다.
	 * @param info
	 */
	public void stay(){
		L1DungeonTimerInfo info		= _mapInfos.get((int)_owner.getMapId());
		if (info == null) {
			return;
		}
		L1DungeonTimerUser timer	= _timers.get(info.getTimerId());
		int current					= timer.increaseAndGetRemainSecond();
		switch(info.getResetType()){
		case DAY:
			int max					= getTimerValue(info);
			if (info.getMaxChargeCount() != 0) {
				max					+= 3600 * timer.getChargeCount();
			}
			DungeonCheckType stay	= CommonUtil.getDungeonTimeCheck(timer.getResetTime(), max, current);
			switch(stay){
			case FAILE:
				stayFail(max);
				break;
			case RESET:
				stayReset(max, timer);
				break;
			default:
				break;
			}
			break;
		case WEEK:
			boolean reset			= CommonUtil.isWeekResetCheck(timer.getResetTime());
			if (info.getMaxChargeCount() != 0) {
				if (reset) {// 초기화
					resetTimer(timer, true);
					releaseTeleport(Getback.GetBack_Location(_owner));
				} else if (timer.getChargeCount() <= 0 || (3600 * timer.getChargeCount()) <= current) {
					releaseTeleport(Getback.GetBack_Location(_owner));
				}
			}
			break;
		case NONE:
			break;
		}
	}
	
	/**
	 * 타이머에 머무르지 못한다.
	 * @param max
	 */
	void stayFail(int max){// 던전시간 종료
		int hours	= (int)max / 3600;
		int minut	= (int)(max % 3600) / 60;
		_owner.sendPackets(minut > 0 ? new S_ServerMessage(1524, hours + StringUtil.EmptyString, minut + StringUtil.EmptyString) : new S_ServerMessage(1522, hours + StringUtil.EmptyString), true); // 입장시간: {0}시간 {1}분 모두 사용
		int[] loc = Getback.GetBack_Location(_owner);
		if (_owner.getMapId() == 11900 || _owner.getMapId() == 12900) {
			loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_OREN);
		} else if (_owner.getMapId() == 1209) {
			loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_ADEN);
		}
		releaseTeleport(loc);
	}
	
	/**
	 * 타이머 초기화
	 * @param max
	 * @param user
	 */
	void stayReset(int max, L1DungeonTimerUser timer){// 던전시간 초기화
		int hours	= (int)max / 3600;
		int minut	= (int)(max % 3600) / 60;
		resetTimer(timer, true);
		_owner.sendPackets(new S_PacketBox(S_PacketBox.TIME_COUNT, max - 1), true);
		_owner.sendPackets(minut > 0 ? new S_ServerMessage(1525, hours + StringUtil.EmptyString, minut + StringUtil.EmptyString) : new S_ServerMessage(1526, hours + StringUtil.EmptyString), true);
	}
	
	/**
	 * 타이머에 머무르지 못하고 귀환한다.
	 * @param loc
	 */
	void releaseTeleport(int[] loc){
		_owner.getTeleport().start(loc[0], loc[1], (short) loc[2], (byte)5, true);
		_owner.getConfig().finishPlaySupport();
	}
	
	static final String ACCOUNT_UPSERT_QUERY = "INSERT INTO "
			+ "dungeon_timer_account "
			+ "(account, timerId, remainSecond, chargeCount, resetTime) "
			+ "VALUES (?, ?, ?, ?, ?) "
			+ "ON DUPLICATE KEY UPDATE "
			+ "remainSecond=?, chargeCount=?, resetTime=?";
	
	static final String CHARACTER_UPSERT_QUERY = "INSERT INTO "
			+ "dungeon_timer_character "
			+ "(charId, timerId, remainSecond, chargeCount, resetTime) "
			+ "VALUES (?, ?, ?, ?, ?) "
			+ "ON DUPLICATE KEY UPDATE "
			+ "remainSecond=?, chargeCount=?, resetTime=?";
	
	/**
	 * 타이머를 갱신한다.(단일)
	 * @param timer
	 */
	public void upsert(L1DungeonTimerUser timer){
		boolean type				= timer instanceof L1DungeonTimerAccount;
		Connection con				= null;
		PreparedStatement pstm		= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement(type ?  ACCOUNT_UPSERT_QUERY : CHARACTER_UPSERT_QUERY);
			int i	= 0;
			if (type) {
				pstm.setString(++i, ((L1DungeonTimerAccount) timer).getAccount());
			} else {
				pstm.setInt(++i, ((L1DungeonTimerCharacter) timer).getCharId());
			}
			pstm.setInt(++i, timer.getTimerId());
			pstm.setInt(++i, timer.getRemainSecond());
			pstm.setInt(++i, timer.getChargeCount());
			pstm.setTimestamp(++i, timer.getResetTime());
			pstm.setInt(++i, timer.getRemainSecond());
			pstm.setInt(++i, timer.getChargeCount());
			pstm.setTimestamp(++i, timer.getResetTime());
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	/**
	 * 타이머를 갱신한다.(전체)
	 */
	public void upsert(){
		if (_timers == null || _timers.isEmpty()) {
			return;
		}
		L1DungeonTimerUser user		= null;
		Connection con				= null;
		PreparedStatement pstm		= null;
		try{
			con		= L1DatabaseFactory.getInstance().getConnection();
			con.setAutoCommit(false);
			
			pstm	= con.prepareStatement(ACCOUNT_UPSERT_QUERY);
			int accountCnt = 0;
			L1DungeonTimerAccount acc = null;
			for (int key : _timers.keySet()) {
				user = _timers.get(key);
				if (user instanceof L1DungeonTimerAccount) {
					acc = (L1DungeonTimerAccount)user;
					int i = 0;
					pstm.setString(++i, acc.getAccount());
					pstm.setInt(++i, acc.getTimerId());
					pstm.setInt(++i, acc.getRemainSecond());
					pstm.setInt(++i, acc.getChargeCount());
					pstm.setTimestamp(++i, acc.getResetTime());
					pstm.setInt(++i, acc.getRemainSecond());
					pstm.setInt(++i, acc.getChargeCount());
					pstm.setTimestamp(++i, acc.getResetTime());
					pstm.addBatch();
					pstm.clearParameters();
					accountCnt++;
				}
			}
			if (accountCnt > 0) {
				pstm.executeBatch();
				pstm.clearBatch();
				con.commit();
			}
			SQLUtil.close(pstm);
			
			pstm	= con.prepareStatement(CHARACTER_UPSERT_QUERY);
			int charCnt = 0;
			L1DungeonTimerCharacter cha = null;
			for (int key : _timers.keySet()) {
				user = _timers.get(key);
				if (user instanceof L1DungeonTimerCharacter) {
					cha = (L1DungeonTimerCharacter)user;
					int i = 0;
					pstm.setInt(++i, cha.getCharId());
					pstm.setInt(++i, cha.getTimerId());
					pstm.setInt(++i, cha.getRemainSecond());
					pstm.setInt(++i, cha.getChargeCount());
					pstm.setTimestamp(++i, cha.getResetTime());
					pstm.setInt(++i, cha.getRemainSecond());
					pstm.setInt(++i, cha.getChargeCount());
					pstm.setTimestamp(++i, cha.getResetTime());
					pstm.addBatch();
					pstm.clearParameters();
					charCnt++;
				}
			}
			if (charCnt > 0) {
				pstm.executeBatch();
				pstm.clearBatch();
				con.commit();
			}
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch(SQLException sqle){
				sqle.printStackTrace();
			}
			e.printStackTrace();
		} catch (Exception e) {
			try {
				con.rollback();
			} catch(SQLException sqle){
				sqle.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			SQLUtil.close(pstm, con);
		}
	}
	
	public void dispose(){
		_timers.clear();
		_mapInfos.clear(); 
	}
	
}


