package l1j.server.server.model.npc.action.id;

import l1j.server.server.construct.skill.L1SkillInfo;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class HighdailyAction implements L1NpcIdAction {// 토벌대원 [ 일일 퀘스트 ]
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new HighdailyAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private HighdailyAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		String htmlid = null;
		L1NpcInstance npc = (L1NpcInstance) obj;
		String desc = npc.getNpcTemplate().getDesc();
		if (s.equals("A")) { // 당신을 돕겠습니다.
			pc.getQuest().setStep(L1Quest.QUEST_HIGHDAILY, 1);
			pc.getInventory().createItem(desc, L1ItemId.PUNITIVE_EXPEDITION_POKET, 7, 0); // 토벌대원의 주머니 7개 지급
			htmlid = "highdaily4";
		} else if (s.equals("B")) { // 몬스터를 토벌하러 가겠습니다.
			pc.getQuest().setStep(L1Quest.QUEST_HIGHDAILY, pc.getQuest().getStep(L1Quest.QUEST_HIGHDAILY) + 1);
			htmlid = "highdaily5";
		} else if (s.equals("C")) { // 약속한 대로 토벌을 완료했습니다.
			silverKnightQuest(pc, obj.getId(), 30041, 30044, L1Quest.QUEST_HIGHDAILY, "highdaily7", "highdaily31", "highdaily20", "highdaily9");
		} else if (s.equals("D")) { // 필요없으니 돌려드리겠습니다.
			int ment = 0;
			for (L1ItemInstance item : pc.getInventory().getItems()) {
				switch (item.getItemId()) {
				case L1ItemId.PUNITIVE_EXPEDITION_POKET: // 토벌 대원의주머니
				case L1ItemId.PUNITIVE_EXPEDITION_BEAD: // 빛나는 구슬
					if (item != null) {
						pc.getInventory().consumeItem(item.getItemId(), item.getCount());
						ment = 1;
					}
					break;
				default:break;
				}
			}
			htmlid = ment == 1 ? "highdaily8" : "highdaily9";
		}
		return htmlid;
	}
	
	// 일일 퀘스트
	private void silverKnightQuest(L1PcInstance pc, int objid, int checkItem_1, int checkItem_2, int Quest, String htmlid_0, String htmlid_1, String htmlid_2, String htmlid_3) {
		int[] count = new int[2];
		if (!pc.getInventory().checkItem(checkItem_1, 100)) {
			count[0] = 1;
		}
		if (!pc.getInventory().checkItem(checkItem_2, 1)) {
			count[1] = 2;
		}
		int total = count[0] + count[1];
		L1ItemInstance item = pc.getInventory().findItemId(checkItem_1);
		switch (total) {
		case 0:
			pc.setExp(pc.getExp() + 30000);
			pc.getQuest().setStep(Quest, pc.getQuest().getStep(Quest) + 1);
			pc.getInventory().consumeItem(checkItem_1, item.getCount());
			pc.getInventory().consumeItem(checkItem_2, 1);
			L1BuffUtil.skillArrayAction(pc, L1SkillInfo.HIGHT_DAILY_BUFF_ARRAY);
			
            if (pc.isGm())
				pc.sendPackets(new S_SystemMessage("Dialog " + htmlid_0), true);											
			
			pc.sendPackets(new S_NPCTalkReturn(objid, htmlid_0), true);
			break;
		case 1:
			if (pc.isGm())
				pc.sendPackets(new S_SystemMessage("Dialog " + htmlid_1), true);											
			pc.sendPackets(new S_NPCTalkReturn(objid, htmlid_1), true);break;
		case 2:
			if (pc.isGm())
				pc.sendPackets(new S_SystemMessage("Dialog " + htmlid_2), true);											
			pc.sendPackets(new S_NPCTalkReturn(objid, htmlid_2), true);break;
		case 3:
			if (pc.isGm())
				pc.sendPackets(new S_SystemMessage("Dialog " + htmlid_3), true);											
			pc.sendPackets(new S_NPCTalkReturn(objid, htmlid_3), true);break;
		default:break;
		}
	}
}

