package l1j.server.GameSystem.astar;

import l1j.server.server.model.map.L1V1Map;
import l1j.server.server.model.map.L1WorldMap;

public final class World {

	public static boolean get_map(int map) {
		return L1WorldMap.getInstance().getMapCK((short) map);
	}

	public static void cloneMap(int targetId, int newId) {}

	public static void resetMap(int targetId, int resetId) {}


	private static final byte BITFLAG_IS_DOOR_IMPASSABLE_X = (byte) 0x80;
	private static final byte BITFLAG_IS_DOOR_IMPASSABLE_Y = (byte) 0x40;

	public static void isDoorMove(int x, int y, int map, boolean h, boolean flag) {
		L1V1Map m = (L1V1Map) L1WorldMap.getInstance().getMap((short) map);
		if (m != null) {
			if (!m.isInMap(x, y)) {
				return;
			}
			if (flag) {
				synchronized (m._doorMap) {
					m._doorMap[x - m.getX()][y - m.getY()] = 0;
				}
			} else {
				byte setBit = BITFLAG_IS_DOOR_IMPASSABLE_Y;
				if (h) {
					setBit = BITFLAG_IS_DOOR_IMPASSABLE_X;
				}
				synchronized (m._doorMap) {
					m._doorMap[x - m.getX()][y - m.getY()] = setBit;
				}
			}
		}
	}

	private static final byte HEADING_TABLE_X[] = { 0, 1, 1, 1, 0, -1, -1, -1 };
	private static final byte HEADING_TABLE_Y[] = { -1, -1, 0, 1, 1, 1, 0, -1 };

	public static boolean isDoorMove(int x, int y, int map, int h) {
		if (h < 0 || h > 7) {
			return false;
		}
		L1V1Map m = (L1V1Map) L1WorldMap.getInstance().getMap((short) map);
		if (m != null) {
			int newX = x + HEADING_TABLE_X[h];
			int newY = y + HEADING_TABLE_Y[h];
			if (x > newX) {
				int doorTile1 = accessDoorTile(newX, y, m);
				int doorTile2 = accessDoorTile(newX, newY, m);
				if (((doorTile1 & BITFLAG_IS_DOOR_IMPASSABLE_X) != 0) || ((doorTile2 & BITFLAG_IS_DOOR_IMPASSABLE_X) != 0)) {
					return true;
				}
			} else if (x < newX) {
				int doorTileOld = accessDoorTile(x, y, m);
				int doorTileNew = accessDoorTile(newX - 1, newY, m);
				if (((doorTileOld & BITFLAG_IS_DOOR_IMPASSABLE_X) != 0) || ((doorTileNew & BITFLAG_IS_DOOR_IMPASSABLE_X) != 0)) {
					return true;
				}
			}
			if (y < newY) {
				int doorTile1 = accessDoorTile(x, newY, m);
				int doorTile2 = accessDoorTile(newX, newY, m);
				if (((doorTile1 & BITFLAG_IS_DOOR_IMPASSABLE_Y) != 0) || ((doorTile2 & BITFLAG_IS_DOOR_IMPASSABLE_Y) != 0)) {
					return true;
				}
			} else if (y > newY) {
				int doorTileOld = accessDoorTile(x, y, m);
				int doorTileNew = accessDoorTile(newX, newY + 1, m);
				if (((doorTileOld & BITFLAG_IS_DOOR_IMPASSABLE_Y) != 0) || ((doorTileNew & BITFLAG_IS_DOOR_IMPASSABLE_Y) != 0)) {
					return true;
				}
			}
		}
		return false;
	}

	private static int accessDoorTile(int x, int y, L1V1Map m) {
		if (!m.isInMap(x, y)) {
			return 0;
		}
		synchronized (m._doorMap) {
			return m._doorMap[x - m.getX()][y - m.getY()];
		}
	}

	public static boolean isMapdynamic(int x, int y, int map) {
		L1V1Map m = (L1V1Map) L1WorldMap.getInstance().getMap((short) map);
		return !m.isPassable(x, y);
	}

	public static boolean isThroughObject(int x, int y, int map, int dir) {
		L1V1Map m = (L1V1Map) L1WorldMap.getInstance().getMap((short) map);
		switch (dir) {
		case 0:
			return (m.accessTile(x, y) & 2) > 0;
		case 1:
			return ((m.accessTile(x, y) & 2) > 0 && (m.accessTile(x, y - 1) & 1) > 0) || ((m.accessTile(x, y) & 1) > 0 && (m.accessTile(x + 1, y) & 2) > 0);
		case 2:
			return (m.accessTile(x, y) & 1) > 0;
		case 3:
			return ((m.accessTile(x, y + 1) & 2) > 0 && (m.accessTile(x, y + 1) & 1) > 0) || ((m.accessTile(x, y) & 1) > 0 && (m.accessTile(x + 1, y + 1) & 2) > 0);
		case 4:
			return (m.accessTile(x, y + 1) & 2) > 0;
		case 5:
			return ((m.accessTile(x, y + 1) & 2) > 0 && (m.accessTile(x - 1, y + 1) & 1) > 0) || ((m.accessTile(x - 1, y) & 1) > 0 && (m.accessTile(x - 1, y + 1) & 2) > 0);
		case 6:
			return (m.accessTile(x - 1, y) & 1) > 0;
		case 7:
			return ((m.accessTile(x, y) & 2) > 0 && (m.accessTile(x - 1, y - 1) & 1) > 0) || ((m.accessTile(x - 1, y) & 1) > 0 && (m.accessTile(x - 1, y) & 2) > 0);
		default:
			return false;
		}
	}

}

