package l1j.server.server.model.npc.action.id;

import l1j.server.IndunSystem.battlecoloseum.BattleColoseum;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.utils.StringUtil;

public class HiColosAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new HiColosAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private HiColosAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return battleColoseumEnter(pc, s);
	}
	
	private String battleColoseumEnter(L1PcInstance pc, String s) {
		try {
			if (s.equalsIgnoreCase("a")) {
				if (pc.getLevel() < 85 
						|| !pc.getInventory().checkItem(L1ItemId.ADENA, 5000)
						|| !BattleColoseum.getInstance().getEnter()
						|| pc.getColoTeam() != 0 
						|| BattleColoseum.getInstance().isPclist(pc)
						|| BattleColoseum.getInstance().getPclistCount() >= 64) {
					return "sungun1";
				}
				pc.getInventory().consumeItem(L1ItemId.ADENA, 5000);
				int line = 1;
				int divid = BattleColoseum.getInstance().getPclistCount() % 4;
				if (divid == 1) {
					line = 2;
				} else if (divid == 2) {
					line = 3;
				} else if (divid == 3) {
					line = 4;
				}
				pc.setColoTeam(line);
				BattleColoseum.getInstance().addPc(pc);
				int ranx, rany;
				if (pc.getColoTeam() == 1) {// 1시
					ranx = 32777;
					rany = 33378;
				} else if (pc.getColoTeam() == 2) {// 4시
					ranx = 32678;
					rany = 33480;
				} else if (pc.getColoTeam() == 3) {// 7시
					ranx = 32580;
					rany = 33380;
				} else {// 10시
					ranx = 32680;
					rany = 33273;
				}
				L1Location loc = new L1Location(ranx, rany, 750).randomLocation(5, true);
				pc.getTeleport().start(loc, 5, true);
				loc = null;
			}
		} catch (Exception e) {
		}
		return StringUtil.EmptyString;
	}
}

