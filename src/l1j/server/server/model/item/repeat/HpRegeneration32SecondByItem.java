package l1j.server.server.model.item.repeat;

import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.RepeatTask;
import l1j.server.server.model.Instance.L1PcInstance;

public class HpRegeneration32SecondByItem extends RepeatTask {
	private static Logger _log = Logger.getLogger(HpRegeneration32SecondByItem.class.getName());

	private final L1PcInstance _pc;
	private final int _effectId;

	public HpRegeneration32SecondByItem(L1PcInstance pc, int effectId, long interval) {
		super(interval);
		_pc			= pc;
		_effectId	= effectId;
	}

	@Override
	public void execute() {
		try {
			if (_pc == null) {
				return;
			}
			if (_pc.isDead()) {
				return;
			}
			regenHp();
		} catch (Throwable e) {
			_log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}

	public void regenHp() {
		int newHp = _pc.getCurrentHp() + _pc.getHpRegen32SecondByItemValue() + _pc.getAbility().getAbsoluteRegenHp();
		_pc.setCurrentHp(newHp);
		if (_effectId > 0) {
			_pc.send_effect(_effectId);
		}
	}
}

