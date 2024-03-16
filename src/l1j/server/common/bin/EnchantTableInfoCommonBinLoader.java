package l1j.server.common.bin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.common.bin.enchant.AccessoryEnchantTableT;
import l1j.server.common.bin.enchant.EnchantTableCommonBin;
import l1j.server.server.utils.SQLUtil;

/**
 * enchant_table_info-common.bin 파일 로더
 * @author LinOffice
 */
public class EnchantTableInfoCommonBinLoader {
	private static Logger _log = Logger.getLogger(EnchantTableInfoCommonBinLoader.class.getName());
	private static EnchantTableCommonBin bin;
	
	private static EnchantTableInfoCommonBinLoader _instance;
	public static EnchantTableInfoCommonBinLoader getInstance() {
		if (_instance == null) {
			_instance = new EnchantTableInfoCommonBinLoader();
		}
		return _instance;
	}

	private EnchantTableInfoCommonBinLoader() {
		loadFile();
	}
	
	private void loadFile(){
		bin = EnchantTableCommonBin.newInstance();
		try {
			bin.readFrom(ProtoInputStream.newInstance("./data/Contents/enchant_table_info-common.bin"));// bin 파일을 읽는다
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		if (!bin.isInitialized())
			throw new IllegalArgumentException(String.format("fail initialized info data.(EnchantTableCommonBin) %d", bin.getInitializeBit()));
		
		if (Config.COMMON.COMMON_ENCHANT_TABLE_INFO_BIN_UPDATE) {
			regist();
		}
	}
	
	private static final String TRUNCATE_QUERY = "truncate table bin_enchant_table_common";
	private static final String INSERT_QUERY = "INSERT INTO bin_enchant_table_common SET "
			+ "item_index=?, bonusLevel_index=?, enchantSuccessProb=?, enchantTotalProb=?, "
			+ "bmEnchantSuccessProb=?, bmEnchantRemainProb=?, bmEnchantFailDownProb=?, bmEnchantTotalProb=?";
	
	private void regist(){
		try {
			java.util.LinkedList<AccessoryEnchantTableT.AccessoryEnchantItemT> list = bin.get_enchant().get_enchant().get_EnchantItems();
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
				int itemIndex = 0;
				for (AccessoryEnchantTableT.AccessoryEnchantItemT itemT : list) {
					java.util.LinkedList<AccessoryEnchantTableT.AccessoryEnchantItemT.AccessoryEnchantBonusT> levels = itemT.get_EnchantBonusLevels();
					++itemIndex;
					int levelIndex = 0;
					for (AccessoryEnchantTableT.AccessoryEnchantItemT.AccessoryEnchantBonusT bonusT : levels) {
						int idx = 0;
						pstm.setInt(++idx, itemIndex);
						pstm.setInt(++idx, ++levelIndex);
						pstm.setInt(++idx, bonusT.get_enchantSuccessProb());
						pstm.setInt(++idx, bonusT.get_enchantTotalProb());
						pstm.setInt(++idx, bonusT.get_bmEnchantSuccessProb());
						pstm.setInt(++idx, bonusT.get_bmEnchantRemainProb());
						pstm.setInt(++idx, bonusT.get_bmEnchantFailDownProb());
						pstm.setInt(++idx, bonusT.get_bmEnchantTotalProb());
						
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

			System.out.println("enchant_table_info-common.bin [update completed]. TABLE : bin_enchant_table_common");
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	public static void reload() {
		_instance.loadFile();
	}
}

