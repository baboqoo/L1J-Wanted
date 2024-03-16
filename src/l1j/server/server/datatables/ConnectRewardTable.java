package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.SQLUtil;

/**
 * 잡속 보상 정보
 * @author LinOffice
 */
public class ConnectRewardTable {
	private static Logger _log = Logger.getLogger(ConnectRewardTable.class.getName());
	
	public static enum REWARD_TYPE {
		NORMAL,
		STANBY_SERVER,
		;
		public static REWARD_TYPE fromString(String val) {
			switch (val) {
			case "NORMAL":
				return NORMAL;
			case "STANBY_SERVER":
				return STANBY_SERVER;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments ConnectRewardTable.REWARD_TYPE, %s", val));
			}
		}
	}
	
	public static class RewardInfo {
		private int id;
		private String description;
		private REWARD_TYPE reward_type;
		private L1Item reward_item;
		private int reward_item_count;
		private int reward_interval_minute;
		private long reward_start_date;
		private long reward_finish_date;
		private S_SystemMessage reward_message_pck;
		private int counter;
		
		public RewardInfo(int id, String description, REWARD_TYPE reward_type,
				L1Item reward_item, int reward_item_count,
				int reward_interval_minute, long reward_start_date, long reward_finish_date) {
			this.id						= id;
			this.description			= description;
			this.reward_type			= reward_type;
			this.reward_item			= reward_item;
			this.reward_item_count		= reward_item_count;
			this.reward_interval_minute	= reward_interval_minute;
			this.reward_start_date		= reward_start_date;
			this.reward_finish_date		= reward_finish_date;
			//this.reward_message_pck		= new S_SystemMessage(String.format("%s 획득: %s(%d) 획득", description, reward_item.getDescKr(), reward_item_count));
			this.reward_message_pck		= new S_SystemMessage(String.format("%s: obtained %s (%d)", description, reward_item.getDesc(), reward_item_count), true);
		}

		public int get_id() {
			return id;
		}
		public String get_description() {
			return description;
		}
		public REWARD_TYPE get_reward_type() {
			return reward_type;
		}
		public L1Item get_reward_item() {
			return reward_item;
		}
		public int get_reward_item_count() {
			return reward_item_count;
		}
		public int get_reward_interval_minute() {
			return reward_interval_minute;
		}
		public long get_reward_start_date() {
			return reward_start_date;
		}
		public long get_reward_finish_date() {
			return reward_finish_date;
		}
		public S_SystemMessage get_reward_message_pck() {
			return reward_message_pck;
		}
		public int increase_counter_and_get() {
			return ++counter;
		}
		public void reset_counter() {
			counter = 0;
		}
	}
	
	private static ConnectRewardTable _instance;
	public static ConnectRewardTable getInstance(){
		if (_instance == null) {
			_instance = new ConnectRewardTable();
		}
		return _instance;
	}
	
	private static final LinkedList<RewardInfo> REWARD_DATA = new LinkedList<>();
	
	public static LinkedList<RewardInfo> get_reward_list() {
		return REWARD_DATA;
	}
	
	private ConnectRewardTable() {
		load();
	}
	
	public void load() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		ItemTable it			= ItemTable.getInstance();
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM connect_reward WHERE is_use='true' AND (reward_start_date IS NULL OR (reward_start_date IS NOT NULL AND reward_finish_date IS NOT NULL AND reward_finish_date > NOW()))");
			rs		= pstm.executeQuery();
			while(rs.next()){
				int id							= rs.getInt("id");
				String description				= rs.getString("description");
				REWARD_TYPE reward_type			= REWARD_TYPE.fromString(rs.getString("reward_type"));
				int reward_item_id				= rs.getInt("reward_item_id");
				L1Item reward_item				= it.getTemplate(reward_item_id);
				if (reward_item == null) {
					System.out.println(String.format("[ConnectRewardTable] REWARD_ITEM_ID_TEMPLATE_NOT_FOUND : ITEMD_ID(%d)", reward_item_id));
					continue;
				}
				int reward_item_count			= rs.getInt("reward_item_count");
				int reward_interval_minute		= rs.getInt("reward_interval_minute");
				Timestamp reward_start_date		= rs.getTimestamp("reward_start_date");
				Timestamp reward_finish_date	= rs.getTimestamp("reward_finish_date");
				REWARD_DATA.add(new RewardInfo(id, description, reward_type, 
						reward_item, reward_item_count, reward_interval_minute, 
						reward_start_date == null ? 0L : reward_start_date.getTime(), 
						reward_finish_date == null ? 0L : reward_finish_date.getTime()));
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
		REWARD_DATA.clear();
		_instance.load();
	}
	
}

