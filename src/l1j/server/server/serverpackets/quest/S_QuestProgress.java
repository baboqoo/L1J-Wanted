package l1j.server.server.serverpackets.quest;

import l1j.server.GameSystem.beginnerquest.bean.L1QuestProgress;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_QuestProgress extends ServerBasePacket {
	private static final String S_QUEST_PROGRESS = "[S] S_QuestProgress";
	private byte[] _byte = null;
	public static final int PROGRESS = 0x0206;

	public S_QuestProgress(L1PcInstance pc) {
		write_init();
		synchronized (pc.getQuest().syncQuest) {
			for (L1QuestProgress progress : pc.getQuest().getQuestProgressList().values()) {
				write_quest_list(progress);
			}
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(PROGRESS);
	}
	
	void write_quest_list(L1QuestProgress progress) {
		writeRaw(0x0a);
		writeBytesWithLength(get_quset(progress));
	}
	
	byte[] get_quset(L1QuestProgress progress) {
		QuestProgress os = null;
		try {
			os = new QuestProgress(progress);
			return os.getBytes();
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
		return null;
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_QUEST_PROGRESS;
	}
}
