package l1j.server.server.model.map;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.Config;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.types.Point;

public class L1MapCluster {
	private static final int CLUSTER_SIZE = 20;
	private int _startX, _startY, _clusterX, _clusterY;
	private final ConcurrentHashMap<Integer, L1Object>[][] _objects;

	@SuppressWarnings("unchecked")
	public L1MapCluster(int startX, int endX, int startY, int endY) {
		_startX = (startX / CLUSTER_SIZE) * CLUSTER_SIZE;
		int endPosX = ((endX + CLUSTER_SIZE - 1) / CLUSTER_SIZE) * CLUSTER_SIZE;
		_clusterX = (endPosX - _startX) / CLUSTER_SIZE;

		_startY = (startY / CLUSTER_SIZE) * CLUSTER_SIZE;
		int endPosY = ((endY + CLUSTER_SIZE - 1) / CLUSTER_SIZE) * CLUSTER_SIZE;
		_clusterY = (endPosY - _startY) / CLUSTER_SIZE;

		_objects = new ConcurrentHashMap[_clusterX][_clusterY];
		for (int i = 0; i < _clusterX; ++i) {
			for (int j = 0; j < _clusterY; ++j) {
				_objects[i][j] = new ConcurrentHashMap<Integer, L1Object>();
			}
		}
	}

	public int calculateClusterX(int x) {
		int clusterX = Math.max(x - _startX, 0) / CLUSTER_SIZE;
		if (clusterX >= _clusterX) {
			clusterX = _clusterX - 1;
		}
		return clusterX;
	}

	public int calculateClusterY(int y) {
		int clusterY = Math.max(y - _startY, 0) / CLUSTER_SIZE;
		if (clusterY >= _clusterY) {
			clusterY = _clusterY - 1;
		}
		return clusterY;
	}

	public void setObject(L1Object object) {
		setObject(object, object.getX(), object.getY());
	}

	public void setObject(L1Object object, int x, int y) {
		if (object == null) {
			return;
		}
		int clusterX = calculateClusterX(x);
		int clusterY = calculateClusterY(y);
		object.setClusterPos(clusterX, clusterY);
		_objects[clusterX][clusterY].put(object.getId(), object);
	}

