package l1j.server.common.bin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.common.bin.entermap.EnterMapTAG;
import l1j.server.common.bin.entermap.EnterMapsCommonBin;
import l1j.server.common.bin.entermap.EnterMapsCommonBin.EnterMapsCommonBinExtend;
import l1j.server.server.utils.SQLUtil;

/**
 * entermaps-common.bin 파일 로더
 * @author LinOffice
 */
public class EnterMapsCommonBinLoader {
	private static Logger _log = Logger.getLogger(EnterMapsCommonBinLoader.class.getName());
	private static EnterMapsCommonBin bin;
	
	private static EnterMapsCommonBinLoader _instance;
	public static EnterMapsCommonBinLoader getInstance() {
		if (_instance == null) {
			_instance = new EnterMapsCommonBinLoader();
		}
		return _instance;
	}
	
	public static EnterMapTAG getEnterMap(int mapId) {
		EnterMapsCommonBinExtend extend = bin.get_MapData(mapId);
		return extend == null ? null : extend.get_MapData();
	}

	private EnterMapsCommonBinLoader() {
		if (Config.COMMON.COMMON_ENTER_MAPS_BIN_UPDATE) {
			loadFile();
		}
	}
	
	private void loadFile(){
		bin = EnterMapsCommonBin.newInstance();
		try {
			bin.readFrom(ProtoInputStream.newInstance("./data/Contents/entermaps-common.bin"));// bin 파일을 읽는다
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		if (!bin.isInitialized())
			throw new IllegalArgumentException(String.format("fail initialized info data.(EnterMapsCommonBin) %d", bin.getInitializeBit()));
	
		regist();
	}
	
	private static final String TRUNCATE_QUERY = "truncate table bin_entermaps_common";
	private static final String INSERT_QUERY = "INSERT INTO bin_entermaps_common SET id=?, "
			+ "action_name=?, number_id=?, loc_x=?, loc_y=?, loc_range=?, "
			+ "priority_id=?, maxUser=?, conditions=?, destinations=? "
			+ "ON DUPLICATE KEY UPDATE "
			+ "number_id=?, loc_x=?, loc_y=?, loc_range=?, priority_id=?, maxUser=?, conditions=?, destinations=?";
	
	private void regist(){
		try {
			HashMap<Integer, EnterMapsCommonBinExtend> list = bin.get_MapData();
			
			int regiCnt = 1, limitCnt = 0;
			HashMap<Integer, ArrayList<EnterMapsCommonBinExtend>> regiMap = new HashMap<Integer, ArrayList<EnterMapsCommonBinExtend>>();
			for (EnterMapsCommonBinExtend info : list.values()) {
				if (limitCnt >= 500) {
					limitCnt = 0;
					regiCnt++;
				}
				ArrayList<EnterMapsCommonBinExtend> regilist = regiMap.get(regiCnt);
				if (regilist == null) {
					regilist = new ArrayList<EnterMapsCommonBinExtend>();
					regiMap.put(regiCnt, regilist);
				}
				regilist.add(info);
				limitCnt++;
			}
			
			Connection con			= null;
			PreparedStatement pstm	= null;
			try {
				con		= L1DatabaseFactory.getInstance().getConnection();
				pstm	= con.prepareStatement(TRUNCATE_QUERY);
				pstm.execute();
			} catch(SQLException e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} finally {
				SQLUtil.close(pstm, con);
			}
			
			for (ArrayList<EnterMapsCommonBinExtend> regiList : regiMap.values()) {
				if (regiList == null || regiList.isEmpty()) {
					continue;
				}
				
				try {
					con		= L1DatabaseFactory.getInstance().getConnection();
					con.setAutoCommit(false);
					
					pstm	= con.prepareStatement(INSERT_QUERY);
					for (EnterMapsCommonBinExtend info : list.values()) {
						for (EnterMapTAG.MapDataTAG tag : info.get_MapData().get_MapData()) {
							int idx = 0;
							String conditions = tag.get_Conditions_toString();
							String destinations = tag.get_Destinations_toString();
							pstm.setInt(++idx, info.get_id());
							pstm.setString(++idx, tag.get_Action());
							pstm.setInt(++idx, tag.get_Number());
							pstm.setInt(++idx, tag.get_X());
							pstm.setInt(++idx, tag.get_Y());
							pstm.setInt(++idx, tag.get_Range());
							pstm.setInt(++idx, tag.get_Priority() == null ? 1 : tag.get_Priority().toInt());
							pstm.setInt(++idx, tag.get_MaxUser());
							pstm.setString(++idx, conditions);
							pstm.setString(++idx, destinations);
							pstm.setInt(++idx, tag.get_Number());
							pstm.setInt(++idx, tag.get_X());
							pstm.setInt(++idx, tag.get_Y());
							pstm.setInt(++idx, tag.get_Range());
							pstm.setInt(++idx, tag.get_Priority() == null ? 1 : tag.get_Priority().toInt());
							pstm.setInt(++idx, tag.get_MaxUser());
							pstm.setString(++idx, conditions);
							pstm.setString(++idx, destinations);
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
			}
			System.out.println("entermaps-common.bin [update completed]. TABLE : bin_entermaps_common");
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
	}
	
	public static void reload() {
		if (Config.COMMON.COMMON_ENTER_MAPS_BIN_UPDATE) {
			_instance.loadFile();
		}
	}
}

