package l1j.server.server.controller.action;

import javolution.util.FastTable;
import l1j.server.server.model.Instance.L1ArrowInstance;
import l1j.server.server.utils.L1SpawnUtil;

/**
 * 화살 트랩 컨트롤러(말하는 섬 던전 2층)
 * @author LinOffice
 */
public class ArrowTrap implements ControllerInterface {
	private static class newInstance {
		public static final ArrowTrap INSTANCE = new ArrowTrap();
	}
	public static ArrowTrap getInstance() {
		return newInstance.INSTANCE;
	}
	
	private FastTable<L1ArrowInstance> arrowList;
	private ArrowTrap() {
		arrowList = L1SpawnUtil.ArrowSpawn();
	}
	
	@Override
	public void execute() {
		int size = 0;
		L1ArrowInstance[] list = null;
		try {
			synchronized (arrowList) {
				if ((size = arrowList.size()) > 0) {
					list = (L1ArrowInstance[]) arrowList.toArray(new L1ArrowInstance[size]);
				}
			}
			if (size > 0) {
				for (L1ArrowInstance arr : list) {
					if (arr == null || arr._destroyed) {
						remove(arr);
					}
					if (arr.getAction()) {
						arr.ai();
						arr.setAction(false);
					} else {
						arr.setAction(true);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			list = null;
		}
	}
	
	@Override
	public void execute(l1j.server.server.model.Instance.L1PcInstance pc) {
	}
	
	public void add(L1ArrowInstance npc) {
		synchronized (arrowList) {
			if (!arrowList.contains(npc)) {
				arrowList.add(npc);
			}
		}
	}

	public void remove(L1ArrowInstance npc) {
		synchronized (arrowList) {
			if (arrowList.contains(npc)) {
				arrowList.remove(npc);
			}
		}
	}

}

