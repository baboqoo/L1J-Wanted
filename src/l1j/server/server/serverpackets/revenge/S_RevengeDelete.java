package l1j.server.server.serverpackets.revenge;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_RevengeDelete extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_REVENGE_DELETE = "[S] S_RevengeDelete";
	public static final int DELETE	= 0x0425;
	
	public static final S_RevengeDelete SUCCESS	= new S_RevengeDelete(eRevengeResult.SUCCESS);
	
	public S_RevengeDelete(eRevengeResult result) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(DELETE);
		writeC(0x08);
		writeC(result.toInt());
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
		return S_REVENGE_DELETE;
	}
}
