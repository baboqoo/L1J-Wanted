package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.utils.CommonUtil;

public class VoyagerAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new VoyagerAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private VoyagerAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		String htmlid = null;
		if (s.equalsIgnoreCase("A")) {
			final int[] diaryno = { 49082, 49083 };
			int pid = CommonUtil.random(diaryno.length);
			int di = diaryno[pid];
			if (di == 49082) { // 홀수 페이지 뽑아라
				htmlid = "voyager6a";
				L1NpcInstance npc = (L1NpcInstance) obj;
				L1ItemInstance item = pc.getInventory().storeItem(di, 1);
				pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().getDesc(), item.getItem().getDesc()), true);
			} else if (di == 49083) { // 짝수 페이지 뽑아라
				htmlid = "voyager6b";
				L1NpcInstance npc = (L1NpcInstance) obj;
				L1ItemInstance item = pc.getInventory().storeItem(di, 1);
				pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().getDesc(), item.getItem().getDesc()), true);
			}
		}
		return htmlid;
	}
}

