package l1j.server.server.serverpackets.trade;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_ExchangeItemListNoti extends ServerBasePacket {
	private static final String S_EXCHANGE_ITEM_LIST_NOTI = "[S] S_ExchangeItemListNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x0409;

	// 아이템 교환
	public S_ExchangeItemListNoti(L1ItemInstance item, int count, S_ExchangeItemListNoti.eExchangeType type, L1PcInstance pc) {
		write_init();
		write_type(type);
		write_add_item_list(item.getItemInfo(pc, count));
		writeH(0x00);
	}
	
	// 케릭터 교환
	public S_ExchangeItemListNoti(L1ItemInstance item, String name, int count, S_ExchangeItemListNoti.eExchangeType type, L1PcInstance pc) {
		write_init();
		write_type(type);
		write_add_item_list(item.getItemInfo(pc, item.getItemId(), count, name));
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_type(S_ExchangeItemListNoti.eExchangeType type) {
		writeRaw(0x08);
		writeRaw(type.value);
	}
	
	void write_add_item_list(byte[] add_item_list) {
		writeRaw(0x12);
		writeBytesWithLength(add_item_list);
	}
	
	public enum eExchangeType{
		EXC_SELF(0),	// 트레이드 윈도우 상단
		EXC_OPPOSITE(1),// 트레이드 윈도우 하단
		;
		private int value;
		eExchangeType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eExchangeType v){
			return value == v.value;
		}
		public static eExchangeType fromInt(int i){
			switch(i){
			case 0:
				return EXC_SELF;
			case 1:
				return EXC_OPPOSITE;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eExchangeType, %d", i));
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
		return S_EXCHANGE_ITEM_LIST_NOTI;
	}
}

