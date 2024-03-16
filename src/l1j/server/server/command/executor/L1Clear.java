package l1j.server.server.command.executor;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class L1Clear implements L1CommandExecutor{
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Clear();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1Clear() {}
	
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		if (pc == null) {
			return false;
		}
		for(L1Object obj : L1World.getInstance().getVisibleObjects(pc, 20)){// 20 범위 내에 오브젝트를 찾아서
			if(obj instanceof L1MonsterInstance){ // 몬스터라면
				L1MonsterInstance npc = (L1MonsterInstance) obj;
				if(npc.isDead()
						|| npc.getNpcId() == 7200003
						|| npc.getNpcId() == 7200029
						|| npc.getNpcId() == 7800000){
					continue;
				}
				npc.receiveDamage(pc, 150000); // 대미지
				pc.send_effect(obj.getId() , 2059);
			}
		}
		return true;
	}
}
