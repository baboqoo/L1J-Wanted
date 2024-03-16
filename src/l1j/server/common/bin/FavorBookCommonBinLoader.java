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
import l1j.server.common.bin.favorbook.AUBIBookInfoForClient;
import l1j.server.common.bin.favorbook.FavorBookCommonBin;
import l1j.server.server.utils.SQLUtil;

/**
 * favor_book-common.bin 파일 로더
 * @author LinOffice
 */
public class FavorBookCommonBinLoader {
	private static Logger _log = Logger.getLogger(FavorBookCommonBinLoader.class.getName());
	private static FavorBookCommonBin bin;
	
	public static java.util.LinkedList<AUBIBookInfoForClient.BookT.CategoryT> get_categories() {
		return bin.get_extend().get_favor().get_book().get_categories();
	}
	
	private static FavorBookCommonBinLoader _instance;
	public static FavorBookCommonBinLoader getInstance() {
		if (_instance == null) {
			_instance = new FavorBookCommonBinLoader();
		}
		return _instance;
	}

	private FavorBookCommonBinLoader() {
		loadFile();
	}
	
	private void loadFile(){
		bin = FavorBookCommonBin.newInstance();
		try {
			bin.readFrom(ProtoInputStream.newInstance("./data/Contents/favor_book-common.bin"));// bin 파일을 읽는다
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		if (!bin.isInitialized())
			throw new IllegalArgumentException(String.format("fail initialized info data.(FavorBookCommonBin) %d", bin.getInitializeBit()));
		
		if (Config.COMMON.COMMON_FAVOR_BOOK_BIN_UPDATE) {
			regist();
		}
	}
	
	private static final String TRUNCATE_QUERY = "truncate table bin_favorbook_common";
	private static final String INSERT_QUERY = "INSERT INTO bin_favorbook_common SET category_id=?, "
			+ "desc_id=?, desc_kr=?, start_date=?, end_date=?, sort=?, "
			+ "slot_id=?, state_infos=?, red_dot_notice=?, default_display_item_id=?";
	
	private void regist(){
		try {
			java.util.LinkedList<AUBIBookInfoForClient.BookT.CategoryT> categorys = bin.get_extend().get_favor().get_book().get_categories();
			Connection con			= null;
			PreparedStatement pstm	= null;
			try {
				con		= L1DatabaseFactory.getInstance().getConnection();
				
				pstm	= con.prepareStatement(TRUNCATE_QUERY);
				pstm.execute();
				SQLUtil.close(pstm);
				
				if (categorys == null || categorys.isEmpty()) {
					return;
				}
				
				con.setAutoCommit(false);
				
				pstm	= con.prepareStatement(INSERT_QUERY);
				for (AUBIBookInfoForClient.BookT.CategoryT info : categorys) {
					for (AUBIBookInfoForClient.BookT.CategoryT.SlotT slot : info.get_slots().values()) {
						int idx = 0;
						pstm.setInt(++idx, info.get_id());
						pstm.setString(++idx, info.get_desc());
						pstm.setString(++idx, info.get_desc() == null ? null : DescKLoader.getDesc(info.get_desc()));
						pstm.setString(++idx, info.get_start_date());
						pstm.setString(++idx, info.get_end_date());
						pstm.setInt(++idx, info.get_sort());
						pstm.setInt(++idx, slot.get_id());
						pstm.setString(++idx, slot.get_state_infos_toString());
						pstm.setInt(++idx, slot.get_red_dot_notice());
						pstm.setInt(++idx, slot.get_default_display_item_id());
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
			System.out.println("favor_book-common.bin [update completed]. TABLE : bin_favorbook_common");
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	public static void reload() {
		_instance.loadFile();
	}
}

