package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.utils.StringUtil;

public class MerinAction implements L1NpcIdAction {// 메린 (진돗개)
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new MerinAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private MerinAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return specialPetCreate(pc, s, 87051);
	}
	
	private String specialPetCreate(L1PcInstance pc, String s, int createItemId){
		if (s.equalsIgnoreCase("0")) {
			if (!pc.getInventory().consumeItem(L1ItemId.ADENA, 500000)) {
				return createItemId == 87050 ? "killton3" : "merin3"; // 아데나 부족시
			}
			L1ItemInstance item = pc.getInventory().storeItem(createItemId, 1);
			pc.sendPackets(new S_ServerMessage(403, item.getLogNameRef()), true);
			return StringUtil.EmptyString;
		}
		return null;
	}
}

