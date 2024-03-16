package l1j.server.common.bin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.common.bin.huntingquest.HuntingGradeRewardT;
import l1j.server.common.bin.huntingquest.HuntingQuestCommonBin;
import l1j.server.common.bin.huntingquest.HuntingQuestConfigT;
import l1j.server.server.utils.SQLUtil;

/**
 * huntingquest-common.bin 파일 로더
 * @author LinOffice
 */
public class HuntingQuestCommonBinLoader {
	private static Logger _log = Logger.getLogger(HuntingQuestCommonBinLoader.class.getName());
	private static HuntingQuestCommonBin bin;
	
	private static HuntingQuestCommonBinLoader _instance;
	public static HuntingQuestCommonBinLoader getInstance() {
		if (_instance == null) {
			_instance = new HuntingQuestCommonBinLoader();
		}
		return _instance;
	}

	private HuntingQuestCommonBinLoader() {
		if (Config.COMMON.COMMON_HUNTING_QUEST_BIN_UPDATE) {
			loadFile();
		}
	}
	
	private void loadFile(){
		bin = HuntingQuestCommonBin.newInstance();
		try {
			bin.readFrom(ProtoInputStream.newInstance("./data/Contents/huntingquest-common.bin"));// bin 파일을 읽는다
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		if (!bin.isInitialized())
			throw new IllegalArgumentException(String.format("fail initialized info data.(HuntingQuestCommonBin) %d", bin.getInitializeBit()));
	
		regist();
	}
	
	private static final String TRUNCATE_QUERY = "truncate table bin_huntingquest_common";
	private static final String INSERT_QUERY = "INSERT INTO bin_huntingquest_common SET maxQuestCount=?, "
			+ "goalKillCount=?, reset_HourOfDay=?, "
			+ "reward_normal_ConditionalRewards=?, reward_normal_UsedItemID=?, reward_normal_UsedAmount=?, "
			+ "reward_dragon_ConditionalRewards=?, reward_dragon_UsedItemID=?, reward_dragon_UsedAmount=?, "
			+ "reward_hightdragon_ConditionalRewards=?, reward_hightdragon_UsedItemID=?, reward_hightdragon_UsedAmount=?, "
			+ "requiredCondition_MinLevel=?, requiredCondition_MaxLevel=?, requiredCondition_Map=?, requiredCondition_LocationDesc=?, "
			+ "enterMapID=?";
	
	private void regist(){
		try {
			HuntingQuestConfigT config = bin.get_quest().get_config();
			HuntingQuestConfigT.SystemT system = config.get_System();
			int resetHourOfDay = system.get_ResetTime() == null ? -1 : system.get_ResetTime().get_HourOfDay();
			HuntingQuestConfigT.SystemT.RewardListT rewardList	= system.get_RewardList();
			HuntingGradeRewardT.ConditionalRewardsT normal		= rewardList.get_Normal();
			String normal_condition			= normal.get_ConditionalReward_toString();
			int normal_itemid				= normal.get_UsedItemID();
			int normal_ammount				= normal.get_UsedAmount();
			HuntingGradeRewardT.ConditionalRewardsT dragon 		= rewardList.get_Dragon();
			String dragon_condition			= dragon.get_ConditionalReward_toString();
			int dragon_itemid				= dragon.get_UsedItemID();
			int dragon_ammount				= dragon.get_UsedAmount();
			HuntingGradeRewardT.ConditionalRewardsT highDragon	= rewardList.get_HighDragon();
			String hightdragon_condition	= highDragon.get_ConditionalReward_toString();
			int hightdragon_itemid			= highDragon.get_UsedItemID();
			int hightdragon_ammount			= highDragon.get_UsedAmount();
			java.util.LinkedList<HuntingQuestConfigT.SystemT.RequiredConditionT> require_list = system.get_RequiredCondition();
			
			Connection con			= null;
			PreparedStatement pstm	= null;
			try {
				con		= L1DatabaseFactory.getInstance().getConnection();
				pstm	= con.prepareStatement(TRUNCATE_QUERY);
				pstm.execute();
				SQLUtil.close(pstm);
				
				con.setAutoCommit(false);
				
				pstm	= con.prepareStatement(INSERT_QUERY);
				for (HuntingQuestConfigT.SystemT.RequiredConditionT require : require_list) {
					int idx = 0;
					pstm.setInt(++idx, config.get_MaxQuestCount());
					pstm.setInt(++idx, config.get_GoalKillCount());
					pstm.setInt(++idx, resetHourOfDay);
					pstm.setString(++idx, normal_condition);
					pstm.setInt(++idx, normal_itemid);
					pstm.setInt(++idx, normal_ammount);
					pstm.setString(++idx, dragon_condition);
					pstm.setInt(++idx, dragon_itemid);
					pstm.setInt(++idx, dragon_ammount);
					pstm.setString(++idx, hightdragon_condition);
					pstm.setInt(++idx, hightdragon_itemid);
					pstm.setInt(++idx, hightdragon_ammount);
					pstm.setInt(++idx, require.get_MinLevel());
					pstm.setInt(++idx, require.get_MaxLevel());
					pstm.setInt(++idx, require.get_Map());
					pstm.setInt(++idx, require.get_LocationDesc());
					pstm.setInt(++idx, config.get_EnterMapID());
					
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
			System.out.println("huntingquest-common.bin [update completed]. TABLE : bin_huntingquest_common");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void reload() {
		if (Config.COMMON.COMMON_HUNTING_QUEST_BIN_UPDATE) {
			_instance.loadFile();
		}
	}
}

