package l1j.server.server.model.npc.action.id;

import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.exp.L1ExpPlayer;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class ArchgunterAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new ArchgunterAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private ArchgunterAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		String htmlid = null;
		if (s.equalsIgnoreCase("a") && !pc.getInventory().checkItem(447011, 1)) {
		    if (pc.getLevel() <= 4) {//해당레벨이하일경우						
//AUTO SRM: 			    pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"\\fH군터(스승): 자네는..아직 레벨[5]도 못만들었나?!"), true); // CHECKED OK
			    pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,S_SystemMessage.getRefText(1128)), true);
//AUTO SRM: 			    pc.sendPackets(new S_SystemMessage("\\aA군터: 레벨[\\aG5\\aA]를 만들고 오게나.."), true); // CHECKED OK
			    pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1126), true), true);
			    htmlid = "archgunter2";
		    } else { //해당레벨이상일경우
                pc.getInventory().storeItem(447011, 1);// 아크프리패스상자
				htmlid = "archgunter1";
				expReward(pc, 3);
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("\\aA군터: 이제 [\\aG훈련교관 테온\\aA]을 만나거라.."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1127), true), true);
				pc.getTeleport().start(32646, 32865, (short) 7783, 5, true);
		    }
		}
		return htmlid;
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
		case 4:
			exp = ExpTable.getExpFromLevelAndPercent(pc.getLevel(), 52, 2.5);
			break;
		case 5:
			exp = ExpTable.getExpFromLevelAndPercent(pc.getLevel(), 52, 4);
			break;
		case 6:
			exp = ExpTable.getExpFromLevelAndPercent(pc.getLevel(), 52, 6);
			break;
		case 7:
			exp = ExpTable.getExpFromLevelAndPercent(pc.getLevel(), 52, 16);
			break;
		case 8:
			exp = ExpTable.getExpFromLevelAndPercent(pc.getLevel(), 52, 25);
			break;
		case 9:
			exp = ExpTable.getExpFromLevelAndPercent(pc.getLevel(), 52, 250);
			break;
		case 10:
			exp = ExpTable.getExpFromLevelAndPercent(pc.getLevel(), 52, 40);
			break;
		case 11:
			exp = ExpTable.getExpFromLevelAndPercent(pc.getLevel(), 52, 400);
			break;
		case 12:
			exp = ExpTable.getExpFromLevelAndPercent(pc.getLevel(), 52, 160);
			break;
		case 13:
			exp = ExpTable.getExpFromLevelAndPercent(pc.getLevel(), 52, 1600);
			break;
		case 14:
			exp = ExpTable.getExpFromLevelAndPercent(pc.getLevel(), 52, 60);
			break;
		case 15:
			exp = ExpTable.getExpFromLevelAndPercent(pc.getLevel(), 52, 600);
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


