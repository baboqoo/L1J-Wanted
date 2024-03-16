package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1ReturnExp implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1ReturnExp();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1ReturnExp() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer tokenizer = new StringTokenizer(arg);
			String pcName = tokenizer.nextToken();
			L1PcInstance target = L1World.getInstance().getPlayer(pcName);
			if (target != null) {
				int oldLevel	= target.getLevel();
				long needExp	= ExpTable.getNeedExpNextLevel(oldLevel);
				long resExp		= 0;
		        if (oldLevel >= 87) {
		        	resExp = (long) (needExp * 0.001D);	// 0.1%
		        } else if (oldLevel >= 86) {
					resExp = (long) (needExp * 0.005D);	// 0.5%
				} else if (oldLevel >= 84) {
					resExp = (long) (needExp * 0.01D);	// 0.1%
				} else if (oldLevel >= 82) {
					resExp = (long) (needExp * 0.015D);	// 0.15%
				} else if (oldLevel >= 80) {
					resExp = (long) (needExp * 0.02D);	// 0.2%
				} else if (oldLevel >= 79) {
					resExp = (long) (needExp * 0.025D);	// 0.25%
				} else if (oldLevel >= 75) {
					resExp = (long) (needExp * 0.03D);	// 0.3%
				} else if (oldLevel >= 70) {
					resExp = (long) (needExp * 0.035D);	// 0.35%
				} else if (oldLevel >= 65) {
					resExp = (long) (needExp * 0.04D);	// 0.4%
				} else {
					resExp = (long) (needExp * 0.045D);	// 0.45%
				}
		        
		        resExp = (long)(resExp * 0.99D);
		        if (resExp <= 0) {
		        	return false;
		        }
				
				target.addExp(resExp);
				target.save();
				target.saveInventory();
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("해당 캐릭터의 경험치가 복구되었습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(639), true), true);
				return true;
			}
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("해당 캐릭터 미접속 상태."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(640), true), true);
			return false;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [캐릭명]"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(157), true), true);
			return false;
		}
	}
}


