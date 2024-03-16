package l1j.server.server.serverpackets.returnedstat;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_TotalDrinkedElixirNoti extends ServerBasePacket {
	private static final String S_TOTAL_DRINKED_ELIXIR_NOTI	= "[S] S_TotalDrinkedElixirNoti";
	private byte[] _byte			= null;
	public static final int NOTI	= 0x01e9;
	
	public S_TotalDrinkedElixirNoti(int elixir) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
		writeRaw(0x08);
		writeRaw(elixir);
		writeH(0x00);
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
		return S_TOTAL_DRINKED_ELIXIR_NOTI;
	}
}

