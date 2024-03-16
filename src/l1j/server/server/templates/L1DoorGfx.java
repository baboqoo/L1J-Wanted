package l1j.server.server.templates;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

public class L1DoorGfx {
	private static Logger _log = Logger.getLogger(L1DoorGfx.class.getName());
	private final int _gfxId;
	private final int _direction;
	private final int _rightEdgeOffset;
	private final int _leftEdgeOffset;

	public L1DoorGfx(int gfxId, int direction, int rightEdgeOffset, int leftEdgeOffset) {
		_gfxId = gfxId;
		_direction = direction;
		_rightEdgeOffset = rightEdgeOffset;
		_leftEdgeOffset = leftEdgeOffset;
	}

	public int getGfxId() {
		return _gfxId;
	}

	public int getDirection() {
		return _direction;
	}

	public int getRightEdgeOffset() {
		return _rightEdgeOffset;
	}

	public int getLeftEdgeOffset() {
		return _leftEdgeOffset;
	}

	public static L1DoorGfx findByGfxId(int gfxId) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM door_gfxs WHERE gfx_id=?");
			pstm.setInt(1, gfxId);
			rs = pstm.executeQuery();
			if(!rs.next())
				return null;
			int id = rs.getInt("gfx_id");
			int dir = rs.getInt("direction");
			int rEdge = rs.getInt("right_edge_offset");
			int lEdge = rs.getInt("left_edge_offset");
			return new L1DoorGfx(id, dir, rEdge, lEdge);
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return null;
	}
}

