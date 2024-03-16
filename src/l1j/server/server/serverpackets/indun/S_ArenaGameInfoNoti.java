package l1j.server.server.serverpackets.indun;

import l1j.server.IndunSystem.indun.IndunInfo;
import l1j.server.common.data.eArenaMapKind;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_ArenaGameInfoNoti extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_ARENA_GAME_INFO_NOTI = "[S] S_ArenaGameInfoNoti";
	public static final int GAME_INFO_NOTI 	= 0x02dd;

 	public S_ArenaGameInfoNoti(L1PcInstance pc, boolean User, IndunInfo info) {
 		write_init();
 		write_player_info(info);
  		write_map_kind(info.map_kind);
  		write_round(1);
  		write_round_time_sec(info.map_kind == eArenaMapKind.Aurakia_Purification ? 1800 : 900);
  		write_result_display_time_sec(60);
		writeH(0x00);
  	}
 	
 	void write_init() {
 		writeC(Opcodes.S_EXTENDED_PROTOBUF);
  		writeH(GAME_INFO_NOTI);
 	}
 	
 	void write_player_info(IndunInfo info) {
 		for (L1PcInstance member : info.getMembers()) {
 			ArenaActorInfoStream os = null;
			try {
				os = new ArenaActorInfoStream(info, member);
				writeC(0x0a);// player_info
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
 	}
 	
 	void write_map_kind(eArenaMapKind map_kind) {
 		writeRaw(0x10);// map_kind
  		writeBit(map_kind.toInt());
 	}
 	
 	void write_round(int round) {
 		writeRaw(0x18);// round
 		writeRaw(round);
 	}
 	
 	void write_round_time_sec(int round_time_sec) {
 		writeRaw(0x20);// round_time_sec
  		writeBit(round_time_sec);
 	}
 	
 	void write_result_display_time_sec(int result_display_time_sec) {
 		writeRaw(0x28);// result_display_time_sec 타임 딜레이
 		writeRaw(result_display_time_sec);
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
		return S_ARENA_GAME_INFO_NOTI;
	}
}
