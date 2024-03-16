package l1j.server.server.clientpackets.proto;

import java.sql.Timestamp;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.serverpackets.shop.S_ShopSellPledge;
import l1j.server.server.utils.CommonUtil;

public class A_PledgeMerchantGoodsList extends ProtoHandler {
	protected A_PledgeMerchantGoodsList(){}
	private A_PledgeMerchantGoodsList(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		L1Clan clan = _pc.getClan();
		if (clan == null || clan.getClanId() == Config.PLEDGE.BEGINNER_PLEDGE_ID || clan.isBot()) {
			return;// 제외시킬 혈맹
		}
		if (CommonUtil.isDayResetTimeCheck(clan.getClanVowPotionTime())) {// 맹세의 영약 1일 시간 체크
			if (clan.getClanVowPotionTime() != null) {
				clan.getClanVowPotionTime().setTime(System.currentTimeMillis());
			} else {
				clan.setClanVowPotionTime(new Timestamp(System.currentTimeMillis()));
			}
			clan.setClanVowPotionCount(0);
			final String sql = String.format("UPDATE clan_data SET vowTime=now(), vowCount=0 WHERE clan_id='%d'", clan.getClanId());
			ClanTable.getInstance().updateClanDungeonTime(sql);
		}
		_pc.sendPackets(new S_ShopSellPledge(_pc), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PledgeMerchantGoodsList(data, client);
	}

}

