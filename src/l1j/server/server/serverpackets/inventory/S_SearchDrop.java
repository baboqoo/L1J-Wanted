package l1j.server.server.serverpackets.inventory;

import java.util.ArrayList;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.serverpackets.warehouse.S_WareHouseItemListNoti;
import l1j.server.server.templates.L1Drop;
import l1j.server.server.templates.L1Item;

public class S_SearchDrop extends ServerBasePacket {
	private static final String S_SEARCH_DROP = "[S] S_SearchDrop";
	private byte[] _byte = null;
	
	public static S_SearchDrop getDropList(L1PcInstance pc, ArrayList<L1Drop> dropList){
		S_SearchDrop pck = new S_SearchDrop();
		pck.writeC(Opcodes.S_EXTENDED_PROTOBUF);
		pck.writeH(S_WareHouseItemListNoti.NOTI);
		
		pck.writeRaw(0x08);
		pck.writeBit(180);
		
		pck.writeRaw(0x10);
		pck.writeBit(dropList.size());
		
		pck.writeRaw(0x18);
		pck.writeRaw(3);
		
		pck.writeRaw(0x20);
		pck.writeRaw(1);

		int cnt = 0;
		ItemTable temp = ItemTable.getInstance();
		L1Item template = null;
		L1ItemInstance dummy = new L1ItemInstance();
		dummy.setIdentified(true);
		for (L1Drop drop : dropList) {
			template = temp.getTemplate(drop.getItemid());
			if (template == null) {
				continue;
			}
			dummy.setItem(template);
			dummy.setEnchantLevel(drop.getEnchant());
			dummy.setBless(template.getBless());
			dummy.updateItemAbility(pc);
			byte[] itemBytes = dummy.getItemInfo(pc);
			int length = itemBytes.length + pck.getBitSize(itemBytes.length) + pck.getBitSize(cnt) + 2;
			
			pck.writeRaw(0x32);
			pck.writeBit(length);
			
			pck.writeRaw(0x08);
			pck.writeBit(cnt);
			
			pck.writeRaw(0x12);
			pck.writeBytesWithLength(itemBytes);
			cnt++;
		}
		dummy = null;
		
		pck.writeRaw(0x38);
		pck.writeB(true);
		
		pck.writeH(0x00);
		return pck;
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_SEARCH_DROP;
	}
}

