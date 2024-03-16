package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_EntranceInfo extends ServerBasePacket {
	private static final String S_ENTRANCE_INFO = "[S] S_EntranceInfo";
	private byte[] _byte = null;
	public static final int ENTRANCE = 0x0252;

	public S_EntranceInfo(int waiting_user_count){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(ENTRANCE);
		writeRaw(0x08);// waiting_user_count
		writeBit(waiting_user_count);
        writeH(0x00);
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
		return S_ENTRANCE_INFO;
	}
}

