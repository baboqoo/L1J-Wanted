package l1j.server.GameSystem.dungeontimer.loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javolution.util.FastMap;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.dungeontimer.bean.L1DungeonTimerAccount;
import l1j.server.GameSystem.dungeontimer.bean.L1DungeonTimerCharacter;
import l1j.server.GameSystem.dungeontimer.bean.L1DungeonTimerInfo;
import l1j.server.GameSystem.dungeontimer.bean.L1DungeonTimerUser;
import l1j.server.GameSystem.dungeontimer.bean.TimerUseType;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

public class L1DungeonTimerUserLoader {
	private static L1DungeonTimerUserLoader _instance;
	public static L1DungeonTimerUserLoader getInstance(){
		if (_instance == null) {
			_instance = new L1DungeonTimerUserLoader();
		}
		return _instance;
	}
	
	private static final FastMap<String, FastMap<Integer, L1DungeonTimerUser>> ACCOUNT_DATA	= new FastMap<>();
	private static final FastMap<Integer, FastMap<Integer, L1DungeonTimerUser>> CHAR_DATA	= new FastMap<>();
	
	/**
	 * 유저에 할당할 타이머를 조사한다.
	 * 타이머가 없으면 생성한다.
	 * @param pc
	 * @return 타이머 리스트
	 */
	public static FastMap<Integer, L1DungeonTimerUser> getDungeonTimer(L1PcInstance pc){
		FastMap<Integer, L1DungeonTimerUser> timers = new FastMap<>();
		
		// 가동중인 타이머 전체 조사
		for (L1DungeonTimerInfo info : L1DungeonTimerLoader.getTimerInfos().values()) {
			L1DungeonTimerUser user = null;
			
			// 캐릭터 타이머에서 조사
			if (CHAR_DATA.containsKey(pc.getId())) {
				for (L1DungeonTimerUser cha : CHAR_DATA.get(pc.getId()).values()) {
					if (info.getTimerId() == cha.getTimerId()) {
						user	= cha;
						break;
					}
				}
			}
			
			// 계정 타이머에서 조사
			if (ACCOUNT_DATA.containsKey(pc.getAccountName())) {
				for (L1DungeonTimerUser acc : ACCOUNT_DATA.get(pc.getAccountName()).values()) {
					if (info.getTimerId() == acc.getTimerId()) {
						user	= acc;
						break;
					}
				}
			}
			
			// 사용중인 타이머 존재
			if (user != null) {
				timers.put(info.getTimerId(), user);
				continue;
			}
			
			// 새로 생성
			timers.put(info.getTimerId(), createTimer(info, pc));
		}
		return timers;
	}
	
	static L1DungeonTimerUser createTimer(L1DungeonTimerInfo info, L1PcInstance pc){
		L1DungeonTimerUser user;
		FastMap<Integer, L1DungeonTimerUser> map;
		if (info.getUseType() == TimerUseType.ACCOUNT) {
			user = new L1DungeonTimerAccount(pc.getAccountName(), info.getTimerId(), 0, 0, null);
			map = ACCOUNT_DATA.get(pc.getAccountName());
			if (map == null) {
				map = new FastMap<>();
				ACCOUNT_DATA.put(pc.getAccountName(), map);
			}
		} else {
			user = new L1DungeonTimerCharacter(pc.getId(), info.getTimerId(), 0, 0, null);
			map = CHAR_DATA.get(pc.getId());
			if (map == null) {
				map = new FastMap<>();
				CHAR_DATA.put(pc.getId(), map);
			}
		}
		map.put(user.getTimerId(), user);
		return user;
	}
	
	private L1DungeonTimerUserLoader(){
		load();
	}
	
	void load(){
		Connection con				= null;
		PreparedStatement pstm		= null;
		ResultSet rs				= null;
		L1DungeonTimerUser timer	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			
			pstm	= con.prepareStatement("SELECT * FROM dungeon_timer_account ORDER BY account ASC, timerId ASC");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				String account				= rs.getString("account");
				int timerId					= rs.getInt("timerId");
				int remainSecond			= rs.getInt("remainSecond");
				int chargeCount				= rs.getInt("chargeCount");
				Timestamp resetTime			= rs.getTimestamp("resetTime");
				timer = new L1DungeonTimerAccount(account, timerId, remainSecond, chargeCount, resetTime);
				FastMap<Integer, L1DungeonTimerUser> map = ACCOUNT_DATA.get(account);
				if (map == null) {
					map = new FastMap<>();
					ACCOUNT_DATA.put(account, map);
				}
				map.put(timer.getTimerId(), timer);
			}
			SQLUtil.close(rs, pstm);
			
			pstm	= con.prepareStatement("SELECT * FROM dungeon_timer_character ORDER BY charId ASC, timerId ASC");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				int charId					= rs.getInt("charId");
				int timerId					= rs.getInt("timerId");
				int remainSecond			= rs.getInt("remainSecond");
				int chargeCount				= rs.getInt("chargeCount");
				Timestamp resetTime			= rs.getTimestamp("resetTime");
				timer = new L1DungeonTimerCharacter(charId, timerId, remainSecond, chargeCount, resetTime);
				FastMap<Integer, L1DungeonTimerUser> map = CHAR_DATA.get(charId);
				if (map == null) {
					map = new FastMap<>();
					CHAR_DATA.put(charId, map);
				}
				map.put(timer.getTimerId(), timer);
			}
			SQLUtil.close(rs, pstm);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
}

