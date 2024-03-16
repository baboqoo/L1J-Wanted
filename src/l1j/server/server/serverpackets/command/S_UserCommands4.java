package l1j.server.server.serverpackets.command;

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
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class S_UserCommands4 extends ServerBasePacket {

	private static final String S_UserCommands4 = "[C] S_UserCommands4";

	private static Logger _log = Logger.getLogger(S_UserCommands4.class.getName());

	private byte[] _byte = null;

	private int j = 0;

	static String[] name;

	public S_UserCommands4(L1PcInstance pc, int number) {
		name = new String[10];
		buildPacket(pc, number);
	}

	private void buildPacket(L1PcInstance pc, int number) {
		String date = time();
		String type = null;
		String title = null;
		writeC(Opcodes.S_BOARD_READ);
		writeD(number);
		//writeS("랭킹 관리자");
		writeS("Ranking Manager");
		switch (number) {
		case 1:
			//title = "킬 랭킹";
			title = "Kill Ranking";
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
			//writeS("\n\r\n\r\n\r" + pc.getName() + "님의 전체랭킹 : " + p + "위"
			//		+ "\n\r\n\r\n\r" + type + " 클래스 랭킹 : " + j + "위"
			writeS("\n\r\n\r\n\r" + pc.getName() + "'s overall ranking: " + p + "above"
			+ "\n\r\n\r\n\r" + type + " Class Ranking: " + j + "above"			
					+ "\n\r\n\r\n\r" + "        ");
		} else {
			/*writeS("\n\r" + "   1위 " + name[0] + "\n\r" +
		                    "   2위 " + name[1] + "\n\r" +
					        "   3위 " + name[2] + "\n\r" +
					        "   4위 " + name[3] + "\n\r" +
					        "   5위 " + name[4] + "\n\r" +
					        "   6위 " + name[5] + "\n\r" +
					        "   7위 " + name[6] + "\n\r" +
					        "   8위 " + name[7] + "\n\r" +
					        "   9위 " + name[8] + "\n\r" +
					        "  10위 " + name[9] + "\n\r" +*/
							writeS("\n\r" + " 1st place " + name[0] + "\n\r" +
							" 2nd place " + name[1] + "\n\r" +
							" 3rd place " + name[2] + "\n\r" +
							" 4th place " + name[3] + "\n\r" +
							" 5th place " + name[4] + "\n\r" +
							" 6th place " + name[5] + "\n\r" +
							" 7th place " + name[6] + "\n\r" +
							" 8th place " + name[7] + "\n\r" +
							" 9th place " + name[8] + "\n\r" +
							" 10th place " + name[9] + "\n\r" +							
			                "             ");
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
				pstm = con.prepareStatement("SELECT char_name FROM characters WHERE AccessLevel = 0 order by PC_Kill desc limit 10");
				break;
			default:
				pstm = con.prepareStatement("SELECT char_name FROM characters WHERE AccessLevel = 0 order by PC_Kill desc limit 10");
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
				sql = (new StringBuilder(String.valueOf(sql))).append(" And AccessLevel = 0 order by PC_Kill desc").toString();
				pstm1 = con.prepareStatement(sql);
				rs1 = pstm1.executeQuery();
				j = 0;
				while (rs1.next()) {
					j++;
					if (objid == rs1.getInt(1))
						break;
				}
			} else if (number == 1) { // 추가부분입니다
				while (rs.next()) {
					name[i] = rs.getString(1);
					i++;
				} 
			} else {
				while (rs.next()) {
					name[i] = rs.getString(1);
					i++;
				}

				// 레코드가 없거나 5보다 작을때
				while (i < 10) {
					//name[i] = "없음.";
					name[i] = "None.";
					i++;
				}
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "S_UserCommands4[]Error", e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(rs1);
			SQLUtil.close(pstm);
			SQLUtil.close(pstm1);
			SQLUtil.close(con);
		}

		return i;
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
		return S_UserCommands4;
	}

}

