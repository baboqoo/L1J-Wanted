package l1j.server.QuestSystem.Loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.QuestSystem.Compensator.NormalQuestCompensator;
import l1j.server.QuestSystem.Compensator.QuestCompensator;
import l1j.server.QuestSystem.Compensator.WeekQuestCompensator;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class MonsterBookCompensateLoader {
	private static Logger _log = Logger.getLogger(MonsterBookCompensateLoader.class.getName());
	
	private static MonsterBookCompensateLoader _instance;
	public static MonsterBookCompensateLoader getInstance(){
		if (_instance == null)
			_instance = new MonsterBookCompensateLoader();
		return _instance;
	}
	
	public static void reload(){
		MonsterBookCompensateLoader tmp = _instance;
		_instance = new MonsterBookCompensateLoader();
		tmp.clear();
		tmp = null;
	}

	/** 일반 도감퀘 보상 **/
	private Map<Integer, ArrayList<NormalQuestCompensator>> _normal_compensators;
	
	/** 주간 도감퀘 보상 **/
	private Map<Integer, WeekQuestCompensator> 				_week_compensators;
	
	private MonsterBookCompensateLoader(){
		_normal_compensators		= new HashMap<Integer, ArrayList<NormalQuestCompensator>>(3);
		for (int i = 1; i <= 3; i++)
			_normal_compensators.put(i, new ArrayList<NormalQuestCompensator>(2));		
		_week_compensators			= new HashMap<Integer, WeekQuestCompensator>(3);
		
		Connection con				= null;
		PreparedStatement pstm 		= null;
		ResultSet rs 				= null;
		QuestCompensator comp		= null;
		StringBuilder sbQry			= new StringBuilder();
		String record				= StringUtil.EmptyString;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			sbQry.append("select * from ").append(NormalQuestCompensator._table);
			pstm = con.prepareStatement(sbQry.toString());
			rs = pstm.executeQuery();
			record = "NormalQuestCompensator";
			while(rs.next()){
				comp = new NormalQuestCompensator();
				comp.set(rs);
				
				_normal_compensators.get(comp.getDifficulty()).add((NormalQuestCompensator) comp);
			}
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			
			sbQry = new StringBuilder();
			sbQry.append("select * from ").append(WeekQuestCompensator._table);
			pstm = con.prepareStatement(sbQry.toString());
			rs = pstm.executeQuery();
			record = "WeekQuestCompensator";

			while(rs.next()){
				comp = new WeekQuestCompensator();
				comp.set(rs);
				
				_week_compensators.put(comp.getDifficulty(), (WeekQuestCompensator) comp);
			}
		} catch (Exception e){
			StringBuilder sb = new StringBuilder();
			sb.append("[ERROR - MonsterBookCompensateLoader]").append(record).append(" read error. \r\n");
			if (comp != null)
				sb.append(comp.getLastRecord()).append("\r\n");
			sb.append(e.getLocalizedMessage());
			_log.log(Level.SEVERE, sb.toString(), e);
			System.out.println(sb.toString());
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public void clear(){
		for (int i = 1; i <= 3; i++)
			_normal_compensators.get(i).clear();
		_normal_compensators.clear();
		_week_compensators.clear();
	}
	
	public ArrayList<NormalQuestCompensator> getNormalCompensators(int difficulty){
		ArrayList<NormalQuestCompensator> list = _normal_compensators.get(difficulty);
		return list;
	}
	
	public ArrayList<WeekQuestCompensator> getWeekCompensators(){
		ArrayList<WeekQuestCompensator> list = new ArrayList<WeekQuestCompensator>(_week_compensators.size());
		list.addAll(_week_compensators.values());
		return list;
	}
	
	public WeekQuestCompensator getWeekCompensator(int difficulty){
		return _week_compensators.get(difficulty);
	}
	
}

