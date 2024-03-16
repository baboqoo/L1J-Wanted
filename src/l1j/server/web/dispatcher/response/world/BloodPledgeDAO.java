package l1j.server.web.dispatcher.response.world;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.construct.L1CastleType;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

/**
 * 혈맹
 * @author LinOffice
 */
public class BloodPledgeDAO {
	private static BloodPledgeDAO _instance;
	public static BloodPledgeDAO getInstance() {
		if (_instance == null) {
			_instance = new BloodPledgeDAO();
		}
		return _instance;
	}
	
	private static ArrayList<BloodPledgeVO> _list = new ArrayList<BloodPledgeVO>();
	
	public static ArrayList<BloodPledgeVO> getList(){
		return _list;
	}
	
	public static ArrayList<BloodPledgeVO> getList(String pledge_name){
		ArrayList<BloodPledgeVO> list = new ArrayList<BloodPledgeVO>();
		for (BloodPledgeVO vo : _list) {
			if (vo.getPledgeName().contains(pledge_name)) {
				list.add(vo);
			}
		}
		return list;
	}
	
	public static BloodPledgeVO getPledge(int pledge_id) {
		BloodPledgeVO vo = null;
		for (BloodPledgeVO clan : _list) {
			if (clan.getPledgeId() == pledge_id) {
				vo = clan;
				break;
			}
		}
		return vo;
	}
	
	private BloodPledgeDAO() {
		load();
	}
	
	private void load(){
		Connection con				= null;
		PreparedStatement pstm		= null;
		ResultSet rs				= null;
		BloodPledgeVO vo					= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT A.*, (SELECT COUNT(ClanID) FROM characters where A.clan_id = ClanID) AS members, CASE WHEN hashouse > 0 THEN IFNULL((SELECT IFNULL(H.house_area, '') FROM house H WHERE H.house_id = A.hashouse), '') ELSE '' END AS houseArea FROM (SELECT clan_id, clan_name, leader_name, hascastle, hashouse, contribution, clan_birthday FROM clan_data WHERE bot = 'false' ORDER BY clan_birthday DESC) A");
			rs = pstm.executeQuery();
			while(rs.next()){
				int hasHouse		= rs.getInt("hashouse");
				String castleName	= getCastleName(rs.getInt("hascastle"));
				String houseArea	= rs.getString("houseArea");
				String houseName	= null;
				if (!StringUtil.isNullOrEmpty(houseArea)) {
					//houseName		= String.format("%s/%s칸", getHouseFrontName(hasHouse), houseArea);
					houseName 		= String.format("%s/%s squares", getHouseFrontName(hasHouse), houseArea);
				} else {
					houseName		= StringUtil.EmptyString;
				}
				int pledge_id			= rs.getInt("clan_id");
				vo = new BloodPledgeVO(pledge_id, rs.getString("clan_name"), rs.getString("leader_name"), castleName, houseName, rs.getInt("members"), rs.getInt("contribution"), rs.getTimestamp("clan_birthday"));
				_list.add(vo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	private String getCastleName(int value) {
		if (value <= 0) {
			return null;
		}
		return L1CastleType.fromInt(value).getName();
	}
	
	static final String[] HOUSE_NAMES = {
		//"기란", "하이네", "아덴", "글루딘"
		"Giran", "Heine", "Aden", "Gludio"
	};
	
	private String getHouseFrontName(int value) {
		if (value >= 262145 && value <= 262189) {
			return HOUSE_NAMES[0];
		}
		if (value >= 327681 && value <= 327691) {
			return HOUSE_NAMES[1];
		}
		if (value >= 458753 && value <= 458819) {
			return HOUSE_NAMES[2];
		}
		return HOUSE_NAMES[3];
	}
	
	public static void reload() {
		release();
		_instance = new BloodPledgeDAO();
	}
	
	public static void release() {
		_list.clear();
		_instance = null;
	}
}

