package l1j.server.server.model.skill.action;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_ExpBoostingInfo;
import l1j.server.server.serverpackets.S_PacketBox;

public class CookDogam extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
            pc.getAbility().addDamageReduction(5);
            pc.setCookingId(_skillId);
            pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_COOKING, 187, 1800), true);
            pc.sendPackets(L1ServerMessage.sm1426);
            pc.sendPackets(new S_ExpBoostingInfo(pc), true);
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.getAbility().addDamageReduction(-5);
			pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_COOKING, 187, 0), true);
			pc.setCookingId(0);
			pc.sendPackets(new S_ExpBoostingInfo(pc), true);
		}
	}
	
	@Override
	public void icon(L1PcInstance pc, int time) {
		pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_COOKING, 187, 1800), true);
	}
	
	@Override
	public void wrap(L1PcInstance pc, boolean flag) {
		super.wrap(pc, flag);
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new CookDogam().setValue(_skillId, _skill);
	}

}

