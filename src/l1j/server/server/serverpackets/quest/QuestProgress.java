package l1j.server.server.serverpackets.quest;

import l1j.server.GameSystem.beginnerquest.bean.L1QuestProgress;
import l1j.server.common.bin.quest.QuestT;
import l1j.server.server.utils.BinaryOutputStream;

public class QuestProgress extends BinaryOutputStream {

	public QuestProgress(L1QuestProgress progress) {
		super();
		write_id(progress.getQuestId());
		write_start_time(progress.getStartTime());
		if (progress.getFinishTime() != 0) {
			write_finish_time(progress.getFinishTime());
		} else {
			QuestT.ObjectiveListT requireList = progress.getBin().get_ObjectiveList();
			if (requireList != null && requireList.get_Objective() != null) {
				for (QuestT.ObjectiveT require : requireList.get_Objective()) {
					write_objectives(require, progress.getQuantity(require.get_ID()));
				}
			}
		}
		write_is_shown_in_quest_window(true);
	}
	
	void write_id(int id) {
		writeC(0x08);// id
		writeBit(id);
	}
	
	void write_start_time(long start_time) {
		writeC(0x10);// start_time
		writeBit(start_time);
	}
	
	void write_finish_time(long finish_time) {
		writeC(0x18);// finish_time
		writeBit(finish_time);
	}
	
	void write_objectives(QuestT.ObjectiveT require, int quantity) {
		writeC(0x22);// objectives
		writeC(6);

		writeC(0x08);// id
		writeC(require.get_ID());

		writeC(0x10); // quantity
		writeC(quantity);

		writeC(0x18); // required_quantity
		writeC(require.get_RequiredQuantity());
	}
	
	void write_is_shown_in_quest_window(boolean is_shown_in_quest_window) {
		writeC(0x28);// is_shown_in_quest_window
		writeB(is_shown_in_quest_window);
	}
	
}

