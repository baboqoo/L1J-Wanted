package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.utils.BinaryOutputStream;

public class S_BasecampChartNoti extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_BASECAMP_CHART_NOTI = "[S] S_BasecampChartNoti";
	public static final int CHART_NOTI	= 0x007f;
	
	public S_BasecampChartNoti(boolean onoff, java.util.LinkedList<S_BasecampChartNoti.CHART_INFO> charts, int team_points, int winner_team_id) {
		write_init();
		if (onoff) {
			if (charts != null && !charts.isEmpty()) {
				write_charts(charts);
			}
			write_team_points(team_points);
			if (winner_team_id > 0) {
				write_winner_team_id(winner_team_id);
			}
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(CHART_NOTI);
	}
	
	void write_charts(java.util.LinkedList<S_BasecampChartNoti.CHART_INFO> charts) {
		for (S_BasecampChartNoti.CHART_INFO chart : charts) {
			writeC(0x0a);// charts
			writeBytesWithLength(get_charts(chart._user_name, chart._user_points));
		}
	}
	
	void write_team_points(int team_points) {
		writeC(0x10);// team_points
		writeBit(team_points);
	}
	
	void write_winner_team_id(int winner_team_id) {
		writeC(0x18);// winner_team_id 이미지 1:나뭇잎, 2:해골, 3:불꽃
		writeC(winner_team_id);
	}
	
	byte[] get_charts(String user_name, long user_points){
		BinaryOutputStream os = new BinaryOutputStream();
		try {
			os.writeC(0x0a);// user_name
			os.writeStringWithLength(user_name);
				
			os.writeC(0x10);// user_points
			os.writeBit(user_points);
			return os.getBytes();
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			if(os != null){
				try {
					os.close();
					os = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public static class CHART_INFO {
		private String _user_name;
		private long _user_points;
		public CHART_INFO(String _user_name, long _user_points) {
			this._user_name = _user_name;
			this._user_points = _user_points;
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
		return S_BASECAMP_CHART_NOTI;
	}
}
