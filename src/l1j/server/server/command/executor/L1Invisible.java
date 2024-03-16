package l1j.server.server.command.executor;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_Invis;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1Invisible implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Invisible();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1Invisible() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (pc == null) {
				return false;
			}
			if (!pc.getSkill().hasSkillEffect(L1SkillId.INVISIBILITY)) {
				pc.setGmInvis(true);
				pc.sendPackets(new S_Invis(pc.getId(), 1), true);
				pc.broadcastPacket(new S_RemoveObject(pc), true);
				pc.getSkill().setSkillEffect(L1SkillId.INVISIBILITY, 0);
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("투명상태가 되었습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(458), true), true);
				L1DollInstance doll = pc.getDoll();
				if (doll != null) {
					pc.sendPackets(new S_Invis(doll.getId(), 1), true);
					S_RemoveObject doll_remove = null;
					for (L1PcInstance target : L1World.getInstance().getVisiblePlayer(doll)) {
						if (target == null || target.getNetConnection() == null || pc == target) {
							continue;
						}
						if (doll_remove == null) {
							doll_remove = new S_RemoveObject(doll);
						}
						target.sendPackets(doll_remove);
					}
					if (doll_remove != null) {
						doll_remove.clear();
						doll_remove = null;
					}
				}
			} else {
				pc.setGmInvis(false);
				pc.delInvis();
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("투명상태를 해제했습니다. "), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(459), true), true);
			}
			return true;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " 커멘드 에러"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(168), true), true);
			return false;
		}
	}
}


