package l1j.server.server.serverpackets.party;

import java.util.Map;

import l1j.server.Config;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_PartyMemberStatus extends ServerBasePacket {
	private static final String S_PARTY_MEMBER_STATUS = "[S] S_PartyMemberStatus";
	private byte[] _byte = null;
	public static final int MEMBER_STATUS	= 0x0339;
	
	public S_PartyMemberStatus(String name, int mark) {
		write_init();
		write_name(name);
		write_mark(mark);
		writeH(0x00);
	}

	public S_PartyMemberStatus(L1PcInstance pc) {
		write_init();
		write_name(pc.getName());
		write_hp_ratio(pc.getCurrentHpPercent());
		write_mp_ratio(pc.getCurrentMpPercent());
		write_status(pc.isDead() ? 0 : 1);
		write_world(pc.getMapId());
		write_location(pc.getLongLocationReverse());
		write_mark(pc._partyMark);
		Map<Integer, byte[]> iconByte = pc.getSkill().getPartyIconSkillBytes();
		if (iconByte != null && !iconByte.isEmpty()) {
			for (byte[] array : iconByte.values()) {
				if (array == null) {
					continue;
				}
				write_activated_spell(array);
			}
		}
		write_cache_server_no(Config.VERSION.SERVER_NUMBER);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(MEMBER_STATUS);
	}
	
	void write_name(String name) {
		writeRaw(0x0a);
		writeS2(name);
	}
	
	void write_hp_ratio(int hp_ratio) {
		writeRaw(0x18);// hp_ratio
		writeBit(hp_ratio);
	}
	
	void write_mp_ratio(int mp_ratio) {
		writeRaw(0x20);// mp_ratio
		writeBit(mp_ratio);
	}
	
	void write_status(int status) {
		writeRaw(0x28);// status
		writeRaw(status);
	}
	
	void write_world(int world) {
		writeRaw(0x30);// world
		writeBit(world);
	}
	
	void write_location(long location) {
		writeRaw(0x38);// location
		writeBit(location);
	}
	
	void write_mark(int mark) {
		writeRaw(0x40);// mark
		writeRaw(mark);
	}
	
	void write_activated_spell(byte[] activated_spell) {
		writeByte(activated_spell);
	}
	
	void write_cache_server_no(int cache_server_no) {
		writeRaw(0x50);// cache_server_no
		writeBit(cache_server_no);
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
		return S_PARTY_MEMBER_STATUS;
	}

}

