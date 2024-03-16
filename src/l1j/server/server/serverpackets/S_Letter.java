package l1j.server.server.serverpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.utils.SQLUtil;

public class S_Letter extends ServerBasePacket {
	private static Logger _log = Logger.getLogger(S_Letter.class.getName());
	private static final String S_LETTER = "[S] S_Letter";
	private byte[] _byte = null;

	public S_Letter(L1ItemInstance item) {
		buildPacket(item);
	}

	private void buildPacket(L1ItemInstance item) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM letter WHERE item_object_id=?");
			pstm.setInt(1, item.getId());
			rs = pstm.executeQuery();
			while (rs.next()) {
				writeC(Opcodes. S_MAIL_INFO);
				writeD(item.getId());
				if (item.getIconId() == 465) {// 열기 전
					writeH(466); // 열기 후
				} else if (item.getIconId() == 606) {
					writeH(605);
				} else if (item.getIconId() == 616) {
					writeH(615);
				} else {
					writeH(item.getIconId());
				}
				writeH(rs.getInt(2));
				writeS(rs.getString(3));
				writeS(rs.getString(4));
				writeByte(rs.getBytes(7));
				writeByte(rs.getBytes(8));
				writeC(rs.getInt(6)); // 텐프레
				writeS(rs.getString(5)); // 일자
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
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
		return S_LETTER;
	}
}

