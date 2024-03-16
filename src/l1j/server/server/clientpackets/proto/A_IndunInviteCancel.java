package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.indun.S_IndunInviteDeny;

public class A_IndunInviteCancel extends ProtoHandler {
	protected A_IndunInviteCancel(){}
	private A_IndunInviteCancel(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);
		int roomnumber		= readBit(); // 방번호
		readP(1);
		int targetlength	= readC();
		String targetname	= readS(targetlength); // 타겟 이름
		L1PcInstance target	= L1World.getInstance().getPlayer(targetname);
		if (target == null) {
			return;
		}
		target.sendPackets(new S_IndunInviteDeny(roomnumber, _pc.getName(), target.getName()), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_IndunInviteCancel(data, client);
	}

}

