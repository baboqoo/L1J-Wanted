package l1j.server.server.command.executor;

import static l1j.server.server.model.skill.L1SkillId.BLESS_WEAPON;
import static l1j.server.server.model.skill.L1SkillId.COMA_B;
import static l1j.server.server.model.skill.L1SkillId.FEATHER_BUFF_A;
import static l1j.server.server.model.skill.L1SkillId.BUFF_BLACK_SAND;
import static l1j.server.server.model.skill.L1SkillId.IRON_SKIN;
import static l1j.server.server.model.skill.L1SkillId.LIFE_MAAN;
import static l1j.server.server.model.skill.L1SkillId.PHYSICAL_ENCHANT_DEX;
import static l1j.server.server.model.skill.L1SkillId.PHYSICAL_ENCHANT_STR;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.model.skill.L1SkillUseType;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1RessBuff implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1RessBuff();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1RessBuff() {}
	
	static final int[] BUFF_ARRAY = { PHYSICAL_ENCHANT_DEX, PHYSICAL_ENCHANT_STR, BLESS_WEAPON, IRON_SKIN, FEATHER_BUFF_A, LIFE_MAAN, BUFF_BLACK_SAND, COMA_B };

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			for (L1PcInstance tg : L1World.getInstance().getVisiblePlayer(pc)) {
				L1SkillUse l1skilluse = new L1SkillUse(true);
				for (int i = 0; i < BUFF_ARRAY.length; i++) {
					l1skilluse.handleCommands(tg, BUFF_ARRAY[i], tg.getId(), tg.getX(), tg.getY(), 0, L1SkillUseType.GMBUFF);
				}
			}
			return true;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " 커멘드 에러"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(168), true), true);
			return false;
		}
	}
}


