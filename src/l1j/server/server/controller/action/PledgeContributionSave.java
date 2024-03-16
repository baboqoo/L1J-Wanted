package l1j.server.server.controller.action;

import java.util.Collection;

import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;

/**
 * 혈맹 공헌도 저장 컨트롤러
 * @author LinOffice
 */
public class PledgeContributionSave implements ControllerInterface {
	private static class newInstance {
		public static final PledgeContributionSave INSTANCE = new PledgeContributionSave();
	}
	public static PledgeContributionSave getInstance() {
		return newInstance.INSTANCE;
	}
	private PledgeContributionSave(){}

	@Override
	public void execute() {
		Collection<L1Clan> list = L1World.getInstance().getAllClans();
		if (list == null || list.isEmpty()) {
			return;
		}
		ClanTable.getInstance().updateContribution(list);
	}
	
	@Override
	public void execute(l1j.server.server.model.Instance.L1PcInstance pc) {
	}

}

