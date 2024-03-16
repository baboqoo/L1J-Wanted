package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.utils.StringUtil;

public class LringAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new LringAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private LringAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return karmaEaring(pc, (L1NpcInstance) obj, s);
	}
	
	private static final int[] earringIdList = { 21020, 21021, 21022, 21023, 21024, 21025, 21026, 21027 };
	
	private String karmaEaring(L1PcInstance pc, L1NpcInstance npc, String s) {
		int earringId = 0;
		L1ItemInstance item = null;
		if (s.equalsIgnoreCase("1")) {
			if (pc.getKarmaLevel() >= 1) {
				earringId = earringIdList[0];
			}
		} else if (s.equalsIgnoreCase("2")) {
			if (pc.getKarmaLevel() >= 2) {
				earringId = earringIdList[1];
			}
		} else if (s.equalsIgnoreCase("3")) {
			if (pc.getKarmaLevel() >= 3) {
				earringId = earringIdList[2];
			}
		} else if (s.equalsIgnoreCase("4")) {
			if (pc.getKarmaLevel() >= 4) {
				earringId = earringIdList[3];
			}
		} else if (s.equalsIgnoreCase("5")) {
			if (pc.getKarmaLevel() >= 5) {
				earringId = earringIdList[4];
			}
		} else if (s.equalsIgnoreCase("6")) {
			if (pc.getKarmaLevel() >= 6) {
				earringId = earringIdList[5];
			}
		} else if (s.equalsIgnoreCase("7")) {
			if (pc.getKarmaLevel() >= 7) {
				earringId = earringIdList[6];
			}
		} else if (s.equalsIgnoreCase("8")) {
			if (pc.getKarmaLevel() >= 8) {
				earringId = earringIdList[7];
			}
		}
		if (earringId != 0) {
			item = pc.getInventory().storeItem(earringId, 1);
			if(item != null)pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().getDesc(), item.getLogNameRef()), true);
			for (int id : earringIdList) {
				if (id == earringId) {
					break;
				}
				if (pc.getInventory().checkItem(id)) {
					pc.getInventory().consumeItem(id, 1);
				}
			}
			return StringUtil.EmptyString;
		}
		return null;
	}
}

