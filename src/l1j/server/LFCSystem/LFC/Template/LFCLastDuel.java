package l1j.server.LFCSystem.LFC.Template;

import java.util.ArrayList;
import java.util.logging.Level;

import l1j.server.LFCSystem.InstanceEnums.InstStatus;
import l1j.server.LFCSystem.LFC.Creator.LFCCreator;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class LFCLastDuel extends LFCObject {
	public static LFCLastDuel createInstance() {
		return new LFCLastDuel();
	}

	private ArrayList<L1NpcInstance> _boundary;

	public LFCLastDuel() {
		super();
		_boundary = new ArrayList<L1NpcInstance>();
	}

	@Override
	public void init() {
		L1NpcInstance boundary;
		for (int i = 32734; i < 32741; i++) {
			boundary = spawnBoundary(i, 32863, 0);
			if (boundary != null)
				_boundary.add(boundary);
		}
		super.init();
	}

	@Override
	public void run() {
		try {
			waitCount();
			for (L1NpcInstance boundary : _boundary)
				deleteNpc(boundary);
			_boundary.clear();
			_boundary = null;
			L1PcInstance red = _red.get(0);
			L1PcInstance blue = _blue.get(0);
			LFCCreator.setInstStatus(red, InstStatus.INST_USERSTATUS_LFC);
			LFCCreator.setInstStatus(blue, InstStatus.INST_USERSTATUS_LFC);
			while (_isrun) {
				Thread.sleep(1000);
				if (!checkSecond() || red == null || blue == null || red.isDead() || blue.isDead()
						|| red.getMapId() != getCopyMapId() || blue.getMapId() != getCopyMapId()) {
					close();
					return;
				}
			}
		} catch (Exception e) {
			close();
			e.printStackTrace();
			_log.log(Level.SEVERE, "L1LfcLastDuel", e);
		}
		return;
	}

	@Override
	public void close() {
		super.close();
	}

	@Override
	public String getName() {
		return "LFCLastDuel";
	}
}

