package l1j.server.server.serverpackets.huntingquest;

import l1j.server.Config;
import l1j.server.GameSystem.huntingquest.user.HuntingQuestUserTemp;
import l1j.server.server.utils.BinaryOutputStream;

public class HuntingQuestMapInfo extends BinaryOutputStream {
	
	protected HuntingQuestMapInfo(HuntingQuestUserTemp temp) {
		super();
		int killCount = temp.getKillCount();
		write_map_number(temp.getMapNumber());
		write_location_desc(temp.getLocationDesc());
		write_kill_count(killCount);
		write_is_complete(killCount >= Config.QUEST.HUNTING_QUEST_CLEAR_VALUE);
		write_quest_id(temp.getQuestId());
	}

	protected void write_map_number(int map_number) {
		writeC(0x08);
		writeBit(map_number);
	}
	
	protected void write_location_desc(int location_desc) {
		writeC(0x10);
		writeBit(location_desc);
	}
	
	protected void write_kill_count(int kill_count) {
		writeC(0x18);
		writeBit(kill_count);
	}
	
	protected void write_is_complete(boolean is_complete) {
		writeC(0x20);
		writeB(is_complete);
	}
	
	protected void write_quest_id(int quest_id) {
		writeC(0x28);
		writeBit(quest_id);
	}
}

