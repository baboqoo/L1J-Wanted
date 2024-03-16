package l1j.server.GameSystem.huntingquest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.util.FastMap;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

/**
 * 사냥터 도감
 * @author LinOffice
 */
public class HuntingQuestTable {
	private static HuntingQuestTable _instance;
	public static HuntingQuestTable getInstance(){
		if (_instance == null) {
			_instance = new HuntingQuestTable();
		}
		return _instance;
	}
	
	private static final FastMap<Integer, HuntingQuestObject> INFO				=	new FastMap<>();
	private static final FastMap<String, HuntingQuestTeleportObject> INFO_TEL	=	new FastMap<>();
	
	/**
	 * 도감 정보 조사
	 * @param mapid
	 * @param subNum(본토의 데스크번호)
	 * @return HuntingQuestObject
	 */
	public static HuntingQuestObject getHuntInfo(int mapid, int locationDesc){
		for (HuntingQuestObject obj : INFO.values()) {
			if (obj.getMapNumber() == mapid && obj.getLocationDesc() == locationDesc) {
				return obj;
			}
		}
		return null;
	}
	
	/**
	 * 도감 정보 조사
	 * @param infoNum(전담 번호)
	 * @return HuntingQuestObject
	 */
	public static HuntingQuestObject getHuntInfo(int infoNum){
		return INFO.get(infoNum);
	}
	
	/**
	 * 도감 텔레포트 정보 조사
	 * @param action_string(텔레포트 요청명)
	 * @return HuntingQuestTeleportObject
	 */
	public static HuntingQuestTeleportObject getHuntTelInfo(String action_string){
		return INFO_TEL.get(action_string);
	}
	
	private HuntingQuestTable(){
		load();
	}
	
	void load(){
		Connection con						= null;
		PreparedStatement pstm				= null;
		ResultSet rs						= null;
		HuntingQuestObject obj				= null;
		HuntingQuestTeleportObject telObj	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM hunting_quest");
			rs		= pstm.executeQuery();
			while(rs.next()){
				obj	= new HuntingQuestObject(rs);
				INFO.put(obj.getQuestId(), obj);
			}
			SQLUtil.close(rs, pstm);
			
			pstm	= con.prepareStatement("SELECT * FROM hunting_quest_teleport");
			rs		= pstm.executeQuery();
			while(rs.next()){
				telObj	= new HuntingQuestTeleportObject(rs);
				INFO_TEL.put(telObj.getActionString(), telObj);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public static void reload(){
		INFO.clear();
		INFO_TEL.clear();
		_instance.load();
	}
}

