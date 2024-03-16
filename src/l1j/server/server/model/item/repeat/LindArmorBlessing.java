package l1j.server.server.model.item.repeat;

import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_EffectLocation;

public class LindArmorBlessing extends TimerTask {
	private static Logger _log = Logger.getLogger(LindArmorBlessing.class.getName());
	private final L1PcInstance _pc;
	public LindArmorBlessing(L1PcInstance pc) {
		_pc = pc;
	}

	@Override
	public void run() {
		try {
			if (_pc == null) {
				return;
			}
			if (!_pc.isDead()) {
				regen();
			}
		} catch (Throwable e) {
			_log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}

	public void regen() {
		_pc.broadcastPacketWithMe(new S_EffectLocation(_pc.getX(), _pc.getY(), 763), true);
	} 
}
