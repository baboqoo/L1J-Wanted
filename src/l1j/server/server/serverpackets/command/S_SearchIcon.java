package l1j.server.server.serverpackets.command;

import java.io.IOException;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.serverpackets.warehouse.S_WareHouseItemListNoti;
import l1j.server.server.utils.BinaryOutputStream;

public class S_SearchIcon extends ServerBasePacket {
	private static final String S_SEARCH_ICON = "[S] S_SearchIcon";
	
	// 인벤아이콘 보기용
	public S_SearchIcon(L1PcInstance pc, int start, int count) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(S_WareHouseItemListNoti.NOTI);
				
		writeC(0x08);
		writeBit(pc.getId());
		
		writeC(0x10);
		writeBit(count);
		
		writeC(0x18);
		writeC(3);
		
		writeC(0x20);
		writeC(1);
				
		L1ItemInstance dummy = ItemTable.getInstance().createItem(40005);
		for (int i = 0; i < count; i++) {
			dummy.getItem().setIconId(start + i);
			dummy.getItem().setDescKr(String.valueOf(start + i));
			dummy.getItem().setDesc(String.valueOf(start + i));
			dummy.setCount(start + i);
			
			writeC(0x32);
			writeBytesWithLength(itemDetailBytes(pc, i, dummy));
		}
		dummy = null;
		
		writeC(0x38); 
		writeB(true);
			
		writeH(0x00);
	}
	
	private byte[] itemDetailBytes(L1PcInstance pc, int index, L1ItemInstance item) {
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
	public byte[] getContent() throws IOException {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_SEARCH_ICON;
	}

}

