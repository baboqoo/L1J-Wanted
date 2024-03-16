package l1j.server.server.model.skill.action;

import l1j.server.Config;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_Effect;

public class ReturnToNature extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (Config.ALT.RETURN_TO_NATURE && cha instanceof L1SummonInstance) {
			L1SummonInstance summon = (L1SummonInstance) cha;
			summon.broadcastPacket(new S_Effect(summon.getId(), 2245), true);
			summon.returnToNature();
		} else if (attacker instanceof L1PcInstance) {
			((L1PcInstance)attacker).sendPackets(L1ServerMessage.sm79);
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
		return new ReturnToNature().setValue(_skillId, _skill);
	}

}

