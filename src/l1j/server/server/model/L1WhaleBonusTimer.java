package l1j.server.server.model;

import java.util.Arrays;
import java.util.List;

import l1j.server.server.model.Instance.L1PcInstance;

/**
 * 굶주린 고래상어 보너스 맵 제한 시간 타이머
 * @author LinOffice
 */
public class L1WhaleBonusTimer implements Runnable {
	public static final List<Integer> MAPS = Arrays.asList(new Integer[] {
			1601, 1650
	});
	
	L1PcInstance _owner;
	int _map_number;
	public L1WhaleBonusTimer(L1PcInstance owner, int map_number) {
		_owner		= owner;
		_map_number	= map_number;
	}
	
	@Override
	public void run() {
		try {
			if (_owner == null || _owner.getMapId() != _map_number) {
				return;
			}
			int[] loc = Getback.GetBack_Location(_owner);
			_owner.getTeleport().start(loc[0], loc[1], (short)loc[2], 5, true);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}

