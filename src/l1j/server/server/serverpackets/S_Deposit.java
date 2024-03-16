package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_Deposit extends ServerBasePacket {

	public S_Deposit(int objecId) {
		writeC(Opcodes.S_DEPOSIT);
		writeD(objecId);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	
	@Override
	public String getType() {
		return S_DEPOSIT;
	}

	private static final String S_DEPOSIT = "[S] S_Deposit";
}

