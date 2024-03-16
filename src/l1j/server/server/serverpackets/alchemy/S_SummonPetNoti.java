package l1j.server.server.serverpackets.alchemy;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_SummonPetNoti extends ServerBasePacket {
	private static final String S_SUMMON_PET_NOTI = "[S] S_SummonPetNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x0911;
	
	public static final S_SummonPetNoti ICON_OFF = new S_SummonPetNoti(null, 0, null);

	public S_SummonPetNoti(L1ItemInstance item, int bonus_duration, L1PcInstance pc) {
		write_init();
		write_bonus_duration(bonus_duration);
		if (bonus_duration > 0) {
			write_monclass(item.getItem().getItemNameId());
			write_petball_nameId(item.getId());
			write_bonus_desc(item.getStatusBytes(pc));
		} else {
			write_monclass(0);
			write_petball_nameId(0);
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_bonus_duration(int bonus_duration) {
		writeRaw(0x08);// bonus_duration
		writeBit(bonus_duration);
	}
	
	void write_monclass(int monclass) {
		writeRaw(0x10);// monclass
		writeBit(monclass);
	}
	
	void write_petball_nameId(int petball_nameId) {
		writeRaw(0x18);// petball_nameId
		writeBit(petball_nameId);
	}
	
	void write_bonus_desc(byte[] bonus_desc) {
		writeRaw(0x22);// bonus_desc
		writeBytesWithLength(bonus_desc);
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
		return S_SUMMON_PET_NOTI;
	}
}
