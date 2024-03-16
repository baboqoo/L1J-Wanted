package l1j.server.server.model;

import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.templates.L1BookMark;
import l1j.server.server.types.Point;
import l1j.server.server.utils.CommonUtil;

public class L1Location extends Point {
	protected L1Map _map = L1Map.newNull();

	public L1Location() {
		super();
	}

	public L1Location(L1Location loc) {
		this(loc._x, loc._y, loc._map);
	}

	public L1Location(int x, int y, int mapId) {
		super(x, y);
		setMap(mapId);
	}

	public L1Location(int x, int y, L1Map map) {
		super(x, y);
		_map = map;
	}

	public L1Location(Point pt, int mapId) {
		super(pt);
		setMap(mapId);
	}

	public L1Location(Point pt, L1Map map) {
		super(pt);
		_map = map;
	}

	public void set(L1Location loc) {
		_map = loc._map;
		_x = loc._x;
		_y = loc._y;
	}

	public void set(int x, int y, int mapId) {
		set(x, y);
		setMap(mapId);
	}

	public void set(int x, int y, L1Map map) {
		set(x, y);
		_map = map;
	}

	public void set(Point pt, int mapId) {
		set(pt);
		setMap(mapId);
	}

	public void set(Point pt, L1Map map) {
		set(pt);
		_map = map;
	}

	public L1Map getMap() {
		return _map;
	}

	public int getMapId() {
		return _map.getId();
	}

	public void setMap(L1Map map) {
		_map = map;
	}

