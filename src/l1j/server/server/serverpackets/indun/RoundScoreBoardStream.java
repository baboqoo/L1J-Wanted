package l1j.server.server.serverpackets.indun;

import l1j.server.server.utils.BinaryOutputStream;

public class RoundScoreBoardStream extends BinaryOutputStream {
	public RoundScoreBoardStream() {
		super();
	}
	
	void write_team_a(boolean is_win, int attack_amount) {
		RoundScore os = null;
		try {
			os = new RoundScore(is_win, attack_amount);
			writeC(0x0a);
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
	
	void write_team_b(boolean is_win, int attack_amount) {
		RoundScore os = null;
		try {
			os = new RoundScore(is_win, attack_amount);
			writeC(0x12);
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
	
	public static class RoundScore extends BinaryOutputStream {
		public RoundScore(boolean is_win, int attack_amount) {
			super();
			write_is_win(is_win);
			write_attack_amount(attack_amount);
		}
		
		void write_is_win(boolean is_win) {
			writeC(0x08);
			writeB(is_win);
		}
		
		void write_attack_amount(int attack_amount) {
			writeC(0x10);
			writeBit(attack_amount);
		}
	}
}

