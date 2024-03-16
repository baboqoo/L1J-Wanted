package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_TaxRate extends ServerBasePacket {
	private static final String _S_TAXRATE = "[S] S_TaxRate";

	public S_TaxRate(int objecId, int currentTax) {
		writeC(Opcodes.S_TAX);
		writeD(objecId);
		writeC(10); // 10%~50%
		writeC(50);
		writeC(currentTax);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return _S_TAXRATE;
	}
}

