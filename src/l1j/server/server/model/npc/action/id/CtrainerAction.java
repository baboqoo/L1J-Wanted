package l1j.server.server.model.npc.action.id;

import l1j.server.server.datatables.CharacterCompanionTable;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.npc.L1NpcIdAction;

public class CtrainerAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new CtrainerAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private CtrainerAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return petTrainer(pc, s);
	}
	
	private String petTrainer(L1PcInstance pc, String s){
        if (s.equalsIgnoreCase("b")) {
        	L1PetInstance pet = pc.getPet();
            if (pet == null) {
            	return "c_trainer3";
            }
            if (pet.is_minus_exp_penalty()) {
            	return "c_trainer4";
            }
            if (pet.get_friend_ship_marble() < 2) {
            	return "c_trainer5";
            }
            if (!pc.getInventory().checkItem(31096, 5)) {
            	return "c_trainer6";
            }
            if ((pet.getPetInfo().getTraningTime() != null) && (System.currentTimeMillis() - pet.getPetInfo().getTraningTime().getTime() < 86400000L)) {
            	return "c_trainer7";
            }
            if (pet.getLevel() < 30) {
            	return "c_trainer8";
            }
            if (pc.getInventory().consumeItem(31096, 5)) {
            	pet.add_friend_ship_marble(-2);
            	int addExp = (int)(ExpTable.getNeedExpNextLevel(52) * 0.003D);
            	pet.getExpHandler().addExp(addExp);
            	CharacterCompanionTable.getInstance().TraningSave(pet.getPetInfo());
                return "c_trainer2";
            }
        }
        return null;
	}
}

