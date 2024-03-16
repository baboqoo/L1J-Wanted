package l1j.server.server.serverpackets.huntingquest;

import java.util.Collection;

import l1j.server.Config;
import l1j.server.GameSystem.huntingquest.user.HuntingQuestUserTemp;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_HuntingQuestMapList extends ServerBasePacket {
	private static final String S_HUNTING_QUEST_MAP_LIST	= "[S] S_HuntingQuestMapList";
	private byte[] _byte	= null;
	public static final int MAP_LIST	= 0x098a;
	
	public S_HuntingQuestMapList(Collection<HuntingQuestUserTemp> templist) {
		write_init();
		int remain_quest_count = Config.QUEST.HUNTING_QUEST_REGIST_COUNT;
		if (!templist.isEmpty()) {
			for (HuntingQuestUserTemp temp : templist) {
				if (temp.isComplete()) {
					remain_quest_count--;
					continue;
				}
				write_hunting_quest_map_list(temp);
			}
		}
		write_remain_quest_count(remain_quest_count);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(MAP_LIST);
	}
	
	void write_hunting_quest_map_list(HuntingQuestUserTemp temp) {
		HuntingQuestMapInfo os = null;
		try {
			os = new HuntingQuestMapInfo(temp);
			writeRaw(0x0a);// hunting_quest_map_list
			writeBytesWithLength(os.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
					os = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	void write_remain_quest_count(int remain_quest_count) {
		writeRaw(0x10);// remain_quest_count
		writeRaw(remain_quest_count);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_HUNTING_QUEST_MAP_LIST;
	}
}

