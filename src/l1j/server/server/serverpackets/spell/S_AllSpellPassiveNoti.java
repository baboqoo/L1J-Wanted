package l1j.server.server.serverpackets.spell;

import java.util.List;

import l1j.server.server.Opcodes;
import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_AllSpellPassiveNoti extends ServerBasePacket {
	private static final String S_ALL_SPELL_PASSIVE_NOTI = "[S] S_AllSpellPassiveNoti";
	private byte[] _byte = null;
	public static final int ALL	= 0x0191;
	
	public S_AllSpellPassiveNoti(List<Integer> list){
		write_init();
		for (int passiveId : list) {
			writeRaw(0x0a);
			writeRaw(passiveId != L1PassiveId.ARMOR_GUARD.toInt() && passiveId != L1PassiveId.BLOOD_TO_SOUL.toInt() ? 2 : 4);// 길이
			write_passiveId(passiveId);
			if (passiveId == L1PassiveId.ARMOR_GUARD.toInt()) {
				write_param(10);
			} else if (passiveId == L1PassiveId.BLOOD_TO_SOUL.toInt()) {
				write_onoffType(false);
			}
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(ALL);
	}
	
	void write_passiveId(int passiveId) {
		writeRaw(0x08);// passiveId
		writeRaw(passiveId);
	}
	
	void write_param(int param) {
		writeRaw(0x10);// param
		writeRaw(param);
	}
	
	void write_onoffType(boolean onoffType) {
		writeRaw(0x18);// onoffType
		writeB(onoffType);
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
		return S_ALL_SPELL_PASSIVE_NOTI;
	}
}

