package l1j.server.server.serverpackets.returnedstat;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_ReturnedStatSlot extends ServerBasePacket {
	private static final String S_RETURNED_STAT_SLOT = "[S] S_ReturnedStatSlot";
	private byte[] _byte = null;
	public static final int EXTEND_SLOT	= 0x43;
	
	public static final int SUBTYPE_RING	= 1;
	public static final int SUBTYPE_RUNE	= 2;
	
	public static final S_ReturnedStatSlot RING_SLOT_LEFT_OPEN		= new S_ReturnedStatSlot(SUBTYPE_RING, 1);
	public static final S_ReturnedStatSlot RING_SLOT_RIGHT_OPEN		= new S_ReturnedStatSlot(SUBTYPE_RING, 2);
	public static final S_ReturnedStatSlot EARRING_SLOT_OPEN		= new S_ReturnedStatSlot(SUBTYPE_RING, 16);
	public static final S_ReturnedStatSlot SHOULDER_SLOT_OPEN		= new S_ReturnedStatSlot(SUBTYPE_RING, 64);
	public static final S_ReturnedStatSlot BADGE_SLOT_OPEN			= new S_ReturnedStatSlot(SUBTYPE_RING, 128);

	public S_ReturnedStatSlot(int subType, int value) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeRaw(EXTEND_SLOT);

		writeD(subType);
		if (subType == SUBTYPE_RING) { // 반지 슬롯
			if (value == 2) {
				value = 15;
			} else if (value == 1) {
				value = 7;
			} else if (value == 0) {
				value = 3;
			}
			writeRaw(value);
		} else if (subType == SUBTYPE_RUNE) {
			writeRaw(1); // 룬 슬롯 1~3
		}
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeH(0x00);
	}
	
	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}
	
	public String getType() {
		return S_RETURNED_STAT_SLOT;
	}
}
