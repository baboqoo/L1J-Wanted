package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.templates.L1SkillsInfo;
import l1j.server.server.utils.SQLUtil;

/**
 * 스킬 상세 정보
 * @author LinOffice
 */
public class SkillsInfoTable {
	private static Logger _log = Logger.getLogger(SkillsInfoTable.class.getName());
	private static final Map<Integer, L1SkillsInfo> DATA = new HashMap<Integer, L1SkillsInfo>();
	
	private static SkillsInfoTable _instance;
	public static SkillsInfoTable getInstance(){
		if (_instance == null) {
			_instance = new SkillsInfoTable();
		}
		return _instance;
	}
	private SkillsInfoTable(){
		load();
	}
	
	public static int getSkillIcon(int skillId){
		return DATA.containsKey(skillId) ? DATA.get(skillId).getIcon() : 0;
	}
	
	public static int getSkillStatusIcon(int skillId){
		return DATA.containsKey(skillId) ? DATA.get(skillId).getOnIconId() : 0;
	}
	
	public static L1SkillsInfo getSkillInfo(int id){
		return DATA.containsKey(id) ? DATA.get(id) : null;
	}
	
	private void load(){
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM skills_info ORDER BY skillId ASC");
			rs = pstm.executeQuery();
			L1SkillsInfo info;
			while(rs.next()){
				info			= new L1SkillsInfo(rs);
				L1Skills skill	= SkillsTable.getTemplate(info.getSkillId());
				if (skill == null) {
					System.out.println(String.format("[SkillsInfoTable] SKILL_TEMPLATE_EMPTY : SKILL_ID(%d)", info.getSkillId()));
					continue;
				}
				skill.setInfo(info);
				DATA.put(info.getSkillId(), info);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public void reload() {
		DATA.clear();
		load();
	}
}

