package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_MiniGame extends ServerBasePacket {
	
	public static final int MINIGAME_START_COUNT	= 64;
	public static final int MINIGAME_LIST			= 66;
	public static final int MINIGAME_END			= 70;
	public static final int MINIGAME_TIME2			= 65;
	public static final int MINIGAME_TIME_CLEAR		= 72;
	
	public S_MiniGame(int subCode, boolean start) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
		case MINIGAME_START_COUNT:
			if (start) {
				writeC(5);
			} else {
				writeC(-1);
			}
			writeC(129);
			writeC(252);
			writeC(125);
			writeC(110);
			writeC(17);
			break;
		case MINIGAME_LIST:
			writeH(9);// 참여자수
			writeH(0);// 등수
			//writeS("\\aH미션 시간 : " + 300);
			writeS("\\aHMission Time: " + 300);
			break;
		case MINIGAME_TIME2:
		case MINIGAME_TIME_CLEAR:
			break;
		case MINIGAME_END:
			writeC(147);
			writeC(92);
			writeC(151);
			writeC(220);
			writeC(42);
			writeC(74);
			break;
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}

