package l1j.server.server.serverpackets.indun;

import l1j.server.common.data.eArenaTeam;
import l1j.server.server.utils.BinaryOutputStream;

public class ArenaGameStatusStream extends BinaryOutputStream {

	public ArenaGameStatusStream() {
		super();
	}
	
	void write_play_time_msec(int play_time_msec) {
		writeC(0x08);// play_time_msec
		writeBit(play_time_msec);
	}
	
	void write_is_timer_run(boolean is_timer_run) {
		writeC(0x10);// is_timer_run
		writeB(is_timer_run);
	}
	
	void write_observer_count(int observer_count) {
		writeC(0x18);// observer_count
		writeC(observer_count);
	}
	
	void write_round_score_board(boolean team_a_is_win, int team_a_attack_amount, boolean team_b_is_win, int team_b_attack_amount) {
		RoundScoreBoardStream os = null;
		try {
			os = new RoundScoreBoardStream();
			os.write_team_a(team_a_is_win, team_a_attack_amount);
			os.write_team_b(team_b_is_win, team_b_attack_amount);
			writeC(0x22);
			writeBytesWithLength(os.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
					os = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	void write_team_status(eArenaTeam team_id, int cheer_msg_count, int team_kill_count) {
		TeamStatus os = null;
		try {
			os = new TeamStatus(team_id, cheer_msg_count, team_kill_count);
			writeC(0x2a);
			writeBytesWithLength(os.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
					os = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static class TeamStatus extends BinaryOutputStream {
		public TeamStatus(eArenaTeam team_id, int cheer_msg_count, int team_kill_count) {
			super();
			write_team_id(team_id);
			write_cheer_msg_count(cheer_msg_count);
			write_team_kill_count(team_kill_count);
		}
		
		void write_team_id(eArenaTeam team_id) {
			writeC(0x08);
			writeC(team_id.toInt());
		}
		
		void write_cheer_msg_count(int cheer_msg_count) {
			writeC(0x10);
			writeC(cheer_msg_count);
		}
		
		void write_team_kill_count(int team_kill_count) {
			writeC(0x18);
			writeC(team_kill_count);
		}
	}
}

