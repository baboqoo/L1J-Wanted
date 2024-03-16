package l1j.server.server.model.npc.action.id;

import l1j.server.IndunSystem.training.TrainingCreator;
import l1j.server.server.datatables.KeyTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcIdAction;

public class KaizerAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new KaizerAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private KaizerAction(){}
	
	private TrainingCreator training = TrainingCreator.getInstance();
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return kaizer(pc, s);
	}
	
	private String kaizer(L1PcInstance pc, String s){
		if (s.equalsIgnoreCase("1")) {// 대여
			if (pc.getInventory().checkItem(80500)) {
				return "bosskey6";// 이미 훈련소 열쇠를 가지고 계신 것 같군요. 많은 분들께서 이용하실 수 있도록 훈련소는 한 사람 당 하나 씩만 대여해 드리고 있습니다.
			}
			if (training.countRoom() >= TrainingCreator.MAX_ROOM_COUNT) {
				return "bosskey3";// 죄송합니다. 지금은 모든 훈련소에서 훈련이 진행 중 입니다.
			}
			return "bosskey4";
		} else if (s.matches("[2-4]")) {
			if (pc.getInventory().checkItem(80500)) {
				return "bosskey6";// 이미 훈련소 열쇠를 가지고 계신 것 같군요. 많은 분들께서 이용하실 수 있도록 훈련소는 한 사람 당 하나 씩만 대여해 드리고 있습니다.
			}
			if (training.countRoom() >= TrainingCreator.MAX_ROOM_COUNT) {
				return "bosskey3";// 죄송합니다. 지금은 모든 훈련소에서 훈련이 진행 중 입니다.
			}
			int count = 0;
			if (s.equalsIgnoreCase("2")) {
				count = 4;
			} else if (s.equalsIgnoreCase("3")) {
				count = 8;
			} else if (s.equalsIgnoreCase("4")) {
				count = 16;
			}
			if (!pc.getInventory().consumeItem(L1ItemId.ADENA, count * 300)) {
				return "bosskey5";// 죄송하지만, 사용료를 지불하지 않으시면 훈련소를 빌려드릴 수 없습니다. 아덴 왕국의 지원금만으로 이 많은 훈련소를 관리하는 것이 쉬운 일은 아니라서요.
			}
			int id = training.blankMapId();
			if (id == 0) {
				return "bosskey3";// 죄송합니다. 지금은 모든 훈련소에서 훈련이 진행 중 입니다.
			}
			training.create(pc, id);
			
			L1ItemInstance item = null;
			for (int i = 0; i < count; i++) {
				item = pc.getInventory().storeItem(80500, 1);
				item.setKeyId(id);
				if (KeyTable.checKey(item)) {
					KeyTable.updateKey(item);
				} else {
					KeyTable.storeKey(item);
				}
			}
			
			return "bosskey7";
			// 같이 훈련을 받으실 분들에게 열쇠를 나누어 주신 다음 저에게 보여주시면 훈련소로 안내해 드리겠습니다.
			// 훈련소의 대여시간은 최대 4시간이며, 훈련 중이라 해도 대여 시간이 종료되면 다음 사람을 위해 훈련소 사용이 중지됩니다.
			// 훈련용 몬스터를 소환하실 때에는 항상 훈련소의 남은 사용 시간을 확인하시기 바랍니다.
		} else if (s.equalsIgnoreCase("6")) {// 입장
			if (training.countRoom() >= TrainingCreator.MAX_ROOM_COUNT) {
				return "bosskey3";// 죄송합니다. 지금은 모든 훈련소에서 훈련이 진행 중 입니다.
			}
			L1ItemInstance item = pc.getInventory().findItemId(80500);
			if (item != null) {
				pc.getTeleport().start(32901, 32814, (short) item.getKeyId(), pc.getMoveState().getHeading(), true);
				return null;
			}
			return "bosskey2";// 훈련소 열쇠를 가지고 있지 않으신 것 같네요. 먼저 훈련소를 대여하신 후에 사용하실 수 있습니다.
		}
		return null;
	}
}

