package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.Map;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

/**
 * 플레이 서포트 지역 정보
 * @author LinOffice
 */
public class PlaySupportTable {
	private static Logger _log = Logger.getLogger(PlaySupportTable.class.getName());
	
	class PlaySupport {
		boolean whole, surround, sub;
	}
	
	/**
     * 플레이서포트 맵 타입별 사용여부 체크
     * @param type
     * @param mapId
     * @return boolean
     */
    public static boolean isSupportMap(int type, int mapId){
    	PlaySupport support = DATA.get(mapId);
    	if (support == null) {
    		return false;
    	}
    	switch(type){
    	case 1:return support.sub;
    	case 2:return support.surround;
    	case 3:return support.whole;
    	default:return false;
    	}
    }
	
    private static final Map<Integer, PlaySupport> DATA = new HashMap<Integer, PlaySupport>();
    
    private static PlaySupportTable _instance;
    public static PlaySupportTable getInstance() {
		if (_instance == null) {
			_instance = new PlaySupportTable();
		}
		return _instance;
	}
    
    private PlaySupportTable() {
        load();
    }
    
    private void load() {
    	Connection con = null;
    	PreparedStatement pstm = null;
    	ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM playsupport");
			rs = pstm.executeQuery();
			PlaySupport support = null;
			while(rs.next()){
				support				= new PlaySupport();
				int mapId			= rs.getInt("mapid");
                support.whole		= rs.getBoolean("whole");
                support.surround	= rs.getBoolean("surround");
                support.sub			= rs.getBoolean("sub");
                DATA.put(mapId, support);
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
    	_instance = null;
		_instance = new PlaySupportTable();
    }
}
