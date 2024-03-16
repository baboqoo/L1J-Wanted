package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class CsaintAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new CsaintAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private CsaintAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return petSoul(pc, s, obj);
	}
	
	private String petSoul(L1PcInstance pc, String s, L1Object obj){
		L1PetInstance pet = pc.getPet();
        if (s.equalsIgnoreCase("a")) {
            if (pet == null) {
            	return "c_saint4";
            }
            if (pet.getPetInfo().getLessExp() <= 0) {
            	return "c_saint5";
            }
            if (pc.isGm())
				pc.sendPackets(new S_SystemMessage("Dialog " + "c_saint10"), true);											

			pc.sendPackets(new S_NPCTalkReturn(obj.getId(), "c_saint10", new String[] { String.valueOf(pet.getLevel() * 4400) }), true);
        } else if (s.equalsIgnoreCase("b")) {
        	if (pet == null) {
        		return "c_saint7";
        	}
            pet.oblivion();
            return "c_saint6";
        } else if (s.equalsIgnoreCase("c")) {
        	if (pet == null) {
        		return "c_saint4";
        	}
        	if (!pc.getInventory().consumeItem(L1ItemId.ADENA, pet.getLevel() * 4400)) {
        		return "c_saint2";
        	}
        	pet.repairExp();
        	return "c_saint3";
        }
        return null;
	}
}

