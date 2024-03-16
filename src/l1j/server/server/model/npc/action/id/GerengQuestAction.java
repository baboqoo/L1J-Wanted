package l1j.server.server.model.npc.action.id;

import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.exp.L1ExpPlayer;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.message.S_ServerMessage;

public class GerengQuestAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new GerengQuestAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private GerengQuestAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return gerangDayQuest(pc, s);
	}
	
	private String gerangDayQuest(L1PcInstance pc, String s){
		if (s.equalsIgnoreCase("a")) { // 영혼 조각
			L1PcInventory inv = pc.getInventory();
			if (inv.checkItem(410506, 1) && inv.checkItem(410507, 100)) {
				inv.consumeItem(410506, 1);
				inv.consumeItem(410507);
				L1ItemInstance coin = inv.storeItem(L1ItemId.KNIGHT_COIN, 500000);
				pc.sendPackets(new S_ServerMessage(403, String.format("%s (500,000)", coin.getItem().getDesc())), true);
				expReward(pc);
			} else {
				return "gereng03";
			}
		}
		return null;
	}
	
	private void expReward(L1PcInstance pc) {
		long exp = ExpTable.getExpFromLevelAndPercent(pc.getLevel(), 52, 20);
		if (pc.getExp() + exp >= L1ExpPlayer.LIMIT_EXP) {
			pc.sendPackets(L1SystemMessage.LIMIT_LEVEL_EXP_FAIL);
			return;
		}
		/** 폭렙 방지 **/
	    if (pc.getLevel() >= 1 && (exp + pc.getExp() > ExpTable.getExpByLevel(pc.getLevel() + 1))) {
	    	exp = ExpTable.getExpByLevel(pc.getLevel() + 1) - pc.getExp();
		}
		pc.addExp(exp);
		pc.send_effect(3944);
	}
}

