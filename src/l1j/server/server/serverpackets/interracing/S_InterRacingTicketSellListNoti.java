package l1j.server.server.serverpackets.interracing;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_InterRacingTicketSellListNoti extends ServerBasePacket {
	private static final String S_INTER_RACING_TICKET_SELL_LIST_NOTI = "[S] S_InterRacingTicketSellListNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x0904;
	
	public S_InterRacingTicketSellListNoti(int npcId, int currency, 
			java.util.LinkedList<S_InterRacingTicketSellListNoti.SellItemT> items){
		write_init();
		write_npcId(npcId);
		write_currency(currency);
		write_items(items);
        writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_npcId(int npcId) {
		writeRaw(0x08);
		writeBit(npcId);
	}
	
	void write_currency(int currency) {
		writeRaw(0x10);
		writeBit(currency);
	}
	
	void write_items(java.util.LinkedList<S_InterRacingTicketSellListNoti.SellItemT> items) {
		if (items == null || items.isEmpty()) {
			return;
		}
		for (S_InterRacingTicketSellListNoti.SellItemT item : items) {
			writeRaw(0x1a);
			writeRaw(getBitSize(item._itemId) + getBitSize(item._price) + 2);
			
			writeRaw(0x08);
			writeBit(item._itemId);
			
			writeRaw(0x10);
			writeBit(item._price);
		}
	}
	
	public static class SellItemT {
		private int _itemId;
		private int _price;
		
		public SellItemT(int itemId, int price) {
			this._itemId = itemId;
			this._price = price;
		}

		public int get_itemId() {
			return _itemId;
		}

		public int get_price() {
			return _price;
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
		return S_INTER_RACING_TICKET_SELL_LIST_NOTI;
	}
}

