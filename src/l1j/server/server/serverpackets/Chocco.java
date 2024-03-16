package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1HauntedHouse;
import l1j.server.server.model.Instance.L1PcInstance;

public class Chocco extends ServerBasePacket{
	private byte[] _byte = null;

	public Chocco(int type){
		writeC(Opcodes.S_EVENT);
		switch(type){
		// 시작
		case 0:
			writeC(71);
			writeC(500);
			break;
		// 종료
		case 1:
			writeC(0x46);
			writeC(147);
			writeC(92);
			writeC(151);
			writeC(220);
			writeC(42);
			writeC(74);
			break;
		// 옆 리스트
		case 2:
			writeC(66);
			writeH(L1HauntedHouse.getInstance().getMembersCount());
			writeH(0x01);
			for (L1PcInstance player : L1HauntedHouse.getInstance().getMembersArray()) {
				writeS(player.getName());
			}
			break;
		// 게임시작
		case 3:
			writeC(64);
			writeC(5);
			writeC(129);
			writeC(252);
			writeC(125);
			writeC(110);
			writeC(17);
			break;
		// 게임 시간
		case 4:
			writeC(0x41);
			writeC(0);
			writeC(0);
			writeC(0);
			writeC(0);
			writeC(0);
			writeC(0);
			break;
		// 시간패킷 삭제
		case 5:
			writeC(72);
			break;
		}
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}
}
