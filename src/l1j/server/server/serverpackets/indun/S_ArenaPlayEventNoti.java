package l1j.server.server.serverpackets.indun;

import l1j.server.IndunSystem.indun.IndunInfo;
import l1j.server.common.data.eArenaTeam;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_ArenaPlayEventNoti extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_ARENA_PLAY_EVENT_NOTI = "[S] S_ArenaPlayEventNoti";
	public static final int PLAY_EVENT_NOTI = 0x02df;
  	
 	public S_ArenaPlayEventNoti(L1PcInstance pc, S_ArenaPlayEventNoti.eType type, IndunInfo info) {
 		write_init();
	  	write_type(type);
	  	write_actor_info(info, pc);
		writeH(0x00);
  	}
 	
 	void write_init() {
 		writeC(Opcodes.S_EXTENDED_PROTOBUF);
  		writeH(PLAY_EVENT_NOTI);
 	}
 	
 	void write_type(S_ArenaPlayEventNoti.eType type) {
 		writeRaw(0x08);// type
 		writeRaw(type.value);
 	}
 	
 	void write_actor_info(IndunInfo info, L1PcInstance pc) {
 		ArenaActorInfoStream os = null;
	  	try {
	  		os = new ArenaActorInfoStream(info, pc);
	  		writeRaw(0x52);// actor_info
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
 	
 	void write_arena_char_id_target(long arena_char_id_target) {
 		writeRaw(0x01a0);
 		writeBit(arena_char_id_target);
 	}
 	
 	void write_arena_char_id_causer(long arena_char_id_causer) {
 		writeRaw(0x01a8);
 		writeBit(arena_char_id_causer);
 	}
 	
 	void write_team_id(eArenaTeam team_id) {
 		writeRaw(0x01f0);
 		writeRaw(team_id.toInt());
 	}
 	
 	void write_spell_id(int spell_id) {
 		writeRaw(0x02c2);
 		writeBit(spell_id);
 	}

 	public enum eType{
		GameForfeited(1),
		GameCountDown3Sec(2),
		GameCountDown10Sec(3),
		PlayerEntered(11),
		PlayerOut(12),
		PlayerDead(13),
		PlayerSpellSucceded(14),
		YourSelfEntered(21),
		BattleHunterWin(31),
		TeamTowerBroke(41),
		;
		private int value;
		eType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eType v){
			return value == v.value;
		}
		public static eType fromInt(int i){
			switch(i){
			case 1:
				return GameForfeited;
			case 2:
				return GameCountDown3Sec;
			case 3:
				return GameCountDown10Sec;
			case 11:
				return PlayerEntered;
			case 12:
				return PlayerOut;
			case 13:
				return PlayerDead;
			case 14:
				return PlayerSpellSucceded;
			case 21:
				return YourSelfEntered;
			case 31:
				return BattleHunterWin;
			case 41:
				return TeamTowerBroke;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eType, %d", i));
			}
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
		return S_ARENA_PLAY_EVENT_NOTI;
	}
}
