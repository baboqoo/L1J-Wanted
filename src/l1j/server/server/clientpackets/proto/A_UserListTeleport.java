package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class A_UserListTeleport extends ProtoHandler {
	protected A_UserListTeleport(){}
	private A_UserListTeleport(byte[] data, GameClient client) {
		super(data, client);
	}
	
//AUTO SRM: 	private static final S_SystemMessage OTHER_SERVER	= new S_SystemMessage("대상은 같은 서버가 아닙니다."); // CHECKED OK
	private static final S_SystemMessage OTHER_SERVER	= new S_SystemMessage(S_SystemMessage.getRefText(146), true);
//AUTO SRM: 	private static final S_SystemMessage SELF_CHECK		= new S_SystemMessage("대상은 자신과 동일합니다."); // CHECKED OK
	private static final S_SystemMessage SELF_CHECK		= new S_SystemMessage(S_SystemMessage.getRefText(147), true);

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost() || !_pc.isGm()) {
			return;
		}
		readP(1);// 0x08
		int serverId		= readBit();
		if (Config.VERSION.SERVER_NUMBER != serverId) {
			_pc.sendPackets(OTHER_SERVER);
			return;
		}
		readP(1);// 0x12
		int nameLength		= readC();
		String name			= readS(nameLength);
		if (name.isEmpty()) {
			return;
		}
		
		L1PcInstance target	= L1World.getInstance().getPlayer(name);
		if (target == null) {
			//_pc.sendPackets(new S_SystemMessage(String.format("%s 라는 케릭터는 월드에 접속중이지 않습니다.", name)), true);
			_pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(18), name), true);
			return;
		}
		if (target == _pc) {
			_pc.sendPackets(SELF_CHECK);
			return;
		}
		L1Location loc = L1Location.randomLocation(target.getLocation(), 1, 2, false);
		_pc.getTeleport().start(loc, _pc.getMoveState().getHeading(), false);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_UserListTeleport(data, client);
	}

}


