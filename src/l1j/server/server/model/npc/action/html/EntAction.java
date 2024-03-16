package l1j.server.server.model.npc.action.html;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.datatables.UBTable;
import l1j.server.server.model.L1DeathMatch;
import l1j.server.server.model.L1HauntedHouse;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PetMatch;
import l1j.server.server.model.L1Racing;
import l1j.server.server.model.L1UltimateBattle;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcHtmlAction;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.StringUtil;

public class EntAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new EntAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private EntAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		String htmlid = null;
		if (npcId == 80085) {
			htmlid = ghostHouse(pc);
		} else if (npcId == 80086 || npcId == 80087) {
			htmlid = deathMatch(pc);
		} else if (npcId == 80088) {
			htmlid = petMatch(pc, Integer.valueOf(s2));
		} else if (npcId == 300000) { // 펫 레이싱
			htmlid = petRacing(pc, npcId);
		} else if (npcId == 200014) {
			if (pc.getLevel() != pc.getHighLevel()) {
				pc.sendPackets(L1SystemMessage.LEVEL_DOWN_CHAR_FAIL);
				return null;
			}
			if (pc.getLevel() > 54) {
				if (pc.getInventory().consumeItem(200000, 1)) {
					pc.getTeleport().start(32723 + CommonUtil.random(10), 32851 + CommonUtil.random(10), (short) 5166, 5, true);
					pc.returnStat();
					htmlid = StringUtil.EmptyString;
				} else {
					pc.sendPackets(L1ServerMessage.sm1290);
				}
			} else
				pc.sendPackets(L1SystemMessage.RETURN_STAT_LEVEL_FAIL);

		} else if (npcId == 50038 || npcId == 50042 || npcId == 50029 || npcId == 50019 || npcId == 50062) {
			htmlid = ultimateWatch(pc, npcId);
		} else {
			htmlid = ultimateEnter(pc, npcId);
		}
		return htmlid;
	}
	
	private String ultimateWatch(L1PcInstance pc, int npcId) {
		/*L1UltimateBattle ub = UBTable.getInstance().getUbForNpcId(npcId);
		L1Location loc = ub.getLocation();
		if (pc.getInventory().consumeItem(L1ItemId.ADENA, 100)) {
			try {
				pc.save();
				pc.beginGhost(loc.getX(), loc.getY(), (short) loc.getMapId(), true);
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		} else {
			pc.sendPackets(L1ServerMessage.sm189);
		}*/
//AUTO SRM: 		pc.sendPackets(new S_SystemMessage("무한대전 관람모드 비활성화 상태."), true); // CHECKED OK
		pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1120), true), true);
		return StringUtil.EmptyString;
	}
	
	private String ultimateEnter(L1PcInstance pc, int npcId) {
		L1UltimateBattle ub = UBTable.getInstance().getUbForNpcId(npcId);
		if (ub == null || pc == null) {
			return StringUtil.EmptyString;
		}
		if (!ub.isActive() || !ub.canPcEnter(pc)) {
			return "colos2";
		}
		if (ub.isNowUb()) {
			return "colos1";
		}
		if (ub.getMembersCount() >= ub.getMaxPlayer()) {
			return "colos4";
		}
		if (!pc.getInventory().checkItem(L1ItemId.ADENA, 10000)) {
			pc.sendPackets(L1ServerMessage.sm189);
			return StringUtil.EmptyString;
		}

		pc.getInventory().removeItem(L1ItemId.ADENA, 10000);
		ub.addMember(pc);
		L1Location loc = ub.getLocation().randomLocation(10, false);
		pc.getTeleport().start(loc.getX(), loc.getY(), ub.getMapId(), 5, true);
		return StringUtil.EmptyString;
	}
	
	private String ghostHouse(L1PcInstance pc) {
		if (L1HauntedHouse.getInstance().getHauntedHouseStatus() == L1HauntedHouse.STATUS_PLAYING) {
			pc.sendPackets(L1ServerMessage.sm1182);
			return StringUtil.EmptyString;
		}
		if (L1HauntedHouse.getInstance().getHauntedHouseStatus() == L1HauntedHouse.STATUS_CLEANUP) {
			pc.sendPackets(L1ServerMessage.sm1183);
			return StringUtil.EmptyString;
		}
		if (L1HauntedHouse.getInstance().getMembersCount() >= 10) {
			pc.sendPackets(L1ServerMessage.sm1184);
			return StringUtil.EmptyString;
		}
		if (pc.getLevel() < 30) {
			pc.sendPackets(new S_ServerMessage(1273, "30", "99"), true);
			return StringUtil.EmptyString;
		}

		if (!L1HauntedHouse.getInstance().isMember(pc)) {
			if (!pc.getInventory().consumeItem(L1ItemId.ADENA, 1000)) {
				pc.sendPackets(L1ServerMessage.sm189);
				return StringUtil.EmptyString;
			}
			L1HauntedHouse.getInstance().addMember(pc);
		}

		if(L1DeathMatch.getInstance().isMember(pc))L1DeathMatch.getInstance().removeMember(pc);
		if(L1Racing.getInstance().contains(0, pc))L1Racing.getInstance().remove(0, pc);
		return StringUtil.EmptyString;
	}
	
	private String deathMatch(L1PcInstance pc) {
		if (L1DeathMatch.getInstance().getDeathMatchStatus() == L1DeathMatch.STATUS_PLAYING) {
			pc.sendPackets(L1ServerMessage.sm1182);
			return StringUtil.EmptyString;
		}
		if (L1DeathMatch.getInstance().getMembersCount() >= 20) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("이미 데스매치가 포화 상태라네."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1121), true), true);
			return StringUtil.EmptyString;
		}
		if (pc.getLevel() < 52) {
			pc.sendPackets(new S_ServerMessage(1273, "52", "99"), true);
			return StringUtil.EmptyString;
		}

		L1DeathMatch.getInstance().addMember(pc);
		if (L1HauntedHouse.getInstance().isMember(pc))
			L1HauntedHouse.getInstance().removeMember(pc);
		return StringUtil.EmptyString;
	}
	
	private String petMatch(L1PcInstance pc, int objid2) {
		Object[] petlist = pc.getPetList().values().toArray();
		if (petlist.length > 0) {
			pc.sendPackets(L1ServerMessage.sm1187); // 펫의 아뮤렛트가 사용중입니다.
			return StringUtil.EmptyString;
		}
		if (!L1PetMatch.getInstance().enterPetMatch(pc, objid2)) {
			pc.sendPackets(L1ServerMessage.sm1182);
		}
		return StringUtil.EmptyString;
	}
	
	private String petRacing(L1PcInstance pc, int npcId) {
		if (L1Racing.getInstance().getGameStatus() == L1HauntedHouse.STATUS_PLAYING) {
			pc.sendPackets(L1ServerMessage.sm1182);
			return StringUtil.EmptyString;
		}
		if (L1Racing.getInstance().getGameStatus() == L1HauntedHouse.STATUS_CLEANUP) {
			pc.sendPackets(L1ServerMessage.sm1182);
			return StringUtil.EmptyString;
		}
		if (L1Racing.getInstance().getMembersCount() >= 8) {
			pc.sendPackets(L1ServerMessage.sm1184);
			return StringUtil.EmptyString;
		}
		if (pc.getLevel() < 30) {
			pc.sendPackets(new S_ServerMessage(1273, "30", "99"), true);
			return StringUtil.EmptyString;
		}

		if (!L1Racing.getInstance().isMember(pc)) {
			if (!pc.getInventory().checkItem(L1ItemId.ADENA, 1000)) {
				pc.sendPackets(L1ServerMessage.sm189);
				return StringUtil.EmptyString;
			}
			pc.getInventory().removeItem(L1ItemId.ADENA, 1000);
			L1Racing.getInstance().add(0, pc);
		}

		if (L1DeathMatch.getInstance().isMember(pc)) {
			L1DeathMatch.getInstance().removeMember(pc);
		}
		if (L1HauntedHouse.getInstance().isMember(pc)) {
			L1Racing.getInstance().remove(0, pc);
		}
		return StringUtil.EmptyString;
	}
}


