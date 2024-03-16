package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

/**
 * 이벤트 정보
 * @author LinOffice
 */
public class EventTable {
	private static Logger _log = Logger.getLogger(EventTable.class.getName());
	
	public static enum EventFlag {
		SPAWN_NPC, DROP_ADENA, DROP_ITEM, POLY
	}
	
	public static class EventSpawnNpc {
		public int X, Y, MAP, HEAD, NPC;
		public L1NpcInstance npcIns;
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("X: ").append(X).append(", Y: ").append(Y).append(", MAP: ").append(MAP).append(", HEAD: ").append(HEAD).append(", NPC : ").append(NPC);
			return sb.toString();
		}
	}
	
	public static class EventInfo {
		private boolean active;
		private int event_id;
		private String description;
		private Timestamp start_date;
		private Timestamp finish_date;
		private boolean broadcast;
		private EventFlag event_flag;
		private LinkedList<EventSpawnNpc> spawn_data;
		private float drop_rate;
		private LinkedList<Integer> finish_delete_item;
		private LinkedList<Integer> finish_map_rollback;
		
		public EventInfo(int event_id, String description,
				Timestamp start_date, Timestamp finish_date, boolean broadcast,
				EventFlag event_flag, LinkedList<EventSpawnNpc> spawn_data,
				float drop_rate) {
			this.event_id = event_id;
			this.description = description;
			this.start_date = start_date;
			this.finish_date = finish_date;
			this.broadcast = broadcast;
			this.event_flag = event_flag;
			this.spawn_data = spawn_data;
			this.drop_rate = drop_rate;
		}
		
		public boolean isActive() {
			return active;
		}
		public void setActive(boolean val) {
			active = val;
		}
		public int getEvent_id() {
			return event_id;
		}
		public String getDescription() {
			return description;
		}
		public Timestamp getStart_date() {
			return start_date;
		}
		public Timestamp getFinish_date() {
			return finish_date;
		}
		public boolean isBroadcast() {
			return broadcast;
		}
		public EventFlag getEvent_flag() {
			return event_flag;
		}
		public LinkedList<EventSpawnNpc> getSpawn_data() {
			return spawn_data;
		}
		public void addSpawn_data(EventSpawnNpc val) {
			if (spawn_data == null) {
				spawn_data = new LinkedList<EventTable.EventSpawnNpc>();
			}
			spawn_data.add(val);
		}
		public float getDrop_rate() {
			return drop_rate;
		}
		public LinkedList<Integer> getFinish_delete_item() {
			return finish_delete_item;
		}
		public void addFinish_delete_item(int itemId) {
			if (finish_delete_item == null) {
				finish_delete_item = new LinkedList<Integer>();
			}
			finish_delete_item.add(itemId);
		}
		public LinkedList<Integer> getFinish_map_rollback() {
			return finish_map_rollback;
		}

		public void addFinish_map_rollback(int mapId) {
			if (finish_map_rollback == null) {
				finish_map_rollback = new LinkedList<Integer>();
			}
			finish_map_rollback.add(mapId);
		}
	}
	
	private static EventTable _instance;
	public static EventTable getInstance(){
		if (_instance == null) {
			_instance = new EventTable();
		}
		return _instance;
	}
	
	private static final LinkedList<EventInfo> EVENT_DATA = new LinkedList<>();
	
	public static LinkedList<EventInfo> getEventList() {
		return EVENT_DATA;
	}
	
	private EventTable() {
		load();
	}
	
	public void load() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		EventInfo info			= null;
		EventSpawnNpc npc		= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM event WHERE start_date IS NOT NULL AND finish_date IS NOT NULL AND finish_date > NOW()");
			rs		= pstm.executeQuery();
			while(rs.next()){
				EventFlag event_flag		= parseEventFlag(rs.getString("event_flag"));
				if (event_flag == null) {
					continue;
				}
				String spawn_data			= rs.getString("spawn_data");
				if (event_flag == EventFlag.SPAWN_NPC && StringUtil.isNullOrEmpty(spawn_data)) {
					continue;
				}
				int event_id				= rs.getInt("event_id");
				String description			= rs.getString("description");
				Timestamp start_date		= rs.getTimestamp("start_date");
				Timestamp finish_date		= rs.getTimestamp("finish_date");
				boolean broadcast			= Boolean.parseBoolean(rs.getString("broadcast"));
				float drop_rate				= rs.getFloat("drop_rate");
				String finish_delete_item	= rs.getString("finish_delete_item");
				String finish_map_rollback	= rs.getString("finish_map_rollback");
				
				info = new EventInfo(event_id, description, start_date, finish_date, broadcast, event_flag, null, drop_rate);
				if (!StringUtil.isNullOrEmpty(spawn_data)) {
					StringTokenizer st = new StringTokenizer(spawn_data, StringUtil.LineString);
					while (st.hasMoreElements()) {
						StringTokenizer token = new StringTokenizer(st.nextToken().trim(), StringUtil.CommaString);
						npc = new EventSpawnNpc();
						while (token.hasMoreElements()) {
							String txt = token.nextToken().trim();
							if (txt.startsWith("X:")) {
								txt = txt.replace("X:", StringUtil.EmptyString);
								npc.X = Integer.parseInt(txt.trim());
							} else if (txt.startsWith("Y:")) {
								txt = txt.replace("Y:", StringUtil.EmptyString);
								npc.Y = Integer.parseInt(txt.trim());
							} else if (txt.startsWith("MAP:")) {
								txt = txt.replace("MAP:", StringUtil.EmptyString);
								npc.MAP = Integer.parseInt(txt.trim());
							} else if (txt.startsWith("HEAD:")) {
								txt = txt.replace("HEAD:", StringUtil.EmptyString);
								npc.HEAD = Integer.parseInt(txt.trim());
							} else if (txt.startsWith("NPC:")) {
								txt = txt.replace("NPC:", StringUtil.EmptyString);
								npc.NPC = Integer.parseInt(txt.trim());
							}
						}
						info.addSpawn_data(npc);
					}
				}
				
				if (!StringUtil.isNullOrEmpty(finish_delete_item)) {
					StringTokenizer st = new StringTokenizer(finish_delete_item, StringUtil.LineString);
					while (st.hasMoreElements()) {
						info.addFinish_delete_item(Integer.parseInt(st.nextToken().trim()));
					}
				}
				if (!StringUtil.isNullOrEmpty(finish_map_rollback)) {
					StringTokenizer st = new StringTokenizer(finish_map_rollback, StringUtil.LineString);
					while (st.hasMoreElements()) {
						info.addFinish_map_rollback(Integer.parseInt(st.nextToken().trim()));
					}
				}
				EVENT_DATA.add(info);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	/**
	 * db에 등록된 아이템 제거
	 * @param deleteList
	 */
	public void finishDeleteItem(LinkedList<Integer> deleteList) {
		StringBuilder sb = new StringBuilder();
		for (Integer id : deleteList) {
			if (sb.length() > 0) {
				sb.append(StringUtil.CommaString);
			}
			sb.append(id);
		}
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement(String.format("DELETE FROM character_items WHERE item_id IN (%s)", sb.toString()));
			pstm.execute();
			SQLUtil.close(pstm);
			pstm	= con.prepareStatement(String.format("DELETE FROM character_warehouse WHERE item_id IN (%s)", sb.toString()));
			pstm.execute();
			SQLUtil.close(pstm);
			pstm	= con.prepareStatement(String.format("DELETE FROM character_elf_warehouse WHERE item_id IN (%s)", sb.toString()));
			pstm.execute();
			SQLUtil.close(pstm);
			pstm	= con.prepareStatement(String.format("DELETE FROM clan_warehouse WHERE item_id IN (%s)", sb.toString()));
			pstm.execute();
			SQLUtil.close(pstm);
			pstm	= con.prepareStatement(String.format("DELETE FROM character_special_warehouse WHERE item_id IN (%s)", sb.toString()));
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	public static EventFlag parseEventFlag(String val) {
		switch (val) {
		case "SPAWN_NPC":
			return EventFlag.SPAWN_NPC;
		case "DROP_ADENA":
			return EventFlag.DROP_ADENA;
		case "DROP_ITEM":
			return EventFlag.DROP_ITEM;
		case "POLY":
			return EventFlag.POLY;
		default:
			return null;
		}
	}
	
	public static void reload() {
		EVENT_DATA.clear();
		getInstance().load();
	}
	
}

