package l1j.server.server.controller.action;

import l1j.server.Config;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;

/**
 * 혈맹 버프 컨트롤러
 * @author LinOffice
 */
public class PledgeBuff implements ControllerInterface {
	private static class newInstance {
		public static final PledgeBuff INSTANCE = new PledgeBuff();
	}
	public static PledgeBuff getInstance() {
		return newInstance.INSTANCE;
	}
	private PledgeBuff(){}

	@Override
	public void execute() {
		for (L1Clan pledge : L1World.getInstance().getAllClans()) {
			if (pledge == null || pledge.isBot()) {
				continue;
			}
			int onlineValue = pledge.getOnlineClanMember().length;
			if (!pledge.isClanBuff() && onlineValue >= Config.PLEDGE.CLAN_BUFF_ACTIVE_MEMBER_COUNT) {
				pledge.setClanBuff(true);
			} else if (pledge.isClanBuff() && onlineValue < Config.PLEDGE.CLAN_BUFF_ACTIVE_MEMBER_COUNT) {
				pledge.setClanBuff(false);
			}
		}
	}
	
	@Override
	public void execute(l1j.server.server.model.Instance.L1PcInstance pc) {
	}

}

