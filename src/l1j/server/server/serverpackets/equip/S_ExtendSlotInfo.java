package l1j.server.server.serverpackets.equip;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.BinaryOutputStream;

public class S_ExtendSlotInfo extends ServerBasePacket {
	private static final String S_EXTEND_SLOT_INFO = "[S] S_ExtendSlotInfo";
	
	// 1:룬, 2, 4:반지, 8:반지, 16:귀걸이, 32, 64:견갑, 128:휘장, 256:좌측 2번 반지, 512:우측 2번 반지, 1024:좌측 2번 귀걸이, 2048:우측 2번 귀걸이
	public static final int SLOT_LOC_UNDEFIND_1		= 1;
	public static final int SLOT_LOC_UNDEFIND_2		= SLOT_LOC_UNDEFIND_1 << 1;
	public static final int SLOT_LOC_L_RING			= SLOT_LOC_UNDEFIND_2 << 1;
	public static final int SLOT_LOC_R_RING			= SLOT_LOC_L_RING << 1;
	public static final int SLOT_LOC_EARRING		= SLOT_LOC_R_RING << 1;
	public static final int SLOT_LOC_UNDEFIND_3		= SLOT_LOC_EARRING << 1;
	public static final int SLOT_LOC_SHOULDER		= SLOT_LOC_UNDEFIND_3 << 1;
	public static final int SLOT_LOC_BADGE			= SLOT_LOC_SHOULDER << 1;
	public static final int SLOT_LOC_L_RING_95		= SLOT_LOC_BADGE << 1;
	public static final int SLOT_LOC_R_RING_100		= SLOT_LOC_L_RING_95 << 1;
	public static final int SLOT_LOC_L_EARRING_101	= SLOT_LOC_R_RING_100 << 1;
	public static final int SLOT_LOC_R_EARRING_103	= SLOT_LOC_L_EARRING_101 << 1;
	
	private byte[] _byte = null;
	public static final int SLOT_INFO		= 0x095f;
	
	private static final int[] SLOT_ARRAY	= { 
		SLOT_LOC_UNDEFIND_1,
		SLOT_LOC_UNDEFIND_2,
		SLOT_LOC_L_RING,
		SLOT_LOC_R_RING, 
		SLOT_LOC_EARRING,
		SLOT_LOC_UNDEFIND_3,
		SLOT_LOC_SHOULDER,
		SLOT_LOC_BADGE, 
		SLOT_LOC_L_RING_95,
		SLOT_LOC_R_RING_100,
		SLOT_LOC_L_EARRING_101,
		SLOT_LOC_R_EARRING_103
	};
	
	public static final S_ExtendSlotInfo REFRESH	= new S_ExtendSlotInfo();// 슬롯 업데이트
	
	public S_ExtendSlotInfo(L1PcInstance pc, boolean login, int slotType) {
		write_init();
		write_slot_kind(S_ExtendSlotInfo.eSLOT_KIND.SLOT_KIND_RING);
		if (login) {
			for (int slot : SLOT_ARRAY) {
				write_extend_slot_info(getExtendSlotInfo(slot, pc));
			}
		} else {
			write_extend_slot_info(getExtendSlotInfo(slotType, pc));
		}
		writeH(0x00);
	}
	
	public S_ExtendSlotInfo() {
		write_init();
		write_slot_kind(S_ExtendSlotInfo.eSLOT_KIND.SLOT_KIND_RUNE);
		write_extend_slot_info(getExtendSlotInfo(1, null));
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SLOT_INFO);
	}
	
	void write_slot_kind(S_ExtendSlotInfo.eSLOT_KIND slot_kind) {
		writeRaw(0x08);// slot_kind
		writeRaw(slot_kind.value);
	}
	
	void write_extend_slot_info(byte[] extend_slot_info) {
		writeRaw(0x12);// extend_slot_info
		writeBytesWithLength(extend_slot_info);
	}
	
	byte[] getExtendSlotInfo(int slot_location, L1PcInstance pc){
		BinaryOutputStream os	= null;
		try {
			os	= new BinaryOutputStream();
			os.writeC(0x08);// slot_location
			os.writeBit(slot_location);
			
			boolean is_open = false;
			switch (slot_location) {
			case SLOT_LOC_UNDEFIND_1:
			case SLOT_LOC_UNDEFIND_2:
				is_open = true;
				break;
			case SLOT_LOC_L_RING:
				is_open = pc.getRingSlotLevel() > 0;// 좌측반지
				break;
			case SLOT_LOC_R_RING:
				is_open = pc.getRingSlotLevel() > 1;// 우측반지
				break;
			case SLOT_LOC_EARRING:
				is_open = pc.getEarringSlotLevel() > 0;// 귀걸이
				break;
			case SLOT_LOC_SHOULDER:
				is_open = pc.getShoulderSlotLevel() > 0;// 견갑
				break;
			case SLOT_LOC_BADGE:
				is_open = pc.getBadgeSlotLevel() > 0;// 휘장
				break;
			case SLOT_LOC_L_RING_95:
				is_open = pc.getRingSlotLevel() > 2;// 좌측 2번 반지
				break;
			case SLOT_LOC_R_RING_100:
				is_open = pc.getRingSlotLevel() > 3;// 우측 2번 반지
				break;
			case SLOT_LOC_L_EARRING_101:
				is_open = pc.getEarringSlotLevel() > 1;// 우측 2번 반지
				break;
			case SLOT_LOC_R_EARRING_103:
				is_open = pc.getEarringSlotLevel() > 2;// 우측 2번 반지
				break;
			}
			
			os.writeC(0x10);// is_open
			os.writeB(is_open);
			
			if (slot_location >= SLOT_LOC_L_RING) {
				os.writeC(0x18);// slot_remaintime
				os.writeBit(is_open ? -1 : 0);
			}
			return os.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
					os = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public enum eSLOT_KIND{
		SLOT_KIND_NULL(0),
		SLOT_KIND_RING(1),
		SLOT_KIND_RUNE(2),
		SLOT_KIND_MAX(3),
		;
		private int value;
		eSLOT_KIND(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eSLOT_KIND v){
			return value == v.value;
		}
		public static eSLOT_KIND fromInt(int i){
			switch(i){
			case 0:
				return SLOT_KIND_NULL;
			case 1:
				return SLOT_KIND_RING;
			case 2:
				return SLOT_KIND_RUNE;
			case 3:
				return SLOT_KIND_MAX;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eSLOT_KIND, %d", i));
			}
		}
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
		return S_EXTEND_SLOT_INFO;
	}
}

