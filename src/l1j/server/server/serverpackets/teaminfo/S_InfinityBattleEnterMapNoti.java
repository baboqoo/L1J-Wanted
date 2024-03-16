package l1j.server.server.serverpackets.teaminfo;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_InfinityBattleEnterMapNoti extends ServerBasePacket {
	private static final String S_INFINITY_BATTLE_ENTER_MAP_NOTI = "[S] S_InfinityBattleEnterMapNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x093c;
	
	public S_InfinityBattleEnterMapNoti(int team_id) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
		writeC(0x08);// team_id
		writeC(team_id);
		writeH(0x00);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_INFINITY_BATTLE_ENTER_MAP_NOTI;
	}
}

