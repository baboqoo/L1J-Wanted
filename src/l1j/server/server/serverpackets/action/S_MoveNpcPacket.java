package l1j.server.server.serverpackets.action;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_MoveNpcPacket extends ServerBasePacket {
	private static final String S_MOVE_NPC_PACKET = "[S] S_MoveNpcPacket";
	private byte[] _byte = null;
	
	public S_MoveNpcPacket(L1MonsterInstance npc, int x, int y, int heading) {
		writeC(Opcodes.S_MOVE_OBJECT);
		writeD(npc.getId());
		writeH(x);
		writeH(y);
		writeC(heading);
		writeH(0);
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
		return S_MOVE_NPC_PACKET;
	}
}

