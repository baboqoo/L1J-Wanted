package l1j.server.common.bin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.common.StringKLoader;
import l1j.server.common.bin.spell.CommonSpellInfo;
import l1j.server.common.bin.spell.CommonSpellInfo.ExtractItem;
import l1j.server.common.bin.spell.CompanionSpellBuff;
import l1j.server.common.bin.spell.SpellCommonBin;
import l1j.server.common.bin.spell.SpellCommonBinExtend;
import l1j.server.common.data.SpellCategory;
import l1j.server.server.utils.SQLUtil;

/**
 * spell-common.bin 파일 로더
 * @author LinOffice
 */
public class SpellCommonBinLoader {
	private static Logger _log = Logger.getLogger(SpellCommonBinLoader.class.getName());
	private static final HashMap<Integer, CommonSpellInfo> DATA = new HashMap<Integer, CommonSpellInfo>();
	private static SpellCommonBin bin;
	
	private static SpellCommonBinLoader _instance;
	public static SpellCommonBinLoader getInstance() {
		if (_instance == null) {
			_instance = new SpellCommonBinLoader();
		}
		return _instance;
	}
	
	public static CommonSpellInfo getData(int spellId) {
		return DATA.get(spellId);
	}

	private SpellCommonBinLoader() {
		if (Config.COMMON.COMMON_SPELL_BIN_UPDATE) {
			loadFile();
		} else {
			load();
		}
	}
	
	private void loadFile(){
		bin = SpellCommonBin.newInstance();
		try {
			bin.readFrom(ProtoInputStream.newInstance("./data/Contents/spell-common.bin"));// bin 파일을 읽는다
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		if (!bin.isInitialized())
			throw new IllegalArgumentException(String.format("fail initialized info data.(SpellCommonBin) %d", bin.getInitializeBit()));
		
		regist();
	}
	
	private static final String TRUNCATE_QUERY = "truncate table bin_spell_common";
	private static final String INSERT_QUERY = "INSERT INTO bin_spell_common SET spell_id=?, "
			+ "spell_category=?, on_icon_id=?, off_icon_id=?, duration=?, tooltip_str_id=?, tooltip_str_kr=?, spell_bonus_list=?, "
			+ "companion_on_icon_id=?, companion_off_icon_id=?, companion_icon_priority=?, companion_tooltip_str_id=?, "
			+ "companion_new_str_id=?, companion_end_str_id=?, companion_is_good=?, companion_duration_show_type=?, "
			+ "delay_group_id=?, extract_item_name_id=?, "
			+ "extract_item_count=?";
	
	private void regist(){
		try {
			HashMap<Integer, SpellCommonBinExtend> list = bin.get_spell_list();
			
			int regiCnt = 1, limitCnt = 0;
			HashMap<Integer, ArrayList<CommonSpellInfo>> regiMap = new HashMap<Integer, ArrayList<CommonSpellInfo>>();
			for (SpellCommonBinExtend info : list.values()) {
				if (limitCnt >= 500) {
					limitCnt = 0;
					regiCnt++;
				}
				ArrayList<CommonSpellInfo> regilist = regiMap.get(regiCnt);
				if (regilist == null) {
					regilist = new ArrayList<CommonSpellInfo>();
					regiMap.put(regiCnt, regilist);
				}
				regilist.add(info.get_spell());
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
			
			for (ArrayList<CommonSpellInfo> regiList : regiMap.values()) {
				if (regiList == null || regiList.isEmpty()) {
					continue;
				}
				
				try {
					con		= L1DatabaseFactory.getInstance().getConnection();
					con.setAutoCommit(false);
					
					pstm	= con.prepareStatement(INSERT_QUERY);
					for (CommonSpellInfo info : regiList) {
						DATA.put(info.get_spell_id(), info);
						int idx = 0;
						pstm.setInt(++idx, info.get_spell_id());
						SpellCategory category = info.get_spell_category();
						pstm.setString(++idx, category == null ? String.format("%s(%d)", SpellCategory.SPELL.name(), SpellCategory.SPELL.toInt()) : String.format("%s(%d)", category.name(), category.toInt()));
						pstm.setInt(++idx, info.get_on_icon_id());
						pstm.setInt(++idx, info.get_off_icon_id());
						pstm.setInt(++idx, info.get_duration());
						pstm.setInt(++idx, info.get_tooltip_str_id());
						pstm.setString(++idx, info.get_tooltip_str_id() == 0 ? null : StringKLoader.getString(info.get_tooltip_str_id()));
						pstm.setString(++idx, info.get_spell_bonus_list_toString());
						CompanionSpellBuff companion = info.get_companion_spell_buff();
						pstm.setInt(++idx, companion == null ? -1 : companion.get_on_icon_id());
						pstm.setInt(++idx, companion == null ? -1 : companion.get_off_icon_id());
						pstm.setInt(++idx, companion == null ? -1 : companion.get_icon_priority());
						pstm.setInt(++idx, companion == null ? -1 : companion.get_tooltip_str_id());
						pstm.setInt(++idx, companion == null ? -1 : companion.get_new_str_id());
						pstm.setInt(++idx, companion == null ? -1 : companion.get_end_str_id());
						pstm.setInt(++idx, companion == null ? -1 : companion.get_is_good());
						pstm.setInt(++idx, companion == null ? -1 : companion.get_duration_show_type());
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
			}
			System.out.println("spell-common.bin [update completed]. TABLE : bin_spell_common");
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
			pstm = con.prepareStatement("SELECT * FROM bin_spell_common");
			rs = pstm.executeQuery();
			while (rs.next()) {
				info = new CommonSpellInfo(rs);
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
		if (Config.COMMON.COMMON_SPELL_BIN_UPDATE) {
			_instance.loadFile();
		} else {
			_instance.load();
		}
	}
}

