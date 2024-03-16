package l1j.server.server.clientpackets;

import l1j.server.IndunSystem.orim.OrimController;
import l1j.server.server.GameClient;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.action.S_DoActionGFX;

public class C_ExtraCommand extends ClientBasePacket {
	private static final String C_EXTRA_COMMAND = "[C] C_ExtraCommand";

	public C_ExtraCommand(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);
		int actionId = readC();

		L1PcInstance pc = client.getActiveChar();
		if (pc == null || pc.isGhost() || pc.getTeleport().isTeleport() || pc.isInvisble()) {
			return;
		}
		if (pc.isShapeChange()) {// 만일을 위해, 변신중은 타플레이어에 송신하지 않는다
			int spriteId = pc.getSpriteId();
			if (spriteId != 6080 && spriteId != 6094) {
				return;
			}
		} else {
		    pc.broadcastPacket(new S_DoActionGFX(pc.getId(), actionId), true);
		}

		if (pc.getMapId() == 9101 && pc.isInParty()) {
			orim(pc, actionId);
		}
	}
	
	private void orim(L1PcInstance pc, int actionId){
		if ((pc.getParty().isLeader(pc) & pc.getX() == 32799) && pc.getY() == 32808 && pc.getMoveState().getHeading() == 4) {
			if (!pc.isShapeChange()) {
				pc.sendPackets(new S_EffectLocation(pc.getX(), pc.getY(), 3206), true);
			}
			if (actionId == 68) {
				OrimController.getInstance().explain = true;
				return;
			}
			int localL1PcInstance3;
			if ((actionId == 66) && (OrimController.getInstance().attackTrap().booleanValue()) && (pc.getMoveState().getHeading() == 4)) {
				L1PcInstance[] arrayOfL1PcInstance1;
				localL1PcInstance3 = (arrayOfL1PcInstance1 = OrimController.getInstance().getPlayMemberArray()).length;
				for (int localL1PcInstance1 = 0; localL1PcInstance1 < localL1PcInstance3; localL1PcInstance1++) {
					L1PcInstance pc1 = arrayOfL1PcInstance1[localL1PcInstance1];
					pc1.send_effect(2029);
				}
				L1PcInstance[] arrayOfL1PcInstance3;
				int i;
				if (OrimController.getInstance().getAtCount() % 2 == 0) {
					L1NpcInstance shell = OrimController.getInstance().getShell1();
					S_DoActionGFX gfxShell1 = new S_DoActionGFX(shell.getId(),10242);
					shell.broadcastPacket(gfxShell1, true);
					i = (arrayOfL1PcInstance3 = OrimController.getInstance().getPlayMemberArray()).length;
					for (localL1PcInstance3 = 0; localL1PcInstance3 < i; localL1PcInstance3++) {
						L1PcInstance pc1 = arrayOfL1PcInstance3[localL1PcInstance3];
						pc1.sendPackets(new S_EffectLocation(32789, 32817, 8233), true);
					}
				} else {
					L1NpcInstance shell = OrimController.getInstance().getShell2();
					S_DoActionGFX gfxShell2 = new S_DoActionGFX(shell.getId(),10242);
					shell.broadcastPacket(gfxShell2, true);
					i = (arrayOfL1PcInstance3 = OrimController.getInstance().getPlayMemberArray()).length;
					for (localL1PcInstance3 = 0; localL1PcInstance3 < i; localL1PcInstance3++) {
						L1PcInstance pc1 = arrayOfL1PcInstance3[localL1PcInstance3];
						pc1.sendPackets(new S_EffectLocation(32795, 32817, 8233), true);
					}
				}
				OrimController.getInstance().addScore(50);
				OrimController.getInstance().addAtCount();
			} else if ((actionId == 69) && (OrimController.getInstance().dependTrap().booleanValue()) && (pc.getMoveState().getHeading() == 4)) {
				L1PcInstance[] arrayOfL1PcInstance2;
				localL1PcInstance3 = (arrayOfL1PcInstance2 = OrimController.getInstance().getPlayMemberArray()).length;
				for (int localL1PcInstance2 = 0; localL1PcInstance2 < localL1PcInstance3; localL1PcInstance2++) {
					L1PcInstance pc1 = arrayOfL1PcInstance2[localL1PcInstance2];
					pc1.send_effect_self(10165);
					pc1.send_effect(2030);
				}
				OrimController.getInstance().addDeCount();
			}
		}
	}

	@Override
	public String getType() {
		return C_EXTRA_COMMAND;
	}
}

