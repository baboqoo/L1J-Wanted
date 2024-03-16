package l1j.server.server.command.executor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.model.skill.L1SkillUseType;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Skills;

public class L1Buff implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Buff();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1Buff() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer tok = new StringTokenizer(arg);
			Collection<L1PcInstance> players = null;
			String s = tok.nextToken();
			//if (s.equals("나")) {
			if (s.equals("me")) {
				players = new ArrayList<L1PcInstance>();
				players.add(pc);
				s = tok.nextToken();
			//} else if (s.equals("전체")) {
			} else if (s.equals("all")) {
				players = L1World.getInstance(). getAllPlayers();
				s = tok.nextToken();
			} else {
				players = L1World.getInstance(). getVisiblePlayer(pc);
			}
			
			int skillId = Integer.parseInt(s);
			int time = 0;
			if (tok.hasMoreTokens()) {
				time = Integer.parseInt(tok.nextToken());
			}
			
			L1Skills skill = SkillsTable.getTemplate(skillId);
			switch (skill.getTarget()) {
			case BUFF:
				for (L1PcInstance tg : players) {
					new L1SkillUse(true).handleCommands(pc, skillId, tg.getId(), tg.getX(), tg.getY(), time, L1SkillUseType.SPELLSC);
				}
				return true;
			case NONE:
				for (L1PcInstance tg : players) {
					new L1SkillUse(true).handleCommands(tg, skillId, tg.getId(), tg.getX(), tg.getY(), time, L1SkillUseType.GMBUFF);
				}
				return true;
			default:
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("버프계의 스킬이 아닙니다. "), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(225), true), true);
				return false;
			}
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [전체, 나] [스킬아이디] [시간] 라고 입력해 주세요. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(226), true), true);
			return false;
		}
	}
}