	public void removeObject(L1Object object) {
		try {
			_objects[object.getClusterX()][object.getClusterY()].remove(object.getId());
			object.setClusterPos(-1, -1);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void onMove(L1Object object) {
		if (object == null) {
			return;
		}
		int clusterX = calculateClusterX(object.getX());
		int clusterY = calculateClusterY(object.getY());
		try {
			if (clusterX != object.getClusterX() || clusterY != object.getClusterY()) {
				_objects[object.getClusterX()][object.getClusterY()].remove(object.getId());
				_objects[clusterX][clusterY].put(object.getId(), object);
				object.setClusterPos(clusterX, clusterY);
			}
		} catch (Exception e) {}
	}

	public void onMove(L1Object object, int x, int y) {
		if (object == null) {
			return;
		}
		int clusterX = calculateClusterX(x);
		int clusterY = calculateClusterY(y);
		try {
			if (clusterX != object.getClusterX() || clusterY != object.getClusterY()) {
				_objects[object.getClusterX()][object.getClusterY()].remove(object.getId());
				_objects[clusterX][clusterY].put(object.getId(), object);
				object.setClusterPos(clusterX, clusterY);
			}
		} catch (Exception e) {}
	}

	public ArrayList<L1Object> getVisiblePoint(L1Location loc, int radius) {
		int mapId = loc.getMapId(); // 루프내에서 부르면(자) 무겁기 때문에
		ArrayList<L1Object> result = new ArrayList<L1Object>();
		int startX = calculateClusterX(loc.getX()) - 1;
		int startY = calculateClusterY(loc.getY()) - 1;
		for (int clusterX = startX; clusterX < startX + 3; ++clusterX) {
			if (clusterX < 0 || clusterX >= _clusterX) {
				continue;
			}
			for (int clusterY = startY; clusterY < startY + 3; ++clusterY) {
				if (clusterY < 0 || clusterY >= _clusterY) {
					continue;
				}
				for (L1Object target : _objects[clusterX][clusterY].values()) {
					if (mapId != target.getMapId()) {
						continue;
					}
					if (loc.getTileLineDistance(target.getLocation()) <= radius) {
						result.add(target);
					}
				}
			}
		}
		return result;
	}

	public ArrayList<L1Object> getVisibleObjects(L1Object object, int radius) {
		int mapId = object.getMapId();
		Point pt = object.getLocation();
		ArrayList<L1Object> result = new ArrayList<L1Object>();
		int objectClusterX = calculateClusterX(object.getX());
		int objectClusterY = calculateClusterY(object.getY());
		int startX = objectClusterX - 1;
		int startY = objectClusterY - 1;
		for (int clusterX = startX; clusterX < startX + 3; ++clusterX) {
			if (clusterX < 0 || clusterX >= _clusterX) {
				continue;
			}
			for (int clusterY = startY; clusterY < startY + 3; ++clusterY) {
				if (clusterY < 0 || clusterY >= _clusterY) {
					continue;
				}
				for (L1Object target : _objects[clusterX][clusterY].values()) {
					if (target.equals(object) || mapId != target.getMapId()) {
						continue;
					}
					if (radius == -1) {
						if (pt.isInScreen(target.getLocation())) {
							result.add(target);
						}
					} else if (radius == 0){
						if (pt.isSamePoint(target.getLocation())) {
							result.add(target);
						}
					} else {
						if (pt.getTileLineDistance(target.getLocation()) <= radius) {
							result.add(target);
						}
					}
				}
			}
		}
		return result;
	}

	public ArrayList<L1PcInstance> getVisiblePlayer(L1Object object, int radius) {
		int mapId = object.getMapId();
		Point pt = object.getLocation();
		ArrayList<L1PcInstance> result = new ArrayList<L1PcInstance>();
		int objectClusterX = calculateClusterX(object.getX());
		int objectClusterY = calculateClusterY(object.getY());
		int startX = objectClusterX - 1;
		int startY = objectClusterY - 1;
		for (int clusterX = startX; clusterX < startX + 3; ++clusterX) {
			if (clusterX < 0 || clusterX >= _clusterX) {
				continue;
			}
			for (int clusterY = startY; clusterY < startY + 3; ++clusterY) {
				if (clusterY < 0 || clusterY >= _clusterY) {
					continue;
				}
				for (L1Object target : _objects[clusterX][clusterY].values()) {
					if (!(target instanceof L1PcInstance)) {
						continue;
					}
					L1PcInstance targetPc = (L1PcInstance) target;
					if (targetPc.equals(object) || mapId != targetPc.getMapId()) {
						continue;
					}
					if (radius == -1) {
						if (pt.isInScreen(target.getLocation())) {
							result.add(targetPc);
						}
					} else if (radius == 0) {
						if (pt.isSamePoint(target.getLocation())) {
							result.add(targetPc);
						}
					} else {
						if (pt.getTileLineDistance(target.getLocation()) <= radius) {
							result.add(targetPc);
						}
					}
				}
			}
		}
		return result;
	}
	
	public ArrayList<L1PcInstance> getVisiblePlayerWithTarget(L1Object object, int radius) {
		int mapId = object.getMapId();
		Point pt = object.getLocation();
		ArrayList<L1PcInstance> result = new ArrayList<L1PcInstance>();
		int objectClusterX = calculateClusterX(object.getX());
		int objectClusterY = calculateClusterY(object.getY());
		int startX = objectClusterX - 1;
		int startY = objectClusterY - 1;
		for (int clusterX = startX; clusterX < startX + 3; ++clusterX) {
			if (clusterX < 0 || clusterX >= _clusterX) {
				continue;
			}
			for (int clusterY = startY; clusterY < startY + 3; ++clusterY) {
				if (clusterY < 0 || clusterY >= _clusterY) {
					continue;
				}
				for (L1Object target : _objects[clusterX][clusterY].values()) {
					if (!(target instanceof L1PcInstance)) {
						continue;
					}
					L1PcInstance targetPc = (L1PcInstance) target;
					if (mapId != targetPc.getMapId()) {
						continue;
					}
					if (radius == -1) {
						if (pt.isInScreen(target.getLocation())) {
							result.add(targetPc);
						}
					} else if (radius == 0) {
						if (pt.isSamePoint(target.getLocation())) {
							result.add(targetPc);
						}
					} else {
						if (pt.getTileLineDistance(target.getLocation()) <= radius) {
							result.add(targetPc);
						}
					}
				}
			}
		}
		return result;
	}

	public ArrayList<L1PcInstance> getVisiblePlayerExceptTargetSight(L1Object object, L1Object target) {
		Point objectPt = object.getLocation();
		Point targetPt = target.getLocation();
		ArrayList<L1PcInstance> result = new ArrayList<L1PcInstance>();
		int objectClusterX = calculateClusterX(object.getX());
		int objectClusterY = calculateClusterY(object.getY());
		int startX = objectClusterX - 1;
		int startY = objectClusterY - 1;
		for (int clusterX = startX; clusterX < startX + 3; ++clusterX) {
			if (clusterX < 0 || clusterX >= _clusterX) {
				continue;
			}
			for (int clusterY = startY; clusterY < startY + 3; ++clusterY) {
				if (clusterY < 0 || clusterY >= _clusterY) {
					continue;
				}
				for (L1Object targetObject : _objects[clusterX][clusterY].values()) {
					if (!(targetObject instanceof L1PcInstance)) {
						continue;
					}
					L1PcInstance element = (L1PcInstance) (targetObject);
					if (element.equals(object)) {
						continue;
					}
					if (Config.SERVER.PC_RECOGNIZE_RANGE == -1) {
						if (objectPt.isInScreen(element.getLocation())) {
							if (!targetPt.isInScreen(element.getLocation())) {
								result.add(element);
							}
						}
					} else {
						if (objectPt.getTileLineDistance(element.getLocation()) <= Config.SERVER.PC_RECOGNIZE_RANGE) {
							if (targetPt.getTileLineDistance(element.getLocation()) > Config.SERVER.PC_RECOGNIZE_RANGE) {
								result.add(element);
							}
						}
					}
				}
			}
		}
		return result;
	}
}

