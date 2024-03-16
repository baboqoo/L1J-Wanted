package l1j.server.server.model.npc.action.id;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.controller.action.War;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.utils.StringUtil;

public class WorldWarEnterAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new WorldWarEnterAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private WorldWarEnterAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return worldWarEnter(pc, s);
	}
	
	private String worldWarEnter(L1PcInstance pc, String s) {
		try {
			if (s.matches("32|33|34|i giran-castle|i orc-castle|i heine-castle")) {
				if (s.matches("32|i giran-castle")) {
					if (!War.getInstance()._isWolrdWarGiran && !pc.isGm()) {
						pc.sendPackets(L1SystemMessage.NOT_ENTER_TIME);
						return StringUtil.EmptyString;
					}
				} else if (s.matches("33|i orc-castle")) {
					if (!War.getInstance()._isWolrdWarOrc && !pc.isGm()) {
						pc.sendPackets(L1SystemMessage.NOT_ENTER_TIME);
						return StringUtil.EmptyString;
					}
				} else if (s.matches("34|i heine-castle")) {
					if (!War.getInstance()._isWolrdWarHeine && !pc.isGm()) {
						pc.sendPackets(L1SystemMessage.NOT_ENTER_TIME);
						return StringUtil.EmptyString;
					}
				}
				if (pc.getLevel() < 60) {
					return StringUtil.EmptyString;
				}
				if (s.matches("i giran-castle|i orc-castle|i heine-castle")) {
					if (pc.getInventory().checkItem(L1ItemId.ADENA, 10000)) {
						pc.getInventory().consumeItem(L1ItemId.ADENA, 10000);
					} else {
						pc.sendPackets(L1ServerMessage.sm189); // \f1%0이 부족합니다.
						return StringUtil.EmptyString;
					}
				}
				
				if (s.equalsIgnoreCase("32")) {// 기란성으로 간다
					int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.WOLRDWAR_GIRAN);
					pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true, true);
				} else if (s.equalsIgnoreCase("33")) { // 오크요새로 간다
					int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.WOLRDWAR_ORC);
					pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true, true);
				} else if (s.equalsIgnoreCase("34")) { // 하이네성으로 간다
					int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.WOLRDWAR_HEINE);
					pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true, true);
				} else if (s.equalsIgnoreCase("i giran-castle")) { // 기란성으로 간다
					int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.WOLRDWAR_GIRAN);
					pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true);
				} else if (s.equalsIgnoreCase("i orc-castle")) { // 오크요새로 간다
					int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.WOLRDWAR_ORC);
					pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true);
				} else if (s.equalsIgnoreCase("i heine-castle")) { // 하이네성으로 간다
					int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.WOLRDWAR_HEINE);
					pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true);
				}
			} else if (s.equalsIgnoreCase("a")) {// 기란성 균열앞으로 간다
				pc.getSkill().setSkillEffect(L1SkillId.ABSOLUTE_BARRIER, 3000);
				L1Location loc = new L1Location(33614, 32755, 4).randomLocation(5, true);
				pc.getTeleport().start(loc, pc.getMoveState().getHeading(), true);
				loc = null;
			} else if (s.equalsIgnoreCase("b")) {// 오크요새 균열앞으로 간다
				pc.getSkill().setSkillEffect(L1SkillId.ABSOLUTE_BARRIER, 3000);
				L1Location loc = new L1Location(32783, 32349, 4).randomLocation(5, true);
				pc.getTeleport().start(loc, pc.getMoveState().getHeading(), true);
				loc = null;
			}
		} catch (Exception e) {
		}
		return StringUtil.EmptyString;
	}
}

