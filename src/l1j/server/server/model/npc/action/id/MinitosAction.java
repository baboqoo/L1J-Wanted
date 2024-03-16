package l1j.server.server.model.npc.action.id;

import l1j.server.Config;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.S_Karma;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.utils.StringUtil;

public class MinitosAction implements L1NpcIdAction {// 첩보원(그림자의 신전측)
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new MinitosAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private MinitosAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		String htmlid = null;
		if (s.equalsIgnoreCase("n")) {// 「화가 나지만 승낙한다」
			htmlid = StringUtil.EmptyString;
			L1PolyMorph.poly(pc, 6035);
			final int[] item_ids = { 41123, 41124, 41125 };
			final int[] item_amounts = { 1, 1, 1 };
			L1ItemInstance item = null;
			for (int i = 0; i < item_ids.length; i++) {
				item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
				pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().getDesc(), item.getItem().getDesc()), true);
				pc.getQuest().setStep(L1Quest.QUEST_SHADOWS, 1);
			}
		} else if (s.equalsIgnoreCase("d")) {// 「그런 임무는 그만둔다」
			htmlid = "minitos09";
			pc.getInventory().consumeItem(41121, 1);
			pc.getInventory().consumeItem(41122, 1);
		} else if (s.equalsIgnoreCase("k")) {// 「초기화한다」
			htmlid = StringUtil.EmptyString;
			pc.getInventory().consumeItem(41123, 1); // 카헬의 타락 한 가루
			pc.getInventory().consumeItem(41124, 1); // 카헬의 무력 한 가루
			pc.getInventory().consumeItem(41125, 1); // 카헬의 아집 한 가루
			pc.getInventory().consumeItem(41126, 1); // 핏자국의 타락 한 정수
			pc.getInventory().consumeItem(41127, 1); // 핏자국의 무력 한 정수
			pc.getInventory().consumeItem(41128, 1); // 핏자국의 아집 한 정수
			pc.getInventory().consumeItem(41129, 1); // 핏자국의 정수
			pc.getQuest().setStep(L1Quest.QUEST_SHADOWS, 0);
		} else if (s.equalsIgnoreCase("e")) {// 정수를 건네준다
			if (pc.getQuest().getStep(L1Quest.QUEST_SHADOWS) == L1Quest.QUEST_END || pc.getKarmaLevel() >= 1) {
				htmlid = StringUtil.EmptyString;
			} else {
				if (pc.getInventory().checkItem(41129)) {
					htmlid = StringUtil.EmptyString;
					pc.addKarma((int) (-1600 * Config.RATE.RATE_KARMA));
					pc.sendPackets(new S_Karma(pc), true);
					pc.getInventory().consumeItem(41121, 1); // 카헬의 계약서
					pc.getInventory().consumeItem(41122, 1); // 카헬의 지령서
					pc.getInventory().consumeItem(41129, 1); // 핏자국의 정수
					pc.getQuest().setStep(L1Quest.QUEST_SHADOWS, L1Quest.QUEST_END);
				} else {
					htmlid = "minitos04";
				}
			}
		} else if (s.equalsIgnoreCase("g")) {// 재빠르게 받는다
			htmlid = StringUtil.EmptyString;
			final int[] item_ids = { 41121 }; // 카헬의 계약서
			final int[] item_amounts = { 1 };
			L1ItemInstance item = null;
			for (int i = 0; i < item_ids.length; i++) {
				item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
				pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().getDesc(), item.getItem().getDesc()), true);
			}
		}
		return htmlid;
	}
}

