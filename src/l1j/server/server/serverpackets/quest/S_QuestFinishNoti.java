package l1j.server.server.serverpackets.quest;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_QuestFinishNoti extends ServerBasePacket {
	private static final String S_QUEST_FINISH_NOTI = "[S] S_QuestFinishNoti";
	private byte[] _byte = null;
	public static final int FINISH_NOTI	= 0x020e;

	public S_QuestFinishNoti(int id) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(FINISH_NOTI);
		writeRaw(0x08);
		writeBit(id);
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
		return S_QUEST_FINISH_NOTI;
	}
}
