package l1j.server.common.bin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.common.DescKLoader;
import l1j.server.common.bin.treasureisland.TreasureIslandBox;
import l1j.server.common.bin.treasureisland.TreasureIslandBoxCommonBin;
import l1j.server.common.bin.treasureisland.TreasureIslandBoxCommonBin.TreasureIslandBoxCommonBinExtend;
import l1j.server.common.bin.treasureisland.TreasureIslandBoxRewardCommonBin;
import l1j.server.common.bin.treasureisland.TreasureIslandBoxRewardCommonBin.TreasureIslandBoxRewardCommonBinExtend;
import l1j.server.server.utils.SQLUtil;

/**
 * treasurebox-common.bin, treasureIslandRewardBox-common.bin 파일 로더
 * @author LinOffice
 */
public class TreasureBoxCommonBinLoader {
	private static Logger _log = Logger.getLogger(TreasureBoxCommonBinLoader.class.getName());
	private static TreasureIslandBoxCommonBin bin;
	private static TreasureIslandBoxRewardCommonBin reward_bin;
	
	public static TreasureIslandBox.TreasureBoxInfoListT.TreasureBoxInfoT.BoxT getBox(String name) {
		for (TreasureIslandBoxCommonBinExtend box : bin.get_extend().values()) {
			if (box.get_box().get_name().equals(name)) {
				return box.get_box();
			}
		}
		return null;
	}
	
	public static TreasureIslandBox.RewardBoxInfoListT.RewardBoxInfoT.RewardBoxT getReward(TreasureIslandBox.TreasureBoxGrade grade) {
		for (TreasureIslandBoxRewardCommonBinExtend reward : reward_bin.get_extend().values()) {
			if (reward.get_reward().get_grade() == grade) {
				return reward.get_reward();
			}
		}
		return null;
	}
	
	private static TreasureBoxCommonBinLoader _instance;
	public static TreasureBoxCommonBinLoader getInstance() {
		if (_instance == null) {
			_instance = new TreasureBoxCommonBinLoader();
		}
		return _instance;
	}
	
	private TreasureBoxCommonBinLoader(){
		loadFile();
	}
	
	private void loadFile(){
		bin = TreasureIslandBoxCommonBin.newInstance();
		reward_bin = TreasureIslandBoxRewardCommonBin.newInstance();
		try {
			bin.readFrom(ProtoInputStream.newInstance("./data/Contents/treasureBox-common.bin"));// bin 파일을 읽는다
			reward_bin.readFrom(ProtoInputStream.newInstance("./data/Contents/treasureIslandRewardBox-common.bin"));// bin 파일을 읽는다
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		if (!bin.isInitialized())
			throw new IllegalArgumentException(String.format("fail initialized info data.(TreasureIslandBoxCommonBin) %d", bin.getInitializeBit()));
		
		if (!reward_bin.isInitialized())
			throw new IllegalArgumentException(String.format("fail initialized info data.(TreasureIslandBoxRewardCommonBin) %d", reward_bin.getInitializeBit()));
		
		if (Config.COMMON.COMMON_TREASUREBOX_BIN_UPDATE) {
			regist();
		}
	}
	
	private static final String TRUNCATE_QUERY = "truncate table bin_treasurebox_common";
	private static final String INSERT_QUERY = "INSERT INTO bin_treasurebox_common SET id=?, name=?, excavateTime=?, desc_id=?, desc_kr=?, grade=?";
	
	private static final String TRUNCATE_REWARD_QUERY = "truncate table bin_treasureboxreward_common";
	private static final String INSERT_REWARD_QUERY = "INSERT INTO bin_treasureboxreward_common SET nameid=?, desc_kr=?, grade=?";
	
	private void regist(){
		try {
			HashMap<Integer, TreasureIslandBoxCommonBinExtend> boxs = bin.get_extend();
			HashMap<Integer, TreasureIslandBoxRewardCommonBinExtend> rewards = reward_bin.get_extend();
			
			Connection con			= null;
			PreparedStatement pstm	= null;
			try {
				con		= L1DatabaseFactory.getInstance().getConnection();
				
				pstm	= con.prepareStatement(TRUNCATE_QUERY);
				pstm.execute();
				SQLUtil.close(pstm);
				
				pstm	= con.prepareStatement(TRUNCATE_REWARD_QUERY);
				pstm.execute();
				SQLUtil.close(pstm);
				
				if (boxs == null || boxs.isEmpty()) {
					return;
				}
				
				con.setAutoCommit(false);
				
				pstm	= con.prepareStatement(INSERT_QUERY);
				for (Map.Entry<Integer, TreasureIslandBoxCommonBinExtend> entry : boxs.entrySet()) {
					TreasureIslandBox.TreasureBoxInfoListT.TreasureBoxInfoT.BoxT boxT = entry.getValue().get_box();
					int idx = 0;
					pstm.setInt(++idx, entry.getKey());
					pstm.setString(++idx, boxT.get_name());
					pstm.setInt(++idx, boxT.get_excavateTime());
					pstm.setString(++idx, boxT.get_desc());
					pstm.setString(++idx, DescKLoader.getDesc(boxT.get_desc()));
					pstm.setString(++idx, String.format("%s(%d)", boxT.get_grade().name(), boxT.get_grade().toInt()));
					pstm.addBatch();
					pstm.clearParameters();
				}
				pstm.executeBatch();
				pstm.clearBatch();
				con.commit();
				
				pstm	= con.prepareStatement(INSERT_REWARD_QUERY);
				for (Map.Entry<Integer, TreasureIslandBoxRewardCommonBinExtend> entry : rewards.entrySet()) {
					TreasureIslandBox.RewardBoxInfoListT.RewardBoxInfoT.RewardBoxT rewardT = entry.getValue().get_reward();
					int idx = 0;
					pstm.setInt(++idx, rewardT.get_nameid());
					pstm.setString(++idx, DescKLoader.getDesc(ItemCommonBinLoader.getCommonInfo(rewardT.get_nameid()).get_desc()));
					pstm.setString(++idx, String.format("%s(%d)", rewardT.get_grade().name(), rewardT.get_grade().toInt()));
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
			System.out.println("treasureBox-common.bin [update completed]. TABLE : bin_treasurebox_common");
			System.out.println("treasureIslandRewardBox-common.bin [update completed]. TABLE : bin_treasureboxreward_common");
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	public static void reload() {
		_instance.loadFile();
	}
	
}

