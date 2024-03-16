package l1j.server.common.bin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.common.DescKLoader;
import l1j.server.common.bin.enchant.EnchantScrollTableInfoCommonBin;
import l1j.server.common.bin.enchant.EnchantScrollTableT;
import l1j.server.server.utils.SQLUtil;

/**
 * enchant_scroll_table_info-common.bin 파일 로더
 * @author LinOffice
 */
public class EnchantScrollTableInfoCommonBinLoader {
	private static Logger _log = Logger.getLogger(EnchantScrollTableInfoCommonBinLoader.class.getName());
	private static EnchantScrollTableInfoCommonBin bin;
	
	private static EnchantScrollTableInfoCommonBinLoader _instance;
	public static EnchantScrollTableInfoCommonBinLoader getInstance() {
		if (_instance == null) {
			_instance = new EnchantScrollTableInfoCommonBinLoader();
		}
		return _instance;
	}
	
	public static EnchantScrollTableT.EnchantScrollTypeListT.EnchantScrollT getScrollT(int nameId) {
		for (EnchantScrollTableT.EnchantScrollTypeListT listT : bin.get_extend().getEnchantScrollT().get_enchatScrollTypeList()) {
			for (EnchantScrollTableT.EnchantScrollTypeListT.EnchantScrollT scrollT : listT.get_scrollList()) {
				if (scrollT.get_nameid() == nameId) {
					return scrollT;
				}
			}
		}
		return null;
	}

	private EnchantScrollTableInfoCommonBinLoader() {
		loadFile();
	}
	
	private void loadFile(){
		bin = EnchantScrollTableInfoCommonBin.newInstance();
		try {
			bin.readFrom(ProtoInputStream.newInstance("./data/Contents/enchant_scroll_table_info-common.bin"));// bin 파일을 읽는다
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		if (!bin.isInitialized())
			throw new IllegalArgumentException(String.format("fail initialized info data.(EnchantScrollTableInfoCommonBin) %d", bin.getInitializeBit()));
	
		if (Config.COMMON.COMMON_ENCHANT_SCROLL_BIN_UPDATE) {
			regist();
		}
	}
	
	private static final String TRUNCATE_QUERY = "truncate table bin_enchant_scroll_table_common";
	private static final String INSERT_QUERY = "INSERT INTO bin_enchant_scroll_table_common SET "
			+ "enchantType=?, nameid=?, desc_kr=?, targetEnchant=?, noTargetMaterialList=?, "
			+ "target_category=?, isBmEnchantScroll=?, elementalType=?, useBlesscodeScroll=?";
	
	private void regist(){
		try {
			java.util.LinkedList<EnchantScrollTableT.EnchantScrollTypeListT> list = bin.get_extend().getEnchantScrollT().get_enchatScrollTypeList();
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
				for (EnchantScrollTableT.EnchantScrollTypeListT listT : list) {
					int enchant_type = listT.get_enchantType();
					java.util.LinkedList<EnchantScrollTableT.EnchantScrollTypeListT.EnchantScrollT> scrollList = listT.get_scrollList();
					
					for (EnchantScrollTableT.EnchantScrollTypeListT.EnchantScrollT scrollT : scrollList) {
						int idx = 0;
						pstm.setInt(++idx, enchant_type);
						pstm.setInt(++idx, scrollT.get_nameid());
						pstm.setString(++idx, DescKLoader.getDesc(ItemCommonBinLoader.getCommonInfo(scrollT.get_nameid()).get_desc()));
						pstm.setInt(++idx, scrollT.get_targetEnchant());
						pstm.setString(++idx, scrollT.get_noTargetMaterialList_toString());
						pstm.setString(++idx, scrollT.get_target_category_toString());
						pstm.setString(++idx, String.valueOf(scrollT.get_isBmEnchantScroll()));
						pstm.setInt(++idx, scrollT.get_elementalType());
						pstm.setInt(++idx, scrollT.get_useBlesscodeScroll());
						
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

			System.out.println("enchant_scroll_table_info-common.bin [update completed]. TABLE : bin_enchant_scroll_table_common");
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	public static void reload() {
		_instance.loadFile();
	}
}

