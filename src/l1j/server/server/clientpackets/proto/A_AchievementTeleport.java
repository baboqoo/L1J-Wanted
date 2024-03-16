package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.MonsterBookTable;
import l1j.server.server.serverpackets.message.S_ServerMessage;

public class A_AchievementTeleport extends ProtoHandler {
	protected A_AchievementTeleport(){}
	private A_AchievementTeleport(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost() || _pc.isNotTeleport()) {
			return;
		}
		if (!_pc.getMap().isEscapable() || _client.isInterServer()) {
			_pc.sendPackets(l1j.server.server.serverpackets.S_Paralysis.TELEPORT_UNLOCK);
			_pc.sendPackets(L1ServerMessage.sm4726);
			return;
		}
		readP(1);
		int monsternumber = read4(read_size()) / 3 + 1;
		if (monsternumber >= 578 && monsternumber <= 618) {
			monsternumber -= 10;// 568 ~ 608
		} else if (monsternumber >= 620 && monsternumber <= 709) {
			monsternumber -= 11;// 609 ~ 698
		} else if (monsternumber >= 711) {
			monsternumber -= 12;// 699 ~
		}
		MonsterBookTable mt	= MonsterBookTable.getInstace();
		int mn				= mt.getMonNum(monsternumber);
		if (mn != 0) {
			int itemId		= mt.getMarterial(monsternumber);
			String itemName	= ItemTable.getInstance().findDescKrByItemId(itemId);
			if (itemName != null) {
				int locx	= mt.getLocX(monsternumber);
				int locy	= mt.getLocY(monsternumber);
				int mapid	= mt.getMapId(monsternumber);
				if (_pc.getInventory().consumeItem(itemId, 1)) {
					_pc.getTeleport().start(locx, locy, (short) mapid, _pc.getMoveState().getHeading(), true);
				} else {
					_pc.sendPackets(new S_ServerMessage(4692, itemName), true);
				}
			}
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_AchievementTeleport(data, client);
	}

}

