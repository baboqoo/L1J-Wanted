package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.GameSystem.beginnerquest.bean.L1QuestProgress;
import l1j.server.common.bin.quest.QuestT.ObjectiveTypeT;
import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.quest.S_QuestProgressUpdateNoti;
import l1j.server.server.serverpackets.quest.S_QuestFinish;

public class A_QuestObjectiveUpdate extends ProtoHandler {
	protected A_QuestObjectiveUpdate(){}
	private A_QuestObjectiveUpdate(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int _id;

	@Override
	protected void doWork() throws Exception {
		if (!Config.QUEST.BEGINNER_QUEST_ACTIVE || _pc == null) {
			return;
		}
		readP(1);
		_id = readBit();
		L1QuestProgress progress = _pc.getQuest().getQuestProgress(_id);
		if (progress == null) {
			return;
		}
		if (progress.getFinishTime() != 0) {// 이미 완료된 퀘스트
			_pc.sendPackets(new S_QuestFinish(S_QuestFinish.eResultCode.FAIL_ALREADY_FINISHED, _id));
			return;
		}
		if (progress.getBin().get_Prerequisite().get_Level().get_Maximum() < _pc.getLevel()) {
			_pc.getQuest().questSkip(progress);
			return;
		}
		if (progress.getBin().get_ObjectiveList().get_Objective().getFirst().get_Type() != ObjectiveTypeT.TUTORIAL_OPEN_UI) {
			System.out.println(String.format("[A_QuestObjectiveUpdate] NOT_TUTORIAL_OPEN_UI : QUEST_ID(%d), CHAR_NAME(%s)", _id, _pc.getName()));
			return;
		}
		progress.setQuantity(1, 1);// 목표 달성
		_pc.sendPackets(new S_QuestProgressUpdateNoti(progress), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_QuestObjectiveUpdate(data, client);
	}

}

