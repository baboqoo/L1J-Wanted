package l1j.server.server.serverpackets.indun;

import l1j.server.common.data.eArenaMapKind;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_IndunMatchingRegister extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_INDUN_MATCHING_REGISTER = "[S] S_IndunMatchingRegister";
	public static final int MATCHING_REGISTER 	= 0x08c2;
	
	public S_IndunMatchingRegister(ArenaMatchingRegisterResult result, eArenaMapKind map_kind) {
		write_init();
		write_result(result);
		write_map_kind(map_kind);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(MATCHING_REGISTER);
	}
	
	void write_result(ArenaMatchingRegisterResult result) {
		writeC(0x08);// result
		writeC(result.value);
	}
	
	void write_map_kind(eArenaMapKind map_kind) {
		writeC(0x10);// map_kind
		writeBit(map_kind.toInt());
	}
 	
 	public enum ArenaMatchingRegisterResult{
		SUCCESS(1),
		FAIL_INVALID_KEY(2),
		FAIL_INVALID_LEVEL(3),
		FAIL_INVALID_MAP_KIND(4),
		FAIL_INDUN_BLOCK(5),
		FAIL_ENTER_COUNT(6),
		FAIL_RESTRICT_ITEM(7),
		;
		private int value;
		ArenaMatchingRegisterResult(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(ArenaMatchingRegisterResult v){
			return value == v.value;
		}
		public static ArenaMatchingRegisterResult fromInt(int i){
			switch(i){
			case 1:
				return SUCCESS;
			case 2:
				return FAIL_INVALID_KEY;
			case 3:
				return FAIL_INVALID_LEVEL;
			case 4:
				return FAIL_INVALID_MAP_KIND;
			case 5:
				return FAIL_INDUN_BLOCK;
			case 6:
				return FAIL_ENTER_COUNT;
			case 7:
				return FAIL_RESTRICT_ITEM;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eResult, %d", i));
			}
		}
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
		return S_INDUN_MATCHING_REGISTER;
	}
}
