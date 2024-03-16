package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.datatables.ClanBlessBuffTable;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.datatables.HouseTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.pledge.S_BlessOfBloodPledgeUpdateNoti;
import l1j.server.server.templates.L1House;

public class A_PledgeBlessShuffle extends ProtoHandler {
	protected A_PledgeBlessShuffle(){}
	private A_PledgeBlessShuffle(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		L1Clan clan = _pc.getClan();
		if (clan == null || !_pc.getInventory().checkItem(L1ItemId.ADENA, 300000)) {
			return;
		}
		L1House house = HouseTable.getInstance().getHouseTable(clan.getHouseId());
		if (house == null || !house.isPurchaseBasement()) {// 지하 아지트
			return;
		}
		_pc.getInventory().consumeItem(L1ItemId.ADENA, 300000);
		clan.setEinhasadBlessBuff(0);
		clan.setBuffFirst(ClanBlessBuffTable.getRandomBuff(clan));
		clan.setBuffSecond(ClanBlessBuffTable.getRandomBuff(clan));
		clan.setBuffThird(ClanBlessBuffTable.getRandomBuff(clan));
		_pc.sendPackets(new S_BlessOfBloodPledgeUpdateNoti(clan), true);	
		ClanTable.getInstance().updateClan(clan);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PledgeBlessShuffle(data, client);
	}

}

