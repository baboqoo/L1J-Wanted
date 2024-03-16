package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.templates.L1Catalyst;
import l1j.server.server.templates.L1CatalystCustom;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.SQLUtil;

public class CatalystTable {
	private static Logger _log = Logger.getLogger(CatalystTable.class.getName());
	private static final HashMap<Integer, HashMap<Integer, L1Catalyst>> DATA = new HashMap<>();
	private static final HashMap<Integer, HashMap<Integer, HashMap<Integer, L1CatalystCustom>>> CUSTOM_DATA = new HashMap<>();
	private static CatalystTable _instance;
	public static CatalystTable getInstance() {
		if (_instance == null) {
			_instance = new CatalystTable();
		}
		return _instance;
	}
	
	/**
	 * 촉매 DB데이터 조사(catalyst-common.bin 의존)
	 * @param name_id
	 * @param input_id
	 * @return L1Catalyst
	 */
	public static L1Catalyst getCatalyst(int name_id, int input_id) {
		HashMap<Integer, L1Catalyst> map = DATA.get(name_id);
		return map == null ? null : map.get(input_id);
	}
	
	/**
	 * 촉매 여부
	 * @param itemId
	 * @return boolean
	 */
	public static boolean isCatalystCustom(int itemId) {
		return CUSTOM_DATA.containsKey(itemId);
	}
	
	/**
	 * 촉매 DB데이터 조사(임의 지정)
	 * @param itemId
	 * @param inputItemId
	 * @param inputEnchant
	 * @return L1CatalystCustom
	 */
	public static L1CatalystCustom getCatalystCustom(int itemId, int inputItemId, int inputEnchant) {
		HashMap<Integer, HashMap<Integer, L1CatalystCustom>> input_map = CUSTOM_DATA.get(itemId);
		if (input_map == null) {
			return null;
		}
		HashMap<Integer, L1CatalystCustom> enchant_map = input_map.get(inputItemId);
		return enchant_map == null ? null : enchant_map.get(inputEnchant);
	}

	private CatalystTable() {
		load();
	}

	private void load() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		L1Catalyst obj			= null;
		L1CatalystCustom custom = null;
		ItemTable it			= ItemTable.getInstance();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			
			pstm = con.prepareStatement("SELECT * FROM catalyst");
			rs = pstm.executeQuery();
			while (rs.next()) {
				obj = new L1Catalyst(rs);
				L1Item temp	= it.findItemByNameId(obj.get_name_id());
				if (temp == null) {
					System.out.println(String.format("[CatalystTable] ITEM_EMPTY : NAME_ID(%d)", obj.get_name_id()));
					continue;
				}
				
				HashMap<Integer, L1Catalyst> map = DATA.get(obj.get_name_id());
				if (map == null) {
					map = new HashMap<>();
					DATA.put(obj.get_name_id(), map);
				}
				map.put(obj.get_input(), obj);
			}
			SQLUtil.close(rs, pstm);
			
			pstm = con.prepareStatement("SELECT * FROM catalyst_custom");
			rs = pstm.executeQuery();
			while (rs.next()) {
				custom = new L1CatalystCustom(rs);
				L1Item temp	= it.getTemplate(custom.getItemId());
				if (temp == null) {
					System.out.println(String.format("[CatalystTable] CUSTOM_ITEM_EMPTY : ITEM_ID(%d)", custom.getItemId()));
					continue;
				}
				temp	= it.getTemplate(custom.getInputItemId());
				if (temp == null) {
					System.out.println(String.format("[CatalystTable] CUSTOM_INPUT_ITEM_EMPTY : ITEM_ID(%d)", custom.getInputItemId()));
					continue;
				}
				temp	= it.getTemplate(custom.getOutputItemId());
				if (temp == null) {
					System.out.println(String.format("[CatalystTable] CUSTOM_OUTPUT_ITEM_EMPTY : ITEM_ID(%d)", custom.getOutputItemId()));
					continue;
				}
				
				HashMap<Integer, HashMap<Integer, L1CatalystCustom>> input_map = CUSTOM_DATA.get(custom.getItemId());
				if (input_map == null) {
					input_map = new HashMap<>();
					CUSTOM_DATA.put(custom.getItemId(), input_map);
				}
				HashMap<Integer, L1CatalystCustom> enchant_map = input_map.get(custom.getInputItemId());
				if (enchant_map == null) {
					enchant_map = new HashMap<>();
					input_map.put(custom.getInputItemId(), enchant_map);
				}
				enchant_map.put(custom.getInputEnchant(), custom);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public static void reload() {
		DATA.clear();
		CUSTOM_DATA.clear();
		_instance.load();
	}
	
}

