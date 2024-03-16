package l1j.server.common.bin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.common.bin.indun.IndunCommonBin;
import l1j.server.common.bin.indun.IndunCommonBin.IndunCommonBinExtend;
import l1j.server.common.bin.indun.IndunInfoForClient;
import l1j.server.common.data.eDungeonType;
import l1j.server.server.utils.SQLUtil;

/**
 * indun_info-common.bin 파일 로더
 * @author LinOffice
 */
public class IndunCommonBinLoader {
	private static Logger _log = Logger.getLogger(IndunCommonBinLoader.class.getName());
	private static final HashMap<Integer, IndunInfoForClient> DATA = new HashMap<Integer, IndunInfoForClient>();
	private static IndunCommonBin bin;
	
	private static IndunCommonBinLoader _instance;
	public static IndunCommonBinLoader getInstance() {
		if (_instance == null) {
			_instance = new IndunCommonBinLoader();
		}
		return _instance;
	}
	
	public static IndunInfoForClient getCommonInfo(int mapKind) {
		return DATA.get(mapKind);
	}

	private IndunCommonBinLoader() {
		if (Config.COMMON.COMMON_INDUN_BIN_UPDATE) {
			loadFile();
		} else {
			load();
		}
	}
	
	private void loadFile(){
		bin = IndunCommonBin.newInstance();
		try {
			bin.readFrom(ProtoInputStream.newInstance("./data/Contents/indun_info-common.bin"));// bin 파일을 읽는다
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		if (!bin.isInitialized())
			throw new IllegalArgumentException(String.format("fail initialized info data.(IndunCommonBin) %d", bin.getInitializeBit()));
		
		regist();
	}
	
	private static final String TRUNCATE_QUERY = "truncate table bin_indun_common";
	private static final String INSERT_QUERY = "INSERT INTO bin_indun_common SET mapKind=?, "
			+ "keyItemId=?, minPlayer=?, maxPlayer=?, minAdena=?, maxAdena=?, minLevel=?, "
			+ "bmkeyItemId=?, eventKeyItemId=?, dungeon_type=?, enable_boost_mode=?";
	
	private void regist(){
		try {
			HashMap<Integer, IndunCommonBinExtend> list = bin.get_indun_list();
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
				for (Map.Entry<Integer, IndunCommonBinExtend> entry : list.entrySet()) {
					IndunInfoForClient info = entry.getValue().get_indun();
					DATA.put(entry.getKey(), info);
					int idx = 0;
					pstm.setInt(++idx, entry.getKey());
					pstm.setInt(++idx, info.get_keyItemId());
					pstm.setInt(++idx, info.get_minPlayer());
					pstm.setInt(++idx, info.get_maxPlayer());
					pstm.setInt(++idx, info.get_minAdena());
					pstm.setInt(++idx, info.get_maxAdena());
					pstm.setString(++idx, info.get_minLevel_toString());
					pstm.setInt(++idx, info.get_bmkeyItemId());
					pstm.setInt(++idx, info.get_eventKeyItemId());
					eDungeonType dungeon_type = info.get_dungeon_type();
					pstm.setString(++idx, dungeon_type == null ? "UNDEFINED(0)" : String.format("%s(%d)", dungeon_type.name(), dungeon_type.toInt()));
					pstm.setString(++idx, String.valueOf(info.get_enable_boost_mode()));
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
			System.out.println("indun-common.bin [update completed]. TABLE : bin_indun_common");
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	private void load() {
		IndunCommonBinExtend info	= null;
		Connection con				= null;
		PreparedStatement pstm		= null;
		ResultSet rs				= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM bin_indun_common");
			rs = pstm.executeQuery();
			while (rs.next()) {
				info = new IndunCommonBinExtend(rs);
				DATA.put(info.get_map_kind(), info.get_indun());
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
		if (Config.COMMON.COMMON_INDUN_BIN_UPDATE) {
			_instance.loadFile();
		} else {
			_instance.load();
		}
	}
}

