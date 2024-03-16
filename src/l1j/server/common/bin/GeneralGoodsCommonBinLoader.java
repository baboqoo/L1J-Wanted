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
import l1j.server.common.DescKLoader;
import l1j.server.common.bin.generalgoods.GeneralGoodsCommonBin;
import l1j.server.common.bin.generalgoods.GeneralGoodsCommonBin.GeneralGoodsCommonBinExtend;
import l1j.server.common.bin.generalgoods.GeneralGoodsList;
import l1j.server.server.utils.SQLUtil;

/**
 * general_goods-common.bin 파일 로더
 * @author LinOffice
 */
public class GeneralGoodsCommonBinLoader {
	private static Logger _log = Logger.getLogger(GeneralGoodsCommonBinLoader.class.getName());
	private static GeneralGoodsCommonBin bin;
	
	private static GeneralGoodsCommonBinLoader _instance;
	public static GeneralGoodsCommonBinLoader getInstance() {
		if (_instance == null) {
			_instance = new GeneralGoodsCommonBinLoader();
		}
		return _instance;
	}

	private GeneralGoodsCommonBinLoader() {
		if (Config.COMMON.COMMON_GENERAL_GOODS_BIN_UPDATE) {
			loadFile();
		} else {
			load();
		}
	}
	
	private void loadFile(){
		bin = GeneralGoodsCommonBin.newInstance();
		try {
			bin.readFrom(ProtoInputStream.newInstance("./data/Contents/general_goods-common.bin"));// bin 파일을 읽는다
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		if (!bin.isInitialized())
			throw new IllegalArgumentException(String.format("fail initialized info data.(GeneralGoodsCommonBin) %d", bin.getInitializeBit()));
		
		regist();
	}
	
	private static final String TRUNCATE_QUERY = "truncate table bin_general_goods_common";
	private static final String INSERT_QUERY = "INSERT INTO bin_general_goods_common SET NameId=?, NameId_kr=?";
	
	private void regist(){
		try {
			java.util.LinkedList<Integer> list = bin.get_extend().get_list().get_NameId();
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
				for (int nameId : list) {
					pstm.setInt(1, nameId);
					pstm.setString(2, DescKLoader.getDesc(ItemCommonBinLoader.getCommonInfo(nameId).get_desc()));
					
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
			System.out.println("general_goods-common.bin [update completed]. TABLE : bin_general_goods_common");
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	private void load() {
		GeneralGoodsCommonBinExtend extend = GeneralGoodsCommonBinExtend.newInstance();
		GeneralGoodsList list = GeneralGoodsList.newInstance();
		extend.set_list(list);
		bin = GeneralGoodsCommonBin.newInstance();
		bin.set_extend(extend);
		
		Connection con				= null;
		PreparedStatement pstm		= null;
		ResultSet rs				= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT NameId FROM bin_general_goods_common");
			rs = pstm.executeQuery();
			while (rs.next()) {
				list.add_NameId(rs.getInt("NameId"));
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "GeneralGoodsCommonBinLoader[]Error", e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, "GeneralGoodsCommonBinLoader[]Error", e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public static void reload() {
		if (Config.COMMON.COMMON_GENERAL_GOODS_BIN_UPDATE) {
			_instance.loadFile();
		} else {
			_instance.load();
		}
	}
}

