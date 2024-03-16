package l1j.server.server.model.skill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.SQLUtil;

public class L1SkillActionFactory {
	private static Logger _log = Logger.getLogger(L1SkillActionFactory.class.getName());
	
	private static class newInstance {
		public static final L1SkillActionFactory INSTANCE = new L1SkillActionFactory();
	}
	public static L1SkillActionFactory getInstance(){
		return newInstance.INSTANCE;
	}
	
	private static final HashMap<Integer, L1SkillActionHandler> DATA = new HashMap<>();
	public static L1SkillActionHandler getHandler(int spellId){
		return DATA.get(spellId);
	}
	
	private L1SkillActionFactory(){
		load();
	}
	
	private void load(){
		Connection con					= null;
		PreparedStatement pstm			= null;
		ResultSet rs					= null;
		L1SkillActionHandler handler	= null;
		L1Skills skill					= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT skillId, className FROM skills_handler ORDER BY skillId ASC");
			rs		= pstm.executeQuery();
			while(rs.next()){
				String className	= rs.getString("className");
				int skillId			= rs.getInt("skillId");
				Class<?> cls		= getClass(className);
				if (cls == null) {
					System.out.println(String.format("[SkillFactory] Class Not Found : ClassName(%s) SkillId(%d)", className, skillId));
					continue;
				}
				handler = (L1SkillActionHandler)cls.newInstance();
				handler._skillId	= skillId;
				skill				= SkillsTable.getTemplate(handler._skillId);
				if (skill == null) {
					System.out.println(String.format("[SkillFactory] Template Empty : SkillId(%d)", skillId));
					continue;
				}
				skill.setHandler(handler);
				handler._skill		= skill;
				DATA.put(handler._skillId, handler);
			}
		} catch(SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	private Class<?> getClass(String path) throws ClassNotFoundException {
		try {
			return Class.forName(String.format("l1j.server.server.model.skill.action.%s", path));
		} catch(ClassNotFoundException excetion) {
			excetion.printStackTrace();
			return null;
		}
	}
	
	public void reload(){
		DATA.clear();
		load();
	}
}

