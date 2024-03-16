package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.construct.L1RegionStatus;

public class S_DeathPenaltyStatus extends ServerBasePacket {
	private static final String S_DEATH_PENALTY_STATUS = "[S] S_DeathPenaltyStatus";
	private byte[] _byte = null;
	public static final int STATUS = 0x01cf;
	
	public S_DeathPenaltyStatus(L1RegionStatus region) {
		write_init();
		write_globalDeathPenaltyStatus(region.getGlobalDeathPenaltyStatus());
		write_expDeathPenaltyStatus(region.getExpDeathPenaltyStatus());
		write_itemDeathPenaltyStatus(region.getItemDeathPenaltyStatus());
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(STATUS);
	}
	
	void write_globalDeathPenaltyStatus(int globalDeathPenaltyStatus) {
		writeRaw(0x08);// globalDeathPenaltyStatus
		writeBit(globalDeathPenaltyStatus);
	}
	
	void write_expDeathPenaltyStatus(int expDeathPenaltyStatus) {
		writeRaw(0x10);// expDeathPenaltyStatus
		writeBit(expDeathPenaltyStatus);
	}
	
	void write_itemDeathPenaltyStatus(int itemDeathPenaltyStatus) {
		writeRaw(0x18);// itemDeathPenaltyStatus
		writeRaw(itemDeathPenaltyStatus);
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
		return S_DEATH_PENALTY_STATUS;
	}
}

