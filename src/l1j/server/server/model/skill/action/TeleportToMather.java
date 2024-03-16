package l1j.server.server.model.skill.action;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_Paralysis;

public class TeleportToMather extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.getMap().isEscapable() || pc.isGm()) {
				if (pc.isNotTeleport() || pc.getNetConnection().isInterServer()) {
					return 0;
				}
				pc.getTeleport().start(33051, 32337, (short) 4, 5, true);
			} else {
				pc.sendPackets(S_Paralysis.TELEPORT_UNLOCK);
				pc.sendPackets(L1ServerMessage.sm647);
			}
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		// TODO Auto-generated method stub

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
		return new TeleportToMather().setValue(_skillId, _skill);
	}

}

