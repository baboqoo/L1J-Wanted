package l1j.server.server.serverpackets;

public class S_LocRefresh extends ServerBasePacket {
	private static final String S_LOC_REFRESH = "[S] S_LocRefresh";
	private byte[] _byte = null;
	public static final int LOC = 0x08ce;

	public static final S_LocRefresh REFRESH = new S_LocRefresh();
	
	private S_LocRefresh() {
		writeC(l1j.server.server.Opcodes.S_EXTENDED_PROTOBUF);
		writeH(LOC);
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
		return S_LOC_REFRESH;
	}

}

