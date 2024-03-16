package l1j.server.server.serverpackets.message;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_MessageBox extends ServerBasePacket {
	private static final String S_MESSAGE_BOX = "[S] S_MessageBox";
	private byte[] _byte = null;
	public static final int BOX	= 0x0333;

	public S_MessageBox(int msg_code, boolean modal){
		write_init();
		write_msg_code(msg_code);
		write_modal(modal);
        writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(BOX);
	}
	
	void write_msg_code(int msg_code) {
		writeRaw(0x08);// msg_code
		writeBit(msg_code);
	}
	
	void write_modal(boolean modal) {
		writeRaw(0x10);// modal
		writeB(modal);
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
		return S_MESSAGE_BOX;
	}
}

