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
import l1j.server.common.bin.spell.CommonSpellInfo;
import l1j.server.common.bin.spell.CommonSpellInfo.ExtractItem;
import l1j.server.common.bin.spell.PassiveSpellCommonBin;
import l1j.server.common.bin.spell.SpellCommonBinExtend;
import l1j.server.server.utils.SQLUtil;

/**
 * passiveSpell-common.bin 파일 로더
 * @author LinOffice
 */
public class PassiveSpellCommonBinLoader {
	private static Logger _log = Logger.getLogger(PassiveSpellCommonBinLoader.class.getName());
	private static final HashMap<Integer, CommonSpellInfo> DATA = new HashMap<>();
	private static PassiveSpellCommonBin bin;
	
	private static PassiveSpellCommonBinLoader _instance;
	public static PassiveSpellCommonBinLoader getInstance() {
		if (_instance == null) {
			_instance = new PassiveSpellCommonBinLoader();
		}
		return _instance;
	}
	
	public static CommonSpellInfo getData(int passiveId) {
		return DATA.get(passiveId);
	}

	private PassiveSpellCommonBinLoader() {
		if (Config.COMMON.COMMON_PASSIVE_SPELL_BIN_UPDATE) {
			loadFile();
		} else {
			load();
		}
	}
	
	private void loadFile(){
		bin = PassiveSpellCommonBin.newInstance();
		try {
			bin.readFrom(ProtoInputStream.newInstance("./data/Contents/passiveSpell-common.bin"));// bin 파일을 읽는다
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		if (!bin.isInitialized())
			throw new IllegalArgumentException(String.format("fail initialized info data.(PassiveSpellCommonBin) %d", bin.getInitializeBit()));
		
		regist();
	}
	
	private static final String TRUNCATE_QUERY = "truncate table bin_passivespell_common";
	private static final String INSERT_QUERY = "INSERT INTO bin_passivespell_common SET passive_id=?, "
			+ "duration=?, spell_bonus_list=?, delay_group_id=?, extract_item_name_id=?, extract_item_count=?";
	
	private void regist(){
		try {
			HashMap<Integer, SpellCommonBinExtend> list = bin.get_spell_list();
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
				for (SpellCommonBinExtend extend : list.values()) {
					CommonSpellInfo info = extend.get_spell();
					DATA.put(info.get_spell_id(), info);
					int idx = 0;
					pstm.setInt(++idx, info.get_spell_id());
					pstm.setInt(++idx, info.get_duration());
					pstm.setString(++idx, info.get_spell_bonus_list_toString());
					pstm.setInt(++idx, info.get_delay_group_id());
					ExtractItem extractItem = info.get_extract_item();
					pstm.setInt(++idx, extractItem == null ? 0 : extractItem.get_name_id());
					pstm.setInt(++idx, extractItem == null ? 0 : extractItem.get_count());
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
			System.out.println("passivespell-common.bin [update completed]. TABLE : bin_passivespell_common");
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	private void load() {
		CommonSpellInfo info		= null;
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM bin_passivespell_common");
			rs = pstm.executeQuery();
			while (rs.next()) {
				info = new CommonSpellInfo(rs, true);
				DATA.put(info.get_spell_id(), info);
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
		if (Config.COMMON.COMMON_PASSIVE_SPELL_BIN_UPDATE) {
			_instance.loadFile();
		} else {
			_instance.load();
		}
	}
}

