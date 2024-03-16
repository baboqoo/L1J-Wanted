package l1j.server.server.serverpackets.party;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_PartyAssistTargetBroadcastNoti extends ServerBasePacket {
	private static final String S_PARTY_ASSIST_TARGET_BROADCAST_NOTI = "[S] S_PartyAssistTargetBroadcastNoti";
	private byte[] _byte = null;
	public static final int ASSIST	= 0x0258;
	
	public static final S_PartyAssistTargetBroadcastNoti RELEASE	= new S_PartyAssistTargetBroadcastNoti(0);// 파티어시스트종료
	
	public S_PartyAssistTargetBroadcastNoti(int target_id) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(ASSIST);
		writeRaw(0x08);// target_id
		writeBit(target_id);// 타겟
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
		return S_PARTY_ASSIST_TARGET_BROADCAST_NOTI;
	}

}

