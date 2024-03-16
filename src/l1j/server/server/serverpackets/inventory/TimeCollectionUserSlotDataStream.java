package l1j.server.server.serverpackets.inventory;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.BinaryOutputStream;

public class TimeCollectionUserSlotDataStream extends BinaryOutputStream {
	
	protected TimeCollectionUserSlotDataStream(L1PcInstance pc, int slotNo, L1ItemInstance item) {
		super();
		write_slotNo(slotNo);
		write_nameId(item.getItem().getItemNameId());
		write_enchant(item.getEnchantLevel());
		write_bless(item.getBless());
		write_extra_desc(item.getStatusBytes(pc));
	}
	
	void write_slotNo(int slotNo) {
		writeC(0x08);// slotNo
		writeC(slotNo);
	}
	
	void write_nameId(int nameId) {
		writeC(0x10);// nameId
		writeBit(nameId);
	}
	
	void write_enchant(int enchant) {
		writeC(0x18);// enchant
		writeBit(enchant);
	}
	
	void write_bless(int bless) {
		writeC(0x20);// bless
		writeC(bless);
	}
	
	void write_extra_desc(byte[] extra_desc) {
		writeC(0x2a);// extra_desc
		writeBytesWithLength(extra_desc);
	}
	
}

