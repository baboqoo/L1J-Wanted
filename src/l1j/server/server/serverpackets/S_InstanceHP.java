package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_InstanceHP extends ServerBasePacket {
	private static final String S_INSTANCE_HP	= "[S] S_InstanceHP";
	private byte[] _byte						= null;
	public static final int HP_EXTEND			= 0x09d6;
	
	public static final S_InstanceHP FINISH		= new S_InstanceHP(0, 0, false);
	
	public S_InstanceHP(int instanceHp_value, int instanceMaxHp_value, boolean enable) {
		write_init();
		write_instanceHp_value(instanceHp_value);
		write_enable(enable);
		write_instanceMaxHp_value(instanceMaxHp_value);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(HP_EXTEND);
	}
	
	void write_instanceHp_value(int instanceHp_value) {
		writeRaw(0x08);// instanceHp_value
		writeBit(instanceHp_value);
	}
	
	void write_enable(boolean enable) {
		writeRaw(0x10);// enable
		writeB(enable);
	}
	
	void write_instanceMaxHp_value(int instanceMaxHp_value) {
		writeRaw(0x18);// instanceMaxHp_value
		writeBit(instanceMaxHp_value);
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
		return S_INSTANCE_HP;
	}

}

