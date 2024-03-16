package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_Teleport extends ServerBasePacket {
	private static final String S_TELEPORT = "[S] S_Teleport";
	private byte[] _byte = null;
	
	public static final S_Teleport REQUEST_SUMMON	= new S_Teleport(null);

	public S_Teleport(L1PcInstance pc) {
		writeC(Opcodes.S_REQUEST_SUMMON);
		//writeH(0x00);
		//writeC(0x40);
		//writeD(pc.getId());
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
		return S_TELEPORT;
	}
}

