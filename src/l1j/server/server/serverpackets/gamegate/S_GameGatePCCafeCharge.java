package l1j.server.server.serverpackets.gamegate;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_GameGatePCCafeCharge extends ServerBasePacket {
	private static final String S_GAME_GATE_PC_CAFE_CHARGE = "[S] S_GameGatePCCafeCharge";
	private byte[] _byte = null;
	public static final int CHARGE	= 0x0343;
	
	public static final S_GameGatePCCafeCharge START	= new S_GameGatePCCafeCharge(eChargeType.START);
	public static final S_GameGatePCCafeCharge STOP		= new S_GameGatePCCafeCharge(eChargeType.STOP);
	
	public S_GameGatePCCafeCharge(eChargeType type) {
		write_init();
		write_chargeType(type);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(CHARGE);
	}
	
	void write_chargeType(eChargeType type) {
		writeRaw(0x08);
		writeRaw(type.toInt());
	}
	
	void write_msg_id(int msg_id) {
		writeRaw(0x10);
		writeBit(msg_id);
	}
	
	public enum eChargeType {
		STOP(0),
		START(1),
		DELAY(2),
		;
		private int value;
		eChargeType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eChargeType v){
			return value == v.value;
		}
		public static eChargeType fromInt(int i){
			switch(i){
			case 0:
				return STOP;
			case 1:
				return START;
			case 2:
				return DELAY;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eChargeType, %d", i));
			}
		}
	}
	
	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}
	
	@Override
	public String getType() {
		return S_GAME_GATE_PC_CAFE_CHARGE;
	}
}

