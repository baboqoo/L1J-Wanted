package l1j.server.server.controller;

import l1j.server.server.datatables.RevengeTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.StringUtil;

/**
 * 복수 시스템 추적 타이머
 * @author LinOffice
 */
public class RevengePursuitController implements Runnable {
	private L1PcInstance _pc;
	public RevengePursuitController(L1PcInstance pc) {
		_pc = pc;
	}
	
	@Override
	public void run() {
		try {
			if (_pc == null || _pc.getNetConnection() == null || _pc.isPrivateShop() || _pc.isAutoClanjoin()) {
				return;
			}
			if (StringUtil.isNullOrEmpty(_pc.getRevengeTarget())) {
				return;
			}
			RevengeTable.getInstance().endTargetPursuit(_pc, _pc.getRevengeTarget());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}

