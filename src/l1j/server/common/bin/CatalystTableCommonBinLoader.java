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
import l1j.server.common.bin.catalyst.CatalystTableCommonBin;
import l1j.server.common.bin.catalyst.CatalystTableT;
import l1j.server.common.bin.item.CommonItemInfo;
import l1j.server.server.utils.SQLUtil;

/**
 * catalyst_table_info-common.bin 파일 로더
 * @author LinOffice
 */
public class CatalystTableCommonBinLoader {
	private static Logger _log = Logger.getLogger(CatalystTableCommonBinLoader.class.getName());
	private static final HashMap<Integer, HashMap<Integer, CatalystTableT.CatalystRewardInfoT>> DATA = new HashMap<>();
	private static CatalystTableCommonBin bin;
	
	private static CatalystTableCommonBinLoader _instance;
	public static CatalystTableCommonBinLoader getInstance() {
		if (_instance == null) {
			_instance = new CatalystTableCommonBinLoader();
		}
		return _instance;
	}
	
	public static boolean isCatalyst(int name_id) {
		return DATA.containsKey(name_id);
	}
	
	public static HashMap<Integer, CatalystTableT.CatalystRewardInfoT> getCatalyst(int name_id) {
		return DATA.get(name_id);
	}
	
	public static CatalystTableT.CatalystRewardInfoT getCatalyst(int name_id, int input) {
		HashMap<Integer, CatalystTableT.CatalystRewardInfoT> map = getCatalyst(name_id);
		if (map == null) {
			return null;
		}
		return map.get(input);
	}

	private CatalystTableCommonBinLoader() {
		if (Config.COMMON.COMMON_CATALYST_BIN_UPDATE) {
			loadFile();
		} else {
			load();
		}
	}
	
	private void loadFile(){
		bin = CatalystTableCommonBin.newInstance();
		try {
			bin.readFrom(ProtoInputStream.newInstance("./data/Contents/catalyst_table_info-common.bin"));// bin 파일을 읽는다
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		if (!bin.isInitialized())
			throw new IllegalArgumentException(String.format("fail initialized info data.(CatalystTableCommonBin) %d", bin.getInitializeBit()));
		
		regist();
	}
	
	private static final String TRUNCATE_QUERY = "truncate table bin_catalyst_common";
	private static final String INSERT_QUERY = "INSERT INTO bin_catalyst_common SET nameId=?, nameId_kr=?, "
			+ "input=?, input_kr=?, output=?, output_kr=?, successProb=?, rewardCount=?, preserveProb=?, failOutput=?, failOutput_kr=? "
			+ "ON DUPLICATE KEY UPDATE successProb=?, rewardCount=?, preserveProb=?";
	
	private void regist(){
		try {
			java.util.LinkedList<CatalystTableT.CatalystRewardInfoT> list = bin.get_catalyst().get_catalyst().get_rewardList();
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
				
				CommonItemInfo itemBin = null;
				String nameId_kr = null;
				String input_kr = null;
				String output_kr = null;
				String failOutput_kr = null;
				pstm	= con.prepareStatement(INSERT_QUERY);
				for (CatalystTableT.CatalystRewardInfoT info : list) {
					HashMap<Integer, CatalystTableT.CatalystRewardInfoT> map = DATA.get(info.get_nameId());
					if (map == null) {
						map = new HashMap<>();
						DATA.put(info.get_nameId(), map);
					}
					map.put(info.get_input(), info);
					
					int idx = 0;
					pstm.setInt(++idx, info.get_nameId());
					itemBin = ItemCommonBinLoader.getCommonInfo(info.get_nameId());
					if (itemBin != null) {
						nameId_kr = DescKLoader.getDesc(itemBin.get_desc());
					}
					pstm.setString(++idx, nameId_kr);
					pstm.setInt(++idx, info.get_input());
					itemBin = ItemCommonBinLoader.getCommonInfo(info.get_input());
					if (itemBin != null) {
						input_kr = DescKLoader.getDesc(itemBin.get_desc());
					}
					pstm.setString(++idx, input_kr);
					pstm.setInt(++idx, info.get_output());
					itemBin = ItemCommonBinLoader.getCommonInfo(info.get_output());
					if (itemBin != null) {
						output_kr = DescKLoader.getDesc(itemBin.get_desc());
					}
					pstm.setString(++idx, output_kr);
					pstm.setInt(++idx, info.get_successProb());
					pstm.setInt(++idx, info.get_rewardCount());
					pstm.setInt(++idx, info.get_preserveProb());
					pstm.setInt(++idx, info.get_failOutput());
					itemBin = ItemCommonBinLoader.getCommonInfo(info.get_failOutput());
					if (itemBin != null) {
						failOutput_kr = DescKLoader.getDesc(itemBin.get_desc());
					}
					pstm.setString(++idx, failOutput_kr);
					pstm.setInt(++idx, info.get_successProb());
					pstm.setInt(++idx, info.get_rewardCount());
					pstm.setInt(++idx, info.get_preserveProb());
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
			System.out.println("catalyst_table_info-common.bin [update completed]. TABLE : bin_catalyst_common");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void load() {
		CatalystTableT.CatalystRewardInfoT info	= null;
		Connection con				= null;
		PreparedStatement pstm		= null;
		ResultSet rs				= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM bin_catalyst_common");
			rs = pstm.executeQuery();
			while (rs.next()) {
				info = new CatalystTableT.CatalystRewardInfoT(rs);
				HashMap<Integer, CatalystTableT.CatalystRewardInfoT> map = DATA.get(info.get_nameId());
				if (map == null) {
					map = new HashMap<>();
					DATA.put(info.get_nameId(), map);
				}
				map.put(info.get_input(), info);
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
		if (Config.COMMON.COMMON_CATALYST_BIN_UPDATE) {
			_instance.loadFile();
		} else {
			_instance.load();
		}
	}
}

