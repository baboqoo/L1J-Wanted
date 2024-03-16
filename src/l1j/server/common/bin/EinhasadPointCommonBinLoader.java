package l1j.server.common.bin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.common.bin.einhasadpoint.EinhasadPointCommonBin;
import l1j.server.common.bin.einhasadpoint.EinhasadPointStatInfoT;
import l1j.server.common.data.eEinhasadStatType;
import l1j.server.server.utils.SQLUtil;

/**
 * einhasad_point_info-common.bin 파일 로더
 * @author LinOffice
 */
public class EinhasadPointCommonBinLoader {
	private static Logger _log = Logger.getLogger(EinhasadPointCommonBinLoader.class.getName());
	private static EinhasadPointCommonBin bin;
	
	private static EinhasadPointCommonBinLoader _instance;
	public static EinhasadPointCommonBinLoader getInstance() {
		if (_instance == null) {
			_instance = new EinhasadPointCommonBinLoader();
		}
		return _instance;
	}
	
	public static EinhasadPointStatInfoT getInfo() {
		return bin.get_data().get_info();
	}
	
	static final HashMap<eEinhasadStatType, HashMap<Byte, EinhasadPointStatInfoT.StatT>> DATA = new HashMap<>();
	
	public static EinhasadPointStatInfoT.StatT getStatT(byte stat, eEinhasadStatType statType) {
		HashMap<Byte, EinhasadPointStatInfoT.StatT> map = DATA.get(statType);
		if (map == null) {
			map = new HashMap<>();
			DATA.put(statType, map);
		}
		EinhasadPointStatInfoT.StatT statT = map.get(stat);
		if (statT == null) {
			statT = find(stat, statType);
			map.put(stat, statT);
		}
		return statT;
	}
	
	static EinhasadPointStatInfoT.StatT find(byte stat, eEinhasadStatType statType) {
		for (EinhasadPointStatInfoT.StatT statT : bin.get_data().get_info().get_Stat()) {
			if (statT.get_index() == statType.toInt() && statT.get_value() == stat) {
				return statT;
			}
		}
		throw new IllegalArgumentException(String.format("invalid arguments EinhasadPointCommonBinLoader statT find, type:%d, stat:%d", statType.toInt(), stat));
	}

	private EinhasadPointCommonBinLoader() {
		loadFile();
	}
	
