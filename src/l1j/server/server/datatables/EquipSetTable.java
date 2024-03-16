package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1EquipmentSet;
import l1j.server.server.model.L1EquipmentSet.EquipSet;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.equip.S_EquipmentSet;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

/**
 * 아이템 세트
 * @author LinOffice
 */
public class EquipSetTable {
	private static Logger _log = Logger.getLogger(EquipSetTable.class.getName());
	
	private static class newInstance {
		public static final EquipSetTable INSTANCE = new EquipSetTable();
	}
	public static EquipSetTable getInstance() {
		return newInstance.INSTANCE;
	}
	private EquipSetTable() {
	}
	
	private static final String LOAD_QUERY		= "SELECT * FROM character_equipset WHERE charId = ? LIMIT 1";
	private static final String DELETE_QUERY	= "DELETE FROM character_equipset WHERE charId = ?";
	private static final String UPDATE_CURRENT_SET_QUERY = "UPDATE character_equipset SET current_set = ? WHERE charId = ?";
	private static final String UPDATE_ALL_QUERY = "UPDATE character_equipset SET current_set = ?, "
			+ "slot1_item = ?, slot2_item = ?, slot3_item = ?, slot4_item = ?, "
			+ "slot1_name = ?, slot2_name = ?, slot3_name = ?, slot4_name = ?, "
			+ "slot1_color = ?, slot2_color = ?, slot3_color = ?, slot4_color = ? "
			+ "WHERE charId = ?";
	
	private static final String[] UPSERT_ITEM_QUERYS = {
		"INSERT INTO character_equipset SET charId = ?, slot1_item = ? ON DUPLICATE KEY UPDATE slot1_item = ?",
		"INSERT INTO character_equipset SET charId = ?, slot2_item = ? ON DUPLICATE KEY UPDATE slot2_item = ?",
		"INSERT INTO character_equipset SET charId = ?, slot3_item = ? ON DUPLICATE KEY UPDATE slot3_item = ?",
		"INSERT INTO character_equipset SET charId = ?, slot4_item = ? ON DUPLICATE KEY UPDATE slot4_item = ?"
	};
	
	private static final String[] UPSERT_DESC_QUERYS = {
		"INSERT INTO character_equipset SET charId = ?, slot1_name = ?, slot1_color = ? ON DUPLICATE KEY UPDATE slot1_name = ?, slot1_color = ?",
		"INSERT INTO character_equipset SET charId = ?, slot2_name = ?, slot2_color = ? ON DUPLICATE KEY UPDATE slot2_name = ?, slot2_color = ?",
		"INSERT INTO character_equipset SET charId = ?, slot3_name = ?, slot3_color = ? ON DUPLICATE KEY UPDATE slot3_name = ?, slot3_color = ?",
		"INSERT INTO character_equipset SET charId = ?, slot4_name = ?, slot4_color = ? ON DUPLICATE KEY UPDATE slot4_name = ?, slot4_color = ?",
	};
	

