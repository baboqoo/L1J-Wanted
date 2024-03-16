package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class NewbiegateAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new NewbiegateAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private NewbiegateAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		riky(pc, s);
		return null;
	}
	
	private void riky(L1PcInstance pc, String s){
		if (s.equalsIgnoreCase("a")) {// 숨겨진계곡
			if (pc.getLevel() >= 1 & pc.getLevel() <= 45) {
				pc.getTeleport().start(32684, 32851, (short) 2005, pc.getMoveState().getHeading(), true);
			} else {
//AUTO SRM: 				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"\\fQ리키: \\f3[Lv.45]\\fQ이하만 입장 허용 레벨입니다."), true); // CHECKED OK
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,S_SystemMessage.getRefText(1143)), true);
			}
		} else if (s.equalsIgnoreCase("b")) {// 기란마을
			pc.getTeleport().start(33436, 32799, (short) 4, pc.getMoveState().getHeading(), true);
		
		} else if (s.equalsIgnoreCase("c")) {// 라우풀신전
			if (pc.getLevel() >= 10 & pc.getLevel() <= 29) {
				pc.getTeleport().start(33184, 33449, (short) 4, pc.getMoveState().getHeading(), true);
			} else {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("은기사 필드 이동 가능레벨 10 ~ 29"), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1140), true), true);
			}
		} else if (s.equalsIgnoreCase("d")) {// 카오틱신전
			if (pc.getLevel() >= 10 & pc.getLevel() <= 29) {
				pc.getTeleport().start(33066, 33218, (short) 4, pc.getMoveState().getHeading(), true);
			} else {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("은기사 필드 이동 가능레벨 10 ~ 29"), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1140), true), true);
			}
		} else if (s.equalsIgnoreCase("f")) {// 수련던전
			if (pc.getLevel() >= 10 & pc.getLevel() < 20) {
				pc.getTeleport().start(32801, 32806, (short) 25, pc.getMoveState().getHeading(), true);
			} else if (pc.getLevel() >= 20 & pc.getLevel() < 30) {
				pc.getTeleport().start(32806, 32746, (short) 26, pc.getMoveState().getHeading(), true);
		    } else if (pc.getLevel() >= 30 & pc.getLevel() < 40) {
		    	pc.getTeleport().start(32808, 32766, (short) 27, pc.getMoveState().getHeading(), true);
			} else if (pc.getLevel() >= 40 & pc.getLevel() < 44) {
				pc.getTeleport().start(32796, 32799, (short) 28, pc.getMoveState().getHeading(), true);
			} else {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("수련 던전 이동 가능레벨 10 ~ 44"), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1141), true), true);
			}
		} else if (s.equalsIgnoreCase("e")) {// 폭풍던전 불신 Lv 45~51
			if (pc.getLevel() >= 45 & pc.getLevel() <= 51) {
				pc.getTeleport().start(32807, 32789, (short) 2010, pc.getMoveState().getHeading(), true);
			}  else {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("폭풍 수련 던전 이동 가능레벨 45 ~ 51"), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1142), true), true);
			}
		}
	}
}


