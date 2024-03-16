package l1j.server.server.serverpackets.command;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_WhoAmount extends ServerBasePacket {
	private static final String S_WHO_AMOUNT = "[S] S_WhoAmount";
	
	public S_WhoAmount(String amount) {// 기존 패킷
		writeC(Opcodes.S_MESSAGE_CODE);
		writeH(0x0051);
		writeC(0x01);
		writeS(amount);
		writeD(0);// 더미로 넣어야 안팅김
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	
	@Override
	public String getType() {
		return S_WHO_AMOUNT;
	}
}

