package l1j.server.server.controller.action;

import java.util.logging.Logger;

import l1j.server.server.datatables.UBTable;
import l1j.server.server.model.L1UltimateBattle;
import l1j.server.server.model.Instance.L1PcInstance;

/**
 * 콜로세움 시간 체크 컨트롤러
 * @author LinOffice
 */
public class UltimateBattleTime implements ControllerInterface {
	private static Logger _log = Logger.getLogger(UltimateBattleTime.class.getName());
	private static class newInstance {
		public static final UltimateBattleTime INSTANCE = new UltimateBattleTime();
	}
	public static UltimateBattleTime getInstance() {
		return newInstance.INSTANCE;
	}
	private UltimateBattleTime(){}
	
	public boolean _coloseumRun = false;

	@Override
	public void execute() {
		try {
			checkUbTime();// UB개시 시간을 체크
		} catch (Exception e1) {
			_log.warning(e1.getMessage());
		}
	}
	
	@Override
	public void execute(L1PcInstance pc) {
	}
	
	void checkUbTime() {
		for (L1UltimateBattle ub : UBTable.getInstance().getAllUb()) {
			if (ub.checkUbTime() && !ub.isActive()) {
				ub.start();
			}
		}
	}

}

