package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.GameSystem.beginnerquest.BeginnerQuestTable;
import l1j.server.GameSystem.beginnerquest.bean.L1QuestProgress;
import l1j.server.common.bin.QuestCommonBinLoader;
import l1j.server.common.bin.quest.QuestT;
import l1j.server.common.data.RewardListT;
import l1j.server.common.data.RewardT;
import l1j.server.server.GameClient;
import l1j.server.server.construct.L1BeginnerQuest;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.serverpackets.quest.S_QuestReveal;
import l1j.server.server.templates.L1Item;

public class A_QuestReveal extends ProtoHandler {
	protected A_QuestReveal(){}
	private A_QuestReveal(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int _id;

	@Override
	protected void doWork() throws Exception {
		if (!Config.QUEST.BEGINNER_QUEST_ACTIVE || _pc == null) {
			return;
		}
		readP(1);// 0x08
		_id			= readBit();
		L1QuestProgress progress	= _pc.getQuest().getQuestProgress(_id);
		if (progress != null) {
			if (progress.getFinishTime() != 0) {
				_pc.sendPackets(new S_QuestReveal(S_QuestReveal.eResultCode.FAIL_ALREADY_FINISHED, _id), true);
				return;
			}
			_pc.sendPackets(new S_QuestReveal(S_QuestReveal.eResultCode.FAIL_ALREADY_REVEALED, _id), true);
			return;
		}
		
		QuestT bin								= QuestCommonBinLoader.getQuest(_id);
		QuestT.PrerequisiteT.LevelT questLevel	= bin.get_Prerequisite().get_Level();
		if (questLevel.get_Minimum() > _pc.getLevel()) {
			_pc.sendPackets(new S_QuestReveal(S_QuestReveal.eResultCode.FAIL, _id), true);
			return;
		}
		
		// TODO 퀘스트 생성
		progress	= new L1QuestProgress(_id, bin);
		_pc.getQuest().putQuestProgress(_id, progress);
		
		if (_pc.getLevel() > questLevel.get_Maximum() || BeginnerQuestTable.isQuestBlock(_id) || _pc.getLevel() > Config.QUEST.BEGINNER_QUEST_LIMIT_LEVEL) {
			_pc.sendPackets(new S_QuestReveal(S_QuestReveal.eResultCode.FAIL_ALREADY_FINISHED, _id), true);
			_pc.getQuest().questSkip(progress);
			return;
		}
		
		if (_id == L1BeginnerQuest.ATTACK_TRAINING) {// 공격 훈련
			L1ItemInstance weapon = _pc.getInventory().getFirstItemType(L1ItemType.WEAPON);// 최초 체크된 무기
			if (weapon != null && !weapon.isEquipped()) {
				weapon.clickItem(_pc, null);
			}
		}
		
		RewardListT advanceRewardList = bin.get_AdvanceRewardList();
		if (advanceRewardList != null && advanceRewardList.get_Reward() != null) {
			advanceReward(advanceRewardList);
		}
		
		_pc.sendPackets(S_QuestReveal.getRevealPck(_id));
	}
	
	/**
	 * 사전 보상 아이템
	 * @param advanceRewardList
	 */
	void advanceReward(RewardListT advanceRewardList) {
		for (RewardT reward : advanceRewardList.get_Reward()) {
			L1Item item = ItemTable.getInstance().findItemByNameId(reward.get_AssetID());
			if (item == null) {
				System.out.println(String.format("[A_QuestReveal] ADVANCE_ITEM_TEMPLATE_NOT_FOUND : NAME_ID(%d), QUEST_ID(%d)", reward.get_AssetID(), _id));
				continue;
			}
			_pc.getInventory().storeItem(item.getItemId(), (int)reward.get_Amount());
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_QuestReveal(data, client);
	}

}

