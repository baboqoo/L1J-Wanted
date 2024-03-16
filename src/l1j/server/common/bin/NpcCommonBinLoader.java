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
import l1j.server.common.DescKLoader;
import l1j.server.common.bin.npc.CommonNPCInfo;
import l1j.server.common.bin.npc.CommonNPCInfo.eTendency;
import l1j.server.common.bin.npc.NpcCommonBin;
import l1j.server.common.bin.npc.NpcCommonBinExtend;
import l1j.server.common.data.ElementalResistance;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

/**
 * npc-common.bin 파일 로더
 * @author LinOffice
 */
public class NpcCommonBinLoader {
	private static Logger _log = Logger.getLogger(NpcCommonBinLoader.class.getName());
	private static final HashMap<Integer, CommonNPCInfo> DATA = new HashMap<>();
	private static NpcCommonBin bin;
	
	private static NpcCommonBinLoader _instance;
	public static NpcCommonBinLoader getInstance() {
		if (_instance == null) {
			_instance = new NpcCommonBinLoader();
		}
		return _instance;
	}
	
	public static CommonNPCInfo getCommonBinInfo(int classId) {
		return DATA.get(classId);
	}
	
	public static CommonNPCInfo getCommonBinInfo(String desc, int spriteId){
		if (StringUtil.isNullOrEmpty(desc) || spriteId == 0) {
			return null;
		}
		for (CommonNPCInfo obj : DATA.values()) {
			if (StringUtil.isNullOrEmpty(obj.get_desc()) || obj.get_sprite_id() == 0) {
				continue;
			}
			if (obj.get_desc().equals(desc) && obj.get_sprite_id() == spriteId) {
				return obj;
			}
		}
		return null;
	}

	private NpcCommonBinLoader() {
		if (Config.COMMON.COMMON_NPC_BIN_UPDATE) {
			loadFile();
		} else {
			load();
		}
	}
	
	private void loadFile(){
		bin = NpcCommonBin.newInstance();
		try {
			bin.readFrom(ProtoInputStream.newInstance("./data/Contents/npc-common.bin"));// bin 파일을 읽는다
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		if (!bin.isInitialized())
			throw new IllegalArgumentException(String.format("fail initialized info data.(NpcCommonBin) %d", bin.getInitializeBit()));
		
		regist();
	}
	
	private static final String TRUNCATE_QUERY = "truncate table bin_npc_common";
	private static final String INSERT_QUERY = "INSERT INTO bin_npc_common SET class_id=?, sprite_id=?, desc_id=?, desc_kr=?, level=?, ac=?, hp=?, mp=?, "
			+ "dex=?, str=?, inti=?, wis=?, con=?, cha=?, "
			+ "mr=?, magic_level=?, magic_bonus=?, magic_evasion=?, "
			+ "resistance_fire=?, resistance_water=?, resistance_air=?, resistance_earth=?, "
			+ "alignment=?, big=?, drop_items=?, tendency=?, category=?, is_bossmonster=?, can_turnundead=?";
	
	private void regist(){
		try {
			HashMap<Integer, NpcCommonBinExtend> list = bin.get_npc_list();
			
			int regiCnt = 1, limitCnt = 0;
			HashMap<Integer, ArrayList<CommonNPCInfo>> regiMap = new HashMap<>();
			for (NpcCommonBinExtend info : list.values()) {
				if (limitCnt >= 500) {
					limitCnt = 0;
					regiCnt++;
				}
				ArrayList<CommonNPCInfo> regilist = regiMap.get(regiCnt);
				if (regilist == null) {
					regilist = new ArrayList<>();
					regiMap.put(regiCnt, regilist);
				}
				regilist.add(info.get_npc());
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
			
			for (ArrayList<CommonNPCInfo> regiList : regiMap.values()) {
				if (regiList == null || regiList.isEmpty()) {
					continue;
				}
				
				try {
					con		= L1DatabaseFactory.getInstance().getConnection();
					con.setAutoCommit(false);
					
					pstm	= con.prepareStatement(INSERT_QUERY);
					for (CommonNPCInfo info : regiList) {
						DATA.put(info.get_class_id(), info);
						int idx = 0;
						pstm.setInt(++idx, info.get_class_id());
						pstm.setInt(++idx, info.get_sprite_id());
						pstm.setString(++idx, info.get_desc() == null ? null : info.get_desc());
						pstm.setString(++idx, info.get_desc() == null ? null : DescKLoader.getDesc(info.get_desc()));
						pstm.setInt(++idx, info.get_level() );
						pstm.setInt(++idx, info.get_ac());
						pstm.setInt(++idx, info.get_hp());
						pstm.setInt(++idx, info.get_mp());
						pstm.setInt(++idx, info.get_dex());
						pstm.setInt(++idx, info.get_str());
						pstm.setInt(++idx, info.get_int());
						pstm.setInt(++idx, info.get_wis());
						pstm.setInt(++idx, info.get_con());
						pstm.setInt(++idx, info.get_cha());
						pstm.setInt(++idx, info.get_mr());
						pstm.setInt(++idx, info.get_magic_level());
						pstm.setInt(++idx, info.get_magic_bonus());
						pstm.setInt(++idx, info.get_magic_evasion());
						ElementalResistance resistance = info.get_elemental_resistance();
						pstm.setInt(++idx, resistance == null ? 0 : resistance.get_fire());
						pstm.setInt(++idx, resistance == null ? 0 : resistance.get_water());
						pstm.setInt(++idx, resistance == null ? 0 : resistance.get_air());
						pstm.setInt(++idx, resistance == null ? 0 : resistance.get_earth());
						pstm.setInt(++idx, info.get_alignment());
						pstm.setString(++idx, String.valueOf(info.get_big()));
						pstm.setString(++idx, info.get_drop_items_toString());
						eTendency tendency = info.get_tendency();
						pstm.setString(++idx, String.format("%s(%d)", tendency.name(), tendency.toInt()));
						pstm.setInt(++idx, info.get_category());
						pstm.setString(++idx, String.valueOf(info.get_is_bossmonster()));
						pstm.setString(++idx, String.valueOf(info.get_can_turnundead()));
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
			System.out.println("npc-common.bin [update completed]. TABLE : bin_npc_common");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void load() {
		CommonNPCInfo info		= null;
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM bin_npc_common");
			rs = pstm.executeQuery();
			while (rs.next()) {
				info = new CommonNPCInfo(rs);
				DATA.put(info.get_class_id(), info);
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
		if (Config.COMMON.COMMON_NPC_BIN_UPDATE) {
			_instance.loadFile();
		} else {
			_instance.load();
		}
	}
}

