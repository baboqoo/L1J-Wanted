package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_ChangeTeamNoti extends ServerBasePacket {
	private static final String S_CHANGE_TEAM_NOTI = "[S] S_ChangeTeamNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x0082;
	
	public S_ChangeTeamNoti(int object_id, int object_team_id){
		write_init();
		write_object_id(object_id);
		write_object_team_id(object_team_id);
        writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_object_id(int object_id) {
		writeRaw(0x08);
		writeBit(object_id);
	}
	
	void write_object_team_id(int object_team_id) {
		writeRaw(0x10);
		writeBit(object_team_id);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_CHANGE_TEAM_NOTI;
	}
}

