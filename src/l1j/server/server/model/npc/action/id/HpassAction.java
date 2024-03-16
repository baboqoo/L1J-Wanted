package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class HpassAction implements L1NpcIdAction {// 기초 훈련 교관 [ 퀘스트 ]
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new HpassAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private HpassAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		String htmlid = null;
		L1NpcInstance npc = (L1NpcInstance) obj;
		String desc = npc.getNpcTemplate().getDesc();
		if (s.equals("0")) { // 저를 단련시켜 주십시오.
			pc.getInventory().createItem(desc, L1ItemId.INSTRUCTOR_PRESENT_POKET_1, 1, 0);
			// 교관의 선물 주머니 [상아탑의 무기1/갑옷4/수련자의 벨트]
			pc.getQuest().setStep(L1Quest.QUEST_HPASS, 1);
			htmlid = "hpass9";
		} else if (s.equals("u")) { // 상아탑 상위 아이템 교환
			if (pc.getLevel() < 40) {
				htmlid = "hpass13";
			} else {
				int check = 0;
				// 투구, 망토 , 갑옷 , 장갑 , 샌달 , 방패
				int[] ivory = new int[] { 20028, 20283, 20126, 20173, 20206, 20232 };
				int[] sacred = new int[] { 30096, 30098, 30097, 30099, 30100, 30101 };
				int[] strong = new int[] { 30090, 30092, 30091, 30093, 30094, 30095 };
				for (L1ItemInstance item : pc.getInventory().getItems()) {
					switch (item.getItemId()) {
					case 20028: // 투구
					case 20283: // 망토
					case 20126: // 갑옷
					case 20173: // 장갑
					case 20206: // 샌달
					case 20232: // 방패
						if (item.getEnchantLevel() == 4) {
							for (int i = 0; i < ivory.length; i++) {
								if (pc.isCrown() || pc.isKnight() || pc.isDarkelf() || pc.isDragonknight() || pc.isFencer() || pc.isLancer()) {
									if (item.getItemId() == ivory[i]) {
										pc.getInventory().MakeDeleteEnchant(ivory[i], 4);
										pc.getInventory().createItem(desc, strong[i], 1, 0);
										// 견고한투구
									}
									check = 1;
								} else {
									if (item.getItemId() == ivory[i]) {
										pc.getInventory().MakeDeleteEnchant(ivory[i], 4);
										pc.getInventory().createItem(desc, sacred[i], 1, 0);
										// 신성한 투구
									}
									check = 1;
								}
							}
						}
						break;
					default:break;
					}
				}
				htmlid = check == 1 ? "hpass11" : "hpass12";
			}
		} else if (s.equals("1")) { // 수행할 준비가 됐습니다. [몬스터의 발톱]
			basisTraningQuest(pc, L1ItemId.MONSTER_TOENAIL, L1ItemId.INSTRUCTOR_PRESENT_POKET_2, obj.getId(), 1, "hpass20", desc);
		} else if (s.equals("2")) { // 수행할 준비가 됐습니다. [몬스터의 이빨]
			basisTraningQuest(pc, L1ItemId.MONSTER_TOOTH, L1ItemId.INSTRUCTOR_PRESENT_POKET_3, obj.getId(), 2, "hpass21", desc);
		} else if (s.equals("3")) { // 수행할 준비가 됐습니다. [녹슨 투구]
			basisTraningQuest(pc, L1ItemId.RUST_HELM, L1ItemId.INSTRUCTOR_PRESENT_POKET_4, obj.getId(), 3, "hpass22", desc);
		} else if (s.equals("4")) { // 수행할 준비가 됐습니다. [녹슨 장갑]
			basisTraningQuest(pc, L1ItemId.RUST_GLOVE, L1ItemId.INSTRUCTOR_PRESENT_POKET_5, obj.getId(), 4, "hpass23", desc);
		} else if (s.equals("5")) { // 수행할 준비가 됐습니다. [녹슨 장화]
			basisTraningQuest(pc, L1ItemId.RUST_BOOTS, L1ItemId.INSTRUCTOR_PRESENT_POKET_6, obj.getId(), 5, "hpass24", desc);
		}
		return htmlid;
	}
	
	// 기초 훈련 교관
	private void basisTraningQuest(L1PcInstance pc, int checkItemId, int createItemId, int objid, int step, String falsehtmlid, String desc) {
		if (pc.getInventory().consumeItem(checkItemId, 20)) {
			pc.getInventory().createItem(desc, createItemId, 1, 0);
			pc.getQuest().setStep(L1Quest.QUEST_HPASS, step >= 0 && step <= 4 ? pc.getQuest().getStep(L1Quest.QUEST_HPASS) + 1 : 255);
			if (pc.isGm())
				pc.sendPackets(new S_SystemMessage("Dialog " + "hpass10"), true);											
			pc.sendPackets(new S_NPCTalkReturn(objid, "hpass10"), true);
		} else {
			if (pc.isGm())
				pc.sendPackets(new S_SystemMessage("Dialog " + falsehtmlid), true);											

			pc.sendPackets(new S_NPCTalkReturn(objid, falsehtmlid), true);
		}
	}
}

