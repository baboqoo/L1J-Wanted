package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.GameSystem.beginnerquest.BeginnerQuestTable;
import l1j.server.GameSystem.beginnerquest.bean.L1QuestProgress;
import l1j.server.common.bin.quest.QuestT;
import l1j.server.server.GameClient;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.serverpackets.quest.S_QuestProgressUpdateNoti;
import l1j.server.server.serverpackets.quest.S_QuestStart;

public class A_QuestStart extends ProtoHandler {
	protected A_QuestStart(){}
	private A_QuestStart(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int _id;

	@Override
	protected void doWork() throws Exception {
		if (!Config.QUEST.BEGINNER_QUEST_ACTIVE || _pc == null) {
			return;
		}
		readP(1);// 0x08
		_id = readBit();
		L1QuestProgress progress = _pc.getQuest().getQuestProgress(_id);
		if (progress == null) {
			_pc.sendPackets(new S_QuestStart(S_QuestStart.eResultCode.FAIL_OBSOLETE, _id), true);
			return;
		}
		if (progress.getFinishTime() != 0) {
			_pc.sendPackets(new S_QuestStart(S_QuestStart.eResultCode.FAIL_ALREADY_FINISHED, _id), true);
			return;
		}
		if (progress.getStartTime() != 0) {
			_pc.sendPackets(new S_QuestStart(S_QuestStart.eResultCode.FAIL_ALREADY_STARTED, _id), true);
			return;
		}
		if (progress.getBin().get_Prerequisite().get_Level().get_Maximum() < _pc.getLevel()) {
			_pc.sendPackets(new S_QuestStart(S_QuestStart.eResultCode.FAIL_ALREADY_FINISHED, _id), true);
			_pc.getQuest().questSkip(progress);
			return;
		}
		if (!progress.getBin().get_AutoActive()) {
			return;
		}
		
		progress.setStartTime(System.currentTimeMillis());// 퀘스트 시작 시간
		_pc.sendPackets(S_QuestStart.getStartPck(_id));
		
		for (QuestT.ObjectiveT required : progress.getBin().get_ObjectiveList().get_Objective()) {
			switch (required.get_Type()) {
			case REACH_LEVEL:// 레벨 달성
				progress.setQuantity(required.get_ID(), _pc.getLevel() > required.get_RequiredQuantity() ? required.get_RequiredQuantity() : _pc.getLevel());
				break;
			case VIEW_DIALOGUE:// 화면 보기
				progress.setQuantity(required.get_ID(), required.get_RequiredQuantity());
				break;
			case TUTORIAL_BLOOD_PLEDGE_CREATE:// 혈맹 생성
				L1Clan clan = _pc.getClan();
				boolean isCreateClan = clan != null && clan.getLeaderId() == _pc.getId();
				progress.setQuantity(required.get_ID(), isCreateClan ? required.get_RequiredQuantity() : 0);
				break;
			case TUTORIAL_BLOOD_PLEDGE_JOIN:// 혈맹 가입
				progress.setQuantity(required.get_ID(), _pc.getClan() != null ? required.get_RequiredQuantity() : 0);
				break;
			case START_PSS:// 플레이서포트 시작
				progress.setQuantity(required.get_ID(), _pc.getConfig().isPlaySupport() ? required.get_RequiredQuantity() : 0);
				break;
			case COLLECT_ITEM:// 아이템 수집
				L1ItemInstance collectItem = _pc.getInventory().findItemNameId(required.get_AssetID());
				progress.setQuantity(required.get_ID(), collectItem != null ? collectItem.getCount() : 0);
				break;
			default:
				progress.setQuantity(required.get_ID(), 0);
				break;
			}
		}
		_pc.sendPackets(new S_QuestProgressUpdateNoti(progress), true);
		if (BeginnerQuestTable.isAutoCompleteQuest(_id)) {// 자동 완료 퀘스트
			_pc.getQuest().questSkip(progress);
		}
		
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_QuestStart(data, client);
	}

}

