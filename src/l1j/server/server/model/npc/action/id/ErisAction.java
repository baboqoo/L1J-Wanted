package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class ErisAction implements L1NpcIdAction {// 이리스
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new ErisAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private ErisAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		if (s.equalsIgnoreCase("A")) { 
			if (pc.getInventory().checkItem(41007)) {
				return "eris19";// 이미 명령서를 갖고 잇을 경우
			}
			pc.getInventory().storeItem(41007, 1);
			return "eris6";
		} else if (s.equalsIgnoreCase("B")) {// 한번만 교환한다
			if (pc.getInventory().checkItem(41007, 1)) {
			    if (pc.getInventory().checkItem(40969, 20)) {
				    pc.getInventory().consumeItem(40969, 20);
				    pc.getInventory().consumeItem(41007, 1);
				    pc.getInventory().storeItem(41008, 1);// 이리스의 가방
				    return "eris18";// 완료시
			    }
			    return "eris5";
			}
			return "eris2";
		} else if (s.equalsIgnoreCase("C")) {// 전부 교환
			if (pc.getInventory().checkItem(41007, 1)) {
				int soulcount = pc.getInventory().checkItemCount(40969);
				int soul = soulcount / 20;
				
				// we dont need to control anymore becouse now the bag is stackable and we are going to consume
				// 1 item, so for sure we will have place
				int max_soul = pc.getInventory().getAvailable() + 1;
				if (soul > max_soul)
				  soul = max_soul;
				  
				if (soul > 0) {
					pc.getInventory().consumeItem(40969, soul * 20);
					pc.getInventory().consumeItem(41007, 1);
					pc.getInventory().storeItem(41008, soul);
					return "eris18";// 완료시
				}
				return "eris5";
			}
			return "eris2";
		}
		return null;
	}
}

