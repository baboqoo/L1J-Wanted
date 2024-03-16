package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.model.L1Einhasad;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.einhasad.S_RestExpInfoNoti;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1EinDayBonus implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1EinDayBonus();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1EinDayBonus() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String name = st.nextToken();
			//if (name.equals("전체")) {
			if (name.equals("all")) {
				for (L1PcInstance target : L1World.getInstance().getAllPlayers()) {
					if(!target.isPrivateShop() && !target.isAutoClanjoin() && !target.noPlayerCK && target.getAccount().getEinhasad().getDayBonus() == 0){
						L1Einhasad ein = target.getAccount().getEinhasad();
						ein.setDayBonus(1);
						ein.updateEinDayBonus();
						target.sendPackets(new S_RestExpInfoNoti(target), true);
//AUTO SRM: 						target.sendPackets(new S_SystemMessage("운영자가 아인하사드 일일 보너스를 지급하였습니다."), true); // CHECKED OK
						target.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(388), true), true);
					}
				}
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("전체 접속된 유저에게 아인하사드 일일 보너스를 지급하였습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(389), true), true);
				return true;
			}
			L1PcInstance target = L1World.getInstance().getPlayer(name);
			L1Einhasad ein = target.getAccount().getEinhasad();
			if (target != null && ein.getDayBonus() == 0) {
				ein.setDayBonus(1);
				ein.updateEinDayBonus();
				target.sendPackets(new S_RestExpInfoNoti(target), true);
//AUTO SRM: 				target.sendPackets(new S_SystemMessage("운영자가 아인하사드 일일 보너스를 지급하였습니다."), true); // CHECKED OK
				target.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(388), true), true);
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage(target.getName() + "님에게 아인하사드 일일 보너스 지급"), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(target.getName()  + S_SystemMessage.getRefText(390), true), true);
				return true;
			}
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("케릭터가 접속중이지 않습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(391), true), true);
			return false;
		} catch (Exception exception) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [전체/케릭명] 라고 입력해 주세요. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(392), true), true);
			return false;
		}
	}
}


