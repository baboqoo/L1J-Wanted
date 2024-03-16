package l1j.server.server.model.monitor;

import l1j.server.server.model.Instance.L1PcInstance;

public class L1PcAutoUpdate extends L1PcMonitor {

	public L1PcAutoUpdate(L1PcInstance owner) {
		super(owner);
	}

	@Override
	public void execTask() {
		int x = _owner.getX(), y = _owner.getY();
		short mapId = _owner.getMapId();
		if ((x >= 33627 && x <= 33636 && y >= 32673 && y <= 32682 && mapId == 15482)
				|| (x >= 32805 && x <= 32815 && y >= 32228 && y <= 32238 && mapId == 15483)
				|| (x >= 32732 && x <= 32741 && y >= 32817 && y <= 32826 && mapId == 15484)
				|| (x >= 33162 && x <= 33175 && y >= 32773 && y <= 32785 && mapId == 4)) {
		    if (_owner.isInvisble()) {
		    	_owner.delInvis();
		    }
		}
		if (_owner.getTeleport().isTeleport()) {// 텔레포트 중에는 오브젝트 업데이트를 진행하지 않는다(충돌방지)
			return;
		}
		_owner.updateObjectMonitor();
	}
	
}

