package l1j.server.server.serverpackets.inventory;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_InventoryEffectNoti extends ServerBasePacket {
	private static final String S_INVENTORY_EFFECT_NOTI = "[S] S_InventoryEffectNoti";
	private byte[] _byte = null;

	public S_InventoryEffectNoti(int code, int item_id, S_InventoryEffectNoti.eEffectCodeType effect_code) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(code);
		write_item_id(item_id);
		write_effect_code(effect_code);
		writeH(0x00);
	}
	
	void write_item_id(int item_id) {
		writeRaw(0x08);
		writeBit(item_id);
	}
	
	void write_effect_code(S_InventoryEffectNoti.eEffectCodeType effect_code) {
		writeRaw(0x10);// effect_code
		writeRaw(effect_code.value);
	}
	
	public enum eEffectCodeType{
		Enchant_Elemental_Fire(1),
		Enchant_Elemental_Water(2),
		Enchant_Elemental_Air(3),
		Enchant_Elemental_Earth(4),
		Gear_Enchant(5),
		Life(6),
		;
		private int value;
		eEffectCodeType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eEffectCodeType v){
			return value == v.value;
		}
		public static eEffectCodeType fromInt(int i){
			switch(i){
			case 1:
				return Enchant_Elemental_Fire;
			case 2:
				return Enchant_Elemental_Water;
			case 3:
				return Enchant_Elemental_Air;
			case 4:
				return Enchant_Elemental_Earth;
			case 5:
				return Gear_Enchant;
			case 6:
				return Life;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eEffectCodeType, %d", i));
			}
		}
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}
	
	@Override
	public String getType() {
		return S_INVENTORY_EFFECT_NOTI;
	}
}

