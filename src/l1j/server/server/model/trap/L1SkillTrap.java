package l1j.server.server.model.trap;

import l1j.server.server.construct.L1RegionStatus;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.model.skill.L1SkillUseType;
import l1j.server.server.storage.TrapStorage;

public class L1SkillTrap extends L1Trap {
	private final int _skillId;
	private final int _skillTimeSeconds;

	public L1SkillTrap(TrapStorage storage) {
		super(storage);
		_skillId			= storage.getInt("skillId");
		_skillTimeSeconds	= storage.getInt("skillTimeSeconds");
	}

	@Override
	public void onTrod(L1PcInstance trodFrom, L1Object trapObj) {
		if (trodFrom.getRegion() == L1RegionStatus.SAFETY) {
			return;
		}
		sendEffect(trapObj);
		new L1SkillUse(true).handleCommands(trodFrom, _skillId, trodFrom.getId(), trodFrom.getX(), trodFrom.getY(), _skillTimeSeconds, L1SkillUseType.GMBUFF);
	}

}

