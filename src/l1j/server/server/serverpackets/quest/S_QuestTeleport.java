package l1j.server.server.serverpackets.quest;

import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_QuestTeleport extends ServerBasePacket {
	private static final String S_QUEST_TELEPORT = "[S] S_QuestTeleport";
	private byte[] _byte = null;
	public static final int TELEPORT	= 0x0210;
	
	private static final ConcurrentHashMap<Integer, S_QuestTeleport> TELEPORT_PCK_DATA		= new ConcurrentHashMap<>();
	
	public static S_QuestTeleport getTeleportPck(int questId){
		S_QuestTeleport pck = TELEPORT_PCK_DATA.get(questId);
		if (pck == null) {
			pck = new S_QuestTeleport(S_QuestTeleport.eResultCode.SUCCESS, questId);
			TELEPORT_PCK_DATA.put(questId, pck);
		}
		return pck;
	}
	
	public S_QuestTeleport(S_QuestTeleport.eResultCode result, int id) {
		write_init();
		write_result(result);
		write_id(id);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(TELEPORT);
	}
	
	void write_result(S_QuestTeleport.eResultCode result) {
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
		FAIL_NOT_ENOUGH_ADENA(2),
		FAIL_WRONG_LOCATION(3),
		FAIL_CANT_TELEPORT_NOW(4);
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
		return S_QUEST_TELEPORT;
	}
}
