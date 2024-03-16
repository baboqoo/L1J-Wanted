package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.utils.StringUtil;

public class DimensionDoorAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new DimensionDoorAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private DimensionDoorAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return dimensionDoor(pc, (L1NpcInstance) obj, s);
	}
	
	private String dimensionDoor(L1PcInstance pc, L1NpcInstance npc, String s) {
		String htmlid = StringUtil.EmptyString;
		int protectionId = 0, sealId = 0, locX = 0, locY = 0;
		short mapId = 0;
		if (npc.getNpcTemplate().getNpcId() == 80059) {
			protectionId = 40909;
			sealId = 40913;
			locX = 32773;
			locY = 32835;
			mapId = 607;
		} else if (npc.getNpcTemplate().getNpcId() == 80060) {
			protectionId = 40912;
			sealId = 40916;
			locX = 32757;
			locY = 32842;
			mapId = 606;
		} else if (npc.getNpcTemplate().getNpcId() == 80061) {
			protectionId = 40910;
			sealId = 40914;
			locX = 32830;
			locY = 32822;
			mapId = 604;
		} else if (npc.getNpcTemplate().getNpcId() == 80062) {
			protectionId = 40911;
			sealId = 40915;
			locX = 32835;
			locY = 32822;
			mapId = 605;
		}

		if (s.equalsIgnoreCase("a")) {
			pc.getTeleport().start(locX, locY, mapId, 5, true);
			htmlid = StringUtil.EmptyString;
		} else if (s.equalsIgnoreCase("b")) {
			L1ItemInstance item = pc.getInventory().storeItem(protectionId, 1);
			if (item != null)
				pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().getDesc(), item.getLogNameRef()), true);
			htmlid = StringUtil.EmptyString;
		} else if (s.equalsIgnoreCase("c")) {
			htmlid = "wpass07";
		} else if (s.equalsIgnoreCase("d")) {
			if (pc.getInventory().checkItem(sealId)) {
				L1ItemInstance item = pc.getInventory().findItemId(sealId);
				pc.getInventory().consumeItem(sealId, item.getCount());
			}
		} else if (s.equalsIgnoreCase("e")) {
			htmlid = StringUtil.EmptyString;
		} else if (s.equalsIgnoreCase("f")) {
			if (pc.getInventory().checkItem(protectionId))
				pc.getInventory().consumeItem(protectionId, 1);
			if (pc.getInventory().checkItem(sealId)) {
				L1ItemInstance item = pc.getInventory().findItemId(sealId);
				pc.getInventory().consumeItem(sealId, item.getCount());
			}
			htmlid = StringUtil.EmptyString;
		}
		return htmlid;
	}
}

