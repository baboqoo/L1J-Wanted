package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_ExpBoostingInfo extends ServerBasePacket {
	private static final String S_EXP_BOOSTING_INFO = "[S] S_ExpBoostingInfo";
	private byte[] _byte = null;
	public static final int INFO = 0x025b;// 추가 경험치
	
	public S_ExpBoostingInfo(L1PcInstance pc) {
		write_init();
		write_exp_boosting_ratio(pc.get_exp_boosting_ratio());
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(INFO);
	}
	
	void write_exp_boosting_ratio(int exp_boosting_ratio) {
		writeRaw(0x08);// exp_boosting_ratio
		writeBit(exp_boosting_ratio);
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
		return S_EXP_BOOSTING_INFO;
	}
}

