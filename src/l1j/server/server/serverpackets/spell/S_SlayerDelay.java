package l1j.server.server.serverpackets.spell;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_SlayerDelay extends ServerBasePacket {
	private static final String S_SLAYER_DELAY		= "[S] S_SlayerDelay";
	private byte[] _byte							= null;
	private static final int SLAYER					= 0x0a20;
	
	public static final S_SlayerDelay SLAYER_ON		= new S_SlayerDelay(true);
	public static final S_SlayerDelay SLAYER_OFF	= new S_SlayerDelay(false);
	
	public S_SlayerDelay(boolean on_off) {
		write_init();
		write_on_off(on_off);
		write_attack_delay_numerator(14260);
		write_attack_delay_denominator(16768);
		write_spell_delay_numerator(31);
		write_spell_delay_denominator(32);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SLAYER);
	}
	
	void write_on_off(boolean on_off) {
		writeRaw(0x08);// on_off
		writeB(on_off);
	}
	
	void write_attack_delay_numerator(int attack_delay_numerator) {
		writeRaw(0x10);// attack_delay_numerator
		writeBit(attack_delay_numerator);
	}
	
	void write_attack_delay_denominator(int attack_delay_denominator) {
		writeRaw(0x18);// attack_delay_denominator
		writeBit(attack_delay_denominator);
	}
	
	void write_spell_delay_numerator(int spell_delay_numerator) {
		writeRaw(0x20);// spell_delay_numerator
		writeRaw(spell_delay_numerator);
	}
	
	void write_spell_delay_denominator(int spell_delay_denominator) {
		writeRaw(0x28);// spell_delay_denominator
		writeRaw(spell_delay_denominator);
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
		return S_SLAYER_DELAY;
	}
}
