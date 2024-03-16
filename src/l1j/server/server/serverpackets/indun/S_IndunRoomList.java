package l1j.server.server.serverpackets.indun;

import l1j.server.IndunSystem.indun.IndunInfo;
import l1j.server.IndunSystem.indun.IndunList;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_IndunRoomList extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_INDUN_ROOM_LIST = "[S] S_IndunRoomList";
	public static final int ROOM_LIST 	= 0x08a4;
	
	public S_IndunRoomList() {
		write_init();
		write_result(ArenaResult.SUCCESS);
		write_page_id(1);
		write_total_page(1);
		write_room_info();
		writeH(0x00); // 버림수
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(ROOM_LIST);
	}
	
	void write_result(ArenaResult result) {
		writeRaw(0x08);// result
		writeRaw(result.value);
	}
	
	void write_page_id(int page_id) {
		writeRaw(0x10);// page_id
		writeRaw(page_id);
	}
	
	void write_total_page(int total_page) {
		writeRaw(0x18);// total_page
		writeRaw(total_page);
	}
	
	void write_room_info() {
		for (IndunInfo info : IndunList.getIndunInfoList().values()) {
			IndunRoomInfoStream os = null;
			try {
				os = new IndunRoomInfoStream(info);
				writeRaw(0x22);// room_info
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
 	
 	public enum ArenaResult{
		SUCCESS(1),
		ERROR_INTENAL(2),
		ERROR_NOT_EXIST_USER(3),
		ERROR_NOT_EXIST_ROOM(4),
		ERROR_ARENACO_DISCONNECTED(5),
		;
		private int value;
		ArenaResult(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(ArenaResult v){
			return value == v.value;
		}
		public static ArenaResult fromInt(int i){
			switch(i){
			case 1:
				return SUCCESS;
			case 2:
				return ERROR_INTENAL;
			case 3:
				return ERROR_NOT_EXIST_USER;
			case 4:
				return ERROR_NOT_EXIST_ROOM;
			case 5:
				return ERROR_ARENACO_DISCONNECTED;
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
		return S_INDUN_ROOM_LIST;
	}
}
