package l1j.server.server.serverpackets.craft;

import javolution.util.FastMap;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1Item;

public class S_CraftMake extends ServerBasePacket {
	private static final String S_CRAFT_MAKE_RESULT			= "[S] S_CraftMakeResult";
	private byte[] _byte									= null;
	
	private static final byte[] MINUS_BYTES					= {
		(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0x01
	};
	
	public static final int MAKE_RESULT						= 0X003b;
	
	public static final S_CraftMake RP_FAILURE						= new S_CraftMake(eCraftMakeReqResultType.RP_FAILURE, 0, 0);
	public static final S_CraftMake RP_ERROR_INVALID_NPC			= new S_CraftMake(eCraftMakeReqResultType.RP_ERROR_INVALID_NPC, 0, 0);
	public static final S_CraftMake RP_ERROR_NPC_OUT_OF_RANGE		= new S_CraftMake(eCraftMakeReqResultType.RP_ERROR_NPC_OUT_OF_RANGE, 0, 0);
	public static final S_CraftMake RP_ERROR_INVEN_OVER				= new S_CraftMake(eCraftMakeReqResultType.RP_ERROR_INVEN_OVER, 0, 0);
	public static final S_CraftMake RP_ERROR_WEIGHT_OVER			= new S_CraftMake(eCraftMakeReqResultType.RP_ERROR_WEIGHT_OVER, 0, 0);
	public static final S_CraftMake RP_ERROR_BLOCKED_CRAFT_ID		= new S_CraftMake(eCraftMakeReqResultType.RP_ERROR_BLOCKED_CRAFT_ID, 0, 0);
	public static final S_CraftMake RP_ERROR_CRAFT_DOES_NOT_EXIST	= new S_CraftMake(eCraftMakeReqResultType.RP_ERROR_CRAFT_DOES_NOT_EXIST, 0, 0);
	public static final S_CraftMake RP_ERROR_INVALID_INPUT			= new S_CraftMake(eCraftMakeReqResultType.RP_ERROR_INVALID_INPUT, 0, 0);
	public static final S_CraftMake RP_ERROR_SUCCESS_COUNT_EXCEED	= new S_CraftMake(eCraftMakeReqResultType.RP_ERROR_SUCCESS_COUNT_EXCEED, 0, 0);
	public static final S_CraftMake RP_ERROR_BAD_QUALIFICATION		= new S_CraftMake(eCraftMakeReqResultType.RP_ERROR_BAD_QUALIFICATION, 0, 0);
	
	/**
	 * 제작결과에 대한 아이템 정보는 동일하므로 패킷을 재사용 처리한다.
	 */
	private static FastMap<Integer, S_CraftMake> SUCCESS_RESULT;
	public static S_CraftMake getSuccessResult(int craftId, L1ItemInstance item){
		if (SUCCESS_RESULT == null) {
			SUCCESS_RESULT = new FastMap<Integer, S_CraftMake>();
		}
		S_CraftMake pck = SUCCESS_RESULT.get(craftId);
		if (pck == null) {
			pck = new S_CraftMake(item);
			SUCCESS_RESULT.put(craftId, pck);
		}
		return pck;
	}
	
	public S_CraftMake(L1ItemInstance output_items) {
		write_init();
		write_eResult(eCraftMakeReqResultType.RP_SUCCESS);
		write_output_items(output_items);
		writeH(0x00);
	}
	
	public S_CraftMake(eCraftMakeReqResultType eResult, int max_success_cnt, int cur_success_cnt) {
		write_init();
		write_eResult(eResult);
		write_limit_craft_info(max_success_cnt, cur_success_cnt);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(MAKE_RESULT);
	}
	
	void write_eResult(eCraftMakeReqResultType eResult) {
		writeRaw(0x08);// eResult
		writeRaw(eResult.value);
	}
	
	// output_items start
	void write_output_items(L1ItemInstance output_items) {
		L1Item temp = output_items.getItem();
		int elemental_type	= 0, elemental_level	= 0;
		if (output_items.getAttrEnchantLevel() > 0) {
			elemental_type	= L1ItemInstance.getElementalEnchantType(output_items.getAttrEnchantLevel());
			elemental_level	= L1ItemInstance.getElementalEnchantValue(output_items.getAttrEnchantLevel());
		}
		byte[] descBytes = temp.getDesc().getBytes();
		int totalLength = 16
				+ getBitSize(temp.getItemNameId())
				+ getBitSize(output_items.getCount())
				+ MINUS_BYTES.length
				+ getBitSize(output_items.getEnchantLevel())
				+ getBitSize(output_items.getBless())
				+ getBitSize(elemental_type)
				+ getBitSize(elemental_level)
				+ descBytes.length
				+ getBitSize(temp.getIconId());
		writeRaw(0x12);
		writeRaw(totalLength);
		write_name_id(temp.getItemNameId());
		write_count(output_items.getCount());
		write_slot(-1);
		write_enchant(output_items.getEnchantLevel());
		write_bless(output_items.getBless());
		write_elemental_type(elemental_type);
		write_elemental_level(elemental_level);
		write_desc(descBytes);
		write_system_desc(0);
		write_broadcast_desc(0);
		write_iconId(temp.getIconId());
		write_url(null);
	}
	
	void write_name_id(int name_id) {
		writeRaw(0x08);// name_id
		writeBit(name_id);
	}
	
	void write_count(int count) {
		writeRaw(0x10);// count
		writeBit(count);
	}
	
	void write_slot(int slot) {
		writeRaw(0x18);// slot
		writeByte(MINUS_BYTES);
	}
	
	void write_enchant(int enchant) {
		writeRaw(0x20);// enchant
		writeBit(enchant);
	}
	
	void write_bless(int bless) {
		writeRaw(0x28);// bless
		writeBit(bless);
	}
	
	void write_elemental_type(int elemental_type) {
		writeRaw(0x30);// elemental_type
		writeBit(elemental_type);
	}
	
	void write_elemental_level(int elemental_level) {
		writeRaw(0x38);// elemental_level
		writeBit(elemental_level);
	}
	
	void write_desc(byte[] desc) {
		writeRaw(0x42);// desc
		writeBytesWithLength(desc);
	}
	
	void write_system_desc(int system_desc) {
		writeRaw(0x48);// system_desc
		writeBit(system_desc);
	}
	
	void write_broadcast_desc(int broadcast_desc) {
		writeRaw(0x50);// broadcast_desc
		writeBit(broadcast_desc);
	}
	
	void write_iconId(int iconId) {
		writeRaw(0x58);// iconId
		writeBit(iconId);
	}
	
	void write_url(String url) {
		writeRaw(0x62);// url
		writeStringWithLength(url);
	}
	// output_items end
	
	// limit_craft_info start
	void write_limit_craft_info(int max_success_cnt, int cur_success_cnt) {
		writeRaw(0x1a);// limit_craft_info
		writeRaw(4);
		write_max_success_cnt(max_success_cnt);
		write_cur_success_cnt(cur_success_cnt);
	}
	
	void write_max_success_cnt(int max_success_cnt) {
		writeRaw(0x08);// max_success_cnt
		writeRaw(max_success_cnt);
	}
	
	void write_cur_success_cnt(int cur_success_cnt) {
		writeRaw(0x10);// cur_success_cnt
		writeRaw(cur_success_cnt);
	}
	// limit_craft_info end
	
	void write_is_final_craft(boolean is_final_craft) {
		writeRaw(0x20);// is_final_craft
		writeB(is_final_craft);
	}
	
	void write_all_server_cur_success_cnt(int all_server_cur_success_cnt) {
		writeRaw(0x28);// all_server_cur_success_cnt
		writeBit(all_server_cur_success_cnt);
	}
	
	public enum eCraftMakeReqResultType{
		RP_SUCCESS(0),										// 아이템 제작에 성공했습니다.
		RP_FAILURE(1),										// 아이템 제작에 실패했습니다.
		RP_ERROR_INVALID_INPUT(2),							// 재료 아이템이 잘못되었습니다.
		RP_ERROR_LACK_OF_ADENA(3),							// 아데나가 부족합니다.
		RP_ERROR_INVEN_OVER(4),								// 인벤토리 공간이 무족합니다.
		RP_ERROR_WEIGHT_OVER(5),							// 무게 게이지가 가득 찼습니다.
		RP_ERROR_INVALID_NPC(6),							// 잘못된 NPC의 제작 요청입니다.
		RP_ERROR_BAD_QUALIFICATION(7),						// 유저의 스탯이 부족합니다.
		RP_ERROR_CRAFT_DOES_NOT_EXIST(8),					// 잘못된 제작 요청입니다.
		RP_ERROR_NO_REQUIRED_ITEM(9),						// 제작 요구 아이템이 부족합니다.
		RP_ERROR_TOO_MANY_MAKE_REQ(10),						// 너무 많은 제작 요청으로 인해 제작이 실패했습니다.
		RP_ERROR_INPUT_ITEM_NOT_EXIST(11),					// 제작 재료 아이템이 부족합니다.
		RP_ERROR_OPTION_ITEM_NOT_EXIST(12),					// 제작 재료 옵션 아이템이 부족합니다.
		RP_ERROR_NPC_OUT_OF_RANGE(13),						// 제작 NPC가 범위 밖에 있습니다.
		RP_ERROR_BLOCKED_CRAFT_ID(14),						// 해당 제작은 서비스가 일시적으로 중단되었습니다.
		RP_ERROR_REJECTED_REQUIRED_ITEM(15),				// 제작에 실패했습니다.
		RP_ERROR_SUCCESS_COUNT_EXCEED(16),					// 제작 불가 : 준비된 수량 매진
		RP_ERROR_BATCH_CRAFT(17),							// 제작에 실패했습니다.
		RP_ERROR_INVALID_PERIOD(18),						// 제작에 
		RP_ERROR_ITEM_COUNT_LESS_THAN_REQUEST_COUNT(19),	// 요청한 수량보다 작습니다.
		RP_ERROR_UNKNOWN(9999),								// 제작에 실패했습니다.
		;
		private int value;
		eCraftMakeReqResultType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eCraftMakeReqResultType v){
			return value == v.value;
		}
		public static eCraftMakeReqResultType fromInt(int i){
			switch(i){
			case 0:
				return RP_SUCCESS;
			case 1:
				return RP_FAILURE;
			case 2:
				return RP_ERROR_INVALID_INPUT;
			case 3:
				return RP_ERROR_LACK_OF_ADENA;
			case 4:
				return RP_ERROR_INVEN_OVER;
			case 5:
				return RP_ERROR_WEIGHT_OVER;
			case 6:
				return RP_ERROR_INVALID_NPC;
			case 7:
				return RP_ERROR_BAD_QUALIFICATION;
			case 8:
				return RP_ERROR_CRAFT_DOES_NOT_EXIST;
			case 9:
				return RP_ERROR_NO_REQUIRED_ITEM;
			case 10:
				return RP_ERROR_TOO_MANY_MAKE_REQ;
			case 11:
				return RP_ERROR_INPUT_ITEM_NOT_EXIST;
			case 12:
				return RP_ERROR_OPTION_ITEM_NOT_EXIST;
			case 13:
				return RP_ERROR_NPC_OUT_OF_RANGE;
			case 14:
				return RP_ERROR_BLOCKED_CRAFT_ID;
			case 15:
				return RP_ERROR_REJECTED_REQUIRED_ITEM;
			case 16:
				return RP_ERROR_SUCCESS_COUNT_EXCEED;
			case 17:
				return RP_ERROR_BATCH_CRAFT;
			case 18:
				return RP_ERROR_INVALID_PERIOD;
			case 19:
				return RP_ERROR_ITEM_COUNT_LESS_THAN_REQUEST_COUNT;
			case 9999:
				return RP_ERROR_UNKNOWN;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eCraftMakeReqResultType, %d", i));
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
		return S_CRAFT_MAKE_RESULT;
	}
}

