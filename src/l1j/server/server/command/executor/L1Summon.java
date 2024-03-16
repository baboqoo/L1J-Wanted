package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Npc;

public class L1Summon implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Summon();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1Summon() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (pc == null || pc.getNetConnection() == null) {
				return false;
			}
			StringTokenizer tok = new StringTokenizer(arg);
			String desc = tok.nextToken();
			int npcid = 0;
			try {
				npcid = Integer.parseInt(desc);
			} catch (NumberFormatException e) {
				npcid = NpcTable.getInstance(). findNpcIdByNameWithoutSpace(desc);
				if (npcid == 0) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("해당 NPC가 발견되지 않습니다. "), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(398), true), true);
					return false;
				}
			}
			int count = 1;
			if (tok.hasMoreTokens()) {
				count = Integer.parseInt(tok.nextToken());
			}
			L1Npc npc = NpcTable.getInstance(). getTemplate(npcid);
			L1SummonInstance summonInst = null;
			for (int i = 0; i < count; i++) {
				summonInst = new L1SummonInstance(npc, pc, 0);
				summonInst.setPetcost(0);
			}
			desc = NpcTable.getInstance().getTemplate(npcid).getDesc();
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(desc + "(ID:" + npcid + ") (" + count + ")를 소환했습니다. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(desc  + "(ID:" + npcid  + ") (" + count  + S_SystemMessage.getRefText(695), true), true);
			return true;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [npcid or name] [서먼수] 라고 입력해 주세요. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(696), true), true);
			return false;
		}
	}
}


