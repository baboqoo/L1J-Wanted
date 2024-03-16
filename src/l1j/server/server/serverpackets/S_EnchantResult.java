package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.construct.item.L1ItemArmorType;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.datatables.EnchantResultTable;
import l1j.server.server.datatables.EnchantResultTable.EnchantResultData;
import l1j.server.server.model.Instance.L1ItemInstance;

public class S_EnchantResult extends ServerBasePacket {
	private static final String S_ENCHANT_RESULT = "[S] S_EnchantResult";
	private byte[] _byte = null;
	public static final int RESULT = 0x025e;
	
	public S_EnchantResult(L1ItemInstance item, L1ItemInstance scroll, S_EnchantResult.EnchantResult enchant_result, int enchant_amount, int element_enchant) {
		EnchantResultData itemData		= EnchantResultTable.getData(item.getItemId());
		EnchantResultData scrollData	= EnchantResultTable.getData(scroll.getItemId());
		boolean color_item				= itemData == null ? false : itemData.is_color_item();
		boolean bm_scroll				= scrollData == null ? false : scrollData.is_bm_scroll();
		boolean acessary 				= item.getItem().getItemType() == L1ItemType.ARMOR && L1ItemArmorType.isAccessary(item.getItem().getType());
		int bless						= scroll.getBless();// 0:축복, 1:일반, 2:저주
		int enchantLevel				= item.getEnchantLevel();
		boolean isOverSafety			= enchantLevel > item.getItem().getSafeEnchant();
		write_init();
		write_object_id(item.getId());
		write_bless(bless);
		write_enchant_result(enchant_result);
		write_bm_scroll(bm_scroll ? 1 : 0);
		write_enchant_amount(enchant_amount);
		if (bless != 2) {
			if ((isOverSafety && color_item) || (enchantLevel >= 5 && bm_scroll)) {
				write_special_enchant_effect(0);
			} else if (enchantLevel < 5 && bm_scroll) {
				write_special_enchant_effect(5);
			}
			if (element_enchant == 0) {
				write_high_enchant((enchantLevel >= 5 && bm_scroll) || (enchantLevel >= 6 && acessary) || (!acessary && !bm_scroll && isOverSafety));
			}
		}
		if (element_enchant > 0) {
			write_element_enchant(element_enchant);
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(RESULT);
	}
	
	void write_object_id(int object_id) {
		writeRaw(0x08);// object_id
		writeBit(object_id);
	}
	
	void write_bless(int bless) {
		writeRaw(0x10);// bless
		writeRaw(bless);
	}
	
	void write_enchant_result(EnchantResult enchant_result) {
		writeRaw(0x18);// enchant_result
		writeRaw(enchant_result.value);
	}
	
	void write_bm_scroll(int bm_scroll) {
		writeRaw(0x20);// bm_scroll 색깔	0.노랑, 1.보라색
		writeRaw(bm_scroll);
	}
	
	void write_enchant_amount(int enchant_amount) {
		writeRaw(0x28);// enchant_amount
		writeBit(enchant_amount);
	}
	
	void write_special_enchant_effect(int special_enchant_effect) {
		writeRaw(0x30);// special_enchant_effect 이팩트
		writeRaw(special_enchant_effect);
	}
	
	void write_high_enchant(boolean high_enchant) {
		writeRaw(0x38);// high_enchant 아이템이미지 0.없음, 1.있음
		writeB(high_enchant);
	}
	
	void write_element_enchant(int element_enchant) {
		writeRaw(0x40);// element_enchant 1:화령속성, 2:수령송성, 3:풍령속성, 4:지령속성
		writeRaw(element_enchant);
	}
	
	void write_enchantDestroyProof(int enchantDestroyProof) {
		writeRaw(0x48);// enchantDestroyProof
		writeBit(enchantDestroyProof);
	}
	
	void write_material(int material) {
		writeRaw(0x50);// material
		writeBit(material);
	}
	
	public enum EnchantResult{
		SUCCESS(0),		// 성공
		FAIL_DESTROY(1),// 실패
		FAIL_REMAIN(2),	// 보호
		;
		private int value;
		EnchantResult(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(EnchantResult v){
			return value == v.value;
		}
		public static EnchantResult fromInt(int i){
			switch(i){
			case 0:
				return SUCCESS;
			case 1:
				return FAIL_DESTROY;
			case 2:
				return FAIL_REMAIN;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eResult, %d", i));
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
		return S_ENCHANT_RESULT;
	}
}

