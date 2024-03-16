package l1j.server.server.model;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.UBTable;
import l1j.server.server.model.L1GroundInventory.GROUND_ITEM_DELETE_TYPE;
import l1j.server.server.model.Instance.L1ItemInstance;

public class L1DeleteItemOnGround {
	private static final List<Integer> NOT_DELETE_MAP_IDS = Arrays.asList(new Integer[] {
			15, 29, 52, 64, 300, 260, 15492, 15493, 15494
	});
	
	private ItemGroundDeleteTimer _deleteTimer;

	public static final int EXECUTE_STATUS_NONE		= 0;
	public static final int EXECUTE_STATUS_READY	= 2;
	private int _executeStatus = EXECUTE_STATUS_NONE;
	
	private static final int INTERVAL = Config.ALT.ALT_ITEM_DELETION_TIME * 60000 - 10000;

	private static final Logger _log = Logger. getLogger(L1DeleteItemOnGround.class.getName());

	public L1DeleteItemOnGround() {
	}

	private class ItemGroundDeleteTimer implements Runnable {
		@Override
		public void run() {
			try {
				switch(_executeStatus) {
				case EXECUTE_STATUS_NONE: {
					//	L1World.getInstance().broadcastPacketToAll(new S_ServerMessage(166, "월드 맵상의 아이템","10초 후에 삭제됩니다"), true);
						_executeStatus = EXECUTE_STATUS_READY;
						GeneralThreadPool.getInstance().schedule(this, 10000);
					}
					break;
				case EXECUTE_STATUS_READY: {
						deleteItem();
				//		L1World.getInstance().broadcastPacketToAll(new S_ServerMessage(166, "월드 맵상의 아이템", "삭제되었습니다"), true);
						_executeStatus = EXECUTE_STATUS_NONE;
						GeneralThreadPool.getInstance().schedule(this, INTERVAL);
					}
					break;
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void initialize() {
		if (Config.ALT.ALT_ITEM_DELETION_TYPE != GROUND_ITEM_DELETE_TYPE.AUTO) {
			return;
		}
		_deleteTimer = new ItemGroundDeleteTimer();
		GeneralThreadPool.getInstance().schedule(_deleteTimer, INTERVAL); // 타이머 개시
	}

	private void deleteItem() {
		int numOfDeleted = 0;
		L1ItemInstance item = null;
		L1Inventory groundInventory = null;
		for (L1Object obj : L1World.getInstance().getObject()) {
			if (!(obj instanceof L1ItemInstance)) {
				continue;
			}
			item = (L1ItemInstance) obj;
			if ((item.getX() == 0 && item.getY() == 0) || item.getItemOwner() != null) {// 지면상의 아이템은 아니고, 누군가의 소유물
				continue;
			}
			if (item.getItem().getItemId() == 40515) {// 정령의 돌
				continue;
			}
			
			short mapId = item.getMapId();
			if (L1HouseLocation.isInHouse(item.getX(), item.getY(), mapId) || NOT_DELETE_MAP_IDS.contains((int)mapId)) {// 제외 맵
				continue;
			}
			// 무한대전시 대전장안 아이템 안사라지게
			boolean ck = false;
			if ((mapId >= 88 && mapId <= 98) || (mapId >= 5557 && mapId <= 5561)) {
			    for (L1UltimateBattle ub : UBTable.getInstance().getAllUb()) {
					if (mapId == ub.getMapId() && ub.isNowUb()) {
						ck = true;
						break;
					}
				}
			}
			if (ck) {
				continue;
			}
			if (L1World.getInstance().getVisiblePlayer(item, Config.ALT.ALT_ITEM_DELETION_RANGE).isEmpty()) {// 지정 범위내에 플레이어가 없으면 삭제
				groundInventory = L1World. getInstance(). getInventory(item.getX(), item.getY(), mapId);
				groundInventory.removeItem(item);
				numOfDeleted++;
			}
		}
		//_log.fine("월드 맵상의 아이템을 자동 삭제. 삭제수: " + numOfDeleted);
		_log.fine("Automatically delete items on the world map. Number of deletions: " + numOfDeleted);
	}
}

