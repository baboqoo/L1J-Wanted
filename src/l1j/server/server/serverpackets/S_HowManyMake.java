package l1j.server.server.serverpackets;

import java.io.IOException;

import l1j.server.server.Opcodes;

public class S_HowManyMake extends ServerBasePacket {
	public S_HowManyMake(int objId, int max, String htmlId) {
		writeC(Opcodes.S_HYPERTEXT_INPUT);
		writeD(objId);
		writeD(0); // ?
		writeD(0); // 스핀 컨트롤의 초기 가격
		writeD(0); // 가격의 하한
		writeD(max); // 가격의 상한
		writeH(0); // ?
		writeS("request");
		writeS(htmlId);
	}

	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}
}

