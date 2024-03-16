package l1j.server.server.model.npc.action.id;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.skill.L1SkillInfo;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.utils.StringUtil;

public class IbsBuffAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new IbsBuffAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private IbsBuffAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return townBuffWorldWar(pc, s);
	}
	
	private String townBuffWorldWar(L1PcInstance pc, String s){
		if (s.equals("a")) {
			if (pc.getInventory().consumeItem(L1ItemId.ADENA, 1000)) {
				boolean hasCastle = checkHasCastle(pc, L1CastleLocation.OT_CASTLE_ID) || checkHasCastle(pc, L1CastleLocation.GIRAN_CASTLE_ID) || checkHasCastle(pc, L1CastleLocation.HEINE_CASTLE_ID);
				L1BuffUtil.skillArrayAction(pc, (hasCastle || pc.getInventory().checkItem(130001, 1)) ? L1SkillInfo.WAR_BUFF_ARRAY : L1SkillInfo.TOWN_BUFF_ARRAY);
				return StringUtil.EmptyString;
			}
			pc.sendPackets(L1ServerMessage.sm189); // \f1%0이 부족합니다.
		}
		return null;
	}
	
	private boolean checkHasCastle(L1PcInstance player, int castle_id) {
		if (player.getClanid() != 0) {
			L1Clan clan = player.getClan();
			if (clan != null) {
				if (clan.getCastleId() == castle_id) {
					return true;
				}
			}
		}
		return false;
	}
}

