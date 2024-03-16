package l1j.server.server.serverpackets.returnedstat;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Ability;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_StatusBaseNoti extends ServerBasePacket {
	private static final String S_STATUS_BASE_NOTI	= "[S] S_StatusBaseNoti";
	private byte[] _byte	= null;
	
	public static final int STAT_REFRESH	= 0x01ea;

	public S_StatusBaseNoti(L1Ability ability) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(STAT_REFRESH);
		
		writeRaw(0x08);
		writeRaw(ability.getStr());
		
		writeRaw(0x10);
		writeRaw(ability.getInt());
		
		writeRaw(0x18);
		writeRaw(ability.getWis());
		
		writeRaw(0x20);
		writeRaw(ability.getDex());
		
		writeRaw(0x28);
		writeRaw(ability.getCon());
		
		writeRaw(0x30);
		writeRaw(ability.getCha());
		
		writeH(0x00);
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
		return S_STATUS_BASE_NOTI;
	}
}

