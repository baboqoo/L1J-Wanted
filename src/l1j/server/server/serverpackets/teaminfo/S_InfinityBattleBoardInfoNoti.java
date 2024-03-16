package l1j.server.server.serverpackets.teaminfo;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_InfinityBattleBoardInfoNoti extends ServerBasePacket {
	private static final String S_INFINITY_BATTLE_BOARD_INFO_NOTI = "[S] S_InfinityBattleBoardInfoNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x093b;
	
	public S_InfinityBattleBoardInfoNoti(int team1_progress, int team2_progress, int team3_progress, int team4_progress) {
		write_init();
		write_board_info(team1_progress, team2_progress, team3_progress, team4_progress);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_board_info(int team1_progress, int team2_progress, int team3_progress, int team4_progress) {
		for (int team_id=1; team_id<5; team_id++) {
			int progress_rate = 0;
			switch (team_id) {
			case 1:
				progress_rate = team1_progress;
				break;
			case 2:
				progress_rate = team2_progress;
				break;
			case 3:
				progress_rate = team3_progress;
				break;
			case 4:
				progress_rate = team4_progress;
				break;
			}
			writeRaw(0x0a);// board_info
			writeRaw(getBitSize(progress_rate) + 3);
			write_team_id(team_id);
			write_progress_rate(progress_rate);
		}
	}
	
	void write_team_id(int team_id) {
		writeRaw(0x08);// team_id
		writeRaw(team_id);
	}
	
	void write_progress_rate(int progress_rate) {
		writeRaw(0x10);// progress_rate
		writeBit(progress_rate);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_INFINITY_BATTLE_BOARD_INFO_NOTI;
	}
}

