package l1j.server.server.model.npc.action.id;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_PacketBox;

public class UhuntAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new UhuntAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private UhuntAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		if (s.equalsIgnoreCase("a")) {
			if (pc.getSkill().hasSkillEffect(L1SkillId.STATUS_CURSE_YAHEE)) {
				pc.sendPackets(L1ServerMessage.sm79);
			} else {
				pc.getSkill().setSkillEffect(L1SkillId.STATUS_CURSE_BARLOG, 1020 * 1000); // 1020
				pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_AURA, 1, 1020), true);
				pc.send_effect(750);
				pc.sendPackets(L1ServerMessage.sm1127);
			}
		}
		return null;
	}
}

