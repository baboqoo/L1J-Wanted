package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;

public class AltarAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new AltarAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private AltarAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return gludinGhostQuest(pc, s);
	}
	
	private String gludinGhostQuest(L1PcInstance pc, String s){
		if (s.equals("A")) {
			if (pc.getInventory().checkItem(41327,20) && pc.getInventory().checkItem(41319,1)) {
				pc.getInventory().consumeItem(41327,20);
				pc.getInventory().consumeItem(41319,1);
				pc.getInventory().storeItem(41325, 1);
				return "altar3";
			}
			return "altar2";
		} else if (s.equals("B")) {
			if (pc.getInventory().checkItem(41327,20) && pc.getInventory().checkItem(41320,1)) {
				pc.getInventory().consumeItem(41327,20);
				pc.getInventory().consumeItem(41320,1);
				pc.getInventory().storeItem(41325, 1);
				return "altar3";
			}
			return "altar2";
		} else if (s.equals("C")) {
			if (pc.getInventory().checkItem(41327,20) && pc.getInventory().checkItem(41321,1)) {
				pc.getInventory().consumeItem(41327,20);
				pc.getInventory().consumeItem(41321,1);
				pc.getInventory().storeItem(41325, 1);
				return "altar3";
			}
			return "altar2";
		} else if (s.equals("D")) {
			if (pc.getInventory().checkItem(41327,20) && pc.getInventory().checkItem(41322,1)) {
				pc.getInventory().consumeItem(41327,20);
				pc.getInventory().consumeItem(41322,1);
				pc.getInventory().storeItem(41325, 1);
				return "altar3";
			}
			return "altar2";
		} else if (s.equals("E")) {
			if (pc.getInventory().checkItem(41327,20) && pc.getInventory().checkItem(41323,1)) {
				pc.getInventory().consumeItem(41327,20);
				pc.getInventory().consumeItem(41323,1);
				pc.getInventory().storeItem(41325, 1);
				return "altar3";
			}
			return "altar2";
		} else if (s.equals("F")) {
			if (pc.getInventory().checkItem(41327,20) && pc.getInventory().checkItem(41324,1)) {
				pc.getInventory().consumeItem(41327,20);
				pc.getInventory().consumeItem(41324,1);
				pc.getInventory().storeItem(41325, 1);
				return "altar3";
			}
			return "altar2";
		} else if (s.equals("G")) {
			if (pc.getInventory().checkItem(41328,1) && pc.getInventory().checkItem(41319,1)) {
				pc.getInventory().consumeItem(41328,1);
				pc.getInventory().consumeItem(41319,1);
				pc.getInventory().storeItem(41326, 1);
				return "altar3";
			}
			return "altar2";
		} else if (s.equals("H")) {
			if (pc.getInventory().checkItem(41328,1) && pc.getInventory().checkItem(41320,1)) {
				pc.getInventory().consumeItem(41328,1);
				pc.getInventory().consumeItem(41320,1);
				pc.getInventory().storeItem(41326, 1);
				return "altar3";
			}
			return "altar2";
		} else if (s.equals("I")) {
			if (pc.getInventory().checkItem(41328,1) && pc.getInventory().checkItem(41321,1)) {
				pc.getInventory().consumeItem(41328,1);
				pc.getInventory().consumeItem(41321,1);
				pc.getInventory().storeItem(41326, 1);
				return "altar3";
			}
			return "altar2";
		} else if (s.equals("J")) {
			if (pc.getInventory().checkItem(41328,1) && pc.getInventory().checkItem(41322,1)) {
				pc.getInventory().consumeItem(41328,1);
				pc.getInventory().consumeItem(41322,1);
				pc.getInventory().storeItem(41326, 1);
				return "altar3";
			}
			return "altar2";
		} else if (s.equals("K")) {
			if (pc.getInventory().checkItem(41328,1) && pc.getInventory().checkItem(41323,1)) {
				pc.getInventory().consumeItem(41328,1);
				pc.getInventory().consumeItem(41323,1);
				pc.getInventory().storeItem(41326, 1);
				return "altar3";
			}
			return "altar2";
		} else if (s.equals("L")) {
			if (pc.getInventory().checkItem(41328,1) && pc.getInventory().checkItem(41324,1)) {
				pc.getInventory().consumeItem(41328,1);
				pc.getInventory().consumeItem(41324,1);
				pc.getInventory().storeItem(41326, 1);
				return "altar3";
			}
			return "altar2";
		}
		return null;
	}
}

