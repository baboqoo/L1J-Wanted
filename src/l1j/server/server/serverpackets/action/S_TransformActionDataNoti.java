package l1j.server.server.serverpackets.action;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_TransformActionDataNoti extends ServerBasePacket {
	private static final String S_TRASNFORM_ACTION_DATA_NOTI = "[S] S_TransformActionDataNoti";
	private byte[] _byte = null;
	public static final int NOTI	= 0x0a26;
	
	public S_TransformActionDataNoti(int target_id, int action) {
		write_init();
		write_target_id(target_id);
		write_action(action);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_target_id(int target_id) {
		writeC(0x08);
		writeBit(target_id);
	}
	
	void write_action(int action) {
		writeC(0x10);
		writeBit(action);
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
		return S_TRASNFORM_ACTION_DATA_NOTI;
	}
}

