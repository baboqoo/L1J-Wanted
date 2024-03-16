package l1j.server.server.serverpackets.warehouse;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.BinaryOutputStream;

public class S_WareHouseInfoNoti extends ServerBasePacket {
	private static final String S_WAREHOUSE_INFO_NOTI = "[S] S_WareHouseInfoNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x0065;

	public S_WareHouseInfoNoti(int warehouseType, int ackType, java.util.LinkedList<S_WareHouseInfoNoti.Item> iteminfo) {
		write_init();
		write_warehouseType(warehouseType);
		write_ackType(ackType);
		if (iteminfo != null && !iteminfo.isEmpty()) {
			write_iteminfo(iteminfo);
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_warehouseType(int warehouseType) {
		writeRaw(0x08);// warehouseType
		writeBit(warehouseType);
	}
	
	void write_ackType(int ackType) {
		writeRaw(0x10);// ackType
		writeBit(ackType);
	}
	
	void write_iteminfo(java.util.LinkedList<S_WareHouseInfoNoti.Item> iteminfo) {
		for (S_WareHouseInfoNoti.Item val : iteminfo) {
			writeRaw(0x1a);// iteminfo
			writeBytesWithLength(val.getBytes());
		}
	}
	
	public static class Item extends BinaryOutputStream {
		public Item(int nameid, int count, int icon, int index, String desc) {
			super();
			write_nameid(nameid);
			write_count(count);
			write_icon(icon);
			write_index(index);
			write_desc(desc);
		}
		
		void write_nameid(int nameid) {
			writeC(0x08);
			writeBit(nameid);
		}
		
		void write_count(int count) {
			writeC(0x10);
			writeBit(count);
		}
		
		void write_icon(int icon) {
			writeC(0x18);
			writeBit(icon);
		}
		
		void write_index(int index) {
			writeC(0x20);
			writeBit(index);
		}
		
		void write_desc(String desc) {
			writeC(0x2a);
			writeStringWithLength(desc);
		}
	}
	
	public enum eAckType{
		RP_REFRESH(0),
		RP_ADD(1),
		;
		private int value;
		eAckType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eAckType v){
			return value == v.value;
		}
		public static eAckType fromInt(int i){
			switch(i){
			case 0:
				return RP_REFRESH;
			case 1:
				return RP_ADD;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eAckType, %d", i));
			}
		}
	}

	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}
	
	public String getType() {
		return S_WAREHOUSE_INFO_NOTI;
	}
}

