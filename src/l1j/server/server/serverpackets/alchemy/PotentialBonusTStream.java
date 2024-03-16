package l1j.server.server.serverpackets.alchemy;

import l1j.server.common.bin.potential.CommonPotentialInfo;
import l1j.server.server.utils.BinaryOutputStream;

public class PotentialBonusTStream extends BinaryOutputStream {
	
	public PotentialBonusTStream() {
		super();
	}
	
	public PotentialBonusTStream(CommonPotentialInfo.BonusInfoT bonusInfoT) {
		super();
		write_bonus_grade(bonusInfoT.get_bonus_grade());
		write_bonus_id(bonusInfoT.get_bonus_id());
		write_bonus_desc(bonusInfoT.get_bonus_desc());
	}
	
	void write_bonus_grade(int bonus_grade) {
		writeC(0x08);
		writeC(bonus_grade);
	}
	
	void write_bonus_id(int bonus_id) {
		writeC(0x10);
		writeBit(bonus_id);
	}
	
	void write_bonus_desc(int bonus_desc) {
		writeC(0x18);
		writeBit(bonus_desc);
	}
	
	void write_bonus_value(String bonus_value) {
		writeC(0x22);
		writeStringWithLength(bonus_value);
	}
	
	void writebonus_desc_str(byte[] bonus_desc_str) {
		writeC(0x2a);
		writeBytesWithLength(bonus_desc_str);
	}
	
}

