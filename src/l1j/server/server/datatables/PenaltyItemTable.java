package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

/**
 * 패널티 보호 아이템 정보
 * @author LinOffice
 */
public class PenaltyItemTable {
	private static Logger _log = Logger.getLogger(PenaltyItemTable.class.getName());
	
	private static PenaltyItemTable _instance;
	public static PenaltyItemTable getInstance() {
		if (_instance == null) {
			_instance = new PenaltyItemTable();
		}
		return _instance;
	}

	private PenaltyItemTable() {
		load();
	}
	
	public static enum ProtectType {
		EQUIP, HAVE
	}
	
	public class ProtectItemData {
		public int _itemId;
		public String _name;
		public ProtectType _type;
		public boolean _itemPanalty;
		public boolean _expPanalty;
		public int _dropItemId;
		public int _msgId;
		public java.util.LinkedList<Integer> _mapIds;
		public boolean _remove;
		public ProtectItemData(int _itemId, String _name, ProtectType _type,
				boolean _itemPanalty, boolean _expPanalty, int _dropItemId,
				int msgId, java.util.LinkedList<Integer> _mapIds, boolean _remove) {
			this._itemId		= _itemId;
			this._name			= _name;
			this._type			= _type;
			this._itemPanalty	= _itemPanalty;
			this._expPanalty	= _expPanalty;
			this._dropItemId	= _dropItemId;
			this._msgId			= msgId;
			this._mapIds		= _mapIds;
			this._remove		= _remove;
		}
	}
	
	private static final java.util.LinkedList<ProtectItemData> PROTECT_DATA	= new java.util.LinkedList<ProtectItemData>();
	private static final java.util.LinkedList<Integer> PASS_DATA			= new java.util.LinkedList<Integer>();
	
	public static java.util.LinkedList<ProtectItemData> getData(){
		return PROTECT_DATA;
	}
	
	public static boolean isPassItem(int itemId){
		return PASS_DATA.contains(itemId);
	}

	private void load() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		ProtectItemData data = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT itemId, name, type, itemPanalty, expPanalty, dropItemId, msgId, mapIds, remove FROM penalty_protect_item ORDER BY itemId ASC");
			rs = pstm.executeQuery();
			while (rs.next()) {
				int itemId = rs.getInt("itemId");
				String name = rs.getString("name");
				ProtectType type = rs.getString("type").equals("equip") ? ProtectType.EQUIP : ProtectType.HAVE;
				boolean itemPanalty = Boolean.valueOf(rs.getString("itemPanalty"));
				boolean expPanalty = Boolean.valueOf(rs.getString("expPanalty"));
				int dropItemId = rs.getInt("dropItemId");
				int msgId = rs.getInt("msgId");
				String mapIds = rs.getString("mapIds");
				java.util.LinkedList<Integer> mapIdList = new java.util.LinkedList<Integer>();
				if (!StringUtil.isNullOrEmpty(mapIds)) {
					String[] mapArray = mapIds.split(StringUtil.CommaString);
					for (String mapId : mapArray) {
						mapIdList.add(Integer.parseInt(mapId));
					}
				}
				boolean remove = Boolean.valueOf(rs.getString("remove"));
				data = new ProtectItemData(itemId, name, type, itemPanalty, expPanalty, dropItemId, msgId, mapIdList, remove);
				PROTECT_DATA.add(data);
			}
			SQLUtil.close(rs, pstm);
			
			pstm = con.prepareStatement("SELECT itemId FROM penalty_pass_item");
			rs = pstm.executeQuery();
			while(rs.next()){
				PASS_DATA.add(rs.getInt("itemId"));
			}
			
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "PanaltyProtectItemTable[]Error", e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, "PanaltyProtectItemTable[]Error", e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public void reload() {
		PROTECT_DATA.clear();
		PASS_DATA.clear();
		load();
	}
}

