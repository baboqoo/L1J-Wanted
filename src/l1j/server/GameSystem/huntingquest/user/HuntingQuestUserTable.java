package l1j.server.GameSystem.huntingquest.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;
import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

/**
 * 유저의 사냥터 도감 정보
 * @author LinOffice
 */
public class HuntingQuestUserTable {
	private static Logger _log = Logger.getLogger(HuntingQuestUserTable.class.getName());
	
	private static class newInstance {
		public static final HuntingQuestUserTable INSTANCE	=	new HuntingQuestUserTable();
	}
	public static HuntingQuestUserTable getInstance(){
		return newInstance.INSTANCE;
	}
	
	private static final FastMap<Integer, HuntingQuestUser> USER_INFO = new FastMap<Integer, HuntingQuestUser>();
	
	private int _id;
	
	// 마지막 primaryKey 구하기
	public int nextId(){
		return ++_id;
	}
	
	/**
	 * 유저의 사냥터 도감 데이터
	 * @param objid
	 * @return HuntingQuestUser
	 */
	public static HuntingQuestUser getUser(final int objid){
		HuntingQuestUser user = USER_INFO.get(objid);
		if (user == null) {
			user = new HuntingQuestUser(objid);
			USER_INFO.put(objid, user);
		}
		return user;
	}
	
	private HuntingQuestUserTable(){
		load();
	}
	
	void load(){
		Connection con				= null;
		PreparedStatement pstm		= null;
		ResultSet rs				= null;
		PreparedStatement tempPstm	= null;
		ResultSet tempRs			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT DISTINCT(objID) FROM character_hunting_quest");
			rs		= pstm.executeQuery();
			while(rs.next()){
				try {
					int objid	= rs.getInt("objID");
					tempPstm	= con.prepareStatement("SELECT * FROM character_hunting_quest WHERE objID=? ORDER BY id ASC");
					tempPstm.setInt(1, objid);
					tempRs		= tempPstm.executeQuery();
					HuntingQuestUser user		= new HuntingQuestUser(objid);
					while(tempRs.next()){
						int id					= tempRs.getInt("id");
						int map_number			= tempRs.getInt("map_number");
						int location_desc		= tempRs.getInt("location_desc");
						int quest_id			= tempRs.getInt("quest_id");
						int kill_count			= tempRs.getInt("kill_count");
						boolean complete		= tempRs.getBoolean("complete");
						HuntingQuestUserTemp obj = new HuntingQuestUserTemp(id, objid, map_number, location_desc, quest_id, kill_count, complete);
						user.add(quest_id, obj);
						if (id > _id) {
							_id = id;
						}
					}
					USER_INFO.put(objid, user);
				} catch(Exception e){
					e.printStackTrace();
				} finally {
					SQLUtil.close(tempRs, tempPstm);
				}
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	// 케릭터 logout때만 Database갱신
	public void store(L1PcInstance pc){
		if (pc.getHuntingQuest() == null) {
			return;
		}
		FastTable<HuntingQuestUserTemp> list	=	new FastTable<HuntingQuestUserTemp>(pc.getHuntingQuest().getInfo().values());
		HuntingQuestUserTemp obj				=	null;
		Connection con						=	null;
		PreparedStatement pstm				=	null;
		try {
			con 	=	L1DatabaseFactory.getInstance().getConnection();
			pstm	=	con.prepareStatement("DELETE FROM character_hunting_quest WHERE objID=?");
			pstm.setInt(1, pc.getId());
			pstm.execute();
			SQLUtil.close(pstm);
			if (list.isEmpty()) {
				return;
			}
			con.setAutoCommit(false);
			pstm	=	con.prepareStatement("INSERT INTO character_hunting_quest SET id=?, objID=?, map_number=?, location_desc=?, quest_id=?, kill_count=?, complete=?");
			for (int i=0; i<list.size(); i++) {
				obj = list.get(i);
				int index = 0;
				pstm.setInt(++index, obj.getId());
				pstm.setInt(++index, obj.getUserObjid());
				pstm.setInt(++index, obj.getMapNumber());
				pstm.setInt(++index, obj.getLocationDesc());
				pstm.setInt(++index, obj.getQuestId());
				pstm.setInt(++index, obj.getKillCount());
				pstm.setString(++index, obj.isComplete() ? StringUtil.TrueString : StringUtil.FalseString);
				pstm.addBatch();
				pstm.clearParameters();
			}
			pstm.executeBatch();
			pstm.clearBatch();
			con.commit();
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch(SQLException sqle){
				sqle.printStackTrace();
			}
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			SQLUtil.close(pstm, con);
		}
	}
	
	// 케릭터 삭제시 메모리영역 제거
	public void removeInfo(int objid){
		if (USER_INFO.containsKey(objid)) {
			USER_INFO.get(objid).clear();
			USER_INFO.remove(objid);
		}
	}

	public void reset() {
		for (HuntingQuestUser value : USER_INFO.values()) {
			value.getInfo().clear();
		}		
		USER_INFO.clear();	
	}

}

