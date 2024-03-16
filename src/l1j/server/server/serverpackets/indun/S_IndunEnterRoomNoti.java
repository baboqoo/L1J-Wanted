package l1j.server.server.serverpackets.indun;

import l1j.server.IndunSystem.indun.IndunInfo;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_IndunEnterRoomNoti extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_INDUN_ENTER_ROOM_NOTI = "[S] S_IndunEnterRoomNoti";
	public static final int ENTER_ROOM_NOTI = 0x08ae;

	public S_IndunEnterRoomNoti(L1PcInstance pc, IndunInfo info) {
		write_init();
		write_room_id(info.room_id);
		write_user_info(pc, info);
		
		/*writeC(0x12);
		writeBytesWithLength(pc.getName().getBytes());
		
		writeC(0x1a);
		writeBytesWithLength(pc.getName().getBytes());*/

		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(ENTER_ROOM_NOTI);
	}
	
	void write_room_id(int room_id) {
		writeC(0x08);// room_id
		writeBit(room_id);
	}
	
	void write_user_info(L1PcInstance pc, IndunInfo info) {
		ArenaUserInfoStream os = null;
		try {
			os = new ArenaUserInfoStream(info, pc);
			writeC(0x12);// user_info
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
		return S_INDUN_ENTER_ROOM_NOTI;
	}
}
