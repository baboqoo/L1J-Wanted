package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class SeiruneAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new SeiruneAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private SeiruneAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		nerva(pc, s);
		return null;
	}
	
	private void nerva(L1PcInstance pc, String s){
		if (s.equals("A") || s.equals("B") || s.equals("C") || s.equals("D") || s.equals("E")) {
			if (pc.getLevel() >= 55) {
				if (pc.getInventory().checkItem(60031, 1) && pc.getInventory().checkItem(60032, 1)) {
					pc.getInventory().consumeItem(60031, 1);
					pc.getInventory().consumeItem(60032, 1);
					if(s.equals("A"))		pc.getInventory().storeItem(60036, 1); // 힘의 엘릭서 룬주머니
					else if(s.equals("B"))	pc.getInventory().storeItem(60037, 1); // 민첩의 엘릭서 룬
					else if(s.equals("C"))	pc.getInventory().storeItem(60038, 1); // 체력의 엘릭서 룬
					else if(s.equals("D"))	pc.getInventory().storeItem(60039, 1); // 지식의 엘릭서 룬
					else if(s.equals("E"))	pc.getInventory().storeItem(60040, 1); // 지혜의 엘릭서 룬
					if (pc.isGm())
						pc.sendPackets(new S_SystemMessage("Dialog " + "seirune6"), true);																
					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "seirune6"), true);
				} else {
					if (pc.isGm())
						pc.sendPackets(new S_SystemMessage("Dialog " + "seirune5"), true);																

					pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "seirune5"), true);
				}
			} else {
				if (pc.isGm())
					pc.sendPackets(new S_SystemMessage("Dialog " + "seirune5"), true);																

				pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "seirune5"), true);
			}
		}
	}
}

