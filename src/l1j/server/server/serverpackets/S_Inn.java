package l1j.server.server.serverpackets;

import java.io.IOException;

import l1j.server.server.Opcodes;

public class S_Inn extends ServerBasePacket {
	
	public S_Inn(int objid, String htmlid, String name) {
		writeC(Opcodes.S_HYPERTEXT);
		writeD(objid);
		writeS(htmlid);
		writeC(0x00);
		writeC(0x01);
		writeC(0x00);
		writeS(name);
	}
	
	public S_Inn(int objId, int max, String htmlid, String price, String name) {
		writeC(Opcodes.S_HYPERTEXT_INPUT);
		writeD(objId);
		writeD(Integer.parseInt(price));// ?
		writeD(1);// 스핀 컨트롤의 초기 가격
		writeD(1);
		writeD(max);
		writeH(0);// ?
		writeS(htmlid);
		writeS(htmlid);
		writeH(2);
		writeS(name);
		writeS(price);
	}

	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}
}

