package l1j.server.server.serverpackets.inventory;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_InventoryYetiAlarmNoti extends ServerBasePacket {
	private static final String S_INVENTORY_YETI_ALARM_NOTI = "[S] S_InventoryYetiAlarmNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x0256;
	
	public S_InventoryYetiAlarmNoti(S_InventoryYetiAlarmNoti.eType type, int name_id, int amount){
		write_init();
		write_type(type);
		write_item(name_id, amount);
        writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_type(S_InventoryYetiAlarmNoti.eType type) {
		writeRaw(0x08);
		writeRaw(type.value);
	}
	
	void write_item(int name_id, int amount) {
		writeRaw(0x12);
		writeRaw(getBitSize(name_id) + getBitSize(amount) + 2);
		
		writeRaw(0x08);// nameId
		writeBit(name_id);
		
		writeRaw(0x10);// amount
		writeBit(amount);
	}
	
	public enum eType{
		Get(1),
		Lost(2),
		;
		private int value;
		eType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eType v){
			return value == v.value;
		}
		public static eType fromInt(int i){
			switch(i){
			case 1:
				return Get;
			case 2:
				return Lost;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eType, %d", i));
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
		return S_INVENTORY_YETI_ALARM_NOTI;
	}
}

