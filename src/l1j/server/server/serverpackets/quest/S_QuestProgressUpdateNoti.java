package l1j.server.server.serverpackets.quest;

import l1j.server.GameSystem.beginnerquest.bean.L1QuestProgress;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_QuestProgressUpdateNoti extends ServerBasePacket {
	private static final String S_QUEST_PROGRESS_UPDATE_NOTI = "[S] S_QuestProgressUpdateNoti";
	private byte[] _byte = null;
	public static final int UPDATE_NOTI	= 0x0207;
	
	public S_QuestProgressUpdateNoti(L1QuestProgress progress) {
		write_init();
		write_quest(progress);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(UPDATE_NOTI);
	}
	
	void write_quest(L1QuestProgress progress) {
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
		return S_QUEST_PROGRESS_UPDATE_NOTI;
	}
}
