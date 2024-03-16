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

public class S_ReadLetter extends ServerBasePacket{
	private static Logger _log = Logger.getLogger(S_Letter.class.getName());
	private static final String S_READ_LETTER = "[S] S_ReadLetter";
	private byte[] _byte = null;
	
	public S_ReadLetter(L1PcInstance pc, int type, int letterType, int id) {
		buildPacket(pc,type,letterType,id);
	}
	private void buildPacket(L1PcInstance pc, int type, int letterType, int id) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM letter WHERE item_object_id = ? ");
			pstm.setInt(1, id);
			rs = pstm.executeQuery();
			
			writeC(Opcodes. S_MAIL_INFO);
			writeC(type); // 16:메일함 17:혈맹메일
			
			if (rs.next()) {
				writeD(rs.getInt(1));   // 게시글 넘버
				writeSS(rs.getString(8)); // 내용
				writeC(id); // 일
				writeS(rs.getString(3)); // 보낸사람
				writeSS(rs.getString(7)); // 제목
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

	public String getType() {
		return S_READ_LETTER;
	}
}

