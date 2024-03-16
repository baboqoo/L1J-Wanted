package l1j.server.common.bin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.common.bin.chargedtimemap.ChargedTimeMapCommonBin;
import l1j.server.common.bin.chargedtimemap.ChargedTimeMapDataT;
import l1j.server.server.utils.SQLUtil;

/**
 * charged_time_map-common.bin 파일 로더
 * @author LinOffice
 */
public class ChargedTimeMapCommonBinLoader {
	private static Logger _log = Logger.getLogger(ChargedTimeMapCommonBinLoader.class.getName());
	private static ChargedTimeMapCommonBin bin;
	
	private static ChargedTimeMapCommonBinLoader _instance;
	public static ChargedTimeMapCommonBinLoader getInstance() {
		if (_instance == null) {
			_instance = new ChargedTimeMapCommonBinLoader();
		}
		return _instance;
	}
	
	public static ChargedTimeMapDataT get_data() {
		return bin.get_extend().get(0).get_data();
	}

	private ChargedTimeMapCommonBinLoader() {
		loadFile();
	}
	
	private void loadFile(){
		bin = ChargedTimeMapCommonBin.newInstance();
		try {
			bin.readFrom(ProtoInputStream.newInstance("./data/Contents/charged_time_map-common.bin"));// bin 파일을 읽는다
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		if (!bin.isInitialized())
			throw new IllegalArgumentException(String.format("fail initialized info data.(ChargedTimeMapCommonBin) %d", bin.getInitializeBit()));
		
		if (Config.COMMON.COMMON_CHARGED_TIME_MAP_BIN_UPDATE) {
			regist();
		}
	}
	
	private static final String TRUNCATE_QUERY = "truncate table bin_charged_time_map_common";
	private static final String INSERT_QUERY = "INSERT INTO bin_charged_time_map_common SET id=?, groups=?, multi_group_list=?";
	
	private void regist(){
		try {
			ChargedTimeMapCommonBin list = bin;
			Connection con			= null;
			PreparedStatement pstm	= null;
			try {
				con		= L1DatabaseFactory.getInstance().getConnection();
				
				pstm	= con.prepareStatement(TRUNCATE_QUERY);
				pstm.execute();
				SQLUtil.close(pstm);
				
				con.setAutoCommit(false);
				
				pstm	= con.prepareStatement(INSERT_QUERY);
				for (ChargedTimeMapCommonBin.ChargedTimeMapCommonBinExtend extend : list.get_extend().values()) {
					ChargedTimeMapDataT dataT = extend.get_data();

					int idx = 0;
					pstm.setInt(++idx, extend.get_id());
					pstm.setString(++idx, dataT.get_groups_toString());
					pstm.setString(++idx, dataT.get_multi_group_list_toString());
					
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

			System.out.println("charged_time_map-common.bin [update completed]. TABLE : bin_charged_time_map_common");
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	public static void reload() {
		_instance.loadFile();
	}
}

