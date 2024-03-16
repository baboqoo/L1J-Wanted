package l1j.server.server.serverpackets.attendance;

import l1j.server.common.data.AttendanceBonusType;
import l1j.server.server.utils.BinaryOutputStream;

public class AttendanceBonusInfo extends BinaryOutputStream {
	public AttendanceBonusInfo() {
		super();
	}
	
	void write_bonusType(AttendanceBonusType bonusType) {
		writeC(0x08);// bonusType
		writeC(bonusType.toInt());
	}
	
	void write_itemId(int itemId) {
		writeC(0x10);// itemId
		writeBit(itemId);
	}
	
	void write_itemCount(int itemCount) {
		writeC(0x18);// itemCount
		writeBit(itemCount);
	}
	
	void write_itemName(String itemName) {
		writeC(0x22);// itemName
		writeStringWithLength(itemName);
	}
	
	void write_itemInteractType(int itemInteractType) {
		writeC(0x28);// itemInteractType
		writeBit(itemInteractType);
	}
	
	void write_itemIcon(int itemIcon) {
		writeC(0x30);// itemIcon
		writeBit(itemIcon);
	}
	
	void write_itemBless(int itemBless) {
		writeC(0x38);// itemBless
		writeC(itemBless);
	}
	
	void write_itemDesc(String itemDesc) {
		writeC(0x42);// itemDesc
		writeStringWithLength(itemDesc);
	}
	
	void write_itemExtraDesc(byte[] itemExtraDesc) {
		writeC(0x4a);// itemExtraDesc
		writeBytesWithLength(itemExtraDesc);
	}
	
	void write_itemAttr(int itemAttr) {
		writeC(0x50);// itemAttr
		writeBit(itemAttr);
	}
}

