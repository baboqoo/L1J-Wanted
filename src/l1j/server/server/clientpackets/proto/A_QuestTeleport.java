package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.GameSystem.beginnerquest.bean.L1QuestProgress;
import l1j.server.common.bin.quest.QuestT;
import l1j.server.server.GameClient;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.quest.S_QuestTeleport;

public class A_QuestTeleport extends ProtoHandler {
	protected A_QuestTeleport(){}
	private A_QuestTeleport(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int _id;

	@Override
	protected void doWork() throws Exception {
		if (!Config.QUEST.BEGINNER_QUEST_ACTIVE || _pc == null) {
			return;
		}
		readP(1);// 0x08
		_id							= readBit();
		L1QuestProgress progress	= _pc.getQuest().getQuestProgress(_id);
		if (progress == null) {
			_pc.sendPackets(new S_QuestTeleport(S_QuestTeleport.eResultCode.FAIL, _id), true);
			System.out.println(String.format("[A_QuestTeleport] NOT_REVEAL_QUEST : QUESTID(%d), CHAR_NAME(%s)", _id, _pc.getName()));
			return;
		}
		
		QuestT.TeleportT tel	= progress.getBin().get_Teleport();
		if (tel == null) {
			_pc.sendPackets(new S_QuestTeleport(S_QuestTeleport.eResultCode.FAIL, _id), true);
			System.out.println(String.format("[A_QuestTeleport] BIN_TEL_INFO_EMPTY : QUESTID(%d), CHAR_NAME(%s)", _id, _pc.getName()));
			return;
		}
		
		if (tel.get_NoTeleport()) {
			return;
		}
		
		if (_pc.isNotTeleport()) {
			_pc.sendPackets(new S_QuestTeleport(S_QuestTeleport.eResultCode.FAIL_CANT_TELEPORT_NOW, _id), true);
			return;
		}
		
		if (_pc.getMap().getInter() != null) {
			_pc.sendPackets(new S_QuestTeleport(S_QuestTeleport.eResultCode.FAIL_WRONG_LOCATION, _id), true);
			return;
		}
		
		if (tel.get_Cost() > 0 && !_pc.getInventory().consumeItem(L1ItemId.ADENA, tel.get_Cost())) {
			_pc.sendPackets(new S_QuestTeleport(S_QuestTeleport.eResultCode.FAIL_NOT_ENOUGH_ADENA, _id), true);
			return;
		}
		
		_pc.getTeleport().start(tel.get_X(), tel.get_Y(),(short) tel.get_MapID(), _pc.getMoveState().getHeading(), true);
		_pc.sendPackets(S_QuestTeleport.getTeleportPck(_id));
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_QuestTeleport(data, client);
	}

}

