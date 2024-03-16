package l1j.server.server.model.item.repeat;

import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.model.Instance.L1PcInstance;

public class ElfArmorBlessing extends TimerTask {
	private static Logger _log = Logger.getLogger(ElfArmorBlessing.class.getName());
	private final L1PcInstance _pc;
	public ElfArmorBlessing(L1PcInstance pc) {
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
		_pc.send_effect(13428);
	} 
}
