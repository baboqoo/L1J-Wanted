package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.L1PinkName;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_PacketBox;

public class ShiningShield extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc._shiningShieldValue = (attacker != null && attacker.getId() == pc.getId()) ? 8 : 4;
			pc.getAC().addAc(-pc._shiningShieldValue);
			pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 3941, time), true);
			
			if (attacker != cha && pc.isPinkName()) {
				L1PinkName.onAction((L1PcInstance)attacker);
			}
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.getAC().addAc(pc._shiningShieldValue);
		}
	}
	
	@Override
	public void icon(L1PcInstance pc, int time) {
		pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 3941, time), true);
	}
	
	@Override
	public void wrap(L1PcInstance pc, boolean flag) {
		super.wrap(pc, flag);
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new ShiningShield().setValue(_skillId, _skill);
	}

}

