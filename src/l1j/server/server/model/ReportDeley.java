package l1j.server.server.model;

import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.model.Instance.L1PcInstance;

public class ReportDeley extends TimerTask {
	private static Logger _log = Logger.getLogger(ReportDeley.class.getName());

	private final L1PcInstance _pc;

	public ReportDeley(L1PcInstance pc) {
		_pc = pc;
		_pc.setReport(false);
	}

	@Override
	public void run() {
		try {
			if (_pc.isDead()) {
				return;
			}
			_pc.setReport(true);
			cancel();
		} catch (Throwable e) {
			_log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}

}








