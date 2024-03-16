package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.controller.action.UserRanking;

public class A_RankMy extends ProtoHandler {
	protected A_RankMy(){}
	private A_RankMy(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private long version;

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);// 0x08
		version = readLong();
		UserRanking.send_my_ranking_ack(_pc, version);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_RankMy(data, client);
	}

}

