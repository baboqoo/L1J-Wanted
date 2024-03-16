package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.GameSystem.beginnerquest.BeginnerQuestTable;
import l1j.server.GameSystem.beginnerquest.BeginnerQuestUserTable;
import l1j.server.GameSystem.beginnerquest.bean.L1QuestProgress;
import l1j.server.common.bin.quest.QuestT;
import l1j.server.common.bin.quest.QuestT.ObjectiveTypeT;
import l1j.server.common.data.OptionalRewardT;
import l1j.server.common.data.RewardListT;
import l1j.server.common.data.RewardT;
import l1j.server.common.data.RewardT.eType;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.exp.L1ExpPlayer;
import l1j.server.server.serverpackets.quest.S_QuestFinish;
import l1j.server.server.templates.L1Item;

public class A_QuestFinish extends ProtoHandler {
	protected A_QuestFinish(){}
	private A_QuestFinish(byte[] data, GameClient client) {
		super(data, client);
	}

	private int _id;
	private int _optional_reward_index;
	
	void parse() {
		while (!isEnd()) {
			int tag = readC();
			switch (tag) {
			case 0x08:
				_id = readBit();
				break;
			case 0x10:
				_optional_reward_index = readC();
				break;
			default:
				return;
			}
		}
	}
	
	@Override
	protected void doWork() throws Exception {
		if (!Config.QUEST.BEGINNER_QUEST_ACTIVE || _pc == null) {
			return;
		}
		if (Config.SERVER.STANDBY_SERVER) {
			_pc.sendPackets(L1SystemMessage.STANBY_USE_FAIL_MSG);
			return;
		}
		parse();
		L1QuestProgress progress = _pc.getQuest().getQuestProgress(_id);
		if (progress == null) {
			_pc.sendPackets(new S_QuestFinish(S_QuestFinish.eResultCode.FAIL_OBSOLETE, _id));
			return;
		}
		if (progress.getFinishTime() != 0) {
			_pc.sendPackets(new S_QuestFinish(S_QuestFinish.eResultCode.FAIL_ALREADY_FINISHED, _id));
			return;
		}
		
		QuestT.ObjectiveListT requireList = progress.getBin().get_ObjectiveList();
		if (requireList != null && requireList.get_Objective() != null) {
			// 목표 달성 여부
			for (QuestT.ObjectiveT quantity : requireList.get_Objective()) {
				if (progress.getQuantity(quantity.get_ID()) < quantity.get_RequiredQuantity()) {
					_pc.sendPackets(new S_QuestFinish(S_QuestFinish.eResultCode.FAIL_NOT_COMPLETED, _id));
					return;
				}
			}
			
			// 재료 삭제
			for (QuestT.ObjectiveT quantity : requireList.get_Objective()) {
				if (quantity.get_Type() == ObjectiveTypeT.COLLECT_ITEM) {
					int consumeDescId = quantity.get_AssetID();
					if (consumeDescId == 17966 || consumeDescId == 14462 || consumeDescId == 25701) {
						continue;
					}
					_pc.getInventory().consumeItemNameId(consumeDescId);
				}
			}
		}
		
		progress.setFinishTime(System.currentTimeMillis());// 퀘스트 종료 시간
		
		RewardListT rewardList = progress.getBin().get_RewardList();

		OptionalRewardT optionalRewardList = progress.getBin().get_OptionalRewardList();
		RewardT optionalReward = null;
		if (optionalRewardList != null && optionalRewardList.get_Reward() != null) {
			optionalReward = optionalRewardList.get_Reward().get(_optional_reward_index);
		}
		reward(rewardList, optionalReward);
		_pc.sendPackets(S_QuestFinish.getFinishPck(_id));
		BeginnerQuestUserTable.getInstance().update_progress(_pc);
	}
	
	/**
	 * 퀘스트 보상
	 * @param rewardList
	 * @param optionalReward
	 */
	void reward(RewardListT rewardList, RewardT optionalReward) {
		try {
			if (rewardList != null && rewardList.get_Reward() != null) {
				for (RewardT rewad : rewardList.get_Reward()) {
					switch (rewad.get_Type()) {
					case ITEM:
						rewardItem(rewad);
						break;
					case EXP:
						rewardExp(rewad);
						break;
					default:
						break;
					}
				}
			}
			
			if (Config.QUEST.BEGINNER_QUEST_FAST_PROGRESS && BeginnerQuestTable.isFastLevel(_id)) {
				int fast_level = BeginnerQuestTable.getFastLevel(_id);
				fastLevelExp(fast_level);
			}
				
			if (optionalReward != null && optionalReward.get_Type() == eType.ITEM) {
				rewardItem(optionalReward);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 퀘스트 보상(아이템)
	 * @param rewad
	 */
	void rewardItem(RewardT rewad){
		if (rewad.get_AssetID() == 17967) {// 수련자의 은무기 상자 제외(제작 시 획득)
			return;
		}
		L1Item item = ItemTable.getInstance().findItemByNameId(rewad.get_AssetID());
		if (item == null) {
			System.out.println(String.format("[A_QuestFinish] REWARD_ITEM_TEMPLATE_NOT_FOUND : NAME_ID(%d), QUEST_ID(%d)", rewad.get_AssetID(), _id));
			return;
		}
		_pc.getInventory().storeItem(item.getItemId(), (int)rewad.get_Amount());
	}
	
	/**
	 * 퀘스트 보상(경험치)
	 * @param rewad
	 */
	void rewardExp(RewardT rewad){
		if (rewad.get_Amount() <= 0) {
			return;
		}
		if (_pc.getExp() + rewad.get_Amount() >= L1ExpPlayer.LIMIT_EXP) {
			return;
		}
		_pc.getExpHandler().addExp(rewad.get_Amount());
	}
	
	/**
	 * 빠른 진행을 위한 경험치 추가 지급
	 * @param growLevel
	 */
	void fastLevelExp(int growLevel){
		if (_pc.getLevel() >= growLevel) {
			return;
		}
		// 이전 레벨 99%
		long growExp	= (long)(ExpTable.getExpByLevel(growLevel - 1) + (long)(ExpTable.getNeedExpNextLevel(growLevel - 1) * 0.99D));
		_pc.setExp(growExp);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_QuestFinish(data, client);
	}

}

