package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_MessageYN;
import l1j.server.server.utils.FaceToFace;

public class C_Fight extends ClientBasePacket {

	private static final String C_FIGHT = "[C] C_Fight";

	public C_Fight(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);
		L1PcInstance pc = client.getActiveChar();
		if (pc == null || pc.isGhost()) {
			return;
		}
		L1PcInstance target = FaceToFace.faceToFace(pc);
		if (target == null || target.isStop()) {
			return;
		}
		if (pc.getFightId() != 0) {
			pc.sendPackets(L1ServerMessage.sm633); 
			return;
		}
		if (target.getFightId() != 0) {
			target.sendPackets(L1ServerMessage.sm634); 
			return;
		}
		pc.setFightId(target.getId());
		target.setFightId(pc.getId());
		target.sendPackets(new S_MessageYN(630, pc.getName()), true);
	}

	@Override
	public String getType() {
		return C_FIGHT;
	}

}

