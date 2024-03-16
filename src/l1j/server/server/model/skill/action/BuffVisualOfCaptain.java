package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;

public class BuffVisualOfCaptain extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			icon(pc, time);
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			hideObject(pc);
			pc.sendPackets(new S_SpellBuffNoti(pc, _skillId, false, -1), true);
		}
	}
	
	/**
	 * 은신몬스터 오브젝트 갱신
	 * @param pc
	 */
	void hideObject(L1PcInstance pc) {
		for (L1Object obj : pc.getKnownObjects()) {
			if (obj instanceof L1MonsterInstance == false) {
				continue;
			}
			L1MonsterInstance mon = (L1MonsterInstance) obj;
			if (!mon.getNpcTemplate().isHide()) {
				continue;
			}
			mon.allTargetClear();// 몬스터 타겟 중지
			pc.sendPackets(new S_RemoveObject(mon), true);// 제거
			pc.removeKnownObject(mon);// 기존 인식 제거
		}
	}
	
	@Override
	public void icon(L1PcInstance pc, int time) {
		pc.sendPackets(new S_SpellBuffNoti(pc, _skillId, true, time), true);
	}
	
	@Override
	public void wrap(L1PcInstance pc, boolean flag) {
		pc.sendPackets(new S_OwnCharStatus(pc), true);
		pc.sendPackets(new S_SPMR(pc), true);
		super.wrap(pc, flag);
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new BuffVisualOfCaptain().setValue(_skillId, _skill);
	}

}

