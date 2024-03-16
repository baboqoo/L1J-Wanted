package l1j.server.server.serverpackets.craft;

import javolution.util.FastMap;
import l1j.server.GameSystem.craft.CraftSuccessCountLoader;
import l1j.server.common.bin.CraftCommonBinLoader;
import l1j.server.common.bin.craft.Craft;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_CraftIdList extends ServerBasePacket {
	private static final String S_CRAFT_ID_LIST	= "[S] S_CraftIdList";
	private byte[] _byte						= null;
	public static final int CRAFT_ID_LIST		= 0x0039;// 제작 리스트
	
	public static final S_CraftIdList ERROR_INVALID_NPC_ID	= new S_CraftIdList(eCraftIdListReqResultType.ERROR_INVALID_NPC_ID);
	public static final S_CraftIdList ERROR_OUT_OF_RANGE	= new S_CraftIdList(eCraftIdListReqResultType.ERROR_OUT_OF_RANGE);
	
	public static void reload(){
		CRFT_NPC_ID_LIST_PACKETS.clear();
	}
	
	private static final FastMap<Integer, ServerBasePacket> CRFT_NPC_ID_LIST_PACKETS = new FastMap<>();
	public static ServerBasePacket getCraftPacket(L1PcInstance pc, int npcId, int[] craftList){
		ServerBasePacket pck = CRFT_NPC_ID_LIST_PACKETS.get(npcId);
		if (pck == null) {
			pck = new S_CraftIdList(eCraftIdListReqResultType.SUCCESS, craftList, pc);
			CRFT_NPC_ID_LIST_PACKETS.put(npcId, pck);
		}
		return pck;
	}
	
	public S_CraftIdList(eCraftIdListReqResultType result, int[] craft_id_list, L1PcInstance pc) {
		write_init();
		write_eResult(result);
		write_craft_id_list(craft_id_list, pc);
		write_eBlindType(1);
		writeH(0x00);
	}
	
	public S_CraftIdList(eCraftIdListReqResultType result) {
		write_init();
		write_eResult(result);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(CRAFT_ID_LIST);
	}
	
	void write_eResult(eCraftIdListReqResultType eResult) {
		writeRaw(0x08);// eResult
		writeRaw(eResult.value);
	}
	
	void write_craft_id_list(int[] craft_id_list, L1PcInstance pc) {
		Craft bin			= null;
		for (int i = 0; i < craft_id_list.length; i++) {
			int craft_id	= craft_id_list[i];
			bin				= CraftCommonBinLoader.getCraft(craft_id);
			if (bin == null) {
				System.out.println(String.format("[S_CraftIdList] BIN_DATA_NOT_FOUND : CRAFT_ID(%d)", craft_id));
				continue;
			}
			int max_success_cnt = bin.get_max_successcount();
			int cur_success_cnt = bin.get_cur_successcount();
			// 제작 제한
			if (max_success_cnt > 0) {
				cur_success_cnt = CraftSuccessCountLoader.getCurrentCount(pc, craft_id, bin.get_SuccessCountType());
			}
			
			writeRaw(0x12);
			writeRaw(craft_id > 16383 ? 8 : craft_id > 127 ? 7 : 6);
			write_craft_id(craft_id);
			write_max_success_cnt(max_success_cnt);
			write_cur_success_cnt(cur_success_cnt);
		}
	}
	
	void write_eBlindType(int eBlindType) {
		writeRaw(0x18);// eBlindType
		writeRaw(eBlindType);
	}
	
	void write_npc_type(eCraftNpcType npc_type) {
		writeRaw(0x20);// npc_type
		writeRaw(npc_type.value);
	}
	
	// craft_id_list
	void write_craft_id(int craft_id) {
		writeRaw(0x08);// craft_id 제작 번호
		write4bit(craft_id);
	}
	
	void write_max_success_cnt(int max_success_cnt) {
		writeRaw(0x10);// max_success_cnt 한정 제작 개수
		writeRaw(max_success_cnt);
	}
	
	void write_cur_success_cnt(int cur_success_cnt) {
		writeRaw(0x18);// cur_success_cnt 만든 개수
		writeRaw(cur_success_cnt);
	}
	
	public enum eCraftIdListReqResultType{
		SUCCESS(0),
		ERROR_INVALID_NPC_ID(1),
		ERROR_OUT_OF_RANGE(2),
		ERROR_UNKNOWN(9999);
		private int value;
		eCraftIdListReqResultType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		
		public boolean equals(eCraftIdListReqResultType v){
			return value == v.value;
		}
		public static eCraftIdListReqResultType fromInt(int i){
			switch(i){
			case 0:
				return SUCCESS;
			case 1:
				return ERROR_INVALID_NPC_ID;
			case 2:
				return ERROR_OUT_OF_RANGE;
			case 9999:
				return ERROR_UNKNOWN;
			default:
				return null;
			}
		}
	}
	
	public enum eCraftNpcType{
		DEFAULT(0),
		MAGIC_DOLL(1),
		;
		private int value;
		eCraftNpcType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eCraftNpcType v){
			return value == v.value;
		}
		public static eCraftNpcType fromInt(int i){
			switch(i){
			case 0:
				return DEFAULT;
			case 1:
				return MAGIC_DOLL;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eCraftNpcType, %d", i));
			}
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
		return S_CRAFT_ID_LIST;
	}
}

