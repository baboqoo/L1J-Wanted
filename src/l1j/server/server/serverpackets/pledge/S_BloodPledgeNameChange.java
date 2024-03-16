package l1j.server.server.serverpackets.pledge;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_BloodPledgeNameChange extends ServerBasePacket {
	private static final String S_BLOODPLEDGE_NAME_CHANGE = "[S] S_BloodPledgeNameChange";
	private byte[] _byte = null;
	
	// 혈맹이름 변경해야할때
	public S_BloodPledgeNameChange(String clanname) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeS(clanname);// 클랜명
		writeC(0x00);
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
		return S_BLOODPLEDGE_NAME_CHANGE;
	}
}

