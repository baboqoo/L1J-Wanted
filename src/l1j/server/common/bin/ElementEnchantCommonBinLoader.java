package l1j.server.common.bin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.common.bin.enchant.ElementEnchantCommonBin;
import l1j.server.common.bin.enchant.ElementEnchantTableT;
import l1j.server.server.utils.SQLUtil;

/**
 * elemnet_enchant_info-common.bin 파일 로더
 * @author LinOffice
 */
public class ElementEnchantCommonBinLoader {
	private static Logger _log = Logger.getLogger(ElementEnchantCommonBinLoader.class.getName());
	private static final java.util.LinkedList<ElementEnchantTableT.ElementalEnchantProbT> DATA = new java.util.LinkedList<ElementEnchantTableT.ElementalEnchantProbT>();
	private static ElementEnchantCommonBin bin;
	
	private static ElementEnchantCommonBinLoader _instance;
	public static ElementEnchantCommonBinLoader getInstance() {
		if (_instance == null) {
			_instance = new ElementEnchantCommonBinLoader();
		}
		return _instance;
	}

	private ElementEnchantCommonBinLoader() {
		if (Config.COMMON.COMMON_ELEMENT_ENCHANT_BIN_UPDATE) {
			loadFile();
		} else {
			load();
		}
	}
	
	private void loadFile(){
		bin = ElementEnchantCommonBin.newInstance();
		try {
			bin.readFrom(ProtoInputStream.newInstance("./data/Contents/elemnet_enchant_info-common.bin"));// bin 파일을 읽는다
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		if (!bin.isInitialized())
			throw new IllegalArgumentException(String.format("fail initialized info data.(ElementEnchantCommonBin) %d", bin.getInitializeBit()));
		
		regist();
	}
	
	private static final String TRUNCATE_QUERY = "truncate table bin_element_enchant_common";
	private static final String INSERT_QUERY = "INSERT INTO bin_element_enchant_common SET "
			+ "prob_index=?, type_index=?, level=?, increaseProb=?, decreaseProb=?";
	
	private void regist(){
		try {
			java.util.LinkedList<ElementEnchantTableT.ElementalEnchantProbT> list = bin.get_enchant().get_enchant().get_probs();
			Connection con			= null;
			PreparedStatement pstm	= null;
			try {
				con		= L1DatabaseFactory.getInstance().getConnection();
				
				pstm	= con.prepareStatement(TRUNCATE_QUERY);
				pstm.execute();
				SQLUtil.close(pstm);
				
				con.setAutoCommit(false);
				
				int probIndex = 0;
				pstm	= con.prepareStatement(INSERT_QUERY);
				for (ElementEnchantTableT.ElementalEnchantProbT info : list) {
					DATA.add(info);
					int typeIndex = 0;
					for (ElementEnchantTableT.ElementalEnchantProbT.ElementEnchantTypeT types : info.get_Types()) {
						int levelIndex = 0;
						for (ElementEnchantTableT.ElementalEnchantProbT.ElementEnchantTypeT.ElementEnchantLevelT level : types.get_Levels()) {
							int idx = 0;
							pstm.setInt(++idx, probIndex);
							pstm.setInt(++idx, typeIndex);
							pstm.setInt(++idx, levelIndex++);
							pstm.setInt(++idx, level.get_IncreaseProb());
							pstm.setInt(++idx, level.get_DecreaseProb());
							
							pstm.addBatch();
							pstm.clearParameters();
						}
						typeIndex++;
					}
					probIndex++;
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

			System.out.println("elemnet_enchant_info-common.bin [update completed]. TABLE : bin_element_enchant_common");
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	private void load() {
		ElementEnchantTableT.ElementalEnchantProbT probs																	= null;
		ElementEnchantTableT.ElementalEnchantProbT.ElementEnchantTypeT type													= null;
		ElementEnchantTableT.ElementalEnchantProbT.ElementEnchantTypeT.ElementEnchantLevelT levelT							= null;
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM bin_element_enchant_common");
			rs = pstm.executeQuery();
			while (rs.next()) {
				int prob_index		= rs.getInt("prob_index");
				int type_index		= rs.getInt("type_index");
				int increaseProb	= rs.getInt("increaseProb");
				int decreaseProb	= rs.getInt("decreaseProb");
				
				if (DATA.isEmpty() || DATA.size() != prob_index) {
					probs = ElementEnchantTableT.ElementalEnchantProbT.newInstance();
					DATA.add(probs);
				}
				
				if (probs.get_Types() == null || probs.get_Types().size() != type_index) {
					type = ElementEnchantTableT.ElementalEnchantProbT.ElementEnchantTypeT.newInstance();
					probs.add_Types(type);
				}
				
				levelT = ElementEnchantTableT.ElementalEnchantProbT.ElementEnchantTypeT.ElementEnchantLevelT.newInstance();
				levelT.set_IncreaseProb(increaseProb);
				levelT.set_DecreaseProb(decreaseProb);
				type.add_Levels(levelT);
				
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
		DATA.clear();
		if (Config.COMMON.COMMON_ELEMENT_ENCHANT_BIN_UPDATE) {
			_instance.loadFile();
		} else {
			_instance.load();
		}
	}
}

