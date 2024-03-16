package l1j.server.server.model.item.collection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.util.FastMap;
import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class L1CollectionLoader {
	private static L1CollectionLoader _instance;
	public static L1CollectionLoader getInstance(){
		if (_instance == null) {
			_instance = new L1CollectionLoader();
		}
		return _instance;
	}
	
	private static final FastMap<Integer, L1CollectionModel> DATA	= new FastMap<Integer, L1CollectionModel>();
	private static final FastTable<Integer> TYPES					= new FastTable<Integer>();
	
	public static L1CollectionModel getCollection(int itemId){
		return DATA.get(itemId);
	}
	
	public static FastTable<Integer> getTypes(){
		return TYPES;
	}
	
	private L1CollectionLoader() {
		load();
	}
	
	private void load(){
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		L1CollectionModel model	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT A.itemId AS itemId, A.type AS type, (SELECT GROUP_CONCAT(B.itemId SEPARATOR ',') FROM item_collection B WHERE 1=1 AND B.type = A.type AND B.itemId <> A.itemId) AS sames FROM item_collection A ORDER BY A.type ASC");
			rs		= pstm.executeQuery();
			while(rs.next()){
				model = new L1CollectionModel(rs.getInt("type"), rs.getInt("itemId"), getParseArray(rs.getString("sames")));
				DATA.put(model.getItemId(), model);
				if (!TYPES.contains(model.getType())) {
					TYPES.add(model.getType());
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	private int[] getParseArray(String str){
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
		TYPES.clear();
		DATA.clear();
		load();
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc == null || pc.getNetConnection() == null) {
				continue;
			}
			pc.getInventory().getCollection().reload();
		}
	}
}

