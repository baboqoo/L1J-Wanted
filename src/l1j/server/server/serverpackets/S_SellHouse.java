package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_SellHouse extends ServerBasePacket {

	private static final String S_SELLHOUSE = "[S] S_SellHouse";
	private byte[] _byte = null;

	public S_SellHouse(int objectId, String houseNumber) {
		buildPacket(objectId, houseNumber);
	}

	private void buildPacket(int objectId, String houseNumber) {
		writeC(Opcodes.S_HYPERTEXT_INPUT);
		writeD(objectId);
		writeD(0); // ?
		writeD(100000); // 스핀 컨트롤의 초기 가격
		writeD(100000); // 가격의 하한
		writeD(2000000000); // 가격의 상한
		writeH(0); // ?
		writeS("agsell");
		writeS("agsell " + houseNumber);
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
		return S_SELLHOUSE;
	}
}

