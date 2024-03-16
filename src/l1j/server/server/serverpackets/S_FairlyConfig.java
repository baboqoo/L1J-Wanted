package l1j.server.server.serverpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

public class S_FairlyConfig extends ServerBasePacket {

	private static Logger _log = Logger.getLogger(S_FairlyConfig.class.getName());
	private static final String S_CHARACTER_CONFIG = "[S] S_CharacterConfig";
	private byte[] _byte = null;

	public static final S_FairlyConfig FAIRLY_NOTI = new S_FairlyConfig();
	
	public S_FairlyConfig() {
		writeC(Opcodes.S_EVENT);
		writeC(0xbc);
		for (int i = 0; i < 512; i++) {
			writeC(0x00);
		}
	}
	
	public S_FairlyConfig(L1PcInstance pc) {
		buildPacket(pc);
	}

	private void buildPacket(L1PcInstance pc) {
		byte data[] = null;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int ok = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_Fairly_Config WHERE object_id=?");
			pstm.setInt(1, pc.getId());
			rs = pstm.executeQuery();
			if (rs.next()) {
				data = rs.getBytes(2);
				ok = 1;
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		writeC(Opcodes.S_EVENT);
		writeC(0xbc);
		if (ok != 0) {
			writeByte(data);
			pc.getConfig().setFairlyInfo(data);
		} else {
			for (int i = 0; i < 512; i++) {
				writeC(0x00);
			}
		}
	}

	@Override
	public byte[] getContent() {
		if (_byte == null)
			_byte = getBytes();
		return _byte;
	}

	@Override
	public String getType() {
		return S_CHARACTER_CONFIG;
	}
}

