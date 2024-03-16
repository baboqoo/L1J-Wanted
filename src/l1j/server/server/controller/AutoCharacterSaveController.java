package l1j.server.server.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.RepeatTask;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

/**
 * 주기적 캐릭터 정보 저장 컨트롤러
 * @author LinOffice
 */
public class AutoCharacterSaveController extends RepeatTask {
	private static Logger _log = Logger.getLogger(AutoCharacterSaveController.class.getName());
	
	public AutoCharacterSaveController(int interval) {
		super(interval);
	}
	
	@Override
	public void execute() {
		try {
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc == null || pc.getNetConnection() == null || pc.getNetConnection().isCharRestart()) {
					continue;
				}
				try {
					pc.save();
					pc.saveInventory();
				} catch(Exception e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
			}
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	

}

