package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.pledge.S_PledgeWatch;
import l1j.server.server.utils.SQLUtil;

public class ClanAttentionTable {
	private static Logger _log = Logger.getLogger(ClanAttentionTable.class.getName());
	private static ClanAttentionTable _instance;
	private static final HashMap<String, ArrayList<String>> _list = new HashMap<String, ArrayList<String>>();

	public static ClanAttentionTable getInstance() {
		if (_instance == null) {
			_instance = new ClanAttentionTable();
		}
		return _instance;
	}

	public boolean isAttentionList(String clanName) {
		return _list.containsKey(clanName);
	}

	public void addAttentionList(String clanName, String attentionName) {
		if (!_list.containsKey(clanName)) {
			ArrayList<String> list = new ArrayList<String>();
			list.add(attentionName);
			_list.put(clanName, list);
		} else {
			_list.get(clanName).add(attentionName);
		}
	}

	public void removeAttentionList(String clanName, String attentionName) {
		if (_list.containsKey(clanName)) {
			_list.get(clanName).remove(attentionName);
			if (_list.get(clanName).size() == 0) {
				_list.remove(clanName);
			}
		}
	}

	public void removeAttentionList(String clanName) {
		if (_list.containsKey(clanName)) {
			_list.remove(clanName);
		}
	}

	public String[] getAttentionClanlist(String clanName) {
		if (_list.get(clanName) == null) {
			return null;
		}
		return _list.get(clanName).toArray(new String[_list.get(clanName).size()]);
	}

	public void writeEmblemAttention(String clanName, String attentionName) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO clan_emblem_attention SET clanname = ?, attentionClanname = ?");
			pstm.setString(1, clanName);
			pstm.setString(2, attentionName);
			pstm.execute();
			addAttentionList(clanName, attentionName);
			L1Clan clan = L1World.getInstance().getClan(clanName);
			for (L1PcInstance pc : clan.getOnlineClanMember()) {
				pc.sendPackets(new S_ServerMessage(3360, attentionName), true);
				pc.sendPackets(new S_PledgeWatch(getAttentionClanlist(clanName)), true);
			}
		} catch (Exception localException) {
			_log.log(Level.SEVERE, "writeEmblemAttention", localException);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public void deleteEmblemAttention(String clanName, String attentionName) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM clan_emblem_attention WHERE clanname=? AND attentionClanname=?");
			pstm.setString(1, clanName);
			pstm.setString(2, attentionName);
			pstm.execute();
			removeAttentionList(clanName, attentionName);
			L1Clan clan = L1World.getInstance().getClan(clanName);
			for (L1PcInstance pc : clan.getOnlineClanMember()) {
				pc.sendPackets(new S_ServerMessage(3359, attentionName), true);
				// /혈맹 주시: %0 혈맹 문장 주시 해제
				pc.sendPackets(new S_PledgeWatch(getAttentionClanlist(clanName)), true);
			}
		} catch (Exception localException) {
			_log.log(Level.SEVERE, "deleteEmblemAttention", localException);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	/** 혈맹 해산시 주시 삭제 **/
	public void deleteClanAttention(String clanName) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM clan_emblem_attention WHERE clanname=?");
			pstm.setString(1, clanName);
			pstm.execute();
		} catch (Exception localException) {
			_log.log(Level.SEVERE, "deleteEmblemAttention", localException);
		} finally {
			SQLUtil.close(pstm, con);
		}
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM clan_emblem_attention WHERE attentionClanname=?");
			pstm.setString(1, clanName);
			pstm.execute();
		} catch (Exception localException) {
			_log.log(Level.SEVERE, "deleteEmblemAttention", localException);
		} finally {
			SQLUtil.close(pstm, con);
		}
		for (L1Clan clan : L1World.getInstance().getAllClans()) {
			removeAttentionList(clan.getClanName(), clanName); // 문장 리스트 삭제
		}
	}

	public void loadEmblemAttention() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		String clanName = null;
		String attentionName = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM clan_emblem_attention");
			rs = pstm.executeQuery();
			while(rs.next()){
				clanName = rs.getString("clanname");
				attentionName = rs.getString("attentionClanname");
				addAttentionList(clanName, attentionName);
			}
		} catch (Exception localException) {
			_log.log(Level.SEVERE, "loadEmblemAttention()", localException);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
}
