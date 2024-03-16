package l1j.server.server.command.executor;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_MessageYN;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StringUtil;

public class L1Ress implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Ress();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1Ress() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			int objid = pc.getId();
			for (L1PcInstance tg : L1World.getInstance(). getVisiblePlayer(pc)) {
				if (tg.getCurrentHp() == 0 && tg.isDead()) {
					tg.send_effect(3944);
					// 축복된 부활 스크롤과 같은 효과
					tg.setTempID(objid);
					tg.sendPackets(new S_MessageYN(322, StringUtil.EmptyString), true); // 또 부활하고 싶습니까? (Y/N)
				} else {
					tg.send_effect(832);
					tg.setCurrentHp(tg.getMaxHp());
					tg.setCurrentMp(tg.getMaxMp());
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


