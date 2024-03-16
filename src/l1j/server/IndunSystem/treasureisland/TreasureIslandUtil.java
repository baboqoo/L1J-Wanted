package l1j.server.IndunSystem.treasureisland;

import javolution.util.FastTable;
import l1j.server.common.bin.treasureisland.TreasureIslandBox;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.serverpackets.message.S_NotificationMessageNoti;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;

public class TreasureIslandUtil {
	protected static final FastTable<L1PcInstance> EXCAVATION_USERS	= new FastTable<L1PcInstance>();
	
	protected static final ServerBasePacket[] TREASURE_STOP_MENTS = {
		new S_NotificationMessageNoti(0, "$36761", "2d e6 83", 1),// 보물섬의 보물이 사라집니다. 더는 보물을 발굴할 수 없습니다.
		new S_NotificationMessageNoti(0, "$36762", "2d e6 83", 1),// 10초 후 보물섬의 통로가 사라집니다.
		new S_NotificationMessageNoti(0, "$36763", "2d e6 83", 1),// 9초 후 보물섬의 통로가 사라집니다.
		new S_NotificationMessageNoti(0, "$36764", "2d e6 83", 1),// 8초 후 보물섬의 통로가 사라집니다.
		new S_NotificationMessageNoti(0, "$36765", "2d e6 83", 1),// 7초 후 보물섬의 통로가 사라집니다.
		new S_NotificationMessageNoti(0, "$36766", "2d e6 83", 1),// 6초 후 보물섬의 통로가 사라집니다.
		new S_NotificationMessageNoti(0, "$36767", "2d e6 83", 1),// 5초 후 보물섬의 통로가 사라집니다.
		new S_NotificationMessageNoti(0, "$36768", "2d e6 83", 1),// 4초 후 보물섬의 통로가 사라집니다.
		new S_NotificationMessageNoti(0, "$36769", "2d e6 83", 1),// 3초 후 보물섬의 통로가 사라집니다.
		new S_NotificationMessageNoti(0, "$36770", "2d e6 83", 1),// 2초 후 보물섬의 통로가 사라집니다.
		new S_NotificationMessageNoti(0, "$36771", "2d e6 83", 1)// 만월의 보물섬이 자욱한 해무에 가려져 이제 접근할 수 없습니다.
	};
	
//AUTO SRM: 	protected static final ServerBasePacket TREASURE_OPEN		= new S_NotificationMessageNoti(0, "하이네 마을에 달의 기사 질리언이 보물섬으로 향하는 통로를 개방했습니다.", "2d e6 83", 10); // CHECKED OK
	protected static final ServerBasePacket TREASURE_OPEN		= new S_NotificationMessageNoti(0, S_SystemMessage.getRefText(1283), "2d e6 83", 10);
//AUTO SRM: 	protected static final ServerBasePacket TREASURE_STOP		= new S_NotificationMessageNoti(0, "만월의 보물섬 종료. 보물을 발굴하고 섬에 체류한 참여자에게 보상을 지급했습니다.", "2d e6 83", 10); // CHECKED OK
	protected static final ServerBasePacket TREASURE_STOP		= new S_NotificationMessageNoti(0, S_SystemMessage.getRefText(1284), "2d e6 83", 10);
//AUTO SRM: 	protected static final ServerBasePacket COMMAND_MESSAGE		= new S_SystemMessage(".보물섬 [시작/종료/리로드]"); // CHECKED OK
	protected static final ServerBasePacket COMMAND_MESSAGE		= new S_SystemMessage(S_SystemMessage.getRefText(54), true);

	/**
	 * 보물 발굴 보상
	 * @param pc
	 * @param obj
	 */
	protected static void excavateReward(L1PcInstance pc, TreasureIslandBox.RewardBoxInfoListT.RewardBoxInfoT.RewardBoxT reward){
		if (reward == null) {
			System.out.println("[TreasureIslandUtil] EXCAVATE_REWARD_BIN_EMPTY");
			return;
		}
		L1Item item = ItemTable.getInstance().findItemByNameId(reward.get_nameid());
		if (pc.getInventory().checkAddItem(item, 1) != L1Inventory.OK) return;
		L1ItemInstance create = pc.getInventory().storeItem(item.getItemId(), 1);
		pc.sendPackets(new S_ServerMessage(143, String.format("%s (%d)", create.getItem().getDesc(), 1)), true);
	}
	
	/**
	 * 종료 보상
	 * @param pc
	 */
	protected static void endReward(L1PcInstance pc){
		if (!EXCAVATION_USERS.contains(pc)) {
			return;// 발굴한 유저 체크
		}
		pc.einGetExcute(300);// 아인하사드 300%
		if (pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(500234), 1) != L1Inventory.OK) return;
		L1ItemInstance item = pc.getInventory().storeItem(500234, 1);// 달빛의 선물 상자
		pc.sendPackets(new S_ServerMessage(143, String.format("%s (%d)", item.getItem().getDesc(), 1)), true);
	}
}



