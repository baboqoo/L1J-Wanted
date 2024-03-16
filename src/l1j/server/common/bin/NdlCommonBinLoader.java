package l1j.server.common.bin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.common.DescKLoader;
import l1j.server.common.bin.ndl.CommonNdlInfo;
import l1j.server.common.bin.ndl.NdlCommonBin;
import l1j.server.common.bin.ndl.NdlCommonBin.NdlCommonBinExtend;
import l1j.server.server.utils.SQLUtil;

/**
 * ndl-common.bin 파일 로더
 * @author LinOffice
 */
public class NdlCommonBinLoader {
	private static Logger _log = Logger.getLogger(NdlCommonBinLoader.class.getName());
	private static NdlCommonBin bin;
	
	private static NdlCommonBinLoader _instance;
	public static NdlCommonBinLoader getInstance() {
		if (_instance == null) {
			_instance = new NdlCommonBinLoader();
		}
		return _instance;
	}
	
	public static CommonNdlInfo getNdl(int map_number){
		NdlCommonBinExtend extend = bin.getNdl(map_number);
		return extend == null ? null : extend.get_ndl();
	}

	private NdlCommonBinLoader() {
		loadFile();
	}
	
	private void loadFile(){
		bin = NdlCommonBin.newInstance();
		try {
			bin.readFrom(ProtoInputStream.newInstance("./data/Contents/ndl-common.bin"));// bin 파일을 읽는다
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		if (!bin.isInitialized())
			throw new IllegalArgumentException(String.format("fail initialized info data.(NdlCommonBin) %d", bin.getInitializeBit()));
	
		if (Config.COMMON.COMMON_NDL_BIN_UPDATE) {
			regist();
		}
	}
	
	private static final String TRUNCATE_QUERY = "truncate table bin_ndl_common";
	private static final String INSERT_QUERY = "INSERT INTO bin_ndl_common SET "
			+ "map_number=?, npc_classId=?, npc_desc_kr=?, "
			+ "territory_startXY=?, territory_endXY=?, territory_location_desc=?, "
			+ "territory_average_npc_value=?, territory_average_ac=?, territory_average_level=?, "
			+ "territory_average_wis=?, territory_average_mr=?, territory_average_magic_barrier=?";
	
	private void regist(){
		try {
			HashMap<Integer, NdlCommonBinExtend> ndl_list = bin.get_ndl_list();
			Connection con			= null;
			PreparedStatement pstm	= null;
			try {
				con		= L1DatabaseFactory.getInstance().getConnection();
				
				pstm	= con.prepareStatement(TRUNCATE_QUERY);
				pstm.execute();
				SQLUtil.close(pstm);
				
				if (ndl_list == null || ndl_list.isEmpty()) {
					return;
				}
				
				con.setAutoCommit(false);
				
				pstm	= con.prepareStatement(INSERT_QUERY);
				for (Map.Entry<Integer, NdlCommonBinExtend> entry : ndl_list.entrySet()) {
					NdlCommonBinExtend extend = entry.getValue();
					CommonNdlInfo ndlInfo = extend.get_ndl();
					int mapNumber = ndlInfo.get_map_number();
					for (CommonNdlInfo.NpcListT npcList : ndlInfo.get_maker_list()) {
						int npc_classId = npcList.get_npc_classId();
						String npc_desc_kr = DescKLoader.getDesc(NpcCommonBinLoader.getCommonBinInfo(npc_classId).get_desc());
						for (CommonNdlInfo.TerritoryT territoryT : npcList.get_territory()) {
							int idx = 0;
							pstm.setInt(++idx, mapNumber);
							pstm.setInt(++idx, npc_classId);
							pstm.setString(++idx, npc_desc_kr);
							
							pstm.setInt(++idx, territoryT.get_startXY());
							pstm.setInt(++idx, territoryT.get_endXY());
							pstm.setInt(++idx, territoryT.get_location_desc());
							pstm.setInt(++idx, territoryT.get_average_npc_value());
							
							CommonNdlInfo.AverageNpcInfoT info = territoryT.get_average_npc_info();
							pstm.setInt(++idx, info == null ? 0 : info.get_average_ac());
							pstm.setInt(++idx, info == null ? 0 : info.get_average_level());
							pstm.setInt(++idx, info == null ? 0 : info.get_average_wis());
							pstm.setInt(++idx, info == null ? 0 : info.get_average_mr());
							pstm.setInt(++idx, info == null ? 0 : info.get_average_magic_barrier());
							pstm.addBatch();
							pstm.clearParameters();
						}
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
			System.out.println("ndl-common.bin [update completed]. TABLE : bin_ndl_common");
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	public static void reload() {
		_instance.loadFile();
	}
}

