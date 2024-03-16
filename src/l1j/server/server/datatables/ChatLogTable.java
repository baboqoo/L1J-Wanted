package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.common.data.ChatType;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

public class ChatLogTable {
	private static Logger _log = Logger.getLogger(ChatLogTable.class.getName());

	/*
	 * 코드적으로는 HashMap를 이용해야 하지만, 퍼포먼스상의 문제가 있을지도 모르기 때문에, 배열로 타협.
	 * HashMap에의 변경을 검토하는 경우는, 퍼포먼스상 문제가 없는가 충분히 주의하는 것.
	 */
	private final boolean[] loggingConfig = new boolean[15];

	private ChatLogTable() {
		loadConfig();
	}

	private void loadConfig() {
		loggingConfig[0] = Config.SERVER.LOGGING_CHAT_NORMAL;
		loggingConfig[1] = Config.SERVER.LOGGING_CHAT_WHISPER;
		loggingConfig[2] = Config.SERVER.LOGGING_CHAT_SHOUT;
		loggingConfig[3] = Config.SERVER.LOGGING_CHAT_WORLD;
		loggingConfig[4] = Config.SERVER.LOGGING_CHAT_CLAN;
		loggingConfig[11] = Config.SERVER.LOGGING_CHAT_PARTY;
		loggingConfig[13] = Config.SERVER.LOGGING_CHAT_COMBINED;
		loggingConfig[14] = Config.SERVER.LOGGING_CHAT_CHAT_PARTY;
	}

	private static ChatLogTable _instance;

	public static ChatLogTable getInstance() {
		if(_instance == null)_instance = new ChatLogTable();
		return _instance;
	}

	private boolean isLoggingTarget(int type) {
		return loggingConfig[type];
	}

	public void storeChat(L1PcInstance pc, L1PcInstance target, String text, ChatType type) {
		if(!isLoggingTarget(type.toInt()))return;

		// type
		// 0:통상 채팅
		// 1:Whisper
		// 2:절규
		// 3:전체 채팅
		// 4:혈맹 채팅
		// 11:파티 채팅
		// 13:연합 채팅
		// 14:채팅 파티
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			if (target != null) {
				pstm = con.prepareStatement("INSERT INTO log_chat (account_name, char_id, name, clan_id, clan_name, locx, locy, mapid, type, target_account_name, target_id, target_name, target_clan_id, target_clan_name, target_locx, target_locy, target_mapid, content, datetime) VALUE (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE())");
				pstm.setString(1, pc.getAccountName());
				pstm.setInt(2, pc.getId());
				pstm.setString(3, pc.isGm() ? "******" : pc.getName());
				pstm.setInt(4, pc.getClanid());
				pstm.setString(5, pc.getClanName());
				pstm.setInt(6, pc.getX());
				pstm.setInt(7, pc.getY());
				pstm.setInt(8, pc.getMapId());
				pstm.setInt(9, type.toInt());
				pstm.setString(10, target.getAccountName());
				pstm.setInt(11, target.getId());
				pstm.setString(12, target.getName());
				pstm.setInt(13, target.getClanid());
				pstm.setString(14, target.getClanName());
				pstm.setInt(15, target.getX());
				pstm.setInt(16, target.getY());
				pstm.setInt(17, target.getMapId());
				pstm.setString(18, text);
			} else {
				pstm = con.prepareStatement("INSERT INTO log_chat (account_name, char_id, name, clan_id, clan_name, locx, locy, mapid, type, content, datetime) VALUE (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE())");
				pstm.setString(1, pc.getAccountName());
				pstm.setInt(2, pc.getId());
				pstm.setString(3, pc.isGm() ? "******" : pc.getName());
				pstm.setInt(4, pc.getClanid());
				pstm.setString(5, pc.getClanName());
				pstm.setInt(6, pc.getX());
				pstm.setInt(7, pc.getY());
				pstm.setInt(8, pc.getMapId());
				pstm.setInt(9, type.toInt());
				pstm.setString(10, text);
			}
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

}
