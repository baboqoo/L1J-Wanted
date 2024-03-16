package l1j.server.server.serverpackets.einhasadpoint;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_EinhasadPointEnchantStat extends ServerBasePacket {
	private static final String S_EINHASAD_POINT_ENCHANT_STAT = "[S] S_EinhasadPointEnchantStat";
	private byte[] _byte = null;
	public static final int STAT	= 0x0927;
	
	public S_EinhasadPointEnchantStat(L1PcInstance pc, int sequence, int value) {
		write_init();
		write_value(value);
		write_sequence(sequence);
		write_cur_enchant_level(pc.getEinCurEnchantLevel());
		write_total_stat(pc.getEinTotalStat());
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(STAT);
	}
	
	void write_value(int value) {
		writeRaw(0x08);// value 0:실패, 1~3:성공
		writeRaw(value);
	}
	
	void write_sequence(int sequence) {
		writeRaw(0x10);// sequence 0:기본, 1:보너스스테이지
		writeRaw(sequence);
	}
	
	void write_cur_enchant_level(int cur_enchant_level) {
		writeRaw(0x18);// cur_enchant_level 남은 카드
		writeRaw(cur_enchant_level);
	}
	
	void write_total_stat(int total_stat) {
		writeRaw(0x20);// total_stat 획득한 스탯
		writeBit(total_stat);
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
		return S_EINHASAD_POINT_ENCHANT_STAT;
	}
}

