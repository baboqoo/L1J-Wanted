package l1j.server.server.model.npc.action.html;

import l1j.server.server.controller.action.War;
import l1j.server.server.datatables.DoorSpawnTable;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtmlAction;
import l1j.server.server.utils.StringUtil;

public class CloseigateAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new CloseigateAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private CloseigateAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		castleDoorClose(pc, npcId, false);
		return StringUtil.EmptyString;
	}
	
	private void castleDoorClose(L1PcInstance pc, int keeperId, boolean isOpen) {
		boolean isNowWar = false;
		int pcCastleId = 0;
		if (pc.getClanid() != 0) {
			L1Clan clan = pc.getClan();
			if (clan != null) {
				pcCastleId = clan.getCastleId();
			}
		}
		if (pcCastleId == 0) {// KKK 혈맹이 없거나, 성혈이 아닌경우
			return;
		}
		if (keeperId == 70656 || keeperId == 70549 || keeperId == 70985) {
			if (castleClan(L1CastleLocation.KENT_CASTLE_ID) && pcCastleId != L1CastleLocation.KENT_CASTLE_ID) {
				return;
			}
			isNowWar = War.getInstance().isNowWar(L1CastleLocation.KENT_CASTLE_ID);
		} else if (keeperId == 70600 || keeperId == 800651) { // OT
			if (castleClan(L1CastleLocation.OT_CASTLE_ID) && pcCastleId != L1CastleLocation.OT_CASTLE_ID) {
				return;
			}
			isNowWar = War.getInstance().isNowWar(L1CastleLocation.OT_CASTLE_ID);
		} else if (keeperId == 70778 || keeperId == 70987 || keeperId == 70687) {
			if (castleClan(L1CastleLocation.WW_CASTLE_ID) && pcCastleId != L1CastleLocation.WW_CASTLE_ID) {
				return;
			}
			isNowWar = War.getInstance().isNowWar(L1CastleLocation.WW_CASTLE_ID);
		} else if (keeperId == 70817 || keeperId == 70800 || keeperId == 70988 || keeperId == 70990 || keeperId == 70989 || keeperId == 70991
				|| keeperId == 800650) {
			if (castleClan(L1CastleLocation.GIRAN_CASTLE_ID) && pcCastleId != L1CastleLocation.GIRAN_CASTLE_ID) {
				return;
			}
			isNowWar = War.getInstance().isNowWar(L1CastleLocation.GIRAN_CASTLE_ID);

		} else if (keeperId == 70863 || keeperId == 70992 || keeperId == 70862 || keeperId == 800652) {
			if (castleClan(L1CastleLocation.HEINE_CASTLE_ID) && pcCastleId != L1CastleLocation.HEINE_CASTLE_ID) {
				return;
			}
			isNowWar = War.getInstance().isNowWar(L1CastleLocation.HEINE_CASTLE_ID);
		} else if (keeperId == 70995 || keeperId == 70994 || keeperId == 70993) {
			if (castleClan(L1CastleLocation.DOWA_CASTLE_ID) && pcCastleId != L1CastleLocation.DOWA_CASTLE_ID) {
				return;
			}
			isNowWar = War.getInstance().isNowWar(L1CastleLocation.DOWA_CASTLE_ID);
		} else if (keeperId == 70996) {
			if (castleClan(L1CastleLocation.ADEN_CASTLE_ID) && pcCastleId != L1CastleLocation.ADEN_CASTLE_ID) {
				return;
			}
			isNowWar = War.getInstance().isNowWar(L1CastleLocation.ADEN_CASTLE_ID);
		}
		for (L1DoorInstance door : DoorSpawnTable.getInstance().getDoorList()) {
			if (door.getKeeperId() == keeperId) {
				if (isNowWar && door.getMaxHp() > 1) {
				} else {
					if (isOpen)	{
						door.open();
					} else {
						door.close();
					}
				}
			}
		}
	}
	
	private boolean castleClan(int castleId) {
		boolean isExistDefenseClan = false;
		for (L1Clan clan : L1World.getInstance().getAllClans()) {
			if (castleId == clan.getCastleId()) {
				isExistDefenseClan = true;
				break;
			}
		}
		return isExistDefenseClan;
	}
}

