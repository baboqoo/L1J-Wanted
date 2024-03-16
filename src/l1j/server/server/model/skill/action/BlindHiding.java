package l1j.server.server.model.skill.action;

import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_Invis;
import l1j.server.server.serverpackets.S_Sound;
import l1j.server.server.serverpackets.object.S_NPCObject;

public class BlindHiding extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.sendPackets(S_Sound.INVIS_SOUND);
			pc.invisible(true);
		} else if (cha instanceof L1NpcInstance) {
			L1NpcInstance npc = (L1NpcInstance) cha;
			Broadcaster.broadcastPacket(npc, new S_Invis(npc.getId(), 1), true);
			Broadcaster.broadcastPacket(npc, S_Sound.INVIS_SOUND);
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.delBlindHiding();
		} else if (cha instanceof L1NpcInstance) {
			L1NpcInstance npc = (L1NpcInstance) cha;
			npc.broadcastPacket(new S_Invis(npc.getId(), 0), true);
			npc.broadcastPacket(new S_NPCObject(npc), true);
		}
	}
	
	@Override
	public void icon(L1PcInstance pc, int time) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void wrap(L1PcInstance pc, boolean flag) {
		super.wrap(pc, flag);
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new BlindHiding().setValue(_skillId, _skill);
	}

}

