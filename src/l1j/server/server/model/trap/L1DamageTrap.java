package l1j.server.server.model.trap;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.storage.TrapStorage;
import l1j.server.server.utils.Dice;

public class L1DamageTrap extends L1Trap {
	private final Dice _dice;
	private final int _base;
	private final int _diceCount;

	public L1DamageTrap(TrapStorage storage) {
		super(storage);
		_dice		= new Dice(storage.getInt("dice"));
		_base		= storage.getInt("base");
		_diceCount	= storage.getInt("diceCount");
	}

	@Override
	public void onTrod(L1PcInstance trodFrom, L1Object trapObj) {
		if (trodFrom.getMapId() >= 12852 && trodFrom.getMapId() <= 12862) {
			L1Map map = L1WorldMap.getInstance().getMap(trodFrom.getMapId());
			if (map.isSafetyZone(trodFrom.getX(), trodFrom.getY())) {
				return;
			}
		}
		sendEffect(trapObj);
		int dmg = _dice.roll(_diceCount) + _base;
		trodFrom.receiveDamage(trodFrom, dmg);
	}
}
