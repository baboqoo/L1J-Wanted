package l1j.server.server.model.skill.action;

import l1j.server.server.ActionCodes;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.action.S_Userform;

public class Alternate extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.getSkill().hasSkillEffect(VANGUARD)) {
				pc.getSkill().removeSkillEffect(VANGUARD);
			}
			pc._isLancerForm = !pc._isLancerForm;
			pc.setCurrentWeapon(pc._isLancerForm ? ActionCodes.ACTION_WalkSpear : pc.getWeapon().getItem().getWeaponType().getAction());// 무기 상태값 변경
			pc.sendPackets(pc._isLancerForm ? S_Userform.LONG : S_Userform.NONE);
			pc.broadcastPacketWithMe(new S_CharVisualUpdate(pc), true);
			pc.sendPackets(new S_PacketBox(S_PacketBox.ATTACK_RANGE, pc, pc.getWeapon()), true);
			pc.sendPackets(S_Userform.REFRESH);
			pc.send_effect(pc._isLancerForm ? 19354 : 19351);
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
		return new Alternate().setValue(_skillId, _skill);
	}

}

