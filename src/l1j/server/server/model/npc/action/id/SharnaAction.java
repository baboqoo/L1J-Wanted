package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.message.S_ServerMessage;

public class SharnaAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new SharnaAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private SharnaAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return sharnaPolyScrollBuy(pc, s, obj);
	}
	
	private String sharnaPolyScrollBuy(L1PcInstance pc, String s, L1Object obj){
		L1NpcInstance npc = (L1NpcInstance) obj;
		int count = 1, itemid = 0;
		
		int level = pc.getLevel();
		if (level >= 30 && level < 40) {
			itemid = 210097;
		} else if (level >= 40 && level < 52) {
			itemid = 210098;
		} else if (level >= 52 && level < 55) {
			itemid = 210099;
		} else if (level >= 55 && level < 60) {
			itemid = 210100;
		} else if (level >= 60 && level < 65) {
			itemid = 210101;
		} else if (level >= 65 && level < 70) {
			itemid = 210102;
		} else if (level >= 70 && level < 75) {
			itemid = 210103;
		} else if (level >= 75 && level < 80) {
			itemid = 210116;
		} else if (level >= 80) {
			itemid = 210117;
		}
		switch(s){
		case "0":count = 1;break;
		case "1":count = 2;break;
		case "2":count = 3;break;
		case "3":count = 4;break;
		case "4":count = 5;break;
		case "5":count = 6;break;
		case "6":count = 7;break;
		case "7":count = 8;break;
		case "8":count = 9;break;
		case "9":count = 10;break;
		case ":":count = 11;break;
		case ";":count = 12;break;
		case "<":count = 13;break;
		case "=":count = 14;break;
		case ">":count = 15;break;
		case "?":count = 16;break;
		case "@":count = 17;break;
		case "A":count = 18;break;
		case "B":count = 19;break;
		case "C":count = 20;break;
		}
		int adena = count * 2500;
		if (pc.getInventory().consumeItem(L1ItemId.ADENA, adena)) {
			L1ItemInstance item = pc.getInventory().storeItem(itemid, count);
			if (item != null) {
				pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().getDesc(), item.getItem().getDesc()), true);
			}
			return "sharna3";
		}
		return "sharna5";
	}
}

