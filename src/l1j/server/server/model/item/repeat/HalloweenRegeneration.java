package l1j.server.server.model.item.repeat;

import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.RepeatTask;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;

public class HalloweenRegeneration extends RepeatTask {
	private static Logger _log = Logger.getLogger(HalloweenRegeneration.class.getName());

	private final L1PcInstance _pc;

	public HalloweenRegeneration(L1PcInstance pc, long interval) {
		super(interval);
		_pc = pc;
	}

	@Override
	public void execute() {
		try {
			if (_pc == null) {
				return;
			}
			if (!_pc.isDead()) {
				regenItem();
			}
		} catch (Throwable e) {
			_log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}

	private static final S_ServerMessage STORE_MESSAGE = new S_ServerMessage(403, "$4324");
	public void regenItem() {		
		_pc.getInventory().storeItem(410000, 1);// 할로윈 호박 파이
		_pc.sendPackets(STORE_MESSAGE);
	}	
}

