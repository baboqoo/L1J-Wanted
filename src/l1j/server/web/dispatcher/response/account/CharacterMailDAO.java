package l1j.server.web.dispatcher.response.account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

/**
 * 마이페이지 편지
 * @author LinOffice
 */
public class CharacterMailDAO {
	private static CharacterMailDAO _instance;
	public static CharacterMailDAO getInstance() {
		if (_instance == null) {
			_instance = new CharacterMailDAO();
		}
		return _instance;
	}
	
	private Map<Integer, CharacterMailVO> DATA;
	
	public List<CharacterMailVO> getList(String charName){
		List<CharacterMailVO> list = new ArrayList<CharacterMailVO>();
		for (CharacterMailVO vo : DATA.values()) {
			if (vo.getReceiver().equals(charName)) {
				list.add(vo);
			}
		}
		return list;
	}
	
	private CharacterMailDAO() {
		DATA = new ConcurrentHashMap<Integer, CharacterMailVO>();
		load();
	}
	
	private void load() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		CharacterMailVO vo = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM letter ORDER BY date");
			rs = pstm.executeQuery();
			while(rs.next()) {
				vo = new CharacterMailVO(
						rs.getInt("item_object_id"), 
						rs.getInt("code"), 
						rs.getString("sender"), 
						rs.getString("receiver"), 
						rs.getTimestamp("date"), 
						rs.getInt("template_id"), 
						rs.getString("subject"), 
						rs.getString("content"), 
						rs.getBoolean("isCheck")
					);
				DATA.put(vo.getId(), vo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public static void release() {
		CharacterMailDAO old = _instance;
    	old.DATA.clear();
    	old = null;
    }
}

