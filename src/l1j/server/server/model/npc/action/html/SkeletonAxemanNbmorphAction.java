package l1j.server.server.model.npc.action.html;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcHtmlAction;
import l1j.server.server.utils.StringUtil;

public class SkeletonAxemanNbmorphAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new SkeletonAxemanNbmorphAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private SkeletonAxemanNbmorphAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		if (pc.getLevel() < 13) {
			if (pc.getInventory().consumeItem(L1ItemId.ADENA, 100)) {
				L1PolyMorph.poly(pc, 2376);
			} else {
				pc.sendPackets(L1ServerMessage.sm189); // \f1아데나가 부족합니다.
			}
		}
		return StringUtil.EmptyString;
	}
}

