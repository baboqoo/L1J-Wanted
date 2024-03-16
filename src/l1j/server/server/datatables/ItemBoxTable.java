package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.util.FastMap;
import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.construct.L1CharacterInfo.L1Class;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

/**
 * 상자성 아이템 정보(트레져박스 확장)
 * @author LinOffice
 */
public class ItemBoxTable {
	private static ItemBoxTable _instance;
	public static ItemBoxTable getInstance(){
		if (_instance == null) {
			_instance = new ItemBoxTable();
		}
		return _instance;
	}
	private ItemBoxTable(){
		load();
	}
	
	private static final FastMap<Integer, ItemBox> DATA = new FastMap<>();
	
	public static boolean isBoxItem(int boxId){
		return DATA.containsKey(boxId);
	}
	
	public static ItemBox getItemBox(int boxId){
		return DATA.get(boxId);
	}
	
	public class ItemBox {
		private FastMap<Integer, FastTable<ItemBoxData>> classItemList = new FastMap<>();
		private FastTable<Integer> limitMaps = new FastTable<>();
		private boolean questBox;
		private int effectId;
		private int[] validateItems;
		private boolean delete;
		public FastMap<Integer, FastTable<ItemBoxData>> getClassItemList() {
			return classItemList;
		}
		public FastTable<Integer> getLimitMaps(){
			return limitMaps;
		}
		public boolean isQuestBox(){
			return questBox;
		}
		public int getEffectId(){
			return effectId;
		}
		public int[] getValidateItems(){
			return validateItems;
		}
		public boolean isDelete(){
			return delete;
		}
	}
	
	public class ItemBoxData{
		private int itemId;
		private int count;
		private int enchant;
		private int bless;
		private int attr;
		private boolean idendi;
		private int limitTime;
		private int chance;
		public ItemBoxData(ResultSet rs)throws SQLException {
			this.itemId		= rs.getInt("itemId");
			this.count		= rs.getInt("count");
			this.enchant	= rs.getInt("enchant");
			this.bless		= rs.getInt("bless");
			this.attr		= rs.getInt("attr");
			this.idendi		= Boolean.parseBoolean(rs.getString("identi"));
			this.limitTime	= rs.getInt("limitTime");
			this.chance		= rs.getInt("chance");
		}
		public int getItemId() {
			return itemId;
		}
		public int getCount() {
			return count;
		}
		public int getEnchant() {
			return enchant;
		}
		public int getBless() {
			return bless;
		}
		public int getAttr() {
			return attr;
		}
		public boolean isIdendi() {
			return idendi;
		}
		public int getLimitTime() {
			return limitTime;
		}
		public int getChance(){
			return chance;
		}
	}
	
	private void load(){
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		ItemBox box				= null;
		try{
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM item_box ORDER BY boxId ASC, classType ASC");
			rs		= pstm.executeQuery();
			while(rs.next()){
				int boxId = rs.getInt("boxId");
				box = DATA.get(boxId);
				if (box == null) {
					box = new ItemBox();
					parseLimitMap(rs.getString("limitMaps"), box);
					box.questBox		= Boolean.parseBoolean(rs.getString("questBox"));
					box.effectId		= rs.getInt("effectId");
					box.validateItems	= parseIntArray(rs.getString("validateItems"));
					box.delete			= Boolean.parseBoolean(rs.getString("boxDelete"));
					DATA.put(boxId, box);
				}
				int classType = L1Class.getType(rs.getString("classType"));
				FastTable<ItemBoxData> classList = box.classItemList.get(classType);
				if (classList == null) {
					classList = new FastTable<>();
					box.classItemList.put(classType, classList);
				}
				classList.add(new ItemBoxData(rs));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	private void parseLimitMap(String str, ItemBox box){
		if (StringUtil.isNullOrEmpty(str)) {
			return;
		}
		String[] array = str.split(StringUtil.CommaString);
		for (String map : array) {
			box.limitMaps.add(Integer.parseInt(map.trim()));
		}
	}
	
	private int[] parseIntArray(String str){
		if (StringUtil.isNullOrEmpty(str)) {
			return null;
		}
		String[] array = str.split(StringUtil.CommaString);
		int[] result = new int[array.length];
		for (int i=0; i<result.length; i++) {
			result[i] = Integer.parseInt(array[i].trim());
		}
		return result;
	}
	
	public void reload(){
		for (ItemBox box : DATA.values()) {
			if (box.classItemList != null) {
				box.classItemList.clear();
				box.classItemList = null;
			}
		}
		DATA.clear();
		load();
	}
}

