package l1j.server.server.serverpackets.party;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_AddPartyMember extends ServerBasePacket {
	private static final String S_ADD_PARTY_MEMBER = "[S] S_AddPartyMember";
	private byte[] _byte = null;
	
	public S_AddPartyMember(L1PcInstance cha){
		writeC(Opcodes.S_EVENT);
		writeRaw(0x69);
		writeD(cha.getId());
		writeS(cha.getName());
		writeRaw(0x01);	// class type
		writeRaw(0x00);
		writeRaw(0x00);
		writeD(cha.getMapId());	// 맵아이디라고 유추한거같은데 다른용도인듯. 
		writeRaw(cha.getCurrentHpPercent());
		writeRaw(0x00);
		writeRaw(0x00);
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
		return S_ADD_PARTY_MEMBER;
	}
}

