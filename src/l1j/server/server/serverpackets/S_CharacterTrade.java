package l1j.server.server.serverpackets;

import java.io.IOException;
import java.util.ArrayDeque;

import l1j.server.GameSystem.charactertrade.bean.CharInfo;
import l1j.server.GameSystem.charactertrade.bean.InvenInfo;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.warehouse.S_WareHouseItemListNoti;
import l1j.server.server.templates.CharacterSkillInfo;
import l1j.server.server.utils.BinaryOutputStream;

public class S_CharacterTrade extends ServerBasePacket{

	public static S_CharacterTrade getCharacterInfo(CharInfo info){
		S_CharacterTrade pck = new S_CharacterTrade();
		pck.writeC(Opcodes.S_HYPERTEXT);
		pck.writeD(0);
		pck.writeS("deposit");
		pck.writeH(0x02);
		pck.writeH(0x02);
		pck.writeS(info.name);
		pck.writeS(info.toString());
		return pck;
	}
	
	public static S_CharacterTrade getSpellList(ArrayDeque<CharacterSkillInfo> spQ){
		S_CharacterTrade pck = new S_CharacterTrade();
		pck.writeC(Opcodes.S_EXTENDED_PROTOBUF);
		pck.writeH(S_WareHouseItemListNoti.NOTI);
		
		if (spQ == null) {
			pck.writeC(0x08);
			pck.writeC(0);
			
			pck.writeC(0x10);
			pck.writeC(0);
			
			pck.writeH(0x00);
			return pck;
		}
				
		pck.writeC(0x08);
		pck.writeBit(180);
		
		pck.writeC(0x10);
		pck.writeBit(spQ.size());
		
		pck.writeC(0x18);
		pck.writeC(3);
		
		pck.writeC(0x20);
		pck.writeC(1);
		
		int cnt = 0;
		L1ItemInstance dummy = ItemTable.getInstance().createItem(40005);
		L1PcInstance pc = new L1PcInstance();
		pc.setType(0);
		while(!spQ.isEmpty()){
			CharacterSkillInfo spell = spQ.poll();
			dummy.getItem().setIconId(spell.getIcon());
			dummy.getItem().setDescKr(spell.getName());
			dummy.getItem().setDesc(spell.getName());
			
			pck.writeC(0x32);
			pck.writeBytesWithLength(itemDetailBytes(pc, cnt, dummy));
		    spell = null;
		    cnt++;
	    }
		dummy = null;
		
		pck.writeC(0x38); 
		pck.writeB(true);
			
		pck.writeH(0x00);
		return pck;
	}
	
	public static S_CharacterTrade getInvList(ArrayDeque<InvenInfo> itemQ){
		S_CharacterTrade pck = new S_CharacterTrade();
		pck.writeC(Opcodes.S_EXTENDED_PROTOBUF);
		pck.writeH(S_WareHouseItemListNoti.NOTI);
		
		if (itemQ == null) {
			pck.writeC(0x08);
			pck.writeC(0);
			
			pck.writeC(0x10);
			pck.writeC(0);
			
			pck.writeH(0x00);
			return pck;
		}
				
		pck.writeC(0x08);
		pck.writeBit(180);
		
		pck.writeC(0x10);
		pck.writeBit(itemQ.size());
		
		pck.writeC(0x18);
		pck.writeC(3);
		
		pck.writeC(0x20);
		pck.writeC(1);
		
		int cnt = 0;
		L1ItemInstance dummy = ItemTable.getInstance().createItem(40005);
		L1PcInstance pc = new L1PcInstance();
		pc.setType(0);
		while(!itemQ.isEmpty()) {
			InvenInfo inven = itemQ.poll();
			if (inven != null) {
				if (inven.item != null) { 
					dummy.setId(inven.id);
					dummy.getItem().setIconId(inven.item.getIconId());
					dummy.getItem().setDescKr(inven.item.getDescKr());
					//dummy.getItem().setDesc(inven.item.getDescKr());
					dummy.getItem().setDesc(inven.item.getDesc());
					dummy.setEnchantLevel(inven.enchant);
					dummy.setAttrEnchantLevel(inven.attr);
					dummy.setIdentified(inven.iden == 1 ? true : false);
					dummy.setCount(inven.count);
					dummy.setBless(inven.bless);
					dummy.updateItemAbility(pc);
					pck.writeC(0x32);
					pck.writeBytesWithLength(itemDetailBytes(pc, cnt, dummy));
				}
				inven = null;
			}
		    cnt++;
	    }
		dummy = null;
		
		pck.writeC(0x38); 
		pck.writeB(true);
			
		pck.writeH(0x00);
		return pck;
	}
	
	private static byte[] itemDetailBytes(L1PcInstance pc, int index, L1ItemInstance item) {
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
}

