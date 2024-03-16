package l1j.server.server.command.executor;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1Prison implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Prison();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1Prison() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			L1PcInstance target = L1World.getInstance().getPlayer(arg);
            if (target != null) {
                holdnow(pc, target);
                return true;
            }
//AUTO SRM:             pc.sendPackets(new S_SystemMessage("그런 캐릭터는 없습니다."), true); // CHECKED OK
            pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(545), true), true);
            return false;
		} catch (Exception exception) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [캐릭명] 라고 입력해 주세요. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(157), true), true);
			return false;
		}
	}
	
	private void holdnow(L1PcInstance pc, L1PcInstance target) {
        try {
        	target.getTeleport().start(32835, 32782, (short) 701, 5, true);
//AUTO SRM:         	pc.sendPackets(new S_SystemMessage((new StringBuilder()).append(target.getName()).append(" 님 감옥으로 이동됨.").toString()), true); // CHECKED OK
        	pc.sendPackets(new S_SystemMessage((new StringBuilder()).append(target.getName()).append(S_SystemMessage.getRefText(547)).toString(), true), true);
//AUTO SRM:             target.sendPackets(new S_SystemMessage("감옥에 감금 되었습니다."), true); // CHECKED OK
            target.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(548), true), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


