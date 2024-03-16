package l1j.server.server.serverpackets.spell;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_SpellDelay extends ServerBasePacket {
	private static final String S_SPELL_DELAY = "[S] S_SpellDelay";
	private byte[] _byte = null;
	public static final int DELAY	= 0x040f;
	
	public S_SpellDelay(int spell_group_id, int next_spell_delay, int next_spell_global_delay) {
		write_init();
		write_next_spell_delay(next_spell_delay);
		write_next_spell_global_delay(next_spell_global_delay);
		write_spell_group_id(spell_group_id);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(DELAY);
	}
	
	void write_next_spell_delay(int next_spell_delay) {
		writeRaw(0x08);
		writeBit(next_spell_delay);
	}
	
	void write_next_spell_global_delay(int next_spell_global_delay) {
		writeRaw(0x10);
		writeBit(next_spell_global_delay);
	}
	
	void write_spell_group_id(int spell_group_id) {
		writeRaw(0x18);
		writeRaw(spell_group_id);
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
		return S_SPELL_DELAY;
	}

}