	private void loadFile(){
		bin = EinhasadPointCommonBin.newInstance();
		try {
			bin.readFrom(ProtoInputStream.newInstance("./data/Contents/einhasad_point_info-common.bin"));// bin 파일을 읽는다
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		if (!bin.isInitialized())
			throw new IllegalArgumentException(String.format("fail initialized info data.(EinhasadPointCommonBin) %d", bin.getInitializeBit()));
		
		if (Config.COMMON.COMMON_EINHASAD_POINT_BIN_UPDATE) {
			regist();
		}
	}
	
	private static final String INSERT_COST_QUERY = "INSERT INTO bin_einpoint_cost_common SET value=?, point=?";
	
	private static final String INSERT_META_QUERY = "INSERT INTO bin_einpoint_meta_common SET index_id=?, stat_type=?, "
			+ "AbilityMetaData1_token=?, AbilityMetaData1_x100=?, AbilityMetaData1_unit=?, "
			+ "AbilityMetaData2_token=?, AbilityMetaData2_x100=?, AbilityMetaData2_unit=?";
	
	private static final String INSERT_STAT_QUERY = "INSERT INTO bin_einpoint_stat_common SET index_id=?, stat_type=?, value=?, "
			+ "Ability1_minIncValue=?, Ability1_maxIncValue=?, "
			+ "Ability2_minIncValue=?, Ability2_maxIncValue=?, "
			+ "StatMaxInfo_level=?, StatMaxInfo_statMax=?, "
			+ "eachStatMax=?, totalStatMax=?";
	
	private static final String INSERT_NORMAL_PROB_QUERY = "INSERT INTO bin_einpoint_normal_prob_common SET Normal_level=?, prob=?";
	
	private static final String INSERT_OVER_STAT_PROB_QUERY = "INSERT INTO bin_einpoint_overstat_prob_common SET over_level=?, prob=?";
	
	private static final String INSERT_PROB_TABLE_QUERY = "INSERT INTO bin_einpoint_prob_table_common SET bonusPoint=?, prob=?, isLastChance=?";
	
	private void regist(){
		try {
			EinhasadPointStatInfoT infoT = bin.get_data().get_info();
			Connection con			= null;
			PreparedStatement pstm	= null;
			try {
				con		= L1DatabaseFactory.getInstance().getConnection();
				
				pstm	= con.prepareStatement("truncate table bin_einpoint_cost_common");
				pstm.execute();
				SQLUtil.close(pstm);
				
				pstm	= con.prepareStatement("truncate table bin_einpoint_meta_common");
				pstm.execute();
				SQLUtil.close(pstm);
				
				pstm	= con.prepareStatement("truncate table bin_einpoint_stat_common");
				pstm.execute();
				SQLUtil.close(pstm);
				
				pstm	= con.prepareStatement("truncate table bin_einpoint_normal_prob_common");
				pstm.execute();
				SQLUtil.close(pstm);
				
				pstm	= con.prepareStatement("truncate table bin_einpoint_overstat_prob_common");
				pstm.execute();
				SQLUtil.close(pstm);
				
				pstm	= con.prepareStatement("truncate table bin_einpoint_prob_table_common");
				pstm.execute();
				SQLUtil.close(pstm);
				
				if (infoT == null) {
					return;
				}
				
				con.setAutoCommit(false);
				
				pstm	= con.prepareStatement(INSERT_COST_QUERY);
				for (EinhasadPointStatInfoT.EnchantCostT costT : infoT.get_EnchantCost()) {
					pstm.setInt(1, costT.get_value());
					pstm.setInt(2, costT.get_point());
					pstm.addBatch();
					pstm.clearParameters();
				}
				pstm.executeBatch();
				pstm.clearBatch();
				con.commit();
				SQLUtil.close(pstm);
				
				pstm	= con.prepareStatement(INSERT_META_QUERY);
				for (EinhasadPointStatInfoT.StatMetaDataT metaT : infoT.get_StatMetaData()) {
					EinhasadPointStatInfoT.StatMetaDataT.AbilityMetaDataT ability1 = metaT.get_AbilityMetaData1();
					EinhasadPointStatInfoT.StatMetaDataT.AbilityMetaDataT ability2 = metaT.get_AbilityMetaData2();
					eEinhasadStatType statType = eEinhasadStatType.fromInt(metaT.get_index());
					int index = 0;
					pstm.setInt(++index, metaT.get_index());
					pstm.setString(++index, String.format("%s(%d)", statType.name(), statType.toInt()));
					pstm.setString(++index, ability1.get_token());
					pstm.setString(++index, String.valueOf(ability1.get_x100()));
					pstm.setString(++index, String.format("%s(%d)", ability1.get_unit().name(), ability1.get_unit().toInt()));
					pstm.setString(++index, ability2.get_token());
					pstm.setString(++index, String.valueOf(ability2.get_x100()));
					pstm.setString(++index, String.format("%s(%d)", ability2.get_unit().name(), ability2.get_unit().toInt()));
					pstm.addBatch();
					pstm.clearParameters();
				}
				pstm.executeBatch();
				pstm.clearBatch();
				con.commit();
				SQLUtil.close(pstm);
				
				pstm	= con.prepareStatement(INSERT_STAT_QUERY);
				for (EinhasadPointStatInfoT.StatT statT : infoT.get_Stat()) {
					EinhasadPointStatInfoT.StatT.AbilityT ability1 = statT.get_Ability1();
					EinhasadPointStatInfoT.StatT.AbilityT ability2 = statT.get_Ability2();
					eEinhasadStatType statType = eEinhasadStatType.fromInt(statT.get_index());
					int index = 0;
					pstm.setInt(++index, statT.get_index());
					pstm.setString(++index, String.format("%s(%d)", statType.name(), statType.toInt()));
					pstm.setInt(++index, statT.get_value());
					pstm.setInt(++index, ability1.get_minIncValue());
					pstm.setInt(++index, ability1.get_maxIncValue());
					pstm.setInt(++index, ability2.get_minIncValue());
					pstm.setInt(++index, ability2.get_maxIncValue());
					EinhasadPointStatInfoT.StatMaxInfoT maxInfo = infoT.get_StatMaxInfo().get(statT.get_index() <= 2 ? 0 : 1);
					pstm.setInt(++index, maxInfo.get_level());
					pstm.setInt(++index, maxInfo.get_statMax());
					pstm.setInt(++index, infoT.get_eachStatMax());
					pstm.setInt(++index, infoT.get_totalStatMax());
					pstm.addBatch();
					pstm.clearParameters();
				}
				pstm.executeBatch();
				pstm.clearBatch();
				con.commit();
				SQLUtil.close(pstm);
				
				if (infoT.has_einhasadProb()) {
					java.util.LinkedList<EinhasadPointStatInfoT.EinhasadProb> einhasadProb = infoT.get_einhasadProb();
					
					if (einhasadProb.getFirst().has_NormalLevels()) {
						pstm	= con.prepareStatement(INSERT_NORMAL_PROB_QUERY);
						for (EinhasadPointStatInfoT.EinhasadProb prob : einhasadProb) {
							for (EinhasadPointStatInfoT.EinhasadProb.EinhasadLevel normal : prob.get_NormalLevels()) {
								pstm.setInt(1, normal.get_level());
								pstm.setInt(2, normal.get_prob());
								pstm.addBatch();
								pstm.clearParameters();
							}
						}
						pstm.executeBatch();
						pstm.clearBatch();
						con.commit();
						SQLUtil.close(pstm);
					}
					
					if (einhasadProb.getFirst().has_OverStatMaxPerLevels()) {
						pstm	= con.prepareStatement(INSERT_OVER_STAT_PROB_QUERY);
						for (EinhasadPointStatInfoT.EinhasadProb prob : einhasadProb) {
							for (EinhasadPointStatInfoT.EinhasadProb.EinhasadLevel normal : prob.get_OverStatMaxPerLevels()) {
								pstm.setInt(1, normal.get_level());
								pstm.setInt(2, normal.get_prob());
								pstm.addBatch();
								pstm.clearParameters();
							}
						}
						pstm.executeBatch();
						pstm.clearBatch();
						con.commit();
						SQLUtil.close(pstm);
					}
					
					if (einhasadProb.getFirst().has_probTable()) {
						pstm	= con.prepareStatement(INSERT_PROB_TABLE_QUERY);
						for (EinhasadPointStatInfoT.EinhasadProb prob : einhasadProb) {
							for (EinhasadPointStatInfoT.EinhasadProb.EinhasadBonusPointProbTable table : prob.get_probTable()) {
								for (EinhasadPointStatInfoT.EinhasadProb.EinhasadBonusPointProbTable.EinhasadBonusPointProb pt : table.get_probList()) {
									pstm.setInt(1, pt.get_bonusPoint());
									pstm.setInt(2, pt.get_prob());
									pstm.setString(3, String.valueOf(table.get_isLastChance()));
									pstm.addBatch();
									pstm.clearParameters();
								}
							}
						}
						pstm.executeBatch();
						pstm.clearBatch();
						con.commit();
						SQLUtil.close(pstm);
					}
				}
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
			System.out.println("einhasad_point_info-common.bin [update completed]. TABLE : bin_einpoint_common");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void reload() {
		_instance.loadFile();
	}
}