	/**
	 * 아이템 저장
	 * @param charObjId
	 * @param equip_set
	 * @param equip
	 */
	public void upsertItems(int charObjId, int equip_set, EquipSet equip) {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement(UPSERT_ITEM_QUERYS[equip_set]);
			int index = 0;
			String slot_info = equip.toItemString();
			pstm.setInt(++index, charObjId);
			pstm.setString(++index, slot_info);
			pstm.setString(++index, slot_info);
			pstm.executeUpdate();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	/**
	 * 이름 and 색깔 저장
	 * @param charObjId
	 * @param equip_set
	 * @param equip
	 * @param slot_name
	 * @param slot_color
	 */
	public void upsertDesc(int charObjId, int equip_set, EquipSet equip, String slot_name, int slot_color) {
		equip.setSlotName(slot_name);
		equip.setSlotColor(slot_color);
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement(UPSERT_DESC_QUERYS[equip_set]);
			int index = 0;
			pstm.setInt(++index, charObjId);
			pstm.setString(++index, slot_name);
			pstm.setInt(++index, slot_color);
			pstm.setString(++index, slot_name);
			pstm.setInt(++index, slot_color);
			pstm.executeUpdate();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	/**
	 * 현재 세트번호 저장
	 * @param charObjId
	 * @param current_set
	 */
	public void updateCurrentSet(int charObjId, int current_set) {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement(UPDATE_CURRENT_SET_QUERY);
			pstm.setInt(1, current_set);
			pstm.setInt(2, charObjId);
			pstm.executeUpdate();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	/**
	 * 전체 저장
	 * @param pc
	 * @param set
	 */
	public void updateAll(L1PcInstance pc, L1EquipmentSet set){
		EquipSet equip1			= set.getEquipSet(0);
		EquipSet equip2			= set.getEquipSet(1);
		EquipSet equip3			= set.getEquipSet(2);
		EquipSet equip4			= set.getEquipSet(3);
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement(UPDATE_ALL_QUERY);
			int index = 0;
			pstm.setInt(++index, set.getCurrentSet());
			pstm.setString(++index, equip1.toItemString());
			pstm.setString(++index, equip2.toItemString());
			pstm.setString(++index, equip3.toItemString());
			pstm.setString(++index, equip4.toItemString());
			pstm.setString(++index, equip1.getSlotName());
			pstm.setString(++index, equip2.getSlotName());
			pstm.setString(++index, equip3.getSlotName());
			pstm.setString(++index, equip4.getSlotName());
			pstm.setInt(++index, equip1.getSlotColor());
			pstm.setInt(++index, equip2.getSlotColor());
			pstm.setInt(++index, equip3.getSlotColor());
			pstm.setInt(++index, equip4.getSlotColor());
			pstm.setInt(++index, pc.getId());
			pstm.executeUpdate();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	/**
	 * 데이터 제거
	 * @param charId
	 */
	public void delete(int charId){
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement(DELETE_QUERY);
			pstm.setInt(1, charId);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	/**
	 * 로그인시 세트 정보를 케릭터에 로드한다.
	 * @param pc
	 */
	public void load(L1PcInstance pc) {
		L1EquipmentSet set		= pc.getEquipmentSet();
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement(LOAD_QUERY);
			pstm.setInt(1, pc.getId());
			rs		= pstm.executeQuery();
			if (rs.next()){
				set.setCurrentSet(rs.getInt("current_set"));
				parseSlotSet(pc, set.getEquipSet(0), rs.getString("slot1_item"), rs.getString("slot1_name"), rs.getInt("slot1_color"));
				parseSlotSet(pc, set.getEquipSet(1), rs.getString("slot2_item"), rs.getString("slot2_name"), rs.getInt("slot2_color"));
				parseSlotSet(pc, set.getEquipSet(2), rs.getString("slot3_item"), rs.getString("slot3_name"), rs.getInt("slot3_color"));
				parseSlotSet(pc, set.getEquipSet(3), rs.getString("slot4_item"), rs.getString("slot4_name"), rs.getInt("slot4_color"));
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		pc.sendPackets(new S_EquipmentSet(set), true);
	}
	
	/**
	 * 데이터 파싱
	 * @param pc
	 * @param slot
	 * @param SlotItemString
	 * @param slotName
	 * @param slotColor
	 */
	void parseSlotSet(L1PcInstance pc, EquipSet equip, String SlotItemString, String slotName, int slotColor) {
		equip.setSlotName(slotName);
		equip.setSlotColor(slotColor);
		if (!StringUtil.isNullOrEmpty(SlotItemString)) {
			ArrayList<L1ItemInstance> slot_items = equip.getSlotItems();
			StringTokenizer token	= new StringTokenizer(SlotItemString, StringUtil.LineString);
			L1ItemInstance item		= null;
			int item_obj_id			= 0;
			while (token.hasMoreElements()) {
				item_obj_id	= Integer.parseInt(token.nextToken().trim());
				item		= pc.getInventory().getItem(item_obj_id);
				if (item != null && !slot_items.contains(item)) {
					slot_items.add(item);// 적재
				}
			}
		}
	}
	
}
