package l1j.server.server.clientpackets;

import l1j.server.LFCSystem.InstanceEnums.InstStatus;
import l1j.server.LFCSystem.InstanceSpace;
import l1j.server.server.GameClient;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.Instance.L1PcInstance;

public class C_Restart extends ClientBasePacket {
	private static final String C_RESTART = "[C] C_Restart";
	
	public C_Restart(byte[] decrypt, GameClient client) throws Exception {
		super(decrypt);
		try {
			if (client == null) {
				return;
			}
			L1PcInstance pc = client.getActiveChar();	
			if (pc == null) {
				return;
			}
			if (pc.isHold() || pc.isDesperado() || pc.isOsiris() || pc.isEternity() || pc.isPhantom() || pc.isShockAttackTeleport() || pc.isShadowStepChaser() || pc.isBehemoth()) {
				return;
			}
			/** LFC **/
			if (InstanceSpace.isInInstance(pc) && pc.getInstStatus() != InstStatus.INST_USERSTATUS_NONE) {
				return;
			}
			if (pc.getOnlineStatus() != 0) {
				if (client.isInterServer()) {
					client.releaseInter();
				}
				pc.setOnlineStatus(0);
				L1Clan clan = pc.getClan();
				if (clan != null) {
					clan.updateClanMemberOnline(pc);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			clear();
		}
	}
	
	@Override
	public String getType() {
		return C_RESTART;
	}
}

