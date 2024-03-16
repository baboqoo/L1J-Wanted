package l1j.server.common.bin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.common.bin.pcmaster.PCMasterCommonBin;
import l1j.server.common.bin.pcmaster.PCMasterInfoForClient;
import l1j.server.server.utils.SQLUtil;

/**
 * pc_master-common.bin 파일 로더
 * @author LinOffice
 */
public class PCMasterCommonBinLoader {
	private static Logger _log = Logger.getLogger(PCMasterCommonBinLoader.class.getName());
	private static PCMasterCommonBin bin;
	private static PCMasterInfoForClient MASTER;
	
	private static PCMasterCommonBinLoader _instance;
	public static PCMasterCommonBinLoader getInstance() {
		if (_instance == null) {
			_instance = new PCMasterCommonBinLoader();
		}
		return _instance;
	}
	
	public static PCMasterInfoForClient getData() {
		return MASTER;
	}

	private PCMasterCommonBinLoader() {
		loadFile();
	}
	
	private void loadFile(){
		bin = PCMasterCommonBin.newInstance();
		try {
			bin.readFrom(ProtoInputStream.newInstance("./data/Contents/pc_master-common.bin"));// bin 파일을 읽는다
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		if (!bin.isInitialized())
			throw new IllegalArgumentException(String.format("fail initialized info data.(PCMasterCommonBin) %d", bin.getInitializeBit()));
		
		MASTER = bin.get_pc_master().get_info();
		if (Config.COMMON.COMMON_PC_MASTER_BIN_UPDATE) {
			regist();
		}
	}
	
	private static final String TRUNCATE_QUERY = "truncate table bin_pc_master_common";
	private static final String INSERT_QUERY = "INSERT INTO bin_pc_master_common SET utilities=?, pc_bonus_map_infos=?, "
			+ "notification=?, buff_group=?, buff_bonus=?";
	
	private void regist(){
		try {
			Connection con			= null;
			PreparedStatement pstm	= null;
			try {
				con		= L1DatabaseFactory.getInstance().getConnection();
				
				pstm	= con.prepareStatement(TRUNCATE_QUERY);
				pstm.execute();
				SQLUtil.close(pstm);
				
				if (MASTER == null) {
					return;
				}
				
				String utilities			= MASTER.get_utilities_toString();
				String pc_bonus_map_infos	= MASTER.get_pc_bonus_map_infos_toString();
				String notification			= MASTER.get_notification_toString();
				String buff_group			= MASTER.get_buff_group_toString();
				String buff_bonus			= MASTER.get_buff_bonus_toString();
				
				pstm	= con.prepareStatement(INSERT_QUERY);
				pstm.setString(1, utilities);
				pstm.setString(2, pc_bonus_map_infos);
				pstm.setString(3, notification);
				pstm.setString(4, buff_group);
				pstm.setString(5, buff_bonus);
				pstm.execute();
			} catch(SQLException e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} catch(Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} finally {
				SQLUtil.close(pstm, con);
			}
			System.out.println("pc_master-common.bin [update completed]. TABLE : bin_pc_master_common");
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	public static void reload() {
		_instance.loadFile();
	}
}

