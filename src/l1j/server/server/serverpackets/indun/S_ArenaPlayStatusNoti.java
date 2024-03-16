package l1j.server.server.serverpackets.indun;

import l1j.server.IndunSystem.indun.IndunInfo;
import l1j.server.common.data.eArenaTeam;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_ArenaPlayStatusNoti extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_ARENA_PLAY_STATUS_NOTI = "[S] S_ArenaPlayStatusNoti";
	public static final int PLAY_STATUS_NOTI = 0x02de;
  	
 	public S_ArenaPlayStatusNoti(L1PcInstance pc, boolean User, IndunInfo info) {
 		write_init();

		if (User) {
			for (L1PcInstance member : info.getMembers()) {
				if (member == pc) {
					continue;
				}
				ArenaPlayerStatusStream os = null;
				try {
					os = new ArenaPlayerStatusStream();
					os.write_arena_char_id(member.getId());
					os.write_kill_count(0);
					os.write_death_count(0);
					os.write_hp_ratio(0);
					os.write_mp_ratio(0);
					os.write_loc_x(0);
					os.write_loc_y(0);
					os.write_poisoned(false);
					os.write_paralysed(false);
					os.write_obj_id(0);
					writeRaw(0x0a);// player_status
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
			
			ArenaGameStatusStream os = null;
			try {
				os = new ArenaGameStatusStream();
				os.write_play_time_msec(0);
				os.write_is_timer_run(false);
				os.write_observer_count(0);
				os.write_team_status(eArenaTeam.TEAM_A, 0, 0);
				writeRaw(0x12);// game_status
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
		} else {
			ArenaPlayerStatusStream os = null;
			try {
				os = new ArenaPlayerStatusStream();
				os.write_arena_char_id(pc.getId());
				os.write_kill_count(0);
				os.write_death_count(0);
				os.write_hp_ratio(pc.getCurrentHpPercent());
				os.write_mp_ratio(pc.getCurrentMpPercent());
				os.write_loc_x(pc.getX());
				os.write_loc_y(pc.getY());
				os.write_poisoned(pc.getPoison() != null);
				os.write_paralysed(pc.isParalyzed() || pc.isPoisonParalyzed());
				os.write_is_live(true);
				os.write_obj_id(pc.getId());
				
				writeRaw(0x0a);// player_status
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

		writeH(0x00);
  	}
 	
 	public S_ArenaPlayStatusNoti(IndunInfo info, L1PcInstance pc) {
 		write_init();
  		
  		if (pc == null) {
  			for (L1PcInstance member : info.getMembers()) {
  				int objId = member.getId();
  				writeRaw(0x0a);// player_status
				writeBit(getBitSize(objId) + 1); // 길이
					
				writeRaw(0x08);// arena_char_id
				writeBit(objId);
  	  		}
  			
  			writeRaw(0x12);// game_status
  			writeRaw(4);// 길이
				
  			writeRaw(0x08);// play_time_msec
  			writeRaw(0);
	  		
  			writeRaw(0x10);// is_timer_run
	  		writeB(true);
  		} else {
  			int objId = pc.getId();
  			writeRaw(0x12);// game_status
			writeBit(getBitSize(objId) + 3); // 길이
			
			writeRaw(0x08);// play_time_msec
			writeBit(objId);
		  	
			writeRaw(0x10);// is_timer_run
			writeB(false);
  		}
  		writeH(0x00);
 	}

 	void write_init() {
 		writeC(Opcodes.S_EXTENDED_PROTOBUF);
  		writeH(PLAY_STATUS_NOTI);
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
		return S_ARENA_PLAY_STATUS_NOTI;
	}
}
