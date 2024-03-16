package l1j.server.server.serverpackets;

import java.sql.*;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.Opcodes;
import l1j.server.server.utils.SQLUtil;

public class S_AuctionBoardRead extends ServerBasePacket {
	private static Logger _log = Logger.getLogger(S_AuctionBoardRead.class.getName());
	private static final String S_AUCTION_BOARD_READ = "[S] S_AuctionBoardRead";
	private byte[] _byte = null;

	public S_AuctionBoardRead(int objectId, String house_number) {
		buildPacket(objectId, house_number);
	}

	private void buildPacket(int objectId, String house_number) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			int number = Integer.valueOf(house_number);
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM board_auction WHERE house_id=?");
			pstm.setInt(1, number);
			rs = pstm.executeQuery();
			Calendar cal = null;
			while (rs.next()) {
				writeC(Opcodes.S_HYPERTEXT);
				writeD(objectId);
				writeS("agsel");
				writeS(house_number); // 아지트의 번호
				writeH(9); // 이하의 캐릭터 라인의 개수
				writeS(rs.getString(2)); // 아지트의 이름
				writeS(rs.getString(6)); // 아지트의 위치
				writeS(String.valueOf(rs.getString(3))); // 아지트의 넓이
				writeS(rs.getString(7)); // 이전의 소유자
				writeS(rs.getString(9)); // 현재의 입찰자
				writeS(String.valueOf(rs.getInt(5))); // 현재의 입찰 가격
				cal = timestampToCalendar((Timestamp) rs.getObject(4));
				int month = cal.get(Calendar.MONTH) + 1;
				int day = cal.get(Calendar.DATE);
				int hour = cal.get(Calendar.HOUR_OF_DAY);
				writeS(String.valueOf(month)); // 마감월
				writeS(String.valueOf(day)); // 마감일
				writeS(String.valueOf(hour)); // 마감시
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	private Calendar timestampToCalendar(Timestamp ts) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(ts.getTime());
		return cal;
	}

	@Override
	public byte[] getContent() {
		if (_byte == null)
			_byte = getBytes();
		return _byte;
	}
	@Override
	public String getType() {
		return S_AUCTION_BOARD_READ;
	}
}

