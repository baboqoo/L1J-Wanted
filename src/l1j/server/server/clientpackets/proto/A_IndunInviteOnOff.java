package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;

public class A_IndunInviteOnOff extends ProtoHandler {
	protected A_IndunInviteOnOff(){}
	private A_IndunInviteOnOff(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);// 0x08
		boolean onoff = readBool();// on off
		_pc.getConfig().setIndunInviteOnOff(onoff);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_IndunInviteOnOff(data, client);
	}

}

