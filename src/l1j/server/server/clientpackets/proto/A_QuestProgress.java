package l1j.server.server.clientpackets.proto;

import java.util.HashMap;

import l1j.server.Config;
import l1j.server.GameSystem.beginnerquest.BeginnerQuestUserTable;
import l1j.server.GameSystem.beginnerquest.bean.L1QuestProgress;
import l1j.server.common.bin.QuestCommonBinLoader;
import l1j.server.common.bin.quest.QuestCommonBin.QuestCommonBinExtend;
import l1j.server.common.bin.quest.QuestT;
import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.quest.S_QuestProgress;

public class A_QuestProgress extends ProtoHandler {
	private long currentTime;
	
	protected A_QuestProgress(){}
	private A_QuestProgress(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (!Config.QUEST.BEGINNER_QUEST_ACTIVE || _pc == null || _pc.getLevel() > Config.QUEST.BEGINNER_QUEST_LIMIT_LEVEL || !_pc.getQuest().getQuestProgressList().isEmpty()) {
			return;
		}
		BeginnerQuestUserTable.getInstance().load_progress(_pc);// 진행중인 퀘스트 정보 로드
		
		if (!_pc.getQuest().getQuestProgressList().isEmpty()) {
			currentTime = System.currentTimeMillis();
			
			// 현재 등록된 퀘스트 조사
			for (L1QuestProgress progress : _pc.getQuest().getQuestProgressList().values()) {
				if (progress == null || progress.getFinishTime() != 0 || progress.getBin().get_Prerequisite().get_Level().get_Maximum() >= _pc.getLevel()) {
					continue;
				}
				setEndQuest(progress);
			}
			
			// 전체 퀘스트 조사
			L1QuestProgress progress	= null;
			QuestT bin					= null;
			HashMap<Integer, QuestCommonBinExtend> questList = QuestCommonBinLoader.getQuestList();
			for (QuestCommonBinExtend extend : questList.values()) {
				bin = extend.get_quest();
				if (bin == null || bin.get_Prerequisite().get_Level().get_Maximum() >= _pc.getLevel()) {
					continue;
				}
				progress	= _pc.getQuest().getQuestProgress(extend.get_quest_number());
				if (progress != null) {
					continue;
				}
				progress	= new L1QuestProgress(extend.get_quest_number(), bin);
				setEndQuest(progress);
				_pc.getQuest().putQuestProgress(progress.getQuestId(), progress);
			}
		}
		_pc.sendPackets(new S_QuestProgress(_pc), true);
	}
	
	void setEndQuest(L1QuestProgress progress){
		if (progress.getStartTime() == 0) {
			progress.setStartTime(currentTime);
		}
		progress.setFinishTime(currentTime);
		QuestT.ObjectiveListT requireList = progress.getBin().get_ObjectiveList();
		if (requireList != null && requireList.get_Objective() != null) {
			for (QuestT.ObjectiveT require : requireList.get_Objective()) {
				progress.setQuantity(require.get_ID(), require.get_RequiredQuantity());
			}
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_QuestProgress(data, client);
	}

}

