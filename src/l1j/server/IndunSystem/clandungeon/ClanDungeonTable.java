package l1j.server.IndunSystem.clandungeon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.SQLUtil;

public class ClanDungeonTable {
	private static ClanDungeonTable _instance;
	public static ClanDungeonTable getInstance(){
		if(_instance==null)_instance=new ClanDungeonTable();
		return _instance;
	}
	
	private FastTable<ClanDungeonObject> clandungeonList;
	
	public ClanDungeonTable(){
		clandungeonList = new FastTable<ClanDungeonObject>();
		load();
	}
	
	private void load(){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * from spawnlist_clandungeon");
			rs = pstm.executeQuery();
			ClanDungeonObject dungeon;
			while(rs.next()){
				dungeon = new ClanDungeonObject(rs.getInt("type"), rs.getInt("stage"), rs.getInt("npc_templateid"), rs.getInt("count"), rs.getBoolean("boss"));
				clandungeonList.add(dungeon);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public FastTable<ClanDungeonObject> getTypeList(int type){
		FastTable<ClanDungeonObject> list = new FastTable<ClanDungeonObject>();
		for (ClanDungeonObject obj : clandungeonList) {
			if (obj._type == type) {
				list.add(obj);
			}
		}
		return list;
	}
	
	public FastTable<ClanDungeonObject> getStageList(int type, int stage){
		FastTable<ClanDungeonObject> list = new FastTable<ClanDungeonObject>();
		for (ClanDungeonObject obj : clandungeonList) {
			if (obj._type != type || obj._stage != stage) {
				continue;
			}
			list.add(obj);
		}
		return list;
	}
	
	public int getRandomMonsterId(int type, int stage){
		ClanDungeonObject temp = null;
		int npcid = 0;
		while (npcid == 0) {
			temp = clandungeonList.get(CommonUtil.random(clandungeonList.size()));
			if (temp._type != type || temp._stage != stage || temp._boss) {
				continue;
			}
			npcid = temp._npcId;
		}
		return npcid;
	}
	
	public static void reload(){
		ClanDungeonTable oldinstance = _instance;
		_instance = new ClanDungeonTable();
		oldinstance.clandungeonList.clear();
		oldinstance = null;
	}
}

