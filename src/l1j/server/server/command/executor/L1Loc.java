package l1j.server.server.command.executor;

import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1Loc implements L1CommandExecutor {
	private static Logger _log = Logger.getLogger(L1Loc.class.getName());

	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Loc();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1Loc() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (pc == null) {
				return false;
			}
			int locx = pc.getX();
			int locy = pc.getY();
			short mapid = pc.getMapId();
			int gab = L1WorldMap.getInstance(). getMap(mapid).getOriginalTile(locx, locy);
			//String msg = String.format("\\aD좌표 (%d, %d, %d) %d", locx, locy, mapid, gab);
			String msg = String.format("\\aDCoordinates (%d, %d, %d) %d", locx, locy, mapid, gab);
			pc.sendPackets(new S_SystemMessage(msg, true), true);
			return true;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			return false;
		}
	}
}

