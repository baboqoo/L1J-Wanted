package l1j.server.server.serverpackets.quest;

import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_QuestFinish extends ServerBasePacket {
	private static final String S_QUEST_FINISH = "[S] S_QuestFinish";
	private byte[] _byte = null;
	public static final int FINISH	= 0x020d;
	
	private static final ConcurrentHashMap<Integer, S_QuestFinish> FINISH_PCK_DATA		= new ConcurrentHashMap<>();
	
	public static S_QuestFinish getFinishPck(int questId){
		S_QuestFinish pck = FINISH_PCK_DATA.get(questId);
		if (pck == null) {
			pck = new S_QuestFinish(eResultCode.SUCCESS, questId);
			FINISH_PCK_DATA.put(questId, pck);
		}
		return pck;
	}
	
	public S_QuestFinish(eResultCode result, int id) {
		write_init();
		write_result(result);
		write_id(id);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(FINISH);
	}
	
	void write_result(eResultCode result) {
		writeRaw(0x08);
		writeRaw(result.status);
	}
	
	void write_id(int id) {
		writeRaw(0x10);
		writeBit(id);
	}
	
	public enum eResultCode{
		SUCCESS(0),
		FAIL(1),
		FAIL_ALREADY_FINISHED(2),
		FAIL_NOT_COMPLETED(3),
		FAIL_NOT_VALID_REWARD_INDEXES(4),
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
		return S_QUEST_FINISH;
	}
}
