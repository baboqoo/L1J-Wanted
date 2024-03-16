package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.utils.StringUtil;

public class GtwEnterAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new GtwEnterAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private GtwEnterAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		if (s.equalsIgnoreCase("a")) {
			switch(npcId){
			case 800800:// 황금 성단
				heine(pc, 32737, 32854);
				break;
			case 800803:// 검은 기사단
				heine(pc, 32769, 32823);
				break;
			case 800806:// 붉은 기사단
				heine(pc, 32705, 32823);
				break;
			default:
				windawood(pc);
				break;
			}							
		}
		return StringUtil.EmptyString;
	}
	
	private void heine(L1PcInstance pc, int x, int y){
		pc.getTeleport().start(x, y, (short) pc.getMapId(), pc.getMoveState().getHeading(), true);
		penaltyTimerDestroy(pc);
	}
	
	private void windawood(L1PcInstance pc){
		int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.WOLRDWAR_WINDAWOOD_BASE);
		pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true);
		penaltyTimerDestroy(pc);
	}
	
	private void penaltyTimerDestroy(L1PcInstance pc){
		if (pc._occupyPenaltyTimer == null) {
			return;
		}
		pc._occupyPenaltyTimer.cancel();
		pc._occupyPenaltyTimer = null;
		pc.sendPackets(S_SpellBuffNoti.PENALTY_OFF);
	}
}

