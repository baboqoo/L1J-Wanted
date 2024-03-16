package l1j.server.server.controller.action;

import l1j.server.server.serverpackets.message.S_SystemMessage;
import java.util.LinkedList;

import l1j.server.server.GameServerSetting;
import l1j.server.server.datatables.DropTable;
import l1j.server.server.datatables.EventTable;
import l1j.server.server.datatables.EventTable.EventInfo;
import l1j.server.server.datatables.EventTable.EventSpawnNpc;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_NotificationMessageNoti;
import l1j.server.server.serverpackets.polymorph.S_PolymorphEvent;
import l1j.server.server.utils.L1SpawnUtil;

/**
 * 이벤트 컨트롤러
 * @author LinOffice
 */
public class Event implements ControllerInterface {
	private static class newInstance {
		public static final Event INSTANCE = new Event();
	}
	public static Event getInstance(){
		return newInstance.INSTANCE;
	}
	private Event(){}

	@Override
	public void execute() {
		try {
			LinkedList<EventInfo> list = EventTable.getEventList();
			if (list == null || list.isEmpty()) {
				return;
			}
			long currentTime = System.currentTimeMillis();
			for (EventInfo event : list) {
				// 시작 시간이 현재 시간보다 클경우 처리되지 않는다
				if (event.getStart_date().getTime() > currentTime) {
					continue;
				}
				if (event.isActive()) {
					// 이벤트 기간 종료
					if (event.getFinish_date().getTime() < currentTime) {
						doFinish(event);
					}
				} else {
					// 이벤트 가동
					doStart(event);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void execute(L1PcInstance pc) {
	}
	
	/**
	 * 이벤트를 시작한다.
	 * @param event
	 */
	void doStart(EventInfo event) {
		event.setActive(true);
		if (event.isBroadcast()) {
//AUTO SRM: 			L1World.getInstance().broadcastPacketToAll(new S_NotificationMessageNoti(0,  String.format("잠시 후 '%s' 가 시작됩니다.", event.getDescription()), "22 b1 4c", 30), true); // CHECKED OK
			L1World.getInstance().broadcastPacketToAll(new S_NotificationMessageNoti(0,  String.format(S_SystemMessage.getRefTextNS(1286) + "%s" + S_SystemMessage.getRefText(1287), event.getDescription()), "22 b1 4c", 30), true);
		}
		switch (event.getEvent_flag()) {
		case SPAWN_NPC:
			spawnNpc(event.getSpawn_data());
			break;
		case DROP_ADENA:
			DropTable.EVENT_DROP_ADENA_RATE = event.getDrop_rate();
			break;
		case DROP_ITEM:
			DropTable.EVENT_DROP_ITEM_RATE = event.getDrop_rate();
			break;
		case POLY:
			L1World.getInstance().broadcastPacketToAll(S_PolymorphEvent.POLY_EVENT_ON);
			GameServerSetting.POLY_LEVEL_EVENT = true;
			break;
		}
	}
	
	/**
	 * 이벤트를 종료한다.
	 * @param event
	 */
	void doFinish(EventInfo event) {
		event.setActive(false);
		if (event.isBroadcast()) {
//AUTO SRM: 			L1World.getInstance().broadcastPacketToAll(new S_NotificationMessageNoti(0,  String.format("잠시 후 '%s' 가 종료됩니다.", event.getDescription()), "22 b1 4c", 30), true); // CHECKED OK
			L1World.getInstance().broadcastPacketToAll(new S_NotificationMessageNoti(0,  String.format(S_SystemMessage.getRefText(1286) + " %s " + S_SystemMessage.getRefText(1288), event.getDescription()), "22 b1 4c", 30), true);
		}
		if (event.getFinish_delete_item() != null) {
			finish_delete_item(event.getFinish_delete_item());
		}
		if (event.getFinish_map_rollback() != null) {
			finish_map_rollback(event.getFinish_map_rollback());
		}
		switch (event.getEvent_flag()) {
		case SPAWN_NPC:
			deleteNpc(event.getSpawn_data());
			break;
		case DROP_ADENA:
			DropTable.EVENT_DROP_ADENA_RATE = 0;
			break;
		case DROP_ITEM:
			DropTable.EVENT_DROP_ITEM_RATE = 0;
			break;
		case POLY:
			L1World.getInstance().broadcastPacketToAll(S_PolymorphEvent.POLY_EVENT_OFF);
			GameServerSetting.POLY_LEVEL_EVENT = false;
			break;
		}
	}
	
	/**
	 * 엔피씨를 스폰한다.
	 * @param spawn_data
	 */
	void spawnNpc(LinkedList<EventSpawnNpc> spawn_data) {
		if (spawn_data == null || spawn_data.isEmpty()) {
			return;
		}
		for (EventSpawnNpc spawn : spawn_data) {
			if (spawn.npcIns != null) {
				continue;
			}
			spawn.npcIns = L1SpawnUtil.spawn2(spawn.X, spawn.Y, (short)spawn.MAP, spawn.HEAD, spawn.NPC, 0, 0, 0);
		}
	}
	
	/**
	 * 엔피씨를 제거한다.
	 * @param spawn_data
	 */
	void deleteNpc(LinkedList<EventSpawnNpc> spawn_data) {
		if (spawn_data == null || spawn_data.isEmpty()) {
			return;
		}
		for (EventSpawnNpc spawn : spawn_data) {
			if (spawn.npcIns == null) {
				continue;
			}
			spawn.npcIns.deleteMe();
			spawn.npcIns = null;
		}
	}
	
	/**
	 * 이벤트 아이템 제거
	 * @param deleteList
	 */
	void finish_delete_item(LinkedList<Integer> deleteList) {
		for (int del : deleteList) {
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				pc.getInventory().consumeItem(del);
			}
		}
		EventTable.getInstance().finishDeleteItem(deleteList);
	}
	
	/**
	 * 이벤트 종료 맵 유저 귀환
	 * @param rollbackList
	 */
	void finish_map_rollback(LinkedList<Integer> rollbackList) {
		for (int mapId : rollbackList) {
			for (L1PcInstance pc : L1World.getInstance().getMapPlayer(mapId)) {
				if (pc == null || pc.getNetConnection() == null) {
					continue;
				}
				int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_GIRAN);
				pc.getTeleport().start(loc[0], loc[1], (short) loc[2], pc.getMoveState().getHeading(), true);
			}
		}
	}

}


