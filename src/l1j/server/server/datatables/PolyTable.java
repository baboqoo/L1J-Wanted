package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.utils.SQLUtil;

public class PolyTable {
	private static Logger _log = Logger.getLogger(PolyTable.class.getName());

	private static PolyTable _instance;

	private final HashMap<String, L1PolyMorph>			_polymorphs		= new HashMap<String, L1PolyMorph>();
	private final HashMap<Integer, L1PolyMorph>			_polyIdIndex	= new HashMap<Integer, L1PolyMorph>();
	private final HashMap<Integer, L1PolyMorph>			_polyspeed		= new HashMap<Integer, L1PolyMorph>();
	
	public static final List<Integer> SPEAR_LANCER_POLY_ADD_LIST	= new ArrayList<Integer>();// 창기사 원거리폼일때 따로 추가할 변신
	public static final List<Integer> PVP_DAMAGE_POLY_ADD_LIST		= new ArrayList<Integer>();// PVP대미지 추가되는 변신
	
	public static enum WeaponeType {
		BOW, SPEAR, BOTH;
		public static WeaponeType fromString(String str){
			switch(str){
			case "bow":		return BOW;
			case "spear":	return SPEAR;
			default:		return BOTH;
			}
		}
	}
	
	public static class PolyItemData {
		private int _itemId;
		private int _polyId;
		private int _duration;
		private int _type;
		private boolean _delete;
		public PolyItemData(ResultSet rs) throws SQLException {
			this._itemId	= rs.getInt("itemId");
			this._polyId	= rs.getInt("polyId");
			this._duration	= rs.getInt("duration");
			this._type		= rs.getString("type").equals("domination") ? L1PolyMorph.MORPH_BY_DOMINATION : L1PolyMorph.MORPH_BY_ITEMMAGIC;
			this._delete	= Boolean.parseBoolean(rs.getString("delete"));
		}
		public int getItemId() {
			return _itemId;
		}
		public int getPolyId() {
			return _polyId;
		}
		public int getDuration() {
			return _duration;
		}
		public int getType() {
			return _type;
		}
		public boolean isDelete() {
			return _delete;
		}
	}
	
	private static final HashMap<WeaponeType, ArrayList<Integer>> _polyweapon	= new HashMap<WeaponeType, ArrayList<Integer>>(3);// bow, spear
	private static final HashMap<Integer, PolyItemData> _polyItems				= new HashMap<Integer, PolyItemData>();
	
	public static boolean isPolyWeapon(WeaponeType type, int poly){
		if (_polyweapon.get(WeaponeType.BOTH).contains(poly)) {
			return true;
		}
		return _polyweapon.get(type).contains(poly);
	}

	public static PolyTable getInstance() {
		if (_instance == null) {
			_instance = new PolyTable();
		}
		return _instance;
	}
	
	public static boolean isPolyItem(int itemId){
		return _polyItems.containsKey(itemId);
	}
	
	public static PolyItemData getPolyItem(int itemId){
		return _polyItems.get(itemId);
	}

	private PolyTable() {
		loadPolymorphs();
	}
	
	private void loadPolymorphs() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			
			pstm	= con.prepareStatement("SELECT * FROM polymorphs");
			rs		= pstm.executeQuery();
			fillPolyTable(rs);
			SQLUtil.close(rs, pstm);
			
			pstm	= con.prepareStatement("SELECT * FROM polyweapon");
			rs		= pstm.executeQuery();
			while(rs.next()){
				int polyId				= rs.getInt("polyId");
				WeaponeType type		= WeaponeType.fromString(rs.getString("weapon"));
				ArrayList<Integer> list = _polyweapon.get(type);
				if (list == null) {
					list = new ArrayList<Integer>();
					_polyweapon.put(type, list);
				}
				list.add(polyId);
			}
			SQLUtil.close(rs, pstm);
			
			pstm	= con.prepareStatement("SELECT * FROM polyitems ORDER BY itemId ASC");
			rs		= pstm.executeQuery();
			while(rs.next()){
				PolyItemData item = new PolyItemData(rs);
				if (ItemTable.getInstance().getTemplate(item._itemId) == null) {
					System.out.println("PolyItem not found ItemId -> " + item._itemId);
					continue;
				}
				_polyItems.put(item._itemId, item);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "error while creating polymorph table", e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	private void fillPolyTable(ResultSet rs) throws SQLException {
		L1PolyMorph poly = null;
		while (rs.next()) {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			int polyId = rs.getInt("polyid");
			int minLevel = rs.getInt("minlevel");
			int weaponEquipFlg = rs.getInt("weaponequip");
			int armorEquipFlg = rs.getInt("armorequip");
			boolean canUseSkill = rs.getBoolean("isSkillUse");
			int causeFlg = rs.getInt("cause");
			boolean bonusPVP = Boolean.parseBoolean(rs.getString("bonusPVP"));
			boolean formLongEnable = Boolean.parseBoolean(rs.getString("formLongEnable"));
			poly = new L1PolyMorph(id, name, polyId, minLevel, weaponEquipFlg, armorEquipFlg, canUseSkill, causeFlg);
			if (name.startsWith("lv") || name.startsWith("15 pc") || name.startsWith("knight of")
					|| name.startsWith("rangking ") || name.startsWith("basic ")
					|| polyId == 15154 || polyId == 14925 || polyId == 14923 || polyId == 14924) {
				_polyspeed.put(Integer.valueOf(polyId), poly);
			}
			_polymorphs.put(name, poly);
			_polyIdIndex.put(polyId, poly);
			
			if (bonusPVP) {
				PVP_DAMAGE_POLY_ADD_LIST.add(polyId);
			}
			if (formLongEnable) {
				SPEAR_LANCER_POLY_ADD_LIST.add(polyId);
			}
		}

		//_log.config("변신 리스트 " + _polymorphs.size() + "건 로드");
		_log.config("Transformation list " + _polymorphs.size() + " entries loaded");
	}

	public L1PolyMorph getTemplate(String name) {
		return _polymorphs.get(name);
	}

	public L1PolyMorph getTemplate(int polyId) {
		return _polyIdIndex.get(polyId);
	}
	
	private boolean _polyEvent;
	
	public boolean getSpeed(int polyId) {
		return _polyspeed.get(Integer.valueOf(polyId)) != null;
	}

	public boolean isPolyEvent() {
		return _polyEvent;
	}

	public void setPolyEvent(boolean i) {
		_polyEvent = i;
	}

	public static void reload() {
		for (ArrayList<Integer> list : _polyweapon.values()) {
			list.clear();
		}
		_polyItems.clear();
		SPEAR_LANCER_POLY_ADD_LIST.clear();
		PVP_DAMAGE_POLY_ADD_LIST.clear();
		
		PolyTable oldInstance = _instance;
		_instance = new PolyTable();
		oldInstance._polymorphs.clear();
		oldInstance._polyIdIndex.clear();
	    oldInstance._polyspeed.clear();
	}
}

