package l1j.server.common.bin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.common.DescKLoader;
import l1j.server.common.bin.craft.Craft;
import l1j.server.common.bin.craft.CraftAttr;
import l1j.server.common.bin.craft.CraftCommonBin;
import l1j.server.common.bin.craft.CraftInfo;
import l1j.server.common.bin.craft.CraftInputList;
import l1j.server.common.bin.craft.CraftOutputList;
import l1j.server.server.utils.SQLUtil;

/**
 * craft-common.bin 파일 로더
 * @author LinOffice
 */
public class CraftCommonBinLoader {
	private static Logger _log = Logger.getLogger(CraftCommonBinLoader.class.getName());
	private static CraftCommonBin bin;
	
	private static CraftCommonBinLoader _instance;
	public static CraftCommonBinLoader getInstance() {
		if (_instance == null) {
			_instance = new CraftCommonBinLoader();
		}
		return _instance;
	}
	
	public static Craft getCraft(int craftId){
		CraftInfo info = bin.get_craft(craftId);
		return info == null ? null : info.get_craft();
	}

	private CraftCommonBinLoader() {
		loadFile();
	}
	
	private void loadFile(){
		bin = CraftCommonBin.newInstance();
		try {
			bin.readFrom(ProtoInputStream.newInstance("./data/Contents/craft-common.bin"));// bin 파일을 읽는다
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		if (!bin.isInitialized()) {
			throw new IllegalArgumentException(String.format("fail initialized info data.(CraftCommonBin) %d", bin.getInitializeBit()));
		}
		
		if (Config.COMMON.COMMON_CRAFT_BIN_UPDATE) {
			regist();
		}
	}
	
	private static final String TRUNCATE_QUERY = "truncate table bin_craft_common";
	private static final String INSERT_QUERY = "INSERT INTO bin_craft_common SET craft_id=?, "
			+ "desc_id=?, desc_kr=?, min_level=?, max_level=?, required_gender=?, "
			+ "min_align=?, max_align=?, min_karma=?, max_karma=?, "
			+ "max_count=?, is_show=?, PCCafeOnly=?, bmProbOpen=?, "
			+ "required_classes=?, required_quests=?, required_sprites=?, required_items=?, "
			+ "inputs_arr_input_item=?, inputs_arr_option_item=?, outputs_success=?, outputs_failure=?, outputs_success_prob_by_million=?, "
			+ "batch_delay_sec=?, period_list=?, cur_successcount=?, max_successcount=?, except_npc=?, SuccessCountType=?";
	
	void regist() {
		try {
			HashMap<Integer, CraftInfo> list = bin.get_craft_list();
			
			int regiCnt = 1, limitCnt = 0;
			HashMap<Integer, ArrayList<CraftInfo>> regiMap = new HashMap<>();
			for (CraftInfo info : list.values()) {
				if (limitCnt >= 500) {
					limitCnt = 0;
					regiCnt++;
				}
				ArrayList<CraftInfo> regilist = regiMap.get(regiCnt);
				if (regilist == null) {
					regilist = new ArrayList<>();
					regiMap.put(regiCnt, regilist);
				}
				regilist.add(info);
				limitCnt++;
			}
			
			Connection con			= null;
			PreparedStatement pstm	= null;
			try {
				con		= L1DatabaseFactory.getInstance().getConnection();
				pstm	= con.prepareStatement(TRUNCATE_QUERY);
				pstm.execute();
			} catch(SQLException e) {
				e.printStackTrace();
			} finally {
				SQLUtil.close(pstm, con);
			}
			
			for (ArrayList<CraftInfo> regiList : regiMap.values()) {
				if (regiList == null || regiList.isEmpty()) {
					continue;
				}
				
				try {
					con		= L1DatabaseFactory.getInstance().getConnection();
					con.setAutoCommit(false);
					
					pstm	= con.prepareStatement(INSERT_QUERY);
					for (CraftInfo info : regiList) {
						Craft craft = info.get_craft();
						CraftAttr attr = craft.get_craft_attr();
						int idx = 0;
						pstm.setInt(++idx, info.get_craft_id());
						pstm.setInt(++idx, attr.get_desc());
						pstm.setString(++idx, attr.get_desc() == 0 ? null : DescKLoader.getDesc(attr.get_desc()));
						pstm.setInt(++idx, attr.get_min_level());
						pstm.setInt(++idx, attr.get_max_level());
						pstm.setInt(++idx, attr.get_required_gender());
						pstm.setInt(++idx, attr.get_min_align());
						pstm.setInt(++idx, attr.get_max_align());
						pstm.setInt(++idx, attr.get_min_karma());
						pstm.setInt(++idx, attr.get_max_karma());
						pstm.setInt(++idx, attr.get_max_count());
						pstm.setString(++idx, String.valueOf(attr.get_show()));
						pstm.setString(++idx, String.valueOf(attr.get_PCCafeOnly()));
						pstm.setString(++idx, String.valueOf(attr.get_bmProbOpen()));
						pstm.setInt(++idx, craft.get_required_classes());
						pstm.setString(++idx, craft.get_required_quests_toString());
						pstm.setString(++idx, craft.get_required_sprites_toString());
						pstm.setString(++idx, craft.get_required_items_toString());
						
						CraftInputList inputs = craft.get_inputs();
						String inputs_arr_input_item = null;
						String inputs_arr_option_item = null;
						if (inputs != null) {
							inputs_arr_input_item	= inputs.get_arr_input_item_toString();
							inputs_arr_option_item	= inputs.get_arr_option_item_toString();
						}
						pstm.setString(++idx, inputs_arr_input_item);
						pstm.setString(++idx, inputs_arr_option_item);
						
						CraftOutputList outputs = craft.get_outputs();
						String outputs_success = null;
						String outputs_failure = null;
						int outputs_success_prob_by_million = 0;
						if (outputs != null) {
							outputs_success	= outputs.get_success_toString();
							outputs_failure	= outputs.get_failure_toString();
							outputs_success_prob_by_million = outputs.get_success_prob_by_million();
						}
						pstm.setString(++idx, outputs_success);
						pstm.setString(++idx, outputs_failure);
						pstm.setInt(++idx, outputs_success_prob_by_million);
						
						pstm.setInt(++idx, craft.get_batch_delay_sec());
						pstm.setString(++idx, craft.get_period_list_toString());
						pstm.setInt(++idx, craft.get_cur_successcount());
						pstm.setInt(++idx, craft.get_max_successcount());
						pstm.setString(++idx, String.valueOf(craft.get_except_npc()));
						Craft.eSuccessCountType successCountType = craft.get_SuccessCountType();
						pstm.setString(++idx, String.format("%s(%d)", successCountType.name(), successCountType.toInt()));
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
			System.out.println("craft-common.bin [update completed]. TABLE : bin_craft_common");
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	public static void reload() {
		_instance.loadFile();
		
		if (Config.COMMON.COMMON_CRAFT_BIN_UPDATE) {
			_instance.regist();
		}
	}
}

