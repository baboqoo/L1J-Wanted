package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1Clan;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

/**
 * 혈맹의 축복 버프 정보
 * @author LinOffice
 */
public final class ClanBlessBuffTable {
	public class ClanBuff {
		public int id;
		public int buffnumber;
		public String mapname;
		public int teleportX, teleportY, teleportM;
		public FastTable<Integer> buffmaplist;
	}
	
	private static ClanBlessBuffTable _instance;

	private final static HashMap<Integer, ClanBuff> DATA = new HashMap<Integer, ClanBuff>();

	public static ClanBlessBuffTable getInstance() {
		if (_instance == null) {
			_instance = new ClanBlessBuffTable();
		}
		return _instance;
	}
	
	private ClanBlessBuffTable() {
		loadBuffDatabase();
	}
	
	public void reload() {
		DATA.clear();
		loadBuffDatabase();
	}

	/**
	 * MAP 정보를 받아와서 해쉬맵에 정보 저장한다, HashMap _maps에 격납한다.
	 */
	private void loadBuffDatabase() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		ClanBuff data			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM clan_bless_buff");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				data				= new ClanBuff();
				int id				= rs.getInt("number");
				data.buffnumber		= rs.getInt("buff_id");
				data.mapname		= rs.getString("map_name");
				data.teleportX		= rs.getInt("teleport_x");
				data.teleportY		= rs.getInt("teleport_y");
				data.teleportM		= rs.getInt("teleport_map_id");
				data.buffmaplist	= new FastTable<Integer>();
				String[] text		= rs.getString("buff_map_list").split(StringUtil.CommaString);
				for (int i=0; i<text.length; i++) {
					data.buffmaplist.add(Integer.parseInt(text[i].trim()));
				}
				DATA.put(new Integer(id), data);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public static int getBuff(int number) {
		ClanBuff buff = DATA.get(number);
		if (buff == null) {
			return 0;
		}
		return buff.buffnumber;
	}
	
	public static ClanBuff getBuffList(int number) {
		for (int i = 1; i < DATA.size() + 1; i++) {
			ClanBuff buff = DATA.get(i);
			if (buff.buffnumber == number) {
				return buff;
			}
		}
		return null;
	}
	
	public static int getRandomBuff(L1Clan pledge){
		int buff = 0;
		while(true){
			ClanBuff pledge_buff = DATA.get(CommonUtil.random(DATA.size()) + 1);
			if (pledge_buff.buffnumber != pledge.getBuffFirst() && pledge_buff.buffnumber != pledge.getBuffSecond() && pledge_buff.buffnumber != pledge.getBuffThird()) {
				buff = pledge_buff.buffnumber;
				break;
			}
		}
		return buff;
	}
	
	public static int getBuffSize() {
		return DATA.size();
	}
}
