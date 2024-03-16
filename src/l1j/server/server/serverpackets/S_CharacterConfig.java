package l1j.server.server.serverpackets;

import java.util.logging.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.Opcodes;
import l1j.server.server.utils.SQLUtil;

public class S_CharacterConfig extends ServerBasePacket {
	private static Logger _log = Logger.getLogger(S_CharacterConfig.class.getName());
	private static final String S_CHARACTER_CONFIG = "[S] S_CharacterConfig";
	private byte[] _byte = null;

	public S_CharacterConfig(int objectId) {
		buildPacket(objectId);
	}

	private void buildPacket(int objectId) {
		int length = 0;
		byte data[] = null;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_config WHERE object_id=?");
			pstm.setInt(1, objectId);
			rs = pstm.executeQuery();
			if (rs.next()) {
				length	= rs.getInt(2);
				data	= rs.getBytes(3);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		if (length != 0) {
			writeC(Opcodes.S_EVENT);
			writeC(0x29);
			writeD(length);
			writeByte(data);
			writeH(0x00);
		} else {
			writeC(Opcodes.S_EVENT);
			writeC(0x29);
			writeD(0);
			writeH(0x00);
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
		return S_CHARACTER_CONFIG;
	}
}

