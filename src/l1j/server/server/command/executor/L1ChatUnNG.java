package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.spell.S_SkillIconGFX;

public class L1ChatUnNG implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1ChatUnNG();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1ChatUnNG() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer tokenizer = new StringTokenizer(arg);
			L1PcInstance target = L1World.getInstance().getPlayer(tokenizer.nextToken());
			if (target != null) {
				target.getSkill().killSkillEffectTimer(L1SkillId.STATUS_CHAT_PROHIBITED);
				target.sendPackets(new S_SkillIconGFX(S_SkillIconGFX.CHAT_ICON, 0), true);
				target.sendPackets(L1ServerMessage.sm288);
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("해당캐릭의 채금을 해제 했습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(285), true), true);
				return true;
			}
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("해당 캐릭터를 찾을 수 없습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(286), true), true);
			return false;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [캐릭터명] 이라고 입력해 주세요."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(157), true), true);
			return false;
		}
	}
}


