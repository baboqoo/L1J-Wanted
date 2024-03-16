package l1j.server.server.model.npc.action.id;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.utils.StringUtil;

public class AbandonAction implements L1NpcIdAction {
	private static class newInstance {
		public static final AbandonAction INSTANCE = new AbandonAction();
	}
	public static AbandonAction getInstance(){
		return newInstance.INSTANCE;
	}
	private AbandonAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return abandon(pc, s, npcId);// 스콜
	}
	
	private String abandon(L1PcInstance pc, String s, int npcId) {
		String htmlid = StringUtil.EmptyString;
		int x = 0, y = 0, mapid = 0, adena = 0;
		if (s.equalsIgnoreCase("a")) {
			if (npcId == 146060) {
				if (pc.getLevel() < 90 || pc.getLevel() > 91 || !pc.getInventory().checkItemOne(L1ItemId.DEATH_PENALTY_SHIELD_ITEMS) || !pc.getInventory().checkItem(L1ItemId.ADENA, 10000)) {
					pc.sendPackets(L1ServerMessage.sm5359); // 입장에 필요한 조건이 맞지 않습니다.
					return StringUtil.EmptyString;
				}
				int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.ABANDON_INTER_WEST_BASE);
				x		= loc[0];
				y		= loc[1];
				mapid	= loc[2];
				adena	= 10000;
			} else if (npcId == 146068) {
				if (pc.getLevel() < 90 || pc.getLevel() > 93 || !pc.getInventory().checkItemOne(L1ItemId.DEATH_PENALTY_SHIELD_ITEMS) || !pc.getInventory().checkItem(L1ItemId.ADENA, 10000)) {
					pc.sendPackets(L1ServerMessage.sm5359); // 입장에 필요한 조건이 맞지 않습니다.
					return StringUtil.EmptyString;
				}
				int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.ABANDON_INTER_EAST_BASE);
				x		= loc[0];
				y		= loc[1];
				mapid	= loc[2];
				adena	= 10000;
			} else {
				if (pc.getLevel() < 75 || pc.getLevel() > 92) {
					pc.sendPackets(L1ServerMessage.sm5359); // 입장에 필요한 조건이 맞지 않습니다.
					return StringUtil.EmptyString;
				}
				x		= 32537;
				y		= 32954;
				mapid	= 777;
			}
			
			if (mapid != 0) {
				pc.getDungoenTimer().enter(x, y, mapid, mapid == 11900 || mapid == 12900, L1ItemId.ADENA, adena);
			} else {
				pc.getConfig().finishPlaySupport();
			}
		} else if (s.equalsIgnoreCase("b")) {
			htmlid = abandonPC(pc, s);
		}
		return htmlid;
	}
	
	public String abandonPC(L1PcInstance pc, String s) {
		int x = 32537, y = 32954, mapid = 0;
		if (s.equalsIgnoreCase("a_man") || s.equalsIgnoreCase("b")) {
			if (pc.getLevel() >= 75 && pc.isPCCafe()) {
				mapid = 778;
			} else {
				pc.sendPackets(L1ServerMessage.sm5359); // 입장에 필요한 조건이 맞지 않습니다.
			}
		}
		if (mapid != 0) {
			pc.getDungoenTimer().enter(x, y, mapid, false, 0, 0);
		} else {
			pc.getConfig().finishPlaySupport();
		}
		return StringUtil.EmptyString;
	}
}

