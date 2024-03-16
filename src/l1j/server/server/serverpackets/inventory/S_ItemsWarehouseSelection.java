package l1j.server.server.serverpackets.inventory;

import java.io.IOException;

import javolution.util.FastTable;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ItemSelectorTable.SelectorWarehouseData;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.serverpackets.warehouse.S_WareHouseItemListNoti;
import l1j.server.server.utils.BinaryOutputStream;

public class S_ItemsWarehouseSelection extends ServerBasePacket {
	private static final String S_ITEMS_WAREHOUSE_SELECTION = "[S] S_ItemsWarehouseSelection";
	private byte[] _byte = null;
	
	public S_ItemsWarehouseSelection(L1PcInstance pc, int useItemId, FastTable<SelectorWarehouseData> list){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(S_WareHouseItemListNoti.NOTI);
		
		writeRaw(0x08);// objid
		writeBit(useItemId);
		
		writeRaw(0x10);// size
		writeBit(list.size());
		
		writeRaw(0x18);// type
		writeRaw(3);
		
		writeRaw(0x20);// price
		writeRaw(1);

		writeRaw(0x28);
		writeBit(180);

		for (SelectorWarehouseData data : list) {
			if (data == null || data._item == null) {
				System.out.println(String.format("[S_ItemsWarehouseSelection] DATA_ITEM_NOT_FOUND : ITEMID(%d)", data._itemId));
				continue;
			}
			writeRaw(0x32);
			writeBytesWithLength(itemInfoBytes(pc, data._index, data._item));
		}
		
		writeRaw(0x38);
		writeB(true);
		
		writeH(0x00);
	}
	
	byte[] itemInfoBytes(L1PcInstance pc, int index, L1ItemInstance item) {
		BinaryOutputStream os = new BinaryOutputStream();
		os.writeC(0x08);
		os.writeBit(index);
		
		os.writeC(0x12);
		os.writeBytesWithLength(item.getItemInfo(pc));
		try {
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return os.getBytes();
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
		return S_ITEMS_WAREHOUSE_SELECTION;
	}
}

