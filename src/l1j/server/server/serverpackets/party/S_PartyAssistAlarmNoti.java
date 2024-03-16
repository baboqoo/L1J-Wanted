package l1j.server.server.serverpackets.party;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_PartyAssistAlarmNoti extends ServerBasePacket {
	private static final String S_PARTY_ASSIST_ALARM_NOTI = "[S] S_PartyAssistAlarmNoti";
	private byte[] _byte = null;
	
	public S_PartyAssistAlarmNoti(int code) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(code);
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
		return S_PARTY_ASSIST_ALARM_NOTI;
	}

}

