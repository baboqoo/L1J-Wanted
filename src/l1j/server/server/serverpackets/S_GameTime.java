package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.gametime.GameTimeClock;

public class S_GameTime extends ServerBasePacket {
	private static final String S_GAME_TIME = "[S] S_GameTime";

	public S_GameTime(long time) {
		buildPacket(time);
	}

	public S_GameTime() {
		long time = GameTimeClock.getInstance().getGameTime().getSeconds();
		buildPacket(time);
	}

	private void buildPacket(long time) {
		writeC(Opcodes.S_TIME);
		writeD((int) time);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return S_GAME_TIME;
	}
}

