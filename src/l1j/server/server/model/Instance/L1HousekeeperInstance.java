package l1j.server.server.model.Instance;

import l1j.server.server.datatables.HouseTable;
import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1NpcTalkData;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1House;
import l1j.server.server.templates.L1Npc;

public class L1HousekeeperInstance extends L1NpcInstance {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param template
	 */
	public L1HousekeeperInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onAction(L1PcInstance pc) {
		L1Attack attack = new L1Attack(pc, this);
		attack.calcHit();
		attack.action();
	}

	@Override
	public void onTalkAction(L1PcInstance pc) {
		int objid = getId();
		int npcid = getNpcTemplate().getNpcId();
		L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(npcid);
		if (talking == null) {
			return;
		}
		
		if (npcid == 11090) {
			talkSoloAgit(pc, npcid, talking);
			return;
		}
		
		String htmlid = null;
		String[] htmldata = null;
		boolean isOwner = false;

		L1Clan clan = pc.getClan();
		if (clan != null) {
			int houseId = clan.getHouseId();
			if (houseId != 0) {
				L1House house = HouseTable.getInstance().getHouseTable(houseId);
				if (npcid == house.getKeeperId()) {
					isOwner = true;
				}
			}
		}

		if (!isOwner) {
			L1House targetHouse = null;
			for (L1House house : HouseTable.getInstance().getHouseTableList()) {
				if (npcid == house.getKeeperId()) {
					targetHouse = house;
					break;
				}
			}
			if (targetHouse == null) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("현재 아지트는 사용되지않습니다"), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1033), true), true);
				return;
			}

			boolean isOccupy = false;
			String clanName = null;
			String leaderName = null;
			for (L1Clan targetClan : L1World.getInstance().getAllClans()) {
				try {
					if (targetHouse.getHouseId() == targetClan.getHouseId()) {
						isOccupy = true;
						clanName = targetClan.getClanName();
						leaderName = targetClan.getLeaderName();
						break;
					}
				} catch (Exception e) {
				}
			}
			
			if (isOccupy) {
				htmlid = "agname";
				htmldata = new String[] { clanName, leaderName, targetHouse.getHouseName() };
			} else {
				htmlid = "agnoname";
				htmldata = new String[] { targetHouse.getHouseName() };
			}
		}

		if (htmlid != null) {
			if (htmldata != null) {
				pc.sendPackets(new S_NPCTalkReturn(objid, htmlid, htmldata), true);
			} else {
				pc.sendPackets(new S_NPCTalkReturn(objid, htmlid), true);
			}
			if (pc.isGm())
				pc.sendPackets(new S_SystemMessage("Dialog " + htmlid), true);											
		} else {
			if (pc.isGm())
				pc.sendPackets(new S_SystemMessage("Dialog " + talking.getCaoticAction() + " | " + talking.getNormalAction()), true);											
			pc.sendPackets(new S_NPCTalkReturn(talking, objid, pc.getAlignment() < -500 ? 2 : 1), true);
		}
	}
	
	void talkSoloAgit(L1PcInstance pc, int npcid, L1NpcTalkData talking) {
		String htmlid = null;
		if (!pc.getInventory().checkItem(31235)) {
			htmlid = "total_soloagit2";
		}
		if (htmlid != null) {
			if (pc.isGm())
				pc.sendPackets(new S_SystemMessage("Dialog " + htmlid), true);											
			pc.sendPackets(new S_NPCTalkReturn(this.getId(), htmlid), true);
		} else {
			if (pc.isGm())
				pc.sendPackets(new S_SystemMessage("Dialog " + talking.getCaoticAction() + " | " + talking.getNormalAction()), true);											
			pc.sendPackets(new S_NPCTalkReturn(talking, this.getId(), pc.getAlignment() < -500 ? 2 : 1), true);
		}
	}

	@Override
	public void onFinalAction(L1PcInstance pc, String action) {
	}

	public void doFinalAction(L1PcInstance pc) {
	}

}


