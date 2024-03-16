package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.spell.S_SkillIconGFX;

public class L1ChatNG2 implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1ChatNG2();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1ChatNG2() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String name = st.nextToken();
			int time = Integer.parseInt(st.nextToken());
			String reason = st.nextToken();

			L1PcInstance tg = L1World.getInstance(). getPlayer(name);

			if (tg != null) {
				tg.getSkill().setSkillEffect(L1SkillId.STATUS_CHAT_PROHIBITED, time * 60 * 1000);
				tg.sendPackets(new S_SkillIconGFX(S_SkillIconGFX.CHAT_ICON, time * 60), true);
				tg.sendPackets(new S_ServerMessage(286, String.valueOf(time)), true); // \f3게임에 적합하지 않는 행동이기 (위해)때문에, 향후%0분간 채팅을 금지합니다.
//AUTO SRM: 				L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(name + " 캐릭터 "+ String.valueOf(time) + "분간 채팅금지 (사유: " + reason + ")"), true); // CHECKED OK
				L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(name  + S_SystemMessage.getRefText(279) + String.valueOf(time)  + S_SystemMessage.getRefText(280) + reason  + ")", true), true);
				return true;
			}
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("해당 캐릭터 미접속."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(277), true), true);
			return false;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [캐릭명] [분] [채금사유] 입력."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(281), true), true);
			return false;
		}
	}
}


