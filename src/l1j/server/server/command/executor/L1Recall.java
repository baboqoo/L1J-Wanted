package l1j.server.server.command.executor;

import java.util.ArrayList;
import java.util.Collection;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1Recall implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Recall();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1Recall() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (pc == null) {
				return false;
			}
			Collection<L1PcInstance> targets = null;
			//if (arg.equalsIgnoreCase("전체")) {
				if (arg.equalsIgnoreCase("all")) {
				targets = L1World.getInstance().getAllPlayers();
			} else {
				targets = new ArrayList<L1PcInstance>();
				L1PcInstance tg = L1World.getInstance().getPlayer(arg);
				if (tg == null) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("그러한 캐릭터는 없습니다. "), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(524), true), true);
					return false;
				}
				targets.add(tg);
			}

			for (L1PcInstance target : targets) {
				if (target.isGm()) {
					return false;
				}
				if (target.isPrivateShop()) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage(target.getName() + " 캐릭은 개인상점모드입니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(target.getName()  + S_SystemMessage.getRefText(561), true), true);
					return false;
				}
				if (target.isAutoClanjoin()) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage(target.getName() + " 님은 자동가입중 입니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(target.getName()  + S_SystemMessage.getRefText(562), true), true);
					return false;
				}
				L1Location loc = recallLoc(pc, 2);
				target.getTeleport().start(loc, target.getMoveState().getHeading(), true);
				// pc.sendPackets(new S_SystemMessage("알림: \\aG"+target.getName()+" 님을 소환했습니다. "), true); // CHECKED OK
//AUTO SRM: 				target.sendPackets(new S_SystemMessage("\\aG게임마스터님께서 \\aA당신\\aG을 소환하였습니다."), true); // CHECKED OK
				target.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(563), true), true);
			}
			return true;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [전체, 캐릭터명]으로 입력해 주세요. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(392), true), true);
			return false;
		}
	}
	
	private L1Location recallLoc(L1Character target, int distance) {
		L1Location loc = new L1Location();
		int locX	= target.getX();
		int locY	= target.getY();
		int heading	= target.getMoveState().getHeading();
		loc.setMap(target.getMapId());
		switch (heading) {
		case 1:locX += distance;	locY -= distance;break;
		case 2:locX += distance;break;
		case 3:locX += distance;	locY += distance;break;
		case 4:locY += distance;break;
		case 5:locX -= distance;	locY += distance;break;
		case 6:locX -= distance;break;
		case 7:locX -= distance;	locY -= distance;break;
		case 0:locY -= distance;break;
		}
		loc.setX(locX);
		loc.setY(locY);
		return loc;
	}
}


