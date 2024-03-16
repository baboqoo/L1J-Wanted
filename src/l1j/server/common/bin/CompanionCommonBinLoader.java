package l1j.server.common.bin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.common.DescKLoader;
import l1j.server.common.bin.companion.CompanionClassCommonBin;
import l1j.server.common.bin.companion.CompanionClassCommonBin.CompanionClassCommonBinExtend;
import l1j.server.common.bin.companion.CompanionSkillCommonBin;
import l1j.server.common.bin.companion.CompanionSkillCommonBin.CompanionSkillCommonBinExtend;
import l1j.server.common.bin.companion.CompanionSkillEnchantCommonBin.CompanionSkillEnchantCommonBinExtend;
import l1j.server.common.bin.companion.CompanionSkillEnchantCommonBin;
import l1j.server.common.bin.companion.CompanionStatCommonBin;
import l1j.server.common.bin.companion.CompanionStatCommonBin.CompanionStatCommonBinExtend;
import l1j.server.common.bin.companion.CompanionT;
import l1j.server.server.utils.SQLUtil;

/**
 * companion-common.bin 파일 로더
 * @author LinOffice
 */
public class CompanionCommonBinLoader {
	private static Logger _log = Logger.getLogger(CompanionCommonBinLoader.class.getName());
	private static final HashMap<Integer, CompanionT.ClassInfoT.ClassT> CLASS_DATA		= new HashMap<>();
	private static final HashMap<Integer, CompanionT.StatT.BaseStatBonusT> STAT_DATA	= new HashMap<>();
	private static final HashMap<Integer, CompanionT.WildSkillT.SkillT> SKILL_DATA		= new HashMap<>();
	private static final HashMap<Integer, CompanionT.SkillEnchantTierT> ENCHANT_DATA	= new HashMap<>();
	
	private static CompanionClassCommonBin class_bin;
	private static CompanionStatCommonBin stat_bin;
	private static CompanionSkillCommonBin skill_bin;
	private static CompanionSkillEnchantCommonBin enchant_bin;
	
	public static CompanionT.ClassInfoT.ClassT getClass(int class_id) {
		return CLASS_DATA.get(class_id);
	}
	
	public static CompanionT.WildSkillT.SkillT getSkillT(int skill_id) {
		return SKILL_DATA.get(skill_id);
	}
	
	public static CompanionT.SkillEnchantTierT getSkillEnchant(int tier) {
		return ENCHANT_DATA.get(tier);
	}
	
	public static int getSkillEnchantSize() {
		return ENCHANT_DATA.size();
	}
	
	private static CompanionCommonBinLoader _instance;
	public static CompanionCommonBinLoader getInstance() {
		if (_instance == null) {
			_instance = new CompanionCommonBinLoader();
		}
		return _instance;
	}

	private CompanionCommonBinLoader() {
		if (Config.COMMON.COMMON_COMPANION_BIN_UPDATE) {
			loadFile();
		} else {
			load();
		}
	}
	
