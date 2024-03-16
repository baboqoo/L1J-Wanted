package l1j.server.server.model.npc.action.id;

import l1j.server.server.serverpackets.message.S_SystemMessage;
import java.util.ArrayList;

import l1j.server.common.data.ChatType;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_ExpBoostingInfo;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.utils.StringUtil;

public class ComaAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new ComaAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private ComaAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		L1NpcInstance npc = (L1NpcInstance) obj;
		if (pc.isInvisble()) {
//AUTO SRM: 			pc.sendPackets(new S_NpcChatPacket(npc, "투명 상태에서 받을 수 없어요.", ChatType.CHAT_NORMAL), true); // CHECKED OK
			pc.sendPackets(new S_NpcChatPacket(npc, S_SystemMessage.getRefText(1130), ChatType.CHAT_NORMAL), true);
			return null;
		}
		comma(pc, s, obj.getId());
		return null;
	}
	
	private void comma(L1PcInstance pc, String s, int objid){
		if (s.equalsIgnoreCase("1")) {
			comaCheck(pc, 3, objid);
		} else if (s.equalsIgnoreCase("2")) {
			comaCheck(pc, 5, objid);
		} else if (s.equalsIgnoreCase("a")) {
			pc.setDeathMatchPiece(pc.getDeathMatchPiece() + 1);
			comaSelect(pc, objid);
		} else if (s.equalsIgnoreCase("b")) {
			pc.setDeathMatchPiece(pc.getDeathMatchPiece() + 2);
			comaSelect(pc, objid);
		} else if (s.equalsIgnoreCase("c")) {
			pc.setDeathMatchPiece(pc.getDeathMatchPiece() + 3);
			comaSelect(pc, objid);
		} else if (s.equalsIgnoreCase("d")) {
			pc.setDeathMatchPiece(pc.getDeathMatchPiece() + 4);
			comaSelect(pc, objid);
		} else if (s.equalsIgnoreCase("e")) {
			pc.setDeathMatchPiece(pc.getDeathMatchPiece() + 5);
			comaSelect(pc, objid);
		} else if (s.equalsIgnoreCase("f")) {
			pc.setGhostHousePiece(pc.getGhostHousePiece() + 1);
			comaSelect(pc, objid);
		} else if (s.equalsIgnoreCase("g")) {
			pc.setGhostHousePiece(pc.getGhostHousePiece() + 2);
			comaSelect(pc, objid);
		} else if (s.equalsIgnoreCase("h")) {
			pc.setGhostHousePiece(pc.getGhostHousePiece() + 3);
			comaSelect(pc, objid);
		} else if (s.equalsIgnoreCase("i")) {
			pc.setGhostHousePiece(pc.getGhostHousePiece() + 4);
			comaSelect(pc, objid);
		} else if (s.equalsIgnoreCase("j")) {
			pc.setGhostHousePiece(pc.getGhostHousePiece() + 5);
			comaSelect(pc, objid);
		} else if (s.equalsIgnoreCase("k")) {
			pc.setPetRacePiece(pc.getPetRacePiece() + 1);
			comaSelect(pc, objid);
		} else if (s.equalsIgnoreCase("l")) {
			pc.setPetRacePiece(pc.getPetRacePiece() + 2);
			comaSelect(pc, objid);
		} else if (s.equalsIgnoreCase("m")) {
			pc.setPetRacePiece(pc.getPetRacePiece() + 3);
			comaSelect(pc, objid);
		} else if (s.equalsIgnoreCase("n")) {
			pc.setPetRacePiece(pc.getPetRacePiece() + 4);
			comaSelect(pc, objid);
		} else if (s.equalsIgnoreCase("o")) {
			pc.setPetRacePiece(pc.getPetRacePiece() + 5);
			comaSelect(pc, objid);
		} else if (s.equalsIgnoreCase("p")) {
			pc.setPetMatchPiece(pc.getPetMatchPiece() + 1);
			comaSelect(pc, objid);
		} else if (s.equalsIgnoreCase("q")) {
			pc.setPetMatchPiece(pc.getPetMatchPiece() + 2);
			comaSelect(pc, objid);
		} else if (s.equalsIgnoreCase("s")) {
			pc.setPetMatchPiece(pc.getPetMatchPiece() + 3);
			comaSelect(pc, objid);
		} else if (s.equalsIgnoreCase("t")) {
			pc.setPetMatchPiece(pc.getPetMatchPiece() + 4);
			comaSelect(pc, objid);
		} else if (s.equalsIgnoreCase("u")) {
			pc.setPetMatchPiece(pc.getPetMatchPiece() + 5);
			comaSelect(pc, objid);
		} else if (s.equalsIgnoreCase("v")) {
			pc.setUltimateBattlePiece(pc.getUltimateBattlePiece() + 1);
			comaSelect(pc, objid);
		} else if (s.equalsIgnoreCase("w")) {
			pc.setUltimateBattlePiece(pc.getUltimateBattlePiece() + 2);
			comaSelect(pc, objid);
		} else if (s.equalsIgnoreCase("x")) {
			pc.setUltimateBattlePiece(pc.getUltimateBattlePiece() + 3);
			comaSelect(pc, objid);
		} else if (s.equalsIgnoreCase("y")) {
			pc.setUltimateBattlePiece(pc.getUltimateBattlePiece() + 4);
			comaSelect(pc, objid);
		} else if (s.equalsIgnoreCase("z")) {
			pc.setUltimateBattlePiece(pc.getUltimateBattlePiece() + 5);
			comaSelect(pc, objid);
		} else if (s.equalsIgnoreCase("3")) {
			commaPieceStatusReset(pc);
			comaSelect(pc, objid);
		} else if (s.equalsIgnoreCase("4")) {
			coma(pc, objid);
		}
	}
	
	private void comaCheck(L1PcInstance pc, int type, int objid) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		if (pc.getInventory().checkItem(3000022, 1)) {
			list.add(3000022);
		}
		if (pc.getInventory().checkItem(3000023, 1)) {
			list.add(3000023);
		}
		if (pc.getInventory().checkItem(3000024, 1)) {
			list.add(3000024);
		}
		if (pc.getInventory().checkItem(3000025, 1)) {
			list.add(3000025);
		}
		if (pc.getInventory().checkItem(3000026, 1)) {
			list.add(3000026);
		}
		if (list.size() >= type) {
			for (int i = 0; i < type; i++) {
				pc.getInventory().consumeItem(list.get(i), 1);
			}
			if (type == 3) {
				pc.getSkill().removeSkillEffect(L1SkillId.COMA_B);
				if (!pc.getSkill().hasSkillEffect(L1SkillId.COMA_A)) {
					pc.getAbility().addAddedCon(1);
					pc.getAbility().addAddedDex(5);
					pc.getAbility().addAddedStr(5);
					pc.getAbility().addShortHitup(3);
					pc.getAC().addAc(-3);
					pc.sendPackets(new S_OwnCharStatus(pc), true);
				}
				pc.send_effect(7382);
				pc.getSkill().setSkillEffect(L1SkillId.COMA_A, 3600 * 1000);
			} else if (type == 5) {
				pc.getSkill().removeSkillEffect(L1SkillId.COMA_A);
				if (!pc.getSkill().hasSkillEffect(L1SkillId.COMA_B)) {
//					pc.addSp(1);
					pc.getAbility().addSp(1);
					pc.getAbility().addAddedCon(3);
					pc.getAbility().addAddedDex(5);
					pc.getAbility().addAddedStr(5);
					pc.getAbility().addShortHitup(5);
					pc.getAC().addAc(-8);
					pc.sendPackets(new S_OwnCharStatus(pc), true);
				}
				pc.send_effect(7383);
				pc.getSkill().setSkillEffect(L1SkillId.COMA_B, 7200 * 1000);
				pc.sendPackets(new S_ExpBoostingInfo(pc), true);
			}
			pc.sendPackets(new S_NPCTalkReturn(objid, StringUtil.EmptyString), true);
		} else {
			if (pc.isGm())
				pc.sendPackets(new S_SystemMessage("Dialog " + "coma3"), true);											
			
			pc.sendPackets(new S_NPCTalkReturn(objid, "coma3"), true);
		}
		list.clear();
	}
	
	private void commaPieceStatusReset(L1PcInstance pc) {
		pc.setDeathMatchPiece(0);
		pc.setGhostHousePiece(0);
		pc.setPetRacePiece(0);
		pc.setPetMatchPiece(0);
		pc.setUltimateBattlePiece(0);
	}
	
	private void comaSelect(L1PcInstance pc, int objid) {
		String[] htmldata = new String[] { String.valueOf(pc.getDeathMatchPiece()), String.valueOf(pc.getGhostHousePiece()), String.valueOf(pc.getPetRacePiece()), String.valueOf(pc.getPetMatchPiece()), String.valueOf(pc.getUltimateBattlePiece()) };
		
		if (pc.isGm())
			pc.sendPackets(new S_SystemMessage("Dialog " + "coma5"), true);											

		pc.sendPackets(new S_NPCTalkReturn(objid, "coma5", htmldata), true);
	}
	
	private void coma(L1PcInstance pc, int objid) {
		int amount = pc.getUltimateBattlePiece() + pc.getDeathMatchPiece() + pc.getGhostHousePiece() + pc.getPetRacePiece() + pc.getPetMatchPiece();
		if (amount < 3 || amount == 4) {
			pc.sendPackets(new S_NPCTalkReturn(objid, "coma3_3"), true);
		} else if (amount == 3) {
			if (comaItemCheck(pc)) {
				commaItemConsume(pc);
				pc.getSkill().removeSkillEffect(L1SkillId.COMA_B);
				if (!pc.getSkill().hasSkillEffect(L1SkillId.COMA_A)) {
					pc.getAbility().addAddedCon(1);
					pc.getAbility().addAddedDex(5);
					pc.getAbility().addAddedStr(5);
					pc.getAbility().addShortHitup(3);
					pc.getAC().addAc(-3);
					pc.sendPackets(new S_OwnCharStatus(pc), true);
				}
				pc.send_effect(7382);
				pc.getSkill().setSkillEffect(L1SkillId.COMA_A, 3600000);
				pc.sendPackets(new S_NPCTalkReturn(objid, StringUtil.EmptyString), true);
			} else {
				pc.sendPackets(new S_NPCTalkReturn(objid, "coma3_2"), true);
			}
		} else if (amount == 5) {
			if (comaItemCheck(pc)) {
				commaItemConsume(pc);
				pc.getSkill().removeSkillEffect(L1SkillId.COMA_A);
				if (!pc.getSkill().hasSkillEffect(L1SkillId.COMA_B)) {
					pc.getAbility().addSp(1);
					pc.getAbility().addAddedCon(3);
					pc.getAbility().addAddedDex(5);
					pc.getAbility().addAddedStr(5);
					pc.getAbility().addShortHitup(5);
					pc.getAC().addAc(-8);
					pc.sendPackets(new S_OwnCharStatus(pc), true);
				}
				pc.send_effect(7383);
				pc.getSkill().setSkillEffect(L1SkillId.COMA_B, 7200000);
				pc.sendPackets(new S_ExpBoostingInfo(pc), true);
				pc.sendPackets(new S_NPCTalkReturn(objid, StringUtil.EmptyString), true);
			} else {
				pc.sendPackets(new S_NPCTalkReturn(objid, "coma3_2"), true);
			}
		} else if (amount > 5) {
			pc.sendPackets(new S_NPCTalkReturn(objid, "coma3_1"), true);
		}
		commaPieceStatusReset(pc);
	}
	
	private boolean comaItemCheck(L1PcInstance pc) {
		if (pc.getInventory().checkItem(3000022, pc.getDeathMatchPiece()) && pc.getInventory().checkItem(3000023, pc.getGhostHousePiece())
				&& pc.getInventory().checkItem(3000024, pc.getPetRacePiece()) && pc.getInventory().checkItem(3000025, pc.getPetMatchPiece())
				&& pc.getInventory().checkItem(3000026, pc.getUltimateBattlePiece())) {
			return true;
		}
		return false;
	}
	
	private void commaItemConsume(L1PcInstance pc) {
		pc.getInventory().consumeItem(3000022, pc.getDeathMatchPiece());
		pc.getInventory().consumeItem(3000023, pc.getGhostHousePiece());
		pc.getInventory().consumeItem(3000024, pc.getPetRacePiece());
		pc.getInventory().consumeItem(3000025, pc.getPetMatchPiece());
		pc.getInventory().consumeItem(3000026, pc.getUltimateBattlePiece());
	}
}


