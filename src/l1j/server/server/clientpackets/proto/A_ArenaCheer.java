package l1j.server.server.clientpackets.proto;

import io.netty.util.internal.StringUtil;
import l1j.server.common.data.eArenaTeam;
import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.indun.S_ArenaCheer;

public class A_ArenaCheer extends ProtoHandler {
	protected A_ArenaCheer(){}
	private A_ArenaCheer(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private eArenaTeam _to_team_id;
	private String _text_msg;

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);// 0x08
		_to_team_id = eArenaTeam.fromInt(readC());
		readP(1);// 0x12
		_text_msg	= readString();
		if (_to_team_id == null) {
			_pc.sendPackets(S_ArenaCheer.FAIL);
			return;
		}
		if (StringUtil.isNullOrEmpty(_text_msg)) {
			_pc.sendPackets(S_ArenaCheer.FAIL);
			return;
		}
		
		_pc.sendPackets(S_ArenaCheer.SUCCESS);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_ArenaCheer(data, client);
	}

}

