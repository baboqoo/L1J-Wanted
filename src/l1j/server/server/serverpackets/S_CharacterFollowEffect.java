package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_CharacterFollowEffect extends ServerBasePacket {
	private static final String S_CHARACTER_FOLLOW_EFFECT	= "[S] S_CharacterFollowEffect";
	private byte[] _byte = null;
	public static final int EFFECT_OBJECT	= 0x09d7;
	
	public S_CharacterFollowEffect(int target_id, boolean enable, int sprite_id) {
		write_init();
		write_target_id(target_id);
		write_enable(enable);
		write_sprite_id(sprite_id);
		writeH(0x00);
	}
	
	public S_CharacterFollowEffect(int target_id, boolean enable, int sprite_id, boolean is_cache, int duration) {
		write_init();
		write_target_id(target_id);
		write_enable(enable);
		write_sprite_id(sprite_id);
		write_is_cache(is_cache);
		write_duration(duration);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(EFFECT_OBJECT);
	}
	
	void write_target_id(int target_id) {
		writeRaw(0x08);// target_id
		writeBit(target_id);
	}
	
	void write_enable(boolean enable) {
		writeRaw(0x10);// enable
		writeB(enable);
	}
	
	void write_sprite_id(int sprite_id) {
		writeRaw(0x18);// sprite_id
		writeBit(sprite_id);
	}
	
	void write_is_cache(boolean is_cache) {
		writeRaw(0x20);// is_cache
		writeB(is_cache);
	}
	
	void write_duration(int duration) {
		writeRaw(0x28);// duration
		writeBit(duration);
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
		return S_CHARACTER_FOLLOW_EFFECT;
	}

}

