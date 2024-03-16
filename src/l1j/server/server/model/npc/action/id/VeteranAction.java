package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcIdAction;

public class VeteranAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new VeteranAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private VeteranAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return beterlang(pc, s, obj);
	}
	
	private String beterlang(L1PcInstance pc, String s, L1Object obj){
		L1NpcInstance npc = (L1NpcInstance) obj;
		String desc = npc.getNpcTemplate().getDesc();
		int result = 0;
		if (s.equals("a")) { // 1. 베테르랑 단검
			if (pc.getInventory().checkItem(30065, 1)) {
				if (pc.isCrown() || pc.isKnight() || pc.isWizard() || pc.isElf() || pc.isDarkelf() || pc.isFencer() || pc.isLancer()) {
					pc.getInventory().createItem(desc, 1126, 1, 0);
					pc.getInventory().consumeItem(30065, 1);
					return "veteranE2";
				}
				return "veteranE6";
			}
			return "veteranE1";
		} else if (s.equals("b")) { // 2. 베테르랑 한손검
			if (pc.getInventory().checkItem(30065, 1)) {
				if (pc.isCrown() || pc.isKnight() || pc.isElf() || pc.isDragonknight() || pc.isFencer() || pc.isLancer()) {
					pc.getInventory().createItem(desc, 1127, 1, 0);
					pc.getInventory().consumeItem(30065, 1);
					return "veteranE2";
				}
				return "veteranE6";
			}
			return "veteranE1";
		} else if (s.equals("c")) { // 3. 베테르랑 양손검
			if (pc.getInventory().checkItem(30065, 1)) {
				if (pc.isCrown() || pc.isKnight() || pc.isDragonknight() || pc.isFencer() || pc.isLancer()) {
					pc.getInventory().createItem(desc, 1128, 1, 0);
					pc.getInventory().consumeItem(30065, 1);
					return "veteranE2";
				}
				return "veteranE6";
			}
			return "veteranE1";
		} else if (s.equals("d")) { // 4. 베테르랑 보우건
			if (pc.getInventory().checkItem(30065, 1)) {
				if (pc.isElf() || pc.isDarkelf() || pc.isIllusionist()) {
					pc.getInventory().createItem(desc, 1129, 1, 0);
					pc.getInventory().consumeItem(30065, 1);
					return "veteranE2";
				}
				return "veteranE6";
			}
			return "veteranE1";
		} else if (s.equals("e")) { // 5. 베테르랑 지팡이
			if (pc.getInventory().checkItem(30065, 1)) {
				if (pc.isWizard() || pc.isIllusionist()) {
					pc.getInventory().createItem(desc, 1130, 1, 0);
					pc.getInventory().consumeItem(30065, 1);
					return "veteranE2";
				}
				return "veteranE6";
			}
			return "veteranE1";
		} else if (s.equals("f")) { // 6. 베테르랑 크로우
			if (pc.getInventory().checkItem(30065, 1)) {
				if (pc.isDarkelf()) {
					pc.getInventory().createItem(desc, 1131, 1, 0);
					pc.getInventory().consumeItem(30065, 1);
					return "veteranE2";
				}
				return "veteranE6";
			}
			return "veteranE1";
		} else if (s.equals("g")) { // 7. 베테르랑 체인소드
			if (pc.getInventory().checkItem(30065, 1)) {
				if (pc.isDragonknight()) {
					pc.getInventory().createItem(desc, 1132, 1, 0);
					pc.getInventory().consumeItem(30065, 1);
					return "veteranE2";
				}
				return "veteranE6";
			}
			return "veteranE1";
		} else if (s.equals("h")) { // 8. 베테르랑 키링크
			if (pc.getInventory().checkItem(30065, 1)) {
				if (pc.isIllusionist()) {
					pc.getInventory().createItem(desc, 1133, 1, 0);
					pc.getInventory().consumeItem(30065, 1);
					return "veteranE2";
				}
				return "veteranE6";
			}
			return "veteranE1";
		} else if (s.equals("i")) { // 1. 베테르랑 판금 갑옷
			if (pc.getInventory().checkItem(30065, 1)) {
				if (pc.isKnight() || pc.isFencer() || pc.isLancer()) {
					pc.getInventory().createItem(desc, 22328, 1, 0);
					pc.getInventory().consumeItem(30065, 1);
					return "veteranE2";
				}
				return "veteranE6";
			}
			return "veteranE1";
		} else if (s.equals("j")) { // 2. 베테르랑 가죽 갑옷
			if (pc.getInventory().checkItem(30065, 1)) {
				if (pc.isCrown() || pc.isElf() || pc.isDarkelf() || pc.isDragonknight() || pc.isIllusionist()) {
					pc.getInventory().createItem(desc, 22329, 1, 0);
					pc.getInventory().consumeItem(30065, 1);
					return "veteranE2";
				}
				return "veteranE6";
			}
			return "veteranE1";
		} else if (s.equals("k")) { // 3. 베테르랑 로브
			if (pc.getInventory().checkItem(30065, 1)) {
				if (pc.isWizard() || pc.isIllusionist()) {
					pc.getInventory().createItem(desc, 22330, 1, 0);
					pc.getInventory().consumeItem(30065, 1);
					return "veteranE2";
				}
				return "veteranE6";
			}
			return "veteranE1";
		} else if (s.equals("l")) { // 4. 베테르랑 방패
			if (pc.getInventory().checkItem(30065, 1)) {
				if (pc.isKnight() || pc.isDragonknight() || pc.isFencer() || pc.isLancer()) {
					pc.getInventory().createItem(desc, 22331, 1, 0);
					pc.getInventory().consumeItem(30065, 1);
					return "veteranE2";
				}
				return "veteranE6";
			}
			return "veteranE1";
		} else if (s.equals("m")) { // 5. 베테르랑 티셔츠
			if (pc.getInventory().checkItem(30065, 1)) {
				pc.getInventory().createItem(desc, 22332, 1, 0);
				pc.getInventory().consumeItem(30065, 1);
				return "veteranE2";
			}
			return "veteranE1";
		} else if (s.equals("n")) { // 6. 베테르랑 장화
			if (pc.getInventory().checkItem(30065, 1)) {
				pc.getInventory().createItem(desc, 22333, 1, 0);
				pc.getInventory().consumeItem(30065, 1);
				return "veteranE2";
			}
			return "veteranE1";
		} else if (s.equals("o")) { // 7. 베테르랑 해골 투구
			if (pc.getInventory().checkItem(30065, 1)) {
				if (pc.isCrown() || pc.isKnight() || pc.isDragonknight() || pc.isFencer() || pc.isLancer()) {
					pc.getInventory().createItem(desc, 22334, 1, 0);
					pc.getInventory().consumeItem(30065, 1);
					return "veteranE2";
				}
				return "veteranE6";
			}
			return "veteranE1";
		} else if (s.equals("p")) { // 8. 베테르랑 마법 망토
			if (pc.getInventory().checkItem(30065, 1)) {
				if (pc.isElf() || pc.isDarkelf() || pc.isIllusionist()) {
					pc.getInventory().createItem(desc, 22335, 1, 0);
					pc.getInventory().consumeItem(30065, 1);
					return "veteranE2";
				}
				return "veteranE6";
			}
			return "veteranE1";
		} else if (s.equals("1")) { // 1. 체력 물약(포도주스) 무기</font>를 아이템으로
			for (int i = 0; i < L1ItemId.BETERLANG_WEAPON_ITEMS.length; i++) {
				if (pc.getInventory().checkItem(L1ItemId.BETERLANG_WEAPON_ITEMS[i], 1)) {
					pc.getInventory().consumeItem(L1ItemId.BETERLANG_WEAPON_ITEMS[i], 1);
					pc.getInventory().createItem(desc, 60029, 50, 0);
					result = 1;
					break;
				}
			}
			return result == 1 ? "veteranE3" : "veteranE4";
		} else if (s.equals("2")) { // 2. 용사의 무기 마법 주문서
			for (int i = 0; i < L1ItemId.BETERLANG_WEAPON_ITEMS.length; i++) {
				if (pc.getInventory().checkItem(L1ItemId.BETERLANG_WEAPON_ITEMS[i], 1)) {
					pc.getInventory().consumeItem(L1ItemId.BETERLANG_WEAPON_ITEMS[i], 1);
					pc.getInventory().createItem(desc, 30068, 1, 0);
					result = 1;
					break;
				}
			}
			return result == 1 ? "veteranE3" : "veteranE4";
		} else if (s.equals("3")) { // 3. 용사의 갑옷 마법 주문서
			for (int i = 0; i < L1ItemId.BETERLANG_WEAPON_ITEMS.length; i++) {
				if (pc.getInventory().checkItem(L1ItemId.BETERLANG_WEAPON_ITEMS[i], 1)) {
					pc.getInventory().consumeItem(L1ItemId.BETERLANG_WEAPON_ITEMS[i], 1);
					pc.getInventory().createItem(desc, 30069, 1, 0);
					result = 1;
					break;
				}
			}
			return result == 1 ? "veteranE3" : "veteranE4";
		} else if (s.equals("4")) { // 1. 체력 물약(포도주스) 방어구</font>를 아이템으로
			for (int i = 0; i < L1ItemId.BETERLANG_ARMOR_ITEMS.length; i++) {
				if (pc.getInventory().checkItem(L1ItemId.BETERLANG_ARMOR_ITEMS[i], 1)) {
					pc.getInventory().consumeItem(L1ItemId.BETERLANG_ARMOR_ITEMS[i], 1);
					pc.getInventory().createItem(desc, 60029, 50, 0);
					result = 1;
					break;
				}
			}
			return result == 1 ? "veteranE3" : "veteranE4";
		} else if (s.equals("5")) { // 2. 용사의 무기 마법 주문서
			for (int i = 0; i < L1ItemId.BETERLANG_ARMOR_ITEMS.length; i++) {
				if (pc.getInventory().checkItem(L1ItemId.BETERLANG_ARMOR_ITEMS[i], 1)) {
					pc.getInventory().consumeItem(L1ItemId.BETERLANG_ARMOR_ITEMS[i], 1);
					pc.getInventory().createItem(desc, 30068, 1, 0);
					result = 1;
					break;
				}
			}
			return result == 1 ? "veteranE3" : "veteranE4";
		} else if (s.equals("6")) { // 3. 용사의 갑옷 마법 주문서
			for (int i = 0; i < L1ItemId.BETERLANG_ARMOR_ITEMS.length; i++) {
				if (pc.getInventory().checkItem(L1ItemId.BETERLANG_ARMOR_ITEMS[i], 1)) {
					pc.getInventory().consumeItem(L1ItemId.BETERLANG_ARMOR_ITEMS[i], 1);
					pc.getInventory().createItem(desc, 30069, 1, 0);
					result = 1;
					break;
				}
			}
			return result == 1 ? "veteranE3" : "veteranE4";
		}
		return null;
	}
}

