package l1j.server.server.serverpackets;

import l1j.server.common.data.ChatType;
import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.utils.StringUtil;

public class S_NpcChatPacket extends ServerBasePacket {
	private static final String S_NPC_CHAT_PACKET = "[S] S_NpcChatPacket";
	private byte[] _byte = null;

	public S_NpcChatPacket(L1NpcInstance npc, String chat, ChatType type) {
		buildPacket(npc, chat, type);
	}

	private void buildPacket(L1NpcInstance npc, String chat, ChatType type) {
		writeC(Opcodes.S_SAY_CODE);
		switch (type) {
		case CHAT_NORMAL: // normal chat
			// desc-?.tbl
			writeC(type.toInt()); // Color
			writeD(npc.getId());
			writeS(StringUtil.isNullOrEmpty(npc.getName()) ? chat : npc.getName() + ": " + chat);
			break;

		case CHAT_SHOUT: // shout
			// desc-?.tbl
			writeC(type.toInt()); // Color
			writeD(npc.getId());
			if (npc.getNpcTemplate().getNpcId() == 70518 || npc.getNpcTemplate().getNpcId() == 70506) {
				writeS(npc.getName() + ": " + chat);
			} else {
				writeS("<" + npc.getName() + "> " + chat);
			}
			break;

		case CHAT_WORLD: // world chat
			writeC(type.toInt()); 
			writeD(npc.getId());
			writeS("[" + npc.getName() + "] " + chat);
			break;

		/*case SOUND: // 음성
			writeC(type.toInt());
			writeD(npc.getId());
			writeS("$4305: #" + chat);
			writeD(0x805E8064);
			break;*/
		default:
			break;
		}
	}
	
	public S_NpcChatPacket(L1Character cha, String chat) {
		writeC(Opcodes.S_SAY_CODE); // Key is 16 , can use
		// desc-?.tbl
		writeC(0); // Color
		writeD(cha.getId());
		writeS(chat);
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
		return S_NPC_CHAT_PACKET;
	}
}

