package l1j.server.server.model.npc.action.html;

import java.util.ArrayList;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.CharSoldierTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.gametime.RealTime;
import l1j.server.server.model.npc.L1NpcHtmlAction;
import l1j.server.server.serverpackets.S_CloseList;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1CharSoldier;
import l1j.server.server.templates.L1Npc;

public class DemandAction implements L1NpcHtmlAction {
	private static class newInstance {
		public static final L1NpcHtmlAction INSTANCE = new DemandAction();
	}
	public static L1NpcHtmlAction getInstance(){
		return newInstance.INSTANCE;
	}
	private DemandAction(){}

	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		soldierHire(pc, obj.getId());
		return null;
	}
	
	private void soldierHire(L1PcInstance pc, int objid) {
		if (pc.getPetList().size() > 0) {
			pc.sendPackets(new S_CloseList(pc.getId()), true);
			return;
		}
		L1PetInstance pet = null;
	    for (L1NpcInstance npc : pc.getPetList().values()) {
	        if (npc instanceof L1PetInstance) {
	            pet = (L1PetInstance)npc;
	            if (pet != null){
	            	pc.sendPackets(L1ServerMessage.sm1187);
	            	pc.sendPackets(new S_CloseList(pc.getId()), true);
	            	return;
	            }
	        }
	    }
		long time = new RealTime().getSeconds();
		CharSoldierTable soldier = CharSoldierTable.getInstance();
		ArrayList<L1CharSoldier> list = soldier.getCharSoldier(pc.getId(), time);
		int d = soldier.SoldierCalculate(pc.getId());
		if (d > 0 && list.size() == 0) {
			if (pc.isGm())
				pc.sendPackets(new S_SystemMessage("Dialog " +"colbert2"), true);											

			pc.sendPackets(new S_NPCTalkReturn(objid, "colbert2"), true);
			return;
		} else if (d == 0) {
			if (pc.isGm())
				pc.sendPackets(new S_SystemMessage("Dialog " +"colbert3"), true);											

			pc.sendPackets(new S_NPCTalkReturn(objid, "colbert3"), true);
			return;
		}

		L1CharSoldier t;
		boolean ck = false;
		for (int i = 0; i < list.size(); i++) {
			t = list.get(i);
			int a = t.getSoldierNpc();
			int b = t.getSoldierCount();
			L1Npc npcTemp = NpcTable.getInstance().getTemplate(a);
			@SuppressWarnings("unused")
			L1SummonInstance summon = null;
			for (int c = 0; c < b; c++) {
				int petcost = 0;

				int charisma = pc.getAbility().getTotalCha();
				charisma += pc.isWizard() ? 6 : 12;
				charisma -= petcost;
				if (charisma >= 6) {
					summon = new L1SummonInstance(npcTemp, pc, true);
				} else {
					if (!ck) {
						pc.sendPackets(L1ServerMessage.sm319);
					}
					ck = true;
					break;
				}
			}
			CharSoldierTable.getInstance().delCharCastleSoldier(pc.getId(), t.getSoldierTime());
		}
		t = null;
		pc.sendPackets(new S_CloseList(pc.getId()), true);
	}
}

