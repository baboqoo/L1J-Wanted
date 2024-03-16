package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1NpcInstance;

public class S_NoSell extends ServerBasePacket {
	private static final String S_NoSell = "[S] S_NoSell";

	private byte[] _byte = null;

	public S_NoSell(L1NpcInstance npc) {
		buildPacket(npc);
	}

	private void buildPacket(L1NpcInstance npc) {
		writeC(Opcodes.S_HYPERTEXT);
		writeD(npc.getId());
		writeS("nosell");
		//writeC(1);
		//writeC(0);
		writeD(0);
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
		return S_NoSell;
	}
}

