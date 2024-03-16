package l1j.server.server.model.npc.action.id;

import l1j.server.server.GameServerSetting;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.utils.StringUtil;

public class DominationTowerEnterAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new DominationTowerEnterAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private DominationTowerEnterAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return jibaeTowerEnter(pc, s, npcId);
	}
	
	private String jibaeTowerEnter(L1PcInstance pc, String s, int npcid){
		if (!GameServerSetting.DOMINATION_TOWER) {
			return "inter_o_enf";
		}
		if (s.equalsIgnoreCase("a")) {
			if (!GameServerSetting.EVENT_DOMINATION_OPEN) {
				return "inter_o_enf";
			}
			int paperId = 0;
			switch(npcid){
			case 800101:paperId = 830001;break;
			case 800102:paperId = 830002;break;
			case 800103:paperId = 830003;break;
			case 800104:paperId = 830004;break;
			case 800105:paperId = 830005;break;
			case 800106:paperId = 830006;break;
			case 800107:paperId = 830007;break;
			case 800108:paperId = 830008;break;
			case 800109:paperId = 830009;break;
			case 800110:paperId = 830010;break;
			}
			if (paperId > 0 && !pc.getInventory().consumeItem(paperId, 1)) {
				return "inter_o_enf";
			}
		} else {
			int amuletId = 0, paperId = 0, subAmuletId = 0;
			switch(npcid){
			case 800101:amuletId = 830022;subAmuletId = 840022;break;
			case 800102:amuletId = 830023;subAmuletId = 840023;break;
			case 800103:amuletId = 830024;subAmuletId = 840024;break;
			case 800104:amuletId = 830025;subAmuletId = 840025;break;
			case 800105:amuletId = 830026;subAmuletId = 840026;break;
			case 800106:amuletId = 830027;subAmuletId = 840027;break;
			case 800107:amuletId = 830028;subAmuletId = 840028;break;
			case 800108:amuletId = 830029;subAmuletId = 840029;break;
			case 800109:amuletId = 830030;subAmuletId = 840030;break;
			case 800110:amuletId = 830031;subAmuletId = 840031;break;
			case 800111:paperId = 830011;break;
			}
			if (amuletId > 0 && !(pc.getInventory().checkItem(560028, 1) || pc.getInventory().checkItem(amuletId, 1) || pc.getInventory().checkItem(subAmuletId, 1))) {
				return "inter_o_enf";
			}
			if (paperId > 0 && !(pc.getInventory().checkItem(560028, 1) || pc.getInventory().consumeItem(paperId, 1))) {
				return "inter_o_enf";
			}
		}
		int x = 0, y = 0, mapId = 0;
		switch(npcid){
		case 800101:x=32725;y=32800;mapId=12852;break;
		case 800102:x=32718;y=32803;mapId=12853;break;
		case 800103:x=32723;y=32803;mapId=12854;break;
		case 800104:x=32593;y=32864;mapId=12855;break;
		case 800105:x=32588;y=32867;mapId=12856;break;
		case 800106:x=32597;y=32866;mapId=12857;break;
		case 800107:x=32597;y=32868;mapId=12858;break;
		case 800108:x=32587;y=32867;mapId=12859;break;
		case 800109:x=32597;y=32867;mapId=12860;break;
		case 800110:x=32726;y=32801;mapId=12861;break;
		case 800111:x=s.equalsIgnoreCase("11") ? 32638 : 32798;y=s.equalsIgnoreCase("11") ? 32793 : 32964;mapId=12862;break;
		}
		pc.getSkill().setSkillEffect(L1SkillId.ABSOLUTE_BARRIER, 3000);
		L1Location loc = new L1Location(x, y, mapId).randomLocation(5, true);
		pc.getTeleport().start(loc.getX(), loc.getY(), (short) loc.getMapId(), pc.getMoveState().getHeading(), true, true);
		loc = null;
	    return StringUtil.EmptyString;
	}
}

