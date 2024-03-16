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
import l1j.server.common.bin.timecollection.TimeCollection;
import l1j.server.common.bin.timecollection.TimeCollectionCommonBin;
import l1j.server.common.data.TimeCollectionSetType;
import l1j.server.server.utils.SQLUtil;

/**
 * time_collection-common.bin 파일 로더
 * @author LinOffice
 */
public class TimeCollectionCommonBinLoader {
	private static Logger _log = Logger.getLogger(TimeCollectionCommonBinLoader.class.getName());
	private static TimeCollectionCommonBin bin;
	
	private static TimeCollectionCommonBinLoader _instance;
	public static TimeCollectionCommonBinLoader getInstance() {
		if (_instance == null) {
			_instance = new TimeCollectionCommonBinLoader();
		}
		return _instance;
	}
	
	public static TimeCollection getData() {
		return bin.get_collection().get_collection();
	}

	private TimeCollectionCommonBinLoader() {
		loadFile();
	}
	
	private void loadFile(){
		bin = TimeCollectionCommonBin.newInstance();
		try {
			bin.readFrom(ProtoInputStream.newInstance("./data/Contents/time_collection-common.bin"));// bin 파일을 읽는다
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		if (!bin.isInitialized())
			throw new IllegalArgumentException(String.format("fail initialized info data.(TimeCollectionCommonBin) %d", bin.getInitializeBit()));
	
		if (Config.COMMON.COMMON_TIME_COLLECTION_BIN_UPDATE) {
			regist();
		}
	}
	
	private static final String TRUNCATE_QUERY = "truncate table bin_timecollection_common";
	private static final String INSERT_QUERY = "INSERT INTO bin_timecollection_common SET "
			+ "buffSelect=?, rewardList=?, enchantSection=?, "
			+ "group_id=?, group_desc=?, group_desc_kr=?, group_level_min=?, group_level_max=?, "
			+ "group_period_StartDate=?, group_period_EndDate=?, "
			+ "group_set_id=?, group_set_desc=?, group_set_desc_kr=?, group_set_defaultTime=?, group_set_recycle=?, group_set_itemSlot=?, group_set_BuffType=?, group_set_endBonus=?, "
			+ "group_set_ExtraTimeId=?, group_set_SetType=?, "
			+ "ExtraTimeSection=?, NPCDialogInfo=?, AlarmSetting=?";
	
	private void regist(){
		try {
			TimeCollection time = bin.get_collection().get_collection();
			
			TimeCollection.BuffSelectT buffSelect				= time.get_BuffSelect();
			String buffUserList = buffSelect.get_user_toString();
			
			TimeCollection.RewardListT rewardList				= time.get_RewardList();
			String rewards		= rewardList.get_Reward_toString();
			
			TimeCollection.EnchantSectionT enchantSection		= time.get_EnchantSection();
			String enchant		= enchantSection.get_EnchantID_toString();
			
			java.util.LinkedList<TimeCollection.GroupT> groups	= time.get_Group();
			
			String npcDialogInfo	= time.get_NPCDialogInfo_toString();
			String alarmSetting		= time.get_AlarmSetting_toString();
			
			Connection con			= null;
			PreparedStatement pstm	= null;
			try {
				con		= L1DatabaseFactory.getInstance().getConnection();
				
				pstm	= con.prepareStatement(TRUNCATE_QUERY);
				pstm.execute();
				SQLUtil.close(pstm);
				
				con.setAutoCommit(false);
				
				pstm	= con.prepareStatement(INSERT_QUERY);
				for (TimeCollection.GroupT group : groups) {
					
					for (TimeCollection.GroupT.SetT setT : group.get_Set()) {
						int idx = 0;
						pstm.setString(++idx, buffUserList);
						pstm.setString(++idx, rewards);
						pstm.setString(++idx, enchant);
						pstm.setInt(++idx, group.get_ID());
						pstm.setInt(++idx, group.get_Desc());
						pstm.setString(++idx, DescKLoader.getDesc(group.get_Desc()));
						TimeCollection.GroupT.LevelT levelT = group.get_Level();
						pstm.setInt(++idx, levelT == null ? 0 : levelT.get_LevelMin());
						pstm.setInt(++idx, levelT == null ? 0 : levelT.get_LevelMax());
						TimeCollection.GroupT.PeriodT periodT = group.get_Period();
						pstm.setString(++idx, periodT == null ? null : periodT.get_StartDate());
						pstm.setString(++idx, periodT == null ? null : periodT.get_EndDate());
						
						pstm.setInt(++idx, setT.get_ID());
						pstm.setInt(++idx, setT.get_Desc());
						pstm.setString(++idx, DescKLoader.getDesc(setT.get_Desc()));
						pstm.setString(++idx, setT.get_DefaultTime());
						pstm.setInt(++idx, setT.get_Recycle());
						pstm.setString(++idx, setT.get_ItemSlot_toString());
						pstm.setString(++idx, setT.get_BuffType_toString());
						pstm.setString(++idx, String.valueOf(setT.get_EndBonus()));
						pstm.setInt(++idx, setT.get_ExtraTimeId());
						TimeCollectionSetType setType = setT.get_SetType();
						pstm.setString(++idx, setType == null ? "NONE(-1)" : String.format("%s(%d)", setType.name(), setType.toInt()));
						
						String extraSection = null;
						if (setT.get_ExtraTimeId() > 0) {
							extraSection = time.get_ExtraTimeSection().get_ExtraTime(setT.get_ExtraTimeId()).get_ExtraTimeT_toString();
						}
						pstm.setString(++idx, extraSection);
						pstm.setString(++idx, npcDialogInfo);
						pstm.setString(++idx, alarmSetting);
						
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
			
			System.out.println("time_collection-common.bin [update completed]. TABLE : bin_timecollection_common");
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	public static void reload() {
		_instance.loadFile();
	}
}

