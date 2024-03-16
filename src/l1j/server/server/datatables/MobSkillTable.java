package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.templates.L1MobSkill;
import l1j.server.server.utils.SQLUtil;

public class MobSkillTable {
	private static Logger _log = Logger.getLogger(MobSkillTable.class.getName());
	private static final HashMap<Integer, L1MobSkill> DATA = new HashMap<Integer, L1MobSkill>();
	
	public static L1MobSkill getTemplate(int id) {
		return DATA.get(id);
	}

	private static MobSkillTable _instance;
	public static MobSkillTable getInstance() {
		if (_instance == null) {
			_instance = new MobSkillTable();
		}
		return _instance;
	}

	private MobSkillTable() {
		loadMobSkillData();
	}

	public static void reload() {
		DATA.clear();
		_instance.loadMobSkillData();
	}
	
	private void loadMobSkillData() {
		Connection con = null;
		PreparedStatement pstm1 = null;
		PreparedStatement pstm2 = null;
		ResultSet rs1 = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm1 = con.prepareStatement("SELECT mobid, COUNT(*) AS cnt FROM mobskill GROUP BY mobid");
			int count = 0, mobid = 0, actNo = 0;
			pstm2 = con.prepareStatement("SELECT * FROM mobskill WHERE mobid = ? ORDER BY mobid, actNo");
			for (rs1 = pstm1.executeQuery(); rs1.next();) {
				mobid = rs1.getInt("mobid");
				count = rs1.getInt("cnt");
				ResultSet rs2 = null;
				try {
					pstm2.setInt(1, mobid);
					L1MobSkill mobskill = new L1MobSkill(count);
					mobskill.set_mobid(mobid);
					rs2 = pstm2.executeQuery();
					while(rs2.next()){
						actNo = rs2.getInt("actNo");
						mobskill.setMobName(rs2.getString("mobname"));
						mobskill.setType(actNo, L1MobSkill.TYPE.fromString(rs2.getString("type")));
						mobskill.setProb(actNo, rs2.getInt("prob"));
						mobskill.setEnableHp(actNo, rs2.getInt("enableHp"));
						mobskill.setEnableCompanionHp(actNo, rs2.getInt("enableCompanionHp"));
						mobskill.setRange(actNo, rs2.getInt("range"));
						mobskill.setLimitCount(actNo, rs2.getInt("limitCount"));
						mobskill.setChangeTarget(actNo, L1MobSkill.CHANGE_TARGET.fromString(rs2.getString("ChangeTarget")));
						mobskill.setAreaWidth(actNo, rs2.getInt("AreaWidth"));
						mobskill.setAreaHeight(actNo, rs2.getInt("AreaHeight"));
						mobskill.setLeverage(actNo, rs2.getInt("Leverage"));
						int skillId = rs2.getInt("SkillId");
						mobskill.setSkillId(actNo, skillId);
						mobskill.setGfxid(actNo, rs2.getInt("Gfxid"));
						mobskill.setActid(actNo, rs2.getInt("Actid"));
						mobskill.setSummon(actNo, rs2.getInt("SummonId"));
						mobskill.setSummonMin(actNo, rs2.getInt("SummonMin"));
						mobskill.setSummonMax(actNo, rs2.getInt("SummonMax"));
						mobskill.setPolyId(actNo, rs2.getInt("PolyId"));
						mobskill.setMsg(actNo, rs2.getString("Msg"));
						
						if (skillId != -1 && SkillsTable.getTemplate(skillId) == null) {
							System.out.println(String.format("[MobSkillTable] SKILL_TEMPLATE_EMPTY : SKILL_ID(%d), MONSTER_ID(%d)", skillId, mobid));
						}
					}
					DATA.put(new Integer(mobid), mobskill);
				} catch (SQLException e1) {
					_log.log(Level.SEVERE, e1.getLocalizedMessage(), e1);
				} finally {
					SQLUtil.close(rs2);
				}
			}

		} catch (SQLException e2) {
			_log.log(Level.SEVERE, "error while creating mobskill table", e2);
		} finally {
			SQLUtil.close(rs1);
			SQLUtil.close(pstm2);
			SQLUtil.close(pstm1);
			SQLUtil.close(con);
		}
	}
}

