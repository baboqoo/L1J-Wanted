package l1j.server.GameSystem.dungeontimer.loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

import javolution.util.FastMap;
import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.dungeontimer.bean.L1DungeonTimeChargedItem;
import l1j.server.GameSystem.dungeontimer.bean.L1DungeonTimerInfo;
import l1j.server.GameSystem.dungeontimer.bean.TimerResetType;
import l1j.server.GameSystem.dungeontimer.bean.TimerUseType;
import l1j.server.server.serverpackets.S_UserPlayInfoNoti.eChargedTimeMapGroup;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class L1DungeonTimerLoader {
	private static L1DungeonTimerLoader _instance;
	public static L1DungeonTimerLoader getInstance(){
		if (_instance == null) {
			_instance = new L1DungeonTimerLoader();
		}
		return _instance;
	}
	
	private static final FastMap<Integer, L1DungeonTimerInfo> ID_DATA			= new FastMap<Integer, L1DungeonTimerInfo>();
	private static final FastMap<Integer, L1DungeonTimerInfo> MAP_DATA			= new FastMap<Integer, L1DungeonTimerInfo>();
	private static final FastMap<Integer, L1DungeonTimeChargedItem> ITEM_DATA	= new FastMap<Integer, L1DungeonTimeChargedItem>();
	
	/**
	 * 타이머 리스트 조사(타이머아이디)
	 * @return list
	 */
	public static FastMap<Integer, L1DungeonTimerInfo> getTimerInfos(){
		return ID_DATA;
	}
	
	/**
	 * 타이머 리스트 조사(맵)
	 * @return list
	 */
	public static FastMap<Integer, L1DungeonTimerInfo> getTimerMapInfos(){
		return MAP_DATA;
	}
	
	/**
	 * 타이머 아이디 존재 여부
	 * @param id
	 * @return boolean
	 */
	public static boolean isDungeonTimerId(int id){
		return ID_DATA.containsKey(id);
	}
	
	/**
	 * 타이머 정보 조사
	 * @param id
	 * @return L1DungeonTimerInfo
	 */
	public static L1DungeonTimerInfo getDungeonTimerId(int id){
		return ID_DATA.get(id);
	}
	
	/**
	 * 타이머 아이디 존재 여부
	 * @param mapId
	 * @return boolean
	 */
	public static boolean isDungeonTimerMap(int mapId){
		return MAP_DATA.containsKey(mapId);
	}
	
	/**
	 * 타이머 정보 조사
	 * @param mapId
	 * @return L1DungeonTimerInfo
	 */
	public static L1DungeonTimerInfo getDungenTimerMap(int mapId){
		return MAP_DATA.get(mapId);
	}
	
	/**
	 * 타이머 충전 아이템 조사
	 * @param itemId
	 * @return boolean
	 */
	public static boolean isDungeonTimerItem(int itemId){
		return ITEM_DATA.containsKey(itemId);
	}
	
	/**
	 * 타이머 충전 아이템 조사
	 * @param itemId
	 * @return timerId
	 */
	public static L1DungeonTimeChargedItem getDungeonTimerItem(int itemId){
		return ITEM_DATA.get(itemId);
	}
	
	private L1DungeonTimerLoader(){
		load();
	}
	
	void load(){
		Connection con				= null;
		PreparedStatement pstm		= null;
		ResultSet rs				= null;
		L1DungeonTimerInfo timer	= null;
		try{
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM dungeon_timer ORDER BY timerId ASC");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				int timerId					= rs.getInt("timerId");
				String descId				= rs.getString("descId");
				TimerUseType useType		= TimerUseType.fromString(rs.getString("useType"));
				FastTable<Integer> mapIds	= getMapIds(rs);
				int timerValue				= rs.getInt("timerValue");
				int bonusLevel				= rs.getInt("bonusLevel");
				int bonusValue				= rs.getInt("bonusValue");
				int pccafeBonusValue		= rs.getInt("pccafeBonusValue");
				TimerResetType resetType	= TimerResetType.fromString(rs.getString("resetType"));
				int minLimitLevel			= rs.getInt("minlimitLevel");
				int maxLimitLevel			= rs.getInt("maxLimitLevel");
				int serialId				= rs.getInt("serialId");
				String serialDescId			= rs.getString("serialDescId");
				int maxChargeCount			= rs.getInt("maxChargeCount");
				eChargedTimeMapGroup group	= eChargedTimeMapGroup.fromString(rs.getString("group"));
				timer = new L1DungeonTimerInfo(timerId, descId, useType, mapIds, timerValue,
						bonusLevel, bonusValue, pccafeBonusValue,
						resetType, minLimitLevel, maxLimitLevel, serialId, serialDescId, maxChargeCount, group);
				ID_DATA.put(timerId, timer);
				if (mapIds != null) {
					for (Integer mapId : mapIds) {
						if (!MAP_DATA.containsKey(mapId)) {
							MAP_DATA.put(mapId, timer);
						}
					}
				}
			}
			SQLUtil.close(rs, pstm);
			
			pstm	= con.prepareStatement("SELECT * FROM dungeon_timer_item ORDER BY itemId ASC");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				int itemId = rs.getInt("itemId");
				int timerId = rs.getInt("timerId");
				int groupId = rs.getInt("groupId");
				ITEM_DATA.put(itemId, new L1DungeonTimeChargedItem(itemId, timerId, groupId));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	FastTable<Integer> getMapIds(ResultSet rs) throws SQLException{
		String mapText		= rs.getString("mapIds");
		if (StringUtil.isNullOrEmpty(mapText)) {
			return null;
		}
		mapText				= mapText.replaceAll(StringUtil.LineString, StringUtil.EmptyString);
		StringTokenizer st	= new StringTokenizer(mapText, StringUtil.CommaString);
		int size = st.countTokens();
		FastTable<Integer> mapIds = new FastTable<Integer>(size);
		for (int i=0; i<size; i++) {
			mapIds.add(Integer.parseInt(st.nextToken().trim()));
		}
		return mapIds;
	}
	
	public void reload(){
		ID_DATA.clear();
		MAP_DATA.clear();
		ITEM_DATA.clear();
		load();
	}
}

