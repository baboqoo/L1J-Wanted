package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.model.skill.L1SkillUseType;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Skills;

public class L1AllBuff implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1AllBuff();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1AllBuff() {}
	
	static final int[] allBuffSkill = { L1SkillId.DECREASE_WEIGHT, L1SkillId.PHYSICAL_ENCHANT_DEX,
			L1SkillId.PHYSICAL_ENCHANT_STR, L1SkillId.BLESS_WEAPON, L1SkillId.BERSERKERS,
			L1SkillId.IMMUNE_TO_HARM, L1SkillId.REDUCTION_ARMOR, L1SkillId.BOUNCE_ATTACK,
			L1SkillId.DOUBLE_BRAKE, L1SkillId.UNCANNY_DODGE,
			L1SkillId.GLOWING_WEAPON, L1SkillId.RESIST_MAGIC, 
			L1SkillId.CLEAR_MIND, L1SkillId.PROTECTION_FROM_ELEMENTAL,
			L1SkillId.AQUA_PROTECTER, L1SkillId.BURNING_WEAPON, L1SkillId.IRON_SKIN, L1SkillId.EXOTIC_VITALIZE,
			L1SkillId.WATER_LIFE, L1SkillId.ELEMENTAL_FIRE, L1SkillId.SOUL_OF_FLAME };

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String name = st.nextToken();
			L1PcInstance target = L1World.getInstance(). getPlayer(name);
			if (target == null) {
				pc.sendPackets(new S_ServerMessage(73, name), true); // \f1%0은 게임을 하고 있지 않습니다.
				return false;
			}

			L1BuffUtil.haste(target, 3600 * 1000);
			//L1BuffUtil.brave(target, 3600 * 1000);
			//L1PolyMorph.doPoly(target, 5641, 7200, L1PolyMorph.MORPH_BY_GM);
			L1Skills skill = null;
			L1SkillUse use = new L1SkillUse(true);
			for (int i = 0; i < allBuffSkill.length; i++) {
				skill = SkillsTable.getTemplate(allBuffSkill[i]);
				use.handleCommands(target, allBuffSkill[i], target.getId(), target.getX(), target.getY(), skill.getBuffDuration() * 1000, L1SkillUseType.GMBUFF);
			}
			use = null;
			st = null;
			return true;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".개인버프 [캐릭명]"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(157), true), true);
			return false;
		}
	}
}


