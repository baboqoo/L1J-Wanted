package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

// 맨처음엔
// S_SkillSound(7382): A타입
// S_SkillSound(7383): B타입 보내주면 됨.
// coin 인벤이미지: 3565
public class S_Coma extends ServerBasePacket {

// j가 40일때 A, 41일때 B
	public S_Coma(int j,int time) {
		writeC(Opcodes.S_EVENT);
		writeC(0x14);
		for (int i = 0; i < 64; i++) {
			writeC(0x00);
		}
		writeC((int)(time + 16) >> 5);
		writeC(j);
		writeC(0x14);
		writeD(0x00000000);
		writeC(0x00);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}

