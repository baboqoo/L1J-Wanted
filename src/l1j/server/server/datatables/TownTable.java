package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.construct.L1TownType;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Town;
import l1j.server.server.utils.SQLUtil;

public class TownTable {
	private static Logger _log = Logger.getLogger(TownTable.class.getName());

	private static TownTable _instance;

	private static final Map<Integer, L1Town> TOWNS = new ConcurrentHashMap<Integer, L1Town>();
	private static final Map<Integer, L1Town> TOWNS_NPCS = new ConcurrentHashMap<Integer, L1Town>();

	public static TownTable getInstance() {
		if (_instance == null) {
			_instance = new TownTable();
		}
		return _instance;
	}

	private TownTable() {
		load();
	}

	public void load() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		
		TOWNS.clear();
		TOWNS_NPCS.clear();
		int townid;
		L1Town town = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			
			pstm = con.prepareStatement("SELECT * FROM town");
			rs = pstm.executeQuery();
			while(rs.next()){
				town = new L1Town();
				townid = rs.getInt("town_id");
				town.set_townid(townid);
				town.set_name(rs.getString("name"));
				town.set_leader_id(rs.getInt("leader_id"));
				town.set_leader_name(rs.getString("leader_name"));
				town.set_tax_rate(rs.getInt("tax_rate"));
				town.set_tax_rate_reserved(rs.getInt("tax_rate_reserved"));
				town.set_sales_money(rs.getInt("sales_money"));
				town.set_sales_money_yesterday(rs.getInt("sales_money_yesterday"));
				town.set_town_tax(rs.getInt("town_tax"));
				town.set_town_fix_tax(rs.getInt("town_fix_tax"));

				TOWNS.put(new Integer(townid), town);
			}
			SQLUtil.close(rs, pstm);
			
			pstm = con.prepareStatement("SELECT * FROM town_npc");
			rs = pstm.executeQuery();
			while(rs.next()){
				int npc_id = rs.getInt("npc_id");				
				townid = L1TownType.fromString(rs.getString("town")).getId();
				//System.out.println("cargando la ciudad del npc " + npc_id + " que tiene como town " + rs.getString("town") + " y ahora tiene un town id " + townid);
				town = TOWNS.get(townid);
				if (town == null) {
					continue;
				}
				TOWNS_NPCS.put(npc_id, town);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public static L1Town[] getTownTableList() {
		return TOWNS.values().toArray(new L1Town[TOWNS.size()]);
	}

	public static L1Town getTownTable(int id) {
		return TOWNS.get(id);
	}

	public static boolean isLeader(L1PcInstance pc, int town_id) {
		L1Town town = getTownTable(town_id);
		return town.get_leader_id() == pc.getId();
	}
	
	public static L1Town getTownFromNpcId(int npc_id) {
		return TOWNS_NPCS.get(npc_id);
	}

	public void updateTaxRate() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE town SET tax_rate = tax_rate_reserved");
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	public void updateSalesMoneyYesterday() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE town SET sales_money_yesterday = sales_money, sales_money = 0");
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
}

