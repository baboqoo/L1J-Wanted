package l1j.server.server.serverpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class S_Ranking extends ServerBasePacket {

	private static final String S_Ranking = "[C] S_Ranking";

	private static Logger _log = Logger.getLogger(S_Ranking.class.getName());

	private byte[] _byte = null;

	private int j = 0;

	static String[] name;

	public S_Ranking(L1PcInstance pc, int number) {
		name = new String[10];
		buildPacket(pc, number);
	}

	public S_Ranking(String text) {
		buildPacket(text);
	}

	private void buildPacket(L1PcInstance pc, int number) {
		String date = time();
		String type = null;
		String title = null;
		writeC(Opcodes.S_BOARD_READ);
		writeD(number);
		/*writeS("랭킹 관리자");
		switch (number) {
		case 1:
			// title = "전체 랭킹";
			title = "전사 랭킹";
			break;
		case 2:
			title = "군주 랭킹";
			break;
		case 3:
			title = "기사 랭킹";
			break;
		case 4:
			title = "요정 랭킹";
			break;
		case 5:
			title = "법사 랭킹";
			break;
		case 6:
			title = "다엘 랭킹";
			break;
		case 7:
			title = "용기사 랭킹";
			break;
		case 8:
			title = "환술사 랭킹";
			break;
		case 9:
			title = "검사 랭킹";
			break;
		case 10:
			title = "창기사 랭킹";
			break;
		}*/
		writeS("Ranking Manager");
		switch (number) {
		case 1:
		// title = "Overall Ranking";
		title = "Warrior Ranking";
		break;
		case 2:
		title = "Royal Ranking";
		break;
		case 3:
		title = "Knight Ranking";
		break;
		case 4:
		title = "Elf Ranking";
		break;
		case 5:
		title = "Wizard Ranking";
		break;
		case 6:
		title = "Dark Elf Ranking";
		break;
		case 7:
		title = "Dragon Knight Ranking";
		break;
		case 8:
		title = "Illusionist Ranking";
		break;
		case 9:
		title = "Fencer Ranking";
		break;
		case 10:
		title = "Lancer Ranking";
		break;
		}		
		writeS(title);
		writeS(date);
		switch (pc.getType()) {
		/*case 0:type = "군주";break;
		case 1:type = "기사";break;
		case 2:type = "요정";break;
		case 3:type = "마법사";break;
		case 4:type = "다크엘프";break;
		case 5:type = "용기사";break;
		case 6:type = "환술사";break;
		case 7:type = "전사";break;
		case 8:type = "검사";break;
		case 9:type = "창기사";break;*/
		case 0:type = "Royal";break;
		case 1:type = "Knight";break;
		case 2:type = "Elf";break;
		case 3:type = "Wizard";break;
		case 4:type = "Dark Elf";break;
		case 5:type = "Dragon Knight";break;
		case 6:type = "Illusionist";break;
		case 7:type = "Warrior";break;
		case 8:type = "Fencer";break;
		case 9:type = "Lancer";break;
		}		
		int p = Rank(pc, number);
		if (number == 9) {
			//writeS("\n\r\n\r\n\r" + pc.getName() + "님의 전체랭킹 : " + p + "위" + "\n\r\n\r\n\r" + type + " 클래스 랭킹 : " + j + "위"
			writeS("\n\r\n\r\n\r" + pc.getName() + "'s overall ranking: " + p + "top" + "\n\r\n\r\n\r " + type + " class ranking: " + j + "above"
					+ "\n\r\n\r\n\r" + "        ");

		} else {
			//writeS("\n\r" + "     1위 " + name[0] + "\n\r" + "     2위 " + name[1] + "\n\r" + "     3위 " + name[2] + "\n\r"
			//		+ "     4위 " + name[3] + "\n\r" + "     5위 " + name[4] + "\n\r" + "     6위 " + name[5] + "\n\r" + "     7위 "
			//		+ name[6] + "\n\r" + "     8위 " + name[7] + "\n\r" + "     9위 " + name[8] + "\n\r" + "    10위 " + name[9]
			writeS("\n\r" + " 1st place " + name[0] + "\n\r" + " 2nd place " + name[1] + "\n\r" + " 3rd place " + name[ 2] + "\n\r"
			+ " 4th place " + name[3] + "\n\r" + " 5th place " + name[4] + "\n\r" + " 6th place " + name[5] + "\n\r " + " 7th place "
			+ name[6] + "\n\r" + " 8th place " + name[7] + "\n\r" + " 9th place " + name[8] + "\n\r" + " 10th place " + name[9]			
					+ "\n\r" + "             ");
		}
	}

	private int Rank(L1PcInstance pc, int number) {
		Connection con = null;
		PreparedStatement pstm = null;
		PreparedStatement pstm1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		int objid = pc.getId();
		int i = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			switch (number) {
			case 1:
				pstm = con
				// .prepareStatement("SELECT char_name FROM characters WHERE AccessLevel = 0 order by Exp desc limit 10");
				// //전체랭킹 주석
						.prepareStatement("SELECT char_name FROM characters WHERE Type = 7 And AccessLevel = 0 order by Exp desc limit 10");
				break;
			case 2:
				pstm = con
						.prepareStatement("SELECT char_name FROM characters WHERE Type = 0 And AccessLevel = 0 order by Exp desc limit 10");
				break;
			case 3:
				pstm = con
						.prepareStatement("SELECT char_name FROM characters WHERE Type = 1 And AccessLevel = 0 order by Exp desc limit 10");
				break;
			case 4:
				pstm = con
						.prepareStatement("SELECT char_name FROM characters WHERE Type = 2 And AccessLevel = 0 order by Exp desc limit 10");
				break;
			case 5:
				pstm = con
						.prepareStatement("SELECT char_name FROM characters WHERE Type = 3 And AccessLevel = 0 order by Exp desc limit 10");
				break;
			case 6:
				pstm = con
						.prepareStatement("SELECT char_name FROM characters WHERE Type = 4 And AccessLevel = 0 order by Exp desc limit 10");
				break;
			case 7:
				pstm = con
						.prepareStatement("SELECT char_name FROM characters WHERE Type = 5 And AccessLevel = 0 order by Exp desc limit 10");
				break;
			case 8:
				pstm = con
						.prepareStatement("SELECT char_name FROM characters WHERE Type = 6 And AccessLevel = 0 order by Exp desc limit 10");
				break;
			case 9:
				pstm = con.prepareStatement("SELECT objid FROM characters WHERE AccessLevel = 0 order by Exp desc");
				break;

			default:
				pstm = con.prepareStatement("SELECT char_name FROM characters WHERE AccessLevel = 0 order by Exp desc limit 10");
				break;
			}

			rs = pstm.executeQuery();
			if (number == 9) {
				while (rs.next()) {
					i++;
					if (objid == rs.getInt(1))
						break;
				}
				String sql = "SELECT objid FROM characters WHERE Type = ";
				sql = (new StringBuilder(String.valueOf(sql))).append(pc.getType()).toString();
				sql = (new StringBuilder(String.valueOf(sql))).append(" And AccessLevel = 0 order by Exp desc").toString();
				pstm1 = con.prepareStatement(sql);
				rs1 = pstm1.executeQuery();
				j = 0;
				while (rs1.next()) {
					j++;
					if (objid == rs1.getInt(1))
						break;
				}
			} else {
				while (rs.next()) {
					name[i] = rs.getString(1);
					i++;
				}

				// 레코드가 없거나 5보다 작을때
				while (i < 10) {
					name[i] = StringUtil.EmptyOneString;
					i++;
				}
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "S_Ranking[]Error", e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(rs1);
			SQLUtil.close(pstm);
			SQLUtil.close(pstm1);
			SQLUtil.close(con);
		}

		return i;
	}

	private void buildPacket(String text) {
		String date = time();
		String title = null;
		writeC(Opcodes.S_BOARD_READ);
		writeD(0);
		//writeS("창고 내역");
		writeS("Warehouse Details");
		writeS(title);
		writeS(date);
		writeS(text);
	}

	private static String time() {
		TimeZone tz = TimeZone.getTimeZone(Config.SERVER.TIME_ZONE);
		Calendar cal = Calendar.getInstance(tz);
		int year = cal.get(Calendar.YEAR) - 2000;
		String year2;
		if (year < 10)
			year2 = StringUtil.ZeroString + year;
		else
			year2 = Integer.toString(year);
		
		int Month = cal.get(Calendar.MONTH) + 1;
		String Month2 = null;
		if (Month < 10)
			Month2 = StringUtil.ZeroString + Month;
		else
			Month2 = Integer.toString(Month);
		
		int date = cal.get(Calendar.DATE);
		String date2 = null;
		if (date < 10)
			date2 = StringUtil.ZeroString + date;
		else
			date2 = Integer.toString(date);
		return year2 + StringUtil.SlushString + Month2 + StringUtil.SlushString + date2;
	}

	@Override
	public byte[] getContent() {
		if (_byte == null)
			_byte = getBytes();
		return _byte;
	}

	public String getType() {
		return S_Ranking;
	}

}

