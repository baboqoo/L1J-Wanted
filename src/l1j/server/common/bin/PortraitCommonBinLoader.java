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
import l1j.server.common.bin.portrait.PortraitCommonBin;
import l1j.server.common.bin.portrait.PortraitCommonBin.PortraitCommonBinExtend;
import l1j.server.common.data.PortraitT;
import l1j.server.server.utils.SQLUtil;

/**
 * portrait-common.bin 파일 로더
 * @author LinOffice
 */
public class PortraitCommonBinLoader {
	private static Logger _log = Logger.getLogger(PortraitCommonBinLoader.class.getName());
	private static final HashMap<Integer, PortraitT> DATA = new HashMap<Integer, PortraitT>();
	private static PortraitCommonBin bin;
	
	private static PortraitCommonBinLoader _instance;
	public static PortraitCommonBinLoader getInstance() {
		if (_instance == null) {
			_instance = new PortraitCommonBinLoader();
		}
		return _instance;
	}
	
	public static PortraitT getPortrait(int asset_id){
		return DATA.get(asset_id);
	}

	private PortraitCommonBinLoader() {
		if (Config.COMMON.COMMON_PORTRAIT_BIN_UPDATE) {
			loadFile();
		} else {
			load();
		}
	}
	
	private void loadFile(){
		bin = PortraitCommonBin.newInstance();
		try {
			bin.readFrom(ProtoInputStream.newInstance("./data/Contents/portrait-common.bin"));// bin 파일을 읽는다
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		if (!bin.isInitialized())
			throw new IllegalArgumentException(String.format("fail initialized info data.(PortraitCommonBin) %d", bin.getInitializeBit()));
	
		regist();
	}
	
	private static final String TRUNCATE_QUERY = "truncate table bin_portrait_common";
	private static final String INSERT_QUERY = "INSERT INTO bin_portrait_common SET asset_id=?, desc_id=?, desc_kr=?";
	
	private void regist(){
		try {
			HashMap<Integer, PortraitCommonBinExtend> list = bin.get_portrait_list();
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
				for (PortraitCommonBinExtend extend : list.values()) {
					PortraitT info = extend.get_portrait();
					DATA.put(info.get_AssetID(), info);
					int idx = 0;
					pstm.setInt(++idx, info.get_AssetID());
					pstm.setString(++idx, info.get_Desc());
					pstm.setString(++idx, info.get_Desc() == null ? null : DescKLoader.getDesc(info.get_Desc()));
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
			System.out.println("portrait-common.bin [update completed]. TABLE : bin_portrait_common");
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	private void load() {
		PortraitT info			= null;
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM bin_portrait_common");
			rs = pstm.executeQuery();
			while (rs.next()) {
				info = new PortraitT(rs);
				DATA.put(info.get_AssetID(), info);
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
		if (Config.COMMON.COMMON_PORTRAIT_BIN_UPDATE) {
			_instance.loadFile();
		} else {
			_instance.load();
		}
	}
}

