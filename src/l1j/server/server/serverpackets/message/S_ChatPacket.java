package l1j.server.server.serverpackets.message;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_ChatPacket extends ServerBasePacket {
	private static final String S_CHAT_PACKET = "[S] S_ChatPacket";

	public S_ChatPacket(String targetname, String chat, int opcode) {
		writeC(opcode);
		writeC(9);
		writeS(String.format("-> (%s) %s", targetname, chat));
	}
	
	// 매니저용 귓말
	public S_ChatPacket(String from, String chat) {
		writeC(Opcodes.S_GM_MESSAGE);
		writeS(from);
		writeS(chat);
	}
	
	public S_ChatPacket(String targetname, int type, String chat) {
		writeC(Opcodes.S_MESSAGE);
		writeC(type);
		writeS(String.format("[%s] %s", targetname, chat));
	}

	public S_ChatPacket(L1PcInstance pc, String chat) {
		writeC(Opcodes.S_MESSAGE);
		writeC(12);//1~99
		writeS(chat);
	}

	public S_ChatPacket(String chat) {
		writeC(Opcodes.S_MESSAGE);
		writeC(0x0F);
		writeD(000000000);
		writeS(chat);
	}

	public S_ChatPacket(L1PcInstance pc, String chat, int a, int b, int c) {
		writeC(Opcodes.S_MESSAGE);
		writeC(4);
		writeS(chat);
	}

	public S_ChatPacket(L1PcInstance pc, String chat, int test) {
		writeC(Opcodes.S_SAY);
		writeC(15);
		writeD(pc.getId());
		writeS(chat);
	}

	public S_ChatPacket(String chat, int opcode) {
		writeC(opcode);
		writeC(2);
		writeS(chat);
	}

	public S_ChatPacket(L1PcInstance pc, String chat, int opcode, int type) {
		writeC(opcode);
				
		switch (type) {
		case 0: // 통상채팅
			writeC(type);
			writeD(pc.getId());
			writeS(String.format("%s: %s", pc.getName(), chat));
			break;
		case 2: // 절규
			writeC(type);
			if (pc.isInvisble()) {
				writeD(0);
			} else {
				writeD(pc.getId());
			}
			writeS(String.format("<%s> %s", pc.getName(), chat));
			writeH(pc.getX());
			writeH(pc.getY());
			break;
        case 3:
        	writeC(type);
        	//if (pc.getName().equalsIgnoreCase("메티스")) {
			if (pc.getName().equalsIgnoreCase("Metis")) {
				writeS(String.format("[******] %s", chat));
            }
            break;
		case 4: // 혈맹채팅
			writeC(type);
			writeS(String.format("{%s} %s", pc.getName(), chat));
			break;
		case 9: // 위스파
			writeC(type);
			writeS(String.format("-> (%s) %s", pc.getName(), chat));
			break;
		case 11: // 파티채팅
			writeC(type);
			writeS(String.format("(%s) %s", pc.getName(), chat));
			break;
		case 12: // 연합 채팅
			writeC(type);
			writeS(String.format("[%s] %s", pc.getName(), chat));
			break;
		case 13:
			writeC(4);
			writeS(String.format("{{%s}} %s", pc.getName(), chat));
			break;
		case 14: // 채팅파티
			writeC(type);
			writeD(pc.getId());
			writeS(String.format("\\fU(%s) %s", pc.getName(), chat)); // #
			break;
		case 15:
			writeC(type);
			writeS(String.format("[%s] ", pc.getName(), chat));
			break;
		case 16: // 위스파
			writeS(pc.getName());
			writeS(chat);
			break;
		case 17: // 군주채팅 +
			writeC(type);
			writeS(String.format("{%s} %s", pc.getName(), chat));
			break;
		default:
			break;
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_CHAT_PACKET;
	}

}
