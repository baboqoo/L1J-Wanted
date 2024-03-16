package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.GameSystem.huntingquest.user.HuntingQuestUser;
import l1j.server.GameSystem.huntingquest.user.HuntingQuestUserTemp;
import l1j.server.common.data.eMonsterBookV2RewardGrade;
import l1j.server.server.GameClient;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.exp.L1ExpPlayer;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_ExpBoostingInfo;
import l1j.server.server.serverpackets.einhasad.S_RestExpInfoNoti;
import l1j.server.server.serverpackets.einhasad.S_RestGaugeChargeNoti;
import l1j.server.server.serverpackets.huntingquest.S_HuntingQuestMapList;
import l1j.server.server.serverpackets.message.S_ServerMessage;

public class A_HuntingQuestReward extends ProtoHandler {
	protected A_HuntingQuestReward(){}
	private A_HuntingQuestReward(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);// 0x08
		int map_number		= readBit();// 맵번호
		readP(1);// 0x10
		int location_desc	= readBit();// 서브번호
		readP(1);// 0x18
		eMonsterBookV2RewardGrade reward_grade	= eMonsterBookV2RewardGrade.fromInt(readC());// 보상 타입

		HuntingQuestUser user		= _pc.getHuntingQuest();
		HuntingQuestUserTemp temp	= user.getTemp(map_number, location_desc);
		if (temp == null || temp.isComplete() || temp.getKillCount() < Config.QUEST.HUNTING_QUEST_CLEAR_VALUE) {
			return;
		}
		
		L1PcInventory inv	= _pc.getInventory();
		int exp_percent		= 0;
		switch(reward_grade){
		// 일반
		case RG_NORMAL:
			if (!inv.consumeItem(L1ItemId.ADENA, 50000)) {
				return;
			}
			exp_percent = 2;
			break;
		// 고급
		case RG_DRAGON:
			if (!inv.consumeItem(L1ItemId.DRAGON_HIGH_DIAMOND, 1)) {
				return;
			}
			_pc.getAccount().getEinhasad().addPoint(Config.EIN.REST_EXP_DEFAULT_RATION * 150, _pc);
			_pc.sendPackets(new S_RestExpInfoNoti(_pc), true);
			_pc.sendPackets(new S_ExpBoostingInfo(_pc), true);
			_pc.sendPackets(S_RestGaugeChargeNoti.PLUS_150);
			exp_percent = _pc.getLevel() >= 90 ? 10 : 25;
			break;
		// 최고급
		case RG_HIGH_DRAGON:
			if (!inv.consumeItem(L1ItemId.DRAGON_FINEST_DIAMOND, 1)) {
				return;
			}
			inv.consumeItem(L1ItemId.DRAGON_FINEST_DIAMOND, 1);
			_pc.getAccount().getEinhasad().addPoint(Config.EIN.REST_EXP_DEFAULT_RATION * 300, _pc);
			_pc.sendPackets(new S_RestExpInfoNoti(_pc), true);
			_pc.sendPackets(new S_ExpBoostingInfo(_pc), true);
			_pc.sendPackets(S_RestGaugeChargeNoti.PLUS_300);
			L1ItemInstance item = inv.storeItem(43030, 1);
			//_pc.sendPackets(new S_ServerMessage(403, item.getDescKr()), true);
			_pc.sendPackets(new S_ServerMessage(403, item.getDesc()), true);
			exp_percent = _pc.getLevel() >= 90 ? 45 : 55;
			break;
		}
		
		if (reward_grade != eMonsterBookV2RewardGrade.RG_NORMAL) {
			// 칼바 트로피
			if (inv.checkItem(30850, 1)) {
				exp_percent		+= 20;
			}
			// 칼바 트로페움
			if (inv.checkItem(30851, 1)) {
				exp_percent		+= 60;
			}
		}
		
		temp.setComplete(true);
		_pc.sendPackets(new S_HuntingQuestMapList(user.getInfo().values()), true);
		
		long exp	= ExpTable.getExpFromLevelAndPercent(_pc.getLevel(), 52, exp_percent);
		if (_pc.getExp() + exp >= L1ExpPlayer.LIMIT_EXP) {
			return;
		}
		/** 폭렙 방지 **/
	    if (exp + _pc.getExp() > ExpTable.getExpByLevel(_pc.getLevel() + 1)) {
	    	exp = (ExpTable.getExpByLevel(_pc.getLevel() + 1) - _pc.getExp());
		}
	    _pc.addExp(exp);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_HuntingQuestReward(data, client);
	}

}

