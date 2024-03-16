package l1j.server.server.model.npc.action.html;

import l1j.server.server.ActionCodes;
import l1j.server.server.datatables.DoorSpawnTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtmlAction;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StringUtil;

public class CastlegateAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new CastlegateAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private CastlegateAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		castleDoorStatus(pc, obj.getId());
		return null;
	}
	
	private void castleDoorStatus(L1PcInstance pc, int objid) {
		String htmlid = null;
		String doorStatus = null;
		String[] htmldata = null;
		String[] doorName = null;
		String doorCrack = null;
		int[] doornpc = null;

		switch (pc.getClan().getCastleId()) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
			htmlid = "orville5";
			doornpc = new int[] { 2031, 2032, 2033, 2034, 2035, 2030 };
			doorName = new String[] { "$1399", "$1400", "$1401", "$1402", "$1403", "$1386" };
			htmldata = new String[12];
			break;
		case 5:
		case 6:
			htmlid = "potempin5";
			doornpc = new int[] { 2051, 2052, 2050 }; // 남문, 동문, 현관문
			doorName = new String[] { "$1399", "$1603", "$1386" };
			htmldata = new String[4];
			break;
		}

		for (int i = 0; i < doornpc.length; i++) {
			L1DoorInstance door = DoorSpawnTable.getInstance().getDoor(doornpc[i]);
			if (door.getOpenStatus() == ActionCodes.ACTION_Close) {
				doorStatus = "$442"; // 닫혀
			} else if (door.getOpenStatus() == ActionCodes.ACTION_Open) {
				doorStatus = "$443"; // 열려
			}
			htmldata[i] = StringUtil.EmptyString + doorName[i] + doorStatus;
			switch (door.getCrackStatus()) {
			case 0:doorCrack = "$439";break;
			case 1:doorCrack = "$438";break;
			case 2:doorCrack = "$437";break;
			case 3:doorCrack = "$436";break;
			case 4:doorCrack = "$435";break;
			default:doorCrack = "$434";break;
			}
			htmldata[i + doornpc.length] = StringUtil.EmptyString + doorName[i] + doorCrack;
		}
		
		if (pc.isGm())
			pc.sendPackets(new S_SystemMessage("Dialog " + htmlid), true);											

		pc.sendPackets(new S_NPCTalkReturn(objid, htmlid, htmldata), true);
		htmlid = null;
		doorStatus = null;
		htmldata = null;
		doorName = null;
		doorCrack = null;
		doornpc = null;
	}
}

