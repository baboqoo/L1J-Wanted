package l1j.server.server.model.npc.action.id;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.utils.StringUtil;

public class OmanTeleportAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new OmanTeleportAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private OmanTeleportAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		try {
			int x = 0, y = 0;
			int mapId = 100 + Integer.parseInt(s);
			if (s.equalsIgnoreCase("1")) {
				if (pc.getInventory().getOmanAmulet().isOmanEnterable(mapId) || pc.getInventory().consumeItem(830001, 1)) {
					x = 32735;y = 32798;	
				} else {
					pc.sendPackets(L1ServerMessage.sm337_OMAN_1); // \f1%0이 부족합니다.
				}
			} else if (s.equalsIgnoreCase("2")) {
				if (pc.getInventory().getOmanAmulet().isOmanEnterable(mapId) || pc.getInventory().consumeItem(830002, 1)) {
					x = 32730;y = 32802;
				} else {
					pc.sendPackets(L1ServerMessage.sm337_OMAN_2); // \f1%0이 부족합니다.
				}
			} else if (s.equalsIgnoreCase("3")) {
				if (pc.getInventory().getOmanAmulet().isOmanEnterable(mapId) || pc.getInventory().consumeItem(830003, 1)) {
					x = 32726;y = 32803;
				} else {
					pc.sendPackets(L1ServerMessage.sm337_OMAN_3); // \f1%0이 부족합니다.
				}
			} else if (s.equalsIgnoreCase("4")) {
				if (pc.getInventory().getOmanAmulet().isOmanEnterable(mapId) || pc.getInventory().consumeItem(830004, 1)) {
					x = 32621;y = 32858;
				} else {
					pc.sendPackets(L1ServerMessage.sm337_OMAN_4); // \f1%0이 부족합니다.
				}
			} else if (s.equalsIgnoreCase("5")) {
				if (pc.getInventory().getOmanAmulet().isOmanEnterable(mapId) || pc.getInventory().consumeItem(830005, 1)) {
					x = 32599;y = 32866;
				} else
					pc.sendPackets(L1ServerMessage.sm337_OMAN_5); // \f1%0이 부족합니다.
			} else if (s.equalsIgnoreCase("6")) {
				if (pc.getInventory().getOmanAmulet().isOmanEnterable(mapId) || pc.getInventory().consumeItem(830006, 1)) {
					x = 32611;y = 32862;
				} else {
					pc.sendPackets(L1ServerMessage.sm337_OMAN_6); // \f1%0이 부족합니다.
				}
			} else if (s.equalsIgnoreCase("7")) {
				if (pc.getInventory().getOmanAmulet().isOmanEnterable(mapId) || pc.getInventory().consumeItem(830007, 1)) {
					x = 32618;y = 32866;
				} else {
					pc.sendPackets(L1ServerMessage.sm337_OMAN_7); // \f1%0이 부족합니다.
				}
			} else if (s.equalsIgnoreCase("8")) {
				if (pc.getInventory().getOmanAmulet().isOmanEnterable(mapId) || pc.getInventory().consumeItem(830008, 1)) {
					x = 32600;y = 32866;
				} else {
					pc.sendPackets(L1ServerMessage.sm337_OMAN_8); // \f1%0이 부족합니다.
				}
			} else if (s.equalsIgnoreCase("9")) {
				if (pc.getInventory().getOmanAmulet().isOmanEnterable(mapId) || pc.getInventory().consumeItem(830009, 1)) {
					x = 32612;y = 32866;
				} else {
					pc.sendPackets(L1ServerMessage.sm337_OMAN_9); // \f1%0이 부족합니다.
				}
			} else if (s.equalsIgnoreCase("10")) {
				if (pc.getInventory().getOmanAmulet().isOmanEnterable(mapId) || pc.getInventory().consumeItem(830010, 1)) {
					x = 32729;y = 32802;
				} else {
					pc.sendPackets(L1ServerMessage.sm337_OMAN_10); // \f1%0이 부족합니다.
				}
			}
			
			if (x != 0 && y != 0) {
				L1Location loc = new L1Location(x, y, mapId).randomLocation(3, true);
				pc.getTeleport().start(loc, 5, true);
				loc = null;
				pc.getSkill().setSkillEffect(L1SkillId.ABSOLUTE_BARRIER, 3000);
			} else {
				pc.getConfig().finishPlaySupport();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return StringUtil.EmptyString;
	}
}