	public void setMap(int mapId) {
		_map = L1WorldMap.getInstance().getMap((short) mapId);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof L1Location)) {
			return false;
		}
		L1Location loc = (L1Location) obj;
		return (this.getMap() == loc.getMap()) && (this.getX() == loc.getX()) && (this.getY() == loc.getY());
	}

	@Override
	public int hashCode() {
		return 7 * _map.getId() + super.hashCode();
	}

	@Override
	public String toString() {
		return String.format("(%d, %d) on %d", _x, _y, _map.getId());
	}

	public static L1Location randomBookmarkLocation(L1BookMark bookm, boolean isRandomTeleport) {
		L1Location newLocation = new L1Location();
		L1Location baseLocation = new L1Location();
		baseLocation.set(bookm.getLocX(), bookm.getLocY(), bookm.getMapId());
		int randomX, randomY = 0;
		int newx = bookm.getLocX() - (bookm.getRandomX() >> 1);
		int newy = bookm.getLocY() - (bookm.getRandomY() >> 1);
		short mapId = (short) bookm.getMapId();
		L1Map map = baseLocation.getMap();
		while (true) {
			randomX = CommonUtil.random(bookm.getRandomX()) + 1;
			randomY = CommonUtil.random(bookm.getRandomY()) + 1;
			newx += randomX;
			newy += randomY;
			newLocation.set(newx, newy, mapId);
			if (isRandomTeleport && isNotEnableLoc(newx, newy, mapId)) {
				continue;
			}

			if (((!isRandomTeleport) || ((!L1CastleLocation.checkInAllWarArea(newx, newy, mapId)) && (!L1HouseLocation.isInHouse(newx, newy, mapId)))) && (map.isInMap(newx, newy)) && (map.isPassable(newx, newy))) {
				break;
			}
		}
		return newLocation;
	}

	public L1Location randomLocation(int max, boolean isRandomTeleport) {
		return randomLocation(0, max, isRandomTeleport);
	}

	public L1Location randomLocation(int min, int max, boolean isRandomTeleport) {
		return L1Location.randomLocation(this, min, max, isRandomTeleport);
	}

	public static L1Location randomLocation(L1Location baseLocation, int min, int max, boolean isRandomTeleport) {
		if (min > max) {
			//throw new IllegalArgumentException("min > max가 되는 인수는 무효");
			throw new IllegalArgumentException("Arguments where min > max are invalid");
		}
		if (max <= 0) {
			return new L1Location(baseLocation);
		}
		if (min < 0) {
			min = 0;
		}

		L1Location newLocation = new L1Location();
		int newX = 0, newY = 0;
		int locX = baseLocation.getX();
		int locY = baseLocation.getY();
		short mapId = (short) baseLocation.getMapId();
		L1Map map = baseLocation.getMap();
		newLocation.setMap(map);
		int locX1 = locX - max;
		int locX2 = locX + max;
		int locY1 = locY - max;
		int locY2 = locY + max;

		int mapX1 = map.getX();
		int mapX2 = mapX1 + map.getWidth();
		int mapY1 = map.getY();
		int mapY2 = mapY1 + map.getHeight();

		if (locX1 < mapX1) {
			locX1 = mapX1;
		}
		if (locX2 > mapX2) {
			locX2 = mapX2;
		}
		if (locY1 < mapY1) {
			locY1 = mapY1;
		}
		if (locY2 > mapY2) {
			locY2 = mapY2;
		}

		int diffX = locX2 - locX1;
		int diffY = locY2 - locY1;

		int trial = 0;
		int amax = (int) Math.pow(1 + (max << 1), 2);
		int amin = (min == 0) ? 0 : (int) Math.pow(1 + ((min - 1) << 1), 2);
		int trialLimit = 40 * amax / (amax - amin);

		while (true) {
			if (trial >= trialLimit) {
				newLocation.set(locX, locY);
				break;
			}
			trial++;

			try {
				newX = locX1 + CommonUtil.random(diffX + 1);
				newY = locY1 + CommonUtil.random(diffY + 1);
			} catch (Exception e) {
//				System.out.println("스폰오류  : XX :: "+ newX +  " YY : "+ newY);
			}
			newLocation.set(newX, newY);

			if (baseLocation.getTileLineDistance(newLocation) < min) {
				continue;
			}
			if (isRandomTeleport) {
				if (L1CastleLocation.checkInAllWarArea(newX, newY, mapId)) {
					continue;
				}
				if (L1HouseLocation.isInHouse(newX, newY, mapId)) {
					continue;
				}
				if (isNotEnableLoc(newX, newY, mapId)) {
					continue;
				}
			}
			if (map.isInMap(newX, newY) && map.isPassable(newX, newY)) {
				break;
			}
		}
		return newLocation;
	}

	public static L1Location randomLocation(int x, int y, L1Map maps, short mapid, int min, int max, boolean isRandomTeleport) {
		if (min > max) {
			//throw new IllegalArgumentException("min > max가 되는 인수는 무효");
			throw new IllegalArgumentException("Arguments where min > max are invalid");
		}
		if (max <= 0) {
			return new L1Location(x, y, mapid);
		}
		if (min < 0) {
			min = 0;
		}

		L1Location newLocation = new L1Location();
		int newX = 0, newY = 0;
		int locX = x;
		int locY = y;
		short mapId = (short) mapid;
		L1Map map = maps;

		newLocation.setMap(map);
		int locX1 = locX - max;
		int locX2 = locX + max;
		int locY1 = locY - max;
		int locY2 = locY + max;

		// map 범위
		int mapX1 = map.getX();
		int mapX2 = mapX1 + map.getWidth();
		int mapY1 = map.getY();
		int mapY2 = mapY1 + map.getHeight();

		// 최대에서도 맵의 범위내까지 보정
		if (locX1 < mapX1) {
			locX1 = mapX1;
		}
		if (locX2 > mapX2) {
			locX2 = mapX2;
		}
		if (locY1 < mapY1) {
			locY1 = mapY1;
		}
		if (locY2 > mapY2) {
			locY2 = mapY2;
		}

		int diffX = locX2 - locX1; // x방향
		int diffY = locY2 - locY1; // y방향

		int trial = 0;
		// 시행 회수를 범위 최소치에 의해 주기 때문에(위해)의 계산
		int amax = (int) Math.pow(1 + (max << 1), 2);
		int amin = (min == 0) ? 0 : (int) Math.pow(1 + ((min - 1) << 1), 2);
		int trialLimit = 40 * amax / (amax - amin);

		while (true) {
			if (trial >= trialLimit) {
				newLocation.set(locX, locY);
				break;
			}
			trial++;

			newX = locX1 + CommonUtil.random(diffX + 1);
			newY = locY1 + CommonUtil.random(diffY + 1);

			newLocation.set(newX, newY);

			// if (x.getTileLineDistance(newLocation) < min) {
			// continue;

			// }
			int a = Math.max(Math.abs(newLocation.getX() - x), Math.abs(newLocation.getY() - y));
			if (a < min) {
				continue;
			}
			if (isRandomTeleport) { // 랜덤 텔레포트의 경우
				if (L1CastleLocation.checkInAllWarArea(newX, newY, mapId)) {
					continue;// 몇개의 성에리어
				}
				if (L1HouseLocation.isInHouse(newX, newY, mapId)) {
					continue;
				}
				if (isNotEnableLoc(newX, newY, mapId)) {
					continue;
				}
			}
			if (map.isInMap(newX, newY) && map.isPassable(newX, newY)) {
				break;
			}
		}
		return newLocation;
	}
	
	/**
	 * 맵내 랜덤한 좌표를 반환한다.
	 * @param baseMap
	 * @return L1Location
	 */
	public static L1Location randomLocation(L1Map baseMap) {
		L1Location newLocation = new L1Location();
		short mapId = (short) baseMap.getId();
		L1Map map = baseMap;
		newLocation.setMap(map);
		int newX = 0, newY = 0;
		int StartX = map.getX(), StartY = map.getY();
		int diffX = map.getWidth(), diffY = map.getHeight();
		while (true) {
			newX = StartX + CommonUtil.random(diffX + 1);
			newY = StartY + CommonUtil.random(diffY + 1);
			newLocation.set(newX, newY);
			if (L1CastleLocation.checkInAllWarArea(newX, newY, mapId)) {
				continue;
			}
			if (L1HouseLocation.isInHouse(newX, newY, mapId)) {
				continue;
			}
			if (isNotEnableLoc(newX, newY, mapId)) {
				continue;
			}
			if (map.isInMap(newX, newY) && map.isPassable(newX, newY)) {
				break;
			}
		}
		return newLocation;
	}
	
	static boolean isNotEnableLoc(int x, int y, short mapId) {
		if (x >= 32704 && x <= 32835 && y >= 33110 && y <= 33234 && mapId == 4) {// 샌드웜지역
			return true;
		}
		if (x >= 33472 && x <= 33536 && y >= 32838 && y <= 32876 && mapId == 4) {// 버경장
			return true;
		}
		if (mapId == 30 && !(x >= 32718 && x <= 32818 && y >= 32718 && y <= 32818)) {// 용의계곡1층
			return true;
		}
		if (mapId == 492 && !(x >= 32738 && x <= 32812 && y >= 32916 && y <= 32999)) {// 아투바오크은신처2층
			return true;
		}
		if (mapId == 285 && !(x >= 32657 && x <= 32762 && y >= 32778 && y <= 32880)) {// 상아탑7층
			return true;
		}
		if (mapId == 15883 && !(x >= 32736 && x <= 32924 && y >= 32732 && y <= 32928)) {// 여왕개미은신처
			return true;
		}
		return false;
	}
	
}