	private void loadFile(){
		class_bin	= CompanionClassCommonBin.newInstance();
		stat_bin	= CompanionStatCommonBin.newInstance();
		skill_bin	= CompanionSkillCommonBin.newInstance();
		enchant_bin	= CompanionSkillEnchantCommonBin.newInstance();
		
		try {
			class_bin.readFrom(ProtoInputStream.newInstance("./data/Contents/companion_class-common.bin"));// bin 파일을 읽는다
			stat_bin.readFrom(ProtoInputStream.newInstance("./data/Contents/companion_stat-common.bin"));// bin 파일을 읽는다
			skill_bin.readFrom(ProtoInputStream.newInstance("./data/Contents/companion_skill-common.bin"));// bin 파일을 읽는다
			enchant_bin.readFrom(ProtoInputStream.newInstance("./data/Contents/companion_skill_enchant-common.bin"));// bin 파일을 읽는다
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		if (!class_bin.isInitialized())
			throw new IllegalArgumentException(String.format("fail initialized info data.(CompanionClassCommonBinLoader) %d", class_bin.getInitializeBit()));
		if (!stat_bin.isInitialized())
			throw new IllegalArgumentException(String.format("fail initialized info data.(CompanionStatCommonBin) %d", stat_bin.getInitializeBit()));
		if (!skill_bin.isInitialized())
			throw new IllegalArgumentException(String.format("fail initialized info data.(CompanionSkillCommonBin) %d", skill_bin.getInitializeBit()));
		if (!enchant_bin.isInitialized())
			throw new IllegalArgumentException(String.format("fail initialized info data.(CompanionSkillEnchantCommonBin) %d", enchant_bin.getInitializeBit()));
		
		regist_class();
		regist_stat();
		regist_skill();
		regist_enchant();
	}
	
	private void regist_class(){
		try {
			HashMap<Integer, CompanionClassCommonBinExtend> map = class_bin.get_Class();
			Connection con			= null;
			PreparedStatement pstm	= null;
			try {
				con		= L1DatabaseFactory.getInstance().getConnection();
				pstm	= con.prepareStatement("truncate table bin_companion_class_common");
				pstm.execute();
				SQLUtil.close(pstm);
				if (map == null || map.isEmpty()) {
					return;
				}
				
				con.setAutoCommit(false);
				pstm	= con.prepareStatement("INSERT INTO bin_companion_class_common SET classId=?, class=?, category=?, element=?, skill=?");
				for (CompanionClassCommonBinExtend extend : map.values()) {
					CompanionT.ClassInfoT.ClassT info = extend.get_Class();
					CLASS_DATA.put(info.get_classId(), info);
					int idx = 0;
					pstm.setInt(++idx, info.get_classId());
					pstm.setString(++idx, info.get_class());
					pstm.setString(++idx, String.format("%s(%d)", info.get_category().name(), info.get_category().toInt()));
					pstm.setString(++idx, String.format("%s(%d)", info.get_element().name(), info.get_element().toInt()));
					pstm.setString(++idx, info.get_Skill_toString());
					pstm.addBatch();
					pstm.clearParameters();
				}
				
				pstm.executeBatch();
				pstm.clearBatch();
				con.commit();
			} catch(SQLException e) {
				try {
					con.rollback();
				} catch(SQLException sqle){
					_log.log(Level.SEVERE, sqle.getLocalizedMessage(), sqle);
				}
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} catch(Exception e) {
				try {
					con.rollback();
				} catch(SQLException sqle){
					_log.log(Level.SEVERE, sqle.getLocalizedMessage(), sqle);
				}
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} finally {
				try {
					con.setAutoCommit(true);
				} catch (SQLException e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
				SQLUtil.close(pstm, con);
			}
			System.out.println("companion_class-common.bin [update completed]. TABLE : bin_companion_class_common");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void regist_stat(){
		try {
			HashMap<Integer, CompanionStatCommonBinExtend> map = stat_bin.get_stat();
			Connection con			= null;
			PreparedStatement pstm	= null;
			try {
				con		= L1DatabaseFactory.getInstance().getConnection();
				pstm	= con.prepareStatement("truncate table bin_companion_stat_common");
				pstm.execute();
				SQLUtil.close(pstm);
				if (map == null || map.isEmpty()) {
					return;
				}
				
				con.setAutoCommit(false);
				pstm	= con.prepareStatement("INSERT INTO bin_companion_stat_common SET id=?, statType=?, value=?, meleeDmg=?, meleeHit=?, regenHP=?, ac=?, spellDmg=?, spellHit=?");
				for (CompanionStatCommonBinExtend extend : map.values()) {
					CompanionT.StatT.BaseStatBonusT info = extend.get_stat();
					STAT_DATA.put(info.get_id(), info);
					int idx = 0;
					pstm.setInt(++idx, info.get_id());
					pstm.setString(++idx, String.format("%s(%d)", info.get_statType().name(), info.get_statType().toInt()));
					pstm.setInt(++idx, info.get_value());
					pstm.setInt(++idx, info.get_meleeDmg());
					pstm.setInt(++idx, info.get_meleeHit());
					pstm.setInt(++idx, info.get_regenHP());
					pstm.setInt(++idx, info.get_AC());
					pstm.setInt(++idx, info.get_spellDmg());
					pstm.setInt(++idx, info.get_spellHit());
					pstm.addBatch();
					pstm.clearParameters();
				}
				
				pstm.executeBatch();
				pstm.clearBatch();
				con.commit();
			} catch(SQLException e) {
				try {
					con.rollback();
				} catch(SQLException sqle){
					_log.log(Level.SEVERE, sqle.getLocalizedMessage(), sqle);
				}
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} catch(Exception e) {
				try {
					con.rollback();
				} catch(SQLException sqle){
					_log.log(Level.SEVERE, sqle.getLocalizedMessage(), sqle);
				}
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} finally {
				try {
					con.setAutoCommit(true);
				} catch (SQLException e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
				SQLUtil.close(pstm, con);
			}
			System.out.println("companion_stat-common.bin [update completed]. TABLE : bin_companion_stat_common");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void regist_skill(){
		try {
			HashMap<Integer, CompanionSkillCommonBinExtend> map = skill_bin.get_skill();
			Connection con			= null;
			PreparedStatement pstm	= null;
			try {
				con		= L1DatabaseFactory.getInstance().getConnection();
				pstm	= con.prepareStatement("truncate table bin_companion_skill_common");
				pstm.execute();
				SQLUtil.close(pstm);
				if (map == null || map.isEmpty()) {
					return;
				}
				
				con.setAutoCommit(false);
				pstm	= con.prepareStatement("INSERT INTO bin_companion_skill_common SET id=?, descNum=?, descKr=?, enchantBonus=?");
				for (CompanionSkillCommonBinExtend extend : map.values()) {
					CompanionT.WildSkillT.SkillT info = extend.get_skill();
					SKILL_DATA.put(info.get_id(), info);
					int idx = 0;
					pstm.setInt(++idx, info.get_id());
					pstm.setInt(++idx, info.get_descNum());
					pstm.setString(++idx, DescKLoader.getDesc(info.get_descNum()));
					pstm.setString(++idx, info.get_EnchantBonus_toString());
					pstm.addBatch();
					pstm.clearParameters();
				}
				
				pstm.executeBatch();
				pstm.clearBatch();
				con.commit();
			} catch(SQLException e) {
				try {
					con.rollback();
				} catch(SQLException sqle){
					_log.log(Level.SEVERE, sqle.getLocalizedMessage(), sqle);
				}
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} catch(Exception e) {
				try {
					con.rollback();
				} catch(SQLException sqle){
					_log.log(Level.SEVERE, sqle.getLocalizedMessage(), sqle);
				}
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} finally {
				try {
					con.setAutoCommit(true);
				} catch (SQLException e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
				SQLUtil.close(pstm, con);
			}
			System.out.println("companion_skill-common.bin [update completed]. TABLE : bin_companion_skill_common");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void regist_enchant(){
		try {
			HashMap<Integer, CompanionSkillEnchantCommonBinExtend> map = enchant_bin.get_enchant();
			Connection con			= null;
			PreparedStatement pstm	= null;
			try {
				con		= L1DatabaseFactory.getInstance().getConnection();
				pstm	= con.prepareStatement("truncate table bin_companion_enchant_common");
				pstm.execute();
				SQLUtil.close(pstm);
				if (map == null || map.isEmpty()) {
					return;
				}
				
				con.setAutoCommit(false);
				pstm	= con.prepareStatement("INSERT INTO bin_companion_enchant_common SET tier=?, enchantCost=?, openCost=?");
				for (CompanionSkillEnchantCommonBinExtend extend : map.values()) {
					CompanionT.SkillEnchantTierT info = extend.get_enchant();
					ENCHANT_DATA.put(info.get_tier(), info);
					int idx = 0;
					pstm.setInt(++idx, info.get_tier());
					pstm.setString(++idx, info.get_EnchantCost_toString());
					pstm.setString(++idx, info.get_OpenCost_toString());
					pstm.addBatch();
					pstm.clearParameters();
				}
				
				pstm.executeBatch();
				pstm.clearBatch();
				con.commit();
			} catch(SQLException e) {
				try {
					con.rollback();
				} catch(SQLException sqle){
					_log.log(Level.SEVERE, sqle.getLocalizedMessage(), sqle);
				}
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} catch(Exception e) {
				try {
					con.rollback();
				} catch(SQLException sqle){
					_log.log(Level.SEVERE, sqle.getLocalizedMessage(), sqle);
				}
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} finally {
				try {
					con.setAutoCommit(true);
				} catch (SQLException e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
				SQLUtil.close(pstm, con);
			}
			System.out.println("companion_skill_enchant-common.bin [update completed]. TABLE : bin_companion_enchant_common");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void load() {
		CompanionT.ClassInfoT.ClassT classInfo		= null;
		CompanionT.StatT.BaseStatBonusT statInfo	= null;
		CompanionT.WildSkillT.SkillT skillInfo		= null;
		CompanionT.SkillEnchantTierT enchantInfo	= null;
		Connection con								= null;
		PreparedStatement pstm						= null;
		ResultSet rs								= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			
			pstm = con.prepareStatement("SELECT * FROM bin_companion_class_common");
			rs = pstm.executeQuery();
			while (rs.next()) {
				classInfo = new CompanionT.ClassInfoT.ClassT(rs);
				CLASS_DATA.put(classInfo.get_classId(), classInfo);
			}
			SQLUtil.close(rs, pstm);
			
			pstm = con.prepareStatement("SELECT * FROM bin_companion_stat_common");
			rs = pstm.executeQuery();
			while (rs.next()) {
				statInfo = new CompanionT.StatT.BaseStatBonusT(rs);
				STAT_DATA.put(statInfo.get_id(), statInfo);
			}
			SQLUtil.close(rs, pstm);
			
			pstm = con.prepareStatement("SELECT * FROM bin_companion_skill_common");
			rs = pstm.executeQuery();
			while (rs.next()) {
				skillInfo = new CompanionT.WildSkillT.SkillT(rs);
				SKILL_DATA.put(skillInfo.get_id(), skillInfo);
			}
			SQLUtil.close(rs, pstm);
			
			pstm = con.prepareStatement("SELECT * FROM bin_companion_enchant_common");
			rs = pstm.executeQuery();
			while (rs.next()) {
				enchantInfo = new CompanionT.SkillEnchantTierT(rs);
				ENCHANT_DATA.put(enchantInfo.get_tier(), enchantInfo);
			}
			
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public static void reload() {
		if (Config.COMMON.COMMON_COMPANION_BIN_UPDATE) {
			_instance.loadFile();
		} else {
			_instance.load();
		}
	}
}

