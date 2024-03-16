package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.utils.L1SpawnUtil;
import l1j.server.server.utils.StringUtil;

public class BossRecallAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new BossRecallAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private BossRecallAction(){}
	
	long actionTime;
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		switch (s) {
		case "1":// 네크로멘서
			recall(pc, 18699);
			return StringUtil.EmptyString;
		case "2":// 데스나이트
			recall(pc, 18698);
			return StringUtil.EmptyString;
		case "3":// 데몬
			recall(pc, 18700);
			return StringUtil.EmptyString;
		case "4":// 피닉스
			recall(pc, 18701);
			return StringUtil.EmptyString;
		case "delcall":// 소환된 몬스터 처치
			delcall();
			return StringUtil.EmptyString;
		default:
			return StringUtil.EmptyString;
		}
	}
	
	void recall(L1PcInstance pc, int npcId) {
		long currentTime = System.currentTimeMillis();
		if (actionTime >= currentTime) {
			pc.sendPackets(new S_ServerMessage(9184), true);// 추가 몬스터 소환은 일정 시간이 지나야 가능합니다.
			return;
		}
		actionTime = currentTime + 60000;// 60초 제한
		L1SpawnUtil.spawn(32753, 32835, (short) 2237, 5, npcId, 0, 3600000);
	}
	
	void delcall() {
		for (L1Object obj : L1World.getInstance().getVisibleObjects(2237).values()) {
			if (obj == null || obj instanceof L1MonsterInstance == false) {
				continue;
			}
			((L1MonsterInstance) obj).deleteMe();
		}
	}
}

