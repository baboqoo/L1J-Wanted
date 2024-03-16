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

/**
 * 스킬 용해제 정보
 * @author LinOffice
 */
public class SpellMeltTable {
	private static SpellMeltTable _instance;
	public static SpellMeltTable getInstance(){
		if (_instance == null) {
			_instance = new SpellMeltTable();
		}
		return _instance;
	}
	
	public class MeltData {
		public int skillId, passiveId, classType, skillItemId, meltItemId;
		public String skillName;
		
		public MeltData(ResultSet rs) throws SQLException {
			this.skillId		= rs.getInt("skillId");
			this.skillName		= rs.getString("skillName");
			this.passiveId		= rs.getInt("passiveId");
			this.classType		= L1Class.getType(rs.getString("classType"));
			this.skillItemId	= rs.getInt("skillItemId");
			this.meltItemId		= rs.getInt("meltItemId");
		}
	}
	
	private static final FastMap<Integer, MeltData> ACTIVE_DATA			= new FastMap<Integer, SpellMeltTable.MeltData>();
	private static final FastMap<Integer, MeltData> PASSIVE_DATA		= new FastMap<Integer, SpellMeltTable.MeltData>();
	private static final FastMap<Integer, FastMap<Integer, FastTable<Integer>>> CLASS_DATA	= new FastMap<Integer, FastMap<Integer,FastTable<Integer>>>();
	
	public static MeltData getActiveData(int skillId){
		return ACTIVE_DATA.containsKey(skillId) ? ACTIVE_DATA.get(skillId) : null;
	}
	
	public static MeltData getPassiveData(int passiveId){
		return PASSIVE_DATA.containsKey(passiveId) ? PASSIVE_DATA.get(passiveId) : null;
	}
	
	public static boolean isSpellMeltItem(int itemId){
		return CLASS_DATA.containsKey(itemId);
	}
	
	public static FastTable<Integer> getClassSkillItemList(int itemId, int classType){
		FastMap<Integer, FastTable<Integer>> meltItemData = CLASS_DATA.get(itemId);
		return meltItemData == null ? null : meltItemData.containsKey(classType) ? meltItemData.get(classType) : null;
	}
	
	private SpellMeltTable(){
		load();
	}
	
	private void load(){
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		MeltData melt			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM spell_melt");
			rs		= pstm.executeQuery();
			while(rs.next()){
				melt = new MeltData(rs);
				if (melt.skillId != -1) {
					ACTIVE_DATA.put(melt.skillId, melt);
				} else if (melt.passiveId > 0) {
					PASSIVE_DATA.put(melt.passiveId, melt);
				}
				
				FastMap<Integer, FastTable<Integer>> meltItems = CLASS_DATA.get(melt.meltItemId);
				if (meltItems == null) {
					meltItems = new FastMap<Integer, FastTable<Integer>>();
					CLASS_DATA.put(melt.meltItemId, meltItems);
				}
				
				FastTable<Integer> itemList = meltItems.get(melt.classType);
				if (itemList == null) {
					itemList = new FastTable<Integer>();
					meltItems.put(melt.classType, itemList);
				}
				itemList.add(melt.skillItemId);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public void reload(){
		ACTIVE_DATA.clear();
		PASSIVE_DATA.clear();
		CLASS_DATA.clear();
		load();
	}
}

