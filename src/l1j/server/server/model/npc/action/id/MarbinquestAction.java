package l1j.server.server.model.npc.action.id;

import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.exp.L1ExpPlayer;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StringUtil;

public class MarbinquestAction implements L1NpcIdAction {// 마빈
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new MarbinquestAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private MarbinquestAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return marbin(s, pc);
	}
	
	private String marbin(String s, L1PcInstance pc){
		try{
			switch(s){
			case "a":
				if (pc.getInventory().checkItem(700013, 1) && pc.getInventory().checkItem(700016, 1) && pc.getInventory().checkItem(700015, 100)) {
					pc.getInventory().consumeItem(700013, 1);	// 불완전한 마법 구슬 조각
					pc.getInventory().consumeItem(700016, 1);	// 상아탑 마법의 정수
					pc.getInventory().consumeItem(700015, 100);	// 얼어붙은 여인의 눈물
					pc.getInventory().storeItem(700017, 5);		// 얼음 큐브
					marbinExpReward(pc, 1);// 2%
					pc.send_effect(3944);
					return "marbinquest6";
				}
				return "marbinquest5";
			case "b":
				if (pc.getInventory().checkItem(700012, 1)) {// 마빈의 주머니
					if (pc.getInventory().checkItem(700013, 1) && pc.getInventory().checkItem(700016, 1) && pc.getInventory().checkItem(700015, 100) && pc.getInventory().checkItem(L1ItemId.DRAGON_DIAMOND, 1)) {
						pc.getInventory().consumeItem(700013, 1);	// 불완전한 마법 구슬 조각
						pc.getInventory().consumeItem(700016, 1);	// 상아탑 마법의 정수
						pc.getInventory().consumeItem(700015, 100);	// 얼어붙은 여인의 눈물
						pc.getInventory().consumeItem(L1ItemId.DRAGON_DIAMOND, 1);	// 드래곤의 다이아몬드
						pc.getInventory().storeItem(700017, 5);		// 얼음 큐브
						marbinExpReward(pc, 2);// 5%
						pc.send_effect(3944);
						return "marbinquest6";
					}
					return "marbinquest5";
				}
				if (pc.getLevel() >= 70) {// 70이상
					pc.getInventory().storeItem(700012, 1);// 마빈의 주머니
					if (!pc.getInventory().checkItem(700011, 1)) {
						pc.getInventory().storeItem(700011, 1); // 마빈의 인증서
					}
					return "marbinquest2";
				}
				return "marbinquest8";
			case "c":
				if (pc.getInventory().checkItem(700013, 1) && pc.getInventory().checkItem(700016, 1) && pc.getInventory().checkItem(700015, 100) && pc.getInventory().checkItem(L1ItemId.DRAGON_HIGH_DIAMOND, 1)) {
					pc.getInventory().consumeItem(700013, 1);	// 불완전한 마법 구슬 조각
					pc.getInventory().consumeItem(700016, 1);	// 상아탑 마법의 정수
					pc.getInventory().consumeItem(700015, 100);	// 얼어붙은 여인의 눈물
					pc.getInventory().consumeItem(L1ItemId.DRAGON_HIGH_DIAMOND, 1);	// 드래곤의 고급 다이아몬드
					pc.getInventory().storeItem(700017, 5);		// 얼음 큐브
					marbinExpReward(pc, 3);// 25%
					pc.send_effect(3944);
					return "marbinquest6";
				}
				return "marbinquest5";
			case "d":
				if (pc.getInventory().checkItem(700013, 1) && pc.getInventory().checkItem(700016, 1) && pc.getInventory().checkItem(700015, 100) && pc.getInventory().checkItem(L1ItemId.DRAGON_FINEST_DIAMOND, 1)) {
					pc.getInventory().consumeItem(700013, 1);	// 불완전한 마법 구슬 조각
					pc.getInventory().consumeItem(700016, 1);	// 상아탑 마법의 정수
					pc.getInventory().consumeItem(700015, 100);	// 얼어붙은 여인의 눈물
					pc.getInventory().consumeItem(L1ItemId.DRAGON_FINEST_DIAMOND, 1);	// 드래곤의 최고급 다이아몬드
					pc.getInventory().storeItem(700017, 5);		// 얼음 큐브
					marbinExpReward(pc, 4);// 55%
					pc.send_effect(3944);
					return "marbinquest6";
				}
				return "marbinquest5";
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return StringUtil.EmptyString;
	}
	
	public void marbinExpReward(L1PcInstance pc, int type) {
		long exp = 0;
		if (type == 1) {
			exp = ExpTable.getExpFromLevelAndPercent(pc.getLevel(), 52, 2);
		} else if (type == 2) {
			exp = ExpTable.getExpFromLevelAndPercent(pc.getLevel(), 52, 5);
		} else if (type == 3) {
			exp = ExpTable.getExpFromLevelAndPercent(pc.getLevel(), 52, 25);
		} else if (type == 4) {
			exp = ExpTable.getExpFromLevelAndPercent(pc.getLevel(), 52, 55);
		} else {
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
	}
}


