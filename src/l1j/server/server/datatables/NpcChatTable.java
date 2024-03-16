package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1NpcChat;
import l1j.server.server.utils.SQLUtil;

public class NpcChatTable {

	private static Logger _log = Logger.getLogger(NpcChatTable.class.getName());

	private static NpcChatTable _instance;

	private HashMap<Integer, L1NpcChat> _npcChatAppearance	= new HashMap<Integer, L1NpcChat>();
	private HashMap<Integer, L1NpcChat> _npcChatDead		= new HashMap<Integer, L1NpcChat>();
	private HashMap<Integer, L1NpcChat> _npcChatHide		= new HashMap<Integer, L1NpcChat>();
	private HashMap<Integer, L1NpcChat> _npcChatGameTime	= new HashMap<Integer, L1NpcChat>();

	public static NpcChatTable getInstance() {
		if (_instance == null) {
			_instance = new NpcChatTable();
		}
		return _instance;
	}
	
	public L1NpcChat getTemplateAppearance(int i) {
		return _npcChatAppearance.get(i);
	}

	public L1NpcChat getTemplateDead(int i) {
		return _npcChatDead.get(i);
	}

	public L1NpcChat getTemplateHide(int i) {
		return _npcChatHide.get(i);
	}

	public L1NpcChat getTemplateGameTime(int i) {
		return _npcChatGameTime.get(i);
	}

	public L1NpcChat[] getAllGameTime() {
		return _npcChatGameTime.values().toArray(new L1NpcChat[_npcChatGameTime.size()]);
	}

	private NpcChatTable() {
		load();
	}
	
	public static void reload() {
		for (L1Npc temp : NpcTable.getInstance().getAllTemplate()) {
			temp.setChat(false);
		}
		NpcChatTable oldInstance = _instance;
		_instance = new NpcChatTable();
		oldInstance._npcChatAppearance.clear();
		oldInstance._npcChatDead.clear();
		oldInstance._npcChatHide.clear();
		oldInstance._npcChatGameTime.clear();
	}

	private void load() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM npcchat");
			rs = pstm.executeQuery();
			L1NpcChat npcChat = null;
			L1Npc template = null;
			NpcTable tb = NpcTable.getInstance();
			while (rs.next()) {
				int npcId	= rs.getInt("npc_id");
				template	= tb.getTemplate(npcId);
				if (template == null) {
					System.out.println(String.format("[NpcChatTable] NOT_FOUND_TEMPLATE NPCID(%d)", npcId));
					continue;
				}
				
				npcChat = new L1NpcChat();
				npcChat.setNpcId(npcId);
				npcChat.setChatTiming(rs.getInt("chat_timing"));
				npcChat.setStartDelayTime(rs.getInt("start_delay_time"));
				npcChat.setChatId1(rs.getString("chat_id1"));
				npcChat.setChatId2(rs.getString("chat_id2"));
				npcChat.setChatId3(rs.getString("chat_id3"));
				npcChat.setChatId4(rs.getString("chat_id4"));
				npcChat.setChatId5(rs.getString("chat_id5"));
				npcChat.setChatInterval(rs.getInt("chat_interval"));
				npcChat.setShout(rs.getBoolean("is_shout"));
				npcChat.setWorldChat(rs.getBoolean("is_world_chat"));
				npcChat.setRepeat(rs.getBoolean("is_repeat"));
				npcChat.setRepeatInterval(rs.getInt("repeat_interval"));
				npcChat.setGameTime(rs.getInt("game_time"));
				npcChat.percent = rs.getInt("percent");
				if (npcChat.getChatTiming() == L1NpcInstance.CHAT_TIMING_APPEARANCE) {
					_npcChatAppearance.put(npcChat.getNpcId(),	npcChat);
				} else if (npcChat.getChatTiming() == L1NpcInstance.CHAT_TIMING_DEAD) {
					_npcChatDead.put(npcChat.getNpcId(), npcChat);
				} else if (npcChat.getChatTiming() == L1NpcInstance.CHAT_TIMING_HIDE) {
					_npcChatHide.put(npcChat.getNpcId(), npcChat);
				} else if (npcChat.getChatTiming() == L1NpcInstance.CHAT_TIMING_GAME_TIME) {
					_npcChatGameTime.put(npcChat.getNpcId(), npcChat);
					template.setChat(true);
				}
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
}

