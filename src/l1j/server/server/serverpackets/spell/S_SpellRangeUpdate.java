package l1j.server.server.serverpackets.spell;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_SpellRangeUpdate extends ServerBasePacket {
	private static final String S_SPELL_RANGE_UPDATE = "[S] S_SpellRangeUpdate";
	private byte[] _byte = null;
	
	public S_SpellRangeUpdate(int code, int spellId, int range) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(code);
		
		// 반복문 사용 가능
		int length = getBitSize(spellId) + getBitSize(range) + 2;
		writeRaw(0x0a);
		writeRaw(length);
		
		writeRaw(0x08);// spellId
		writeBit(spellId);
		
		writeRaw(0x10);// range
		writeBit(range);
		
		writeH(0x00);
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
		return S_SPELL_RANGE_UPDATE;
	}
}

