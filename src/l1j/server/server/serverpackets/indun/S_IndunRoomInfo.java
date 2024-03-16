package l1j.server.server.serverpackets.indun;

import l1j.server.IndunSystem.indun.IndunInfo;
import l1j.server.common.data.eDungeonType;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_IndunRoomInfo extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_INDUN_ROOM_INFO = "[S] S_IndunRoomInfo";
	public static final int ROOM_INFO 	= 0x08a6;
	
	public S_IndunRoomInfo(ArenaInfomationResult result, IndunInfo info) {
		write_init();
		write_result(result);
		write_room_info(info);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(ROOM_INFO);
	}
	
	void write_result(ArenaInfomationResult result) {
		writeRaw(0x08);// result
		writeRaw(result.value);
	}
	
	void write_room_info(IndunInfo info) {
		IndunRoomDetailInfoStream os = null;
		try {
			os = new IndunRoomDetailInfoStream();
			os.write_min_level(info.min_level);
			os.write_dungeon_type(eDungeonType.DEFENCE_TYPE);
			os.write_max_player(info.max_player);
			os.write_distribution_type(info.distribution_type);
			os.write_condition(info.fee, 0);
			os.write_map_kind(info.map_kind); // 방 타입
			
			writeRaw(0x12);// room_info
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
 	
 	public enum ArenaInfomationResult{
		SUCCESS(1),
		ERROR_INTENAL(2),
		ERROR_NOT_EXIST_USER(3),
		ERROR_NOT_EXIST_ROOM(4),
		;
		private int value;
		ArenaInfomationResult(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(ArenaInfomationResult v){
			return value == v.value;
		}
		public static ArenaInfomationResult fromInt(int i){
			switch(i){
			case 1:
				return SUCCESS;
			case 2:
				return ERROR_INTENAL;
			case 3:
				return ERROR_NOT_EXIST_USER;
			case 4:
				return ERROR_NOT_EXIST_ROOM;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eResult, %d", i));
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
		return S_INDUN_ROOM_INFO;
	}
}
