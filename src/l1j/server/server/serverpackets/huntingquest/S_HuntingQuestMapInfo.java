package l1j.server.server.serverpackets.huntingquest;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_HuntingQuestMapInfo extends ServerBasePacket {
	private static final String S_HUNTING_QUEST_MAP_INFO	= "[S] S_HuntingQuestMapInfo";
	private byte[] _byte	= null;
	public static final int MAP_INFO	= 0x0991;
	
	public S_HuntingQuestMapInfo(L1PcInstance pc, int map_number) {
		boolean timerMap = pc.getDungoenTimer().isTimerInfo(map_number);
		write_init();
		write_map_number(map_number);
		write_is_can_random_teleport(!timerMap ? true : pc.getDungoenTimer().isEnter(map_number));
		if (timerMap) {
			write_map_limit_time(pc.getDungoenTimer().getRestTimer(map_number));
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(MAP_INFO);
	}
	
	void write_map_number(int map_number) {
		writeRaw(0x08);// map_number
		writeBit(map_number);
	}
	
	void write_is_can_random_teleport(boolean is_can_random_teleport) {
		writeRaw(0x10);// is_can_random_teleport
		writeB(is_can_random_teleport);
	}
	
	void write_map_limit_time(int map_limit_time) {
		writeRaw(0x18);// map_limit_time
		writeBit(map_limit_time);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_HUNTING_QUEST_MAP_INFO;
	}
}

