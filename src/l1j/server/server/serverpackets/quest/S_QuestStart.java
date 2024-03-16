package l1j.server.server.serverpackets.quest;

import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_QuestStart extends ServerBasePacket {
	private static final String S_QUEST_START = "[S] S_QuestStart";
	private byte[] _byte = null;
	public static final int START = 0x020b;
	
	private static final ConcurrentHashMap<Integer, S_QuestStart> START_PCK_DATA			= new ConcurrentHashMap<>();
	
	public static S_QuestStart getStartPck(int questId){
		S_QuestStart pck = START_PCK_DATA.get(questId);
		if (pck == null) {
			pck = new S_QuestStart(S_QuestStart.eResultCode.SUCCESS, questId);
			START_PCK_DATA.put(questId, pck);
		}
		return pck;
	}
	
	public S_QuestStart(S_QuestStart.eResultCode result, int id) {
		write_init();
		write_result(result);
		write_id(id);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(START);
	}
	
	void write_result(S_QuestStart.eResultCode result) {
		writeRaw(0x08);
		writeRaw(result.status);
	}
	
	void write_id(int id) {
		writeRaw(0x10);
		writeBit(id);
	}
	
	public enum eResultCode {
		SUCCESS(0),
		FAIL(1),
		FAIL_ALREADY_REVEALED(2),
		FAIL_ALREADY_STARTED(3),
		FAIL_ALREADY_FINISHED(4),
		FAIL_OBSOLETE(5);
		private int status;
		eResultCode(int val){
			status = val;
		}
		public int getStatus(){
			return status;
		}
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
		return S_QUEST_START;
	}
}
