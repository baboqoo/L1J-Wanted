package l1j.server.server.model;

import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.object.S_ItemDropObject;

public class L1GroundInventory extends L1Inventory {
	private static final long serialVersionUID = 1L;
	
	public static enum GROUND_ITEM_DELETE_TYPE {
		NONE, STD, AUTO;
		public static GROUND_ITEM_DELETE_TYPE fromString(String str) {
			switch (str) {
			case "none":
				return NONE;
			case "std":
				return STD;
			case "auto":
				return AUTO;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments GROUND_ITEM_DELETE_TYPE, %s", str));
			}
		}
	}

	private class DeletionTimer implements Runnable {
		private final L1ItemInstance _item;

		public DeletionTimer(L1ItemInstance item) {
			_item = item;
		}

		@Override
		public void run() {
			try {
				synchronized (L1GroundInventory.this) {
					if (!_items.contains(_item)) {// 주워진 타이밍에 따라서는 이 조건을 채울 수 있다
						return; // 이미 주워지고 있다
					}
					removeItem(_item);
				}
			} catch (Throwable t) {
				_log.log(Level.SEVERE, t.getLocalizedMessage(), t);
			}
		}
	}

	private void setTimer(L1ItemInstance item) {
		if (Config.ALT.ALT_ITEM_DELETION_TYPE != GROUND_ITEM_DELETE_TYPE.STD) {
			return;
		}
		if (item.getItemId() == 40515) {
			return; // 정령의 돌
		}
		GeneralThreadPool.getInstance().schedule(new DeletionTimer(item), Config.ALT.ALT_ITEM_DELETION_TIME * 60000);
	}

	public L1GroundInventory(int objectId, int x, int y, short map) {
		setId(objectId);
		setX(x);
		setY(y);
		setMap(map);
		L1World.getInstance().addVisibleObject(this);
	}

	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		if (perceivedFrom == null || this == null) {
			return;
		}
		for (L1ItemInstance item : getItems()) {
			perceivedFrom.addKnownObject(item);
			perceivedFrom.sendPackets(new S_ItemDropObject(item), true);
		}
	}

	// 인식 범위내에 있는 플레이어에 오브젝트 송신
	@Override
	public void insertItem(L1ItemInstance item) {
		setTimer(item);
		S_ItemDropObject s_dropItem = new S_ItemDropObject(item);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			pc.sendPackets(s_dropItem);
			pc.addKnownObject(item);
		}
		s_dropItem.clear();
		s_dropItem = null;
		_items.add(item);
	}

	// 보이는 범위내에 있는 플레이어의 오브젝트 갱신
	@Override
	public void updateItem(L1ItemInstance item) {
		S_ItemDropObject s_dropItem = new S_ItemDropObject(item);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(item)) {
			pc.sendPackets(s_dropItem);
		}
		s_dropItem.clear();
		s_dropItem = null;
	}

	// 하늘 목록 파기 및 보이는 범위내에 있는 플레이어의 오브젝트 삭제
	@Override
	public void deleteItem(L1ItemInstance item) {
		S_RemoveObject s_remove = new S_RemoveObject(item);
		L1World world = L1World.getInstance();
		for (L1PcInstance pc : world.getRecognizePlayer(item)) {
			pc.sendPackets(s_remove);
			pc.removeKnownObject(item);
		}
		s_remove.clear();
		s_remove = null;
		_items.remove(item);
		if (_items.size() == 0) {
			world.removeVisibleObject(this);
		}
	}

	private static Logger _log = Logger.getLogger(L1GroundInventory.class.getName());
}

