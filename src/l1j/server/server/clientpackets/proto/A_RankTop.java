package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.controller.action.UserRanking;

public class A_RankTop extends ProtoHandler {
	protected A_RankTop(){}
	private A_RankTop(byte[] data, GameClient client) {
		super(data, client);
	}

	private int classId;
	private long version;
	
	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);// 0x08
		classId = readC();// 검색하는 클래스 타입 0~10
		readP(1);// 0x10
		version = readLong();
		UserRanking.send_top_ranker_ack(_pc, classId, version);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_RankTop(data, client);
	}

}

