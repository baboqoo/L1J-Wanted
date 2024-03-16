package l1j.server.server.serverpackets.craft;

import l1j.server.GameSystem.craft.CraftSuccessCountLoader;
import l1j.server.common.bin.craft.Craft;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_CraftLimitedInfo extends ServerBasePacket {
	private static final String S_CRAFT_LIMITED_INFO	= "[S] S_CraftLimitedInfo";
	private byte[] _byte								= null;
	public static final int LIMITED_INFO				= 0x0943;// 통합 제작 UI
	
	public static final S_CraftLimitedInfo RP_ERROR_CRAFT_ID = new S_CraftLimitedInfo(S_CraftLimitedInfo.eCraftIdInfoReqResultType.RP_ERROR_CRAFT_ID);
	
	public S_CraftLimitedInfo(S_CraftLimitedInfo.eCraftIdInfoReqResultType result) {
		write_init();
		write_eResult(result);
		writeH(0x00);
	}
	
	public S_CraftLimitedInfo(L1PcInstance pc, S_CraftLimitedInfo.eCraftIdInfoReqResultType result, Craft bin) {
		write_init();
		write_eResult(result);
		write_craft_id_info(pc, bin);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(LIMITED_INFO);
	}
	
	void write_eResult(S_CraftLimitedInfo.eCraftIdInfoReqResultType eResult) {
		writeRaw(0x08);// eResult
		writeRaw(eResult.value);
	}
	
	void write_craft_id_info(L1PcInstance pc, Craft bin) {
		int craft_id = bin.get_id();
		int max_successcount = bin.get_max_successcount();
		int cur_successcount = bin.get_cur_successcount();
		if (max_successcount > 0) {
			cur_successcount = CraftSuccessCountLoader.getCurrentCount(pc, craft_id, bin.get_SuccessCountType());
		}
		writeRaw(0x12);
		writeRaw(craft_id > 16383 ? 8 : craft_id > 127 ? 7 : 6);// 길이
		write_craft_id(craft_id);
		write_max_success_cnt(max_successcount);
		write_cur_success_cnt(cur_successcount);
	}
	
	void write_craft_id(int craft_id) {
		writeRaw(0x08);// craft_id
		writeBit(craft_id);
	}
	
	void write_max_success_cnt(int max_success_cnt) {
		writeRaw(0x10);// max_success_cnt
		writeRaw(max_success_cnt);
	}
	
	void write_cur_success_cnt(int cur_success_cnt) {
		writeRaw(0x18);// cur_success_cnt
		writeRaw(cur_success_cnt);
	}
	
	public enum eCraftIdInfoReqResultType{
		RP_SUCCESS(0),
		RP_ERROR_CRAFT_ID(1),
		RP_ERROR_UNKNOWN(9999),
		;
		private int value;
		eCraftIdInfoReqResultType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eCraftIdInfoReqResultType v){
			return value == v.value;
		}
		public static eCraftIdInfoReqResultType fromInt(int i){
			switch(i){
			case 0:
				return RP_SUCCESS;
			case 1:
				return RP_ERROR_CRAFT_ID;
			case 9999:
				return RP_ERROR_UNKNOWN;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eCraftIdInfoReqResultType, %d", i));
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
		return S_CRAFT_LIMITED_INFO;
	}
}

