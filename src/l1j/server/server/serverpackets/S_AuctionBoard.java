package l1j.server.server.serverpackets;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;

public class S_AuctionBoard extends ServerBasePacket {
	private static Logger _log = Logger.getLogger(S_AuctionBoard.class.getName());
	private static final String S_AUCTION_BOARD = "[S] S_AuctionBoard";
	private byte[] _byte = null;

	public S_AuctionBoard(L1NpcInstance board, L1PcInstance pc) {
		buildPacket(board, pc);
	}

	private void buildPacket(L1NpcInstance board, L1PcInstance pc) {
		ArrayList<Integer> houseList = new ArrayList<Integer>();
		int houseId = 0;
		int count = 0;
		int[] id = null;
		String[] name = null;
		int[] area = null;
		int[] month = null;
		int[] day = null;
		int[] time = null;
		int[] price = null;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			
			try {
				pstm = con.prepareStatement("SELECT * FROM board_auction");
				rs = pstm.executeQuery();
				while (rs.next()) {
					houseId = rs.getInt(1);
					if (board.getX() == 33424 && board.getY() == 32824) { // 경매 게시판(기란)
						if (houseId >= 262145 && houseId <= 262189) {
							houseList.add(houseId);
							count++;
						}
					} else if (board.getX() == 33585 && board.getY() == 33235) { // 경매 게시판(하이네)
						if (houseId >= 327681 && houseId <= 327691) {
							houseList.add(houseId);
							count++;
						}
					} else if (board.getX() == 33959 && board.getY() == 33253) { // 경매 게시판(아덴)
						if (houseId >= 458753 && houseId <= 458819) {
							houseList.add(houseId);
							count++;
						}
					} else if (board.getX() == 32611 && board.getY() == 32775) { // 경매 게시판(글루디오)
						if (houseId >= 65537 && houseId <= 65542) {
							houseList.add(houseId);
							count++;
						}
					}
				}
			} catch (SQLException e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} finally {
				SQLUtil.close(rs, pstm);
			}

			try {
				id = new int[count];
				name = new String[count];
				area = new int[count];
				month = new int[count];
				day = new int[count];
				time = new int[count];
				price = new int[count];
				Calendar cal = null;
				for (int i = 0; i < count; ++i) {
					pstm = con.prepareStatement("SELECT * FROM board_auction WHERE house_id=?");
					houseId = houseList.get(i);
					pstm.setInt(1, houseId);
					rs = pstm.executeQuery();
					while (rs.next()) {
						id[i] = rs.getInt(1);
						name[i] = rs.getString(2);
						area[i] = rs.getInt(3);
						cal = timestampToCalendar((Timestamp) rs.getObject(4));
						month[i] = cal.get(Calendar.MONTH) + 1;
						day[i] = cal.get(Calendar.DATE);
						time[i] = cal.get(Calendar.HOUR_OF_DAY);
						price[i] = rs.getInt(5);
					}
					SQLUtil.close(rs, pstm);
				}
			} catch (SQLException e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} finally {
				SQLUtil.close(rs, pstm);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		
		int current = 0;
		for (int i = 0; i < count; ++i) {
			if (price[i] > 0){
				current++;;
			}
		}
		if (current == 0){
			if (pc.isGm())
				pc.sendPackets(new S_SystemMessage("Dialog " + "agnolist"), true);																
			
			pc.sendPackets(new S_NPCTalkReturn(board.getId(), "agnolist"), true);
		} else {
			writeC(Opcodes.S_AGIT_LIST);
		    writeD(board.getId());
		    writeH(count); // 레코드수
			for (int i = 0; i < count; ++i) {
				writeD(id[i]); // 아지트의 번호
				writeS(name[i]); // 아지트의 이름
				writeH(area[i]); // 아지트의 넓이
				writeC(month[i]); // 마감월
				writeC(day[i]); // 마감일
				writeH(time[i]); // 마감시간
				writeD(price[i]); // 현재의 입찰 가격
			}
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
		return S_AUCTION_BOARD;
	}
}

