package l1j.server.GameSystem.attendance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import javolution.util.FastMap;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.attendance.bean.AttendanceAccount;
import l1j.server.GameSystem.attendance.bean.AttendanceRewardHistory;
import l1j.server.GameSystem.attendance.bean.AttendanceRandomItem;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class AttendanceAccountTable {
	private static AttendanceAccountTable _instance;
	public static AttendanceAccountTable getInstance(){
		if (_instance == null) {
			_instance = new AttendanceAccountTable();
		}
		return _instance;
	}
	
	private FastMap<String, AttendanceAccount> _info;
	
	private AttendanceAccountTable(){
		_info = new FastMap<String, AttendanceAccount>();
		load();
	}
	
	ConcurrentHashMap<AttendanceGroupType, byte[]> parseLoadGroupData(byte[] data){
		ConcurrentHashMap<AttendanceGroupType, byte[]> groupData = new ConcurrentHashMap<>();
		for (int offset=0; offset<data.length; offset++) {
			byte flag = data[offset];
			if (flag == (byte)0xFF){// 그룹 구분
				AttendanceGroupType type = AttendanceGroupType.fromInt(data[++offset]);// 구분값 읽을 위치
				
				int readLast = data.length;
				for (int j=offset; j<data.length; j++) {
					if (data[j] == (byte)0xFF) {// 다음 그룹 구분
						readLast = j;
						break;
					}
				}
				
				byte[] value = new byte[readLast - offset - 1];
				for (int j=0; j<value.length; j++) {
					value[j] = data[++offset];
				}
				groupData.put(type, value);
			}
		}
		return groupData;
	}
	
	byte[] parseInsertGroupData(ConcurrentHashMap<AttendanceGroupType, byte[]> groupData){
		int size = groupData.size() << 1;
		for (byte[] data : groupData.values()) {
			size += data.length;
		}
		byte[] result = new byte[size];
		int cnt = -1;
		for (Map.Entry<AttendanceGroupType, byte[]> entry : groupData.entrySet()) {
			result[++cnt] = (byte)0xFF;
			result[++cnt] = (byte)entry.getKey().getGroupId();
			for (byte data : entry.getValue()) {
				result[++cnt] = data;
			}
		}
		return result;
	}
	
	ConcurrentHashMap<AttendanceGroupType, Boolean> parseLoadGroupOpen(byte[] data){
		ConcurrentHashMap<AttendanceGroupType, Boolean> result = new ConcurrentHashMap<>();
		for (int offset=0; offset<data.length; offset++) {
			byte flag = data[offset];
			if (flag == (byte)0xFF){// 그룹 구분
				AttendanceGroupType type = AttendanceGroupType.fromInt(data[++offset]);// 구분값 읽을 위치
				boolean value = data[++offset] == (byte)0x01 ? true : false;// 상태갑 읽을 위치
				if (type == null) {
					continue;
				}
				result.put(type, value);
			}
		}
		return result;
	}
	
	byte[] parseInsertGroupOpen(ConcurrentHashMap<AttendanceGroupType, Boolean> opens){
		int size = opens.size() * 3;
		byte[] result = new byte[size];
		int cnt = -1;
		for (Map.Entry<AttendanceGroupType, Boolean> entry : opens.entrySet()) {
			result[++cnt] = (byte)0xFF;
			result[++cnt] = (byte)entry.getKey().getGroupId();
			result[++cnt] = entry.getValue() == true ? (byte) 0x01 : (byte)0x00; 
		}
		return result;
	}
	
	private static final String DIVID_REGEX	= "===============";
	private static final String FLAG_REGEX	= "---------------";
	
	ConcurrentHashMap<AttendanceGroupType, HashMap<Integer, HashMap<Integer, ArrayList<AttendanceRandomItem>>>> parseLoadRandomItem(String str){
		ConcurrentHashMap<AttendanceGroupType, HashMap<Integer, HashMap<Integer, ArrayList<AttendanceRandomItem>>>> result = new ConcurrentHashMap<>();
		if (StringUtil.isNullOrEmpty(str)) {
			return result;
		}
		
		HashMap<Integer, HashMap<Integer, ArrayList<AttendanceRandomItem>>> map = null;
		String[] array = str.split(DIVID_REGEX);
		for (int i=0; i<array.length; i++) {
			String one = array[i];
			String[] oneDetail = one.split(FLAG_REGEX);
			AttendanceGroupType groupType = AttendanceGroupType.fromInt(Integer.parseInt(oneDetail[0].trim()));
			if (groupType == null) {
				continue;
			}
			
			map = new HashMap<>();
			if (!StringUtil.isNullOrEmpty(oneDetail[1])) {
				AttendanceRandomItem radomItem = null;
				StringTokenizer st = new StringTokenizer(oneDetail[1].trim(), StringUtil.LineString);
				while (st.hasMoreElements()) {
					String oneLineText = st.nextToken().replaceAll(StringUtil.EmptyOneString, StringUtil.EmptyString);// 공백 제거
					String[] randomArray = oneLineText.split(StringUtil.ColonString);// 인덱스 분리, 0:그룹, 1:인덱스, 2:초기화횟수, 3:아이템리스트
					String[] randomItemArray = randomArray[3].split(StringUtil.CommaString);// 아이템 분리
					ArrayList<AttendanceRandomItem> randomItemList = new ArrayList<AttendanceRandomItem>(randomItemArray.length);
					for (String itemIndex : randomItemArray) {
						radomItem = AttendanceTable.getRandomItem(groupType, Integer.parseInt(itemIndex));
						if (radomItem != null) {
							randomItemList.add(radomItem);
						}
					}
					HashMap<Integer, ArrayList<AttendanceRandomItem>> randomInfo = new HashMap<Integer, ArrayList<AttendanceRandomItem>>(1);
					randomInfo.put(Integer.parseInt(randomArray[2]), randomItemList);
					map.put(Integer.parseInt(randomArray[1]), randomInfo);
				}
			}
			result.put(groupType, map);
		}
		return result;
	}
	
	String parseInsertRandomItem(ConcurrentHashMap<AttendanceGroupType, HashMap<Integer, HashMap<Integer, ArrayList<AttendanceRandomItem>>>> randomItems){
		if (randomItems == null || randomItems.isEmpty()) {
			return StringUtil.EmptyString;
		}
		
		StringBuilder sb = new StringBuilder();
		int cnt = 0;
		AttendanceGroupType key = null;
		HashMap<Integer, HashMap<Integer, ArrayList<AttendanceRandomItem>>> value = null;
		for (Map.Entry<AttendanceGroupType, HashMap<Integer, HashMap<Integer, ArrayList<AttendanceRandomItem>>>> entry : randomItems.entrySet()) {
			cnt++;
			key		= entry.getKey();
			value	= entry.getValue();
			sb.append(key.getGroupId()).append(StringUtil.LineString);
			sb.append(FLAG_REGEX).append(StringUtil.LineString);
			
			if (value != null && !value.isEmpty()) {
				for (Map.Entry<Integer, HashMap<Integer, ArrayList<AttendanceRandomItem>>> detail : value.entrySet()) {
					sb.append(detail.getKey()).append(StringUtil.ColonString);
					HashMap<Integer, ArrayList<AttendanceRandomItem>> items = detail.getValue();
					if (!items.isEmpty()) {
						for (Map.Entry<Integer, ArrayList<AttendanceRandomItem>> itemEntry : items.entrySet()) {
							sb.append(itemEntry.getKey()).append(StringUtil.ColonString);
							ArrayList<AttendanceRandomItem> itemList = itemEntry.getValue();
							for (int i=0; i<itemList.size(); i++) {
								sb.append(i == 0 ? StringUtil.EmptyString : StringUtil.CommaString).append(itemList.get(i).getIndex());
							}
							sb.append(StringUtil.LineString);
							break;
						}
					}
				}
			}
			
			if (cnt < randomItems.size()) {
				sb.append(DIVID_REGEX).append(StringUtil.LineString);
			}
		}
		return sb.toString();
	}
	
	ConcurrentHashMap<AttendanceGroupType, ArrayList<AttendanceRewardHistory>> parseLoadRewardHistory(String str){
		ConcurrentHashMap<AttendanceGroupType, ArrayList<AttendanceRewardHistory>> result = new ConcurrentHashMap<>();
		if (StringUtil.isNullOrEmpty(str)) {
			return result;
		}
		
		String[] array = str.split(DIVID_REGEX);
		for (int i=0; i<array.length; i++) {
			String one = array[i];
			String[] oneDetail = one.split(FLAG_REGEX);
			AttendanceGroupType groupType = AttendanceGroupType.fromInt(Integer.parseInt(oneDetail[0].trim()));
			if (groupType == null) {
				continue;
			}
			
			ArrayList<AttendanceRewardHistory> list = result.get(groupType);
			if (list == null) {
				list = new ArrayList<AttendanceRewardHistory>();
				result.put(groupType, list);
			}
			
			if (!StringUtil.isNullOrEmpty(oneDetail[1])) {
				AttendanceRewardHistory completed = null;
				StringTokenizer st = new StringTokenizer(oneDetail[1].trim(), StringUtil.LineString);// 개행별로 나눈다
				while (st.hasMoreElements()) {
					String oneLineText = st.nextToken().replaceAll(StringUtil.EmptyOneString, StringUtil.EmptyString);// 공백 제거
					String[] detail = oneLineText.split(StringUtil.ColonString);// 인덱스 분리, 0:인덱스, 1:데스크아이디, 2:수량
					completed = new AttendanceRewardHistory(groupType, Integer.parseInt(detail[0]), Integer.parseInt(detail[1]), Integer.parseInt(detail[2]));
					list.add(completed);
				}
			}
			
		}
		return result;
	}
	
	String parseInsertRewardHistory(ConcurrentHashMap<AttendanceGroupType, ArrayList<AttendanceRewardHistory>> completedItems){
		if (completedItems == null || completedItems.isEmpty()) {
			return StringUtil.EmptyString;
		}
		
		StringBuilder sb = new StringBuilder();
		int cnt = 0;
		AttendanceGroupType key = null;
		ArrayList<AttendanceRewardHistory> value = null;
		for (Map.Entry<AttendanceGroupType, ArrayList<AttendanceRewardHistory>> entry : completedItems.entrySet()) {
			cnt++;
			key		= entry.getKey();
			value	= entry.getValue();
			sb.append(key.getGroupId()).append(StringUtil.LineString);
			sb.append(FLAG_REGEX).append(StringUtil.LineString);
			
			if (value != null && !value.isEmpty()) {
				AttendanceRewardHistory completed = null;
				for (int i=0; i<value.size(); i++) {
					completed = value.get(i);
					if (completed == null) {
						continue;
					}
					sb.append(completed.getIndex()).append(StringUtil.ColonString);
					sb.append(completed.getItemDescId()).append(StringUtil.ColonString);
					sb.append(completed.getItemCount()).append(StringUtil.LineString);
				}
			}
			
			if (cnt < completedItems.size()) {
				sb.append(DIVID_REGEX).append(StringUtil.LineString);
			}
		}
		return sb.toString();
	}
	
	void load(){
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM attendance_accounts");
			rs = pstm.executeQuery();
			AttendanceAccount attend = null;
			while(rs.next()){
				String account				= rs.getString("account");
				
				int dailyCount				= rs.getInt("dailyCount");
				boolean isCompleted			= Boolean.valueOf(rs.getString("isCompleted"));
				Timestamp resetDate			= rs.getTimestamp("resetDate");
				
				ConcurrentHashMap<AttendanceGroupType, byte[]> groupDatas	= parseLoadGroupData(rs.getBytes("groupData"));
				ConcurrentHashMap<AttendanceGroupType, Boolean> groupOpens	= parseLoadGroupOpen(rs.getBytes("groupOpen"));
				ConcurrentHashMap<AttendanceGroupType, HashMap<Integer, HashMap<Integer, ArrayList<AttendanceRandomItem>>>> randomItems	= parseLoadRandomItem(rs.getString("randomItems"));
				ConcurrentHashMap<AttendanceGroupType, ArrayList<AttendanceRewardHistory>> rewardHistory	= parseLoadRewardHistory(rs.getString("rewardHistory"));

				attend = new AttendanceAccount(account,
						dailyCount, isCompleted, resetDate, groupDatas, groupOpens,
						AttendanceTable.getGroupItems(), randomItems, rewardHistory);
				_info.put(attend.getAccount(), attend);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public void store(AttendanceAccount attend){
		if (attend == null) {
			return;
		}
		Connection con				= null;
		PreparedStatement pstm		= null;
		int idx						= 0;
		
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE attendance_accounts SET dailyCount=?, isCompleted=?, resetDate=?, groupData=?, groupOpen=?, randomItems=?, rewardHistory=? WHERE account=?");
			
			pstm.setInt(++idx, attend.getDailyCount());
			pstm.setString(++idx, String.valueOf(attend.isCompleted()));
			pstm.setTimestamp(++idx, attend.getResetDate());
			pstm.setBytes(++idx, parseInsertGroupData(attend.getGroupData()));
			pstm.setBytes(++idx, parseInsertGroupOpen(attend.getGroupOpens()));
			pstm.setString(++idx, parseInsertRandomItem(attend.getRandomItems()));
			pstm.setString(++idx, parseInsertRewardHistory(attend.getRewardHisoty()));
			pstm.setString(++idx, attend.getAccount());
			pstm.execute();
		} catch(SQLException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	private void insert(AttendanceAccount account){
		Connection con			= null;
		PreparedStatement pstm	= null;
		int idx					= 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO attendance_accounts SET account=?, dailyCount=?, isCompleted=?, resetDate=?, groupData=?, groupOpen=?, randomItems=?, rewardHistory=?");
			pstm.setString(++idx, account.getAccount());
			pstm.setInt(++idx, account.getDailyCount());
			pstm.setString(++idx, String.valueOf(account.isCompleted()));
			pstm.setTimestamp(++idx, account.getResetDate());
			pstm.setBytes(++idx, parseInsertGroupData(account.getGroupData()));
			pstm.setBytes(++idx, parseInsertGroupOpen(account.getGroupOpens()));
			pstm.setString(++idx, parseInsertRandomItem(account.getRandomItems()));
			pstm.setString(++idx, parseInsertRewardHistory(account.getRewardHisoty()));
			pstm.execute();
		} catch(SQLException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	public AttendanceAccount getAccountInfos(String name){
		if (!_info.containsKey(name)) {
			attendanceCreate(name);
		}
		return _info.get(name);
	}
	
	void attendanceCreate(String name){
		long currentTime = System.currentTimeMillis();
		
		ConcurrentHashMap<AttendanceGroupType, byte[]> groupDatas		= new ConcurrentHashMap<>();
		ConcurrentHashMap<AttendanceGroupType, Boolean> groupOpens		= new ConcurrentHashMap<>();
		ConcurrentHashMap<AttendanceGroupType, HashMap<Integer, HashMap<Integer, ArrayList<AttendanceRandomItem>>>> randomItems = new ConcurrentHashMap<>();
		ConcurrentHashMap<AttendanceGroupType, ArrayList<AttendanceRewardHistory>> completedItems = new ConcurrentHashMap<AttendanceGroupType, ArrayList<AttendanceRewardHistory>>();
		for (AttendanceGroupType type : AttendanceGroupType.getAllList()) {
			groupDatas.put(type, new byte[AttendanceTable.getGroupItemSize(type)]);
			if (type.isTab()) {
				groupOpens.put(type, false);
			}
			randomItems.put(type, new HashMap<>());
			completedItems.put(type, new ArrayList<>());
		}
		
		AttendanceAccount account = new AttendanceAccount(name, 
				0, false, new Timestamp(currentTime), groupDatas, groupOpens,
				AttendanceTable.getGroupItems(), randomItems, completedItems);
		insert(account);
		_info.put(account.getAccount(), account);
	}
	
	public static void reload(){
		AttendanceAccountTable oldInstance = _instance;
		_instance = new AttendanceAccountTable();
		oldInstance._info.clear();
		oldInstance._info = null;
		oldInstance = null;
	}
}

