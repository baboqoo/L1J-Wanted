package l1j.server.server.serverpackets.equip;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_EquipmentWindow extends ServerBasePacket {
	public static final byte EQUIPMENT_INDEX_HEML			= 1;// 투구
	public static final byte EQUIPMENT_INDEX_ARMOR			= 2;// 갑옷
	public static final byte EQUIPMENT_INDEX_T				= 3;// 티
	public static final byte EQUIPMENT_INDEX_CLOAK			= 4;// 장갑
	public static final byte EQUIPMENT_INDEX_PAIR			= 5;// 각반
	public static final byte EQUIPMENT_INDEX_BOOTS			= 6;// 부츠
	public static final byte EQUIPMENT_INDEX_GLOVE			= 7;// 장갑
	public static final byte EQUIPMENT_INDEX_SHIELD			= 8;// 방패
	public static final byte EQUIPMENT_INDEX_WEAPON			= 9;// 무기
	public static final byte EQUIPMENT_INDEX_NECKLACE		= 11;// 목걸이
	public static final byte EQUIPMENT_INDEX_BELT			= 12;// 벨트
	public static final byte EQUIPMENT_INDEX_EARRING_L_1	= 13;// 귀걸이 좌측 1번
	public static final byte EQUIPMENT_INDEX_RING_L_1		= 19;// 반지 좌측 1번
	public static final byte EQUIPMENT_INDEX_RING_R_1		= 20;// 반지 우측 1번
	public static final byte EQUIPMENT_INDEX_RING_L_2		= 21;// 반지 좌측 2번
	public static final byte EQUIPMENT_INDEX_RING_R_2		= 22;// 반지 우측 2번
	public static final byte EQUIPMENT_INDEX_RING_L_3		= 23;// 반지 좌측 3번
	public static final byte EQUIPMENT_INDEX_RING_R_3		= 24;// 반지 우측 3번
	public static final byte EQUIPMENT_INDEX_RUNE1			= 25;// 룬/유물
	public static final byte EQUIPMENT_INDEX_RUNE2			= 26;
	public static final byte EQUIPMENT_INDEX_RUNE3			= 27;
	public static final byte EQUIPMENT_INDEX_EARRING_R_1	= 28;// 귀걸이 우측 1번
	public static final byte EQUIPMENT_INDEX_SENTENCE		= 30;// 문장
	public static final byte EQUIPMENT_INDEX_SHOULDER		= 31;// 견갑
	public static final byte EQUIPMENT_INDEX_BADGE			= 32;// 휘장
	public static final byte EQUIPMENT_INDEX_PENDANT		= 33;// 펜던트
	public static final byte EQUIPMENT_INDEX_EARRING_L_2	= 34;// 귀걸이 좌측 2번
	public static final byte EQUIPMENT_INDEX_EARRING_R_2	= 35;// 귀걸이 우측 2번
	
	public S_EquipmentWindow(int itemObjId, int index, boolean isEq) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(0x42);
		writeD(itemObjId);
		writeC(index);
		writeB(isEq);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	
	private static final String S_EQUIPMENT_WINDOWS = "[S] S_EquipmentWindow";
	
	@Override
	public String getType() {
		return S_EQUIPMENT_WINDOWS;
	}
}
