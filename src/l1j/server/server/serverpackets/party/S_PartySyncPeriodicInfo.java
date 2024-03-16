package l1j.server.server.serverpackets.party;

import java.util.Map;

import l1j.server.Config;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_PartySyncPeriodicInfo extends ServerBasePacket {
	private static final String S_PARTY_SYNC_PERIODIC_INFO = "[S] S_PartySyncPeriodicInfo";
	private byte[] _byte = null;
	public static final int SYNC	= 0x033b;
	
	public S_PartySyncPeriodicInfo(L1PcInstance[] members) {
		write_init();
		for (L1PcInstance member : members) {
			if (member == null) {
				continue;
			}
			write_status(member);
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SYNC);
	}
	
	void write_status(L1PcInstance member) {
		byte[] memberName = member.getName().getBytes();
		int locationReverce = member.getLongLocationReverse();
		int length = 5 + memberName.length
				+ getBitSize(member.getMapId())
				+ getBitSize(locationReverce)
				+ member.getSkill().getPartyIconSkillBytesLength()
				+ getBitSize(Config.VERSION.SERVER_NUMBER);
		Map<Integer, byte[]> iconByte = member.getSkill().getPartyIconSkillBytes();
		writeRaw(0x0a);
		writeBit(length);
		writeRaw(0x0a);
		writeBytesWithLength(memberName);
		writeRaw(0x30);
		writeBit(member.getMapId());
		writeRaw(0x38);
		writeBit(locationReverce);
		if (iconByte != null && !iconByte.isEmpty()) {
			for (byte[] array : iconByte.values()) {
				writeByte(array);
			}
		}
		writeRaw(0x50);
		writeBit(Config.VERSION.SERVER_NUMBER);
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
		return S_PARTY_SYNC_PERIODIC_INFO;
	}

}

