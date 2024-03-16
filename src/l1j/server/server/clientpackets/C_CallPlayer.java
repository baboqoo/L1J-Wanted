package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StringUtil;

public class C_CallPlayer extends ClientBasePacket {
	private static final String C_CALL_PLAYER = "[C] C_CallPlayer";

	public C_CallPlayer(byte[] decrypt, GameClient client) {
		super(decrypt);
		L1PcInstance pc = client.getActiveChar();
		if (pc == null || !pc.isGm()) {
			return;
		}
		String name = readS();
		if (StringUtil.isNullOrEmpty(name)) {
			return;
		}

		L1PcInstance target = L1World.getInstance().getPlayer(name);
		if (target == null) {
			//pc.sendPackets(new S_SystemMessage(String.format("%s 라는 케릭터는 월드에 접속중이지 않습니다.", name)), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(18), name), true);
			return;
		}
		if (target == pc) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("자기 자신은 소환할 수 없습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(82), true), true);
			return;
		}
		L1Location loc = L1Location.randomLocation(pc.getLocation(), 1, 2, false);
		target.getTeleport().start(loc, target.getMoveState().getHeading(), false);
	}

	@Override
	public String getType() {
		return C_CALL_PLAYER;
	}
}


