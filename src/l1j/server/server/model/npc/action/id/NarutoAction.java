package l1j.server.server.model.npc.action.id;

import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.exp.L1ExpPlayer;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class NarutoAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new NarutoAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private NarutoAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return naruter(pc, s);
	}
	
	private String naruter(L1PcInstance pc, String s){
		if(pc.getLevel() < 30)return "naruto2";
		if (s.equals("a")) {// 일반보상
			if (pc.getInventory().checkItem(9992, 5) && pc.getInventory().checkItem(9993, 1)){
				pc.getInventory().consumeItem(9992, 5);
				pc.getInventory().consumeItem(9993, 1);
				pc.getInventory().storeItem(9994, 1);
				expReward(pc, 1);
				return "naruto3";
			}
			return "naruto4";
		} else if (s.equals("b")) {// 특별한보상
			if (pc.getInventory().checkItem(9992, 5) && pc.getInventory().checkItem(9993, 1) && pc.getInventory().checkItem(1000004, 1)){
				pc.getInventory().consumeItem(9992, 5);
				pc.getInventory().consumeItem(9993, 1);
				pc.getInventory().consumeItem(1000004, 1);
				pc.getInventory().storeItem(9994, 1);
				expReward(pc, 2);
				return "naruto3";
			}
			return "naruto4";
		} else if (s.equals("c")) {// 빛나는 특별한보상
			if (pc.getInventory().checkItem(9992, 5) && pc.getInventory().checkItem(9993, 1) && pc.getInventory().checkItem(1000007, 1)){
				pc.getInventory().consumeItem(9992, 5);
				pc.getInventory().consumeItem(9993, 1);
				pc.getInventory().consumeItem(1000007, 1);
				pc.getInventory().storeItem(9994, 1);
				expReward(pc, 3);
				return "naruto3";
			}
			return "naruto4";
		}
		return null;
	}
	
	private void expReward(L1PcInstance pc, int type) {
		long exp = 0;
		switch(type){
		case 1:
			exp = ExpTable.getExpFromLevelAndPercent(pc.getLevel(), 52, 2);
			break;
		case 2:
			exp = ExpTable.getExpFromLevelAndPercent(pc.getLevel(), 52, 5);
			break;
		case 3:
			exp = ExpTable.getExpFromLevelAndPercent(pc.getLevel(), 52, 20);
			break;
		default:
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("잘못된 요구입니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1125), true), true);
			return;
		}
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


