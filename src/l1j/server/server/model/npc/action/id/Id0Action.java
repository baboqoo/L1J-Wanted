package l1j.server.server.model.npc.action.id;

import l1j.server.IndunSystem.hadin.HadinCreator;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StringUtil;

public class Id0Action implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new Id0Action();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private Id0Action(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		if (s.equalsIgnoreCase("enter")) {
			if (pc.isInParty()) {
				if (pc.getParty().isLeader(pc)) {
					if (pc.getParty().getNumOfMembers() >= 5) {// 파티원 7
						boolean ck = true;
						for (L1PcInstance member : pc.getParty().getMembersArray()) {
							if (pc.getMapId() != member.getMapId()) {
//AUTO SRM: 								pc.sendPackets(new S_SystemMessage("파티원이 다 모이지 않았습니다."), true); // CHECKED OK
								pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1134), true), true);
								ck = false;
								break;
							}
						}
						if (ck) {
							HadinCreator.getInstance().startHadin(pc);
//AUTO SRM: 							L1World.getInstance().broadcastServerMessage("\\aD" + pc.getName() + " 님이 동료들과 함께 과거로 여행을 떠났습니다.", true); // CHECKED OK
							L1World.getInstance().broadcastServerMessage("\\aD" + pc.getName()  + S_SystemMessage.getRefText(1135), true);
						}
						return StringUtil.EmptyString;
					}
					return "id0_1";
				}
				return "id0_2";
			}
			return "id0_2";
		}
		return null;
	}
}


