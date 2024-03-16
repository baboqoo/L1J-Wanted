package l1j.server.server.model.trap;

import l1j.server.server.construct.L1RegionStatus;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.poison.L1DamagePoison;
import l1j.server.server.model.poison.L1ParalysisPoison;
import l1j.server.server.model.poison.L1SilencePoison;
import l1j.server.server.storage.TrapStorage;

public class L1PoisonTrap extends L1Trap {
	private final String _type;
	private final int _delay;
	private final int _time;
	private final int _damage;

	public L1PoisonTrap(TrapStorage storage) {
		super(storage);
		_type	= storage.getString("poisonType");
		_delay	= storage.getInt("poisonDelay");
		_time	= storage.getInt("poisonTime");
		_damage	= storage.getInt("poisonDamage");
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
		if (_type.equals("d")) {
			if (trodFrom.getRegion() != L1RegionStatus.SAFETY) {
				L1DamagePoison.doInfection(trodFrom, trodFrom, _time, _damage, false);
			}
		} else if (_type.equals("s")) {
			L1SilencePoison.doInfection(trodFrom); //침묵독
		} else if (_type.equals("p")) {
			L1ParalysisPoison.doInfection(trodFrom, _delay, _time);
		}
	}
}

