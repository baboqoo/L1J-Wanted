package l1j.server.server.model.npc.action.id;

import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.exp.L1ExpPlayer;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class EkinsAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new EkinsAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private EkinsAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return ekins(pc, s, obj);
	}
	
	private String ekins(L1PcInstance pc, String s, L1Object obj){
		L1ItemInstance item = null;
		L1NpcInstance npc = (L1NpcInstance) obj;
		String desc = npc.getDesc();
		if (!pc.isGm() && pc.getLevel() < 82) {
			return "ekins3";
		}
		if (s.equalsIgnoreCase("a")) { //성장의 구슬 조각 2프로
			if (pc.getInventory().consumeItem(810001, 1)) {
				expReward(pc, 1);
				item = pc.getInventory().storeItem(810016, 5);
				pc.sendPackets(new S_ServerMessage(143, desc, item.getItem().getDesc() + " (5)"), true);
				return "ekins4";
			}
			return "ekins5";
		} else if (s.equalsIgnoreCase("b")) { //성장의 구술 6프로
			if (pc.getInventory().consumeItem(810002, 1)) {
				expReward(pc, 6);
				item = pc.getInventory().storeItem(810016, 8);
				pc.sendPackets(new S_ServerMessage(143, desc, item.getItem().getDesc() + " (8)"), true);
				return "ekins4";
			}
			return "ekins5";
		} else if (s.equalsIgnoreCase("c")) { // 유니콘의 징표 2.5프로
			if (pc.getInventory().consumeItem(31088, 1)) {
				expReward(pc, 4);
				item = pc.getInventory().storeItem(810016, 5);
				pc.sendPackets(new S_ServerMessage(143, desc, item.getItem().getDesc() + " (5)"), true);
				return "ekins4";
			}
			return "ekins5";
		} else if (s.equalsIgnoreCase("d")) { // 유니콘의 징표 + 드래곤의 다이아몬드 4프로
			if (pc.getInventory().checkItem(31088, 1) && pc.getInventory().checkItem(1000004, 1)) {
				pc.getInventory().consumeItem(31088, 1);
				pc.getInventory().consumeItem(1000004, 1);
				expReward(pc, 5);
				item = pc.getInventory().storeItem(810016, 8);
				pc.sendPackets(new S_ServerMessage(143, desc, item.getItem().getDesc() + " (8)"), true);
				return "ekins4";
			}
			return "ekins5";
		} else if (s.equalsIgnoreCase("e")) { // 유니콘의 징표 + 드래곤의 고급 다이아몬드 16프로
			if (pc.getInventory().checkItem(31088, 1) && pc.getInventory().checkItem(1000007, 1)) {
				pc.getInventory().consumeItem(31088, 1);
				pc.getInventory().consumeItem(1000007, 1);
				item = pc.getInventory().storeItem(810016, 32);
				expReward(pc, 7);
				pc.sendPackets(new S_ServerMessage(143, desc, item.getItem().getDesc() + " (32)"), true);
				return "ekins4";
			}
			return "ekins5";
		} else if (s.equalsIgnoreCase("f")) { // 유니콘의 징표 10회 2.5프로
			if (pc.getInventory().consumeItem(31088, 10)) {
				expReward(pc, 8);
				item = pc.getInventory().storeItem(810016, 50);
				pc.sendPackets(new S_ServerMessage(143, desc, item.getItem().getDesc() + " (50)"), true);
				return "ekins4";
			}
			return "ekins5";
		} else if (s.equalsIgnoreCase("g")) { // 유니콘의 징표 100회 2.5프로
			if (pc.getInventory().consumeItem(31088, 100)) {
				expReward(pc, 9);
				item = pc.getInventory().storeItem(810016, 500);
				pc.sendPackets(new S_ServerMessage(143, desc, item.getItem().getDesc() + " (500)"), true);
				return "ekins4";
			}
			return "ekins5";
		} else if (s.equalsIgnoreCase("j")) { // 유니콘의 징표 + 드래곤의 다이아몬드 10회 4프로
			if (pc.getInventory().checkItem(31088, 10) && pc.getInventory().checkItem(1000004, 10)) {
				pc.getInventory().consumeItem(31088, 10);
				pc.getInventory().consumeItem(1000004, 10);
				expReward(pc, 10);
				item = pc.getInventory().storeItem(810016, 80);
				pc.sendPackets(new S_ServerMessage(143, desc, item.getItem().getDesc() + " (80)"), true);
				return "ekins4";
			}
			return "ekins5";
		} else if (s.equalsIgnoreCase("k")) { // 유니콘의 징표 + 드래곤의 다이아몬드 100회 4프로
			if (pc.getInventory().checkItem(31088, 100) && pc.getInventory().checkItem(1000004, 100)) {
				pc.getInventory().consumeItem(31088, 100);
				pc.getInventory().consumeItem(1000004, 100);
				expReward(pc, 11);
				item = pc.getInventory().storeItem(810016, 800);
				pc.sendPackets(new S_ServerMessage(143, desc, item.getItem().getDesc() + " (800)"), true);
				return "ekins4";
			}
			return "ekins5";
		} else if (s.equalsIgnoreCase("h")) { // 유니콘의 징표 + 드래곤의 고급 다이아몬드 10회 16프로
			if (pc.getInventory().checkItem(31088, 10) && pc.getInventory().checkItem(1000007, 10)) {
				pc.getInventory().consumeItem(31088, 10);
				pc.getInventory().consumeItem(1000007, 10);
				item = pc.getInventory().storeItem(810016, 320);
				expReward(pc, 12);
				pc.sendPackets(new S_ServerMessage(143, desc, item.getItem().getDesc() + " (320)"), true);
				return "ekins4";
			}
			return "ekins5";
		} else if (s.equalsIgnoreCase("i")) { // 유니콘의 징표 + 드래곤의 고급 다이아몬드 100회 16프로
			if (pc.getInventory().checkItem(31088, 100) && pc.getInventory().checkItem(1000007, 100)) {
				pc.getInventory().consumeItem(31088, 100);
				pc.getInventory().consumeItem(1000007, 100);
				item = pc.getInventory().storeItem(810016, 3200);
				expReward(pc, 13);
				pc.sendPackets(new S_ServerMessage(143, desc, item.getItem().getDesc() + " (3200)"), true);
				return "ekins4";
			}
			return "ekins5";
		} else if (s.equalsIgnoreCase("l")) { //성장의 구술 10회 6프로
			if (pc.getInventory().consumeItem(810002, 10)) {
				expReward(pc, 14);
				item = pc.getInventory().storeItem(810016, 80);
				pc.sendPackets(new S_ServerMessage(143, desc, item.getItem().getDesc() + " (80)"), true);
				return "ekins4";
			}
			return "ekins5";
		} else if (s.equalsIgnoreCase("m")) { //성장의 구술 100회 6프로
			if (pc.getInventory().consumeItem(810002, 100)) {
				expReward(pc, 15);
				item = pc.getInventory().storeItem(810016, 800);
				pc.sendPackets(new S_ServerMessage(143, desc, item.getItem().getDesc() + " (800)"), true);
				return "ekins4";
			}
			return "ekins5";
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


