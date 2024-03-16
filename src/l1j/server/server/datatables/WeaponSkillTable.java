package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.construct.L1Attr;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.model.L1WeaponSkill;
import l1j.server.server.model.L1WeaponSkill.WeaponSkillAttackType;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

/**
 * 무기 스킬 정보
 * @author LinOffice
 */
public class WeaponSkillTable {
	private static Logger _log = Logger.getLogger(WeaponSkillTable.class.getName());

	private static final HashMap<Integer, ConcurrentHashMap<WeaponSkillAttackType, L1WeaponSkill>> DATA = new HashMap<Integer, ConcurrentHashMap<WeaponSkillAttackType, L1WeaponSkill>>();

	public static ConcurrentHashMap<WeaponSkillAttackType, L1WeaponSkill> getWeaponSkill(int weaponId){
		return DATA.get(weaponId);
	}
	
	private static WeaponSkillTable _instance;
	public static WeaponSkillTable getInstance() {
		if (_instance == null) {
			_instance = new WeaponSkillTable();
		}
		return _instance;
	}

	private WeaponSkillTable() {
		loadWeaponSkill();
	}

	private void loadWeaponSkill() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM weapon_skill");
			rs = pstm.executeQuery();
			fillWeaponSkillTable(rs);
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	private void fillWeaponSkillTable(ResultSet rs) throws SQLException {
		L1WeaponSkill weaponSkill = null;
		while(rs.next()){
			int weaponId			= rs.getInt("weapon_id");
			int probability			= rs.getInt("probability");
			int fixDamage			= rs.getInt("fix_damage");
			int randomDamage		= rs.getInt("random_damage");
			int area				= rs.getInt("area");
			int skillId				= rs.getInt("skill_id");
			int skillTime			= rs.getInt("skill_time");
			int effectId			= rs.getInt("effect_id");
			int effectTarget		= rs.getInt("effect_target");
			boolean isArrowType		= rs.getBoolean("arrow_type");
			L1Attr attr				= L1Attr.fromString(rs.getString("attr"));
			int enchant_probability	= rs.getInt("enchant_probability");
			int enchant_damage		= rs.getInt("enchant_damage");
			int int_damage			= rs.getInt("int_damage");
			int spell_damage		= rs.getInt("spell_damage");
			int enchant_limit		= rs.getInt("enchant_limit");
			WeaponSkillAttackType attackType = WeaponSkillAttackType.fromString(rs.getString("attackType"));
			boolean hpStill			= Boolean.parseBoolean(rs.getString("hpStill"));
			int hpStillProbability	= rs.getInt("hpStill_probabliity");
			int hpStillValue		= rs.getInt("hpStillValue");
			boolean mpStill			= Boolean.parseBoolean(rs.getString("mpStill"));
			int mpStillProbability	= rs.getInt("mpStill_probabliity");
			int mpStillValue		= rs.getInt("mpStillValue");
			int stillEffectId		= rs.getInt("stillEffectId");
			
			weaponSkill = new L1WeaponSkill(
					weaponId, probability, fixDamage, randomDamage, area, skillId, skillTime, effectId, effectTarget, isArrowType, attr,
					enchant_probability, enchant_damage, int_damage, spell_damage, enchant_limit, attackType, 
					hpStill, hpStillProbability, hpStillValue, mpStill, mpStillProbability, mpStillValue, stillEffectId);
			
			ConcurrentHashMap<WeaponSkillAttackType, L1WeaponSkill> map = DATA.get(weaponId);
			if (map == null) {
				map = new ConcurrentHashMap<WeaponSkillAttackType, L1WeaponSkill>();
				DATA.put(weaponId, map);
			}
			map.put(weaponSkill.getAttackType(), weaponSkill);
		}
		//_log.config("무기 스킬 리스트 " + DATA.size() + "건 로드");
		_log.config("Weapon skill list " + DATA.size() + " entries loaded");
	}
	
	public static void reload() {
		DATA.clear();
		_instance.loadWeaponSkill();
		
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc == null || pc.getNetConnection() == null || pc.getInventory() == null) {
				continue;
			}
			for (L1ItemInstance item : pc.getInventory().getItems()) {
				if (item == null || item.getItem().getItemType() != L1ItemType.WEAPON) {
					continue;
				}
				item.setWeaponSkill();
			}
		}
	}
}

