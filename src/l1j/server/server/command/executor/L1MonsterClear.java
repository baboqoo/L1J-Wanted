package l1j.server.server.command.executor;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1MonsterClear implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1MonsterClear();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1MonsterClear() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		int cnt = 0;
		for (L1Object obj : L1World.getInstance().getObject()) {
			if (obj instanceof L1MonsterInstance) {
				L1MonsterInstance mon = (L1MonsterInstance) obj;
				if (mon.isDead()
						|| mon.getNpcId() == 7200003
						|| mon.getNpcId() == 7200029
						|| mon.getNpcId() == 7800000) {
					continue;
				}
				mon.die(pc);
				cnt++;
			}
		}
//AUTO SRM: 		pc.sendPackets(new S_SystemMessage("몬스터 " + cnt + "마리를 죽였습니다."), true); // CHECKED OK
		pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(485) + cnt  + S_SystemMessage.getRefText(486), true), true);
		return true;
	}
}


