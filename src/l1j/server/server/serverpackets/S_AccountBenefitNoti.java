package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_AccountBenefitNoti extends ServerBasePacket {
	private static final String S_ACCOUNT_BENEFIT_NOTI = "[S] S_AccountBenefitNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x013B;
	
	public S_AccountBenefitNoti(S_AccountBenefitNoti.eBenefitType type, boolean enable) {
		write_init();
		write_type(type);
		write_enable(enable);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_type(S_AccountBenefitNoti.eBenefitType type) {
		writeRaw(0x08);
		writeRaw(type.value);
	}
	
	void write_enable(boolean enable) {
		writeRaw(0x10);
		writeB(enable);
	}
	
	public enum eBenefitType{
		ABT_NONE(0),
		ABT_NEW(1),
		ABT_DORMANT(2),
		;
		private int value;
		eBenefitType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eBenefitType v){
			return value == v.value;
		}
		public static eBenefitType fromInt(int i){
			switch(i){
			case 0:
				return ABT_NONE;
			case 1:
				return ABT_NEW;
			case 2:
				return ABT_DORMANT;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eBenefitType, %d", i));
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
		return S_ACCOUNT_BENEFIT_NOTI;
	}
}

