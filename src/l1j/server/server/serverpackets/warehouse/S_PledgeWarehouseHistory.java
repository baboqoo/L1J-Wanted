package l1j.server.server.serverpackets.warehouse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.SQLUtil;

public class S_PledgeWarehouseHistory extends ServerBasePacket {
	private static final String S_PLEDGE_WAREHOUSE_HISTORY = "[S] S_PledgeWarehouseHistory";
	private byte[] _byte = null;

	public S_PledgeWarehouseHistory(L1PcInstance pc) {
		buildPacket(pc);
	}

	private void buildPacket(L1PcInstance pc) {
		writeC(Opcodes.S_EVENT);
		writeC(0x75);
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM clan_history WHERE clan_id =? ORDER BY num DESC");
			pstm.setInt(1, pc.getClanid());
			rs = pstm.executeQuery();
			rs.last();
			writeD(rs.getRow());
			rs.beforeFirst();
			while (rs.next()) {
				writeS(rs.getString("char_name"));
				writeC(rs.getInt("ckck"));
				writeS(rs.getString("item_name"));
				writeD(rs.getInt("item_count"));
				Timestamp minut = new Timestamp((System.currentTimeMillis() - rs.getTimestamp("time").getTime()));
				writeD((int) minut.getTime() / 60000);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_PLEDGE_WAREHOUSE_HISTORY;
	}
}

