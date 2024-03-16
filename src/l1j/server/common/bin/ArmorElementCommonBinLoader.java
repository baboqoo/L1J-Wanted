package l1j.server.common.bin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.common.bin.enchant.ArmorElementCommonBin;
import l1j.server.common.bin.enchant.ArmorElementalEnchantBonus;
import l1j.server.common.bin.enchant.ArmorElementCommonBin.ArmorElementCommonBinExtend;
import l1j.server.server.utils.SQLUtil;

/**
 * armor_element-common.bin 파일 로더
 * @author LinOffice
 */
public class ArmorElementCommonBinLoader {
	private static Logger _log = Logger.getLogger(ArmorElementCommonBinLoader.class.getName());
	private static ArmorElementCommonBin bin;
	
	private static ArmorElementCommonBinLoader _instance;
	public static ArmorElementCommonBinLoader getInstance() {
		if (_instance == null) {
			_instance = new ArmorElementCommonBinLoader();
		}
		return _instance;
	}

	private ArmorElementCommonBinLoader() {
		loadFile();
	}
	
	private void loadFile(){
		bin = ArmorElementCommonBin.newInstance();
		try {
			bin.readFrom(ProtoInputStream.newInstance("./data/Contents/armor_element-common.bin"));// bin 파일을 읽는다
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		if (!bin.isInitialized())
			throw new IllegalArgumentException(String.format("fail initialized info data.(ArmorElementCommonBin) %d", bin.getInitializeBit()));
		
		if (Config.COMMON.COMMON_ARMOR_ELEMENT_BIN_UPDATE) {
			regist();
		}
	}
	
	private static final String TRUNCATE_QUERY = "truncate table bin_armor_element_common";
	private static final String INSERT_QUERY = "INSERT INTO bin_armor_element_common SET type=?, enchant=?, fr=?, wr=?, ar=?, er=?";
	
	private void regist(){
		try {
			java.util.LinkedList<ArmorElementCommonBinExtend> list = bin.get_enchant();
			Connection con			= null;
			PreparedStatement pstm	= null;
			try {
				con		= L1DatabaseFactory.getInstance().getConnection();
				
				pstm	= con.prepareStatement(TRUNCATE_QUERY);
				pstm.execute();
				SQLUtil.close(pstm);
				
				if (list == null || list.isEmpty()) {
					return;
				}
				
				con.setAutoCommit(false);
				
				pstm	= con.prepareStatement(INSERT_QUERY);
				for (ArmorElementCommonBinExtend extend : list) {
					ArmorElementalEnchantBonus enchant = extend.get_enchant();
					int type = enchant.get_type();
					for (ArmorElementalEnchantBonus.EnchantBonus bonus : enchant.get_enchant_bonus_list()) {
						int idx = 0;
						pstm.setInt(++idx, type);
						pstm.setInt(++idx, bonus.get_enchant());
						pstm.setInt(++idx, bonus.get_fr());
						pstm.setInt(++idx, bonus.get_wr());
						pstm.setInt(++idx, bonus.get_ar());
						pstm.setInt(++idx, bonus.get_er());
						
						pstm.addBatch();
						pstm.clearParameters();
					}
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

			System.out.println("armor_element-common.bin [update completed]. TABLE : bin_armor_element_common");
			
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	public static void reload() {
		_instance.loadFile();
	}
}

