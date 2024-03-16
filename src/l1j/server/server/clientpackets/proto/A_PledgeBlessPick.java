package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.ClanBlessBuffTable;
import l1j.server.server.datatables.ClanBlessBuffTable.ClanBuff;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.datatables.HouseTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.pledge.S_BlessOfBloodPledgeUpdateNoti;
import l1j.server.server.templates.L1House;

public class A_PledgeBlessPick extends ProtoHandler {
	protected A_PledgeBlessPick(){}
	private A_PledgeBlessPick(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		L1Clan clan = _pc.getClan();
		if (clan == null) {
			return;
		}
		L1House house = HouseTable.getInstance().getHouseTable(clan.getHouseId());
		if (house == null || !house.isPurchaseBasement()) {
			return;// 지하 아지트
		}
		
		readP(1);// 0x0a
		readBit();
		
		readP(1);// 0x08
		int world_id = readBit();
		
		readP(1);// 0x10
		A_PledgeBlessPick.eState world_state = A_PledgeBlessPick.eState.fromInt(readC());
		
		switch(world_state){
		case Idle:
			clan.setEinhasadBlessBuff(world_id);
			_pc.sendPackets(new S_BlessOfBloodPledgeUpdateNoti(clan), true);
			ClanTable.getInstance().updateClan(clan);
			break;
		case Selected:
			if (_pc.getMap().isEscapable()) {
				_pc.sendPackets(L1ServerMessage.sm563);
				_pc.sendPackets(S_Paralysis.TELEPORT_UNLOCK);
				break;
			}
			ClanBuff buff = ClanBlessBuffTable.getBuffList(world_id);
			if (buff != null && _pc.getInventory().consumeItem(L1ItemId.ADENA, 1000)) {
				_pc.sendPackets(S_Paralysis.TELEPORT_UNLOCK);
				_pc.getTeleport().start(buff.teleportX, buff.teleportY, (short) buff.teleportM, _pc.getMoveState().getHeading(), true);
			}
			break;
		case Unselected:
			if (_pc.getInventory().consumeItem(L1ItemId.ADENA, 500000)) {
				clan.setEinhasadBlessBuff(world_id);
				_pc.sendPackets(new S_BlessOfBloodPledgeUpdateNoti(clan), true);
				ClanTable.getInstance().updateClan(clan);
			}
			break;
		}
	}
	
	public enum eState{
		Idle(1),
		Selected(2),
		Unselected(3),
		;
		private int value;
		eState(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eState v){
			return value == v.value;
		}
		public static eState fromInt(int i){
			switch(i){
			case 1:
				return Idle;
			case 2:
				return Selected;
			case 3:
				return Unselected;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eState, %d", i));
			}
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PledgeBlessPick(data, client);
	}

}

