package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.utils.StringUtil;

public class CanusAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new CanusAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private CanusAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return kanus(pc, s);
	}
	
	private String kanus(L1PcInstance pc, String s) {
		if (s.equals("a")) { // 기르타스 전방으로 이동
			pc.getTeleport().start(32855, 32862, (short) 537, pc.getMoveState().getHeading(), true);
		} else if (s.equals("b")) { // 전초기지로 이동
			pc.getTeleport().start(32804, 32864, (short) 537, pc.getMoveState().getHeading(), true);
		} else if (s.equals("d")) { // 전투 지역을 확인(5000아데나)
			if (pc.getInventory().consumeItem(L1ItemId.ADENA, 5000)) {
				ghostTeleport(pc, 32854, 32862, (short)537, 30);
				return StringUtil.EmptyString;
			}
			return "canus3";
		}
		return null;
	}
	
	private void ghostTeleport(L1PcInstance pc, int x, int y, short m, int second) {
		try{
			pc.save();
			pc.beginGhost(x, y, (short) m, false, second); //30초
		}catch(Exception e){}
	}
}

