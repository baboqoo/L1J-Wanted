package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_ExpPotion extends ServerBasePacket {

	public S_ExpPotion(int time){
		writeC(Opcodes.S_EVENT);
		writeC(0x14);
		for (int i = 0; i < 45; i++) {
			writeC(0x00);
		}
		writeC((int)(time + 8) / 16);
		for (int i = 0; i < 16; i++) {
			writeC(0x00);
		}
		writeC(0x14);
		writeD(0x00000000);
		writeC(0x00);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}

