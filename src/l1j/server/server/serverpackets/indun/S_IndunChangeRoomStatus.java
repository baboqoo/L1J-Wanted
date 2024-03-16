package l1j.server.server.serverpackets.indun;

import l1j.server.IndunSystem.indun.IndunInfo;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_IndunChangeRoomStatus extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_INDUN_CHANGE_ROOM_STATUS = "[S] S_IndunChangeRoomStatus";
	public static final int CHANGE_ROOM_STATUS 	= 0x08ab;
	
	public S_IndunChangeRoomStatus(IndunInfo info) {
		write_init();
		write_room_id(info.room_id);
		write_observer_count(0);
		write_player_info(info);
		write_room_info(info);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(CHANGE_ROOM_STATUS);
	}
	
	void write_room_id(int room_id) {
		writeC(0x08);// room_id
		writeBit(room_id);
	}
	
	void write_observer_count(int observer_count) {
		writeC(0x10);// observer_count
		writeC(observer_count);
	}
	
	void write_player_info(IndunInfo info) {
		for (L1PcInstance member : info.getMembers()) {
			ArenaUserInfoStream os = null;
			try {
				os = new ArenaUserInfoStream(info, member);
				writeC(0x1a);// player_info
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
	
	void write_room_info(IndunInfo info) {
		IndunRoomInfoStream os = null;
		try {
			os = new IndunRoomInfoStream(info);
			writeC(0x22);// room_info
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

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_INDUN_CHANGE_ROOM_STATUS;
	}
}
