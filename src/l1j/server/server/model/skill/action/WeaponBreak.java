package l1j.server.server.model.skill.action;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.utils.CommonUtil;

public class WeaponBreak extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (attacker == null) {
			return 0;
		}
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			L1ItemInstance weapon = pc.getWeapon();
			if (weapon != null) {
				int weaponDamage = CommonUtil.random(attacker.getAbility().getTotalInt() / 3) + 1;
				pc.sendPackets(new S_ServerMessage(268, weapon.getLogNameRef()), true);
				pc.getInventory().receiveDamage(weapon, weaponDamage);
			}
		} else if (cha instanceof L1MonsterInstance) {
			((L1NpcInstance) cha).setWeaponBreaked(true);
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
		return new WeaponBreak().setValue(_skillId, _skill);
	}

}

