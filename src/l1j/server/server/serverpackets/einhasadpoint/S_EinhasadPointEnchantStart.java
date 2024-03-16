package l1j.server.server.serverpackets.einhasadpoint;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_EinhasadPointEnchantStart extends ServerBasePacket {
	private static final String S_EINHASAD_POINT_ENCHANT_START = "[S] S_EinhasadPointEnchantStart";
	private byte[] _byte = null;
	public static final int START			= 0x0925;
	
	public S_EinhasadPointEnchantStart(L1PcInstance pc) {
		write_init();
		if (pc.getEinBonusCardOpenValue() > 0) {// 보너스 카드 오픈 상태에서 선택전 종료
			write_cur_enchant_level(pc.getEinCurEnchantLevel());// 남은 카드
			write_last_sequence(0);
			write_result_sequence(1);
			write_last_value(0);
			write_result_value(pc.getEinBonusCardOpenValue());
		} else {
			write_cur_enchant_level(pc.getEinCurEnchantLevel());// 남은 카드
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(START);
	}
	
	void write_cur_enchant_level(int cur_enchant_level) {
		writeRaw(0x08);// cur_enchant_level
		writeBit(cur_enchant_level);
	}
	
	void write_last_sequence(int last_sequence) {
		writeRaw(0x10);// last_sequence
		writeBit(last_sequence);
	}
	
	void write_result_sequence(int result_sequence) {
		writeRaw(0x18);// result_sequence
		writeBit(result_sequence);
	}
	
	void write_last_value(int last_value) {
		writeRaw(0x20);// last_value
		writeBit(last_value);
	}
	
	void write_result_value(int result_value) {
		writeRaw(0x28);// result_value
		writeBit(result_value);
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
		return S_EINHASAD_POINT_ENCHANT_START;
	}
}

