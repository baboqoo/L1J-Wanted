package l1j.server.GameSystem.attendance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.attendance.bean.AttendanceAccount;
import l1j.server.GameSystem.attendance.bean.AttendanceItem;
import l1j.server.GameSystem.attendance.bean.AttendanceRandomItem;
import l1j.server.common.data.AttendanceBonusType;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.SQLUtil;

public class AttendanceTable {
	private static AttendanceTable _instance;
	public static AttendanceTable getInstance() {
		if (_instance == null) {
			_instance = new AttendanceTable();
		}
		return _instance;
	}
	
	// key:groupType, value:(key:index, value:item)
	private static final ConcurrentHashMap<AttendanceGroupType, HashMap<Integer, AttendanceItem>> GROUP_ITEM_DATA = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<AttendanceGroupType, Integer> GROUP_ITEM_SIZE_DATA	= new ConcurrentHashMap<>();
	private static final HashMap<AttendanceGroupType, ArrayList<AttendanceRandomItem>> RANDOM	= new HashMap<>();
	
	public static ConcurrentHashMap<AttendanceGroupType, HashMap<Integer, AttendanceItem>> getGroupItems() {
		return GROUP_ITEM_DATA;
	}
	
	public static int getGroupItemSize(AttendanceGroupType type) {
		if (!GROUP_ITEM_SIZE_DATA.containsKey(type)) {
			return 0;
		}
		return GROUP_ITEM_SIZE_DATA.get(type);
	}
	
	public static ArrayList<AttendanceRandomItem> getRandomItemList(AttendanceGroupType type){
		return RANDOM.get(type);
	}
	
	public static AttendanceRandomItem getRandomItem(AttendanceGroupType type, int index){
		if (!RANDOM.containsKey(type)) {
			return null;
		}
		AttendanceRandomItem temp = null;
		for (AttendanceRandomItem check : RANDOM.get(type)) {
			if (check.getIndex() == index) {
				temp = check;
				break;
			}
		}
		return temp;
	}
	
	/**
	 * 랜덤 아이템 5개 선별
	 * 등급이 전부 다르게 설정된다.
	 * @param groupType
	 * @return ArrayList<AttendanceRandomItem>
	 */
	public static ArrayList<AttendanceRandomItem> getRandomItemListSelector(AttendanceGroupType groupType){
		ArrayList<AttendanceRandomItem> resultList	= new ArrayList<AttendanceRandomItem>(5);
		ArrayList<AttendanceRandomItem> randomList	= getRandomItemList(groupType);// 그룹의 랜덤 아이템 리스트
		AttendanceRandomItem choice;
		if (randomList != null && !randomList.isEmpty()) {
			for (int i=1; i<=5; i++) {
				choice = randomList.get(CommonUtil.random(randomList.size()));// 무작위로 하나를 선택
				if (resultList.contains(choice)) {// 동일한 물품이 선택될시 다시 선택
					i--;
					continue;
				}
				boolean levelCheck = false;
				for (AttendanceRandomItem send : resultList) {
					if (send.getLevel() == choice.getLevel()) {// 등급 검사
						levelCheck = true;
						break;
					}
				}
				if (levelCheck) {// 같은 등급의 아이템 선택될시 다시 선택
					i--;
					continue;
				}
				resultList.add(choice);
			}
		}
		return resultList;
	}
	
	private AttendanceTable() {
		load();
	}

	void load() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM attendance_item ORDER BY groupType");
			rs		= pstm.executeQuery();
			while(rs.next()){
				int group_type = rs.getInt("groupType");
				AttendanceGroupType groupType = AttendanceGroupType.fromInt(group_type);
				if (groupType == null) {
					System.out.println(String.format("[AttendanceTable] GROUP_TYPE_EMPTY : TYPE(%d)", group_type));
					continue;
				}
				HashMap<Integer, AttendanceItem> map = GROUP_ITEM_DATA.get(groupType);
				if (map == null) {
					map = new HashMap<Integer, AttendanceItem>();
					GROUP_ITEM_DATA.put(groupType, map);
				}
				
				int itemId = rs.getInt("item_id");
				L1Item temp = ItemTable.getInstance().getTemplate(itemId);
				if (temp == null) {
					System.out.println(String.format("[AttendanceTable] ITEM_TEMPLATE_EMPTY : ITEM_ID(%d)", itemId));
					continue;
				}
				
				String bonusType = rs.getString("bonus_type");
				bonusType = bonusType.substring(bonusType.indexOf("(") + 1, bonusType.indexOf(")"));
				
				int index = rs.getInt("index");
				
				map.put(index, 
						new AttendanceItem(index, itemId, rs.getInt("count"), rs.getInt("enchant"), 
						Boolean.valueOf(rs.getString("broadcast")), AttendanceBonusType.fromInt(Integer.parseInt(bonusType))));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		
		loadRandom();
		for (Map.Entry<AttendanceGroupType, HashMap<Integer, AttendanceItem>> entry : GROUP_ITEM_DATA.entrySet()) {
			GROUP_ITEM_SIZE_DATA.put(entry.getKey(), entry.getValue().size());
		}
	}
	
	void loadRandom() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM attendance_item_random ORDER BY groupType");
			rs		= pstm.executeQuery();
			ArrayList<AttendanceRandomItem> items;
			while(rs.next()){
				AttendanceGroupType groupType = AttendanceGroupType.fromInt(rs.getInt("groupType"));
				if (groupType == null) {
					System.out.println(String.format("[AttendanceTable] GROUP_TYPE_EMPTY : TYPE(%d)", groupType));
					continue;
				}
				items = RANDOM.get(groupType);
				if (items == null) {
					items = new ArrayList<AttendanceRandomItem>();
					RANDOM.put(groupType, items);
				}
				items.add(new AttendanceRandomItem(
						groupType, 
						rs.getInt("index"), rs.getInt("itemId"), rs.getInt("count"), 
						Boolean.parseBoolean(rs.getString("broadcast")), rs.getInt("level")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public void reload() {
		release();
		load();
		
		AttendanceAccount attendAccount = null;
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc == null || pc.getAccount() == null || pc.isPrivateShop() || pc.isAutoClanjoin() || pc.noPlayerCK) {
				continue;
			}
			attendAccount = pc.getAccount().getAttendance();
			if (attendAccount == null) {
				continue;
			}
			attendAccount.sendPacket(pc);
		}
	}
	
	private void release(){
		for (HashMap<Integer, AttendanceItem> map : GROUP_ITEM_DATA.values()) {
			map.clear();
		}
		for (ArrayList<AttendanceRandomItem> list : RANDOM.values()) {
			list.clear();
		}
		RANDOM.clear();
		GROUP_ITEM_DATA.clear();
		GROUP_ITEM_SIZE_DATA.clear();
	}
}
