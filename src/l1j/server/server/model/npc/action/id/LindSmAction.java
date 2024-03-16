package l1j.server.server.model.npc.action.id;

import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class LindSmAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new LindSmAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private LindSmAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		String htmlid = null;
		L1NpcInstance npc = (L1NpcInstance) obj;
		String desc = npc.getNpcTemplate().getDesc();
		if (s.equals("a")) { // 1.레벨 상승(최대 75레벨)
			if (pc.getLevel() < 51) {
				pc.setExp(ExpTable.getExpByLevel(51));
			} else if (pc.getLevel() >= 51 && pc.getLevel() < 75) {
				pc.setExp(ExpTable.getExpByLevel(pc.getLevel() + 1));
			} else if (pc.getLevel() >= 75) {
				htmlid = "lind_sm1";
			}
		} else if (s.equals("b")) { // 2.지원품 지급
			if (pc.getLevel() < 75) {
				htmlid = "lind_sm9";
			} else if (pc.getInventory().checkItem(30070, 1)) { // 지급 확인서가 있을경우
				htmlid = "lind_sm4";
			} else { // 지급 [ 장비 지급 확인서, 장비 지급함, 회상의 촛불 ]
				pc.getInventory().createItem(desc, 30071, 1, 0); // 아덴왕국 장비 지급함
				pc.getInventory().createItem(desc, 30070, 1, 0); // 아덴왕국 장비 지급 확인서
				pc.getInventory().createItem(desc, 200000, 1, 0); // 회상의 촛불
				htmlid = "lind_sm2";
			}
		} else if (s.equals("c")) { // 3.회상의 촛불 지원
			if (pc.getInventory().checkItem(200000, 1)) { // 회상의 촛불
				htmlid = "lind_sm5";
			} else {
				pc.getInventory().createItem(desc, 200000, 1, 0);
				htmlid = "lind_sm6";
			}
		} else if (s.equals("d")) { // 1.드래곤의 혈흔(안타라스) 받기
			pc.send_effect(7783);
			pc.getSkill().setSkillEffect(L1SkillId.ANTA_BUFF, 7200 * 1000);
			pc.sendPackets(new S_OwnCharAttrDef(pc), true);
			pc.sendPackets(S_PacketBox.ANTARAS_BUFF);
			htmlid = "lind_sm8";
			if (pc.isGm())
				pc.sendPackets(new S_SystemMessage("Dialog " + "lind_sm8"), true);											

			pc.sendPackets(new S_NPCTalkReturn(obj.getId(), "lind_sm8"), true);
		} else if (s.equals("e")) { // 2.드래곤의 혈흔(파푸리온) 받기.
			pc.send_effect(7783);
			pc.getSkill().setSkillEffect(L1SkillId.FAFU_BUFF, 7200 * 1000);
			pc.sendPackets(new S_OwnCharAttrDef(pc), true);
			pc.sendPackets(S_PacketBox.FAFURION_BUFF);
			htmlid = "lind_sm8";
		}
		return htmlid;
	}
}

