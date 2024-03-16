package l1j.server.common.bin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.common.StringKLoader;
import l1j.server.common.bin.einhasadpoint.EinhasadPointFaithCommonBin;
import l1j.server.common.bin.einhasadpoint.EinhasadPointFaithT;
import l1j.server.server.utils.SQLUtil;

/**
 * einhasad_point_faith_info-common.bin 파일 로더
 * @author LinOffice
 */
public class EinhasadPointFaithCommonBinLoader {
	private static Logger _log = Logger.getLogger(EinhasadPointFaithCommonBinLoader.class.getName());
	private static EinhasadPointFaithCommonBin bin;
	
	private static EinhasadPointFaithCommonBinLoader _instance;
	public static EinhasadPointFaithCommonBinLoader getInstance() {
		if (_instance == null) {
			_instance = new EinhasadPointFaithCommonBinLoader();
		}
		return _instance;
	}
	
	public static EinhasadPointFaithT.BuffInfoT getBuffInfoT() {
		return bin.get_data().get_info().get_BuffInfo();
	}
	
	public static EinhasadPointFaithT.GroupListT getGroupListT() {
		return bin.get_data().get_info().get_GroupList();
	}
	
	/**
	 * 그룹 조사
	 * @param indexId
	 * @return EinhasadPointFaithT.GroupListT.GroupT
	 */
	public static EinhasadPointFaithT.GroupListT.GroupT getGroupT(int indexId) {
		for (EinhasadPointFaithT.GroupListT.GroupT groupT : bin.get_data().get_info().get_GroupList().get_Group()) {
			for (EinhasadPointFaithT.GroupListT.GroupT.IndexT indexT : groupT.get_index()) {
				if (indexT.get_indexId() == indexId) {
					return groupT;
				}
			}
		}
		return null;
	}
	
	/**
	 * 인덱스 조사
	 * @param indexId
	 * @return EinhasadPointFaithT.GroupListT.GroupT.IndexT
	 */
	public static EinhasadPointFaithT.GroupListT.GroupT.IndexT getIndexT(int indexId) {
		for (EinhasadPointFaithT.GroupListT.GroupT groupT : bin.get_data().get_info().get_GroupList().get_Group()) {
			for (EinhasadPointFaithT.GroupListT.GroupT.IndexT indexT : groupT.get_index()) {
				if (indexT.get_indexId() == indexId) {
					return indexT;
				}
			}
		}
		return null;
	}

	private EinhasadPointFaithCommonBinLoader() {
		loadFile();
	}
	
	private void loadFile(){
		bin = EinhasadPointFaithCommonBin.newInstance();
		try {
			bin.readFrom(ProtoInputStream.newInstance("./data/Contents/einhasad_point_faith_info-common.bin"));// bin 파일을 읽는다
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		if (!bin.isInitialized())
			throw new IllegalArgumentException(String.format("fail initialized info data.(EinhasadPointFaithCommonBin) %d", bin.getInitializeBit()));
		
		if (Config.COMMON.COMMON_EINHASAD_POINT_FAITH_BIN_UPDATE) {
			regist();
		}
	}
	
	private static final String TRUNCATE_QUERY = "truncate table bin_einpoint_faith_common";
	private static final String INSERT_QUERY = "INSERT INTO bin_einpoint_faith_common SET "
			+ "GroupId=?, spellId=?, "
			+ "Index_indexId=?, Index_spellId=?, Index_cost=?, Index_duration=?, Index_additional_desc=?, Index_additional_desc_kr=?, "
			+ "additional_desc=?, additional_desc_kr=?, BuffInfo_tooltipStrId=?, BuffInfo_tooltipStrId_kr=?";
	
	private void regist(){
		try {
			EinhasadPointFaithT faithT = bin.get_data().get_info();
			Connection con			= null;
			PreparedStatement pstm	= null;
			try {
				con		= L1DatabaseFactory.getInstance().getConnection();
				
				pstm	= con.prepareStatement(TRUNCATE_QUERY);
				pstm.execute();
				SQLUtil.close(pstm);
				
				if (faithT == null) {
					return;
				}
				
				con.setAutoCommit(false);
				
				pstm	= con.prepareStatement(INSERT_QUERY);
				int buffInfoTooltipStrId = faithT.get_BuffInfo() == null ? 0 : faithT.get_BuffInfo().get_tooltipStrId();
				for (EinhasadPointFaithT.GroupListT.GroupT groupT : faithT.get_GroupList().get_Group()) {
					int GroupId = groupT.get_GroupId();
					int spellId = groupT.get_spellId();
					int additional_desc = groupT.get_additional_desc();
					for (EinhasadPointFaithT.GroupListT.GroupT.IndexT indexT : groupT.get_index()) {
						int idx = 0;
						pstm.setInt(++idx, GroupId);
						pstm.setInt(++idx, spellId);
						pstm.setInt(++idx, indexT.get_indexId());
						pstm.setInt(++idx, indexT.get_spellId());
						pstm.setInt(++idx, indexT.get_cost());
						pstm.setInt(++idx, indexT.get_duration());
						pstm.setInt(++idx, indexT.get_additional_desc());
						pstm.setString(++idx, indexT.get_additional_desc() == 0 ? null : StringKLoader.getString(indexT.get_additional_desc()));
						pstm.setInt(++idx, additional_desc);
						pstm.setString(++idx, additional_desc == 0 ? null : StringKLoader.getString(additional_desc));
						pstm.setInt(++idx, buffInfoTooltipStrId);
						pstm.setString(++idx, buffInfoTooltipStrId == 0 ? null : StringKLoader.getString(buffInfoTooltipStrId));
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
			System.out.println("einhasad_point_faith_info-common.bin [update completed]. TABLE : bin_einpoint_faith_common");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void reload() {
		_instance.loadFile();
	}
}

