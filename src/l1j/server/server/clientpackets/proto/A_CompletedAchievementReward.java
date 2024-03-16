package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.MonsterBookTable;
import l1j.server.server.model.L1Cooking;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_CompletedAchievementReward;
import l1j.server.server.serverpackets.message.S_ServerMessage;

public class A_CompletedAchievementReward extends ProtoHandler {
	protected A_CompletedAchievementReward(){}
	private A_CompletedAchievementReward(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);
		int monNum = read4(read_size());
		int value = -1;
		if (monNum == 1) {
			value = 1;
		} else if (monNum == 2) {
			value = 2;
		} else if (monNum >= 3 && monNum <= 2130) {
			value = monNum % 3;// 699번 까지
		} else if (monNum >= 2131) {
			value = (monNum - 2128) % 3;// 699번 이상부터 3번부터 시작
		}
		if (value < 0) {
			return;
		}
		long exp = 0;
		L1ItemInstance item = null;
		switch(value){// 도감 1~3단계별로 아이템지급
		case 1:
			exp = ExpTable.getExpFromLevelAndPercent(_pc.getLevel(), 52, 1);
		    if (_pc.getLevel() >= 1 && (exp + _pc.getExp() > ExpTable.getExpByLevel(_pc.getLevel() + 1))) {
		    	exp = (ExpTable.getExpByLevel(_pc.getLevel() + 1) - _pc.getExp());
			}
		    _pc.addExp(exp);
			L1Cooking.eatCooking(_pc, L1SkillId.DOGAM_BUFF, 1800);
			break;
		case 2:
			exp = ExpTable.getExpFromLevelAndPercent(_pc.getLevel(), 52, 13);
		    if (_pc.getLevel() >= 1 && (exp + _pc.getExp() > ExpTable.getExpByLevel(_pc.getLevel() + 1))) {
		    	exp = (ExpTable.getExpByLevel(_pc.getLevel() + 1) - _pc.getExp());
			}
		    _pc.addExp(exp);
			if (_pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(L1ItemId.ADENA), 5000) != L1Inventory.OK) return;
			item = _pc.getInventory().storeItem(L1ItemId.ADENA, 5000);
			//_pc.sendPackets(new S_ServerMessage(403, item.getDescKr() + " (5000)"), true);
			_pc.sendPackets(new S_ServerMessage(403, item.getDesc() + " (5000)"), true);
			break;
		case 0:
			exp = ExpTable.getExpFromLevelAndPercent(_pc.getLevel(), 52, 64);
		    if (_pc.getLevel() >= 1 && (exp + _pc.getExp() > ExpTable.getExpByLevel(_pc.getLevel() + 1))) {
		    	exp = (ExpTable.getExpByLevel(_pc.getLevel() + 1) - _pc.getExp());
			}
		    _pc.addExp(exp);
			if (_pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(5548), 1) != L1Inventory.OK) return;
			item = _pc.getInventory().storeItem(5548, 1);
			//_pc.sendPackets(new S_ServerMessage(403, item.getDescKr() + " (1)"), true);
			_pc.sendPackets(new S_ServerMessage(403, item.getDesc() + " (1)"), true);
			break;
		}
		_pc.sendPackets(new S_CompletedAchievementReward(S_CompletedAchievementReward.eResultCode.REQUEST_REWARD_SUCCESS, monNum), true);
		MonsterBookTable book = MonsterBookTable.getInstace();
		book.setMonQuest(_pc.getId(), monNum, 1);
		book.saveMonsterQuest(_pc.getId());
		_pc.send_effect(3944);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_CompletedAchievementReward(data, client);
	}

}

