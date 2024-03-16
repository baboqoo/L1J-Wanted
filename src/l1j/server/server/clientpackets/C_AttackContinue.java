package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.controller.AttackContinueController;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public class C_AttackContinue extends ClientBasePacket {

	public C_AttackContinue(byte[] decrypt, GameClient client) {
		super(decrypt);
		L1PcInstance pc = client.getActiveChar();
		if (pc == null || pc.isInvisble()) {
			return;
		}
		L1Object target = L1World.getInstance().findObject(readD());
		if (target == null || target instanceof L1Character == false || target == pc) {
			AttackContinueController.stop(pc);
			return;
		}
		AttackContinueController.start(pc, (L1Character)target);
	}
}
