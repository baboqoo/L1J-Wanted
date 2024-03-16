package l1j.server.server.serverpackets;

import l1j.server.Config;
import l1j.server.server.Opcodes;

public class S_PKMessageAtBattleServer extends ServerBasePacket {
	private static final String S_PK_MESSAGE_AT_BATTLE_SERVER = "[S] S_PKMessageAtBattleServer";
	private byte[] _byte = null;
	public static final int NOTI = 0x0247;
	
	// [서버번호]%s의 공격으로 [서버번호]%s가 사망하였습니다.
	public S_PKMessageAtBattleServer(int interkind, String killer_name, String die_name) {
		write_init();
		write_interkind(interkind);
		write_killer_name(killer_name);
		write_killer_cache_no(Config.VERSION.SERVER_NUMBER);
		write_die_name(die_name);
		write_die_cache_no(Config.VERSION.SERVER_NUMBER);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_interkind(int interkind) {
		writeRaw(0x08);
		writeRaw(interkind);
	}
	
	void write_killer_name(String killer_name) {
		writeRaw(0x12);
		writeStringWithLength(killer_name);
	}
	
	void write_killer_cache_no(int killer_cache_no) {
		writeRaw(0x18);
		writeBit(killer_cache_no);
	}
	
	void write_die_name(String die_name) {
		writeRaw(0x22);
		writeStringWithLength(die_name);
	}
	
	void write_die_cache_no(int die_cache_no) {
		writeRaw(0x28);
		writeBit(die_cache_no);
	}
	
	void write_killer_name_str(int killer_name_str) {
		writeRaw(0x30);
		writeBit(killer_name_str);
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
		return S_PK_MESSAGE_AT_BATTLE_SERVER;
	}
}

